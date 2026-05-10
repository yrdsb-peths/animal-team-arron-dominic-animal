import greenfoot.*;

public class Projectile extends Actor {
    private Enemy target;
    private int damage;
    private int speed = GameConfig.s(8);
    
    private StatusEffect payload;

    public Projectile(Enemy target, int damage, StatusEffect payload) {
        this.target = target;
        this.damage = damage;
        this.payload = payload;
        
        GreenfootImage img = new GreenfootImage(15, 15);
        img.setColor(Color.YELLOW);
        img.fillOval(0, 0, 15, 15);
        setImage(img);
    }

    @Override
    public void act() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        // If target died before we reached it, destroy the projectile
        if (target == null || target.getWorld() == null || target.isDead()) {
            world.removeObject(this);
            return;
        }

        turnTowards(target.getX(), target.getY());
        move(speed * GameConfig.GAME_SPEED);

        // Inside Projectile.act()
        if (intersects(target)) {
            // LOGIC: If Projectile is to the RIGHT of the enemy, it's a backstab!
            // (Because enemies are facing Left)
            boolean isBackstab = (getX() > target.getX());
            
            // If it's a backstab, we use the 'bypassShield' version of takeDamage
            if (isBackstab) {
                target.takeDamage(damage, true); // True = Ignore Shield
                // Optional: Show a "Backstab!" message
                // getWorld().addObject(new FloatingText("BACKSTAB!", Color.RED, 15, 1), getX(), getY());
            } else {
                target.takeDamage(damage); // Normal damage logic (blocked by shield)
            }
            
            // If there is a status effect payload (like Sniper Slow), apply it too
            if (payload != null) {
                target.applyEffect(payload);
            }
            
            getWorld().removeObject(this);
        }
    }
}