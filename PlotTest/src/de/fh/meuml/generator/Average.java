package de.fh.meuml.generator;

import java.util.ArrayList;
import java.util.Map.Entry;

import de.fh.meuml.core.Data;
import de.fh.meuml.core.DataLine;

public class Average extends AttributeGenerator {
	private int windowSize;
	private int placement;
	public final static String name = "avg";
	public Average(int windowSize, int placement) {
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
			double avg = 0.0;

			for (int i = start; i < (start + windowSize); i++) {
				sum += values.get(i).data.get(index);
			}
			avg = sum / windowSize;

			for (int i = 0; i < (placement); i++) {
				values.get(i).data.add(avg);
			}
			start++;

			while ((start + windowSize) < values.size()) {
				sum = sum
						- values.get(start - 1).data.get(index)
						+ values.get(start + windowSize).data
								.get(index);
				avg=sum/windowSize;
				values.get(start + placement - 1).data.add(avg);
				start ++;
			}
			for (int i = values.size() - placement; i < values.size(); i++) {
				values.get(i).data.add(avg);
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}
}
