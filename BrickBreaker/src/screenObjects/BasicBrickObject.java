/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import static brickbreaker.BrickBreakerMain.getDebug;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author ab29627
 */
public class BasicBrickObject extends AbstractScreenObject{
    
    private boolean blinking;
    private int blinkTime, blinkTimer;
    
    public BasicBrickObject(float x, float y, int width, int height){
        
        super(x, y, width, height, BASICBRICKOBJECTID, true, true);
        
        init();
        
    }
    
    public void init(){
        
        setCollision(false);
        setIsVisible(false);
        
        blinkTime = 0;
        
        //set movement mulitpliers to equal width and height
        setxMovementMultiplier(10);
        setyMovementMultiplier(10);
        
        setCollisionShape(new Rectangle((int)getX(), (int)getY(), getWidth(), getHeight()));
        
    }

    @Override
    public void move() {
        
        moveX(getDeltaX() * getSpeed());
        moveY(getDeltaY() * getSpeed());
        
        ((Rectangle)getCollisionShape()).setLocation((int)getX(), (int)getY());
        
    }

    @Override
    public void handleInput(String inputMethod, ArrayList<Integer> inputList, String inputMethodRemove, ArrayList<Integer> inputListReleased) {
        
    }

    @Override
    public void runLogic() {
        
        if(blinking){
            if(blinkTime > blinkTimer){
                setIsVisible(!getIsVisible());
                blinkTime -= blinkTimer;
            }else{
                blinkTime++;
            }
        }
        
    }

    @Override
    public void drawObject(Graphics2D g) {
        
        Color tempC = g.getColor();
        
        if(getIsVisible()){
        
        g.setColor(getColor());
        g.fillRect((int) getX(), (int) getY(), getWidth(), getHeight());
        g.setColor(Color.BLACK);
        g.drawRect((int) getX(), (int) getY(), getWidth(), getHeight());
        
        
        }
        
        if (getDebug().isEnabled()) {
            if (isCollision()) {
                g.setColor(Color.GREEN);
                g.draw(getCollisionShape());
            } 
//            else {
//                g.setColor(Color.RED);
//            }
//            g.draw(getCollisionShape());
        }
        
        g.setColor(tempC);
    }

    public boolean isBlinking() {
        return blinking;
    }

    public void setBlinking(boolean blinking) {
        this.blinking = blinking;
    }

    public int getBlinkTime() {
        return blinkTime;
    }

    public void setBlinkTime(int blinkTime) {
        this.blinkTime = blinkTime;
    }

    public int getBlinkTimer() {
        return blinkTimer;
    }

    public void setBlinkTimer(int blinkTimer) {
        this.blinkTimer = blinkTimer;
    }
    
    
    
}
