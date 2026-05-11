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
        return (int)(data.cost * data.level * GameConfig.UPGRADE_COST_STEEPNESS);
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
        
        img.setColor(new Color(40, 40, 40)); // Dark BG
        img.fill();
        
        // Border matches their current level color
        img.setColor(UnitVisuals.getLevelColor(data.level));
        img.drawRect(0, 0, w-1, h-1);
        img.drawRect(1, 1, w-3, h-3); // Thick border

        // Draw the visual representation of what the unit looks like NOW
        img.drawImage(UnitVisuals.draw(data.id, data.level, data.color), w/2 - 20, 20);
        
        // Text Info
        img.setColor(Color.WHITE);
        img.setFont(new Font("SansSerif", true, false, 14));
        img.drawString(data.unitClass.getSimpleName().replace("Unit", ""), 10, h/2 + 10);
        img.drawString("Level: " + data.level + " / " + GameConfig.MAX_UNIT_LEVEL, 10, h/2 + 30);
        
        if (data.level < GameConfig.MAX_UNIT_LEVEL) {
            img.setColor(Color.GREEN);
            img.drawString("Cost: $" + getUpgradeCost(), 10, h/2 + 60);
        } else {
            img.setColor(new Color(255, 215, 0));
            img.drawString("MAXED OUT", 10, h/2 + 60);
        }
        setImage(img);
    }
}