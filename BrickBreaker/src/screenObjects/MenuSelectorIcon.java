/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author JamesLaptop
 */
public class MenuSelectorIcon extends AbstractScreenObject {
    
    private boolean blinking;
    private int blinkTimer;
    private int blinkTime = 15;
    
    public MenuSelectorIcon(int x, int y, int width, int height, int maxPosition, Color color, boolean blinking, boolean acceptingInput){
        
        super(x, y, width, height, (short)1, false, false);
        
        this.blinking = blinking;
        
        setAcceptingInput(acceptingInput);
        
        setColor(color);
        
        setyMovementMultiplier(20);
        setxMovementMultiplier(20);
        
        setPosition(0);
        setMaxPosition(maxPosition);
        
        setInputDelay(6);
        
    }
    
    //logic method
    @Override
    public void runLogic(){
        
        delayedInputLogicManager();
        
        //blinks the selector if blinking == true
        if(blinking){
            if(blinkTimer >= blinkTime){
                blinkTimer = 0;
                setIsVisible(!getIsVisible());
            }
            else
                blinkTimer ++;
        }
        
    }

    //move method
    @Override
    public void move() {
        
        setX(getX() + getDeltaX());
        setY(getY() + getDeltaY());
        
    }

    //draw method
    @Override
    public void drawObject(Graphics2D g) {
        
        if(getIsVisible())
            g.fillRect((int)getX(), (int)getY(), getWidth(), getHeight());
        
    }
    
    //handle input method
    @Override
    public void handleInput(String inputMethod, ArrayList<Integer> inputList, String inputMethodRemove, ArrayList<Integer> inputListReleased) {

        if (getAcceptingInput()) {
            for (Integer key : inputListReleased) {
                switch (inputMethod) {

                    //default input handling
                    case "default":
                        //handle arrowkey input
                        switch (key) {

                            case KeyEvent.VK_DOWN:
                                //makes sure it will be in bounds
                                if (getPosition() < getMaxPosition()) {
                                    moveYMultiply(+1);
                                    setPosition(getPosition() + 1);
                                    setIsVisible(true);
                                    blinkTimer = 0;
                                    delayInput(getInputDelay());
                                }
                                break;
                            case KeyEvent.VK_UP:
                                //makes sure it will be in bounds
                                if (getPosition() > 0) {
                                    moveYMultiply(-1);
                                    setPosition(getPosition() - 1);
                                    setIsVisible(true);
                                    blinkTimer = 0;
                                    delayInput(getInputDelay());
                                }
                                break;

                        }

                }
            }
        }
    }
    
    //getter/setter methods
    public boolean getBlinking(){
        return blinking;
    }
    
    public int getBlinkTime(){
        return blinkTime;
    }
    
    public void setBlinking(boolean blink){
        blinking = blink;
    }
    
    public void setBlinkTime(int t){
        blinkTime = t;
    }
    
}
