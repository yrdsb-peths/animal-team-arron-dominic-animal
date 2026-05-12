import greenfoot.*;
import java.util.List;

public class IceStatue extends Actor {
    private int phase = 0; 
    private int timer = 0;
    private int maxHP;
    private int rotationSpeed = 0;
    private GreenfootImage baseImg;
    private double currentGlow = 0;

    public IceStatue(GreenfootImage enemyImg, int enemyMaxHP) {
        this.baseImg = enemyImg;
        this.maxHP = enemyMaxHP;
        // Create the high-quality crystal silhouette
        setImage(renderCrystalStatue(enemyImg, 0));
    }

    public void act() {
        timer++;
        MyWorld world = (MyWorld)getWorld();
        if (world == null) return;

        // Mist spawns regardless of phase
        if (timer % 4 == 0) {
            world.addObject(new AbsoluteZeroMist(), getX() + Greenfoot.getRandomNumber(30)-15, getY() + 20);
        }

        if (phase == 0) { 
            // 1. FASTER CRYSTALLIZATION
            // Increase the glow increment so it hits 1.0 within the shorter staging time
            currentGlow += (1.0 / GameConfig.SNIPER_ICE_STAGING_TIME); 
            setImage(renderCrystalStatue(baseImg, currentGlow));
            
            if (timer > GameConfig.SNIPER_ICE_STAGING_TIME) {
                phase = 1;
                timer = 0;
            }
        } 
        else if (phase == 1) { 
            // 2. FASTER SHIVERING
            // We multiply intensity by 2 so it feels more violent since it's shorter
            int intensity = (timer / 6); 
            setLocation(getX() + Greenfoot.getRandomNumber(intensity + 1) - (intensity/2), 
                        getY() + Greenfoot.getRandomNumber(intensity + 1) - (intensity/2));
            
            // Fast Pulse
            if (timer % 6 < 3) setImage(renderCrystalStatue(baseImg, 1.3));
            else setImage(renderCrystalStatue(baseImg, 0.7));

            if (timer > GameConfig.SNIPER_ICE_PRESSURE_TIME) {
                explode();
            }
        }
    }

    /**
     * The "Secret Sauce": Renders the enemy as a refracted crystal.
     */
    private GreenfootImage renderCrystalStatue(GreenfootImage original, double glowLevel) {
        int w = original.getWidth();
        int h = original.getHeight();
        GreenfootImage img = new GreenfootImage(w + 10, h + 10);
    
        // 1. Greenfoot logic uses Greenfoot's Color
        GreenfootImage silhouette = new GreenfootImage(original);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Color col = silhouette.getColorAt(x, y);
                if (col.getAlpha() > 20) {
                    // This uses greenfoot.Color because of your 'import greenfoot.*'
                    silhouette.setColorAt(x, y, new Color(20, 100, 200, 180));
                }
            }
        }
        img.drawImage(silhouette, 5, 5);
    
        // 2. Graphics2D logic MUST use java.awt.Color
        java.awt.Graphics2D g = img.getAwtImage().createGraphics();
        
        // FIX 1: Explicitly call java.awt.Color so Graphics2D accepts it
        g.setColor(new java.awt.Color(150, 255, 255, (int)Math.min(255, 100 * glowLevel)));
        
        for (int i = 0; i < 5; i++) {
            int rx = Greenfoot.getRandomNumber(w);
            int ry = Greenfoot.getRandomNumber(h);
            int[] px = {rx, rx + 15, rx - 5};
            int[] py = {ry, ry + 20, ry + 15};
            g.fillPolygon(px, py, 3);
        }
    
        // FIX 2: Explicitly call java.awt.Color here too
        g.setStroke(new java.awt.BasicStroke(2f));
        g.setColor(new java.awt.Color(255, 255, 255, 200));
        g.drawOval(5, 5, w, h);
    
        g.dispose();
        return img;
    }


    private void explode() {
        MyWorld world = (MyWorld)getWorld();
        int blastDamage = 150 + (int)(maxHP * GameConfig.SNIPER_ICE_EXPLODE_MULT);
        
        // Visual Nova
        world.addObject(new AbsoluteZeroNova(), getX(), getY());
        
        // Shatter Shards
        for (int i = 0; i < 20; i++) world.addObject(new IceShard(), getX(), getY());
        
        // Damage & Freeze Survivors
        List<Enemy> targets = getObjectsInRange(GameConfig.SNIPER_ICE_EXPLODE_RADIUS, Enemy.class);
        for (Enemy e : targets) {
            e.takeDamage(blastDamage, true);
            e.applyEffect(EffectFactory.createSlow(4.0, 0.1f, 0.5f)); // Heavy freeze + Weakness
        }

        world.startShake(15, 12);
        world.removeObject(this);
    }
}