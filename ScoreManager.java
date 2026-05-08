import greenfoot.*;

public class ScoreManager {

    /** The player's current score in the ongoing game session. */
    private static int score = 0;

    /** The best score achieved since the project was launched. Not persisted to disk. */
    private static int highScore = 0;

    // ─────────────────────────────────────────────────────────────────────────
    // SCORE MANIPULATION
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Adds points to the current score.
     * Called by Roadroller.checkRemove() (+1 per car dodged),
     * Train.checkRemove() (+5 per ambulance dodged),
     * and TheWorldStand.act() (+2 per obstacle destroyed by the Stand).
     *
     * @param amount  Number of points to add (should be positive).
     */
    public static void addScore(int amount) {
        score += amount;
    }

    /**
     * Directly sets the score.
     * Used ONLY by the time-rewind system (Time_RewindManager) to restore
     * the score to its value at a past frame.  Do not call this from gameplay code.
     *
     * @param s  The score value to restore.
     */
    public static void setScore(int s) { score = s; }

    /**
     * Resets the current score to 0, copying it to highScore first if it's
     * a new record.  Called at the start of each new PlayingState.
     */
    public static void reset() {
        if (score > highScore) highScore = score;
        score = 0;
    }

    /**
     * Updates highScore if the current score exceeds it.
     * Called by GameOverState.enter() to freeze the best score for display.
     * reset() already does this, but GameOverState calls this separately
     * for clarity.
     */
   public static void updateHighScore() {
        
        if (score > highScore) highScore = score;
        
        // 2. THE FIX: Switch from DataManager to SaveManager
        int allTimeBest = SaveManager.getInt("all_time_high"); 
        if (score > allTimeBest) {
            SaveManager.setInt("all_time_high", score);
            SaveManager.save(); // Save to user_stats.txt
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GETTERS
    // ─────────────────────────────────────────────────────────────────────────

    /** @return The current running score. */
    public static int getScore()     { return score; }

    /** @return The best score achieved this session. */
    public static int getHighScore() { return highScore; }
    
    
}
