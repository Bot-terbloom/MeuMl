package de.fh.meuml.generator;

import de.fh.meuml.core.Data;

public abstract class AttributeGenerator {

	public abstract void generateAttribute(Data data, String fromAttribute,
			String name);
	
	public abstract String getName();
	
}
