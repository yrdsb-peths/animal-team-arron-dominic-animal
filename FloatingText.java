import greenfoot.*;

public class FloatingText extends Actor {
    private int lifeTimer;
    private int fadeTime;
    private int speed;

    // --- CONSTRUCTOR 1: The "Master" (Handles everything) ---
    public FloatingText(String text, Color color, int fontSize, int floatSpeed, int duration) {
        this.lifeTimer = duration;
        this.fadeTime = duration / 3; // Starts fading in the last 1/3rd of life
        this.speed = floatSpeed;
        
        GreenfootImage img = new GreenfootImage(text, fontSize, color, new Color(0,0,0,0));
        setImage(img);
    }

    // --- CONSTRUCTOR 2: The "Quick Drop" (For Gold/Damage) ---
    // Usage: new FloatingText("+$10", Color.YELLOW);
    public FloatingText(String text, Color color) {
        this(text, color, 18, 1, 60); // 18px, floats up, 1 second
    }

    // --- CONSTRUCTOR 3: The "Announcement" (For Bosses/Waves) ---
    // Usage: new FloatingText("NEW THREAT!", Color.RED, 240);
    public FloatingText(String text, Color color, int duration) {
        this(text, color, 40, 0, duration); // 40px, stays still, custom duration
    }

    @Override
    public void act() {
        // Move
        setLocation(getX(), getY() - speed);
        
        // Timer and Fading
        lifeTimer--;
        if (lifeTimer < fadeTime) {
            int alpha = (int)((double)lifeTimer / fadeTime * 255);
            getImage().setTransparency(Math.max(0, alpha));
        }
        
        if (lifeTimer <= 0) {
            getWorld().removeObject(this);
        }
    }
}