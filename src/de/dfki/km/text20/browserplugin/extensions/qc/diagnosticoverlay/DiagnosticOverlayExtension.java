/*
 * DiagnosticOverlayImpl.java
 * 
 * Copyright (c) 2011, Ralf Biedert All rights reserved.
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
package de.dfki.km.text20.browserplugin.extensions.qc.diagnosticoverlay;

import static net.jcores.shared.CoreKeeper.$;
import net.jcores.shared.interfaces.functions.F0;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.Init;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.informationbroker.InformationBroker;
import net.xeoh.plugins.informationbroker.InformationListener;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.MasterGazeHandlerItem;
import de.dfki.km.text20.browserplugin.services.extensionmanager.Extension;
import de.dfki.km.text20.browserplugin.services.extensionmanager.annotations.ExtensionMethod;
import de.dfki.km.text20.browserplugin.services.mastergazehandler.MasterGazeHandler;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationEventType;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationListener;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.raw.RawGazeEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.raw.RawGazeListener;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * @author Ralf Biedert
 */
@PluginImplementation
public class DiagnosticOverlayExtension implements Extension {
    /** */
    @InjectPlugin
    public InformationBroker informationBroker;
    
    /** The overlay frame to show */
    OverlayFrame overlayFrame;

    /**
     * Shows the diagnostic overlay.
     */
    @ExtensionMethod
    public void diagnosticOverlay() {
        $.sys.oneTime(new F0() {
            @Override
            public void f() {
                DiagnosticOverlayExtension.this.overlayFrame.activate();
            }
        }, 100);
    }

    /** */
    @Init
    public void init() {
        // Create the overlay which we use for rendering
        this.overlayFrame = new OverlayFrame();
        
        // Register us to the current gaze handler
        this.informationBroker.subscribe(MasterGazeHandlerItem.class, new InformationListener<MasterGazeHandler>() {
            @Override
            public void update(MasterGazeHandler handler) {
                handler.getGazeEvaluator().addEvaluationListener(new RawGazeListener() {

                    @Override
                    public void newEvaluationEvent(RawGazeEvent event) {
                        // Get the current tracking events 
                        final EyeTrackingEvent e = event.getTrackingEvent();
                        DiagnosticOverlayExtension.this.overlayFrame.trackingEvent(e);
                    }

                    @Override
                    public boolean requireUnfilteredEvents() {
                        return false;
                    }
                });
                
                handler.getGazeEvaluator().addEvaluationListener(new FixationListener() {
                    @Override
                    public void newEvaluationEvent(FixationEvent event) {
                        if(event.getType() != FixationEventType.FIXATION_START) return;
                        DiagnosticOverlayExtension.this.overlayFrame.fixation(event.getFixation());
                    }
                });
            }
        });
    }
}
