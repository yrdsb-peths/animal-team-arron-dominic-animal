import greenfoot.*;
import java.util.List;

public class Explosion extends Actor {
    private int size = 20;
    private int damage;
    private boolean hasDealtDamage = false;

    public Explosion(int damage) {
        this.damage = damage;
    }

    public void act() {
        if (!hasDealtDamage) {
            // Deal massive damage to enemies in a 1.5 unit radius
            List<Enemy> targets = getObjectsInRange(GameConfig.s(150), Enemy.class);
            for (Enemy e : targets) {
                e.takeDamage(damage, true);
            }
            ((MyWorld)getWorld()).startShake(15, 10); // Shake the screen!
            hasDealtDamage = true;
        }

        size += 15;
        updateImage();

        if (size > 200) {
            getWorld().removeObject(this);
        }
    }

    private void updateImage() {
        GreenfootImage img = new GreenfootImage(size, size);
        int alpha = Math.max(0, 255 - (size * 1));
        
        // Fire Colors
        img.setColor(new Color(255, 150, 0, alpha)); // Orange
        img.fillOval(0, 0, size, size);
        img.setColor(new Color(255, 255, 200, alpha)); // Inner White/Yellow
        img.fillOval(size/4, size/4, size/2, size/2);
        setImage(img);
    }
}