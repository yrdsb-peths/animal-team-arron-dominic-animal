import greenfoot.*;

public class FogOverlay extends Actor {
    private int waveStarted;

    protected void addedToWorld(World world) {
        waveStarted = ((MyWorld)world).getGSM().getWaveNumber();
    }

    public void act() {
        MyWorld world = (MyWorld)getWorld();
        if (world == null) return;

        // Remove fog when the wave ends or a new calamity starts
        if (world.getGSM().getWaveNumber() != waveStarted) {
            CalamityManager.stopFog(); // We should add a way to turn off the fog logic
            world.removeObject(this);
        }
    }
}