import greenfoot.*;

public class UnitVisuals {
    public static GreenfootImage draw(int unitID, int level, Color baseColor) {
        int baseSize = 40;
        int scaledSize = (int)(baseSize * (1 + (level - 1) * GameConfig.LEVEL_VISUAL_SCALE));
        GreenfootImage img = new GreenfootImage(scaledSize + 10, scaledSize + 10); // Extra room for glows
        
        int center = img.getWidth() / 2;
        int drawX = 5;
        int drawY = 5;

        // 1. DRAW THE RANK AURA (Glow)
        if (level >= 2) {
            Color glowColor = getLevelColor(level);
            img.setColor(new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), 100));
            img.fillOval(0, 0, img.getWidth(), img.getHeight());
        }

        // 2. DRAW THE BASE SHAPE
        img.setColor(baseColor);
        if (unitID == 4 || unitID == 7) { // Alchemist/Coward
            img.fillOval(drawX, drawY, scaledSize, scaledSize);
        } else {
            img.fillRect(drawX, drawY, scaledSize, scaledSize);
        }

        // 3. DRAW LEVEL-SPECIFIC DISTINCT FEATURES
        img.setColor(Color.BLACK);
        if (level >= 3) { // Reinforced Border
            img.drawRect(drawX, drawY, scaledSize-1, scaledSize-1);
            img.drawRect(drawX+2, drawY+2, scaledSize-5, scaledSize-5);
        }
        
        if (level >= 4) { // Add "Cross" pattern for Elite
            img.setColor(new Color(255, 255, 255, 150));
            img.fillRect(center-2, drawY, 4, scaledSize);
            img.fillRect(drawX, center-2, scaledSize, 4);
        }

        if (level == 5) { // The "Ascended" Crown/Star
            img.setColor(Color.WHITE);
            int[] xPoints = {center, center+8, center-8};
            int[] yPoints = {drawY-2, drawY+10, drawY+10};
            img.fillPolygon(xPoints, yPoints, 3);
        }

        return img;
    }

    public static Color getLevelColor(int level) {
        switch(level) {
            case 2: return GameConfig.LVL_2_COLOR;
            case 3: return GameConfig.LVL_3_COLOR;
            case 4: return GameConfig.LVL_4_COLOR;
            case 5: return GameConfig.LVL_5_COLOR;
            default: return GameConfig.LVL_1_COLOR;
        }
    }
}