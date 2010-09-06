/*
 * IO.java
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
package de.dfki.km.text20.util.system;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rb
 *
 */
public class IO {
    /**
     * @param file
     * 
     * @return .
     */
    public static List<String> readLines(File file) {
        final List<String> rval = new ArrayList<String>();

        try {
            final FileInputStream fstream = new FileInputStream(file);
            final DataInputStream in = new DataInputStream(fstream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                rval.add(strLine);
            }

            //Close the input stream
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rval;
    }

    /**
     * @param file
     * @param lines
     */
    public static void writeLines(File file, List<String> lines) {

        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);

            final PrintStream printStream = new PrintStream(fout);

            for (String string : lines) {
                printStream.print(string);
            }
        } catch (IOException e) {
            System.err.println("Unable to write to file");
            System.exit(-1);
        } finally {
            if (fout == null) return;
            try {
                fout.close();
            } catch (IOException e) {
                // This Java crap is so butt ugly
                e.printStackTrace();
            }
        }
    }
}
