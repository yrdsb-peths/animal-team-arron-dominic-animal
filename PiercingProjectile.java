import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class PiercingProjectile extends Actor {
    private int damage;
    private int speed = GameConfig.s(12); // Faster than normal!
    private List<Enemy> alreadyHit = new ArrayList<>(); // Memory list

    public PiercingProjectile(int damage) {
        this.damage = damage;
        
        // Draw a long yellow laser beam
        GreenfootImage img = new GreenfootImage(40, 10);
        img.setColor(Color.CYAN);
        img.fillRect(0, 0, 40, 10);
        setImage(img);
    }

    @Override
    public void act() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        // Move strictly straight to the right
        setLocation(getX() + speed, getY());

        // Check for enemies touching the laser
        List<Enemy> touchingEnemies = getIntersectingObjects(Enemy.class);
        for (Enemy e : touchingEnemies) {
            // Only hurt them if we haven't hurt them yet!
            if (!alreadyHit.contains(e) && !e.isDead()) {
                //true means bypass shield
                e.takeDamage(damage, GameConfig.RAILGUN_LASER_BYPASS);
                alreadyHit.add(e); // Add to memory
            }
        }

        // Delete projectile when it goes off screen
        if (getX() > world.getWidth() + 50) {
            world.removeObject(this);
        }
    }
}