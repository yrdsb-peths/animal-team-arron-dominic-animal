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
        // 1. Set Level from Registry (This reads the permanent level!)
        this.level = UnitRegistry.getByClass(this.getClass()).level;
    
        // 2. Scale Stats cleanly based on the flat multiplier
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
        
        //
        if (isDead) { handleDeath(world); return; }
    
        if (hurtTimer.isActive()) {
            hurtTimer.update(world);
            if (hurtTimer.isExpired()) {
                setImage(normalImage); // Returns to the clean stage image
            }
        }

        // 1. Tick down the Commander Buff
        if (buffFrames > 0) buffFrames--;
    
        // 2. PRIMARY COOLDOWN TICK
        if (isDroughtAffected) {
            if (world.getActCount() % 2 == 0) attackCooldown.update(world);
        } else {
            attackCooldown.update(world);
        }
    
        // 3. SPEED BOOST TICKS (Commander Buff & Overclock)
        // If we have either buff, we tick the timer a second time
        if (hasCommanderBuff() || AbilityManager.isOverclocked()) {
            attackCooldown.update(world);
        }
    
        // 4. SELF-RAGE TICK (Specific to BasicUnit)
        // We check a new boolean we'll define in BasicUnit
        if (isSelfRaged()) {
            // Ticks a third time every 2nd frame (Approx 50% boost) 
            // or just call update(world) for 100% boost.
            if (world.getActCount() % 2 == 0) {
                attackCooldown.update(world);
            }
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
        
        float hpMult = GameConfig.LEVEL_HP_MULT;
        float dmgMult = GameConfig.LEVEL_DMG_MULT;

        // Apply the Level 4 Spike!
        if (level == 4) {
            hpMult = GameConfig.LVL_4_STAT_SPIKE_HP;
            dmgMult = GameConfig.LVL_4_STAT_SPIKE_DMG;
            getWorld().addObject(new FloatingText("STAT SURGE!", Color.ORANGE, 45), getX(), getY() - 20);
        }

        this.maxHealth = (int)(maxHealth * hpMult);
        this.health = maxHealth;
        
        // Note: Damage scaling depends on how your specific unit handles bullets
        // Most use: baseDamage * Math.pow(LEVEL_DMG_MULT, level-1)
        // Ensure your attack() methods pull the current level!

        updateVisual(); 
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
        if (isDead) return;
        
        if (hasCommanderBuff()) amount = (int)(amount * 0.7); 
        health -= amount;
    
        // --- IMPROVED HIT FLASH ---
        // We only trigger the flash if the unit is still alive
        if (health > 0) {
            // Create the flash image on the fly so it doesn't get stuck
            GreenfootImage flash = new GreenfootImage(normalImage);
            
            // WALLS get a White/Grey flash (sparks), others get Red (blood)
            if (this instanceof WallUnit) {
                flash.setColor(new Color(255, 255, 255, 120)); // White impact
            } else {
                flash.setColor(new Color(255, 0, 0, 100)); // Red tint
            }
            
            flash.fill();
            setImage(flash);
            
            hurtTimer.reset();
            hurtTimer.start();
        } else {
            die();
        }
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
    
    // --- COMMANDER DOMAIN BUFF SYSTEM ---
    private int buffFrames = 0;
    
    public void applyCommanderBuff() { 
        // Changed from 300 to 10. 
        // The Domain reapplies this every frame, so it only drops 
        // a fraction of a second after the Domain vanishes!
        buffFrames = 10; 
    }
    
    public boolean hasCommanderBuff() { 
        return buffFrames > 0; 
    }
    
    protected int getFinalDamage(int baseDamage) {
        if (hasCommanderBuff()) return (int)(baseDamage * 1.3); // +30% Damage Buff
        return baseDamage;
    }

    // UPDATE act() to decrement the buff
    // (Inside your act() method, right under `if (isDead) return;`, add this:)
    // if (buffFrames > 0) buffFrames--;
    
    // Add this helper to the bottom of Unit.java so BasicUnit can talk to it
    public boolean isSelfRaged() { return false; }
    
}