import greenfoot.*;
import java.util.List;

public abstract class Unit extends Actor {

    protected int health;
    protected int maxHealth;
    protected int laneIndex;
    protected int colIndex;
    protected boolean isDead = false;
    
    private GameTimer hurtTimer = new GameTimer(0.15, false); // Flash for 0.15s
    private GreenfootImage normalImage;
    private GreenfootImage hurtImage;
    
    // Cooldown timer for attacks
    protected GameTimer attackCooldown;
    
    protected int level = 1;


    public Unit(int health, int laneIndex, int colIndex, double cooldownSeconds) {
        // 1. Set Level from Registry
        UnitRegistry.UnitData data = UnitRegistry.getByClass(this.getClass());
        this.level = UnitRegistry.getByClass(this.getClass()).level;
    
        // 2. Scale Stats
        this.maxHealth = (int)(health * Math.pow(GameConfig.LEVEL_HP_MULT, level - 1));
        this.health = maxHealth;
        this.laneIndex = laneIndex;
        this.colIndex = colIndex;
        
        double scaledCD = cooldownSeconds * Math.pow(GameConfig.LEVEL_COOLDOWN_MULT, level - 1);
        this.attackCooldown = new GameTimer(scaledCD, false); 
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
        
        if (isDroughtAffected) {
            // Use the world reference directly instead of getObjects()
            if (world.getActCount() % 2 == 0) {
                attackCooldown.update(world);
            }
        } else {
            attackCooldown.update(world);
        }
        
        // OVERCLOCK MAGIC
        if (AbilityManager.isOverclocked()) {
            attackCooldown.update(world);
        }
        
        updateBehavior(world);
    }

    protected void updateBehavior(MyWorld world) {
        // EMP PREVENTS FIRING!
        if (CalamityManager.isEMPActive()) return;

        if (!attackCooldown.isActive() || attackCooldown.isExpired()) {
            Enemy target = findTarget();
            if (target != null) {
                attack(target);
                attackCooldown.reset();
                attackCooldown.start();
            }
        }
    }
        
      
    public abstract void updateVisual(); 
    
    public void upgrade() {
        if (level >= GameConfig.MAX_UNIT_LEVEL) return;
        level++;
        
        // Exponential Scaling
        this.maxHealth = (int)(maxHealth * GameConfig.LEVEL_HP_MULT);
        this.health = maxHealth;
        
        double currentCD = attackCooldown.getTotalFrames() / 60.0;
        attackCooldown.setDuration(currentCD * GameConfig.LEVEL_COOLDOWN_MULT);
        
        updateVisual(); // Now this works!
        
        getWorld().addObject(new FloatingText("LVL " + level, UnitVisuals.getLevelColor(level), 30), getX(), getY());
    }
    
    public int getUpgradeCost() {
        int baseCost = UnitRegistry.getByClass(this.getClass()).cost;
        // EXPONENTIAL FORMULA: Matches the Shop logic
        double exponentialCurve = Math.pow(GameConfig.UPGRADE_COST_EXP_MULT, this.level - 1);
        return (int)(baseCost * GameConfig.UPGRADE_COST_BASE_MULT * exponentialCurve);
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

    public void takeDamage(int amount, Enemy attacker) {
        health -= amount;
        
        if (normalImage == null) normalImage = new GreenfootImage(getImage());
        if (hurtImage == null) {
            hurtImage = new GreenfootImage(normalImage);
            hurtImage.setColor(new Color(255, 0, 0, 100)); 
            hurtImage.fill();
        }
        setImage(hurtImage);
        hurtTimer.reset();
        hurtTimer.start();
    
        if (health <= 0) die();
    }
    // Fallback for Acid Puddles and Earthquakes
    public void takeDamage(int amount) {
        takeDamage(amount, null);
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
    
    public int getMaxHealth() {
        return maxHealth;
    }
    
    // Abstract helper to ensure we know the starting HP for scaling
    protected abstract int getBaseHPFromConfig();
        
    public int getLevel() {
        return this.level;
    }
    
}