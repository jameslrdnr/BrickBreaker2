/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screenObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author JamesLaptop
 */
public abstract class AbstractScreenObject {
    
    //global variable declarations
    //------------------------------------------------------------------
    private final short idNum;
    private float x, y, deltaX, deltaY;
    private final float initX, initY;
    private final float rX, bY;
    /*layers:
    0 = backround layer
    1 = backround OBJECT layer
    2 = map layer
    3 = screenobject layer
    4 = player layer
    */
    public final byte BACKROUNDLAYER = 0, BACKROUNDOBJLAYER = 1, MAPLAYER = 2, SCREENOBJLAYER = 3, PLAYERLAYER = 4;
    
    private static final String OBJECTIDFILE = "src\\assets\\ObjectIDMap.properties";
    private static Properties OBJECTIDMAP;
    /*ids:
    0 = someone forgot to set a id
    1 - 9 : random stuff
    10-14 : particles
    20-29 : enemies
    30 - 49 : pickups
    */
    public static final byte MENUSELECTORICONID = 1,
            BASICBACKGROUNDOBJECTID = 2,
            BASICBRICKOBJECTID = 3,
            BASICMAPOBJECTID = 4,
            DEBUGID = 5,
            PLAYERSCREENOBJECTID = 6,
            BASICPLAYERBULLETSCREENOBJECTID = 7,
            BASICENEMYBULLETSCREENOBJECTID = 8,
            BASICMISSILESCREENOBJECTID = 9,
            BASICPARTICLESYSTEMID = 10,
            BASICPARTICLEID = 11,
            BASICENEMYSCREENOBJECTID = 20,
            BASICHEALTHPICKUPSCREENOBJECTID = 30,
            BASICPOINTPICKUPSCREENOBJECTID = 31,
            BASICTHREESHOTPICKUPSCREENOBJECTID = 32;
    
    public final int LEFT = 180, RIGHT = 0, UP = 90, DOWN = 270;
    
    private int width, height, layer;
    private boolean collision, Fading;
    //for direction you are facing
    private float degrees, speed, oldDegrees;
    //set default color
    private Color color = Color.CYAN;
    private boolean isVisible, acceptingInput, delayInput; 
    private int inputDelay, inputFrameCounter, colorFadeTime, colorFadeTimer;
    private int yMovementMultiplier = 1, xMovementMultiplier = 1, position, maxPosition;
    private Shape collisionShape;
    private Shape myShape;
    private BasicParticleSystem particleSys;
    private float rDif, gDif, bDif;
    
    
    //array of rainbow colors
    public static Color[] rainbowColors;


    
    //init constructors
    public AbstractScreenObject(float xTemp, float yTemp, int widthTemp, int heightTemp, short id, boolean collisionTemp, boolean acceptingInput){
        
//        OBJECTIDMAP = new Properties();
//        try{
//            FileInputStream in = new FileInputStream(OBJECTIDFILE);
//            OBJECTIDMAP.load(in);
//            in.close();
//        }
//        catch(Exception e){
//            System.out.println("Error reading in OBJECTIDMAP : " + e);
//        }
//        MENUSELECTORICONID = Byte.parseByte(OBJECTIDMAP.getProperty("MENUSELECTORICONID"));
//        BASICBACKGROUNDOBJECTID = Byte.parseByte(OBJECTIDMAP.getProperty("BASICBACKGROUNDOBJECTID"));
//        BASICBRICKOBJECTID = Byte.parseByte(OBJECTIDMAP.getProperty("BASICBRICKOBJECTID"));
//        BASICMAPOBJECTID = Byte.parseByte(OBJECTIDMAP.getProperty("BASICMAPOBJECTID"));
//        DEBUGID = Byte.parseByte(OBJECTIDMAP.getProperty("DEBUGID"));
//        PLAYERSCREENOBJECTID = Byte.parseByte(OBJECTIDMAP.getProperty("PLAYERSCREENOBJECTID"));
//        BASICPLAYERBULLETSCREENOBJECTID = Byte.parseByte(OBJECTIDMAP.getProperty("BASICPLAYERBULLETSCREENOBJECTID"));
//        BASICPARTICLESYSTEMID = Byte.parseByte(OBJECTIDMAP.getProperty("BASICPARTICLESYSTEMID"));
//        BASICPARTICLEID = Byte.parseByte(OBJECTIDMAP.getProperty("BASICPARTICLEID"));
//        BASICENEMYSCREENOBJECTID = Byte.parseByte(OBJECTIDMAP.getProperty("BASICENEMYSCREENOBJECTID"));
//        BASICHEALTHPICKUPSCREENOBJECTID = Byte.parseByte(OBJECTIDMAP.getProperty("BASICHEALTHPICKUPSCREENOBJECTID"));
//        BASICPOINTPICKUPSCREENOBJECTID = Byte.parseByte(OBJECTIDMAP.getProperty("BASICPOINTPICKUPSCREENOBJECTID"));
        
        x = xTemp;
        y = yTemp;
        initX = x;
        initY = y;
        deltaX = 0;
        deltaY = 0;
        width = widthTemp;
        height = heightTemp;
        rX = x + width;
        bY = y + height;
        collision = collisionTemp;
        this.acceptingInput = acceptingInput;
        idNum = id;
        Fading = false;
        
        //create and fill rainbowColors
        rainbowColors = new Color[6];
        addColors();
    }
    
