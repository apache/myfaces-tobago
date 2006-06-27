package org.apache.myfaces.tobago.example.demo.overview;

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

/*
 * Created 19.01.2005 21:49:41.
 * $Id:  $
 */

import javax.faces.model.SelectItem;


public class SheetConfig {

  private static final String[] SHEET_PAGER_POSITION_KEYS = {
    "none",
    "left",
    "center",
    "right"
  };



  private boolean sheetShowHeader;
  private int sheetFirst;
  private int sheetRows;
  private int sheetDirectLinkCount;
  private String sheetRowPagingPosition;
  private String sheetDirectPagingPosition;
  private String sheetPagePagingPosition;
  private SelectItem[] sheetDirectLinkCountItems;
  private SelectItem[] sheetPagingPositionItems;
  private boolean sheetConfigPopup;

  public SheetConfig() {
    sheetFirst = 0;
    sheetRows = 7;
    sheetDirectLinkCount = 5;
    sheetDirectLinkCountItems = createSheetDirectLinkCountItems();
    sheetShowHeader = true;
    sheetRowPagingPosition = SHEET_PAGER_POSITION_KEYS[1];
    sheetDirectPagingPosition = SHEET_PAGER_POSITION_KEYS[2];
    sheetPagePagingPosition = SHEET_PAGER_POSITION_KEYS[3];
    sheetPagingPositionItems = createSheetPagingPositionItems();
  }


  public String configSheet() {
    sheetConfigPopup = !sheetConfigPopup;
    return null;
  }

  private SelectItem[] createSheetDirectLinkCountItems() {
    int[] counts = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    SelectItem[] items = new SelectItem[counts.length];
    for (int i = 0; i < counts.length; i++) {
      items[i] = new SelectItem(counts[i], Integer.toString(counts[i]));
    }
    return items;
  }



  private SelectItem[] createSheetPagingPositionItems() {
    SelectItem[] items = new SelectItem[SHEET_PAGER_POSITION_KEYS.length];

    for (int i = 0; i < SHEET_PAGER_POSITION_KEYS.length; i++) {
      items[i] = new SelectItem(SHEET_PAGER_POSITION_KEYS[i], SHEET_PAGER_POSITION_KEYS[i]);
    }
    return items;
  }



  public boolean isSheetShowHeader() {
    return sheetShowHeader;
  }

  public void setSheetShowHeader(boolean sheetShowHeader) {
    this.sheetShowHeader = sheetShowHeader;
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
}
