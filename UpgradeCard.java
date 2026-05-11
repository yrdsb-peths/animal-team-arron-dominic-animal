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
        // Upgrade every existing unit of this type on the board
        List<Unit> units = world.getObjects(Unit.class);
        for (Unit u : units) {
            if (u.getClass() == data.unitClass) {
                u.reactToGlobalUpgrade();
            }
        }
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
    
        // 2. THE UNIT INSTANCE (The "Real" Object)
        // We create a real instance of the unit using its spawner.
        // We pass -1, -1 because it's not actually going on the grid.
        Unit dummy = data.spawner.create(-1, -1); 
        
        // Grab the image directly from the Actor itself
        GreenfootImage unitIcon = dummy.getImage();
        
        // Center the real actor's image on our card
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
            img.drawString("Upgrade: $" + getUpgradeCost(), 15, h/2 + 70);
        } else {
            img.setColor(new Color(255, 215, 0));
            img.drawString("MAX TECH", 15, h/2 + 70);
        }
        
        setImage(img);
    }
}