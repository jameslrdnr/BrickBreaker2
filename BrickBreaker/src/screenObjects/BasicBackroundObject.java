/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;

/**
 *
 * @author JamesLaptop
 */
public class BasicBackroundObject extends AbstractScreenObject{
    
    private BasicBrickObject[][] dimensions;
    private final float minSpawnrateBound = .55f, maxSpawnrateDifference = .1f;
    private final int minSmoothPasses = 1, maxSmoothPassDif = 1;
    private float spawnRate;
    private int smoothPasses;
    private String initSpawnLoc;
    
    public BasicBackroundObject(float tempX, float tempY, int tempWidth, int tempHeight){
        
        dimensions = new BasicBrickObject[tempWidth][tempHeight];
        
        setX(tempX);
        setY(tempY);
        
        setWidth(tempWidth * 10);
        setHeight(tempHeight * 10);
        
        init();
        
    }
    
    public void init() {
        
        //set graphical layer
        setLayer(2);
        
        //decides spawnrate of cubes in default generator based of a minimum and a max differential
        spawnRate = (float)( Math.random() * maxSpawnrateDifference) + minSpawnrateBound;
        
        smoothPasses = (int)(Math.random() * maxSmoothPassDif) + minSmoothPasses;
        
        setCollisionShape(new Rectangle((int)getX(), (int)getY(), getWidth(), getHeight()));
        
        setColor(new Color(78, 9, 125));

        for (int x = 0; x < dimensions.length; x++) {
            for (int y = 0; y < dimensions[x].length; y++) {
                //add the brick object in
                dimensions[x][y] = new BasicBrickObject(getX() + x * 10, getY() + y * 10, 10, 10);
                dimensions[x][y].setIsVisible(false);
                dimensions[x][y].setColor(getColor());
                dimensions[x][y].setDeltaX(getDeltaX());
                dimensions[x][y].setDeltaY(getDeltaY());
                //sets border of object to open space no matter what
                if (y == 0 || y == dimensions[x].length - 1) {
                    dimensions[x][y].setColor(Color.red);
                    dimensions[x][y].setIsVisible(false);
                } else if (x == 0 || x == dimensions.length - 1) {
                    dimensions[x][y].setColor(Color.red);
                    dimensions[x][y].setIsVisible(false);
                } else {
                    if(Math.random() <= spawnRate){
                        dimensions[x][y].setIsVisible(true);
                    }
                }
            }
        }
        
        dimensions = objectSmoothing(dimensions, smoothPasses);
        
    }
    
    public BasicBrickObject[][] objectSmoothing(BasicBrickObject[][] feederObj, int passes) {
        
        while (passes > 0) {
            BasicBrickObject[][] tempObj = feederObj;
            //set vars so that it will not go past bounds, extra empty space on sides is included in smoothing to smooth outer edges
            for (int x = 1; x < feederObj.length - 1; x++) {
                for (int y = 1; y < feederObj[x].length - 1; y++) {

                    int solidCount = 0, hiddenCount = 0;

                    for (int lx = x - 1; lx <= x + 1; lx++) {
                        for (int ly = y - 1; ly <= y + 1; ly++) {
                            if (feederObj[lx][ly].getIsVisible()) {
                                solidCount++;
                            } else {
                                hiddenCount++;
                            }

                        }
                    }

                    if (solidCount > hiddenCount) {
                        tempObj[x][y].setIsVisible(true);
                    } else {
                        tempObj[x][y].setIsVisible(false);
                    }

                }
            }
            
            passes--;
            
            feederObj = tempObj;

        }

        return feederObj;

    }

    @Override
    public void move() {
        
        moveX(getDeltaX());
        moveY(getDeltaY());
        ((Rectangle)getCollisionShape()).setLocation((int)getX(), (int)getY());
        
        for(BasicBrickObject[] slice : dimensions){
            for(BasicBrickObject piece : slice){
                piece.setDeltaX(getDeltaX());
                piece.setDeltaY(getDeltaY());
                piece.move();
            }
        }
        
    }

    @Override
    public void handleInput(String inputMethod, ArrayList<Integer> inputList, String inputMethodRemove, ArrayList<Integer> inputListReleased){
            
    }

    @Override
    public void runLogic() {
        
    }

    @Override
    public void drawObject(Graphics2D g) {
        
        Color gameC = g.getColor();
        
        g.setColor(getColor());
        for(BasicBrickObject[] slice : dimensions){
            for(BasicBrickObject piece : slice){
                if(piece.getIsVisible())
                    piece.drawObject(g);
            }
        }
        
        if(Debug.isEnabled()){
            if(isCollision())
                g.setColor(Color.GREEN);
            else
                g.setColor(Color.RED);
            g.draw(getCollisionShape());
        }
        
    }

    @Override
    public boolean shouldDestroyObject() {
        
        if(Debug.isEnabled() && checkIsOffScreen(250)){
            System.out.println("Obj removed : " + getX() + " : " + getY());
        }
        
        //returns true if COMPLETELY out of bounds by 250 units
        return checkIsOffScreen(250);
        
    }

    public String getInitSpawnLoc() {
        return initSpawnLoc;
    }

    public void setInitSpawnLoc(String initSpawnLoc) {
        this.initSpawnLoc = initSpawnLoc;
    }

    public BasicBrickObject[][] getDimensions() {
        return dimensions;
    }

    public void setDimensions(BasicBrickObject[][] dimensions) {
        this.dimensions = dimensions;
    }
    
    
    
}
    