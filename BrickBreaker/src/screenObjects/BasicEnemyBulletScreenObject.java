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
public class BasicEnemyBulletScreenObject extends AbstractScreenObject{
   
    //default bullet damage
    private final int defaultDamage = 5;
    
    //speed of the bullet
    private final int speed = 4;
    
    //how far off the bullets will aim
    private final double ranVariance = 30;
    
    //distance out of bounds which will cause bullet to be removed
    private int boundsDistance;
    
    //has a bullet collided
    private boolean hasHit;
    
    //how much damage the bullet causes
    private int damage;


    public BasicEnemyBulletScreenObject() {
        super();

        damage = defaultDamage;
        init();
    }

    public BasicEnemyBulletScreenObject(float xTemp, float yTemp, int widthTemp, int heightTemp, boolean collisionTemp, boolean acceptingInput) {
        super(xTemp, yTemp, widthTemp, heightTemp, BASICENEMYBULLETSCREENOBJECTID, collisionTemp, acceptingInput);
        if (Debug.isEnabled()) {
            System.out.println("Creating Enemy Bullet (id : " + getIdNum() + ") at X : " + xTemp + " Y : " + yTemp);
        }
        
        //this centers the shape
        float tempX = getX() - getWidth() / 2;
        float tempY = getY() - getHeight() / 2;
        setX(tempX);
        setY(tempY);
        
        //this should set the x and y movement
        setSpeed(speed);
        setDegrees(DOWN);
        
        if (Debug.isEnabled()) {
            System.out.println("deltaX : " + getDeltaX());
            System.out.println("deltaY : " + getDeltaY());

        }
        
        damage = defaultDamage;
        init();
    }
    
    public BasicEnemyBulletScreenObject(float xTemp, float yTemp, int widthTemp, int heightTemp, boolean collisionTemp, boolean acceptingInput, float parentSpeed, PlayerScreenObject player) {
        super(xTemp, yTemp, widthTemp, heightTemp, BASICPLAYERBULLETSCREENOBJECTID, collisionTemp, acceptingInput);
        if (Debug.isEnabled()) {
            System.out.println("Creating  Enemy Bullet at X : " + xTemp + " Y : " + yTemp);
        }
        
        //this centers the shape
        float tempX = getX() - getWidth() / 2;
        float tempY = getY() - getHeight() / 2;
        setX(tempX);
        setY(tempY);
        
        createDiamond();
        
        //this should set the x and y movement
        setSpeed(speed + parentSpeed);
        
        //distance between mouse and player on the x - mouseX - playerX
        double ranXVariance = Math.pow(-1, (int)(Math.random() * 2)) * Math.random() * ranVariance;
        float playerDistanceFromBulletX = (float) (player.getMidIntersectPoint().getX() - getMyShape().getBounds2D().getCenterX() + ranXVariance);
        
        //distance between mouse and player on the y - mouseY - playerY
        double ranYVariance = Math.pow(-1, (int)(Math.random() * 2)) * Math.random() * ranVariance;
        float playerDistanceFromBulletY = - (float) (player.getMidIntersectPoint().getY() - getMyShape().getBounds2D().getCenterY() + ranYVariance);
        
        setDegrees((float) (Math.toDegrees(Math.atan2(playerDistanceFromBulletY, playerDistanceFromBulletX))));
        rotateMyShape(-(getDegrees() - 90), getMyShape().getBounds2D().getCenterX(), getMyShape().getBounds2D().getCenterY());
        
        if (Debug.isEnabled()) {
            System.out.println("deltaX : " + getDeltaX());
            System.out.println("deltaY : " + getDeltaY());
            System.out.println("xMovementMultiplier : " + getxMovementMultiplier());
            System.out.println("yMovementMultiplier : " + getyMovementMultiplier());
            
        }
        
        damage = defaultDamage;
        init();
    }
    
    private void init() {
        
        setColor(Color.RED);
        
        if(getHeight() >= getWidth()){
            boundsDistance = getHeight();

        }
        else{
            boundsDistance = getWidth();
        }
        if (Debug.isEnabled()) {
            System.out.println("boundsDistance : " + this.boundsDistance);
        }
        hasHit = false;
    }
    
    @Override
    public void move() {
        translateMyShape(getSpeed() * getDeltaX(), getSpeed() * getDeltaY());
        setCollisionShape(getMyShape());
    }

    @Override
    public boolean shouldDestroyObject(){
        if(checkIsOffScreen((int)boundsDistance) || hasHit){
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
        
        
        g.setColor(getColor());
        
        g.fill(getMyShape());
        
        if(Debug.isEnabled()){
            if (isCollision()) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.RED);
            }
        
            g.draw(getCollisionShape());
        }
        
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
    
    public void setHasHit(boolean hasHit){
        this.hasHit = hasHit;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public void setDamage(int damage) {
        this.damage = damage;
    }

}
