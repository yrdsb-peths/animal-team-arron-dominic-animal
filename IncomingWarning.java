import greenfoot.*;

public class IncomingWarning extends Actor {
    private int timer = 120; // Show for 2 seconds

    public IncomingWarning() {
        GreenfootImage img = new GreenfootImage(40, 40);
        img.setColor(Color.RED);
        img.fillOval(0, 0, 40, 40);
        img.setColor(Color.WHITE);
        img.setFont(new Font("SansSerif", true, false, 30));
        img.drawString("!", 15, 32);
        setImage(img);
    }

    public void act() {
        // Flash effect
        if (timer % 20 < 10) getImage().setTransparency(0);
        else getImage().setTransparency(255);
        
        timer--;
        if (timer <= 0) getWorld().removeObject(this);
    }
}