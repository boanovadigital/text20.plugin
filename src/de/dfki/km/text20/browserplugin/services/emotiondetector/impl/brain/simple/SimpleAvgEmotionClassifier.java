/*
 * SimpleAvgEmotionClassifier.java
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
package de.dfki.km.text20.browserplugin.services.emotiondetector.impl.brain.simple;

import java.util.ArrayList;

import de.dfki.km.text20.browserplugin.services.emotiondetector.EmotionClassifier;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;

public class SimpleAvgEmotionClassifier implements EmotionClassifier {
    /** Contains the brain tracking events to be evaluated for the next emotion */
    private ArrayList<BrainTrackingEvent> events = new ArrayList<BrainTrackingEvent>();


	@SuppressWarnings("unchecked")
	@Override
    public String getEmotion() {
    	if(!this.events.isEmpty()){

    		// sum of channels to be used
    		double sumSmile = 0, sumFurrow = 0, sumEngagement = 0;

    		// count of events taken into consideration for each channel (e.g. when smile is zero it is not included for its calculations)
    		double cntSmile = 0, cntFurrow = 0, cntEngagement = 0;

    		// average of each channel
    		double avgSmile, avgFurrow, avgEngagement;


	    	ArrayList<BrainTrackingEvent> currEvents;

	    	// retrieve events and clear for coming events
	    	synchronized (this.events) {
	    		currEvents = (ArrayList<BrainTrackingEvent>) this.events.clone();
	    		this.events.clear();
			}

	    	for(BrainTrackingEvent e : currEvents){

				double value = e.getValue("channel:smile");
				sumSmile += value;
				cntSmile = (value == 0? cntSmile : cntSmile+1);

				value = e.getValue("channel:furrow");
				sumFurrow += value;
				cntFurrow = (value == 0? cntFurrow : cntFurrow+1);

				value = e.getValue("channel:engagement");
				sumEngagement += value;
				cntEngagement++;
			}

			// calculate error
			if(cntSmile != 0){
				avgSmile = sumSmile / cntSmile;
			} else{
				avgSmile = 0;
			}

			if(cntFurrow != 0){
				avgFurrow = sumFurrow / cntFurrow;
			} else{
				avgFurrow = 0;
			}

			if(cntEngagement != 0){
				avgEngagement = sumEngagement / cntEngagement;
			} else{
				avgEngagement = 0;
			}

			String emotion = "";

			if(avgEngagement <= 0.4)
				emotion += "bored";

			else if(avgFurrow >= 0.2)
					emotion += "doubt";

			else if(avgEngagement >= 0.7)
					emotion += "interested";

			else if(avgSmile >= 0.8)
					emotion += "happy";

			else emotion += "neutral";

			emotion += " " + round(avgFurrow) +" "+ round(avgSmile) +" "+ round(avgEngagement);

			return emotion;
    	}
    	return null;
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
