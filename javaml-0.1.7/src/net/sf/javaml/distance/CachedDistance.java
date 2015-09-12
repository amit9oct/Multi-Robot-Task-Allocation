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

import java.util.HashMap;

import net.sf.javaml.core.Instance;
import be.abeel.util.Pair;

/**
 * This class implements a wrapper around other distance measure to cache
 * previously calculated distances.
 * 
 * This should only be used with time consuming distance measures. For Euclidean
 * distance for example it is faster to recalculate it each time.
 * 
 * @author Thomas Abeel
 * 
 */
public class CachedDistance implements DistanceMeasure {

	/**
     * 
     */
	private static final long serialVersionUID = 8794275694780229816L;
	private DistanceMeasure dm = null;

	public CachedDistance(DistanceMeasure dm) {
		this.dm = dm;
	}

	// row map
	HashMap<Pair<Instance, Instance>, Double> cache = new HashMap<Pair<Instance, Instance>, Double>();

	public double measure(Instance i, Instance j) {
		Pair<Instance, Instance> pair = new Pair<Instance, Instance>(i, j);
		if (cache.containsKey(pair)) {
			return cache.get(pair);
		} else {
			double dist = dm.measure(i, j);
			cache.put(pair, dist);
			return dist;
		}
	}

	public boolean compare(double x, double y) {
		return dm.compare(x, y);
	}

	@Override
	public double getMinValue() {
		return dm.getMinValue();
	}

	@Override
	public double getMaxValue() {
		return dm.getMaxValue();
	}

}
