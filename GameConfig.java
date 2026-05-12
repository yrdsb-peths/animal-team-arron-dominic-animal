import greenfoot.*;

public class GameConfig {

   // ── DEBUG SETTINGS ──────────────────────────────────────────────────
    public static final boolean DEBUG_MODE = true; // Set to false for final build
          
    public static final String DEBUG_KEY_WAVE_SKIP_1  = "[";
    public static final String DEBUG_KEY_WAVE_SKIP_10 = "]";
    //Economy Config
    public static final int STARTING_GOLD = 20000000;
    
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
    public static final double KAMIKAZE_SWARM_COUNT_MULT = 1.5;  // 1.5x normal wave size!
    public static final int    KAMIKAZE_SWARM_SHIELD     = 3;    // Ignores 3 shots
    
    public static final double SHIELD_SWARM_COUNT_MULT = 1.0;  // normal wave size
    public static final int RUMBLING_BOMBER_CHANCE = 35;
    
   // Blood Moon (The Endurance Slime Swarm)
    public static final double BLOODMOON_COUNT_MULT      = 1.5;  // 1.5x normal wave size (lasts forever!)
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

    // ── World & UI Layout ─────────────────────────────────────────────────────
    public static final int PLAYABLE_HEIGHT = 520;   // Increase this for taller lanes!
    public static final int NUM_LANES = 5;           // Fewer lanes = taller lanes!
    public static final int WORLD_WIDTH  = 800;
    public static final int UI_TRAY_HEIGHT = s(80);  // Make this shorter!
    
    // Automatically calculates perfect layout (DO NOT CHANGE THESE MATH LINES)
    public static final int UI_TRAY_Y = PLAYABLE_HEIGHT + (UI_TRAY_HEIGHT / 2);
    public static final int WORLD_HEIGHT = PLAYABLE_HEIGHT + UI_TRAY_HEIGHT; 
    public static final int LANE_HEIGHT = PLAYABLE_HEIGHT / NUM_LANES; 
    
    // ── Visual Scaling ────────────────────────────────────────────────────────
    public static final int UNIT_SIZE = s(50); // Change this to make your plants bigger/smaller!
    public static final double SCALE = 1.0;
    public static int s(int value) { return (int)(value * SCALE); }

    // ── UI SCROLL CONFIG ──────────────────────────────────────────────────────
    public static final int MENU_X = s(50);
    public static final int MENU_TOP_LIMIT = s(80);    
    // DYNAMIC: Scroll naturally stops exactly where the UI Tray begins!
    public static final int MENU_BOTTOM_LIMIT = PLAYABLE_HEIGHT - s(40); 
    public static final int MENU_CARD_SPACING = s(65);    

    // Lane config
    
    public static final int GRID_COLS = 9;        // columns units can be placed in
    public static final int BASE_X = s(60);       // left-edge X where Base lives
    public static final int GRID_START_X = s(120); // first placeable column
    public static final int GRID_COL_WIDTH = (WORLD_WIDTH - GRID_START_X) / GRID_COLS;
    
    //Base config
    
    public static final int BASE_LIVES = 30;
    
    // Volume config
    public static final int MASTER_VOLUME = 80;
  
    // ── UNIT STATS ───────────────────────────────────────────────────────────
    public static final int BASIC_UNIT_COST = 50;
    public static final int BASIC_UNIT_HP = 3;
    public static final double BASIC_UNIT_COOLDOWN = 0.8; // Shoots every 0.8 seconds
    public static final int BASIC_UNIT_DAMAGE = 50;
    // --- COMMANDER / RAGE SETTINGS ---
    public static final double COMMANDER_SPEED_BOOST = 2; // 30% faster fire rate for allies
    public static final double COMMANDER_DAMAGE_BOOST = 1.5; // 30% more damage for allies
    
