import greenfoot.*;
import java.util.List;

public class CalamityManager {
    private static int lastCalamityWave = 0;
    private static int crashWavesLeft = 0;
    private static boolean fogActive = false;
    private static int lastSeenWave = 0;
    
    /** Main loop: called by PlayingState every frame */
    public static void update(MyWorld world, int wave) {
        if (GameConfig.DEBUG_MODE) {
            handleDebugKeys(world);
        }

        // DECREMENT DURATIONS ONLY ONCE PER WAVE CHANGE
        if (wave > lastSeenWave) {
            if (crashWavesLeft > 0) crashWavesLeft--;
            lastSeenWave = wave;
        }

        // Trigger random calamity
        if (wave >= GameConfig.CALAMITY_INTERVAL && wave % GameConfig.CALAMITY_INTERVAL == 0 && wave != lastCalamityWave) {
            lastCalamityWave = wave;
            triggerRandomCalamity(world);
        }
    }

    private static void handleDebugKeys(MyWorld world) {
        String key = Greenfoot.getKey();
        if (key == null) return;

        // Press letters to trigger specific calamities for testing
        if (key.equals("f")) runFinancialCrash(world);
        if (key.equals("e")) runEarthquake(world);
        if (key.equals("m")) runMatrix(world);
        if (key.equals("d")) runGreatDrought(world);
        if (key.equals("l")) runLaserBeam(world);
        if (key.equals("p")) runPurpleRain(world);
        if (key.equals("g")) runGreatFog(world);
    }

    private static void triggerRandomCalamity(MyWorld world) {
        int choice = GameRNG.getRandomNumber(7);
        if (choice == 0) runEarthquake(world);
        else if (choice == 1) runMatrix(world);
        else if (choice == 2) runGreatDrought(world);
        else if (choice == 3) runLaserBeam(world);
        else if (choice == 4) runPurpleRain(world);
        else if (choice == 5) runFinancialCrash(world);
        else if (choice == 6) runGreatFog(world);
    }

    // --- IMPLEMENTATIONS ---

    private static void runEarthquake(MyWorld world) {
        announce(world, "EARTHQUAKE", "Buildings crumble! -50% HP", Color.ORANGE);
        world.startShake(120, 15);
        for (Unit u : world.getObjects(Unit.class)) {
            u.takeDamage(u.getMaxHealth() / 2);
        }
    }

    private static void runMatrix(MyWorld world) {
        announce(world, "THE MATRIX", "Diagonal units deleted!", Color.CYAN);
        for (Unit u : world.getObjects(Unit.class)) {
            if ((u.getLaneIndex() + u.getColIndex()) % 2 == 0) {
                u.die();
            }
        }
    }

    private static void runGreatDrought(MyWorld world) {
        announce(world, "GREAT DROUGHT", "Plants are parched! 50% Speed", Color.YELLOW);
        for (Unit u : world.getObjects(Unit.class)) u.applyDrought();
    }

    private static void runFinancialCrash(MyWorld world) {
        crashWavesLeft = GameConfig.CRASH_DURATION;
        announce(world, "FINANCIAL CRASH", "Prices x5 for 5 waves!", Color.RED);
    }

    private static void runLaserBeam(MyWorld world) {
        int targetLane = GameRNG.getRandomNumber(GameConfig.NUM_LANES);
        announce(world, "ORBITAL STRIKE", "Lane " + (targetLane+1) + " targetted!", Color.RED);
        world.addObject(new CalamityLaser(targetLane), world.getWidth()/2, LaneManager.getLaneY(targetLane));
    }

    private static void runPurpleRain(MyWorld world) {
        announce(world, "PURPLE RAIN", "Acid rain incoming!", Color.MAGENTA);
        world.addObject(new PurpleRainController(), world.getWidth() / 2, world.getHeight() / 2);
    }

    private static void runGreatFog(MyWorld world) {
        fogActive = true;
        announce(world, "GREAT FOG", "Vision obscured!", Color.LIGHT_GRAY);
        world.addObject(new FogOverlay(), world.getWidth()/2, world.getHeight()/2);
    }

    private static void announce(MyWorld world, String title, String sub, Color c) {
        world.getGSM().pushState(new BossCinematicState(title + "\n" + sub, 120));
    }

    public static int getPriceMultiplier() { return (crashWavesLeft > 0) ? GameConfig.CRASH_PRICE_MULT : 1; }
    public static boolean isFogActive() { return fogActive; }
    public static void stopFog() { fogActive = false; }
}