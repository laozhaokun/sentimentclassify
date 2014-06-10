package cn.edu.thu.ml.perceptron;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhf
 * @email zhf.thu@gmail.com
 * @version 创建时间：2014年6月1日 上午9:31:53 
 * Class that represents a Perceptron with a
 * weight vector, and a number of epochs and a learning rate for
 * training
 */
public class Perceptron {
	private HashMap<String, Double> weights = new HashMap<String, Double>();
	private int epochs;
	private String learningRate;

	public Perceptron(int epochs, String learningRate) {
		this.epochs = epochs;
		this.learningRate = learningRate;
	}

	public Perceptron(String learningRate) {
		this(1, learningRate);
	}

	public Perceptron(HashMap<String, Double> weights) {
		this.weights = weights;
	}

	public Perceptron() {
		this(10, "-2");
	}

	public static double dotProduct(Map<String, Integer> m1,
			Map<String, Double> m2) {
		double result = 0.0;
		for (String key1 : m1.keySet()) {
			double value2 = 0.0;
			if (m2.containsKey(key1))
				value2 = m2.get(key1);
			result += m1.get(key1) * value2;
		}
		return result;
	}
	
	public HashMap<String,Double> train(List<Instance> trainset){
		//for each epoch
		for (int t=1; t<=this.epochs; t++){			
			double currentLearningRate = 0; 
			 if (Double.parseDouble(this.learningRate)>=-10 || Double.parseDouble(this.learningRate)<=10   ){
				currentLearningRate = Math.pow(10,Double.parseDouble(this.learningRate));
			}else{
				System.err.println("learningRate = " +this.learningRate  + " is invalide.");
				break;
			}
						
			for (Instance i : trainset){
				//if misclassified, update with gradient
				if (this.misclassified(i)){
					//update weights
					for (String feature : i.getFeatures()){
						Double featureValue = new Double("0.0");
						//if feature can be found in current weights
						if (this.weights.containsKey(feature)){
							featureValue = this.weights.get(feature);
						}
						//update weight
						this.weights.put(feature, featureValue+(currentLearningRate*i.getFeatureVector().get(feature)*i.getLabel()));
					}
				}
			}
		}
		return this.weights;
	}
	
	/**
	 * checks whether an instance is misclassified by trained Perceptron or not
	 * @param i test instance
	 * @return true or false
	 */
	public boolean misclassified(Instance i){
		if ((dotProduct(i.getFeatureVector(),this.weights))*i.getLabel()<=0){
			return true;
		}
		else{
			return false;
		}
	}

	public void saveWeightsToFile(String file) {

		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(new File(file)));
			for (String key : this.weights.keySet()) {
				out.append(key + ":" + this.weights.get(key).toString() + " ");
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * test the Perceptron instance on a given test set
	 * @param testset must be an Array of Instances
	 * @return error rate (double), i.e. #correctly_classified_samples / #all_samples
	 */
	public double test(List<Instance> testset){
		int errors = 0;
		double errorRate = 0.0;
		for (Instance i : testset){
			if (this.misclassified(i)){
				errors ++;
			}
		}
		errorRate = errors/(double) testset.size();
		return errorRate;
	}
	

	public HashMap<String, Double> getWeights() {
		return weights;
	}

	public void setWeights(HashMap<String, Double> weights) {
		this.weights = weights;
	}

	public int getEpochs() {
		return epochs;
	}

	public void setEpochs(int epochs) {
		this.epochs = epochs;
	}

	public String getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(String learningRate) {
		this.learningRate = learningRate;
	}

}
