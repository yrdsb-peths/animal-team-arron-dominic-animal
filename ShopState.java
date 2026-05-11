import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class ShopState implements GameState {
    private List<Actor> ui = new ArrayList<>();
    private ShopBackground curtain;

    @Override
    public void enter(MyWorld world) {
        // 1. Add the Opaque Curtain first
        curtain = new ShopBackground();
        world.addObject(curtain, world.getWidth()/2, world.getHeight()/2);

        // 2. Add Title
        addUI(world, new UIText("TECH RESEARCH LAB", 40, Color.CYAN), world.getWidth()/2, GameConfig.s(60));
        addUI(world, new UIText("Gold: $" + CurrencyManager.getGold(), 24, Color.YELLOW), world.getWidth()/2, GameConfig.s(100));

        // 3. Grid of Cards
        for (int i = 0; i < UnitRegistry.roster.size(); i++) {
            UnitRegistry.UnitData data = UnitRegistry.roster.get(i);
            UpgradeCard card = new UpgradeCard(data);
            int x = GameConfig.SHOP_START_X + (i % 4) * GameConfig.SHOP_SPACING_X;
            int y = GameConfig.SHOP_START_Y + (i / 4) * GameConfig.SHOP_CARD_HEIGHT;
            addUI(world, card, x, y);
        }

        addUI(world, new UIText("[ PRESS '" + GameConfig.KEY_SHOP.toUpperCase() + "' TO RESUME ]", 20, Color.WHITE), world.getWidth()/2, world.getHeight() - GameConfig.s(40));
    }

    @Override
    public void update(MyWorld world) {
        // Use getKey to prevent flickering
        String key = Greenfoot.getKey();
        if (GameConfig.KEY_SHOP.equals(key)) {
            world.getGSM().popState();
        }
        
        // Update gold display (Index 1 in our UI list)
        if (ui.size() > 1) {
            ((UIText)ui.get(1)).setText("Gold: $" + CurrencyManager.getGold());
        }
    }

    @Override
    public void exit(MyWorld world) {
        world.removeObject(curtain);
        world.removeObjects(ui);
        ui.clear();
        
        // Force world repaint to clear the curtain immediately
        world.repaint();
    }

    private void addUI(MyWorld world, Actor a, int x, int y) {
        world.addObject(a, x, y);
        ui.add(a);
    }
}