import greenfoot.*;

public class BigWallUnit extends WallUnit {

    public BigWallUnit(int laneIndex, int colIndex) {
        super(laneIndex, colIndex);
        // Use BigWall specific base HP
        this.health = GameConfig.BIG_WALL_UNIT_HP;
        this.maxHealth = health;
        updateVisual();
    }

    @Override
    public void updateVisual() {
        // Big Wall is physically imposing
        int size = 52; 
        GreenfootImage img = new GreenfootImage(size, size);
        
        // The 3-Stage Height Logic (Restored & Enhanced)
        int wallH = (size / 3) * currentStage;
        int yOff = size - wallH;
        
        // 1. DYNAMIC COLOR PALETTE (Level-based Industrial Metals)
        Color plateColor = new Color(60, 60, 70); // LVL 1: Original Iron Grey
        if (level == 2) plateColor = new Color(80, 85, 100); // LVL 2: Hardened Steel
        if (level == 3) plateColor = new Color(40, 70, 110);  // LVL 3: Cobalt Alloy
        if (level == 4) plateColor = new Color(30, 20, 40);   // LVL 4: Void Metal
        if (level == 5) plateColor = new Color(255, 230, 150); // LVL 5: Eternal Aurum

        Color accent = UnitVisuals.getLevelColor(level);

        // 2. DRAW THE MASSIVE STRUCTURE
        img.setColor(plateColor);
        // Beveled edges for "Thick" look
        img.fillPolygon(new int[]{4, size-4, size, 0}, new int[]{yOff, yOff, size, size}, 4);

        // 3. THE "COOL AS F" SEAMS (The 1/3, 2/3 Lines)
        // Instead of thin brick lines, these are Heavy Armor Gaps
        img.setColor(Color.BLACK);
        for (int i = 1; i < currentStage; i++) {
            int lineY = yOff + (i * (wallH / currentStage));
            // Heavy Seam
            img.fillRect(0, lineY - 1, size, 3); 
            // Internal Plate Detailing (Vertical seams)
            if (i == 1) img.drawLine(size/3, yOff, size/3, lineY);
            if (i == 2) img.drawLine(2*size/3, lineY - (wallH/currentStage), 2*size/3, lineY);
        }

        // 4. INDUSTRIAL DETAILS (Differentiating from normal wall)
        // Rivets (Bolts) in the corners of every plate
        img.setColor(new Color(0, 0, 0, 180));
        for (int row = 0; row < currentStage; row++) {
            int rY = yOff + (row * (wallH / currentStage)) + 4;
            img.fillOval(4, rY, 4, 4);
            img.fillOval(size-8, rY, 4, 4);
        }

        // 5. LEVEL EVOLUTION (High-Level Fortress Features)
        if (level >= 2) {
            // Steel Reinforcement Ribs
            img.setColor(new Color(255, 255, 255, 50));
            img.fillRect(size/2 - 2, yOff, 4, wallH);
        }

        if (level >= 3) {
            // Glowing Energy Vents (Blue Stage)
            img.setColor(new Color(0, 200, 255));
            img.fillRect(size/2 - 6, yOff + wallH/2 - 2, 12, 4);
            img.drawRect(0, yOff, size-1, wallH-1);
        }

        if (level >= 4) {
            // Defensive Spikes (Obsidian Stage)
            img.setColor(accent);
            for(int x = 10; x < size; x += 15) {
                img.fillPolygon(new int[]{x-4, x, x+4}, new int[]{yOff+10, yOff-4, yOff+10}, 3);
            }
        }

        if (level == 5) {
            // 1. OVERRIDE BASE: Deep "Void-Metal" (Dark Indigo-Black)
            Color abyssBlack = new Color(15, 15, 25);
            img.setColor(abyssBlack);
            img.fillPolygon(new int[]{2, size-2, size, 0}, new int[]{yOff, yOff, size, size}, 4);

            // 2. REINFORCED INSET: Layered Armor Look
            // This creates a "frame within a frame" to give it depth
            img.setColor(new Color(45, 45, 60)); // Gunmetal Grey
            img.drawRect(4, yOff+2, size-9, wallH-5);
            img.drawRect(8, yOff+6, size-17, wallH-13);

            // 3. ETHEREAL RUNES (Thin, High-Contrast Cyan)
            // Instead of a "White Smash", we use 1-pixel thin lines for ancient tech
            Color energyColor = new Color(0, 255, 200); // Sharp Mint/Cyan
            img.setColor(energyColor);
            
            int midX = size / 2;
            int midY = yOff + (wallH / 2);
            
            // Central Diamond "Core"
            img.drawPolygon(new int[]{midX, midX+8, midX, midX-8}, 
                            new int[]{midY-8, midY, midY+8, midY}, 4);
            
            // Circuitry-style "Pulse Lines" connecting to the rivets
            img.drawLine(midX, midY-8, midX, yOff+6);             // Top vertical
            img.drawLine(midX, midY+8, midX, yOff+wallH-6);      // Bottom vertical
            img.drawLine(midX-8, midY, 8, midY);                  // Left horizontal
            img.drawLine(midX+8, midY, size-8, midY);             // Right horizontal

            // 4. OBSIDIAN BATTLEMENTS (Dark, Sharp Spikes)
            // No longer bright gold spikes, but dark obsidian with glowing tips
            for (int x = 10; x < size; x += 16) {
                img.setColor(new Color(25, 25, 40)); // Dark Spike Base
                img.fillPolygon(new int[]{x-5, x, x+5}, new int[]{yOff, yOff-14, yOff}, 3);
                
                img.setColor(energyColor); // Tiny glowing tip
                img.drawLine(x, yOff-14, x, yOff-10);
            }

            // 5. INDUSTRIAL RIVETS (The "Small Details")
            img.setColor(Color.BLACK);
            img.fillOval(6, yOff+4, 4, 4);
            img.fillOval(size-10, yOff+4, 4, 4);
            img.fillOval(6, size-8, 4, 4);
            img.fillOval(size-10, size-8, 4, 4);
            
            // 6. FINAL DEPTH SHADING
            // A semi-transparent black gradient on the left and right edges
            img.setColor(new Color(0, 0, 0, 180));
            img.fillRect(0, yOff, 4, wallH); 
            img.fillRect(size-4, yOff, 4, wallH);
            
            // Subdued "Power Hum" (A very faint cyan outline on the very edge)
            img.setColor(new Color(0, 255, 200, 40)); 
            img.drawRect(0, yOff, size-1, wallH-1);
        }

        setImage(img);
        setNormalImage(img);
    }
    
    @Override protected int getBaseHPFromConfig() { return GameConfig.BIG_WALL_UNIT_HP; }
}