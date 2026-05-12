import greenfoot.*;

public class WallUnit extends Unit {
    protected int currentStage = 3;
    protected GameTimer healTimer = new GameTimer(1.0, true);

    public WallUnit(int laneIndex, int colIndex) {
        super(GameConfig.WALL_UNIT_HP, laneIndex, colIndex, 999.0); 
        healTimer.start(); // FIX: The timer was never started!
        updateVisual();
    }

    @Override
    public void takeDamage(int amount) {
        super.takeDamage(amount); 
        float hpPercent = (float)health / maxHealth;
        int newStage = (hpPercent > 0.66f) ? 3 : (hpPercent > 0.33f) ? 2 : 1;

        if (newStage != currentStage) {
            currentStage = newStage;
            updateVisual();
        }
    }
    
    @Override
    public void takeDamage(int amount, Enemy attacker) {
        super.takeDamage(amount, attacker);
        
        float hpPercent = (float)health / maxHealth;
        int newStage = (hpPercent > 0.66f) ? 3 : (hpPercent > 0.33f) ? 2 : 1;

        if (newStage != currentStage) {
            currentStage = newStage;
            updateVisual();
        }
        
        if (level >= GameConfig.WALL_THORNS_UNLOCK && attacker != null && !attacker.isDead()) {
            MyWorld world = (MyWorld)getWorld();
            int wave = world.getGSM().getWaveNumber();
            int retaliationDmg = (GameConfig.WALL_THORN_BASE_DMG * level) + (wave * 10) + (int)(amount * GameConfig.WALL_THORN_MULTIPLIER);
            
            attacker.takeDamage(retaliationDmg, true); 
            world.addObject(new FloatingText("-" + retaliationDmg, Color.ORANGE, 16, 2, 20), attacker.getX(), attacker.getY());
            world.addObject(new BlockSpark(Color.RED), attacker.getX(), attacker.getY());
        }
    }

    @Override
    protected void updateBehavior(MyWorld world) {
        super.updateBehavior(world);
        
        // Only run healing if this specific unit allows it
        if (shouldApplyHealing()) {
            handleHealingLogic(world);
        }
    }

    /** Small Wall allows healing at Level 4/5. Big Wall will override this to say 'false'. */
    protected boolean shouldApplyHealing() {
        return level >= GameConfig.WALL_HEAL_UNLOCK;
    }

    /** The actual visual and math logic for healing */
    private void handleHealingLogic(MyWorld world) {
        healTimer.update(world);
        if (healTimer.isExpired() && health < maxHealth) {
            int healAmt = (int)(maxHealth * GameConfig.WALL_HEALING_AMOUNT/100.0); 
            health = Math.min(maxHealth, health + healAmt);
            
            // Visuals
            int w = getImage().getWidth();
            int h = getImage().getHeight();
            world.addObject(new WallHealPulse(w, h), getX(), getY());
            
            for(int i=0; i < 4; i++) {
                int rx = getX() + (GameRNG.getRandomNumber(w) - w/2);
                int ry = getY() + (h/2);
                world.addObject(new RepairBit(), rx, ry);
            }
            updateVisual(); 
        }
    }

    @Override
    public void die() {
        if (isDead) return;
        
        if (this instanceof BigWallUnit) {
            if (level >= GameConfig.BIG_WALL_EXPLODE_UNLOCK) {
                getWorld().addObject(new Explosion(5000), getX(), getY());
            }
        } else {
            if (level >= GameConfig.WALL_EXPLODE_UNLOCK) {
                getWorld().addObject(new Explosion(2000), getX(), getY());
            }
        }
        super.die();
    }

    @Override
    public void updateVisual() {
        int size = 45;
        GreenfootImage img = new GreenfootImage(size, size);
        
        int wallH = (size / 3) * currentStage;
        int yOff = size - wallH;
        Color lvlColor = UnitVisuals.getLevelColor(level);

        Color primary = new Color(100, 70, 40); 
        if (level == 2) primary = new Color(120, 120, 120); 
        if (level == 3) primary = new Color(70, 90, 130);   
        if (level == 4) primary = new Color(40, 20, 60);    
        if (level == 5) primary = new Color(220, 200, 100); 

        img.setColor(primary);
        img.fillRect(2, yOff, size-4, wallH);

        img.setColor(new Color(0, 0, 0, 150));
        for (int i = 1; i < currentStage; i++) {
            int lineY = yOff + (i * (wallH / currentStage));
            img.drawLine(2, lineY, size-3, lineY);
        }
        for (int i = 0; i < currentStage; i++) {
            int rowY = yOff + (i * (wallH / currentStage));
            if (i % 2 == 0) img.drawLine(size/2, rowY, size/2, rowY + (wallH/currentStage));
            else {
                img.drawLine(size/4, rowY, size/4, rowY + (wallH/currentStage));
                img.drawLine(3*size/4, rowY, 3*size/4, rowY + (wallH/currentStage));
            }
        }

        img.setColor(lvlColor);
        if (level >= 2) img.drawRect(2, yOff, size-5, wallH-1); 
        
        if (level >= 3) { 
            img.setColor(new Color(200, 200, 255, 100));
            img.fillRect(8, yOff, 4, wallH);
            img.fillRect(size-12, yOff, 4, wallH);
        }
        if (level >= 4) { 
            img.setColor(lvlColor);
            img.drawOval(size/2-5, yOff + wallH/2-5, 10, 10);
            img.fillOval(size/2-2, yOff + wallH/2-2, 4, 4);
        }
        if (level == 5) { 
            int[] sx = {5, 10, 15, 20, 25, 30, 35, 40};
            for(int x : sx) {
                img.fillPolygon(new int[]{x-3, x, x+3}, new int[]{yOff, yOff-6, yOff}, 3);
            }
            img.setColor(Color.WHITE);
            img.drawRect(size/2-8, yOff+4, 16, wallH-8);
        }

        setImage(img);
        setNormalImage(img);
    }

    @Override protected void attack(Enemy target) {}
    
    @Override protected int getBaseHPFromConfig() { return GameConfig.WALL_UNIT_HP; }
}