package org.apache.myfaces.tobago.layout;

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

import java.util.ArrayList;
import java.util.List;

/**
 * User: lofwyr
 * Date: 25.01.2008
 */
public class GridArray {

  private List<Cell> grid;

  private int columnCount;
  private int rowCount;

  public GridArray(int columnCount, int rowCount) {
    this.columnCount = columnCount;

    assert columnCount > 0;
    assert rowCount > 0;

    grid = new ArrayList<Cell>(columnCount * rowCount);
    enlarge(rowCount);
  }

  public Cell get(int column, int row) {
    assert column >= 0 && column < columnCount : "column=" + column + " columnCount=" + columnCount;
    assert row >= 0 : "row=" + row;

    if (row >= rowCount) {
      return null;
    } else {
      return grid.get(column + row * columnCount);
    }
  }

  public void set(int column, int row, Cell cell) {
    if (row >= rowCount) {
      enlarge(row + 1);
    }
    grid.set(column + row * columnCount, cell);

  }

  private void enlarge(int newRowCount) {
    for (; rowCount < newRowCount; rowCount++) {
      for (int i = 0; i < columnCount; i++) {
        grid.add(null);
      }
    }
  }

  public int getColumnCount() {
    return columnCount;
  }

  public int getRowCount() {
    return rowCount;
  }
}
