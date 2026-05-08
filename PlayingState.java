import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class PlayingState implements GameState {

    /** Track UI elements so we can remove them cleanly on exit */
    private List<Actor> uiElements = new ArrayList<>();

    /** Score display label */
    private UIText scoreDisplay;

    /** Tracks when this session started (for playtime saving) */
    private long sessionStartTime;

    @Override
    public void enter(MyWorld world) {
        // ── Reset everything ──────────────────────────────────────────────────
        world.removeObjects(world.getObjects(null)); // clear all actors
        ScoreManager.reset();
        sessionStartTime = System.currentTimeMillis();

        // ── Background ────────────────────────────────────────────────────────
        world.setBackground(new GreenfootImage(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT));
        world.getBackground().setColor(new Color(30, 30, 50));
        world.getBackground().fill();

        // ── Spawn the player ──────────────────────────────────────────────────
        Player player = new Player();
        world.addObject(player, world.getWidth() / 2, world.getHeight() / 2);

        // ── UI ────────────────────────────────────────────────────────────────
        scoreDisplay = new UIText("SCORE: 0", GameConfig.s(22), Color.WHITE);
        world.addObject(scoreDisplay, GameConfig.s(60), GameConfig.s(20));
        uiElements.add(scoreDisplay);

        // ── Music ─────────────────────────────────────────────────────────────
        // AudioManager.playLoop("game_bgm");

        // ── Seed the random number generator ─────────────────────────────────
        GameRNG.randomize();
    }

    @Override
    public void update(MyWorld world) {
        // Update the score display every frame
        scoreDisplay.setText("SCORE: " + ScoreManager.getScore());

        // Add your per-frame gameplay logic here:
        // - Spawning enemies
        // - Difficulty scaling
        // - Checking win/lose conditions
    }

    @Override
    public void exit(MyWorld world) {
        // Save playtime
        long playedMs = System.currentTimeMillis() - sessionStartTime;
        int playedSeconds = (int)(playedMs / 1000);
        SaveManager.addInt("total_playtime", playedSeconds);
        SaveManager.save();

        // Update high score
        ScoreManager.updateHighScore();

        // Remove UI
        world.removeObjects(uiElements);
        uiElements.clear();

        // AudioManager.stop("game_bgm");
    }
}