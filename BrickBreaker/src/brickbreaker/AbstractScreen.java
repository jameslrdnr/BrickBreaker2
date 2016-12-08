/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

//this is a test, now its of branches

import screenObjects.AbstractScreenObject;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;
import screenObjects.Debug;


/**
 *
 * @author ge29779
 */
public abstract class AbstractScreen extends JPanel implements KeyListener, MouseMotionListener {
    
    private final int defaultScreenWidth = 800;
    private final int defaultScreenHeight = 600;
    private int delayCounter, timeToDelay;
    private boolean delayAllInput;
    private int mouseX, mouseY;
    private BufferedImage back;
    private boolean enableMasterDebug;
    private char nextScreen;
    //adds debug object
    private Debug debug = new Debug(this);
    /*layers:
    0 = backround layer
    1 = backround OBJECT layer
    2 = map layer
    3 = screenobject layer
    4 = player layer
    */
    public final byte BACKROUNDLAYER = 0, BACKROUNDOBJLAYER = 1, MAPLAYER = 2, SCREENOBJLAYER = 3, PLAYERLAYER = 4;
    
    
    //use this to determine if you are in a menu or the game and what controls to use because of that
    private String inputMethod, inputMethodRemove;
    private ArrayList<Integer> inputList, dumpList, removeList;
    
    private ArrayList<ArrayList<AbstractScreenObject>> objectsList  = new ArrayList<ArrayList<AbstractScreenObject>>();
    
    //constructors
    //------------------------------------------------------------------
    public AbstractScreen(){
        //new arraylist for managing screen objects
        
        abstractInit();
        
    }
            
    public AbstractScreen(ArrayList<ArrayList<AbstractScreenObject>> objectsArrayList){
        
        abstractInit();
        
        this.objectsList = objectsArrayList;
        
    }
    
    public void abstractInit(){
        
        //create the screenObjLists
        for(int i = 0; i < 5; i++){
            objectsList.add(new ArrayList<AbstractScreenObject>());
        }
        
        //adds basic size/listeners
        setSize(defaultScreenWidth, defaultScreenHeight);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        
        
        //creates the three input lists
        inputList = new ArrayList<Integer>();
        dumpList = new ArrayList<Integer>();
        removeList = new ArrayList<Integer>();
        
        inputMethod = "default";
        inputMethodRemove = "default";
        
        //default values for delaying input
        delayCounter = 0;
        timeToDelay = 0;
        delayAllInput = false;
        
        //adds debug variables
        debug.setInputDelay(10);
        
        nextScreen = ' ';
    }
    
    //logic method
    //------------------------------------------------------------------
    abstract void runLogic();
    
    //graphics methods
    //------------------------------------------------------------------
    abstract void drawGame(Graphics2D g);
    
    //ScreenObject methods
    //------------------------------------------------------------------
    
    //draws screen objects in the ScreenObject ArrayList
    public void drawScreenObjects(Graphics2D g){
        for(ArrayList<AbstractScreenObject> list : getObjectsList()){
            for(AbstractScreenObject ob : list){
                ob.drawObject(g);
            }
        }
    }
    //runs all the screen object logic methods
    public void runScreenObjectLogic(){
        
        removeObjectsManager();
        
        getDebug().runLogic();
        
        for(ArrayList<AbstractScreenObject> list : getObjectsList()){
            for(AbstractScreenObject ob : list){
                ob.runLogic();
            }
        }
    
    }
    
