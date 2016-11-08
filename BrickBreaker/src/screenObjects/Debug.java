/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import brickbreaker.BrickBreakerMain;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author JamesLaptop
 */
public class Debug extends AbstractScreenObject{
    
    
    private Font debugFont = new Font(Font.DIALOG, Font.PLAIN, 12);
    private int mouseX, mouseY;
    private MouseMotionListener mouseMotionListener;
    private KeyListener keyListener;
    private static boolean enabled = false;
    
    public Debug(){
        super();
        
        init();
    }
    
    public Debug(KeyListener ks){
        super();
        
        keyListener = ks;
        
        init();
    }
    
    public void init(){
        
        setX(40);
        setY(40);
        
        setAcceptingInput(true);
        setInputDelay(10);
    }

    @Override
    public void move() {
    }

    @Override
    public void handleInput(String inputMethod, int key) {
        if(getAcceptingInput()){
            switch (inputMethod) {
                case "default" :
                    //F3 is the key for debug, enables or disables things respectively
                    if (key == KeyEvent.VK_F3) {
                        setIsVisible(!getIsVisible());
                        setEnabled(!isEnabled());
                        delayInput(getInputDelay());
                    }
                    break;
            }
        }
    }

    @Override
    public void runLogic() {
        
        delayedInputLogicManager();
        
    }
    
    //master debug method (includes every debug method)
    public void masterDebug(Graphics g){
        //get current game params
        Font gameFont = g.getFont();
        Color gameColor = g.getColor();
        
        //set new params
        g.setFont(debugFont);
        g.setColor(Color.ORANGE);
        
        //call specific debug methods
        mouseTrackerDebug(g, 10, 10);
        
        //set game params back the way it was
        g.setFont(gameFont);
        g.setColor(gameColor);
        
    }
    
    
    public void mouseTrackerDebug(Graphics g, int x, int y){
        
        g.drawString("MouseX Position : " + mouseX, x, y);
        g.drawString("MouseY Position : " + mouseY, x, y + 10);
        
    }

    //main draw method
    @Override
    public void drawObject(Graphics2D g) {
        if(enabled)
            masterDebug(g);
    }
    
    //getter/setter methods

    public int getMouseX() {
        return mouseX;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public KeyListener getKeyListener() {
        return keyListener;
    }

    public void setKeyListener(KeyListener keyListener) {
        this.keyListener = keyListener;
    }
    
    
    
}
