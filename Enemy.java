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
    protected List<StatusEffect> activeEffects = new ArrayList<>();
    
    protected double exactX;
    protected double exactY;

    // NEW: Attack Timer system
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

        this.speedMultiplier = 1.0f; // Reset before effects are applied

        Iterator<StatusEffect> it = activeEffects.iterator();
        while (it.hasNext()) {
            StatusEffect effect = it.next();
            effect.update(this);
            if (effect.isExpired()) it.remove();
        }

        updateBehavior(world);

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
        
        if (unitInFront != null && !unitInFront.isDead()) {
            // We are blocked! Tick the attack timer.
            attackTimer.update(world);
            if (attackTimer.isExpired()) {
                performAttack(unitInFront);
            }
        } else {
            // Path is clear! Walk forward.
            performMovement();
        }
    }

    /** 
     * HOOK 1: Every enemy subclass MUST define how it attacks.
     */
    protected abstract void performAttack(Unit target);

    /** 
     * HOOK 2: How the enemy moves. You can override this for jumping/teleporting.
     */
    protected void performMovement() {
        // Calculate the decimal speed (e.g., 0.75)
        double moveAmount = baseSpeed * speedMultiplier;
        
        // Update our high-precision coordinate
        this.exactX -= moveAmount;
        
        // Snap the actual actor to the integer version of our precise coordinate
        super.setLocation((int)exactX, (int)exactY);
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
        health -= amount;
        if (health <= 0) die();
    }

    public void die() {
        if (!isDead) {
            isDead = true;
            CurrencyManager.earn(10);
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

}