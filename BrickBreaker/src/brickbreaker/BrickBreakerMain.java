/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 *
 * @author JamesLaptop
 */
public class BrickBreakerMain extends JFrame {
    
    private AbstractScreen currentScreen;
    private KeyListener input;
    private static boolean isPlaying;
    private Graphics g;
    private double now, lastTime, deltaTime;
    private double tickTimer;
    final double tickNum = 60D;
    private double ns = 1000000000 / tickNum;
    
    public BrickBreakerMain(){
        //init the screen
        super("Breakin Bricks");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        currentScreen = new TitleScreen();
        add(currentScreen);
        //NEED TO DO THIS EVERYTIME YOU CHANGE SCREENS FOR INPUT TO WORK
        currentScreen.requestFocusInWindow();
        
        pack();
        
        setVisible(true);
        
        //init the basic variables
        isPlaying = true;
        g = currentScreen.getGraphics();
        lastTime = System.nanoTime();
        
        //start game
        playGame();
    }
    
    //MAIN GAME METHOD
    //------------------------------------------------------------------
    public void playGame(){
        
        while(isPlaying){
            
            long now = System.nanoTime();
            
            deltaTime += (now - lastTime) / ns;
            
            lastTime = now;
            
            if(deltaTime >= 1){
                
                currentScreen.runLogic();
                currentScreen.repaint();
                
                deltaTime--;
            }
            
            if(currentScreen.getNextScreen() != ' '){
                switch(currentScreen.getNextScreen()){
                    //play case
                    case 'P' :
                        currentScreen = new PlayScreen();
                        break;
                    case 'I' : 
                        currentScreen = new InstructionScreen();
                        break;
                    case 'H' :
                        //currentScreen = new HighScoresScreen();
                        break;
                    case 'O' :
                        System.out.println("This");
                        break;
                    case 'Q' :
                        setIsPlaying(false);
                        setVisible(false);
                        dispose();
                        break;
                    default : 
                        System.out.println("Unspecified screen switch, fix this in brick breaker main");
                }
                //sets new graphics object and requests input focus
                g = currentScreen.getGraphics();
                currentScreen.requestFocusInWindow();
            }
            
        }
        
        /*
        while(isPlaying){
            //sets the frame cap to whatever you want 
            if(tickTimer >= tickLength){
                currentScreen.runLogic(getDeltaTime());
                currentScreen.repaint();
                
                //resets the tick timer
                tickTimer = 0;
            }
            
            //update the time variables
            updateTimeVars();
            //updates the deltaTime variables
            tickTimer += getDeltaTime();
            
            System.out.println(tickTimer + " : " + getDeltaTime());
        }
        */
        
    }
    
    //other inmportant methods
    //------------------------------------------------------------------
    
    
    
    //time methods
    //------------------------------------------------------------------
    
    
    /*
    
    public long getDeltaTime(){
        deltaTime = timeElapsed - oldTimeElapsed;
        return deltaTime;
    }
    
    public long getTimeElapsed(){
        timeElapsed = System.nanoTime();
        return timeElapsed;
    }
    
    public void updateTimeVars(){
        oldTimeElapsed = timeElapsed;
        timeElapsed = getTimeElapsed();
        
    }
    */
    
    
    //gamestate methods
    public static void setIsPlaying(boolean b){
        isPlaying = b;
    }
    
    public static boolean getIsPlaying(){
        return isPlaying;
    }
    
}
