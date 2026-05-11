import greenfoot.*;

public class MatrixGlitch extends Actor {
    private int life = 50;
    
    public MatrixGlitch() {
        updateVisual();
    }
    
    private void updateVisual() {
        int size = 60; // Large
        GreenfootImage img = new GreenfootImage(size, size);
        
        // Neon Colors
        Color[] glitchColors = {Color.CYAN, Color.GREEN, Color.MAGENTA, Color.WHITE};
        
        // Draw random "digital chunks"
        for (int i = 0; i < 15; i++) {
            img.setColor(glitchColors[GameRNG.getRandomNumber(glitchColors.length)]);
            int rw = GameRNG.getRandomNumber(size/2);
            int rh = GameRNG.getRandomNumber(10);
            img.fillRect(GameRNG.getRandomNumber(size), GameRNG.getRandomNumber(size), rw, rh);
        }
        setImage(img);
    }
    
    public void act() {
        if (life % 3 == 0) updateVisual(); // Flickers every 3 frames
        life--;
        if (life <= 0) getWorld().removeObject(this);
    }
}