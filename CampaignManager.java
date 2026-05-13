import java.util.Arrays;
import java.util.List;

public class CampaignManager {
    
    public static class LevelConfig {
        public int id;
        public String title;
        public String tutorialText;
        public int maxWaves;
        public List<Integer> allowedUnits;
        
        public boolean spawnKamikaze;
        public boolean spawnShield;
        public boolean spawnSlime;
        public boolean calamitiesActive;

        public LevelConfig(int id, String title, String tutorialText, int maxWaves, 
                           List<Integer> allowedUnits, boolean kamikaze, boolean shield, 
                           boolean slime, boolean calamities) {
            this.id = id;
            this.title = title;
            this.tutorialText = tutorialText;
            this.maxWaves = maxWaves;
            this.allowedUnits = allowedUnits;
            this.spawnKamikaze = kamikaze;
            this.spawnShield = shield;
            this.spawnSlime = slime;
            this.calamitiesActive = calamities;
        }
    }

    public static LevelConfig getLevel(int id) {
        switch (id) {
            case 1:
                return new LevelConfig(1, "LEVEL 1: FIRST CONTACT", 
                    "DEFEND THE BASE!\nUse Basic Units and Walls.", 5, 
                    Arrays.asList(1, 5), false, false, false, false);
            case 2:
                return new LevelConfig(2, "LEVEL 2: WALL BREAKERS", 
                    "Kamikazes explode on walls.\nUse Snipers to slow them!", 7, 
                    Arrays.asList(1, 2, 5), true, false, false, false);
            case 3:
                return new LevelConfig(3, "LEVEL 3: HEAVY ARMOR", 
                    "Shields block bullets.\nAlchemists use Splash Damage!", 8, 
                    Arrays.asList(1, 2, 4, 5, 7), true, true, false, false);
            case 4:
                return new LevelConfig(4, "LEVEL 4: ZERO HOUR", 
                    "WARNING: Calamities Active.\nSurvive the Slime Horde.", 10, 
                    Arrays.asList(1, 2, 3, 4, 5, 6, 7), true, true, true, true);
            default:
                return getLevel(1);
        }
    }
}