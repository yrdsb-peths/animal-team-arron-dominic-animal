import greenfoot.*;

public class FloatingText extends Actor {
    private int lifeTimer = 60; // Lasts 1 second (60 frames)
    private int speed;

    public FloatingText(String text, Color color, int size, int floatSpeed) {
        this.speed = floatSpeed;
        GreenfootImage img = new GreenfootImage(text, size, color, new Color(0,0,0,0));
        setImage(img);
    }

    @Override
    public void act() {
        // Float upwards
        setLocation(getX(), getY() - speed);
        
        // Fade out
        lifeTimer--;
        if (lifeTimer < 20) {
            getImage().setTransparency(lifeTimer * 12); // Fades away gracefully
        }
        
        if (lifeTimer <= 0) {
            getWorld().removeObject(this);
        }
    }
}