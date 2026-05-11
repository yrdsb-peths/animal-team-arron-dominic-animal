// ==================================================
// FILE: ./CalamityManager.java
// ==================================================
import greenfoot.*;

public class CalamityManager {
    private static int lastCalamityWave = 0;
    private static int crashWavesLeft = 0;
    private static boolean fogActive = false;
    private static int lastSeenWave = 0;
    
    private static GameTimer empTimer = new GameTimer(0, false); 
    
    public static void reset() {
        lastCalamityWave = 0;
        crashWavesLeft = 0;
        fogActive = false;
        lastSeenWave = 0;
        empTimer = new GameTimer(0, false);
    }
    
    public static void update(MyWorld world, int wave, String key) {
        // 2. Pass it into handleDebugKeys
        if (GameConfig.DEBUG_MODE) handleDebugKeys(world, key);
        
        empTimer.update(world);

        if (wave > lastSeenWave) {
            if (crashWavesLeft > 0) crashWavesLeft--;
            lastSeenWave = wave;
        }

        if (wave >= GameConfig.CALAMITY_INTERVAL && wave % GameConfig.CALAMITY_INTERVAL == 0 && wave != lastCalamityWave) {
            lastCalamityWave = wave;
            triggerRandomCalamity(world);
        }
    }

    // 1. Add 'String key' to parameters
    private static void handleDebugKeys(MyWorld world, String key) {
        // String key = Greenfoot.getKey();  <--- DELETE THIS LINE
        if (key == null) return;
        
        if (key.equals(GameConfig.DEBUG_KEY_QUAKE)) runEarthquake(world);
        if (key.equals(GameConfig.DEBUG_KEY_MATRIX)) runMatrix(world);
        if (key.equals(GameConfig.DEBUG_KEY_DROUGHT)) runGreatDrought(world);
        if (key.equals(GameConfig.DEBUG_KEY_LASER)) runLaserBeam(world);
        if (key.equals(GameConfig.DEBUG_KEY_RAIN)) runPurpleRain(world);
        if (key.equals(GameConfig.DEBUG_KEY_CRASH)) runFinancialCrash(world);
        if (key.equals(GameConfig.DEBUG_KEY_FOG)) runGreatFog(world);
        if (key.equals(GameConfig.DEBUG_KEY_EMP)) runEMP(world);
        if (key.equals(GameConfig.DEBUG_KEY_BLOODMOON)) runBloodMoon(world);
        if (key.equals(GameConfig.DEBUG_KEY_KAMIKAZE)) runKamikazeSwarm(world);
        if (key.equals(GameConfig.DEBUG_KEY_RUMBLING)) runRumbling(world);
    }

    private static void triggerRandomCalamity(MyWorld world) {
        int choice = GameRNG.getRandomNumber(GameConfig.TOTAL_CALAMITIES);
        switch (choice) {
            case 0: runEarthquake(world); break;
            case 1: runMatrix(world); break;
            case 2: runGreatDrought(world); break;
            case 3: runLaserBeam(world); break;
            case 4: runPurpleRain(world); break;
            case 5: runFinancialCrash(world); break;
            case 6: runGreatFog(world); break;
            case 7: runEMP(world); break;
            case 8: runBloodMoon(world); break;
            case 9: runKamikazeSwarm(world); break;
            case 10: runRumbling(world); break;
        }
    }
    
    // ── HELPER TO GET WAVE MANAGER ──
    private static WaveManager getWaveManager(MyWorld world) {
        if (world.getGSM().peekState() instanceof PlayingState) {
            return ((PlayingState)world.getGSM().peekState()).getWaveManager();
        }
        return null;
    }

    // ──────────────── TACTICAL SWARM CALAMITIES ──────────────── 


    private static void runRumbling(MyWorld world) {
        announce(world, "THE RUMBLING", "Heavy Shields incoming!", Color.GRAY, () -> {
            WaveManager wm = getWaveManager(world);
            if (wm != null) wm.replaceCurrentWave(WaveManager.EnemySpawn.HEAVY_SHIELD, GameConfig.SHIELD_SWARM_COUNT_MULT,1, false);
        });
    }

    // ──────────────── ENVIRONMENTAL CALAMITIES ──────────────── 

    private static void runEMP(MyWorld world) {
        announce(world, "EMP BLAST", "Towers disabled for " + (int)GameConfig.EMP_DURATION + "s!", Color.BLUE, () -> {
            empTimer = new GameTimer(GameConfig.EMP_DURATION, false);
            empTimer.start();
            world.addObject(new FloatingText("SYSTEM FAILURE", Color.CYAN, 60), world.getWidth()/2, world.getHeight()/2);
        });
    }

    private static void runKamikazeSwarm(MyWorld world) {
        announce(world, "GOBLIN TACTICS", "Kamikaze Swarm incoming!", Color.RED, () -> {
            WaveManager wm = getWaveManager(world);
            if (wm != null) {
                // 3x Count, 0.4s speed, Elite=True (gets 1 shield)
                wm.replaceCurrentWave(WaveManager.EnemySpawn.KAMIKAZE, GameConfig.KAMIKAZE_SWARM_COUNT_MULT, 0.4, true);
            }
        });
    }

