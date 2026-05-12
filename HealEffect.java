import greenfoot.*;

public class HealEffect extends Actor {
    private int timer = 30;

    public void act() {
        setLocation(getX(), getY() - 1); // Float up
        timer--;
        
        if (timer % 10 < 5) getImage().setTransparency(0);
        else getImage().setTransparency(255);
        
        if (timer <= 0) getWorld().removeObject(this);
    }

    public HealEffect() {
        GreenfootImage img = new GreenfootImage(20, 20);
        img.setColor(Color.GREEN);
        // Draw a "+" sign
        img.fillRect(8, 0, 4, 20);
        img.fillRect(0, 8, 20, 4);
        setImage(img);
    }
}