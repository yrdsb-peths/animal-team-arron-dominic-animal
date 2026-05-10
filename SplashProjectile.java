import greenfoot.*;
import java.util.List;

public class SplashProjectile extends Actor {
    private Enemy target;
    private int damage;
    private int splashRadius; // e.g., 100 pixels
    private int speed = GameConfig.s(8);

    public SplashProjectile(Enemy target, int damage, int splashRadius) {
        this.target = target;
        this.damage = damage;
        this.splashRadius = splashRadius;
        
        GreenfootImage img = new GreenfootImage(20, 20);
        img.setColor(Color.ORANGE);
        img.fillOval(0, 0, 20, 20);
        setImage(img);
    }

    @Override
    public void act() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        if (target == null || target.getWorld() == null || target.isDead()) {
            world.removeObject(this);
            return;
        }

        turnTowards(target.getX(), target.getY());
        move(speed);

        // ON IMPACT:
        if (intersects(target)) {
            // 1. Damage enemies in range (Splash)
            List<Enemy> enemiesHit = getObjectsInRange(splashRadius, Enemy.class);
            for (Enemy e : enemiesHit) {
                e.takeDamage(damage);
            }
            
            // 2. SMART PUDDLE SPAWNING
            // Look for an existing puddle exactly where we hit
            List<DamagePuddle> existing = getObjectsInRange(GameConfig.s(40), DamagePuddle.class);
            
            if (!existing.isEmpty()) {
                // If one exists, grab the first one and add a layer
                existing.get(0).addLayer();
            } else {
                // Otherwise, create a new one
                world.addObject(new DamagePuddle(GameConfig.PUDDLE_DURATION, GameConfig.PUDDLE_TICK_DAMAGE), getX(), getY());
            }
            
            world.removeObject(this);
        }
    }
}