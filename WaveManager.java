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

    /** Seconds between individual enemy spawns within a wave.(to be changed!) */
    private static double SPAWN_INTERVAL   = 1.5;

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
    private GameTimer spawnTimer = new GameTimer(SPAWN_INTERVAL, true);

    /** Counts down the break period between waves. One-shot (loop = false). */
    private GameTimer waveBreakTimer = new GameTimer(WAVE_BREAK_TIME, false);

    /** True while the inter-wave break countdown is running. */
    private boolean waitingForBreak = false;

    /** True while there are still enemies to spawn OR alive in the world. */
    private boolean waveInProgress  = false;


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

        spawnTimer.reset();
        spawnTimer.start();
    }

    /**
     * Fills spawnQueue with the enemy roster for the given wave number.
     * Edit this method freely to design your wave curves and difficulty ramps.
     *
     * @param waveNum  The wave being built (starts at 1).
     */
    private void buildWave(int waveNum) {
        int enemyCount = 3 + waveNum * 2; // 5 on wave 1, 7 on wave 2, ...

        for (int i = 0; i < enemyCount; i++) {
            int lane = GameRNG.getRandomNumber(GameConfig.NUM_LANES);

            // Gradually introduce tougher enemies as waves progress
            int roll = GameRNG.getRandomNumber(10);

            if (waveNum >= 5 && roll == 0) {
                spawnQueue.add(new EnemySpawn(EnemySpawn.TANK, lane));
                spawnQueue.add(new EnemySpawn(EnemySpawn.BASIC, lane)); // placeholder
            } else if (waveNum >= 3 && roll <= 2) {
                spawnQueue.add(new EnemySpawn(EnemySpawn.BASIC, lane)); // placeholder
            } else {
                spawnQueue.add(new EnemySpawn(EnemySpawn.BASIC, lane));
            }
        }
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
        EnemySpawn spawn = spawnQueue.poll();
        if (spawn == null) return;

        int spawnX = world.getWidth() + GameConfig.s(30); // just off right edge
        int spawnY = LaneManager.getLaneY(spawn.lane);

        Enemy enemy = spawn.create();
        enemy.setLane(spawn.lane);
        world.addObject(enemy, spawnX, spawnY);
    }

    /** Switches to break mode, awards the wave-clear gold bonus. */
    private void triggerWaveBreak(MyWorld world) {
        waveInProgress  = false;
        waitingForBreak = true;
        waveBreakTimer.reset();
        waveBreakTimer.start();

        // Gold bonus scales with wave number
        CurrencyManager.earn(WAVE_CLEAR_BONUS + currentWave * 2);

        // AudioManager.play("wave_clear");
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

        // ── Type constants (add more as you create enemy classes) ─────────────
        public static final int BASIC = 0;
        public static final int FAST  = 1; // needs FastEnemy.java
        public static final int TANK  = 2; // needs TankEnemy.java

        // ── Fields ────────────────────────────────────────────────────────────
        public final int type;
        public final int lane;

        public EnemySpawn(int type, int lane) {
            this.type = type;
            this.lane = lane;
        }

        /**
         * Instantiates the correct Enemy subclass for this spawn.
         * Add new cases here as you build more enemy types.
         */
        public Enemy create() {
            switch (type) {
                // Uncomment each case once the corresponding class exists:
                // case FAST: return new FastEnemy();
                default:   return new BasicEnemy();
                case TANK: return new TankEnemy();
            }
        }
    }
}