/*
 * ─────────────────────────────────────────────────────────────────────────────
 * WaveManager.java  —  ENEMY WAVE SPAWNING AND PROGRESSION SYSTEM
 * ─────────────────────────────────────────────────────────────────────────────
 * Role:
 *   Controls when enemies appear, in which lanes, in what order, and how
 *   difficult each wave gets.  It also manages the calm "break" period between
 *   waves so the player has time to spend currency and place new units.
 *
 * Wave lifecycle:
 *   startFirstWave()
 *     └→ buildWave(1)  fills spawnQueue
 *     └→ spawnTimer starts ticking
 *        Each tick: pop one EnemySpawn from queue → addObject(enemy, rightEdge, laneY)
 *     └→ when queue is empty AND all enemies are dead → triggerWaveBreak()
 *        waveBreakTimer counts down (player can place units)
 *     └→ timer expires → buildWave(2) → repeat, scaling difficulty
 *
 * Adding new enemy types:
 *   1. Create your enemy class (e.g. ShieldEnemy.java) extending Enemy.
 *   2. Add a constant in EnemySpawn: public static final int SHIELD = 3;
 *   3. Add a case in EnemySpawn.create(): case SHIELD: return new ShieldEnemy();
 *   4. Reference it in buildWave() at whatever wave threshold you like.
 *
 * Uses GameRNG (not Greenfoot.getRandomNumber) so lane selection is
 * deterministic and rewind-safe.
 *
 * Interacts with:
 *   PlayingState (owns this), LaneManager (spawn Y),
 *   CurrencyManager (wave-clear bonus), GameRNG, GameTimer,
 *   Enemy and its subclasses, HUD (reads getWaveNumber(), isBreakTime())
 * ─────────────────────────────────────────────────────────────────────────────
 */
import greenfoot.*;
import java.util.LinkedList;
import java.util.Queue;

public class WaveManager {

    // ─────────────────────────────────────────────────────────────────────────
    // CONFIGURATION
    // ─────────────────────────────────────────────────────────────────────────

    /** Seconds of break time the player gets between waves. */
    private static final double WAVE_BREAK_TIME  = GameConfig.WAVE_BREAK_TIME;

    /** Gold awarded to the player each time a wave is cleared. */
    private static final int WAVE_CLEAR_BONUS    = GameConfig.WAVE_CLEAR_BONUS;


    // ─────────────────────────────────────────────────────────────────────────
    // STATE
    // ─────────────────────────────────────────────────────────────────────────

    /** Which wave the player is currently on (1-indexed). */
    private int currentWave = 0;

    /** Enemies queued to be spawned for the current wave. */
    private Queue<EnemySpawn> spawnQueue = new LinkedList<>();

    /** Fires each time it's time to release the next enemy from the queue. */
    private GameTimer spawnTimer = new GameTimer(GameConfig.SPAWN_INTERVAL_BASE, true);

    /** Counts down the break period between waves. One-shot (loop = false). */
    private GameTimer waveBreakTimer = new GameTimer(WAVE_BREAK_TIME, false);

    /** True while the inter-wave break countdown is running. */
    private boolean waitingForBreak = false;

    /** True while there are still enemies to spawn OR alive in the world. */
    private boolean waveInProgress  = false;

    /**Warn user about slime*/
    private boolean slimeDiscovered = false;
    
    private static int forcedEnemyType = -1;
    // ─────────────────────────────────────────────────────────────────────────
    // LIFECYCLE
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Kicks off wave 1.  Call once from PlayingState.enter() after all
     * other systems have been initialised.
     */
    public void startFirstWave() {
        currentWave = 0;
        startNextWave();
    }

    /** Internal: increments wave counter, builds the queue, starts spawning. */
    private void startNextWave() {
        currentWave++;
        spawnQueue.clear();
        buildWave(currentWave);

        waveInProgress  = true;
        waitingForBreak = false;

        // Uses Config values to calculate the new speed
        double newInterval = Math.max(
            GameConfig.MIN_SPAWN_INTERVAL, 
            GameConfig.SPAWN_INTERVAL_BASE - (currentWave * GameConfig.DIFF_PACE_SPEEDUP)
        );
        
        spawnTimer.setDuration(newInterval);
        spawnTimer.start();
    }

