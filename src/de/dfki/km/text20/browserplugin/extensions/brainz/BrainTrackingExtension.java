/*
 * ScreenShotExtension.java
 * 
 * Copyright (c) 2009, Ralf Biedert All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the author nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package de.dfki.km.text20.browserplugin.extensions.brainz;


import java.util.logging.Logger;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import de.dfki.km.text20.browserplugin.browser.browserplugin.JSExecutor;
import de.dfki.km.text20.browserplugin.services.devicemanager.TrackingDeviceManager;
import de.dfki.km.text20.browserplugin.services.emotiondetector.impl.brain.rmserror.TrainedAvgEmotionClassifier;
import de.dfki.km.text20.browserplugin.services.emotiondetector.impl.brain.rmserror.TrainedPeakEmotionClassifier;
import de.dfki.km.text20.browserplugin.services.emotiondetector.impl.brain.simple.SimpleAvgEmotionClassifier;
import de.dfki.km.text20.browserplugin.services.emotiondetector.impl.brain.simple.SimplePeakEmotionClassifier;
import de.dfki.km.text20.browserplugin.services.extensionmanager.Extension;
import de.dfki.km.text20.browserplugin.services.extensionmanager.SetupParameter;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingListener;

/**
 * @author rb
 * 
 */
@PluginImplementation
public class BrainTrackingExtension implements Extension {

	/** */
	final Logger logger = Logger.getLogger(this.getClass().getName());

	/** */
	@InjectPlugin
	public TrackingDeviceManager deviceManager;

	/** */
	JSExecutor jsExecutor;

	// farida start
	//EmotionClassifierImpl emotionDetector;
	TrainedAvgEmotionClassifier trainedAvgEmotionClassifier = new TrainedAvgEmotionClassifier();
	TrainedPeakEmotionClassifier trainedPeakEmotionClassifier = new TrainedPeakEmotionClassifier();
	SimpleAvgEmotionClassifier simpleAvgEmotionClassifier = new SimpleAvgEmotionClassifier();
	SimplePeakEmotionClassifier simplePeakEmotionClassifier = new SimplePeakEmotionClassifier();
	
	
	//
	//UserTrainingModel trainingModel;

