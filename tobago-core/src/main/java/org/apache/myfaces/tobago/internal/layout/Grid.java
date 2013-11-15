/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.internal.layout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.Orientation;
import org.apache.myfaces.tobago.layout.RelativeLayoutToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grid {

  private static final Logger LOG = LoggerFactory.getLogger(Grid.class);

  // TODO: check if it is faster with arrays.
  /**
   * The rectangular data as a 1-dim list
   */
  private List<Cell> cells;

  private BankHead[] columnHeads;
  private BankHead[] rowHeads;

  private int columnCount;
  private int rowCount;

  private int columnCursor;
  private int rowCursor;

  private boolean columnOverflow;
  private boolean rowOverflow;

  private List<Integer> errorIndexes;

  public Grid(final LayoutTokens columns, final LayoutTokens rows) {
    assert columns.getSize() > 0;
    assert rows.getSize() > 0;

    this.columnCount = columns.getSize(); 
    this.rowCount = rows.getSize(); 
    
    this.columnHeads = new BankHead[columnCount];
    for (int i = 0; i < columnCount; i++) {
      columnHeads[i] = new BankHead(columns.get(i));
    }
    this.rowHeads = new BankHead[rowCount];
    for (int i = 0; i < rowCount; i++) {
      rowHeads[i] = new BankHead(rows.get(i));
    }
    final int size = columnCount * rowCount;
    this.cells = new ArrayList<Cell>(size);
    for (int i = 0; i < size; i++) {
      this.cells.add(null);
    }
  }

  public void add(final OriginCell cell, int columnSpan, final int rowSpan) {

    assert columnSpan > 0;
    assert rowSpan > 0;

    boolean error = false;

    if (columnSpan + columnCursor > columnCount) {
      LOG.warn("The columnSpan is to large for the actual position in the grid. Will be fixed. "
          + "columnSpan=" + columnSpan + " columnCursor=" + columnCursor + " columnCount=" + columnCount);
      columnSpan = columnCount - columnCursor;
      error = true;
    }

    cell.setColumnSpan(columnSpan);
    cell.setRowSpan(rowSpan);

    for (int i = 1; i < columnSpan; i++) {
      if (getCell(i + columnCursor, rowCursor) != null) {
        LOG.warn("The columnSpan is to large for the actual position in the grid. Will be fixed. "
            + "columnSpan=" + columnSpan + " columnCursor=" + columnCursor + " columnCount=" + columnCount);
        columnSpan = i - 1;
        error = true;
      }
    }

    for (int j = 0; j < rowSpan; j++) {
      for (int i = 0; i < columnSpan; i++) {
        final Cell actualCell;
        if (i == 0 && j == 0) {
          actualCell = cell;
        } else {
          actualCell = new SpanCell(cell, i == 0, j == 0);
        }
        assert getCell(i + columnCursor, j + rowCursor) == null : "Position in the cell must be free.";
        setCell(i + columnCursor, j + rowCursor, actualCell);
        if (error) {
          addError(i + columnCursor, j + rowCursor);
        }
      }
    }

    findNextFreeCell();
  }

  public Cell getCell(final int i, final int j, final Orientation orientation) {
    return orientation == Orientation.HORIZONTAL ? getCell(i, j) : getCell(j, i);
  }

  public Cell getCell(final int column, final int row) {
    assert column >= 0 && column < columnCount : "column=" + column + " columnCount=" + columnCount;
    assert row >= 0 : "row=" + row;

    if (row >= rowCount) {
      return null;
    } else {
      return cells.get(column + row * columnCount);
    }
  }

  public void setCell(final int column, final int row, final Cell cell) {
    if (row >= rowCount) {
      enlarge(row - rowCount + 1);
    }
    cells.set(column + row * columnCount, cell);
  }

  private void findNextFreeCell() {
    for (; rowCursor < rowCount; rowCursor++) {
      for (; columnCursor < columnCount; columnCursor++) {
        if (getCell(columnCursor, rowCursor) == null) {
          return;
        }
      }
      columnCursor = 0;
    }
  }

  public BankHead[] getBankHeads(final Orientation orientation) {
    return orientation == Orientation.HORIZONTAL ? columnHeads : rowHeads;
  }
  
  private void enlarge(final int newRows) {
    
    // process cells
    for (int i = 0; i < newRows; i++) {
      for (int j = 0; j < columnCount; j++) {
        cells.add(null);
      }
    }

    // process heads
    final BankHead[] newRowHeads = new BankHead[rowCount + newRows];
    System.arraycopy(rowHeads, 0, newRowHeads, 0, rowHeads.length);
    rowHeads = newRowHeads;
    // todo: shorter in jdk 1.6: rowHeads = Arrays.copyOf(rowHeads, rowHeads.length + newRows);
    
    for (int i = rowCount; i < rowCount + newRows; i++) {
      rowHeads[i] = new BankHead(RelativeLayoutToken.DEFAULT_INSTANCE);
    }

    rowCount += newRows;
  }

  public boolean isOverflow(final Orientation orientation) {
    return orientation == Orientation.HORIZONTAL ? columnOverflow : rowOverflow;
  }

  public void setOverflow(final boolean overflow, final Orientation orientation) {
    if (orientation == Orientation.HORIZONTAL) {
      this.columnOverflow = overflow;
    } else {
      this.rowOverflow = overflow;
    }
  }

  public boolean isOverflow() {
    return columnOverflow;
  }

  public void setColumnOverflow(final boolean columnOverflow) {
    this.columnOverflow = columnOverflow;
  }

  public boolean isRowOverflow() {
    return rowOverflow;
  }

  public void setRowOverflow(final boolean rowOverflow) {
    this.rowOverflow = rowOverflow;
  }

  public void addError(final int i, final int j) {
    if (errorIndexes == null) {
      errorIndexes = new ArrayList<Integer>();
    }
    errorIndexes.add(j * columnCount + i);
  }

  public boolean hasError(final int i, final int j) {
    if (errorIndexes == null) {
      return false;
    }
    return errorIndexes.contains(j * columnCount + i);
  }

  public int getColumnCount() {
    return columnCount;
  }

  public int getRowCount() {
    return rowCount;
  }

  /**
   * Prints the state of the grid as an Unicode shape like this:
   * ┏━┳━┳━┳━┯━┓
   * ┃█┃█┃█┃█│➞┃
   * ┠─╂─╊━╇━┿━┫
   * ┃⬇┃⬇┃█│➞│➞┃
   * ┣━╉─╊━╈━╈━┫
   * ┃█┃⬇┃█┃█┃█┃
   * ┣━╇━╇━╉─╊━┩
   * ┃█│➞│➞┃⬇┃◌│
   * ┡━┿━┿━╉─╂─┤
   * │◌│◌│◌┃⬇┃◌│
   * └─┴─┴─┺━┹─┘
   */
  public String gridAsString() {

    final StringBuilder builder = new StringBuilder();

    // top of grid
    for (int i = 0; i < columnCount; i++) {
      if (i == 0) {
        if (getCell(i, 0) != null) {
          builder.append("┏");
        } else {
          builder.append("┌");
        }
      } else {
        final Cell c = getCell(i - 1, 0);
        final Cell d = getCell(i, 0);
        if (c == null && d == null) {
          builder.append("┬");
        } else {
          if (connected(c, d)) {
            builder.append("┯");
          } else {
            if (c == null) {
              builder.append("┲");
            } else if (d == null) {
              builder.append("┱");
            } else {
              builder.append("┳");
            }
          }
        }
      }
      if (getCell(i, 0) != null) {
        builder.append("━");
      } else {
        builder.append("─");
      }

    }
    if (getCell(columnCount - 1, 0) != null) {
      builder.append("┓");
    } else {
      builder.append("┐");
    }
    builder.append("\n");

    for (int j = 0; j < rowCount; j++) {

      // between the cells
      if (j != 0) {
        for (int i = 0; i < columnCount; i++) {
          if (i == 0) {
            final Cell b = getCell(0, j - 1);
            final Cell d = getCell(0, j);
            if (b == null && d == null) {
              builder.append("├");
            } else {
              if (connected(b, d)) {
                builder.append("┠");
              } else {
                if (b == null) {
                  builder.append("┢");
                } else if (d == null) {
                  builder.append("┡");
                } else {
                  builder.append("┣");
                }
              }
            }
          } else {
            final Cell a = getCell(i - 1, j - 1);
            final Cell b = getCell(i, j - 1);
            final Cell c = getCell(i - 1, j);
            final Cell d = getCell(i, j);
//            a│b
//            ─┼─
//            c│d
            if (connected(a, b)) {
              if (connected(c, d)) {
                if (connected(a, c)) {
                  builder.append("┼");
                } else {
                  builder.append("┿");
                }
              } else {
                builder.append("╈");
              }
            } else {
              if (connected(c, d)) {
                if (connected(a, c)) {
                  builder.append("╄");
                } else if (connected(b, d)) {
                  builder.append("╃");
                } else {
                  builder.append("╇");
                }
              } else {
                if (connected(a, c)) {
                  if (connected(b, d)) {
                    builder.append("╂");
                  } else {
                    builder.append("╊");
                  }
                } else {
                  if (connected(b, d)) {
                    builder.append("╉");
                  } else {
                    builder.append("╋");
                  }
                }
              }
            }
          }
          final Cell a = getCell(i, j - 1);
          final Cell c = getCell(i, j);
          if (connected(a, c)) {
            builder.append("─");
          } else {
            builder.append("━");
          }
        }
        final Cell a = getCell(columnCount - 1, j - 1);
        final Cell c = getCell(columnCount - 1, j);
        if (a == null && c == null) {
          builder.append("┤");
        } else {
          if (connected(a, c)) {
            builder.append("┨");
          } else {
            if (a == null) {
              builder.append("┪");
            } else if (c == null) {
              builder.append("┩");
            } else {
              builder.append("┫");
            }
          }
        }
        builder.append("\n");
      }

      // cell
      for (int i = 0; i < columnCount; i++) {
        if (i == 0) {
          if (getCell(i, j) != null) {
            builder.append("┃");
          } else {
            builder.append("│");
          }
        } else {
          final Cell c = getCell(i - 1, j);
          final Cell d = getCell(i, j);
          if (connected(c, d)) {
            builder.append("│");
          } else {
            builder.append("┃");
          }
        }
        if (hasError(i, j)) {
          builder.append("✖"); //↯
        } else {
          if (getCell(i, j) instanceof OriginCell) {
            builder.append("█");
          } else if (getCell(i, j) instanceof SpanCell) {
            if (j == 0) {
              builder.append("➞");
            } else {
              final Cell a = getCell(i, j - 1);
              final Cell c = getCell(i, j);
              if (connected(a, c)) {
                builder.append("⬇");
              } else {
                builder.append("➞");
              }
            }
          } else {
            builder.append("◌");
          }
        }
      }
      if (getCell(columnCount - 1, j) != null) {
        builder.append("┃");
      } else {
        builder.append("│");
      }
      builder.append("\n");
    }

    //last bottom
    for (int i = 0; i < columnCount; i++) {
      if (i == 0) {
        if (getCell(0, rowCount - 1) != null) {
          builder.append("┗");
        } else {
          builder.append("└");
        }
      } else {
        final Cell a = getCell(i - 1, rowCount - 1);
        final Cell b = getCell(i, rowCount - 1);
        if (a == null && b == null) {
          builder.append("┴");
        } else {
          if (connected(a, b)) {
            builder.append("┷");
          } else {
            if (a == null) {
              builder.append("┺");
            } else if (b == null) {
              builder.append("┹");
            } else {
              builder.append("┻");
            }
          }
        }
      }
      if (getCell(i, rowCount - 1) != null) {
        builder.append("━");
      } else {
        builder.append("─");
      }
    }
    if (getCell(columnCount - 1, rowCount - 1) != null) {
      builder.append("┛");
    } else {
      builder.append("┘");
    }
    builder.append("\n");

    return builder.toString();
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append(gridAsString());
    builder.append("columnHeads=");
    builder.append(Arrays.toString(columnHeads));
    builder.append("\n");
    builder.append("rowHeads=");
    builder.append(Arrays.toString(rowHeads));
    builder.append("\n");
    return builder.toString();
  }

  private boolean connected(final Cell a, final Cell b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return a.getOrigin().equals(b.getOrigin());
  }
}
