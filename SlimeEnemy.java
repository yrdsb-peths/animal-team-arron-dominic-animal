import greenfoot.*;

public class SlimeEnemy extends Enemy {
    public SlimeEnemy() {
        super(GameConfig.SLIME_ENEMY_HP, GameConfig.SLIME_ENEMY_DAMAGE, 
              GameConfig.SLIME_ENEMY_SPEED, GameConfig.SLIME_ENEMY_ATK_COOLDOWN, 0); 
        this.baseDrop = GameConfig.DROP_SLIME; 
        GreenfootImage img = new GreenfootImage(50, 50);
        img.setColor(new Color(0, 150, 0));
        img.fillOval(0, 0, 50, 50);
        setImage(img);
    }

    @Override
    protected void performAttack(Unit target) { target.takeDamage(this.damage); }

    @Override
    protected void handleDeath(MyWorld world) {
        CurrencyManager.earn(25);
        ScoreManager.addScore(100);

        int currentLane = this.getLaneIndex();

        // Dynamically spawn based on GameConfig
        for (int i = 0; i < GameConfig.SLIME_SPLIT_COUNT; i++) {
            // Clever math to alternate lanes: 0 (same), 1 (down), -1 (up), 2 (down 2), etc.
            // This spreads them out up and down automatically!
            int laneOffset = (i % 3 == 1) ? 1 : (i % 3 == 2) ? -1 : 0;
            
            int targetLane = currentLane + laneOffset;
            
            // Make sure we don't accidentally spawn them out of bounds!
            targetLane = Math.max(0, Math.min(GameConfig.NUM_LANES - 1, targetLane));

            MiniSlimeEnemy mini = new MiniSlimeEnemy(targetLane);
            
            // Stagger horizontally so they don't overlap 
            int offsetX = i * 20; 
            
            world.addObject(mini, (int)this.exactX + offsetX, LaneManager.getLaneY(targetLane));
        }

        world.removeObject(this);
    }
}