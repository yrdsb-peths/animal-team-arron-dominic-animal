import greenfoot.*;

public class TimeFreezeOverlay extends Actor {
    private int lifeTimer;
    private int maxLife;

    public TimeFreezeOverlay(int durationFrames) {
        this.lifeTimer = durationFrames;
        this.maxLife = durationFrames;
        updateVisual(255);
    }

    public void act() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        lifeTimer--;

        // Fade out during the last second
        if (lifeTimer < 60) {
            int alpha = (int)((double)lifeTimer / 60 * 150);
            updateVisual(Math.max(0, alpha));
        }

        if (lifeTimer <= 0) world.removeObject(this);
    }

    private void updateVisual(int alpha) {
        GreenfootImage img = new GreenfootImage(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        // Deep icy blue filter with dynamic transparency
        img.setColor(new Color(0, 50, 150, Math.min(150, alpha))); 
        img.fill();
        setImage(img);
    }
}