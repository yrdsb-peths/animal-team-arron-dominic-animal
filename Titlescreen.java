import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Title Screen.
 * 
 * @author (Dominic Meng) 
 * @version (a version number or a date)
 */
public class Titlescreen extends World
{
    Label titleLabel = new Label("The Elephont", 60);
    /**
     * Constructor for objects of class Titlescreen.
     * 
     */
    public Titlescreen()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1); 
        
        GreenfootImage bg = new GreenfootImage("background.jpg");
        bg.scale(600, 400);
        setBackground(bg);

        addObject(titleLabel, getWidth()/2, 200);
        prepare();
    }

    public void act()
    {
        if(Greenfoot.isKeyDown("space"))
        {
            MyWorld gameWorld = new MyWorld();
            Greenfoot.setWorld(gameWorld);
        }
    }
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        Elephant elephant = new Elephant();
        addObject(elephant,458,108);
        elephant.setLocation(316,73);
        Label label = new Label("Use \u2190 and \u2192 to Move", 40);
        addObject(label,220,257);
        label.setLocation(297,243);
        Label label2 = new Label("Press <space> to Start", 40);
        addObject(label2, 249, 331);
        label2.setLocation(295,326);
        label.setLocation(300,266);
        
    }
}
