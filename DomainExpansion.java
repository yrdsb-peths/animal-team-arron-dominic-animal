import greenfoot.*;
import java.util.List;

public class DomainExpansion extends Actor {
    private int targetRadius;
    private int currentRadius = 0;
    private boolean isFullyOpen = false;
    private int lifeTimer = 300; // 5 Seconds
    
    public DomainExpansion(int maxRadius) {
        this.targetRadius = maxRadius;
        // Create an initial image so it's not invisible on frame 1
        setImage(new GreenfootImage(1, 1));
    }

    public void act() {
        MyWorld world = (MyWorld)getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        // 1. OPENING ANIMATION
        if (!isFullyOpen) {
            currentRadius += 12; 
            drawExpandingRing(currentRadius);
            if (currentRadius >= targetRadius) {
                currentRadius = targetRadius;
                isFullyOpen = true;
                drawMandala(currentRadius); 
            }
            return; 
        }

        // 2. ANIMATION: SPIN & PULSE
        setRotation(getRotation() + 2); 
        int pulse = 200 + (int)(Math.sin(System.currentTimeMillis() / 150.0) * 55);

        // 3. SUSTAIN LOGIC: Lasts forever if enemies are inside
        List<Enemy> enemies = getObjectsInRange(targetRadius, Enemy.class);
        if (enemies.isEmpty()) {
            lifeTimer--;
        }

        // 4. FADE OUT
        if (lifeTimer < 40) {
            pulse = (int)(lifeTimer * (255.0 / 40.0));
        }
        getImage().setTransparency(Math.max(0, Math.min(255, pulse)));

        // 5. BUFF LOGIC
        List<Unit> allies = getObjectsInRange(targetRadius, Unit.class);
        for (Unit u : allies) u.applyCommanderBuff();

        if (lifeTimer <= 0) world.removeObject(this);
    }

    private void drawExpandingRing(int r) {
        if (r < 2) return;
        GreenfootImage img = new GreenfootImage(r * 2 + 2, r * 2 + 2);
        img.setColor(new Color(0, 255, 255));
        img.drawOval(0, 0, r*2, r*2);
        setImage(img);
    }

    private void drawMandala(int r) {
        // Create canvas with 10px padding for glow
        int size = r * 2 + 20;
        GreenfootImage img = new GreenfootImage(size, size);
        int c = size / 2;
        
        // --- LAYER 1: OUTER NEON GLOW ---
        img.setColor(new Color(0, 100, 255, 50));
        img.fillOval(0, 0, size-1, size-1);
        
        // --- MAGIC RINGS ---
        img.setColor(new Color(0, 255, 255));
        img.drawOval(10, 10, r*2, r*2);         // Outer Heavy
        img.drawOval(12, 12, r*2-4, r*2-4);     // Outer Support
        img.drawOval(size/2 - r/2, size/2 - r/2, r, r); // Inner Containment Ring
        
        // --- THE STAR ---
        img.setColor(Color.WHITE);
        // Draw two overlapping triangles for a perfect hexagram
        for (int i = 0; i < 3; i++) {
            // Triangle 1
            int x1 = c + (int)(Math.cos(i * 2 * Math.PI/3) * r);
            int y1 = c + (int)(Math.sin(i * 2 * Math.PI/3) * r);
            int x2 = c + (int)(Math.cos((i+1) * 2 * Math.PI/3) * r);
            int y2 = c + (int)(Math.sin((i+1) * 2 * Math.PI/3) * r);
            img.drawLine(x1, y1, x2, y2);
            
            // Triangle 2 (Rotated)
            int offset = (int)(Math.PI);
            int x3 = c + (int)(Math.cos(i * 2 * Math.PI/3 + offset) * r);
            int y3 = c + (int)(Math.sin(i * 2 * Math.PI/3 + offset) * r);
            int x4 = c + (int)(Math.cos((i+1) * 2 * Math.PI/3 + offset) * r);
            int y4 = c + (int)(Math.sin((i+1) * 2 * Math.PI/3 + offset) * r);
            img.drawLine(x3, y3, x4, y4);
        }
            
        // --- LAYER 4: RUNIC SYMBOLS (Dots around the ring) ---
        img.setColor(new Color(0, 255, 255));
        for (int i = 0; i < 12; i++) {
            int x = c + (int)(Math.cos(i * Math.PI/6) * (r * 0.8));
            int y = c + (int)(Math.sin(i * Math.PI/6) * (r * 0.8));
            img.fillOval(x-4, y-4, 8, 8);
            img.setColor(Color.WHITE);
            img.drawOval(x-4, y-4, 8, 8);
        }

        // --- LAYER 5: CENTER CORE ---
        img.fillOval(c-10, c-10, 20, 20);
        
        setImage(img);
    }
}