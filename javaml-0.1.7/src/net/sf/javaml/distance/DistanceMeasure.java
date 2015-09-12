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
package net.sf.javaml.distance;

import java.io.Serializable;

import net.sf.javaml.core.Instance;

/**
 * A distance measure is an algorithm to calculate the distance, similarity or
 * correlation between two instances.
 * 
 * There are three types of distance measures: distance, similarity and
 * correlation measures.
 * 
 * Some distance measures are normalized, i.e. in the interval [0,1], but this
 * is not required by the interface.
 * 
 * 
 * @see net.sf.javaml.distance.AbstractDistance
 * @see net.sf.javaml.distance.AbstractSimilarity
 * @see net.sf.javaml.distance.AbstractCorrelation
 * 
 * 
 * @author Thomas Abeel
 * 
 */
public interface DistanceMeasure extends Serializable {
	/**
	 * Calculates the distance between two instances.
	 * 
	 * @param i
	 *            the first instance
	 * @param j
	 *            the second instance
	 * @return the distance between the two instances
	 */
	public double measure(Instance x, Instance y);

	/**
	 * Returns whether the first distance, similarity or correlation is better
	 * than the second distance, similarity or correlation.
	 * 
	 * Both values should be calculated using the same measure.
	 * 
	 * For similarity measures the higher the similarity the better the measure,
	 * for distance measures it is the lower the better and for correlation
	 * measure the absolute value must be higher.
	 * 
	 * @param x
	 *            the first distance, similarity or correlation
	 * @param y
	 *            the second distance, similarity or correlation
	 * @return true if the first distance is better than the second, false in
	 *         other cases.
	 */
	public boolean compare(double x, double y);

	/**
	 * Returns the value that this distance metric produces for the lowest
	 * distance or highest similarity. This is mainly useful to initialize
	 * variables to be used in comparisons with the compare method of this
	 * class.
	 * 
	 * 
	 * 
	 * @return minimum possible value of the distance metric
	 */
	public double getMinValue();

	/**
	 * Returns the value that this distance metric produces for the highest
	 * distance or lowest similarity. This is
	 * mainly useful to initialize variables to be used in comparisons with the
	 * compare method of this class.
	 * 
	 * @return maximum possible value of the distance metric
	 */
	public double getMaxValue();
}
