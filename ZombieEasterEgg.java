import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ZombieEasterEgg here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ZombieEasterEgg extends Enemy
{
    /**
     * Act - do whatever the ZombieEasterEgg wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    
    @Override
    protected void handleDeath(MyWorld world) {
        world.removeObject(this);
    }
}

