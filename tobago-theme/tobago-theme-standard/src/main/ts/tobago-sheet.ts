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

import {Page} from "./tobago-page";
import {Css} from "./tobago-css";
import {ClientBehaviors} from "./tobago-client-behaviors";
import {ColumnSelector} from "./tobago-column-selector";
import {Selectable} from "./tobago-selectable";

interface MousemoveData {
  columnIndex: number;
  originalClientX: number;
  originalHeaderColumnWidth: number;
  mousemoveListener: (event: MouseEvent) => boolean;
  mouseupListener: (event: MouseEvent) => boolean;
}

interface MousedownOnRowData {
  x: number;
  y: number;
}

export enum SelectMode {
  none,
  singleMode,
  ctrlMode_singleOrNone,
  ctrlMode_multi,
  shiftMode
}

export class Sheet extends HTMLElement {

  static readonly SCROLL_BAR_SIZE: number = Sheet.getScrollBarSize();
  private static readonly SUFFIX_LAZY_UPDATE: string = "::lazy-update";

  private columnSelector: ColumnSelector;

  mousemoveData: MousemoveData;
  mousedownOnRowData: MousedownOnRowData;

  lastCheckMillis: number;

  private lazyCheckInterval;
  private lazyActive: boolean;
  private sheetLoader: HTMLElement;

  constructor() {
    super();
  }

  private static getScrollBarSize(): number {
    const body = document.getElementsByTagName("body").item(0);

    const outer = document.createElement("div");
    outer.style.visibility = "hidden";
    outer.style.width = "100px";
    outer.style.overflow = "scroll";
    body.append(outer);

    const inner = document.createElement("div");
    inner.style.width = "100%";
    outer.append(inner);
    const widthWithScroll = inner.offsetWidth;

    body.removeChild(outer);

    return 100 - widthWithScroll;
  }

  private static getDummyRowTemplate(columns: number, rowIndex: number): string {
    return `<tr row-index="${rowIndex}" dummy="dummy">
<td colspan="${columns}"> </td>
</tr>`;
  }

  connectedCallback(): void {
    if (this.querySelector("thead input.tobago-selected[name='" + this.id + "::columnSelector']")) {
      this.columnSelector = new ColumnSelector(this);
    }

    this.initScrollbarFiller();
    if (this.dataset.tobagoLayout) {
      const resizeObserver = new ResizeObserver((entries) => this.fixColumnWidth());
      resizeObserver.observe(this);
    }

    // resize column: mouse events -------------------------------------------------------------------------------- //

    for (const resizeElement of this.querySelectorAll(":scope > header > table > thead > tr > th > .tobago-resize")) {
      resizeElement.addEventListener("click", function (): boolean {
        return false;
      });
      resizeElement.addEventListener("mousedown", this.mousedown.bind(this));
    }

    // scrolling -------------------------------------------------------------------------------------------------- //
    const sheetBody = this.body;

    // restore scroll position
    if (!this.lazy) {
      const scrollPosition = this.scrollPosition;
      sheetBody.scrollLeft = scrollPosition[0];
      sheetBody.scrollTop = scrollPosition[1];
    }

    this.syncScrolling();

    // scroll events
    sheetBody.addEventListener("scroll", this.scrollAction.bind(this));

    // add selection listeners ------------------------------------------------------------------------------------ //
    this.rowElements.forEach((row) => this.initSelectionListener(row));
    if (this.columnSelector && this.columnSelector.headerElement.type === "checkbox") {
      this.columnSelector.headerElement.addEventListener("click", this.initSelectAllCheckbox.bind(this));
    }
    this.addEventListener(ClientBehaviors.ROW_SELECTION_CHANGE, this.syncSelected.bind(this));
    this.syncSelected(null); //TOBAGO-2254

    // lazy load by scrolling ----------------------------------------------------------------- //
    if (this.lazy) {
      // prepare the sheet with some auto-created (empty) rows
      const tableBody = this.tableBody;
      const columns = tableBody.rows[0].cells.length;

      const firstRow: HTMLTableRowElement = tableBody.rows[0];
      for (let i = 0; i < this.getRowIndex(firstRow); i++) {
        firstRow.insertAdjacentHTML("beforebegin", Sheet.getDummyRowTemplate(columns, i));
      }
      const lastRow: HTMLTableRowElement = tableBody.rows[tableBody.rows.length - 1];
      for (let i = this.getRowIndex(lastRow) + 1; i < this.rowCount; i++) {
        tableBody.insertAdjacentHTML("beforeend", Sheet.getDummyRowTemplate(columns, i));
      }

      this.body.addEventListener("scroll", this.activateLazyCheckInterval.bind(this));

      const lazyScrollPosition = this.lazyScrollPosition;
      const firstVisibleRow = this.getRowElement(lazyScrollPosition[0]);
      if (firstVisibleRow) {
        this.body.scrollTop = firstVisibleRow.offsetTop + lazyScrollPosition[1];
        //in Firefox setting "scrollTop" triggers scroll event -> lazyCheck()
      }

      this.lazyCheck();
    }

    // init paging by pages ---------------------------------------------------------------------------------------- //

    /*
        for (const pagingText of this.querySelectorAll(".tobago-paging")) {

          console.warn("register ************** click on paging");
          pagingText.addEventListener("click", this.clickOnPaging.bind(this));

          const pagingInput = pagingText.querySelector("input");
          console.warn("register ************** blur on paging");
          pagingInput.addEventListener("blur", this.blurPaging.bind(this));

          console.warn("register ************** keydown on paging");
          pagingInput.addEventListener("keydown", function (event: KeyboardEvent): void {
            if (event.key === Key.ENTER) {
              event.stopPropagation();
              event.preventDefault();
              event.currentTarget.dispatchEvent(new Event("blur"));
            }
          });
        }
    */
  }

