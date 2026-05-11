import greenfoot.*;
import java.util.List;

public class HeatTrail extends Actor {
    private GameTimer lifeTimer = new GameTimer(3.0, false);
    private GameTimer tickTimer = new GameTimer(0.5, true);
    private int damage;

    public HeatTrail(int damage, int width) {
        this.damage = damage;
        GreenfootImage img = new GreenfootImage(width, 10);
        img.setColor(new Color(255, 100, 0, 150)); // Glowing Orange
        img.fillRect(0, 0, width, 10);
        setImage(img);
        
        lifeTimer.start();
        tickTimer.start();
    }

    public void act() {
        MyWorld world = (MyWorld)getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        lifeTimer.update(world);
        tickTimer.update(world);

        if (lifeTimer.isExpired()) {
            world.removeObject(this);
            return;
        }

        // Fade out smoothly
        int alpha = (int)((lifeTimer.getSecondsRemaining() / 3.0) * 150);
        getImage().setTransparency(Math.max(0, alpha));

        if (tickTimer.isExpired()) {
            List<Enemy> enemies = getIntersectingObjects(Enemy.class);
            for(Enemy e : enemies) e.takeDamage(damage, true);
        }
    }
}