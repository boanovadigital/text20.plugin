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
