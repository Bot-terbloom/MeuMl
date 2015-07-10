 package de.fh.as.neuron;

import de.fh.as.neuron.destinations.DstFeature;
import de.fh.as.neuron.destinations.IDestination;

public class Pocket extends PLA {

	private double[][] desire = null; // laufen -> 1
	private double[][] fail = null; // still -> -1

	public Pocket(DstFeature dst, int n, double theta) {
		super(dst, n, theta);
		this.desire = dst.getDesire();
		this.fail = dst.getFail();
	}

	@Override
	public void train() {
		double[] backup;
		double err1, err2;

		backup = getWeights();
		err1 = errRate();
		setNewIn();
		super.train();

		err2 = errRate();

		if (err1 < err2)
			setWeights(backup);
	}

	private double errRate() {
		double err = 0.0;
		double []val = new double[desire.length];
		for (int i = 0; i < desire.length; i++) {

			for (int j = 0; j < desire[i].length; j++) {
				val[i]=desire[i][j];
				this.setIn(val);
				
			}
			err += Math.abs(this.getOut() - getDstVal());
			for (int j = 0; j < fail[i].length; j++) {
				val[i]=fail[i][j];
				this.setIn(val);
				
			}
			err += Math.abs(this.getOut() - getDstVal());
		}
		err = err / (fail.length + desire.length);

		return err;
	}

	// public void train_() {
	// double out = 0;
	// double in = 0;
	// double w = 0;
	// double age = 0;
	//
	// double d = 0;
	// double delta = 0;
	//
	// d = this.getDstVal();
	// out = this.getOut();
	// age = this.getAge();
	//
	// for (int i = 0; i < this.getInCount(); i++) {
	// in = this.getIn(i);
	// w = this.getWeight(i);
	// delta = getAlgorithm().getDelta(in, out, d, getLearnRate(), w, age);
	// this.addWeight(delta, i);
	// }
	// this.addTeta(getAlgorithm().getDelta(-1, out, d, getLearnRate(),
	// this.getTeta(), age));
	//
	// if (this.isVerbose()) {
	// this.printConfig();
	// this.printStatus();
	// }
	// this.age();
	// }

}
