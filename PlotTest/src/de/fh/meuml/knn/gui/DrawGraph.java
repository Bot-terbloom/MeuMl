package de.fh.meuml.knn.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;

import javax.swing.*;

import de.fh.meuml.core.Data;
import de.fh.meuml.core.DataLine.Annotation;
import de.fh.meuml.generator.Energy;
import de.fh.meuml.knn.Featureset;
import de.fh.meuml.knn.KNN;
import de.fh.meuml.knn.Vector;
import de.fh.meuml.knn.norm.NormEuklid;
import de.fh.meuml.knn.norm.NormManhatten;

@SuppressWarnings("serial")
public class DrawGraph extends JPanel {
	DecimalFormat df = new DecimalFormat("###,##0.0");
	public static int heigth = 600;
	public static int width = 1400;

	public static int offsetH = heigth/8;
	public static int offsetW = width/8;
	
	static Featureset fs;
	static String text = "";
	static int n = 1000;

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.draw(new Line2D.Double(0, heigth -offsetH, width, heigth -offsetH));
		g2.draw(new Line2D.Double(offsetW, heigth, offsetW, 0));

		if (fs != null)
			fs.draw(g2);

		int offset = 50;
		int tab = 50;
		int space = 20;

		g2.setColor(Color.BLACK);
		g2.drawString(text, width/2-100, offset);

		String tmp = "Energy AccX";
		g2.drawString(tmp, 50, offset + space);

		tmp = "Energy AccY";
		g2.drawString(tmp, width-offsetW, heigth-offsetH+20);
	}

	public static void main(String[] args) {
		run(5);
	}

	public static void run(int k) {
		int timeout = 0;
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new DrawGraph());
		f.setSize(width, heigth);
		f.setLocation(100, 50);
		f.setVisible(true);

		int sensorId = 1324189;

		String nameLaufen = "laufen";
		String nameGehen = "gehen";
		String nameDrehen = "drehen";
		//String basepath = "measurements/2015.04.30/08/";
		String basepath = "measurements\\2015.04.30\\08\\";
		// 2-1300;3000
		Data laufen1 = Data.getDataFromFile(basepath + "laufen.csv",
				nameLaufen + 1, Annotation.Laufen);
		Data drehen1 = Data.getDataFromFile(basepath + "drehen.csv",
				nameDrehen + 1, Annotation.Drehen);
		Data gehen1 = Data.getDataFromFile(basepath + "gehen.csv",
				nameGehen + 1, Annotation.Gehen);

		//basepath = "measurements/2015.04.30/20/";
		basepath = "measurements\\2015.04.30\\20\\";
		Data laufen2 = Data.getDataFromFile(basepath + "laufen.csv",
				nameLaufen + 2, Annotation.Laufen);
		Data drehen2 = Data.getDataFromFile(basepath + "drehen.csv",
				nameDrehen + 2, Annotation.Drehen);
		Data gehen2 = Data.getDataFromFile(basepath + "gehen.csv",
				nameGehen + 2, Annotation.Gehen);

		laufen1.generateFeature();
		gehen1.generateFeature();
		drehen1.generateFeature();

		laufen2.generateFeature();
		gehen2.generateFeature();
		drehen2.generateFeature();

		double[] dataL11 = laufen1.getAttribute(Data.Fields.AccelX,
				Energy.name, sensorId);
		double[] dataL12 = laufen1.getAttribute(Data.Fields.AccelY,
				Energy.name, sensorId);

		double[] dataG11 = gehen1.getAttribute(Data.Fields.AccelX, Energy.name,
				sensorId);
		double[] dataG12 = gehen1.getAttribute(Data.Fields.AccelY, Energy.name,
				sensorId);

		double[] dataL21 = laufen2.getAttribute(Data.Fields.AccelX,
				Energy.name, sensorId);
		double[] dataL22 = laufen2.getAttribute(Data.Fields.AccelY,
				Energy.name, sensorId);

		double[] dataG21 = gehen2.getAttribute(Data.Fields.AccelX, Energy.name,
				sensorId);
		double[] dataG22 = gehen2.getAttribute(Data.Fields.AccelY, Energy.name,
				sensorId);

		Featureset fs1 = new Featureset(dataL11, dataL12);
		fs1.setColor(Color.GREEN);

		Featureset fs2 = new Featureset(dataG11, dataG12);
		fs2.setColor(Color.BLUE);

		fs = fs1;
		text = "Laufen";
		f.repaint();
		wait(timeout);

		fs = fs2;
		text = "Gehen";
		f.repaint();
		wait(timeout);

		fs1.addFeature(fs2);

		Featureset uc1 = new Featureset(dataL21, dataL22);
		uc1.setAnnotation(Annotation.Laufen);
		Featureset uc2 = new Featureset(dataG21, dataG22);
		uc2.setAnnotation(Annotation.Gehen);
		uc1.addFeature(uc2);
		uc1.setColor(Color.MAGENTA);

		KNN knn = new KNN(k, new NormEuklid(), fs1);

		fs = knn.getTrainingsets().get(0);
		text = "Trainingsdaten";
		f.repaint();
		wait(timeout);

		fs = uc1;
		text = "Eingabe";
		f.repaint();
		wait(timeout);

		knn.classify(uc1);

		fs = uc1;
		text = "Klassifizierung";
		f.repaint();
		wait(timeout);

		System.out.println();
		System.out.println("Qualität kNN (" + k + "): " + knn.eval.toString());

		while (true) {
			wait(1000);
			// f.repaint();
		}
	}

	public static void wait(int n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