    public AbstractScreenObject(){
        
        
        speed = 0;
        x = 0;
        y = 0;
        initX = x;
        initY = y;
        deltaX = 0;
        deltaY = 0;
        width = 0;
        height = 0;
        rX = x + width;
        bY = y + height;
        collision = false;
        acceptingInput = false;
        inputDelay = 0;
        delayInput = false;
        idNum = 0;
        Fading = false;
        //create and fill rainbowColors
        rainbowColors = new Color[6];
        addColors();
    }
    
    //called to see if object should be removed
    public boolean shouldDestroyObject(){
        return false;
    }
    
    //movement methods
    //------------------------------------------------------------------
    
    public void movementHandler(){
        if(getDegrees()<0)
            setDegrees(360+getDegrees());
        if(getDegrees()>360)
            setDegrees(getDegrees() - 360);
        if (getDegrees() != getOldDegrees()) {
            setDeltaX((float) Math.cos(Math.toRadians(degrees)));
            //bc of inverted Y axis bullshit
            setDeltaY(-(float) Math.sin(Math.toRadians(degrees)));
        }
        oldDegrees = getDegrees();
        move();
    }
    
    public abstract void move();
    
    public void moveXMultiply(float dX){
        setX(getX() + dX * xMovementMultiplier);
    }
    
    public void moveX(float dX){
        setX(getX() + dX);
    }
    
    public void moveYMultiply(float dY){
        setY(getY() + dY * yMovementMultiplier);
    }
    
    public void moveY(float dY){
        setY(getY() + dY);
    }
    
    //object input handler methods
    //------------------------------------------------------------------
    
    //this is the method the screen calls
    public void inputHandler(String inputMethod, ArrayList<Integer> inputList, String inputMethodRemove, ArrayList<Integer> inputListReleased){
        //checks to see if accepting input at all
        if(getAcceptingInput()){
            //checks to see if input is delayed
            if(delayInput == false){
                handleInput(inputMethod, inputList, inputMethodRemove, inputListReleased);
            }
        }
    }
    
    //delayed input methods
    public void delayInput(int time){
        inputFrameCounter = time;
        delayInput = true;
    }
    
    public void delayedInputLogicManager(){
        if (delayInput == true) {
            inputFrameCounter--;
        }
        //checks to see if all delayed frames have been parsed thorugh
        if (inputFrameCounter <= 0) {
            delayInput = false;
        }
    }
    
    //object specific input logic
    public abstract void handleInput(String inputMethod, ArrayList<Integer> inputList, String inputMethodRemove, ArrayList<Integer> inputListReleased);
    
    //object logic methods
    //------------------------------------------------------------------
    public abstract void runLogic();
    
    
    //collision detection methods
    //------------------------------------------------------------------
    
    public boolean testBoundingIntersection(Shape tempShape){
        Area areaA = new Area(getCollisionShape().getBounds2D());
        areaA.intersect(new Area(tempShape.getBounds2D()));
        return !areaA.isEmpty();
    }
    
    public boolean testIntersection(Shape tempShape) {
        Area areaA = new Area(getCollisionShape());
        areaA.intersect(new Area(tempShape));
        return !areaA.isEmpty();
    }
    
    public boolean checkIsOffScreen(int byAmount){
        
        if(getX() + getWidth() < 0 - byAmount){
            return true;
        }else if(getX() > brickbreaker.BrickBreakerMain.SCREENWIDTH + byAmount){
            return true;
        }else if(getY() + getHeight() < 0 - byAmount){
            return true;
        }else if(getY() > brickbreaker.BrickBreakerMain.SCREENHEIGHT + byAmount){
            return true;
        }
        
        return false;
        
    }
    
