/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 22.06.2004 09:08:37.
 * $Id$
 */
package com.atanion.tobago.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SheetState {

  private static final Log LOG = LogFactory.getLog(SheetState.class);
  public static final String SEPARATOR = ",";

  private int sortedColumn;
  private boolean ascending;
  private String selected;
  private String columnWidths;

  public void resetSelected() {
    selected = "";
  }

  public int[] getSelectedIndices() {
    List list = parse(selected);
    int[] indices = new int[list.size()];
    for (int i = 0; i < indices.length; i++){
      indices[i] = ((Integer)list.get(i)).intValue();
    }
    return indices;
  }

  public static List parse(String widthListString) {
    List list = new ArrayList();

    StringTokenizer tokenizer = new StringTokenizer(widthListString, ",");
    while (tokenizer.hasMoreElements()) {
      String token = tokenizer.nextToken().trim();
      if (token.length() > 0) {
        try {
          list.add(new Integer(token));
        } catch (NumberFormatException e) {
          LOG.warn(e.getMessage(), e);
        }
      }
    }

    return list;
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

  public String getSelected() {
    return selected;
  }

  public void setSelected(String selected) {
    this.selected = selected;
  }

  public String getColumnWidths() {
    return columnWidths;
  }

  public void setColumnWidths(String columnWidths) {
    this.columnWidths = columnWidths;
  }
}
