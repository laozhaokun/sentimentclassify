package cn.edu.thu.ml.perceptron;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhf 
 * @email zhf.thu@gmail.com
 * @version 创建时间：2014年6月1日 上午9:05:30
 * Create vectors for further using.
 */
public class CretateVector {
	//just test
	public static void main(String[] args) throws IOException {
		String file = "C:\\Users\\logic\\Desktop\\corpus_final\\kitchen.train";
		List<Instance> list = createVector(file);
		for(Instance ins : list){
			Map<String,Integer> map = ins.getFeatureVector();
			int label = ins.getLabel();
			for(Map.Entry<String, Integer> m : map.entrySet()){
				System.out.print(m.getKey() + ":" +m.getValue() + " ");
			}
			System.out.println(label);
		}
	}
	
	/**
	 * reads text file
	 * @param file file to be read and converted
	 * @return reviews as ArrayList of Instance
	 */

	public static List<Instance> createVector(String file) throws IOException{
		List<Instance> list = new ArrayList<Instance>();
		BufferedReader br = new BufferedReader(new FileReader(new File(file)));
		String line = br.readLine();
		while(line != null){
			Map<String,Integer> map = new HashMap<String,Integer>();
			String[] review = line.split(" ");
			int label = 0;
			if (review[review.length-1].equals("#label#:negative")) {
				label = -1;
			} else if (review[review.length-1].equals("#label#:positive")){
				label = 1;
			}
			for (int i = 0; i <= review.length-2; i++) {
				String[] keyvalue = review[i].split(":");
				if (keyvalue.length == 2) {
					String key = keyvalue[0];
					int value = Integer.parseInt(keyvalue[1]);
					map.put(key, value);
				}
			}
			Instance instance = new Instance(map,label);
			list.add(instance);
			line = br.readLine();
		}
		return list;
	}

}
