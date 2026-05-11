import greenfoot.*;
import java.util.List;

public abstract class Unit extends Actor {

    protected int health;
    protected int laneIndex;
    protected int colIndex;
    protected boolean isDead = false;
    
    private GameTimer hurtTimer = new GameTimer(0.15, false); // Flash for 0.15s
    private GreenfootImage normalImage;
    private GreenfootImage hurtImage;
    
    // Cooldown timer for attacks
    protected GameTimer attackCooldown;

    public Unit(int health, int laneIndex, int colIndex, double cooldownSeconds) {
        this.health = health;
        this.laneIndex = laneIndex;
        this.colIndex = colIndex;
        this.attackCooldown = new GameTimer(cooldownSeconds, false);
    }

    @Override
    public final void act() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;
        
        if (hurtTimer.isActive()) {
            hurtTimer.update(world);
            if (hurtTimer.isExpired()) setImage(normalImage);
        }
        
        if (isDead) {
            handleDeath(world);
            return;
        }
        attackCooldown.update(world);
        
        // OVERCLOCK MAGIC: Tick the timer a second time! 
        // This halves the cooldown of every unit on the board.
        if (AbilityManager.isOverclocked()) {
            attackCooldown.update(world);
        }
        
        if (isDroughtAffected) {
            // Only tick the cooldown every other frame (50% speed)
            if (getWorld().getObjects(MyWorld.class).get(0).getActCount() % 2 == 0) {
                attackCooldown.update(world);
            }
        } else {
            attackCooldown.update(world);
        }
        updateBehavior(world);
    }

    protected void updateBehavior(MyWorld world) {
        if (!attackCooldown.isActive() || attackCooldown.isExpired()) {
            Enemy target = findTarget();
            if (target != null) {
                attack(target);
                attackCooldown.reset();
                attackCooldown.start();
            }
        }
    }

    /** Finds the furthest-left enemy in the exact same lane. */
    protected Enemy findTarget() {
        Enemy furthest = null;
        int furthestX = Integer.MAX_VALUE;

        for (Enemy e : getWorld().getObjects(Enemy.class)) {
            if (e.getLaneIndex() == laneIndex && !e.isDead()) {
                // Pick the one with the lowest X coordinate (closest to base)
                if (e.getX() < furthestX && isWithinRange(e)) {
                    furthest = e;
                    furthestX = e.getX();
                }
            }
        }
        return furthest;
    }

    protected boolean isWithinRange(Enemy e) {
        return true; // Default: infinite range in the same lane. Override for melee!
    }

    protected abstract void attack(Enemy target);

    public void takeDamage(int amount) {
        health -= amount;
        // Trigger hurt animation
        if (normalImage == null) normalImage = new GreenfootImage(getImage());
        if (hurtImage == null) {
            hurtImage = new GreenfootImage(normalImage);
            hurtImage.setColor(new Color(255, 0, 0, 100)); // Transparent red overlay
            hurtImage.fill();
        }
        setImage(hurtImage);
        hurtTimer.reset();
        hurtTimer.start();
    
        if (health <= 0) die();
    }
    
    protected void setNormalImage(GreenfootImage newImg) {
        this.normalImage = new GreenfootImage(newImg);
        // Create a new hurt image based on the new look
        this.hurtImage = new GreenfootImage(normalImage);
        this.hurtImage.setColor(new Color(255, 0, 0, 100));
        this.hurtImage.fill();
    }

    public void die() {
        if (!isDead) {
            isDead = true;
            LaneManager.vacate(laneIndex, colIndex); // Free the grid cell!
        }
    }

    private void handleDeath(MyWorld world) {
        world.removeObject(this);
    }
    
    public boolean isDead() {
        return isDead;
    }
    
    public boolean isTargetable() {
        return true; 
    }
    
    public int getLaneIndex() { return laneIndex; }
    public int getColIndex() { return colIndex; }
    
    private boolean isDroughtAffected = false;
    
    public void applyDrought() {
        this.isDroughtAffected = true;
        // Visually turn the unit a bit brown/dry
        getImage().setColor(new Color(100, 60, 20, 100));
        getImage().fill();
    }

}