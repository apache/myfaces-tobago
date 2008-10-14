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
 * Because of the algorithm we have indices without gap.
 */
public final class PartitionEquation implements Equation {

  private int begin;
  private int end;
  private int parent;

  /**
   *
   * @param begin lowest index
   * @param end one more than the largest index
   * @param parent parent index
   */
  public PartitionEquation(int begin, int end, int parent) {
    this.begin = begin;
    this.end = end;
    this.parent = parent;
  }

  public void fillRow(double[] row) {
    int i = 0;
    for (; i < begin; i++) {
      row[i] = 0.0;
    }
    for (; i < end; i++) {
      row[i] = 1.0;
    }
    for (; i < row.length; i++) {
      row[i] = 0.0;
    }
    row[parent] = -1;
  }
}
