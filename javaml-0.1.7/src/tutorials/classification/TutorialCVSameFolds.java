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
package tutorials.classification;

import java.io.File;
import java.util.Map;
import java.util.Random;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.FileHandler;

/**
 * This tutorial shows how you can do multiple cross-validations with the same
 * folds.
 * 
 * @author Thomas Abeel
 * 
 */
public class TutorialCVSameFolds {
    /**
     * Default cross-validation with the same folds for multiple runs.
     */
    public static void main(String[] args) throws Exception {
        /* Load data */
        Dataset data = FileHandler.loadDataset(new File("devtools/data/iris.data"), 4, ",");
        /* Construct KNN classifier */
        Classifier knn = new KNearestNeighbors(5);
        /* Construct new cross validation instance with the KNN classifier, */
        CrossValidation cv = new CrossValidation(knn);
        /*
         * Perform 5-fold cross-validation on the data set with a user-defined
         * random generator
         */
        Map<Object, PerformanceMeasure> p = cv.crossValidation(data, 5, new Random(1));

        Map<Object, PerformanceMeasure> q = cv.crossValidation(data, 5, new Random(1));

        Map<Object, PerformanceMeasure> r = cv.crossValidation(data, 5, new Random(25));

        System.out.println("Accuracy=" + p.get("Iris-virginica").getAccuracy());
        System.out.println("Accuracy=" + q.get("Iris-virginica").getAccuracy());
        System.out.println("Accuracy=" + r.get("Iris-virginica").getAccuracy());
        System.out.println(p);
        System.out.println(q);
        System.out.println(r);

    }
}
