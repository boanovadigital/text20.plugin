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
    	if(!events.isEmpty()){    		
    		
    		// sum of channels to be used
    		double sumSmile = 0, sumFurrow = 0, sumEngagement = 0;
    		
    		// count of events taken into consideration for each channel (e.g. when smile is zero it is not included for its calculations)
    		double cntSmile = 0, cntFurrow = 0, cntEngagement = 0;
    		
    		// average of each channel
    		double avgSmile, avgFurrow, avgEngagement;
    		
    		
	    	ArrayList<BrainTrackingEvent> currEvents;
	    	
	    	// retrieve events and clear for coming events
	    	synchronized (events) {
	    		currEvents = (ArrayList<BrainTrackingEvent>)events.clone();
	    		events.clear();
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
				
			
			if(avgEngagement <= 0.4)
				return "bored";
			
			if(avgFurrow >= 0.2)
				return "doubt";
			
			if(avgEngagement >= 0.7)
				return "interested";

			if(avgSmile >= 0.8)
				return "happy";
				
			return "neutral";
    	}
    	return null;
    }
    
    /**
     * Adds an event to the list
     * 
     * @param event the new event to be added to the list
     */
    public void addEvent(BrainTrackingEvent event){
    	synchronized (events) {
    		events.add(event);
		}
    }
    
    /** Clears event list */
    public void clearEvents(){
    	synchronized (events) {
    		events.clear();
		}
    }
}
