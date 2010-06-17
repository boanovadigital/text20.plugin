/*
 * DashboardUtils.java
 *
 * Copyright (c) 2007, Ralf Biedert, DFKI. All rights reserved.
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
package de.dfki.km.text20.browserplugin.services.onscreenmenu.impl;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

/**
 * A collection some small and usefull dashboard related functions.
 *
 * @author Ralf Biedert
 *
 */
public class DashboardUtilities {

    /**
     * Encapsulates the SwingUtilities function, but without the ugly try-hassle. Returns
     * null if everything went okay, or the exception in case of an exception.
     *
     * @param r
     *                The runnable to perform.
     * @return .
     */
    public static Exception invokeAndWait(final Runnable r) {
        // Check if we already are in the EDT
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
            return null;
        }

        Exception rval = null;

        // SwingUtilities.invokeLater(r);

        // If not, we'll soon be.
        try {
            SwingUtilities.invokeAndWait(r);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            rval = e;
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
            rval = e;
        }

        return rval;
    }

    /**
     * @param runnable
     */
    public static void invokeLater(final Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }
}
