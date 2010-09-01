/*
 * TextFilter.java
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
package de.dfki.km.text20.util.language;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * Some functions about text processing.
 * 
 * @deprecated Is now in augmented text services project
 * @author rb
 *
 */
@Deprecated
public class TextProcessing {

    /** Characters we want to remove */
    final static char punctuationList[] = { '.', ',', '\n', '\t', '\r', '!', '?', ';', '"', '\'', '(', ')', '{', '}', '-', '!', '?', '\'', ' ', ':', '[', ']', '_' };

    /** Sort the punctuation list */
    static {
        Arrays.sort(punctuationList);
    }

    /**
     * 
     * Gets all words from a text
     * 
     * @param text
     * @return Array of words
     */
    public static String[] getWords(final String text) {

        final ArrayList<String> result = new ArrayList<String>();
        final StringBuilder textBuilder = new StringBuilder(text);
        final StringBuilder tempBuilder = new StringBuilder();

        for (int i = 0; i < textBuilder.length(); i++) {
            final char c = textBuilder.charAt(i);
            if (!isPunctuation(c)) {
                tempBuilder.append(c);
            } else {
                if (tempBuilder.length() > 0) {
                    result.add(tempBuilder.toString());
                }
                tempBuilder.delete(0, tempBuilder.length());
            }
        }
        if (tempBuilder.length() > 0) {
            result.add(tempBuilder.toString());
        }

        final String[] temp = new String[result.size()];
        System.arraycopy(result.toArray(), 0, temp, 0, result.size());

        return temp;
    }

    /**
     * @param word
     * 
     * @return .
     */
    public static boolean isPunctuation(final String word) {
        for (int i = 0; i < word.length(); i++) {
            if (!isPunctuation(word.charAt(i))) return false;
        }
        return true;
    }

    private static boolean isPunctuation(final char c) {
        if (Arrays.binarySearch(punctuationList, c) < 0) return false;
        return true;
    }
}
