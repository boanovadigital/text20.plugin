/*
 * TestGazeEvaluator.java
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
package de.dfki.km.text20.sandbox.misc;

import java.net.URI;
import java.net.URISyntaxException;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluatorManager;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationListener;
import de.dfki.km.text20.services.evaluators.gaze.options.addgazeevaluationlistener.OptionRequestVersion;

/**
 * @author rb
 *
 */
public class TestGazeEvaluator {
    /**
     * @param args
     * @throws URISyntaxException 
     */
    public static void main(final String[] args) throws URISyntaxException {

        PluginManager pm = PluginManagerFactory.createPluginManager();
        pm.addPluginsFrom(new URI("classpath://*"));

        final GazeEvaluatorManager gem = pm.getPlugin(GazeEvaluatorManager.class);
        final GazeEvaluator ge = gem.createEvaluator(null);

        ge.addEvaluationListener(new FixationListener() {

            @Override
            public void newEvaluationEvent(FixationEvent event) {
                System.out.println("You will never see me ...");
            }
        }, new OptionRequestVersion(FixationListener.class, "Ralf Biedert", 130));

        pm.shutdown();
    }
}
