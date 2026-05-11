// ==================================================
// FILE: ./KamikazeEnemy.java
// ==================================================
import greenfoot.*;

public class KamikazeEnemy extends Enemy {
    private int shieldPlating = 0;

    public KamikazeEnemy() {
        super(GameConfig.KAMIKAZE_ENEMY_HP, GameConfig.KAMIKAZE_ENEMY_DAMAGE, 
              GameConfig.KAMIKAZE_ENEMY_SPEED, GameConfig.KAMIKAZE_ENEMY_ATK_COOLDOWN, 0); 
        this.baseDrop = GameConfig.DROP_KAMIKAZE;
        updateVisual();
    }
    
    /** Allows CalamityManager to buff this specific unit */
    public void setElite(int shieldLayers, double hpMult, float speedMult) {
        this.shieldPlating = shieldLayers;
        this.health = (int)(this.health * hpMult);
        this.baseSpeed *= speedMult;
        updateVisual();
    }

    @Override
    public void takeDamage(int amount, boolean bypassShield) {
        // If we have shield plating and the damage doesn't specifically bypass shields...
        if (shieldPlating > 0 && !bypassShield) {
            shieldPlating--;
            // Visual feedback for shield hit
            getWorld().addObject(new FloatingText("BLOCK!", Color.CYAN, 15, 2, 30), getX(), getY());
            updateVisual();
            return; // Damage absorbed!
        }
        super.takeDamage(amount, bypassShield);
    }

    @Override
    protected void performAttack(Unit target) {
        // Special logic: Deals massive damage to walls, normal damage to units
        if (target instanceof WallUnit || target instanceof BigWallUnit) {
            target.takeDamage(getFinalDamage(GameConfig.KAMIKAZE_WALL_DAMAGE), this);
        } else {
            target.takeDamage(getFinalDamage(GameConfig.KAMIKAZE_ENEMY_DAMAGE), this);
        }
        
        // Kamikaze explodes!
        this.takeDamage(99999, true); 
    }

    private void updateVisual() {
        int size = 30;
        GreenfootImage img = new GreenfootImage(size, size);
        
        // Base Color
        img.setColor(Color.RED);
        img.fillOval(5, 5, 20, 20);
        
        // If it has shield plating, give it a blue "Energy Shell"
        if (shieldPlating > 0) {
            img.setColor(new Color(0, 255, 255, 100)); // Cyan glow
            img.drawOval(2, 2, 26, 26);
            img.drawOval(3, 3, 24, 24); // Double thickness
        }

        img.setColor(Color.ORANGE);
        img.fillRect(10, 0, 10, 30);
        img.fillRect(0, 10, 30, 10);
        setImage(img);
    }

    @Override
    protected void handleDeath(MyWorld world) {
        world.removeObject(this);
    }
}