/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import brickbreaker.AbstractScreen;
import brickbreaker.BrickBreakerMain;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ge29779
 */
public class BasicPlayerBulletScreenObject extends AbstractScreenObject{
   
    //what angle is the bullet rotated at
    private final float degreesToRotate = 10;
    
    
    //speed of the bullet
    private final int speed = 4;
    
    //the name of the shape of the bullet
    private String shapeName;
    
    //color of the bullet
    private Color color;
    
    //color of the player
    private Color playerColor;
    
    
    //distance out of bounds which will cause bullet to be removed
    private int boundsDistance;

    public BasicPlayerBulletScreenObject() {
        super();

        init();
    }

    public BasicPlayerBulletScreenObject(float xTemp, float yTemp, int widthTemp, int heightTemp, boolean collisionTemp, boolean acceptingInput, float playerDegrees, Color playerColor) {
        super(xTemp, yTemp, widthTemp, heightTemp, BASICPLAYERBULLETSCREENOBJECTID, collisionTemp, acceptingInput);
        if (Debug.isEnabled()) {
            System.out.println("Creating Bullet at X : " + xTemp + " Y : " + yTemp);
        }
        
        //this centers the shape
        float tempX = getX() - getWidth() / 2;
        float tempY = getY() - getHeight() / 2;
        setX(tempX);
        setY(tempY);
        
        //this should set the x and y movement
        //have to subtract 90 degreesToRotate to fix a bug
        setDeltaX((float)(Math.cos(Math.toRadians(playerDegrees-90))));
        setDeltaY((float)(Math.sin(Math.toRadians(playerDegrees-90))));
        setxMovementMultiplier(speed);
        setyMovementMultiplier(speed);
        
        if (Debug.isEnabled()) {
            System.out.println("deltaX : " + getDeltaX());
            System.out.println("deltaY : " + getDeltaY());

        }
        
        
        this.playerColor = playerColor;
        if (Debug.isEnabled()) {
            System.out.println("Degrees : " + this.degreesToRotate);
            System.out.println("playerColor : " + this.playerColor.toString());
        }
        init();
    }
    
    public BasicPlayerBulletScreenObject(float xTemp, float yTemp, int widthTemp, int heightTemp, boolean collisionTemp, boolean acceptingInput, float playerDegrees, Color playerColor, int parentSpeedX, int parentSpeedY) {
        super(xTemp, yTemp, widthTemp, heightTemp, (short) 0, collisionTemp, acceptingInput);
        if (Debug.isEnabled()) {
            System.out.println("Creating Bullet at X : " + xTemp + " Y : " + yTemp);
        }
        
        //this centers the shape
        float tempX = getX() - getWidth() / 2;
        float tempY = getY() - getHeight() / 2;
        setX(tempX);
        setY(tempY);
        
        //this should set the x and y movement
        //have to subtract 90 degreesToRotate to fix a bug
        setDeltaX((float)(Math.cos(Math.toRadians(playerDegrees-90))));
        setDeltaY((float)(Math.sin(Math.toRadians(playerDegrees-90))));
        setxMovementMultiplier(speed + parentSpeedX);
        setyMovementMultiplier(speed - parentSpeedY);
        
        
        if (Debug.isEnabled()) {
            System.out.println("deltaX : " + getDeltaX());
            System.out.println("deltaY : " + getDeltaY());
            System.out.println("xMovementMultiplier : " + getxMovementMultiplier());
            System.out.println("yMovementMultiplier : " + getyMovementMultiplier());
            
            System.out.println("parentSpeedX : " + parentSpeedX);
            System.out.println("parentSpeedY : " + parentSpeedY);

        }
        
        
        this.playerColor = playerColor;
        if (Debug.isEnabled()) {
            System.out.println("Degrees : " + this.degreesToRotate);
            System.out.println("playerColor : " + this.playerColor.toString());
        }
        init();
    }
    
