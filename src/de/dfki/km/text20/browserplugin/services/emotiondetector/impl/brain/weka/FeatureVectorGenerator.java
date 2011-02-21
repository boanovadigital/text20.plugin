/*
 * FeatureVectorGenerator.java
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

import java.util.ArrayList;

public class FeatureVectorGenerator {
	/** Size of the window when generating feature vectors */
	final public static int WINDOWSIZE = 20;

	/** Step number between subsequent windows when generating feature vectors */
	final public static int STEP = 2;

	/**
	 * Generates feature vectors from the data provided using the given the window and step size
	 *
	 * @param data
	 * @return Feature vectors
	 */
	public static ArrayList<double []> getFeatureVectors(ArrayList<double []> data){
		ArrayList<double []> fvectors = new ArrayList<double[]>();

		// goes through the data with the given step size
		for(int i=0; data.size()>WINDOWSIZE+i; i+=STEP){

			//the feature vector to be filled
			double [] v = new double[data.get(i).length*WINDOWSIZE];

			// goes through all the attributes of the instances
			for(int j=0; j<data.get(i).length; j++)

				// goes through the instances within the current window
				for(int k=0; k<WINDOWSIZE; k++)
					v[k+j*WINDOWSIZE] = data.get(i+k)[j];
			fvectors.add(v);
		}
		return fvectors;
	}
}
