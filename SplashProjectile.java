import greenfoot.*;
import java.util.List;

public class SplashProjectile extends Actor {
    private Enemy target;
    private int damage;
    private int splashRadius; 
    private int speed = GameConfig.s(8);
    private int level; // ADDED: Store the level of the Alchemist

    // UPDATED CONSTRUCTOR: Now accepts level
    public SplashProjectile(Enemy target, int damage, int splashRadius, int level) {
        this.target = target;
        this.damage = damage;
        this.splashRadius = splashRadius;
        this.level = level; // Store it
        
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
        move(speed * GameConfig.GAME_SPEED);

        if (intersects(target)) {
            // 1. Splash Damage
            List<Enemy> enemiesHit = getObjectsInRange(splashRadius, Enemy.class);
            for (Enemy e : enemiesHit) {
                e.takeDamage(damage, GameConfig.ALCHEMIST_SPLASH_BYPASS);
            }
            
            // 2. SMART PUDDLE SPAWNING
            List<DamagePuddle> existing = getObjectsInRange(GameConfig.s(40), DamagePuddle.class);
            
            if (!existing.isEmpty()) {
                existing.get(0).addLayer();
            } else {
                // FIXED: Now correctly passes the level into the Puddle!
                world.addObject(new DamagePuddle(GameConfig.PUDDLE_DURATION, GameConfig.PUDDLE_TICK_DAMAGE, level), getX(), getY());
            }
            
            world.removeObject(this);
        }
    }
}