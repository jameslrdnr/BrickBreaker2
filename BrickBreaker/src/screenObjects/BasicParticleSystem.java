/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 *
 * @author JamesLaptop
 */
public class BasicParticleSystem extends AbstractScreenObject {

    private ArrayList<BasicParticle> particles = new ArrayList<BasicParticle>();
    
    private float spawnRadius = 0, particleSpeedVariance = .05f, minParticleSpeed = .1f, timeAlive, lifeTime, particleLifeTime = 1.3f, lifeTimeVariance = .2f, deltaXModifier, deltaYModifier;
    private double particleDensity = 1.0;
    private int particleWidth = 1, particleHeight = 1;
    private Polygon particlePolygon;
    private boolean inheritInertia = false, particleFade = false, permanent = false;
    
    public BasicParticleSystem(float x, float y, int particleWidth, int particleHeight, float lifeTime, float partLifeTime, double density, float radius){
        
        super(x, y, 0, 0, BASICPARTICLESYSTEMID, false, false);
        particleDensity = density;
        spawnRadius = radius;
        this.lifeTime = lifeTime;
        this.particleLifeTime = partLifeTime;
        this.particleWidth = particleWidth;
        this.particleHeight = particleHeight;
        init();
        
    }
    
    public void init(){
        
        setIsVisible(true);
        
        timeAlive = 0;
        
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
                
        deltaXModifier = 0;
        deltaYModifier = 0;
        
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
    public boolean shouldDestroyObject(){
        
        if(timeAlive >= lifeTime && permanent == false){
            return true;
        }
        
        return false;
    }
    
    @Override
    public void runLogic() {
        
        timeAlive += 1.0/60.0;
        
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
        
        float spawnX = getX(), spawnY = getY(), tempDX = minParticleSpeed, tempDY = minParticleSpeed;
        
        tempDX += (float) (Math.random() * particleSpeedVariance);
        tempDY += (float) (Math.random() * particleSpeedVariance);
        
        if(Math.random() > .5){
            tempDX = tempDX * -1;
        }
        if(Math.random() > .5){
            tempDY = tempDY * -1;
        }
        
        if(spawnRadius != 0){
            spawnX += ((Math.random() * spawnRadius) - (Math.random() * spawnRadius));
            spawnY += ((Math.random() * spawnRadius) - (Math.random() * spawnRadius));
        }
        
        BasicParticle part = new BasicParticle(spawnX, spawnY, particleWidth, particleHeight, particleLifeTime, getColor());
        part.setIsVisible(getIsVisible());
        
        if(inheritInertia){
            tempDX += getDeltaX();
            tempDY += getDeltaY();
        }
        tempDX += deltaXModifier;
        tempDY += deltaYModifier;
        part.setDeltaX(tempDX);
        part.setDeltaY(tempDY);
        
        part.setFade(particleFade);
        
        particles.add(part);
        
    }
    
    public void createParticles(int particleNumber, float radius, int width, int height, float lifeTime, float minSpeed, float speedVariance, boolean fade, Color color) {

        for (int i = 0; i < particleNumber; i++) {

            float spawnX = getX(), spawnY = getY(), tempDX = minSpeed, tempDY = minSpeed;

            spawnX += ((Math.random() * radius) - (Math.random() * radius));
            spawnY += ((Math.random() * radius) - (Math.random() * radius));

            tempDX += (float) (Math.random() * speedVariance);
            tempDY += (float) (Math.random() * speedVariance);

            if (Math.random() > .5) {
                tempDX = tempDX * -1;
            }
            if (Math.random() > .5) {
                tempDY = tempDY * -1;
            }

            if (inheritInertia) {
                tempDX += getDeltaX();
                tempDY += getDeltaY();
            }

            BasicParticle part = new BasicParticle(spawnX, spawnY, width, height, lifeTime, color);
            part.setDeltaX(tempDX);
            part.setDeltaY(tempDY);
            part.setIsVisible(true);
            part.setFade(fade);

            particles.add(part);

        }

    }
    
    public void createParticles(int particleNumber, float radius, int width, int height, float lifeTime, float minSpeed, float speedVariance, boolean fade, Color[] colors){
        int subNum = particleNumber/colors.length;
        
        for(int i = 0; i < subNum; i++){
            for(int c = 0; c < colors.length; c++){
                createParticles(1, radius, width, height, lifeTime, minSpeed, speedVariance, fade, colors[c]);
            }
        }
        
    }
    
    //getter/setters

    public float getSpawnRadius() {
        return spawnRadius;
    }

    public void setSpawnRadius(float spawnRadius) {
        this.spawnRadius = spawnRadius;
    }

    public float getParticleSpeedVariance() {
        return particleSpeedVariance;
    }

    public void setParticleSpeedVariance(float particleSpeedVariance) {
        this.particleSpeedVariance = particleSpeedVariance;
    }

    public float getMinParticleSpeed() {
        return minParticleSpeed;
    }

    public void setMinParticleSpeed(float minParticleSpeed) {
        this.minParticleSpeed = minParticleSpeed;
    }

    public float getLifeTime() {
        return particleLifeTime;
    }

    public void setLifeTime(float lifeTime) {
        this.particleLifeTime = lifeTime;
    }

    public float getLifeTimeVariance() {
        return lifeTimeVariance;
    }

    public void setLifeTimeVariance(float lifeTimeVariance) {
        this.lifeTimeVariance = lifeTimeVariance;
    }

    public double getParticleDensity() {
        return particleDensity;
    }

    public void setParticleDensity(double particleDensity) {
        this.particleDensity = particleDensity;
    }

    public int getParticleWidth() {
        return particleWidth;
    }

    public void setParticleWidth(int particleWidth) {
        this.particleWidth = particleWidth;
    }

    public int getParticleHeight() {
        return particleHeight;
    }

    public void setParticleHeight(int particleHeight) {
        this.particleHeight = particleHeight;
    }

    public Polygon getParticlePolygon() {
        return particlePolygon;
    }

    public void setParticlePolygon(Polygon particlePolygon) {
        this.particlePolygon = particlePolygon;
    }

    public boolean isInheritInertia() {
        return inheritInertia;
    }

    public void setInheritInertia(boolean inheritInertia) {
        this.inheritInertia = inheritInertia;
    }

    public boolean isParticleFade() {
        return particleFade;
    }

    public void setParticleFade(boolean particleFade) {
        this.particleFade = particleFade;
    }

    public float getDeltaXModifier() {
        return deltaXModifier;
    }

    public void setDeltaXModifier(float deltaXModifier) {
        this.deltaXModifier = deltaXModifier;
    }

    public float getDeltaYModifier() {
        return deltaYModifier;
    }

    public void setDeltaYModifier(float deltaYModifier) {
        this.deltaYModifier = deltaYModifier;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }
    
    
}