  // -------------------------------------------------------------------------------------- //

  /*
  When a scroll event occurs, activateLazyCheckInterval, which triggers lazyCheck() every 100ms till all rows are
  loaded.

  On initial load OR on lazyCheckInterval:
  -> Tobago.Sheet.lazyCheck()
    1. check, if the the lazy reload is currently active
      a) yes -> do nothing
      b) no  -> step 2.
    2. check, if there are data need to load (depends on scroll position and already loaded data)
      a) yes -> set lazy reload to active and make an AJAX request with Tobago.Sheet.reloadLazy()
      b) no  -> do nothing and stop lazyCheckInterval

     AJAX response -> 1. update the rows in the sheet from the response
                      2. go to the first part of this description
  */

  private activateLazyCheckInterval(event: Event): void {
    if (!this.lazyCheckInterval) {
      this.lazyCheckInterval = setInterval(() => this.lazyCheck(), 100);
    }
  }

  /**
   * Checks if a lazy update is required, because there are unloaded rows in the visible area.
   */
  lazyCheck(event?: Event): void {
    if (this.lazyActive) {
      // nothing to do, because there is an active AJAX running; check again later
      return;
    }

    const next: { from: number; to: number } = this.nextLazyLoadInterval();
    // console.info("next from=%o to=%o", next?.from, next?.to); // @DEV_ONLY
    if (next !== null) {
      this.lazyActive = true;
      const rootNode = this.getRootNode() as ShadowRoot | Document;
      const input = rootNode.getElementById(this.id + ":pageActionlazy") as HTMLInputElement;
      input.value = String(next.from + 1); //input.value rowIndex starts at 1 (not 0)
      console.debug(`reload sheet with action '${input.id}'`); // @DEV_ONLY

      faces.ajax.request(
          input.id,
          null,
          {
            params: {
              "jakarta.faces.behavior.event": "lazy",
              "tobago.sheet.lazyFirstRow": next.from,
              "tobago.sheet.lazyLastRow": next.to + 1 //to+1, because the to-row should also be loaded
            },
            execute: this.id,
            render: this.id,
            onevent: this.lazyResponse.bind(this),
            onerror: this.lazyError.bind(this)
          });
    } else {
      clearInterval(this.lazyCheckInterval);
      delete this.lazyCheckInterval;
    }
  }

  private nextLazyLoadInterval(): { from: number; to: number } {
    const firstVisibleRowIndex = this.firstVisibleRowIndex;
    const lastVisibleRowIndex = this.lastVisibleRowIndex;
    const firstVisibleRow = this.getRowElement(firstVisibleRowIndex);
    let from = this.getFirstDummyRowIndex(firstVisibleRow, lastVisibleRowIndex);
    let to = this.getLastDummyRowIndex(firstVisibleRow, lastVisibleRowIndex);

    const isNewArea = from === firstVisibleRowIndex && to === lastVisibleRowIndex;
    const visibleRowCount = lastVisibleRowIndex - firstVisibleRowIndex + 1;
    const remainingLazyRows = isNewArea ? this.lazyRows : Math.max(this.lazyRows - visibleRowCount, 0);
    const rangeStart = firstVisibleRowIndex - Math.floor(remainingLazyRows / 2);
    const rangeEnd = lastVisibleRowIndex + Math.ceil(remainingLazyRows / 2);

    const startElement = this.getRowElement(Math.max(rangeStart, 0));
    from = this.getFirstDummyRowIndex(startElement, rangeEnd);

    if (from >= 0) {
      const fromElement = this.getRowElement(from);
      to = this.getLastDummyRowIndex(fromElement, Math.max(from + (this.lazyRows - 1), rangeEnd));

      if (to - from < this.lazyRows - 1) {
        from = this.getLastDummyRowIndex(fromElement, Math.min(to - (this.lazyRows - 1), rangeStart));
      }
      return {from, to};
    } else {
      return null;
    }
  }

