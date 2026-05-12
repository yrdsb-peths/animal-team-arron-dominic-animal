// ==================================================
// FILE: ./BasicUnit.java
// ==================================================
import greenfoot.*;
import java.util.List;

public class BasicUnit extends Unit {
    private int stackCount = 1;
    private boolean isRaged = false;
    private GameTimer domainTimer = new GameTimer(10.0, true); 
    private DomainExpansion myDomain = null; // Track our domain!
    private GameTimer domainRestTimer = new GameTimer(7.0, false); 
    private boolean domainIsReady = true;

    public BasicUnit(int laneIndex, int colIndex) {
        super(GameConfig.BASIC_UNIT_HP, laneIndex, colIndex, GameConfig.BASIC_UNIT_COOLDOWN);
        this.stackCount = 1; 
        // CRITICAL: You must start the timer or it stays at 10.0 forever!
        domainTimer.start(); 
        updateVisual();
    }
    
    @Override
    protected void updateBehavior(MyWorld world) {
        // 1. RADAR CHECK (~1.5 cells)
        List<Enemy> neighbors = getObjectsInRange(GameConfig.s(120), Enemy.class);
        boolean enemyInPersonalSpace = !neighbors.isEmpty();

        // 2. RAGE MODE (Level 3+)
        if (level >= GameConfig.BASIC_RAGE_UNLOCK) {
            if (enemyInPersonalSpace && !isRaged) {
                isRaged = true;
                updateVisual();
                
                // ADJUST SPEED HERE:
                double newCd = (GameConfig.BASIC_UNIT_COOLDOWN * Math.pow(GameConfig.LEVEL_COOLDOWN_MULT, level - 1)) 
                               / GameConfig.COMMANDER_SPEED_BOOST; 
                attackCooldown.setDuration(newCd);
            }
             else if (!enemyInPersonalSpace && isRaged) {
                isRaged = false;
                updateVisual();
                // RESET TO NORMAL
                double normalCd = (GameConfig.BASIC_UNIT_COOLDOWN * Math.pow(GameConfig.LEVEL_COOLDOWN_MULT, level - 1));
                attackCooldown.setDuration(normalCd);
            }
        }

        // 3. COMMANDER DOMAIN (Level 5)
        if (level >= GameConfig.BASIC_DOMAIN_UNLOCK) {
            // PHASE 1: Domain is currently active in the world
            if (myDomain != null && myDomain.getWorld() != null) {
                domainIsReady = false; // Cannot be ready while domain is out
                domainRestTimer.stop(); // Keep rest timer paused
            } 
            else {
                // PHASE 2: Domain just vanished, start the 5 second rest
                if (!domainIsReady && !domainRestTimer.isActive() && !domainRestTimer.isExpired()) {
                    domainRestTimer.reset();
                    domainRestTimer.start();
                }
        
                // PHASE 3: Tick the rest timer
                domainRestTimer.update(world);
        
                if (domainRestTimer.isExpired()) {
                    domainIsReady = true;
                }
        
                // PHASE 4: If rested and ready, check for enemies to trigger
                if (domainIsReady) {
                    boolean enemyNearby = !getObjectsInRange(GameConfig.s(150), Enemy.class).isEmpty();
                    if (enemyNearby) {
                        myDomain = new DomainExpansion(GameConfig.s(160));
                        world.addObject(myDomain, getX(), getY());
                        
                        // Reset the rest timer for the next cycle
                        domainRestTimer.reset(); 
                        domainIsReady = false;
                    }
                }
            }
        }
        super.updateBehavior(world);
    }
    
    public void addStack() {
        stackCount++;
        double levelHPBoost = Math.pow(GameConfig.LEVEL_HP_MULT, this.level - 1);
        int unitBaseHPAtCurrentLevel = (int)(GameConfig.BASIC_UNIT_HP * levelHPBoost);
        
        int newMaxHP = (int)(unitBaseHPAtCurrentLevel * (stackCount * 0.6 + Math.sqrt(stackCount) * 0.4));
        
        this.health += (newMaxHP - this.maxHealth); 
        this.maxHealth = newMaxHP;
        
        updateVisual();
    }

    @Override
    public void updateVisual() {
        GreenfootImage img = UnitVisuals.draw(1, level, Color.GREEN);
        int size = img.getWidth();
        
        // --- DRAW THE SWARM DOTS ---
        int displayCount = Math.max(1, stackCount);
        int gridSide = (int)Math.ceil(Math.sqrt(displayCount));
        int dotSize = Math.max(2, (size / gridSide) - 2); 
        int offset = (size - (gridSide * (dotSize + 1))) / 2; 

        img.setColor(Color.GREEN);
        if (level == 5) img.setColor(Color.WHITE); 

        int drawn = 0;
        for (int row = 0; row < gridSide; row++) {
            for (int col = 0; col < gridSide; col++) {
                if (drawn >= stackCount) break;
                img.fillRect(offset + col * (dotSize + 1), offset + row * (dotSize + 1), dotSize, dotSize);
                drawn++;
            }
        }

        // --- RAGE VISUAL OVERLAY ---
        if (isRaged) {
            // 1. Crimson wash over the whole unit
            img.setColor(new Color(255, 0, 0, 80)); 
            img.fillRect(0, 0, size, size);
            
            // 2. Angry thick glowing red border
            img.setColor(Color.RED);
            img.drawRect(0, 0, size-1, size-1);
            img.drawRect(1, 1, size-3, size-3);
            img.drawRect(2, 2, size-5, size-5);
        }
        
        setImage(img);
        setNormalImage(img);
    }

    @Override
    protected void attack(Enemy target) {
        fireAt(target);
        if (level >= GameConfig.BASIC_SWARM_UNLOCK) {
            Enemy topTarget = findTargetInLane(laneIndex - 1);
            if (topTarget != null) fireAt(topTarget);
            
            Enemy bottomTarget = findTargetInLane(laneIndex + 1);
            if (bottomTarget != null) fireAt(bottomTarget);
        }
    }

    private void fireAt(Enemy target) {
        double linearPart = stackCount * 0.4;
        double curvePart = Math.sqrt(stackCount) * 0.6;
        int baseDmg = (int)(GameConfig.BASIC_UNIT_DAMAGE * (linearPart + curvePart));
        int totalDamage = (int)(baseDmg * Math.pow(GameConfig.LEVEL_DMG_MULT, level - 1));
        getWorld().addObject(new Projectile(target, totalDamage, null), getX(), getY());
    }

    private Enemy findTargetInLane(int laneToCheck) {
        if (laneToCheck < 0 || laneToCheck >= GameConfig.NUM_LANES) return null;
        Enemy furthest = null;
        int furthestX = Integer.MAX_VALUE;
        for (Enemy e : getWorld().getObjects(Enemy.class)) {
            if (e.getLaneIndex() == laneToCheck && !e.isDead() && e.getX() < furthestX) {
                furthest = e;
                furthestX = e.getX();
            }
        }
        return furthest;
    }
    
    public int getStackCount() { return stackCount; }
    @Override protected int getBaseHPFromConfig() { return GameConfig.BASIC_UNIT_HP; }
    
}