package de.dfki.km.text20.browserplugin.services.emotiondetector.impl.brain.simple;

import java.util.ArrayList;

import de.dfki.km.text20.browserplugin.services.emotiondetector.EmotionClassifier;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;

public class SimplePeakEmotionClassifier implements EmotionClassifier {
    /** Contains the brain tracking events to be evaluated for the next emotion */
    private ArrayList<BrainTrackingEvent> events = new ArrayList<BrainTrackingEvent>();

    
	@SuppressWarnings("unchecked")
	@Override
    public String getEmotion() {
    	if(!events.isEmpty()){    		
	    	ArrayList<BrainTrackingEvent> currEvents;
	    	
	    	// retrieve events and clear for coming events
	    	synchronized (events) {
	    		currEvents = (ArrayList<BrainTrackingEvent>)events.clone();
	    		events.clear();
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
	    	
	    	if(avg <= 0.4)
	    		bored = true;
	    	else if(avg >= 0.7)
	    			interested = true;
	    	
	    	// doubt or happy (have peaks)
	    	
	    	double peakFurrow = 0;
	    	double peakSmile = 0;
	    	
	    	for(BrainTrackingEvent b : currEvents){
	    		if(b.getValue("channel:furrow") >= 0.2){
	    			doubt = true;
	    			peakFurrow = b.getValue("channel:furrow") > peakFurrow ? b.getValue("channel:furrow") : peakFurrow;
	    		}
	    		else if(b.getValue("channel:smile") >= 0.8){
	    			happy = true;
	    			peakSmile = b.getValue("channel:smile") > peakSmile ? b.getValue("channel:smile") : peakSmile;
	    		}
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
    
    private double round(double num){
    	return ((double)Math.round(num*100))/100.0;
    }
}
