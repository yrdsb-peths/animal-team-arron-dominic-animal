import greenfoot.*;

public class PlacementManager {
    private int selectedUnit = 1; 
    private Actor previewActor;

    public void update(MyWorld world) {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        
        // Switch units with keys
        if (Greenfoot.isKeyDown("1")) { selectedUnit = 1; updatePreview(world); }
        if (Greenfoot.isKeyDown("2")) { selectedUnit = 2; updatePreview(world); }
        if (Greenfoot.isKeyDown("escape") || Greenfoot.isKeyDown("0")) { 
            selectedUnit = 0; // 0 = Nothing selected
            updatePreview(world); 
        }
    
        if (mouse != null) {
            // 1. Manage the Preview (only show if a unit is actually selected)
            if (selectedUnit != 0) {
                if (previewActor == null) updatePreview(world);
                int col = LaneManager.colFromX(mouse.getX());
                int lane = LaneManager.laneFromY(mouse.getY());
                if (col != -1) {
                    previewActor.setLocation(LaneManager.getCellX(col), LaneManager.getLaneY(lane));
                    previewActor.getImage().setTransparency(LaneManager.isOccupied(lane, col) ? 100 : 180);
                }
            } else if (previewActor != null) {
                world.removeObject(previewActor);
                previewActor = null;
            }
    
            // 2. Handle Clicks
            if (Greenfoot.mouseClicked(null)) {
                // CRITICAL FIX: If the mouse clicked an Actor, check if it's UI
                // If we clicked a button or a card, STOP and don't place a unit!
                Actor clicked = mouse.getActor();
                if (clicked instanceof UIUnitCard || clicked instanceof UISpeedButton || clicked instanceof UICancelButton) {
                    return; 
                }
    
                if (mouse.getButton() == 1) { // Left Click
                    attemptPlacement(world, mouse.getX(), mouse.getY());
                } else if (mouse.getButton() == 3) { // Right Click
                    int col = LaneManager.colFromX(mouse.getX());
                    int lane = LaneManager.laneFromY(mouse.getY());
                    Unit u = LaneManager.getUnitAt(lane, col);
                    if (u != null) u.die();
                }
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