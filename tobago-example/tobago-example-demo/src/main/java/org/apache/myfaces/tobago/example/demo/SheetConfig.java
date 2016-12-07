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

package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.layout.ShowPosition;
import org.apache.myfaces.tobago.model.Selectable;

import javax.faces.model.SelectItem;
import java.util.HashSet;
import java.util.Set;

public class SheetConfig {

  private boolean sheetShowHeader;
  private boolean showPagingAlways;
  private int sheetFirst;
  private int sheetRows;
  private int sheetDirectLinkCount;
  private ShowPosition sheetRowPagingPosition;
  private ShowPosition sheetDirectPagingPosition;
  private ShowPosition sheetPagePagingPosition;
  private SelectItem[] sheetDirectLinkCountItems;
  private SelectItem[] sheetPagingPositionItems;
  private SelectItem[] sheetSelectableItems;
  private Selectable selectable;
  private boolean showDirectLinksArrows;
  private boolean showPageRangeArrows;
  private boolean sheetConfigPopup;

  public SheetConfig() {
    sheetFirst = 0;
    sheetRows = 10;
    sheetDirectLinkCount = 5;
    sheetDirectLinkCountItems = createSheetItems(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
    sheetShowHeader = true;
    showPagingAlways = false;
    sheetRowPagingPosition = ShowPosition.left;
    sheetDirectPagingPosition = ShowPosition.center;
    sheetPagePagingPosition = ShowPosition.right;
    sheetPagingPositionItems = createSheetItems(ShowPosition.values());
    final Set<Selectable> selectableSupportedBySheet = new HashSet<Selectable>();
    for (Selectable selectable : Selectable.values()) {
      if (selectable.isSupportedBySheet()) {
        selectableSupportedBySheet.add(selectable);
      }
    }
    sheetSelectableItems = createSheetItems(selectableSupportedBySheet.toArray());
    selectable = Selectable.singleOrNone;
    showDirectLinksArrows = false;
    showPageRangeArrows = true;
  }

  public String configSheet() {
    sheetConfigPopup = true;
    return null;
  }

  private SelectItem[] createSheetItems(final Object[] values) {
    final SelectItem[] items = new SelectItem[values.length];

    for (int i = 0; i < values.length; i++) {
      items[i] = new SelectItem(values[i], values[i].toString());
    }
    return items;
  }


  public boolean isSheetShowHeader() {
    return sheetShowHeader;
  }

  public void setSheetShowHeader(final boolean sheetShowHeader) {
    this.sheetShowHeader = sheetShowHeader;
  }

  public boolean isShowPagingAlways() {
    return showPagingAlways;
  }

  public void setShowPagingAlways(final boolean showPagingAlways) {
    this.showPagingAlways = showPagingAlways;
  }

  public int getSheetFirst() {
    return sheetFirst;
  }

  public int getSheetRows() {
    return sheetRows;
  }

  public void setSheetRows(final int sheetRows) {
    this.sheetRows = sheetRows;
  }

  public int getSheetFirstValue() {
    final int value = getSheetFirst();
    return (value - 1) < 0 ? 0 : value - 1;
  }

  public void setSheetFirst(final int sheetFirst) {
    this.sheetFirst = sheetFirst;
  }

  public boolean isSheetConfigPopup() {
    return sheetConfigPopup;
  }

  public void setSheetConfigPopup(final boolean sheetConfigPopup) {
    this.sheetConfigPopup = sheetConfigPopup;
  }

  public int getSheetDirectLinkCount() {
    return sheetDirectLinkCount;
  }

  public void setSheetDirectLinkCount(final int sheetDirectLinkCount) {
    this.sheetDirectLinkCount = sheetDirectLinkCount;
  }

  public SelectItem[] getSheetDirectLinkCountItems() {
    return sheetDirectLinkCountItems;
  }

  public ShowPosition getSheetRowPagingPosition() {
    return sheetRowPagingPosition;
  }

  public void setSheetRowPagingPosition(final ShowPosition sheetRowPagingPosition) {
    this.sheetRowPagingPosition = sheetRowPagingPosition;
  }

  public ShowPosition getSheetDirectPagingPosition() {
    return sheetDirectPagingPosition;
  }

  public void setSheetDirectPagingPosition(final ShowPosition sheetDirectPagingPosition) {
    this.sheetDirectPagingPosition = sheetDirectPagingPosition;
  }

  public ShowPosition getSheetPagePagingPosition() {
    return sheetPagePagingPosition;
  }

  public void setSheetPagePagingPosition(final ShowPosition sheetPagePagingPosition) {
    this.sheetPagePagingPosition = sheetPagePagingPosition;
  }

  public SelectItem[] getSheetPagingPositionItems() {
    return sheetPagingPositionItems;
  }

  public SelectItem[] getSheetSelectableItems() {
    return sheetSelectableItems;
  }

  public void setSheetSelectableItems(final SelectItem[] sheetSelectableItems) {
    this.sheetSelectableItems = sheetSelectableItems;
  }

  public Selectable getSelectable() {
    return selectable;
  }

  public void setSelectable(final Selectable selectable) {
    this.selectable = selectable;
  }

  public boolean isShowDirectLinksArrows() {
    return showDirectLinksArrows;
  }

  public void setShowDirectLinksArrows(boolean showDirectLinksArrows) {
    this.showDirectLinksArrows = showDirectLinksArrows;
  }

  public boolean isShowPageRangeArrows() {
    return showPageRangeArrows;
  }

  public void setShowPageRangeArrows(boolean showPageRangeArrows) {
    this.showPageRangeArrows = showPageRangeArrows;
  }
}
