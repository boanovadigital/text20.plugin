/*
 * ARFFGenerator.java
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
package de.dfki.km.text20.browserplugin.services.emotiondetector.impl.brain.weka;

import java.io.FileWriter;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;

/*
 * Generates ARFF Files for training
 */
public class ARFFGenerator {

	public static String generateFile(final String fileName, ArrayList<EmotionClass> classes){
		final StringBuilder s = new StringBuilder();

		// build header
		s.append("@RELATION emotions\n");

		// header attributes
		if(!classes.get(0).data.isEmpty()){
			for(int i=0; i<classes.get(0).data.get(0).length*FeatureVectorGenerator.WINDOWSIZE; i++)
				s.append("@ATTRIBUTE f"+i+" NUMERIC\n");
		}
		// header class
		s.append("@ATTRIBUTE class {");
		for(int i=0; i<classes.size(); i++){
			s.append(classes.get(i).emotion);
			if(i==classes.size()-1)
				s.append("}\n");
			else
				s.append(",");
		}
		// data
		s.append("@DATA\n");

		// go through all classes and add feature vectors to string
		for(EmotionClass e : classes){
			if(!e.data.isEmpty()){
				ArrayList<double[]> featureVectors = FeatureVectorGenerator.getFeatureVectors(e.data);
				for(double [] instance : featureVectors){
					for(double value : instance)
						s.append(value+", ");
					s.append(e.emotion+"\n");
				}
			}
		}

		// write to file
		try {
			AccessController.doPrivileged(new PrivilegedAction<Boolean>() {

                @Override
                @SuppressWarnings("boxing")
                public Boolean run() {
                    try {
                    	FileWriter fw = new FileWriter(fileName, false);
                    	fw.write(s.toString());
            			fw.flush();
            			fw.close();
						return true;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return false;
                }
            });

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "finished";
	}

}
