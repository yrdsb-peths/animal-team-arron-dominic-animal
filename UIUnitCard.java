import greenfoot.*;

public class UIUnitCard extends Actor {
    private int unitID;
    private int price;
    private Color unitColor;
    private String key;
    private PlacementManager pm;

    public UIUnitCard(int id, int price, Color color, String key, PlacementManager pm) {
        this.unitID = id;
        this.price = price;
        this.unitColor = color;
        this.key = key;
        this.pm = pm;
        
        updateImage();
    }

    public void act() {
        // Redraw only if selection changes
        updateImage();
        
        // Allow clicking the card itself to select
        if (Greenfoot.mouseClicked(this)) {
            pm.setSelectedUnit(this.unitID);
        }
    
        // 2. Redraw to show if we are selected
        updateImage();
    }

    private void updateImage() {
        boolean isSelected = (pm.getSelectedUnit() == unitID);
        int size = GameConfig.s(50);
        GreenfootImage img = new GreenfootImage(size, size + 20);
        
        // Background - Brighten if selected
        img.setColor(isSelected ? Color.WHITE : new Color(50, 50, 50));
        img.fill();
        
        // Unit Icon
        img.setColor(unitColor);
        img.fillRect(10, 10, size - 20, size - 20);
        
        // Price & Key Label
        img.setColor(isSelected ? Color.BLACK : Color.WHITE);
        img.setFont(new Font("SansSerif", true, false, 12));
        img.drawString("$" + price, 5, size + 12);
        img.drawString("[" + key + "]", size - 20, 15);
        
        setImage(img);
    }

}