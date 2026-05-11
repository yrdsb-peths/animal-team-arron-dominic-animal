import greenfoot.*;

public class BasicUnit extends Unit {
    private int stackCount = 1;

    public BasicUnit(int laneIndex, int colIndex) {
        super(GameConfig.BASIC_UNIT_HP, laneIndex, colIndex, GameConfig.BASIC_UNIT_COOLDOWN);
        updateVisual();
    }

    public void addStack() {
        stackCount++;
        // Maintain your original HP stacking logic
        int newMaxHP = (int)(GameConfig.BASIC_UNIT_HP * (stackCount * 0.6 + Math.sqrt(stackCount) * 0.4));
        this.health += (newMaxHP - this.maxHealth);
        this.maxHealth = newMaxHP;
        updateVisual();
    }

    @Override
    public void updateVisual() {
        // Get the tactical background frame from UnitVisuals
        GreenfootImage img = UnitVisuals.draw(1, level, Color.GREEN);
        int size = img.getWidth();
        
        // Draw the Swarm Dots on top of the frame
        int gridSide = (int)Math.ceil(Math.sqrt(stackCount));
        int dotSize = Math.max(2, (size / gridSide) - 2); 
        int offset = (size - (gridSide * (dotSize + 1))) / 2; // Center the dots

        img.setColor(Color.GREEN);
        if (level == 5) img.setColor(Color.WHITE); // Elite troops at Lvl 5

        int drawn = 0;
        for (int row = 0; row < gridSide; row++) {
            for (int col = 0; col < gridSide; col++) {
                if (drawn >= stackCount) break;
                img.fillRect(offset + col * (dotSize + 1), offset + row * (dotSize + 1), dotSize, dotSize);
                drawn++;
            }
        }
        
        setImage(img);
        setNormalImage(img);
    }

    @Override
    protected void attack(Enemy target) {
        double linearPart = stackCount * 0.4;
        double curvePart = Math.sqrt(stackCount) * 0.6;
        int baseDmg = (int)(GameConfig.BASIC_UNIT_DAMAGE * (linearPart + curvePart));
        
        // Apply 3x Level Multiplier
        int totalDamage = (int)(baseDmg * Math.pow(GameConfig.LEVEL_DMG_MULT, level - 1));
        getWorld().addObject(new Projectile(target, totalDamage, null), getX(), getY());
    }
    
    public int getStackCount()
    {
        return stackCount;
    }
    
    @Override protected int getBaseHPFromConfig() { return GameConfig.BASIC_UNIT_HP; }
}