  private getFirstDummyRowIndex(row: HTMLTableRowElement, toRowIndex: number): number {
    if (row) {
      const rowIndex: number = this.getRowIndex(row);
      if (row.hasAttribute("dummy")) {
        return rowIndex;
      } else if (rowIndex < toRowIndex) {
        return this.getFirstDummyRowIndex(row.nextElementSibling as HTMLTableRowElement, toRowIndex);
      } else if (rowIndex > toRowIndex) {
        return this.getFirstDummyRowIndex(row.previousElementSibling as HTMLTableRowElement, toRowIndex);
      }
    }
    return -1;
  }

  private getLastDummyRowIndex(row: HTMLTableRowElement, toRowIndex: number): number {
    if (row) {
      const rowIndex: number = this.getRowIndex(row);
      if (row.hasAttribute("dummy")) {
        if (rowIndex < toRowIndex) {
          const next = this.getLastDummyRowIndex(row.nextElementSibling as HTMLTableRowElement, toRowIndex);
          if (next > -1) {
            return next;
          }
        } else if (rowIndex > toRowIndex) {
          const previous = this.getLastDummyRowIndex(row.previousElementSibling as HTMLTableRowElement, toRowIndex);
          if (previous > -1) {
            return previous;
          }
        }
        return rowIndex;
      }
    }
    return -1;
  }

  nextLazyLoad(): number {
    const rows = this.lazyRows;
    const firstVisibleRowIndex = this.firstVisibleRowIndex;
    const firstVisibleRow = this.getRowElement(firstVisibleRowIndex);

    const maxNextLazyLoad = firstVisibleRowIndex + rows - 1;
    const nextDummyRowIndex = this.getNextDummyRowIndex(firstVisibleRow.nextElementSibling, rows - 1);
    if (nextDummyRowIndex && nextDummyRowIndex <= maxNextLazyLoad) {
      if (firstVisibleRow.hasAttribute("dummy")) {
        return firstVisibleRowIndex;
      } else {
        return nextDummyRowIndex;
      }
    }

    const minNextLazyLoad = firstVisibleRowIndex - rows;
    const prevDummyRowIndex = this.getPreviousDummyRowIndex(firstVisibleRow, rows);
    if (prevDummyRowIndex && prevDummyRowIndex >= minNextLazyLoad) {
      const prevLazyLoad = prevDummyRowIndex - rows + 1;
      return prevLazyLoad >= 0 ? prevLazyLoad : 0;
    }

    return null;
  }

  private getNextDummyRowIndex(row: Element, iterationCount: number): number {
    if (row && iterationCount > 0) {
      if (row.hasAttribute("dummy")) {
        return Number(row.getAttribute("row-index"));
      } else if (row.classList.contains(Css.TOBAGO_COLUMN_PANEL)) {
        return this.getNextDummyRowIndex(row.nextElementSibling, iterationCount);
      } else {
        return this.getNextDummyRowIndex(row.nextElementSibling, --iterationCount);
      }
    } else {
      return null;
    }
  }

  private getPreviousDummyRowIndex(row: Element, iterationCount: number): number {
    if (row && iterationCount > 0) {
      if (row.hasAttribute("dummy")) {
        return Number(row.getAttribute("row-index"));
      } else if (row.classList.contains(Css.TOBAGO_COLUMN_PANEL)) {
        return this.getPreviousDummyRowIndex(row.previousElementSibling, iterationCount);
      } else {
        return this.getPreviousDummyRowIndex(row.previousElementSibling, --iterationCount);
      }
    } else {
      return null;
    }
  }

  lazyResponse(event: EventData): void {
    const updates: NodeListOf<Element> = event.responseXML?.querySelectorAll("update");
    if (updates && event.status === "complete") {
      for (const update of updates) {
        const id = update.getAttribute("id");
        if (this.id === id) { // is a Faces element id, but not a technical id from the framework
          console.debug(`[tobago-sheet][complete] Update after faces.ajax complete: #${id}`); // @DEV_ONLY

          update.id = update.id + Sheet.SUFFIX_LAZY_UPDATE; //hide from faces.js

          this.sheetLoader = document.createElement("div");
          this.sheetLoader.innerHTML = update.textContent;
        }
      }
    } else if (updates && event.status === "success") {
      for (const update of updates) {
        const id = update.getAttribute("id");
        if (this.id + Sheet.SUFFIX_LAZY_UPDATE === id) {
          console.debug(`[tobago-sheet][success] Update after faces.ajax complete: #${id}`); // @DEV_ONLY

          const newRows = this.sheetLoader.querySelectorAll<HTMLTableRowElement>(".tobago-body tbody>tr");
          for (const newRow of newRows) {
            if (newRow.hasAttribute("row-index")) {
              const rowIndex = Number(newRow.getAttribute("row-index"));
              const rowElement = this.getRowElement(rowIndex);
              const previousElement = rowElement.previousElementSibling;
              // first remove the old element and then add the new element
              // otherwise eventlisteners cannot be registered
              if (previousElement == null) {
                const parentElement = rowElement.parentElement;
                rowElement.remove();
                parentElement.insertAdjacentElement("afterbegin", newRow);
              } else {
                rowElement.remove();
                previousElement.insertAdjacentElement("afterend", newRow);
              }
            } else if (newRow.classList.contains(Css.TOBAGO_COLUMN_PANEL)) {
              const rowIndex = Number(newRow.getAttribute("name"));
              const columnPanel = this.tableBody.querySelector(`tr[name='${rowIndex}'].${Css.TOBAGO_COLUMN_PANEL}`);
              if (columnPanel) {
                const previousElement = columnPanel.previousElementSibling;
                columnPanel.remove();
                previousElement.insertAdjacentElement("afterend", newRow);
              } else {
                const rowElement = this.getRowElement(rowIndex);
                rowElement.insertAdjacentElement("afterend", newRow);
              }
            }

            this.initSelectionListener(newRow);
          }

          this.sheetLoader.remove();
        }
      }

      const lazyScrollPosition = this.lazyScrollPosition;
      const firstRow = this.getRowElement(lazyScrollPosition[0]);
      if (firstRow) {
        this.body.scrollTop = firstRow.offsetTop + lazyScrollPosition[1];
        //in Firefox setting "scrollTop" triggers scroll event -> lazyCheck()
      }

      this.lazyActive = false;
    }
  }

