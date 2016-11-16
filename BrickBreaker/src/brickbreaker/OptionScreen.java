/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import screenObjects.AbstractScreenObject;
import screenObjects.BorderLayout;

/**
 *
 * @author JamesLaptop
 */
public class OptionScreen extends AbstractScreen {
    
    private Font normalFont = new Font(Font.MONOSPACED, Font.BOLD, 16);
    private Font titleFont = new Font(Font.MONOSPACED, Font.BOLD, 32);
    private AbstractScreenObject tempOb;
    
    public OptionScreen(){
        super();
        
        init();
        
    }
    
    //default variables
    public void init(){
        
        setInputMethod("default");
        setFont(normalFont);
        
        tempOb = new BorderLayout(150, 150, 100, 30);
        getObjectsList().add(tempOb);
    }

    @Override
    void runLogic() {
        handleInput(getInputList());
        //debug info
        getDebug().setMouseX(getMouseX());
        getDebug().setMouseY(getMouseY());
        
        runScreenObjectLogic();
        
        moveScreenObjects();
        
    }

    @Override
    void drawGame(Graphics2D g) {
        g.setFont(normalFont);
        setBackground(Color.BLACK);
        g.setColor(Color.WHITE);
        //adds debug for graphics
        if(getDebug().getIsVisible())
            getDebug().drawObject(g);
        
        setFont(titleFont);
        drawCenteredString(g, "Options", getWidth()/2, 75, getFont());
        setFont(normalFont);
        
        drawScreenObjects(g);
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        setMouseX(me.getX());
        setMouseY(me.getY());
    }

    @Override
    public void specificInput(ArrayList<Integer> inputList) {
        
        for (int key : inputList) {

            //give debug the input
            getDebug().inputHandler(getInputMethod(), key);

            //give the screen objects the input
            for (AbstractScreenObject ob : getObjectsList()) {
                if (ob.getAcceptingInput()) {
                    ob.handleInput(getInputMethod(), key);
                }
            }

            switch (getInputMethod()) {

                //default case
                case "default":

                    //cycle through all input
                    if (key == KeyEvent.VK_0) {
                        System.out.println("here");
                    }
                    
                    if(key == KeyEvent.VK_ESCAPE){
                        setNextScreen('T');
                    }

                    break;

            }
        }

    }
    
}