    public void removeObjectsManager(){

        for (ArrayList<AbstractScreenObject> list : getObjectsList()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).shouldDestroyObject()) {
                    list.remove(i);
                    i--;
                }
            }
        }
    }

    //runs all the screen object move methods
    public void moveScreenObjects(){
        for (ArrayList<AbstractScreenObject> list : getObjectsList()) {
            for (AbstractScreenObject ob : list) {
                ob.move();
            }
        }
    }
    
    //screen changing methods
    //------------------------------------------------------------------
    public char switchScreens(){
        return nextScreen;
    }
    
    public void killScreen(){
        setVisible(false);
        
    }
    
    //all the weird behind the scenes graphics stuff
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //sets a new buffered image in memory to draw them
        if(back == null)
            back = (BufferedImage)(createImage(getWidth(),getHeight()));
        //creates graphics pointer to image in memory
	Graphics2D gMemory = back.createGraphics();
        //clears screen
        gMemory.setColor(Color.BLACK);
        gMemory.fillRect(0,0,getWidth(),getHeight());
        //ACTUALLY DRAWS EVERYTHING
        //draws using new graphics pointer
        drawGame(gMemory);
        //passes the image from memory to graphics card to handle
        g2d.drawImage(back, null, 0, 0);
    }
    
    @Override
    public void update(Graphics g){
        paintComponent(g);
    }
    
    public void drawCenteredString(Graphics2D g, String str, int x, int y, Font font){
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        String drawString = str;
        int nX, nY;
        nX = x - (metrics.stringWidth(str) / 2);
        nY = y + (metrics.getHeight() / 2);
        
        
        g.drawString(drawString, nX, nY);
        
    }
    
    
    //input functions
    //------------------------------------------------------------------
    
    public abstract void specificInput(ArrayList<Integer> inputList, ArrayList<Integer> inputListReleased);
    
    public ArrayList<Integer> handleInput(ArrayList<Integer> inputList) {
        //adds all the new input keys to the list of currently pressed keys
        if (delayAllInput == false) {
            for (int ob : getDumpList()) {
                if (inputList.contains(ob) == false) {
                    inputList.add(ob);
                }
            }
        }
        //removes all keys that have been released
        for (int ob : getRemoveList()) {
            if (inputList.contains(ob)) {
                for (int i = 0; i < inputList.size(); i++) {
                    if (inputList.get(i) == ob) {
                        inputList.remove(i);
                        i--;
                    }
                }
            }
        }
        //resets dump and remove lists
        //gives the current input to the specific input method
        specificInput(inputList, getRemoveList());

        getDumpList().clear();
        getRemoveList().clear();

        return inputList;

    }
    
    @Override
    public void keyPressed(KeyEvent ke){
        if(getDumpList().contains(ke.getKeyCode()) == false)
            getDumpList().add(ke.getKeyCode());
    }
    
    @Override
    public void keyReleased(KeyEvent ke){
        if(getRemoveList().contains(ke.getKeyCode()) == false)
            getRemoveList().add(ke.getKeyCode());
    }
    
    //all the input delay handling methods
    
    public void delayInputManager(){
        
        if(delayAllInput){
            
            setDelayCounter(getDelayCounter() + 1);

            if (getDelayCounter() >= timeToDelay) {
                setDelayCounter(0);
                delayAllInput = false;
            }
            
        }
        
    }
    
    public void delayInput(int time){
        setTimeToDelay(time);
        delayAllInput = true;
    }
    
    //ScreenObject ArrayList manipulating
    //------------------------------------------------------------------
    
    public void addToObjectsArray(byte layer, AbstractScreenObject obj){
        objectsList.get(layer).add(obj);
    }
    
    public void removeToObjectsArray(byte layer, int i){
        objectsList.get(layer).remove(i);
    }
    
    public void removeToObjectsArray(byte layer, AbstractScreenObject obj){
        objectsList.get(layer).remove(obj);
    }
    
    //getter/setter functions
    //------------------------------------------------------------------
    public ArrayList<ArrayList<AbstractScreenObject>> getObjectsList(){
        return this.objectsList;
    }
    
    public int getMouseX(){
        return mouseX;
    }
    
    public int getMouseY(){
        return mouseY;
    }
    
    public String getInputMethod(){
        return inputMethod;
    }
    
    public boolean getEnableMasterDebug(){
        return enableMasterDebug;
    }
    
    public void setMouseX(int mouseXTemp){
        mouseX = mouseXTemp;
    }
    
    public void setMouseY(int mouseYTemp){
        mouseY = mouseYTemp;
    }
    
    public void setInputMethod(String s){
        inputMethod = s;
    }
    
    public void setEnableMasterDebug(boolean b){
        enableMasterDebug = b;
    }

    public char getNextScreen() {
        return nextScreen;
    }

    public void setNextScreen(char nextScreen) {
        this.nextScreen = nextScreen;
    }

    public ArrayList<Integer> getInputList() {
        return inputList;
    }

    public void setInputList(ArrayList<Integer> inputList) {
        this.inputList = inputList;
    }

    public ArrayList<Integer> getDumpList() {
        return dumpList;
    }

    public void setDumpList(ArrayList<Integer> dumpList) {
        this.dumpList = dumpList;
    }

    public ArrayList<Integer> getRemoveList() {
        return removeList;
    }

    public void setRemoveList(ArrayList<Integer> removeList) {
        this.removeList = removeList;
    }

    public Debug getDebug() {
        return debug;
    }

    public void setDebug(Debug debug) {
        this.debug = debug;
    }

    public int getDelayCounter() {
        return delayCounter;
    }

    public void setDelayCounter(int delayCounter) {
        this.delayCounter = delayCounter;
    }

    public int getTimeToDelay() {
        return timeToDelay;
    }

    public void setTimeToDelay(int timeToDelay) {
        this.timeToDelay = timeToDelay;
    }
    
    public boolean isDelayAllInput() {
        return delayAllInput;
    }

    public void setDelayAllInput(boolean delayAllInput) {
        this.delayAllInput = delayAllInput;
    }

    public String getInputMethodRemove() {
        return inputMethodRemove;
    }

    public void setInputMethodRemove(String inputMethodRemove) {
        this.inputMethodRemove = inputMethodRemove;
    }
    
    
}