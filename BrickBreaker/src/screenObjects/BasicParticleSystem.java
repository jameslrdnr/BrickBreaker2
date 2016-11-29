/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;

/**
 *
 * @author JamesLaptop
 */
public class BasicParticleSystem extends AbstractScreenObject {

    private ArrayList<BasicParticle> particles = new ArrayList<BasicParticle>();
    
    private float particleDensity, spawnRadius = 0, particleSpeed = 4;
    private int lifeTime, lifeTimeVariance = 0, particleWidth, particleHeight;
    private Shape particleShape;
    private boolean inheritInertia = false;
    
    public BasicParticleSystem(float x, float y, int particleWidth, int particleHeight, int lifeTime, float density, float radius){
        
        super(x, y, 0, 0, (short)0, false, false);
        particleDensity = density;
        spawnRadius = radius;
        this.lifeTime = lifeTime;
        this.particleWidth = particleWidth;
        this.particleHeight = particleHeight;
        init();
        
    }
    
    public void init(){
        
        
        
    }
    
    @Override
    public void move() {
        
        moveX(getDeltaX());
        moveY(getDeltaY());
        
    }

    @Override
    public void handleInput(String inputMethod, int key) {
        
    }

    @Override
    public void runLogic() {
        
        double randNum = Math.random();
        
        if(randNum < particleDensity){
            spawnParticle();
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
            g.setColor(Color.PINK);
            g.fillRect((int)getX() - 5, (int)getY() - 5, 10, 10);
            g.setColor(tempC);
        }
        
    }
    
    public void spawnParticle(){
        
        float spawnX = 50, spawnY = 50, tempDX = 1, tempDY = 1;
        
        tempDX = (float) (Math.random() * particleSpeed);
        tempDY = (float) (Math.random() * particleSpeed);
        
        if(spawnRadius == 0){
        spawnX = getX();
        spawnY = getY();
        }
        
        BasicParticle part = new BasicParticle(spawnX, spawnY, particleWidth, particleHeight, lifeTime, particleShape, getColor());
        
        if(inheritInertia){
            
        }else{
            part.setDeltaX(tempDX);
            part.setDeltaY(tempDY);
        }
        
        particles.add(part);
        
    }
    
}
