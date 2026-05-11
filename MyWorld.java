import greenfoot.*;

public class MyWorld extends World {

    private GameStateManager gsm;
    private int actCount = 0;

    public MyWorld() {
        super(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, 1, false);

        // Load saved data from disk (scores, settings, unlocks)
        SaveManager.load();

        // Create the state machine — it runs the whole game
        gsm = new GameStateManager(this);

       
        // Define the layer order (first = on top, last = at bottom)
        setPaintOrder(
            BossIntroOverlay.class, // Cinemtatics on very top
            FloatingText.class,     // Damage/Money popups
            BlockSpark.class,
            UIText.class,           // UI Text
            AbilityButton.class,    // UI Buttons
            UISpeedButton.class,
            UIUnitCard.class,       // Menu Cards
            FrostPulse.class,
            FogOverlay.class,       // NEW: Fog covers the grid
            PurpleRainController.class, // NEW: Purple tint covers the grid
            Projectile.class,       
            Unit.class,             
            Enemy.class             
        );

        // Pre-load all sounds (no lag on first play)
        AudioManager.init();

        // Start at the main menu
        gsm.pushState(new MenuState());
        
    }
    
    private int shakeTimer = 0;
    private int shakeIntensity = 0;
    
    public void startShake(int duration, int intensity) {
        this.shakeTimer = duration;
        this.shakeIntensity = intensity;
    }
    
    @Override
    public void act() {
        super.act(); // Essential for world act
        actCount++;
        gsm.update();
        
        // Handle Screen Shake
        if (shakeTimer > 0) {
            int xOffset = Greenfoot.getRandomNumber(shakeIntensity * 2) - shakeIntensity;
            int yOffset = Greenfoot.getRandomNumber(shakeIntensity * 2) - shakeIntensity;
            getBackground().drawImage(getBackground(), xOffset, yOffset); // Visual trick
            shakeTimer--;
            if (shakeTimer <= 0) {
                // Reset background to normal position after shake
                // Repaint the background color if you don't use an image:
                getBackground().setColor(new Color(30, 30, 50));
                getBackground().fill();
            }
        }
    }
    
    /** Gives actors access to the state machine */
    public GameStateManager getGSM() {
        return gsm;
    }
    
    /** Gives other classes access to the current "Time" of the world */
    public int getActCount() {
        return actCount;
    }
}