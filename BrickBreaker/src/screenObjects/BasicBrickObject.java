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
 * @author ab29627
 */
public class BasicBrickObject extends AbstractScreenObject{
    
    public BasicBrickObject(float x, float y, int width, int height){
        
        super(x, y, width, height, true, true);
        
        init();
        
    }
    
    public void init(){
        
        //set movement mulitpliers to equal width and height
        setxMovementMultiplier(10);
        setyMovementMultiplier(10);
        
    }

    @Override
    public void move() {
        
    }

    @Override
    public void handleInput(String inputMethod, int key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void runLogic() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void drawObject(Graphics2D g) {
        
        Color tempC = g.getColor();
        
        g.setColor(Color.BLACK);
        g.fillRoundRect((int) getX(), (int) getY(), getWidth(), getHeight(), 10, 10);
        g.setColor(getColor());
        g.fillRect((int) getX(), (int) getY(), getWidth()-2, getHeight()-2);
        
        g.setColor(tempC);
    }
    
}
