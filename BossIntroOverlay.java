import greenfoot.*;

public class BossIntroOverlay extends Actor {
    private int life = 180; // 3 seconds
    private String bossName;

    public BossIntroOverlay(String name) {
        this.bossName = name;
        int w = GameConfig.WORLD_WIDTH;
        int h = GameConfig.WORLD_HEIGHT;
        
        GreenfootImage img = new GreenfootImage(w, h);
        
        // 1. Darken the screen (Cinema bars or full fade)
        img.setColor(new Color(0, 0, 0, 180)); 
        img.fillRect(0, 0, w, h);
        
        // 2. The Red "WARNING" Bar
        img.setColor(new Color(200, 0, 0));
        img.fillRect(0, h/2 - 50, w, 100);
        
        // 3. Text
        img.setColor(Color.WHITE);
        img.setFont(new Font("Courier New", true, false, 50));
        img.drawString("NEW THREAT DETECTED", w/2 - 250, h/2 - 10);
        
        img.setFont(new Font("Courier New", true, true, 40));
        img.setColor(Color.YELLOW);
        img.drawString("> " + bossName.toUpperCase() + " <", w/2 - 150, h/2 + 35);
        
        setImage(img);
    }

    public void act() {
        life--;
        // Flicker effect
        if (life > 150 && (life % 10 < 5)) getImage().setTransparency(100);
        else getImage().setTransparency(255);
        
        if (life <= 0) {
            getWorld().removeObject(this);
        }
    }
}