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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.internal.layout.MathUtils;
import org.apache.myfaces.tobago.layout.Measure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
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
 * - Preferred: no restriction (come later down in the optimization function
 * <p/>
 * - Nested Layout managers: x_0 = x_0_0 + x_0_1
 * <p/>
 * 3.) Finding minimization function
 * <p/>
 * a) collect preferred values p_j
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

  private static final Logger LOG = LoggerFactory.getLogger(SystemOfEquations.class);

  private int numberOfVariables;
  private List<Equation> equations = new ArrayList<Equation>();
  private double[][] data;
  private Step step;

  public SystemOfEquations(int numberOfVariables) {
    this.numberOfVariables = numberOfVariables;
    this.step = Step.NEW;
  }

  public void addEqualsEquation(Equation equation) {
    assert step == Step.NEW;

    equations.add(equation);
  }

  public int[] addVariables(int number) {
    assert step == Step.NEW;
    assert number > 0;

    int[] indices = new int[number];
    for (int i = 0; i < number; i++) {
      indices[i] = numberOfVariables + i;
    }
    numberOfVariables += number;
    return indices;
  }

  public Measure[] solve() {
    prepare();
    gauss();
    reduce();
    return result();
  }

  private void prepare() {
    assert step == Step.NEW;
    step = step.next();
//    data = new double[equations.size() + equalEquations.size()][];

    // if there are more variables than equations
    // fill the rest with zero rows.
    for (int j = equations.size(); j < numberOfVariables; j++) {
      equations.add(new ZeroEquation());
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Before sorting: " + this);
    }

    Collections.sort(equations, new EquationComparator());

    if (numberOfVariables != equations.size()) {
      LOG.warn("SOE have not correct dimensions: " + this); // todo: remove this warning, when all problems are solved
    }

    data = new double[equations.size()][];
    for (int i = 0; i < equations.size(); i++) {
      data[i] = equations.get(i).fillRow(numberOfVariables + 1);
    }

    step = step.next();
  }

  private void gauss() {

    if (LOG.isDebugEnabled()) {
      LOG.debug(this);
    }

    assert step == Step.PREPARED;
    step = step.next();

    for (int j = 0; j < numberOfVariables; j++) {
      // normalize row
//      if (LOG.isDebugEnabled()) {
//        LOG.debug(this);
//      }
      double factor = data[j][j];
      if (MathUtils.isZero(factor)) {
        int nonZeroIndex = findNonZero(j);
        if (nonZeroIndex != -1) {
          swapRow(j, nonZeroIndex);
          factor = data[j][j];
        } else {
          insertRow(j);
          continue;
        }
      }
      divideRow(j, factor);
      // substract multiple of j-row to any folowing row
      for (int k = j + 1; k < data.length; k++) {
        substractMultipleOfRowJToRowK(j, k);
      }
    }

    step = step.next();
  }

  private void reduce() {
    if (LOG.isDebugEnabled()) {
      LOG.debug(this);
    }

    assert step == Step.TRIANGULAR;
    step = step.next();

    for (int j = equations.size() - 1; j >= 0; j--) {
      if (rowNull(j)) {
        LOG.error("Not solvable: " + this);
        continue;
      }
      for (int k = j - 1; k >= 0; k--) {
//        if (LOG.isDebugEnabled()) {
//          LOG.debug("j=" + j + "k=" + k + this);
//        }
        double factor = data[k][j];
        if (MathUtils.isNotZero(factor)) {
          data[k][j] -= data[j][j] * factor;
          data[k][numberOfVariables] -= data[j][numberOfVariables] * factor;
        }
      }
    }

    step = step.next();
  }

  private Measure[] result() {
    assert step == Step.DIAGONAL;

    if (LOG.isDebugEnabled()) {
      LOG.debug(this);
    }

    double[] original = copyResult();

    round(original);

    Measure[] rounded = copyResultMeasure();

    if (LOG.isDebugEnabled()) {
      LOG.debug("after adjust remainders:  " + Arrays.toString(rounded));
    }

    return rounded;
  }

  private double[] copyResult() {
    double[] result = new double[numberOfVariables];
    for (int i = 0; i < numberOfVariables; i++) {
      result[i] = data[i][numberOfVariables];
    }
    return result;
  }

  private Measure[] copyResultMeasure() {
    Measure[] result = new Measure[numberOfVariables];
    for (int i = 0; i < numberOfVariables; i++) {
      assert MathUtils.isInteger(data[i][numberOfVariables]);
      result[i] = Measure.valueOf((int) Math.round(data[i][numberOfVariables]));
    }
    return result;
  }

  private void round(double[] original) {

    for (Equation equation : equations) {
      if (equation instanceof PartitionEquation) {
        PartitionEquation partition = (PartitionEquation) equation;

        int begin = partition.getBegin();
        int count = partition.getCount();
        double[] temp = new double[count];
        // copy from data to temporary array
        for (int i = 0; i < count; i++) {
          temp[i] = data[i + begin][numberOfVariables];
        }
        // processing
        int parent = partition.getParent();
        MathUtils.adjustRemainders(temp, original[parent] - data[parent][numberOfVariables]);
        // write back to data
        for (int i = 0; i < count; i++) {
          data[i + begin][numberOfVariables] = temp[i];
        }
      } else if (equation instanceof CombinationEquation) {
        CombinationEquation combination = (CombinationEquation) equation;
        combination.getParent();
        int parent = combination.getParent();
        int span = combination.getSpan();
        double sum = (span - 1) * combination.getSpacing().getPixel(); // the spacings
        for (int i = 0; i < span; i++) {
          double value = data[i + parent][numberOfVariables];
          assert MathUtils.isInteger(value);
          sum += value;
        }
        int index = combination.getNewIndex();
        double old = data[index][numberOfVariables];
        if (Math.abs(old - Math.round(sum)) < 1) {
          data[index][numberOfVariables] = Math.round(sum);
        } else {
          data[index][numberOfVariables] = Math.round(old);
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("Change value for index=" + index + " from " + old + " -> " + Math.round(sum));
        }

      } //else if (equation instanceof )
    }
  }

  private void swapRow(int j, int k) {
    double[] temp = data[j];
    data[j] = data[k];
    data[k] = temp;
  }

  private int findNonZero(int j) {
    for (int k = j + 1; k < data.length; k++) {
      if (MathUtils.isNotZero(data[k][j])) {
        return k;
      }
    }
    return -1;
  }

  private void insertRow(int j) {
    LOG.warn("Not unique solvable, inserting dummy");
    double[][] temp = data;
    data = new double[temp.length + 1][];
    for (int i = 0; i < temp.length; i++) {
      if (i < j) {
        data[i] = temp[i];
      } else {
        data[i + 1] = temp[i];
      }
    }
    data[j] = new double[numberOfVariables + 1];
    data[j][j] = 1;
    data[j][numberOfVariables] = 100.0; // todo: default
    LOG.warn("Setting free (undefined) variable x_" + j + " to " + data[j][numberOfVariables]);
    equations.add(j, new ZeroEquation()); // fixme
  }

  /**
   * Searches for a row where all values are zero (comes from ZeroEquation)
   *
   * @return The row index or -1 if nothing was found.
   */
  private int findFullZero() {
    for (int j = data.length - 1; j >= 0; j--) {
      boolean allZero = true;
      for (double value : data[j]) {
        if (MathUtils.isNotZero(value)) {
          allZero = false;
          break;
        }
      }
      if (allZero) {
        return j;
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
    if (MathUtils.isNotZero(factor)) {
      for (int i = 0; i < numberOfVariables + 1; i++) {
        data[k][i] -= data[j][i] * factor;
      }
//      fixResultOfRow(k);
    }
  }

  /**
   * Determines if the row j has only null entries, accept the last one.
   *
   * @param j Index of the row to test.
   * @return Is the row quasi null?
   */
  private boolean rowNull(int j) {
    for (int i = 0; i < numberOfVariables; i++) {
      if (MathUtils.isNotZero(data[j][i])) {
        return false;
      }
    }
    return true;

  }

  public double[][] getData() {
    return data;
  }

  public int getNumberOfVariables() {
    return numberOfVariables;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(
        step + " number of equations=" + equations.size() + " number of variables=" + numberOfVariables + " ");
    switch (step) {
      case NEW:
      case PREPARE_IN_PROGRESS:
        toString1(builder, equations);
        break;
      case PREPARED:
        toString0(builder, true);
        break;
      default:
        toString0(builder, false);
        break;
    }
    return builder.toString();
  }

  private void toString0(StringBuilder builder, boolean showEquation) {
    builder.append("[\n");
    for (int i = 0; i < data.length; i++) {
      builder.append(Arrays.toString(data[i]));
      if (showEquation) {
        builder.append(" from ");
        builder.append(equations.get(i));
      }
      builder.append("\n");
    }
    builder.append("]");
  }

  private void toString1(StringBuilder builder, List<Equation> equations) {
    builder.append("[\n");
    for (Equation equation : equations) {
      builder.append(equation);
      builder.append(",\n");
    }
    builder.append("]");
  }

  private static enum Step {
    NEW, PREPARE_IN_PROGRESS, PREPARED, GAUSS_IN_PROGRESS, TRIANGULAR, REDUCE_IN_PROGRESS, DIAGONAL;

    public Step next() {
      return values()[ordinal() + 1];
    }
  }
}
