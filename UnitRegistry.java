import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class UnitRegistry {
    
    // 1. A tiny data container for unit information
    public static class UnitData {
        public int id;
        public String key;
        public int cost;
        public Color color;
        public Spawner spawner;
        public Class<?> unitClass;
        public int level = 1; 

        public UnitData(int id, String key, int cost, Color color, Spawner spawner , Class<?> unitClass) {
            this.id = id; this.key = key; this.cost = cost; 
            this.color = color; this.spawner = spawner;
            this.unitClass = unitClass;
        }
    }

    // 2. An interface that acts as a "Recipe" to create the unit
    public interface Spawner {
        Unit create(int lane, int col);
    }

    // 3. The Master List!
    public static List<UnitData> roster = new ArrayList<>();

    static {
        // EVERY TIME YOU MAKE A NEW UNIT, JUST ADD ONE LINE HERE:
        roster.add(new UnitData(1, "1", GameConfig.BASIC_UNIT_COST, Color.GREEN, (l, c) -> new BasicUnit(l, c), BasicUnit.class));
        roster.add(new UnitData(2, "2", GameConfig.SNIPER_UNIT_COST, Color.MAGENTA, (l, c) -> new SniperUnit(l, c), SniperUnit.class));
        roster.add(new UnitData(3, "3", GameConfig.RAILGUN_UNIT_COST, Color.CYAN, (l, c) -> new RailgunUnit(l, c), RailgunUnit.class));
        roster.add(new UnitData(4, "4", GameConfig.ALCHEMIST_UNIT_COST, Color.ORANGE, (l, c) -> new AlchemistUnit(l, c), AlchemistUnit.class));
        roster.add(new UnitData(5, "5", GameConfig.WALL_UNIT_COST, new Color(100,70,40), (l, c) -> new WallUnit(l, c), WallUnit.class));
        roster.add(new UnitData(6, "6", GameConfig.BIG_WALL_UNIT_COST, new Color(60, 60, 70), (l, c) -> new BigWallUnit(l, c), BigWallUnit.class));
        roster.add(new UnitData(7, "7", GameConfig.COWARD_UNIT_COST, Color.YELLOW, (l, c) -> new CowardUnit(l, c), CowardUnit.class));
    }
    
    // Helper method to look up a unit by its ID
    public static UnitData getById(int id) {
        for(UnitData d : roster) {
            if(d.id == id) return d;
        }
        return roster.get(0); // Fallback to basic unit
    }
    
    public static UnitData getByClass(Class<?> clazz) {
        for (UnitData d : roster) {
            if (d.unitClass == clazz) return d;
        }
        return roster.get(0);
    }

    public static void loadLevels() {
        for (UnitData d : roster) {
            // Read permanent tech level from disk, default to 1
            d.level = SaveManager.getInt("unit_lvl_" + d.id); 
            if (d.level == 0) d.level = 1; // Failsafe
        }
    }
}