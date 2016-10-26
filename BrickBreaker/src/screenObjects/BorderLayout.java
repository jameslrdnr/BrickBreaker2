package screenObjects;

import java.awt.BasicStroke;
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
    
    private int borderLineWidth, borderLineHeight, cornerHeight, cornerWidth;
    private double arcAngle, arcDisplacement;
    private final BasicStroke defaultStroke = new BasicStroke(1);
    
    public BorderLayout(){
        
        init();
        
    }
    
    public BorderLayout(float x, float y, int width, int height){
        super(x, y, width, height, (short)0, false, false);
        
        init();
        
    }
    
    private void init(){
        
        setColor(Color.WHITE);
        setIsVisible(true);
        
        setCornerHeight(20);
        setCornerWidth(20);
        
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
        g.setStroke(defaultStroke);
        drawCornerPieces(g);
        
    }
    
    public void drawCornerPieces(Graphics2D g){
        
        //draws the top left corner
        g.drawArc((int)getX(), (int)getY(), getCornerWidth(), getCornerHeight(), 90, 90);
        
        //draws top right corner
        g.drawArc((int)getX() + getWidth() - getCornerWidth(), (int)getY(), getCornerWidth(), getCornerHeight(), 90, -90);
        
        //draws bottom left corner
        g.drawArc((int)getX(), (int)getY() + getHeight(), getCornerWidth(), getCornerHeight(), 180, 90);
        
        //draws bottom right corner
        g.drawArc((int)getX() + getWidth() - getCornerWidth(), (int)getY() + getHeight(), getCornerWidth(), getCornerHeight(), 270, 90);
    }
    
    public void drawLineSegments(Graphics2D g){
        //draws top line
        g.drawLine((int)getX() + getCornerWidth()/2, (int)getY(), (int)getX() + getWidth() - getCornerWidth()/2, (int)getY());
        //draws bottom line
        g.drawLine((int)getX() + getCornerWidth()/2, (int)getY() + getHeight() + getCornerHeight(), (int)getX() + getWidth() - getCornerWidth()/2, (int)getY() + getHeight() + getCornerHeight());
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

    public int getCornerHeight() {
        return cornerHeight;
    }

    public void setCornerHeight(int cornerHeight) {
        this.cornerHeight = cornerHeight;
    }

    public int getCornerWidth() {
        return cornerWidth;
    }

    public void setCornerWidth(int cornerWidth) {
        this.cornerWidth = cornerWidth;
    }
    
    
    
}
