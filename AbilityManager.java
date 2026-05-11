import greenfoot.*;
import java.util.List;

public class AbilityManager {
    // ── TIMERS ──
    private static GameTimer overclockActive  = new GameTimer(GameConfig.OVERCLOCK_DURATION, false);
    private static GameTimer overclockCooldown= new GameTimer(GameConfig.OVERCLOCK_COOLDOWN, false);
    
    private static GameTimer nukeCooldown     = new GameTimer(GameConfig.NUKE_COOLDOWN, false);
    
    private static GameTimer freezeCooldown   = new GameTimer(GameConfig.FREEZE_COOLDOWN, false);

    public static void update(MyWorld world) {
        overclockActive.update(world);
        overclockCooldown.update(world);
        nukeCooldown.update(world);
        freezeCooldown.update(world);
        
        // Keyboard Shortcuts
        //if (Greenfoot.isKeyDown("1")) tryOverclock(world);
        //if (Greenfoot.isKeyDown("2")) tryTimeFreeze(world);
        //if (Greenfoot.isKeyDown("3")) tryNuke(world);
    }

    // ── ABILITY 1: OVERCLOCK ──
    public static void tryOverclock(MyWorld world) {
        if (!overclockCooldown.isActive() && CurrencyManager.spend(GameConfig.OVERCLOCK_COST)) {
            overclockActive.reset(); overclockActive.start();
            overclockCooldown.reset(); overclockCooldown.start();
            world.addObject(new FloatingText("OVERCLOCK!", Color.CYAN, 40, 2, 60), world.getWidth()/2, world.getHeight()/2);
        }
    }

    // ── ABILITY 2: TIME FREEZE ──
    public static void tryTimeFreeze(MyWorld world) {
        if (!freezeCooldown.isActive() && CurrencyManager.spend(GameConfig.FREEZE_COST)) {
            freezeCooldown.reset(); freezeCooldown.start();
            
            // Add visual overlay
            world.addObject(new TimeFreezeOverlay((int)(GameConfig.FREEZE_DURATION * 60)), world.getWidth()/2, world.getHeight()/2);
            world.addObject(new FloatingText("TIME FREEZE!", Color.BLUE, 50, 0, 60), world.getWidth()/2, world.getHeight()/2);
            
            // Apply a massive SlowEffect (0.0 multiplier = frozen) to ALL enemies currently on screen
            for (Enemy e : world.getObjects(Enemy.class)) {
                e.applyEffect(EffectFactory.createSlow(GameConfig.FREEZE_DURATION, 0.0f));
            }
        }
    }

    // ── ABILITY 3: TACTICAL NUKE ──
    public static void tryNuke(MyWorld world) {
        if (!nukeCooldown.isActive() && CurrencyManager.spend(GameConfig.NUKE_COST)) {
            nukeCooldown.reset(); nukeCooldown.start();
            
            world.addObject(new FloatingText("TACTICAL NUKE DEPLOYED", Color.RED, 50, 0, 100), world.getWidth()/2, 100);
            world.addObject(new Nuke(), world.getWidth()/2, world.getHeight()/2);
        }
    }

    // Getters for Buttons to check Cooldowns
    public static boolean isOverclocked() { return overclockActive.isActive() && !overclockActive.isExpired(); }
    public static double getOverclockCD() { return overclockCooldown.isActive() ? overclockCooldown.getSecondsRemaining() : 0; }
    public static double getFreezeCD() { return freezeCooldown.isActive() ? freezeCooldown.getSecondsRemaining() : 0; }
    public static double getNukeCD() { return nukeCooldown.isActive() ? nukeCooldown.getSecondsRemaining() : 0; }
}