/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import brickbreaker.PlayScreen;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author ge29779
 */
public class BasicEnemyScreenObject extends AbstractEnemyScreenObject{
   
    private final float maxSpeed = 5;
    private final float minSpeed = 1;
    private final float minHealth = 1;
    private final float maxHealthOfEnemy = 100;
    
    public BasicEnemyScreenObject() {
        super();

        init();
    }
    
    public BasicEnemyScreenObject(float xTemp, float yTemp, int widthTemp, int heightTemp, float backgroundDeltaY, boolean collisionTemp, boolean acceptingInput) {
        super(xTemp, yTemp, widthTemp, heightTemp, BASICENEMYSCREENOBJECTID, collisionTemp, acceptingInput);
        
        float speed;
        speed = maxSpeed - (maxSpeed * (getWidth() * getHeight()) / (float)Math.pow(PlayScreen.minBasicEnemySide + PlayScreen.basicEnemyMaxSideDeviation,2));
        if(speed < minSpeed){
            speed = minSpeed;
        }
        
        setSpeed(speed);
        setDegrees(DOWN);
        
        float myHealth;
        myHealth = maxHealthOfEnemy * (getWidth() * getHeight()) / (float)Math.pow(PlayScreen.minBasicEnemySide + PlayScreen.basicEnemyMaxSideDeviation,2);
        if(myHealth < minHealth){
            myHealth = minHealth;
        }
        
        setMaxHealth(myHealth);
        setHealth(myHealth);

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
            setShooting(true);
            setShootTimer(0.0);
        }
    }
    
    
}
