package de.fh.as.neuron.destinations;

import java.util.Random;

import de.fh.as.neuron.Neuron;

public class DstFeature implements IDestination {

	private double[][] desire = null; // laufen -> 1
	private double[][] fail = null;	// still -> -1
	double dst;

	public DstFeature(double[][] desire, double[][] fail){
		this.desire = desire;
		this.fail = fail;
	}
	
	@Override
	public double get(Neuron ne) {
		return dst;
	}

	@Override
	public boolean isValid(Neuron ne) {
		return Neuron.EPSILON>Math.abs(ne.getOut()-dst);
	}

	@Override
	public void printResult(Neuron ne) {
		// TODO Automatisch generierter Methodenstub
	}

	@Override
	public void setNewIn(Neuron ne) {
		double[][] tmp = this.getFail();
		double []val = new double[tmp.length];
		int index;
		dst = -1;

		if (new Random().nextBoolean()) {
			tmp = this.getDesire();
			dst = 1;
		}
		index = new Random().nextInt(tmp.length);
		for (int i = 0; i < tmp.length; i++) {
			val[i]= tmp[i][index];
		}
		ne.setIn(val);
	}

	public double[][] getDesire() {
		return desire;
	}

	public void setDesire(double[][] desire) {
		this.desire = desire;
	}

	public double[][] getFail() {
		return fail;
	}

	public void setFail(double[][] fail) {
		this.fail = fail;
	}

}
