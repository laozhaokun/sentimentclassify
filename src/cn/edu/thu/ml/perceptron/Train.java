package cn.edu.thu.ml.perceptron;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.edu.thu.ml.svm.CreateVector;

/**
 * @author zhf
 * @email zhf.thu@gmail.com
 * @version 创建时间：2014年6月1日 上午9:47:58
 * This class is used to train the datasets and evaluate the trained result.
 */
public class Train {
	static String[] epochs = { "1", "10", "100" };
	static String[] learningRates = {"3", "2", "1", "0", "-1" ,"-2","-3"};
	static String name = "all";
	static PrintStream stdout = System.out;

	public static void train() throws IOException {
			// read training set
			List<Instance> trainset = CretateVector
					.createVector("data\\" + name + ".train");
			if (trainset == null) {
				System.err
						.println("Corpus file could not be read. Training continues with next given file.");
				continue;
			}
			// for each parameter setting, train a perceptron and save the trained weight vector
			for (String epoch : epochs) {
				int epochInt = Integer.parseInt(epoch);
				for (String learningRate : learningRates) {

					Perceptron p = new Perceptron(epochInt, learningRate);
					p.train(trainset);
					p.saveWeightsToFile("output\\perceptron/weightVectors/trained_" + name
									+ "_" + epoch + "_" + learningRate + ".txt");
					System.out.println("generated trained_"+ name + "_" + epoch + "_" + learningRate + ".txt");
				}
			}
	}

	
	public static HashMap<String, Double> weightVectorFromFile (File f) {
		String line = new String();
		HashMap<String, Double> weightVector = new HashMap<String, Double>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			line = br.readLine();
			if (line==null){
				System.err.println("Weight vector file is empty: "+f.toString());
				return null;
			}
			br.close();
			String[] arrayWithFeatures = line.split(" ");
			for (String feature : arrayWithFeatures) {
				String[] featureAndValue = feature.split(":");
				String key = featureAndValue[0];
				Double value = Double.parseDouble(featureAndValue[1]);
				weightVector.put(key, value);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Weight vector file not found: "+f.toString());
			weightVector = null;
		} catch (IOException e) {
			e.printStackTrace();
			weightVector = null;
		}
		
		return weightVector;
	}

	public static void main(String[] args) throws IOException {
//		long startTime = new Date().getTime();
//		System.out.println("Start training.");
//	 	train();
//		System.out.println("Finished training.");
//		long endTime = new Date().getTime();
//		System.out.println("It used " + (endTime - startTime)/1000 + " seconds to train.");
	}
}
