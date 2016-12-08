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
import sun.misc.Timeable;

/**
 *
 * @author JamesLaptop
 */
public class BasicParticleSystem extends AbstractScreenObject {

    private ArrayList<BasicParticle> particles = new ArrayList<BasicParticle>();
    
    private float spawnRadius = 0, particleSpeedVariance = .2f, minParticleSpeed = .3f, lifeTime = 1.3f, lifeTimeVariance = .2f;
    private double particleDensity = 1.0;
    private int particleWidth = 1, particleHeight = 1;
    private Polygon particlePolygon;
    private boolean inheritInertia = false;
    
    public BasicParticleSystem(float x, float y, int particleWidth, int particleHeight, float lifeTime, double density, float radius){
        
        super(x, y, 0, 0, (short)0, false, false);
        particleDensity = density;
        spawnRadius = radius;
        this.lifeTime = lifeTime;
        this.particleWidth = particleWidth;
        this.particleHeight = particleHeight;
        init();
        
    }
    
    public void init(){
        
        setIsVisible(true);
        
        int[] xVals = new int[4];
        int[] yVals = new int[4];
        //default Xs
        xVals[0] = (int)getX();
        xVals[1] = (int)getX() + particleWidth;
        xVals[2] = (int)getX() + particleWidth;
        xVals[3] = (int)getX();
        //default Ys
        yVals[0] = (int)getY();
        yVals[1] = (int)getY();
        yVals[2] = (int)getY() + particleHeight;
        yVals[3] = (int)getY() + particleHeight;
        
        particlePolygon = new Polygon(xVals, yVals, 4);
                
    }
    
    @Override
    public void move() {
        
        moveX(getDeltaX());
        moveY(getDeltaY());
        
        for(BasicParticle part : particles){
            part.move();
        }
        
    }

    @Override
    public void handleInput(String inputMethod, ArrayList<Integer> inputList, String inputMethodRemove, ArrayList<Integer> inputListReleased) {
        
    }

    @Override
    public void runLogic() {
        
        if(Math.random() <= particleDensity){
            spawnParticle();
        }
        
        for(int i = 0; i < particles.size(); i++){
            if(particles.get(i).shouldDestroyObject()){
                particles.remove(i);
                i--;
            }
            else
                particles.get(i).runLogic();
        }
        
    }

    @Override
    public void drawObject(Graphics2D g) {
        
        for(BasicParticle piece : particles){
            if(piece.getIsVisible())
                piece.drawObject(g);
        }
        
        if(Debug.isEnabled()){
            Color tempC = g.getColor();
            g.setColor(Color.CYAN);
            g.fillRect((int)getX() - 2, (int)getY() - 2, 4, 4);
            g.setColor(tempC);
        }
        
    }
    
    public void spawnParticle(){
        
        float spawnX = 50, spawnY = 50, tempDX = minParticleSpeed, tempDY = minParticleSpeed;
        
        tempDX += (float) (Math.random() * particleSpeedVariance);
        tempDY += (float) (Math.random() * particleSpeedVariance);
        
        if(Math.random() > .5){
            tempDX = tempDX * -1;
        }
        if(Math.random() > .5){
            tempDY = tempDY * -1;
        }
        
        if(spawnRadius == 0){
        spawnX = getX();
        spawnY = getY();
        }else{
            
        }
        
        BasicParticle part = new BasicParticle(spawnX, spawnY, particleWidth, particleHeight, lifeTime, getColor());
        part.setIsVisible(getIsVisible());
        
        if(inheritInertia){
            tempDX += getDeltaX();
            tempDY += getDeltaY();
        }
        part.setDeltaX(tempDX);
        part.setDeltaY(tempDY);
        
        particles.add(part);
        
    }
    
}
