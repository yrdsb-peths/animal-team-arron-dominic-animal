public class GameConfig {

    // ── Debug ─────────────────────────────────────────────────────────────────
    public static boolean DEBUG_MODE = false;
    
    // ── Game Engine Speed ─────────────────────────────────────────────────────
    public static int GAME_SPEED = 1; // 1x, 2x, or 4x speed

    // ── World Size ────────────────────────────────────────────────────────────
    public static final int WORLD_WIDTH  = 800;   // adjust to your game
    public static final int WORLD_HEIGHT = 500;   // adjust to your game
    
      // ── Scale ─────────────────────────────────────────────────────────────────
    // Change this one number to scale the whole game up or down.
    // 1.0 = normal. 1.5 = 50% bigger. 0.75 = 25% smaller.
    public static final double SCALE = 1.0;

    /** Scales any pixel value by SCALE. Use for all sizes and positions. */
    public static int s(int value) {
        return (int)(value * SCALE);
    }

    // Lane config
    
    public static final int NUM_LANES = 5;
    public static final int LANE_HEIGHT = WORLD_HEIGHT / NUM_LANES;
    public static final int GRID_COLS = 9;        // columns units can be placed in
    public static final int BASE_X = s(60);       // left-edge X where Base lives
    public static final int GRID_START_X = s(120); // first placeable column
    public static final int GRID_COL_WIDTH = (WORLD_WIDTH - GRID_START_X) / GRID_COLS;
    
    //Base config
    
    public static final int BASE_LIVES = 3;
    
    // Volume config
    public static final int MASTER_VOLUME = 80;
    
    //Economy Config
    public static final int STARTING_GOLD = 350;
  
    // ── UNIT STATS ───────────────────────────────────────────────────────────
    public static final int BASIC_UNIT_COST = 30;
    public static final int BASIC_UNIT_HP = 3;
    public static final double BASIC_UNIT_COOLDOWN = 1.0; // Shoots every 1.5 seconds
    public static final int BASIC_UNIT_DAMAGE = 20;
    
    public static final int SNIPER_UNIT_COST = 100;
    public static final int SNIPER_UNIT_HP = 8;
    public static final double SNIPER_UNIT_COOLDOWN = 3.0; // Shoots slow
    public static final int SNIPER_UNIT_DAMAGE = 120;       // Hits very hard

    // ── ENEMY STATS ──────────────────────────────────────────────────────────
    public static final int ENEMY_DROP = 20;
    public static final int BASIC_ENEMY_HP = 100;
    public static final int BASIC_ENEMY_DAMAGE = 1;
    public static final float BASIC_ENEMY_SPEED = 1.5f;
    public static final double BASIC_ENEMY_ATK_COOLDOWN = 1.0; // Attacks every 1 sec
    
    public static final int TANK_ENEMY_HP = 500;
    public static final int TANK_ENEMY_DAMAGE = 2;
    public static final float TANK_ENEMY_SPEED = 0.5f; // Very slow
    public static final double TANK_ENEMY_ATK_COOLDOWN = 2.0; // Attacks every 2 seconds
    
    // Wave/spawn Config
    public static final double WAVE_BREAK_TIME = 5.0;
    public static final int WAVE_CLEAR_BONUS = 100;
    public static final double SPAWN_INTERVAL_BASE = 1.5; // Base seconds between spawns
    
    // ── DYNAMIC DIFFICULTY SCALING ──────────────────────────────────────────
    // Higher numbers = Much harder game.
    
    /** How much Enemy HP increases per wave (0.10 = +10% per wave) */
    public static final float  DIFF_HP_GROWTH       = 0.10f;
    
    /** How much Enemy Damage increases per wave (0.05 = +5% per wave) */
    public static final float  DIFF_DMG_GROWTH      = 0.05f;
    
    /** How many more enemies per wave (1.5 = 15 more enemies every 10 waves) */
    public static final double DIFF_QUANTITY_GROWTH = 1.5;
    
    /** How much faster enemies spawn per wave (0.08 seconds faster every wave) */
    public static final double DIFF_PACE_SPEEDUP    = 0.08;
    
    /** The fastest enemies are allowed to spawn (don't set to 0 or game crashes!) */
    public static final double MIN_SPAWN_INTERVAL   = 0.5;
    // ── ENEMY VARIETY SCALING ────────────────────────────────────────────────
    /** Starting % chance for a Tank on Wave 1 */
    public static final int TANK_CHANCE_START = 5;
    
    /** % Increase in Tank chance per wave */
    public static final int TANK_CHANCE_GROWTH = 3;
    
    /** Maximum % chance for a Tank (prevents basic enemies from disappearing) */
    public static final int TANK_CHANCE_MAX    = 40;
    //Effect Config 
        // ── SNIPER EFFECT STATS ──────────────────────────────────────────────────
        public static final double SNIPER_SLOW_DURATION = 3.0; // 3 seconds
        public static final float  SNIPER_SLOW_POWER    = 0.5f; // 50% speed
}