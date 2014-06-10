package cn.edu.thu.ml.perceptron;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author zhf 
 * @email zhf.thu@gmail.com
 * @version 创建时间：2014年6月1日 上午9:06:58
 */
public class Instance {
	private Map<String,Integer> featureVector;
	private int label;
	public Instance(Map<String, Integer> featureVector, int label) {
		super();
		this.featureVector = featureVector;
		this.label = label;
	}
	@Override
	public String toString() {
		return "Instance [featureVector=" + featureVector + ", label=" + label
				+ "]";
	}
	public Map<String, Integer> getFeatureVector() {
		return featureVector;
	}
	public void setFeatureVector(HashMap<String, Integer> featureVector) {
		this.featureVector = featureVector;
	}
	public int getLabel() {
		return label;
	}
	public void setLabel(int label) {
		this.label = label;
	}
	public Set<String> getFeatures(){
		return this.featureVector.keySet();
	}
	
}
