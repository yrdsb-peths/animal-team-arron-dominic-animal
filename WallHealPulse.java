import greenfoot.*;

public class WallHealPulse extends Actor {
    private int alpha = 150;
    private int growth = 0;

    public WallHealPulse(int width, int height) {
        GreenfootImage img = new GreenfootImage(width + 10, height + 10);
        img.setColor(new Color(50, 255, 100, 100)); // Very soft green
        img.fill();
        // Add a bright border
        img.setColor(new Color(200, 255, 200, 150));
        img.drawRect(0, 0, img.getWidth()-1, img.getHeight()-1);
        setImage(img);
    }

    public void act() {
        alpha -= 10;
        growth += 1;
        if (alpha <= 0) {
            getWorld().removeObject(this);
        } else {
            getImage().setTransparency(alpha);
            // Slightly expand the pulse as it fades
            GreenfootImage img = getImage();
            img.scale(img.getWidth() + growth, img.getHeight() + growth);
        }
    }
}