    private void init() {
        
        this.shapeName = BrickBreakerMain.getOptions().getProperty("bulletShape");
        if (Debug.isEnabled()) {
            System.out.println("Shape : " + this.shapeName);
        }
        
        switch(shapeName){
            case "diamond" : createDiamond(); break;
            default : createDiamond();
        }
        
        String colorString = BrickBreakerMain.getOptions().getProperty("bulletColor");
        switch(colorString){
            case "playerColor" : color = playerColor; break;
            case "random" : color = getRandomColor(); break;
            default : try {
                                Field field = Class.forName("java.awt.Color").getField(colorString.toLowerCase()); // toLowerCase because the color fields are RED or red, not Red
                                color = (Color) field.get(null);
                            } catch (Exception ex) {
                                Logger.getLogger(PlayerScreenObject.class.getName()).log(Level.SEVERE, null, ex);
                                color = Color.white;
                            }
        }
        if (Debug.isEnabled()) {
            System.out.println("Color : " + this.color);
        }
        
        if(getHeight() >= getWidth()){
            boundsDistance = getHeight();

        }
        else{
            boundsDistance = getWidth();
        }
        if (Debug.isEnabled()) {
            System.out.println("boundsDistance : " + this.boundsDistance);
        }
        
    }
    
    @Override
    public void move() {
        translateMyShape(getDeltaX() * getxMovementMultiplier(), getDeltaY() * getyMovementMultiplier());
        rotateMyShape(degreesToRotate, getMyShape().getBounds2D().getCenterX(), getMyShape().getBounds2D().getCenterY());
        setCollisionShape(getMyShape());
    }

    @Override
    public boolean shouldDestroyObject(){
        if(checkIsOffScreen((int)boundsDistance)){
            return true;
        }
        else{
            return false;
        }
    }
    
    
    @Override //NOTE - This checks for if the CENTER is off the screen by the amount
    public boolean checkIsOffScreen(int byAmount){
        if(getMyShape().getBounds2D().getCenterX() < 0 - byAmount){
            return true;
        }else if(getMyShape().getBounds2D().getCenterX() > brickbreaker.BrickBreakerMain.SCREENWIDTH + byAmount){
            return true;
        }else if(getMyShape().getBounds2D().getCenterY() < 0 - byAmount){
            return true;
        }else if(getMyShape().getBounds2D().getCenterY() > brickbreaker.BrickBreakerMain.SCREENHEIGHT + byAmount){
            return true;
        }
        
        return false;
    }
    
    @Override
    public void handleInput(String inputMethod, ArrayList<Integer> inputList, String inputMethodRemove, ArrayList<Integer> inputListReleased) {}

    @Override
    public void runLogic() {}

    @Override
    public void drawObject(Graphics2D g) {
        
        Color gameColor = g.getColor();
        
        g.setColor(color);
        
        g.fill(getMyShape());
        
        if(Debug.isEnabled()){
            if (isCollision()) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.RED);
            }

            g.draw(getCollisionShape());
        }
        
        g.setColor(gameColor);
        
    }

    private Color getRandomColor() {
        addColors();
        int ranIndex = (int)(Math.random() * rainbowColors.length);
        return rainbowColors[ranIndex];
    }

    
    private void createDiamond() {
        
        //points for shape creation
        Point topMidPoint = new Point((int) (getX() + (getWidth() / 2)), (int) (getY()));
        Point midLeftPoint = new Point((int) (getX() + getWidth() / 4), (int) (getY() + (getHeight() / 2)));
        Point midRightPoint = new Point((int) (getX() + getWidth() * 3 / 4), (int) (getY() + (getHeight() / 2)));
        Point bottomMidPoint = new Point((int) (getX() + (getWidth() / 2)), (int) (getY() + getHeight()));

        int[] xpoints = {(int)topMidPoint.getX(), (int)midRightPoint.getX(), (int)bottomMidPoint.getX(), (int)midLeftPoint.getX()};
        int[] ypoints = {(int)topMidPoint.getY(), (int)midRightPoint.getY(), (int)bottomMidPoint.getY(), (int)midLeftPoint.getY()};
        
        Shape tempShape = new Polygon(xpoints, ypoints, 4);
        
        setMyShape(tempShape);
        setCollisionShape(getMyShape());
    }
    
    public float getBoundsDistance() {
        return boundsDistance;
    }
    
}
