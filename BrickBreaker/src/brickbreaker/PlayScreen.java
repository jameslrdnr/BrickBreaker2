/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
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

    //fonts used for drawing anything
    private final Font scoreFont = new Font(Font.MONOSPACED, Font.BOLD, 48);
    private final Font titleFont = new Font(Font.MONOSPACED, Font.BOLD, 32);

    //score stuff
    //########################
    
    //########################
    //map generation variables
    private final int chunkWidth = 80, chunkHeight = 40, numChunks = 5, screenWidth, screenHeight, LEFT = 180, RIGHT = 0, UP = 90, DOWN = 270;
    private float cubeSpawnRate;
    private float screenScrollSpeed;
    private String currentChunkGenType;
    private int chunkGenCount;

    private int BackroundMinDimension = 10;
    private int backroundMaxDimensionDeviation = 6;
    
    public static final int minBasicEnemySide = 10;
    public static final int basicEnemyMaxSideDeviation = 20;

    private float backroundObjectMaxMovementSpeedDeviation = 2f;
    private float backroundObjectMinMovementSpeed = 1f;
    private float backroundObjectSpawnRate = .0015f;
    
    private int backroundStarNum = 300;

    //########################
    private Properties IDlist;
    private String IDMapLoc = "src/assets/ObjectIDMap.properties";
    
    private double basicEnemySpawnRate, healthPickupSpawnRate, pointPickupSpawnRate, threeShotPickupSpawnRate;
    
    //other variables
    private Color[] explosionColors;
    

    AbstractScreenObject player;

    public PlayScreen(int w, int h) {

        super();

        screenWidth = w;
        screenHeight = h;
        
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
        player.setSpeed(0);
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
        setCurrentChunkGenType("random");

        cubeSpawnRate = .01f;
        basicEnemySpawnRate = 100.0;
        healthPickupSpawnRate = .001;
        pointPickupSpawnRate = .001;
        threeShotPickupSpawnRate = .001;
        
        //spawn backround stars

        for(int i = 0; i < backroundStarNum; i++){
            int blinker = (int)(Math.random() * 120) + 240;
            BasicBrickObject tempOb = new BasicBrickObject((int)(Math.random() * screenWidth), (int)(Math.random() * screenHeight), (int)(Math.random() * 3) + 1, (int)(Math.random() * 3) + 1);
            tempOb.setBlinkTimer(blinker);
            tempOb.setBlinkTime((int)(Math.random() * blinker));
            tempOb.setBlinking(true);
            if(Math.random() > .5)
                tempOb.setIsVisible(true);
            tempOb.setColor(Color.WHITE);
            getObjectsList().get(BACKROUNDLAYER).add(tempOb);
        }
        
        //spawn map chunks
        for (int t = 0; t < numChunks; t++) {

            getObjectsList().get(MAPLAYER).add(new BasicMapObject(0, -(chunkHeight * t * 10) - chunkHeight * 10, chunkWidth, chunkHeight));
            ((BasicMapObject) getObjectsList().get(MAPLAYER).get(t)).setChunkNum((short) t);

        }

        System.out.println("######@@@@@");
        System.out.println(getObjectsList().get(MAPLAYER).get(0).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(1).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(2).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(3).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(4).getY());
        System.out.println("######@@@@@");

        /*
        ((BasicBackroundObject)getObjectsList().get(MAPLAYER).get(0)).setDimensions(generateChunk(getCurrentChunkGenType(), 0, getChunkGenCount()));
        ((BasicBackroundObject)getObjectsList().get(MAPLAYER).get(1)).setDimensions(generateChunk(getCurrentChunkGenType(), 0, getChunkGenCount()));
        ((BasicBackroundObject)getObjectsList().get(MAPLAYER).get(2)).setDimensions(generateChunk(getCurrentChunkGenType(), 0, getChunkGenCount()));
        ((BasicBackroundObject)getObjectsList().get(MAPLAYER).get(3)).setDimensions(generateChunk(getCurrentChunkGenType(), 0, getChunkGenCount()));
        ((BasicBackroundObject)getObjectsList().get(MAPLAYER).get(4)).setDimensions(generateChunk(getCurrentChunkGenType(), 0, getChunkGenCount()));
         */
        //chunk1 = chunkInitRandomizer(chunk1, 0);
        //chunk2 = chunkInitRandomizer(chunk2, 4);
        System.out.println("######^^");
        System.out.println(getObjectsList().get(MAPLAYER).get(0).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(1).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(2).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(3).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(4).getY());
        System.out.println("######^^");

        screenScrollSpeed = 2f;

        //move first chunk halfway off screen
        for (int i = 0; i < getObjectsList().get(MAPLAYER).size(); i++) {

            getObjectsList().get(MAPLAYER).get(i).moveYMultiply(-chunkHeight * 2);
            getObjectsList().get(MAPLAYER).get(i).setDegrees(DOWN);
            getObjectsList().get(MAPLAYER).get(i).setSpeed(screenScrollSpeed);
            ((BasicMapObject) getObjectsList().get(MAPLAYER).get(i)).setCubeSpawnRate(cubeSpawnRate);
            ((BasicMapObject) getObjectsList().get(MAPLAYER).get(i)).generateRandomPlacementChunk();

        }

        System.out.println("######");
        System.out.println(getObjectsList().get(MAPLAYER).get(0).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(1).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(2).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(3).getY());
        System.out.println(getObjectsList().get(MAPLAYER).get(4).getY());
        System.out.println("######");

        getDebug().setIsVisible(false);
        getDebug().setEnabled(false);

        explosionColors = new Color[3];
        explosionColors[0] = Color.RED;
        //yellow
        explosionColors[1] = new Color(237, 229, 80);
        //orange
        explosionColors[2] = new Color(234, 164, 42);
        
        //debug particle system
        
//        BasicParticleSystem ps = new BasicParticleSystem(300, 400, 3, 3, 4, 4, 100, 0);
//        ps.setParticleSpeedVariance(4);
//        ps.setPermanent(true);
//        
//        getObjectsList().get(SCREENOBJLAYER).add(ps);
        
        BasicMissleScreenObject ms = new BasicMissleScreenObject(200, 100, 20, 5, player);
        getObjectsList().get(SCREENOBJLAYER).add(ms);
        
        //TestCube ts = new TestCube(300, 400, 100, 10);
        //getObjectsList().get(SCREENOBJLAYER).add(ts);
        
    }

    @Override
    void runLogic() {
        
        //pass mouse input to debug
        if (getDebug().isEnabled() && isPaused()) {
            getDebug().setMouseX(getMouseX());
            getDebug().setMouseY(getMouseY());
            getDebug().runLogic();
        }

        //input
        delayInputManager();
        handleInput(getInputList());

        //all logic dependant on pausing
        if (!isPaused()) {
            
            if(getDebug().isEnabled()){
                getDebug().setMouseX(getMouseX());
                getDebug().setMouseY(getMouseY());
            }

            removeObjectsManager();

            //handle score
            handleScore();

            //run move() on all objects in objects array
            moveScreenObjects();

            //run logic() on all objects in objects array
            runScreenObjectLogic();

            //generate all screenobjects that are proceduraly generated
            generateScreenObjects();

            //handle map chunks
            chunkHandler();

            //handle collision of player object
            for (int i = 0; i < getObjectsList().get(MAPLAYER).size(); i++) {
                for (BasicBrickObject[] slice : ((BasicMapObject) getObjectsList().get(MAPLAYER).get(i)).getDimensions()) {
                    for (BasicBrickObject piece : slice) {
                        if (piece.isCollision()) {
                            if (player.testBoundingIntersection(piece.getCollisionShape())) {
                                if (player.testIntersection(piece.getCollisionShape())) {

                                    //insert code for collision here
                                    if (!Debug.isEnabled()) {
                                        ((PlayerScreenObject) player).changeHealth(-10f);
                                    }

                                    piece.setCollision(false);
                                    piece.setIsVisible(false);

                                    //makes a particle system after a collision
                                    BasicParticleSystem ps = new BasicParticleSystem(piece.getX() + piece.getWidth() / 2, piece.getY() + piece.getHeight() / 2, 3, 3, 6f, 0f, 0, 0);
                                    ps.setDegrees(DOWN);
                                    ps.setSpeed(screenScrollSpeed);
                                    ps.setDeltaYModifier(screenScrollSpeed);
                                    ps.setInheritInertia(true);
                                    ps.createParticles(60, 8, 3, 3, .5f, 0, 1f, true, explosionColors);
                                    getObjectsList().get(SCREENOBJLAYER).add(ps);

                                }
                            }
                        }
                    }
                }
            }
            
            //Enemy bullets hitting player
            for (int i = 0; i < getObjectsList().get(SCREENOBJLAYER).size(); i++) {
                
                if(getObjectsList().get(SCREENOBJLAYER).get(i) instanceof BasicEnemyBulletScreenObject){
                    BasicEnemyBulletScreenObject bullet = (BasicEnemyBulletScreenObject) getObjectsList().get(SCREENOBJLAYER).get(i);
                    if(bullet.testBoundingIntersection(player.getCollisionShape())){
                        if(bullet.testIntersection(player.getCollisionShape())){
                            ((PlayerScreenObject) player).changeHealth(-bullet.getDamage());
                            bullet.setHasHit(true);
                            BasicParticleSystem ps = new BasicParticleSystem(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 3, 3, 6f, 0f, 0, 0);
                            ps.setDegrees(DOWN);
                            ps.setSpeed(screenScrollSpeed);
                            ps.setDeltaYModifier(screenScrollSpeed);
                            ps.setInheritInertia(true);
                            ps.createParticles(60, 8, 3, 3, .5f, 0, 1f, true, explosionColors);
                            getObjectsList().get(SCREENOBJLAYER).add(ps);
                        }
                    }
                }
            }
            
            //handle collision between player bullets and enemies
            for (int i = 0; i < getObjectsList().get(SCREENOBJLAYER).size(); i++) {
                if(getObjectsList().get(SCREENOBJLAYER).get(i).getIdNum() == 0){
                    System.out.println(getObjectsList().get(SCREENOBJLAYER).get(i).getClass());
                }
                if(getObjectsList().get(SCREENOBJLAYER).get(i) instanceof AbstractEnemyScreenObject){
                    AbstractEnemyScreenObject enemy = (AbstractEnemyScreenObject) getObjectsList().get(SCREENOBJLAYER).get(i);
                    for (int j = 0; j < getObjectsList().get(SCREENOBJLAYER).size(); j++) {
                        if(getObjectsList().get(SCREENOBJLAYER).get(j) instanceof BasicPlayerBulletScreenObject){
                            BasicPlayerBulletScreenObject bullet = (BasicPlayerBulletScreenObject) getObjectsList().get(SCREENOBJLAYER).get(j);
                            if (enemy.testBoundingIntersection(bullet.getCollisionShape())) {
                                if (enemy.testIntersection(bullet.getCollisionShape())) {
                                    enemy.changeHealth(-bullet.getDamage());
                                    bullet.setHasHit(true);
                                    //makes a particle system after a collision
                                    BasicParticleSystem ps = new BasicParticleSystem(enemy.getX() + enemy.getWidth() / 2, enemy.getY() + enemy.getHeight() / 2, 3, 3, 6f, 0f, 0, 0);
                                    ps.setInheritInertia(false);
                                    ps.createParticles(60, 8, 3, 3, .5f, 0, 1f, true, explosionColors);
                                    
                                    getObjectsList().get(SCREENOBJLAYER).add(ps);
                                }
                            }
                        }
                    } 
                }    
            }        
                    
//                }
            

            //player shooting
            if (((PlayerScreenObject) player).isShooting()) {
                PlayerScreenObject playerObj = (PlayerScreenObject) player;
                AbstractScreenObject bullet = new BasicPlayerBulletScreenObject((float) playerObj.getMidIntersectPoint().getX(), (float) playerObj.getMidIntersectPoint().getY(), 11, 11, true, true, playerObj.getDegrees(), playerObj.getPlayerColor());
                bullet.setIsVisible(true);
                getObjectsList().get(SCREENOBJLAYER).add(bullet);
                if(((PlayerScreenObject) player).getThreeShot()&&((PlayerScreenObject) player).getPowerUpAmmo()>0){
                    AbstractScreenObject bullet1 = new BasicPlayerBulletScreenObject((float) playerObj.getMidIntersectPoint().getX(), (float) playerObj.getMidIntersectPoint().getY(), 11, 11, true, true, playerObj.getDegrees()+20, playerObj.getPlayerColor());
                    AbstractScreenObject bullet2 = new BasicPlayerBulletScreenObject((float) playerObj.getMidIntersectPoint().getX(), (float) playerObj.getMidIntersectPoint().getY(), 11, 11, true, true, playerObj.getDegrees()-20, playerObj.getPlayerColor());
                    //AbstractScreenObject bullet3 = new BasicPlayerBulletScreenObject();
                    //bullet1.setDegrees(bullet.getDegrees()-100);
                    bullet1.setIsVisible(true);
                    getObjectsList().get(SCREENOBJLAYER).add(bullet1);
                    //bullet2.setDegrees(bullet.getDegrees()-80);
                    bullet2.setIsVisible(true);
                    getObjectsList().get(SCREENOBJLAYER).add(bullet2);
                    ((PlayerScreenObject) player).setPowerUpAmmo(((PlayerScreenObject) player).getPowerUpAmmo()-1);
                    if(((PlayerScreenObject) player).getPowerUpAmmo()==0){
                        ((PlayerScreenObject) player).setThreeShot(false);
                    }
                    
                }
                ((PlayerScreenObject) player).setShooting(false);
            }
            
            //enemy shooting
            for(int i = 0; i < getObjectsList().get(SCREENOBJLAYER).size(); i++){
                if(getObjectsList().get(SCREENOBJLAYER).get(i) instanceof BasicEnemyScreenObject){
                    BasicEnemyScreenObject enemy  = (BasicEnemyScreenObject)getObjectsList().get(SCREENOBJLAYER).get(i);
                    if (enemy.isShooting()) {
                        AbstractScreenObject bullet = new BasicEnemyBulletScreenObject((float) enemy.getMyShape().getBounds().getCenterX(), (float) enemy.getMyShape().getBounds().getCenterY(), 11, 11, true, false, enemy.getSpeed(), (PlayerScreenObject)player);
                        bullet.setIsVisible(true);
                        getObjectsList().get(SCREENOBJLAYER).add(bullet);
                        enemy.setShooting(false);
                    }
                }
            }
            
            //SCREEN OBJ COLLISIONS, CHECK IN SWITCH W/ ID'S
            
            for (int i = 0; i < getObjectsList().get(SCREENOBJLAYER).size(); i++) {
                AbstractScreenObject ob = getObjectsList().get(SCREENOBJLAYER).get(i);
                if (ob.isCollision()) {
                    //checks for collision w/ the player
                    if (player.testIntersection(ob.getCollisionShape())) {
                        BasicParticleSystem ps;
                        switch (ob.getIdNum()) {
                            case AbstractEnemyScreenObject.BASICHEALTHPICKUPSCREENOBJECTID:
                                ((PlayerScreenObject) player).changeHealth(25);
                                getObjectsList().get(SCREENOBJLAYER).remove(i);
                                i--;
                                //makes a particle system after a collision
                                ps = new BasicParticleSystem(ob.getX() + ob.getWidth() / 2, ob.getY() + ob.getHeight() / 2, 3, 3, 6f, 0f, 0, 0);
                                ps.setDegrees(DOWN);
                                ps.setSpeed(screenScrollSpeed);
                                ps.setDeltaYModifier(screenScrollSpeed);
                                ps.setInheritInertia(true);
                                ps.createParticles(20, 8, 3, 3, .6f, 0, .75f, true, Color.GREEN);
                                getObjectsList().get(SCREENOBJLAYER).add(ps);
                                break;
                            case AbstractScreenObject.BASICPOINTPICKUPSCREENOBJECTID:
                                setScore(getScore() + 5);
                                getObjectsList().get(SCREENOBJLAYER).remove(i);
                                i--;
                                //makes a particle system after a collision
                                ps = new BasicParticleSystem(ob.getX() + ob.getWidth() / 2, ob.getY() + ob.getHeight() / 2, 3, 3, 6f, 0f, 0, 0);
                                ps.setDegrees(DOWN);
                                ps.setSpeed(screenScrollSpeed);
                                ps.setDeltaYModifier(screenScrollSpeed);
                                ps.setInheritInertia(true);
                                ps.createParticles(20, 8, 3, 3, .6f, 0, .75f, true, Color.YELLOW);
                                getObjectsList().get(SCREENOBJLAYER).add(ps);
                                break;
                            case AbstractScreenObject.BASICTHREESHOTPICKUPSCREENOBJECTID:
                                ((PlayerScreenObject) player).setPowerUpAmmo(25);
                                ((PlayerScreenObject) player).setThreeShot(true);
                                getObjectsList().get(SCREENOBJLAYER).remove(i);
                                i--;
                                //makes a particle system after a collision
                                ps = new BasicParticleSystem(ob.getX() + ob.getWidth() / 2, ob.getY() + ob.getHeight() / 2, 3, 3, 6f, 0f, 0, 0);
                                ps.setDegrees(DOWN);
                                ps.setSpeed(screenScrollSpeed);
                                ps.setDeltaYModifier(screenScrollSpeed);
                                ps.setInheritInertia(true);
                                ps.createParticles(20, 8, 3, 3, .6f, 0, .75f, true, Color.CYAN);
                                getObjectsList().get(SCREENOBJLAYER).add(ps);
                                break;
                                
                        }
                    }
                    
                    
                    //checking other screen obj collisions
                    if (ob.getIdNum() == AbstractScreenObject.BASICMISSILESCREENOBJECTID) {
                        if (ob.testIntersection(((BasicMissleScreenObject) ob).getTarget().getCollisionShape())) {
                            ((PlayerScreenObject) player).changeHealth(-25);
                            ((BasicMissleScreenObject) ob).setLifeTime(0);
                            BasicParticleSystem ps = new BasicParticleSystem(ob.getX() + ob.getWidth() / 2, ob.getY() + ob.getHeight() / 2, 3, 3, 6f, 0f, 0, 0);
                            ps.setDegrees(DOWN);
                            ps.setSpeed(screenScrollSpeed);
                            ps.setDeltaYModifier(ob.getDeltaY() * ob.getSpeed()/2f);
                            ps.setDeltaXModifier(ob.getDeltaX() * ob.getSpeed()/2f);
                            ps.setInheritInertia(true);
                            ps.createParticles(75, 20, 4, 5, 1f, 0, 1.5f, true, explosionColors);
                            getObjectsList().get(SCREENOBJLAYER).add(ps);
                        }
                    }
                }
            }
            
            //END GAME CONDITION 
            if (((PlayerScreenObject) player).getHealth() <= 0) {
                setNextScreen('S');
            }
        }
        //pause screen logic
        else{
            
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
        
        //draws the player's health
        drawHealth(g);
        
        //pause screen graphics
        if(isPaused()){
            
            g.setColor(Color.WHITE);
         
            drawCenteredString(g, "Paused", getWidth()/2, getHeight()/2, titleFont);

        }
    }

    @Override
    public void specificInput(ArrayList<Integer> inputList, ArrayList<Integer> inputListReleased) {

        //pass input to screen objects and debug
        getDebug().inputHandler(getInputMethod(), inputList, getInputMethodRemove(), inputListReleased);

        for (ArrayList<AbstractScreenObject> list : getObjectsList()) {
            for (AbstractScreenObject ob : list) {
                ob.inputHandler(getInputMethod(), inputList, getInputMethodRemove(), inputListReleased);
            }
        }
        //cycle through input for the screen below here----------
        //pressed key
        for (int key : inputList) {
            if (key == KeyEvent.VK_ESCAPE) {
                delayInput(30);
                setPaused(!isPaused());
            }
        }
        //released key
        for (int key : inputListReleased) {
            
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
        ((PlayerScreenObject)player).setMouseX(me.getX());
        ((PlayerScreenObject)player).setMouseY(me.getY());
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
    
    private void drawHealth(Graphics2D g) {
         //inits
        Font initFont = g.getFont();
        Color initColor = g.getColor();
        
        //change values to use
        g.setFont(scoreFont);
        g.setColor(Color.red);
        
        
        //draw to rectangles with green on top of red to show health
        g.fillRect(getWidth() / 32, getHeight() * 11 / 12, getWidth() / 8, getHeight() / 60);
        g.setColor(Color.GREEN);
        g.fillRect(getWidth() / 32, getHeight() * 11 / 12, (int)(getWidth() / 8.0 * ((PlayerScreenObject)player).getHealth() / ((PlayerScreenObject)player).getMaxHealth()), getHeight() / 60);
        
        
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
    public void moveScreen() {

        /*
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

         */
    }

    public void moveChunk(int chunkPos, float deltaX, float deltaY) {

        //moves the whole chunk
        
        for (int i = 0; i < getObjectsList().get(MAPLAYER).size(); i++) {
            for (BasicBrickObject[] slice : ((BasicBackroundObject) getObjectsList().get(MAPLAYER).get(i)).getDimensions()) {
                for (BasicBrickObject piece : slice) {
                    piece.moveXMultiply(deltaX);
                    piece.moveYMultiply(deltaY);
                }
            }
        }

    }

    public void chunkHandler() {
        //chunk count is the number of chunks of a certain type to generate

        for (int chunkNum = 0; chunkNum < numChunks; chunkNum++) {

            BasicMapObject obj = ((BasicMapObject) getObjectsList().get(MAPLAYER).get(chunkNum));

            short behindPos;
            if (obj.getChunkNum() == 0) {
                behindPos = 4;
            } else {
                behindPos = (short) (obj.getChunkNum() - 1);
            }

            //checks to see if off the edge of the screen by at least --- pixels here------------
            if (getObjectsList().get(MAPLAYER).get(chunkNum).getY() >= brickbreaker.BrickBreakerMain.SCREENHEIGHT) {
                //
                getObjectsList().get(MAPLAYER).get(chunkNum).moveY(-(chunkHeight * 4 * 10));
                ((BasicMapObject) getObjectsList().get(MAPLAYER).get(chunkNum)).resetPieces();
                ((BasicMapObject) getObjectsList().get(MAPLAYER).get(chunkNum)).setJustMoved(true);

            }


            if (obj.isJustMoved()) {
                obj.setJustMoved(false);
                obj.generateNewChunk((BasicMapObject) getObjectsList().get(MAPLAYER).get(behindPos), currentChunkGenType);
                chunkGenCount--;
            }

            if (chunkGenCount <= 0) {

                //rand int 0-3
                int randHolder = (int) (Math.random() * 0);

                if (randHolder == 0) {
                    setCurrentChunkGenType("random");
                } else if (randHolder == 1) {
                    setCurrentChunkGenType("empty");
                } else if (randHolder == 2) {
                    setCurrentChunkGenType("solid");
                } else if (randHolder == 3) {
                    setCurrentChunkGenType("alley");
                }

                chunkGenCount = (int) (Math.random() * 3) + 4;

            }
        }

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
    //generation for the backrounds and other environmental graphics
    public void generateScreenObjects() {

        if (Math.random() <= backroundObjectSpawnRate) {
            if (Debug.isEnabled()) {
                System.out.println("Spawned!");
            }
            getObjectsList().get(BACKROUNDOBJLAYER).add(generateBakcroundObject());

        }
        
        if ((int)(Math.random() * basicEnemySpawnRate) == 0) {
            if (Debug.isEnabled()) {
                System.out.println("Spawned Enemy!");
            }
            
            int ranx = (int)(Math.random() * getScreenWidth());
            int y = -50;
            
            int ranWidth = (int)((Math.random() * basicEnemyMaxSideDeviation) + minBasicEnemySide);
            int ranHeight = ranWidth;
            
            AbstractScreenObject enemy = new BasicEnemyScreenObject(ranx, y, ranWidth, ranHeight, screenScrollSpeed, true, false);
            getObjectsList().get(SCREENOBJLAYER).add(enemy);

        }
        
        if(Math.random() <= healthPickupSpawnRate){
            if (Debug.isEnabled()) {
                System.out.println("Spawned HP Pickup!");
            }
            
            int ranx = (int)(Math.random() * getScreenWidth());
            int y = -50;
            
            BasicHealthPickupScreenObject pickup = new BasicHealthPickupScreenObject(ranx, y, 26, 26);
            pickup.setDegrees(DOWN);
            pickup.setSpeed(screenScrollSpeed);
            getObjectsList().get(SCREENOBJLAYER).add(pickup);
            
        }
        
        if(Math.random() <= pointPickupSpawnRate){
            if (Debug.isEnabled()) {
                System.out.println("Spawned Point Pickup!");
            }
            
            int ranx = (int)(Math.random() * getScreenWidth());
            int y = -50;
            
            BasicPointPickupScreenObject pickup = new BasicPointPickupScreenObject(ranx, y, 26, 26);
            pickup.setDegrees(DOWN);
            pickup.setSpeed(screenScrollSpeed);
            getObjectsList().get(SCREENOBJLAYER).add(pickup);
            
        }
        if(Math.random() <= threeShotPickupSpawnRate){
            if (Debug.isEnabled()) {
                System.out.println("Spawned ThreeShot Pickup!");
            }

            int ranx = (int)(Math.random() * getScreenWidth());
            int y = -50;
            
            BasicThreeShotPickupScreenObject pickup = new BasicThreeShotPickupScreenObject(ranx, y, 26, 26);
            pickup.setDegrees(DOWN);
            pickup.setSpeed(screenScrollSpeed);
            getObjectsList().get(SCREENOBJLAYER).add(pickup);
            
        }

    }

    public BasicBackroundObject generateBakcroundObject() {
        float tempX = 0, tempY = 0;
        float tempD = 0;
        float tempSpeed = backroundObjectMinMovementSpeed;
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
            
            tempD = DOWN - 45;
            tempD += (Math.random() * 90);

        } else if (randConst <= 2) {
            spawnLoc = "South";
            //south spawn quadrant
            tempY = (BrickBreakerMain.SCREENHEIGHT);
            tempX = (float) Math.random() * BrickBreakerMain.SCREENWIDTH;
            
            tempD = UP - 45;
            tempD += (Math.random() * 90);

        } else if (randConst <= 3) {
            spawnLoc = "West";
            //west spawn quadrant
            tempX = -(BackroundMinDimension + backroundMaxDimensionDeviation) * 10;
            tempY = (float) Math.random() * BrickBreakerMain.SCREENHEIGHT;
            
            tempD = RIGHT - 45;
            tempD += (Math.random() * 90);

        } else if (randConst <= 4) {
            spawnLoc = "East";
            //east spawn quadrant
            tempX = BrickBreakerMain.SCREENWIDTH;
            tempY = (float) Math.random() * BrickBreakerMain.SCREENHEIGHT;
            
            tempD = LEFT - 45;
            tempD += (Math.random() * 90);

        }
        
        tempSpeed += (Math.random() * backroundObjectMaxMovementSpeedDeviation);

        BasicBackroundObject newBackObj = new BasicBackroundObject(tempX, tempY, (int) (Math.random() * backroundMaxDimensionDeviation) + BackroundMinDimension, (int) (Math.random() * backroundMaxDimensionDeviation) + BackroundMinDimension);

        newBackObj.setDegrees(tempD);
        newBackObj.setSpeed(tempSpeed);
        newBackObj.setCollision(false);
        newBackObj.setIsVisible(true);
        newBackObj.setInitSpawnLoc(spawnLoc);

        if (Debug.isEnabled()) {
            System.out.println("degrees : " + tempD + " -- Speed : " + tempSpeed);
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
