/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import static brickbreaker.BrickBreakerMain.options;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import screenObjects.AbstractScreenObject;
import screenObjects.MenuSelectorIcon;
import screenObjects.PlayerScreenObject;
/**
 *
 * @author JamesLaptop
 */
public class OptionScreen extends AbstractScreen {
    
    private Font normalFont = new Font(Font.MONOSPACED, Font.BOLD, 16);
    private Font titleFont = new Font(Font.MONOSPACED, Font.BOLD, 32);
    private MenuSelectorIcon optionSelector = new MenuSelectorIcon(getWidth()/4 - 50, getHeight()/2 - 179, 10, 10, getHeight() - 80, Color.WHITE, true, true);
    private MenuSelectorIcon optionSelector2 = new MenuSelectorIcon(getWidth()/2 + 110, getHeight()/2 - 179, 10, 10, getHeight() - 80, Color.WHITE, false, false);
    private PlayerScreenObject player = new PlayerScreenObject(3*(getWidth()/4) + 70, getHeight()/2 - 180, 35, 35, false, false);
    private final String fileName = "#src\\assets\\options.properties";
    
    
    public OptionScreen(){
        super();
        
        init();
    }
    
    //default variables
    public void init(){
        
        setInputMethod("default");
        setFont(normalFont);
        getObjectsList().get(SCREENOBJLAYER).add(optionSelector);
        getObjectsList().get(SCREENOBJLAYER).add(optionSelector2);
        optionSelector2.setIsVisible(false);
        optionSelector2.setBlinking(false);
        optionSelector.setMaxPosition(3);
        optionSelector2.setMaxPosition(2);
        optionSelector2.setBlinkTime(17);
        optionSelector.setBlinkTime(17);
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
        drawCenteredString(g, "Options", getWidth()/2, 30, getFont());
        setFont(normalFont);
               
        if(optionSelector.getAcceptingInput() == true){
            drawCenteredString(g, "Press Escape to go Back", getWidth()/4, getHeight()/2 + 200, getFont());
        }
        drawCenteredString(g, "Audio", getWidth()/4, getHeight()/2 - 140, getFont());
        drawCenteredString(g, "Video", getWidth()/4, getHeight()/2 - 160, getFont());
        drawCenteredString(g, "Player", getWidth()/4, getHeight()/2 - 180, getFont());
        drawCenteredString(g, "Game", getWidth()/4, getHeight()/2 - 120, getFont());
        g.drawLine(getWidth()/2, 70, getWidth()/2, getHeight());
        g.drawLine(0, 70, getWidth(), 70);
        
        
        //###################################
        //Game options
        //###################################
        if(optionSelector.getPosition() == 0)
        {
            drawCenteredString(g, "Player Color", 3*(getWidth()/4), getHeight()/2 - 180, getFont());
            player.drawObject(g);
            drawCenteredString(g, "Player Type", 3*(getWidth()/4), getHeight()/2 - 160, getFont());
            drawCenteredString(g, "Bullet Color", 3*(getWidth()/4), getHeight()/2 - 140, getFont());
            
        }
        
        
        //###################################
        //Video options
        //###################################
        if(optionSelector.getPosition() == 1)
        {
            drawCenteredString(g, "Video", 3*(getWidth()/4), getHeight()/2 - 180, getFont());
            drawCenteredString(g, "Video", 3*(getWidth()/4), getHeight()/2 - 160, getFont());
            drawCenteredString(g, "Video", 3*(getWidth()/4), getHeight()/2 - 140, getFont());
        }    
        


        //###################################
        //Volume options
        //###################################
        if(optionSelector.getPosition() == 2)
        {
            drawCenteredString(g, "Volume Up", 3*(getWidth()/4), getHeight()/2 - 180, getFont());
            drawCenteredString(g, "Volume Down", 3*(getWidth()/4), getHeight()/2 - 160, getFont());
            drawCenteredString(g, "Mute", 3*(getWidth()/4), getHeight()/2 - 140, getFont());
            if(options.getProperty("volume") == "0"){
                System.out.println(options.getProperty("volume"));
            }
            else if(options.getProperty("volume") == "1"){
                g.fillRect(getWidth()/4 * 3 - 25, getHeight()/2 + 140, 50, 60);
                System.out.println(options.getProperty("volume"));
            }
            else if(options.getProperty("volume") == "2"){
                g.fillRect(getWidth()/4 * 3 - 25, getHeight()/2 + 80, 50, 120);
                System.out.println(options.getProperty("volume"));
            }
            else if(options.getProperty("volume") == "3"){
                g.fillRect(getWidth()/4 * 3 - 25, getHeight()/2 + 20, 50, 180);
                System.out.println(options.getProperty("volume"));
            }
            else if(options.getProperty("volume") == "4"){
                g.fillRect(getWidth()/4 * 3 - 25, getHeight()/2 - 40, 50, 240);
                System.out.println(options.getProperty("volume"));
            }
            else if(options.getProperty("volume") == "5"){
                g.fillRect(getWidth()/4 * 3 - 25, getHeight()/2 - 100, 50, 300);
                System.out.println(options.getProperty("volume"));
            }
            drawCenteredString(g, options.getProperty("volume"), getWidth()/4 * 3, getHeight()/2 + 225, titleFont);
            
            
             
            
            
        }                                      
        
        
        if(optionSelector.getPosition() == 3)
        {
            drawCenteredString(g, "Difficulty Up", 3*(getWidth()/4), getHeight()/2 - 180, getFont());           
            drawCenteredString(g, "Difficulty Down", 3*(getWidth()/4), getHeight()/2 - 160, getFont());
            drawCenteredString(g, "Game", 3*(getWidth()/4), getHeight()/2 - 140, getFont());
            
            if(options.getProperty("difficulty") == "easy"){
                System.out.println(options.getProperty("difficulty"));
            }
            else if(options.getProperty("difficulty") == "medium"){
                System.out.println(options.getProperty("difficulty"));
            }
            else if(options.getProperty("difficulty") == "hard"){
                System.out.println(options.getProperty("difficulty"));
            }
            drawCenteredString(g, "Difficulty:" + options.getProperty("difficulty"), 3*(getWidth()/4), getHeight()/2, getFont());
        } 
        
        
        
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
    public void specificInput(ArrayList<Integer> inputList, ArrayList<Integer> inputListReleased) {
        
        for (int key : inputList) {

            //give debug the input
            getDebug().inputHandler(getInputMethod(), inputList, getInputMethodRemove(), inputListReleased);

            //give the screen objects the input
            for (ArrayList<AbstractScreenObject> list : getObjectsList()) {
                for (AbstractScreenObject ob : list) {
                    ob.inputHandler(getInputMethod(), inputList, getInputMethodRemove(), inputListReleased);
                }
            }

            switch (getInputMethod()) {

                //default case
                case "default":

                    //cycle through all input
                    if (key == KeyEvent.VK_0){
                        System.out.println("here");
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
                    }
                    
                    if(key == KeyEvent.VK_ESCAPE){
                        setNextScreen('T');
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
                    }
                    
                    if(key == KeyEvent.VK_ENTER){
                        optionSelector.setBlinking(false);
                        optionSelector.setAcceptingInput(false);
                        optionSelector.setIsVisible(false);
                        optionSelector.setIsVisible(true);
                        optionSelector2.setAcceptingInput(true);
                        optionSelector2.setBlinking(true);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
                        setInputMethod("SecondScreen");
                    }
                    break;
                
                case "SecondScreen":
                    
                    //#####################################################
                    //Selector Movement
                    //#####################################################  
                    if(key == KeyEvent.VK_DOWN){
                        if(optionSelector2.getPosition() < optionSelector2.getMaxPosition()){
                            optionSelector2.moveY(+20);
                            optionSelector2.setPosition(optionSelector2.getPosition() + 1);                         
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                    if(key == KeyEvent.VK_UP){
                        if(optionSelector2.getPosition() > 0){
                            optionSelector2.moveY(-20);
                            optionSelector2.setPosition(optionSelector2.getPosition() - 1);
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                    
                    
                    //#####################################################
                    //Back
                    //#####################################################
                    if(key == KeyEvent.VK_ESCAPE){
                        optionSelector2.setBlinking(false);
                        optionSelector2.setAcceptingInput(false);
                        optionSelector2.setIsVisible(false);
                        optionSelector.setAcceptingInput(true);
                        optionSelector.setBlinking(true);
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
                        setInputMethod("default");
                    }          
                                  
                    //#####################################################
                    //###################################
                    //Audio Settings
                    //###################################
                    //#####################################################
                    
                    
                    //###################################
                    //Volume Up
                    //###################################
                    if(optionSelector.getPosition() == 2 && optionSelector2.getPosition() == 0 && options.getProperty("volume").equals("0")){
                        if(key == KeyEvent.VK_ENTER){            
                            options.setProperty("volume", "1");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }   
                    }
                    else if(optionSelector.getPosition() == 2 && optionSelector2.getPosition() == 0 && options.getProperty("volume").equals("1")){
                        if(key == KeyEvent.VK_ENTER){            
                            options.setProperty("volume", "2");
                        }
                        try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                    }
                    else if(optionSelector.getPosition() == 2 && optionSelector2.getPosition() == 0 && options.getProperty("volume").equals("2")){
                        if(key == KeyEvent.VK_ENTER){            
                            options.setProperty("volume", "3");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }   
                    }
                    else if(optionSelector.getPosition() == 2 && optionSelector2.getPosition() == 0 && options.getProperty("volume").equals("3")){
                        if(key == KeyEvent.VK_ENTER){            
                            options.setProperty("volume", "4");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }   
                    }
                    else if(optionSelector.getPosition() == 2 && optionSelector2.getPosition() == 0 && options.getProperty("volume").equals("4")){
                        if(key == KeyEvent.VK_ENTER){            
                            options.setProperty("volume", "5");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }   
                    }                    
                    
                    
                    
                    //###################################
                    //Volume Down
                    //###################################
                    else if(optionSelector.getPosition() == 2 && optionSelector2.getPosition() == 1 && options.getProperty("volume").equals("5")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("volume", "4");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }         
                    else if(optionSelector.getPosition() == 2 && optionSelector2.getPosition() == 1 && options.getProperty("volume").equals("4")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("volume", "3");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }
                    else if(optionSelector.getPosition() == 2 && optionSelector2.getPosition() == 1 && options.getProperty("volume").equals("3")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("volume", "2");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }
                    else if(optionSelector.getPosition() == 2 && optionSelector2.getPosition() == 1 && options.getProperty("volume").equals("2")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("volume", "1");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }         
                    else if(optionSelector.getPosition() == 2){
                        if(optionSelector2.getPosition() == 2){
                            if(key == KeyEvent.VK_ENTER){
                            //############################################
                            //Mute the volume
                            //############################################
                            options.setProperty("volume", "0");
                            
                            }
                                
                        }
                            
                            
                    }
                    
                    //#####################################################
                    //###################################
                    //Video Settings
                    //###################################
                    //#####################################################
                    if(optionSelector.getPosition() == 1){
                        if(optionSelector2.getPosition() == 0){
                            if(key == KeyEvent.VK_ENTER){
                            //############################################
                            //Video option 1
                            //############################################
                            }
                                
                        }
                                                        
                    }
                    if(optionSelector.getPosition() == 1){
                        if(optionSelector2.getPosition() == 1){
                            if(key == KeyEvent.VK_ENTER){
                            //############################################
                            //Video option 2
                            //############################################
                            }
                                
                        }                          
                            
                    }
                    if(optionSelector.getPosition() == 1){
                        if(optionSelector2.getPosition() == 2){
                            if(key == KeyEvent.VK_ENTER){
                            //############################################
                            //Video option 3
                            //############################################
                            }
                                
                        }
                                                       
                    }
                    //#####################################################
                    //###################################
                    //Player Settings
                    //###################################
                    //#####################################################
                    if(optionSelector.getPosition() == 0 && optionSelector2.getPosition() == 0 && options.getProperty("playerColor").equals("rainbowCycle")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("playerColor", "red");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }         
                    else if(optionSelector.getPosition() == 0 && optionSelector2.getPosition() == 0 && options.getProperty("playerColor").equals("red")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("playerColor", "blue");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }
                    else if(optionSelector.getPosition() == 0 && optionSelector2.getPosition() == 0 && options.getProperty("playerColor").equals("blue")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("playerColor", "yellow");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }
                    else if(optionSelector.getPosition() == 0 && optionSelector2.getPosition() == 0 && options.getProperty("playerColor").equals("yellow")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("playerColor", "cyan");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }
                    else if(optionSelector.getPosition() == 0 && optionSelector2.getPosition() == 0 && options.getProperty("playerColor").equals("cyan")){
                        if(key == KeyEvent.VK_ENTER){                        
                            options.setProperty("playerColor", "rainbowRandom");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }  
                    else if(optionSelector.getPosition() == 0 && optionSelector2.getPosition() == 0 && options.getProperty("playerColor").equals("rainbowRandom")){
                        if(key == KeyEvent.VK_ENTER){                        
                            options.setProperty("playerColor", "rainbowCycle");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }  
                        
    
                    //############################################
                    //Solid or Outlined Player
                    //############################################
                    if(optionSelector.getPosition() == 0 && optionSelector2.getPosition() == 1 && options.getProperty("playerType").equals("outlined")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("playerType", "solid");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }         
                    else if(optionSelector.getPosition() == 0 && optionSelector2.getPosition() == 1 && options.getProperty("playerType").equals("solid")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("playerType", "shifting");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }
                    else if(optionSelector.getPosition() == 0 && optionSelector2.getPosition() == 1 && options.getProperty("playerType").equals("shifting")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("playerType", "outlined");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }
                    
                    
                    //Bullet Color
                    if(optionSelector.getPosition() == 0 && optionSelector2.getPosition() == 2 && options.getProperty("bulletColor").equals("rainbowRandom")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("bulletColor", "rainbowCycle");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }
                    else if(optionSelector.getPosition() == 0 && optionSelector2.getPosition() == 2 && options.getProperty("bulletColor").equals("rainbowCycle")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("bulletColor", "red");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }         
                    else if(optionSelector.getPosition() == 0 && optionSelector2.getPosition() == 2 && options.getProperty("bulletColor").equals("red")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("bulletColor", "blue");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }
                    else if(optionSelector.getPosition() == 0 && optionSelector2.getPosition() == 2 && options.getProperty("bulletColor").equals("blue")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("bulletColor", "yellow");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }
                    else if(optionSelector.getPosition() == 0 && optionSelector2.getPosition() == 2 && options.getProperty("bulletColor").equals("yellow")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("bulletColor", "cyan");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }
                    else if(optionSelector.getPosition() == 0 && optionSelector2.getPosition() == 2
                            && options.getProperty("bulletColor").equals("cyan")){
                        if(key == KeyEvent.VK_ENTER){                        
                            options.setProperty("bulletColor", "rainbowRandom");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }
                    
                    
                    //Difficulty Up
                    if(optionSelector.getPosition() == 3 && optionSelector2.getPosition() == 0 && options.getProperty("difficulty").equals("easy")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("difficulty", "medium");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }         
                    else if(optionSelector.getPosition() == 3 && optionSelector2.getPosition() == 0 && options.getProperty("difficulty").equals("medium")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("difficulty", "hard");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }                            
                    
                    //Difficulty Down
                    if(optionSelector.getPosition() == 3 && optionSelector2.getPosition() == 1 && options.getProperty("difficulty").equals("hard")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("difficulty", "medium");
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }         
                    else if(optionSelector.getPosition() == 3 && optionSelector2.getPosition() == 1 && options.getProperty("difficulty").equals("medium")){
                        if(key == KeyEvent.VK_ENTER){                         
                            options.setProperty("difficulty", "easy");
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(OptionScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                                                    
                    }
                   
                    
                    
                    
                    break;
                        
            }
        }

    }
   
}