import greenfoot.*;
import java.util.List;

public class SplashProjectile extends Actor {
    private Enemy target;
    private int damage;
    private int splashRadius; 
    private int speed = GameConfig.s(8);
    private int level; 
    private int rotationAngle = 0; 

    public SplashProjectile(Enemy target, int damage, int splashRadius, int level) {
        this.target = target;
        this.damage = damage;
        this.splashRadius = splashRadius;
        this.level = level;
        updateImage();
    }

    private void updateImage() {
        if (level < 5) {
            // --- YOUR ORIGINAL BEAKER (LVL 1-4) ---
            int size = 25;
            GreenfootImage img = new GreenfootImage(size, size);
            java.awt.Graphics2D g2 = img.getAwtImage().createGraphics();
            g2.rotate(Math.toRadians(rotationAngle), size/2, size/2);
            
            g2.setColor(new java.awt.Color(200, 255, 255, 180));
            g2.setStroke(new java.awt.BasicStroke(2f));
            g2.drawOval(5, 10, 15, 12); 
            g2.drawRect(10, 2, 5, 10);   
            
            java.awt.Color toxicGreen = (level >= 3) ? new java.awt.Color(180, 0, 255) : new java.awt.Color(50, 255, 50);
            g2.setColor(toxicGreen);
            g2.fillOval(7, 12, 11, 8);
            g2.dispose();
            setImage(img);
        } else {
            // --- THE OMEGA SINGULARITY (LVL 5) ---
            int size = 30;
            GreenfootImage img = new GreenfootImage(size, size);
            long t = System.currentTimeMillis();
            img.setColor(new Color(50, 255, 50, 150)); // Green Mist
            img.fillOval(0, 0, size-1, size-1);
            img.setColor(Color.WHITE); // Core
            img.fillOval(size/2 - 4, size/2 - 4, 8, 8);
            img.setColor(new Color(200, 0, 255)); // Purple Bolts
            for(int i=0; i<2; i++) {
                int rx = Greenfoot.getRandomNumber(size);
                int ry = Greenfoot.getRandomNumber(size);
                img.drawLine(size/2, size/2, rx, ry);
            }
            setImage(img);
        }
    }

    @Override
    public void act() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        if (target == null || target.getWorld() == null || target.isDead()) {
            world.removeObject(this);
            return;
        }

        // Animation logic
        if (level < 5) {
            rotationAngle += 20;
            updateImage();
        } else {
            setRotation(getRotation() + 25);
            if (world.getActCount() % 2 == 0) updateImage();
        }

        turnTowards(target.getX(), target.getY());
        move(speed * GameConfig.GAME_SPEED);

        // Trail
        if (world.getActCount() % 3 == 0) {
            Color trailColor = (level >= 5) ? Color.CYAN : (level >= 3) ? Color.MAGENTA : Color.GREEN;
            world.addObject(new BlockSpark(trailColor), getX(), getY());
        }

        if (intersects(target)) {
            List<Enemy> enemiesHit = getObjectsInRange(splashRadius, Enemy.class);
            for (Enemy e : enemiesHit) e.takeDamage(damage, GameConfig.ALCHEMIST_SPLASH_BYPASS);
            
            List<DamagePuddle> existing = getObjectsInRange(GameConfig.s(40), DamagePuddle.class);
            if (!existing.isEmpty()) existing.get(0).addLayer();
            else world.addObject(new DamagePuddle(GameConfig.PUDDLE_DURATION, GameConfig.PUDDLE_TICK_DAMAGE, level), getX(), getY());
            
            for(int i=0; i<5; i++) world.addObject(new RepairBit(), getX(), getY());
            world.removeObject(this);
        }
    }
}