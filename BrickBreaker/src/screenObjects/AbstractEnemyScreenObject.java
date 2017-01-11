/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import com.sun.scenario.effect.Offset;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author ge29779
 */
public abstract class AbstractEnemyScreenObject extends AbstractScreenObject{
    
    //constants
    //------------------------------------------------------------------
    
    //used for timers
    private final int framesPerSecond = 60;
    
    //highest health value
    private final float maxHealth = 100;
    
    //variables
    //------------------------------------------------------------------
    
    //how long between shooting
    private double shootTime;
    
    //timer used to shoot
    private double shootTimer;

    

    public void setShootTimer(double shootTimer) {
        this.shootTimer = shootTimer;
    }
    
    //health value for bars etc
    private float health;
    
    public AbstractEnemyScreenObject() {
        super();

        init();
    }
    
    public AbstractEnemyScreenObject(float xTemp, float yTemp, int widthTemp, int heightTemp, float backgroundDeltaY, boolean collisionTemp, boolean acceptingInput) {
        super(xTemp, yTemp, widthTemp, heightTemp, (short) 0, collisionTemp, acceptingInput);
        setDeltaY(backgroundDeltaY);
        
        init();
    }
    
    private void init() {
        health = 50;
    }

    @Override
    public void move() {
        translateMyShape(getDeltaX(), getDeltaY());
    }
    
    @Override
    public void runLogic(){
        handleShooting();
        
        shootTimer += 1.0 / framesPerSecond;
        
        if(getParticleSys() != null){
            getParticleSys().runLogic();
        }
    }

    public abstract void handleShooting();
    
    @Override
    public void drawObject(Graphics2D g) {
        g.setColor(getColor());
        
        g.fill(getMyShape());
        
        if(getHealth() < getMaxHealth()){
            drawHealth(g);
        }
        
    }

    private void drawHealth(Graphics2D g) {
         //inits
        Color initColor = g.getColor();
        
        //change values to use
        g.setColor(Color.red);
        
        
        //draw to rectangles with green on top of red to show health
        g.fillRect((int)getMyShape().getBounds2D().getX(), (int)getMyShape().getBounds2D().getY() - 8, getWidth(), 5);
        g.setColor(Color.GREEN);
        g.fillRect((int)getMyShape().getBounds2D().getX(), (int)getMyShape().getBounds2D().getY() - 8, (int)(getWidth() * getHealth() / getMaxHealth()), 5);
        
        
         //reset to inits
        g.setColor(initColor);
    }

    @Override
    public boolean shouldDestroyObject() {
        return checkIsOffScreen(300);
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

    public double getShootTime() {
        return shootTime;
    }

    public void setShootTime(double shootTime) {
        this.shootTime = shootTime;
    }

    public double getShootTimer() {
        return shootTimer;
    }
}