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

package org.apache.myfaces.tobago.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Holds the sorting state for a sheet (data table).
 *
 * <p>This class maintains an ordered list of {@link SortedColumn} entries representing the
 * current multi-column sort, where the first entry has the highest precedence. The size of
 * the list is limited by {@code max}.</p>
 *
 * <p>The property {@link #getToBeSortedLevel()} indicates how many leading sort levels must be
 * (re)applied during the current request. It is increased when the sort order changes and reset by
 * {@link #sorted()} after the data has been processed.</p>
 *
 * <p>New sort requests move the affected column to the front of the list; repeating the request on the
 * same column toggles its direction.</p>
 */
public class SortedColumnList implements Serializable {
  private final int max;
  private final boolean showNumbers;
  private final List<SortedColumn> list;
  private int toBeSortedLevel;

  /**
   * Creates a new sort state with a maximum number of tracked sort levels.
   *
   * <p>The list will keep at most {@code max} entries. If {@code max} is less than {@code 1}, it will
   * be normalized to {@code 1}. When {@code max > 1}, {@link #isShowNumbers()} will be {@code true}
   * so that the UI can optionally display sort order numbers.</p>
   *
   * @param max the maximum number of columns to consider for multi-column sorting
   */
  public SortedColumnList(final int max) {
    this.max = Math.max(max, 1);
    this.showNumbers = max > 1;
    this.list = new ArrayList<>(max);
    this.toBeSortedLevel = 0;
  }

  /**
   * Returns whether no sort order is currently defined.
   */
  public boolean isEmpty() {
    return list.size() == 0;
  }

  /**
   * Returns the primary sort column, if any.
   */
  public SortedColumn getFirst() {
    return list.size() > 0 ? list.get(0) : null;
  }

  /**
   * Updates the current sort state for the given column identifier.
   *
   * <p>If the column is already the primary sort key, its direction is toggled and at least the first
   * level is marked to be re-sorted. Otherwise, the column is moved/inserted to the front as ascending,
   * previous occurrence (if any) is removed, and the number of levels to be sorted is adjusted.
   * The list size is then normalized to the configured maximum.</p>
   *
   * @param columnId the technical identifier of the column to sort by
   */
  public void updateSortState(final String columnId) {
    if (getFirst() != null && Objects.equals(getFirst().getId(), columnId)) {
      getFirst().toggle();
      toBeSortedLevel = Math.max(1, toBeSortedLevel);
    } else {
      final int index = indexOf(columnId);
      if (index >= 0) {
        list.remove(index);
        toBeSortedLevel--;
      }
      list.add(0, new SortedColumn(columnId, true));
      toBeSortedLevel++;
      checkSize();
    }
  }

  /**
   * Adds a sorted column entry to the end of the list.
   *
   * @param columnId the technical identifier of the column
   * @param ascending {@code true} for ascending order, {@code false} for descending
   * @deprecated Only for backward compatibility of deprecated functions. Use {@link #updateSortState(String)} instead.
   */
  @Deprecated(since = "5.3.0", forRemoval = true)
  public void add(final String columnId, final boolean ascending) {
    list.add(new SortedColumn(columnId, ascending));
    toBeSortedLevel++;
    checkSize();
  }

  /**
   * Returns the {@link SortedColumn} at the given position.
   *
   * @param index zero-based index into the current sort list
   * @return the {@link SortedColumn} at the given index
   */
  public SortedColumn get(int index) {
    return list.get(index);
  }

  /**
   * Ensures the internal list and bookkeeping respect the configured maximum size.
   *
   * <p>Removes trailing entries while the size exceeds {@code max} and caps
   * {@link #toBeSortedLevel} to {@code max} as well.</p>
   */
  private void checkSize() {
    while (size() > max) {
      list.remove(size() - 1);
    }
    if (toBeSortedLevel > max) {
      toBeSortedLevel = max;
    }
  }

  /**
   * Returns the number of active sort levels currently stored.
   *
   * @return the size of the internal list of {@link SortedColumn} entries
   */
  public int size() {
    return list.size();
  }

  /**
   * Removes all {@link SortedColumn} entries from this list.
   */
  public void clear() {
    list.clear();
  }

  /**
   * Indicates that every column is sorted and a SortActionEvent is not executed in this request.
   */
  public void sorted() {
    toBeSortedLevel = 0;
  }

  /**
   * Returns the index of a column with the given identifier.
   *
   * @param id the technical identifier of the column to look up
   */
  public int indexOf(String id) {
    for (int i = 0; i < size(); i++) {
      SortedColumn sortedColumn = list.get(i);
      if (Objects.equals(sortedColumn.getId(), id)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * <p>If {@code max > 1} the UI may show sort order numbers, which can be queried.</p>
   *
   * <p>Method is used internally by the Tobago sorting component.</p>
   */
  public boolean isShowNumbers() {
    return showNumbers;
  }

  /**
   * <p>Number of levels (columns) which are still to be sorted. Is smaller or equals
   * the size of {@code list} (columns to sort by).</p>
   *
   * <p>Method is used internally by the Tobago sorting component.</p>
   *
   * @return the number of levels (columns) which must be sorted this request
   */
  public int getToBeSortedLevel() {
    return toBeSortedLevel;
  }

  /**
   * @deprecated Please use {@link #sorted()}
   */
  @Deprecated(since = "5.9.1", forRemoval = true)
  public void setToBeSortedLevel(int toBeSortedLevel) {
    this.toBeSortedLevel = toBeSortedLevel;
  }
}
