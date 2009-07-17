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

/**
 * This equation describes the partition of one column (or row) into some other columns (or rows).
 * E. g.: column2 = column4 + column5 + column6 -> 0 0 -1 0 1 1 1 0 ...
 * In this example are: begin=4, end=6, parent=2, span=1
 * Because of the algorithm we have indices without gap.
 */
public final class PartitionEquation implements Equation {

  private int begin;
  private int end;
  private int parent;
  private int span;
  private double innerSpacing;
  private double outerSpacing;

  /**
   * @param begin        lowest index
   * @param end          one more than the largest index
   * @param parent       parent index
   * @param span         number of parent cells
   * @param innerSpacing space between two cells between begin and end
   * @param outerSpacing space between two cells inside the span
   */
  public PartitionEquation(int begin, int end, int parent, int span, double innerSpacing, double outerSpacing) {
    this.begin = begin;
    this.end = end;
    this.parent = parent;
    this.span = span;
    this.innerSpacing = innerSpacing;
    this.outerSpacing = outerSpacing;
  }

  public void fillRow(double[] row) {
    assert begin >= 0 && end > 0 && parent >= 0 && span > 0;
    assert begin < end;
    assert parent + span <= begin || parent >= end;

    int i = 0;
    for (; i < begin; i++) {
      row[i] = 0.0;
    }
    for (; i < end; i++) {
      row[i] = 1.0;
    }
    for (; i < row.length - 1; i++) {
      row[i] = 0.0;
    }
    // the last variable contains a constant, this is here the sum of spaces between cells.
    row[row.length - 1] =  (span - 1) * outerSpacing - (end - begin - 1) * innerSpacing;

    for (i = parent; i < parent + span; i++) {
      row[i] = -1.0;
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("PartitionEquation: ");
    // cells from parent
    builder.append(" x_");
    builder.append(parent);
    if (span > 2) {
      builder.append(" + ...");
    }
    if (span >= 2) {
      builder.append(" + x_");
      builder.append(parent + span - 1);
    }
    // plus spacing
    if (span >= 2) {
      builder.append(" + ");
      if (span >= 2) {
        builder.append(span - 1);
        builder.append(" * ");
      }
      builder.append(outerSpacing);
    }
    // sub cells
    builder.append(" = ");
    builder.append("x_");
    builder.append(begin);
    if (end - begin > 2) {
      builder.append(" + ...");
    }
    if (end - begin >= 2) {
      builder.append(" + x_");
      builder.append(end - 1);
    }
    // plus spacing
    if (end - begin >= 2) {
      builder.append(" + ");
      if (end - begin > 2) {
        builder.append(end - begin - 1);
        builder.append(" * ");
      }
      builder.append(innerSpacing);
    }

    return builder.toString();
  }
}
