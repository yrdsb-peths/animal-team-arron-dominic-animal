import greenfoot.*;

public class SniperUnit extends Unit {
    private int animTimer = 0; // Tracks the "recoil" animation

    public SniperUnit(int laneIndex, int colIndex) {
        super(GameConfig.SNIPER_UNIT_HP, laneIndex, colIndex, GameConfig.SNIPER_UNIT_COOLDOWN);
        updateVisual();
    }

    @Override
    protected void attack(Enemy target) {
        int scaledDmg = (int)(GameConfig.SNIPER_UNIT_DAMAGE * Math.pow(GameConfig.LEVEL_DMG_MULT, level - 1));
        float debuff = (level >= GameConfig.SNIPER_DEBUFF_UNLOCK) ? GameConfig.SNIPER_FREEZE_WEAKNESS : 1.0f;
        
        StatusEffect iceSlow = EffectFactory.createSlow(GameConfig.SNIPER_SLOW_DURATION, GameConfig.SNIPER_SLOW_POWER, debuff);
        getWorld().addObject(new Projectile(target, scaledDmg, iceSlow), getX(), getY());

        // TRIGGER ANIMATION
        animTimer = 10; 
    }

    @Override
    public void updateBehavior(MyWorld world) {
        super.updateBehavior(world);
        if (animTimer > 0) {
            animTimer--;
            updateVisual(); // Redraw with recoil/flash
        }
    }

    @Override
    public void updateVisual() {
        // We pass the animTimer to the visual drawer to handle the kickback
        setImage(UnitVisuals.drawSniper(level, animTimer));
        setNormalImage(getImage());
    }
        
    @Override protected int getBaseHPFromConfig() { return GameConfig.SNIPER_UNIT_HP; }
}