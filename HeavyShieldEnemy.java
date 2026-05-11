import greenfoot.*;

public class HeavyShieldEnemy extends Enemy {
    private int flashTimer = 0;

    public HeavyShieldEnemy() {
        super(GameConfig.HEAVY_SHIELD_HP, GameConfig.HEAVY_SHIELD_DAMAGE, 
              GameConfig.HEAVY_SHIELD_SPEED, GameConfig.HEAVY_SHIELD_ATK_COOLDOWN, 0); 
        this.baseDrop = GameConfig.DROP_HEAVY_SHIELD; 
        updateVisual(false);
    }

    @Override
    public void takeDamage(int amount, boolean bypassShield) {
        if (bypassShield) {
            // Only Acid, Railgun, Splash, or Backstabs work here!
            super.takeDamage(amount, true); 
        } else {
            // --- HEAVY BLOCK FEEDBACK ---
            flashTimer = 7; // Slightly longer flash for heavy impact
            updateVisual(true);
            
            if (getWorld() != null) {
                // 1. Spawn MORE sparks (Heavy metal clashing!)
                for(int i=0; i < 6; i++) {
                    getWorld().addObject(new BlockSpark(new Color(255, 255, 100)), getX() - 15, getY());
                }
                
                // 2. Big "BLOCKED" text to warn the player
                getWorld().addObject(new FloatingText("IMMUNE", Color.WHITE, 14, 1, 40), getX(), getY() - 40);
            }
        }
    }

    @Override
    protected void updateBehavior(MyWorld world) {
        super.updateBehavior(world);
        
        // Handle visual reset for the flash
        if (flashTimer > 0) {
            flashTimer--;
            if (flashTimer == 0) updateVisual(false);
        }
    }

    private void updateVisual(boolean isFlashing) {
        GreenfootImage img = new GreenfootImage(50, 50);
        
        // Body (Dark Blue)
        img.setColor(new Color(0, 0, 100)); 
        img.fillRect(15, 5, 35, 40); 
        
        // The Massive Shield (Dark Grey or White if flashing)
        if (isFlashing) {
            img.setColor(new Color(255, 215, 0)); // Gold flash
        } else {
            img.setColor(Color.DARK_GRAY);
        }
        img.fillRect(0, 0, 15, 50); 
        
        // Add a "Heavy" detail - metal bolts on the shield
        img.setColor(isFlashing ? Color.WHITE : Color.BLACK);
        img.drawRect(2, 2, 11, 46);
        img.fillOval(5, 5, 4, 4);
        img.fillOval(5, 40, 4, 4);
        
        setImage(img);
    }

    @Override protected void performAttack(Unit target) { target.takeDamage(this.damage); }
    @Override protected void handleDeath(MyWorld world) { world.removeObject(this); }
}