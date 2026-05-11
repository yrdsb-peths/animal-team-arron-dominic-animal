import greenfoot.*;

public class ShopBackground extends Actor {
    public ShopBackground() {
        GreenfootImage img = new GreenfootImage(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        img.setColor(new Color(20, 20, 30)); // Solid Dark Navy (No transparency)
        img.fill();
        
        // Add a nice border
        img.setColor(Color.CYAN);
        img.drawRect(5, 5, GameConfig.WORLD_WIDTH - 11, GameConfig.WORLD_HEIGHT - 11);
        setImage(img);
    }
}