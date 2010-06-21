/*
 * SimpleAndWhatIWouldHaveLikedFormatter.java
 *
 * Copyright (c) 2008, Ralf Biedert, DFKI. All rights reserved.
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