    public static final int SNIPER_UNIT_COST = 100;
    public static final int SNIPER_UNIT_HP = 8;
    public static final double SNIPER_UNIT_COOLDOWN = 3.0; // Shoots slow
    public static final int SNIPER_UNIT_DAMAGE = 120;       // Hits very hard
    public static final int SNIPER_SLOW_RADIUS = s(120);
    public static final int SNIPER_ICE_KILL_UNLOCK = 4;
    // Damage = (Enemy Max HP * 0.4) + Flat 100. High HP enemies make BIGGER bombs.
    public static final float SNIPER_ICE_EXPLODE_MULT = 0.4f; 
    public static final int SNIPER_ICE_EXPLODE_RADIUS = s(160);
    public static final int SNIPER_ICE_STAGING_TIME = 20; // Time to turn into crystal (was 40)
    public static final int SNIPER_ICE_PRESSURE_TIME = 20; // Time spent shivering before boom (was 80)
     
    public static final int RAILGUN_UNIT_COST = 200;
    public static final int RAILGUN_UNIT_HP = 5;
    public static final double RAILGUN_UNIT_COOLDOWN = 2.0; // Shoots slow, but clears waves
    public static final int RAILGUN_UNIT_DAMAGE = 50;
    public static final int RAILGUN_TRAIL_UNLOCK       = 2;    // Leaves a burning line
    public static final int RAILGUN_TRAIL_DAMAGE       = 25;   // Damage per half-second
    public static final int RAILGUN_SUPER_LASER_UNLOCK = 5;    // Manual Click Ultimate!
    public static final double RAILGUN_SUPER_LASER_CD  = 30.0; // 30 seconds cooldown!
    public static final int RAILGUN_SUPER_LASER_DMG    = 25000;// Absolutely 
    
    public static final int ALCHEMIST_UNIT_COST = 200;
    public static final int ALCHEMIST_UNIT_HP = 4;
    public static final double ALCHEMIST_UNIT_COOLDOWN = 4.0; 
    public static final int ALCHEMIST_UNIT_DAMAGE = 70; // High impact damage
    
    public static final int WALL_UNIT_COST = 50;
    public static final int WALL_UNIT_HP = 20;
    
    public static final int BIG_WALL_UNIT_COST = 500;
    public static final int BIG_WALL_UNIT_HP = 200;
    
    public static final int COWARD_UNIT_COST = 75;    // Very cheap!
    public static final int COWARD_UNIT_HP = 1;       // Dies instantly if caught off guard
    public static final double COWARD_UNIT_COOLDOWN = 1.0; 
    public static final int COWARD_UNIT_DAMAGE = 50;
    public static final int COWARD_SCARE_RANGE = s(50); // Distance before it hides
    // ========================================================================
    // ── UNIT ABILITY UNLOCKS & STATS ────────────────────────────────────────
    // ========================================================================
    
    // --- BASIC UNIT ---
    public static final int BASIC_SWARM_UNLOCK         = 2;    // Fires at adjacent lanes
    public static final int BASIC_RAGE_UNLOCK          = 3;    // +50% Fire Rate when enemies are close
    public static final int BASIC_DOMAIN_UNLOCK        = 5;    // Commander Domain (Buffs nearby units)
    
    // --- ALCHEMIST UNIT ---
    public static final int ALCHEMIST_CORROSIVE_UNLOCK = 2;    // Puddles melt armor
    public static final int ALCHEMIST_STICKY_UNLOCK    = 3;    // Puddles Slow enemies 50%
    public static final int ALCHEMIST_CONTAGION_UNLOCK = 4;    // Dead enemies spawn puddles
    public static final float ALCHEMIST_DMG_AMP        = 1.5f; // Take 50% more damage
    
    // --- SNIPER UNIT ---
    public static final int SNIPER_DEBUFF_UNLOCK       = 2;    // Enemies deal less damage
    public static final float SNIPER_FREEZE_WEAKNESS   = 0.5f; // Deals 50% damage while frozen
  
    // --- WALL UNITS ---
    public static final int WALL_THORNS_UNLOCK         = 2; 
    public static final int WALL_HEAL_UNLOCK           = 4;    // Auto-heals 3% HP every second
    public static final int WALL_EXPLODE_UNLOCK        = 5;    // Explodes on death
    public static final float WALL_THORN_MULTIPLIER    = 1.0f; // Reflect 100% of damage
    public static final int WALL_THORN_BASE_DMG        = 20;   // Minimum damage dealt back
    
    // --- BIG WALL UNITS ---
    public static final int BIG_WALL_EXPLODE_UNLOCK    = 4;    // Explodes on death
    public static final int BIG_WALL_IMMUNE_UNLOCK     = 5;    // Immune to Calamities
    
