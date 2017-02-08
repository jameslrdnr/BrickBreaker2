/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import com.sun.javafx.geom.Point2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

/**
 *
 * @author JamesLaptop
 */
public class BasicMissleScreenObject extends AbstractScreenObject{
    
    private AbstractScreenObject target;
    private float tX, tY;
    
    private boolean isTurning, trackMode, speedMode, targetNorth, targetEast;
    private float acceleration, maxSpeed, maxTurnSpeed, degreesToTurn,  cumulativeDX, cumulativeDY, lifeTime;
    private float oldDegrees, oldTX, oldTY, oldX, oldY;
    private double centerX, centerY;
    
    public BasicMissleScreenObject(float x, float y, int width, int height, AbstractScreenObject t){
        super(x, y, width, height, BASICMISSILESCREENOBJECTID, true, false);
        
        init();
        
        target = t;
        
        
    }
    
    public void init(){
        
        setSpeed(2.5f);
        setMaxTurnSpeed(2.5f);
        lifeTime = 360;
        maxSpeed = 4;
        acceleration = .05f;
        cumulativeDX = 0;
        cumulativeDY = 0;
        
        //graphics vars
        setColor(new Color(255, 48, 244));
        
        setMyShape(new Rectangle((int)(getX() - getWidth()/2f), (int)(getY() - getHeight()/2f), getWidth(), getHeight()));
        
        centerX = ((Rectangle)getMyShape()).getCenterX();
        centerY = ((Rectangle)getMyShape()).getCenterY();
        
        BasicParticleSystem ps = new BasicParticleSystem(getX(), getY(), 3, 4, 1f, .5f, .5, 0);
        ps.setParticleSpeedVariance(.5f);
        ps.setPermanent(true);
        ps.setColor(Color.GRAY);
        ps.setSpeed(getSpeed());
        ps.setIsVisible(true);
        setParticleSys(ps);
        
        setDegrees(RIGHT);
        rotateMyShape(-getDegrees(), centerX, centerY);
        setCollisionShape(getMyShape());
        
        oldTX = 0;
        oldTY = 0;
        
    }

    @Override
    public void move() {
        
        moveX(getDeltaX() * getSpeed());
        moveY(getDeltaY() * getSpeed());
        
        getParticleSys().setDegrees(getDegrees());
        getParticleSys().setSpeed(getSpeed());
        getParticleSys().movementHandler();
        
        cumulativeDX += getDeltaX() * getSpeed();
        cumulativeDY += getDeltaY() * getSpeed();
        if(cumulativeDX >= 1 || cumulativeDX <= -1){
            translateMyShape((int)cumulativeDX, 0);
            cumulativeDX -= (int)cumulativeDX;
        }
        if(cumulativeDY >= 1 || cumulativeDY <= -1){
            translateMyShape(0, (int)cumulativeDY);
            cumulativeDY -= (int)cumulativeDY;
        }
        
        setCollisionShape(getMyShape());
        
    }

    @Override
    public void handleInput(String inputMethod, ArrayList<Integer> inputList, String inputMethodRemove, ArrayList<Integer> inputListReleased) {
        
    }

