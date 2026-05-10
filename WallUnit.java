import greenfoot.*;

public class WallUnit extends Unit {
    
    // Keep track of which "stage" we are currently showing to avoid redrawing every frame
    private int currentStage = 3;

    public WallUnit(int laneIndex, int colIndex) {
        super(GameConfig.WALL_UNIT_HP, laneIndex, colIndex, 999.0); 
        updateVisual();
    }

    /** 
     * We override takeDamage so that every time the wall is hit, 
     * it checks if it needs to "shrink".
     */
    @Override
    public void takeDamage(int amount) {
        super.takeDamage(amount); // Do the normal health reduction and red flash
        
        // Calculate new stage
        float hpPercent = (float)health / GameConfig.WALL_UNIT_HP;
        int newStage;
        
        if (hpPercent > 0.66f) newStage = 3;
        else if (hpPercent > 0.33f) newStage = 2;
        else newStage = 1;

        // Only redraw if the stage actually changed
        if (newStage != currentStage) {
            currentStage = newStage;
            updateVisual();
        }
    }

    private void updateVisual() {
        int fullSize = 45;
        // The canvas stays 45x45 so the unit doesn't "jump" around in the grid
        GreenfootImage img = new GreenfootImage(fullSize, fullSize);
        
        // Calculate how many pixels high the wall should be (15, 30, or 45)
        int wallHeight = (fullSize / 3) * currentStage;
        
        // Calculate the Y starting position so it stays "on the ground" (bottom of the cell)
        int yOffset = fullSize - wallHeight;

        // 1. Draw the main block
        img.setColor(new Color(100, 70, 40)); // Brown
        img.fillRect(0, yOffset, fullSize, wallHeight);
        
        // 2. Draw the black border
        img.setColor(Color.BLACK);
        img.drawRect(0, yOffset, fullSize - 1, wallHeight - 1);
        
        // 3. Draw the "brick lines"
        // If stage 3: 2 lines. If stage 2: 1 line. If stage 1: 0 lines.
        for (int i = 1; i < currentStage; i++) {
            int lineY = yOffset + (i * (wallHeight / currentStage));
            img.drawLine(0, lineY, fullSize, lineY);
        }

        setImage(img);
        
        // IMPORTANT: The base Unit class uses 'normalImage' for the hurt-flash logic.
        // We need to tell the parent class that the "normal" look has changed.
        setNormalImage(img); 
    }

    @Override
    protected void attack(Enemy target) {
        // Walls still don't attack!
    }
}