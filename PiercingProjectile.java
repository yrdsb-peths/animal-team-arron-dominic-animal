import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class PiercingProjectile extends Actor {
    private int damage;
    private int level; // ADDED: Store the level
    private int speed = GameConfig.s(12); 
    private List<Enemy> alreadyHit = new ArrayList<>(); 

    // UPDATED CONSTRUCTOR: Now accepts level
    public PiercingProjectile(int damage, int level) {
        this.damage = damage;
        this.level = level; // Save it
        
        GreenfootImage img = new GreenfootImage(40, 10);
        img.setColor(Color.CYAN);
        img.fillRect(0, 0, 40, 10);
        setImage(img);
    }

    @Override
    public void act() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        setLocation(getX() + speed * GameConfig.GAME_SPEED, getY());

        List<Enemy> touchingEnemies = getIntersectingObjects(Enemy.class);
        for (Enemy e : touchingEnemies) {
            if (!alreadyHit.contains(e) && !e.isDead()) {
                e.takeDamage(damage, GameConfig.RAILGUN_LASER_BYPASS);
                alreadyHit.add(e); 
            }
        }

        // --- UPDATED DELETION LOGIC ---
        if (getX() > world.getWidth() + 50) {
            // HEAT TRAIL MECHANIC: Check if we are at the required level
            if (level >= GameConfig.RAILGUN_TRAIL_UNLOCK) {
                // Spawn the trail across the whole lane
                int worldW = world.getWidth();
                world.addObject(new HeatTrail(GameConfig.RAILGUN_TRAIL_DAMAGE, worldW), worldW / 2, getY());
            }
            
            world.removeObject(this);
        }
    }
}