	// farida end

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension
	 * #executeFunction(java.lang.String, java.lang.String)
	 */
	public Object executeDynamicFunction(String function, String args) {

		if (function.equals("brainTrackerInitTraining")) {

			// farida start
			//emotionDetector = new EmotionClassifierImpl();
			//trainedAvgEmotionClassifier.clearEvents();
			//trainedPeakEmotionClassifier.clearEvents();
			//simpleAvgEmotionClassifier.clearEvents();
			//simplePeakEmotionClassifier.clearEvents();
			
			// for training
			//String modelname = args.substring(1, args.length()-1);
			//trainingModel = new UserTrainingModel(modelname);
			// farida end

			this.deviceManager.getBrainTrackingDevice().addTrackingListener(
					new BrainTrackingListener() {

						@SuppressWarnings("boxing")
						@Override
						public void newTrackingEvent(BrainTrackingEvent event) {
							//emotionDetector.addEvent(event);
							
							// Root Mean Square Error Training
							
						
							//trainedAvgEmotionClassifier.model.addEvent(event);
							trainedPeakEmotionClassifier.model.addEvent(event);
							
							//trainedAvgEmotionClassifier.addEvent(event);
							//trainedPeakEmotionClassifier.addEvent(event);
							//simpleAvgEmotionClassifier.addEvent(event);
							//simplePeakEmotionClassifier.addEvent(event);
							
							
							
							
							// for training
							//trainingModel.addEvent(event);
							
							//String next = event.getChannels().iterator().next();
							//double value = event.getValue(next);							
							//BrainTrackingExtension.this.jsExecutor.executeJSFunction("specialCallback","brainTrackerValues", next, value);
						}
					});
			return "Brain Tracker Initialized for Training!";
		}
		
		if (function.equals("brainTrackerInitEvaluation")) {

			// farida start
			//emotionDetector = new EmotionClassifierImpl();
			//trainedAvgEmotionClassifier.clearEvents();
			trainedPeakEmotionClassifier.clearEvents();
			//simpleAvgEmotionClassifier.clearEvents();
			//simplePeakEmotionClassifier.clearEvents();
			
			// for training
			//String modelname = args.substring(1, args.length()-1);
			//trainingModel = new UserTrainingModel(modelname);
			// farida end

			this.deviceManager.getBrainTrackingDevice().addTrackingListener(
					new BrainTrackingListener() {

						@SuppressWarnings("boxing")
						@Override
						public void newTrackingEvent(BrainTrackingEvent event) {
							//emotionDetector.addEvent(event);
							
							// Root Mean Square Error Training
							
						
							//trainedAvgEmotionClassifier.model.addEvent(event);
							//trainedPeakEmotionClassifier.model.addEvent(event);
							
							//trainedAvgEmotionClassifier.addEvent(event);
							trainedPeakEmotionClassifier.addEvent(event);
							//simpleAvgEmotionClassifier.addEvent(event);
							//simplePeakEmotionClassifier.addEvent(event);
							
							
							
							
							// for training
							//trainingModel.addEvent(event);
							
							//String next = event.getChannels().iterator().next();
							//double value = event.getValue(next);							
							//BrainTrackingExtension.this.jsExecutor.executeJSFunction("specialCallback","brainTrackerValues", next, value);
						}
					});
			return "Brain Tracker Initialized for Evaluation!";
		}

		if (function.equals("getTrainedAvgEmotion")) {
			// String emotion = args.substring(1, args.length()-1);			
			// trainingModel.addEmotionClass(emotion);
			// trainingModel.generateTrainingFile();
			//return emotionDetector.getEmotion();
			return trainedAvgEmotionClassifier.getEmotion();
		}
		if (function.equals("getTrainedPeakEmotion")){
			return trainedPeakEmotionClassifier.getSimpleTrainedEmotion();
		}
		
		if (function.equals("getSimpleAvgEmotion")){
			return simpleAvgEmotionClassifier.getEmotion();
		}
		if (function.equals("getSimplePeakEmotion")){
			return simplePeakEmotionClassifier.getEmotion();
		}
		
		if (function.equals("startRmseTraining")){
			trainedAvgEmotionClassifier.model.clearEvents();
			trainedPeakEmotionClassifier.model.clearEvents();
		}		
		if (function.equals("stopRmseTraining")){
			String emotion = args.substring(1, args.length()-1);
			trainedAvgEmotionClassifier.model.calculateAvgSimple(emotion);			
			trainedPeakEmotionClassifier.model.calculateAvgSimple(emotion);
			
			double size = trainedPeakEmotionClassifier.model.avgSimple.size();
			double avg = trainedPeakEmotionClassifier.model.avgSimple.get(emotion);
			return " avg size : "+size+" avg calculated: "+" avg: "+avg; // for debugging
		}
		
		if(function.equals("trainValues")){
			String in = args.substring(1, args.length()-1);
			String [] sVal = in.split("s");	
			
			trainedPeakEmotionClassifier.model.avgSimple.put("happy", Double.parseDouble(sVal[0]));
			trainedPeakEmotionClassifier.model.avgSimple.put("interested", Double.parseDouble(sVal[1]));
			trainedPeakEmotionClassifier.model.avgSimple.put("doubt", Double.parseDouble(sVal[2]));
			trainedPeakEmotionClassifier.model.avgSimple.put("bored", Double.parseDouble(sVal[3]));
			
			double size = trainedPeakEmotionClassifier.model.avgSimple.size();
			double avg = trainedPeakEmotionClassifier.model.avgSimple.get("doubt");
			
			return " avg size : "+size+" avg calculated: "+" avg: "+avg; // for debugging
		}
		
		

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension
	 * #getSupportedFunctions()
	 */
	public String[] getDynamicFunctions() {
		return new String[] { "brainTrackerInitTraining", "brainTrackerInitEvaluation", "getTrainedAvgEmotion", "getTrainedPeakEmotion", "getSimpleAvgEmotion", "getSimplePeakEmotion","startRmseTraining", "stopRmseTraining", "trainValues"};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension
	 * #
	 * setParameter(de.dfki.km.augmentedtext.browserplugin.services.extensionmanager
	 * .SetupParameter, java.lang.Object)
	 */
	public void setParameter(SetupParameter parameter, Object value) {
		if (parameter == SetupParameter.JAVASCRIPT_EXECUTOR) {
			this.jsExecutor = (JSExecutor) value;
		}
	}
}
