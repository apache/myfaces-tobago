package org.apache.myfaces.tobago.model;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import org.apache.myfaces.tobago.component.UIData;
import org.apache.myfaces.tobago.event.SortActionEvent;

import javax.faces.component.UIColumn;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
// TODO find a better solution for this 
public class SheetState implements Serializable {

  private static final Log LOG = LogFactory.getLog(SheetState.class);
  public static final String SEPARATOR = ",";

  private int first = -1;
  private int sortedColumn = -1;
  private boolean ascending;
  private String columnWidths;
  private List<Integer> selectedRows;

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
    this.selectedRows = selectedRows;
  }

  public int getSortedColumn() {
    return sortedColumn;
  }

  public void setSortedColumn(int sortedColumn) {
    this.sortedColumn = sortedColumn;
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

  public boolean updateSortState(SortActionEvent sortEvent) {
    UIData sheet = sortEvent.getSheet();
    UIColumn uiColumn = sortEvent.getColumn();
    int actualColumn = -1;
    List<UIColumn> rendererdColumns = sheet.getRendererdColumns();
    for (int i = 0; i < rendererdColumns.size(); i++) {
      if (uiColumn == rendererdColumns.get(i)) {
        actualColumn = i;
        break;
      }
    }
    if (actualColumn == -1) {
      LOG.warn("Can't find column to sort in rendered columns of sheet!");
      return false;
    }

    if (actualColumn == sortedColumn) {
      ascending = !ascending;
    } else {
      ascending = true;
      sortedColumn = actualColumn;
    }
    return true;
  }
}
