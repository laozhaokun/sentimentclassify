package cn.edu.thu.ml.perceptron;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author zhf 
 * @email zhf.thu@gmail.com
 * @version 创建时间：2014年6月8日 下午12:39:16
 * This class is used to test a review's classification.
 */
public class TestClassify {
	public static HashMap<String, Integer> convertReviewToHashMap(String review) {
		HashMap<String, Integer> reviewAsHashMap = new HashMap<String, Integer>();
		//it means the review is a raw text,but it may not be key:value format,either!
		//in that situation it will be unknown
		if(!review.contains(":")){
			review = review.replaceAll("[^a-zA-Z 0-9]", "").toLowerCase(); 
			String[] reviewArray = review.split(" ");
			for(String word : reviewArray){
				if (reviewAsHashMap.containsKey(word)) {
				reviewAsHashMap.put(word, reviewAsHashMap.get(word)+1);
				} else {
					reviewAsHashMap.put(word, 1);
				}
			}
		}else{
			String[] reviewArray = review.split(" ");
			for (String word : reviewArray) {
				String[] keyvalue = word.split(":");
				if (keyvalue.length == 2) {
					String key = keyvalue[0];
					int value = Integer.parseInt(keyvalue[1]);
					reviewAsHashMap.put(key, value);
				}
			}
		}
		return reviewAsHashMap;
	}
	public static int evaluateReview(String text) {
		String file = "output\\perceptron/weightVectors//trained_all_10_1.txt";
		HashMap<String, Double> weightVector = Train.weightVectorFromFile(new File(file));
		HashMap<String, Integer> review = convertReviewToHashMap(text);
		int label = 0;
		double dotP = Perceptron.dotProduct(review, weightVector);
		System.err.println("dotP = " + dotP);
		if (dotP > 0) {
			label = 1;
		} else if (dotP < 0) {
			label = -1;
		} else {
			label = 0;
		}
		return label;
	}
	public static void printResult(String text){
		int label = evaluateReview(text);
		if (label ==1){
			System.out.println("positive!");
		}
		else if (label == -1){
			System.out.println("negative!");
		}
		else {
			System.out.println("i don't know ...");
		}
		System.out.println("-----------------------------------------");
	}
	public static void main(String[] args) throws IOException {
//		String text = "good:2 very:3 ok:2 thank:1 you:1 much:1";
		String text = "hello,i hate this meal.";
		printResult(text);
		
		//below is from a given file
		String file = "output\\perceptron/weightVectors//test.txt";
		BufferedReader br = new BufferedReader(new FileReader(new File(file)));
		String line = br.readLine();
		while(line != null){
			printResult(line);
			line = br.readLine();
		}
	}


}
