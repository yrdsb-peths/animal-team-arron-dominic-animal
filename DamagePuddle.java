import greenfoot.*;
import java.util.List;

public class DamagePuddle extends Actor {
    private GameTimer lifeTimer;
    private GameTimer tickTimer;
    private int baseTickDamage;
    private int layers = 1;
    private int level;
    private int bubbleFrame = 0; 

    public DamagePuddle(double durationSeconds, int damagePerTick, int level) {
        this.level = level;
        this.baseTickDamage = damagePerTick;
        this.lifeTimer = new GameTimer(durationSeconds, false);
        this.lifeTimer.start();
        this.tickTimer = new GameTimer(0.5, true); 
        this.tickTimer.start();
        updateVisual();
    }

    public void addLayer() {
        if (layers < GameConfig.PUDDLE_MAX_LAYERS) layers++;
        lifeTimer.reset();
        lifeTimer.start();
        updateVisual();
    }

    private void updateVisual() {
        int size = GameConfig.s(80 + (layers * 5));
        GreenfootImage img = new GreenfootImage(size, size);
        long t = System.currentTimeMillis();

        if (level < 4) {
            // --- YOUR ORIGINAL BUBBLE BLOB (LVL 1-3) ---
            java.awt.Graphics2D g2 = img.getAwtImage().createGraphics();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            
            java.awt.Color liquidColor = (level >= 3) ? new java.awt.Color(150, 0, 200, 100 + (layers * 20)) : new java.awt.Color(50, 255, 50, 100 + (layers * 20));
            g2.setColor(liquidColor);
            g2.fillOval(5, 15, size-10, size-30);
            g2.fillOval(15, 5, size-30, size-10);
            
            bubbleFrame++;
            g2.setColor(new java.awt.Color(255, 255, 255, 100)); 
            for (int i = 0; i < layers + 1; i++) {
                int bx = (int)((Math.sin(bubbleFrame * 0.1 + i) * 0.3 + 0.5) * size);
                int by = (int)((Math.cos(bubbleFrame * 0.08 + i) * 0.3 + 0.5) * size);
                int bSize = (bubbleFrame + i * 10) % 15;
                if (bSize < 12) g2.drawOval(bx, by, bSize, bSize);
            }
            g2.dispose();
        } else {
            // --- THE MELTDOWN ZONE (LVL 4) ---
            img.setColor(new Color(0, 60, 20, 200));
            img.fillOval(5, 5, size-10, size-10);
            int pulse = (int)(Math.abs(Math.sin(t / 200.0)) * 100) + 100;
            img.setColor(new Color(50, 255, 50, pulse));
            img.drawOval(2, 2, size-5, size-5);
            // Hazard Logo
            img.setColor(new Color(0, 0, 0, 150));
            int mid = size/2;
            img.fillPolygon(new int[]{mid, mid + 10, mid - 10}, new int[]{mid - 10, mid + 10, mid + 10}, 3);
            // Rising Particles
            img.setColor(Color.WHITE);
            for(int i=0; i<3; i++) {
                int px = (int)((t/5 + i*30) % (size-20)) + 10;
                int py = (int)(Math.sin((t+i*100)/150.0) * 15) + mid;
                img.fillRect(px, py, 2, 2);
            }
        }
        setImage(img);
    }

    @Override
    public void act() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        lifeTimer.update(world);
        tickTimer.update(world);
        
        if (world.getActCount() % 5 == 0) updateVisual();

        if (lifeTimer.isExpired()) {
            world.removeObject(this);
            return;
        }

        if (tickTimer.isExpired()) {
            List<Enemy> enemiesInside = getIntersectingObjects(Enemy.class);
            for (Enemy e : enemiesInside) {
                e.takeDamage(baseTickDamage * layers, true);
                if (level >= GameConfig.ALCHEMIST_CORROSIVE_UNLOCK) e.applyEffect(new CorrosiveEffect(1.0, GameConfig.ALCHEMIST_DMG_AMP));
                if (level >= GameConfig.ALCHEMIST_STICKY_UNLOCK) e.applyEffect(new SlowEffect(0.5, 0.5f, 1.0f));
            }
        }
    }
}