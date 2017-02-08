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
    private float speed;
    
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
    
    //is three shot active
    private boolean threeShot;
    
    //number of power up shots
    private int PowerUpAmmo;
    
    //all location vars
    private int midPointDistance;
    private Point midIntersectPoint;
    
    //health value for bars etc
    private float health;
    
    //how far rotated the player should be
    private float mouseDegrees;
    
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
    private boolean shooting, movingDiag;
    
    //janky shit for making the mid intersect point move not brokenly
    private float cumulativeDX, cumulativeDY;

    
       
    public PlayerScreenObject(float xTemp, float yTemp, int widthTemp, int heightTemp, boolean collisionTemp, boolean acceptingInput) {
        super(xTemp, yTemp, widthTemp, heightTemp, PLAYERSCREENOBJECTID, collisionTemp, acceptingInput);

        init();
    }

    public void init() {
        
        speed = 3.5f;

        //set to default
        isPlayerColorCycle = false;
        isPlayerColorRandom = false;
        isPlayerColorFade = false;
        colorTimer = colorTransitionSpeed;
        shootTimer = shootTime;
        threeShot = false;
        health = maxHealth;
        cumulativeDX = 0;
        cumulativeDY = 0;
        mouseDegrees = 0f;
        currentDegreesRotated = 0f;
        degreesToRotate = 0f;
        currentDegreesRotated = 0f;
        mouseX = 0f;
        mouseY = 0f;
        movingDiag = false;

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
        
        bottomLPoint = new Point((int) super.getX(), (int)(super.getY() + getHeight()));
        topMPoint = new Point((int) (super.getX() + getWidth() /2), (int) super.getY());
        bottomRPoint = new Point((int) (super.getX() + getWidth()), (int)(super.getY() + getHeight()));
        midIntersectPoint = new Point((int) (super.getX() + getWidth() /2), (int) (super.getY() + midPointDistance));
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
        ps.setInheritInertia(true);
        ps.setColor(playerColor);
        //screen scroll speed
        ps.setDeltaYModifier(2f);
        setParticleSys(ps);
        
        //makes it so you can do immediate input into the game W/ D or ->
        setDegrees(1);

    }

    @Override
    public void move() {
        
        cumulativeDX += getDeltaX() * getSpeed();
        cumulativeDY += getDeltaY() * getSpeed();
        if(cumulativeDX >= 1 || cumulativeDX <= -1){
            midIntersectPoint.translate((int)cumulativeDX, 0);
            translateMyShape((int)cumulativeDX, 0);
            cumulativeDX -= (int)cumulativeDX;
        }
        if(cumulativeDY >= 1 || cumulativeDY <= -1){
            midIntersectPoint.translate(0, (int)cumulativeDY);
            translateMyShape(0, (int)cumulativeDY);
            cumulativeDY -= (int)cumulativeDY;
        }
        
        
        
        rotateMyShape(degreesToRotate, midIntersectPoint.getX(), midIntersectPoint.getY());
        setCollisionShape(getMyShape());
        currentDegreesRotated = mouseDegrees;
        setDegrees(currentDegreesRotated - 90);
        
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
                                setDegrees(LEFT);
                                setSpeed(speed);
                            }
                            else{
                                setDeltaX(0);
                            }

                        } if (keyPressed == KeyEvent.VK_RIGHT || keyPressed == KeyEvent.VK_D) {

                            if (midIntersectPoint.getX() + radius < BrickBreakerMain.SCREENWIDTH) {
                                setDegrees(RIGHT);
                                setSpeed(speed);
                            }
                            else{
                                setDeltaX(0);
                            }

                        } if (keyPressed == KeyEvent.VK_UP || keyPressed == KeyEvent.VK_W) {

                            if (midIntersectPoint.getY() - radius > 0) {
                                setDegrees(UP);
                                setSpeed(speed);
                            }
                            else{
                                setDeltaY(0);
                            }

                        } if (keyPressed == KeyEvent.VK_DOWN || keyPressed == KeyEvent.VK_S) {

                            if (midIntersectPoint.getY() + radius < BrickBreakerMain.SCREENHEIGHT) {
                                setDegrees(DOWN);
                                setSpeed(speed);
                            }
                            else{
                                setDeltaY(0);
                            }
                            
                        }
                        if((inputList.contains(KeyEvent.VK_UP) || inputList.contains(KeyEvent.VK_W)) && (inputList.contains(KeyEvent.VK_RIGHT) || inputList.contains(KeyEvent.VK_D))){
                            setDegrees(UP - 45);
                            setSpeed(speed);
                        }
                        if((inputList.contains(KeyEvent.VK_UP) || inputList.contains(KeyEvent.VK_W)) && (inputList.contains(KeyEvent.VK_LEFT) || inputList.contains(KeyEvent.VK_A))){
                            setDegrees(UP + 45);
                            setSpeed(speed);
                        }
                        if((inputList.contains(KeyEvent.VK_DOWN) || inputList.contains(KeyEvent.VK_S)) && (inputList.contains(KeyEvent.VK_RIGHT) || inputList.contains(KeyEvent.VK_D))){
                            setDegrees(DOWN + 45);
                            setSpeed(speed);
                        }
                        if((inputList.contains(KeyEvent.VK_DOWN) || inputList.contains(KeyEvent.VK_S)) && (inputList.contains(KeyEvent.VK_LEFT) || inputList.contains(KeyEvent.VK_A))){
                            setDegrees(DOWN - 45);
                            setSpeed(speed);
                        }
                        if (keyPressed == KeyEvent.VK_SPACE && shootTimer >= shootTime) {
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
                            setSpeed(0);

                        } else if (keyReleased == KeyEvent.VK_RIGHT || keyReleased == KeyEvent.VK_D) {
                            setSpeed(0);

                        } else if (keyReleased == KeyEvent.VK_UP || keyReleased == KeyEvent.VK_W) {
                            setSpeed(0);

                        } else if (keyReleased == KeyEvent.VK_DOWN || keyReleased == KeyEvent.VK_S) {
                            setSpeed(0);
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
                mouseDegrees = (float) (Math.toDegrees(Math.atan2(mouseDistanceFromPlayerX, mouseDistanceFromPlayerY)));
            } //Q2
            else {
                mouseDegrees = (float) Math.toDegrees(Math.atan2(mouseDistanceFromPlayerX, mouseDistanceFromPlayerY));
            }
        } else //Q4
        if (mouseDistanceFromPlayerY < 0) {
            mouseDegrees = (float) Math.toDegrees(Math.atan2(mouseDistanceFromPlayerX, mouseDistanceFromPlayerY));
        } //Q1
        else {
            mouseDegrees = (float) Math.toDegrees(Math.atan2(mouseDistanceFromPlayerX, mouseDistanceFromPlayerY));
        }
        degreesToRotate = mouseDegrees - currentDegreesRotated;
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

    public void setSpeedHolder(int speedHolder) {
        this.speed = speedHolder;
    }
    
     public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }
    
    public void setThreeShot(boolean threeShot){
        this.threeShot = threeShot;
    }
    
    public void setPowerUpAmmo(int PowerUpAmmo){
        this.PowerUpAmmo = PowerUpAmmo;
    }
    
    public int getPowerUpAmmo(){
        return PowerUpAmmo;
    }
    
    public boolean getThreeShot(){
        return threeShot;
    }
    
    public void changeHealth(float changeBy) {
        if(!Debug.isEnabled()){
            this.health += changeBy;
        }
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

    public float getMouseDegrees() {
        return mouseDegrees;
    }
    
    @Override
    public float getX(){
        return (float)(midIntersectPoint.getX() - getWidth() / 2.0);
    }
    
    @Override
    public float getY(){
        return (float)(midIntersectPoint.getY() - getWidth() / 2.0);
    }
}
