import greenfoot.*;
import java.util.List;

public class CalamityLaser extends Actor {
    private int timer = 120; // 2 seconds

    public CalamityLaser(int lane) {
        GreenfootImage img = new GreenfootImage(GameConfig.WORLD_WIDTH, 60);
        img.setColor(new Color(255, 0, 0, 150));
        img.fill();
        setImage(img);
    }

    public void act() {
        timer--;
        if (timer == 30) { // The moment of impact
            getImage().setTransparency(255);
            List<Unit> units = getIntersectingObjects(Unit.class);
            for(Unit u : units) u.die();
            List<Enemy> enemies = getIntersectingObjects(Enemy.class);
            for(Enemy e : enemies) e.die();
        }
        if (timer <= 0) getWorld().removeObject(this);
    }
}