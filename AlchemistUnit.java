import greenfoot.*;

public class AlchemistUnit extends Unit {
    private int ventTimer = 0; 

    public AlchemistUnit(int laneIndex, int colIndex) {
        super(GameConfig.ALCHEMIST_UNIT_HP, laneIndex, colIndex, GameConfig.ALCHEMIST_UNIT_COOLDOWN);
        updateVisual();
    }

    @Override
    protected void attack(Enemy target) {
        int scaledDmg = (int)(GameConfig.ALCHEMIST_UNIT_DAMAGE * Math.pow(GameConfig.LEVEL_DMG_MULT, level - 1));
        getWorld().addObject(new SplashProjectile(target, scaledDmg, GameConfig.SPLASH_RADIUS, level), getX(), getY());
        
        // Trigger the pressure vent animation (flashes white/purple in UnitVisuals)
        ventTimer = 15; 
    }

    @Override
    protected void updateBehavior(MyWorld world) {
        super.updateBehavior(world);
        
        // 1. Tick down the attack animation timer
        if (ventTimer > 0) ventTimer--;

        // 2. THE FIX: Update visuals ALWAYS for Level 5
        // We do this every frame (or every 2nd frame) so the orbits and plasma 
        // beam in UnitVisuals.drawAlchemist can actually move.
        if (level == 5) {
            updateVisual(); 
        } 
        // For Lvl 1-4, you might only want to update when attacking to save CPU,
        // but if you want them to bubble/spin too, just remove the 'if' and call updateVisual()
        else if (ventTimer > 0) {
            updateVisual();
        }
    }

    @Override
    public void updateVisual() {
        // This pulls from UnitVisuals, which uses System.currentTimeMillis() 
        // to calculate where the "spinning" parts should be.
        setImage(UnitVisuals.drawAlchemist(level, ventTimer));
        setNormalImage(getImage());
    }
 
    @Override protected int getBaseHPFromConfig() { return GameConfig.ALCHEMIST_UNIT_HP; }
}