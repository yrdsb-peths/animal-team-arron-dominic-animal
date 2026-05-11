import greenfoot.*;

public class FallingDebris extends Actor {
    private int speed = 6 + GameRNG.getRandomNumber(6);
    
    public FallingDebris() {
        int size = 15 + GameRNG.getRandomNumber(20);
        GreenfootImage img = new GreenfootImage(size, size);
        int shade = 40 + GameRNG.getRandomNumber(60);
        img.setColor(new Color(shade, shade, shade));
        img.fill();
        setImage(img);
    }
    
    public void act() {
        setLocation(getX(), getY() + speed);
        turn(3);
        if (getY() > getWorld().getHeight() + 20) getWorld().removeObject(this);
    }
}