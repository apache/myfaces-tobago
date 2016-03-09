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
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIColumn;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SheetState implements Serializable, ScrollPositionState {

  private static final long serialVersionUID = 2L;

  private static final Logger LOG = LoggerFactory.getLogger(SheetState.class);

  public static final String SEPARATOR = ",";

  private int first;
  private String sortedColumnId;
  private boolean ascending;
  private boolean toBeSorted;
  private String columnWidths;
  private List<Integer> selectedRows;
  private ScrollPosition scrollPosition;
  private ExpandedState expandedState;
  private SelectedState selectedState;

  public SheetState() {
    reset();
  }

  public void reset() {
    first = -1;
    sortedColumnId = null;
    ascending = true;
    toBeSorted = false;
    columnWidths = null;
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
    selectedRows = new ArrayList<Integer>();
  }

  public List<Integer> getSelectedRows() {
    return selectedRows;
  }

  public void setSelectedRows(final List<Integer> selectedRows) {
    assert selectedRows != null;
    this.selectedRows = selectedRows;
  }

  public String getSortedColumnId() {
    return sortedColumnId;
  }

  public void setSortedColumnId(final String sortedColumnId) {
    if (StringUtils.notEquals(this.sortedColumnId, sortedColumnId)) {
      this.sortedColumnId = sortedColumnId;
      toBeSorted = true;
    }
  }

  public boolean isAscending() {
    return ascending;
  }

  public void setAscending(final boolean ascending) {
    if (this.ascending != ascending) {
      this.ascending = ascending;
      toBeSorted = true;
    }
  }

  public String getColumnWidths() {
    return columnWidths;
  }

  public void setColumnWidths(final String columnWidths) {
    this.columnWidths = columnWidths;
  }

  public int getFirst() {
    return first;
  }

  public void setFirst(final int first) {
    this.first = first;
  }

  public void updateSortState(final SortActionEvent sortEvent) {

    final UIColumn actualColumn = sortEvent.getColumn();

    if (actualColumn.getId().equals(sortedColumnId)) {
      setAscending(!isAscending());
    } else {
      setAscending(true);
      setSortedColumnId(actualColumn.getId());
    }
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

  public boolean isToBeSorted() {
    return toBeSorted;
  }

  public void setToBeSorted(boolean toBeSorted) {
    this.toBeSorted = toBeSorted;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static Integer[] parseScrollPosition(final String value) {
    Integer[] position = null;
    if (!StringUtils.isBlank(value)) {
      final int sep = value.indexOf(";");
      if (LOG.isInfoEnabled()) {
        LOG.info("value = \"" + value + "\"  sep = " + sep + "");
      }
      if (sep == -1) {
        throw new NumberFormatException(value);
      }
      final int left = Integer.parseInt(value.substring(0, sep));
      final int top = Integer.parseInt(value.substring(sep + 1));
      position = new Integer[2];
      position[0] = left;
      position[1] = top;
    }
    return position;
  }
}
