import greenfoot.*;

public class Player extends Actor {

    // ── Movement ──────────────────────────────────────────────────────────────
    private int speed = GameConfig.s(4);

    // ── State ─────────────────────────────────────────────────────────────────
    private boolean isDead = false;

    /** How long after death before transitioning to Game Over */
    private GameTimer deathTimer = new GameTimer(2.0, false);

    // ── Constructor ───────────────────────────────────────────────────────────

    public Player() {
        // Draw a simple placeholder sprite (replace with a real image later)
        GreenfootImage img = new GreenfootImage(GameConfig.s(40), GameConfig.s(40));
        img.setColor(Color.CYAN);
        img.fillOval(0, 0, img.getWidth(), img.getHeight());
        setImage(img);
    }

    // ── Act ───────────────────────────────────────────────────────────────────

    @Override
    public void act() {
        MyWorld world = (MyWorld) getWorld();
        if (world == null) return;

        // Only run during PlayingState
        if (!world.getGSM().isState(PlayingState.class)) return;

        if (isDead) {
            handleDeath(world);
        } else {
            handleMovement();
        }
    }

    // ── Movement ──────────────────────────────────────────────────────────────

    private void handleMovement() {
        // Arrow key movement — modify this for your game's control scheme
        if (Greenfoot.isKeyDown("up")    && getY() > GameConfig.s(20))
            setLocation(getX(), getY() - speed);
        if (Greenfoot.isKeyDown("down")  && getY() < getWorld().getHeight() - GameConfig.s(20))
            setLocation(getX(), getY() + speed);
        if (Greenfoot.isKeyDown("left")  && getX() > GameConfig.s(20))
            setLocation(getX() - speed, getY());
        if (Greenfoot.isKeyDown("right") && getX() < getWorld().getWidth() - GameConfig.s(20))
            setLocation(getX() + speed, getY());
    }

    // ── Death ─────────────────────────────────────────────────────────────────

    public void die() {
        if (isDead) return; // already dead, ignore
        isDead = true;

        // Visual feedback — turn red
        GreenfootImage img = new GreenfootImage(GameConfig.s(40), GameConfig.s(40));
        img.setColor(Color.RED);
        img.fillOval(0, 0, img.getWidth(), img.getHeight());
        setImage(img);

        // Start the death countdown
        deathTimer.reset();
        deathTimer.start();

        // AudioManager.play("death_sound");
    }

    private void handleDeath(MyWorld world) {
        deathTimer.update(world);
        if (deathTimer.isExpired()) {
            world.getGSM().changeState(new GameOverState());
        }
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    public boolean isDead() { return isDead; }
}