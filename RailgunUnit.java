// ==================================================
// FILE: ./RailgunUnit.java
// ==================================================
import greenfoot.*;

public class RailgunUnit extends Unit {
    private GameTimer superLaserCooldown;
    private boolean isUltimateReady = false;

    public RailgunUnit(int laneIndex, int colIndex) {
        super(GameConfig.RAILGUN_UNIT_HP, laneIndex, colIndex, GameConfig.RAILGUN_UNIT_COOLDOWN);
        superLaserCooldown = new GameTimer(GameConfig.RAILGUN_SUPER_LASER_CD, false);
        superLaserCooldown.start(); 
        updateVisual();
    }

    @Override
    protected void updateBehavior(MyWorld world) {
        super.updateBehavior(world);
        if (level >= GameConfig.RAILGUN_SUPER_LASER_UNLOCK) {
            superLaserCooldown.update(world);
            boolean currentlyReady = superLaserCooldown.isExpired();
            
            // Pulse the visual while ready or update while charging
            if (currentlyReady || !superLaserCooldown.isExpired()) {
                if (world.getActCount() % 4 == 0) updateVisual();
            }

            if (currentlyReady != isUltimateReady) {
                isUltimateReady = currentlyReady;
                updateVisual();
            }

            if (isUltimateReady && Greenfoot.mouseClicked(this)) {
                fireUltimate(world);
            }
        }
    }

    private void fireUltimate(MyWorld world) {
        isUltimateReady = false;
        superLaserCooldown.reset();
        superLaserCooldown.start();
        updateVisual(); 
        int scaledDmg = (int)(GameConfig.RAILGUN_SUPER_LASER_DMG * Math.pow(GameConfig.LEVEL_DMG_MULT, level - 1));
        world.addObject(new SuperLaser(scaledDmg, getX()), getX() + (GameConfig.WORLD_WIDTH / 2), getY()); 
    }

    @Override
    public void updateVisual() {
        // Get the base image (keeps the exact same size so bullets align perfectly!)
        GreenfootImage baseImg = UnitVisuals.draw(3, level, Color.CYAN);
        
        if (level >= GameConfig.RAILGUN_SUPER_LASER_UNLOCK) {
            int w = baseImg.getWidth();
            int h = baseImg.getHeight();
            int center = h / 2;
            
            if (isUltimateReady) {
                // 1. READY STATE: Glowing Yellow Core in the body
                baseImg.setColor(Color.YELLOW);
                baseImg.fillRect(4, center - 4, w / 2 - 8, 8); 
                
                // Add tiny "CLICK" text at the bottom edge of the unit
                baseImg.setFont(new Font("SansSerif", true, false, 11));
                baseImg.drawString("CHARGED", 2, h - 2);
                
                // Highlight the whole gun frame
                baseImg.drawRect(0, 0, w-1, h-1);
            } else {
                // 2. CHARGING STATE: Energy meter built right into the armor plating
                double pct = superLaserCooldown.getPercentComplete();
                
                // Draw an empty black battery slot in the back half of the gun
                baseImg.setColor(Color.BLACK);
                baseImg.fillRect(4, center - 4, w / 2 - 8, 8);
                
                // Fill the battery with Cyan energy as it charges
                baseImg.setColor(Color.CYAN);
                baseImg.fillRect(4, center - 4, (int)((w / 2 - 8) * pct), 8);
            }
        }
        
        setImage(baseImg);
        setNormalImage(baseImg);
    }

    @Override
    protected void attack(Enemy target) {
        int scaledDmg = (int)(GameConfig.RAILGUN_UNIT_DAMAGE * Math.pow(GameConfig.LEVEL_DMG_MULT, level - 1));
        // Firing from exact X,Y (which is now perfectly centered)
        getWorld().addObject(new PiercingProjectile(scaledDmg, level), getX(), getY());
    }
        
    @Override protected int getBaseHPFromConfig() { return GameConfig.RAILGUN_UNIT_HP; }
}