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
        
        // UPDATED: Now passes 'level' as the 4th parameter!
        getWorld().addObject(new SplashProjectile(target, scaledDmg, GameConfig.SPLASH_RADIUS, level), getX(), getY());
    }
 
    @Override protected int getBaseHPFromConfig() { return GameConfig.ALCHEMIST_UNIT_HP; }
}