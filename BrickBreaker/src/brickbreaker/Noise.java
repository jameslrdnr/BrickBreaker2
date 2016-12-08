/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brickbreaker;

/**
 *
 * @author nk83868
 */
//package cc.holstr.musicShow;

import static brickbreaker.BrickBreakerMain.options;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Hello world!
 *
 */
public class Noise 
{
    //String volume = options.getProperty("volume");
    //int vol=Integer.parseInt(volume);
    //float decibel = (float)(vol-3)*6;
    //public static void main( String[] args )
    //{
    Clip clip=null;
    FloatControl gainControl;
    public Noise(float volume, String filepath) {
    		//Clip clip = null;
			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
				clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				gainControl = 
		    		 (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		    		gainControl.setValue(volume);
                        }
                        catch(Exception e){
                        
                        }
// Reduce volume by 10 decibels.
		    		/*clip.start();
		    		do {
		                    Thread.sleep(15);
		                } while (clip.isRunning());
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
	            try {
	                clip.close();
	            } catch (Exception exp) {
	            }*/
	        }
    // plays the clip once
    public void play() {
		try {
			if (clip != null) {
				new Thread() {
                                        @Override
					public void run() {
						synchronized (clip) {
							clip.stop();
							clip.setFramePosition(0);
							clip.start();
                                                        
						}
					}
				}.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public void stop(){
		if(clip == null) return;
		clip.stop();
                clip.close();
                clip = null;
                
	}
    public void loop() {
		try {
			if (clip != null) {
				new Thread() {
                                        @Override
					public void run() {
						synchronized (clip) {
							clip.stop();
							clip.setFramePosition(0);
							clip.loop(Clip.LOOP_CONTINUOUSLY);
                                                        
						}
					}
				}.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public boolean isActive(){
		return clip.isRunning();
	}
    public void setVolume(float volume){
        gainControl.setValue(volume);
    }
    

}


