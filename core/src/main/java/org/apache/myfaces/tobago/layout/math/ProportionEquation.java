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

public final class ProportionEquation implements Equation {

  private int index1;
  private int index2;
  private double factor1;
  private double factor2;

  public ProportionEquation(int index1, int index2, double factor1, double factor2) {
    this.index1 = index1;
    this.index2 = index2;
    this.factor1 = factor1;
    this.factor2 = factor2;
  }

  public void fillRow(double[] row) {
    for (int i = 0; i < row.length; i++) {
      row[i]
          = i == index1
          ? factor2
          : i == index2
          ? -factor1
          : 0.0;
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ProportionEquation:  ");
    if (factor2 != 1.0) {
      builder.append(factor2);
      builder.append(" *");
    }
    builder.append(" x_");
    builder.append(index1);
    builder.append(" = ");
    if (factor1 != 1.0) {
      builder.append(factor1);
      builder.append(" *");
    }
    builder.append(" x_");
    builder.append(index2);
    return builder.toString();
  }

}
