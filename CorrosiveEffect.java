import greenfoot.*;

public class CorrosiveEffect implements StatusEffect {
    private GameTimer durationTimer;
    private float damageTakenMult;

    public CorrosiveEffect(double duration, float damageTakenMult) {
        this.durationTimer = new GameTimer(duration, false);
        this.durationTimer.start();
        this.damageTakenMult = damageTakenMult;
    }

    @Override
    public void update(Enemy enemy) {
        enemy.damageTakenMultiplier *= damageTakenMult;
        // Visual: Flash them bright sickly green!
        enemy.getImage().setColor(Color.GREEN); 
        durationTimer.update((MyWorld) enemy.getWorld());
    }

    @Override public boolean isExpired() { return durationTimer.isExpired(); }
    @Override public String getId() { return "corrosive"; }
}