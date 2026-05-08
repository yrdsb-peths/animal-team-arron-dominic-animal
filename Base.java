import greenfoot.*;

public class Base extends Actor {
    public int lives = GameConfig.BASE_LIVES;
    
    public void takeDamage() {
        lives--;
        // We will expand this later to trigger GameOverState!
    }
}