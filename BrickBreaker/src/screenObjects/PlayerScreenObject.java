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

    //how long each color should be displayed
    private final double shootTime = 0.05;
    
//used for timers
    private final int framesPerSecond = 60;
        
    //highest health value
    private final float maxHealth = 100;


    //variables
    //------------------------------------------------------------------
    
    int rFade, gFade, bFade, fadeStep, fadeRate;
    
    //sets movement multiplier
    private int speed;
    
    //color used to draw player
    private Color playerColor;


    //choosing random rainbow colors
    private boolean isPlayerColorRandom;

    //cycling through rainbow colors
    private boolean isPlayerColorCycle;
    
    //cycling through all coors w/ fade effect
    private boolean isPlayerColorFade;

    //which color is currently selected from rainbowColors
    private int currentColorIndex;

    //timer used to switch colors
    private double colorTimer;
    
    //timer used to shoot
    private double shootTimer;
    
    //all location vars
    private int midPointDistance;
    private Point midIntersectPoint;
    
    //health value for bars etc
    private float health;
    
    //how far rotated the player should be
    private float degrees;
    
    //how far rotated the player currently is be
    private float currentDegreesRotated;
    
    //how far the player still needs to rotate
    private float degreesToRotate;
    
    //location of the mouse
    private float mouseX;
    private float mouseY;
    
    //farthest distance from midPoint
    private double radius;
    
    //playScreen will use this to designate when to creat a bullet
    private boolean shooting;

    
       
    public PlayerScreenObject(float xTemp, float yTemp, int widthTemp, int heightTemp, boolean collisionTemp, boolean acceptingInput) {
        super(xTemp, yTemp, widthTemp, heightTemp, PLAYERSCREENOBJECTID, collisionTemp, acceptingInput);

        init();
    }

    public void init() {
        speed = 2;

        setxMovementMultiplier(speed);
        setyMovementMultiplier(speed);

        

        //set to default
        isPlayerColorCycle = false;
        isPlayerColorRandom = false;
        isPlayerColorFade = false;
        colorTimer = colorTransitionSpeed;
        shootTimer = shootTime;
        health = maxHealth;
        degrees = 0f;
        currentDegreesRotated = 0f;
        degreesToRotate = 0f;
        currentDegreesRotated = 0f;
        mouseX = 0f;
        mouseY = 0f;

        //option read from options property file
        String colorText = BrickBreakerMain.getOptions().getProperty("playerColor");

        //random rainbow color
        if (colorText.equals("rainbowRandom")) {
            isPlayerColorRandom = true;
            playerColor = rainbowColors[0];
        } //cycling through rainbow
        else if (colorText.equals("rainbowCycle")) {
            isPlayerColorCycle = true;
            playerColor = rainbowColors[0];
        }
        else if(colorText.equals("rainbowFade")){
            isPlayerColorFade = true;
            rFade = -1;
            gFade = 1;
            bFade = 0;
            fadeStep = 1;
            fadeRate = 5;
            playerColor = new Color(255, 0, 0);
        }
        //all other cases should be a real color
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
        
        Point topMPoint, bottomRPoint, bottomLPoint;
        
        bottomLPoint = new Point((int) getX(), (int)(getY() + getHeight()));
        topMPoint = new Point((int) (getX() + getWidth() /2), (int) getY());
        bottomRPoint = new Point((int) (getX() + getWidth()), (int)(getY() + getHeight()));
        midIntersectPoint = new Point((int) (getX() + getWidth() /2), (int) (getY() + midPointDistance));
        //creation of arrays containning points for collision
        int[] pointXVals = {(int)bottomLPoint.getX(), (int)topMPoint.getX(), (int)bottomRPoint.getX(), (int)midIntersectPoint.getX()};
        int[] pointYVals = {(int)bottomLPoint.getY(), (int)topMPoint.getY(), (int)bottomRPoint.getY(), (int)midIntersectPoint.getY()};
        
        setMyShape(new Polygon(pointXVals, pointYVals, 4));
        setCollisionShape(getMyShape());
        
        radius = midIntersectPoint.distance(bottomLPoint);
        
        BasicParticleSystem ps = new BasicParticleSystem((float)midIntersectPoint.getX(), (float)midIntersectPoint.getY(), 2, 2, 0, .6f, .6, 4);
        ps.setParticleFade(true);
        ps.setIsVisible(true);
        ps.setPermanent(true);
        ps.setColor(playerColor);
        //screen scroll speed
        ps.setDeltaYModifier(2f);
        setParticleSys(ps);

    }

    @Override
    public void move() {
        //this will break if either delta is not an int, casting will cut decimals
        midIntersectPoint.translate((int)getDeltaX() * getxMovementMultiplier(), (int)getDeltaY() * getyMovementMultiplier());
        translateMyShape(getDeltaX() *  getxMovementMultiplier(), getDeltaY() *  getyMovementMultiplier());

        
        rotateMyShape(degreesToRotate, midIntersectPoint.getX(), midIntersectPoint.getY());
        setCollisionShape(getMyShape());
        currentDegreesRotated = degrees;
        
        getParticleSys().setX((float)midIntersectPoint.getX());
        getParticleSys().setY((float)midIntersectPoint.getY());
        getParticleSys().move();
        
       }
    
    
        @Override
    public void handleInput(String inputMethod, ArrayList<Integer> inputList, String inputMethodRemove, ArrayList<Integer> inputListReleased) {

        if (getAcceptingInput()) {
            for (Integer keyPressed : inputList) {
                switch (inputMethod) {
                    case "default": {
                        if (keyPressed == KeyEvent.VK_LEFT || keyPressed == KeyEvent.VK_A) {

                            //not against the wall    
                            if (midIntersectPoint.getX() - radius > 0) {
                                setDeltaX(-1);
                            }
                            else{
                                setDeltaX(0);
                            }

                        } else if (keyPressed == KeyEvent.VK_RIGHT || keyPressed == KeyEvent.VK_D) {

                            if (midIntersectPoint.getX() + radius < BrickBreakerMain.SCREENWIDTH) {
                                setDeltaX(1);
                            }
                            else{
                                setDeltaX(0);
                            }

                        } else if (keyPressed == KeyEvent.VK_UP || keyPressed == KeyEvent.VK_W) {

                            if (midIntersectPoint.getY() - radius > 0) {
                                setDeltaY(-1);
                            }
                            else{
                                setDeltaY(0);
                            }

                        } else if (keyPressed == KeyEvent.VK_DOWN || keyPressed == KeyEvent.VK_S) {

                            if (midIntersectPoint.getY() + radius < BrickBreakerMain.SCREENHEIGHT) {
                                setDeltaY(1);
                            }
                            else{
                                setDeltaY(0);
                            }
                            
                        } else if (keyPressed == KeyEvent.VK_SPACE && shootTimer >= shootTime) {
                            shooting = true;
                            shootTimer = 0;
                        }
                    }
                }
            }
            
            
            for (Integer keyReleased : inputListReleased) {
                switch (inputMethod) {
                    case "default": {
                        //for(int keyPressed : )
                        if (keyReleased == KeyEvent.VK_LEFT || keyReleased == KeyEvent.VK_A) {
                            setDeltaX(0);

                        } else if (keyReleased == KeyEvent.VK_RIGHT || keyReleased == KeyEvent.VK_D) {
                            setDeltaX(0);

                        } else if (keyReleased == KeyEvent.VK_UP || keyReleased == KeyEvent.VK_W) {
                            setDeltaY(0);

                        } else if (keyReleased == KeyEvent.VK_DOWN || keyReleased == KeyEvent.VK_S) {
                            setDeltaY(0);
                        } 
                    }
                }
            }
        }
    }

    @Override
    public void runLogic() {

        handleDegrees();

        //add to the timer if it is being used
        if (isPlayerColorCycle || isPlayerColorRandom || isPlayerColorFade) {
            colorTimer += 1.0 / framesPerSecond;
        }

        shootTimer += 1.0 / framesPerSecond;

        getParticleSys().runLogic();
        getParticleSys().setColor(playerColor);
        
        if(health > maxHealth)
            health = maxHealth;
        
    }

    public void handleDegrees() {

        //distance between mouse and player on the x - mouseX - playerX
        float mouseDistanceFromPlayerX = (float) (mouseX - midIntersectPoint.getX());

        //distance between mouse and player on the y - mouseY - playerY
        float mouseDistanceFromPlayerY = -(float) (mouseY - midIntersectPoint.getY());

        //third or fourth quadrant, so must add pi/180
        if (mouseDistanceFromPlayerX < 0) {
            //Q3
            if (mouseDistanceFromPlayerY < 0) {
                degrees = (float) (Math.toDegrees(Math.atan2(mouseDistanceFromPlayerX, mouseDistanceFromPlayerY)));
            } //Q2
            else {
                degrees = (float) Math.toDegrees(Math.atan2(mouseDistanceFromPlayerX, mouseDistanceFromPlayerY));
            }
        } else //Q4
        if (mouseDistanceFromPlayerY < 0) {
            degrees = (float) Math.toDegrees(Math.atan2(mouseDistanceFromPlayerX, mouseDistanceFromPlayerY));
        } //Q1
        else {
            degrees = (float) Math.toDegrees(Math.atan2(mouseDistanceFromPlayerX, mouseDistanceFromPlayerY));
        }
        degreesToRotate = degrees - currentDegreesRotated;
    }

    @Override
    public void drawObject(Graphics2D g) {

        //Set initial values
        //------------------------------------------------------------------
        Color gameColor = g.getColor();
        if (isPlayerColorFade) {
            if (playerColor.getRed() >= 255) {
                rFade = -1;
                gFade = 1;
                bFade = 0;
            } else if (playerColor.getGreen() >= 255) {
                rFade = 0;
                gFade = -1;
                bFade = 1;
            } else if (playerColor.getBlue() >= 255) {
                rFade = 1;
                gFade = 0;
                bFade = -1;
            }
            //debug
            //System.out.println((playerColor.getRed() + fadeRate * rFade) + " : " + (playerColor.getGreen() + fadeRate * gFade) + " : " + (playerColor.getBlue() + fadeRate * bFade));
            playerColor = new Color(playerColor.getRed() + fadeRate * rFade, playerColor.getGreen() + fadeRate * gFade, playerColor.getBlue() + fadeRate * bFade);
        } else if (colorTimer >= colorTransitionSpeed && (isPlayerColorCycle || isPlayerColorRandom)) {
            if (isPlayerColorRandom) {
                currentColorIndex = (int) (Math.random() * rainbowColors.length);
                playerColor = rainbowColors[currentColorIndex];
            } else if (isPlayerColorCycle) {
                if (currentColorIndex != rainbowColors.length - 1) {
                    currentColorIndex++;
                } else {
                    currentColorIndex = 0;
                }

                playerColor = rainbowColors[currentColorIndex];
            }
            colorTimer = 0;
        }

        
        g.setColor(playerColor);
        g.fill(getMyShape());
        
        if (BrickBreakerMain.getDebug().isEnabled()) {
            if (isCollision()) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.RED);
            }

            g.draw(getCollisionShape());
            
            //draw the walls
            g.setColor(Color.WHITE);
            g.drawOval((int)midIntersectPoint.getX(),(int) midIntersectPoint.getY(), 3, 3);
        }
        
        getParticleSys().drawObject(g);
        
        //Reset variables to initials
        //------------------------------------------------------------------
        g.setColor(gameColor);

    }
   
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
     public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }
    
    public void changeHealth(float changeBy) {
        this.health += changeBy;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMouseX(float mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(float mouseY) {
        this.mouseY = mouseY;
    }
    
    public boolean isShooting() {
        return shooting;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }
    
    public Color getPlayerColor() {
        return playerColor;
    }

    public Point getMidIntersectPoint() {
        return midIntersectPoint;
    }

    public float getDegrees() {
        return degrees;
    }
}
