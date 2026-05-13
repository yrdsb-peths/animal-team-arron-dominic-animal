import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class MenuState implements GameState {

    private List<Actor> uiElements = new ArrayList<>();

    @Override
    public void enter(MyWorld world) {
        // Load the permanent tech levels every time we enter the menu
        UnitRegistry.loadLevels();

        world.setBackground(new GreenfootImage(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT));
        world.getBackground().setColor(new Color(20, 20, 40)); 
        world.getBackground().fill();

        int midX = world.getWidth() / 2;
        int midY = world.getHeight() / 2;

        addUI(world, new UIText("TOWER DEFENSE", GameConfig.s(60), Color.WHITE), midX, midY - GameConfig.s(120));

        // Display Tech Points
        int tp = SaveManager.getTechPoints();
        addUI(world, new UIText("TECH POINTS: " + tp, GameConfig.s(22), Color.CYAN), midX, midY - GameConfig.s(60));

        // Determine Highest Level Unlocked (Default to 1)
        int highestLevel = SaveManager.getInt("highest_level");
        if (highestLevel == 0) highestLevel = 1;

        // Level Select Buttons (Green if unlocked, Gray if locked)
        addUI(world, new UIText("[ 1 ] PLAY LEVEL 1", 20, highestLevel >= 1 ? Color.GREEN : Color.GRAY), midX, midY);
        addUI(world, new UIText("[ 2 ] PLAY LEVEL 2", 20, highestLevel >= 2 ? Color.GREEN : Color.GRAY), midX, midY + 30);
        addUI(world, new UIText("[ 3 ] PLAY LEVEL 3", 20, highestLevel >= 3 ? Color.GREEN : Color.GRAY), midX, midY + 60);
        addUI(world, new UIText("[ 4 ] PLAY LEVEL 4", 20, highestLevel >= 4 ? Color.GREEN : Color.GRAY), midX, midY + 90);

        // Shop Prompt
        addUI(world, new UIText("[ T ] ENTER TECH LAB", 24, Color.YELLOW), midX, midY + 150);
    }

    @Override
    public void update(MyWorld world) {
        String key = Greenfoot.getKey();
        if (key == null) return;

        int highestLevel = SaveManager.getInt("highest_level");
        if (highestLevel == 0) highestLevel = 1;

        if ("1".equals(key) && highestLevel >= 1) world.getGSM().changeState(new PlayingState(1));
        if ("2".equals(key) && highestLevel >= 2) world.getGSM().changeState(new PlayingState(2));
        if ("3".equals(key) && highestLevel >= 3) world.getGSM().changeState(new PlayingState(3));
        if ("4".equals(key) && highestLevel >= 4) world.getGSM().changeState(new PlayingState(4));

        if ("t".equals(key)) {
            world.getGSM().pushState(new ShopState());
        }
    }

    @Override
    public void exit(MyWorld world) {
        world.removeObjects(uiElements);
        uiElements.clear();
    }

    private void addUI(MyWorld world, Actor a, int x, int y) {
        world.addObject(a, x, y);
        uiElements.add(a);
    }
}