  lazyError(data: ErrorData): void {
    console.error(`Sheet lazy loading error:
Error Name: ${data.errorName}
Error errorMessage: ${data.errorMessage}
Response Code: ${data.responseCode}
Response Text: ${data.responseText}
Status: ${data.status}
Type: ${data.type}`);
  }

  private initScrollbarFiller(): void {
    // set width of the scrollbar filler
    if (this.scrollbarFiller) {
      /* Use min/maxWidth instead of width to force the browser to use the exact px value. Otherwise, it is possible
      that the browser use 14,785px instead of 15px. */
      this.scrollbarFiller.style.minWidth = Sheet.SCROLL_BAR_SIZE + "px";
      this.scrollbarFiller.style.maxWidth = Sheet.SCROLL_BAR_SIZE + "px";

      this.updateScrollbarFillerVisibility();

      // Initialize observer to show/hide scrollbar filler, depending on whether the sheet body has a scrollbar or not.
      const resizeObserver = new ResizeObserver((entries) => {
        for (const entry of entries) {
          if (entry.target === this.body) {
            this.updateScrollbarFillerVisibility();
          }
        }
      });
      resizeObserver.observe(this.body);
    }
  }

  private updateScrollbarFillerVisibility() {
    if (this.body.offsetWidth > this.body.clientWidth) {
      this.scrollbarFiller.classList.add(Css.TOBAGO_SHOW);
    } else {
      this.scrollbarFiller.classList.remove(Css.TOBAGO_SHOW);
    }
  }

  /**
   * Fix column width for columns with fr-value if the layout contains both fr-values and px-values.
   * The CSS function calc(100% - [number]px) doesn't work in table.
   */
  private fixColumnWidth(): void {
    const renderedColumnWidth: string[] = this.renderedColumnWidth;

    const hasPxAndFrValues = renderedColumnWidth.some(value => value.endsWith("px") || value.endsWith("fr"));
    if (hasPxAndFrValues) {
      const percent100InPx = parseFloat(getComputedStyle(this).width);
      if (percent100InPx > 0) {
        let frBaseInPx = percent100InPx;
        renderedColumnWidth.forEach((value) => {
          if (value.endsWith("px")) {
            frBaseInPx -= parseFloat(value);
          } else if (value.endsWith("%")) {
            frBaseInPx -= (percent100InPx * parseFloat(value) / 100);
          } else if (value === "auto") {
            const autoInPercent = 100 / (this.headerCols.length - 2);
            frBaseInPx -= (percent100InPx * autoInPercent / 100);
          }
        });

        const sumOfFr = renderedColumnWidth
            .filter(value => value.endsWith("fr"))
            .map(value => parseFloat(value))
            .reduce((previousValue, currentValue) => previousValue + currentValue, 0);

        renderedColumnWidth.forEach((value, index) => {
          if (value.endsWith("fr")) {
            // Use percent value instead of px value to prioritize the sheet width over column (percentage) width.
            const widthPercent = 100 * frBaseInPx / percent100InPx / sumOfFr * parseFloat(value) + "%";
            this.headerCols.item(index).style.width = widthPercent;
            this.bodyCols.item(index).style.width = widthPercent;
          }
        });
      }
    }
  }

