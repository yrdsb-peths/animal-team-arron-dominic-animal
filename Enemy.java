import greenfoot.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public abstract class Enemy extends Actor {

    protected int health;
    protected int damage;
    protected boolean isDead = false;
    
    protected int laneIndex;
    protected float baseSpeed;
    public float speedMultiplier = 1.0f; // Modifiable by Status Effects
    
    public float damageDealtMultiplier = 1.0f; // Sniper Weakness
    public float damageTakenMultiplier = 1.0f; // Alchemist Corrosive
    
    protected List<StatusEffect> activeEffects = new ArrayList<>();
    
    protected double exactX;
    protected double exactY;
    
    protected int baseDrop;
    protected int spawnWave = 1;
    protected int maxHealth; 
    
    protected GameTimer attackTimer;

    public Enemy(int maxHealth, int damage, float baseSpeed, double attackCooldown, int laneIndex) {
        this.health = maxHealth;
        this.damage = damage;
        this.baseSpeed = baseSpeed;
        this.laneIndex = laneIndex;
        
        this.exactX = 0; 
        this.exactY = 0;
        
        // Timer loops automatically based on GameConfig stats
        this.attackTimer = new GameTimer(attackCooldown, true);
        this.attackTimer.start();
    }
    
    protected void addedToWorld(World world) {
        this.exactX = getX();
        this.exactY = getY();
    }

    @Override
    public final void act() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        if (isDead) {
            handleDeath(world);
            return;
        }
        
        //Reset multipliers
        this.speedMultiplier = 1.0f; 
        this.damageDealtMultiplier = 1.0f;
        this.damageTakenMultiplier = 1.0f;


        Iterator<StatusEffect> it = activeEffects.iterator();
        while (it.hasNext()) {
            StatusEffect effect = it.next();
            effect.update(this);
            if (effect.isExpired()) it.remove();
        }

        updateBehavior(world);
        
        // Inside Enemy.act()
        if (CalamityManager.isFogActive()) {
            // MAGIC HACK: 1 out of 255 is completely invisible to the human eye, 
            // but Greenfoot still registers it for Bullet and Wall collisions!
            getImage().setTransparency(1); 
        } else {
            getImage().setTransparency(255); // Normal
        }
        
        if (getWorld() != null) {
            checkHitbox(world);
            checkOffScreen(world);
        }
    }

    // ── THE FLEXIBLE BEHAVIOR SYSTEM ──────────────────────────────────────────
    
    /**
     * The default AI: Check for a unit. If blocked, attack. If clear, move.
     * You can override this ENTIRE method if you want an enemy that acts totally uniquely.
     */
    protected void updateBehavior(MyWorld world) {
        Unit unitInFront = (Unit) getOneIntersectingObject(Unit.class);
        
        // Only attack if the unit is actually targetable!
        if (unitInFront != null && !unitInFront.isDead() && unitInFront.isTargetable()) {
            attackTimer.update(world);
            if (attackTimer.isExpired()) {
                performAttack(unitInFront);
            }
        } else {
            performMovement(); // Keep walking right over them!
        }
    }

    /** 
     * Standard attack logic for 90% of enemies.
     * Automatically calculates weakness debuffs and applies Thorns tracking!
     */
    protected void performAttack(Unit target) {
        target.takeDamage(getFinalDamage(this.damage), this);
    }
    
    /** 
     * How the enemy moves. You can override this for jumping/teleporting.
     */

    protected void performMovement() {
        // Multiply by GAME_SPEED so they run faster
        double moveAmount = baseSpeed * speedMultiplier * GameConfig.GAME_SPEED;
        
        this.exactX -= moveAmount;
        super.setLocation((int)exactX, (int)exactY);
    }
    
        
    public void scaleStats(float hpMult, float dmgMult, int waveNum) {
        this.health = (int)(this.health * hpMult);
        this.damage = (int)(this.damage * dmgMult);
        this.spawnWave = waveNum;
    }
    // ──────────────────────────────────────────────────────────────────────────

    protected void checkHitbox(MyWorld world) {
        Base base = (Base) getOneIntersectingObject(Base.class);
        if (base != null) {
            base.takeDamage();
            this.die(); 
        }
    }

    public void applyEffect(StatusEffect newEffect) {
        activeEffects.removeIf(e -> e.getId().equals(newEffect.getId()));
        activeEffects.add(newEffect);
    }
    
    public int getLaneIndex() { return laneIndex; }
    public boolean isDead() { return isDead; }
    public void setLane(int lane) { this.laneIndex = lane; }

    public void takeDamage(int amount) {
        if (isDead) return;
        int finalAmount = (int)(amount * damageTakenMultiplier);
        
        health -= finalAmount;
        if (health <= 0) die();
    }
    
    protected int getFinalDamage(int baseDmg) {
        return (int)(baseDmg * damageDealtMultiplier);
    }
    
    // --- UPDATED TAKE DAMAGE ---
    public void takeDamage(int amount, boolean bypassShield) {
        if (isDead) return;
        
        // Apply Corrosive Gas amplification!
        int finalAmount = (int)(amount * damageTakenMultiplier);
        
        health -= finalAmount;
        if (health <= 0) die();
    }

    public void die() {
        if (!isDead) {
            isDead = true;
            MyWorld world = (MyWorld)getWorld();
            
            // --- NEW: LEVEL 4 SNIPER LOGIC ---
            if (markedForIceStatue) {
                world.addObject(new IceStatue(new GreenfootImage(getImage()), maxHealth), getX(), getY());
                world.removeObject(this);
                return; // Skip normal death rewards for now, statue handles it
            }
    
            /// ALCHEMIST LVL 5 CONTAGION
            if (this.damageTakenMultiplier > 1.0f) {
                 // FIX: Ensure the Alchemist is actually Level 5 before triggering this!
                 int alchLevel = UnitRegistry.getById(4).level; // 4 is Alchemist ID
                 if (alchLevel >= GameConfig.ALCHEMIST_CONTAGION_UNLOCK) {
                     // Pass level 5 to inherit max traits
                     world.addObject(new DamagePuddle(4.0, 10, 5), getX(), getY());
                     world.addObject(new FloatingText("CONTAGION", Color.GREEN, 15), getX(), getY());
                 }
            }

            
            // 1. Calculate Wave Scaling (LINEAR FORMULA)
            double dropGrowthRate = GameConfig.ENEMY_DROP_GROWTH_PCT / 100.0;
            double waveMult = 1.0 + (dropGrowthRate * spawnWave);
            
            // 2. Calculate Risk Multiplier (Left side = 2.5x, Right side = 1.0x)
            double screenPercent = (double)getX() / GameConfig.WORLD_WIDTH; 
            screenPercent = Math.max(0.0, Math.min(1.0, screenPercent)); 
            double riskMult = 1.0 + (1.5 * (1.0 - screenPercent)); 

            // 3. Final Calculation
            int finalDrop = (int)(baseDrop * waveMult * riskMult);
            
            CurrencyManager.earn(finalDrop);
            ScoreManager.addScore(finalDrop * 2);

            String amountText = GameConfig.formatNumber(finalDrop);
            getWorld().addObject(new FloatingText("+$" + amountText, Color.YELLOW), getX(), getY());
        }
    }

    protected abstract void handleDeath(MyWorld world);

    private void checkOffScreen(MyWorld world) {
        int buffer = GameConfig.s(150);
        if (getX() < -buffer || getX() > world.getWidth() + buffer ||
            getY() < -buffer || getY() > world.getHeight() + buffer) {
            world.removeObject(this);
        }
    }
    
    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        this.exactX = x;
        this.exactY = y;
    }
    
    protected boolean markedForIceStatue = false;

    public void markForIceKill() {
        this.markedForIceStatue = true;
    }

}