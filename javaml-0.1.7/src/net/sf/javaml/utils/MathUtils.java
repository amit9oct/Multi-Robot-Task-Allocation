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
package net.sf.javaml.utils;

import net.sf.javaml.core.Complex;

/**
 * A class that provides some utility math methods. - Comparing doubles for
 * equality and order with a precision.
 * 
 * @author Thomas Abeel
 * 
 */
public class MathUtils {

    /**
     * Test whether a is zero with acceptable error.
     * 
     * @param a
     * @return true if a is zero, false otherwise
     */
    public static boolean zero(double a) {
        return Math.abs(a) < epsilon;

    }

    private static final double epsilon = 1e-6;

    /**
     * Tests if a is equal to b.
     * 
     * @param a
     *            a Complex number
     * @param b
     *            a Complex number
     */
    public static boolean eq(Complex a, Complex b) {
        return eq(a.re, b.re) && eq(a.im, b.im);
    }

    /**
     * Tests if a is equal to b.
     * 
     * @param a
     *            a double
     * @param b
     *            a double
     */
    public static boolean eq(double a, double b) {
        return ((a - b < epsilon) && (b - a < epsilon)) || (Double.isNaN(a) && Double.isNaN(b));
    }

    /**
     * Tests if a is smaller or equal to b.
     * 
     * @param a
     *            a double
     * @param b
     *            a double
     */
    public static boolean le(double a, double b) {
        return (a - b < epsilon);
    }

    /**
     * Tests if a is greater or equal to b.
     * 
     * @param a
     *            a double
     * @param b
     *            a double
     */
    public static boolean ge(double a, double b) {
        return (b - a < epsilon);
    }

    /**
     * Tests if a is smaller than b.
     * 
     * @param a
     *            a double
     * @param b
     *            a double
     */
    public static boolean lt(double a, double b) {
        return (b - a > epsilon);
    }

    /**
     * Tests if a is greater than b.
     * 
     * @param a
     *            a double
     * @param b
     *            a double
     */
    public static boolean gt(double a, double b) {
        return (a - b > epsilon);
    }

    /**
     * XXX DOC
     * 
     * @param me1
     * @param me2
     */
    public static boolean eq(double[] me1, double[] me2) {
        if (me1.length != me2.length)
            return false;
        for (int i = 0; i < me1.length; i++) {
            if (!MathUtils.eq(me1[i], me2[i]))
                return false;
        }
        return true;
    }

    /**
     * XXX DOC
     * 
     * @param array
     */
    public static double min(double[] array) {
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min)
                min = array[i];
        }
        return min;
    }

    public static double log2(double n) {
        return Math.log(n) / Math.log(2);
    }

    /**
     * Calculates the harmonic mean of the values in the supplied array.
     * 
     * @param g
     * @return
     */
    public static double harmonicMean(double[] g) {
        double sum = 0;

        for (double d : g)
            sum += 1 / d;

        return g.length / sum;
    }

    public static double arithmicMean(double[] g) {
        double sum = 0;

        for (double d : g)
            sum += d;

        return sum / g.length;
    }

}
