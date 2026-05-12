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
            case 1: // BASIC UNIT: THE TECHNO-HIVE
        int canvasSize = size + 20; // Give room for external parts
        img = new GreenfootImage(canvasSize, canvasSize);
        int c = canvasSize / 2;
        int b = size / 2; // Half-size of the actual unit body

        // --- LAYER 1: THE CHASSIS (Dark Metal Frame) ---
        img.setColor(new Color(20, 20, 25));
        img.fillPolygon(new int[]{c-b, c+b, c+b+5, c-b-5}, 
                        new int[]{c-b, c-b, c+b, c+b}, 4); // Beveled plate
        
        // --- LEVEL 4: OVER-ARMOR PLATING ---
        if (level >= 4) {
            img.setColor(new Color(40, 45, 50));
            // Heavy side-guards
            img.fillRect(c-b-8, c-15, 6, 30);
            img.fillRect(c+b+2, c-15, 6, 30);
            img.setColor(lvlAccent);
            img.drawRect(c-b-8, c-15, 6, 30);
            img.drawRect(c+b+2, c-15, 6, 30);
        }

        // --- LAYER 2: NEON ACCENTS ---
        img.setColor(lvlAccent);
        img.drawRect(c-b, c-b, size, size); // Main border
        
        // --- LEVEL 3: ENERGY VENTS ---
        if (level >= 3) {
            img.setColor(new Color(lvlAccent.getRed(), lvlAccent.getGreen(), lvlAccent.getBlue(), 120));
            // Draw four corner "Engine Glows"
            img.fillOval(c-b-4, c-b-4, 8, 8);
            img.fillOval(c+b-4, c-b-4, 8, 8);
            img.fillOval(c-b-4, c+b-4, 8, 8);
            img.fillOval(c+b-4, c+b-4, 8, 8);
        }

        // --- LAYER 3: THE CORE (The "Eye") ---
        img.setColor(new Color(0, 0, 0));
        img.fillOval(c-10, c-10, 20, 20);
        img.setColor(level >= 4 ? Color.CYAN : Color.GREEN);
        img.fillOval(c-6, c-6, 12, 12); // Glowing center
        img.setColor(Color.WHITE);
        img.fillOval(c-2, c-4, 4, 4); // Reflective "Glance"
        
        break;

        case 2: // --- THE "VOICE OF THE ARCTIC" SNIPER REDESIGN ---
                java.awt.Graphics2D g2 = img.getAwtImage().createGraphics();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dynamic Animation Timers
                long time = System.currentTimeMillis();
                float breathe = (float)(Math.sin(time / 400.0) * 0.5 + 0.5); // 0.0 to 1.0
                boolean laserFlicker = (time % 200 < 150); // Sharp flickering effect
                
                // Professional Color Palette
                java.awt.Color steel = new java.awt.Color(45, 50, 60);
                java.awt.Color charcoal = new java.awt.Color(20, 20, 25);
                java.awt.Color iceBlue = new java.awt.Color(100, 230, 255);
                java.awt.Color laserPink = new java.awt.Color(255, 0, 150);

                // 1. THE MAIN RECEIVER (The Body)
                // A sleek, low-profile rectangle (Not a bird!)
                g2.setColor(charcoal);
                g2.fillRect(5, center - 8, size - 20, 16);
                g2.setColor(steel);
                g2.drawRect(5, center - 8, size - 20, 16);

                // 2. THE PRECISION BARREL
                g2.setColor(java.awt.Color.GRAY);
                int barrelWidth = level >= 4 ? 6 : 3;
                g2.fillRect(center, center - (barrelWidth/2), size - center, barrelWidth);

                // 3. LEVEL 2: TACTICAL SCOPE & SUPPRESSOR
                if (level >= 2) {
                    // Rectangular Scope (Top-mounted)
                    g2.setColor(charcoal);
                    g2.fillRect(center - 10, center - 16, 12, 8);
                    // Glowing Lens
                    g2.setColor(new java.awt.Color(iceBlue.getRed(), iceBlue.getGreen(), iceBlue.getBlue(), (int)(150 + (105 * breathe))));
                    g2.fillRect(center - 8, center - 14, 8, 4);
                    
                    // Barrel Muzzle Shroud
                    g2.setColor(charcoal);
                    g2.fillRect(size - 8, center - 4, 8, 8);
                }

                // 4. LEVEL 3: COOLING FINS & BIPOD
                if (level >= 3) {
                    // Cooling Fins (Explains the freeze power)
                    g2.setColor(iceBlue);
                    for(int i=0; i<3; i++) {
                        g2.fillRect(10 + (i*6), center - 12, 2, 4);
                    }
                    
                    // Deployment Bipod (Bottom-mounted)
                    g2.setColor(java.awt.Color.DARK_GRAY);
                    g2.drawLine(15, center + 8, 10, size - 2);
                    g2.drawLine(25, center + 8, 30, size - 2);
                }

                // 5. LEVEL 4: TWIN RAIL & LASER SIGHT (Animation)
                if (level >= 4) {
                    // Twin Rail-Gun conversion
                    g2.setColor(charcoal);
                    g2.fillRect(center + 5, center - 7, size - center - 10, 4);
                    g2.fillRect(center + 5, center + 3, size - center - 10, 4);
                    
                    // The "Ice Core" - Pulsing Cyan Light
                    g2.setColor(new java.awt.Color(0, 255, 255, (int)(200 * breathe)));
                    g2.fillRect(center + 8, center - 2, 10, 4);

                    // FLICKERING LASER SIGHT
                    if (laserFlicker) {
                        g2.setColor(laserPink);
                        g2.setStroke(new java.awt.BasicStroke(1f));
                        // Dot at the end of the line
                        g2.drawLine(size - 5, center + 5, size + 200, center + 5);
                        g2.fillOval(size - 2, center + 4, 3, 3);
                    }
                }

                // Level 5 Border Glow (The Merciless Finish)
                if (level == 5) {
                    g2.setStroke(new java.awt.BasicStroke(1.5f));
                    g2.setColor(new java.awt.Color(255, 255, 255, (int)(100 * breathe)));
                    g2.drawRect(2, center - 20, size - 4, 40);
                }

                g2.dispose();
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
    public static GreenfootImage drawSniper(int level, int animTimer) {
        // We make the canvas wider to accommodate a long, intimidating barrel
        int width = 80; 
        int height = 50;
        GreenfootImage img = new GreenfootImage(width, height);
        java.awt.Graphics2D g2 = img.getAwtImage().createGraphics();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        
        int center = height / 2;
        int recoil = (animTimer > 5) ? 8 : 0; 
        int drawX = 10 - recoil; // Starting X position

        // PALETTE
        java.awt.Color obsidian = new java.awt.Color(20, 20, 25);
        java.awt.Color magentaCore = new java.awt.Color(255, 0, 255);
        java.awt.Color coldCyan = new java.awt.Color(0, 255, 255);
        java.awt.Color trim = new java.awt.Color(100, 100, 120);

        // 1. THE REAR STOCK (Visual Balance)
        // This stops it from looking like a floating diamond
        g2.setColor(obsidian);
        if (level >= 2) {
            g2.fillRect(drawX, center - 6, 12, 12); // The butt-stock
            g2.setColor(trim);
            g2.drawRect(drawX, center - 6, 12, 12);
        }

        // 2. THE MAIN CHASSIS (Sleek horizontal body)
        g2.setColor(obsidian);
        int chassisWidth = 25 + (level * 5);
        g2.fillRect(drawX + 10, center - 10, chassisWidth, 20);
        
        // 3. THE MAGENTA CORE (Integrated into the gun)
        // Instead of a huge diamond, it's a glowing power cell inside the gun
        g2.setColor(magentaCore);
        int[] dx = {drawX + 15, drawX + 25, drawX + 35, drawX + 25};
        int[] dy = {center - 8, center - 15, center - 8, center - 1};
        if (level == 1) { // Level 1 is still the classic diamond
             int[] d1x = {drawX + 10, drawX + 25, drawX + 40, drawX + 25};
             int[] d1y = {center, center - 15, center, center + 15};
             g2.fillPolygon(d1x, d1y, 4);
        } else {
            g2.fillPolygon(dx, dy, 4); // Smaller core for Lvl 2+
        }

        // 4. THE BARREL (Serious Ballistics)
        g2.setColor(java.awt.Color.DARK_GRAY);
        int barrelLen = 20 + (level * 8);
        int barrelY = (level >= 4) ? 4 : 3; // Thicker barrel for high levels
        g2.fillRect(drawX + 10 + chassisWidth, center - (barrelY/2), barrelLen, barrelY);
        
        // Muzzle Brake (The "Heavy" look)
        if (level >= 3) {
            g2.setColor(java.awt.Color.BLACK);
            g2.fillRect(drawX + 10 + chassisWidth + barrelLen - 5, center - 6, 8, 12);
        }

        // 5. THE OPTICS (Cold, Digital Eye)
        if (level >= 2) {
            g2.setColor(obsidian);
            g2.fillRect(drawX + 18, center - 18, 15, 6); // Scope tube
            g2.setColor(coldCyan);
            g2.fillRect(drawX + 30, center - 17, 2, 4); // Glowing lens
            
            // Level 3+ Laser sight
            if (level >= 3) {
                g2.setColor(new java.awt.Color(255, 0, 255, 100));
                g2.drawLine(drawX + 35, center + 5, width, center + 5);
            }
        }

        // 6. LEVEL 4+ IONIC COOLING (Frost Vapor)
        if (level >= 4) {
            g2.setColor(coldCyan);
            g2.setStroke(new java.awt.BasicStroke(1.5f));
            // Draw "Cooling Pipes" along the top/bottom
            g2.drawLine(drawX + 12, center - 12, drawX + 12 + chassisWidth, center - 12);
            g2.drawLine(drawX + 12, center + 12, drawX + 12 + chassisWidth, center + 12);
            
            // Add a glow to the barrel
            g2.setColor(new java.awt.Color(0, 255, 255, 50));
            g2.fillRect(drawX + 10 + chassisWidth, center - 5, barrelLen, 10);
        }
        
        if(level >= 5)
        {
            // ==========================================
            // LEVEL 5: THE OMEGA RAIL (ZERO-POINT PLATFORM)
            // ==========================================
            
            // 1. FLOATING REAR ANCHORS (Mag-Lev Fins)
            g2.setColor(obsidian);
            int expansion = (animTimer > 0) ? 10 : 0; // The fins "flare" when firing
            
            // Top Fin
            g2.fillPolygon(new int[]{drawX, drawX + 15, drawX + 10}, 
                           new int[]{center - 5 - expansion, center - 25 - expansion, center - 5 - expansion}, 3);
            // Bottom Fin
            g2.fillPolygon(new int[]{drawX, drawX + 15, drawX + 10}, 
                           new int[]{center + 5 + expansion, center + 25 + expansion, center + 5 + expansion}, 3);

            // 2. THE VOID CORE (Floating Fractured Sphere)
            // It pulses between Magenta and Cyan
            float pulse = (float)(Math.sin(System.currentTimeMillis() / 200.0) * 0.5 + 0.5);
            g2.setColor(new java.awt.Color(255, 0, 255, 200));
           if (animTimer > 5) g2.setColor(java.awt.Color.WHITE); // Flash white
            
            g2.fillOval(drawX + 15, center - 12, 24, 24);
            g2.setColor(coldCyan);
            g2.setStroke(new java.awt.BasicStroke(2f));
            g2.drawOval(drawX + 15, center - 12, 24, 24); // Cyan containment ring

            // 3. THE LEVITATING ACCELERATOR RAILS
            g2.setColor(obsidian);
            int railWidth = 50;
            // The rails are split and float above/below the line of fire
            g2.fillRect(drawX + 40, center - 14 - (expansion/2), railWidth, 6); // Top Rail
            g2.fillRect(drawX + 40, center + 8 + (expansion/2), railWidth, 6);  // Bottom Rail
            
            // Neon cyan circuitry on the rails
            g2.setColor(coldCyan);
            g2.fillRect(drawX + 45, center - 12 - (expansion/2), 30, 2);
            g2.fillRect(drawX + 45, center + 10 + (expansion/2), 30, 2);

            // 4. THE SINGULARITY (The Scope / Eye)
            // A floating diamond above the core
            g2.setColor(obsidian);
            int[] eyeX = {drawX + 22, drawX + 27, drawX + 32, drawX + 27};
            int[] eyeY = {center - 25, center - 35, center - 25, center - 15};
            g2.fillPolygon(eyeX, eyeY, 4);
            g2.setColor(coldCyan);
            g2.fillOval(drawX + 25, center - 27, 4, 4); // The "Merciless Eye"

            // 5. THE MUZZLE IMPACT (Massive energy burst)
            if (animTimer > 0) {
                // Large cyan ring
                g2.setStroke(new java.awt.BasicStroke(3f));
                g2.setColor(new java.awt.Color(0, 255, 255, animTimer * 20));
                g2.drawOval(drawX + 80, center - 20, 40, 40);
                
                // Pure white core flash
                g2.setColor(new java.awt.Color(255, 255, 255, animTimer * 25));
                g2.fillRect(drawX + 40, center - 2, 60, 4);
            }
            
            // 6. AMBIENT FROST PARTICLES (Digital dust)
            g2.setColor(new java.awt.Color(150, 255, 255, 100));
            for(int i=0; i<3; i++) {
                int px = drawX + (int)(Math.random() * 80);
                int py = center + (int)(Math.random() * 40) - 20;
                g2.fillRect(px, py, 2, 2);
            }
        }


        // 7. MUZZLE FLASH
        if (animTimer > 7) {
            g2.setColor(java.awt.Color.WHITE);
            int flashX = drawX + 10 + chassisWidth + barrelLen;
            g2.fillOval(flashX, center - 10, 20, 20);
            g2.setColor(magentaCore);
            g2.fillOval(flashX + 5, center - 5, 10, 10);
        }

        g2.dispose();
        return img;
    }
    
    private static void fillArc(GreenfootImage img, int x, int y, int w, int h, int start, int angle, Color color) {
        java.awt.Graphics2D g = img.getAwtImage().createGraphics();
        g.setColor(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
        g.fillArc(x, y, w, h, start, angle);
        g.dispose();
    }

}