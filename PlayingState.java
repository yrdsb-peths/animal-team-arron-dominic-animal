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

        world.setBackground(new GreenfootImage(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT));
        world.getBackground().setColor(new Color(30, 30, 50));
        world.getBackground().fill();

        // Spawn Base
        base = new Base();
        GreenfootImage baseImg = new GreenfootImage(50, GameConfig.WORLD_HEIGHT);
        baseImg.setColor(Color.BLUE);
        baseImg.fillRect(0, 0, 50, GameConfig.WORLD_HEIGHT);
        base.setImage(baseImg);
        world.addObject(base, GameConfig.BASE_X, GameConfig.WORLD_HEIGHT / 2);

        // Initialize Managers
        LaneManager.reset();
        CurrencyManager.reset();
        waveManager = new WaveManager();
        placementManager = new PlacementManager();

        // Start the game!
        waveManager.startFirstWave();
        GameConfig.GAME_SPEED = 1;//Rest game speed
        // UI
        waveDisplay = new UIText("WAVE: 1", GameConfig.s(22), Color.WHITE);
        goldDisplay = new UIText("GOLD: " + CurrencyManager.getGold(), GameConfig.s(22), Color.YELLOW);
        
        world.addObject(waveDisplay, GameConfig.s(90), GameConfig.s(20));
        world.addObject(goldDisplay, GameConfig.s(300), GameConfig.s(20));
        UISpeedButton speedBtn = new UISpeedButton();
        world.addObject(speedBtn, world.getWidth() - GameConfig.s(70), world.getHeight() - GameConfig.s(30));
        
        uiElements.add(waveDisplay);
        uiElements.add(goldDisplay);
        uiElements.add(speedBtn);
        
        AbilityButton ob = new AbilityButton("OVERCLOCK", GameConfig.OVERCLOCK_COST);
        world.addObject(ob, GameConfig.s(60), world.getHeight() - GameConfig.s(40));
        uiElements.add(ob);
        
        createUnitMenu(world);
        GameRNG.randomize();
    }

    @Override
    public void update(MyWorld world) {
        // Run game logic
        waveManager.update(world);
        placementManager.update(world);
        AbilityManager.update(world); // <--- ADD THIS LINE HERE

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
}