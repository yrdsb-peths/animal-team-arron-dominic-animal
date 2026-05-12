import greenfoot.*;
import java.util.List;

public class BasicUnit extends Unit {
    private int stackCount = 1;
    private boolean isRaged = false;
    private GameTimer domainTimer = new GameTimer(10.0, true); // Pulses every 10s
    private GameTimer domainActiveTimer = new GameTimer(4.0, false); 

    public BasicUnit(int laneIndex, int colIndex) {
        super(GameConfig.BASIC_UNIT_HP, laneIndex, colIndex, GameConfig.BASIC_UNIT_COOLDOWN);
        this.stackCount = 1; 
        updateVisual();
    }
    
    @Override
    protected void updateBehavior(MyWorld world) {
        // 1. RADAR CHECK (1-unit radius = roughly 100 pixels)
        List<Enemy> neighbors = getObjectsInRange(GameConfig.s(100), Enemy.class);
        boolean enemyInPersonalSpace = !neighbors.isEmpty();

        // 2. RAGE MODE (Level 3+)
        if (level >= GameConfig.BASIC_RAGE_UNLOCK) {
            if (enemyInPersonalSpace && !isRaged) {
                isRaged = true;
                world.addObject(new RageAura(this), getX(), getY()); 
            } else if (!enemyInPersonalSpace && isRaged) {
                isRaged = false;
            }
        }

        // 3. COMMANDER DOMAIN (Level 5)
        if (level >= GameConfig.BASIC_DOMAIN_UNLOCK) {
            domainTimer.update(world);
            if (domainTimer.isExpired() && enemyInPersonalSpace) {
                domainActiveTimer.reset();
                domainActiveTimer.start();
                world.addObject(new DomainExpansion(GameConfig.s(120)), getX(), getY());
            }
            domainActiveTimer.update(world);
        }

        // 4. ATTACK SPEED LOGIC
        // If Raged, we force-tick the cooldown an extra time (50% faster)
        if (isRaged) attackCooldown.forceTick();
        
        super.updateBehavior(world);
    }
    
    public void addStack() {
        stackCount++;
        
        // Calculate the base HP for a single unit AT THIS LEVEL
        double levelHPBoost = Math.pow(GameConfig.LEVEL_HP_MULT, this.level - 1);
        int unitBaseHPAtCurrentLevel = (int)(GameConfig.BASIC_UNIT_HP * levelHPBoost);
        
        // Use that boosted base HP to calculate the new stack total
        int newMaxHP = (int)(unitBaseHPAtCurrentLevel * (stackCount * 0.6 + Math.sqrt(stackCount) * 0.4));
        
        this.health += (newMaxHP - this.maxHealth); // Heal by the difference
        this.maxHealth = newMaxHP;
        
        updateVisual();
    }

    @Override
    public void updateVisual() {
        // Get the tactical background frame from UnitVisuals
        GreenfootImage img = UnitVisuals.draw(1, level, Color.GREEN);
        int size = img.getWidth();
        
        // Draw the Swarm Dots on top of the frame
        int displayCount = Math.max(1, stackCount);
        int gridSide = (int)Math.ceil(Math.sqrt(displayCount));
        int dotSize = Math.max(2, (size / gridSide) - 2); 
        int offset = (size - (gridSide * (dotSize + 1))) / 2; // Center the dots

        img.setColor(Color.GREEN);
        if (level == 5) img.setColor(Color.WHITE); // Elite troops at Lvl 5

        int drawn = 0;
        for (int row = 0; row < gridSide; row++) {
            for (int col = 0; col < gridSide; col++) {
                if (drawn >= stackCount) break;
                img.fillRect(offset + col * (dotSize + 1), offset + row * (dotSize + 1), dotSize, dotSize);
                drawn++;
            }
        }
        
        setImage(img);
        setNormalImage(img);
    }

    @Override
    protected void attack(Enemy target) {
        // Attack the main target
        fireAt(target);

        // SWARM MECHANIC: Also fire at adjacent lanes if Level 2+
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
    
    public int getStackCount()
    {
        return stackCount;
    }
    
    @Override protected int getBaseHPFromConfig() { return GameConfig.BASIC_UNIT_HP; }
}