package de.fh.meuml.generator;

import java.util.ArrayList;
import java.util.Map.Entry;

import de.fh.meuml.core.Data;
import de.fh.meuml.core.DataLine;

public class StdDeviation extends AttributeGenerator {
	private int windowSize;
	private int placement;
	public final static String name = "devi";
	
	public StdDeviation(int windowSize, int placement) {
		this.windowSize = windowSize;
		this.placement = placement;
	}
	
	@Override
	public void generateAttribute(Data data, String fromAttribute, String toAttribute) {
		data.headers.put(toAttribute, data.headers.entrySet().size());
		int index = data.headers.get(fromAttribute);
		for (Entry<Integer, ArrayList<DataLine>> set : data.lines.entrySet()) {
			ArrayList<DataLine> values = set.getValue();
			int start = 0;
			double sum = 0.0;
			double mean = 0.0;
			double deviation = 0.0;
			ArrayList<Double> window = new ArrayList<>();
			
			for (int i = start; i < (start + windowSize); i++) {
				sum += values.get(i).data.get(index);
				window.add(values.get(i).data.get(index));
			}
			mean = sum / windowSize;
			deviation = calcDev(mean, window);
			
			for (int i = 0; i < (placement); i++) {
				values.get(i).data.add(deviation);
			}
			start++;

			while ((start + windowSize) < values.size()) {
				sum = sum
						- values.get(start - 1).data.get(index)
						+ values.get(start + windowSize).data
								.get(index);
				mean=sum/windowSize;
				
				window.remove(0);
				window.add(values.get(start + windowSize).data
								.get(index));
				deviation = calcDev(mean, window);
				values.get(start + placement - 1).data.add(deviation);
				start ++;
			}
				for (int i = values.size() - placement; i < values.size(); i++) {
					values.get(i).data.add(deviation);
			}
		}
	}
	
	private double calcDev(double mean, ArrayList<Double> window){
		double dev = 0.0;
		
		for (int i = 0; i < window.size(); i++) {
			dev = Math.pow(mean-window.get(i), 2);
		}
		dev = dev/window.size();
		dev = Math.sqrt(dev);
		return dev;
	}
	
	@Override
	public String getName() {
		return name;
	}
}
