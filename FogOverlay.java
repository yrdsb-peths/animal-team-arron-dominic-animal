import greenfoot.*;

public class FogOverlay extends Actor {
    private int waveStarted;

    public FogOverlay() {
        // Milky, bright grey fog
        GreenfootImage img = new GreenfootImage(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        img.setColor(new Color(200, 200, 200, 160)); // 160 alpha = very thick, but you can see the grid
        img.fill();
        setImage(img);
    }

    protected void addedToWorld(World world) {
        waveStarted = ((MyWorld)world).getGSM().getWaveNumber();
    }

    public void act() {
        MyWorld world = (MyWorld)getWorld();
        // Pause logic if cinematic is playing
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        // Remove fog when the wave ends
        if (world.getGSM().getWaveNumber() != waveStarted) {
            CalamityManager.stopFog(); 
            world.removeObject(this);
        }
    }
}