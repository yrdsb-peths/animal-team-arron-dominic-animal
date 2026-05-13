import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class GameOverState implements GameState {

    private List<Actor> uiElements = new ArrayList<>();
    private int levelId;

    public GameOverState(int levelId) {
        this.levelId = levelId;
    }

    @Override
    public void enter(MyWorld world) {
        int midX = world.getWidth() / 2;
        int midY = world.getHeight() / 2;

        addUI(world, new UIText("GAME OVER", GameConfig.s(55), Color.RED), midX, midY - GameConfig.s(90));
        addUI(world, new UIText("Base Destroyed on Level " + levelId, GameConfig.s(28), Color.WHITE), midX, midY - GameConfig.s(20));

        addUI(world, new UIText("[ ENTER : Try Again ]   [ ESC : Menu ]", GameConfig.s(18), Color.CYAN), midX, midY + GameConfig.s(100));
    }

    @Override
    public void update(MyWorld world) {
        String key = Greenfoot.getKey();

        if ("enter".equals(key)) {
            world.getGSM().changeState(new PlayingState(levelId)); // Restart same level
        } else if ("escape".equals(key)) {
            world.getGSM().changeState(new MenuState());
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