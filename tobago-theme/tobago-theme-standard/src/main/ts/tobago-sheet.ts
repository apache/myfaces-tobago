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
    this.getRowElements().forEach((row) => this.initSelectionListener(row));
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

      this.sheetBody.addEventListener("scroll", this.lazyCheck.bind(this));

      const lazyScrollPosition = this.lazyScrollPosition;
      const firstVisibleRow
          = this.tableBody.querySelector<HTMLTableRowElement>(`tr[row-index='${lazyScrollPosition[0]}']`);
      this.sheetBody.scrollTop
          = firstVisibleRow.offsetTop + lazyScrollPosition[1]; //triggers scroll event -> lazyCheck()
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

  public get selectable(): Selectable {
    return Selectable[this.dataset.tobagoSelectionMode];
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
    return this.querySelector("input[id$='::selected']");
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
    return this.querySelector("input[id$='::lazyScrollPosition']");
  }

  get lazyUpdate(): boolean {
    return this.hasAttribute("lazy-update");
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

          const newRows = this.sheetLoader.querySelectorAll<HTMLTableRowElement>(".tobago-body tbody>tr");
          for (const newRow of newRows) {
            if (newRow.hasAttribute("row-index")) {
              const rowIndex = Number(newRow.getAttribute("row-index"));
              const row = this.tableBody.querySelector(`tr[row-index='${rowIndex}']`);
              const previousElement = row.previousElementSibling;
              // first remove the old element and then add the new element
              // otherwise eventlisteners cannot be registered
              if (previousElement == null) {
                const parentElement = row.parentElement;
                row.remove();
                parentElement.insertAdjacentElement("afterbegin", newRow);
              } else {
                row.remove();
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
                const row = this.tableBody.querySelector(`tr[row-index='${rowIndex}']`);
                row.insertAdjacentElement("afterend", newRow);
              }
            }

            this.initSelectionListener(newRow);
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

  private initSelectionListener(row: HTMLTableRowElement): void {
    let clickElement: HTMLTableRowElement | HTMLTableCellElement;

    if (this.columnSelector && this.columnSelector.disabled) {
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
      this.getRowElements().forEach((rowElement) => {
        if (rowElement.hasAttribute("row-index")) {
          rowIndexes.push(Number(rowElement.getAttribute("row-index")));
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

    this.getRowElements().forEach((rowElement) => {
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
            everyRowSelected = false;
          }
        }
        this.columnSelector.headerElement.checked = everyRowSelected;
      }
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

  getHiddenExpanded(): HTMLInputElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.getElementById(this.id + "::expanded") as HTMLInputElement;
  }

  getRowElements(): NodeListOf<HTMLTableRowElement> {
    return this.getBodyTable().querySelectorAll(":scope > tbody > tr");
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-sheet") == null) {
    window.customElements.define("tobago-sheet", Sheet);
  }
});
