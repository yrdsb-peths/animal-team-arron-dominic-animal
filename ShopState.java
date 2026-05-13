import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class ShopState implements GameState {
    private List<Actor> ui = new ArrayList<>();
    private ShopBackground curtain;

    @Override
    public void enter(MyWorld world) {
        curtain = new ShopBackground();
        world.addObject(curtain, world.getWidth()/2, world.getHeight()/2);

        addUI(world, new UIText("TECH RESEARCH LAB", 40, Color.CYAN), world.getWidth()/2, GameConfig.s(60));
        addUI(world, new UIText("Tech Points: " + SaveManager.getTechPoints(), 24, Color.YELLOW), world.getWidth()/2, GameConfig.s(100));

        for (int i = 0; i < UnitRegistry.roster.size(); i++) {
            UnitRegistry.UnitData data = UnitRegistry.roster.get(i);
            UpgradeCard card = new UpgradeCard(data);
            int x = GameConfig.SHOP_START_X + (i % 4) * GameConfig.SHOP_SPACING_X;
            int y = GameConfig.SHOP_START_Y + (i / 4) * GameConfig.SHOP_CARD_HEIGHT;
            addUI(world, card, x, y);
        }

        addUI(world, new UIText("[ PRESS 'T' TO RESUME ]", 20, Color.WHITE), world.getWidth()/2, world.getHeight() - GameConfig.s(40));
    }

    @Override
    public void update(MyWorld world) {
        String key = Greenfoot.getKey();
        if ("t".equals(key) || "escape".equals(key)) {
            world.getGSM().popState();
        }
        
        // Update Tech Points display
        if (ui.size() > 1) {
            ((UIText)ui.get(1)).setText("Tech Points: " + SaveManager.getTechPoints());
        }
    }

    @Override
    public void exit(MyWorld world) {
        world.removeObject(curtain);
        world.removeObjects(ui);
        ui.clear();
        world.repaint();
    }

    private void addUI(MyWorld world, Actor a, int x, int y) {
        world.addObject(a, x, y);
        ui.add(a);
    }
}