  mousedown(event: MouseEvent): void {

    Page.page(this).dataset.SheetMousedownData = this.id;

    // begin resizing
    console.debug("down");
    this.headerCols.forEach((col, i) => {
      if (!col.classList.contains(Css.TOBAGO_ROW_FILLER) && !col.classList.contains(Css.TOBAGO_BEHAVIOR_CONTAINER)) {
        col.style.width = getComputedStyle(col).width;
      } else if (col.classList.contains(Css.TOBAGO_ROW_FILLER)) {
        col.style.width = "auto";
      }
      this.bodyCols.item(i).style.width = col.style.width;
    });

    const resizeElement = event.currentTarget as HTMLElement;
    const columnIndex = parseInt(resizeElement.dataset.tobagoColumnIndex);
    const headerColumn = this.headerCols.item(columnIndex);
    this.mousemoveData = {
      columnIndex: columnIndex,
      originalClientX: event.clientX,
      originalHeaderColumnWidth: parseInt(getComputedStyle(headerColumn).width),
      mousemoveListener: this.mousemove.bind(this),
      mouseupListener: this.mouseup.bind(this)
    };

    document.addEventListener("mousemove", this.mousemoveData.mousemoveListener);
    document.addEventListener("mouseup", this.mousemoveData.mouseupListener);
  }

  private mousemove(event: MouseEvent): void {
    let delta = event.clientX - this.mousemoveData.originalClientX;
    delta = -Math.min(-delta, this.mousemoveData.originalHeaderColumnWidth - 10);
    const columnWidth = this.mousemoveData.originalHeaderColumnWidth + delta;
    this.headerCols.item(this.mousemoveData.columnIndex).style.width = columnWidth + "px";
    this.bodyCols.item(this.mousemoveData.columnIndex).style.width = columnWidth + "px";
    if (window.getSelection) {
      window.getSelection().removeAllRanges();
    }
  }

  private mouseup(event: MouseEvent): void {
    console.debug("up");

    // switch off the mouse move listener
    document.removeEventListener("mousemove", this.mousemoveData.mousemoveListener);
    document.removeEventListener("mouseup", this.mousemoveData.mouseupListener);
    const renderedColWidths: number[] = [];

    this.headerCols.forEach((col, i) => {
      if (!col.classList.contains(Css.TOBAGO_ROW_FILLER) && !col.classList.contains(Css.TOBAGO_BEHAVIOR_CONTAINER)) {
        renderedColWidths[i] = parseInt(getComputedStyle(col).width);
      }
    });

    const oldWidth: number[] = this.hiddenInputWidthValue;
    const newWidth: number[] = [];

    let renderedColWidthsIndex = 0;
    this.hiddenInputRenderedValue.forEach((rendered, index) => {
      if (rendered) {
        newWidth[index] = renderedColWidths[renderedColWidthsIndex];
        renderedColWidthsIndex++;
      } else if (oldWidth[index]) {
        newWidth[index] = oldWidth[index];
      } else {
        newWidth[index] = -1;
      }
    });

    this.hiddenInputWidthValue = newWidth;
  }

  scrollAction(event: Event): void {
    this.syncScrolling();
    const sheetBody = event.currentTarget as HTMLElement;
    const scrollLeft = Math.round(sheetBody.scrollLeft);

    if (this.lazy) {
      const firstVisibleRowIndex = this.firstVisibleRowIndex;
      const firstVisibleRow = this.getRowElement(firstVisibleRowIndex);
      const firstVisibleRowScrollTop = sheetBody.scrollTop - firstVisibleRow.offsetTop;

      this.lazyScrollPosition = [firstVisibleRowIndex, Math.round(firstVisibleRowScrollTop), scrollLeft];
    } else {
      this.scrollPosition = [scrollLeft, Math.round(sheetBody.scrollTop)];
    }
  }

