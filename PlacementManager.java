import greenfoot.*;

public class PlacementManager {
    private int selectedUnit = 1; 
    private Actor previewActor;

    public void update(MyWorld world) {
        
        // 1. Automatically check keyboard inputs based on the Registry
        for (UnitRegistry.UnitData data : UnitRegistry.roster) {
            if (Greenfoot.isKeyDown(data.key) && selectedUnit != data.id) {
                selectedUnit = data.id; 
                updatePreview(world); 
            }
        }

        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            if (previewActor == null) updatePreview(world);
            
            // Snap preview to grid
            int col = LaneManager.colFromX(mouse.getX());
            int lane = LaneManager.laneFromY(mouse.getY());
            
            if (col != -1) {
                previewActor.setLocation(LaneManager.getCellX(col), LaneManager.getLaneY(lane));
                previewActor.getImage().setTransparency(LaneManager.isOccupied(lane, col) ? 100 : 180);
            }
            
            if (Greenfoot.mouseClicked(null)) {
                if (mouse.getButton() == 1) { // Left Click: Place
                    attemptPlacement(world, mouse.getX(), mouse.getY());
                }
                else if (mouse.getButton() == 3) { // Right Click: Remove
                    Unit u = LaneManager.getUnitAt(lane, col);
                    if (u != null) u.die(); 
                }
            }
        }
    }

    private void updatePreview(MyWorld world) {
        if (previewActor != null) world.removeObject(previewActor);
        previewActor = new Actor() {}; 
        GreenfootImage img = new GreenfootImage(40, 40);
        
        // Automatically get the right color from the Registry
        UnitRegistry.UnitData data = UnitRegistry.getById(selectedUnit);
        img.setColor(data.color);
        
        img.fillRect(0, 0, 40, 40);
        previewActor.setImage(img);
        world.addObject(previewActor, 0, 0);
    }

    private void attemptPlacement(MyWorld world, int x, int y) {
        int col = LaneManager.colFromX(x);
        int lane = LaneManager.laneFromY(y);

        if (col == -1 || LaneManager.isOccupied(lane, col)) return; 

        // Automatically get the right Cost and Unit Class from the Registry
        UnitRegistry.UnitData data = UnitRegistry.getById(selectedUnit);

        if (CurrencyManager.spend(data.cost)) {
            Unit u = data.spawner.create(lane, col); // Magically creates the correct unit!
            LaneManager.occupy(lane, col, u);
            world.addObject(u, LaneManager.getCellX(col), LaneManager.getLaneY(lane));
        }
    }
    
    public int getSelectedUnit() { return selectedUnit; }
    public void setSelectedUnit(int id) { this.selectedUnit = id; }
}