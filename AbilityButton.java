import greenfoot.*;

public class AbilityButton extends Actor {
    private String label;
    private int cost;
    
    public AbilityButton(String label, int cost) {
        this.label = label;
        this.cost = cost;
        updateImage();
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            // Tell the manager to try and activate
            AbilityManager.tryActivate((MyWorld)getWorld());
        }
        updateImage();
    }

    private void updateImage() {
        boolean active = AbilityManager.isOverclocked();
        int w = GameConfig.s(150), h = GameConfig.s(40);
        GreenfootImage img = new GreenfootImage(w, h);
        
        // Change color if active (Glowing) or cooling down
        img.setColor(active ? Color.WHITE : new Color(0, 150, 200));
        img.fill();
        img.setColor(Color.BLACK);
        img.drawRect(0, 0, w-1, h-1);
        
        img.setFont(new Font("SansSerif", true, false, 12));
        img.drawString(label + " ($" + cost + ")", 20, h/2 + 5);
        setImage(img);
    }
}