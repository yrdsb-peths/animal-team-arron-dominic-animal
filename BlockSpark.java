import greenfoot.*;

public class BlockSpark extends Actor {
    private int vx, vy;
    private int life;

    // Updated constructor to accept a specific color
    public BlockSpark(Color color) {
        // Randomize speed and life for "Heavy" feel
        vx = GameRNG.getRandomNumber(7) + 3; 
        vy = GameRNG.getRandomNumber(8) - 4; 
        life = 10 + GameRNG.getRandomNumber(10);
        
        // Randomize size slightly (2px to 5px)
        int size = GameRNG.getRandomNumber(3) + 2;
        GreenfootImage img = new GreenfootImage(size, size);
        img.setColor(color);
        img.fill();
        setImage(img);
    }

    public void act() {
        setLocation(getX() + vx, getY() + vy);
        life--;
        if (life <= 0) getWorld().removeObject(this);
    }
}