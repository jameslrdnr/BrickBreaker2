///*
 /* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

import static brickbreaker.BrickBreakerMain.options;
import java.util.ArrayList;

/**
 *
 * @author nk83868
 */
public class MasterSound {
    private Noise bgSong;
//    private String volume=options.getProperty("volume");
    //private int lvl = Integer.getInteger(volume);
    //int soundEffectNum=0;
    private float vol = (float)((5-5)*-4)+25;
    
    ArrayList<Noise> sounds = new ArrayList<>();
    //sounds.add(bgSong);
    public MasterSound(){
        
    }
    /*public void setBgMusic(String filename){
        /*if(bgSong.isPlaying()){
            bgSong.close();
        }
        bgSong = new Noise(vol,filename);
        sounds.add(bgSong);
        
    }  ***//////*/////*/
    public void cleanMusic(){
        for(int i=0; i<sounds.size();){
            if(!sounds.get(i).isActive()){
                //sounds.get(i).stop();
                //sounds.remove(i);
                System.out.println("notActive");
            }
        }
    }
    public void playBgMusic(String filename){
       /* if(bgSong.isPlaying()){
            bgSong.close();
        }*/
        bgSong = new Noise(vol,filename);
        sounds.add(bgSong);
        bgSong.loop();
    }
    public void stopBgMusic(){
        //bgSong.endMusic();
        bgSong.stop();
        
    }/*
    public void setSF(String filename){
        soundEffect1 = new MP3(filename, false);
        
    }*/
    public void newSF(String filename){
       // soundEffect1.play();
       Noise soundF = new Noise(vol, filename);
       sounds.add(soundF);
       soundF.play();
       
    }
    public void endSound(){
        bgSong.stop();
        for(int i=0; i<sounds.size();){
            sounds.get(i).stop();
            sounds.remove(i);
        }
            
        //implement Arraylist 
    }
    public void setVolume(){
        for(int i=0; i<sounds.size(); i++){
            sounds.get(i).setVolume(vol);
        }
    }
            
}
