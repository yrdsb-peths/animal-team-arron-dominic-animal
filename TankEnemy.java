import greenfoot.*;

public class TankEnemy extends Enemy {

    public TankEnemy() {
        // Use Tank stats from GameConfig
        super(GameConfig.TANK_ENEMY_HP, 
              GameConfig.TANK_ENEMY_DAMAGE, 
              GameConfig.TANK_ENEMY_SPEED, 
              GameConfig.TANK_ENEMY_ATK_COOLDOWN, 
              0); 
              this.baseDrop = GameConfig.DROP_TANK;
        // Tank visual: A big, tough-looking rectangle
        GreenfootImage img = new GreenfootImage(50, 50);
        img.setColor(Color.GRAY);
        img.fillRect(0, 0, 50, 50);
        img.setColor(Color.BLACK);
        img.drawRect(0, 0, 49, 49); // Give it a border
        setImage(img);
    }

    @Override
    protected void performAttack(Unit target) {
        // Tanks hit harder than basic enemies
        target.takeDamage(this.damage);
    }

    @Override
    protected void handleDeath(MyWorld world) {
        // Maybe tanks drop more gold?
        CurrencyManager.earn(25);
        world.removeObject(this);
    }
}