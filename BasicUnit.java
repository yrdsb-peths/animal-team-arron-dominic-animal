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
    private int droneRotation = 0; // Animates the Lvl 2 Drones
    private double animTimer = 0;

    public BasicUnit(int laneIndex, int colIndex) {
        super(GameConfig.BASIC_UNIT_HP, laneIndex, colIndex, GameConfig.BASIC_UNIT_COOLDOWN);
        this.stackCount = 1; 
        // CRITICAL: You must start the timer or it stays at 10.0 forever!
        domainTimer.start(); 
        updateVisual();
    }
    
    @Override
    protected void updateBehavior(MyWorld world) {
        // 1. RADAR CHECK
        List<Enemy> neighbors = getObjectsInRange(GameConfig.s(120), Enemy.class);
        boolean enemyInPersonalSpace = !neighbors.isEmpty();
    
        // 2. RAGE STATE TOGGLE (No more setDuration!)
        if (level >= GameConfig.BASIC_RAGE_UNLOCK) {
            if (enemyInPersonalSpace && !isRaged) {
                isRaged = true;
                updateVisual();
            } else if (!enemyInPersonalSpace && isRaged) {
                isRaged = false;
                updateVisual();
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
        
                /// PHASE 3: Tick the rest timer
                domainRestTimer.update(world);
            
                if (domainRestTimer.isExpired()) {
                    domainIsReady = true;
                }
            
                // PHASE 4: Trigger Logic
                if (domainIsReady) {
                    // A. Check for enemy (The "Panic" Trigger)
                    boolean enemyNearby = !getObjectsInRange(GameConfig.s(150), Enemy.class).isEmpty();
                    
                    // B. Check the 10-second timer (The "Automatic" Trigger)
                    domainTimer.update(world);
            
                    if (enemyNearby || domainTimer.isExpired()) {
                        myDomain = new DomainExpansion(GameConfig.s(160));
                        world.addObject(myDomain, getX(), getY());
                        
                        // Clean up for next cycle
                        domainRestTimer.reset(); 
                        domainTimer.reset(); // Reset the 10s automatic timer
                        domainIsReady = false;
                        
                        if (enemyNearby) System.out.println("!!! DOMAIN: TRIGGERED BY ENEMY");
                        else System.out.println("!!! DOMAIN: TRIGGERED AUTOMATICALLY");
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
        // Get the core chassis we designed above
        GreenfootImage img = UnitVisuals.draw(1, level, Color.GREEN);
        int c = img.getWidth() / 2;
        int b = GameConfig.UNIT_SIZE / 2;
        animTimer += 0.1;
    
        // --- LEVEL 2: ORBITAL DEFENSE DRONES ---
        if (level >= 2) {
            // We move the drones in a wide figure-eight outside the unit
            int dx = (int)(Math.sin(animTimer) * 35);
            int dy = (int)(Math.cos(animTimer * 0.5) * 15);
            
            drawCoolDrone(img, c + dx, c + dy - 25); // Top Drone
            drawCoolDrone(img, c - dx, c - dy + 25); // Bottom Drone
        }
    
        // --- LEVEL 3: RAGE OVERLOAD ---
        if (isRaged) {
            // Pulse a red "Warning" ring around the unit
            int pulse = (int)(Math.abs(Math.sin(animTimer * 2)) * 20);
            img.setColor(new Color(255, 0, 0, 150 - pulse * 5));
            img.drawOval(c - b - pulse, c - b - pulse, (b*2) + pulse*2, (b*2) + pulse*2);
            
            // Red Hot Core
            img.setColor(new Color(255, 50, 50));
            img.fillOval(c-6, c-6, 12, 12);
        }
        
        // --- LEVEL 4: NANO-HEX SHIELD ---
        if (level >= 4) {
            img.setColor(new Color(0, 255, 255, 40));
            // Draw a faint hexagonal grid over the chassis
            for(int i=0; i<3; i++) {
                img.drawRect(c-15 + i*10, c-15, 5, 30);
                img.drawRect(c-15, c-15 + i*10, 30, 5);
            }
        }
    
        setImage(img);
        setNormalImage(img);
    }
    
    @Override
    protected void attack(Enemy dummyTarget) {
        // 1. Always fire at the primary lane
        Enemy center = findTargetInLane(laneIndex);
        if (center != null) fireAt(center);
    
        // 2. Fire at adjacent lanes if Level 2+
        if (level >= GameConfig.BASIC_SWARM_UNLOCK) {
            Enemy top = findTargetInLane(laneIndex - 1);
            if (top != null) fireAt(top);
            
            Enemy bottom = findTargetInLane(laneIndex + 1);
            if (bottom != null) fireAt(bottom);
        }
    }

    private void fireAt(Enemy target) {
        double linearPart = stackCount * 0.4;
        double curvePart = Math.sqrt(stackCount) * 0.6;
        int baseDmg = (int)(GameConfig.BASIC_UNIT_DAMAGE * (linearPart + curvePart));
        int totalDamage = (int)(baseDmg * Math.pow(GameConfig.LEVEL_DMG_MULT, level - 1));
        getWorld().addObject(new Projectile(target, totalDamage, null), getX(), getY());
    }

        // Rename this to match the one I used in findTarget or update it:
    private Enemy findTargetInLane(int laneToCheck) {
        if (laneToCheck < 0 || laneToCheck >= GameConfig.NUM_LANES) return null;
        
        Enemy furthest = null;
        int furthestX = Integer.MAX_VALUE;
        
        // Scan all enemies to find the one closest to the base in THIS specific lane
        for (Enemy e : getWorld().getObjects(Enemy.class)) {
            if (e.getLaneIndex() == laneToCheck && !e.isDead()) {
                if (e.getX() < furthestX) {
                    furthest = e;
                    furthestX = e.getX();
                }
            }
        }
        return furthest;
    }
    /** 
     * Overriding findTarget so the Basic Unit "wakes up" if an enemy 
     * is in ANY of its valid lanes (Own, Top, or Bottom).
     */
    @Override
    protected Enemy findTarget() {
        // If Level 1, only look at our own lane (standard behavior)
        if (level < GameConfig.BASIC_SWARM_UNLOCK) {
            return super.findTarget();
        }
    
        // If Level 2+, we scan all 3 lanes to see if we should start firing
        Enemy bestTarget = null;
        int closestX = Integer.MAX_VALUE;
    
        // Scan Lane-1, Lane, and Lane+1
        for (int offset = -1; offset <= 1; offset++) { 
            int checkLane = laneIndex + offset;
            
            // Safety check to stay inside world lanes (0-4)
            if (checkLane >= 0 && checkLane < GameConfig.NUM_LANES) {
                Enemy e = findTargetInLane(checkLane);
                if (e != null && e.getX() < closestX) {
                    closestX = e.getX();
                    bestTarget = e;
                }
            }
        }
        
        // If this returns an enemy, updateBehavior will trigger the attack() method
        return bestTarget; 
    }
    
    private void drawCoolDrone(GreenfootImage img, int x, int y) {
        // Drone is a "Diamond" shape with a white wing
        img.setColor(new Color(50, 50, 60));
        int[] px = {x, x+8, x, x-8};
        int[] py = {y-5, y, y+5, y};
        img.fillPolygon(px, py, 4);
        
        img.setColor(Color.CYAN);
        img.drawPolygon(px, py, 4);
        
        // Tiny engine flare
        img.setColor(Color.WHITE);
        img.fillRect(x-2, y-2, 4, 4);
    }

    public int getStackCount() { return stackCount; }
    
    @Override protected int getBaseHPFromConfig() { return GameConfig.BASIC_UNIT_HP; }
    
    @Override
    public boolean isSelfRaged() {
        return isRaged;
    }
    
}