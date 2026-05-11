import greenfoot.*;

public class PlacementManager {
    private int selectedUnit = 1; 
    private int lastSelectedUnit = -1; // Track changes
    private Actor previewActor;
    
    private boolean upPressed = false;
    private boolean downPressed = false;

    public void update(MyWorld world) {
        // 1. Check keyboard inputs for unit selection
        for (int i = 0; i < UnitRegistry.roster.size(); i++) {
            UnitRegistry.UnitData data = UnitRegistry.roster.get(i);
            if (Greenfoot.isKeyDown(data.key)) {
                selectedUnit = data.id; 
            }
        }

        // 2. Refresh Preview IF the unit changed OR it hasn't been created yet
        if (selectedUnit != lastSelectedUnit || previewActor == null || previewActor.getWorld() == null) {
            updatePreview(world);
            lastSelectedUnit = selectedUnit;
        }

        // 3. Mouse Logic
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            int col = LaneManager.colFromX(mouse.getX());
            int lane = LaneManager.laneFromY(mouse.getY());
            
            if (col != -1 && lane != -1) {
                previewActor.setLocation(LaneManager.getCellX(col), LaneManager.getLaneY(lane));
                // Red if blocked, faint white if clear
                previewActor.getImage().setTransparency(LaneManager.isOccupied(lane, col) ? 100 : 180);
            } else {
                previewActor.getImage().setTransparency(0); // Hide in tray
            }
            
            if (Greenfoot.mouseClicked(null)) {
                if (mouse.getButton() == 1) attemptPlacement(world, mouse.getX(), mouse.getY());
                else if (mouse.getButton() == 3) {
                    Unit u = LaneManager.getUnitAt(lane, col);
                    if (u != null) u.die(); 
                }
            }
        }
    }

    private void updatePreview(MyWorld world) {
        if (previewActor != null && previewActor.getWorld() != null) {
            world.removeObject(previewActor);
        }
        
        previewActor = new Actor() {}; 
        UnitRegistry.UnitData data = UnitRegistry.getById(selectedUnit);
        
        // Match the unit's actual visual size and shape
        int size = 40;
        GreenfootImage img = new GreenfootImage(size, size);
        img.setColor(data.color);

        // SHAPE LOGIC: Make the ghost look like the unit!
        if (selectedUnit == 4 || selectedUnit == 7) { // Alchemist and Coward are circles
            img.fillOval(0, 0, size, size);
        } else { // Others are squares
            img.fillRect(0, 0, size, size);
        }
        
        previewActor.setImage(img);
        world.addObject(previewActor, 0, 0);
    }

    private void attemptPlacement(MyWorld world, int x, int y) {
        int col = LaneManager.colFromX(x);
        int lane = LaneManager.laneFromY(y);
        
        // 1. Safety check
        if (col == -1 || lane == -1) return; 
    
        Unit existingUnit = LaneManager.getUnitAt(lane, col);
        UnitRegistry.UnitData data = UnitRegistry.getById(selectedUnit);
        
        // If Basic Unit and identical, handle stacking (Keep this if you like stacking)
        if (selectedUnit == 1 && existingUnit instanceof BasicUnit) {
            if (CurrencyManager.spend(data.cost * CalamityManager.getPriceMultiplier())) {
                ((BasicUnit)existingUnit).addStack();
            }
            return;
        }
        
        // IF occupied, DO NOTHING (Spam click removed)
        if (existingUnit != null) return; 
        
        int finalCost = data.cost * CalamityManager.getPriceMultiplier();
        if (CurrencyManager.spend(finalCost)) {
            Unit u = data.spawner.create(lane, col);
            LaneManager.occupy(lane, col, u);
            world.addObject(u, LaneManager.getCellX(col), LaneManager.getLaneY(lane));
        }
    }
    
    public int getSelectedUnit() { return selectedUnit; }
    public void setSelectedUnit(int id) { this.selectedUnit = id; }
    
}