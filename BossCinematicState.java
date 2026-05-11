import greenfoot.*;

public class BossCinematicState implements GameState {
    private int lifeFrames;
    private BossIntroOverlay overlay;
    private Runnable onComplete; // This stores the calamity logic

    public BossCinematicState(String bossName, int durationFrames, Runnable onComplete) {
        this.lifeFrames = durationFrames;
        this.overlay = new BossIntroOverlay(bossName);
        this.onComplete = onComplete;
    }
    
    public BossCinematicState(String bossName, int durationFrames) {
        this(bossName, durationFrames, null); 
    }

    @Override
    public void enter(MyWorld world) {
        world.addObject(overlay, world.getWidth()/2, world.getHeight()/2);
        world.startShake(lifeFrames, 12);
    }

    @Override
    public void update(MyWorld world) {
        lifeFrames--;
        if (lifeFrames <= 0) {
            world.getGSM().popState();
        }
    }

    @Override
    public void exit(MyWorld world) {
        world.removeObject(overlay);
        // TRIGGER THE CALAMITY LOGIC NOW
        if (onComplete != null) {
            onComplete.run();
        }
    }
}