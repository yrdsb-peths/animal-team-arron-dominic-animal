import greenfoot.*;

public class MicroSlimeEnemy extends Enemy {
    public MicroSlimeEnemy(int targetLane) {
        super(GameConfig.MICRO_SLIME_HP, GameConfig.MICRO_SLIME_DAMAGE, 
              GameConfig.MICRO_SLIME_SPEED, GameConfig.MICRO_SLIME_ATK_COOLDOWN, targetLane); 
               this.baseDrop = GameConfig.DROP_MICROSLIME; 
        // Visual: Very tiny dark green circle
        GreenfootImage img = new GreenfootImage(10, 10);
        img.setColor(new Color(0, 100, 50)); 
        img.fillOval(0, 0, 10, 10);
        setImage(img);
    }

    @Override
    protected void handleDeath(MyWorld world) {
        CurrencyManager.earn(1); 
        ScoreManager.addScore(5);
        world.removeObject(this); // The swarm ends here!
    }
}