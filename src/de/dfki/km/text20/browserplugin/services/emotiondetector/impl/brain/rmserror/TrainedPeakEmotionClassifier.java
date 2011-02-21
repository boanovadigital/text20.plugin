/*
 * TrainedPeakEmotionClassifier.java
 *
 * Copyright (c) 2010, Ralf Biedert, DFKI. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 *
 */
package de.dfki.km.text20.browserplugin.services.emotiondetector.impl.brain.rmserror;

import java.util.ArrayList;

import de.dfki.km.text20.browserplugin.services.emotiondetector.EmotionClassifier;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;

public class TrainedPeakEmotionClassifier implements EmotionClassifier {
	 /** Contains the brain tracking events to be evaluated for the next emotion */
    private ArrayList<BrainTrackingEvent> events = new ArrayList<BrainTrackingEvent>();

	/** Training data */
	public TrainingModel model;

	/**
	 * Constructor
	 */
	public TrainedPeakEmotionClassifier(){
		this.model = new TrainingModel();
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public String getEmotion() {

		// sum of channels to be used
		double sumSmile = 0, sumFurrow = 0, sumEngagement = 0;

		// count of events taken into consideration for each channel (e.g. when smile is zero it is not included for its calculations)
		double cntSmile = 0, cntFurrow = 0, cntEngagement = 0;

		// average of each channel
		double avgSmile, avgFurrow, avgEngagement;

		// Root Mean Squared Error for each emotion
		//double rmseHappy, rmseInterested, rmseDoubt, rmseBored;

		// goes through all events and calculates sum and count
		ArrayList<BrainTrackingEvent> currEvents;

		synchronized(this.events){
			currEvents = (ArrayList<BrainTrackingEvent>) this.events.clone();
			this.events.clear();
		}

		if(currEvents.isEmpty())
			return null;

		boolean bored = false, doubt = false, interested = false, happy = false;

    	// bored or interested (continuous, take average)
    	double sum = 0;
    	double avg = 0;
    	for(BrainTrackingEvent b : currEvents){
    		sum += b.getValue("channel:engagement");
    	}
    	avg = sum / currEvents.size();

    	double thresholdBored = this.model.avgSimple.containsKey("bored") ? this.model.avgSimple.get("bored").doubleValue(): 0.4;
    	double thresholdDoubt = this.model.avgSimple.containsKey("doubt") ? this.model.avgSimple.get("doubt").doubleValue(): 0.2;
    	double thresholdInterested = this.model.avgSimple.containsKey("interested")? this.model.avgSimple.get("interested").doubleValue(): 0.7;
    	double thresholdHappy = this.model.avgSimple.containsKey("happy") ? this.model.avgSimple.get("happy").doubleValue(): 0.8;

    	if(avg <= thresholdBored)
    		bored = true;
    	else if(avg >= thresholdInterested)
    			interested = true;

    	// doubt or happy (have peaks)

    	double peakFurrow = 0;
    	double peakSmile = 0;

    	for(BrainTrackingEvent b : currEvents){
    		if(b.getValue("channel:furrow") >= thresholdDoubt){
    			doubt = true;
    			peakFurrow = b.getValue("channel:furrow") > peakFurrow ? b.getValue("channel:furrow") : peakFurrow;
    		}
    		else if(b.getValue("channel:smile") >= thresholdHappy)
    				happy = true;
    				peakSmile = b.getValue("channel:smile") > peakSmile ? b.getValue("channel:smile") : peakSmile;
    	}

    	// return by priorities (bored > doubt > interested > happy)
    	String emotion = "";
    	if(bored)
    		emotion += "bored";
    	else if(doubt)
    			emotion += "doubt";
    	else if(interested)
    			emotion += "interested";
    	else if(happy)
    			emotion += "happy";
    	else emotion +="neutral";

    	emotion += " " + round(peakFurrow) +" "+ round(peakSmile) +" "+ round(avg);

    	return emotion;
	}

	@SuppressWarnings("unchecked")
    public String getSimpleTrainedEmotion(){
		// goes through all events and calculates sum and count
		ArrayList<BrainTrackingEvent> currEvents;

		synchronized(this.events){
			currEvents = (ArrayList<BrainTrackingEvent>) this.events.clone();
			this.events.clear();
		}

		if(currEvents.isEmpty())
			return null;

		boolean boredSimple = false, doubtSimple = false, interestedSimple = false, happySimple = false;
		boolean boredTrained = false, doubtTrained = false, interestedTrained = false, happyTrained = false;

    	// bored or interested (continuous, take average)
    	double sum = 0;
    	double avg = 0;
    	for(BrainTrackingEvent b : currEvents){
    		sum += b.getValue("channel:engagement");
    	}
    	avg = sum / currEvents.size();

    	// trained

    	double thresholdBoredTrained = this.model.avgSimple.containsKey("bored") ? this.model.avgSimple.get("bored").doubleValue(): 0.4;
    	double thresholdDoubtTrained = this.model.avgSimple.containsKey("doubt") ? this.model.avgSimple.get("doubt").doubleValue(): 0.2;
    	double thresholdInterestedTrained = this.model.avgSimple.containsKey("interested")? this.model.avgSimple.get("interested").doubleValue(): 0.7;
    	double thresholdHappyTrained = this.model.avgSimple.containsKey("happy") ? this.model.avgSimple.get("happy").doubleValue(): 0.8;

    	if(avg <= thresholdBoredTrained)
    		boredTrained = true;
    	else if(avg >= thresholdInterestedTrained)
    			interestedTrained = true;

    	// doubt or happy (have peaks)

    	double peakFurrow = 0;
    	double peakSmile = 0;

    	for(BrainTrackingEvent b : currEvents){
    		if(b.getValue("channel:furrow") >= thresholdDoubtTrained){
    			doubtTrained = true;
    			peakFurrow = b.getValue("channel:furrow") > peakFurrow ? b.getValue("channel:furrow") : peakFurrow;
    		}
    		else if(b.getValue("channel:smile") >= thresholdHappyTrained || b.getValue("channel:laugh") >= thresholdHappyTrained){
    				happyTrained = true;
    				peakSmile = b.getValue("channel:smile") > peakSmile ? b.getValue("channel:smile") : (b.getValue("channel:laugh") > peakSmile? b.getValue("channel:laugh") : peakSmile) ;
    			}
    	}

    	// return by priorities (bored > doubt > interested > happy)
    	String emotionTrained = "";
    	if(boredTrained)
    		emotionTrained += "bored";
    	else if(doubtTrained)
    			emotionTrained += "doubt";
    	else if(interestedTrained)
    			emotionTrained += "interested";
    	else if(happyTrained)
    			emotionTrained += "happy";
    	else emotionTrained +="neutral";

    	// simple

    	if(avg <= 0.4)
    		boredSimple = true;
    	else if(avg >= 0.7)
    			interestedSimple = true;

    	// doubt or happy (have peaks)
    	for(BrainTrackingEvent b : currEvents){
    		if(b.getValue("channel:furrow") >= 0.2){
    			doubtSimple = true;
    		}
    		else if(b.getValue("channel:smile") >= 0.8 || b.getValue("channel:laugh") >= 0.8 ){
    			happySimple = true;
    		}
    	}

    	// return by priorities (bored > doubt > interested > happy)
    	String emotionSimple = "";
    	if(boredSimple)
    		emotionSimple += "bored";
    	else if(doubtSimple)
    			emotionSimple += "doubt";
    	else if(interestedSimple)
    			emotionSimple += "interested";
    	else if(happySimple)
    			emotionSimple += "happy";
    	else emotionSimple +="neutral";


    	// mixed

    	String emotionMixed = "";
    	if(boredSimple)
    		emotionMixed += "bored";
    	else if(doubtTrained)
    			emotionMixed += "doubt";
    	else if(interestedSimple)
    			emotionMixed += "interested";
    	else if(happyTrained)
    			emotionMixed += "happy";
    	else emotionMixed +="neutral";

    	return emotionSimple + " " + emotionTrained+ " " + emotionMixed + " " + round(peakFurrow) +" "+ round(peakSmile) +" "+ round(avg);
	}

	public String getEmotionRMSE(){
		// 0-furrow, 1-smile, 2-engagement
		double [] values = new double[3];
		int cnt = 0;
		synchronized (this.events) {
			cnt = this.events.size();
			for(BrainTrackingEvent b : this.events){
				values[0] += b.getValue("channel:furrow");
				values[1] += b.getValue("channel:smile");
				values[2] += b.getValue("channel:engagement");
			}
			this.events.clear();
		}

		// calculate average
		for(double d : values)
			d = d / cnt;

		double rmseHappy = getRMSE(values, "happy"), rmseInterested = getRMSE(values, "interested"), rmseDoubt = getRMSE(values, "doubt"), rmseBored = getRMSE(values, "bored");

		// return emotion with minimum Root Mean Squared Error
		if(rmseHappy < rmseInterested && rmseHappy < rmseDoubt && rmseHappy < rmseBored)
			return "happy";
		if(rmseInterested < rmseHappy && rmseInterested < rmseDoubt && rmseInterested < rmseBored)
			return "interested";
		if(rmseDoubt < rmseHappy && rmseDoubt < rmseInterested && rmseDoubt < rmseBored)
			return "happy";
		if(rmseBored < rmseHappy && rmseBored < rmseInterested && rmseBored < rmseDoubt)
			return "happy";

		return null;
	}



	/**
	 * Returns the Root Mean Squared Error of the current values and the emotion class
	 *
	 * @param values the calculated average of the events
	 * @param emotion the emotion to be compared to
	 * @return the Root Mean Square Error of the values and the given emotion
	 */
	public double getRMSE(double [] values, String emotion){
		double [] avg = this.model.avgComplex.get(emotion);
		double squareSum = 0;
		for(int i = 0; i< avg.length; i++)
			squareSum += Math.pow(values[i] - avg [i], 2);
		return Math.pow(squareSum / avg.length, 0.5);
	}


	/**
     * Adds an event to the list
     *
     * @param event the new event to be added to the list
     */
    public void addEvent(BrainTrackingEvent event){
    	synchronized (this.events) {
    		this.events.add(event);
		}
    }

    /** Clears event list */
    public void clearEvents(){
    	synchronized (this.events) {
    		this.events.clear();
		}
    }

    private double round(double num){
    	return Math.round(num * 100) / 100.0;
    }
  }
