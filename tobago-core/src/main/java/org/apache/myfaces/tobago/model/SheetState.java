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

import org.apache.myfaces.tobago.event.SortActionEvent;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.internal.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SheetState implements Serializable, ScrollPositionState {

  private static final long serialVersionUID = 2L;

  private int first;
  private SortedColumnList sortedColumnList;
  private List<Integer> columnWidths;
  private List<Integer> selectedRows;
  private ScrollPosition scrollPosition;
  private ExpandedState expandedState;
  private SelectedState selectedState;

  /**
   * @deprecated since 5.3.0
   */
  @Deprecated
  public SheetState() {
    this(1);
  }

  public SheetState(final int maxSortColumns) {
    reset(maxSortColumns);
  }

  /**
   * @deprecated since 5.3.0
   */
  @Deprecated
  public void reset() {
    reset(1);
  }

  public void reset(final int maxSortColumns) {
    first = -1;
    sortedColumnList = new SortedColumnList(maxSortColumns);
    columnWidths = new ArrayList<>();
    resetSelected();
    if (expandedState != null) {
      expandedState.reset();
    }
    if (selectedState != null) {
      selectedState.clear();
    }
    if (scrollPosition != null) {
      scrollPosition.clear();
    } else {
      scrollPosition = new ScrollPosition();
    }
  }

  public void resetSelected() {
    selectedRows = new ArrayList<>();
  }

  public List<Integer> getSelectedRows() {
    return selectedRows;
  }

  public void setSelectedRows(final List<Integer> selectedRows) {
    assert selectedRows != null;
    this.selectedRows = selectedRows;
  }

  /**
   * @deprecated since 5.3.0
   */
  @Deprecated
  public String getSortedColumnId() {
    if (sortedColumnList.isEmpty()) {
      return null;
    } else {
      return sortedColumnList.getFirst().getId();
    }
  }

  /**
   * @deprecated since 5.3.0, please use {@link #updateSortState(String id)}
   */
  @Deprecated
  public void setSortedColumnId(final String sortedColumnId) {
    Deprecation.LOG.warn("Method SheetState.setSortedColumnId() should not be called!");
    if (sortedColumnList.isEmpty()) {
      sortedColumnList.add(sortedColumnId, true);
    } else {
      if (StringUtils.notEquals(sortedColumnList.getFirst().getId(), sortedColumnId)) {
        sortedColumnList.getFirst().setId(sortedColumnId);
      }
    }
  }

  /**
   * @deprecated since 5.3.0
   */
  @Deprecated
  public boolean isAscending() {
    if (sortedColumnList.isEmpty()) {
      return true;
    } else {
      return sortedColumnList.getFirst().isAscending();
    }
  }

  /**
   * @deprecated since 5.3.0, please use {@link #updateSortState(String id)}
   */
  @Deprecated
  public void setAscending(final boolean ascending) {
    Deprecation.LOG.warn("Method SheetState.setAscending() should not be called!");
    if (sortedColumnList.isEmpty()) {
      sortedColumnList.add(null, ascending);
    } else {
      if (sortedColumnList.getFirst().isAscending() != ascending) {
        sortedColumnList.getFirst().setAscending(ascending);
      }
    }
  }

  public SortedColumnList getSortedColumnList() {
    return sortedColumnList;
  }

  public List<Integer> getColumnWidths() {
    return columnWidths;
  }

  public void setColumnWidths(final List<Integer> columnWidths) {
    this.columnWidths = columnWidths;
  }

  public boolean isDefinedColumnWidths() {
    for (final Integer columnWidth : columnWidths) {
      if (columnWidth < 0) {
        return false;
      }
    }
    return columnWidths.size() > 0;
  }

  public int getFirst() {
    return first;
  }

  public void setFirst(final int first) {
    this.first = first;
  }

  /**
   * @deprecated since 4.2.0, please use {@link #updateSortState(String id)}
   */
  @Deprecated
  public void updateSortState(final SortActionEvent sortEvent) {
    updateSortState(sortEvent.getColumn().getId());
  }

  public void updateSortState(final String columnId) {
    sortedColumnList.updateSortState(columnId);
  }

  public void resetSortState() {
    sortedColumnList.clear();
  }

  @Override
  public ScrollPosition getScrollPosition() {
    return scrollPosition;
  }

  public void setScrollPosition(final ScrollPosition scrollPosition) {
    this.scrollPosition = scrollPosition;
  }

  public ExpandedState getExpandedState() {
    if (expandedState == null) {
      expandedState = new ExpandedState(2);
    }
    return expandedState;
  }

  public void setExpandedState(final ExpandedState expandedState) {
    this.expandedState = expandedState;
  }

  public SelectedState getSelectedState() {
    if (selectedState == null) {
      selectedState = new SelectedState();
    }
    return selectedState;
  }

  public void setSelectedState(final SelectedState selectedState) {
    this.selectedState = selectedState;
  }

  /**
   * @deprecated since 5.3.0, please use {@link #getToBeSortedLevel()}
   */
  @Deprecated
  public boolean isToBeSorted() {
    return getToBeSortedLevel() > 0;
  }

  /**
   * @deprecated since 5.3.0, please use {@link #sorted()}
   */
  @Deprecated
  public void setToBeSorted(final boolean toBeSorted) {
    if (toBeSorted) {
      sortedColumnList.setToBeSortedLevel(Math.max(1, sortedColumnList.getToBeSortedLevel()));
    } else {
      sorted();
    }
  }

  public int getToBeSortedLevel() {
    return sortedColumnList.getToBeSortedLevel();
  }

  public void sorted() {
    sortedColumnList.sorted();
  }
}
