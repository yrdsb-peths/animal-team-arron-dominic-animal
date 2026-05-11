import greenfoot.*;
import java.util.List;

public class CowardUnit extends Unit {
    private boolean isScared = false;
    private int burstShotsPending = 0;
    private GameTimer burstTimer = new GameTimer(0.1, true); // Fires extremely fast

    public CowardUnit(int laneIndex, int colIndex) {
        super(GameConfig.COWARD_UNIT_HP, laneIndex, colIndex, GameConfig.COWARD_UNIT_COOLDOWN);
        updateVisual();
    }

    @Override
    protected void updateBehavior(MyWorld world) {
        List<Enemy> closeEnemies = getObjectsInRange(GameConfig.COWARD_SCARE_RANGE, Enemy.class);
        boolean shouldBeScared = !closeEnemies.isEmpty();

        // SCARING TRIGGERED!
        if (shouldBeScared && !isScared) {
            if (level >= GameConfig.COWARD_GIFT_UNLOCK) {
                burstShotsPending = 3; // Load 3 rapid-fire shots!
                burstTimer.reset();
                burstTimer.start();
                world.addObject(new FloatingText("PARTING GIFT!", Color.YELLOW, 12, 1, 30), getX(), getY() - 20);
            }
        }

        if (shouldBeScared != isScared) {
            isScared = shouldBeScared;
            updateVisual();
        }

        if (!isScared) {
            super.updateBehavior(world); 
        } else if (burstShotsPending > 0) {
            // Unload the burst shots even while hiding!
            burstTimer.update(world);
            if (burstTimer.isExpired()) {
                Enemy target = findTarget();
                if (target != null) attack(target);
                burstShotsPending--;
            }
        }
    }

    @Override
    protected void attack(Enemy target) {
        int scaledDmg = (int)(GameConfig.COWARD_UNIT_DAMAGE * Math.pow(GameConfig.LEVEL_DMG_MULT, level - 1));
        getWorld().addObject(new Projectile(target, scaledDmg, null), getX(), getY());
    }

    @Override public boolean isTargetable() { return !isScared; }
    @Override public void updateVisual() {
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