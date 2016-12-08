/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.ArrayList;

/**
 *
 * @author JamesLaptop
 */
public class BasicParticle extends AbstractScreenObject {
    
    private Polygon shape;
    private boolean fade;
    private float fadeAmount;
    int[] xVals;
    int[] yVals;
    float cumulativeDX, cumulativeDY;
    
    private float lifeTime, timeAlive;
    
    public BasicParticle(float tX, float tY, int w, int h, float maxLife, Color color){
        super(tX, tY, w, h, (short) 0, false, false);
        setColor(color);
        lifeTime = maxLife;
        init();
    }

    BasicParticle() {
        
    }
    
    public void init(){
        
        timeAlive = 0;
        
        xVals = new int[4];
        yVals = new int[4];
        
        //default Xs
        xVals[0] = (int)getX();
        xVals[1] = (int)getX() + getWidth();
        xVals[2] = (int)getX() + getWidth();
        xVals[3] = (int)getX();
        //default Ys
        yVals[0] = (int)getY();
        yVals[1] = (int)getY();
        yVals[2] = (int)getY() + getHeight();
        yVals[3] = (int)getY() + getHeight();
        
        shape = new Polygon(xVals, yVals, 4);
        
        cumulativeDX = 0;
        cumulativeDY = 0;
        
    }

    @Override
    public void move() {
        
        moveY(getDeltaY());
        moveX(getDeltaX());
        
        if(cumulativeDX >= 1 || cumulativeDX <= -1){
            shape.translate((int)cumulativeDX, 0);
            if(getDeltaX() > 0)
                cumulativeDX -= (int)cumulativeDX;
            else
                cumulativeDX -= (int)cumulativeDX;
        }
        if(cumulativeDY >= 1 || cumulativeDY <= -1){
            shape.translate(0, (int)cumulativeDY);
            if(getDeltaY() > 0)
                cumulativeDY -= (int)cumulativeDY;
            else
                cumulativeDY -= (int)cumulativeDY;
        }
        
    }

    @Override
    public void handleInput(String inputMethod, ArrayList<Integer> inputList, String inputMethodRemove, ArrayList<Integer> inputListReleased) {
        
    }

    @Override
    public void runLogic() {
        
        timeAlive += 1.0/60.0;
        
        if(fade){
            fadeAmount = 255 - (((float)timeAlive)/lifeTime) * 255;
            setColor(new Color(getColor().getRed(), getColor().getGreen(), getColor().getBlue(), fadeAmount));
        }
        
        cumulativeDX += getDeltaX();
        cumulativeDY += getDeltaY();
        
    }

    @Override
    public void drawObject(Graphics2D g) {
        
        g.setColor(getColor());
        
        g.fill(shape);
        
        if(Debug.isEnabled()){
            g.fillRect((int)getX(), (int)getY(), 5, 5);
        }
        
    }
    
    @Override
    public boolean shouldDestroyObject(){
        //returns true if it has exceeded lifetime
        return timeAlive >= lifeTime;
    }
    
    //getter setters

    public boolean isFade() {
        return fade;
    }

    public void setFade(boolean fade) {
        this.fade = fade;
    }

    public Polygon getShape() {
        return shape;
    }

    public void setShape(Polygon shape) {
        this.shape = shape;
    }
    
    
    
}
