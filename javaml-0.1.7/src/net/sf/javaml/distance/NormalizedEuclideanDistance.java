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

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.DatasetTools;

/**
 * A normalized version of the Euclidean distance. This distance measure is
 * normalized in the interval [0,1].
 * 
 * High values denote low similar items (high distance) and low values denote
 * highly similar items (low distance).
 * 
 * @author Thomas Abeel
 * 
 */
public class NormalizedEuclideanDistance extends EuclideanDistance {

    /**
     * 
     */
    private static final long serialVersionUID = -6489071802740149683L;

    private Dataset data;

    public NormalizedEuclideanDistance(Dataset data) {
        super();
        this.data = data;
    }

    public double measure(Instance i, Instance j) {
        Instance min = DatasetTools.minAttributes(data);
        Instance max = DatasetTools.maxAttributes(data);

        Instance normI = normalizeMidrange(0.5, 1, min, max, i);
        Instance normJ = normalizeMidrange(0.5, 1, min, max, j);
        return super.calculateDistance(normI, normJ) / Math.sqrt(i.noAttributes());

    }

    private Instance normalizeMidrange(double normalMiddle, double normalRange, Instance min, Instance max,
            Instance instance) {
        double[] out = new double[instance.noAttributes()];
        for (int i = 0; i < out.length; i++) {
            double range = Math.abs(max.value(i) - min.value(i));
            double middle = Math.abs(max.value(i) + min.value(i)) / 2;
            out[i] = ((instance.value(i) - middle) / range) * normalRange + normalMiddle;
        }
        return new DenseInstance(out, instance);
    }
}
