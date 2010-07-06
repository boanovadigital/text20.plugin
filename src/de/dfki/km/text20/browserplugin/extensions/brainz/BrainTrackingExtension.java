/*
 * ScreenShotExtension.java
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
package de.dfki.km.text20.browserplugin.extensions.brainz;

import java.io.FileWriter;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
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

        if (function.equals("brainTrackerCallbackForValues")) {

            // farida start
            //emotionDetector = new EmotionClassifierImpl();
            trainedAvgEmotionClassifier.clearEvents();
            trainedPeakEmotionClassifier.clearEvents();
            simpleAvgEmotionClassifier.clearEvents();
            simplePeakEmotionClassifier.clearEvents();

            // for training
            //String modelname = args.substring(1, args.length()-1);
            //trainingModel = new UserTrainingModel(modelname);
            // farida end

            this.deviceManager.getBrainTrackingDevice().addTrackingListener(new BrainTrackingListener() {

                @SuppressWarnings("boxing")
                @Override
                public void newTrackingEvent(BrainTrackingEvent event) {
                    //emotionDetector.addEvent(event);

                    // Root Mean Square Error Training

                    try {
                        trainedAvgEmotionClassifier.model.addEvent(event);
                        trainedPeakEmotionClassifier.model.addEvent(event);

                        trainedAvgEmotionClassifier.addEvent(event);
                        trainedPeakEmotionClassifier.addEvent(event);
                        simpleAvgEmotionClassifier.addEvent(event);
                        simplePeakEmotionClassifier.addEvent(event);
                    } catch (Exception e) {
                        AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
                            @SuppressWarnings("boxing")
                            public Boolean run() {
                                try {
                                    FileWriter fw = new FileWriter("test.txt", false);
                                    fw.write("sth went wrong :(");
                                    fw.flush();
                                    fw.close();
                                    return true;
                                } catch (IOException ee) {
                                    // TODO Auto-generated catch block
                                    ee.printStackTrace();
                                }
                                return false;
                            }
                        });
                    }

                    // for training
                    //trainingModel.addEvent(event);

                    //String next = event.getChannels().iterator().next();
                    //double value = event.getValue(next);							
                    //BrainTrackingExtension.this.jsExecutor.executeJSFunction("specialCallback","brainTrackerValues", next, value);
                }
            });
            return "More Brainz!";
        }

        if (function.equals("getTrainedAvgEmotion")) {
            // String emotion = args.substring(1, args.length()-1);			
            // trainingModel.addEmotionClass(emotion);
            // trainingModel.generateTrainingFile();
            //return emotionDetector.getEmotion();
            return trainedAvgEmotionClassifier.getEmotion();
        }
        if (function.equals("getTrainedPeakEmotion")) { return trainedPeakEmotionClassifier.getEmotion(); }

        if (function.equals("getSimpleAvgEmotion")) { return simpleAvgEmotionClassifier.getEmotion(); }
        if (function.equals("getSimplePeakEmotion")) { return simplePeakEmotionClassifier.getEmotion(); }

        if (function.equals("startRmseTraining")) {
            trainedAvgEmotionClassifier.model.clearEvents();
            trainedPeakEmotionClassifier.model.clearEvents();
        }
        if (function.equals("stopRmseTraining")) {
            String emotion = args.substring(1, args.length() - 1);
            trainedAvgEmotionClassifier.model.calculateAvgSimple(emotion);
            trainedPeakEmotionClassifier.model.calculateAvgSimple(emotion);

            double size = trainedAvgEmotionClassifier.model.avgSimple.size();
            double avg = trainedAvgEmotionClassifier.model.avgSimple.get(emotion);
            return " avg size : " + size + " avg calculated: " + " avg: " + avg; // for debugging
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
        return new String[] { "brainTrackerCallbackForValues", "getTrainedAvgEmotion", "getTrainedPeakEmotion", "getSimpleAvgEmotion", "getSimplePeakEmotion", "startRmseTraining", "stopRmseTraining" };
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
