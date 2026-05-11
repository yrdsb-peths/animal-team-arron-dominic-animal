import greenfoot.*;
import java.util.List;

public class UpgradeCard extends Actor {
    private UnitRegistry.UnitData data;

    public UpgradeCard(UnitRegistry.UnitData data) {
        this.data = data;
        updateImage();
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            int cost = getUpgradeCost();
            if (data.level < GameConfig.MAX_UNIT_LEVEL && CurrencyManager.spend(cost)) {
                data.level++;
                applyGlobalUpgrade();
                updateImage();
            }
        }
    }
   
    private int getUpgradeCost() {
        // Formula: BaseCost * 10 * (3.0 ^ (Level-1))
        double exponentialCurve = Math.pow(GameConfig.UPGRADE_COST_EXP_MULT, data.level - 1);
        return (int)(data.cost * GameConfig.UPGRADE_COST_BASE_MULT * exponentialCurve);
    }

    private void applyGlobalUpgrade() {
        MyWorld world = (MyWorld)getWorld();
        
        // DELETED the loop that upgrades existing units!
        // Tech upgrades now ONLY apply to future placements from the PlacementManager.
        
        world.addObject(new FloatingText("TECH RESEARCHED!", Color.YELLOW, 40), world.getWidth()/2, GameConfig.s(100));
    }
    
    private void updateImage() {
        int w = GameConfig.SHOP_CARD_WIDTH;
        int h = GameConfig.SHOP_CARD_HEIGHT;
        GreenfootImage img = new GreenfootImage(w, h);
        
        // 1. Background & Border
        img.setColor(new Color(25, 25, 35));
        img.fill();
        img.setColor(UnitVisuals.getLevelColor(data.level));
        img.drawRect(0, 0, w-1, h-1);
    
        Unit dummy = data.spawner.create(-1, -1); 
        GreenfootImage unitIcon = dummy.getImage();
        
        int iconX = (w - unitIcon.getWidth()) / 2;
        int iconY = 30; 
        img.drawImage(unitIcon, iconX, iconY);
        
        // 3. Text Info
        img.setColor(Color.WHITE);
        img.setFont(new Font("SansSerif", true, false, 16));
        String name = data.unitClass.getSimpleName().replace("Unit", "");
        img.drawString(name, 15, h/2 + 25);
        
        img.setFont(new Font("SansSerif", false, false, 14));
        img.drawString("Lvl: " + data.level, 15, h/2 + 45);
        
        if (data.level < GameConfig.MAX_UNIT_LEVEL) {
            img.setColor(new Color(100, 255, 100));
            img.drawString("Upgrade: $" + getUpgradeCost(), 15, h/2 + 65);
            
            // --- DEBUG MODE: SHOW NEXT LEVEL STATS ON THE CARD ---
            if (GameConfig.DEBUG_MODE) {
                img.setFont(new Font("Courier New", false, false, 11));
                img.setColor(Color.LIGHT_GRAY);
                
                int nextLvl = data.level + 1;
                int nextHP = (int)(dummy.getMaxHealth() / Math.pow(GameConfig.LEVEL_HP_MULT, data.level - 1) * Math.pow(GameConfig.LEVEL_HP_MULT, nextLvl - 1));
                
                // We fake the damage config fetch based on class name for debugging
                int baseDmg = 0;
                if (name.equals("Basic")) baseDmg = GameConfig.BASIC_UNIT_DAMAGE;
                if (name.equals("Sniper")) baseDmg = GameConfig.SNIPER_UNIT_DAMAGE;
                if (name.equals("Railgun")) baseDmg = GameConfig.RAILGUN_UNIT_DAMAGE;
                if (name.equals("Alchemist")) baseDmg = GameConfig.ALCHEMIST_UNIT_DAMAGE;
                
                int nextDmg = (int)(baseDmg * Math.pow(GameConfig.LEVEL_DMG_MULT, nextLvl - 1));
                
                if (baseDmg > 0) {
                    img.drawString("Nxt-> HP:" + nextHP + " DMG:" + nextDmg, 5, h - 8);
                } else {
                    img.drawString("Nxt-> HP:" + nextHP, 5, h - 8);
                }
            }
        } else {
            img.setColor(new Color(255, 215, 0));
            img.drawString("MAX TECH", 15, h/2 + 70);
        }
        
        setImage(img);
    }
}