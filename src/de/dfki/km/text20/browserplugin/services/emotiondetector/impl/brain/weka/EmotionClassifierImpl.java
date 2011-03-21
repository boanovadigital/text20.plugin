/*
 * EmotionClassifierImpl.java
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

import de.dfki.km.text20.browserplugin.services.emotiondetector.EmotionClassifier;

/** Not yet complete... */
public class EmotionClassifierImpl implements EmotionClassifier {

	/** User specific training model */
	public UserTrainingModel model;

	/** Training data */
	//public Instances trainData;

	/** The classifier to be used for classification */
	//public LibSVM classifier;

	@Override
	public String getEmotion() {
		// TODO Auto-generated method stub
		return null;
	}

	/** Initializes and trains the classifier */
	public void train(){
	/*	// read training data from arff file
		try {
			BufferedReader reader = new BufferedReader(new FileReader("Training/"+model.modelname+".arff"));
			trainData = new Instances(reader);
			reader.close();
			// setting class attribute
			trainData.setClassIndex(trainData.numAttributes() - 1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// instantiate and build classifier
		classifier = new LibSVM();
		try {
			classifier.buildClassifier(trainData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	*/
	}

}
