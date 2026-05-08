import greenfoot.*;

public class MyWorld extends World {

    private GameStateManager gsm;

    public MyWorld() {
        super(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, 1, false);

        // Load saved data from disk (scores, settings, unlocks)
        SaveManager.load();

        // Create the state machine — it runs the whole game
        gsm = new GameStateManager(this);

       // Define the layer order (first = on top, last = at bottom)
        setPaintOrder(
            UIText.class,      // UI text always on top
            // Projectile.class,  // Uncomment when created
            // Unit.class,        // Uncomment when created
            Enemy.class
            // LaneTile.class     // Uncomment when created
        );

        // Pre-load all sounds (no lag on first play)
        AudioManager.init();

        // Start at the main menu
        gsm.pushState(new MenuState());
        
    }

    @Override
    public void act() {
        // The entire game flows through this one line, 60 times per second
        gsm.update();
    }

    /** Gives actors access to the state machine */
    public GameStateManager getGSM() {
        return gsm;
    }
}