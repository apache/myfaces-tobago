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

/**
 * This equation describes the partition of one column (or row) into some other columns (or rows).
 * E. g.: column2 = column4 + column5 + column6 -> 0 0 -1 0 1 1 1 0 ...
 * In this example are: begin=4, end=6, parent=2, span=1
 * Because of the algorithm we have indices without gap.
 */
public final class PartitionEquation extends AbstractEquation {

  private int begin;
  private int count;
  private int parent;
  private Measure spacing;
  private Measure beginOffset;
  private Measure endOffset;

  /**
   * @param begin   lowest index
   * @param count   number of cells of the partition
   * @param parent  parent index
   * @param spacing space between two cells of the partition
   * @param beginOffset offset before the first cell
   * @param endOffset offset after the last cell
   * @param debug Logging information
   */
  public PartitionEquation(
      int begin, int count, int parent, Measure spacing, Measure beginOffset, Measure endOffset, Object debug) {
    super(debug);

    assert spacing != null;
    assert beginOffset != null;
    assert endOffset != null;
    
    this.begin = begin;
    this.count = count;
    this.parent = parent;
    this.spacing = spacing;
    this.beginOffset = beginOffset;
    this.endOffset = endOffset;

    assert begin >= 0 && count > 0 && parent >= 0;
    assert parent <= begin;
  }

  public double[] fillRow(int length) {
    double[] row = new double[length];
    int i = 0;
    for (; i < begin; i++) {
      row[i] = 0.0;
    }
    // these variables are the cells from the partition
    for (; i < begin + count; i++) {
      row[i] = 1.0;
    }
    // this variable is for the rest (overfull (scrollbar?) or underfull).
    row[i++] = 1.0;

    for (; i < row.length - 1; i++) {
      row[i] = 0.0;
    }
    // the last variable contains a constant, this is here the sum of spaces between cells.
    row[row.length - 1] = -((count - 1) * spacing.getPixel() + beginOffset.getPixel() + endOffset.getPixel());

    // the variable for parent
    row[parent] = -1.0;

    return row;
  }

  public int priority() {
    return 30;
  }

  public int getBegin() {
    return begin;
  }

  public int getCount() {
    return count;
  }

  public int getParent() {
    return parent;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("PartitionEquation:   ");
    // cells from parent
    builder.append(" x_");
    builder.append(parent);
    // sub cells
    builder.append(" = ");
    builder.append("x_");
    builder.append(begin);
    if (count > 2) {
      builder.append(" + ...");
    }
    if (count >= 2) {
      builder.append(" + x_");
      builder.append(begin + count - 1);
    }
    // plus spacing
    if (spacing.getPixel() > 0) {
      if (count >= 2) {
        builder.append(" + ");
        if (count > 2) {
          builder.append(count - 1);
          builder.append(" * ");
        }
        builder.append(spacing);
      }
    }

    // plus offsets
    if (beginOffset.getPixel() > 0) {
        builder.append(" + ");
        builder.append(beginOffset);
    }
    if (endOffset.getPixel() > 0) {
        builder.append(" + ");
        builder.append(endOffset);
    }
    // rest
    builder.append(" + x_");
    builder.append(begin + count);

    appendDebug(builder);

    return builder.toString();
  }
}
