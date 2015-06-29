package de.fh.as.neuron.destinations;

import de.fh.as.neuron.Neuron;

public interface IDestination {

	public abstract double get(Neuron ne);

	public boolean isValid(Neuron ne);
	
	public void printResult(Neuron ne);

	public void setNewIn(Neuron ne);
}
