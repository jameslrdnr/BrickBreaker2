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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
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
    
    public static Properties options;
    private static final String optionsFile = "src\\assets\\options.properties";
    
    public BrickBreakerMain(){
        //init the screen
        super("Breakin Bricks");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        currentScreen = new TitleScreen();
        
        //init options and set to values from file
        options = new Properties();
        loadOptions();
        
        
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
                        remove(currentScreen);
                        currentScreen = new PlayScreen();
                        add(currentScreen);
                        currentScreen.requestFocusInWindow();
                        g = currentScreen.getGraphics();
                        pack();
                        currentScreen.requestFocus();
                        break;
                    case 'I' : 
                        remove(currentScreen);
                        currentScreen = new InstructionScreen();
                        add(currentScreen);
                        currentScreen.requestFocusInWindow();
                        g = currentScreen.getGraphics();
                        pack();
                        break;
                    case 'H' :
                        remove(currentScreen);
                        currentScreen = new HighScoresScreen(10);
                        add(currentScreen);
                        currentScreen.requestFocusInWindow();
                        g = currentScreen.getGraphics();
                        pack();
                        break;
                    case 'O' :
                        remove(currentScreen);
                        currentScreen = new OptionScreen();
                        add(currentScreen);
                        g = currentScreen.getGraphics();
                        pack();
                        break;
                    case 'Q' :
                        setIsPlaying(false);
                        setVisible(false);
                        dispose();
                        break;
                    case 'T' :
                        remove(currentScreen);
                        currentScreen = new TitleScreen();
                        add(currentScreen);
                        currentScreen.grabFocus();
                        g = currentScreen.getGraphics();
                        pack();
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
    
    //just to make it easier to use options from other classes
    //------------------------------------------------------------------
    public static Properties getOptions(){
        return options;
    }
    
    //load options from options.properties
    //------------------------------------------------------------------
    public static void loadOptions() {
        try{
            FileInputStream in = new FileInputStream(optionsFile);
            options.load(in);
            in.close();
        }
        catch(Exception e){
            System.out.println("Error reading in options : " + e);
        }
    }
    
    //save optionsToSave to options.properties and set options to options to save
    //------------------------------------------------------------------
    public static void storeOptions(Properties optionsToSave){
        options = optionsToSave;
        try{
            FileOutputStream out = new FileOutputStream(optionsFile);
            options.store(out, optionsFile);
            out.close();
        }
        catch(Exception e){
            System.out.println("Error writing in options : " + e);
        }
    }
    
}
