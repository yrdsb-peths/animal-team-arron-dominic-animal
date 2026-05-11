import greenfoot.*;

public class PurpleRainController extends Actor {
    private int waveStarted;
    
    protected void addedToWorld(World world) {
        // This now works thanks to the GSM bridge we built!
        waveStarted = ((MyWorld)world).getGSM().getWaveNumber();
        
        // Visual: Make the background slightly purple
        world.getBackground().setColor(new Color(100, 0, 100, 30));
        world.getBackground().fill();
    }

    public void act() {
        MyWorld world = (MyWorld)getWorld();
        if (world == null) return;

        // 1. AUTO-REMOVE: If the wave number changes, the calamity is over!
        if (world.getGSM().getWaveNumber() != waveStarted) {
            // Clean up the background color back to normal
            world.getBackground().setColor(new Color(30, 30, 50));
            world.getBackground().fill();
            world.removeObject(this);
            return;
        }

        // 2. RAIN LOGIC: Spawn puddles randomly
        // Use the config name we set earlier: RAIN_CHANCE_PER_ACT
        if (GameRNG.getRandomNumber(1000) < GameConfig.RAIN_TICK_CHANCE) {
            int rx = GameRNG.getRandomNumber(world.getWidth());
            int ry = GameRNG.getRandomNumber(world.getHeight());
            
            // Drop a toxic puddle that lasts 3 seconds
            world.addObject(new DamagePuddle(3.0, 5), rx, ry);
            
            // Visual: A small purple drop falling (optional)
            // world.addObject(new FloatingText(".", Color.MAGENTA, 30, -5, 20), rx, ry - 100);
        }
    }
}