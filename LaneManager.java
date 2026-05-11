/*
 * ─────────────────────────────────────────────────────────────────────────────
 * LaneManager.java  —  GRID LAYOUT AND CELL OCCUPANCY FOR THE LANE SYSTEM
 * ─────────────────────────────────────────────────────────────────────────────
 * Role:
 *   A static utility class that answers two questions for every other system:
 *     1. "What pixel position is lane N / column C?"
 *     2. "Is this cell already occupied by a Unit?"
 *
 *   Nothing in the game should hardcode lane Y-coordinates or cell X-coordinates.
 *   All of that math lives here so changing GameConfig constants automatically
 *   updates every system that calls getLaneY() or getCellX().
 *
 * Grid layout:
 *   Lanes run horizontally across the screen (top lane = 0).
 *   Columns are the placeable slots within a lane, numbered LEFT to RIGHT.
 *   Column 0 starts at GameConfig.GRID_START_X (just to the right of the Base).
 *   Enemies spawn off the right edge and walk left through all columns.
 *
 *   Visual (5 lanes, 9 columns):
 *
 *     [BASE] | col0 | col1 | col2 | ... | col8 |  ← lane 0
 *            | col0 | col1 | col2 | ... | col8 |  ← lane 1
 *            ...
 *            | col0 | col1 | col2 | ... | col8 |  ← lane 4
 *                                                 (enemies enter from the right →)
 *
 * Interacts with:
 *   WaveManager (spawn Y), PlacementManager (place X/Y, check occupancy),
 *   Unit (report its cell so it can be vacated on death),
 *   Enemy (lane Y for display only — actual Y is set at spawn time)
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class LaneManager {

    // ─────────────────────────────────────────────────────────────────────────
    // INTERNAL STATE  (reset at the start of every PlayingState)
    // ─────────────────────────────────────────────────────────────────────────

    /** Which cells have a Unit standing in them. [lane][col] */
    private static boolean[][] occupied =
        new boolean[GameConfig.NUM_LANES][GameConfig.GRID_COLS];

    /** The Unit reference stored in each cell (null = empty). [lane][col] */
    private static Unit[][] unitGrid =
        new Unit[GameConfig.NUM_LANES][GameConfig.GRID_COLS];


    // ─────────────────────────────────────────────────────────────────────────
    // LIFECYCLE — call once at the start of each game session
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Wipes all occupancy state.
     * Called by PlayingState.enter() before any Units are placed.
     */
    public static void reset() {
        occupied = new boolean[GameConfig.NUM_LANES][GameConfig.GRID_COLS];
        unitGrid  = new Unit[GameConfig.NUM_LANES][GameConfig.GRID_COLS];
    }


    // ─────────────────────────────────────────────────────────────────────────
    // COORDINATE CONVERSION  —  index → pixel
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Returns the pixel Y-centre of the given lane.
     * Use this when spawning enemies (WaveManager) or placing units (PlacementManager).
     *
     * @param lane  Lane index 0 (top) to NUM_LANES-1 (bottom).
     * @return      Y coordinate of the lane's centre row.
     */
    
     public static int getLaneY(int lane) {
        // Stays the same, but now it naturally stops at 500
        return (lane * GameConfig.LANE_HEIGHT) + (GameConfig.LANE_HEIGHT / 2);
    }

    /**
     * Returns the pixel X-centre of the given grid column.
     *
     * @param col  Column index 0 (leftmost placeable) to GRID_COLS-1.
     * @return     X coordinate of the column's centre.
     */
    public static int getCellX(int col) {
        return GameConfig.GRID_START_X + col * GameConfig.GRID_COL_WIDTH
               + GameConfig.GRID_COL_WIDTH / 2;
    }


    // ─────────────────────────────────────────────────────────────────────────
    // COORDINATE CONVERSION  —  pixel → index  (used by PlacementManager)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Converts a raw Y pixel (e.g. mouse click) to a lane index.
     * Always returns a valid lane — clamps to the nearest edge lane if the
     * click is outside the world boundaries.
     *
     * @param y  Raw pixel Y from Greenfoot.getMouseInfo().getY().
     * @return   Lane index in [0, NUM_LANES-1].
     */
    /** Converts a raw Y pixel to a lane index. */
    public static int laneFromY(int y) {
        // If clicking in the UI tray, return -1 (invalid)
        if (y > GameConfig.PLAYABLE_HEIGHT) return -1;
        
        int lane = y / GameConfig.LANE_HEIGHT;
        return Math.max(0, Math.min(GameConfig.NUM_LANES - 1, lane));
    }


    /**
     * Converts a raw X pixel to a column index.
     * Returns -1 if the X is in the Base zone (left of GRID_START_X) or
     * past the right edge of the grid — indicating an invalid placement.
     *
     * @param x  Raw pixel X from Greenfoot.getMouseInfo().getX().
     * @return   Column index in [0, GRID_COLS-1], or -1 if outside the grid.
     */
    public static int colFromX(int x) {
        if (x < GameConfig.GRID_START_X) return -1; // Base area — no placement
        int col = (x - GameConfig.GRID_START_X) / GameConfig.GRID_COL_WIDTH;
        return (col >= 0 && col < GameConfig.GRID_COLS) ? col : -1;
    }


    // ─────────────────────────────────────────────────────────────────────────
    // OCCUPANCY  —  track which cells have Units in them
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Returns true if (lane, col) already contains a Unit, or if the
     * coordinates are out of bounds (treat out-of-bounds as "occupied"
     * so PlacementManager never tries to place there).
     */
    public static boolean isOccupied(int lane, int col) {
        if (!inBounds(lane, col)) return true;
        return occupied[lane][col];
    }

    /**
     * Marks a cell as occupied and stores the Unit reference.
     * Called by PlacementManager right after placing a Unit.
     *
     * @param lane  Lane index.
     * @param col   Column index.
     * @param unit  The Unit being placed in this cell.
     */
    public static void occupy(int lane, int col, Unit unit) {
        if (!inBounds(lane, col)) return;
        occupied[lane][col] = true;
        unitGrid[lane][col]  = unit;
    }

    /**
     * Frees a cell so a new Unit can be placed there.
     * Units MUST call this from their die() or removal logic.
     *
     * @param lane  Lane index.
     * @param col   Column index.
     */
    public static void vacate(int lane, int col) {
        if (!inBounds(lane, col)) return;
        occupied[lane][col] = false;
        unitGrid[lane][col]  = null;
    }

    /**
     * Returns the Unit in a cell, or null if the cell is empty.
     * Useful for inspecting a specific grid position.
     */
    public static Unit getUnitAt(int lane, int col) {
        if (!inBounds(lane, col)) return null;
        return unitGrid[lane][col];
    }
    
    // ─────────────────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────────────────
    
    private static boolean inBounds(int lane, int col) {
        return lane >= 0 && lane < GameConfig.NUM_LANES
            && col  >= 0 && col  < GameConfig.GRID_COLS;
    }
}