/*
 * AePlayWave.java
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
package de.dfki.km.text20.util.rd3party.sound;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author http://www.anyexample.com/programming/java/java_play_wav_sound_file.xml
 *
 */
public class AePlayWave extends Thread {

    enum Position {
        LEFT, NORMAL, RIGHT
    }

    private final static int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb

    private final Position curPosition;

    private final InputStream inputStream;

    /**
     * @param resourceAsStream
     */
    public AePlayWave(final InputStream resourceAsStream) {
        this(resourceAsStream, Position.NORMAL);
    }

    /**
     * @param fis
     * @param p
     */
    public AePlayWave(final InputStream fis, final Position p) {
        this.inputStream = fis;
        this.curPosition = p;
    }

    /**
     * @param wavfile
     * @throws FileNotFoundException 
     */
    public AePlayWave(final String wavfile) throws FileNotFoundException {
        this(wavfile, Position.NORMAL);
    }

    /**
     * @param wavfile
     * @param p
     * @throws FileNotFoundException 
     */
    public AePlayWave(final String wavfile, final Position p)
                                                             throws FileNotFoundException {
        this(new FileInputStream(wavfile), p);
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */

    @Override
    public void run() {

        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(this.inputStream);
        } catch (final UnsupportedAudioFileException e1) {
            e1.printStackTrace();
            return;
        } catch (final IOException e1) {
            e1.printStackTrace();
            return;
        }

        final AudioFormat format = audioInputStream.getFormat();
        SourceDataLine auline = null;
        final DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (final LineUnavailableException e) {
            e.printStackTrace();
            return;
        } catch (final Exception e) {
            e.printStackTrace();
            return;
        }

        if (auline.isControlSupported(FloatControl.Type.PAN)) {
            final FloatControl pan = (FloatControl) auline.getControl(FloatControl.Type.PAN);
            if (this.curPosition == Position.RIGHT) {
                pan.setValue(1.0f);
            } else if (this.curPosition == Position.LEFT) {
                pan.setValue(-1.0f);
            }
        }

        auline.start();
        int nBytesRead = 0;
        final byte[] abData = new byte[AePlayWave.EXTERNAL_BUFFER_SIZE];

        try {
            while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) {
                    auline.write(abData, 0, nBytesRead);
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
            return;
        } finally {
            auline.drain();
            auline.close();
        }

    }
}
