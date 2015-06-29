package de.fh.meuml.core;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.math.plot.Plot2DPanel;

import de.fh.as.neuron.Neuron;
import de.fh.meuml.generator.Energy;

public class main {
	public static void main(String[] args) {
		int sensorId = 1324189;

		// String basepath = "..\\measurements\\2015.04.30\\08\\";
		String basepath = "./measurements/2015.04.30/08/";

		String nameLaufen = "laufen";
		String nameSitzen = "sitzen";

		Data sitzen = Data.getDataFromFile(basepath + "sitzen.csv", nameSitzen);
		Data laufen = Data.getDataFromFile(basepath + "laufen.csv", nameLaufen);

		sitzen.generateFeature();
		laufen.generateFeature();

//		A2.runStatistics(laufen, sitzen, sensorId);
//		A2.plotGraphs(sitzen, laufen, sensorId);
//		A2.runP5_1(laufen, sitzen, sensorId);

		double[][] c1 = new double[2][];
		double[][] c2 = new double[2][];
		
		c1[0] = sitzen.getAttribute(Data.Fields.AccelY, Energy.name, sensorId);
		c1[1] = sitzen.getAttribute(Data.Fields.GyroZ, Energy.name, sensorId);
		
		
		c2[0] = laufen.getAttribute(Data.Fields.AccelY, Energy.name, sensorId);
		c2[1] = laufen.getAttribute(Data.Fields.GyroZ, Energy.name, sensorId);

//		System.out.println(Arrays.toString(c1[0]));
//		System.out.println(Arrays.toString(c2[0]));
//		System.out.println(Arrays.toString(c1[1]));
//		System.out.println(Arrays.toString(c2[1]));
		Neuron ne = A2.trainPocket(c1, c2);
		DecimalFormat df = new DecimalFormat("###,##0.000");
		double[] weights = ne.getWeights();
		String output = "";
		System.out.println("Theta: " + df.format(ne.getTeta()));
		for (int i = 0; i < weights.length; i++) {
			System.out.println("w" + i + ": " + df.format(weights[i]));
			output += "x" + i + " = "
					+ df.format(ne.getTeta() / ne.getWeight(i)) + "\n";
		}
		System.out.println(output);
		

		// still.showPlot(sensorId, true, Data.Fields.AccelY.getText(),
		// "prev aY still", "eng aY still");
		// laufen.showPlot(sensorId, true, Data.Fields.AccelY.getText(),
		// "prev aY laufen", "eng aY laufen");
		// still.showPlot(sensorId, true, Data.Fields.AccelY.getText(),
		// "prev aY", "avg aY", "eng aY", "med aY", "std aY");
	}

}
