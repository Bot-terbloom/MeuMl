package de.fh.meuml.core;
import java.io.*;
import org.math.plot.Plot2DPanel;

import de.fh.meuml.generator.*;

public class main
{
	public static void main(String[] args)
	{
		/** Wie viele Datensätze habe ich insgesamt? (Über alle Datensatz)
		 * 
		 * */
		int sensorId = 1324189;
		//String basepath = "..\\measurements\\2015.04.30\\08\\";
		String basepath = "../measurements/2015.04.30/08/";
		//Data laufen = getDataFromFile(basepath + "laufen.csv");
		//Data still = getDataFromFile(basepath + "sitzen.csv");
		Data laufen = getDataFromFile(basepath + "laufen.csv");
		Data still = getDataFromFile(basepath + "sitzen.csv");
		
		System.out.print("Anzahl Datensätze: ");
        System.out.println(still.getAttributeByValue(Data.Fields.ID.getText(), sensorId).length);
        
        System.out.print("Mean AccX: ");
        System.out.println(getMean(still.getAttributeByValue(Data.Fields.AccelX.getText(), sensorId)));
        System.out.print("Mean AccY: ");
        System.out.println(getMean(still.getAttributeByValue(Data.Fields.AccelY.getText(), sensorId)));
        System.out.print("Mean AccZ: ");
        System.out.println(getMean(still.getAttributeByValue(Data.Fields.AccelZ.getText(), sensorId)));
		
        System.out.print("StdDev AccX: ");
        System.out.println(calcDev(still.getAttributeByValue(Data.Fields.AccelX.getText(), sensorId)));
        System.out.print("StdDev AccY: ");
        System.out.println(calcDev(still.getAttributeByValue(Data.Fields.AccelY.getText(), sensorId)));
        System.out.print("StdDev AccZ: ");
        System.out.println(calcDev(still.getAttributeByValue(Data.Fields.AccelZ.getText(), sensorId)));
        
//        still.generateAttribute(Data.Fields.AccelY.getText(), "prev aY", new Prev(5));
//        still.generateAttribute(Data.Fields.AccelY.getText(), "avg aY", new Average(11, 6));
//        still.generateAttribute(Data.Fields.AccelY.getText(), "std aY", new StdDeviation(11, 6));
//        still.generateAttribute(Data.Fields.AccelY.getText(), "med aY", new Median(11, 6));
        still.generateAttribute(Data.Fields.AccelY.getText(), "prev aY still", new Prev(1));
        still.generateAttribute(Data.Fields.AccelY.getText(), "eng aY still", new Energy(91, 46));
        //double medEngStill = getMean(still.getAttributeByValue("eng aY", sensorId));
        
        laufen.generateAttribute(Data.Fields.AccelY.getText(), "prev aY laufen", new Prev(1));
        laufen.generateAttribute(Data.Fields.AccelY.getText(), "eng aY laufen", new Energy(91, 46));
        //double medEngLaufen = getMean(laufen.getAttributeByValue("eng aY", sensorId));
        
        Plot2DPanel plot = still.getPlot(sensorId, null, "eng aY still");
        plot = laufen.getPlot(sensorId, plot, "eng aY laufen");
        Data.showPlot(plot, "diff");
        
//        still.showPlot(sensorId, true, Data.Fields.AccelY.getText(), "prev aY still", "eng aY still");
//        laufen.showPlot(sensorId, true, Data.Fields.AccelY.getText(), "prev aY laufen", "eng aY laufen");
//        still.showPlot(sensorId, true, Data.Fields.AccelY.getText(), "prev aY", "avg aY", "eng aY", "med aY", "std aY");
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

		mean = mean/data.length;
		
		for (int i = 0; i < data.length; i++) {
			dev = Math.pow(mean-data[i], 2);
		}
		dev = dev/data.length;
		dev = Math.sqrt(dev);
		return dev;
	}
	
	public static Data getDataFromFile(String file) {
		BufferedReader inputStream = null;
		Data data;

        try {
            inputStream = new BufferedReader(new FileReader(file));

            String l;
            data = new Data(inputStream.readLine());
            while ((l = inputStream.readLine()) != null) {
            	data.addLine(l);
            }
        } catch (Exception e) {
        	System.out.println("Something went wrong: " + e.getMessage());
        	e.printStackTrace();
        	return new Data("");
        } finally {
            if (inputStream != null) {
            	try {
            		inputStream.close();
            	} catch (IOException e) {
            		System.out.println("Couldn't close input stream");
            	}
            }
        }
        return data;
	}
}
