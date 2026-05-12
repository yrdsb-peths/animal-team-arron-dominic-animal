import greenfoot.*;

public class IceShard extends Actor {
    private int vx, vy;
    private int life = 30;

    public IceShard() {
        vx = Greenfoot.getRandomNumber(15) - 7;
        vy = Greenfoot.getRandomNumber(15) - 7;
        GreenfootImage img = new GreenfootImage(8, 4);
        img.setColor(new Color(150, 240, 255));
        img.fill();
        setImage(img);
        setRotation(Greenfoot.getRandomNumber(360));
    }

    public void act() {
        setLocation(getX() + vx, getY() + vy);
        setRotation(getRotation() + 10);
        life--;
        if (life <= 0) getWorld().removeObject(this);
    }
}