import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class MenuState implements GameState {

    private List<Actor> uiElements = new ArrayList<>();

    @Override
    public void enter(MyWorld world) {
        // Set a background colour or image
        world.setBackground(new GreenfootImage(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT));
        world.getBackground().setColor(new Color(20, 20, 40)); // dark blue
        world.getBackground().fill();

        int midX = world.getWidth() / 2;
        int midY = world.getHeight() / 2;

        // Title
        addUI(world, new UIText("TOWER DEFENSE", GameConfig.s(60), Color.WHITE),
              midX, midY - GameConfig.s(80));

        // High score display
        int best = SaveManager.getInt("all_time_high");
        addUI(world, new UIText("BEST: " + best, GameConfig.s(22), Color.YELLOW),
              midX, midY);

        // Prompt
        addUI(world, new UIText("[ ENTER : PLAY ]", GameConfig.s(24), Color.GREEN),
              midX, midY + GameConfig.s(80));

        // Optional: start menu music
        // AudioManager.playLoop("menu_bgm");
    }

    @Override
    public void update(MyWorld world) {
        String key = Greenfoot.getKey();

        if ("enter".equals(key)) {
            world.getGSM().changeState(new PlayingState());
        }
    }

    @Override
    public void exit(MyWorld world) {
        world.removeObjects(uiElements);
        uiElements.clear();
        // AudioManager.stop("menu_bgm");
    }

    private void addUI(MyWorld world, Actor a, int x, int y) {
        world.addObject(a, x, y);
        uiElements.add(a);
    }
}