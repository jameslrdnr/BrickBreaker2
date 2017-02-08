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
public class TestCube extends BasicBrickObject{
    
    int deg;
    
    public TestCube(float x, float y, int width, int height) {
        super(x, y, width, height);
    }
    
    @Override
    public void runLogic(){
        deg++;
        moveX(.5f);
    }
    
    @Override
    public void drawObject(Graphics2D g){
        g.setColor(Color.PINK);
        g.rotate(Math.toRadians(deg), getX() + getWidth()/2, getY() + getHeight()/2);
        g.fillRect((int)getX(), (int)getY(), getWidth(), getHeight());
        g.rotate(-Math.toRadians(deg), getX() + getWidth()/2, getY() + getHeight()/2);
    }
    
}
