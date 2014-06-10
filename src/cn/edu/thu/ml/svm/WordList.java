package cn.edu.thu.ml.svm;

import java.io.*;
import java.util.*;

import org.apache.commons.io.FileUtils;

public class WordList {
	private static HashMap<String, Double> createWordMap(String file) {
		HashMap<String, Double> wordMap = new HashMap<String, Double>();
		try {
			List<String> list = FileUtils.readLines(new File(file));
			for (int i = 0; i < list.size(); i++) {
				String line = (String) list.get(i);
				StringTokenizer stk = new StringTokenizer(line, " ");
				int end = stk.countTokens();
				if (line.contains("#label#"))
					end -= 1;
				for (int j = 0; j < end; j++) {
					String token = stk.nextToken();
					String[] info = token.split(":");
					//debug use
					// System.out.println("info.length : " + info.length + "," +
					// info[0]);
					// System.out.println("reading " + file.getName() +
					// "'s line " + i + "-->"+ info[0] + ":" + info[1]);
					String word = info[0];
					double value = Double.parseDouble(info[1]);

					if (wordMap.containsKey(word)) {
						wordMap.put(word, wordMap.get(word) + value);
					} else {
						wordMap.put(word, value);
					}
				}
			}
			return wordMap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<String> createWordList(String file,
			int minLength, double threshold) {
		HashMap<String, Double> fullWordMap = createWordMap(file);

		// filter rare occur words
		ArrayList<String> wordList = new ArrayList<String>();
		for (String word : fullWordMap.keySet()) {
			double value = fullWordMap.get(word);
			if (word.length() >= minLength && value >= threshold) {
				wordList.add(word);
			}
		}

		return wordList;
	}

	public static boolean saveWordList(ArrayList<String> wordList, String wlname) {
		try {
			StringBuffer sb = new StringBuffer();

			// save words
			for (int i = 0; i < wordList.size(); i++) {
				sb.append(wordList.get(i) + "\n");
			}

			// write word list file
			FileUtils.writeStringToFile(new File(wlname), sb.toString());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
