import greenfoot.*;

public class RailgunUnit extends Unit {
    public RailgunUnit(int laneIndex, int colIndex) {
        super(GameConfig.RAILGUN_UNIT_HP, laneIndex, colIndex, GameConfig.RAILGUN_UNIT_COOLDOWN);
        updateVisual();
    }

    @Override
    public void updateVisual() {
        setImage(UnitVisuals.draw(3, level, Color.CYAN));
        setNormalImage(getImage());
    }

    @Override
    protected void attack(Enemy target) {
        int scaledDmg = (int)(GameConfig.RAILGUN_UNIT_DAMAGE * Math.pow(GameConfig.LEVEL_DMG_MULT, level - 1));
        
        // UPDATED: Now passing 'level' as the second parameter
        getWorld().addObject(new PiercingProjectile(scaledDmg, level), getX(), getY());
    }
        
    @Override protected int getBaseHPFromConfig() { return GameConfig.RAILGUN_UNIT_HP; }
}