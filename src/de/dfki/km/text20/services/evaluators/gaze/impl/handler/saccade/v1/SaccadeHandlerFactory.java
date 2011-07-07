/*
 * SaccadeHandlerFactory.java
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
package de.dfki.km.text20.services.evaluators.gaze.impl.handler.saccade.v1;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.annotations.Capabilities;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.base.annotations.meta.Author;
import net.xeoh.plugins.base.annotations.meta.Version;
import net.xeoh.plugins.base.util.OptionUtils;
import de.dfki.km.text20.services.evaluators.common.options.SpawnEvaluatorOption;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluationListener;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.GazeHandler;
import de.dfki.km.text20.services.evaluators.gaze.GazeHandlerFactory;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.SaccadeListener;
import de.dfki.km.text20.services.evaluators.gaze.options.AddGazeEvaluationListenerOption;
import de.dfki.km.text20.services.evaluators.gaze.options.spawnevaluator.OptionGazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.options.spawnevaluator.OptionGazeEvaluatorPassthrough;

/**
 * @author Ralf Biedert
 *
 */
@PluginImplementation
@Version(version = 10000)
@Author(name = "Ralf Biedert")
public class SaccadeHandlerFactory implements GazeHandlerFactory {

    /** */
    @InjectPlugin
    public PluginManager pluginManager;

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.gazeevaluator.GazeHandlerCreator#getEvaluatorType()
     */
    @Override
    public Class<? extends GazeEvaluationListener<?>> getEvaluatorType() {
        return SaccadeListener.class;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.gazeevaluator.GazeHandlerCreator#spawnEvaluator()
     */
    @Override
    public GazeHandler spawnEvaluator(GazeEvaluationListener<?> listener,
                                      SpawnEvaluatorOption... options) {

        // Handle options
        final OptionUtils<SpawnEvaluatorOption> ou = new OptionUtils<SpawnEvaluatorOption>(options);
        final AddGazeEvaluationListenerOption[] passThrough = ou.get(OptionGazeEvaluatorPassthrough.class).getOptions();
        final GazeEvaluator gazeEvaluator = ou.get(OptionGazeEvaluator.class).getGazeEvaluator();

        // Create the element
        final SaccadeHandler saccadeHandler = new SaccadeHandler((SaccadeListener) listener, passThrough);

        // Perform some setup
        saccadeHandler.setPluginManager(this.pluginManager);
        saccadeHandler.setGazeEvaluator(gazeEvaluator);
        saccadeHandler.init(passThrough);

        // Return it‚
        return saccadeHandler;
    }

    /**
     * @return .
     */
    @Capabilities
    public String[] getCapabilities() {
        return new String[] { "meta:status:maturity:release" };
    }

}
