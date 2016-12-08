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
    
    public BasicBrickObject(float x, float y, int width, int height){
        
        super(x, y, width, height, (short)0, true, true);
        
        init();
        
    }
    
    public void init(){
        
        setCollision(false);
        setIsVisible(false);
        
        //set movement mulitpliers to equal width and height
        setxMovementMultiplier(10);
        setyMovementMultiplier(10);
        
        setCollisionShape(new Rectangle((int)getX(), (int)getY(), getWidth(), getHeight()));
        
    }

    @Override
    public void move() {
        
        moveX(getDeltaX());
        moveY(getDeltaY());
        
        ((Rectangle)getCollisionShape()).setLocation((int)getX(), (int)getY());
        
    }

    @Override
    public void handleInput(String inputMethod, ArrayList<Integer> inputList, String inputMethodRemove, ArrayList<Integer> inputListReleased) {
        
    }

    @Override
    public void runLogic() {
        
    }

    @Override
    public void drawObject(Graphics2D g) {
        
        Color tempC = g.getColor();
        
        if(getIsVisible()){
        
        g.setColor(getColor());
        g.fillRect((int) getX(), (int) getY(), getWidth(), getHeight());
        g.setColor(Color.BLACK);
        g.drawRoundRect((int) getX(), (int) getY(), getWidth(), getHeight(), 1, 1);
        
        
        }
        
        if (getDebug().isEnabled()) {
            if (isCollision()) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.RED);
            }

            g.draw(getCollisionShape());
        }
        
        g.setColor(tempC);
    }
    
}
