package de.fh.as.neuron;

import java.text.DecimalFormat;
import java.util.Random;

import de.fh.as.neuron.Learningalgorithms.ILearningalgorithm;
import de.fh.as.neuron.Transferfunctions.ITransferfunction;
import de.fh.as.neuron.destinations.IDestination;

/**
 * Diese Klasse bildet ein Neuron ab, wie es in der Veranstaltung Adaptive
 * Systeme WS/SS 2015 behandelt wurde.
 * 
 * @author hankeb
 */
public class Neuron {
	private static Random rnd = new Random();
	private static DecimalFormat df = new DecimalFormat("###,##0.000000");
	
	/*
	 * set EPSILON to 0.4 for heavy-side-experience
	 */
	public static double EPSILON = 0.0001;
	/**
	 * Eingänge des Neuron
	 */
	private double in[];

	/**
	 * Ausgang des Neuron
	 */
	private double out = 0.0;

	/**
	 * Gewichtung der Eingänge
	 */
	private double weights[];

	/**
	 * Mathematische Hilfsvariable zum Verschieben der klassifizierneden
	 * Hyoerebene in den Koordinatenursprung.
	 */
	private double teta = 0.5;

	private double delta = 0.0;
	/**
	 * Die vom Neuron verwendete Transferfunktion
	 */
	private ITransferfunction transfer;

	private double age = 1;
	private double aging = 0.0001;

	private double learningrate = 0.1;
	private IDestination dst;
	private ILearningalgorithm algorithm;
	private boolean verbose = false;

	/**
	 * Standardkonstruktor
	 */
	@SuppressWarnings("unused")
	private Neuron() {
	}

	public Neuron(int n) {
		setInCount(n);
		this.setTeta(teta);
		this.setLearningRate(teta/5);
		this.setTransfer(Transferfunctions.getTStep());
		this.setAlgorithm(Learningalgorithms.getLHebb());
		this.setRndWeights();
	}
	public Neuron(IDestination dst, int n) {
		this(n);
		this.dst = dst;
	}

	/**
	 * Ermitteln der gewichteten Summe der Eingangssignale einschließlich Teta.
	 * 
	 * @return gewichtete Summe.
	 */
	public double weightedSum() {
		double sum = 0.0;

		for (int i = 0; i < weights.length; i++) {
			sum += in[i] * weights[i];
		}
		return sum - teta;
	}

	/**
	 * "Schaltet" das Neuron
	 */
	private void execute() {
		if (in.length != weights.length) {
			weights = new double[in.length];
			this.setNewIn();
		}
		this.out = getTransfer().function(weightedSum(), getAge());
	}

	/**
	 * Hilfsfunktion: Setzt die Gewichte auf zufällige Werte.
	 */
	private void setRndWeights() {
		for (int i = 0; i < in.length; i++) {
			this.weights[i] = rnd.nextDouble();
		}
	}

	/**
	 * Setzt die neue Eingänge
	 * 
	 * @param n
	 *            Anzahl der zu setzenden Eingänge
	 */
	public void setInCount(int n) {
		this.in = new double[n];
		this.weights = new double[n];
	}

	/**
	 * Ausgabe des Zustands des Neurons auf der Konsole.
	 */
	public void printConfig() {
		System.out.print("Teta: " + df.format(this.getTeta()));

		System.out.print("  Weights: ");
		System.out.print("{");
		for (int i = 0; i < in.length; i++) {
			System.out.print(df.format(weights[i]));
			if (i < in.length - 1)
				System.out.print(", ");
		}
		System.out.print("} " + "  Age(Aging): " + df.format(this.getAge())
				+ "(" + this.getAging() + ")");
	}

	/**
	 * Ausgabe der Ein und Ausgänge des Neurons auf der Konsole.
	 */
	public void printStatus() {
		System.out.print("\t");
		for (int i = 0; i < in.length; i++) {
			System.out.print(df.format(getIn(i)));
			if (i < in.length - 1)
				System.out.print("; ");
		}
		System.out.println(" -> " + df.format(getOut()));
	}

	public void learn(int n) {
		learn(n, true);
	}

	public void learn(int n, boolean test) {
//		int c = 0;

		for (int i = 0; i < n; i++) {
			setNewIn();
			this.train();
//			c++;
			if (test && this.isValid()) {
				break;
			}
		}
//		System.out.println("\nTrainingsvorgänge: " + c);
	}

	public void train() {
		double out = 0;
		double in = 0;
		double w = 0;
		double age = 0;

		double d = 0;
		double delta = 0;

		d = this.getDstVal();
		out = this.getOut();
		age = this.getAge();

		for (int i = 0; i < this.getInCount(); i++) {
			in = this.getIn(i);
			w = this.getWeight(i);
			delta = getAlgorithm().getDelta(in, out, d, getLearnRate(), w, age);
			this.addWeight(delta, i);
		}
		this.addTeta(getAlgorithm().getDelta(-1, out, d, getLearnRate(),
				this.getTeta(), age));

		if (this.isVerbose()) {
			this.printConfig();
			this.printStatus();
		}
		this.age();
	}

	public void setNewIn() {
		this.dst.setNewIn(this);
	}

	protected double getDstVal() {
		return dst.get(this);
	}

	public IDestination getDestination() {
		return this.dst;
	}

	public void printResult() {
		dst.printResult(this);
	}

	public double getLearnRate() {
		return learningrate;
	}

	public void setLearningRate(double learningrate) {
		this.learningrate = learningrate;
	}

	public boolean isValid() {
		return this.dst.isValid(this);
	}

	public double getOut() {
		return out;
	}

	public double getIn(int i) {
		return in[i];
	}

	public double[] getIn() {
		return in;
	}

	public void setIn(double input[]) {
		in = input;
		this.execute();
	}

	public void setIn(double val, int i) {
		this.in[i] = val;
		this.execute();
	}

	public double getWeight(int i) {
		return weights[i];
	}

	public double[] getWeights() {
		return weights;
	}
	// public void setWeights(double w[]) {
	// weights = w;
	// this.execute();
	// }

	public void setWeight(double w, int i) {
		this.weights[i] = w;
		this.execute();
	}
	
	public void setWeights(double[] w) {
		this.weights = w;
		this.execute();
	}

	public void addWeight(double delta, int i) {
		this.weights[i] += delta;
	}

	public double getTeta() {
		return teta;
	}

	public void setTeta(double teta) {
		this.teta = teta;
	}

	public void addTeta(double delta) {
		this.teta += delta;
	}

	public int getInCount() {
		return in.length;
	}

	public ITransferfunction getTransfer() {
		return transfer;
	}

	public void setTransfer(ITransferfunction transfer) {
		this.transfer = transfer;
	}

	public double getAge() {
		return age;
	}

	public void setAge(double age) {
		this.age = age;
	}

	public void age() {
		age = age + this.aging;
	}

	public double getAging() {
		return aging;
	}

	public void setAging(double aging) {
		this.aging = aging;
	}

	public ILearningalgorithm getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(ILearningalgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public void configure(double age, double aging, double rate) {
		this.setAge(age);
		this.setAging(aging);
		this.setLearningRate(rate);
	}

	public void configure(double age, double aging) {
		this.setAge(age);
		this.setAging(aging);
	}
	
	public void setDestination(IDestination dst){
		this.dst = dst;
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}
}