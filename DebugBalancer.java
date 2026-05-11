import greenfoot.*;

public class DebugBalancer {
    
    public static void printCheatSheet() {
        if (!GameConfig.DEBUG_MODE) return;

        System.out.println("\n=======================================================");
        System.out.println("====== 🛠️ DIO-DGE IT: BALANCE CHEAT SHEET 🛠️ ======");
        System.out.println("=======================================================\n");

        for (UnitRegistry.UnitData data : UnitRegistry.roster) {
            String name = data.unitClass.getSimpleName().replace("Unit", "").toUpperCase();
            System.out.println("--- [" + name + "] Base Cost: $" + data.cost + " ---");
            
            // Figure out base stats
            int baseHp = 0, baseDmg = 0;
            double baseCd = 0.0;
            
            if (name.equals("BASIC")) { baseHp = GameConfig.BASIC_UNIT_HP; baseDmg = GameConfig.BASIC_UNIT_DAMAGE; baseCd = GameConfig.BASIC_UNIT_COOLDOWN; }
            if (name.equals("SNIPER")) { baseHp = GameConfig.SNIPER_UNIT_HP; baseDmg = GameConfig.SNIPER_UNIT_DAMAGE; baseCd = GameConfig.SNIPER_UNIT_COOLDOWN; }
            if (name.equals("RAILGUN")) { baseHp = GameConfig.RAILGUN_UNIT_HP; baseDmg = GameConfig.RAILGUN_UNIT_DAMAGE; baseCd = GameConfig.RAILGUN_UNIT_COOLDOWN; }
            if (name.equals("ALCHEMIST")) { baseHp = GameConfig.ALCHEMIST_UNIT_HP; baseDmg = GameConfig.ALCHEMIST_UNIT_DAMAGE; baseCd = GameConfig.ALCHEMIST_UNIT_COOLDOWN; }
            if (name.equals("WALL")) { baseHp = GameConfig.WALL_UNIT_HP; }
            if (name.equals("BIGWALL")) { baseHp = GameConfig.BIG_WALL_UNIT_HP; }
            if (name.equals("COWARD")) { baseHp = GameConfig.COWARD_UNIT_HP; baseDmg = GameConfig.COWARD_UNIT_DAMAGE; baseCd = GameConfig.COWARD_UNIT_COOLDOWN; }

            for (int lvl = 1; lvl <= GameConfig.MAX_UNIT_LEVEL; lvl++) {
                int hp = (int)(baseHp * Math.pow(GameConfig.LEVEL_HP_MULT, lvl - 1));
                int dmg = (int)(baseDmg * Math.pow(GameConfig.LEVEL_DMG_MULT, lvl - 1));
                double cd = baseCd * Math.pow(GameConfig.LEVEL_COOLDOWN_MULT, lvl - 1);
                
                int placeCost = (int)(data.cost * Math.pow(GameConfig.PLACEMENT_COST_MULT, lvl - 1));
                
                int upgradeCost = 0;
                if (lvl < GameConfig.MAX_UNIT_LEVEL) {
                    upgradeCost = (int)(data.cost * GameConfig.UPGRADE_COST_BASE_MULT * Math.pow(GameConfig.UPGRADE_COST_EXP_MULT, lvl - 1));
                }

                System.out.printf("LVL %d | Place: $%5d | ShopUpgrd: $%6d || HP: %5d | DMG: %5d | CD: %.2fs\n", 
                                  lvl, placeCost, upgradeCost, hp, dmg, cd);
            }
            System.out.println("");
        }
        System.out.println("=======================================================\n");
    }
}