    //this is added later (looking at you garett) - K
    public void addColors() {

        //red
        rainbowColors[0] = new Color(255, 0, 0);

        //orange
        rainbowColors[1] = new Color(255, 127, 0);

        //yellow
        rainbowColors[2] = new Color(255, 255, 0);

        //green
        rainbowColors[3] = new Color(0, 255, 0);

        //blue - NOTE - not default blue due to being hard to see
        rainbowColors[4] = new Color(0, 140, 255);

        //violet
        rainbowColors[5] = new Color(139, 0, 255);

    }
    
    
    /*
    public boolean testIntersection(Shape otherShape) {
        Area areaA = new Area(getCollisionShape());
        Area tempArea = areaA;
        //temp area will equal a new area that is all the points that actually intersect
        tempArea.intersect(new Area(otherShape));
        //equals checks if they are the same, if not it returns false, which means there is a collision
        return !areaA.equals(tempArea);
    }
    */


    //ancient history lies here
    
    /*
    private boolean collidesWith(AbstractScreenObject ob){
        
        float otherX = ob.getX();
        float otherY = ob.getY();
        int otherWidth = ob.getWidth();
        int otherHeight = ob.getHeight();
        
        //checks if it can collide at all
        if(collision == false){
            return false;
        }
        //checks to see if it is on same Y level
        if(otherY+otherHeight >= y && otherY <= y+height){
            //checks to see if it is in the same X level
            if(otherX+width >= x && otherX <= x+width){
                return true;
            }
        }
        
        return false;
    }
    //find which face the other object hit, then return a char representing it (ie: 'R','L','T','B')
    public char willCollideWithFace(AbstractScreenObject ob){
        
        float otherX = ob.getX();
        float otherY = ob.getY();
        float otherDeltaX = ob.getDeltaX();
        float otherDeltaY = ob.getDeltaY(); 
        int otherWidth = ob.getWidth();
        int otherHeight = ob.getHeight();
        float otherRX = otherX + otherWidth;
        float otherBY = otherY + otherHeight;
        
        //checks if it can even collide
        if(collision == false)
            return ' ';
        
        //checks for top collision
        if((otherBY+otherDeltaY >= y-5 && otherBY+otherDeltaY <= y+25) && (otherRX+otherDeltaX >= x && otherX+otherDeltaX <= rX)){
            return 'T';
        }
        //check for bottom
        if((otherY+otherDeltaY >= bY+5 && otherY+otherDeltaY <= bY-25) && (otherRX+otherDeltaX >= x && otherX+otherDeltaX <= rX)){
            return 'B';
        }
        //check for right
        if((otherBY+otherDeltaY >= y && otherY+otherDeltaY <= bY) && (otherX+otherDeltaX >= rX-25 && otherX+otherDeltaX <= rX+5)){
            return 'R';
        }
        //check for left
        if((otherBY+otherDeltaY >= y && otherY+otherDeltaY <= bY) && (otherRX+otherDeltaX >= x-5 && otherX+otherDeltaX <= x+25)){
            return 'L';
        }        
        return ' ';
    }
    */
    
    
    
    
    //graphics methods
    //------------------------------------------------------------------
    public abstract void drawObject(Graphics2D g);
    
    public void fadeToColor(Color c2){
        rDif = (c2.getRed() - getColor().getRed()) / colorFadeTimer;
        gDif = (c2.getGreen() - getColor().getGreen()) / colorFadeTimer;
        bDif = (c2.getBlue() - getColor().getBlue()) / colorFadeTimer;
        
        Fading = true;
    }
    
    
    //getter/setter methods here
    //------------------------------------------------------------------
    public float getX(){
        return x;
    }
    
    public float getY(){
        return y;
    }
    
    public int getHeight(){
        return height;
    }
    
    public int getWidth(){
        return width;
    }
    
    public float getDeltaX(){
        return deltaX;
    }
    
    public float getDeltaY(){
        return deltaY;
    }
    
    public Color getColor(){
        return color;
    }
    
    public boolean getIsVisible(){
        return isVisible;
    }
    
    public boolean getAcceptingInput(){
        return acceptingInput;
    }
    
    public int getyMovementMultiplier() {
        return yMovementMultiplier;
    }

    public void setyMovementMultiplier(int yMovementMultiplier) {
        this.yMovementMultiplier = yMovementMultiplier;
    }

    public int getxMovementMultiplier() {
        return xMovementMultiplier;
    }

    public void setxMovementMultiplier(int xMovementMultiplier) {
        this.xMovementMultiplier = xMovementMultiplier;
    }
    
    public void setX(float xTemp){
        x = xTemp;
    }
    
    public void setY(float yTemp){
        y = yTemp;
    }
    
    public void setWidth(int widthTemp){
        width = widthTemp;
    }
    
    public void setHeight(int heightTemp){
        height = heightTemp;
    }
    
