import greenfoot.*;

public class SlowEffect implements StatusEffect {
    private GameTimer durationTimer;
    private float slowMultiplier;
    private String id;

    /**
     * @param duration How many seconds the slow lasts.
     * @param multiplier 0.5f = half speed, 0.2f = very slow, 0.0f = frozen.
     */
    public SlowEffect(double duration, float multiplier) {
        this.durationTimer = new GameTimer(duration, false);
        this.durationTimer.start();
        this.slowMultiplier = multiplier;
        this.id = "slow"; // Generic ID so slows don't stack infinitely
    }

    @Override
    public void update(Enemy enemy) {
        // 1. Slow them down
        enemy.speedMultiplier *= slowMultiplier;
        
        // 2. VISUAL TEST: Turn the enemy blue so we know it worked
        enemy.getImage().setColor(Color.BLUE);
        enemy.getImage().fillOval(0, 0, enemy.getImage().getWidth(), enemy.getImage().getHeight());
    
        // 3. Tick the timer
        MyWorld world = (MyWorld) enemy.getWorld();
        durationTimer.update(world);
    }

    @Override
    public boolean isExpired() {
        return durationTimer.isExpired();
    }

    @Override
    public String getId() {
        return id;
    }
}