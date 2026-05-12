import greenfoot.*;

public class AbsoluteZeroNova extends Actor {
    private int size = 10;
    private int alpha = 255;

    public void act() {
        size += 20;
        alpha -= 10;
        
        GreenfootImage img = new GreenfootImage(size, size);
        img.setColor(new Color(180, 255, 255, Math.max(0, alpha)));
        img.fillOval(0, 0, size, size);
        img.setColor(new Color(255, 255, 255, Math.max(0, alpha)));
        img.drawOval(2, 2, size-4, size-4);
        
        setImage(img);
        if (alpha <= 0) getWorld().removeObject(this);
    }
}