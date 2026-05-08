// ==================================================
// FILE: ./UICancelButton.java
// ==================================================
import greenfoot.*;

public class UICancelButton extends Actor {
    private PlacementManager pm;

    public UICancelButton(PlacementManager pm) {
        this.pm = pm;
        GreenfootImage img = new GreenfootImage(GameConfig.s(50), GameConfig.s(50));
        img.setColor(new Color(150, 0, 0)); // Dark Red
        img.fill();
        img.setColor(Color.WHITE);
        img.drawRect(0, 0, img.getWidth()-1, img.getHeight()-1);
        img.drawLine(15, 15, 35, 35);
        img.drawLine(35, 15, 15, 35);
        setImage(img);
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            pm.setSelectedUnit(0); // Set to "None"
        }
    }
}