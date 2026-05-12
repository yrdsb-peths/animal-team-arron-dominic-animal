import greenfoot.*;
import java.util.List;

public class DomainExpansion extends Actor {
    private int radius = 10;
    private int maxRadius;
    private int alpha = 180;

    public DomainExpansion(int maxRadius) {
        this.maxRadius = maxRadius;
        updateImage();
    }

    public void act() {
        MyWorld world = (MyWorld)getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        // 1. Expand and Fade
        radius += 8;
        alpha -= 6;

        // 2. Buff every unit inside the circle
        List<Unit> units = getObjectsInRange(radius, Unit.class);
        for (Unit u : units) {
            u.applyCommanderBuff();
        }

        if (alpha <= 0 || radius >= maxRadius) {
            world.removeObject(this);
        } else {
            updateImage();
        }
    }

    private void updateImage() {
        GreenfootImage img = new GreenfootImage(radius * 2, radius * 2);
        
        // 1. A thick, glowing outer ring
        img.setColor(new Color(0, 255, 255, alpha));
        for(int i = 0; i < 5; i++) { // Draw 5 rings to make it thick
            img.drawOval(i, i, (radius * 2) - 1 - (i*2), (radius * 2) - 1 - (i*2));
        }
        
        // 2. A more noticeable inner pulse
        img.setColor(new Color(0, 255, 255, alpha / 3)); // Increased from alpha/4
        img.fillOval(5, 5, (radius * 2) - 11, (radius * 2) - 11);
        setImage(img);
    }
}