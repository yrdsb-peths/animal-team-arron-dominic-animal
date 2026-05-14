import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class ZombieYetiEnemy extends Enemy {
    
    // --- STATE MACHINE ---
    private enum State { MOVE, ATTACK, IDLE, DIE }
    private State currentState = State.MOVE;
    
    // --- ANIMATION FRAMES ---
    private GreenfootImage[] moveFrames;
    private GreenfootImage[] attackFrames;
    private GreenfootImage[] idleFrames;
    private GreenfootImage[] dieFrames;
    
    private int animIndex = 0;
    private GameTimer animTimer = new GameTimer(0.3, true); // Speed of animation (0.1s per frame)
    private boolean deathRewardsGiven = false;

    // Sprite scale (Change these if your boss is too big/small on the screen)
    private static final int SPRITE_WIDTH = 100;
    private static final int SPRITE_HEIGHT = 100;

    public ZombieYetiEnemy() {
        super(GameConfig.ZOMBIE_YETI_HP, GameConfig.ZOMBIE_YETI_DAMAGE, 
              GameConfig.ZOMBIE_YETI_SPEED, GameConfig.ZOMBIE_YETI_ATK_COOLDOWN, 0); 
        this.baseDrop = GameConfig.DROP_ZOMBIE_YETI;
        
        // 1. Load all sprite frames from your folders
        moveFrames   = loadFrames("Move3", "Move3");
        attackFrames = loadFrames("Attack3", "Attack3");
        idleFrames   = loadFrames("Idle3", "Idle3");
        dieFrames    = loadFrames("Die3", "Die3");

        // 2. Set the starting image
        if (moveFrames.length > 0) {
            setImage(moveFrames[0]);
        }
        
        animTimer.start(); 
    }

    /**
     * DYNAMIC LOADER: Looks in images/enemies/zombieboss/[folder]/[prefix]_000.png
     * It automatically stops loading when it runs out of numbered files.
     */
    private GreenfootImage[] loadFrames(String folder, String prefix) {
        List<GreenfootImage> frames = new ArrayList<>();
        int i = 0;
        while (true) {
            try {
                // Format the number to exactly 3 digits (000, 001, 002...)
                String formattedNum = String.format("%03d", i);
                String path = "enemies/zombieboss/" + folder + "/" + prefix + "_" + formattedNum + ".png";
                
                GreenfootImage img = new GreenfootImage(path);
                img.scale(SPRITE_WIDTH, SPRITE_HEIGHT); // Ensure the boss fits the lanes!
                frames.add(img);
                i++;
            } catch (IllegalArgumentException e) {
                // File not found -> We've loaded all available frames for this animation!
                if (i == 0) System.out.println("WARNING: Could not load " + folder + " frames!");
                break;
            }
        }
        return frames.toArray(new GreenfootImage[0]);
    }

    @Override
    protected void updateBehavior(MyWorld world) {
        // 1. Determine what the Zombie should be doing
        Unit unitInFront = (Unit) getOneIntersectingObject(Unit.class);
        
        State nextState;
        if (speedMultiplier <= 0.0f) {
            // Time Freeze ability is active!
            nextState = State.IDLE;
        } else if (unitInFront != null && !unitInFront.isDead() && unitInFront.isTargetable()) {
            // Blocked by a unit -> ATTACK
            nextState = State.ATTACK;
        } else {
            // Clear path -> MOVE
            nextState = State.MOVE;
        }
        
        // 2. Change state if necessary (resetting the frame counter to 0)
        if (currentState != nextState) {
            currentState = nextState;
            animIndex = 0;
        }
        
        // 3. Let the superclass actually perform the movement math & attack logic
        super.updateBehavior(world); 
        
        // 4. Tick the animation timer and swap the image
        animTimer.update(world);
        if (animTimer.isExpired()) {
            advanceAnimation();
        }
    }

    private void advanceAnimation() {
        GreenfootImage[] currentArray = null;
        
        switch (currentState) {
            case MOVE:   currentArray = moveFrames; break;
            case ATTACK: currentArray = attackFrames; break;
            case IDLE:   currentArray = idleFrames; break;
            default: break;
        }

        // Loop the animation
        if (currentArray != null && currentArray.length > 0) {
            animIndex = (animIndex + 1) % currentArray.length;
            setImage(currentArray[animIndex]);
        }
    }

    /**
     * OVERRIDES standard death behavior. 
     * Instead of instantly deleting the Yeti, it plays the death animation first.
     */
    @Override
    protected void handleDeath(MyWorld world) {
        // 1. Give money and score ONLY once
        if (!deathRewardsGiven) {
            CurrencyManager.earn(this.baseDrop);
            ScoreManager.addScore(150);
            world.startShake(15, 8); // Heavy thud on the screen
            
            currentState = State.DIE;
            animIndex = 0;
            deathRewardsGiven = true;
        }

        // 2. Step through the death animation
        animTimer.update(world);
        if (animTimer.isExpired() && dieFrames.length > 0) {
            if (animIndex < dieFrames.length - 1) {
                // Move to next death frame
                animIndex++;
                setImage(dieFrames[animIndex]);
            } else {
                // Animation finished! NOW we remove it from the world.
                world.removeObject(this);
            }
        } else if (dieFrames.length == 0) {
            // Failsafe in case Die frames failed to load
            world.removeObject(this); 
        }
    }
}
