package org.apache.myfaces.tobago.layout.math;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: lofwyr
 * Date: 09.04.2008 16:58:29
 * <p/>
 * |  3*      |  2*        |  auto       |   200px  |
 * <p/>
 * ---------------------------------------------------
 * | 10-x-100 |            10-x-x                    |
 * ---------------------------------------------------
 * |          10-90-1000                 | 100-190-x |
 * ---------------------------------------------------
 * |----------|                                      |
 * ||    |    |                 100-x-x              |
 * |----------|                                      |
 * ---------------------------------------------------
 * <p/>
 * Algorithm:
 * <p/>
 * 1.) Naming the columns widths x_0, x_1, x_2, x_3 and for the sub-table x_0_0, x_0_1
 * <p/>
 * 2.) Collecting Constrains:
 * <p/>
 * a) Look to columns/rows definition:
 * <p/>
 * - Pixel: set it directly: x_3 = 200
 * <p/>
 * - Relative: Collect all and devide: - 2 * x_0 + 3 * x_1 = 0 (for n Elements you get n-1 equations)
 * <p/>
 * - Auto (may be default): No restriction
 * <p/>
 * b) Component constratins:
 * <p/>
 * - Min: x_0 >= 10 or x_1 + x_2 + x_3 >= 10 (if not defined: ignore)
 * <p/>
 * - Max: x_0 <= 100 (if not defined: ignore)
 * <p/>
 * - Prefered: no restriction (come later down in the optimization function
 * <p/>
 * - Nested Layout managers: x_0 = x_0_0 + x_0_1
 * <p/>
 * 3.) Finding minimization function
 * <p/>
 * a) collect prefered values p_j
 * <p/>
 * b) abs(90 - x_0+x_1+x_2) ... abs(90 - x_0+x_1+x_2) or quadratic
 * <p/>
 * c) top down
 * <p/>
 * - for pages: set page width to fix value (if there is any)
 * <p/>
 * - for popups: set sum of columns to be minimized
 * <p/>
 * <p/>
 * if (there is no match: convert some/all constraints to minimized error funktions)
 */

public class SystemOfEquations {

  private static final Log LOG = LogFactory.getLog(SystemOfEquations.class);

  private int numberOfVariables;
  private List<Equation> equations = new ArrayList<Equation>();
  private double[][] data;

  public SystemOfEquations(int numberOfVariables) {
    this.numberOfVariables = numberOfVariables;
  }

  public void addEqualsEquation(FixedEquation equation) {
    equations.add(equation);
  }

  public void addEqualsEquation(PartitionEquation equation) {
    equations.add(equation);
  }

  public void addEqualsEquation(ProportionEquation equation) {
    equations.add(equation);
  }

  public int[] addVariables(int number) {
    assert number > 0;
    int[] indices = new int[number];
    for (int i = 0; i < number; i++) {
      indices[i] = numberOfVariables + i;
    }
    numberOfVariables += number;
    return indices;
  }

  public void prepare() {
//    data = new double[equations.size() + equalEquations.size()][];
    data = new double[equations.size()][];
    for (int i = 0; i < equations.size(); i++) {
      data[i] = new double[numberOfVariables + 1];
      equations.get(i).fillRow(data[i]);
    }
    /*
    for (int i = 0; i < equalEquations.size(); i++) {
      data[i + equations.size()] = equalEquations.get(i);
    }
*/
  }

  public void gauss() {

//    prepare();

    for (int j = 0; j < data.length; j++) {
      // normalize row
      double factor = data[j][j];
      if (factor == 0) {
        int nonZeroIndex = findNonZero(j);
        if (nonZeroIndex != -1) {
          swapRow(j, nonZeroIndex);
          factor = data[j][j];
        } else {
          LOG.info("nicht eindeutig lösbar");
        }
      }
      divideRow(j, factor);
      // substract multiple of j-row to any folowing row
      for (int k = j + 1; k < data.length; k++) {
        substractMultipleOfRowJToRowK(j, k);
      }
    }
  }

  public void step2() {
    for (int j = equations.size() - 1; j >= 0; j--) {
      for (int k = j - 1; k >= 0; k--) {
        substractMultipleOfRowJToRowK(j, k);
      }
    }
  }

  public double[] result() {
    double[] result = new double[numberOfVariables];
    for (int i = 0; i < numberOfVariables; i++) {
      result[i] = data[i][numberOfVariables];
    }
    return result;
  }

  private void swapRow(int j, int k) {
    double[] temp = data[j];
    data[j] = data[k];
    data[k] = temp;
  }

  private int findNonZero(int j) {
    for (int k = j + 1; k < equations.size(); k++) {
      if (data[k][j] != 0.0) {
        return k;
      }
    }
    return -1;
  }

  private void divideRow(int j, double denominator) {
    // todo: denominator != null
    for (int i = 0; i < numberOfVariables + 1; i++) {
      data[j][i] = data[j][i] / denominator;
    }
  }

  private void substractMultipleOfRowJToRowK(int j, int k) {
    double factor = data[k][j];
    if (factor != 0) {
      for (int i = 0; i < numberOfVariables + 1; i++) {
        data[k][i] -= data[j][i] * factor;
      }
    }
  }

  public double[][] getData() {
    return data;
  }

  public int getNumberOfVariables() {
    return numberOfVariables;
  }

  @Override
  public String toString() {
    String result;
    if (data == null) { // the toString0 needed the data, but it may not be initilized.
      prepare();
      result = toString0();
      data = null; // reset the state
    } else {
      result = toString0();
    }
    return result;
  }

  private String toString0() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[\n");
    for (double[] row : data) {
      buffer.append(Arrays.toString(row));
      buffer.append("\n");
    }
    buffer.append("]");
    return buffer.toString();
  }
}
