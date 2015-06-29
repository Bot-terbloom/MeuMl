package de.fh.meuml.core;

import java.text.DecimalFormat;

public class Evaluation
{
	public int TP = 0;
	public int TN = 0;
	public int FP = 0;
	public int FN = 0;
	
	public String toString(int size) {
		return "TP: " + TP + " (" + getRate(TP, size) + ")" + 
				"; TN: " + TN + " (" + getRate(TN, size) + ")" +
				"; FP: " + FP + " (" + getRate(FP, size) + ")" +
				"; FN: " + FN + " (" + getRate(FN, size) + ")";
	}
	
	private String getRate(int cnt, int size) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(((double)(cnt) / (double)(size)) * 100);
	}
	
	public void increase(Evaluation other) {
		TP += other.TP;
		TN += other.TN;
		FP += other.FP;
		FN += other.FN;
	}
}
