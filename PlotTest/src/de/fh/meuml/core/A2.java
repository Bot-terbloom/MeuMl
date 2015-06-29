package de.fh.meuml.core;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Arrays;

import org.math.plot.Plot2DPanel;

import de.fh.as.neuron.Neuron;
import de.fh.as.neuron.PLA;
import de.fh.as.neuron.Pocket;
import de.fh.as.neuron.destinations.DstFeature;
import de.fh.meuml.generator.Energy;

public class A2 {
	private static int runs = 1000;
	
	public static void runStatistics(Data laufen, Data still, int sensorId){

		System.out.print("Anzahl Datensätze: ");
		System.out.println(still.getAttributeByValue(Data.Fields.ID.getText(),
				sensorId).length);

		System.out.print("Mean AccX: ");
		System.out.println(getMean(still.getAttributeByValue(
				Data.Fields.AccelX.getText(), sensorId)));
		System.out.print("Mean AccY: ");
		System.out.println(getMean(still.getAttributeByValue(
				Data.Fields.AccelY.getText(), sensorId)));
		System.out.print("Mean AccZ: ");
		System.out.println(getMean(still.getAttributeByValue(
				Data.Fields.AccelZ.getText(), sensorId)));

		System.out.print("StdDev AccX: ");
		System.out.println(calcDev(still.getAttributeByValue(
				Data.Fields.AccelX.getText(), sensorId)));
		System.out.print("StdDev AccY: ");
		System.out.println(calcDev(still.getAttributeByValue(
				Data.Fields.AccelY.getText(), sensorId)));
		System.out.print("StdDev AccZ: ");
		System.out.println(calcDev(still.getAttributeByValue(
				Data.Fields.AccelZ.getText(), sensorId)));	
	}
	
	public static double getMean(double[] data) {
		double mean = 0.0;
		for (int i = 0; i < data.length; i++) {
			mean += data[i];
		}
		mean = mean / data.length;
		return mean;
	}

	public static double calcDev(double[] data) {
		double dev = 0.0;
		double mean = 0.0;

		for (int i = 0; i < data.length; i++) {
			mean += data[i];
		}

		mean = mean / data.length;

		for (int i = 0; i < data.length; i++) {
			dev = Math.pow(mean - data[i], 2);
		}
		dev = dev / data.length;
		dev = Math.sqrt(dev);
		return dev;
	}
	
	public static void runP5_1(Data laufen, Data sitzen, int sensorId) {
		DecimalFormat df = new DecimalFormat("###,##0.000");
		Neuron ne;
		double[] class1;
		double[] class2;

		class1 = laufen.getAttributeByValue(
				laufen.getAttributeName(Data.Fields.AccelY, Energy.name),
				sensorId);
		class2 = sitzen.getAttributeByValue(
				sitzen.getAttributeName(Data.Fields.AccelY, Energy.name),
				sensorId);

		System.out.println("PLA");
		for (int i = 0; i < 10; i++) {
			ne = trainPLA(class1, class2);
			System.out.println("w1 = " + df.format(ne.getWeight(0))
					+ ", theta = " + ne.getTeta() + "\t=>\tx = "
					+ df.format(ne.getTeta() / ne.getWeight(0)));
		}

		System.out.println("Pocket");
		for (int i = 0; i < 10; i++) {
			ne = trainPocket(class1, class2);
			System.out.println("w1 = " + df.format(ne.getWeight(0))
					+ ", theta = " + ne.getTeta() + "\t=>\tx = "
					+ df.format(ne.getTeta() / ne.getWeight(0)));
		}
	}
	
	public static Neuron trainPLA(double[] desire, double[] fail){
		double[][]d = new double[1][desire.length];
		double[][]f = new double[1][desire.length];
		d[0]=desire;
		f[0]=fail;
		return trainPLA(d, f);
	}
	
	public static Neuron trainPLA(double[][] desire, double[][] fail){
		DstFeature dst = new DstFeature(desire, fail);
		PLA pla = new PLA(dst, desire.length);	
		
		for (int i = 0; i < runs; i++) {
			pla.setNewIn();
			pla.train();	
		}
		return pla;
	}

	public static Neuron trainPocket(double[] desire, double[] fail){
		double[][]d = new double[1][desire.length];
		double[][]f = new double[1][desire.length];
		d[0]=desire;
		f[0]=fail;
		return trainPocket(d, f);
	}
	
	public static Neuron trainPocket(double[][] desire, double[][] fail){
		DstFeature dst = new DstFeature(desire, fail);
		Pocket pocket = new Pocket(dst, desire.length);	
		
		for (int i = 0; i < runs; i++) {
			pocket.setNewIn();
			pocket.train();	
//			System.out.println(Arrays.toString(pocket.getWeights()));
//			System.out.println(pocket.getDestination().get(pocket)-pocket.getOut());
		}
		return pocket;
	}
	
	public static void plotGraphs(Data sitzen, Data laufen, int sensorId){
		String attributeName;
		
		attributeName =  sitzen.getAttributeName(Data.Fields.AccelY, Energy.name);
		Plot2DPanel plot = sitzen.getPlot(sensorId,  attributeName);
		attributeName =  laufen.getAttributeName(Data.Fields.AccelY, Energy.name);
		plot = laufen.getPlot(sensorId, plot, attributeName);
		
		Data.showPlot(plot, "diff");
	}
}
