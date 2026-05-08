import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class GameOverState implements GameState {

    private List<Actor> uiElements = new ArrayList<>();

    @Override
    public void enter(MyWorld world) {
        int midX = world.getWidth() / 2;
        int midY = world.getHeight() / 2;

        int finalScore = ScoreManager.getScore();
        int bestScore  = SaveManager.getInt("all_time_high");

        // Update best score if beaten
        if (finalScore > bestScore) {
            SaveManager.setInt("all_time_high", finalScore);
            SaveManager.save();
            bestScore = finalScore;
        }

        // "GAME OVER" title
        addUI(world, new UIText("GAME OVER", GameConfig.s(55), Color.RED),
              midX, midY - GameConfig.s(90));

        // Score
        addUI(world, new UIText("SCORE: " + finalScore, GameConfig.s(28), Color.WHITE),
              midX, midY - GameConfig.s(20));

        // Best
        addUI(world, new UIText("BEST:  " + bestScore, GameConfig.s(24), Color.YELLOW),
              midX, midY + GameConfig.s(30));

        // Replay prompt
        addUI(world, new UIText("[ ENTER : Play Again ]   [ ESC : Menu ]",
                                GameConfig.s(18), Color.CYAN),
              midX, midY + GameConfig.s(100));

        // AudioManager.play("game_over_sound");
    }

    @Override
    public void update(MyWorld world) {
        String key = Greenfoot.getKey();

        if ("enter".equals(key)) {
            world.getGSM().changeState(new PlayingState());
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