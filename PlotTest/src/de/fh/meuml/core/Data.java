package de.fh.meuml.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;
import org.math.plot.PlotPanel;

import de.fh.meuml.core.DataLine.Annotation;
import de.fh.meuml.generator.*;

public class Data {
	public HashMap<Integer, ArrayList<DataLine>> lines = new HashMap<Integer, ArrayList<DataLine>>();
	// public String[] headers;
	public HashMap<String, Integer> headers = new HashMap<String, Integer>();
	public Double startTimestamp = null;
	private String name = "";

	@SuppressWarnings("unused")
	private Data() {
	}

	public enum Fields {
		ID("ID"), Timestamp("Timestamp"), AccelX("accelX (m/s^2)"), AccelY(
				"accelY (m/s^2)"), AccelZ("accelZ (m/s^2)"), GyroX(
				"gyroX (rad/s)"), GyroY("gyroY (rad/s)"), GyroZ("gyroZ (rad/s)"), MagnetX(
				"magnetX (uT)"), MagnetY("magnetY (uT)"), MagnetZ(
				"magnetZ (uT)");
		private final String code;

		private Fields(String code) {
			this.code = code;
		}

		public String getText() {
			return this.code;
		}
	}

	public Data(String header, String name) {
		this.name = name;
		String[] headers = header.split(",");
		for (int i = 0; i < headers.length; i++) {
			this.headers.put(headers[i], i);
		}
	}

	public void addLine(String line, Annotation annotation) {
		if (startTimestamp == null) {
			String[] first = line.split(",");
			startTimestamp = Double.parseDouble(first[headers.get("Timestamp")]);
		}
		DataLine dataLine = new DataLine(line, headers, startTimestamp, annotation);
		if (lines.get(dataLine.id) == null) {
			lines.put(dataLine.id, new ArrayList<DataLine>());
		}
		lines.get(dataLine.id).add(dataLine);
	}

	public double[] getAttribute(Data.Fields attribute, String generatorName, int id) {
		return getAttributeByValue(
				getAttributeName(attribute, generatorName),
				id);
	}
	
	public double[] getAttributeByValue(String returnAttribute, int id) {
		if (lines.get(id) == null) {
			System.out.println("There is no entry for " + id);
			return new double[0];
		}
		double arr[] = new double[lines.get(id).size()];
		Double value;
		for (int i = 0; i < lines.get(id).size(); i++) {
			value = lines.get(id).get(i).data.get(headers
					.get(returnAttribute));
			if (value == null) {
				arr[0] = 0.0;
			} else {
				arr[i] = value;
			}
		}

		return arr;
	}

	public void generateAttribute(Fields fromAttribute, AttributeGenerator generator) {
		String toAtt = getAttributeName(fromAttribute, generator.getName());
		String fromAtt = fromAttribute.getText();
		generator.generateAttribute(this, fromAtt, toAtt);
		
		
		// System.out.println(lines.get(1324180).get(7).data.get("avg aX"));
		// name = name;
	}

	public Plot2DPanel getPlot(int id, String... fields) {
		return getPlot(id, null, fields);
	}
	
	public Plot2DPanel getPlot(int id, Plot2DPanel plot, String... fields) {
		double[] tss = getAttributeByValue("Timestamp", id);
		if (plot == null) {
			plot = new Plot2DPanel();
		}
		if (fields.length == 1) {
			double[] points = getAttributeByValue(fields[0], id);
			plot.addLinePlot("my plot", tss, points);

			double[] line = new double[points.length];
			for (int i = 0; i < line.length; i++) {
				line[i] = 90.0;
			}
			plot.addLinePlot("my plot", tss, line);
			// showPlot(plot, fields[0]);
		}/*
		 * else { for (int i = 0; i < fields.length; i++) { double[] points =
		 * getAttributeByValue(fields[i], id); plot.addLinePlot("my plot", tss,
		 * points); //showPlot(plot, fields[i]); } }
		 */
		return plot;
	}

	public static void showPlot(PlotPanel plot, String name) {
		JFrame frame = new JFrame(name);
		frame.setSize(1024, 768);
		frame.setContentPane(plot);
		frame.setVisible(true);
		frame.addWindowListener(new MyWindowListener());
	}

	public static Data getDataFromFile(String file, String name, Annotation annotation) {
		BufferedReader inputStream = null;
		Data data;

		try {
			inputStream = new BufferedReader(new FileReader(file));

			String l;
			data = new Data(inputStream.readLine(), name);
			while ((l = inputStream.readLine()) != null) {
				data.addLine(l, annotation);
			}
		} catch (Exception e) {
			System.out.println("Something went wrong: " + e.getMessage());
			e.printStackTrace();
			return new Data("", name);
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

	public void generateFeature() {

		int offset = 1;
		int ws = 91;
		int middle = ws / 2 + 1;
		Data.Fields attribute =  null;

		

		
		// genAtt(Fields.AccelY.getText(), "avg aY",
		// new Average(windowSize, windowSize / 2 + 1));
		attribute = Fields.AccelY;
		generateAttribute(attribute, new Average(ws, middle));
		generateAttribute(attribute, new Median(ws, middle));
		generateAttribute(attribute, new Prev(offset));
		generateAttribute(attribute, new Energy(ws, middle));

		attribute = Fields.AccelX;
		generateAttribute(attribute, new Average(ws, middle));
		generateAttribute(attribute, new Median(ws, middle));
		generateAttribute(attribute, new Prev(offset));
		generateAttribute(attribute, new Energy(ws,middle));
		
		attribute = Fields.AccelZ;
		generateAttribute(attribute, new Average(ws, middle));
		generateAttribute(attribute, new Median(ws, middle));
		generateAttribute(attribute, new Prev(offset));
		generateAttribute(attribute, new Energy(ws,middle));
		
		
		attribute = Fields.GyroY;
		generateAttribute(attribute, new Average(ws, middle));
		generateAttribute(attribute, new Median(ws, middle));
		generateAttribute(attribute, new Prev(offset));
		generateAttribute(attribute, new Energy(ws,middle));
		
		attribute = Fields.GyroX;
		generateAttribute(attribute, new Average(ws, middle));
		generateAttribute(attribute, new Median(ws, middle));
		generateAttribute(attribute, new Prev(offset));
		generateAttribute(attribute, new Energy(ws,middle));
		
		attribute = Fields.GyroZ;
		generateAttribute(attribute, new Average(ws, middle));
		generateAttribute(attribute, new Median(ws, middle));
		generateAttribute(attribute, new Prev(offset));
		generateAttribute(attribute, new Energy(ws,middle));

		// data.generateAttribute(Data.Fields.AccelY.getText(), "std aY", new
		// StdDeviation(11, 6));
		// double medEngStill = getMean(data.getAttributeByValue("eng aY",
		// sensorId));
		// data.generateAttribute(Data.Fields.AccelY.getText(),
		// "prev aY laufen", new Prev(1));
		// data.generateAttribute(Data.Fields.AccelY.getText(), "eng aY laufen",
		// new Energy(91, 46));
		// double medEngLaufen = getMean(laufen.getAttributeByValue("eng aY",
		// sensorId));
	}
	
	public String getAttributeName(Data.Fields attribute,  String nameAttributeGenerator){
		return getAttributeName(attribute.getText(), nameAttributeGenerator);
	}
	
	public String getAttributeName(String attribute,  String nameAttributeGenerator){
		return attribute+" "+nameAttributeGenerator+" "+this.name;
	}
	
}
