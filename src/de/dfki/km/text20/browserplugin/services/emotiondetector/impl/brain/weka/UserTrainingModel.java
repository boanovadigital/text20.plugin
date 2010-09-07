package de.dfki.km.text20.browserplugin.services.emotiondetector.impl.brain.weka;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;

import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;

public class UserTrainingModel {

	/** Contains the events of the emotion being trained */
	public ArrayList<double []> trainingValues;

	/** Contains the classes to be added to the file */
	public ArrayList<EmotionClass> classes;

	/** name of training model */
	public String modelname;


	/** Constructor */
	public UserTrainingModel(String modelname){
		this.trainingValues = new ArrayList<double[]>();
		this.classes = new ArrayList<EmotionClass>();
		this.modelname = modelname;
	}

	/** Adds and event to the emotion being trained */
	public void addEvent(BrainTrackingEvent event){
		synchronized(this.trainingValues){
			double[] values = {event.getValue("channel:furrow"), event.getValue("channel:smile"), event.getValue("channel:laugh"), event.getValue("channel:engagement")};
			this.trainingValues.add(values);
		}
	}

	/**
	 * Creates and adds an emotion class with the according data to the list of classes
	 *
	 * @param emotion The name of the emotion trained
	 */
	@SuppressWarnings("unchecked")
	public void addEmotionClass(String emotion){
		EmotionClass e = new EmotionClass(emotion);
		synchronized(this.trainingValues){
			e.data = (ArrayList<double[]>) this.trainingValues.clone();
			this.trainingValues.clear();
		}

		this.classes.add(e);
	}

	/** Generates ARFF file containing training data */
	public void generateTrainingFile(){
		try{
			AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
                @SuppressWarnings("boxing")
                @Override
                public Boolean run() {
                    // Create the directory if it doesn't exist yet
                    return new File("Training").mkdirs();
                }
            });
			ARFFGenerator.generateFile("Training\\" + this.modelname + ".arff", this.classes);

		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
