import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Elephant's name is Elephont.
 * 
 * @author Dominic 
 * @version April 27th, 2026
 */
public class Elephant extends Actor
{
    GreenfootSound elephantSound = new GreenfootSound("elephantSound.mp3");
    GreenfootImage[] idleRight = new GreenfootImage[8];
    GreenfootImage[] idleLeft = new GreenfootImage[8];
    // Direction the elephont is facing
    String facing = "right";
    SimpleTimer animationTimer = new SimpleTimer();
    
    public Elephant()// The code gets run when object is created
    {
        for(int i = 0; i < 8; i++)
        {
            idleRight[i] = new GreenfootImage("images/elephant_idle/Idle" + i + ".png");
            idleRight[i].scale(100, 100);
        }
        
        for(int i = 0; i < idleLeft.length; i++)
        {
            idleLeft[i] = new GreenfootImage("images/elephant_idle/Idle" + i + ".png");
            idleLeft[i].mirrorHorizontally();
            idleLeft[i].scale(100, 100);
        }
        
        animationTimer.mark();
        
        
        setImage(idleRight[0]); // beginning image
    }
    
    
    //Animate the elephant
    int imageIndex = 0;
    public void animateElephant()
    {
        if(animationTimer.millisElapsed() < 100)
        {
            return;
        }
        animationTimer.mark();
        
        if(facing.equals("right"))
        {
            setImage(idleRight[imageIndex]);
            imageIndex = (imageIndex + 1) % idleRight.length;
        }
        else
        {
            setImage(idleLeft[imageIndex]);
            imageIndex = (imageIndex + 1) % idleLeft.length;
        }
    }
    
    public void act()
    {
        // Add your action code here.
        if(Greenfoot.isKeyDown("left"))
        {
            move(-5);
            facing = "left";
        }
        else if(Greenfoot.isKeyDown("right"))
        {
            move(5);
            facing = "right";
        }
        
        // Remove banana if elephont touches it
        eat();
        
        animateElephant(); // Animate the elephant
    }
    
    public void eat()
    {
        if(isTouching(Watermelon.class))
        {
            removeTouching(Watermelon.class);
            MyWorld world = (MyWorld) getWorld();
            world.createWatermelon();
            world.increaseScore();
            elephantSound.play();
        }
        // eat the banana and spawn new banana if a banana is eaten
        
    }
}