    /**
     * Fills spawnQueue with the enemy roster for the given wave number.
     * Edit this method freely to design your wave curves and difficulty ramps.
     *
     * @param waveNum  The wave being built (starts at 1).
     */
    private void buildWave(int waveNum) {
        int enemyCount = 3 + (int)(waveNum * GameConfig.DIFF_QUANTITY_GROWTH);

        for (int i = 0; i < enemyCount; i++) {
            int lane = GameRNG.getRandomNumber(GameConfig.NUM_LANES);
            //Forced Wave
            if (forcedEnemyType != -1) {
                if (forcedEnemyType == EnemySpawn.RUMBLING_MIX) {
                    // The Evil Combo: Roll between Kamikaze and Shield
                    int type = (GameRNG.getRandomNumber(100) < GameConfig.RUMBLING_BOMBER_CHANCE) 
                                ? EnemySpawn.KAMIKAZE 
                                : EnemySpawn.SHIELD;
                    spawnQueue.add(new EnemySpawn(type, lane));
                } else {
                    // Normal forced wave (e.g. 100% Kamikaze for Stampede)
                    spawnQueue.add(new EnemySpawn(forcedEnemyType, lane));
                }
                continue; // Skip all the random rolls below!
            }
            int roll = GameRNG.getRandomNumber(100);

            // Calculate current chances
            int tankChance    = (waveNum >= GameConfig.TANK_WAVE_MIN)    ? Math.min(GameConfig.TANK_CHANCE_MAX, GameConfig.TANK_CHANCE_START + (waveNum * GameConfig.TANK_CHANCE_GROWTH)) : -1;
            int slimeChance   = (waveNum >= GameConfig.SLIME_WAVE_MIN)   ? Math.min(GameConfig.SLIME_CHANCE_MAX, GameConfig.SLIME_CHANCE_START + (waveNum * GameConfig.SLIME_CHANCE_GROWTH)) : -1;
            int shieldChance  = (waveNum >= GameConfig.SHIELD_WAVE_MIN)  ? Math.min(GameConfig.SHIELD_CHANCE_MAX, GameConfig.SHIELD_CHANCE_START + (waveNum * GameConfig.SHIELD_CHANCE_GROWTH)) : -1;
            int KAMIKAZEChance = (waveNum >= GameConfig.KAMIKAZE_WAVE_MIN) ? Math.min(GameConfig.KAMIKAZE_CHANCE_MAX, GameConfig.KAMIKAZE_CHANCE_START + (waveNum * GameConfig.KAMIKAZE_CHANCE_GROWTH)) : -1;

            // ONE CHAIN TO RULE THEM ALL
            // If you set SLIME_CHANCE_START to 100, the first 'if' will always trigger,
            // and the 'else if' blocks below it will be skipped.
            if (slimeChance > 0 && roll < slimeChance) {
                spawnQueue.add(new EnemySpawn(EnemySpawn.SLIME, lane));
            } 
            else if (shieldChance > 0 && roll < shieldChance) {
                spawnQueue.add(new EnemySpawn(EnemySpawn.SHIELD, lane));
            } 
            else if (KAMIKAZEChance > 0 && roll < KAMIKAZEChance) {
                spawnQueue.add(new EnemySpawn(EnemySpawn.KAMIKAZE, lane));
            } 
            else if (tankChance > 0 && roll < tankChance) {
                spawnQueue.add(new EnemySpawn(EnemySpawn.TANK, lane));
            } 
            else {
                // If nothing else rolls successfully, it's a Basic Enemy
                spawnQueue.add(new EnemySpawn(EnemySpawn.BASIC, lane));
            }
        }
        forcedEnemyType = -1; 
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PER-FRAME UPDATE — call this from PlayingState.update() every frame
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Ticks the spawn timer, releases queued enemies, detects wave clears,
     * and manages the inter-wave break countdown.
     *
     * @param world  Needed to add enemies and count live enemies.
     */
    public void update(MyWorld world) {

        // ── BREAK MODE: count down until next wave ────────────────────────────
        if (waitingForBreak) {
            waveBreakTimer.update(world);
            if (waveBreakTimer.isExpired()) {
                startNextWave();
            }
            return; // nothing else to do during break
        }

        // ── WAVE IN PROGRESS ──────────────────────────────────────────────────
        if (waveInProgress) {

            // Tick spawn timer; when it fires, release the next enemy
            spawnTimer.update(world);
            if (spawnTimer.isExpired() && !spawnQueue.isEmpty()) {
                spawnNext(world);
            }

            // Wave is cleared when: queue is empty AND no enemies remain alive
            boolean queueDone   = spawnQueue.isEmpty();
            boolean allDefeated = world.getObjects(Enemy.class).isEmpty();

            if (queueDone && allDefeated) {
                triggerWaveBreak(world);
            }
        }
    }

    /** Pops one spawn from the queue and adds the enemy actor to the world. */

    private void spawnNext(MyWorld world) {
        EnemySpawn spawn = spawnQueue.peek(); // Look at the next enemy
        if (spawn == null) return;

        // THE BOSS CINEMATIC TRIGGER
        if (spawn.type == EnemySpawn.SLIME && !slimeDiscovered) {
            slimeDiscovered = true;
            spawn.warningTriggered = true;
            
            // PUSH THE CINEMATIC STATE (This freezes the game for 3 seconds!)
            world.getGSM().pushState(new BossCinematicState("THE KING SLIME", 180));
            
            // Give the player 1 second of calm to place units AFTER the cinematic ends
            spawnTimer.setDuration(1.0); 
            spawnTimer.reset();
            return; 
        }
        
        // Normal spawn logic
        spawnQueue.poll(); // Actually remove from queue
        
        int spawnX = world.getWidth() + GameConfig.s(30); 
        int spawnY = LaneManager.getLaneY(spawn.lane);

        Enemy enemy = spawn.create(currentWave); 
        enemy.setLane(spawn.lane);
        
        if (enemy instanceof KamikazeEnemy && !spawnQueue.isEmpty()) { 
             // We can check a flag or just apply it if it's a high-volume wave
             ((KamikazeEnemy)enemy).setElite(GameConfig.KAMIKAZE_SWARM_SHIELD, 1.0, 1.0f);
        }
        
        world.addObject(enemy, spawnX, spawnY);
    }

    /** Switches to break mode, awards the wave-clear gold bonus. */
    private void triggerWaveBreak(MyWorld world) {
        waveInProgress  = false;
        waitingForBreak = true;
        waveBreakTimer.reset();
        waveBreakTimer.start();

        // 1. Base Clear Bonus
        CurrencyManager.earn(WAVE_CLEAR_BONUS + currentWave * 2);
        ScoreManager.addScore(currentWave*50); 

        // 2. INTEREST (The Greed Mechanic!)
        int interest = (int)(CurrencyManager.getGold() * GameConfig.INTEREST_RATE);
        if (interest > 0) {
            CurrencyManager.earn(interest);
            
            // Spawn HUGE text in the middle of the screen
            String msg = "WAVE CLEARED!\nInterest Earned: +$" + interest;
            world.addObject(new FloatingText(msg, Color.YELLOW, 180), world.getWidth() / 2, world.getHeight() / 2);
        }
    }
    
    public void replaceCurrentWave(int enemyType, double countMultiplier, double customSpawnInterval, boolean isElite) {
        this.currentWave++;           // Treat this as a brand new wave
        this.waveInProgress = true;   // Force the spawning state to ON
        this.waitingForBreak = false; // Instantly cancel the break timer if it was active
        spawnQueue.clear();
        int baseCount = 3 + (int)(currentWave * GameConfig.DIFF_QUANTITY_GROWTH);
        int finalCount = (int)(baseCount * countMultiplier);

        for (int i = 0; i < finalCount; i++) {
            int lane = GameRNG.getRandomNumber(GameConfig.NUM_LANES);
            spawnQueue.add(new EnemySpawn(enemyType, lane, isElite));
        }
        
        // Bypass the MIN_SPAWN_INTERVAL cap completely for Calamities!
        spawnTimer.setDuration(customSpawnInterval); 
        spawnTimer.reset();
    }
    
    public static void forceNextWaveType(int type) {
        forcedEnemyType = type;
    }
    
    private static int forcedPattern = 0; // 0 = Normal, 1 = Stampede, 2 = The Rumbling
    
    public static void forceNextWavePattern(int pattern) {
        forcedPattern = pattern;
    }
    // ─────────────────────────────────────────────────────────────────────────
    // QUERIES — read by HUD and PlayingState
    // ─────────────────────────────────────────────────────────────────────────

    /** @return The current wave number (1-indexed). */
    public int getWaveNumber() { return currentWave; }

    /** @return True while the inter-wave break timer is counting down. */
    public boolean isBreakTime() { return waitingForBreak; }

    /** @return Seconds left in the break period. */
    public double getBreakTimeRemaining() { return waveBreakTimer.getSecondsRemaining(); }

    
    // ─────────────────────────────────────────────────────────────────────────
    // INNER CLASS — enemy spawn descriptor
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Lightweight data object that describes one enemy to be spawned:
     * its type and which lane it enters from.
     *
     * Add new enemy type constants and cases in create() as you build them.
     */
    public static class EnemySpawn {
        public static final int BASIC = 0;
        public static final int FAST  = 1; 
        public static final int TANK  = 2; 
        public static final int SHIELD = 3;
        public static final int KAMIKAZE = 4;
        public static final int SLIME = 5;
        public static final int HEAVY_SHIELD = 6;
        public static final int RUMBLING_MIX = 99;

        public final int type;
        public final int lane;
        public final boolean elite; // NEW
        
        public boolean warningTriggered = false;

        public EnemySpawn(int type, int lane, boolean elite) {
            this.type = type;
            this.lane = lane;
            this.elite = elite;
        }
        
        // Backwards compatibility for normal waves
        public EnemySpawn(int type, int lane) {
            this(type, lane, false); 
        }
        
        public Enemy create(int waveNum) {
            Enemy e;
            switch (type) {
                case TANK:         e = new TankEnemy(); break;
                case SHIELD:       e = new ShieldBearerEnemy(); break;
                case KAMIKAZE:     e = new KamikazeEnemy(); break; 
                case SLIME:        e = new SlimeEnemy(); break;
                case HEAVY_SHIELD: e = new HeavyShieldEnemy(); break;
                default:           e = new BasicEnemy(); break; 
            }
            
            // --- TRUE TIERED SCALING FOR WAVE 100 ROADMAP ---
            double hpMult = 1.0;
            double dmgMult = 1.0;

            if (waveNum <= 20) {
                // TIER 1 (Waves 1-20): Gentle Linear. Lets Lvl 1 & 2 units shine.
                hpMult = 1.0 + (waveNum * 0.4); 
                dmgMult = 1.0 + (waveNum * 0.2);
            } 
            else if (waveNum <= 50) {
                // TIER 2 (Waves 21-50): Mild Exponential. Forces Lvl 3 upgrades.
                // Starts where Tier 1 left off (~9.0x) and grows gently.
                hpMult = 9.0 * Math.pow(1.08, waveNum - 20);
                dmgMult = 5.0 + (waveNum * 0.4);
            } 
            else if (waveNum <= 80) {
                // TIER 3 (Waves 51-80): Aggressive Exponential. Forces Lvl 4 Spike.
                // Starts where Tier 2 left off (~90x).
                hpMult = 90.0 * Math.pow(1.12, waveNum - 50);
                dmgMult = 15.0 + (waveNum * 1.0);
            } 
            else {
                // TIER 4 (Waves 81-100+): The Endgame. Demands Level 5 and Abilities.
                // Starts where Tier 3 left off (~2700x).
                hpMult = 2700.0 * Math.pow(1.18, waveNum - 80);
                dmgMult = 45.0 + (waveNum * 2.0);
            }

            // Slimes always get 2.5x more HP than whatever the current multiplier is
            if (type == SLIME) {
                hpMult *= 2.5; 
            }

            e.scaleStats((float)hpMult, (float)dmgMult, waveNum);
            
            // --- NEW: APPLY ELITE BUFFS ---
            if (elite) {
                if (e instanceof SlimeEnemy) {
                    ((SlimeEnemy)e).setElite(GameConfig.BLOODMOON_SHIELD, GameConfig.BLOODMOON_HP_MULT, GameConfig.BLOODMOON_SPEED_BOOST);
                } else if (e instanceof KamikazeEnemy) {
                    ((KamikazeEnemy)e).setElite(GameConfig.KAMIKAZE_SWARM_SHIELD, 1.0, 1.0f);
                }
            }
            
            return e;
        }
    }
    
    public void debugSkipToWave(int targetWave) {
        // Ensure we don't go below 1
        this.currentWave = Math.max(1, targetWave - 1); 
        
        // Wipe the current field so the new difficulty takes over instantly
        // (Wait for startNextWave to increment it back to the target)
        startNextWave(); 
        
        // Visual feedback
        System.out.println("DEBUG: Jumped to Wave " + currentWave);
    }
}