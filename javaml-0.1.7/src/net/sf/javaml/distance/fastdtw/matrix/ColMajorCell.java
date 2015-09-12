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
package net.sf.javaml.distance.fastdtw.matrix;

public class ColMajorCell {

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public ColMajorCell(int column, int row) {
        col = column;
        this.row = row;
    }

    public boolean equals(Object o) {
        return (o instanceof ColMajorCell) && ((ColMajorCell) o).col == col && ((ColMajorCell) o).row == row;
    }

    public int hashCode() {
        return (1 << col) + row;
    }

    public String toString() {
        return "(" + col + "," + row + ")";
    }

    private final int col;

    private final int row;
}