package de.fh.meuml.core;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import java.awt.Color;

import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;
import org.math.plot.PlotPanel;

import de.fh.meuml.generator.AttributeGenerator;

public class Data
{
	public HashMap<Integer, ArrayList<DataLine>> lines = new HashMap<Integer, ArrayList<DataLine>>();
	//public String[] headers;
	public HashMap<String, Integer> headers = new HashMap<String, Integer>();
	public Double startTimestamp = null;
	
	public enum Fields {
		ID("ID"), Timestamp("Timestamp"),
		AccelX("accelX (m/s^2)"), AccelY("accelY (m/s^2)"), AccelZ("accelZ (m/s^2)"),
		GyroX("gyroX (rad/s)"), GyroY("gyroY (rad/s)"), GyroZ("gyroZ (rad/s)"),
		MagnetX("magnetX (uT)"), MagnetY("magnetY (uT)"), MagnetZ("magnetZ (uT)");
		private final String code;
		private Fields(String code) {
			this.code = code;
		}
		public String getText() {
			return this.code;
		}
	}
	
	public Data(String header) {
		String[] headers = header.split(",");
		for (int i = 0; i < headers.length; i++) {
			this.headers.put(headers[i], i);
		}
	}
	
	public void addLine(String line) {
		if (startTimestamp == null) {
			String[] first = line.split(",");
			startTimestamp = Double.parseDouble(first[headers.get("Timestamp")]);
			System.out.println(startTimestamp);
		}
		DataLine dataLine = new DataLine(line, headers, startTimestamp);
		if (lines.get(dataLine.id) == null) {
			lines.put(dataLine.id, new ArrayList<DataLine>());
		}
		lines.get(dataLine.id).add(dataLine);
	}
	
	public double[] getAttributeByValue(String returnAttrbute, int id) {
		if (lines.get(id) == null) {
			System.out.println("There is no entry for " + id);
			return new double[0];
		}
		double arr[] = new double[lines.get(id).size()];
		Double value;
		for (int i = 0; i < lines.get(id).size(); i++) {
			if ((value = lines.get(id).get(i).data.get(headers.get(returnAttrbute))) == null) {
				arr[0] = 0.0;
			} else {
				arr[i] = value;
			}
		}
		
		return arr;
	}
	
	public void generateAttribute(String fromAttribute, String name, AttributeGenerator generator) {
		generator.generateAttribute(this, fromAttribute, name);
		//System.out.println(lines.get(1324180).get(7).data.get("avg aX"));
		//name = name;
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
			//showPlot(plot, fields[0]);
		}/* else {
			for (int i = 0; i < fields.length; i++) {
				double[] points = getAttributeByValue(fields[i], id);
				plot.addLinePlot("my plot", tss, points);
				//showPlot(plot, fields[i]);
			}
		}*/
		return plot;
	}
	
	public static void showPlot(PlotPanel plot, String name) {
		JFrame frame = new JFrame(name);
		frame.setSize(1024, 768);
		frame.setContentPane(plot);
		frame.setVisible(true);
		frame.addWindowListener(new MyWindowListener());
	}
}
