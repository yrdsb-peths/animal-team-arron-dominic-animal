import greenfoot.*;

public class RageAura extends Actor {
    private Unit owner;

    public RageAura(Unit owner) {
        this.owner = owner;
        GreenfootImage img = new GreenfootImage(60, 60);
        img.setColor(new Color(255, 0, 0, 80)); // Faint red glow
        img.fillOval(0, 0, 60, 60);
        setImage(img);
    }

    public void act() {
        if (owner == null || owner.getWorld() == null || owner.isDead()) {
            getWorld().removeObject(this);
            return;
        }
        
        // Stick to the unit
        setLocation(owner.getX(), owner.getY());
        
        // Pulse visibility
        int alpha = 80 + (int)(Math.sin(System.currentTimeMillis() / 100.0) * 40);
        getImage().setTransparency(alpha);
    }
}