/*
 * AbstractHandlerFactory.java
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
package de.dfki.km.text20.services.evaluators.gaze.util.handler;

import static net.jcores.jre.CoreKeeper.$;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.annotations.Capabilities;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.base.util.OptionUtils;
import de.dfki.km.text20.services.evaluators.common.options.SpawnEvaluatorOption;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluationEvent;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluationListener;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.GazeHandler;
import de.dfki.km.text20.services.evaluators.gaze.GazeHandlerFactory;
import de.dfki.km.text20.services.evaluators.gaze.options.AddGazeEvaluationListenerOption;
import de.dfki.km.text20.services.evaluators.gaze.options.spawnevaluator.OptionGazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.options.spawnevaluator.OptionGazeEvaluatorPassthrough;

/**
 * Abstract base class for all handler factories.
 * 
 * @author Ralf Biedert
 * @since 1.4
 */
public abstract class AbstractGazeHandlerFactory implements GazeHandlerFactory {

    /** */
    @InjectPlugin
    public PluginManager pluginManager;

    /** Our listener type */
    private Class<? extends GazeEvaluationListener<? extends GazeEvaluationEvent>> listener;

    /** Our handler */
    @SuppressWarnings("rawtypes")
    private Class<? extends AbstractGazeHandler> handler;

    /**
     * Construct a factory with the given handlers
     * 
     * @param type
     * @param handler
     */
    @SuppressWarnings("rawtypes")
    public AbstractGazeHandlerFactory(Class<? extends GazeEvaluationListener<? extends GazeEvaluationEvent>> type,
                                  Class<? extends AbstractGazeHandler> handler) {
        this.listener = type;
        this.handler = handler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.evaluators.common.HandlerFactory#getEvaluatorType()
     */
    @Override
    public Class<? extends GazeEvaluationListener<? extends GazeEvaluationEvent>> getEvaluatorType() {
        return this.listener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.text20.services.evaluators.common.HandlerFactory#spawnEvaluator(de.dfki.km.text20.services.evaluators
     * .common.EvaluationListener, de.dfki.km.text20.services.evaluators.common.options.SpawnEvaluatorOption[])
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public GazeHandler spawnEvaluator(GazeEvaluationListener<? extends GazeEvaluationEvent> l,
                                      SpawnEvaluatorOption... options) {

        // Handle options
        final OptionUtils<SpawnEvaluatorOption> ou = new OptionUtils<SpawnEvaluatorOption>(options);
        final AddGazeEvaluationListenerOption[] passThrough = ou.get(OptionGazeEvaluatorPassthrough.class).getOptions();
        final GazeEvaluator gazeEvaluator = ou.get(OptionGazeEvaluator.class).getGazeEvaluator();

        final AbstractGazeHandler h = $(this.handler).spawn().get(0);

        // Perform some setup
        h.attachedListener = l;
        h.options = passThrough;
        h.pluginManager = this.pluginManager;
        h.gazeEvaluator = gazeEvaluator;
        h.init();

        // Return it‚
        return h;
    }

    /**
     * @return .
     */
    @Capabilities
    public String[] getCapabilities() {
        return new String[] { "meta:status:maturity:release" };
    }
}
