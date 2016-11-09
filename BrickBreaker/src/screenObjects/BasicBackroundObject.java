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

/**
 *
 * @author JamesLaptop
 */
public class BasicBackroundObject extends AbstractScreenObject{
    
    private BasicBrickObject[][] dimensions;
    private final float minSpawnrateBound = .5f, maxSpawnrateDifference = .1f;
    private final int minSmoothPasses = 1, maxSmoothPassDif = 1;
    private float spawnRate;
    private int smoothPasses;
    
    public BasicBackroundObject(float tempX, float tempY, int tempWidth, int tempHeight){
        
        dimensions = new BasicBrickObject[tempWidth][tempHeight];
        
        setX(tempX);
        setY(tempY);
        
        setWidth(tempWidth * 10);
        setHeight(tempHeight * 10);
        
        init();
        
    }
    
    public void init() {
        
        //decides spawnrate of cubes in default generator based of a minimum and a max differential
        spawnRate = (float)( Math.random() * maxSpawnrateDifference) + minSpawnrateBound;
        System.out.println(spawnRate);
        
        smoothPasses = (int)(Math.random() * maxSmoothPassDif) + minSmoothPasses;
        
        setCollisionShape(new Rectangle((int)getX(), (int)getY(), getWidth(), getHeight()));

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
    public void handleInput(String inputMethod, int key){
            
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
        
        if(brickbreaker.BrickBreakerMain.getDebug().isEnabled()){
            if(isCollision())
                g.setColor(Color.GREEN);
            else
                g.setColor(Color.RED);
            g.draw(getCollisionShape());
        }
        
    }
    
}
    