    @Override
    public void runLogic() {
        
        lifeTime--;
        
        /*// decides what mode the missile is in
        
        tX = target.getX();
        tY = target.getY();
        float ySide, xSide, tDist, tDeg;
        xSide = tX - getX();
        //y goes down so its weird
        ySide = getY() - tY;
        
        
        
        tDist = (float)Math.sqrt(ySide * ySide + xSide * xSide);
        
        //checking what quadrant it is in, then calc the degrees to turn
        tDeg = (float)Math.toDegrees(Math.atan2(xSide, ySide));
//        if(xSide > 0){
//            //Q1
//            if(ySide > 0){
//                tDeg = getDegrees() - (float)Math.toDegrees(Math.atan2(xSide, ySide));
//            }
//            //Q2
//            else{
//                tDeg = getDegrees() - (float)Math.toDegrees(Math.atan2(xSide, ySide));
//            }
//        }
//        else{
//            //Q3
//            if(ySide < 0){
//                tDeg = getDegrees() - (float)Math.toDegrees(Math.atan2(xSide, ySide));
//            }
//            //Q4
//            else{
//            
//            }
//        }
        
        if(tDeg >= 0){
            
        }
        
        degreesToTurn = tDeg - (getDegrees());
        
        if(degreesToTurn > 180){
            degreesToTurn = degreesToTurn - 360;
        }else if(degreesToTurn < -180){
            degreesToTurn = degreesToTurn + 360;
        }
        
        //calc the degrees needed to rotate
        if(degreesToTurn >= 0){
            //if turning is positive degrees
            if(degreesToTurn >= maxTurnSpeed){
                setDegrees(getDegrees() + maxTurnSpeed);
                degreesToTurn = maxTurnSpeed;
            }else{
                setDegrees(getDegrees() + degreesToTurn);
            }
        }else{
            //if turning is negative degrees
            if(degreesToTurn <= maxTurnSpeed*-1){
                setDegrees(getDegrees() - maxTurnSpeed);
                degreesToTurn = -maxTurnSpeed;
            }else{
                setDegrees(getDegrees() - degreesToTurn);
            }
        }
        
        
        */
        //actually rotate the shape
        
        centerX = getMyShape().getBounds2D().getCenterX();
        centerY = getMyShape().getBounds2D().getCenterY();
        
        degreesToTurn = getDegreesToRotate(target);
        
        float degThisTurn = degreesToTurn;
        
        if(degreesToTurn >= 0){
            System.out.println("Positive - " + degreesToTurn);
            //if turning is positive degrees
            if(degreesToTurn >= maxTurnSpeed){
                degThisTurn = maxTurnSpeed;
                setDegrees(getDegrees()  + maxTurnSpeed);
            }else{
                setDegrees(getDegrees() + degThisTurn);
            }
        }else{
            System.out.println("Negative - " + degreesToTurn);
            //if turning is negative degrees
            if(degreesToTurn <= maxTurnSpeed*-1){
                degThisTurn = maxTurnSpeed*-1;
                setDegrees(getDegrees()  + maxTurnSpeed*-1);
            }else{
                setDegrees(getDegrees() + degThisTurn);
            }
        }
        
        rotateMyShape(-degThisTurn, centerX, centerY);
        
        System.out.println("--- Missile debug info ---");
        System.out.println();
        System.out.println("Targeting info");
        System.out.println("Tracking Mode? : " + trackMode);
        System.out.println("Missile : (" + getX() + ", " + getY() + ")");
        System.out.println("Missile Shape : (" + getMyShape().getBounds2D().getX() + ", " + getMyShape().getBounds2D().getY() + ")");
        System.out.println("Missile Deg : (" + getDegrees() + ")");
        System.out.println("Missile Deltas : (" + getDeltaX() + ", " + getDeltaY() + ")");
        System.out.println("Target : (" + target.getX() + ", " + target.getY() + ")");
        System.out.println("Target Dir : (" + targetNorth + ", " + targetEast + ")");
        System.out.println();
        System.out.println("Calculations");
        //System.out.println("Travel Distance - " + tDist);
        //System.out.println("Degrees - " + getDegrees() + " : " + tDeg);
        System.out.println("Degrees to turn - " + degreesToTurn);
        System.out.println();
        
        degreesToTurn = 0;
        
        oldTX = tX;
        oldTY = tY;
        oldX = getX();
        oldY = getY();
        
        oldDegrees = getDegrees();
        
        getParticleSys().runLogic();
        
    }
    
    @Override
    public boolean shouldDestroyObject(){
        return lifeTime <= 0;
    }

    @Override
    public void drawObject(Graphics2D g) {
        
        getParticleSys().drawObject(g);
        
        g.setColor(getColor());
        //g.rotate(Math.toRadians(getDegrees()), getX() + getWidth()/2, getY() + getHeight()/2);
        
        g.fill(getMyShape());
        //g.rotate(-Math.toRadians(getDegrees()), getX() + getWidth()/2, getY() + getHeight()/2);
        
        
        if(Debug.isEnabled()){
            
            if(isCollision())
                g.setColor(Color.GREEN);
            else
                g.setColor(Color.RED);
            g.draw(getCollisionShape());
            g.setColor(Color.yellow);
            g.fillRect((int)getX(), (int)getY(), 2, 2);
            
        }
        
    }
    
    public void rotateMyShape(){
        Shape tempShape = getMyShape();
        
        //((Polygon)tempShape).
        
    }

    public AbstractScreenObject getTarget() {
        return target;
    }

    public void setTarget(AbstractScreenObject target) {
        this.target = target;
    }

    public boolean isIsTurning() {
        return isTurning;
    }

    public void setIsTurning(boolean isTurning) {
        this.isTurning = isTurning;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getMaxTurnSpeed() {
        return maxTurnSpeed;
    }

    public void setMaxTurnSpeed(float maxTurnSpeed) {
        this.maxTurnSpeed = maxTurnSpeed;
    }

    public float getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(float lifeTime) {
        this.lifeTime = lifeTime;
    }
    
    
    
}
