/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import brickbreaker.BrickBreakerMain;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ge29779
 */
public class PlayerScreenObject extends AbstractScreenObject {

    //constants
    //------------------------------------------------------------------
    
    //how long each color should be displayed
    private final double colorTransitionSpeed = Double.parseDouble(BrickBreakerMain.getOptions().getProperty("colorTransitionSpeed"));

    //used for timers
    private final int framesPerSecond = 60;
    
    //total screenWidth - width of border
    private final float usableScreenWidth = BrickBreakerMain.SCREENWIDTH - 7;

    //variables
    //------------------------------------------------------------------
    
    //sets movement multiplier
    private int speed;

    //color used to draw player
    private Color playerColor;

    //array of rainbow colors
    private Color[] colorsArray;

    //choosing random rainbow colors
    private boolean isPlayerColorRandom;

    //cycling through rainbow colors
    private boolean isPlayerColorCycle;

    //which color is currently selected from colorsArray
    private int currentColorIndex;

    //timer used to switch colors
    private double colorTimer;
    
    //all location vars
    private int midPointDistance;
    
    private Point topMPoint, bottomRPoint, bottomLPoint, midIntersectPoint;
    
    

    public PlayerScreenObject() {
        super();

        init();
    }

    public PlayerScreenObject(float xTemp, float yTemp, int widthTemp, int heightTemp, boolean collisionTemp, boolean acceptingInput) {
        super(xTemp, yTemp, widthTemp, heightTemp, (short) 0, collisionTemp, acceptingInput);

        init();
    }

    public void init() {
        
        setLayer(5);
        
        speed = 2;

        setxMovementMultiplier(speed);
        setyMovementMultiplier(speed);

        //create and fill colorsArray
        colorsArray = new Color[6];
        addColors();

        //set to default
        isPlayerColorCycle = false;
        isPlayerColorRandom = false;
        colorTimer = colorTransitionSpeed;

        //option read from options property file
        String colorText = BrickBreakerMain.getOptions().getProperty("playerColor");

        //random rainbow color
        if (colorText.equals("rainbowRandom")) {
            isPlayerColorRandom = true;
            playerColor = colorsArray[0];
        } //cycling through rainbow
        else if (colorText.equals("rainbowCycle")) {
            isPlayerColorCycle = true;
            playerColor = colorsArray[0];
        } //all other cases should be a real color
        else {
            try {
                Field field = Class.forName("java.awt.Color").getField(colorText.toLowerCase()); // toLowerCase because the color fields are RED or red, not Red
                playerColor = (Color) field.get(null);
            } catch (Exception ex) {
                Logger.getLogger(PlayerScreenObject.class.getName()).log(Level.SEVERE, null, ex);
                playerColor = Color.white;
            }
        }
        
        
        //all shape variables/creations
        
        midPointDistance = getHeight()/2;
        //locations of all points
        bottomLPoint = new Point((int) getX(), (int)(getY() + getHeight()));
        topMPoint = new Point((int) (getX() + getWidth() /2), (int) getY());
        bottomRPoint = new Point((int) (getX() + getWidth()), (int)(getY() + getHeight()));
        midIntersectPoint = new Point((int) (getX() + getWidth() /2), (int) (getY() + midPointDistance));
        //creation of arrays containning points for colision
        int[] pointXVals = {(int)bottomLPoint.getX(), (int)bottomRPoint.getX(), (int)topMPoint.getX()};
        int[] pointYVals = {(int)bottomLPoint.getY(), (int)bottomRPoint.getY(), (int)topMPoint.getY()};
        
        setCollisionShape(new Polygon(pointXVals, pointYVals, 3));

    }

    @Override
    public void move() {
        //this will break if either delta is not an int, casting will cut decimals
        bottomLPoint.translate((int)getDeltaX() * getxMovementMultiplier(), (int)getDeltaY() * getyMovementMultiplier());
        bottomRPoint.translate((int)getDeltaX() * getxMovementMultiplier(), (int)getDeltaY() * getyMovementMultiplier());
        topMPoint.translate((int)getDeltaX() * getxMovementMultiplier(), (int)getDeltaY() * getyMovementMultiplier());
        midIntersectPoint.translate((int)getDeltaX() * getxMovementMultiplier(), (int)getDeltaY() * getyMovementMultiplier());
        //update the collision bounds location - will break if you try and use something thts not an integer for deltaX or Y
        ((Polygon)getCollisionShape()).translate((int)getDeltaX() * getxMovementMultiplier(), (int)getDeltaY() * getyMovementMultiplier());
    }

    @Override
    public void handleInput(String inputMethod, int key) {

        switch (inputMethod) {
            case "default": {
                if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                    
                    //not against the wall
                    if(getX() > 0){
                        setDeltaX(-getxMovementMultiplier());
                    }
                    

                } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                    
                    if((getX() + getWidth()) < usableScreenWidth){
                        setDeltaX(getxMovementMultiplier());
                    }
                    

                }
            }
        }
    }

    @Override
    public void runLogic() {
        setDeltaX(0);
        if (isPlayerColorCycle || isPlayerColorRandom) {
            colorTimer += 1.0 / framesPerSecond;
        }
    }

    @Override
    public void drawObject(Graphics2D g) {

        //Set initial values
        //------------------------------------------------------------------
        Color gameColor = g.getColor();

        if (colorTimer >= colorTransitionSpeed && (isPlayerColorCycle || isPlayerColorRandom)) {
            if (isPlayerColorRandom) {
                currentColorIndex = (int) (Math.random() * colorsArray.length);
                playerColor = colorsArray[currentColorIndex];
            } else if (isPlayerColorCycle) {
                if (currentColorIndex != colorsArray.length - 1) {
                    currentColorIndex++;
                } else {
                    currentColorIndex = 0;
                }

                playerColor = colorsArray[currentColorIndex];
            }
            colorTimer = 0;
        }

        g.setColor(playerColor);

        //Triangle 1
        //------------------------------------------------------------------
        int[] Triangle1XVals = {(int)bottomLPoint.getX(), (int)topMPoint.getX(), (int)midIntersectPoint.getX()};
        int[] Triangle1YVals = {(int)bottomLPoint.getY(), (int)topMPoint.getY(), (int)midIntersectPoint.getY()};
        Polygon triangle1 = new Polygon(Triangle1XVals, Triangle1YVals, 3);
        
        g.fill(triangle1);
        
        //Triangle 2
        //------------------------------------------------------------------
        int[] Triangle2XVals = {(int)midIntersectPoint.getX(), (int)topMPoint.getX(), (int)bottomRPoint.getX()};
        int[] Triangle2YVals = {(int)midIntersectPoint.getY(), (int)topMPoint.getY(), (int)bottomRPoint.getY()};
        Polygon triangle2 = new Polygon(Triangle2XVals, Triangle2YVals, 3);
        
        g.fill(triangle2);
        
        
        //enables debug info if debug is enabled
        if (BrickBreakerMain.getDebug().isEnabled()) {
            if (isCollision()) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.RED);
            }

            g.draw(getCollisionShape());
        }
        //Reset variables to initials
        //------------------------------------------------------------------
        g.setColor(gameColor);

    }

    //this is added later (looking at you garett) - K
    public void addColors() {

        //red
        colorsArray[0] = new Color(255, 0, 0);

        //orange
        colorsArray[1] = new Color(255, 127, 0);

        //yellow
        colorsArray[2] = new Color(255, 255, 0);

        //green
        colorsArray[3] = new Color(0, 255, 0);

        //blue
        colorsArray[4] = new Color(0, 0, 255);

        //violet
        colorsArray[5] = new Color(139, 0, 255);

    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

}
