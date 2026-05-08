public class GameConfig {

    // ── World Size ────────────────────────────────────────────────────────────
    public static final int WORLD_WIDTH  = 800;   // adjust to your game
    public static final int WORLD_HEIGHT = 500;   // adjust to your game
    
    // Volume
    public static final int MASTER_VOLUME = 80;

    // ── Scale ─────────────────────────────────────────────────────────────────
    // Change this one number to scale the whole game up or down.
    // 1.0 = normal. 1.5 = 50% bigger. 0.75 = 25% smaller.
    public static final double SCALE = 1.0;

    /** Scales any pixel value by SCALE. Use for all sizes and positions. */
    public static int s(int value) {
        return (int)(value * SCALE);
    }

    // ── Game Constants ────────────────────────────────────────────────────────
    // Add your game's constants here as you build.
    // Example:
    // public static final int PLAYER_SPEED = s(4);
    // public static final int ENEMY_SPEED  = s(3);

    // ── Debug ─────────────────────────────────────────────────────────────────
    public static boolean DEBUG_MODE = false;
}