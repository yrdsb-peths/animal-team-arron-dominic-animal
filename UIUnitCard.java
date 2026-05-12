import greenfoot.*;

public class UIUnitCard extends Actor {
    private int unitID;
    private int price;
    private Color unitColor;
    private String key;
    private PlacementManager pm;
    private int homeY; // Where the card wants to be

    public UIUnitCard(int id, int price, Color color, String key, PlacementManager pm, int homeY) {
        this.unitID = id;
        this.price = price;
        this.unitColor = color;
        this.key = key;
        this.pm = pm;
        this.homeY = homeY;
        updateImage();
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
            updateImage();
        }
    }
    
    private void updateImage() {
        boolean isSelected = (pm.getSelectedUnit() == unitID);
        int size = GameConfig.s(50);
        
        GreenfootImage img = new GreenfootImage(size, size + 20);
        
        // Background
        img.setColor(isSelected ? Color.WHITE : new Color(40, 40, 40));
        img.fill();
        
        // Unit Icon
        img.setColor(unitColor);
        img.fillRect(10, 10, size - 20, size - 20);
        
        // PRICE (Bottom)
        img.setColor(isSelected ? Color.BLACK : Color.WHITE);
        img.setFont(new Font("SansSerif", true, false, 12));
        
        // --- FIXED: EXACT SAME MATH AS PLACEMENT MANAGER ---
        UnitRegistry.UnitData data = UnitRegistry.getById(unitID);
        int scaledCost = (int)(data.cost * Math.pow(GameConfig.PLACEMENT_COST_MULT, data.level - 1));
        int currentDisplayPrice = scaledCost * CalamityManager.getPriceMultiplier();
    
        img.drawString("$" + GameConfig.formatNumber(currentDisplayPrice), 5, size + 15);
        
        // KEYBOARD SHORTCUT 
        img.setColor(isSelected ? Color.RED : Color.YELLOW);
        img.setFont(new Font("SansSerif", true, false, 14));
        img.drawString("[" + key + "]", size - 25, 15);
        
        setImage(img);
    }
}