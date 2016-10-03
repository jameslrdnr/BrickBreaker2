/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ge29779
 */
public class HighScoresScreen extends AbstractScreen implements Comparator<String>{

    //variables
    //------------------------------------------------------------------
    private int currentScore;
    private boolean typing;
    private String playerName;
    private ArrayList<String> scoresList;
    private final String fileName;
    private BufferedImage background;
        
    //constructor receiving the score and setting the variables
    //------------------------------------------------------------------
    public HighScoresScreen(int currentScore){
        super();
        this.currentScore = currentScore;
        typing = true;
        playerName = "";
        fileName = "src\\assets\\HighScoresFile";
        scoresList = new ArrayList<String>();
        addScores();
        //Scanner scan = new Scanner(System.in);
        //scoresList.add(scan.nextLine());
        //sortScores();
        //Collections.reverse(scoresList);
        //writeScores();
    }
    
    
    @Override
    public void runLogic() {
        if(typing){
            return;
            
        }
        else{
            sortScores();
            Collections.reverse(scoresList);
            writeScores();
            System.out.println(scoresList);
            
        }
    }

    @Override
    public void drawGame(Graphics2D g) {
        
        //Make a black background to draw onto
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setColor(Color.WHITE);
        
        if(typing){            
            //Draw playerName
            drawCenteredString(g, playerName, 400, 300, new Font("Garamond", Font.PLAIN, 24));
        }
        
        else{
            //Draw playerName
            drawCenteredString(g, scoresList.toString(), 400, 300, new Font("Garamond", Font.PLAIN, 24));
        }
    }

    
    //Handles name input
    //------------------------------------------------------------------
    @Override
    public void keyTyped(KeyEvent e) {}
    
    //Sorts arrayList of scores
    //------------------------------------------------------------------
    public void sortScores(){
        scoresList.sort(this);
    }

    //Compares strings of score/name combos and compares by score
    //------------------------------------------------------------------
    @Override
    public int compare(String o1, String o2) {
        if(Integer.parseInt(o1.substring(0, o1.indexOf(' '))) > Integer.parseInt(o2.substring(0, o1.indexOf(' '))) ){
            return 1;
        }
        else if(Integer.parseInt(o1.substring(0, o1.indexOf(' '))) == Integer.parseInt(o2.substring(0, o1.indexOf(' '))) ){
            if(o1.substring(o1.indexOf(' '), o1.length()).compareTo(o2.substring(o2.indexOf(' '), o2.length())) == 1)
                return -1;
            else if(o1.substring(o1.indexOf(' ')).compareTo(o2.substring(o2.indexOf(' '))) == -1)
                return 1;
            else
                return 0;
        }
        else if(Integer.parseInt(o1.substring(0, o1.indexOf(' '))) < Integer.parseInt(o2.substring(0, o1.indexOf(' '))) ){
            return -1;
        }
        else{
            throw new Error("compare has failed with o1 - " + o1 + " and o2 - " + o2);
        }
    }

    private void addScores() {
        
        //Uses declared final name
        
        //Reader will access one line at a time
        String line = null;
        
        try{
            //Generic Reader
            FileReader fileReader = new FileReader(fileName);
            
            //Supposed to always use BufferedReader
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            //Add each line to scoresList
            while((line = bufferedReader.readLine()) != null){
                scoresList.add(line);
            }
            
            //Close the file
            bufferedReader.close();
        }
        catch(FileNotFoundException e){
            System.out.println("'" + fileName + "' " + "not found");
        }
        catch(IOException e){
            System.out.println("Failed to read file : " + fileName);
        }
    }
    
    private void writeScores(){
        
        //Uses declared file name
        
        try{
            //Generic Writer
            FileWriter fileWriter = new FileWriter(fileName);
            
            //Supposed to use BufferedWriter
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        
            //Write each string in scoresList to a line
            for(String current : scoresList){
                bufferedWriter.write(current);
                bufferedWriter.newLine();
            }
            
            //Close the file
            bufferedWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(HighScoresScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void specificInput(ArrayList<Integer> inputList) {
        //System.out.print("Key Typed");
        for(int key : inputList)
        if(typing){
            if(key == KeyEvent.VK_BACK_SPACE){
                playerName = playerName.substring(0, playerName.length()-1);
                System.out.println("BackSpace");
            }
            else if(key == KeyEvent.VK_ENTER){
                typing = false;
                scoresList.add(currentScore + " " + playerName);
                System.out.println("Enter");
            }
            else if(Character.isLetterOrDigit(key)){
                playerName += (char)key;
                System.out.println("" + (char)key);
            }
            //System.out.print("In typing");
        }
    }
}
