package de.fh.meuml.core;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.math.plot.Plot2DPanel;

import de.fh.meuml.core.Data.Fields;
import de.fh.meuml.core.DataLine.Annotation;
import de.fh.meuml.generator.*;
import de.fh.meuml.knn.gui.DrawGraph;
import de.fh.as.neuron.Neuron;

public class main {
	public static void main(String[] args) {
		int sensorId = 1324189;
		String basepath = "measurements\\2015.04.30\\08\\";
		String nameLaufen = "laufen";
		String nameGehen = "gehen";
		String nameDrehen = "drehen";
//		String basepath = "../measurements/2015.04.30/08/";
		Data training = Data.getDataFromFile(basepath + "laufen.csv", nameLaufen, Annotation.Laufen);
		Data laufen1 = Data.getDataFromFile(basepath + "laufen.csv", nameLaufen, Annotation.Laufen);
		Data drehen1 = Data.getDataFromFile(basepath + "drehen.csv", nameDrehen, Annotation.Drehen);
		Data gehen1 = Data.getDataFromFile(basepath + "gehen.csv", nameGehen, Annotation.Gehen);
		
		basepath = "measurements\\2015.04.30\\20\\";
		Data eval = Data.getDataFromFile(basepath + "laufen.csv", nameLaufen, Annotation.Laufen);
		Data laufen2 = Data.getDataFromFile(basepath + "laufen.csv", nameLaufen, Annotation.Laufen);
		Data drehen2 = Data.getDataFromFile(basepath + "drehen.csv", nameDrehen, Annotation.Drehen);
		Data gehen2 = Data.getDataFromFile(basepath + "gehen.csv", nameGehen, Annotation.Gehen);
		
		System.out.println("Trainingsdaten laufen: " + laufen1.lines.get(sensorId).size());
		System.out.println("Trainingsdaten gehen: " + gehen1.lines.get(sensorId).size());
		System.out.println("Trainingsdaten drehen: " + drehen1.lines.get(sensorId).size());
		System.out.println("Evaluierungsdaten laufen: " + laufen2.lines.get(sensorId).size());
		System.out.println("Evaluierungsdaten gehen: " + gehen2.lines.get(sensorId).size());
		System.out.println("Evaluierungsdaten drehen: " + drehen2.lines.get(sensorId).size());
		
		double theta = 0.1;
		int k = 4;
		
		mergeData(training, gehen1);
		//training = mergeData(training, drehen1);
		training.generateFeature();
		
		mergeData(eval, gehen2);
		//eval = mergeData(eval, drehen2);
		eval.generateFeature();
		
		Node tree = new Node(training.lines.get(sensorId), training.headers, 3, theta,
				training.getAttributeName(Fields.AccelX, Energy.name),
				training.getAttributeName(Fields.AccelY, Energy.name),
				training.getAttributeName(Fields.AccelZ, Energy.name));
		tree.print();
		
		Evaluation result = tree.fire(eval.lines.get(sensorId));
		System.out.println("Qualität Entscheidungsbaum: " + result.toString());
		
		gehen1.generateFeature();
		laufen1.generateFeature();
		drehen1.generateFeature();
		gehen2.generateFeature();
		laufen2.generateFeature();
		drehen2.generateFeature();
		
		double[][] c1 = new double[2][];
		double[][] c2 = new double[2][];
		double[][] c3 = new double[2][];
		
		c1[0] = gehen1.getAttribute(Fields.AccelX, Energy.name, sensorId);
		c1[1] = gehen1.getAttribute(Fields.AccelY, Energy.name, sensorId);
		
		c2[0] = laufen1.getAttribute(Fields.AccelX, Energy.name, sensorId);
		c2[1] = laufen1.getAttribute(Fields.AccelY, Energy.name, sensorId);
		
		c3[0] = drehen1.getAttribute(Fields.AccelX, Energy.name, sensorId);
		c3[1] = drehen1.getAttribute(Fields.AccelY, Energy.name, sensorId);
		
		double[][] e1 = new double[2][];
		double[][] e2 = new double[2][];
		double[][] e3 = new double[2][];
		
		e1[0] = gehen2.getAttribute(Fields.AccelX, Energy.name, sensorId);
		e1[1] = gehen2.getAttribute(Fields.AccelY, Energy.name, sensorId);
		
		e2[0] = laufen2.getAttribute(Fields.AccelX, Energy.name, sensorId);
		e2[1] = laufen2.getAttribute(Fields.AccelY, Energy.name, sensorId);
		
		e3[0] = drehen2.getAttribute(Fields.AccelX, Energy.name, sensorId);
		e3[1] = drehen2.getAttribute(Fields.AccelY, Energy.name, sensorId);
		
		Neuron ne = A2.trainPLA(c1, c2, theta);
		double[] weights = ne.getWeights();
		DecimalFormat df = new DecimalFormat("###,##0.000");
		String output = "";
		System.out.println("Theta: " + df.format(ne.getTeta()));
		for (int i = 0; i < weights.length; i++) {
			System.out.println("w" + i + ": " + df.format(weights[i]));
			output += "x" + i + " = "
					+ df.format(ne.getTeta() / ne.getWeight(i)) + "\n";
		}
		System.out.println(output);
		
		result = new Evaluation();
		for (int i = 0; i < e1[0].length; i++) {
			double[] in = {e1[0][i], e1[1][i]};
			ne.setIn(in);
			double out = ne.getOut();
			result.count((out == 1.0 ? Annotation.Gehen : Annotation.Laufen), Annotation.Gehen);
		}
		for (int i = 0; i < e2[0].length; i++) {
			double[] in = {e2[0][i], e2[1][i]};
			ne.setIn(in);
			double out = ne.getOut();
			result.count((out == 1.0 ? Annotation.Gehen : Annotation.Laufen), Annotation.Laufen);
		}
		System.out.println("Qualität PLA: " + result.toString());
		
		ne = A2.trainPocket(c1, c2, theta);
		weights = ne.getWeights();
		output = "";
		System.out.println("Theta: " + df.format(ne.getTeta()));
		for (int i = 0; i < weights.length; i++) {
			System.out.println("w" + i + ": " + df.format(weights[i]));
			output += "x" + i + " = "
					+ df.format(ne.getTeta() / ne.getWeight(i)) + "\n";
		}
		System.out.println(output);
		
		result = new Evaluation();
		for (int i = 0; i < e1[0].length; i++) {
			double[] in = {e1[0][i], e1[1][i]};
			ne.setIn(in);
			double out = ne.getOut();
			result.count((out == 1.0 ? Annotation.Gehen : Annotation.Laufen), Annotation.Gehen);
		}
		for (int i = 0; i < e2[0].length; i++) {
			double[] in = {e2[0][i], e2[1][i]};
			ne.setIn(in);
			double out = ne.getOut();
			result.count((out == 1.0 ? Annotation.Gehen : Annotation.Laufen), Annotation.Laufen);
		}
		System.out.println("Qualität Pocket: " + result.toString());
		
		DrawGraph.run(k);
		
		// still.showPlot(sensorId, true, Data.Fields.AccelY.getText(),
		// "prev aY still", "eng aY still");
		// laufen.showPlot(sensorId, true, Data.Fields.AccelY.getText(),
		// "prev aY laufen", "eng aY laufen");
		// still.showPlot(sensorId, true, Data.Fields.AccelY.getText(),
		// "prev aY", "avg aY", "eng aY", "med aY", "std aY");
		
//		  System.out.print("Anzahl Datensätze: ");
//        System.out.println(still.getAttributeByValue(Data.Fields.ID.getText(), sensorId).length);
//        
//        System.out.print("Mean AccX: ");
//        System.out.println(getMean(still.getAttributeByValue(Data.Fields.AccelX.getText(), sensorId)));
//        System.out.print("Mean AccY: ");
//        System.out.println(getMean(still.getAttributeByValue(Data.Fields.AccelY.getText(), sensorId)));
//        System.out.print("Mean AccZ: ");
//        System.out.println(getMean(still.getAttributeByValue(Data.Fields.AccelZ.getText(), sensorId)));
//		
//        System.out.print("StdDev AccX: ");
//        System.out.println(calcDev(still.getAttributeByValue(Data.Fields.AccelX.getText(), sensorId)));
//        System.out.print("StdDev AccY: ");
//        System.out.println(calcDev(still.getAttributeByValue(Data.Fields.AccelY.getText(), sensorId)));
//        System.out.print("StdDev AccZ: ");
//        System.out.println(calcDev(still.getAttributeByValue(Data.Fields.AccelZ.getText(), sensorId)));
//        
//        still.generateAttribute(Data.Fields.AccelY.getText(), "prev aY", new Prev(5));
//        still.generateAttribute(Data.Fields.AccelY.getText(), "avg aY", new Average(11, 6));
//        still.generateAttribute(Data.Fields.AccelY.getText(), "std aY", new StdDeviation(11, 6));
//        still.generateAttribute(Data.Fields.AccelY.getText(), "med aY", new Median(11, 6));
//        still.generateAttribute(Data.Fields.AccelY.getText(), "prev aY still", new Prev(1));
//        still.generateAttribute(Data.Fields.AccelY.getText(), "eng aY still", new Energy(91, 46));
//        //double medEngStill = getMean(still.getAttributeByValue("eng aY", sensorId));
//        
//        laufen.generateAttribute(Data.Fields.AccelY.getText(), "prev aY laufen", new Prev(1));
//        laufen.generateAttribute(Data.Fields.AccelY.getText(), "eng aY laufen", new Energy(91, 46));
//        //double medEngLaufen = getMean(laufen.getAttributeByValue("eng aY", sensorId));
//        
//        Plot2DPanel plot = still.getPlot(sensorId, null, "eng aY still");
//        plot = laufen.getPlot(sensorId, plot, "eng aY laufen");
//        Data.showPlot(plot, "diff");
//        
//        still.showPlot(sensorId, true, Data.Fields.AccelY.getText(), "prev aY still", "eng aY still");
//        laufen.showPlot(sensorId, true, Data.Fields.AccelY.getText(), "prev aY laufen", "eng aY laufen");
//        still.showPlot(sensorId, true, Data.Fields.AccelY.getText(), "prev aY", "avg aY", "eng aY", "med aY", "std aY");
	}
	
	public static Data mergeData(Data... datas) {
		Data result = datas[0];
		if (datas[0].headers.size() != datas[1].headers.size()) {
			System.out.println("Header size doesn't match");
			return null;
		}
		for (int i = 0; i < datas[0].headers.size(); i++) {
			if (datas[0].headers.get(i) != datas[1].headers.get(i)) {
				System.out.println("Headers don't match");
				return null;
			}
		}
		for (int i : datas[1].lines.keySet()) {
			for (int j = 0; j < datas[1].lines.get(i).size(); j++) {
				result.lines.get(i).add(datas[1].lines.get(i).get(j));
			}
		}
		
		return result;
	}
	
	public static double getMean(double[] data) {
		double mean = 0.0;
		for (int i = 0; i < data.length; i++) {
			mean += data[i];
		}
		mean = mean / data.length;
		return mean;
	}
	
	public static double calcDev(double[] data){
		double dev = 0.0;
		double mean = 0.0;
		
		for (int i = 0; i < data.length; i++) {
			mean += data[i];
		}
		dev = dev/data.length;
		dev = Math.sqrt(dev);
		return dev;
	}

}
