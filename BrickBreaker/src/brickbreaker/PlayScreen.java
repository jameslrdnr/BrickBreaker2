/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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

    //#########################
    //all score stuff
    //used for timers and score
    private final int framesPerSecond = 60;

    //a constant which is the score gained every second
    private final double scorePerSecond = 1.0;

    //how much score is added per frame off of time alone
    private final double scorePerFrame = scorePerSecond / framesPerSecond;

    //score variable
    private double score;

    //font used for drawing the score
    private final Font scoreFont = new Font(Font.MONOSPACED, Font.BOLD, 48);

    //########################
    //map generation variables
    private final int chunkWidth = 79, chunkHeight = 40, numChunks = 5;
    private float cubeSpawnRate;
    private float screenScrollSpeed;
    private String currentChunkGenType;
    private int chunkGenCount;

    private int BackroundMinDimension = 10;
    private int backroundMaxDimensionDeviation = 6;

    private float backroundObjectMaxMovementSpeedDeviation = 1.5f;
    private float backroundObjectMinMovementSpeed = 1f;
    private float backroundObjectSpawnRate = .01f;

    //########################
    private Properties IDlist;
    private String IDMapLoc = "src/assets/ObjectIDMap.properties";

    AbstractScreenObject player;

    public PlayScreen() {

        super();

        init();

    }

    public void init() {

        setVisible(true);

        IDlist = new Properties();

        //input handling
        setInputMethod("default");

        //adds player to screen objects
        player = new PlayerScreenObject(300, 300, 25, 25, true, true);
        player.setIsVisible(true);
        getObjectsList().get(PLAYERLAYER).add(player);

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
        //create all the pieces themselves
        for (int t = 0; t < numChunks; t++) {
            for (int y = numChunks; y >= 0; y--) {

                getObjectsList().get(MAPLAYER).add(new BasicBackroundObject(0, -(chunkHeight * t * 10), chunkWidth, chunkHeight));

            }
        }

        System.out.println("######@@@@@");
        System.out.println(getObjectsList().get(MAPLAYER).get(0).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(1).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(2).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(3).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(4).getY());
        System.out.println("######@@@@@");

        setCurrentChunkGenType("random");

        cubeSpawnRate = .015f;

        ((BasicBackroundObject)getObjectsList().get(MAPLAYER).get(0)).setDimensions(generateChunk(getCurrentChunkGenType(), 0, getChunkGenCount()));
        ((BasicBackroundObject)getObjectsList().get(MAPLAYER).get(1)).setDimensions(generateChunk(getCurrentChunkGenType(), 0, getChunkGenCount()));
        ((BasicBackroundObject)getObjectsList().get(MAPLAYER).get(2)).setDimensions(generateChunk(getCurrentChunkGenType(), 0, getChunkGenCount()));
        ((BasicBackroundObject)getObjectsList().get(MAPLAYER).get(3)).setDimensions(generateChunk(getCurrentChunkGenType(), 0, getChunkGenCount()));
        ((BasicBackroundObject)getObjectsList().get(MAPLAYER).get(4)).setDimensions(generateChunk(getCurrentChunkGenType(), 0, getChunkGenCount()));

        //chunk1 = chunkInitRandomizer(chunk1, 0);
        //chunk2 = chunkInitRandomizer(chunk2, 4);
        System.out.println("######^^");
        System.out.println(getObjectsList().get(MAPLAYER).get(0).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(1).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(2).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(3).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(4).getY());
        System.out.println("######^^");

        //move first chunk halfway off screen
        for (int i = 0; i < getObjectsList().get(MAPLAYER).size(); i++) {

            getObjectsList().get(MAPLAYER).get(i).moveYMultiply(-chunkHeight * 2);

        }

        screenScrollSpeed = .4f;

        System.out.println("######");
        System.out.println(getObjectsList().get(MAPLAYER).get(0).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(1).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(2).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(3).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(4).getY());
        System.out.println("######");

        getDebug().setIsVisible(false);
        getDebug().setEnabled(false);

    }

    @Override
    void runLogic() {

        removeObjectsManager();

        //pass mouse input to debug
        if (getDebug().isEnabled()) {
            getDebug().setMouseX(getMouseX());
            getDebug().setMouseY(getMouseY());
        }

        //input
        handleInput(getInputList());
        delayInputManager();

        //handle score
        handleScore();

        //move map tiles
        moveScreen();

        //run move() on all objects in objects array
        moveScreenObjects();

        //run logic() on all objects in objects array
        runScreenObjectLogic();

        //generate all screenobjects that are proceduraly generated
        generateScreenObjects();

        //handle collision of player object
        for (int i = 0; i < getObjectsList().get(MAPLAYER).size(); i++) {
            for (BasicBrickObject[] slice : ((BasicBackroundObject) getObjectsList().get(MAPLAYER).get(i)).getDimensions()) {
                for (BasicBrickObject piece : slice) {
                    if (piece.isCollision()) {
                        if (player.testBoundingIntersection(piece.getCollisionShape())) {
                            if (player.testIntersection(piece.getCollisionShape())) {

                                //insert code for collision here
                                setScore(getScore() - 5);
                                piece.setCollision(false);

                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    void drawGame(Graphics2D g) {

        setBackground(Color.BLACK);
        g.setColor(Color.WHITE);

        //draw screen objects
        drawScreenObjects(g);

        //draws the map chunks
        //drawMap(masterChunk, g);

        //draws debug
        if (getDebug().isEnabled()) {
            getDebug().drawObject(g);
        }

        //draws score
        drawScore(g);

    }

    @Override
    public void specificInput(ArrayList<Integer> inputList) {

        //pass input to screen objects and debug
        for (int key : inputList) {

            getDebug().inputHandler(getInputMethod(), key);

            for (ArrayList<AbstractScreenObject> list : getObjectsList()) {
                for (AbstractScreenObject ob : list) {
                    ob.inputHandler(getInputMethod(), key);
                }
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

    //will be called every frame to do all score handling
    private void handleScore() {
        //used just because it is easier to understand
        double scoreToAdd = 0;

        //score over time
        scoreToAdd += scorePerFrame;

        //add the score accrued this frame to the overall score
        score += scoreToAdd;

    }

    private void drawScore(Graphics2D g) {
        //inits
        Font initFont = g.getFont();
        Color initColor = g.getColor();

        //change values to use
        g.setFont(scoreFont);
        g.setColor(Color.red);

        //draw the score - NOTE - this line is aids
        g.drawString((long) score + "", getWidth() - g.getFontMetrics().stringWidth((long) score + "") - g.getFontMetrics().charWidth(0) / 2 - 10, g.getFontMetrics().getHeight() * 3 / 4);

        //reset to inits
        g.setFont(initFont);
        g.setColor(initColor);
    }

    
    //map draw methods
    /*
    public void drawMap(BasicBrickObject[][][] chunk, Graphics2D g) {

        for (BasicBrickObject[][] tempChunk : chunk) {
            for (BasicBrickObject[] slice : tempChunk) {
                for (BasicBrickObject piece : slice) {
                    if (piece.getIsVisible()) {
                        piece.drawObject(g);
                    }
                }
            }
        }

    }
    */

    //map generation methods
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
    public BasicBrickObject[][] generateSolidChunk(BasicBrickObject[][] chunk, boolean bool) {

        for (BasicBrickObject[] slice : chunk) {
            for (BasicBrickObject piece : slice) {
                piece.setIsVisible(bool);
                piece.setCollision(bool);
            }
        }

        return chunk;

    }

    public BasicBrickObject[][] generateAlleyChunk(BasicBrickObject[][] inputChunk) {

        BasicBrickObject[][] tempChunk = inputChunk;

        return tempChunk;

    }

    public BasicBrickObject[][] generateRandomPlacementChunk(BasicBrickObject[][] chunk) {

        for (int x = 0; x < chunk.length; x++) {
            for (int y = 0; y < chunk[x].length; y++) {
                chunk[x][y].setColor(Color.WHITE);
                chunk[x][y].setIsVisible(true);
                chunk[x][y].setCollision(true);

                //see if it should be filled space
                if (Math.random() > cubeSpawnRate) {
                    chunk[x][y].setIsVisible(false);
                    chunk[x][y].setCollision(false);
                }

                //debug stuff
                //System.out.println(tempChunk[x][y].getIsVisible() + "## (" + x + "," + y + ")");
            }
        }

        return chunk;

    }

    public void moveScreen() {

        
        for (int i = 0; i < getObjectsList().get(MAPLAYER).size(); i++) {
            for (BasicBrickObject[] slice : ((BasicBackroundObject) getObjectsList().get(MAPLAYER).get(i)).getDimensions()) {
                for (BasicBrickObject piece : slice) {
                    piece.moveYMultiply(screenScrollSpeed);
                    piece.move();
                }
            }
        }

        for (int c = 0; c < getObjectsList().get(MAPLAYER).size(); c++) {
            if (getObjectsList().get(MAPLAYER).get(c).getY() > 900) {
                ((BasicBackroundObject)getObjectsList().get(MAPLAYER).get(c)).setDimensions(generateChunk(getCurrentChunkGenType(), c, 0));
                moveChunk(c, 0, -(chunkHeight * numChunks));
            }
        }

    }

    public void moveChunk(int chunkPos, float deltaX, float deltaY) {

        for (int i = 0; i < getObjectsList().get(MAPLAYER).size(); i++) {
        for (BasicBrickObject[] slice : ((BasicBackroundObject)getObjectsList().get(MAPLAYER).get(i)).getDimensions()) {
            for (BasicBrickObject piece : slice) {
                piece.moveXMultiply(deltaX);
                piece.moveYMultiply(deltaY);
            }
        }
        }

    }

    public BasicBrickObject[][] generateChunk(String chunkType, int chunkNum, int chunkCount) {

        BasicBrickObject[][] tempChunk = ((BasicBackroundObject)getObjectsList().get(MAPLAYER).get(chunkNum)).getDimensions();

        //#################
        //add all types of chunks here along w/ logic manager to handle if its time to switch chunk types
        switch (chunkType) {

            case "random":
                tempChunk = generateRandomPlacementChunk(tempChunk);
                break;
            case "alley":
                tempChunk = generateAlleyChunk(tempChunk);
                break;
            case "solid":
                tempChunk = generateSolidChunk(tempChunk, true);
                break;
            case "empty":
                tempChunk = generateSolidChunk(tempChunk, false);
                break;

        }

        //chunk debug
        /*
        
        for(int x = 0; x < chunkWidth; x++){
            for(int y = 0; y < chunkHeight; y++){
                if(y == 0 || y == chunkHeight){
                    tempChunk[x][y].setIsVisible(true);
                    tempChunk[x][y].setColor(Color.ORANGE);
                    tempChunk[x][y].setCollision(true);
                }
            }
        }

         */
        //final vars to set/return
        return tempChunk;

    }

    //generation for the backrounds and other environmental graphics
    public void generateScreenObjects() {

        if (Math.random() <= backroundObjectSpawnRate) {
            if (Debug.isEnabled())
                System.out.println("Spawned!");
            getObjectsList().get(BACKROUNDOBJLAYER).add(generateBakcroundObject());
            
        }

    }

    public BasicBackroundObject generateBakcroundObject() {
        float tempX = 0, tempY = 0;
        float tempDX = 0, tempDY = 0;
        String spawnLoc = "Something went wrong";
        double randConst = Math.random();

        randConst = randConst * 4;

        if (Debug.isEnabled()) {
            System.out.println("randConst : " + randConst);
        }

        //1 = N spawn quadrant, 2 = S spawn quadrant, 3 = W spawn quadrant, 4 = E spawn Quadrant
        if (randConst <= 1) {
            spawnLoc = "North";
            //north spawn quad
            tempY = -(BackroundMinDimension + backroundMaxDimensionDeviation) * 10;
            tempX = (float) Math.random() * BrickBreakerMain.SCREENWIDTH;
            randConst = Math.random();
            //wether deltaX is + or -
            if (randConst < .5) {
                tempDX = ((float) (Math.random() * backroundObjectMaxMovementSpeedDeviation) + backroundObjectMinMovementSpeed) / 2;
            } else {
                tempDX = -((float) (Math.random() * backroundObjectMaxMovementSpeedDeviation) + backroundObjectMinMovementSpeed) / 2;
            }
            //DY must be +
            tempDY = ((float) (Math.random() * backroundObjectMaxMovementSpeedDeviation) + backroundObjectMinMovementSpeed);

        } else if (randConst <= 2) {
            spawnLoc = "South";
            //south spawn quadrant
            tempY = (BrickBreakerMain.SCREENHEIGHT);
            tempX = (float) Math.random() * BrickBreakerMain.SCREENWIDTH;
            randConst = Math.random();
            //wether deltaX is + or -
            if (randConst < .5) {
                tempDX = ((float) (Math.random() * backroundObjectMaxMovementSpeedDeviation) + backroundObjectMinMovementSpeed) / 2;
            } else {
                tempDX = -((float) (Math.random() * backroundObjectMaxMovementSpeedDeviation) + backroundObjectMinMovementSpeed) / 2;
            }
            //DY must be -
            tempDY = -((float) (Math.random() * backroundObjectMaxMovementSpeedDeviation) + backroundObjectMinMovementSpeed);

        } else if (randConst <= 3) {
            spawnLoc = "West";
            //west spawn quadrant
            tempX = -(BackroundMinDimension + backroundMaxDimensionDeviation) * 10;
            tempY = (float) Math.random() * BrickBreakerMain.SCREENHEIGHT;
            randConst = Math.random();
            //wether deltaX is + or -
            if (randConst < .5) {
                tempDY = ((float) (Math.random() * backroundObjectMaxMovementSpeedDeviation) + backroundObjectMinMovementSpeed) / 2;
            } else {
                tempDY = -((float) (Math.random() * backroundObjectMaxMovementSpeedDeviation) + backroundObjectMinMovementSpeed) / 2;
            }
            //DX must be +
            tempDX = ((float) (Math.random() * backroundObjectMaxMovementSpeedDeviation) + backroundObjectMinMovementSpeed);

        } else if (randConst <= 4) {
            spawnLoc = "East";
            //east spawn quadrant
            tempX = BrickBreakerMain.SCREENWIDTH;
            tempY = (float) Math.random() * BrickBreakerMain.SCREENHEIGHT;
            randConst = Math.random();
            //wether deltaX is + or -
            if (randConst < .5) {
                tempDY = ((float) (Math.random() * backroundObjectMaxMovementSpeedDeviation) + backroundObjectMinMovementSpeed) / 2;
            } else {
                tempDY = -((float) (Math.random() * backroundObjectMaxMovementSpeedDeviation) + backroundObjectMinMovementSpeed) / 2;
            }
            //DX must be -
            tempDX = -((float) (Math.random() * backroundObjectMaxMovementSpeedDeviation) + backroundObjectMinMovementSpeed);

        }

        BasicBackroundObject newBackObj = new BasicBackroundObject(tempX, tempY, (int) (Math.random() * backroundMaxDimensionDeviation) + BackroundMinDimension, (int) (Math.random() * backroundMaxDimensionDeviation) + BackroundMinDimension);

        newBackObj.setDeltaX(tempDX);
        newBackObj.setDeltaY(tempDY);
        newBackObj.setCollision(false);
        newBackObj.setIsVisible(true);
        newBackObj.setInitSpawnLoc(spawnLoc);

        if (Debug.isEnabled()) {
            System.out.println("DX : " + tempDX + " DY : " + tempDY);
            System.out.println("Init Spawn Loc : " + spawnLoc);
            System.out.println();
        }

        return newBackObj;
    }

    //######################
    //getter/setter methods
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

}
