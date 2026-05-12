import greenfoot.*;

public class RepairBit extends Actor {
    private int vx, vy;
    private int life = 40;
    private int alpha = 255;

    public RepairBit() {
        // Randomize size and horizontal drift
        int size = GameRNG.getRandomNumber(4) + 2;
        vx = GameRNG.getRandomNumber(3) - 1; // -1, 0, or 1
        vy = -(GameRNG.getRandomNumber(2) + 1); // Move up at speed 1 or 2
        
        GreenfootImage img = new GreenfootImage(size, size);
        // Vary between bright lime and emerald green
        Color c = (GameRNG.getRandomNumber(2) == 0) ? 
                  new Color(100, 255, 100) : new Color(0, 200, 50);
        img.setColor(c);
        img.fill();
        setImage(img);
    }

    public void act() {
        setLocation(getX() + vx, getY() + vy);
        life--;
        
        // Smoothly fade out
        alpha = (life * 255) / 40;
        getImage().setTransparency(alpha);
        
        if (life <= 0) {
            getWorld().removeObject(this);
        }
    }
}