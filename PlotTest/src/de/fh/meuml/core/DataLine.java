package de.fh.meuml.core;
import java.util.ArrayList;
import java.util.HashMap;

public class DataLine
{
	public enum Annotation {Laufen, Sitzen, Gehen, Drehen, Keine};
	
	//public HashMap<String, Double> data = new HashMap<String, Double>();
	public ArrayList<Double> data = new ArrayList<Double>();
	Annotation annotation = Annotation.Keine;
	int id;
    
	public DataLine(String line, HashMap<String, Integer> header, Double startTimestamp, Annotation annotation) {
		this.annotation = annotation;
		String[] data = line.split(",");
		for (int i = 0; i < data.length; i++) {
			Double value = Double.parseDouble(data[i]);
			if (header.get("ID") == i) {
				this.id = Integer.parseInt(data[i]);
			} else if (header.get("Timestamp") == i) {
				value = (startTimestamp == null) ? 0.0 : value - startTimestamp;
			}
			//this.data.put(header[i], Double.parseDouble(data[i]));
			this.data.add(value);
		}
	}
}
