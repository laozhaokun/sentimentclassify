package cn.edu.thu.ml.svm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import cn.edu.thu.ml.svm.WordList;
import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.InvalidInputDataException;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;

/**
 * generate the libsvm/liblinear format file for training
 *the format is :class_tag index:value index2:value2
 */
public class Main
{
	public static void main(String[] args) throws IOException, InvalidInputDataException {
//		generateVector();
//		train();
		classify("");
	}
	public static void generateVector() throws IOException
	{
		String dataDir = "data/";
		double threshold = 5;
		int minLength = 3;
		File dir = new File(dataDir);
		long startTime = new Date().getTime();
		for(File file : dir.listFiles()){
			String name[] = file.getName().split("\\.");
			System.out.println("start processing " + name[0] + "." + name[1] + "....");
			
			String wlname = "output/svm/" + name[0] + "_tokens.txt";
			String vname = "output/svm/" + name[0] + "_svm.svm";
			String info = "output/svm/" + name[0] + "_number.txt";
			
			ArrayList<String> wordList = WordList.createWordList(dataDir + "/" + file.getName(),minLength,threshold);
			int nwords = wordList.size();
			ArrayList<StringBuffer> vectors = CreateVector.createVector(dataDir + "/" + file.getName(), wordList);
			int ndocs = vectors.size();
			
			WordList.saveWordList(wordList, wlname);
			CreateVector.saveVector(vectors, vname);
			FileUtils.writeStringToFile(new File(info), "numberOfwords=" + nwords + "\nnumberOfdocs=" + ndocs);
			System.out.println("finished processing " + file.getName());
		}
		long endTime = new Date().getTime();
		System.out.println("It used " + (endTime - startTime)/1000 + " seconds to generate svm file.");
	}
	
	public static void train() throws IOException, InvalidInputDataException{
		String file = "output\\svm/book_svm.svm";
		Problem problem = Problem.readFromFile(new File(file),-1);

		SolverType solver = SolverType.L2R_LR; // -s 0
		double C = 1.0;    // cost of constraints violation
		double eps = 0.01; // stopping criteria

		Parameter parameter = new Parameter(solver, C, eps);
		Model model = Linear.train(problem, parameter);
		File modelFile = new File("output/model");
		model.save(modelFile);
		System.out.println(modelFile.getAbsolutePath());
		// load model or use it directly
		model = Model.load(modelFile);

		Feature[] instance = { new FeatureNode(1, 4), new FeatureNode(2, 2) };
		double prediction = Linear.predict(model, instance);
		System.out.println(prediction);
		int nr_fold = 10;
	    double[] target = new double[problem.l];
		Linear.crossValidation(problem, parameter, nr_fold, target);
	}

	//TODO
	public static void classify(String modelfile) throws IOException{
		Model model = Model.load(new File(modelfile));
	}
}

