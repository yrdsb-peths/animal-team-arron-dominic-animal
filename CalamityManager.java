import greenfoot.*;
import java.util.List;

public class CalamityManager {
    private static int lastCalamityWave = 0;
    
    public static void checkCalamity(MyWorld world, int wave) {
        // Trigger every 5 waves, starting at wave 5
        if (wave >= 5 && wave % 5 == 0 && wave != lastCalamityWave) {
            lastCalamityWave = wave;
            triggerRandomCalamity(world, wave);
        }
    }

    private static void triggerRandomCalamity(MyWorld world, int wave) {
        int choice = GameRNG.getRandomNumber(6); // 0 to 5
        
        switch(choice) {
            case 0: runEarthquake(world); break;
            case 1: runMatrix(world); break;
            case 2: runGreatDrought(world); break;
            case 3: runStampede(world); break;
            case 4: runRumbling(world); break;
            case 5: runLaserBeam(world); break;
        }
    }

    // --- CALAMITY LOGIC ---

    private static void runEarthquake(MyWorld world) {
        announce(world, "CALAMITY: EARTHQUAKE", "All units HP reduced by 50%!", Color.ORANGE);
        world.startShake(100, 15);
        List<Unit> units = world.getObjects(Unit.class);
        for (Unit u : units) {
            u.takeDamage(u.health / 2);
        }
    }

    private static void runMatrix(MyWorld world) {
        announce(world, "CALAMITY: THE MATRIX", "Diagonal corruption detected!", Color.CYAN);
        // Diagonal math: laneIndex + colIndex is even or odd
        List<Unit> units = world.getObjects(Unit.class);
        for (Unit u : units) {
            if ((u.getLaneIndex() + u.getColIndex()) % 2 == 0) {
                u.die();
            }
        }
    }

    private static void runGreatDrought(MyWorld world) {
        announce(world, "CALAMITY: GREAT DROUGHT", "Plants are parched! Attack speed halved.", Color.YELLOW);
        List<Unit> units = world.getObjects(Unit.class);
        for (Unit u : units) {
            u.applyDrought(); // You'll need to add this to Unit.java
        }
    }

    private static void runLaserBeam(MyWorld world) {
        int targetLane = GameRNG.getRandomNumber(GameConfig.NUM_LANES);
        announce(world, "CALAMITY: ORBITAL STRIKE", "Lane " + (targetLane+1) + " is being erased!", Color.RED);
        world.addObject(new CalamityLaser(targetLane), world.getWidth()/2, LaneManager.getLaneY(targetLane));
    }

    private static void runStampede(MyWorld world) {
        announce(world, "CALAMITY: STAMPEDE", "A horde of Breakers approaches!", Color.RED);
        // We will tell WaveManager to force the next wave to be 100% Breakers
        WaveManager.forceNextWaveType(WaveManager.EnemySpawn.KAMIKAZE);
    }

    private static void runRumbling(MyWorld world) {
        announce(world, "CALAMITY: THE RUMBLING", "The heavy weights are here.", Color.GRAY);
        WaveManager.forceNextWaveType(WaveManager.EnemySpawn.TANK);
    }

    private static void announce(MyWorld world, String title, String sub, Color c) {
        world.getGSM().pushState(new BossCinematicState(title + "\n" + sub, 180));
    }
}