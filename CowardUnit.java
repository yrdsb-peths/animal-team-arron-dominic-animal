import greenfoot.*;
import java.util.List;

public class CowardUnit extends Unit {
    private boolean isScared = false;

    public CowardUnit(int laneIndex, int colIndex) {
        super(GameConfig.COWARD_UNIT_HP, laneIndex, colIndex, GameConfig.COWARD_UNIT_COOLDOWN);
        updateVisual();
    }

    @Override
    protected void updateBehavior(MyWorld world) {
        // 1. Check if any enemies are dangerously close
        List<Enemy> closeEnemies = getObjectsInRange(GameConfig.COWARD_SCARE_RANGE, Enemy.class);
        boolean shouldBeScared = !closeEnemies.isEmpty();

        // 2. If our state changes from brave to scared (or vice versa), update the visual
        if (shouldBeScared != isScared) {
            isScared = shouldBeScared;
            updateVisual();
        }

        // 3. Only attack if we are NOT scared
        if (!isScared) {
            super.updateBehavior(world); // This triggers the attackCooldown and attack()
        }
    }

    @Override
    protected void attack(Enemy target) {
        getWorld().addObject(new Projectile(target, GameConfig.COWARD_UNIT_DAMAGE, null), getX(), getY());
    }

    // THIS IS THE MAGIC: Enemies ask this unit if they should stop.
    @Override
    public boolean isTargetable() {
        return !isScared; // If scared, enemies ignore me!
    }

    private void updateVisual() {
        GreenfootImage img = new GreenfootImage(40, 40);
        
        if (isScared) {
            // HIDDEN: Just a tiny brown bump on the ground
            img.setColor(new Color(130, 80, 30)); // Dirt brown
            img.fillOval(10, 30, 20, 10);
        } else {
            // POPPED UP: A tall, bright yellow flower looking thing
            img.setColor(Color.YELLOW);
            img.fillOval(10, 0, 20, 20); // Head
            img.setColor(Color.GREEN);
            img.fillRect(18, 20, 4, 20); // Stem
        }
        
        setImage(img);
        setNormalImage(img); // Update hurt flash reference
    }
}