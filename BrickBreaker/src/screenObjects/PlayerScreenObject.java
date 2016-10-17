/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import brickbreaker.BrickBreakerMain;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ge29779
 */
public class PlayerScreenObject extends AbstractScreenObject {
    
    Color playerColor;
    boolean isPlayerColorRandom;
    
    ArrayList<Color> colorsArray;
    
    public PlayerScreenObject(){
        super();
    }
    
    public PlayerScreenObject(float xTemp, float yTemp, int widthTemp, int heightTemp, boolean collisionTemp, boolean acceptingInput){
        super(xTemp, yTemp, widthTemp, heightTemp, collisionTemp, acceptingInput);
        setxMovementMultiplier(1);
        setyMovementMultiplier(1);
        
        //create and fill colorsArray
        colorsArray = new ArrayList<>();
        addColors();
        
        //option read from options property file
        String text = BrickBreakerMain.getOptions().getProperty("playerColor");
        
        //random color case
        if(text.equals("random")){
            isPlayerColorRandom = true;
        }
        
        //all other cases should be a real color
        else{
                try{
                    Field field = Class.forName("java.awt.Color").getField(text.toLowerCase()); // toLowerCase because the color fields are RED or red, not Red
                    playerColor = (Color)field.get(null);
                } catch (Exception ex) {
                    Logger.getLogger(PlayerScreenObject.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        
        
    }

    @Override
    public void move() {
        moveX(getDeltaX());
        
    }

    @Override
    public void handleInput(String inputMethod, int key) {
                
        switch(inputMethod){
            case "default" : {
                if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A){
                    setDeltaX(-1);
                       
                    }
                else if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D){
                    setDeltaX(1);
                       
                    }
                }
            }
        
        System.out.println("Delta x: " + getDeltaX());
    }

    @Override
    public void runLogic() {
        setDeltaX(0);
        }

    @Override
    public void drawObject(Graphics2D g) {
        
        //Set initial values
        //------------------------------------------------------------------
        Color gameColor = g.getColor();
        
        if(isPlayerColorRandom){
            int randR = (int) (Math.random() * 200);
            int randG = (int) (Math.random() * 200);
            int randB = (int) (Math.random() * 200);
            playerColor = new Color(randR, randG, randB);
            playerColor.darker();
            
        }
        
        g.setColor(playerColor);
        
        //Points of triangles
        //------------------------------------------------------------------
        Point bottomLeftPoint = new Point((int) getX(), (int)(getY() + getHeight()));
        Point topMiddlePoint = new Point((int) (getX() + getWidth() /2), (int) getY());
        Point bottomMiddlePoint = new Point((int) (getX() + getWidth() /2), (int) (getY() + getHeight() / 2));
        Point bottomRightPoint = new Point((int) (getX() + getWidth()), (int)(getY() + getHeight()));
        
        //Triangle 1
        //------------------------------------------------------------------
        int[] Triangle1XVals = {(int)bottomLeftPoint.getX(), (int)topMiddlePoint.getX(), (int)bottomMiddlePoint.getX()};
        int[] Triangle1YVals = {(int)bottomLeftPoint.getY(), (int)topMiddlePoint.getY(), (int)bottomMiddlePoint.getY()};
        Polygon triangle1 = new Polygon(Triangle1XVals, Triangle1YVals, 3);
        
        g.fill(triangle1);
        
        //Triangle 2
        //------------------------------------------------------------------
        int[] Triangle2XVals = {(int)bottomMiddlePoint.getX(), (int)topMiddlePoint.getX(), (int)bottomRightPoint.getX()};
        int[] Triangle2YVals = {(int)bottomMiddlePoint.getY(), (int)topMiddlePoint.getY(), (int)bottomRightPoint.getY()};
        Polygon triangle2 = new Polygon(Triangle2XVals, Triangle2YVals, 3);
        
        g.fill(triangle2);
        
        
        //Reset variables to initials
        //------------------------------------------------------------------
        g.setColor(gameColor);
                
    }
    
    
    //this is added later (looking at you garett)
    public void addColors(){
        
        //light green
        colorsArray.add(new Color(95, 204, 31));
    }
}
