import greenfoot.*;
import java.util.List;

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
            // 1. Determine if this specific hit ignores shields
            // Check A: Is it a backstab?
            boolean backstabHit = (getX() > target.getX());
            
            // Check B: Does the config say this unit type ignores shields?
            boolean configBypasses = (payload instanceof SlowEffect) ? 
                                     GameConfig.SNIPER_PROJECTILE_BYPASS : 
                                     GameConfig.BASIC_PROJECTILE_BYPASS;
    
            // Combine logic: Bypass if (it's a backstab AND config allows it) OR (unit type bypasses)
            boolean finalBypass = (backstabHit && GameConfig.BACKSTAB_ALWAYS_BYPASS) || configBypasses;
    
            // 2. APPLY DAMAGE
            target.takeDamage(damage, finalBypass);
    
            // 3. APPLY EFFECTS (Swarm Slow vs. Single Effect)
            if (payload instanceof SlowEffect) {
                // Trigger Swarm Slow Visual
                getWorld().addObject(new FrostPulse(GameConfig.SNIPER_SLOW_RADIUS), getX(), getY());
                
                // Find every enemy in the blast radius
                List<Enemy> swarm = getObjectsInRange(GameConfig.SNIPER_SLOW_RADIUS, Enemy.class);
                for (Enemy e : swarm) {
                    // Create a fresh copy of the slow effect for every enemy in the swarm
                    StatusEffect frost = EffectFactory.createSlow(
                        GameConfig.SNIPER_SLOW_DURATION, 
                        GameConfig.SNIPER_SLOW_POWER
                    );
                    e.applyEffect(frost);
                }
            } 
            else if (payload != null) {
                // If it's a different effect (not a slow), apply only to the main target
                target.applyEffect(payload);
            }
    
            // 4. CLEANUP: Optional backstab text for feedback
            if (backstabHit && GameConfig.BACKSTAB_ALWAYS_BYPASS) {
                 getWorld().addObject(new FloatingText("BACKSTAB!", Color.RED, 15, 1, 30), getX(), getY() - 10);
            }
    
            getWorld().removeObject(this);
        }
    }
}