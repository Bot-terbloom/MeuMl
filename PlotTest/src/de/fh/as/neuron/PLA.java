package de.fh.as.neuron;

import de.fh.as.neuron.destinations.IDestination;

public class PLA extends Neuron {

	public PLA(IDestination dst, int n, double theta) {
		super(dst, n);
		this.setTransfer(Transferfunctions.getTSign());
		this.setAlgorithm(Learningalgorithms.getLHebb());
		this.configure(1, 0.0, 0.005);
		this.setTeta(theta);
	}

}
