package screenObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author JamesLaptop
 */
public class BorderLayout extends AbstractScreenObject{
    
    private int borderLineWidth, borderLineHeight;
    private double arcAngle, arcDisplacement;
    private Arc2D cornerTR, cornerTL, cornerBR, cornerBL;
    
    public BorderLayout(){
        
        init();
        
    }
    
    public BorderLayout(float x, float y, int width, int height){
        super(x, y, width, height, true, true);
        
        init();
        
    }
    
    private void init(){
        setColor(Color.WHITE);
        setIsVisible(true);
        
        cornerTL = new Arc2D.Double((double)getX(), (double)getY(), 25, 25, getArcAngle(), getArcDisplacement(), Arc2D.OPEN);
        
        
        
    }

    @Override
    public void move() {
        
    }

    @Override
    public void handleInput(String inputMethod, int key) {
        
    }

    @Override
    public void runLogic() {
        
    }

    @Override
    public void drawObject(Graphics2D g) {
        
        //draw the corner pieces
        drawCornerPieces(g);
        
    }
    
    public void drawCornerPieces(Graphics2D g){
        
        
        
        g.draw(cornerTL);
        
    }
    
    //getter/setter methods

    public int getBorderLineWidth() {
        return borderLineWidth;
    }

    public void setBorderLineWidth(int borderLineWidth) {
        this.borderLineWidth = borderLineWidth;
    }

    public int getBorderLineHeight() {
        return borderLineHeight;
    }

    public void setBorderLineHeight(int borderLineHeight) {
        this.borderLineHeight = borderLineHeight;
    }

    public double getArcAngle() {
        return arcAngle;
    }

    public void setArcAngle(double arcAngle) {
        this.arcAngle = arcAngle;
    }

    public double getArcDisplacement() {
        return arcDisplacement;
    }

    public void setArcDisplacement(double arcDisplacement) {
        this.arcDisplacement = arcDisplacement;
    }
    
    
    
}
