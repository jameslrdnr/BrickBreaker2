/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author ge29779
 */
public class BasicEnemyScreenObject extends AbstractEnemyScreenObject{
   
//    MAKE IDS
    public BasicEnemyScreenObject() {
        super();

        init();
    }
    
    public BasicEnemyScreenObject(float xTemp, float yTemp, int widthTemp, int heightTemp, float backgroundDeltaY, boolean collisionTemp, boolean acceptingInput) {
        super(xTemp, yTemp, widthTemp, heightTemp, BASICENEMYSCREENOBJECTID, collisionTemp, acceptingInput);
        setDeltaY(backgroundDeltaY);
        
        init();
    }
    
    public void init(){
        
        setColor(Color.red);
        
        setShootTime(1);
        setShootTimer(getShootTime());
        
        Rectangle rect = new Rectangle((int) getX(),(int) getY(),(int) getWidth(),(int) getHeight());
        setMyShape(rect);
        setCollisionShape(getMyShape());
    }
    
    
    

    @Override
    public void handleInput(String inputMethod, ArrayList<Integer> inputList, String inputMethodRemove, ArrayList<Integer> inputListReleased) {
        
    }


    @Override
    public void handleShooting() {
        if(getShootTimer() >= getShootTime() && !checkIsOffScreen(0)){
            
        }
    }
    
    
}
