import greenfoot.*;

public class AlchemistUnit extends Unit {

    public AlchemistUnit(int laneIndex, int colIndex) {
        super(GameConfig.ALCHEMIST_UNIT_HP, laneIndex, colIndex, GameConfig.ALCHEMIST_UNIT_COOLDOWN);
        
        // Visual: An Orange circle
        GreenfootImage img = new GreenfootImage(40, 40);
        img.setColor(Color.ORANGE);
        img.fillOval(0, 0, 40, 40);
        setImage(img);
    }

    @Override
    protected void attack(Enemy target) {
        // Fires the Splash Projectile, which will also drop the puddle on impact!
        getWorld().addObject(new SplashProjectile(target, GameConfig.ALCHEMIST_UNIT_DAMAGE, GameConfig.SPLASH_RADIUS), getX(), getY());
    }
}