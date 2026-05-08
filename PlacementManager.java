import greenfoot.*;

public class PlacementManager {
    private int selectedUnit = 1; 
    private Actor previewActor;

    public void update(MyWorld world) {
        // Switch units with 1, 2
        if (Greenfoot.isKeyDown("1")) { selectedUnit = 1; updatePreview(world); }
        if (Greenfoot.isKeyDown("2")) { selectedUnit = 2; updatePreview(world); }

        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            // 1. Manage the Preview Actor (The Ghost)
            if (previewActor == null) updatePreview(world);
            
            // Snap preview to the grid
            int col = LaneManager.colFromX(mouse.getX());
            int lane = LaneManager.laneFromY(mouse.getY());
            
            if (col != -1) {
                previewActor.setLocation(LaneManager.getCellX(col), LaneManager.getLaneY(lane));
                // Make it red if blocked, white if clear
                previewActor.getImage().setTransparency(LaneManager.isOccupied(lane, col) ? 100 : 180);
            }
            
            if (Greenfoot.mouseClicked(null)) {
                // LEFT CLICK (Button 1): Place
                if (mouse.getButton() == 1) {
                    attemptPlacement(world, mouse.getX(), mouse.getY());
                }
                // RIGHT CLICK (Button 3): Remove
                else if (mouse.getButton() == 3) {
                    Unit u = LaneManager.getUnitAt(lane, col);
                    if (u != null) u.die(); // No refund
                }
            }
            
            // 2. Handle Placement
            if (Greenfoot.mouseClicked(null)) {
                attemptPlacement(world, mouse.getX(), mouse.getY());
            }
        }
    }

    private void updatePreview(MyWorld world) {
        if (previewActor != null) world.removeObject(previewActor);
        
        // Create a visual that matches the selected unit
        previewActor = new Actor() {}; 
        GreenfootImage img;
        if (selectedUnit == 1) {
            img = new GreenfootImage(40, 40);
            img.setColor(Color.GREEN);
        } else {
            img = new GreenfootImage(40, 40);
            img.setColor(Color.MAGENTA);
        }
        img.fillRect(0, 0, 40, 40);
        previewActor.setImage(img);
        world.addObject(previewActor, 0, 0);
    }

    private void attemptPlacement(MyWorld world, int x, int y) {
        int col = LaneManager.colFromX(x);
        int lane = LaneManager.laneFromY(y);

        if (col == -1 || LaneManager.isOccupied(lane, col)) return; 

        int cost = (selectedUnit == 1) ? GameConfig.BASIC_UNIT_COST : GameConfig.SNIPER_UNIT_COST;

        if (CurrencyManager.spend(cost)) {
            Unit u = (selectedUnit == 1) ? new BasicUnit(lane, col) : new SniperUnit(lane, col);
            LaneManager.occupy(lane, col, u);
            world.addObject(u, LaneManager.getCellX(col), LaneManager.getLaneY(lane));
        }
    }
    
    public int getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(int id) {
        this.selectedUnit = id;
    }

}