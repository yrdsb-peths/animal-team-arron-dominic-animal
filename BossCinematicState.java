import greenfoot.*;

public class BossCinematicState implements GameState {
    private int lifeFrames;
    private BossIntroOverlay overlay;

    public BossCinematicState(String bossName, int durationFrames) {
        this.lifeFrames = durationFrames;
        this.overlay = new BossIntroOverlay(bossName);
    }

    @Override
    public void enter(MyWorld world) {
        // Add the dark overlay
        world.addObject(overlay, world.getWidth()/2, world.getHeight()/2);
        // Start the camera shake!
        world.startShake(lifeFrames, 12); // Shakes for the full duration
    }

    @Override
    public void update(MyWorld world) {
        lifeFrames--;
        
        // Flicker effect for the first 0.5 seconds (frames 180 to 150)
        if (lifeFrames > 150) {
            if (lifeFrames % 10 < 5) overlay.getImage().setTransparency(100);
            else overlay.getImage().setTransparency(255);
        } else {
            overlay.getImage().setTransparency(255);
        }
        
        // Cinematic over! Resume the game.
        if (lifeFrames <= 0) {
            world.getGSM().popState();
        }
    }

    @Override
    public void exit(MyWorld world) {
        world.removeObject(overlay);
    }
}