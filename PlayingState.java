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
    private Base base; 
    private CampaignManager.LevelConfig levelConfig;
    private int levelId;

    public PlayingState(int levelId) {
        this.levelId = levelId;
        this.levelConfig = CampaignManager.getLevel(levelId);
    }

    @Override
    public void enter(MyWorld world) {
        world.removeObjects(world.getObjects(null)); 
        ScoreManager.reset();
        sessionStartTime = System.currentTimeMillis();
        
        UnitRegistry.loadLevels(); // Ensure units are at their saved tech levels
        
        GreenfootImage bg = new GreenfootImage(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        bg.setColor(new Color(30, 30, 50)); 
        bg.fill();
        bg.setColor(Color.BLACK);
        bg.fillRect(0, GameConfig.PLAYABLE_HEIGHT, world.getWidth(), 5); 
        world.setBackground(bg);
    
        base = new Base();
        GreenfootImage baseImg = new GreenfootImage(50, GameConfig.PLAYABLE_HEIGHT);
        baseImg.setColor(Color.BLUE);
        baseImg.fillRect(0, 0, 50, GameConfig.PLAYABLE_HEIGHT);
        base.setImage(baseImg);
        world.addObject(base, GameConfig.BASE_X, GameConfig.PLAYABLE_HEIGHT / 2);
    
        LaneManager.reset();
        CurrencyManager.reset();
        CalamityManager.reset();
        
        // Pass the level rules into WaveManager
        waveManager = new WaveManager();
        waveManager.setLevel(levelConfig); 
        waveManager.startFirstWave();
        
        placementManager = new PlacementManager();
        GameConfig.GAME_SPEED = 1;
    
        waveDisplay = new UIText("WAVE: 1 / " + levelConfig.maxWaves, GameConfig.s(22), Color.WHITE);
        goldDisplay = new UIText("GOLD: " + GameConfig.formatNumber(CurrencyManager.getGold()), GameConfig.s(22), Color.YELLOW);
        world.addObject(waveDisplay, GameConfig.s(90), GameConfig.s(20));
        world.addObject(goldDisplay, GameConfig.s(300), GameConfig.s(20));
        uiElements.add(waveDisplay);
        uiElements.add(goldDisplay);
    
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
        
        createUnitMenu(world);
        GameRNG.randomize();

        // Start the level with a big tutorial cinematic
        world.getGSM().pushState(new BossCinematicState(levelConfig.title + "\n" + levelConfig.tutorialText, 180));
    }

    @Override
    public void update(MyWorld world) {
        String key = Greenfoot.getKey();
        waveManager.update(world);
        placementManager.update(world);
        AbilityManager.update(world);
        CalamityManager.update(world, waveManager.getWaveNumber(), key);

        waveDisplay.setText("WAVE: " + waveManager.getWaveNumber() + " / " + levelConfig.maxWaves + " | LIVES: " + base.lives);
        goldDisplay.setText("GOLD: " + CurrencyManager.getGold());
 
        if (base.lives <= 0) {
            world.getGSM().changeState(new GameOverState(levelId)); // Pass levelId to Game Over
        }
    }
    
    @Override
    public void exit(MyWorld world) {
        world.removeObjects(uiElements);
        uiElements.clear();
    }
    
    private void createUnitMenu(MyWorld world) {
        UIScrollManager.reset();
        int visibleCount = 0;

        for (int i = 0; i < UnitRegistry.roster.size(); i++) {
            UnitRegistry.UnitData data = UnitRegistry.roster.get(i);
            
            // Only spawn the card if the CampaignManager says it's allowed in this level!
            if (!levelConfig.allowedUnits.contains(data.id)) continue;
            
            int homeY = GameConfig.MENU_TOP_LIMIT + (visibleCount * GameConfig.MENU_CARD_SPACING);
            UIUnitCard card = new UIUnitCard(data.id, data.cost, data.color, data.key, placementManager, homeY);
            world.addObject(card, GameConfig.MENU_X, homeY);
            uiElements.add(card);
            
            // Auto-select the first allowed unit
            if (visibleCount == 0) placementManager.setSelectedUnit(data.id);
            visibleCount++;
        }
    }
        
    public int getWaveNumber() { return waveManager.getWaveNumber(); }
    public WaveManager getWaveManager() { return waveManager; }
}