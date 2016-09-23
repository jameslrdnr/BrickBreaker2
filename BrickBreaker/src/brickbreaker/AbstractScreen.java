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
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;


/**
 *
 * @author ge29779
 */
public abstract class AbstractScreen extends JPanel implements KeyListener, MouseMotionListener {
    
    private final int defaultScreenWidth = 800;
    private final int defaultScreenHeight = 600;
    private int mouseX, mouseY;
    private MouseInputAdapter mouseListener;
    private Font debugFont;
    private BufferedImage back;
    private boolean enableMasterDebug;
    private char nextScreen;
    
    
    //use this to determine if you are in a menu or the game and what controls to use because of that
    private String inputMethod;
    private ArrayList<Integer> inputList, dumpList, removeList;
    
    private ArrayList<AbstractScreenObject> objectsArrayList;
    
    //constructors
    //------------------------------------------------------------------
    public AbstractScreen(){
        //new arraylist for managing screen objects
        this.objectsArrayList = new ArrayList<AbstractScreenObject>();
        
        //adds basic size/listeners
        setSize(defaultScreenWidth, defaultScreenHeight);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        //creates the three input lists
        inputList = new ArrayList<Integer>();
        dumpList = new ArrayList<Integer>();
        removeList = new ArrayList<Integer>();
        
        //adds debug variables
        debugFont = new Font(Font.DIALOG, Font.PLAIN, 12);
        enableMasterDebug = false;
        
        nextScreen = ' ';
        
    }
            
    public AbstractScreen(ArrayList<AbstractScreenObject> objectsArrayList){
        this.objectsArrayList = objectsArrayList;
    }
    
    //logic method
    //------------------------------------------------------------------
    abstract void runLogic();
    
    //graphics methods
    //------------------------------------------------------------------
    abstract void drawGame(Graphics g);
    
    //ScreenObject methods
    //------------------------------------------------------------------
    
    
    
    //draws screen objects in the ScreenObject ArrayList
    public void drawScreenObjects(Graphics g){
        for(AbstractScreenObject ob : getObjectsArray()){
            if(ob.getIsVisible()){
                ob.drawObject(g);
            }
        }
    }
    //runs all the screen object logic methods
    public void runScreenObjectLogic(){
        for(AbstractScreenObject ob : getObjectsArray()){
            ob.runLogic();
        }
    }
    
    //runs all the screen object move methods
    public void moveScreenObjects(){
        for(AbstractScreenObject ob : getObjectsArray()){
            ob.move();
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
        super.paintComponent((Graphics2D) g);
        Graphics2D g2d = (Graphics2D) g;
        //sets a new buffered image in memory to draw them
        back = (BufferedImage)(createImage(getWidth(),getHeight()));
        //creates graphics pointer to image in memory
	Graphics gMemory = back.createGraphics();
        //clears screen
        gMemory.setColor(Color.BLACK);
        gMemory.fillRect(0,0,getWidth(),getHeight());
        //draws using new graphics pointer
        drawGame(gMemory);
        //passes the image from memory to graphics card to handle
        g2d.drawImage(back, null, 0, 0);
    }
    
    @Override
    public void update(Graphics g){
        paintComponent(g);
    }
    
    public void drawCenteredString(Graphics g, String str, int x, int y, Font font){
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        String drawString = str;
        int nX, nY;
        nX = x - (metrics.stringWidth(str) / 2);
        nY = y - (metrics.getHeight() / 2);
        
        g.drawString(drawString, nX, nY);
        
    }
    
    //@@@
    //graphic debug methods
    //@@@
    //graphics debug methods
    
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
    
    //input functions
    //------------------------------------------------------------------
    
    public void specificInput(ArrayList<Integer> inputList){
        
    }
    
    public void handleInput(ArrayList<Integer> inputList){
        //adds all the new input keys to the list of currently pressed keys
        for(int ob : getDumpList()){
            if(getInputList().contains(ob) == false)
                getInputList().add(ob);
        }
        
        for(int ob : getRemoveList()){
            if(getInputList().contains(ob) == true)
                for(int i = 0; i < getRemoveList().size(); i++){
                  if(getRemoveList().get(i) == ob)
                        getRemoveList().remove(i);
            }
        }
        
        //resets dump and remove lists
        getDumpList().clear();
        getRemoveList().clear();
        //gives the current input to the specific input method
        specificInput(inputList);
    }
    
    @Override
    public void keyPressed(KeyEvent ke){
        if(getDumpList().contains(ke.getKeyCode()) == false)
            getDumpList().add(ke.getKeyCode());
    }
    
    @Override
    public void keyReleased(KeyEvent ke){
        System.out.println("*** : " + ke.getKeyCode());
        if(getRemoveList().contains(ke.getKeyCode()) == true)
            for(int i = 0; i < getRemoveList().size(); i++){
                if(getRemoveList().get(i) == ke.getKeyCode())
                    getRemoveList().remove(i);
            }
    }
    
    //ScreenObject ArrayList manipulating
    //------------------------------------------------------------------
    
    public void addToObjectsArray(AbstractScreenObject obj){
        objectsArrayList.add(obj);
    }
    
    public void removeToObjectsArray(int i){
        objectsArrayList.remove(i);
    }
    
    public void removeToObjectsArray(AbstractScreenObject obj){
        objectsArrayList.remove(obj);
    }
    
    //getter/setter functions
    //------------------------------------------------------------------
    public ArrayList<AbstractScreenObject> getObjectsArray(){
        return this.objectsArrayList;
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
    
    
    
    
}