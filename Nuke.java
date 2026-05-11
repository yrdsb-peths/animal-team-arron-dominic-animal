import greenfoot.*;

public class Nuke extends Actor {
    private int timer = 120; // Lasts 2 seconds
    private int maxRadius = GameConfig.WORLD_WIDTH * 2;
    private int currentRadius = 10;

    public Nuke() {
        updateVisual();
    }

    public void act() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        timer--;

        // PHASE 1: The Impact (Flashbang & Damage)
        if (timer == 115) { 
            world.startShake(60, 30); // Absolutely massive screen shake
            
            // Annihilate everything
            for (Enemy e : world.getObjects(Enemy.class)) {
                e.takeDamage(99999, true); // 99k True Damage!
            }
        }

        // PHASE 2: Expanding Shockwave
        if (timer > 60) {
            currentRadius += (maxRadius / 30); // Expand extremely fast
        }

        updateVisual();

        if (timer <= 0) {
            world.removeObject(this);
        }
    }

    private void updateVisual() {
        GreenfootImage img = new GreenfootImage(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        int centerX = img.getWidth() / 2;
        int centerY = img.getHeight() / 2;

        if (timer > 105) {
            // Blinding White Flash
            img.setColor(Color.WHITE);
            img.fill();
        } 
        else if (timer > 40) {
            // Expanding Fireball
            img.setColor(new Color(255, 100, 0, 200)); // Fiery Orange
            img.fillOval(centerX - currentRadius/2, centerY - currentRadius/2, currentRadius, currentRadius);
            
            // White Hot Core
            img.setColor(new Color(255, 255, 200, 255));
            int core = currentRadius / 2;
            img.fillOval(centerX - core/2, centerY - core/2, core, core);
        } 
        else {
            // Fading Smoke
            int alpha = (int)((double)timer / 40 * 255);
            img.setColor(new Color(50, 0, 0, alpha)); // Dark smoky red
            img.fill();
        }

        setImage(img);
    }
}