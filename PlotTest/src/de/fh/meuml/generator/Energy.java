package de.fh.meuml.generator;

import java.util.ArrayList;
import java.util.Map.Entry;

import de.fh.meuml.core.Data;
import de.fh.meuml.core.DataLine;

public class Energy extends AttributeGenerator {
	private int windowSize;
	private int placement;
	public final static String name = "eng";
	
	public Energy(int windowSize, int placement) {
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

			for (int i = start; i < (start + windowSize); i++) {
				sum += Math.abs(values.get(i).data.get(index));
			}

			for (int i = 0; i < (placement); i++) {
				values.get(i).data.add(sum);
			}
			start++;

			while ((start + windowSize) < values.size()) {
				sum = sum
						- Math.abs(values.get(start - 1).data.get(index))
						+ Math.abs(values.get(start + windowSize).data
								.get(index));
				values.get(start + placement - 1).data.add(sum);
				start ++;
			}
				for (int i = values.size() - placement; i < values.size(); i++) {
					values.get(i).data.add(sum);
			}
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
}
