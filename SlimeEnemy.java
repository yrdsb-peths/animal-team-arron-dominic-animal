import greenfoot.*;

public class SlimeEnemy extends Enemy {
    private int currentMaxHealth = -1;
    private GameTimer animTimer = new GameTimer(0.1, true); // Updates visual 10 FPS

    public SlimeEnemy() {
        super(GameConfig.SLIME_ENEMY_HP, GameConfig.SLIME_ENEMY_DAMAGE, 
              GameConfig.SLIME_ENEMY_SPEED, GameConfig.SLIME_ENEMY_ATK_COOLDOWN, 0); 
        this.baseDrop = GameConfig.DROP_SLIME; 
    }

    @Override
    protected void updateBehavior(MyWorld world) {
        super.updateBehavior(world); // Normal walk and attack logic
        
        // We capture the max health the first frame after WaveManager scales it
        if (currentMaxHealth == -1) currentMaxHealth = this.health;
        
        // Animate the pulse and color shift
        animTimer.update(world);
        if (animTimer.isExpired() || currentMaxHealth == this.health) {
            double hpPercent = Math.max(0.0, (double)this.health / currentMaxHealth);
            updateVisual(hpPercent);
        }
    }

    @Override
    protected void performAttack(Unit target) { target.takeDamage(this.damage); }

    @Override
    protected void handleDeath(MyWorld world) {
        CurrencyManager.earn(25);
        ScoreManager.addScore(100);

        int currentLane = this.getLaneIndex();

        for (int i = 0; i < GameConfig.SLIME_SPLIT_COUNT; i++) {
            int laneOffset = (i % 3 == 1) ? 1 : (i % 3 == 2) ? -1 : 0;
            int targetLane = currentLane + laneOffset;
            targetLane = Math.max(0, Math.min(GameConfig.NUM_LANES - 1, targetLane));

            MiniSlimeEnemy mini = new MiniSlimeEnemy(targetLane);
            int offsetX = i * 20; 
            world.addObject(mini, (int)this.exactX + offsetX, LaneManager.getLaneY(targetLane));
        }
        world.removeObject(this);
    }
    
    private void updateVisual(double hpPercent) {
        int size = 60; // Huge!
        GreenfootImage img = new GreenfootImage(size, size + 10);
        
        long time = System.currentTimeMillis();
        
        // 1. Pulsing Toxic Aura (Breathes in and out)
        int pulse = (int)(Math.sin(time / 150.0) * 4) + 4; 
        img.setColor(new Color(0, 255, 0, 80)); 
        img.fillOval(pulse, pulse, size - pulse*2, size - pulse*2);
        
        // 2. Color Shifting Body (Shifts between Green and Yellow/Orange)
        int r = (int)(Math.sin(time / 200.0) * 50) + 50; 
        img.setColor(new Color(r, 180, 0));
        img.fillOval(8, 8, size - 16, size - 16);
        
        // 3. Glowing Red Eyes (They pulse slightly too)
        int eyeSize = (int)(Math.sin(time / 100.0) * 2) + 8; 
        img.setColor(Color.RED);
        img.fillOval(18, 22, eyeSize, eyeSize); // Left eye
        img.fillOval(34, 22, eyeSize, eyeSize); // Right eye
        
        setImage(img);
    }
}