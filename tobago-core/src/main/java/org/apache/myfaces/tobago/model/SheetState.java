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

import jakarta.faces.application.ProjectStage;
import jakarta.faces.context.FacesContext;
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
  private LazyScrollPosition lazyScrollPosition;
  private ExpandedState expandedState;
  private SelectedState selectedState;

  /**
   * @deprecated use ShettState(1) instead
   */
  @Deprecated(since = "5.3.0", forRemoval = true)
  public SheetState() {
    this(1);
  }

  /**
   * Creates a sheet state with a maximum number of simultaneously sortable columns.
   *
   * <p>
   * The parameter {@code maxSortColumns} specifies how many columns are taken into account when sorting.
   * Use a value {@code >1} if multiple rows contain the same value. In this case, the value in the next sort-selected
   * column is used for further sorting.
   * </p>
   *
   * @param maxSortColumns number of columns considered for sorting
   */
  public SheetState(final int maxSortColumns) {
    reset(maxSortColumns);
  }

  /**
   * @deprecated use reset(1) instead
   */
  @Deprecated(since = "5.3.0", forRemoval = true)
  public void reset() {
    reset(1);
  }

  /**
   * Reset the sheet state.
   *
   * @param maxSortColumns number of columns considered for sorting
   */
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
    if (lazyScrollPosition != null) {
      lazyScrollPosition.clear();
    } else {
      lazyScrollPosition = new LazyScrollPosition();
    }
  }

  /**
   * Reset selected rows.
   */
  public void resetSelected() {
    selectedRows = new ArrayList<>();
  }

  /**
   * @return list of selected row indexes
   */
  public List<Integer> getSelectedRows() {
    return selectedRows;
  }

  /**
   * @param selectedRows indexes of selected rows
   */
  public void setSelectedRows(final List<Integer> selectedRows) {
    assert selectedRows != null;
    this.selectedRows = selectedRows;
  }

  /**
   * @deprecated Please use {@link #getSortedColumnList()}.get(...).getId()
   */
  @Deprecated(since = "5.3.0", forRemoval = true)
  public String getSortedColumnId() {
    if (sortedColumnList.isEmpty()) {
      return null;
    } else {
      return sortedColumnList.getFirst().getId();
    }
  }

  /**
   * @deprecated Please use {@link #getSortedColumnList()}.get(...).setId()
   */
  @Deprecated(since = "5.3.0", forRemoval = true)
  public void setSortedColumnId(final String sortedColumnId) {
    if (!FacesContext.getCurrentInstance().isProjectStage(ProjectStage.Production)) {
      Deprecation.LOG.warn("Method SheetState.setSortedColumnId() should not be called!");
    }
    if (sortedColumnList.isEmpty()) {
      sortedColumnList.add(sortedColumnId, true);
    } else {
      if (StringUtils.notEquals(sortedColumnList.getFirst().getId(), sortedColumnId)) {
        sortedColumnList.getFirst().setId(sortedColumnId);
      }
    }
  }

  /**
   * @deprecated Please use {@link #getSortedColumnList()}.get(...).isAscending()
   */
  @Deprecated(since = "5.3.0", forRemoval = true)
  public boolean isAscending() {
    if (sortedColumnList.isEmpty()) {
      return true;
    } else {
      return sortedColumnList.getFirst().isAscending();
    }
  }

  /**
   * @deprecated Please use {@link #getSortedColumnList()}.get(...).setAscending()
   */
  @Deprecated(since = "5.3.0", forRemoval = true)
  public void setAscending(final boolean ascending) {
    if (!FacesContext.getCurrentInstance().isProjectStage(ProjectStage.Production)) {
      Deprecation.LOG.warn("Method SheetState.setAscending() should not be called!");
    }
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

  /**
   * @return index of the first visible row
   */
  public int getFirst() {
    return first;
  }

  /**
   * @param first index of the first visible row
   */
  public void setFirst(final int first) {
    this.first = first;
  }

  /**
   * @deprecated Please use {@link #updateSortState(String id)}
   */
  @Deprecated(since = "4.2.0", forRemoval = true)
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

  public LazyScrollPosition getLazyScrollPosition() {
    return lazyScrollPosition;
  }

  public void setLazyScrollPosition(LazyScrollPosition lazyScrollPosition) {
    this.lazyScrollPosition = lazyScrollPosition;
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
   * @deprecated Please use {@link #getToBeSortedLevel()}
   */
  @Deprecated(since = "5.3.0", forRemoval = true)
  public boolean isToBeSorted() {
    return getToBeSortedLevel() > 0;
  }

  /**
   * @deprecated Please use {@link #sorted()}
   */
  @Deprecated(since = "5.3.0", forRemoval = true)
  public void setToBeSorted(final boolean toBeSorted) {
    if (toBeSorted) {
      sortedColumnList.setToBeSortedLevel(Math.max(1, sortedColumnList.getToBeSortedLevel()));
    } else {
      sorted();
    }
  }

  /**
   * @return the number of levels (columns) which must be sorted this request
   */
  public int getToBeSortedLevel() {
    return sortedColumnList.getToBeSortedLevel();
  }

  /**
   * Indicates that the sheet is sorted and a SortActionEvent is not executed in this request.
   */
  public void sorted() {
    sortedColumnList.sorted();
  }
}
