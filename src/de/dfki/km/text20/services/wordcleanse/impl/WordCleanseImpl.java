/*
 * WordCleanseImpl.java
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
package de.dfki.km.text20.services.wordcleanse.impl;

import java.util.Arrays;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.Init;
import de.dfki.km.text20.services.wordcleanse.WordCleanse;

/**
 * @author rb
 */
@PluginImplementation
public class WordCleanseImpl implements WordCleanse {

    /** Characters we want to remove */
    final char punctuationList[] = { '.', ',', '\n', '\t', '\r', '!', '?', ';', '"', '\'', '(', ')' };

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.sandbox.services.wordcleanse.WordCleanse#cleanse(java.lang.String)
     */
    @Override
    public String cleanse(final String word) {
        String rval = word.trim();

        // Cut beginning
        while (isPunctuation(rval.charAt(0))) {
            rval = rval.substring(1);
        }

        // Cut beginning
        while (isPunctuation(rval.charAt(rval.length() - 1))) {
            rval = rval.substring(0, rval.length() - 1);
        }

        return rval;

    }

    /** */
    @Init
    public void init() {
        Arrays.sort(this.punctuationList);
    }

    /**
     * @param word
     *
     * @return .
     */
    @Override
    public boolean isPunctuation(final String word) {
        for (int i = 0; i < word.length(); i++) {
            if (!isPunctuation(word.charAt(i))) return false;
        }
        return true;
    }

    private boolean isPunctuation(final char c) {
        if (Arrays.binarySearch(this.punctuationList, c) < 0) return false;
        return true;
    }
}
