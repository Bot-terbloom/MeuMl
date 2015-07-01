package de.fh.meuml.knn.norm;

import de.fh.meuml.knn.Vector;

public class NormEuklid implements INorm{

	@Override
	public double calcNorm(Vector a, Vector b) {
		double[]c = a.getVec();
		double[]d = b.getVec();
		double sum = 0;
		for (int i = 0; i < c.length; i++) {
			sum+=Math.pow(c[i] - d[i], 2);
		}
		sum = Math.sqrt(sum);
		return sum;
	}

}
