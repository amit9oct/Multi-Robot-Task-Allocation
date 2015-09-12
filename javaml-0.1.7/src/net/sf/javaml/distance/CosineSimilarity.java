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

import net.sf.javaml.core.Instance;

/**
 * This similarity based distance measure actually measures the angle between
 * two vectors. 
 * 
 * The value returned lies in the interval [0,1].
 * 
 * @author Thomas Abeel
 * 
 */
public class CosineSimilarity extends AbstractSimilarity {

    /**
     * 
     */
    private static final long serialVersionUID = 330926456281777694L;

    @Override
    public double measure(Instance x, Instance y) {
        if (x.noAttributes() != y.noAttributes()) {
            throw new RuntimeException("Both instances should contain the same number of values.");
        }
        double sumTop = 0;
        double sumOne = 0;
        double sumTwo = 0;
        for (int i = 0; i < x.noAttributes(); i++) {
            sumTop += x.value(i) * y.value(i);
            sumOne += x.value(i) * x.value(i);
            sumTwo += y.value(i) * y.value(i);
        }
        double cosSim = sumTop / (Math.sqrt(sumOne) * Math.sqrt(sumTwo));
        if (cosSim < 0)
            cosSim = 0;//This should not happen, but does because of rounding errorsl
        return cosSim;

    }

   
}
