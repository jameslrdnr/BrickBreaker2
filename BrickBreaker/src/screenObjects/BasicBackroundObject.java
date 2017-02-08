/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
    private Color[] colors;
    private String initSpawnLoc;
    
    public BasicBackroundObject(float tempX, float tempY, int tempWidth, int tempHeight){
        super(tempX, tempY, tempWidth, tempHeight, BASICBACKGROUNDOBJECTID, false, false);
        
        dimensions = new BasicBrickObject[tempWidth][tempHeight];
        
        
        init();
        
    }
    
    public void init() {
        
        //set graphical layer
        setLayer(2);
        
        //sets color options
        colors = new Color[5];
        colors[0] = new Color(81, 82, 91);
        colors[1] = new Color(64, 64, 73);
        colors[2] = new Color(85, 85, 96);
        colors[3] = new Color(99, 99, 107);
        colors[4] = new Color(79, 80, 84);
        
        //decides spawnrate of cubes in default generator based of a minimum and a max differential
        spawnRate = (float)( Math.random() * maxSpawnrateDifference) + minSpawnrateBound;
        
        smoothPasses = (int)(Math.random() * maxSmoothPassDif) + minSmoothPasses;
        
        setCollisionShape(new Rectangle((int)getX(), (int)getY(), getWidth() * 10, getHeight() * 10));
        
        setColor(new Color(Color.PINK.getRGB()));

        for (int x = 0; x < dimensions.length; x++) {
            for (int y = 0; y < dimensions[x].length; y++) {
                //add the brick object in
                dimensions[x][y] = new BasicBrickObject(getX() + x * 10, getY() + y * 10, 10, 10);
                dimensions[x][y].setIsVisible(false);
                dimensions[x][y].setColor(colors[(int)(Math.random() * 5)]);
                dimensions[x][y].setDegrees(getDegrees());
                dimensions[x][y].setSpeed(getSpeed());
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
        
        moveX(getDeltaX() * getSpeed());
        moveY(getDeltaY() * getSpeed());
        ((Rectangle)getCollisionShape()).setLocation((int)getX(), (int)getY());
        
        for(BasicBrickObject[] slice : dimensions){
            for(BasicBrickObject piece : slice){
                piece.setDegrees(getDegrees());
                piece.setSpeed(getSpeed());
                piece.movementHandler();
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
            g.fillRect((int)getX(), (int)getY(), 5, 5);
        }
        
    }

    @Override
    public boolean shouldDestroyObject() {
        
        if(Debug.isEnabled() && checkIsOffScreen(250)){
            System.out.println("Obj removed : " + getX() + " : " + getY());
            return true;
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
    