// ==================================================
// FILE: ./SuperLaser.java (NEW FILE)
// ==================================================
import greenfoot.*;
import java.util.List;

public class SuperLaser extends Actor {
    private int damage;
    private int timer = 0;
    
    private int chargeTime = 40;  // Frames spent winding up
    private int blastTime = 15;   // Frames the actual damage beam stays
    private int fadeTime = 20;    // Frames fading out
    private int totalTime;
    
    private int originX; // The Railgun's X
    
    public SuperLaser(int damage, int originX) {
        this.damage = damage;
        this.originX = originX;
        this.totalTime = chargeTime + blastTime + fadeTime;
        setImage(new GreenfootImage(1, 1));
    }

    public void act() {
        MyWorld world = (MyWorld)getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;
        
        timer++;

        // PHASE 1: CHARGING
        if (timer < chargeTime) {
            updateVisualCharge();
            // Pull in energy sparks
            if (timer % 3 == 0) {
                int sparkX = getX() + Greenfoot.getRandomNumber(300);
                int sparkY = getY() + Greenfoot.getRandomNumber(60) - 30;
                world.addObject(new BlockSpark(Color.CYAN), sparkX, sparkY);
            }
        } 
        // PHASE 2: THE BLAST
        else if (timer == chargeTime) {
            world.startShake(blastTime + fadeTime, 20); // MASSIVE Shake
            world.addObject(new FloatingText("ION CANNON FIRED!", Color.CYAN, 40), world.getWidth()/2, getY() - 50);
            dealDamage(world);
            updateVisualBlast(255);
        } 
        else if (timer > chargeTime && timer < chargeTime + blastTime) {
            // Shaking the beam violently
            setLocation(getX(), getY() + Greenfoot.getRandomNumber(5) - 2);
            updateVisualBlast(255);
        } 
        // PHASE 3: FADE OUT
        else if (timer >= chargeTime + blastTime) {
            int fadeTick = timer - (chargeTime + blastTime);
            int alpha = 255 - (int)((double)fadeTick / fadeTime * 255);
            updateVisualBlast(Math.max(0, alpha));
        }

        if (timer >= totalTime) world.removeObject(this);
    }

    private void dealDamage(MyWorld world) {
        // Find every enemy currently on the board
        List<Enemy> enemies = world.getObjects(Enemy.class);
        for (Enemy e : enemies) {
            // If they are to the RIGHT of the Railgun AND in the same lane
            if (e.getX() >= originX && Math.abs(e.getY() - getY()) <= GameConfig.LANE_HEIGHT / 2 + 20) {
                e.takeDamage(damage, true); // True Damage (Bypasses shields)
            }
        }
    }

    private void updateVisualCharge() {
        int w = GameConfig.WORLD_WIDTH;
        GreenfootImage img = new GreenfootImage(w, 20);
        img.setColor(new Color(0, 255, 255, 100)); // Faint cyan aiming line
        img.fillRect(0, 9, w, 2);
        setImage(img);
    }

    private void updateVisualBlast(int alpha) {
        int w = GameConfig.WORLD_WIDTH;
        int h = 100; // HUGE beam
        GreenfootImage img = new GreenfootImage(w, h);
        
        // Outer Glow
        img.setColor(new Color(0, 150, 255, Math.min(100, alpha)));
        img.fillRect(0, 0, w, h);
        
        // Inner Beam
        img.setColor(new Color(0, 255, 255, alpha));
        img.fillRect(0, h/4, w, h/2);
        
        // White Hot Core
        img.setColor(new Color(255, 255, 255, alpha));
        img.fillRect(0, h/2 - 10, w, 20);
        
        setImage(img);
    }
}