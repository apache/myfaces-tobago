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

import org.apache.myfaces.tobago.layout.AutoLayoutToken;
import org.apache.myfaces.tobago.layout.LayoutToken;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.PixelLayoutToken;

/**
 * This equation describes the partition of one column (or row) into some other columns (or rows).
 * E. g.: column2 = column4 + column5 + column6 -> 0 0 -1 0 1 1 1 0 ...
 * In this example are: begin=4, end=6, parent=2, span=1
 * Because of the algorithm we have indices without gap.
 */
public final class CombinationEquation extends AbstractEquation {

  private int newIndex;
  private int parent;
  private int span;
  private Measure spacing;
  private LayoutToken token;

  /**
   * @param newIndex new index
   * @param parent   parent index
   * @param span     number of parent cells
   * @param spacing  space between two cells inside the span
   * @param token
   */
  public CombinationEquation(int newIndex, int parent, int span, Measure spacing, LayoutToken token, Object debug) {
    super(debug);
    this.newIndex = newIndex;
    this.parent = parent;
    this.span = span;
    this.spacing = spacing;
    this.token = token;
  }

  public double[] fillRow(int length) {
    assert newIndex >= 0 && parent >= 0 && span > 0;
    assert parent + span <= newIndex || parent > newIndex;

    double[] row = new double[length];

    int i = 0;
    for (; i < newIndex; i++) {
      row[i] = 0.0;
    }
    for (; i < newIndex + 1; i++) {
      row[i] = 1.0;
    }
    for (; i < row.length - 1; i++) {
      row[i] = 0.0;
    }
    // the last variable contains a constant, this is here the sum of spaces between cells.
    row[row.length - 1] =  (span - 1) * spacing.getPixel();

    for (i = parent; i < parent + span; i++) {
      row[i] = -1.0;
    }

    return row;
  }

  public int priority() {
    if (token instanceof AutoLayoutToken) {
      return 12;
    } else if (token instanceof PixelLayoutToken) {
      return 11;
    } else {
      return 10;
    }
  }

  public int getNewIndex() {
    return newIndex;
  }

  public int getParent() {
    return parent;
  }

  public int getSpan() {
    return span;
  }

  public Measure getSpacing() {
    return spacing;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("CombinationEquation: ");
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
    if (spacing.getPixel() > 0) {
      if (span >= 2) {
        builder.append(" + ");
        if (span > 2) {
          builder.append(span - 1);
          builder.append(" * ");
        }
        builder.append(spacing);
      }
    }
    // sub cells
    builder.append(" = ");
    builder.append("x_");
    builder.append(newIndex);

    appendDebug(builder);

    return builder.toString();
  }
}
