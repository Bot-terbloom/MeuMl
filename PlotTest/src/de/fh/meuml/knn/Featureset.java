package de.fh.meuml.knn;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Featureset extends Vector{

	private ArrayList<Vector> features = new ArrayList<>();
	
	public Featureset(double[]... feature) {
		this.addFeatures(feature);
		this.setVec(getCentroid());
		this.setColor(Color.BLUE);
		this.setH(getH()*3);
	}

	public Vector getCentroidVector() {
		Vector result = new Vector(getCentroid());
		result.setColor(Color.MAGENTA);
		return result;
	}

	public double[] getCentroid() {
		if (getFeatures().isEmpty()) {
			return null;
		}
		int dim = getFeatures().get(0).getVec().length;
		double[] sum = new double[dim];

		for (int i = 0; i < getFeatures().size(); i++) {
			for (int j = 0; j < dim; j++) {
				sum[j] += getFeatures().get(i).getVec()[j];
			}
		}
		for (int i = 0; i < sum.length; i++) {
			sum[i] = sum[i] / getFeatures().size();
		}
		return sum;
	}

	public ArrayList<Vector> getFeatures() {
		return features;
	}

	public void addFeatures(double[]... feature) {

		for (int i = 0; i < feature[0].length; i++) {
			double[] vec = new double[feature.length];
			for (int j = 0; j < feature.length; j++) {

				vec[j] = feature[j][i];
			}
			addFeature(new Vector(vec));
		}
		this.setVec(getCentroid());
	}
	
	public void addFeature(Vector vec){
		features.add(vec);
		this.setVec(getCentroid());
	}
	
	public void addFeature(Featureset fs){
		features.addAll(fs.getFeatures());
		this.setVec(getCentroid());
	}

	public void draw(Graphics2D g2){
		if (features.isEmpty())
			return;
			for (int i = 0; i < features.size(); i++) {
				features.get(i).draw(g2);
			}
//		super.draw(g2);
	}

	public void setColor(Color color){
		super.setColor(color);
		for (int i = 0; i < features.size(); i++) {
			features.get(i).setColor(color);
		}
	}

	public Vector getFeature(int i){
		return features.get(i);
	}
	
	public int size(){
		return features.size();
	}
}