/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 *
 * @author JamesLaptop
 */
public class BasicParticle extends AbstractScreenObject {
    
    private Shape shape;
    private boolean fade;
    private float fadeAmount;
    
    private int lifeTime, timeAlive;
    
    public BasicParticle(float tX, float tY, int w, int h, int maxLife, Shape tShape, Color color){
        super(tX, tY, w, h, (short) 0, false, false);
        shape = tShape;
        setColor(color);
        init();
    }

    BasicParticle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void init(){
        
        timeAlive = 0;
        
    }

    @Override
    public void move() {
        
        moveY(getDeltaY());
        moveX(getDeltaX());
        
    }

    @Override
    public void handleInput(String inputMethod, int key) {
        
    }

    @Override
    public void runLogic() {
        
        timeAlive++;
        
        if(fade){
            fadeAmount = 255 - (((float)timeAlive)/lifeTime) * 255;
            setColor(new Color(getColor().getRed(), getColor().getGreen(), getColor().getBlue(), fadeAmount));
        }
        
    }

    @Override
    public void drawObject(Graphics2D g) {
        
        g.setColor(getColor());
        
        g.fill(shape);
        
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
    
    
}
