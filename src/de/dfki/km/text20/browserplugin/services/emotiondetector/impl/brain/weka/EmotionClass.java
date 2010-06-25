package de.dfki.km.text20.browserplugin.services.emotiondetector.impl.brain.weka;

import java.util.ArrayList;

public class EmotionClass {
	/** Name of the emotion */
	public String emotion;
	
	/** Contains all the data instances*/
	public ArrayList<double []> data;
	
	/**
	 * Constructor 
	 * 
	 * @param emotion The name of the emotion
	 */
	public EmotionClass(String emotion){
		this.emotion = emotion;	
	}
}
