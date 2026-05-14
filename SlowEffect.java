import greenfoot.*;

public class SlowEffect implements StatusEffect {
    private GameTimer durationTimer;
    private float slowMultiplier;
    private float dmgDealtMultiplier;
    private String id;

    public SlowEffect(double duration, float multiplier, float dmgDealtMult) {
        this.durationTimer = new GameTimer(duration, false);
        this.durationTimer.start();
        this.slowMultiplier = multiplier;
        this.dmgDealtMultiplier = dmgDealtMult;
        this.id = "slow"; 
    }

       @Override
    public void update(Enemy enemy) {
        enemy.speedMultiplier *= slowMultiplier;
        enemy.damageDealtMultiplier *= dmgDealtMultiplier; // Apply weakness!
        
        // Prevent the crude blue circle from ruining the Yeti's sprite!
        if (!(enemy instanceof ZombieYetiEnemy)) {
            enemy.getImage().setColor(Color.BLUE);
            enemy.getImage().fillOval(0, 0, enemy.getImage().getWidth(), enemy.getImage().getHeight());
        }
    
        durationTimer.update((MyWorld) enemy.getWorld());
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