package de.fh.meuml.core;
import java.util.ArrayList;
import java.util.HashMap;

public class DataLine
{
	//public HashMap<String, Double> data = new HashMap<String, Double>();
	public ArrayList<Double> data = new ArrayList<Double>();
	int id;
    
	public DataLine(String line, HashMap<String, Integer> header, Double startTimestamp) {
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
