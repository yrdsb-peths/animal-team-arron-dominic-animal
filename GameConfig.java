public class GameConfig {

   // ── DEBUG SETTINGS ──────────────────────────────────────────────────
    public static final boolean DEBUG_MODE = true; // Set to false for final build
      
    //Economy Config
    public static final int STARTING_GOLD = 200000;
    
    // ── CALAMITY MASTER SETTINGS ────────────────────────────────────────
    public static final int CALAMITY_INTERVAL = 5;      // Happens every 5 waves
    public static final int TOTAL_CALAMITIES = 11;       // Update this when adding new ones!

    // ── SPECIFIC CALAMITY STATS ─────────────────────────────────────────
    public static final double DROUGHT_SPEED_MULT = 0.5;
    public static final int    CRASH_DURATION     = 5;
    public static final int    CRASH_PRICE_MULT   = 5;
    public static final int    RAIN_TICK_CHANCE   = 10; // Out of 1000
    public static final int    FOG_REVEAL_RANGE   = s(150);
    
    
    // Laser
    public static final int LASER_CHARGE_TIME = 90;  // Aiming phase (flicker)
    public static final int LASER_BLAST_TIME  = 20;  // How long the "Big Beam" stays
    public static final int LASER_FADE_TIME   = 15;  // Dissipation phase
    public static final int LASER_CORE_WIDTH  = s(20); // The white center
    public static final int LASER_GLOW_WIDTH  = s(70); // The colored outer beam
    
    public static final double EMP_DURATION       = 10.0; // Seconds units are disabled
    public static final int    BLOODMOON_SWARM    = 5;   // Number of extra enemies spawned
    
    // Kamikaze Swarm (The Quantity Threat)
    public static final double KAMIKAZE_SWARM_COUNT_MULT = 5.0;  // 10x normal wave size!
    public static final int    KAMIKAZE_SWARM_SHIELD     = 3;    // Ignores 3 shots
    
    public static final double SHIELD_SWARM_COUNT_MULT = 1.0;  // normal wave size
    public static final int RUMBLING_BOMBER_CHANCE = 35;
    
   // Blood Moon (The Endurance Slime Swarm)
    public static final double BLOODMOON_COUNT_MULT      = 4.0;  // 4x normal wave size (lasts forever!)
    public static final double BLOODMOON_SPAWN_RATE      = 0.25; // Break the 0.5s cap! 4 slimes per second!
    public static final int    BLOODMOON_SHIELD          = 2;    // Ignores 2 shots
    public static final double BLOODMOON_HP_MULT         = 2.0;  // 2x Base HP (on top of scaling!)
    public static final float  BLOODMOON_SPEED_BOOST     = 1.3f; // 30% faster

    // ── SUPERIOR SLIME SCALING ─────────────────────────────────────────
    // Slimes scale faster than normal enemies to stay terrifying
    public static final float  SLIME_BONUS_HP_GROWTH     = 0.25f; // +25% HP per wave (instead of normal 10%)
    public static final float  SLIME_BONUS_DMG_GROWTH    = 0.10f; // +10% Dmg per wave (instead of normal 5%)
    
    // ── CALAMITY DEBUG KEYS ─────────────────────────────────────────────
    public static final String DEBUG_KEY_QUAKE     = "e";
    public static final String DEBUG_KEY_MATRIX    = "m";
    public static final String DEBUG_KEY_DROUGHT   = "d";
    public static final String DEBUG_KEY_LASER     = "l";
    public static final String DEBUG_KEY_RAIN      = "p";
    public static final String DEBUG_KEY_CRASH     = "f";
    public static final String DEBUG_KEY_FOG       = "g";
    public static final String DEBUG_KEY_EMP       = "t"; 
    public static final String DEBUG_KEY_BLOODMOON = "b";
    public static final String DEBUG_KEY_KAMIKAZE   = "k"; 
    public static final String DEBUG_KEY_RUMBLING    = "h"; 
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
  
    // ── UNIT STATS ───────────────────────────────────────────────────────────
    public static final int BASIC_UNIT_COST = 50;
    public static final int BASIC_UNIT_HP = 3;
    public static final double BASIC_UNIT_COOLDOWN = 0.8; // Shoots every 0.8 seconds
    public static final int BASIC_UNIT_DAMAGE = 30;
    
    public static final int SNIPER_UNIT_COST = 100;
    public static final int SNIPER_UNIT_HP = 8;
    public static final double SNIPER_UNIT_COOLDOWN = 3.0; // Shoots slow
    public static final int SNIPER_UNIT_DAMAGE = 120;       // Hits very hard
     public static final int SNIPER_SLOW_RADIUS = s(120);
     
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
    
    // ── ATTACK & SHIELD RULES ──────────────────────────────────────────────
    public static final boolean BASIC_PROJECTILE_BYPASS   = false; // Standard bullets blocked
    public static final boolean SNIPER_PROJECTILE_BYPASS  = false; // Sniper bullets blocked
    public static final boolean RAILGUN_LASER_BYPASS      = true;  // Lasers ignore shields
    public static final boolean ALCHEMIST_SPLASH_BYPASS   = true;  // Explosions ignore shields
    public static final boolean ALCHEMIST_PUDDLE_BYPASS   = true;  // Puddles ignore shields
    public static final boolean BACKSTAB_ALWAYS_BYPASS   = true;  // Hitting the back ignores shields

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
    public static final int KAMIKAZE_ENEMY_HP = 50; // Very weak!
    public static final int KAMIKAZE_ENEMY_DAMAGE = 10; 
    public static final int KAMIKAZE_WALL_DAMAGE = 150; // Massive damage to walls!
    public static final float KAMIKAZE_ENEMY_SPEED = 2.5f; // Super Fast
    public static final double KAMIKAZE_ENEMY_ATK_COOLDOWN = 0.5;
    
    //Heavy shield
    public static final int HEAVY_SHIELD_HP = 3000;
    public static final int HEAVY_SHIELD_DAMAGE = 5;
    public static final float HEAVY_SHIELD_SPEED = 0.15f; 
    public static final double HEAVY_SHIELD_ATK_COOLDOWN = 2.0;
     
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
    public static final double INTEREST_RATE = 0.05; // 5% of saved gold per wave
    
    // ── ABILITIES ────────────────────────────────────────────────────────
    public static final int OVERCLOCK_COST = 300;
    public static final double OVERCLOCK_DURATION = 5.0; 
    public static final double OVERCLOCK_COOLDOWN = 10.0; 

    public static final int NUKE_COST = 5000;
    public static final double NUKE_COOLDOWN = 20.0; // Long cooldown!

    public static final int FREEZE_COST = 1500;
    public static final double FREEZE_DURATION = 6.0; // 6 seconds of frozen enemies
    public static final double FREEZE_COOLDOWN = 15.0;
    
    // Base Enemy Drops
    public static final int DROP_BASIC = 10;
    public static final int DROP_TANK = 35;
    public static final int DROP_SHIELD = 50;
    public static final int DROP_KAMIKAZE = 5; // Low drop, they blow themselves up!
    public static final int DROP_SLIME = 50;
    public static final int DROP_MINISLIME = 10;
    public static final int DROP_MICROSLIME = 5;
    public static final int DROP_HEAVY_SHIELD = 200; 

    // How much extra gold enemies drop per wave (0.10 = +10% per wave)
    public static final float DROP_GROWTH_PER_WAVE = 0.10f;
    
    
    // ── UI SCROLL CONFIG ─────────────────────────────────────────────────────
    public static final int MENU_X = s(50);
    public static final int MENU_TOP_LIMIT = s(80);    // Highest a card can go
    public static final int MENU_BOTTOM_LIMIT = s(420); // Lowest a card can go
    public static final int MENU_CARD_SPACING = s(65);
}