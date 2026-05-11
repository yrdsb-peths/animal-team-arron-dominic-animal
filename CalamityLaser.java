import greenfoot.*;
import java.util.List;

public class CalamityLaser extends Actor {
    private enum State { AIMING, BLASTING, FADING }
    private State currentState = State.AIMING;
    
    private int timer;
    private int lane;
    private Color beamColor = new Color(255, 50, 50); // Red destructive energy

    public CalamityLaser(int lane) {
        this.lane = lane;
        this.timer = GameConfig.LASER_CHARGE_TIME;
        updateVisual();
    }

    public void act() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null) return;

        timer--;

        switch (currentState) {
            case AIMING:
                // Flicker effect: random transparency
                if (timer % 4 == 0) getImage().setTransparency(GameRNG.getRandomNumber(100) + 50);
                
                if (timer <= 0) {
                    currentState = State.BLASTING;
                    timer = GameConfig.LASER_BLAST_TIME;
                    world.startShake(20, 15); // BIG destructive shake
                    triggerDestruction(world);
                }
                break;

            case BLASTING:
                // Solid, bright, and slightly vibrating
                getImage().setTransparency(255);
                setLocation(getX(), getY() + (GameRNG.getRandomNumber(3) - 1)); // Shake the beam itself
                
                if (timer <= 0) {
                    currentState = State.FADING;
                    timer = GameConfig.LASER_FADE_TIME;
                }
                break;

            case FADING:
                // Shrink and disappear
                int alpha = (int)((double)timer / GameConfig.LASER_FADE_TIME * 255);
                getImage().setTransparency(Math.max(0, alpha));
                if (timer <= 0) world.removeObject(this);
                break;
        }
        
        updateVisual();
    }

    private void updateVisual() {
        int w = GameConfig.WORLD_WIDTH;
        int h = GameConfig.LASER_GLOW_WIDTH;
        GreenfootImage img = new GreenfootImage(w, h);

        if (currentState == State.AIMING) {
            // Just a thin warning line
            img.setColor(beamColor);
            img.fillRect(0, h/2 - 1, w, 3);
        } 
        else if (currentState == State.BLASTING || currentState == State.FADING) {
            // The Destructive Beam
            
            // 1. Outer Glow (Translucent)
            img.setColor(new Color(beamColor.getRed(), beamColor.getGreen(), beamColor.getBlue(), 120));
            img.fillRect(0, 0, w, h);
            
            // 2. Secondary Inner Glow
            img.setColor(new Color(beamColor.getRed(), 150, 150, 180));
            img.fillRect(0, h/4, w, h/2);
            
            // 3. The "White Hot" Core
            img.setColor(Color.WHITE);
            int coreH = GameConfig.LASER_CORE_WIDTH;
            img.fillRect(0, h/2 - coreH/2, w, coreH);
        }

        setImage(img);
    }

    private void triggerDestruction(MyWorld world) {
        // Kill everything in the lane
        List<Unit> units = getIntersectingObjects(Unit.class);
        for(Unit u : units) u.die();
        
        List<Enemy> enemies = getIntersectingObjects(Enemy.class);
        for(Enemy e : enemies) e.die();
        
        // Add "Energy Scorch" particles or text
        world.addObject(new FloatingText("ZAPPPPP！！！", Color.RED, 30), getX(), getY());
        
        // Spawn some MatrixGlitches along the line for extra "destructive" flavor
        for (int i = 0; i < 5; i++) {
            int rx = GameRNG.getRandomNumber(world.getWidth());
            world.addObject(new MatrixGlitch(), rx, getY());
        }
    }
}