import greenfoot.*;

public class AbilityManager {
    // Set loop to false so it stops after the duration
    private static GameTimer overclockTimer = new GameTimer(GameConfig.OVERCLOCK_DURATION, false);

    public static void update(MyWorld world) {
        overclockTimer.update(world);
        
        // Key shortcut support
        if (Greenfoot.isKeyDown("space")) {
            tryActivate(world);
        }
    }

    public static void tryActivate(MyWorld world) {
        // Only activate if NOT currently running
        if (!overclockTimer.isActive()) {
            if (CurrencyManager.spend(GameConfig.OVERCLOCK_COST)) {
                overclockTimer.reset(); // Set frames back to max
                overclockTimer.start(); // Set active = true
                world.addObject(new FloatingText("OVERCLOCK!", Color.CYAN, 40, 2), world.getWidth()/2, world.getHeight()/2);
            }
        }
    }

    public static boolean isOverclocked() {
        // It is only "overclocked" if the timer is active AND has time left
        return overclockTimer.isActive() && !overclockTimer.isExpired();
    }
}