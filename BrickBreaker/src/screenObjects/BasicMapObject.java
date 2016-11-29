/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author JamesLaptop
 */
public class BasicMapObject extends AbstractScreenObject {
    
    private BasicBrickObject[][] mapPanel;
    
    private short chunkNum;
    private String chunkType;
    private boolean justMoved;
    
    private float cubeSpawnRate;

    public BasicMapObject(){
        
        super(0,0,0,0,(short)0,false,false);
        
        init();
        
    }
    
    public BasicMapObject(float x, float y, int width, int height){
        
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        setDeltaX(0);
        setDeltaY(0);
        
        init();
        
    }
    
    public BasicMapObject(float x, float y, int width, int height, float dX, float dY){
        
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        setDeltaX(dX);
        setDeltaY(dY);
        
        init();
        
    }
    
    public void init(){
        
        //make the map panel
        mapPanel = new BasicBrickObject[getWidth()][getHeight()];
        
        //set default color
        setColor(Color.WHITE);
        
        //add new pieces w/ current settings
        for(int x = 0; x < mapPanel.length; x++){
            for(int y = 0; y < mapPanel[x].length; y++){
                
                if(mapPanel[x][y] == null){
                    mapPanel[x][y] = new BasicBrickObject(x * 10, getY() + y * 10, 10, 10);
                    mapPanel[x][y].setIsVisible(false);
                    mapPanel[x][y].setCollision(false);
                    mapPanel[x][y].setDeltaX(getDeltaX());
                    mapPanel[x][y].setDeltaY(getDeltaY());
                    mapPanel[x][y].setColor(getColor());
                }
            }
        }
        
    }
    
    @Override
    public void move() {
        
        for(BasicBrickObject[] slice : mapPanel){
            for(BasicBrickObject piece : slice){
                piece.setDeltaY(getDeltaY());
                piece.setDeltaX(getDeltaX());
                piece.move();
            }
        }
        
        moveX(getDeltaX());
        moveY(getDeltaY());
        
        
    }

    @Override
    public void handleInput(String inputMethod, int key) {
        
    }

    @Override
    public void runLogic() {
        
    }

    @Override
    public void drawObject(Graphics2D g) {
        
        for(BasicBrickObject[] slice : mapPanel){
            for(BasicBrickObject piece : slice){
                piece.drawObject(g);
            }
        }
        Color tempG = g.getColor();
        if(Debug.isEnabled()){
            if(isCollision())
                g.setColor(Color.GREEN);
            else
                g.setColor(Color.RED);
            g.fillRect((int)getX(), (int)getY(), 35, 35);
        }
        
    }
    
    public BasicBrickObject[][] getDimensions(){
        return mapPanel;
    }
    
    public void resetPieces(){
        for(int x = 0; x < mapPanel.length; x++){
            for(int y = 0; y < mapPanel[x].length; y++){
                BasicBrickObject obj = mapPanel[x][y];
                obj.setX(getX() + x * 10);
                obj.setY(getY() + y * 10);
                obj.setDeltaX(getDeltaX());
                obj.setDeltaY(getDeltaY());
            }
        }
    }
    
    public void generateSolidChunk(boolean bool) {

        for (BasicBrickObject[] slice : mapPanel) {
            for (BasicBrickObject piece : slice) {
                piece.setIsVisible(bool);
                piece.setCollision(bool);
            }
        }

    }

    public void generateAlleyChunk(BasicMapObject inputChunk) {

        BasicBrickObject[][] tempChunk;

    }

    public void generateRandomPlacementChunk() {

        for (int x = 0; x < mapPanel.length; x++) {
            for (int y = 0; y < mapPanel[x].length; y++) {
                mapPanel[x][y].setColor(Color.WHITE);
                
                if (Math.random() < cubeSpawnRate) {
                    mapPanel[x][y].setIsVisible(true);
                    mapPanel[x][y].setCollision(true);
                } else{
                mapPanel[x][y].setIsVisible(false);
                mapPanel[x][y].setCollision(false);
                }

                //see if it should be filled space
                

                //debug stuff
                //System.out.println(tempChunk[x][y].getIsVisible() + "## (" + x + "," + y + ")");
            }
        }
        

    }
    
    public void generateNewChunk(BasicMapObject prevChunk, String chunkType){
        
        //#################
        //add all types of chunks here along w/ logic manager to handle if its time to switch chunk types
        switch (chunkType) {

            case "random":
                generateRandomPlacementChunk();
                setChunkType(chunkType);
                break;
            case "alley":
                generateAlleyChunk(prevChunk);
                setChunkType(chunkType);
                break;
            case "solid":
                generateSolidChunk(true);
                setChunkType(chunkType);
                break;
            case "empty":
                generateSolidChunk(false);
                setChunkType(chunkType);
                break;
        
        }
    }

    public float getCubeSpawnRate() {
        return cubeSpawnRate;
    }

    public void setCubeSpawnRate(float cubeSpawnRate) {
        this.cubeSpawnRate = cubeSpawnRate;
    }

    @Override
    public void moveY(float dY){
        setY(getY() + dY);
    }

    public short getChunkNum() {
        return chunkNum;
    }

    public void setChunkNum(short chunkNum) {
        this.chunkNum = chunkNum;
    }

    public String getChunkType() {
        return chunkType;
    }

    public void setChunkType(String chunkType) {
        this.chunkType = chunkType;
    }

    public boolean isJustMoved() {
        return justMoved;
    }

    public void setJustMoved(boolean justMoved) {
        this.justMoved = justMoved;
    }
    
    
    
}
