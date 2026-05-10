import greenfoot.*;

public class KamikazeEnemy extends Enemy {
    public KamikazeEnemy() {
        super(GameConfig.KAMIKAZE_ENEMY_HP, GameConfig.KAMIKAZE_ENEMY_DAMAGE, 
              GameConfig.KAMIKAZE_ENEMY_SPEED, GameConfig.KAMIKAZE_ENEMY_ATK_COOLDOWN, 0); 
              this.baseDrop = GameConfig.DROP_KAMIKAZE;
        // Visual: A spiky red danger orb
        GreenfootImage img = new GreenfootImage(30, 30);
        img.setColor(Color.RED);
        img.fillOval(5, 5, 20, 20);
        img.setColor(Color.ORANGE);
        img.fillRect(10, 0, 10, 30); // Spikes
        img.fillRect(0, 10, 30, 10);
        setImage(img);
    }

    @Override
    protected void performAttack(Unit target) {
        // If it's a wall, do MASSIVE damage. Otherwise, do normal kamikaze damage.
        if (target instanceof WallUnit || target instanceof BigWallUnit) {
            target.takeDamage(GameConfig.KAMIKAZE_WALL_DAMAGE);
        } else {
            target.takeDamage(GameConfig.KAMIKAZE_ENEMY_DAMAGE);
        }
        
        // Kamikaze! The wall KAMIKAZE explodes and dies immediately.
        this.takeDamage(9999, true); 
    }

    @Override
    protected void handleDeath(MyWorld world) {
        ScoreManager.addScore(25); // Low score, he killed himself!
        world.removeObject(this);
    }
}