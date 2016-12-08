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

/**
 *
 * @author ab29627
 */
public class InstructionScreen extends AbstractScreen{
Font InstructionFont = new Font(Font.MONOSPACED, Font.BOLD, 20);
Font InstructionTitleFont = new Font(Font.MONOSPACED, Font.BOLD, 50);

    public InstructionScreen(){
        super();
    }

    @Override
    void runLogic() {
        
        handleInput(getInputList());
        
    }

    @Override
    void drawGame(Graphics2D g) {
        setBackground(Color.BLACK);
        g.setColor(Color.WHITE);
        drawCenteredString(g, "Instructions", getWidth()/2, getHeight()/2 - 175, InstructionTitleFont);
        drawCenteredString(g, "Use WASD to move", getWidth()/2, getHeight()/2 - 60, InstructionFont);
        drawCenteredString(g, "Hit red bricks for extra points", getWidth()/2, getHeight()/2-40, InstructionFont);
        drawCenteredString(g, "Avoid hitting white bricks or enemies", getWidth()/2, getHeight()/2 - 20, InstructionFont);
        drawCenteredString(g, "Speed increases as you go", getWidth()/2, getHeight()/2, InstructionFont);
        drawCenteredString(g, "The farther you go, the more points you get", getWidth()/2, getHeight()/2 + 20, InstructionFont);
        drawCenteredString(g, "Press ESC To Return to Main Menu", getWidth()/2, getHeight()/2 + 200, InstructionFont);
        
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void specificInput(ArrayList<Integer> inputList, ArrayList<Integer> inputListReleased) {
        
        for( int key : inputList){
            if(key == KeyEvent.VK_ESCAPE){
                setNextScreen('T');
            }
        }
    }
    
}
