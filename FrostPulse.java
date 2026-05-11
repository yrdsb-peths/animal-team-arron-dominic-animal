import greenfoot.*;

public class FrostPulse extends Actor {
    private int size = 10;
    private int maxSize;
    private int alpha = 150;

    public FrostPulse(int radius) {
        this.maxSize = radius * 2;
        updateImage();
    }

    public void act() {
        size += 10; // Expand
        alpha -= 8; // Fade
        
        if (alpha <= 0 || size >= maxSize) {
            getWorld().removeObject(this);
        } else {
            updateImage();
        }
    }

    private void updateImage() {
        GreenfootImage img = new GreenfootImage(size, size);
        img.setColor(new Color(0, 150, 255, alpha));
        img.drawOval(0, 0, size-1, size-1);
        img.setColor(new Color(0, 200, 255, alpha/2));
        img.fillOval(5, 5, size-10, size-10);
        setImage(img);
    }
}