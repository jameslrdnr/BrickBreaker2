/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import screenObjects.*;

/**
 *
 * @author JamesLaptop
 */
public class PlayScreen extends AbstractScreen {
    
    //########################
    //map generation variables
    
    private BasicBrickObject[][] chunk1, chunk2;
    private final int chunkWidth = 78, chunkHeight = 40;
    private float cubeSpawnRate;
    private float screenScrollSpeed;
    private String currentChunkGenType;
    private int chunkGenCount;
    
    //########################
    
    private Properties IDlist;
    private String IDMapLoc = "src/assets/ObjectIDMap.properties";
    
    AbstractScreenObject player;

    
    public PlayScreen(){
        
        init();
        
    }
    
    public void init(){
        
        setVisible(true);
        
        IDlist = new Properties();
        
        //input handling
        setInputMethod("default");
        
        
        //adds player to screen objects
        player = new PlayerScreenObject(300, 300, 25, 25, true, true);
        player.setIsVisible(true);
        getObjectsArray().add(player);
        
        
        //attempts to load idnumbers
        try {
            FileInputStream in = new FileInputStream(IDMapLoc);
            //trys to add nums to properties variables
            try {
                IDlist.load(in);
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(PlayScreen.class.getName()).log(Level.SEVERE, "failed to add retrieved properties to properties obj", ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PlayScreen.class.getName()).log(Level.SEVERE, "failed to retrive objectsID List", ex);
        }
        
        //done with all file input/IO bullshit because fuck try catchs
        //#################################
        
        
        //#################################
        //all map generation code here (includes speed/spawn vars)
        
        setCurrentChunkGenType("random");
        
        cubeSpawnRate = .01f;
        
        chunk1 = new BasicBrickObject[chunkWidth][chunkHeight];
        chunk2 = new BasicBrickObject[chunkWidth][chunkHeight];
        
        chunk1 = generateChunk(getCurrentChunkGenType(), getChunkGenCount());
        chunk2 = generateChunk(getCurrentChunkGenType(), getChunkGenCount());
        
        //chunk1 = chunkInitRandomizer(chunk1, 0);
        //chunk2 = chunkInitRandomizer(chunk2, 4);
        
        //move first chunk halfway off screen
        for(BasicBrickObject[] slice : chunk1){
            for(BasicBrickObject piece : slice){
                
                piece.moveY(-chunkHeight*2);
                
            }
        }
        
        screenScrollSpeed = .4f;
        
    }
    
    
    @Override
    void runLogic() {
        
        //pass mouse input to debug
        getDebug().setMouseX(getMouseX());
        getDebug().setMouseY(getMouseY());
        
        //move map tiles
        moveScreen(chunk1);
        moveScreen(chunk2);
        
        //run logic() on all objects in objects array
        runScreenObjectLogic();
        
        //input
        handleInput(getInputList());
        
        //run move() on all objects in objects array
        moveScreenObjects();
        
    }

    @Override
    void drawGame(Graphics2D g) {
        
        setBackground(Color.BLACK);
        g.setColor(Color.WHITE);
        
        //draws the 2 map chunks
        drawMap(chunk1, g);
        //drawMap(chunk2, g);
        
        //draw screen objects
        drawScreenObjects(g);
        
        
        
        
        getDebug().drawObject(g);
        
    }

    @Override
    public void specificInput(ArrayList<Integer> inputList) {
        //pass input to screen objects
        for(int key : inputList){
            for (AbstractScreenObject ob : getObjectsArray()) {
                ob.inputHandler(getInputMethod(), key);
            }
        }
        
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //map draw methods
    
    public void drawMap(BasicBrickObject[][] chunk, Graphics2D g){
        
        for(BasicBrickObject[] slice : chunk){
            for(BasicBrickObject piece : slice){
                if(piece.getIsVisible())
                    piece.drawObject(g);
            }
        }
        
    }
    
    //map generation methods
    
    //for generating a new chunk entirely
    public BasicBrickObject[][] chunkRamdomizer(BasicBrickObject id, float rarity){
        
        BasicBrickObject[][] tempChunk = new BasicBrickObject[chunkWidth][chunkHeight];
        
        return chunkRandomizer(id, rarity, tempChunk);
        
    }
    
    public BasicBrickObject[][] chunkRandomizer(BasicBrickObject id, float rarity, BasicBrickObject[][] chunk){
        
        BasicBrickObject[][] tempChunk = chunk;
        
        //randomize the type of block into the chunk w/ rarity
        for(BasicBrickObject[] slice : tempChunk){
            for(BasicBrickObject piece : slice){
                double rand = Math.random();
                if(rand <= rarity){
                    piece = id;
                }
            }
        }
        
        //final return/set methods
        chunk = tempChunk;
        return chunk;
        
    }

    
    /*
    ###############################
    looks fucking awesome but doesnt serve our purpose
    
    public BasicBrickObject[][] chunkInitRandomizer(BasicBrickObject[][] chunk, int count){
        
        BasicBrickObject[][] tempChunk;
        
        //makes the blocks more similar to their neighbors by finding what most bloacks are around them and then changing to the most common type
        for(int i = count; i  > 0; i--){
            
            tempChunk = chunk;
            
            for (int x = 0; x < chunk.length; x++) {
                for (int y = 0; y < chunk[x].length; y++) {
                    //debug stuff
                    //System.out.println(tempChunk[x][y].getIsVisible() + "## (" + x + "," + y + ")");
                    
                    
                    int solidCount = 0, hiddenCount = 0;
                    //handling special cases near edge of array
                    if(x ==0 || x == chunkWidth-1){
                        tempChunk[x][y].setColor(Color.ORANGE);
                        tempChunk[x][y].setIsVisible(true);
                        tempChunk[x][y].setCollision(true);
                    }
                    //special case
                    else if(y == 0){
                        for(int tempX = x-1; tempX <= 1; tempX++){
                            for(int tempY = 0; tempY <= 1; tempY++){
                                if(chunk[x][y].getIsVisible()){
                                    solidCount++;
                                }
                                else{
                                    hiddenCount++;
                                }
                            }
                        }
                        if(solidCount > hiddenCount){
                            tempChunk[x][y].setIsVisible(true);
                            tempChunk[x][y].setCollision(true);
                        }
                        else{
                            tempChunk[x][y].setIsVisible(false);
                            tempChunk[x][y].setCollision(false);
                        }
                    }
                    //special case
                    else if(y == chunkHeight-1){
                        for(int tempX = x-1; tempX <= 1; tempX++){
                            for(int tempY = chunkHeight-2; tempY < chunkHeight; tempY++){
                                if(chunk[tempX][tempY].getIsVisible())
                                    solidCount++;
                                else
                                    hiddenCount++;
                            }
                        }
                        if(solidCount > hiddenCount){
                            tempChunk[x][y].setIsVisible(true);
                            tempChunk[x][y].setCollision(true);
                        }
                        else{
                            tempChunk[x][y].setIsVisible(false);
                            tempChunk[x][y].setCollision(false);
                        }
                    }
                    else{
                        for(int tempX = x-1; tempX <= x+1; tempX++){
                            for(int tempY = y-1; tempY <= y+1; tempY++){
                                if(chunk[tempX][tempY].getIsVisible()){
                                    solidCount++;
                                    System.out.println(tempChunk[x][y].getIsVisible() + "## (" + x + "," + y + ")");
                                }
                                else
                                    hiddenCount++;
                            }
                        }
                        if(solidCount > hiddenCount){
                            tempChunk[x][y].setIsVisible(true);
                            tempChunk[x][y].setCollision(true);
                        }
                        else{
                            tempChunk[x][y].setIsVisible(false);
                            tempChunk[x][y].setCollision(false);
                        }
                    }
                }
            }
            
            chunk = tempChunk;
            
        }
        
        return chunk;
        
    }

*/
    
    public BasicBrickObject[][] generateAlleyChunk(BasicBrickObject[][] inputChunk){
        
        BasicBrickObject[][] tempChunk = inputChunk;
        
        
        
        return tempChunk;
        
    }
    
    public BasicBrickObject[][] generateRandomPlacementChunk(BasicBrickObject[][] chunk){
        
        BasicBrickObject[][] tempChunk = chunk;
        
        for (int x = 0; x < tempChunk.length; x++) {
            for (int y = 0; y < tempChunk[x].length; y++) {
                tempChunk[x][y] = new BasicBrickObject(x*10, y*10, 10, 10);
                tempChunk[x][y].setColor(Color.WHITE);
                tempChunk[x][y].setIsVisible(true);
                tempChunk[x][y].setCollision(true);
                
                //see if it should be filled space
                if(Math.random() > cubeSpawnRate){
                    tempChunk[x][y].setIsVisible(false);
                    tempChunk[x][y].setCollision(false);
                }
                
                //debug stuff
                //System.out.println(tempChunk[x][y].getIsVisible() + "## (" + x + "," + y + ")");
            }
        }
        
        return tempChunk;
        
    }
    
    public void moveScreen(BasicBrickObject[][] tempChunk){
        
        for(BasicBrickObject[] slice : tempChunk){
            for(BasicBrickObject piece : slice){
                piece.moveY(screenScrollSpeed);
            }
        }
    }
    
    
    
    
    
    //######################
    //getter/setter methods
    
    public BasicBrickObject[][] generateChunk(String chunkType, int chunkCount){
        
        BasicBrickObject[][] tempChunk = new BasicBrickObject[chunkWidth][chunkHeight];
        
        //add all types of chunks here along w/ logic manager to handle if its time to switch chunk types
        
        
        //final vars to set/return
        setChunkGenCount(getChunkGenCount()-1);
        return tempChunk;
        
    }

    public float getCubeSpawnRate() {
        return cubeSpawnRate;
    }

    public void setCubeSpawnRate(float cubeSpawnRate) {
        this.cubeSpawnRate = cubeSpawnRate;
    }

    public float getScreenScrollSpeed() {
        return screenScrollSpeed;
    }

    public void setScreenScrollSpeed(float screenScrollSpeed) {
        this.screenScrollSpeed = screenScrollSpeed;
    }

    public String getCurrentChunkGenType() {
        return currentChunkGenType;
    }

    public void setCurrentChunkGenType(String currentChunkGenType) {
        this.currentChunkGenType = currentChunkGenType;
    }

    public int getChunkGenCount() {
        return chunkGenCount;
    }

    public void setChunkGenCount(int chunkGenCount) {
        this.chunkGenCount = chunkGenCount;
    }
    
    
    
    
    
    
    
    
    
    
}
