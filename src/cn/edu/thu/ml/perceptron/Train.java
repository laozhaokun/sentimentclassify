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
	static String[] setNames = {"all","book", "electronic", "dvd", "kitchen"};
	static PrintStream stdout = System.out;

	public static void train() throws IOException {
		for (String name : setNames) {
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
	}

	public static void evaluate() throws IOException{
		for(String epoch : epochs){
			File file = new File("output/perceptron/test_"+epoch);
			file.createNewFile();
			System.err.println("-----------------" + epoch+" epochs\n");
			for (String learningRate : learningRates){
				System.out.println("-----------------------------------");
				System.out.println("learning rate: "+learningRate+"\n");
				
				//these are the trained-original pairs we want to inspect:
				
				//read trained weight vectors from files
				HashMap<String, Double> weightVectorAll = weightVectorFromFile(new File(String.format("output\\perceptron/weightVectors//trained_%s_%s_%s.txt","book", epoch, learningRate)));
				HashMap<String, Double> weightVectorBooks = weightVectorFromFile(new File(String.format("output\\perceptron/weightVectors/trained_%s_%s_%s.txt","book", epoch, learningRate)));
				HashMap<String, Double> weightVectorDvd = weightVectorFromFile(new File(String.format("output\\perceptron/weightVectors/trained_%s_%s_%s.txt","dvd", epoch, learningRate)));
				HashMap<String, Double> weightVectorElectronics = weightVectorFromFile(new File(String.format("output\\perceptron/weightVectors/trained_%s_%s_%s.txt","electronic", epoch, learningRate)));
				HashMap<String, Double> weightVectorKitchen = weightVectorFromFile(new File(String.format("output\\perceptron/weightVectors/trained_%s_%s_%s.txt","kitchen", epoch, learningRate)));

				//if weight vector file could not be found or is empty
				if (weightVectorAll == null || weightVectorBooks == null || weightVectorDvd == null || weightVectorElectronics == null || weightVectorKitchen == null ){
					continue;
				}
				
				
				//read original instances
				List<Instance> oriSetBooks = CretateVector.createVector("data/book.train");
				List<Instance> oriSetDvd = CretateVector.createVector("data/dvd.train");
				List<Instance> oriSetElectronics = CretateVector.createVector("data/electronic.train");
				List<Instance> oriSetKitchen = CretateVector.createVector("data/kitchen.train");

				double d1 = new Perceptron(weightVectorBooks).test(oriSetBooks);
				System.out.println("books on books:\t"+d1);
				double d2 = new Perceptron(weightVectorDvd).test(oriSetDvd);
				System.out.println("dvd on dvd:\t"+d2);
				double d3 = new Perceptron(weightVectorElectronics).test(oriSetElectronics);
				System.out.println("electronics on electronics:\t"+d3);
				double d4 = new Perceptron(weightVectorKitchen).test(oriSetKitchen);
				System.out.println("kitchen on kitchen:\t"+d4);
				double avgCatOnCat = (d1+d2+d3+d4)/4;
				System.out.println("cat on cat:\t"+avgCatOnCat+"\n");

				Perceptron p = new Perceptron(weightVectorAll);
				
				double f1 = p.test(oriSetBooks);
				double f2 = p.test(oriSetDvd);
				double f3 = p.test(oriSetElectronics);
				double f4 = p.test(oriSetKitchen);
			
				double avg = (f1+f2+f3+f4)/4;
				
				System.out.println("all on books:\t"+f1);
				System.out.println("all on dvd:\t"+f2);
				System.out.println("all on electronics:\t"+f3);
				System.out.println("all on kitchen:\t"+f4);
				System.out.println("avg:\t"+avg);
			}
			System.setOut(stdout); //reset out path
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
		//
		long startTime1 = new Date().getTime();
		System.out.println("Start testing.");
		evaluate();
		System.out.println("Finished testing.");
		long endTime1 = new Date().getTime();
		System.out.println("It used " + (endTime1 - startTime1)/1000 + " seconds to evaluate.");
	}
}
