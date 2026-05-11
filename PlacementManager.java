import greenfoot.*;

public class PlacementManager {
    private int selectedUnit = 1; 
    private Actor previewActor;
    
    private boolean upPressed = false;
    private boolean downPressed = false;
    int cost = UnitRegistry.getById(selectedUnit).cost * CalamityManager.getPriceMultiplier();
    public void update(MyWorld world) {
        
        // 1. Find the list index of the currently selected unit
        int currentIndex = 0;
        for (int i = 0; i < UnitRegistry.roster.size(); i++) {
            if (UnitRegistry.roster.get(i).id == selectedUnit) {
                currentIndex = i;
                break;
            }
        }

        // 2. UP ARROW: Select previous unit and scroll it to the top
        boolean up = Greenfoot.isKeyDown("up");
        if (up && !upPressed) {
            if (currentIndex > 0) {
                currentIndex--;
                selectedUnit = UnitRegistry.roster.get(currentIndex).id;
                updatePreview(world);
                UIScrollManager.setScroll(currentIndex * GameConfig.MENU_CARD_SPACING);
            }
        }
        upPressed = up;

        // 3. DOWN ARROW: Select next unit and scroll it to the top
        boolean down = Greenfoot.isKeyDown("down");
        if (down && !downPressed) {
            if (currentIndex < UnitRegistry.roster.size() - 1) {
                currentIndex++;
                selectedUnit = UnitRegistry.roster.get(currentIndex).id;
                updatePreview(world);
                UIScrollManager.setScroll(currentIndex * GameConfig.MENU_CARD_SPACING);
            }
        }
        downPressed = down;

        // 4. Automatically check keyboard inputs (Number Keys) based on the Registry
        for (int i = 0; i < UnitRegistry.roster.size(); i++) {
            UnitRegistry.UnitData data = UnitRegistry.roster.get(i);
            if (Greenfoot.isKeyDown(data.key) && selectedUnit != data.id) {
                selectedUnit = data.id; 
                updatePreview(world); 
                // Also scroll perfectly if they press a number key!
                UIScrollManager.setScroll(i * GameConfig.MENU_CARD_SPACING);
            }
        }

        // 5. Mouse Placement Logic
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
    
        if (col == -1) return;
    
        // Look up the unit currently in this spot
        Unit existingUnit = LaneManager.getUnitAt(lane, col);
        UnitRegistry.UnitData data = UnitRegistry.getById(selectedUnit);
    
        // SPECIAL LOGIC: Stacking Basic Units
        if (selectedUnit == 1 && existingUnit instanceof BasicUnit) {
            if (CurrencyManager.spend(data.cost)) {
                ((BasicUnit)existingUnit).addStack();
                return; // Exit early, we don't need to create a new object
            }
        }
    
        // NORMAL LOGIC: Block if occupied by anything else
        if (existingUnit != null) return; 
        int baseCost = data.cost;
        int finalCost = baseCost * CalamityManager.getPriceMultiplier();
        if (CurrencyManager.spend(finalCost)) {
            Unit u = data.spawner.create(lane, col);
            LaneManager.occupy(lane, col, u);
            world.addObject(u, LaneManager.getCellX(col), LaneManager.getLaneY(lane));
        }
    }
    
    public int getSelectedUnit() { return selectedUnit; }
    public void setSelectedUnit(int id) { this.selectedUnit = id; }
}