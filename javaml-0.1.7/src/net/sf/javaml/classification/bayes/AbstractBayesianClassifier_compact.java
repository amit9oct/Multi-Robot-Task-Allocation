/**
 * This file is part of the Java Machine Learning Library
 * 
 * The Java Machine Learning Library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * The Java Machine Learning Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Java Machine Learning Library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * Copyright (c) 2006-2012, Thomas Abeel
 * 
 * Project: http://java-ml.sourceforge.net/
 * 
 */
package net.sf.javaml.classification.bayes;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;

import net.sf.javaml.classification.AbstractClassifier;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

/**
 * Compact Abstract Bayesian classifier (supervised). This classifier calculates
 * and stores Bayesian frequencies (numerator of Bayesian probabilities) out of
 * the dataset. Specific Bayesian models build further on this class (Naive
 * Bayes, KDB, ...) The compact version doesnt use lists of sampleids: used by
 * entropy based discretisation methods.
 * 
 * @author Lieven Baeyens
 */
public class AbstractBayesianClassifier_compact extends AbstractClassifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4142042654172176726L;

	Functions fnc = new Functions();
	/* Number of classes */
	protected int numClasses;
	/* Number of features */
	protected int numFeatures;
	/* Number of training samples */
	protected int numInstances;
	/* Laplace correction */
	protected boolean laplace;
	/*
	 * Logarithmic results to avoid rounding results to zero because of limited
	 * computer precision
	 */
	protected boolean log;
	/* Sparseness of used dataset */
	protected boolean sparse;

	/* Object to store temporary results */
	protected BayesResult trainResult;
	protected Dataset trainingData;

	/* mapping int to String */
	protected Object[] classes;
	/* mapping String to Integer */
	protected HashMap<String, Integer> Classname2IndexCCountermap;
	/* initial capacity of hashmaps to avoid resizing */
	protected int initialCap;

	/**
	 * Instantiate the Compact Abstract Bayesian classifier algorithm.
	 * 
	 * 
	 * @param lap
	 *            laplace correction
	 * @param log
	 *            logarithmic results to avoid rounding results to zero because
	 *            of limited computer precision
	 * @param sparse
	 *            sparseness of used dataset
	 */
	public AbstractBayesianClassifier_compact(boolean lap, boolean log,
			boolean sparse) {

		this.laplace = lap;
		this.log = log;
		this.sparse = sparse;
		Classname2IndexCCountermap = new HashMap<String, Integer>();
	}

	/**
	 * Instantiate the Abstract Bayesian classifier building process.
	 * 
	 * 
	 * @param data
	 *            dataset to build model on
	 */

	@Override
	public void buildClassifier(Dataset data) {
		this.trainingData = data;
		trainResult = new BayesResult();
		numInstances = trainingData.size();
		numClasses = trainingData.classes().size();
		classes = new Object[numClasses];

		Iterator it = trainingData.classes().iterator();
		int cnt = 0;
		while (it.hasNext()) {
			String classname = it.next().toString();
			Classname2IndexCCountermap.put(classname, cnt);
			classes[cnt] = classname;
			cnt++;
		}

		// calculate freqs for class priors
		trainResult.setClassFreqs(calculateClassFreqs(trainingData));
		// calculate class priors
		trainResult.setClassProbs(calculateClassProbs());
		// calculate freqs for conditional probabilities
		trainResult
				.setFeatureTable_compact(conditionalFreq_compact(trainingData));

		// Handling of sparse datasets -> calculating probability of (event:)
		// a feature being absent
		// for when this feature is absent in the sample during classification
		if (sparse) {
			trainResult.setFeatureTable_compact(updateFT_compact());
		}
		// capacity of hashmaps to avoid resizing since the size is MAX
		initialCap = ((int) Math.ceil(numFeatures / 0.75) + 10);

	}

	/**
	 * Handling of sparse datasets -> calculating probability of (event:) a
	 * feature being absent for when this feature is absent in the sample during
	 * classification
	 */
	private Hashtable<Integer, Hashtable<Double, ClassCounter_compact>> updateFT_compact() {
		Hashtable<Integer, Hashtable<Double, ClassCounter_compact>> featureName_HT = trainResult
				.getFeatureTable_compact();
		double[] freq = trainResult.getClassFreqs().clone();
		for (Object key : featureName_HT.keySet()) {

			if (!featureName_HT.get(key).containsKey(012345.6789)) {
				featureName_HT.get(key).put(012345.6789,
						new ClassCounter_compact(classes.length));

				for (int k = 0; k < numClasses; k++) {
					// frequency of absent feature is calculated using existing
					// freqs of other values of this feature for each class
					featureName_HT.get(key).get(012345.6789).setCountClass(
							(freq[k] - sumOccurencesAllFVsForClass_compact(
									(Integer) key, k)), k);
				}
			}

		}
		return featureName_HT;
	}

	/**
	 * Cfr. updateFT() method
	 * 
	 */

	private double sumOccurencesAllFVsForClass_compact(int FN, int c) {
		Hashtable<Integer, Hashtable<Double, ClassCounter_compact>> featureName_HT = trainResult
				.getFeatureTable_compact();
		double Sum_score = 0.0;
		for (Object key2 : featureName_HT.get(FN).keySet()) {
			Sum_score += featureName_HT.get(FN).get(key2).getCountClass(c);
		}
		return Sum_score;
	}

	/**
	 * Calculates the frequences for class priors
	 * 
	 * @param Instances2Train
	 *            The trainingData examples from which class priors will be
	 *            estimated
	 */
	private double[] calculateClassFreqs(Dataset Instances2Train) {
		double[] classFrequencies = new double[numClasses];

		// init class counts
		for (int i = 0; i < numClasses; i++)
			classFrequencies[i] = 0;

		// increment the count of the class that each example belongs to
		for (Instance inst : Instances2Train) {
			classFrequencies[Classname2IndexCCountermap.get(inst.classValue())]++;
		}

		return classFrequencies;
	}

	/**
	 * Cfr. buildClassifier() method: calculate the class priors out of freqs
	 * 
	 */
	private double[] calculateClassProbs() {
		double[] probs = trainResult.getClassFreqs().clone();
		double[] freq = trainResult.getClassFreqs().clone();
		// divide by total number of instances to have class probabilities P(C1)
		// f.e. = #C1/(#C1+#C2)
		for (int k = 0; k < numClasses; k++) {
			// log
			probs[k] = (freq[k] + 1) / (numInstances + numClasses);

		}
		return probs;
	}

	/**
	 * Calculates the frequencies of conditional probs of each feature in the
	 * different classes
	 * 
	 * @param Instances2Train
	 *            The trainingData examples from which counts will be estimated
	 */
	private Hashtable<Integer, Hashtable<Double, ClassCounter_compact>> conditionalFreq_compact(
			Dataset Instances2Train) {
		// will still need division by total count of class frequency to become
		// the conditional probs
		// this will be done on the fly when calculating the result

		// Initialize hashtable giving conditional prob of each class given a
		// feature
		Hashtable<Integer, Hashtable<Double, ClassCounter_compact>> featureName_HT = new Hashtable<Integer, Hashtable<Double, ClassCounter_compact>>(
				numFeatures);
		Hashtable<Double, ClassCounter_compact> featureValue_CC;// = new
		// Hashtable<String,ClassCounter>();

		for (Instance inst : Instances2Train) {

			for (Object key : inst.keySet()) {

				int featureName = (Integer) key;
				Double featureValue = inst.value((Integer) key);

				if (!featureName_HT.containsKey(featureName)) {

					featureValue_CC = new Hashtable<Double, ClassCounter_compact>();
					featureValue_CC.put(featureValue, new ClassCounter_compact(
							numClasses));
					featureName_HT.put(featureName, featureValue_CC);
				} else {
					if (!featureName_HT.get(featureName).containsKey(
							featureValue)) {
						featureName_HT.get(featureName).put(featureValue,
								new ClassCounter_compact(classes.length));
					}
				}

				featureName_HT.get(featureName).get(featureValue)
						.setCountClass(
								featureName_HT.get(featureName).get(
										featureValue).getCountClass(
										Classname2IndexCCountermap.get(inst
												.classValue())) + 1,
								Classname2IndexCCountermap.get(inst
										.classValue()));

			}

		}
		numFeatures = featureName_HT.size();
		return (featureName_HT);

	}

	/**
	 * Fetches the value of a feature in the sample to classify. Including a
	 * feature absence (in sample) check
	 * 
	 * @param topology_element
	 *            current feature being handled
	 * @param inst
	 *            current Instance being handled
	 */
	protected double getInstValue(int topology_element, Instance inst) {
		if (!inst.containsKey(topology_element))
			return 012345.6789;
		else {
			return inst.get(topology_element);
		}
	}

	/**
	 * Makes negative values possible (working with logs) when calculating
	 * classification winner feature absence check
	 * 
	 * @param distribution
	 *            class score distribution during classification process
	 */
	public HashMap<Object, Double> calcFictionalChances(
			HashMap<Object, Double> distribution) {
		double smallestBuildingBlock = (100 / distribution.keySet().size());
		LinkedHashMap outS = fnc.sortHashMapByValues(distribution, true);
		int index = 1;

		for (Object key : outS.keySet()) {
			distribution.put(key, (index * smallestBuildingBlock));
			index++;
		}
		return distribution;
	}

	/**
	 * Prepares the trained datastructures for handling new (in sample)
	 * feature-values
	 * 
	 * 
	 * @param inst
	 *            current Instance being handled
	 */

	protected void coverAbsentFeatures_And_fill_helpMap_compact(Instance inst) {
		Hashtable<Integer, Hashtable<Double, ClassCounter_compact>> featureName_HT = trainResult
				.getFeatureTable_compact();
		for (Object key : inst.keySet()) {
			int featureName = (Integer) key;
			Double featureValue = inst.value((Integer) key);
			if (!featureName_HT.containsKey(featureName)) {

			} else {
				if (!featureName_HT.get(featureName).containsKey(featureValue)) {
					featureName_HT.get(featureName).put(featureValue,
							new ClassCounter_compact(classes.length));
				}
			}
		}
		trainResult.setFeatureTable_compact(featureName_HT);
	}

	/**
	 * public getter methods
	 */

	public Object[] getClassesMap() {
		return classes;
	}

	public HashMap<String, Integer> getClassesRevMap() {
		return Classname2IndexCCountermap;
	}

	public double[] getClassFreqs() {
		return trainResult.getClassFreqs().clone();

	}

	public boolean getSparse() {
		return sparse;
	}

	public Hashtable<Integer, Hashtable<Double, ClassCounter_compact>> getFeatureTable_compact() {
		return trainResult.getFeatureTable_compact();
	}

}
