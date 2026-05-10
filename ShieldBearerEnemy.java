import greenfoot.*;

public class ShieldBearerEnemy extends Enemy {
    public ShieldBearerEnemy() {
        super(GameConfig.SHIELD_ENEMY_HP, GameConfig.SHIELD_ENEMY_DAMAGE, 
              GameConfig.SHIELD_ENEMY_SPEED, GameConfig.SHIELD_ENEMY_ATK_COOLDOWN, 0); 
        this.baseDrop = GameConfig.DROP_SHIELD; 
        // Visual: A blue square with a thick GREY wall in front of it
        GreenfootImage img = new GreenfootImage(45, 45);
        img.setColor(Color.BLUE);
        img.fillRect(5, 5, 40, 35); // Body
        img.setColor(Color.LIGHT_GRAY);
        img.fillRect(0, 0, 10, 45); // Shield at the front (left side)
        setImage(img);
    }

    @Override
    public void takeDamage(int amount, boolean bypassShield) {
        if (bypassShield) {
            super.takeDamage(amount, true); // Take damage normally
        } else {
            // Blocked! We do nothing, completely ignoring the basic attack/sniper.
            // Optional: You could play a "Clang" sound effect here!
        }
    }

    @Override
    protected void performAttack(Unit target) { target.takeDamage(this.damage); }

    @Override
    protected void handleDeath(MyWorld world) {
        CurrencyManager.earn(20);
        ScoreManager.addScore(150);
        world.removeObject(this);
    }
}