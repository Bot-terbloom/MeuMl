package de.fh.meuml.knn;

import java.util.Random;

import de.fh.meuml.core.DataLine.Annotation;
import de.fh.meuml.knn.gui.AbstractDrawable;

public class Vector extends AbstractDrawable {

	private double[] vec;
	public Annotation real;
	
	public Vector() {
		vec = new double[2];
	}
	
	public Vector(double[] vec){
		this.vec = vec;
	}

	public double d(double[] vec) {
		double distance = 0.0;
		double[] tmp = new double[getVec().length];
		
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = getVec()[i] - vec[i];
			distance += Math.pow(tmp[i], 2);
		}
		distance = Math.sqrt(distance);
		return distance;
	}

	@Override
	public double getX() {
		return vec[0];
	}
	@Override
	public double getY() {
		return vec[1];
	}

	@Override
	public void setX(double val) {
		this.vec[0] = val;
	}

	@Override
	public void setY(double val) {
		this.vec[1] = val;
	}

	public double[] getVec() {
		return vec;
	}

	public void setVec(double[] vec) {
		this.vec = vec;
	}
	
	private static double[]getBaseVec(){
		Random rnd = new Random();
		double[] vec = new double[2];
		double scale = AbstractDrawable.SCALE/13;
		vec[0]= rnd.nextDouble();
		vec[1]= rnd.nextDouble();
		
		vec[0] = vec[0]*scale;
		vec[1] = vec[1]*scale;
		return vec;
	}

}
