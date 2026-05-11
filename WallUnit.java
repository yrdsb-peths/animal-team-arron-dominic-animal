import greenfoot.*;

public class WallUnit extends Unit {
    protected int currentStage = 3;

    public WallUnit(int laneIndex, int colIndex) {
        super(GameConfig.WALL_UNIT_HP, laneIndex, colIndex, 999.0); 
        updateVisual();
    }

    @Override
    public void takeDamage(int amount) {
        super.takeDamage(amount); 
        float hpPercent = (float)health / maxHealth;
        int newStage = (hpPercent > 0.66f) ? 3 : (hpPercent > 0.33f) ? 2 : 1;

        if (newStage != currentStage) {
            currentStage = newStage;
            updateVisual();
        }
    }
    
    @Override
    public void takeDamage(int amount, Enemy attacker) {
        super.takeDamage(amount, attacker);
        
        // THORNS MECHANIC (REWORKED)
        if (level >= GameConfig.WALL_THORNS_UNLOCK && attacker != null) {
            // Math: Base (20) * Level (2) + amount (1) = 41 damage.
            // This is actually noticeable against a 200-400 HP enemy!
            int retaliationDmg = (GameConfig.WALL_THORN_BASE_DMG * level) + 
                                 (int)(amount * GameConfig.WALL_THORN_MULTIPLIER);
            
            attacker.takeDamage(retaliationDmg, true); 
            
            // VISUAL FEEDBACK: Make it pop!
            // We use a specific color (Orange/Red) so it looks like a "Counter"
            if (getWorld() != null) {
                getWorld().addObject(new FloatingText("-" + retaliationDmg, Color.ORANGE, 15, 2, 25), 
                                     attacker.getX(), attacker.getY());
                
                // Spawn some red sparks to show the "Prick"
                for(int i=0; i<2; i++) {
                    getWorld().addObject(new BlockSpark(Color.RED), getX(), getY());
                }
            }
        }
    }

    @Override
    public void updateVisual() {
        int size = 45;
        GreenfootImage img = new GreenfootImage(size, size);
        
        // Height scaling based on HP stage
        int wallH = (size / 3) * currentStage;
        int yOff = size - wallH;
        Color lvlColor = UnitVisuals.getLevelColor(level);

        // 1. MATERIAL SELECTION (The "Respect" Factor)
        Color primary = new Color(100, 70, 40); // Lvl 1: Wood/Dirt
        if (level == 2) primary = new Color(120, 120, 120); // Lvl 2: Stone
        if (level == 3) primary = new Color(70, 90, 130);   // Lvl 3: Reinforced Steel
        if (level == 4) primary = new Color(40, 20, 60);    // Lvl 4: Obsidian
        if (level == 5) primary = new Color(220, 200, 100); // Lvl 5: Divine Gold

        // 2. DRAW BASE BLOCK
        img.setColor(primary);
        img.fillRect(2, yOff, size-4, wallH);

        // 3. DRAW DYNAMIC BRICK LINES (The "Cool" Lines)
        img.setColor(new Color(0, 0, 0, 150));
        for (int i = 1; i < currentStage; i++) {
            int lineY = yOff + (i * (wallH / currentStage));
            img.drawLine(2, lineY, size-3, lineY);
        }
        // Vertical mortar lines (offset per row)
        for (int i = 0; i < currentStage; i++) {
            int rowY = yOff + (i * (wallH / currentStage));
            int midY = rowY + (wallH / currentStage / 2);
            if (i % 2 == 0) img.drawLine(size/2, rowY, size/2, rowY + (wallH/currentStage));
            else {
                img.drawLine(size/4, rowY, size/4, rowY + (wallH/currentStage));
                img.drawLine(3*size/4, rowY, 3*size/4, rowY + (wallH/currentStage));
            }
        }

        // 4. LEVEL-UP EVOLUTION (Details)
        img.setColor(lvlColor);
        if (level >= 2) img.drawRect(2, yOff, size-5, wallH-1); // Edge Highlight
        
        if (level >= 3) { // IRON BARS (Blue Stage)
            img.setColor(new Color(200, 200, 255, 100));
            img.fillRect(8, yOff, 4, wallH);
            img.fillRect(size-12, yOff, 4, wallH);
        }

        if (level >= 4) { // RUNIC GLOW (Purple Stage)
            img.setColor(lvlColor);
            img.drawOval(size/2-5, yOff + wallH/2-5, 10, 10);
            img.fillOval(size/2-2, yOff + wallH/2-2, 4, 4);
        }

        if (level == 5) { // THE AEGIS (Golden Stage)
            // Top Spikes
            int[] sx = {5, 10, 15, 20, 25, 30, 35, 40};
            for(int x : sx) {
                img.fillPolygon(new int[]{x-3, x, x+3}, new int[]{yOff, yOff-6, yOff}, 3);
            }
            // Golden Shield Crest
            img.setColor(Color.WHITE);
            img.drawRect(size/2-8, yOff+4, 16, wallH-8);
        }

        setImage(img);
        setNormalImage(img);
    }

    @Override protected void attack(Enemy target) {}
    
    @Override protected int getBaseHPFromConfig() { return GameConfig.WALL_UNIT_HP; }
}