  private initSelectionListener(row: HTMLTableRowElement): void {
    let clickElement: HTMLTableRowElement | HTMLTableCellElement;

    if (this.columnSelector && this.columnSelector.disabled) {
      return;
    } else if (this.columnSelector && this.columnSelector.rowElement(row)
        && this.columnSelector.rowElement(row).disabled) {
      return;
    } else if ([Selectable.single, Selectable.singleOrNone, Selectable.multi].includes(this.selectable)) {
      clickElement = row;
    } else if (this.selectable === Selectable.none && this.columnSelector
        && [Selectable.single, Selectable.singleOrNone, Selectable.multi].includes(this.columnSelector.selectable)
        && !row.classList.contains(Css.TOBAGO_COLUMN_PANEL)) {
      clickElement = row.querySelector("td:first-child");
      clickElement.addEventListener("click", (event) => event.stopPropagation()); //TOBAGO-2276
    } else if (this.columnSelector && this.columnSelector.selectable === Selectable.none) {
      const rowElement = this.columnSelector.rowElement(row);
      if (rowElement) {
        rowElement.addEventListener("click", (event) => event.preventDefault());
      }
    }

    if (clickElement) {
      clickElement.addEventListener("mousedown", (event: MouseEvent) => {
        this.mousedownOnRowData = {
          x: event.clientX,
          y: event.clientY
        };
      });

      clickElement.addEventListener("click", (event: MouseEvent) => {
        const row: HTMLTableRowElement = (event.currentTarget as HTMLElement).closest("tr");

        if (this.mousedownOnRowData) { // integration test: mousedownOnRowData may be 'null'
          if (Math.abs(this.mousedownOnRowData.x - event.clientX)
              + Math.abs(this.mousedownOnRowData.y - event.clientY) > 5) {
            // The user has moved the mouse. We assume, the user want to select some text inside the sheet,
            // so we don't select the row.
            return;
          }
        }

        const selectable = this.columnSelector ? this.columnSelector.selectable : this.selectable;
        const ctrlPressed = event.ctrlKey || event.metaKey;
        const shiftPressed = event.shiftKey && this.lastClickedRowIndex > -1;

        /* eslint-disable max-len */
        // @formatter:off
        const selectMode: SelectMode
        = !this.columnSelector && selectable === Selectable.single                                 ? SelectMode.singleMode
        : !this.columnSelector && selectable === Selectable.singleOrNone && !ctrlPressed           ? SelectMode.singleMode
        : !this.columnSelector && selectable === Selectable.singleOrNone && ctrlPressed            ? SelectMode.ctrlMode_singleOrNone
        : !this.columnSelector && selectable === Selectable.multi && !ctrlPressed && !shiftPressed ? SelectMode.singleMode
        : !this.columnSelector && selectable === Selectable.multi && ctrlPressed                   ? SelectMode.ctrlMode_multi
        : !this.columnSelector && selectable === Selectable.multi && shiftPressed                  ? SelectMode.shiftMode
        : this.columnSelector  && selectable === Selectable.single                                 ? SelectMode.singleMode
        : this.columnSelector  && selectable === Selectable.singleOrNone                           ? SelectMode.ctrlMode_singleOrNone
        : this.columnSelector  && selectable === Selectable.multi && !shiftPressed                 ? SelectMode.ctrlMode_multi
        : this.columnSelector  && selectable === Selectable.multi && shiftPressed                  ? SelectMode.shiftMode
        : SelectMode.none;
        // @formatter:on
        /* eslint-enable max-len */

        const selected = this.selected;
        const rowIndex = Number(row.classList.contains(Css.TOBAGO_COLUMN_PANEL)
            ? row.getAttribute("name") : row.getAttribute("row-index"));

        switch (selectMode) {
          case SelectMode.singleMode:
            selected.clear();
            selected.add(rowIndex);
            break;
          case SelectMode.ctrlMode_singleOrNone:
            if (selected.has(rowIndex)) {
              selected.delete(rowIndex);
            } else {
              selected.clear();
              selected.add(rowIndex);
            }
            break;
          case SelectMode.ctrlMode_multi:
            if (selected.has(rowIndex)) {
              selected.delete(rowIndex);
            } else {
              selected.add(rowIndex);
            }
            break;
          case SelectMode.shiftMode:
            for (let i = Math.min(rowIndex, this.lastClickedRowIndex);
                 i <= Math.max(rowIndex, this.lastClickedRowIndex); i++) {
              selected.add(i);
            }
            break;
        }

        this.lastClickedRowIndex = rowIndex;
        this.selected = selected;
      });
    }
  }

  private initSelectAllCheckbox(event: MouseEvent): void {
    const selected = this.selected;

    if (this.lazy || this.rows === 0) {
      if (selected.size === this.rowCount) {
        selected.clear();
      } else {
        for (let i = 0; i < this.rowCount; i++) {
          selected.add(i);
        }
      }
    } else {
      const rowIndexes: number[] = [];
      const tableRows = this.rowElements;
      tableRows.forEach((rowElement) => {
        if (rowElement.hasAttribute("row-index")) {
          if (!this.columnSelector.rowElement(rowElement).disabled) {
            rowIndexes.push(Number(rowElement.getAttribute("row-index")));
          }
        }
      });
      let everyRowAlreadySelected = true;
      rowIndexes.forEach((rowIndex, index, array) => {
        if (!selected.has(rowIndex)) {
          everyRowAlreadySelected = false;
          selected.add(rowIndex);
        }
      });

      if (everyRowAlreadySelected) {
        rowIndexes.forEach((rowIndex, index, array) => {
          selected.delete(rowIndex);
        });
      }
    }

    this.selected = selected;
  }

