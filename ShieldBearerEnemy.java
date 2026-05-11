import greenfoot.*;

public class ShieldBearerEnemy extends Enemy {
    private int flashTimer = 0;

    public ShieldBearerEnemy() {
        super(GameConfig.SHIELD_ENEMY_HP, GameConfig.SHIELD_ENEMY_DAMAGE, 
              GameConfig.SHIELD_ENEMY_SPEED, GameConfig.SHIELD_ENEMY_ATK_COOLDOWN, 0); 
        this.baseDrop = GameConfig.DROP_SHIELD; 
        updateVisual(false);
    }

    @Override
    public void takeDamage(int amount, boolean bypassShield) {
        if (bypassShield) {
            super.takeDamage(amount, true); 
        } else {
            // --- BLOCK FEEDBACK ---
            flashTimer = 5; // Flash for 5 frames
            updateVisual(true);
            getWorld().addObject(new FloatingText("BLOCKED", Color.WHITE, 12, 1, 30), getX(), getY() - 30);
            // Spawn sparks at the front of the shield (left side of the actor)
            if (getWorld() != null) {
                for(int i=0; i<3; i++) {
                    getWorld().addObject(new BlockSpark(Color.LIGHT_GRAY), getX() - 20, getY());
                }
            }
        }
    }

    @Override
    protected void updateBehavior(MyWorld world) {
        super.updateBehavior(world);
        
        // Handle flashing visual reset
        if (flashTimer > 0) {
            flashTimer--;
            if (flashTimer == 0) updateVisual(false);
        }
    }

    private void updateVisual(boolean isFlashing) {
        GreenfootImage img = new GreenfootImage(45, 45);
        img.setColor(Color.BLUE);
        img.fillRect(5, 5, 40, 35); 
        
        // The Shield part
        img.setColor(isFlashing ? Color.WHITE : Color.LIGHT_GRAY);
        img.fillRect(0, 0, 10, 45); 
        setImage(img);
    }

    @Override protected void performAttack(Unit target) { target.takeDamage(this.damage); }
    @Override protected void handleDeath(MyWorld world) { world.removeObject(this); }
}