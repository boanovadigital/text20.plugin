/*
 * BrainTrackingExtension.java
 *
 * Copyright (c) 2010, Farida Ismail, DFKI. All rights reserved.
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
 * @author Farida Ismail
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

    /** */
    TrainedAvgEmotionClassifier trainedAvgEmotionClassifier = new TrainedAvgEmotionClassifier();
    TrainedPeakEmotionClassifier trainedPeakEmotionClassifier = new TrainedPeakEmotionClassifier();
    SimpleAvgEmotionClassifier simpleAvgEmotionClassifier = new SimpleAvgEmotionClassifier();
    SimplePeakEmotionClassifier simplePeakEmotionClassifier = new SimplePeakEmotionClassifier();

    /*
     * (non-Javadoc)
     *
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension
     * #executeFunction(java.lang.String, java.lang.String)
     */
    @Override
    @SuppressWarnings("boxing")
    public Object executeDynamicFunction(String function, String args) {

        if (function.equals("brainTrackerInitTraining")) {
            this.deviceManager.getBrainTrackingDevice().addTrackingListener(new BrainTrackingListener() {
                @Override
                public void newTrackingEvent(BrainTrackingEvent event) {
                    BrainTrackingExtension.this.trainedPeakEmotionClassifier.model.addEvent(event);
                }
            });
            return "Brain Tracker Initialized for Training!";
        }

        if (function.equals("brainTrackerInitEvaluation")) {
            this.trainedPeakEmotionClassifier.clearEvents();
            this.deviceManager.getBrainTrackingDevice().addTrackingListener(new BrainTrackingListener() {

                @Override
                public void newTrackingEvent(BrainTrackingEvent event) {
                    BrainTrackingExtension.this.trainedPeakEmotionClassifier.addEvent(event);
                }
            });
            return "Brain Tracker Initialized for Evaluation!";
        }

        if (function.equals("getTrainedAvgEmotion")) { return this.trainedAvgEmotionClassifier.getEmotion(); }

        if (function.equals("getTrainedPeakEmotion")) { return this.trainedPeakEmotionClassifier.getSimpleTrainedEmotion(); }

        if (function.equals("getSimpleAvgEmotion")) { return this.simpleAvgEmotionClassifier.getEmotion(); }

        if (function.equals("getSimplePeakEmotion")) { return this.simplePeakEmotionClassifier.getEmotion(); }

        if (function.equals("startRmseTraining")) {
            this.trainedAvgEmotionClassifier.model.clearEvents();
            this.trainedPeakEmotionClassifier.model.clearEvents();
        }

        if (function.equals("stopRmseTraining")) {
            String emotion = args.substring(1, args.length() - 1);
            this.trainedAvgEmotionClassifier.model.calculateAvgSimple(emotion);
            this.trainedPeakEmotionClassifier.model.calculateAvgSimple(emotion);

            double size = this.trainedPeakEmotionClassifier.model.avgSimple.size();
            double avg = this.trainedPeakEmotionClassifier.model.avgSimple.get(emotion);
            return " avg size : " + size + " avg calculated: " + " avg: " + avg; // for debugging
        }

        if (function.equals("trainValues")) {
            String in = args.substring(1, args.length() - 1);
            String[] sVal = in.split("s");

            this.trainedPeakEmotionClassifier.model.avgSimple.put("happy", Double.parseDouble(sVal[0]));
            this.trainedPeakEmotionClassifier.model.avgSimple.put("interested", Double.parseDouble(sVal[1]));
            this.trainedPeakEmotionClassifier.model.avgSimple.put("doubt", Double.parseDouble(sVal[2]));
            this.trainedPeakEmotionClassifier.model.avgSimple.put("bored", Double.parseDouble(sVal[3]));

            double size = this.trainedPeakEmotionClassifier.model.avgSimple.size();
            double avg = this.trainedPeakEmotionClassifier.model.avgSimple.get("doubt");

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
    @Override
    public String[] getDynamicFunctions() {
        return new String[] { "brainTrackerInitTraining", "brainTrackerInitEvaluation", "getTrainedAvgEmotion", "getTrainedPeakEmotion", "getSimpleAvgEmotion", "getSimplePeakEmotion", "startRmseTraining", "stopRmseTraining", "trainValues" };
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
    @Override
    public void setParameter(SetupParameter parameter, Object value) {
        if (parameter == SetupParameter.JAVASCRIPT_EXECUTOR) {
            this.jsExecutor = (JSExecutor) value;
        }
    }
}
