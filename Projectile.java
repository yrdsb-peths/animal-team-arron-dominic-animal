import greenfoot.*;
import java.util.List;

public class Projectile extends Actor {
    private Enemy target;
    private int damage;
    private int speed = GameConfig.s(8);
    private int level; 
    private StatusEffect payload;

    public Projectile(Enemy target, int damage, StatusEffect payload) {
        this.target = target;
        this.damage = damage;
        this.payload = payload;
        
        if (payload instanceof SlowEffect) {
            GreenfootImage img = new GreenfootImage(25, 15);
            img.setColor(new Color(100, 200, 255)); 
            img.fillPolygon(new int[]{0, 15, 25, 15}, new int[]{7, 0, 7, 15}, 4);
            img.setColor(Color.WHITE); 
            img.drawPolygon(new int[]{0, 15, 25, 15}, new int[]{7, 0, 7, 15}, 4);
            setImage(img);
        } else {
            GreenfootImage img = new GreenfootImage(15, 15);
            img.setColor(Color.YELLOW);
            img.fillOval(0, 0, 15, 15);
            setImage(img);
        }
    }

    @Override
    public void act() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null || !world.getGSM().isState(PlayingState.class)) return;

        // 1. GUIDANCE LOGIC: Only turn if target is still valid
        if (target != null && target.getWorld() != null && !target.isDead()) {
            turnTowards(target.getX(), target.getY());
        }

        // 2. MOVEMENT: Keep flying in current direction
        move(speed * GameConfig.GAME_SPEED);

        // 3. COLLISION: Check for ANY enemy, not just the target
        Enemy hit = (Enemy) getOneIntersectingObject(Enemy.class);
        if (hit != null && !hit.isDead()) {
            handleHit(world, hit);
            return;
        }

        // 4. CLEANUP: Remove if off-screen
        if (isAtEdge()) {
            world.removeObject(this);
        }
    }

    private void handleHit(MyWorld world, Enemy victim) {
        boolean isSniperShot = (payload instanceof SlowEffect);
        
        // Determine tech level for specialty kills
        if (isSniperShot) {
            this.level = UnitRegistry.getById(2).level;
        } else {
            this.level = UnitRegistry.getById(1).level;
        }

        // Sniper Execute Logic
        if (isSniperShot && victim.health <= damage && level >= GameConfig.SNIPER_ICE_KILL_UNLOCK) {
             victim.markForIceKill();
        }

        // Shield Bypass Logic
        boolean backstabHit = (getX() > victim.getX());
        boolean configBypasses = (isSniperShot) ? GameConfig.SNIPER_PROJECTILE_BYPASS : GameConfig.BASIC_PROJECTILE_BYPASS;
        boolean finalBypass = (backstabHit && GameConfig.BACKSTAB_ALWAYS_BYPASS) || configBypasses;

        victim.takeDamage(damage, finalBypass);

        // Apply Payload
        if (isSniperShot) {
            world.addObject(new FrostPulse(GameConfig.SNIPER_SLOW_RADIUS), getX(), getY());
            List<Enemy> swarm = getObjectsInRange(GameConfig.SNIPER_SLOW_RADIUS, Enemy.class);
            for (Enemy e : swarm) {
                e.applyEffect(EffectFactory.createSlow(GameConfig.SNIPER_SLOW_DURATION, GameConfig.SNIPER_SLOW_POWER, 1.0f));
            }
        } else if (payload != null) {
            victim.applyEffect(payload);
        }

        if (backstabHit) {
             world.addObject(new FloatingText("BACKSTAB!", Color.RED, 15, 1, 30), getX(), getY() - 10);
        }

        world.removeObject(this);
    }
}