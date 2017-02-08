/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author JamesLaptop
 */
public class BasicPickupScreenObject extends AbstractScreenObject{
    
    public BasicPickupScreenObject(float x, float y, int width, int height, short id){
        super(x, y, width, height, id, true, false);
    }

    @Override
    public void move() {
        moveX(getDeltaX() * getSpeed());
        moveY(getDeltaY() * getSpeed());
        if(getParticleSys() != null){
            getParticleSys().setDegrees(getDegrees());
            getParticleSys().setSpeed(getSpeed());
            getParticleSys().movementHandler();
        }
        
        if(getCollisionShape() != null)
            ((Rectangle)getCollisionShape()).setLocation((int)getX(), (int)getY());
        
    }

    @Override
    public void handleInput(String inputMethod, ArrayList<Integer> inputList, String inputMethodRemove, ArrayList<Integer> inputListReleased) {
        
    }

    @Override
    public void runLogic() {
        
    }

    @Override
    public void drawObject(Graphics2D g) {
        
        g.drawOval((int)getX(), (int)getY(), 5, 5);
        
    }
    
}