    public void setDeltaX(float deltaXTemp){
        deltaX = deltaXTemp;
    }
    
    public void setDeltaY(float deltaYTemp){
        deltaY = deltaYTemp; 
    }
    
    public void setColor(Color c){
        color = c;
    }
    
    public void setIsVisible(boolean b){
        isVisible = b;
    }
    
    public void setAcceptingInput(boolean b){
        acceptingInput = b;
    }
    
    public short getIdNum() {
        return idNum;
    }

    public float getInitX() {
        return initX;
    }

    public float getInitY() {
        return initY;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getMaxPosition() {
        return maxPosition;
    }

    public void setMaxPosition(int maxPosition) {
        this.maxPosition = maxPosition;
    }

    public int getInputDelay() {
        return inputDelay;
    }

    public void setInputDelay(int inputDelay) {
        this.inputDelay = inputDelay;
    }

    public boolean getDelayInput() {
        return delayInput;
    }

    public void setDelayInput(boolean delayInput) {
        this.delayInput = delayInput;
    }

    public int getInputFrameCounter() {
        return inputFrameCounter;
    }

    public void setInputFrameCounter(int inputFrameCounter) {
        this.inputFrameCounter = inputFrameCounter;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public Shape getCollisionShape() {
        return collisionShape;
    }

    public void setCollisionShape(Shape collisionShape) {
        this.collisionShape = collisionShape;
    }
    
    public Shape getMyShape() {
        return myShape;
    }

    public void setMyShape(Shape myShape) {
        this.myShape = myShape;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
    
    //Used to get the translated form of a shape
    public void translateMyShape(float xTrans, float yTrans){
        AffineTransform at = AffineTransform.getTranslateInstance(xTrans, yTrans);
        Shape translatedShape = at.createTransformedShape(getMyShape());
        setMyShape(translatedShape);
    }
    
    
    //Used to get the rotated form of a shape
    public void rotateMyShape(float degrees, double xCoordinate, double yCooridate) {
        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(degrees), xCoordinate, yCooridate);
        Shape rotatedShape = at.createTransformedShape(getMyShape());
        setMyShape(rotatedShape);    
    } 
    
    public float getDegreesToRotate(AbstractScreenObject target){
        
        float tY = (float)target.getMyShape().getBounds2D().getCenterY(), tX = (float)target.getMyShape().getBounds2D().getCenterX(), degToT, xSide, ySide, currentDeg = getDegrees(), finalDeg;
        xSide = tX - getX();
        //y goes down so its weird
        ySide = getY() - tY;
        //degToT = (float)Math.toDegrees(Math.atan2(xSide, ySide));
        
        //Q1 & 4
        if(xSide >= 0){
            //if in Q1
            if(ySide >= 0){
                degToT = (float)Math.toDegrees(Math.atan(ySide/xSide));
            }
            //if in Q4
            else{
                degToT = (float)Math.toDegrees(Math.atan(ySide/xSide));
            }
        }
        //Q2 & 3
        else{
            //if in Q2
            if(ySide >= 0){
                degToT = 180 + (float)Math.toDegrees(Math.atan(ySide/xSide));
            }
            //if in Q3
            else{
                degToT = 180 + (float)Math.toDegrees(Math.atan(ySide/xSide));
            }
        }
        
        finalDeg = degToT - currentDeg;
        
        if(finalDeg > 180){
            finalDeg = -360 + finalDeg;
        } else if(finalDeg < -180){
            finalDeg = 360 + finalDeg;
        }
        
        //turn it into a 360 angle if negative
        /*if(degToT < 0){
            degToT = 360 + degToT;
        }
        */
        return (finalDeg);
        
    }

    public BasicParticleSystem getParticleSys() {
        return particleSys;
    }

    public void setParticleSys(BasicParticleSystem particleSys) {
        this.particleSys = particleSys;
    }

    public int getColorFadeTime() {
        return colorFadeTime;
    }

    public void setColorFadeTime(int colorFadeTime) {
        this.colorFadeTime = colorFadeTime;
    }

    public int getColorFadeTimer() {
        return colorFadeTimer;
    }

    public void setColorFadeTimer(int colorFadeTimer) {
        this.colorFadeTimer = colorFadeTimer;
    }

    public boolean isFading() {
        return Fading;
    }

    public void setFading(boolean isFading) {
        this.Fading = isFading;
    }

    public float getrDif() {
        return rDif;
    }

    public float getgDif() {
        return gDif;
    }

    public float getbDif() {
        return bDif;
    }

    public float getDegrees() {
        return degrees;
    }

    public void setDegrees(float degrees) {
        this.degrees = degrees;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getOldDegrees() {
        return oldDegrees;
    }
    
    
    
}
