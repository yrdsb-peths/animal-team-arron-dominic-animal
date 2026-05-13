
import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class VictoryState implements GameState {
    private List<Actor> ui = new ArrayList<>();
    private int levelId;

    public VictoryState(int levelId) {
        this.levelId = levelId;
    }

    @Override
    public void enter(MyWorld world) {
        int midX = world.getWidth() / 2;
        int midY = world.getHeight() / 2;

        world.addObject(new ShopBackground(), midX, midY); // Dark overlay

        addUI(world, new UIText("VICTORY!", 60, Color.GREEN), midX, midY - 60);
        addUI(world, new UIText("Level " + levelId + " Cleared.", 30, Color.WHITE), midX, midY);
        
        // Massive Tech Point Bonus
        int bonus = levelId * 5;
        SaveManager.addTechPoints(bonus);
        SaveManager.setInt("highest_level", Math.max(SaveManager.getInt("highest_level"), levelId + 1));
        
        addUI(world, new UIText("+" + bonus + " Tech Points Earned", 24, Color.YELLOW), midX, midY + 40);
        addUI(world, new UIText("[ ENTER ] Return to Menu", 18, Color.CYAN), midX, midY + 120);
    }

    @Override
    public void update(MyWorld world) {
        if ("enter".equals(Greenfoot.getKey())) {
            world.getGSM().changeState(new MenuState());
        }
    }

    @Override
    public void exit(MyWorld world) {
        world.removeObjects(ui);
    }

    private void addUI(MyWorld world, Actor a, int x, int y) {
        world.addObject(a, x, y);
        ui.add(a);
    }
}