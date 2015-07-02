package de.fh.meuml.core;

import java.text.DecimalFormat;
import java.util.HashMap;

public class Evaluation
{
	HashMap<DataLine.Annotation, TrueFalse> classes = new HashMap<DataLine.Annotation, TrueFalse>();
	
	private class TrueFalse {
		public int trues = 0;
		public int falses = 0;
	}
	
	public void count(DataLine.Annotation guessed, DataLine.Annotation real) {
		if (classes.get(guessed) == null) {
			classes.put(guessed, new TrueFalse());
		}
		if (guessed == real) {
			classes.get(guessed).trues++;
		} else {
			classes.get(guessed).falses++;
		}
	}
	
	@Override
	public String toString() {
		String result = "";
		String classNames = "";
		int i = 0;
		if (classes.keySet().size() == 2) {
			int TP = 0, FP = 0, TN = 0, FN = 0;
			for (DataLine.Annotation a : classes.keySet()) {
				if (i == 0) {
					TP = classes.get(a).trues;
					FP = classes.get(a).falses;
				} else {
					TN = classes.get(a).trues;
					FN = classes.get(a).falses;
				}
				classNames += "|" + (i == 0 ? "Positives" : "Negatives") + " = " + a + "| ";
				result += "True " + (i == 0 ? "Positives" : "Negatives") + ": " + classes.get(a).trues + "\n";
				result += "False " + (i == 0 ? "Positives" : "Negatives") + ": " + classes.get(a).falses + "\n";
				i++;
			}
			result += "TPR: " + getRate(TP, TP + FN) + "% " +
					"FPR: " + getRate(FP, TN + FP) + "% " +
					"TNR: " + getRate(TN, TN + FP) + "% " +
					"FNR: " + getRate(FN, TP + FN) + "% " +
					"Fehlerrate: " + getRate(FN + FP, TP + FP + TN + FN) + "% ";
		} else {
			for (DataLine.Annotation a : classes.keySet()) {
				char name = (char)(i + 65);
				classNames += name + " = " + a + " ";
				result += "True " + name + ": " + classes.get(a).trues + "\n";
				result += "False " + name + ": " + classes.get(a).falses + "\n";
				i++;
			}
		}
		
		return classNames + "\n" + result;
	}
	
	private String getRate(int cnt, int size) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(((double)(cnt) / (double)(size)) * 100);
	}
}
