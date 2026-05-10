import greenfoot.*;

public class BasicEnemy extends Enemy {

    public BasicEnemy() {
        // Pass all the stats from GameConfig!
        super(GameConfig.BASIC_ENEMY_HP, 
              GameConfig.BASIC_ENEMY_DAMAGE, 
              GameConfig.BASIC_ENEMY_SPEED, 
              GameConfig.BASIC_ENEMY_ATK_COOLDOWN, 
              0); 
              this.baseDrop = GameConfig.DROP_BASIC;
        setImage(new GreenfootImage(40, 40));
        getImage().setColor(Color.RED);
        getImage().fillOval(0, 0, 40, 40);
    }

    // MANDATORY: Define how it attacks
    @Override
    protected void performAttack(Unit target) {
        target.takeDamage(this.damage); // Just do normal damage
    }

    @Override
    protected void handleDeath(MyWorld world) {
        world.removeObject(this);
    }
}