  private syncSelected(event: CustomEvent) {
    const selected = event ? event.detail.selection : this.selected;

    const tableRows = this.rowElements;
    tableRows.forEach((rowElement) => {
      const isColumnPanel = rowElement.classList.contains(Css.TOBAGO_COLUMN_PANEL);
      const rowIndex = Number(isColumnPanel ? rowElement.getAttribute("name") : rowElement.getAttribute("row-index"));
      const inputElement = !isColumnPanel ? this.columnSelector?.rowElement(rowElement) : null;

      if (selected.has(rowIndex)) {
        rowElement.classList.add(Css.TOBAGO_SELECTED);
        rowElement.classList.add(Css.TABLE_INFO);
        if (inputElement) {
          inputElement.checked = true;
        }
      } else {
        rowElement.classList.remove(Css.TOBAGO_SELECTED);
        rowElement.classList.remove(Css.TABLE_INFO);
        if (inputElement) {
          inputElement.checked = false;
        }
      }
    });

    if (this.columnSelector && this.columnSelector.headerElement.type === "checkbox") {
      if (this.lazy || this.rows === 0) {
        this.columnSelector.headerElement.checked = selected.size === this.rowCount;
      } else {
        let everyRowSelected = true;
        for (let i = this.first; i < this.first + this.rows; i++) {
          if (!selected.has(i)) {
            const factor = tableRows.length > this.rows ? 2 : 1; // columnPanel
            const element = tableRows.item((i - this.first) * factor);
            const inputElement = this.columnSelector.rowElement(element);
            if (inputElement == null || !inputElement.disabled) {
              everyRowSelected = false;
            }
          }
        }
        this.columnSelector.headerElement.checked = everyRowSelected;
      }
    }
  }

  /*
    clickOnPaging(event: MouseEvent): void {
      const element = event.currentTarget as HTMLElement;

      console.warn("execute  ************** click on paging");
      const output: HTMLElement = element.querySelector("span");
      output.style.display = "none";

      const input: HTMLInputElement = element.querySelector("input");
      input.style.display = "initial";
      input.focus();
      input.select();
    }
  */

  /*
    blurPaging(event: FocusEvent): void {
      console.warn("execute  ************** blur on paging");
      const input = event.currentTarget as HTMLInputElement;
      const output: HTMLElement = input.parentElement.querySelector("span");
      const number = Number.parseInt(input.value); // sanitizing
      if (number > 0 && number.toString() !== output.innerHTML) {
        console.debug("Reloading sheet '%s' old value='%s' new value='%s'", this.id, output.innerHTML, number);
        output.innerHTML = number.toString();
        faces.ajax.request(
            input.id,
            null,
            {
              params: {
                "jakarta.faces.behavior.event": "reload"
              },
              execute: this.id,
              render: this.id
            });
      } else {
        console.info("no update needed");
        input.value = output.innerHTML;
        input.style.display = "none";
        output.style.display = "initial";
      }
    }
  */

  syncScrolling(): void {
    // sync scrolling of body to header
    const header = this.header;
    if (header) {
      header.scrollLeft = this.body.scrollLeft;
    }
  }

  private getRowElement(index: number): HTMLTableRowElement {
    return this.tableBody.querySelector<HTMLTableRowElement>(`tr[row-index='${index}']`);
  }

  private getRowIndex(row: HTMLTableRowElement): number {
    if (row.hasAttribute("row-index")) {
      return Number(row.getAttribute("row-index"));
    } else if (row.classList.contains(Css.TOBAGO_COLUMN_PANEL)) {
      return Number(row.getAttribute("name"));
    }
    return null;
  }

  public get selectable(): Selectable {
    return Selectable[this.dataset.tobagoSelectionMode];
  }

  get hiddenInputWidthValue(): number[] {
    return JSON.parse(this.hiddenInputWidth.value);
  }

  set hiddenInputWidthValue(value: number[]) {
    this.hiddenInputWidth.value = JSON.stringify(value);
  }

