/*
 * SessionMerger.java
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
package de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.plainxml;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;

/**
 * @author Ralf Biedert
 *
 */
public class SessionMerger {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {

        final String f1 = readFileAsString("1.xml");
        final String f2 = readFileAsString("2.xml");

        System.out.println(merge(f1, f2));
    }

    /**
     * @param f
     * @return .
     */
    public static String merge(final String... f) {
        String rval = f[0];
        for (int i = 1; i < f.length; i++) {
            rval = merge(rval, f[i]);
        }

        return rval;
    }

    /**
     * @param filePath
     * @return .
     */
    public static String readFileAsString(final String filePath) {
        try {
            final StringBuffer fileData = new StringBuffer(1000);
            BufferedReader reader;

            reader = new BufferedReader(new FileReader(filePath));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                final String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }
            reader.close();
            return fileData.toString();
        } catch (final FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private static String merge(final String f1, final String f2) {
        if (f1.length() <= 1) return f2;
        if (f2.length() <= 1) return f1;

        final int i1 = f2.indexOf("<allEvents");
        final int i11 = f2.indexOf(">", i1);
        final int i2 = f2.indexOf("</allEvents>");

        final String substring = f2.substring(i1 + i11 - i1 + 1, i2 + 12);

        // Make target string safe for replacement (otherwise some logs will crash)
        return f1.replaceAll("</allEvents>", Matcher.quoteReplacement(substring));
    }

}