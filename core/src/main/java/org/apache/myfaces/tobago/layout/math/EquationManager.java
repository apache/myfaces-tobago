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

/**
 * Manages the relation between the Tree of LayoutManagers and the Linear System of Equations
 */
public class EquationManager {

  private static final Log LOG = LogFactory.getLog(EquationManager.class);

  private SystemOfEquations equations;
  private double[] result;

  public int addComponentRoot() {
    equations = new SystemOfEquations(1);
    return 0;
  }

  public void setFixedLength(int index, int length) {
    equations.addEqualsEquation(new FixedEquation(index, length));
    LOG.info(equations);
  }

  public int[] divide(int index, int number) {
    int[] newIndices = addSubTree(index, number, 1);
    LOG.info(equations);
    return newIndices;
  }

  public int addComponent(int index, int span) {
    int[] newIndices = addSubTree(index, 1, span);
    LOG.info(equations);
    return newIndices[0];
  }

  private int[] addSubTree(int index, int number, int span) {

    assert number > 0;
    assert span > 0;

    int[] newIndices = equations.addVariables(number);
    equations.addEqualsEquation(new PartitionEquation(
        newIndices[0],
        newIndices[newIndices.length - 1] + 1,
        index,
        span));
    return newIndices;
  }

  public void setProportion(int index1, int index2, int factor1, int factor2) {
    equations.addEqualsEquation(new ProportionEquation(index1, index2, factor1, factor2));
    LOG.info(equations);
  }

  public void solve() {

    LOG.info("solve:\n" + equations);

    equations.prepare();
    equations.gauss();
    equations.reduce();
    result = equations.result();
  }

  public double[] getResult() {
    return result;
  }

  public int getNumberOfVariable() {
    return equations.getNumberOfVariables();
  }

  @Override
  public String toString() {
    return "EquationManager: " + equations.toString();
  }
}
