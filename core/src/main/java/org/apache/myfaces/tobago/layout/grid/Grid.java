package org.apache.myfaces.tobago.layout.grid;

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
import org.apache.myfaces.tobago.layout.AutoLayoutToken;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Orientation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grid {

  private static final Log LOG = LogFactory.getLog(Grid.class);

  // TODO: check if it is faster with arrays.
  /**
   * The rectangular data as a 1-dim list
   */
  private List<Cell> cells;
  private LayoutTokens columns;
  private LayoutTokens rows;
  private Measure[] widths;
  private Measure[] heights;

  private List<Integer> errorIndexes;

  private int columnCursor;
  private int rowCursor;
  private int[] horizontalIndices;
  private int[] verticalIndices;

  public Grid(LayoutTokens columns, LayoutTokens rows) {
    assert columns.getSize() > 0;
    assert rows.getSize() > 0;

    this.columns = columns;
    this.rows = rows;
    int size = columns.getSize() * rows.getSize();
    this.cells = new ArrayList<Cell>(size);
    for (int i = 0; i < size; i++) {
      this.cells.add(null);
    }

    widths = new Measure[columns.getSize()];
    heights = new Measure[rows.getSize()];
  }

  public void add(OriginCell cell, int columnSpan, int rowSpan) {

    assert columnSpan > 0;
    assert rowSpan > 0;

    boolean error = false;

    if (columnSpan + columnCursor > columns.getSize()) {
      LOG.warn("The columnSpan is to large for the actual position in the grid. Will be fixed. "
          + "columnSpan=" + columnSpan + " columnCursor=" + columnCursor + " columnCount=" + columns.getSize());
      columnSpan = columns.getSize() - columnCursor;
      error = true;
    }

    cell.setColumnSpan(columnSpan);
    cell.setRowSpan(rowSpan);

    for (int i = 1; i < columnSpan; i++) {
      if (getCell(i + columnCursor, rowCursor) != null) {
        LOG.warn("The columnSpan is to large for the actual position in the grid. Will be fixed. "
            + "columnSpan=" + columnSpan + " columnCursor=" + columnCursor + " columnCount=" + columns.getSize());
        columnSpan = i - 1;
        error = true;
      }
    }

    for (int j = 0; j < rowSpan; j++) {
      for (int i = 0; i < columnSpan; i++) {
        Cell actualCell;
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

  public Cell getCell(int i, int j, Orientation orientation) {
    return orientation == Orientation.HORIZONTAL ? getCell(i, j) : getCell(j, i);
  }

  public Cell getCell(int column, int row) {
    assert column >= 0 && column < columns.getSize() : "column=" + column + " columnCount=" + columns.getSize();
    assert row >= 0 : "row=" + row;

    if (row >= rows.getSize()) {
      return null;
    } else {
      return cells.get(column + row * columns.getSize());
    }
  }

  public void setCell(int column, int row, Cell cell) {
    if (row >= rows.getSize()) {
      enlarge(row - rows.getSize() + 1);
    }
    cells.set(column + row * columns.getSize(), cell);
  }

  private void findNextFreeCell() {
    for (; rowCursor < rows.getSize(); rowCursor++) {
      for (; columnCursor < columns.getSize(); columnCursor++) {
        if (getCell(columnCursor, rowCursor) == null) {
          return;
        }
      }
      columnCursor = 0;
    }
  }

  public LayoutTokens getTokens(Orientation orientation) {
    return orientation == Orientation.HORIZONTAL ? getColumns() : getRows();
  }

  public LayoutTokens getColumns() {
    return columns;
  }

  public LayoutTokens getRows() {
    return rows;
  }

  private void enlarge(int newRows) {
    for (int i = 0; i < newRows; i++) {
      for (int j = 0; j < columns.getSize(); j++) {
        cells.add(null);
      }
      rows.addToken(AutoLayoutToken.INSTANCE);
    }

    Measure[] oldHeights = heights;
    heights = new Measure[heights.length + newRows];
    System.arraycopy(oldHeights, 0, heights, 0, oldHeights.length);
  }

  public void addError(int i, int j) {
    if (errorIndexes == null) {
      errorIndexes = new ArrayList<Integer>();
    }
    errorIndexes.add(j * columns.getSize() + i);
  }

  public boolean hasError(int i, int j) {
    if (errorIndexes == null) {
      return false;
    }
    return errorIndexes.contains(j * columns.getSize() + i);
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

    StringBuilder builder = new StringBuilder();

    // top of grid
    for (int i = 0; i < columns.getSize(); i++) {
      if (i == 0) {
        if (getCell(i, 0) != null) {
          builder.append("┏");
        } else {
          builder.append("┌");
        }
      } else {
        Cell c = getCell(i - 1, 0);
        Cell d = getCell(i, 0);
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
    if (getCell(columns.getSize() - 1, 0) != null) {
      builder.append("┓");
    } else {
      builder.append("┐");
    }
    builder.append("\n");

    for (int j = 0; j < rows.getSize(); j++) {

      // between the cells
      if (j != 0) {
        for (int i = 0; i < columns.getSize(); i++) {
          if (i == 0) {
            Cell b = getCell(0, j - 1);
            Cell d = getCell(0, j);
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
            Cell a = getCell(i - 1, j - 1);
            Cell b = getCell(i, j - 1);
            Cell c = getCell(i - 1, j);
            Cell d = getCell(i, j);
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
          Cell a = getCell(i, j - 1);
          Cell c = getCell(i, j);
          if (connected(a, c)) {
            builder.append("─");
          } else {
            builder.append("━");
          }
        }
        Cell a = getCell(columns.getSize() - 1, j - 1);
        Cell c = getCell(columns.getSize() - 1, j);
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
      for (int i = 0; i < columns.getSize(); i++) {
        if (i == 0) {
          if (getCell(i, j) != null) {
            builder.append("┃");
          } else {
            builder.append("│");
          }
        } else {
          Cell c = getCell(i - 1, j);
          Cell d = getCell(i, j);
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
              Cell a = getCell(i, j - 1);
              Cell c = getCell(i, j);
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
      if (getCell(columns.getSize() - 1, j) != null) {
        builder.append("┃");
      } else {
        builder.append("│");
      }
      builder.append("\n");
    }

    //last bottom
    for (int i = 0; i < columns.getSize(); i++) {
      if (i == 0) {
        if (getCell(0, rows.getSize() - 1) != null) {
          builder.append("┗");
        } else {
          builder.append("└");
        }
      } else {
        Cell a = getCell(i - 1, rows.getSize() - 1);
        Cell b = getCell(i, rows.getSize() - 1);
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
      if (getCell(i, rows.getSize() - 1) != null) {
        builder.append("━");
      } else {
        builder.append("─");
      }
    }
    if (getCell(columns.getSize() - 1, rows.getSize() - 1) != null) {
      builder.append("┛");
    } else {
      builder.append("┘");
    }
    builder.append("\n");

    return builder.toString();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(gridAsString());
    builder.append("columns=");
    builder.append(columns);
    builder.append("\n");
    builder.append("rows=");
    builder.append(rows);
    builder.append("\n");
    builder.append("widths=");
    builder.append(Arrays.toString(widths));
    builder.append("\n");
    builder.append("heights=");
    builder.append(Arrays.toString(heights));
    builder.append("\n");
    return builder.toString();
  }

  private boolean connected(Cell a, Cell b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return a.getOrigin().equals(b.getOrigin());
  }

  public int[] getHorizontalIndices() {
    return horizontalIndices;
  }

  public void setHorizontalIndices(int[] horizontalIndices) {
    this.horizontalIndices = horizontalIndices;
  }

  public int[] getVerticalIndices() {
    return verticalIndices;
  }

  public void setVerticalIndices(int[] verticalIndices) {
    this.verticalIndices = verticalIndices;
  }

  public Measure[] getSizes(Orientation orientation) {
    return orientation == Orientation.HORIZONTAL ? getWidths() : getHeights();
  }

  public Measure[] getWidths() {
    return widths;
  }

  public Measure[] getHeights() {
    return heights;
  }
}
