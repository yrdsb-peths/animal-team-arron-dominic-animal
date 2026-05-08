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

        if (intersects(target)) {
            target.takeDamage(damage);
            
            if (payload != null) {
                target.applyEffect(payload);
            }
            
            world.removeObject(this);
        }
    }
}