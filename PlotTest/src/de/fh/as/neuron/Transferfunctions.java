package de.fh.as.neuron;

public final class Transferfunctions {

	private Transferfunctions(){}
	
	public interface ITransferfunction {
		public double function(double x, double lambda);
	}
	
	private static class TSign implements ITransferfunction{
		@Override
		public double function(double x, double lambda) {
			double result = -1;
			if (x >= 0.0)
				result = 1.0;
			return result;
		}
	}

	public static ITransferfunction getTSign(){
		return new TSign();
	}
	
	private static class TStep implements ITransferfunction{
		@Override
		public double function(double x, double lambda) {
			double result = 0;
			if (x >= 0.0)
				result = 1.0;
			return result;
		}
	}
	
	public static ITransferfunction getTStep(){
		return new TStep();
	}
	
	private static class TFermi implements ITransferfunction{
		@Override
		public double function(double x, double lambda) {
			double exp = (-1.0)*lambda*x;
			return (1.0/(1.0+Math.pow(Math.E, exp)));
		}
	}
	
	public static ITransferfunction getTFermi(){
		return new TFermi();
	}
	
	private static class TTanh implements ITransferfunction{
		@Override
		public double function(double x, double lambda) {
			return (Math.tanh(lambda*x)+1)/2;
		}
	}
	
	public static ITransferfunction getTTanh(){
		return new TTanh();
	}
}
