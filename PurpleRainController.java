import greenfoot.*;

public class PurpleRainController extends Actor {
    private int waveStarted;
    
    public PurpleRainController() {
        // Visual: A full-screen translucent purple tint
        GreenfootImage img = new GreenfootImage(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        img.setColor(new Color(150, 0, 200, 60)); // 60 alpha = clearly visible but tinted
        img.fill();
        setImage(img);
    }
    
    protected void addedToWorld(World world) {
        waveStarted = ((MyWorld)world).getGSM().getWaveNumber();
    }

    public void act() {
        MyWorld world = (MyWorld)getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        // Auto remove when wave changes
        if (world.getGSM().getWaveNumber() != waveStarted) {
            world.removeObject(this);
            return;
        }

        // Puddle Spawn Chance
        if (GameRNG.getRandomNumber(100) < 5) {
            // Only spawn on the playable grid, not behind the UI menu!
            int minX = GameConfig.GRID_START_X;
            int rx = minX + GameRNG.getRandomNumber(world.getWidth() - minX);
            int ry = GameRNG.getRandomNumber(world.getHeight());
            
            // Drop the toxic ACID puddle! (Lasts 4 seconds, deals 1 damage every 0.5s)
            world.addObject(new AcidPuddle(4.0, 1), rx, ry);
        }
    }
}