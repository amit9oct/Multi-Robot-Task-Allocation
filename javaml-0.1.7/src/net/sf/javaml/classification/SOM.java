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
package net.sf.javaml.classification;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import net.sf.javaml.clustering.SOM.GridType;
import net.sf.javaml.clustering.SOM.LearningType;
import net.sf.javaml.clustering.SOM.NeighbourhoodFunction;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.tools.DatasetTools;

/**
 * Classifier based on the Self-organized map clustering.
 * 
 * * @author Thomas Abeel
 * 
 * @see net.sf.javaml.clustering.SOM
 */
public class SOM extends AbstractClassifier {

    private static final long serialVersionUID = 6369609967132433683L;

    private net.sf.javaml.clustering.SOM som;

    /**
     * 
     * Create a new self-organizing map with the provided parameters.
     * 
     * @param xdim
     *            number of dimension on x-axis
     * @param ydim
     *            number of dimension on y-axis
     * @param grid
     *            type of grid
     * @param iterations
     *            number of iterations
     * @param learningRate
     *            learning rate of the algorithm
     * @param initialRadius
     *            initial radius
     * @param learning
     *            type of learning to use
     * @param nbf
     *            neighborhood function
     */
    public SOM(int xdim, int ydim, GridType grid, int iterations, double learningRate, int initialRadius,
            LearningType learning, NeighbourhoodFunction nbf) {
        som = new net.sf.javaml.clustering.SOM(xdim, ydim, grid, iterations, learningRate, initialRadius, learning, nbf);

    }

    private Instance[] centroids;

    private Vector<Map<Object, Double>> distribution;

    @Override
    public void buildClassifier(Dataset data) {
        Dataset[] clusters = som.cluster(data);
        centroids = new Instance[clusters.length];
        distribution = new Vector<Map<Object, Double>>();
        for (int i = 0; i < clusters.length; i++) {
            centroids[i] = DatasetTools.average(clusters[i]);
            distribution.add(distribution(data, clusters[i]));
        }
    }

    private Map<Object, Double> distribution(Dataset original, Dataset dataset) {
        Map<Object, Double> out = new HashMap<Object, Double>();
        for (Object o : original.classes()) {
            out.put(o, 0.0);
        }
        for (Instance i : dataset) {
            out.put(i.classValue(), out.get(i.classValue()) + (1.0 / dataset.size()));
        }
        return out;
    }

    @Override
    public Map<Object, Double> classDistribution(Instance inst) {
        double min = Double.POSITIVE_INFINITY;
        EuclideanDistance ed = new EuclideanDistance();
        int index = 0;
        for (int i = 0; i < centroids.length; i++) {
            double d = ed.measure(centroids[i], inst);
            if (d < min) {
                d = min;
                index = i;
            }
        }
        return distribution.get(index);
    }

}
