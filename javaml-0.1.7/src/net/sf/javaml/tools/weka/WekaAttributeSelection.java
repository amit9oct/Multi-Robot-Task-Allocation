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
package net.sf.javaml.tools.weka;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.featureselection.FeatureRanking;
import net.sf.javaml.featureselection.FeatureScoring;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.AttributeEvaluator;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

/**
 * Provides a bridge between Java-ML and the Attribute Selection algorithms in
 * WEKA.
 * 
 * @author Irwan Krisna
 */
public class WekaAttributeSelection implements FeatureScoring,
		FeatureRanking {

	/* variable for attribute selection */
	private AttributeSelection attrsel;

	/* variable for Weka's ASEvaluation */
	private ASEvaluation evaluator;

	/* variable for Weka's ASSearch */
	private ASSearch Searcher;

	/* variable for Weka's Attribute Evaluator */
	private AttributeEvaluator attreval;

	/* variable for Number of attributes */
	public int num_attr;

	/* variable for Number of attributes */
	private Instances newData;

	/* variable for score of each attribute */
	private double scoring;

	/* variable for ranking of each attribute */
	private int ranking;

	/* variable for attribute number */
	private int attr_number;

	public HashMap<Integer, Integer> numbers = new HashMap<Integer, Integer>();

	/*
	 * Build Constructor for attribute selection, AS evaluation, AS searcher and
	 * attribute evaluator
	 */
	public WekaAttributeSelection(ASEvaluation evaluator,
			ASSearch searcher) {
		this.attrsel = new AttributeSelection();
		this.evaluator = evaluator;
		this.Searcher = searcher;
		this.attreval = (AttributeEvaluator) evaluator;
	}

	public void build(Dataset data) {
		try {
			/*
			 * convertdata to and from the WEKA format.
			 */
			Instances inst = new ToWekaUtils(data).getDataset();
			/*
			 * set the attribute/subset evaluator, search and the format of the
			 * input instances.
			 */
			attrsel.setEvaluator(evaluator);
			attrsel.setSearch(Searcher);
			attrsel.setInputFormat(inst);

			/*
			 * Produce a new data
			 */

			newData = Filter.useFilter(inst, attrsel);
			num_attr = newData.numAttributes();
			/*
			 * Generate a map for Attributes' number and their ranks
			 */
			for (int a = 0; a < num_attr - 1; a++) {
				char ff = newData.attribute(a).name().charAt(3);
				attr_number = Integer.parseInt(Character.toString(ff));

				numbers.put(attr_number, new Integer(a));
			}

		} catch (Exception ex) {
			Logger.getLogger(WekaAttributeSelection.class.getName()).log(
					Level.SEVERE, null, ex);
		}

	}

	public int noAttributes() {

		return num_attr;
	}

	public double score(int attribute) {
		try {
			scoring = attreval.evaluateAttribute(attribute);
		} catch (Exception ex) {
			Logger.getLogger(WekaAttributeSelection.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		return scoring;
	}

	public int rank(int attIndex) {
		ranking = (Integer) numbers.get(attIndex);
		return ranking;
	}

}
