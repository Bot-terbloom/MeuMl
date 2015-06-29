package de.fh.meuml.core;

import java.util.ArrayList;
import java.util.HashMap;

import de.fh.meuml.core.DataLine.Annotation;

public class Node
{
	public ArrayList<Node> children = new ArrayList<Node>();
	ArrayList<DataLine> data;
	HashMap<String, Integer> headers;
	String chosenFeature = "";
	Double chosenThreshold = 0.0;
	String name;
	ArrayList<Annotation> classes = new ArrayList<DataLine.Annotation>();
	boolean isLeft;
	public Annotation leafClass = Annotation.Keine;
	
	public Node(ArrayList<DataLine> data, HashMap<String, Integer> headers, int sepCount, boolean isLeft, Node parent, String... features) {
		this.data = data;
		this.headers = headers;
		this.isLeft = isLeft;
		
		for (DataLine d : this.data) {
			if (!classes.contains(d.annotation)) {
				classes.add(d.annotation);
			}
		}
		
		if (getEntropy() > 0.1) {
			double minEntropy = Double.MAX_VALUE;
			ArrayList<DataLine> minLeft = new ArrayList<DataLine>();
			ArrayList<DataLine> minRight = new ArrayList<DataLine>();
			for (int i = 0; i < features.length; i++) {
				double[] minMax = getMinMax(features[i]);
				for (int j = 1; j <= sepCount; j++) {
					double threshold = (double)(j)/(double)(sepCount + 1) * (minMax[1] - minMax[0]) + minMax[0];
					ArrayList<DataLine> left = new ArrayList<DataLine>();
					ArrayList<DataLine> right = new ArrayList<DataLine>();
					for (DataLine d : this.data) {
						if (d.data.get(this.headers.get(features[i])) < threshold) {
							left.add(d);
						} else {
							right.add(d);
						}
					}
					double entropy = getEntropy(this.classes, left) + getEntropy(this.classes, right);
					if (entropy < minEntropy) {
						chosenFeature = features[i];
						chosenThreshold = threshold;
						minEntropy = entropy;
						minLeft = left;
						minRight = right;
					}
				}
			}
			name = chosenFeature + " (" + chosenThreshold + ")";
			children.add(new Node(minLeft, headers, sepCount, true, this, features));
			children.add(new Node(minRight, headers, sepCount, false, this, features));
		} else {
			leafClass = getDominantClass();
			name = "Leaf: " + leafClass;
			chosenFeature = parent.chosenFeature;
			chosenThreshold = parent.chosenThreshold;
		}
	}
	
	public Evaluation fire(ArrayList<DataLine> evalData) {
		Evaluation result = new Evaluation();
		return fire(evalData, result);
	}
	
	public Evaluation fire(ArrayList<DataLine> evalData, Evaluation current) {
		if (children.size() < 1) {
			for (DataLine d : evalData) {
				if (d.data.get(this.headers.get(chosenFeature)) < chosenThreshold && isLeft) {
					if (d.annotation == leafClass) {
						current.FP++;
					} else {
						current.FN++;
					}
				} else {
					if (d.annotation == leafClass) {
						current.TP++;
					} else {
						current.TN++;
					}
				}
			}
		} else {
			ArrayList<DataLine> left = new ArrayList<DataLine>();
			ArrayList<DataLine> right = new ArrayList<DataLine>();
			for (DataLine d : evalData) {
				if (d.data.get(this.headers.get(chosenFeature)) < chosenThreshold) {
					left.add(d);
				} else {
					right.add(d);
				}
			}
			current = children.get(0).fire(left, current);
			current = children.get(1).fire(right, current);
		}
		
		return current;
	}
	
	private Annotation getDominantClass() {
		Annotation result = null;
		double max = 0.0;
		for (Annotation a : classes) {
			double likelihood = getLikelihood(a);
			if (likelihood > max) {
				max = likelihood;
				result = a;
			}
		}
		return result;
	}
	
	private double getEntropy() {
		return getEntropy(classes, data);
	}
	
	private static double getEntropy(ArrayList<Annotation> classes, ArrayList<DataLine> data) {
		double sum = 0.0;
		for (Annotation a : classes) {
			double likelihood = getLikelihood(data, a);
			sum += (likelihood * (likelihood == 0.0 ? 0.0 : Math.log(likelihood)));
		}
		return -sum;
	}
	
	private double getLikelihood(Annotation a) {
		return getLikelihood(data, a);
	}
	
	private static double getLikelihood(ArrayList<DataLine> data, Annotation annotation) {
		int cnt = 0;
		for (DataLine d : data) {
			cnt += (d.annotation == annotation ? 1 : 0);
		}
		return (double)cnt/(double)data.size();
	}
	
	private double[] getMinMax(String feature) {
		double[] minMax = {Double.MAX_VALUE, Double.MIN_VALUE};
		for (DataLine d : data) {
			double value = d.data.get(headers.get(feature));
			if (value < minMax[0]) {
				minMax[0] = value;
			}
			if (value > minMax[1]) {
				minMax[1] = value;
			}
		}
		return minMax;
	}
	
	public void print() {
		print("", true);
	}
	
	public void print(String prefix, boolean isTail) {
		System.out.println(prefix + (isTail ? "L-- ": "|-- ") + name);
		for (int i = 0; i < children.size(); i++) {
			children.get(i).print(prefix + (isTail ? "    " : "|   "), (i == children.size() - 1));
		}
	}
}
