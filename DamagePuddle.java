import greenfoot.*;
import java.util.List;

public class DamagePuddle extends Actor {
    private GameTimer lifeTimer;
    private GameTimer tickTimer;
    private int baseTickDamage;
    private int layers = 1;
    private int level;

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
        if (layers < GameConfig.PUDDLE_MAX_LAYERS) {
            layers++;
            updateVisual();
        }
        // Refresh the duration so the puddle stays longer if you keep hitting it
        lifeTimer.reset();
        lifeTimer.start();
    }

    private void updateVisual() {
        int size = GameConfig.s(80);
        GreenfootImage img = new GreenfootImage(size, size);
        
        // Color changes based on intensity
        Color puddleColor;
        if (layers == 1)      puddleColor = new Color(255, 0, 0, 80);   // Pale Red
        else if (layers == 2) puddleColor = new Color(255, 0, 0, 160);  // Bright Red
        else                  puddleColor = new Color(150, 0, 200, 200); // Deep Purple (Max)

        img.setColor(puddleColor);
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
            List<Enemy> enemiesInside = getIntersectingObjects(Enemy.class);
            for (Enemy e : enemiesInside) {
                e.takeDamage(baseTickDamage * layers, true);
                
                // CORROSIVE GAS MECHANIC
                if (level >= GameConfig.ALCHEMIST_CORROSIVE_UNLOCK) {
                    e.applyEffect(new CorrosiveEffect(1.0, GameConfig.ALCHEMIST_DMG_AMP));
                }
                if (level >= GameConfig.ALCHEMIST_STICKY_UNLOCK) {
                    e.applyEffect(new SlowEffect(0.5, 0.5f, 1.0f)); // 50% slow
                }
            }
        }
    }
}