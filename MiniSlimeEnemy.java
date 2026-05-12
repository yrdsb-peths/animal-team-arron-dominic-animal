import greenfoot.*;

public class MiniSlimeEnemy extends Enemy {
    public MiniSlimeEnemy(int targetLane) {
        super(GameConfig.MINISLIME_ENEMY_HP, GameConfig.MINISLIME_ENEMY_DAMAGE, 
              GameConfig.MINISLIME_ENEMY_SPEED, GameConfig.MINISLIME_ENEMY_ATK_COOLDOWN, targetLane); 
              this.baseDrop = GameConfig.DROP_MINISLIME;
        GreenfootImage img = new GreenfootImage(20, 20);
        img.setColor(Color.GREEN);
        img.fillOval(0, 0, 20, 20);
        setImage(img);
    }

    @Override
    protected void handleDeath(MyWorld world) {
        CurrencyManager.earn(5); 
        ScoreManager.addScore(20);

        // THE DOUBLE SPLIT!
        for (int i = 0; i < GameConfig.MINISLIME_SPLIT_COUNT; i++) {
            MicroSlimeEnemy micro = new MicroSlimeEnemy(this.getLaneIndex());
            
            // Stagger them horizontally backwards (+15 pixels per slime)
            int offsetX = i * 15; 
            
            world.addObject(micro, (int)this.exactX + offsetX, LaneManager.getLaneY(this.getLaneIndex()));
        }

        world.removeObject(this);
    }
}