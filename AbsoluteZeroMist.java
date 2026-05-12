import greenfoot.*;

public class AbsoluteZeroMist extends Actor {
    private int alpha = 100;

    public AbsoluteZeroMist() {
        GreenfootImage img = new GreenfootImage(40, 40);
        img.setColor(new Color(255, 255, 255, alpha));
        img.fillOval(0, 0, 40, 40);
        setImage(img);
    }

    public void act() {
        setLocation(getX(), getY() + 1); // Mist sinks (cold air)
        alpha -= 4;
        if (alpha <= 0) getWorld().removeObject(this);
        else getImage().setTransparency(alpha);
    }
}