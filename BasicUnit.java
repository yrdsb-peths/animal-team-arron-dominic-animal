import greenfoot.*;

public class BasicUnit extends Unit {
    private int stackCount = 1;

    public BasicUnit(int laneIndex, int colIndex) {
        super(GameConfig.BASIC_UNIT_HP, laneIndex, colIndex, GameConfig.BASIC_UNIT_COOLDOWN);
        updateVisual();
    }

    @Override
    public void takeDamage(int amount) {
        // Use the actual current health
        this.health -= amount;
        
        // Trigger visual hurt flash (inherited from Unit)
        super.takeDamage(0); // We pass 0 because we already subtracted health manually
        
        if (this.health <= 0) die();
    }

    public void addStack() {
        // 1. Calculate what the Max HP WAS before adding the new unit
        int oldMaxHP = (int)(GameConfig.BASIC_UNIT_HP * (stackCount * 0.6 + Math.sqrt(stackCount) * 0.4));
        
        stackCount++; // Increase count
        
        // 2. Calculate the NEW Max HP
        int newMaxHP = (int)(GameConfig.BASIC_UNIT_HP * (stackCount * 0.6 + Math.sqrt(stackCount) * 0.4));
        
        // 3. How much HP did the "fresh troops" bring?
        int hpGained = newMaxHP - oldMaxHP;
        
        // 4. Update current health
        // This adds the new guy's HP to the current pile. 
        // Wounded soldiers stay wounded, but the squad is now healthier.
        this.health += hpGained;
        
        updateVisual();
    }

    @Override
    protected void attack(Enemy target) {
        // DAMAGE HYBRID SCALING (40% Linear + 60% Curve)
        // Guaranteed minimum 50% damage contribution per unit.
        double linearPart = stackCount * 0.4;
        double curvePart = Math.sqrt(stackCount) * 0.6;
        
        int totalDamage = (int)(GameConfig.BASIC_UNIT_DAMAGE * (linearPart + curvePart));
        
        getWorld().addObject(new Projectile(target, totalDamage, null), getX(), getY());
    }

    private void updateVisual() {
        int size = 40;
        GreenfootImage img = new GreenfootImage(size, size);
        
        // 1. Draw the Grid Swarm
        int gridSide = (int)Math.ceil(Math.sqrt(stackCount));
        int dotSize = Math.max(2, (size / gridSide) - 1); 

        img.setColor(Color.GREEN);
        int drawn = 0;
        for (int row = 0; row < gridSide; row++) {
            for (int col = 0; col < gridSide; col++) {
                if (drawn >= stackCount) break;
                img.fillRect(col * (dotSize + 1), row * (dotSize + 1), dotSize, dotSize);
                drawn++;
            }
        }
        
        // 2. Draw a small number in the corner if stack > 1
        if (stackCount > 1) {
            img.setColor(Color.WHITE);
            img.setFont(new Font("SansSerif", true, false, 12));
            img.drawString("" + stackCount, 2, size - 2);
        }
        
        setImage(img);
        setNormalImage(img);
    }
    
    public int getStackCount() {
        return stackCount;
    }
}