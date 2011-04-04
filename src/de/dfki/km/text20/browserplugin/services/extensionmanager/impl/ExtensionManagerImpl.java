/*
 * ExtensionManagerImpl.java
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
package de.dfki.km.text20.browserplugin.services.extensionmanager.impl;

import static net.jcores.CoreKeeper.$;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import net.jcores.interfaces.functions.F1;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.PluginLoaded;
import de.dfki.km.text20.browserplugin.services.extensionmanager.DynamicExtension;
import de.dfki.km.text20.browserplugin.services.extensionmanager.Extension;
import de.dfki.km.text20.browserplugin.services.extensionmanager.ExtensionManager;
import de.dfki.km.text20.browserplugin.services.extensionmanager.annotations.ExtensionMethod;

/**
 * @author rb
 */
@PluginImplementation
public class ExtensionManagerImpl implements ExtensionManager {

    /** */
    final Logger logger = Logger.getLogger(this.getClass().getName());

    /** */
    private final List<Extension> allKnownExtensions = new ArrayList<Extension>();

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.ExtensionManager
     * #executeFunction(java.lang.String, java.lang.String)
     */
    @Override
    public Object executeFunction(String function, String args) {
        // Arguments (first, remove global () , then remove '' on each param).
        String[] split = $($(args).replace("\\(([^)]*)\\)", "$1").get(0).split(",")).replace("'([^']*)'", "$1").decode().array(String.class);
        if(split.length == 1 && split[0].equals("")) split = new String[0];
        
        // TODO: Improve the loop, hash the functions instead..
        for (Extension e : this.allKnownExtensions) {
            // First check if the extension was declared in a normal way, in that case, just execute the function
            if (e instanceof DynamicExtension) {
                DynamicExtension ee = (DynamicExtension) e;
                final List<String> supported = Arrays.asList(ee.getDynamicFunctions());
                if (supported.contains(function)) { return ee.executeDynamicFunction(function, args); }
            }
            
            // We really want to make sure this code does not break the normal extension handling ...
            try {
                // Next, check if there is a suitable method
                final Method[] methods = e.getClass().getMethods();
                
                // Check all methods if there is something matching
                for (Method method : methods) {
                    try {
                        
                        if(!method.getName().equals(function)) continue;
                        
                        // Make sure the number of args match
                        final Class<?>[] params = method.getParameterTypes();
                        if(params.length != split.length) continue;
                        
                        // Check it the method has an annotation
                        if(method.getAnnotation(ExtensionMethod.class) == null) continue;
                        
                        // Try to cast the arguments
                        final Object[] cast = new Object[params.length];
                        for (int i = 0; i < cast.length; i++) {
                            final Class<?> type = params[i];
                            
                            if(type.equals(int.class) || type.equals(Integer.class)) {
                                cast[i] = Integer.decode(split[i]);
                            }
                            
                            if(type.equals(byte.class) || type.equals(Byte.class)) {
                                cast[i] = Byte.decode(split[i]);
                            }
                            
                            if(type.equals(short.class) || type.equals(Short.class)) {
                                cast[i] = Short.decode(split[i]);
                            }
                            
                            if(type.equals(long.class) || type.equals(Long.class)) {
                                cast[i] = Long.decode(split[i]);
                            }
                            
                            if(type.equals(boolean.class) || type.equals(Boolean.class)) {
                                cast[i] = Boolean.valueOf(split[i]);
                            }
                            
                            if(type.equals(float.class) || type.equals(Float.class)) {
                                cast[i] = Float.valueOf(split[i]);
                            }
                            
                            if(type.equals(double.class) || type.equals(Double.class)) {
                                cast[i] = Double.valueOf(split[i]);
                            }
                            
                            if(type.equals(String.class)) {
                                cast[i] = split[i];
                            }
                        }
                        
                        // Eventually, try to invoke the method
                        try {
                            return method.invoke(e, cast);
                        } catch (IllegalArgumentException e1) {
                            e1.printStackTrace();
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        } catch (InvocationTargetException e1) {
                            e1.printStackTrace();
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.ExtensionManager
     * #getExtensions()
     */
    @Override
    public List<String> getExtensions() {
        final List<String> rval = new ArrayList<String>();

        for (Extension e : this.allKnownExtensions) {
            if (e instanceof DynamicExtension) {
                DynamicExtension ee = (DynamicExtension) e;
                final List<String> supported = Arrays.asList(ee.getDynamicFunctions());
                rval.addAll(supported);
            }

            // Get all methods declared by annotations
            final List<String> byAnnotation = $(e.getClass().getMethods()).map(new F1<Method, String>() {
                @Override
                public String f(Method arg0) {
                   ExtensionMethod method = arg0.getAnnotation(ExtensionMethod.class);
                   if(method == null) return null;
                   
                   return arg0.getName();
                }
            }).compact().list();
            rval.addAll(byAnnotation);
        }

        return rval;
    }

    /**
     * @param extension
     */
    @PluginLoaded
    public void newExtension(Extension extension) {
        this.allKnownExtensions.add(extension);
    }
}
