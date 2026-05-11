import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

public class PlayingState implements GameState {

    private List<Actor> uiElements = new ArrayList<>();
    private UIText waveDisplay; 
    private UIText goldDisplay;
    private long sessionStartTime;

    private WaveManager waveManager;
    private PlacementManager placementManager;
    private Base base; // Store reference to Base to show lives

    @Override
    public void enter(MyWorld world) {
        world.removeObjects(world.getObjects(null)); 
        ScoreManager.reset();
        sessionStartTime = System.currentTimeMillis();
    
        // 1. Setup Background with a visual separator
        GreenfootImage bg = new GreenfootImage(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        bg.setColor(new Color(30, 30, 50)); // Dark background
        bg.fill();
        // Draw the "Ground" line at Y=500 to separate UI from Game
        bg.setColor(Color.BLACK);
        bg.fillRect(0, GameConfig.PLAYABLE_HEIGHT, world.getWidth(), 5); 
        world.setBackground(bg);
    
        // 2. Spawn Base (Only as tall as the PLAYABLE area, not the whole world!)
        base = new Base();
        GreenfootImage baseImg = new GreenfootImage(50, GameConfig.PLAYABLE_HEIGHT);
        baseImg.setColor(Color.BLUE);
        baseImg.fillRect(0, 0, 50, GameConfig.PLAYABLE_HEIGHT);
        base.setImage(baseImg);
        // Center it vertically in the top 500 pixels
        world.addObject(base, GameConfig.BASE_X, GameConfig.PLAYABLE_HEIGHT / 2);
    
        // 3. Initialize Managers
        LaneManager.reset();
        CurrencyManager.reset();
        CalamityManager.reset();
        waveManager = new WaveManager();
        placementManager = new PlacementManager();
    
        // 4. Start the game logic
        waveManager.startFirstWave();
        GameConfig.GAME_SPEED = 1;
    
        // 5. TOP UI (Wave/Gold)
        waveDisplay = new UIText("WAVE: 1", GameConfig.s(22), Color.WHITE);
        goldDisplay = new UIText("GOLD: " + CurrencyManager.getGold(), GameConfig.s(22), Color.YELLOW);
        world.addObject(waveDisplay, GameConfig.s(90), GameConfig.s(20));
        world.addObject(goldDisplay, GameConfig.s(300), GameConfig.s(20));
        uiElements.add(waveDisplay);
        uiElements.add(goldDisplay);
    
        // 6. BOTTOM UI TRAY (Ability Buttons & Speed)
        // We use the new UI_TRAY_Y (e.g., 550) to center them in the bottom tray
        int trayY = GameConfig.UI_TRAY_Y;
        int btnSpacing = GameConfig.s(175);
        int startX = GameConfig.s(220);
    
        AbilityButton btn1 = new AbilityButton(1, "OVERCLOCK", GameConfig.OVERCLOCK_COST);
        AbilityButton btn2 = new AbilityButton(2, "TIME FREEZE", GameConfig.FREEZE_COST);
        AbilityButton btn3 = new AbilityButton(3, "TAC NUKE", GameConfig.NUKE_COST);
        UISpeedButton speedBtn = new UISpeedButton();
    
        world.addObject(btn1, startX, trayY);
        world.addObject(btn2, startX + btnSpacing, trayY);
        world.addObject(btn3, startX + (btnSpacing * 2), trayY);
        world.addObject(speedBtn, world.getWidth() - GameConfig.s(70), trayY);
    
        uiElements.add(btn1); uiElements.add(btn2); uiElements.add(btn3); uiElements.add(speedBtn);
        
        // 7. Unit Selection Menu
        createUnitMenu(world);
        GameRNG.randomize();
    }

    @Override
    public void update(MyWorld world) {
        // Run game logic
        waveManager.update(world);
        placementManager.update(world);
        AbilityManager.update(world);
        
        CalamityManager.update(world, waveManager.getWaveNumber());

        // Update UI displays
        waveDisplay.setText("WAVE: " + waveManager.getWaveNumber() + " | LIVES: " + base.lives);
        goldDisplay.setText("GOLD: " + CurrencyManager.getGold());
 
        // If Base dies, Game Over
        if (base.lives <= 0) {
            world.getGSM().changeState(new GameOverState());
        }
    }
    
    @Override
    public void exit(MyWorld world) {
        long playedMs = System.currentTimeMillis() - sessionStartTime;
        SaveManager.addInt("total_playtime", (int)(playedMs / 1000));
        ScoreManager.updateHighScore();

        world.removeObjects(uiElements);
        uiElements.clear();
    }
    
    private void createUnitMenu(MyWorld world) {
        UIScrollManager.reset();

        // Create the cards at their "Home" positions
        for (int i = 0; i < UnitRegistry.roster.size(); i++) {
            UnitRegistry.UnitData data = UnitRegistry.roster.get(i);
            
            // HomeY starts at Top Limit and moves down
            int homeY = GameConfig.MENU_TOP_LIMIT + (i * GameConfig.MENU_CARD_SPACING);
            
            UIUnitCard card = new UIUnitCard(
                data.id, data.cost, data.color, data.key, placementManager, homeY
            );
            world.addObject(card, GameConfig.MENU_X, homeY);
            uiElements.add(card);
        }
    }
        
    public int getWaveNumber() {
        return waveManager.getWaveNumber();
    }
    
    public WaveManager getWaveManager() {
        return waveManager;
    }
}