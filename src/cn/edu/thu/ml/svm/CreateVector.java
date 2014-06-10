package cn.edu.thu.ml.svm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;

/**
 * create a libsvm standard format file
 */
public class CreateVector {
	public static ArrayList<StringBuffer> createVector(String file,
			ArrayList<String> wordList) {
		ArrayList<StringBuffer> vectors = new ArrayList<StringBuffer>();
		try {
			//read all lines from the file to a list
			List<String> list = FileUtils.readLines(new File(file));
			//for each line in this list
			for (int i = 0; i < list.size(); i++) {
				ArrayList<T> vector = new ArrayList<T>();
				StringBuffer strvec = new StringBuffer();
				//separate the label and the content in a line to a string array
				String[] line = list.get(i).split("#label#:");
				String content = "", label = "";
				if (line.length == 2) {
					content = line[0].trim();
					label = line[1].trim();
				} else
					System.err.println("ERROR");
				//the class label
				if (label.equals("positive"))
					strvec.append("1");
				else if (label.equals("negative"))
					strvec.append("-1");
				strvec.append(" ");

				// get the content
				StringTokenizer tokenizer = new StringTokenizer(content, " ");

				int end = tokenizer.countTokens();
				for (int j = 0; j < end; j++) {
					//get the token and time that each token appears
					String[] str = tokenizer.nextToken().split(":");
					String word = str[0];
					double count = Double.parseDouble(str[1]);

					int idx = wordList.indexOf(word) + 1;
					if (idx >= 1) {
						vector.add(new T(idx, count));
					}
				}

				// sort feature list and convert it to strvec
				Collections.sort(vector);
				for (T f : vector) {
					strvec.append(f.dim + ":" + f.value + " ");
				}
				// add to the dataset
				vectors.add(strvec);
			}
			return vectors;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean saveVector(ArrayList<StringBuffer> vectors,
			String vname) {
		try {
			StringBuffer content = new StringBuffer();
			for (int i = 0; i < vectors.size(); i++) {
				content.append(vectors.get(i) + "\n");
			}

			FileUtils.writeStringToFile(new File(vname), content.toString());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}

class T implements Comparable<T> {
	int dim;
	double value;

	public T(int dim, double value) {
		this.dim = dim;
		this.value = value;
	}

	@Override
	public int compareTo(T comp) {
		if (this.dim > comp.dim)
			return 1;
		else if (this.dim < comp.dim)
			return -1;
		else
			return 0;
	}
}
