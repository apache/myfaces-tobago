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

import java.util.Arrays;

/**
 * Manages the relation between the Tree of LayoutManagers and the Linear System of Equations
 */
public class Solver {

  private static final Log LOG = LogFactory.getLog(Solver.class);

  private SystemOfEquations equations;
  private Node current;

  public Solver() {
    equations = new SystemOfEquations(0);
    current = new Node(0, null);
    addSubTree(1);
  }

  protected void addSubTree(int number) {

    assert number > 0;
    int n = equations.getNumberOfVariables();
    int[] newIndices = equations.addVariables(number);
    if (!current.isRoot()) {
      equations.addEqualsEquation(
          new PartitionEquation(newIndices[0], newIndices[newIndices.length - 1] + 1, current.getIndexOfVariable()));
    }
    for (int i = n; i < n + number; i++) {
      assert newIndices[i - n] == i;
      current.getChildren().add(new Node(i, current));
    }
  }

  protected void descend(int index) {
    current = current.getChildren().get(index);
  }

  protected void ascend() {
    current = current.getParent();
  }

  protected void setFixedLength(int index, int length) {
    equations.addEqualsEquation(new FixedEquation(getIndexOfVariable(index), length));
  }

  private int getIndexOfVariable(int index) {
    return current.getChildren().get(index).getIndexOfVariable();
  }

  protected void setProportion(int index1, int index2, int factor1, int factor2) {
    equations.addEqualsEquation(
        new ProportionEquation(getIndexOfVariable(index1), getIndexOfVariable(index2), factor1, factor2));
  }

  protected void solve() {
    equations.prepare();
    equations.gauss();
    equations.step2();
    double[] result = equations.result();
    LOG.info("result: " + Arrays.toString(result));
  }
}
