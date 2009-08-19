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

import org.apache.myfaces.tobago.layout.LayoutToken;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.Measure;

/**
 * Manages the relation between the Tree of LayoutManagers and the Linear System of Equations
 */
public class EquationManager {

  private SystemOfEquations equations;
  private Measure[] result;

  public int addComponentRoot() {
    equations = new SystemOfEquations(1);
    return 0;
  }

  public void setFixedLength(int index, Measure length, Object debug) {
    equations.addEqualsEquation(new FixedEquation(index, length, debug));
  }

  public int[] partition(
      int index, int number, Measure spacing, Measure beginOffset, Measure endOffset,
      LayoutTokens tokens, Object debug) {

    assert number > 0;

    int[] newIndices = equations.addVariables(number + 1);
    equations.addEqualsEquation(
        new PartitionEquation(newIndices[0], number, index, spacing, beginOffset, endOffset, debug));
    equations.addEqualsEquation(
        new RemainderEquation(newIndices[number], tokens, debug));
    return newIndices;
  }

  public int combine(int index, int span, Measure spacing, LayoutToken token, Object debug) {

    assert span > 0;

    int[] newIndices = equations.addVariables(1);
    equations.addEqualsEquation(new CombinationEquation(newIndices[0], index, span, spacing, token, debug));
    return newIndices[0];
  }

  public void proportionate(int index1, int index2, int factor1, int factor2, Object debug) {

    assert index1 >= 0;
    assert index2 >= 0;
    assert factor1 > 0;
    assert factor2 > 0;

    equations.addEqualsEquation(new ProportionEquation(index1, index2, factor1, factor2, debug));
  }

  public void solve() {
    result = equations.solve();
  }

  public Measure[] getResult() {
    return result;
  }

  @Override
  public String toString() {
    return "EquationManager: " + equations.toString();
  }
}
