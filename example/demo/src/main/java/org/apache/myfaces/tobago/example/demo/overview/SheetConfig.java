package org.apache.myfaces.tobago.example.demo.overview;

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

/*
 * Created 19.01.2005 21:49:41.
 * $Id$
 */


import javax.faces.model.SelectItem;


public class SheetConfig {

  private static final String[] SHEET_PAGER_POSITION_KEYS = {
      "none",
      "left",
      "center",
      "right"
  };

  private static final String[] SHEET_SELECTABLE_KEYS = {
      "none",
      "single",
      "multi"
  };

  private boolean sheetShowHeader;
  private boolean showPagingAlways;
  private int sheetFirst;
  private int sheetRows;
  private int sheetDirectLinkCount;
  private String sheetRowPagingPosition;
  private String sheetDirectPagingPosition;
  private String sheetPagePagingPosition;
  private SelectItem[] sheetDirectLinkCountItems;
  private SelectItem[] sheetPagingPositionItems;
  private SelectItem[] sheetSelectableItems;
  private String selectable;
  private boolean sheetConfigPopup;

  public SheetConfig() {
    sheetFirst = 0;
    sheetRows = 10;
    sheetDirectLinkCount = 5;
    sheetDirectLinkCountItems = createSheetItems(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
    sheetShowHeader = true;
    showPagingAlways = false;
    sheetRowPagingPosition = SHEET_PAGER_POSITION_KEYS[1];
    sheetDirectPagingPosition = SHEET_PAGER_POSITION_KEYS[2];
    sheetPagePagingPosition = SHEET_PAGER_POSITION_KEYS[3];
    sheetPagingPositionItems = createSheetItems(SHEET_PAGER_POSITION_KEYS);
    sheetSelectableItems = createSheetItems(SHEET_SELECTABLE_KEYS);
    selectable = SHEET_SELECTABLE_KEYS[2];
  }


  public String configSheet() {
    sheetConfigPopup = true;
    return null;
  }

  private SelectItem[] createSheetItems(Object[] values) {
    SelectItem[] items = new SelectItem[values.length];

    for (int i = 0; i < values.length; i++) {
      items[i] = new SelectItem(values[i], values[i].toString());
    }
    return items;
  }


  public boolean isSheetShowHeader() {
    return sheetShowHeader;
  }

  public void setSheetShowHeader(boolean sheetShowHeader) {
    this.sheetShowHeader = sheetShowHeader;
  }

  public boolean isShowPagingAlways() {
    return showPagingAlways;
  }

  public void setShowPagingAlways(boolean showPagingAlways) {
    this.showPagingAlways = showPagingAlways;
  }

  public int getSheetFirst() {
    return sheetFirst;
  }

  public int getSheetRows() {
    return sheetRows;
  }

  public void setSheetRows(int sheetRows) {
    this.sheetRows = sheetRows;
  }

  public int getSheetFirstValue() {
    int value = getSheetFirst();
    return (value - 1) < 0 ? 0 : value - 1;
  }

  public void setSheetFirst(int sheetFirst) {
    this.sheetFirst = sheetFirst;
  }

  public boolean isSheetConfigPopup() {
    return sheetConfigPopup;
  }

  public void setSheetConfigPopup(boolean sheetConfigPopup) {
    this.sheetConfigPopup = sheetConfigPopup;
  }

  public int getSheetDirectLinkCount() {
    return sheetDirectLinkCount;
  }

  public void setSheetDirectLinkCount(int sheetDirectLinkCount) {
    this.sheetDirectLinkCount = sheetDirectLinkCount;
  }

  public SelectItem[] getSheetDirectLinkCountItems() {
    return sheetDirectLinkCountItems;
  }

  public String getSheetRowPagingPosition() {
    return sheetRowPagingPosition;
  }

  public void setSheetRowPagingPosition(String sheetRowPagingPosition) {
    this.sheetRowPagingPosition = sheetRowPagingPosition;
  }

  public String getSheetDirectPagingPosition() {
    return sheetDirectPagingPosition;
  }

  public void setSheetDirectPagingPosition(String sheetDirectPagingPosition) {
    this.sheetDirectPagingPosition = sheetDirectPagingPosition;
  }

  public String getSheetPagePagingPosition() {
    return sheetPagePagingPosition;
  }

  public void setSheetPagePagingPosition(String sheetPagePagingPosition) {
    this.sheetPagePagingPosition = sheetPagePagingPosition;
  }

  public SelectItem[] getSheetPagingPositionItems() {
    return sheetPagingPositionItems;
  }

  public SelectItem[] getSheetSelectableItems() {
    return sheetSelectableItems;
  }

  public void setSheetSelectableItems(SelectItem[] sheetSelectableItems) {
    this.sheetSelectableItems = sheetSelectableItems;
  }

  public String getSelectable() {
    return selectable;
  }

  public void setSelectable(String selectable) {
    this.selectable = selectable;
  }
}
