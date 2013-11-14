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

public class SheetState implements Serializable {

  private static final long serialVersionUID = 7765536344426661777L;

  private static final Logger LOG = LoggerFactory.getLogger(SheetState.class);

  public static final String SEPARATOR = ",";

  private int first = -1;
  private String sortedColumnId;
  private boolean ascending;
  private String columnWidths;
  private List<Integer> selectedRows;
  private Integer[] scrollPosition;
  private ExpandedState expandedState;
  private SelectedState selectedState;

  public SheetState() {
    resetSelected();
  }

  public void resetSelected() {
    selectedRows = new ArrayList<Integer>();
  }

  public List<Integer> getSelectedRows() {
    return selectedRows;
  }

  public void setSelectedRows(List<Integer> selectedRows) {
    assert selectedRows != null;
    this.selectedRows = selectedRows;
  }

  public String getSortedColumnId() {
    return sortedColumnId;
  }

  public void setSortedColumnId(String sortedColumnId) {
    this.sortedColumnId = sortedColumnId;
  }

  public boolean isAscending() {
    return ascending;
  }

  public void setAscending(boolean ascending) {
    this.ascending = ascending;
  }

  public String getColumnWidths() {
    return columnWidths;
  }

  public void setColumnWidths(String columnWidths) {
    this.columnWidths = columnWidths;
  }

  public int getFirst() {
    return first;
  }

  public void setFirst(int first) {
    this.first = first;
  }

  public void updateSortState(SortActionEvent sortEvent) {

    UIColumn actualColumn = sortEvent.getColumn();

    if (actualColumn.getId().equals(sortedColumnId)) {
      ascending = !ascending;
    } else {
      ascending = true;
      sortedColumnId = actualColumn.getId();
    }
  }

  public Integer[] getScrollPosition() {
    return scrollPosition;
  }

  public void setScrollPosition(Integer[] scrollPosition) {
    this.scrollPosition = scrollPosition;
  }

  public ExpandedState getExpandedState() {
    if (expandedState == null) {
      expandedState = new ExpandedState(2);
    }
    return expandedState;
  }

  public void setExpandedState(ExpandedState expandedState) {
    this.expandedState = expandedState;
  }

  public SelectedState getSelectedState() {
    if (selectedState == null) {
      selectedState = new SelectedState();
    }
    return selectedState;
  }

  public void setSelectedState(SelectedState selectedState) {
    this.selectedState = selectedState;
  }

  public static Integer[] parseScrollPosition(String value) {
    Integer[] position = null;
    if (!StringUtils.isBlank(value)) {
      int sep = value.indexOf(";");
      if (LOG.isInfoEnabled()) {
        LOG.info("value = \"" + value + "\"  sep = " + sep + "");
      }
      if (sep == -1) {
        throw new NumberFormatException(value);
      }
      int left = Integer.parseInt(value.substring(0, sep));
      int top = Integer.parseInt(value.substring(sep + 1));
      position = new Integer[2];
      position[0] = left;
      position[1] = top;
    }
    return position;
  }
}