  private get hiddenInputWidth(): HTMLInputElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.querySelector("input[id='" + this.id + "::widths']");
  }

  get hiddenInputRenderedValue(): boolean[] {
    return JSON.parse(this.hiddenInputRendered.value);
  }

  set hiddenInputRenderedValue(value: boolean[]) {
    this.hiddenInputRendered.value = JSON.stringify(value);
  }

  private get hiddenInputRendered(): HTMLInputElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.querySelector("input[id='" + this.id + "::rendered']");
  }

  /**
   * Return the width of all rendered col tags as a string array. If the user has set the width manually, the exact
   * pixel value is used, otherwise the layout value.
   */
  private get renderedColumnWidth(): string[] {
    const renderedColumnWidth: string[] = [];
    const layoutValues: any[] = JSON.parse(this.dataset.tobagoLayout).columns;
    this.hiddenInputRenderedValue.forEach((rendered, i) => {
      if (rendered) {
        let pxValue = -1;
        if (this.hiddenInputWidthValue.length > i) {
          pxValue = this.hiddenInputWidthValue[i];
        }

        if (pxValue > -1) {
          renderedColumnWidth.push(pxValue + "px");
        } else {
          const layoutValue = layoutValues[i % layoutValues.length];
          if (typeof layoutValue === "number") {
            renderedColumnWidth.push(layoutValue + "fr");
          } else if (typeof layoutValue === "object" && layoutValue.measure !== undefined) {
            renderedColumnWidth.push(layoutValue.measure);
          } else {
            renderedColumnWidth.push(layoutValue); //auto
          }
        }
      }
    });
    return renderedColumnWidth;
  }

  get scrollPosition(): number[] {
    return JSON.parse(this.hiddenInputScrollPosition.value);
  }

  set scrollPosition(value: number[]) {
    this.hiddenInputScrollPosition.value = JSON.stringify(value);
  }

  private get hiddenInputScrollPosition(): HTMLInputElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.querySelector("input[id='" + this.id + "::scrollPosition']");
  }

  get selected(): Set<number> {
    return new Set<number>(JSON.parse(this.hiddenInputSelected.value));
  }

  set selected(value: Set<number>) {
    const oldSelectedSet = this.selected;
    this.hiddenInputSelected.value = JSON.stringify(Array.from(value));

    const fireSelectionChangeEvent = oldSelectedSet.size !== value.size
        || ![...oldSelectedSet].every((x) => value.has(x));
    if (fireSelectionChangeEvent) {
      this.dispatchEvent(new CustomEvent(ClientBehaviors.ROW_SELECTION_CHANGE, {
        detail: {
          selection: value,
          rowsOnPage: this.rowsOnPage,
          rowCount: this.rowCount
        },
      }));
    }
  }

  private get hiddenInputSelected(): HTMLInputElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.querySelector("input[id='" + this.id + "::selected']");
  }

  get hiddenExpanded(): HTMLInputElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.querySelector("input[id='" + this.id + "::expanded']");
  }

  get lastClickedRowIndex(): number {
    return Number(this.dataset.tobagoLastClickedRowIndex);
  }

  set lastClickedRowIndex(value: number) {
    this.dataset.tobagoLastClickedRowIndex = String(value);
  }

  get lazy(): boolean {
    return this.hasAttribute("lazy");
  }

  set lazy(update: boolean) {
    if (update) {
      this.setAttribute("lazy", "");
    } else {
      this.removeAttribute("lazy");
    }
  }

  get lazyScrollPosition(): number[] {
    return JSON.parse(this.hiddenInputLazyScrollPosition.value);
  }

  set lazyScrollPosition(value: number[]) {
    this.hiddenInputLazyScrollPosition.value = JSON.stringify(value);
  }

  private get hiddenInputLazyScrollPosition(): HTMLInputElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.querySelector("input[id='" + this.id + "::lazyScrollPosition']");
  }

  get lazyRows(): number {
    return parseInt(this.getAttribute("lazy-rows"));
  }

  get first(): number {
    return parseInt(this.dataset.tobagoFirst);
  }

  get rows(): number {
    return parseInt(this.getAttribute("rows"));
  }

  get rowCount(): number {
    return parseInt(this.getAttribute("row-count"));
  }

  get rowsOnPage(): number {
    if (this.lazy || this.rows === 0 || this.rows >= this.rowCount) {
      return this.rowCount;
    } else {
      return Math.min(this.rowCount - this.first, this.rows);
    }
  }

  get header(): HTMLElement {
    return this.querySelector(":scope > header");
  }

  get headerCols(): NodeListOf<HTMLElement> {
    return this.querySelectorAll(":scope > header > table > colgroup > col");
  }

  get scrollbarFiller(): HTMLTableElement {
    return this.header?.querySelector(":scope > .tobago-scrollbar-filler");
  }

  get body(): HTMLElement {
    return this.querySelector(":scope > .tobago-body");
  }

  get bodyTable(): HTMLElement {
    return this.querySelector(":scope > .tobago-body > table");
  }

  get bodyCols(): NodeListOf<HTMLElement> {
    return this.bodyTable.querySelectorAll(":scope > colgroup > col");
  }

  get tableBody(): HTMLTableSectionElement {
    return this.bodyTable.querySelector(":scope > tbody");
  }

  get rowElements(): NodeListOf<HTMLTableRowElement> {
    return this.tableBody.querySelectorAll(":scope > tr");
  }

  get firstVisibleRowIndex(): number {
    const rowElements = this.tableBody.rows;
    let min = 0;
    let max = rowElements.length;
    let index: number;
    while (min < max) {
      index = Math.round((max - min) / 2) + min;
      if (rowElements[index] === undefined
          || rowElements[index].offsetTop > this.body.scrollTop) {
        index--;
        max = index;
      } else {
        min = index;
      }
    }

    return this.getRowIndex(rowElements[index]);
  }

  get lastVisibleRowIndex(): number {
    const rowElements = this.tableBody.rows;
    for (let i = this.firstVisibleRowIndex; i < rowElements.length; i++) {
      if (rowElements[i].offsetTop > (this.body.scrollTop + this.body.offsetHeight)) {
        const index = Math.max(0, i - 1);
        return this.getRowIndex(rowElements[index]);
      }
    }

    return this.getRowIndex(rowElements[rowElements.length - 1]);
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-sheet") == null) {
    window.customElements.define("tobago-sheet", Sheet);
  }
});
