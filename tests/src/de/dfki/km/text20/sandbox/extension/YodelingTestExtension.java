/*
 * YodelingTestExtension.java
 *
 * Copyright (c) 2011, Ralf Biedert, DFKI. All rights reserved.
 *
 * Public Domain.
 */
package de.dfki.km.text20.sandbox.extension;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.Timer;
import net.xeoh.plugins.base.annotations.events.Init;
import de.dfki.km.text20.browserplugin.services.extensionmanager.Extension;

/**
 * This is a very simple test for you to see how to write an extension.
 * 
 * Simply export this class (and whatever else is needed) as a JAR,
 * and add it as an extension (see 'Simple Test.html'). 
 * 
 * @author Ralf Biedert
 */
@PluginImplementation
// @Suppress(listOfPluginsToSuppress ) <-- Does that make sense?
public class YodelingTestExtension implements Extension {

    /**
     * Called when the extension was loaded. Do you init-stuff in there (the annotation is 
     * important, and that the method is public, not its name!)
     */
    @Init
    public void init() {
        System.out.println("Yodeling Succeded");
    }

    /**
     * Override this method, it will be executed when some JavaScript code calls you. 
     * 
     * @param function Contains the function to call
     * @param args Contains all arguments
     * 
     * @return Something you wish to return to JavaScript.
     */
    @Override
    public Object executeDynamicFunction(String function, String args) {
        return "YODEL WORLD";
    }

    
    /**
     * You MUST return all function names executeDynamicFunction() can handle. 
     */
    @Override
    public String[] getDynamicFunctions() {
        return new String[] { "YODEL" };
    }

    
    /**
     * Can be safely removed :-) 
     */
    @Timer(period=1000)
    public void nag() {
        System.out.println("Yodeling extension reports: I'm still there ...");
    }
}
