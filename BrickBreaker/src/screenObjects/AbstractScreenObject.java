/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author JamesLaptop
 */
public abstract class AbstractScreenObject {
    
    //global variable declarations
    //------------------------------------------------------------------
    private final short idNum = 1;
    private float x, y, deltaX, deltaY;
    private final float initX, initY;
    private final float rX, bY;
    private int width, height;
    private final boolean collision;
    private Color color;
    private boolean isVisible, acceptingInput; 
    private int yMovementMultiplier, xMovementMultiplier, position, maxPosition;
    
    //init constructors
    public AbstractScreenObject(float xTemp, float yTemp, int widthTemp, int heightTemp, boolean collisionTemp, boolean acceptingInput){
        x = xTemp;
        y = yTemp;
        initX = x;
        initY = y;
        deltaX = 0;
        deltaY = 0;
        width = widthTemp;
        height = heightTemp;
        rX = x + width;
        bY = y + height;
        collision = collisionTemp;
        this.acceptingInput = acceptingInput;
    }
    
    public AbstractScreenObject(){
        x = 0;
        y = 0;
        initX = x;
        initY = y;
        deltaX = 0;
        deltaY = 0;
        width = 0;
        height = 0;
        rX = x + width;
        bY = y + height;
        collision = false;
        acceptingInput = false;
    }    
    
    //getter/setter methods here
    //------------------------------------------------------------------
    public float getX(){
        return x;
    }
    
    public float getY(){
        return y;
    }
    
    public int getHeight(){
        return height;
    }
    
    public int getWidth(){
        return width;
    }
    
    public float getDeltaX(){
        return deltaX;
    }
    
    public float getDeltaY(){
        return deltaY;
    }
    
    public Color getColor(){
        return color;
    }
    
    public boolean getIsVisible(){
        return isVisible;
    }
    
    public boolean getAcceptingInput(){
        return acceptingInput;
    }
    
    public int getyMovementMultiplier() {
        return yMovementMultiplier;
    }

    public void setyMovementMultiplier(int yMovementMultiplier) {
        this.yMovementMultiplier = yMovementMultiplier;
    }

    public int getxMovementMultiplier() {
        return xMovementMultiplier;
    }

    public void setxMovementMultiplier(int xMovementMultiplier) {
        this.xMovementMultiplier = xMovementMultiplier;
    }
    
    public void setX(float xTemp){
        x = xTemp;
    }
    
    public void setY(float yTemp){
        y = yTemp;
    }
    
    public void setWidth(int widthTemp){
        width = widthTemp;
    }
    
    public void setHeight(int heightTemp){
        height = heightTemp;
    }
    
    public void seDeltaX(float deltaXTemp){
        deltaX = deltaXTemp;
    }
    
    public void setDeltaY(float deltaYTemp){
        deltaY = deltaYTemp; 
    }
    
    public void setColor(Color c){
        color = c;
    }
    
    public void setIsVisible(boolean b){
        isVisible = b;
    }
    
    public void setAcceptingInput(boolean b){
        acceptingInput = b;
    }
    
    public short getIdNum() {
        return idNum;
    }

    public float getInitX() {
        return initX;
    }

    public float getInitY() {
        return initY;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getMaxPosition() {
        return maxPosition;
    }

    public void setMaxPosition(int maxPosition) {
        this.maxPosition = maxPosition;
    }
    
    //movement methods
    //------------------------------------------------------------------
    public abstract void move();
    
    public void moveX(float dX){
        setX(getX() + dX * xMovementMultiplier);
    }
    
    public void moveY(float dY){
        setY(getY() + dY * yMovementMultiplier);
    }
    
    //object input handler methods
    //------------------------------------------------------------------
    //this is the method the screen calls
    public void inputHandler(String inputMethod, int key){
        if(getAcceptingInput()){
            handleInput(inputMethod, key);
        }
    }
    //object specific input logic
    public abstract void handleInput(String inputMethod, int key);
    
    //object logic methods
    //------------------------------------------------------------------
    public abstract void runLogic();
    
    
    //collision detection methods
    //------------------------------------------------------------------
    private boolean collidesWith(AbstractScreenObject ob){
        
        float otherX = ob.getX();
        float otherY = ob.getY();
        int otherWidth = ob.getWidth();
        int otherHeight = ob.getHeight();
        
        //checks if it can collide at all
        if(collision == false){
            return false;
        }
        //checks to see if it is on same Y level
        if(otherY+otherHeight >= y && otherY <= y+height){
            //checks to see if it is in the same X level
            if(otherX+width >= x && otherX <= x+width){
                return true;
            }
        }
        
        return false;
    }
    //find which face the other object hit, then return a char representing it (ie: 'R','L','T','B')
    public char willCollideWithFace(AbstractScreenObject ob){
        
        float otherX = ob.getX();
        float otherY = ob.getY();
        float otherDeltaX = ob.getDeltaX();
        float otherDeltaY = ob.getDeltaY(); 
        int otherWidth = ob.getWidth();
        int otherHeight = ob.getHeight();
        float otherRX = otherX + otherWidth;
        float otherBY = otherY + otherHeight;
        
        //checks if it can even collide
        if(collision == false)
            return ' ';
        
        //checks for top collision
        if((otherBY+otherDeltaY >= y-5 && otherBY+otherDeltaY <= y+25) && (otherRX+otherDeltaX >= x && otherX+otherDeltaX <= rX)){
            return 'T';
        }
        //check for bottom
        if((otherY+otherDeltaY >= bY+5 && otherY+otherDeltaY <= bY-25) && (otherRX+otherDeltaX >= x && otherX+otherDeltaX <= rX)){
            return 'B';
        }
        //check for right
        if((otherBY+otherDeltaY >= y && otherY+otherDeltaY <= bY) && (otherX+otherDeltaX >= rX-25 && otherX+otherDeltaX <= rX+5)){
            return 'R';
        }
        //check for left
        if((otherBY+otherDeltaY >= y && otherY+otherDeltaY <= bY) && (otherRX+otherDeltaX >= x-5 && otherX+otherDeltaX <= x+25)){
            return 'L';
        }        
        return ' ';
    }
    
    //graphics methods
    //------------------------------------------------------------------
    public void drawObject(Graphics g){
        System.out.println(getClass().getName() + " : does not have draw method overridden, this is default message");
    }
    
    
    
}
