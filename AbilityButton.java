import greenfoot.*;

public class AbilityButton extends Actor {
    private int abilityId; // 1 = Overclock, 2 = Freeze, 3 = Nuke
    private String label;
    private int cost;
    
    public AbilityButton(int id, String label, int cost) {
        this.abilityId = id;
        this.label = label;
        this.cost = cost;
        updateImage();
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            MyWorld world = (MyWorld)getWorld();
            if (abilityId == 1) AbilityManager.tryOverclock(world);
            if (abilityId == 2) AbilityManager.tryTimeFreeze(world);
            if (abilityId == 3) AbilityManager.tryNuke(world);
        }
        updateImage(); // Always update to show cooldowns!
    }

    private void updateImage() {
        double cd = 0;
        boolean active = false;
        
        if (abilityId == 1) { cd = AbilityManager.getOverclockCD(); active = AbilityManager.isOverclocked(); }
        if (abilityId == 2) { cd = AbilityManager.getFreezeCD(); }
        if (abilityId == 3) { cd = AbilityManager.getNukeCD(); }

        int w = GameConfig.s(160), h = GameConfig.s(45);
        GreenfootImage img = new GreenfootImage(w, h);
        
        if (cd > 0) {
            img.setColor(new Color(50, 50, 50)); // Dark grey for cooldown
            img.fill();
            img.setColor(Color.RED);
            img.fillRect(0, 0, (int)(w * (cd / (abilityId==1?GameConfig.OVERCLOCK_COOLDOWN : abilityId==2?GameConfig.FREEZE_COOLDOWN : GameConfig.NUKE_COOLDOWN))), h);
        } else if (active) {
            img.setColor(Color.CYAN); // Glowing cyan for active overclock
            img.fill();
        } else {
            boolean canAfford = CurrencyManager.getGold() >= cost;
            img.setColor(canAfford ? new Color(0, 100, 200) : new Color(80, 80, 80));
            img.fill();
        }
        
        img.setColor(Color.WHITE);
        img.drawRect(0, 0, w-1, h-1);
        
        img.setFont(new Font("SansSerif", true, false, 14));
        if (cd > 0) {
            img.drawString(String.format("READY IN %.1fs", cd), 15, h/2 + 5);
        } else {
            img.drawString("["+abilityId+"] " + label, 10, 18);
            img.drawString("$" + cost, 10, 38);
        }
        setImage(img);
    }
}