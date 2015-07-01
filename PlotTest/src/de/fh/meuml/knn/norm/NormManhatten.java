package de.fh.meuml.knn.norm;

import de.fh.meuml.knn.Vector;

public class NormManhatten implements INorm {

	@Override
	public double calcNorm(Vector a, Vector b) {
		double[] c = a.getVec();
		double[] d = b.getVec();
		double sum = 0;
		for (int i = 0; i < c.length; i++) {
			sum += Math.abs(c[i] - d[i]);
		}
		return sum;
	}

}
