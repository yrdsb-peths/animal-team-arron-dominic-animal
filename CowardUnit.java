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

    
    @Override
    public void updateVisual() {
        // If scared, we show a special small "Hidden" image
        if (isScared) {
            GreenfootImage hidden = new GreenfootImage(20, 10);
            hidden.setColor(new Color(130, 80, 30));
            hidden.fillOval(0, 0, 20, 10);
            setImage(hidden);
        } else {
            setImage(UnitVisuals.draw(7, level, Color.YELLOW));
        }
        setNormalImage(getImage());
    }
    
    @Override protected int getBaseHPFromConfig() { return GameConfig.COWARD_UNIT_HP; }
}