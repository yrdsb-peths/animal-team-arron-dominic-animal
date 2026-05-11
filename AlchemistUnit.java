import greenfoot.*;
public class AlchemistUnit extends Unit {
    public AlchemistUnit(int laneIndex, int colIndex) {
        super(GameConfig.ALCHEMIST_UNIT_HP, laneIndex, colIndex, GameConfig.ALCHEMIST_UNIT_COOLDOWN);
        updateVisual();
    }

    @Override
    public void updateVisual() {
        setImage(UnitVisuals.draw(4, level, Color.ORANGE));
        setNormalImage(getImage());
    }

    @Override
    protected void attack(Enemy target) {
        int scaledDmg = (int)(GameConfig.ALCHEMIST_UNIT_DAMAGE * Math.pow(GameConfig.LEVEL_DMG_MULT, level - 1));
        // We also scale the puddle tick damage slightly
        int puddleDmg = (int)(GameConfig.PUDDLE_TICK_DAMAGE * Math.pow(1.5, level - 1));
        
        getWorld().addObject(new SplashProjectile(target, scaledDmg, GameConfig.SPLASH_RADIUS), getX(), getY());
    }
 
    @Override protected int getBaseHPFromConfig() { return GameConfig.ALCHEMIST_UNIT_HP; }
}