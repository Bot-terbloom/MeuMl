package de.fh.as.neuron;

import de.fh.as.neuron.Transferfunctions.ITransferfunction;

public final class Learningalgorithms {
	private Learningalgorithms() {
	}

	public interface ILearningalgorithm {
		public double getDelta(double x, double y, double d, double m,
				double w, double age);
	}

	private static class LHebb implements ILearningalgorithm {
		@Override
		public double getDelta(double x, double y, double d, double m,
				double w, double age) {
			return (m / age) * (d - y) * x;
		}
	}

	public static ILearningalgorithm getLHebb() {
		return new LHebb();
	}

	private static class LFermi implements ILearningalgorithm {
		@Override
		public double getDelta(double x, double y, double d, double m,
				double w, double age) {
			ITransferfunction transfer = Transferfunctions.getTFermi();
			double e = d - y;
			double s = transfer.function(x * w, age);
			double s_ = s - Math.pow(s, 2);
			return m * e * x * s_;
		}
	}

	public static ILearningalgorithm getLFermi() {
		return new LFermi();
	}

	private static class LTanh implements ILearningalgorithm {
		@Override
		public double getDelta(double x, double y, double d, double m,
				double w, double age) {
			ITransferfunction transfer = Transferfunctions.getTTanh();
			double e = d - y;
			double s = transfer.function(x * w, age);
			double s_ = s - Math.pow(s, 2);
			s = 2 * s;
			return m * e * x * s_;
		}
	}

	public static ILearningalgorithm getLTanh() {
		return new LTanh();
	}
}
