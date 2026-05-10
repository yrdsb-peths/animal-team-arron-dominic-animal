import greenfoot.*;

public class RailgunUnit extends Unit {

    public RailgunUnit(int laneIndex, int colIndex) {
        super(GameConfig.RAILGUN_UNIT_HP, laneIndex, colIndex, GameConfig.RAILGUN_UNIT_COOLDOWN);
        
        // Visual: A Cyan rectangle
        GreenfootImage img = new GreenfootImage(40, 40);
        img.setColor(Color.CYAN);
        img.fillRect(0, 0, 40, 40);
        
        // Add a little "barrel" to make it look like a cannon
        img.setColor(Color.WHITE);
        img.fillRect(20, 15, 20, 10);
        setImage(img);
    }

    @Override
    protected void attack(Enemy target) {
        // We don't care about the target, we just shoot a piercing beam straight ahead!
        getWorld().addObject(new PiercingProjectile(GameConfig.RAILGUN_UNIT_DAMAGE), getX(), getY());
    }
}