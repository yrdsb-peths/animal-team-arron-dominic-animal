import greenfoot.*;

public class UpgradeCard extends Actor {
    private UnitRegistry.UnitData data;

    public UpgradeCard(UnitRegistry.UnitData data) {
        this.data = data;
        updateImage();
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            int cost = getUpgradeCost();
            if (data.level < GameConfig.MAX_UNIT_LEVEL && SaveManager.spendTechPoints(cost)) {
                data.level++;
                SaveManager.setInt("unit_lvl_" + data.id, data.level);
                SaveManager.save();
                
                MyWorld world = (MyWorld)getWorld();
                world.addObject(new FloatingText("TECH UPGRADED!", Color.YELLOW, 40), world.getWidth()/2, GameConfig.s(100));
                updateImage();
            }
        }
    }
   
    private int getUpgradeCost() {
        if (data.level >= GameConfig.MAX_UNIT_LEVEL) return 0;
        return GameConfig.TECH_UPGRADE_COSTS[data.level];
    }

    private void updateImage() {
        int w = GameConfig.SHOP_CARD_WIDTH;
        int h = GameConfig.SHOP_CARD_HEIGHT;
        GreenfootImage img = new GreenfootImage(w, h);
        
        img.setColor(new Color(25, 25, 35));
        img.fill();
        img.setColor(UnitVisuals.getLevelColor(data.level));
        img.drawRect(0, 0, w-1, h-1);
    
        Unit dummy = data.spawner.create(-1, -1); 
        GreenfootImage unitIcon = dummy.getImage();
        
        int iconX = (w - unitIcon.getWidth()) / 2;
        int iconY = 30; 
        img.drawImage(unitIcon, iconX, iconY);
        
        img.setColor(Color.WHITE);
        img.setFont(new Font("SansSerif", true, false, 16));
        String name = data.unitClass.getSimpleName().replace("Unit", "");
        img.drawString(name, 15, h/2 + 25);
        
        img.setFont(new Font("SansSerif", false, false, 14));
        img.drawString("Lvl: " + data.level, 15, h/2 + 45);
        
        if (data.level < GameConfig.MAX_UNIT_LEVEL) {
            img.setColor(new Color(100, 255, 100));
            img.drawString("Cost: " + getUpgradeCost() + " TP", 15, h/2 + 65);
        } else {
            img.setColor(new Color(255, 215, 0));
            img.drawString("MAX TECH", 15, h/2 + 70);
        }
        
        setImage(img);
    }
}