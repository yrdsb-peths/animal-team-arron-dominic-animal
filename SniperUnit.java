import greenfoot.*;

public class SniperUnit extends Unit {

    public SniperUnit(int laneIndex, int colIndex) {
        // Use the stats from GameConfig
        super(GameConfig.SNIPER_UNIT_HP, laneIndex, colIndex, GameConfig.SNIPER_UNIT_COOLDOWN);
        
        // Give it a visual (A purple rectangle)
        GreenfootImage img = new GreenfootImage(40, 40);
        img.setColor(Color.MAGENTA);
        img.fillRect(0, 0, 40, 40);
        setImage(img);
    }

    @Override
    protected void attack(Enemy target) {
        // Fire a projectile that does Sniper damage!
        StatusEffect iceSlow = EffectFactory.createSlow(
            GameConfig.SNIPER_SLOW_DURATION, 
            GameConfig.SNIPER_SLOW_POWER
        );
        getWorld().addObject(new Projectile(target, GameConfig.SNIPER_UNIT_DAMAGE,iceSlow), getX(), getY());
    }
}