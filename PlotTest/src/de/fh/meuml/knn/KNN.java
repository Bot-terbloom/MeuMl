package de.fh.meuml.knn;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.fh.meuml.core.DataLine.Annotation;
import de.fh.meuml.core.Evaluation;
import de.fh.meuml.knn.norm.INorm;

public class KNN {

	private ArrayList<Featureset> trainingsets = new ArrayList<>();
	INorm norm;
	int k = 1;
	public Evaluation eval = new Evaluation();

	public KNN(int k, INorm norm, Featureset... trainingset) {
		this.k = k;
		this.norm = norm;
		for (int i = 0; i < trainingset.length; i++) {
			this.getTrainingsets().add(trainingset[i]);
		}
	}

	public class Neighbours {
		ArrayList<Vector> neighbours = new ArrayList<>();
		int k = 1;
		Vector exemplar;

		@SuppressWarnings("unused")
		private Neighbours() {
		}

		public Neighbours(Vector vec, int k) {
			this.exemplar = vec;
			this.k=k;
		}

		public ArrayList<Vector> getNeighbours() {
			return this.neighbours;
		}

		public void findNearestNeighbours() {
			for (int i = 0; i < getTrainingsets().size(); i++) {
				for (int j = 0; j < getTrainingsets().get(i).size(); j++) {
					testAndInsert((getTrainingsets().get(i)).getFeature(j));	
				}
				
			}
		}

		private void testAndInsert(Vector vec) {
			Vector runner = vec;
			Vector tmp;

			if (neighbours.size() <= k) {
				neighbours.add(vec);
				return;
			}
			for (int i = 0; i < neighbours.size(); i++) {
				if (norm.calcNorm(runner, exemplar) < norm.calcNorm(
						neighbours.get(i), exemplar)) {
					tmp = runner;
					runner = neighbours.get(i);
					neighbours.set(i, tmp);
				}
			}
		}
	}

	public void addTrainingsdata(Featureset... trainingsdata) {
		for (int i = 0; i < trainingsdata.length; i++) {
			this.getTrainingsets().add(trainingsdata[i]);
		}
	}

	private void classify(Vector vec) {
		HashMap<Color, Integer> classes = new HashMap<>();
		Color color = Color.MAGENTA;
		Neighbours tmp = new Neighbours(vec, k);
		tmp.findNearestNeighbours();
		ArrayList<Vector> nb = tmp.getNeighbours();
		int max = 0;

		for (int i = 0; i < nb.size(); i++) {
			color = nb.get(i).getColor();
			if (classes.containsKey(color)) {
				classes.put(color, classes.get(color) + 1);
			} else {
				classes.put(color, 1);
			}
		}

		Iterator<?> it = classes.entrySet().iterator();
		int count;

		while (it.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<Color, Integer> pair = (Entry<Color, Integer>) it.next();
			count = ((Integer) pair.getValue()).intValue();
			if (max < count) {
				max = count;
				color = pair.getKey();
			}
			it.remove(); // avoids a ConcurrentModificationException
		}
		vec.setColor(color);
		Annotation guessed = Annotation.Keine;
		if (color == Color.GREEN) {
			guessed = Annotation.Laufen;
		} else if(color == Color.BLUE) {
			guessed = Annotation.Gehen;
		}
		eval.count(guessed, vec.real);
		//Laufen = Grün, Gehen = Blau, Testset = Magenta
	}
	
	public void classify(Featureset fs){
		ArrayList<Vector>fl = fs.getFeatures();
		for (int i = 0; i < fl.size(); i++) {
			classify(fl.get(i));
		}
	}

	public ArrayList<Featureset> getTrainingsets() {
		return trainingsets;
	}

	public void setTrainingsets(ArrayList<Featureset> trainingsets) {
		this.trainingsets = trainingsets;
	}
}
