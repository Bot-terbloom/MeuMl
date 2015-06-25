package de.fh.meuml.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;

import de.fh.meuml.core.Data;
import de.fh.meuml.core.DataLine;

public class Median extends AttributeGenerator {
	private int windowSize;
	private int placement;
	
	public Median(int windowSize, int placement) {
		this.windowSize = windowSize;
		this.placement = placement;
	}
	
	@Override
	public void generateAttribute(Data data, String fromAttribute, String toAttribute) {
		data.headers.put(toAttribute, data.headers.entrySet().size());
		int index = data.headers.get(fromAttribute);
		
		for (Entry<Integer, ArrayList<DataLine>> set : data.lines.entrySet()) {
			ArrayList<DataLine> values = set.getValue();
			ArrayList<Double> list = new ArrayList<Double>();
			ArrayList<Double> tmpList = null;
			int start = 0;
			double median = 0.0;

			for (int i = start; i < (start + windowSize); i++) {
				list.add(values.get(i).data.get(index));
			}
			tmpList = new ArrayList<Double>(list);
			Collections.sort(tmpList);
			median = tmpList.get(placement - 1);
			for (int i = 0; i < (placement); i++) {
				values.get(i).data.add(median);
			}
			start++;

			while ((start + windowSize) < values.size()) {
				list.remove(0);
				list.add(values.get(start + windowSize).data.get(index));
				tmpList = new ArrayList<Double>(list);
				Collections.sort(tmpList);
				median = tmpList.get(placement - 1);
				values.get(start + placement - 1).data.add(median);
				start++;
			}
			for (int i = values.size() - placement; i < values.size(); i++) {
				values.get(i).data.add(median);
			}
		}
	}
}