    // --- COWARD UNIT ---
    public static final int COWARD_GIFT_UNLOCK         = 2;    // Fires burst before hiding
    
        
    // ========================================================================
    // ── THE DIFFICULTY ENGINE (EASY BALANCING KNOBS) ────────────────────────
    // ========================================================================

    // --- 1. PLAYER POWER SPIKES (The "Grace Period" Creators) ---
    public static final int MAX_UNIT_LEVEL = 5;
    // Base ^ (Level-1). 
    public static final float LEVEL_HP_MULT       = 3.0f;  
    public static final float LEVEL_DMG_MULT      = 4.0f;  
    public static final float LEVEL_COOLDOWN_MULT = 0.85f; 
    public static final float LEVEL_VISUAL_SCALE  = 0.12f; 

    // --- 2. ECONOMY: THE HEAVY COST OF POWER ---
    // Placement Cost = Base * (PLACEMENT_COST_MULT ^ (Level - 1))
    public static final float PLACEMENT_COST_MULT = 2.5f;  

    // Upgrade Cost = Base * UPGRADE_BASE_MULT * (UPGRADE_EXP_MULT ^ (Level - 1))
    // Example Sniper ($100): L2=$1,000 | L3=$3,000 | L4=$9,000 | L5=$27,000
    public static final float UPGRADE_COST_BASE_MULT = 10f; 
    public static final float UPGRADE_COST_EXP_MULT  = 9.6f; 
    public static final int MAX_RESEARCH_COST = 2000000;

    // --- 3. ENEMY THREAT (The Relentless Creep) ---
    // Instead of decimals, we use easy-to-read percentages.
    // +12% HP per wave means enemies double their HP every ~6 waves.
    public static final double ENEMY_HP_GROWTH_PCT   = 150.0; // 150% increase per wave
    public static final double ENEMY_DMG_GROWTH_PCT  = 50.0;  // 50% increase per wave
    public static final double ENEMY_DROP_GROWTH_PCT = 20.0; // 20% more gold dropped per wave

    // ========================================================================
    

    // --- LEVEL COLORS (For Borders/Glows) ---
    public static final Color LVL_1_COLOR = Color.WHITE;
    public static final Color LVL_2_COLOR = new Color(50, 255, 50);  // Green
    public static final Color LVL_3_COLOR = new Color(50, 150, 255); // Rare Blue
    public static final Color LVL_4_COLOR = new Color(200, 50, 255); // Epic Purple
    //public static final Color LVL_5_COLOR = new Color(255, 215, 0);  // Legendary Gold
    public static final Color LVL_5_COLOR = new Color(130, 100, 20); // Old, weathered Bronze
    
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
    public static final float HEAVY_SHIELD_SPEED = 0.3f; 
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
    
    /** How many more enemies per wave (3.5 = 35 more enemies every 10 waves) */
    public static final double DIFF_QUANTITY_GROWTH = 3.5;
    
    /** How much faster enemies spawn per wave (0.12 seconds faster every wave) */
    public static final double DIFF_PACE_SPEEDUP    = 0.12;
    
    /** The fastest enemies are allowed to spawn (don't set to 0 or game crashes!) */
    public static final double MIN_SPAWN_INTERVAL   = 0.2;
    
    
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
    
    // --- SHOP CONFIG ---
    public static final String KEY_SHOP = "s"; // Press S to open shop
    public static final int SHOP_CARD_WIDTH = s(140);
    public static final int SHOP_CARD_HEIGHT = s(160);
    public static final int SHOP_START_X = s(160); // Where the first card draws
    public static final int SHOP_START_Y = s(240);
    public static final int SHOP_SPACING_X = s(160);
    
    
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

    // How much extra gold enemies drop per wave (0.05 = +5% per wave)
    public static final float DROP_GROWTH_PER_WAVE = 0.05f;
    
    /** Converts 1500 to "1.5K", 1500000 to "1.5M", etc. */
    public static String formatNumber(int value) {
        if (value < 1000) return String.valueOf(value);
        if (value < 1000000) {
            double kValue = value / 1000.0;
            return String.format("%.1fK", kValue);
        }
        double mValue = value / 1000000.0;
        return String.format("%.1fM", mValue);
    }

}