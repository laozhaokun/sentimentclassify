package de.bwaldvogel.liblinear;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * @author zhf 
 * @email zhf.thu@gmail.com
 * @version 创建时间：2014年6月4日 上午10:29:50
 */
public class Test {
	static String file = "E:\\DevProj\\workspace\\sentimentclassify\\output\\svm/book_svm.svm";
	public static void main(String[] args) throws Exception {
//		String trainArgs[] = { "E:\\DevProj\\workspace\\sentimentclassify\\output\\svm/book_svm.svm" };
//		Train.main(trainArgs);
		run();
	}
	public static void run() throws IOException, InvalidInputDataException{
//		Problem problem = new Problem();
		Problem problem = Problem.readFromFile(new File(file),-1);
//		problem.l = random.nextInt(100) + 1; // number of training examples
//		problem.n = random.nextInt(100) + 1;; // number of features
//		problem.x = new FeatureNode[problem.l][];; // feature nodes
//		problem.y = new double[problem.l];; // target values

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
}
