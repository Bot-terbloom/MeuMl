package de.fh.meuml.knn.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class AbstractDrawable {
	private double x = 0;
	private double y = 0;
	private double h = 2;
	private Color color = Color.GREEN;
	public static double SCALE = -0.6;

	public AbstractDrawable() {
	}

	public AbstractDrawable(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void draw(Graphics2D g2) {
		double xBase = DrawGraph.offsetW;
		double x = (SCALE * (getX()) + getH() / 2);
		double yBase = DrawGraph.heigth - DrawGraph.offsetH;
		double y = (SCALE * getY() - getH() / 2);
		Ellipse2D.Double circle = new Ellipse2D.Double(xBase - x, yBase + y,
				getH(), getH());
		g2.setColor(Color.BLACK);
		if (getH() >= 10) {
			g2.draw(new Line2D.Double(xBase - x - getH() / 2, yBase + y
					+ getH() / 2, xBase - x + getH() * 3 / 2, yBase + y
					+ getH() / 2));
			g2.draw(new Line2D.Double(xBase - x + getH() / 2, yBase + y
					- getH() / 2, xBase - x + getH() / 2, yBase + y + getH()
					* 3 / 2));
		}
		g2.setColor(this.color);
		g2.fill(circle);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}
}
