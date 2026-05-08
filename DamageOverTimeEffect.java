import greenfoot.*;

public class DamageOverTimeEffect implements StatusEffect {
    private GameTimer durationTimer;
    private GameTimer tickTimer = new GameTimer(0.5, true); // Damage every 0.5 seconds
    private int damage;
    private String id;

    public DamageOverTimeEffect(double duration, int damage, String id) {
        this.durationTimer = new GameTimer(duration, false);
        this.durationTimer.start();
        this.tickTimer.start();
        this.damage = damage;
        this.id = id;
    }

    @Override
    public void update(Enemy enemy) {
        MyWorld world = (MyWorld) enemy.getWorld();
        durationTimer.update(world);
        tickTimer.update(world);

        if (tickTimer.isExpired()) {
            enemy.takeDamage(damage);
            // Visual feedback: flash the enemy purple
            enemy.getImage().setColor(Color.MAGENTA); 
        }
    }

    @Override
    public boolean isExpired() { return durationTimer.isExpired(); }

    @Override
    public String getId() { return id; }
}