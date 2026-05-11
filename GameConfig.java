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
    
    public static final int BASE_LIVES = 9;
    
    // Volume config
    public static final int MASTER_VOLUME = 80;
    
    //Economy Config
    public static final int STARTING_GOLD = 1500;
  
    // ── UNIT STATS ───────────────────────────────────────────────────────────
    public static final int BASIC_UNIT_COST = 50;
    public static final int BASIC_UNIT_HP = 3;
    public static final double BASIC_UNIT_COOLDOWN = 0.8; // Shoots every 0.8 seconds
    public static final int BASIC_UNIT_DAMAGE = 20;
    
    public static final int SNIPER_UNIT_COST = 100;
    public static final int SNIPER_UNIT_HP = 8;
    public static final double SNIPER_UNIT_COOLDOWN = 3.0; // Shoots slow
    public static final int SNIPER_UNIT_DAMAGE = 120;       // Hits very hard
    
    public static final int RAILGUN_UNIT_COST = 200;
    public static final int RAILGUN_UNIT_HP = 5;
    public static final double RAILGUN_UNIT_COOLDOWN = 2.0; // Shoots slow, but clears waves
    public static final int RAILGUN_UNIT_DAMAGE = 50;
    
    public static final int ALCHEMIST_UNIT_COST = 200;
    public static final int ALCHEMIST_UNIT_HP = 4;
    public static final double ALCHEMIST_UNIT_COOLDOWN = 4.0; 
    public static final int ALCHEMIST_UNIT_DAMAGE = 70; // High impact damage
    
    public static final int WALL_UNIT_COST = 50;
    public static final int WALL_UNIT_HP = 20;
    
    public static final int BIG_WALL_UNIT_COST = 500;
    public static final int BIG_WALL_UNIT_HP = 200;
    
    public static final int COWARD_UNIT_COST = 50;    // Very cheap!
    public static final int COWARD_UNIT_HP = 1;       // Dies instantly if caught off guard
    public static final double COWARD_UNIT_COOLDOWN = 1.0; 
    public static final int COWARD_UNIT_DAMAGE = 50;
    public static final int COWARD_SCARE_RANGE = s(50); // Distance before it hides

    // ── ENEMY STATS ──────────────────────────────────────────────────────────
    public static final int ENEMY_DROP = 20;
    
    //Basic enemy
    public static final int BASIC_ENEMY_HP = 100;
    public static final int BASIC_ENEMY_DAMAGE = 1;
    public static final float BASIC_ENEMY_SPEED = 1.5f;
    public static final double BASIC_ENEMY_ATK_COOLDOWN = 1.0; // Attacks every 1 sec
    
    //Tank Enemy
    public static final int TANK_ENEMY_HP = 500;
    public static final int TANK_ENEMY_DAMAGE = 2;
    public static final float TANK_ENEMY_SPEED = 0.5f; // Very slow
    public static final double TANK_ENEMY_ATK_COOLDOWN = 2.0; // Attacks every 2 seconds
        
    // SHIELD BEARER
    public static final int SHIELD_ENEMY_HP = 800;
    public static final int SHIELD_ENEMY_DAMAGE = 2;
    public static final float SHIELD_ENEMY_SPEED = 0.3f; 
    public static final double SHIELD_ENEMY_ATK_COOLDOWN = 4.0;
    
    // WALL KAMIKAZE (Sapper)
    public static final int KAMIKAZE_ENEMY_HP = 30; // Very weak!
    public static final int KAMIKAZE_ENEMY_DAMAGE = 10; 
    public static final int KAMIKAZE_WALL_DAMAGE = 150; // Massive damage to walls!
    public static final float KAMIKAZE_ENEMY_SPEED = 2.5f; // Super Fast
    public static final double KAMIKAZE_ENEMY_ATK_COOLDOWN = 0.5;
    
    // 
     
    public static final int SLIME_ENEMY_HP = 400;
    public static final float SLIME_ENEMY_SPEED = 0.6f;
    public static final int SLIME_ENEMY_DAMAGE = 1;
    public static final double SLIME_ENEMY_ATK_COOLDOWN = 2.0;
    
    // MINI SLIME
    public static final int MINISLIME_ENEMY_HP = 200;
    public static final int MINISLIME_ENEMY_DAMAGE = 1;
    public static final float MINISLIME_ENEMY_SPEED = 1.8f; // Fast!
    public static final double MINISLIME_ENEMY_ATK_COOLDOWN = 1.0;
    
    // ── SLIME SPLIT MULTIPLIERS ──
    public static final int SLIME_SPLIT_COUNT = 5;      // 1 Big Slime -> 3 Minis
    public static final int MINISLIME_SPLIT_COUNT = 3;  // 1 Mini Slime -> 2 Micros

    // ── MICRO SLIME STATS (Stage 3) ──
    public static final int MICRO_SLIME_HP = 100;          // little hp
    public static final int MICRO_SLIME_DAMAGE = 1;
    public static final float MICRO_SLIME_SPEED = 2.8f;  // Extremely fast!
    public static final double MICRO_SLIME_ATK_COOLDOWN = 0.5;
    
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
    // TANK (Already there, but let's organize)
    public static final int TANK_WAVE_MIN      = 2;
    public static final int TANK_CHANCE_START  = 5;
    public static final int TANK_CHANCE_GROWTH = 3;
    public static final int TANK_CHANCE_MAX    = 30;

    // SHIELD BEARER (Counters Snipers)
    public static final int SHIELD_WAVE_MIN      = 4;     // Starts appearing wave 4
    public static final int SHIELD_CHANCE_START  = 2;     // 2% chance at wave 4
    public static final int SHIELD_CHANCE_GROWTH = 1;     // force Alchemist use
    public static final int SHIELD_CHANCE_MAX    = 20;

    // KAMIKAZE / WALL KAMIKAZE (Threatens Walls)
    public static final int KAMIKAZE_WAVE_MIN      = 3;    // Starts appearing wave 3
    public static final int KAMIKAZE_CHANCE_START  = 3;   
    public static final int KAMIKAZE_CHANCE_GROWTH = 1;
    public static final int KAMIKAZE_CHANCE_MAX    = 15;   // Keep low at 15 so they are "surprises"

    // SLIME (Mini-Boss / Lane Splitter)
    public static final int SLIME_WAVE_MIN      = 6;      // Starts appearing wave 6
    public static final int SLIME_CHANCE_START  = 2;//2
    public static final int SLIME_CHANCE_GROWTH = 1;
    public static final int SLIME_CHANCE_MAX    = 10;     // Low max of 10 because they split into 5!
    
    //Effect Config 
        // ── SNIPER EFFECT STATS ──────────────────────────────────────────────────
        public static final double SNIPER_SLOW_DURATION = 5.0; // 3 seconds
        public static final float  SNIPER_SLOW_POWER    = 0.2f; // 20% speed
        
        public static final int SPLASH_RADIUS = s(200);
        public static final double PUDDLE_DURATION = 7.0;
        public static final int PUDDLE_MAX_LAYERS = 5;
        public static final int PUDDLE_TICK_DAMAGE = 10;
        public static final int LASER_DAMAGE = 100;
        
    // ── ECONOMY & DROPS ──────────────────────────────────────────────────
    public static final double INTEREST_RATE = 0.1; // 10% of saved gold per wave
    
    // Ability Costs
    public static final int OVERCLOCK_COST = 300;
    public static final double OVERCLOCK_DURATION = 5.0; // 5 seconds of 2x attack speed

    // Base Enemy Drops
    public static final int DROP_BASIC = 10;
    public static final int DROP_TANK = 35;
    public static final int DROP_SHIELD = 50;
    public static final int DROP_KAMIKAZE = 5; // Low drop, they blow themselves up!
    public static final int DROP_SLIME = 50;
    public static final int DROP_MINISLIME = 10;
    public static final int DROP_MICROSLIME = 5;

    // How much extra gold enemies drop per wave (0.10 = +10% per wave)
    public static final float DROP_GROWTH_PER_WAVE = 0.10f;
    
    // ── CALAMITY SETTINGS ──────────────────────────────────────────────────
    public static final int CALAMITY_INTERVAL = 5;      // Every 5 waves
    public static final int CRASH_DURATION = 5;         // Financial Crash lasts 5 waves
    public static final int CRASH_PRICE_MULT = 5;       // Prices x5
    public static final double DROUGHT_SPEED_MULT = 0.5; // 50% fire rate
    public static final int FOG_REVEAL_RANGE = s(150);  // Distance units "see" in fog
    public static final int RAIN_CHANCE_PER_ACT = 10;   // Chance for a raindrop per frame
    
    // ── UI SCROLL CONFIG ─────────────────────────────────────────────────────
    public static final int MENU_X = s(50);
    public static final int MENU_TOP_LIMIT = s(80);    // Highest a card can go
    public static final int MENU_BOTTOM_LIMIT = s(420); // Lowest a card can go
    public static final int MENU_CARD_SPACING = s(65);
}