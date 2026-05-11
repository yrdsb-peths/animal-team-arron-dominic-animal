import greenfoot.*;
import java.util.List;

public class AcidPuddle extends Actor {
    private GameTimer lifeTimer;
    private GameTimer tickTimer;
    private int damage;

    public AcidPuddle(double durationSeconds, int damagePerTick) {
        this.damage = damagePerTick;
        this.lifeTimer = new GameTimer(durationSeconds, false);
        this.lifeTimer.start();
        
        this.tickTimer = new GameTimer(0.5, true); 
        this.tickTimer.start();

        // Visual: A bright toxic purple puddle
        int size = GameConfig.s(70);
        GreenfootImage img = new GreenfootImage(size, size);
        img.setColor(new Color(180, 0, 255, 150)); 
        img.fillOval(0, 0, size, size);
        setImage(img);
    }

    @Override
    public void act() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        lifeTimer.update(world);
        tickTimer.update(world);

        if (lifeTimer.isExpired()) {
            world.removeObject(this);
            return;
        }

        if (tickTimer.isExpired()) {
            // HURTS YOUR UNITS, NOT ENEMIES!
            List<Unit> unitsInside = getIntersectingObjects(Unit.class);
            for (Unit u : unitsInside) {
                u.takeDamage(damage); 
            }
        }
    }
}