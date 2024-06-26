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
import {Key} from "./tobago-key";
import {Css} from "./tobago-css";
import {ClientBehaviors} from "./tobago-client-behaviors";

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

export class Sheet extends HTMLElement {

  static readonly SCROLL_BAR_SIZE: number = Sheet.getScrollBarSize();
  private static readonly SUFFIX_LAZY_UPDATE: string = "::lazy-update";

  mousemoveData: MousemoveData;
  mousedownOnRowData: MousedownOnRowData;

  lastCheckMillis: number;

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

  private static isInputElement(element: HTMLElement): boolean {
    return ["INPUT", "TEXTAREA", "SELECT", "A", "BUTTON"].indexOf(element.tagName) > -1;
  }

  private static getDummyRowTemplate(columns: number, rowIndex: number): string {
    return `<tr row-index="${rowIndex}" dummy="dummy">
<td colspan="${columns}"> </td>
</tr>`;
  }

  connectedCallback(): void {
    // synchronize column widths ----------------------------------------------------------------------------------- //

    // basic idea: there are two possible sources for the sizes:
    // 1. the columns attribute of <tc:sheet> like {"columns":[1.0,1.0,1.0]}, held by data attribute "tobago-layout"
    // 2. the hidden field which may contain a value like ",300,200,100,"
    //
    // The 1st source usually is the default set by the developer.
    // The 2nd source usually is the value set by the user manipulating the column widths.
    //
    // So, if the 2nd is set, we use it, if not set, we use the 1st source.

    const columnWidths = this.loadColumnWidths();
    console.info("columnWidths: %s", JSON.stringify(columnWidths));
    if (columnWidths && columnWidths.length === 0) { // active, but empty
      // otherwise use the layout definition
      let tokens: any[] = JSON.parse(this.dataset.tobagoLayout).columns;
      const columnRendered = this.isColumnRendered();

      const headerCols = this.getHeaderCols();
      const bodyTable = this.getBodyTable();
      const bodyCols = this.getBodyCols();
      const borderLeftWidth
          = Number(getComputedStyle(bodyTable.querySelector("td:first-child")).borderLeftWidth.slice(0, -2));
      const borderRightWidth
          = Number(getComputedStyle(bodyTable.querySelector("td:last-child")).borderRightWidth.slice(0, -2));
      const tableWidth = bodyTable.offsetWidth - (borderLeftWidth + borderRightWidth) / 2;

      console.assert(headerCols.length - 1 === bodyCols.length,
          "header and body column number doesn't match: %d != %d ", headerCols.length - 1, bodyCols.length);

      while (tokens.length < headerCols.length - 2) {
        tokens = [...tokens, ...tokens];
      }
      tokens = tokens.slice(0, headerCols.length - 2);

      let sumRelative = 0; // tbd: is this needed?
      let widthRelative = tableWidth;
      let r = 0;
      for (let i = 0; i < tokens.length; i++) {
        if (columnRendered[i]) {
          if (typeof tokens[i] === "number") {
            sumRelative += tokens[i];
          } else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
            const intValue = parseInt(tokens[i].measure);
            if (tokens[i].measure.lastIndexOf("px") > 0) {
              widthRelative -= intValue;
            } else if (tokens[i].measure.lastIndexOf("%") > 0) {
              widthRelative -= tableWidth * intValue / 100;
            }
          } else if (tokens[i] === "auto") {
            const value = headerCols.item(r).offsetWidth;
            widthRelative -= value;
            tokens[i] = {measure: `${value}px`}; // converting "auto" to a specific value
          } else {
            console.debug("(layout columns a) auto? token[i]='%s' i=%i", tokens[i], i);
          }
        }
      }
      if (widthRelative < 0) {
        widthRelative = 0;
      }

      r = 0;
      for (let i = 0; i < tokens.length; i++) {
        let colWidth = 0;
        if (columnRendered[i]) {
          if (typeof tokens[i] === "number") {
            colWidth = tokens[i] * widthRelative / sumRelative;
          } else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
            const intValue = parseInt(tokens[i].measure);
            if (tokens[i].measure.lastIndexOf("px") > 0) {
              colWidth = intValue;
            } else if (tokens[i].measure.lastIndexOf("%") > 0) {
              colWidth = tableWidth * intValue / 100;
            }
          } else {
            console.debug("(layout columns b) auto? token[i]='%s' i=%i", tokens[i], i);
          }
          if (colWidth > 0) { // because tokens[i] == "auto"
            headerCols.item(r).setAttribute("width", String(colWidth));
            bodyCols.item(r).setAttribute("width", String(colWidth));
          }
          r++;
        }
      }
    }
    this.addHeaderFillerWidth();

    // resize column: mouse events -------------------------------------------------------------------------------- //

    for (const resizeElement of this.querySelectorAll(".tobago-resize")) {
      resizeElement.addEventListener("click", function (): boolean {
        return false;
      });
      resizeElement.addEventListener("mousedown", this.mousedown.bind(this));
    }

    // scrolling -------------------------------------------------------------------------------------------------- //
    const sheetBody = this.getBody();

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
    let selectionMode = this.dataset.tobagoSelectionMode;
    if (selectionMode !== "none") {
      for (const row of this.getRowElements()) {
        this.initTableRowForSelection(row, selectionMode);
      }
    } else {
      selectionMode = this.getSelectorAllCheckbox()?.dataset.tobagoSelectionMode;
      if (selectionMode) {
        const cells: NodeListOf<HTMLTableCellElement> = this.getBodyTable().querySelectorAll("tbody>tr>td:first-child");
        for (const cell of cells) {
          this.initTableCellForSelection(cell, selectionMode);
        }
      }
    }

    if (selectionMode === "multi") {
      const selectedSet = new Set<number>(JSON.parse(this.getHiddenSelected().value));
      this.calculateSelectorAllChecked(selectedSet);
    }

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

      this.sheetBody.addEventListener("scroll", this.lazyCheck.bind(this));

      const lazyScrollPosition = this.lazyScrollPosition;
      const firstVisibleRow
          = this.tableBody.querySelector<HTMLTableRowElement>(`tr[row-index='${lazyScrollPosition[0]}']`);
      this.sheetBody.scrollTop
          = firstVisibleRow.offsetTop + lazyScrollPosition[1]; //triggers scroll event -> lazyCheck()
    }

    // ---------------------------------------------------------------------------------------- //

    for (const checkbox of this.querySelectorAll("thead .tobago-selected")) {
      checkbox.addEventListener("click", this.clickOnCheckboxForAll.bind(this));
    }

    // init paging by pages ---------------------------------------------------------------------------------------- //

    for (const pagingText of this.querySelectorAll(".tobago-paging")) {

      pagingText.addEventListener("click", this.clickOnPaging.bind(this));

      const pagingInput = pagingText.querySelector("input");
      pagingInput.addEventListener("blur", this.blurPaging.bind(this));

      pagingInput.addEventListener("keydown", function (event: KeyboardEvent): void {
        if (event.key === Key.ENTER) {
          event.stopPropagation();
          event.preventDefault();
          event.currentTarget.dispatchEvent(new Event("blur"));
        }
      });
    }
  }

  private getSelectionMode() {
    let selectionMode = this.dataset.tobagoSelectionMode;
    if (selectionMode === "none") {
      const selectorAllCheckbox = this.getSelectorAllCheckbox();
      selectionMode = selectorAllCheckbox?.dataset.tobagoSelectionMode;
    }
    return selectionMode;
  }

  private initTableCellForSelection(cell: HTMLTableCellElement, columnSelectionMode: string) {
    if (columnSelectionMode === "single" || columnSelectionMode === "singleOrNone" || columnSelectionMode === "multi") {
      cell.addEventListener("mousedown", this.mousedownOnRow.bind(this));
      cell.addEventListener("click", this.clickOnColumnSelector.bind(this));
    }
    const checkbox = cell.querySelector("input.tobago-selected");
    checkbox?.addEventListener("click", (event) => {
      event.preventDefault();
    });
  }

  private initTableRowForSelection(row: HTMLTableRowElement, sheetSelectionMode: string): void {
    if (sheetSelectionMode === "single" || sheetSelectionMode === "singleOrNone" || sheetSelectionMode === "multi") {
      row.addEventListener("mousedown", this.mousedownOnRow.bind(this));
      row.addEventListener("click", this.clickOnRow.bind(this));
    }
    const checkbox = row.querySelector("td > input.tobago-selected");
    checkbox?.addEventListener("click", (event) => {
      event.preventDefault();
    });
  }

  // attribute getter + setter ---------------------------------------------------------- //
  get scrollPosition(): number[] {
    return JSON.parse(this.hiddenInputScrollPosition.value);
  }

  set scrollPosition(value: number[]) {
    this.hiddenInputScrollPosition.value = JSON.stringify(value);
  }

  private get hiddenInputScrollPosition(): HTMLInputElement {
    return this.querySelector("input[id$='::scrollPosition']");
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
    return this.querySelector("input[id$='::lazyScrollPosition']");
  }

  get lazyUpdate(): boolean {
    return this.hasAttribute("lazy-update");
  }

  get lazyRows(): number {
    return parseInt(this.getAttribute("lazy-rows"));
  }

  get rows(): number {
    return parseInt(this.getAttribute("rows"));
  }

  get rowCount(): number {
    return parseInt(this.getAttribute("row-count"));
  }

  get sheetBody(): HTMLDivElement {
    return this.querySelector(".tobago-body");
  }

  get tableBody(): HTMLTableSectionElement {
    return this.querySelector(".tobago-body tbody");
  }

  get firstVisibleRowIndex(): number {
    const rowElements = this.tableBody.rows;
    let min = 0;
    let max = rowElements.length;
    let index: number;
    while (min < max) {
      index = Math.round((max - min) / 2) + min;
      if (rowElements[index] === undefined
          || rowElements[index].offsetTop > this.sheetBody.scrollTop) {
        index--;
        max = index;
      } else {
        min = index;
      }
    }

    return this.getRowIndex(rowElements[index]);
  }

  // -------------------------------------------------------------------------------------- //

  /*
    when an event occurs (initial load OR scroll event OR AJAX response)

    then -> Tobago.Sheet.lazyCheck()
            1. check, if the lazy reload is currently active
               a) yes -> do nothing and exit
               b) no  -> step 2.
            2. check, if there are data need to load (depends on scroll position and already loaded data)
               a) yes -> set lazy reload to active and make an AJAX request with Tobago.Sheet.reloadLazy()
               b) no  -> do nothing and exit

     AJAX response -> 1. update the rows in the sheet from the response
                      2. go to the first part of this description
  */

  /**
   * Checks if a lazy update is required, because there are unloaded rows in the visible area.
   */
  lazyCheck(event?: Event): void {
    if (this.lazyActive) {
      // nothing to do, because there is an active AJAX running
      return;
    }

    if (this.lastCheckMillis && Date.now() - this.lastCheckMillis < 100) {
      // do nothing, because the last call was just a moment ago
      return;
    }

    this.lastCheckMillis = Date.now();
    const next = this.nextLazyLoad();
    // console.info("next %o", next); // @DEV_ONLY
    if (next !== null) {
      this.lazyActive = true;
      const rootNode = this.getRootNode() as ShadowRoot | Document;
      const input = rootNode.getElementById(this.id + ":pageActionlazy") as HTMLInputElement;
      input.value = String(next + 1); //input.value rowIndex starts at 1 (not 0)
      console.debug(`reload sheet with action '${input.id}'`); // @DEV_ONLY

      faces.ajax.request(
          input.id,
          null,
          {
            "jakarta.faces.behavior.event": "lazy",
            "tobago.sheet.lazyFirstRow": next,
            execute: this.id,
            render: this.id,
            onevent: this.lazyResponse.bind(this),
            onerror: this.lazyError.bind(this)
          });
    }
  }

  nextLazyLoad(): number {
    const rows = this.rows > 0 ? this.rows : this.lazyRows;
    const firstVisibleRowIndex = this.firstVisibleRowIndex;
    const firstVisibleRow = this.tableBody.querySelector(`tr[row-index='${firstVisibleRowIndex}']`);

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

          const tbody = this.querySelector(".tobago-body tbody");
          const newRows = this.sheetLoader.querySelectorAll<HTMLTableRowElement>(".tobago-body tbody>tr");
          const sheetSelectionMode = this.dataset.tobagoSelectionMode;
          const columnSelectorSelectionMode = this.getSelectorAllCheckbox()?.dataset.tobagoSelectionMode;
          for (const newRow of newRows) {
            if (newRow.hasAttribute("row-index")) {
              const rowIndex = Number(newRow.getAttribute("row-index"));
              const row = tbody.querySelector(`tr[row-index='${rowIndex}']`);
              row.insertAdjacentElement("afterend", newRow);
              row.remove();
              if (sheetSelectionMode !== "none") {
                this.initTableRowForSelection(newRow, sheetSelectionMode);
              } else if (columnSelectorSelectionMode) {
                this.initTableCellForSelection(row.querySelector("td:first-child"), columnSelectorSelectionMode);
              }
            } else if (newRow.classList.contains(Css.TOBAGO_COLUMN_PANEL)) {
              const rowIndex = Number(newRow.getAttribute("name"));
              const columnPanel = tbody.querySelector(`tr[name='${rowIndex}'].${Css.TOBAGO_COLUMN_PANEL}`);
              if (columnPanel) {
                columnPanel.insertAdjacentElement("afterend", newRow);
                columnPanel.remove();
              } else {
                const row = tbody.querySelector(`tr[row-index='${rowIndex}']`);
                row.insertAdjacentElement("afterend", newRow);
              }
            }
          }

          this.sheetLoader.remove();
        }
      }

      const lazyScrollPosition = this.lazyScrollPosition;
      const firstRow
          = this.tableBody.querySelector<HTMLTableRowElement>(`tr[row-index='${lazyScrollPosition[0]}']`);
      this.sheetBody.scrollTop = firstRow.offsetTop + lazyScrollPosition[1];

      this.lazyActive = false;
    }
  }

  private getRowIndex(row: HTMLTableRowElement): number {
    if (row.hasAttribute("row-index")) {
      return Number(row.getAttribute("row-index"));
    } else if (row.classList.contains(Css.TOBAGO_COLUMN_PANEL)) {
      return Number(row.getAttribute("name"));
    }
    return null;
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

  loadColumnWidths(): number[] {
    const hidden = document.getElementById(this.id + "::widths");
    if (hidden) {
      return JSON.parse(hidden.getAttribute("value"));
    } else {
      return undefined;
    }
  }

  saveColumnWidths(widths: number[]): void {
    const hidden = document.getElementById(this.id + "::widths");
    if (hidden) {
      hidden.setAttribute("value", JSON.stringify(widths));
    } else {
      console.warn("ignored, should not be called, id='%s'", this.id);
    }
  }

  isColumnRendered(): boolean[] {
    const hidden = document.getElementById(this.id + "::rendered");
    return JSON.parse(hidden.getAttribute("value")) as boolean[];
  }

  addHeaderFillerWidth(): void {
    const last = document.getElementById(this.id).querySelector("tobago-sheet header table col:last-child");
    if (last) {
      last.setAttribute("width", String(Sheet.SCROLL_BAR_SIZE));
    }
  }

  mousedown(event: MouseEvent): void {

    Page.page(this).dataset.SheetMousedownData = this.id;

    // begin resizing
    console.debug("down");

    const resizeElement = event.currentTarget as HTMLElement;
    const columnIndex = parseInt(resizeElement.dataset.tobagoColumnIndex);
    const headerColumn = this.getHeaderCols().item(columnIndex);
    this.mousemoveData = {
      columnIndex: columnIndex,
      originalClientX: event.clientX,
      originalHeaderColumnWidth: parseInt(headerColumn.getAttribute("width")),
      mousemoveListener: this.mousemove.bind(this),
      mouseupListener: this.mouseup.bind(this)
    };

    document.addEventListener("mousemove", this.mousemoveData.mousemoveListener);
    document.addEventListener("mouseup", this.mousemoveData.mouseupListener);
  }

  mousemove(event: MouseEvent): boolean {
    console.debug("move");
    let delta = event.clientX - this.mousemoveData.originalClientX;
    delta = -Math.min(-delta, this.mousemoveData.originalHeaderColumnWidth - 10);
    const columnWidth = this.mousemoveData.originalHeaderColumnWidth + delta;
    this.getHeaderCols().item(this.mousemoveData.columnIndex).setAttribute("width", String(columnWidth));
    this.getBodyCols().item(this.mousemoveData.columnIndex).setAttribute("width", String(columnWidth));
    if (window.getSelection) {
      window.getSelection().removeAllRanges();
    }
    return false;
  }

  mouseup(event: MouseEvent): boolean {
    console.debug("up");

    // switch off the mouse move listener
    document.removeEventListener("mousemove", this.mousemoveData.mousemoveListener);
    document.removeEventListener("mouseup", this.mousemoveData.mouseupListener);
    // copy the width values from the header to the body, (and build a list of it)
    const tokens: any[] = JSON.parse(this.dataset.tobagoLayout).columns;
    const columnRendered = this.isColumnRendered();
    const columnWidths = this.loadColumnWidths();

    const bodyTable = this.getBodyTable();
    const headerCols = this.getHeaderCols();
    const bodyCols = this.getBodyCols();
    const widths: number[] = [];
    let usedWidth = 0;
    let headerBodyColCount = 0;
    for (let i = 0; i < columnRendered.length; i++) {
      if (columnRendered[i]) {
        // last column is the filler column
        const newWidth = parseInt(headerCols.item(headerBodyColCount).getAttribute("width"));
        // for the hidden field
        widths[i] = newWidth;
        usedWidth += newWidth;

        const oldWidth = parseInt(bodyCols.item(headerBodyColCount).getAttribute("width"));
        if (oldWidth !== newWidth) {
          bodyCols.item(headerBodyColCount).setAttribute("width", String(newWidth));
        }
        headerBodyColCount++;
      } else if (columnWidths !== undefined && columnWidths.length >= i) {
        widths[i] = columnWidths[i];
      } else {
        if (typeof tokens[i] === "number") {
          widths[i] = 100;
        } else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
          const intValue = parseInt(tokens[i].measure);
          if (tokens[i].measure.lastIndexOf("px") > 0) {
            widths[i] = intValue;
          } else if (tokens[i].measure.lastIndexOf("%") > 0) {
            widths[i] = parseInt(bodyTable.style.width) / 100 * intValue;
          }
        }
      }
    }

    // store the width values in a hidden field
    this.saveColumnWidths(widths);
    return false;
  }

  scrollAction(event: Event): void {
    this.syncScrolling();
    const sheetBody = event.currentTarget as HTMLElement;
    const scrollLeft = Math.round(sheetBody.scrollLeft);

    if (this.lazy) {
      const firstVisibleRowIndex = this.firstVisibleRowIndex;
      const firstVisibleRow
          = this.tableBody.querySelector<HTMLTableRowElement>(`tr[row-index='${firstVisibleRowIndex}']`);
      const firstVisibleRowScrollTop = sheetBody.scrollTop - firstVisibleRow.offsetTop;

      this.lazyScrollPosition = [firstVisibleRowIndex, Math.round(firstVisibleRowScrollTop), scrollLeft];
    } else {
      this.scrollPosition = [scrollLeft, Math.round(sheetBody.scrollTop)];
    }
  }

  mousedownOnRow(event: MouseEvent): void {
    console.debug("mousedownOnRow");
    this.mousedownOnRowData = {
      x: event.clientX,
      y: event.clientY
    };
  }

  clickOnCheckboxForAll(event: MouseEvent): void {
    const selectedSet = new Set<number>(JSON.parse(this.getHiddenSelected().value));
    const oldSelectedSet = new Set<number>(selectedSet);
    const checkbox = event.currentTarget as HTMLInputElement;
    if (checkbox.checked) {
      this.selectAll(selectedSet);
    } else {
      this.deselectAll(selectedSet);
    }
    this.getHiddenSelected().value = JSON.stringify(Array.from(selectedSet)); // write back to element
    this.fireSelectionChange(oldSelectedSet, selectedSet);
  }

  clickOnColumnSelector(event: MouseEvent): void {
    event.stopPropagation();
    this.clickOnRow(event);
  }

  clickOnRow(event: MouseEvent): void {
    const row = event.currentTarget as HTMLTableRowElement;
    if (row.classList.contains(Css.TOBAGO_SELECTED) || !Sheet.isInputElement(row)) {

      if (this.mousedownOnRowData) { // integration test: mousedownOnRowData may be 'null'
        if (Math.abs(this.mousedownOnRowData.x - event.clientX)
            + Math.abs(this.mousedownOnRowData.y - event.clientY) > 5) {
          // The user has moved the mouse. We assume, the user want to select some text inside the sheet,
          // so we doesn't select the row.
          return;
        }
      }

      const rows = this.getRowElements();
      const selector = this.getSelectorCheckbox(row);
      const selectionMode = this.getSelectionMode();
      const selectedSet = new Set<number>(JSON.parse(this.getHiddenSelected().value));
      const oldSelectedSet = new Set<number>(selectedSet);
      if ((!event.ctrlKey && !event.metaKey && !selector)
          || selectionMode === "single" || selectionMode === "singleOrNone") {
        this.deselectAll(selectedSet);
        this.resetSelected(selectedSet);
      }

      const lastClickedRowIndex = parseInt(this.dataset.tobagoLastClickedRowIndex);
      if (event.shiftKey && selectionMode === "multi" && lastClickedRowIndex > -1) {
        if (lastClickedRowIndex <= row.sectionRowIndex) {
          this.selectRange(
              selectedSet, rows, lastClickedRowIndex, row.sectionRowIndex, true, false);
        } else {
          this.selectRange(
              selectedSet, rows, row.sectionRowIndex, lastClickedRowIndex, true, false);
        }
      } else if (selectionMode !== "singleOrNone" || !this.isRowSelected(selectedSet, row)) {
        this.toggleSelection(selectedSet, row, selector);
      }
      this.getHiddenSelected().value = JSON.stringify(Array.from(selectedSet)); // write back to element
      this.fireSelectionChange(oldSelectedSet, selectedSet);
    }
  }

  private fireSelectionChange(oldSelectedSet: Set<number>, newSelectedSet: Set<number>) {
    const fireEvent = (oldSelectedSet, newSelectedSet) =>
        oldSelectedSet.size == newSelectedSet.size && [...oldSelectedSet].every((x) => newSelectedSet.has(x));
    if (fireEvent) {
      this.dispatchEvent(new CustomEvent(ClientBehaviors.ROW_SELECTION_CHANGE, {
        detail: {
          selection: newSelectedSet,
        },
      }));
    }
  }

  clickOnPaging(event: MouseEvent): void {
    const element = event.currentTarget as HTMLElement;

    const output: HTMLElement = element.querySelector("span");
    output.style.display = "none";

    const input: HTMLInputElement = element.querySelector("input");
    input.style.display = "initial";
    input.focus();
    input.select();
  }

  blurPaging(event: FocusEvent): void {
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
            "jakarta.faces.behavior.event": "reload",
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

  syncScrolling(): void {
    // sync scrolling of body to header
    const header = this.getHeader();
    if (header) {
      header.scrollLeft = this.getBody().scrollLeft;
    }
  }

  getHeader(): HTMLElement {
    return this.querySelector("tobago-sheet>header");
  }

  getHeaderTable(): HTMLElement {
    return this.querySelector("tobago-sheet>header>table");
  }

  getHeaderCols(): NodeListOf<HTMLElement> {
    return this.querySelectorAll("tobago-sheet>header>table>colgroup>col");
  }

  getBody(): HTMLElement {
    return this.querySelector("tobago-sheet>.tobago-body");
  }

  getBodyTable(): HTMLElement {
    return this.querySelector("tobago-sheet>.tobago-body>table");
  }

  getBodyCols(): NodeListOf<HTMLElement> {
    return this.querySelectorAll("tobago-sheet>.tobago-body>table>colgroup>col");
  }

  getHiddenSelected(): HTMLInputElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.getElementById(this.id + "::selected") as HTMLInputElement;
  }

  getHiddenScrollPosition(): HTMLInputElement {
    return this.querySelector("input[id$='::scrollPosition']");
  }

  getHiddenExpanded(): HTMLInputElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.getElementById(this.id + "::expanded") as HTMLInputElement;
  }

  /**
   * Get the element, which indicates the selection
   */
  getSelectorCheckbox(row: HTMLTableRowElement): HTMLInputElement {
    return row.querySelector("tr>td>input.tobago-selected");
  }

  private getSelectorAllCheckbox(): HTMLInputElement {
    return this.querySelector("thead>tr>th>span>input.tobago-selected");
  }

  private calculateSelectorAllChecked(selectedSet: Set<number>) {
    const selectorAll = this.getSelectorAllCheckbox();
    if (selectorAll) {
      let selected = false;
      for (const row of this.getRowElements()) {
        const checkbox: HTMLInputElement = this.getSelectorCheckbox(row);
        if (checkbox === null || !checkbox.disabled) {
          const rowIndex = Number(row.getAttribute("row-index"));
          if (selectedSet.has(rowIndex)) {
            selected = true;
          } else {
            selected = false;
            break;
          }
        }
      }
      selectorAll.checked = selected;
    }
  }

  getRowElements(): NodeListOf<HTMLTableRowElement> {
    return this.getBodyTable().querySelectorAll("tbody>tr");
  }

  getFirst(): number {
    return parseInt(this.dataset.tobagoFirst);
  }

  isRowSelected(selectedSet: Set<number>, row: HTMLTableRowElement): boolean {
    return selectedSet.has(parseInt(row.dataset.tobagoRowIndex));
  }

  resetSelected(selectedSet: Set<number>): void {
    selectedSet.clear();
  }

  toggleSelection(selectedSet: Set<number>, row: HTMLTableRowElement, checkbox: HTMLInputElement): void {
    this.dataset.tobagoLastClickedRowIndex = String(row.sectionRowIndex);
    if (checkbox === null || !checkbox.disabled) {
      const rowIndex = Number(row.getAttribute("row-index"));
      if (selectedSet.has(rowIndex)) {
        this.deselectRow(selectedSet, rowIndex, row, checkbox);
      } else {
        this.selectRow(selectedSet, rowIndex, row, checkbox);
      }
    }
    this.calculateSelectorAllChecked(selectedSet);
  }

  selectAll(selectedSet: Set<number>): void {
    const rows = this.getRowElements();
    this.selectRange(selectedSet, rows, 0, rows.length - 1, true, false);
  }

  deselectAll(selectedSet: Set<number>): void {
    const rows = this.getRowElements();
    this.selectRange(selectedSet, rows, 0, rows.length - 1, false, true);
  }

  toggleAll(selectedSet: Set<number>): void {
    const rows = this.getRowElements();
    this.selectRange(selectedSet, rows, 0, rows.length - 1, true, true);
  }

  selectRange(
      selectedSet: Set<number>, rows: NodeListOf<HTMLTableRowElement>, first: number, last: number,
      selectDeselected: boolean, deselectSelected: boolean): void {
    for (let i = first; i <= last; i++) {
      const row: HTMLTableRowElement = rows.item(i);
      const checkbox: HTMLInputElement = this.getSelectorCheckbox(row);
      if (checkbox === null || !checkbox.disabled) {
        const rowIndex = Number(row.getAttribute("row-index"));
        const on = selectedSet.has(rowIndex);
        if (selectDeselected && !on) {
          this.selectRow(selectedSet, rowIndex, row, checkbox);
        } else if (deselectSelected && on) {
          this.deselectRow(selectedSet, rowIndex, row, checkbox);
        }
      }
    }
    this.calculateSelectorAllChecked(selectedSet);
  }

  /**
   * @param selectedSet value of the hidden input-element to store the selection.
   * @param rowIndex int: zero based index of the row.
   * @param row tr-element: the row.
   * @param checkbox input-element: selector in the row.
   */
  selectRow(
      selectedSet: Set<number>, rowIndex: number, row: HTMLTableRowElement, checkbox?: HTMLInputElement): void {
    selectedSet.add(rowIndex);
    row.classList.add(Css.TOBAGO_SELECTED);
    row.classList.add(Css.TABLE_INFO);
    if (checkbox) {
      checkbox.checked = true;
      setTimeout(function (): void {
        checkbox.checked = true;
      }, 0);
    }
  }

  /**
   * @param selectedSet value of the hidden input-element to store the selection.
   * @param rowIndex int: zero based index of the row.
   * @param row tr-element: the row.
   * @param checkbox input-element: selector in the row.
   */
  deselectRow(
      selectedSet: Set<number>, rowIndex: number, row: HTMLTableRowElement, checkbox?: HTMLInputElement): void {
    selectedSet.delete(rowIndex);
    row.classList.remove(Css.TOBAGO_SELECTED);
    row.classList.remove(Css.TABLE_INFO);
    if (checkbox) {
      checkbox.checked = false;
      // XXX check if this is still needed... Async because of TOBAGO-1312
      setTimeout(function (): void {
        checkbox.checked = false;
      }, 0);
    }
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-sheet") == null) {
    window.customElements.define("tobago-sheet", Sheet);
  }
});
