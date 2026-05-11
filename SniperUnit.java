import greenfoot.*;

public class SniperUnit extends Unit {
    public SniperUnit(int laneIndex, int colIndex) {
        super(GameConfig.SNIPER_UNIT_HP, laneIndex, colIndex, GameConfig.SNIPER_UNIT_COOLDOWN);
        updateVisual();
    }

    @Override
    public void updateVisual() {
        setImage(UnitVisuals.draw(2, level, Color.MAGENTA));
        setNormalImage(getImage());
    }

    @Override
    protected void attack(Enemy target) {
        int scaledDmg = (int)(GameConfig.SNIPER_UNIT_DAMAGE * Math.pow(GameConfig.LEVEL_DMG_MULT, level - 1));
        StatusEffect iceSlow = EffectFactory.createSlow(GameConfig.SNIPER_SLOW_DURATION, GameConfig.SNIPER_SLOW_POWER);
        getWorld().addObject(new Projectile(target, scaledDmg, iceSlow), getX(), getY());
    }
        
    @Override protected int getBaseHPFromConfig() { return GameConfig.SNIPER_UNIT_HP; }
}