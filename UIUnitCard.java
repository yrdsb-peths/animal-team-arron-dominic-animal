// ==================================================
// FILE: ./UIUnitCard.java
// ==================================================
import greenfoot.*;

public class UIUnitCard extends Actor {
    private int unitID;
    private int price;
    private Color unitColor;
    private String key;
    private PlacementManager pm;
    private int homeY; // Where the card wants to be

    // SMART CACHING: Prevents the game from lagging by only redrawing when needed
    private boolean lastSelected = false;
    private int lastLevel = -1;
    private int lastPriceMult = -1;

    public UIUnitCard(int id, int price, Color color, String key, PlacementManager pm, int homeY) {
        this.unitID = id;
        this.price = price;
        this.unitColor = color;
        this.key = key;
        this.pm = pm;
        this.homeY = homeY;
        
        // Force the first draw
        UnitRegistry.UnitData data = UnitRegistry.getById(unitID);
        updateImage(data, false, 1);
    }

    public void act() {
        // 1. Update position based on scroll
        int targetY = homeY - UIScrollManager.getOffset();
        setLocation(getX(), targetY);

        // 2. Visibility Culling (Hide if outside the menu area)
        if (targetY < GameConfig.MENU_TOP_LIMIT || targetY > GameConfig.MENU_BOTTOM_LIMIT) {
            if (getImage().getTransparency() != 0) getImage().setTransparency(0);
        } else {
            // Only update/show if inside the area
            if (getImage().getTransparency() == 0) getImage().setTransparency(255);
            
            if (Greenfoot.mouseClicked(this)) {
                pm.setSelectedUnit(this.unitID);
            }
            
            // 3. SMART REDRAW LOGIC
            boolean isSelected = (pm.getSelectedUnit() == unitID);
            UnitRegistry.UnitData data = UnitRegistry.getById(unitID);
            int currentPriceMult = CalamityManager.getPriceMultiplier();

            // ONLY redraw the image if the state, level, or price multiplier has changed!
            if (isSelected != lastSelected || data.level != lastLevel || currentPriceMult != lastPriceMult) {
                updateImage(data, isSelected, currentPriceMult);
                lastSelected = isSelected;
                lastLevel = data.level;
                lastPriceMult = currentPriceMult;
            }
        }
    }
    
    private void updateImage(UnitRegistry.UnitData data, boolean isSelected, int priceMult) {
        int size = GameConfig.s(50);
        GreenfootImage img = new GreenfootImage(size, size + 20);
        
        // Background
        img.setColor(isSelected ? Color.WHITE : new Color(40, 40, 40));
        img.fill();
        
        // ==========================================
        // TRUE VISUALS: Grab the actual unit's look!
        // ==========================================
        Unit dummy = data.spawner.create(-1, -1); 
        GreenfootImage unitIcon = new GreenfootImage(dummy.getImage()); // Clone it so we don't break the original
        
        // Scale it down to fit perfectly inside the card
        int iconSize = size - 20;
        unitIcon.scale(iconSize, iconSize);
        img.drawImage(unitIcon, 10, 10);
        // ==========================================
        
        // PRICE (Bottom)
        img.setColor(isSelected ? Color.BLACK : Color.WHITE);
        img.setFont(new Font("SansSerif", true, false, 12));
        
        int scaledCost = (int)(price * Math.pow(GameConfig.PLACEMENT_COST_MULT, data.level - 1));
        int currentDisplayPrice = scaledCost * priceMult;
    
        img.drawString("$" + GameConfig.formatNumber(currentDisplayPrice), 5, size + 15);
        
        // KEYBOARD SHORTCUT (Top Right)
        img.setColor(isSelected ? Color.RED : Color.YELLOW);
        img.setFont(new Font("SansSerif", true, false, 14));
        img.drawString("[" + key + "]", size - 25, 15);
        
        setImage(img);
    }
}