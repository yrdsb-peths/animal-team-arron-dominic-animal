import greenfoot.*;

public class BasicUnit extends Unit {
    public static final int COST = GameConfig.BASIC_UNIT_COST;

    public BasicUnit(int laneIndex, int colIndex) {
        super(GameConfig.BASIC_UNIT_HP, laneIndex, colIndex, GameConfig.BASIC_UNIT_COOLDOWN); // 100 HP, 1.5 second cooldown
        
        GreenfootImage img = new GreenfootImage(40, 40);
        img.setColor(Color.GREEN);
        img.fillRect(0, 0, 40, 40);
        setImage(img);
    }

    @Override
    protected void attack(Enemy target) {
        //Project takes in 1. target 2. damage
        getWorld().addObject(new Projectile(target, GameConfig.BASIC_UNIT_DAMAGE, null), getX(), getY());
    }
}