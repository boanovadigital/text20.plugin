/*
 * TestSerialization.java
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
package de.dfki.km.text20.sandbox.misc;

import static net.jcores.CoreKeeper.$;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author rb
 * 
 */
public class TestOptions {

    /**
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static void main(final String[] args) throws UnsupportedEncodingException {
        Map<String, String> hashmap = $("clusters=1&autoupdateAttributed=true&fixation%5BminimumDuration%5D=100&fixation%5Bmaxfixationradius%5D=50").decode().split("&").hashmap();
        System.out.println(hashmap);
    }
}
