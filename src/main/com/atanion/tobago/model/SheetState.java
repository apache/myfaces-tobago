/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 22.06.2004 09:08:37.
 * $Id$
 */
package com.atanion.tobago.model;

public class SheetState {
  private int first;
  private int sortedColumn;
  private boolean ascending;

  public int getFirst() {
    return first;
  }

  public void setFirst(int first) {
    this.first = first;
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
}
