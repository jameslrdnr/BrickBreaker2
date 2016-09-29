/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import screenObjects.AbstractScreenObject;

/**
 *
 * @author JamesLaptop
 */
public class OptionScreen extends AbstractScreen {
    
    private Font normalFont = new Font(Font.MONOSPACED, Font.BOLD, 16);
    
    public OptionScreen(){
        super();
        setInputMethod("default");
    }

    @Override
    void runLogic() {
        handleInput(getInputList());
        //debug info
        getDebug().setMouseX(getMouseX());
        getDebug().setMouseY(getMouseY());
        getDebug().runLogic();
        
    }

    @Override
    void drawGame(Graphics g) {
        g.setFont(normalFont);
        //adds debug for graphics
        if(getDebug().getIsVisible())
            getDebug().drawObject(g);
        
        drawCenteredString(g, "Test", 150, 150, getFont());
        
        g.fillRect(40, 40, 40, 40);
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
        System.out.println("mouse moves");
    }

    @Override
    public void specificInput(ArrayList<Integer> inputList) {
        
        for (int key : inputList) {

            //give debug the input
            getDebug().inputHandler(getInputMethod(), key);

            //give the screen objects the input
            for (AbstractScreenObject ob : getObjectsArray()) {
                if (ob.getAcceptingInput()) {
                    ob.handleInput(getInputMethod(), key);
                }
            }

            switch (getInputMethod()) {

                //default case
                case "default":

                    //cycle through all input
                    if (key == KeyEvent.VK_0) {
                    }

                    break;

            }
        }

    }
    
}
