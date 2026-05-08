// ==================================================
// FILE: ./UISpeedButton.java
// ==================================================
import greenfoot.*;

public class UISpeedButton extends Actor {
    private int[] speeds = {1, 2, 4};
    private int currentIndex = 0;

    public UISpeedButton() {
        updateImage();
    }

    public void act() {
        // Detect click on this specific UI element
        if (Greenfoot.mouseClicked(this)) {
            currentIndex = (currentIndex + 1) % speeds.length;
            GameConfig.GAME_SPEED = speeds[currentIndex];
            updateImage();
        }
    }

    private void updateImage() {
        int currentSpeed = speeds[currentIndex];
        
        // Draw the button
        GreenfootImage img = new GreenfootImage(GameConfig.s(100), GameConfig.s(40));
        
        // Change color based on speed (Grey for 1x, Orange for 2x, Red for 4x)
        if (currentSpeed == 1) img.setColor(Color.DARK_GRAY);
        else if (currentSpeed == 2) img.setColor(new Color(200, 100, 0)); // Orange
        else img.setColor(new Color(200, 0, 0)); // Red
        
        img.fill();
        img.setColor(Color.WHITE);
        img.drawRect(0, 0, img.getWidth() - 1, img.getHeight() - 1);
        
        // Draw Text
        img.setFont(new Font("SansSerif", true, false, GameConfig.s(16)));
        img.drawString(currentSpeed + "x SPEED >>", GameConfig.s(10), GameConfig.s(25));
        
        setImage(img);
    }
}