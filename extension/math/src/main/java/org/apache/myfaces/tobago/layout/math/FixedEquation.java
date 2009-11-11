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

import org.apache.myfaces.tobago.layout.Measure;

import java.util.Arrays;

public final class FixedEquation extends AbstractEquation {

  private int index;
  private Measure result;

  public FixedEquation(int index, Measure result, Object debug) {
    super(debug);
    this.index = index;
    this.result = result;
  }

  public double[] fillRow(int length) {
    double[] row = new double[length];
    Arrays.fill(row, 0.0);
    row[index] = 1.0;
    row[row.length - 1] = result.getPixel();
    return row;
  }

  public int priority() {
    return 40;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("FixedEquation:        x_");
    builder.append(index);
    builder.append(" = ");
    builder.append(result);

    appendDebug(builder);

    return builder.toString();
  }
}
