import greenfoot.*;

public class UnitVisuals {
    
    public static GreenfootImage draw(int unitID, int level, Color baseColor) {
        // Base size grows slightly per level
        int size = (int)(40 * (1 + (level - 1) * GameConfig.LEVEL_VISUAL_SCALE));
        GreenfootImage img = new GreenfootImage(size, size);
        int center = size / 2;
        Color lvlAccent = getLevelColor(level);

        img.setColor(baseColor);

        switch(unitID) {
            case 1: // BASIC UNIT (Draws the "Frame" for the squad)
                if (level >= 2) {
                    img.setColor(new Color(40, 40, 40)); // Dark base
                    img.fillRect(0, 0, size, size);
                    img.setColor(lvlAccent);
                    img.drawRect(0, 0, size-1, size-1);
                }
                if (level >= 4) {
                    // Corner reinforcements
                    img.fillRect(0, 0, 8, 8); img.fillRect(size-8, 0, 8, 8);
                    img.fillRect(0, size-8, 8, 8); img.fillRect(size-8, size-8, 8, 8);
                }
                break;

            case 2: // SNIPER (Diamond evolution)
                int[] px = {center, size, center, 0};
                int[] py = {0, center, size, center};
                img.fillPolygon(px, py, 4); // Lvl 1
                
                if (level >= 2) { // Inner crosshair
                    img.setColor(Color.BLACK);
                    img.drawPolygon(px, py, 4);
                    img.drawLine(center, 5, center, size-5);
                    img.drawLine(5, center, size-5, center);
                }
                if (level >= 3) { // Extended Sniper Barrel
                    img.setColor(Color.GRAY);
                    img.fillRect(center, center-2, size/2 + 5, 4); 
                }
                if (level >= 4) { // Tech Sight
                    img.setColor(lvlAccent);
                    img.fillRect(center+5, center-6, 4, 4);
                }
                if (level == 5) { // Floating Shards
                    img.setColor(lvlAccent);
                    img.fillOval(2, 2, 6, 6); img.fillOval(size-8, 2, 6, 6);
                    img.fillOval(2, size-8, 6, 6); img.fillOval(size-8, size-8, 6, 6);
                }
                break;

            case 3: // RAILGUN (Cannon evolution)
                img.fillRect(0, center-8, size, 16); // Lvl 1
                
                if (level >= 2) { // Armor plating
                    img.setColor(Color.DARK_GRAY);
                    img.fillRect(0, center-12, size/2, 24); 
                }
                if (level >= 3) { // Energy cores
                    img.setColor(Color.WHITE);
                    img.fillRect(size/4, center-4, size/2, 8);
                }
                if (level >= 4) { // Dual Prongs
                    img.setColor(lvlAccent);
                    img.fillRect(size/2, center-10, size/2, 4);
                    img.fillRect(size/2, center+6, size/2, 4);
                }
                if (level == 5) { // Energy Tip
                    img.setColor(Color.YELLOW);
                    img.fillOval(size-10, center-5, 10, 10);
                }
                break;

            case 4: // ALCHEMIST (Flask evolution)
                img.fillOval(5, center-5, size-10, center+5); // Belly
                img.fillRect(center-5, 5, 10, center);        // Neck
                
                if (level >= 2) { // Cork
                    img.setColor(new Color(139, 69, 19)); 
                    img.fillRect(center-4, 2, 8, 6);
                }
                if (level >= 3) { // Bubbles & Glass
                    img.setColor(Color.WHITE);
                    img.fillOval(center-8, center+5, 4, 4);
                    img.fillOval(center+2, center+10, 3, 3);
                    img.drawLine(size-8, center, size-8, size-5); // Glass shine
                }
                if (level >= 4) { // Toxic Aura / Spill
                    img.setColor(lvlAccent);
                    img.fillRect(center-12, size-4, 24, 4);
                }
                if (level == 5) { // Golden Cauldron frame
                    img.setColor(Color.YELLOW);
                    img.drawOval(2, center-8, size-4, center+8);
                    img.drawLine(2, center, 2, size);
                    img.drawLine(size-2, center, size-2, size);
                }
                break;

            case 7: // COWARD (Helmet evolution)
                img.fillOval(5, 5, size-10, size-10); // Lvl 1
                
                if (level >= 2) { // Iron Helmet
                    // Replace: img.fillArc(5, 5, size-10, size-10, 0, 180); 
                    fillArc(img, 5, 5, size-10, size-10, 0, 180, Color.GRAY); 
                }
                if (level >= 3) { // Helmet Spike
                    img.fillPolygon(new int[]{center-3, center+3, center}, new int[]{5, 5, 0}, 3);
                }
                if (level >= 4) { // Shield
                    img.setColor(lvlAccent);
                    img.fillRect(size-8, center-10, 6, 20);
                }
                if (level == 5) { // Spartan Crest
                    // Replace: img.fillArc(center-10, 0, 20, 15, 0, 180);
                    fillArc(img, center-10, 0, 20, 15, 0, 180, Color.RED);
                }
                break;

        }

        return img;
    }

    public static Color getLevelColor(int level) {
        if (level == 2) return new Color(50, 255, 50);  // Green Accent
        if (level == 3) return new Color(50, 150, 255); // Blue Accent
        if (level == 4) return new Color(200, 50, 255); // Purple Accent
        if (level == 5) return Color.YELLOW;            // Gold Accent
        return Color.WHITE;
    }
    
    private static void fillArc(GreenfootImage img, int x, int y, int w, int h, int start, int angle, Color color) {
        java.awt.Graphics2D g = img.getAwtImage().createGraphics();
        g.setColor(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
        g.fillArc(x, y, w, h, start, angle);
        g.dispose();
    }

}