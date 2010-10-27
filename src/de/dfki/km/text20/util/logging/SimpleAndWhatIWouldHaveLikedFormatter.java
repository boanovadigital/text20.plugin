/*
 * SimpleAndWhatIWouldHaveLikedFormatter.java
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
package de.dfki.km.text20.util.logging;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Formats logging messages beautifully ...
 * 
 * @author rb
 */
public class SimpleAndWhatIWouldHaveLikedFormatter extends Formatter {

    @Override
    public String format(final LogRecord record) {
        final StringBuilder sb = new StringBuilder();

        final Level level = record.getLevel();

        // Did I mention that I sometimes (quite frequently) really hate Java?
        if (level.intValue() == Level.SEVERE.intValue()) {
            sb.append("(!SEVERE!)");
        } else if (level.intValue() == Level.WARNING.intValue()) {
            sb.append("(!)");
        } else if (level.intValue() == Level.FINE.intValue()) {
            sb.append("    ");
        } else if (level.intValue() == Level.FINER.intValue()) {
            sb.append("     ");
        } else if (level.intValue() == Level.FINEST.intValue()) {
            sb.append("      ");
        } else {
            sb.append("   ");
        }

        sb.append(record.getMessage());
        sb.append("\n");

        return sb.toString();
    }

}
