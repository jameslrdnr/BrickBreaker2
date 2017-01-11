/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author JamesLaptop
 */
public class BasicHealthPickupScreenObject extends BasicPickupScreenObject{
    
    private Color color1, color2;
    private boolean c;
    
    public BasicHealthPickupScreenObject(float x, float y, int width, int height) {
        super(x, y, width, height, BASICHEALTHPICKUPSCREENOBJECTID);
        
        init();
        
    }
    
    public void init(){
        
        c = true;
        
        color1 = new Color(15, 249, 7);
        color2 = new Color(7, 91, 4);
        
        setColorFadeTimer(60);
        setColorFadeTime(0);
        
        setMyShape(new Rectangle((int)getX(), (int)getY(), getWidth(), getHeight()));
        setCollisionShape(getMyShape());
        setCollision(true);
        
        setColor(color1);
        
        setParticleSys(new BasicParticleSystem(getX() + getWidth()/2, getY() + getHeight()/2, 1, 1, -1, 1f, 25, 15));
        getParticleSys().setParticleFade(true);
        getParticleSys().setColor(color1);
        getParticleSys().setIsVisible(true);
        getParticleSys().setParticleSpeedVariance(0);
        
    }
    
    @Override
    public void runLogic(){
        
        if(getColorFadeTime() >= getColorFadeTimer()){
            if(c == true){
                fadeToColor(color2);
                c = false;
            }else{
                fadeToColor(color1);
                c = true;
            }
            setColorFadeTime(getColorFadeTime() - getColorFadeTimer());
        }
        
        setColor(new Color(getColor().getRed() + (int)getrDif(), getColor().getGreen() + (int)getgDif(), getColor().getBlue() + (int)getbDif()));
        
        setColorFadeTime(getColorFadeTime()+ 1);
        
        getParticleSys().runLogic();
        
    }
    
    @Override
    public void drawObject(Graphics2D g){
        getParticleSys().drawObject(g);
        g.setColor(Color.GRAY);
        g.fillRoundRect((int)getX(), (int)getY(), getWidth(), getHeight(), 8, 8);
        g.setColor(getColor());
        g.fillRect((int)getX(), (int)getY() + getHeight()/2 - getHeight()/12, getWidth(), getHeight()/6);
        g.fillRect((int)getX() + getHeight()/2 - getHeight()/12, (int)getY(), getWidth()/6, getHeight());
        
        if(Debug.isEnabled()){
            g.setColor(Color.RED);
            g.draw(getCollisionShape());
        }
        
    }
    
    @Override
    public boolean shouldDestroyObject(){
        return checkIsOffScreen(200);
    }
    
}
