package de.fh.meuml.generator;

import java.util.ArrayList;
import java.util.Map.Entry;

import de.fh.meuml.core.Data;
import de.fh.meuml.core.DataLine;

public class Prev extends AttributeGenerator {
	private int offset = 1;
	public final static String name = "prev";
	
	public Prev(int offset) {
		this.offset = offset;
	}
	
	@Override
	public void generateAttribute(Data data, String fromAttribute, String toAttribute) {
		data.headers.put(toAttribute, data.headers.entrySet().size());
		int index = data.headers.get(fromAttribute);
		for (Entry<Integer, ArrayList<DataLine>> set : data.lines.entrySet()) {
			ArrayList<DataLine> values = set.getValue();
			
			double prev = values.get(0).data.get(index);
			double swap = values.get(offset).data.get(index);
			for (int i = 0; i < offset; i++) {
				values.get(i).data.add(swap - prev);
			}

			for (int i = offset; i < values.size(); i++) {
				prev = values.get(i - offset).data.get(index);
				swap = values.get(i).data.get(index);	
				values.get(i).data.add(swap - prev);
			}
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
}
