import greenfoot.*;
import java.util.List;

public class BasicUnit extends Unit {
    private int stackCount = 1;
    private boolean isRaged = false;
    private GameTimer domainTimer = new GameTimer(10.0, true); 
    private DomainExpansion myDomain = null; 
    private GameTimer domainRestTimer = new GameTimer(7.0, false); 
    private boolean domainIsReady = true;

    // --- ANIMATION CLOCK ---
    private float orbitAngle = 0;

    public BasicUnit(int laneIndex, int colIndex) {
        super(GameConfig.BASIC_UNIT_HP, laneIndex, colIndex, GameConfig.BASIC_UNIT_COOLDOWN);
        this.stackCount = 1; 
        domainTimer.start(); 
        updateVisual();
    }
    
    @Override
    protected void updateBehavior(MyWorld world) {
        // --- ANIMATION HOOK ---
        // Speed increases with level and Rage
        float speedMult = (float)(1.0 + (level * 0.5));
        if (isRaged) speedMult *= 2.5f;
        orbitAngle += speedMult * GameConfig.GAME_SPEED;
        updateVisual(); 
        // ----------------------

        List<Enemy> neighbors = getObjectsInRange(GameConfig.s(120), Enemy.class);
        boolean enemyInPersonalSpace = !neighbors.isEmpty();
    
        if (level >= GameConfig.BASIC_RAGE_UNLOCK) {
            if (enemyInPersonalSpace && !isRaged) isRaged = true;
            else if (!enemyInPersonalSpace && isRaged) isRaged = false;
        }

        if (level >= GameConfig.BASIC_DOMAIN_UNLOCK) {
            if (myDomain != null && myDomain.getWorld() != null) {
                domainIsReady = false; 
                domainRestTimer.stop(); 
            } else {
                if (!domainIsReady && !domainRestTimer.isActive() && !domainRestTimer.isExpired()) {
                    domainRestTimer.reset();
                    domainRestTimer.start();
                }
                domainRestTimer.update(world);
                if (domainRestTimer.isExpired()) domainIsReady = true;
                if (domainIsReady) {
                    boolean enemyNearby = !getObjectsInRange(GameConfig.s(150), Enemy.class).isEmpty();
                    domainTimer.update(world);
                    if (enemyNearby || domainTimer.isExpired()) {
                        myDomain = new DomainExpansion(GameConfig.s(160));
                        world.addObject(myDomain, getX(), getY());
                        domainRestTimer.reset(); 
                        domainTimer.reset();
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
    }

    /**
     * THE EVOLUTION ENGINE
     * Lvl 1-2: Solid & Heavy
     * Lvl 3-4: The Swarm
     * Lvl 5: The Ultimate Divine
     */
    @Override
    public void updateVisual() {
        int size = GameConfig.s(90); // Increased size to allow for massive orbits
        GreenfootImage img = new GreenfootImage(size, size);
        int center = size / 2;
        
        // LEVITATION: Jesus-style bobbing (Exaggerated)
        float bobFreq = isRaged ? 5.0f : 2.5f;
        int bob = (int)(Math.sin(Math.toRadians(orbitAngle * bobFreq)) * 7);
        
        // THEME: Neon Green identity
        Color theme = new Color(0, 255, 100); 
        if (level == 4) theme = new Color(150, 255, 150); // Divine White-Green
        if (isRaged) theme = Color.RED;
    
        switch(level) {
            case 1: // THE SEED
                img.setColor(new Color(20, 60, 20));
                img.fillRect(center-15, center-15+bob, 30, 30);
                img.setColor(theme);
                img.drawRect(center-15, center-15+bob, 29, 29);
                img.fillOval(center-3, center-3+bob, 6, 6);
                break;
    
            case 2: // THE REINFORCED
                img.setColor(new Color(40, 40, 40));
                img.fillRect(center-18, center-18+bob, 36, 36);
                img.setColor(theme);
                img.drawRect(center-18, center-18+bob, 35, 35);
                img.fillRect(center-24, center-4+bob, 6, 8);
                img.fillRect(center+18, center-4+bob, 6, 8);
                img.fillOval(center-4, center-4+bob, 8, 8);
                break;
    
            case 3: // THE DUAL-HEALIX SWARM (Exaggerated)
                // Core: Pulsing Triangle
                drawPolygon(img, center, center+bob, 12, 3, orbitAngle, theme);
                
                for (int i = 0; i < stackCount; i++) {
                    double baseA = i * (360.0 / stackCount);
                    
                    // Orbit 1: Fast, Elliptical (Horizontal)
                    double a1 = Math.toRadians(orbitAngle * 2.5 + baseA);
                    int x1 = (int)(center + Math.cos(a1) * 30);
                    int y1 = (int)(center + Math.sin(a1) * 15) + bob; // Flattened Y
                    
                    // Orbit 2: Slower, Elliptical (Vertical)
                    double a2 = Math.toRadians(-orbitAngle * 1.5 + baseA + 180);
                    int x2 = (int)(center + Math.cos(a2) * 15);
                    int y2 = (int)(center + Math.sin(a2) * 35) + bob; // Flattened X
    
                    img.setColor(theme);
                    img.fillRect(x1-2, y1-2, 5, 5);
                    img.fillRect(x2-2, y2-2, 5, 5);
                    
                    // Energy Web: Connect drones in the same orbit
                    img.setColor(new Color(theme.getRed(), theme.getGreen(), theme.getBlue(), 60));
                    img.drawLine(x1, y1, x2, y2);
                }
                break;
    
            case 4: // THE ULTIMATE: Divine Mandala / Nano-Fortress
                // 1. Radiant Aura
                int pulse = 30 + (int)(Math.sin(Math.toRadians(orbitAngle * 3)) * 10);
                img.setColor(new Color(255, 255, 255, 40));
                img.fillOval(center-pulse/2, center-pulse/2+bob, pulse, pulse);
                
                // 2. The Divine Core (Diamond)
                img.setColor(Color.WHITE);
                int[] dxs = {center, center+12, center, center-12};
                int[] dys = {center-12+bob, center+bob, center+12+bob, center+bob};
                img.fillPolygon(dxs, dys, 4);
                
                // 3. The Great Halo (Outer rotating runic ring)
                img.setColor(theme);
                img.drawOval(center-28, center-28+bob, 56, 56);
                for(int j=0; j<8; j++) {
                    double a = Math.toRadians(-orbitAngle * 0.5 + (j*45));
                    int rx = (int)(center + Math.cos(a) * 28);
                    int ry = (int)(center + Math.sin(a) * 28) + bob;
                    img.fillOval(rx-3, ry-3, 6, 6);
                }
                
                // 4. The Stack Drones (Inner High-Speed Swarm)
                for (int i = 0; i < stackCount; i++) {
                    double angle = Math.toRadians(orbitAngle * 2 + (i * (360.0 / stackCount)));
                    int x = (int)(center + Math.cos(angle) * 18);
                    int y = (int)(center + Math.sin(angle) * 18) + bob;
                    img.setColor(Color.WHITE);
                    img.fillRect(x-2, y-2, 4, 4);
                }
                break;
        }
    
        setImage(img);
        setNormalImage(img);
    }

    /** Helper to draw rotating shapes for cores */
    private void drawPolygon(GreenfootImage img, int x, int y, int radius, int sides, float rotation, Color color) {
        int[] px = new int[sides];
        int[] py = new int[sides];
        for (int i = 0; i < sides; i++) {
            double angle = Math.toRadians(rotation + (i * (360.0 / sides)));
            px[i] = x + (int)(Math.cos(angle) * radius);
            py[i] = y + (int)(Math.sin(angle) * radius);
        }
        img.setColor(color);
        img.fillPolygon(px, py, sides);
    }

    @Override
    protected void attack(Enemy dummyTarget) {
        Enemy center = findTargetInLane(laneIndex);
        if (center != null) fireAt(center);
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

    private Enemy findTargetInLane(int laneToCheck) {
        if (laneToCheck < 0 || laneToCheck >= GameConfig.NUM_LANES) return null;
        Enemy furthest = null;
        int furthestX = Integer.MAX_VALUE;
        for (Enemy e : getWorld().getObjects(Enemy.class)) {
            if (e.getLaneIndex() == laneToCheck && !e.isDead() && e.getX() < furthestX) {
                furthest = e; furthestX = e.getX();
            }
        }
        return furthest;
    }

    @Override
    protected Enemy findTarget() {
        if (level < GameConfig.BASIC_SWARM_UNLOCK) return super.findTarget();
        Enemy bestTarget = null;
        int closestX = Integer.MAX_VALUE;
        for (int offset = -1; offset <= 1; offset++) { 
            int checkLane = laneIndex + offset;
            if (checkLane >= 0 && checkLane < GameConfig.NUM_LANES) {
                Enemy e = findTargetInLane(checkLane);
                if (e != null && e.getX() < closestX) {
                    closestX = e.getX(); bestTarget = e;
                }
            }
        }
        return bestTarget; 
    }
    /** Helper to draw a non-filled geometric wireframe */
    private void drawWireframeShape(GreenfootImage img, int x, int y, int radius, int sides, float rotation, Color color) {
        int[] px = new int[sides];
        int[] py = new int[sides];
        for (int i = 0; i < sides; i++) {
            double angle = Math.toRadians(rotation + (i * (360.0 / sides)));
            px[i] = x + (int)(Math.cos(angle) * radius);
            py[i] = y + (int)(Math.sin(angle) * radius);
        }
        img.setColor(color);
        img.drawPolygon(px, py, sides);
        // Draw internal lines to the center to make it look 3D
        for (int i = 0; i < sides; i++) {
            img.drawLine(x, y, px[i], py[i]);
        }
    }
    
    public int getStackCount() { return stackCount; }
    @Override protected int getBaseHPFromConfig() { return GameConfig.BASIC_UNIT_HP; }
    @Override public boolean isSelfRaged() { return isRaged; }
}