    private static void runBloodMoon(MyWorld world) {
        announce(world, "BLOOD MOON", "The Slime King's Army Awakes!", Color.RED, () -> {
            WaveManager wm = getWaveManager(world);
            if (wm != null) {
                // 4x Count, 0.25s speed (insanely fast), Elite=True (gets 2 shields, double HP, speed boost)
                wm.replaceCurrentWave(WaveManager.EnemySpawn.SLIME, GameConfig.BLOODMOON_COUNT_MULT, GameConfig.BLOODMOON_SPAWN_RATE, true);
            }
            
            // Tint the screen dark red to match the Blood Moon theme!
            GreenfootImage bg = world.getBackground();
            bg.setColor(new Color(50, 0, 0, 100)); // Dark semi-transparent red
            bg.fill(); 
        });
    }

    private static void runEarthquake(MyWorld world) {
        announce(world, "EARTHQUAKE", "Buildings crumble! -50% Max HP", Color.ORANGE, () -> {
            world.startShake(60, 20);
            for (int i = 0; i < 30; i++) {
                world.addObject(new FallingDebris(), GameRNG.getRandomNumber(world.getWidth()), -GameRNG.getRandomNumber(300));
            }
            for (Unit u : world.getObjects(Unit.class)) u.takeDamage(u.getMaxHealth() / 2);
        });
    }

    private static void runMatrix(MyWorld world) {
        announce(world, "THE MATRIX", "Diagonal units deleted!", Color.CYAN, () -> {
            for (Unit u : world.getObjects(Unit.class)) {
                if ((u.getLaneIndex() + u.getColIndex()) % 2 == 0) {
                    world.addObject(new MatrixGlitch(), u.getX(), u.getY());
                    u.die();
                }
            }
        });
    }

    private static void runGreatDrought(MyWorld world) {
        announce(world, "GREAT DROUGHT", "Existing plants parched! 50% Speed", Color.YELLOW, () -> {
            for (Unit u : world.getObjects(Unit.class)) u.applyDrought();
        });
    }

    private static void runFinancialCrash(MyWorld world) {
        announce(world, "FINANCIAL CRASH", "Prices x5 for 5 waves!", Color.RED, () -> {
            crashWavesLeft = GameConfig.CRASH_DURATION;
        });
    }

    private static void runLaserBeam(MyWorld world) {
        int targetLane = 0;
        int maxLaneValue = -1;

        // 1. Scan lanes to find the most valuable target
        for (int l = 0; l < GameConfig.NUM_LANES; l++) {
            int currentLaneValue = 0;
            
            for (int c = 0; c < GameConfig.GRID_COLS; c++) {
                Unit u = LaneManager.getUnitAt(l, c);
                if (u != null) {
                    currentLaneValue += getUnitValueFromConfig(u);
                }
            }

            if (currentLaneValue > maxLaneValue) {
                maxLaneValue = currentLaneValue;
                targetLane = l;
            } else if (currentLaneValue == maxLaneValue && currentLaneValue > 0) {
                // Tie-breaker: 50% chance to switch
                if (GameRNG.getRandomNumber(100) < 50) targetLane = l;
            }
        }

        // 2. If the board is empty, pick at random
        if (maxLaneValue <= 0) {
            targetLane = GameRNG.getRandomNumber(GameConfig.NUM_LANES);
        }

        final int finalLane = targetLane; 
        announce(world, "TARGETED STRIKE", "LANE" + (finalLane + 1) + " locked!", Color.RED, () -> {
            world.addObject(new CalamityLaser(finalLane), world.getWidth()/2, LaneManager.getLaneY(finalLane));
        });
    }

    private static void runPurpleRain(MyWorld world) {
        announce(world, "PURPLE RAIN", "Acid rain incoming!", Color.MAGENTA, () -> {
            world.addObject(new PurpleRainController(), world.getWidth() / 2, world.getHeight() / 2);
        });
    }

    private static void runGreatFog(MyWorld world) {
        announce(world, "GREAT FOG", "Vision obscured!", Color.LIGHT_GRAY, () -> {
            fogActive = true;
            world.addObject(new FogOverlay(), world.getWidth()/2, world.getHeight()/2);
        });
    }

    private static void announce(MyWorld world, String title, String sub, Color c, Runnable action) {
        world.getGSM().pushState(new BossCinematicState(title + "\n" + sub, 120, action));
    }

    /** 
     * HELPER: Fetches cost directly from GameConfig based on the class type.
     */
    private static int getUnitValueFromConfig(Unit u) {
        if (u instanceof BasicUnit) {
            return ((BasicUnit) u).getStackCount() * GameConfig.BASIC_UNIT_COST;
        }
        if (u instanceof SniperUnit)    return GameConfig.SNIPER_UNIT_COST;
        if (u instanceof RailgunUnit)   return GameConfig.RAILGUN_UNIT_COST;
        if (u instanceof AlchemistUnit) return GameConfig.ALCHEMIST_UNIT_COST;
        if (u instanceof BigWallUnit)   return GameConfig.BIG_WALL_UNIT_COST;
        if (u instanceof WallUnit)      return GameConfig.WALL_UNIT_COST;
        if (u instanceof CowardUnit)    return GameConfig.COWARD_UNIT_COST;
        
        return 0; // Default fallback
    }
    public static int getPriceMultiplier() { return (crashWavesLeft > 0) ? GameConfig.CRASH_PRICE_MULT : 1; }
    public static boolean isFogActive() { return fogActive; }
    public static void stopFog() { fogActive = false; }
    public static boolean isEMPActive() { return empTimer.isActive() && !empTimer.isExpired(); }
}