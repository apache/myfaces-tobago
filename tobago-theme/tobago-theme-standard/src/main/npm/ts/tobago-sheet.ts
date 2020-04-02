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

import {DomUtils} from "./tobago-utils";
import {Page} from "./tobago-page";

class Sheet extends HTMLElement {

  static readonly SCROLL_BAR_SIZE: number = Sheet.getScrollBarSize();

  mousemoveData: any;
  mousedownOnRowData: any;

  lastCheckMillis: number;

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

  private static getRowTemplate(columns: number, rowIndex: number) : string {
    return `<tr row-index="${rowIndex}" class="tobago-sheet-row" dummy="dummy">
<td class="tobago-sheet-cell" colspan="${columns}"> </td>
</tr>`;
  }

  constructor() {
    super();
  }

  connectedCallback(): void {

    if (this.lazyUpdate) {
      // nothing to do here, will be done in method lazyResponse()
      return;
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

    let columnWidths = this.loadColumnWidths();
    console.info("columnWidths: %s", JSON.stringify(columnWidths));
    if (columnWidths && columnWidths.length === 0) { // active, but empty
      // otherwise use the layout definition
      const tokens = JSON.parse(this.dataset.tobagoLayout).columns;
      const columnRendered = this.isColumnRendered();

      const headerCols = this.getHeaderCols();
      const bodyTable = this.getBodyTable();
      const bodyCols = this.getBodyCols();

      console.assert(headerCols.length - 1 === bodyCols.length,
          "header and body column number doesn't match: %d != %d ", headerCols.length - 1, bodyCols.length);

      let sumRelative = 0; // tbd: is this needed?
      let widthRelative = bodyTable.offsetWidth;
      for (let i = 0; i < tokens.length; i++) {
        if (columnRendered[i]) {
          if (typeof tokens[i] === "number") {
            sumRelative += tokens[i];
          } else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
            const intValue = parseInt(tokens[i].measure);
            if (tokens[i].measure.lastIndexOf("px") > 0) {
              widthRelative -= intValue;
            } else if (tokens[i].measure.lastIndexOf("%") > 0) {
              widthRelative -= bodyTable.offsetWidth * intValue / 100;
            }
          } else {
            console.debug("auto? = " + tokens[i]);
          }
        }
      }
      if (widthRelative < 0) {
        widthRelative = 0;
      }

      let headerBodyColCount = 0;
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
              colWidth = bodyTable.offsetWidth * intValue / 100;
            }
          } else {
            console.debug("auto? = " + tokens[i]);
          }
          if (colWidth > 0) { // because tokens[i] == "auto"
            headerCols.item(headerBodyColCount).setAttribute("width", String(colWidth));
            bodyCols.item(headerBodyColCount).setAttribute("width", String(colWidth));
          }
          headerBodyColCount++;
        }
      }
    }
    this.addHeaderFillerWidth();

    // resize column: mouse events -------------------------------------------------------------------------------- //

    for (const resizeElement of <NodeListOf<HTMLElement>>this.querySelectorAll(".tobago-sheet-headerResize")) {
      resizeElement.addEventListener("click", function (): boolean {
        return false;
      });
      resizeElement.addEventListener("mousedown", this.mousedown.bind(this));
    }

    // scrolling -------------------------------------------------------------------------------------------------- //
    const sheetBody = this.getBody();

    // restore scroll position
    const value: number[] = JSON.parse(this.getHiddenScrollPosition().getAttribute("value"));
    sheetBody.scrollLeft = value[0];
    sheetBody.scrollTop = value[1];

    this.syncScrolling();

    // scroll events
    sheetBody.addEventListener("scroll", this.scroll.bind(this));

    // add selection listeners ------------------------------------------------------------------------------------ //
    const selectionMode = this.dataset.tobagoSelectionMode;
    if (selectionMode === "single" || selectionMode === "singleOrNone" || selectionMode === "multi") {

      for (const row of this.getRowElements()) {
        row.addEventListener("mousedown", this.mousedownOnRow.bind(this));

        row.addEventListener("click", this.clickOnRow.bind(this));
      }
    }

    for (const checkbox of <NodeListOf<HTMLInputElement>>this.querySelectorAll(
        ".tobago-sheet-cell > input.tobago-sheet-columnSelector")) {
      checkbox.addEventListener("click", (event) => {
        event.preventDefault();
      });
    }

    // lazy load by scrolling ----------------------------------------------------------------- //

      const lazy = this.lazy;

      if (lazy) {
        // prepare the sheet with some auto-created (empty) rows
        const rowCount = this.rowCount;
        const sheetBody = this.tableBodyDiv;
        const tableBody = this.tableBody;
        const columns = tableBody.rows[0].cells.length;
        let current: HTMLTableRowElement = tableBody.rows[0]; // current row in this algorithm, begin with first
        // the algorithm goes straight through all rows, not selectors, because of performance
        for (let i = 0; i < rowCount; i++) {
          if (current) {
            const rowIndex = Number(current.getAttribute("row-index"));
            if (i < rowIndex) {
              const template = Sheet.getRowTemplate(columns, i);
              current.insertAdjacentHTML("beforebegin", template);
            } else if (i === rowIndex) {
              current = current.nextElementSibling as HTMLTableRowElement;
            // } else { TBD: I think this is not possible
            //   const template = Sheet.getRowTemplate(columns, i);
            //   current.insertAdjacentHTML("afterend", template);
            //   current = current.nextElementSibling as HTMLTableRowElement;
            }
          } else {
            const template = Sheet.getRowTemplate(columns, i);
            tableBody.insertAdjacentHTML("beforeend", template);
          }
        }

        sheetBody.addEventListener("scroll", this.lazyCheck.bind(this));

        // initial
        this.lazyCheck();
      }

    // ---------------------------------------------------------------------------------------- //

    for (const checkbox of <NodeListOf<HTMLInputElement>>this.querySelectorAll(
        ".tobago-sheet-header .tobago-sheet-columnSelector")) {
      checkbox.addEventListener("click", this.clickOnCheckbox.bind(this));
    }

    // init paging by pages ---------------------------------------------------------------------------------------- //

    for (const pagingText of <NodeListOf<HTMLElement>>this.querySelectorAll(".tobago-sheet-pagingText")) {

      pagingText.addEventListener("click", this.clickOnPaging.bind(this));

      const pagingInput = pagingText.querySelector("input.tobago-sheet-pagingInput");
      pagingInput.addEventListener("blur", this.blurPaging.bind(this));

      pagingInput.addEventListener("keydown", function (event: KeyboardEvent): void {
        if (event.keyCode === 13) {
          event.stopPropagation();
          event.preventDefault();
          event.currentTarget.dispatchEvent(new Event("blur"));
        }
      });
    }
  }

  // attribute getter + setter ---------------------------------------------------------- //

  get lazyActive():boolean {
    return this.hasAttribute("lazy-active");
  }

  set lazyActive(update:boolean) {
    if (update) {
      this.setAttribute("lazy-active", "");
    } else {
      this.removeAttribute("lazy-active");
    }
  }

  get lazy():boolean {
    return this.hasAttribute("lazy");
  }

  set lazy(update:boolean) {
    if (update) {
      this.setAttribute("lazy", "");
    } else {
      this.removeAttribute("lazy");
    }
  }

  get lazyUpdate():boolean {
    return this.hasAttribute("lazy-update");
  }

  get rows():number {
    return parseInt(this.getAttribute("rows"));
  }

  get rowCount():number {
    return parseInt(this.getAttribute("row-count"));
  }

  get tableBodyDiv(): HTMLDivElement {
    return this.querySelector(".tobago-sheet-body");
  }

  get tableBody(): HTMLTableSectionElement {
    return this.querySelector(".tobago-sheet-bodyTable>tbody");
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
  lazyCheck(event?): void {

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
    if (next) {
      this.lazyActive = true;
      const rootNode = this.getRootNode() as ShadowRoot | Document;
      const input = rootNode.getElementById(this.id + ":pageActionlazy") as HTMLInputElement;
      input.value = String(next);
      this.reloadWithAction(input);
    }
  }

  nextLazyLoad(): number {
    // find first tr in current visible area
    const rows = this.rows;
    const rowElements = this.tableBody.rows;
    let min = 0;
    let max = rowElements.length;
    // binary search
    let i;
    while (min < max) {
      i = Math.floor((max - min) / 2) + min;
      // console.log("min i max -> %d %d %d", min, i, max); // @DEV_ONLY
      if (this.isRowAboveVisibleArea(rowElements[i])) {
        min = i + 1;
      } else {
        max = i;
      }
    }
    for (i = min; i < min + rows && i < rowElements.length; i++) {
      if (this.isRowDummy(rowElements[i])) {
        return i + 1;
      }
    }

    return null;
  }

  isRowAboveVisibleArea(tr: HTMLTableRowElement): boolean {
    const sheetBody = this.tableBodyDiv;
    const viewStart = sheetBody.scrollTop;
    const trEnd = tr.offsetTop + tr.clientHeight;
    return trEnd < viewStart;
  }

  isRowDummy(tr): boolean {
    return tr.hasAttribute("dummy");
  }

  lazyResponse(event): void {
    let updates;
    if (event.status === "complete") {
      updates = event.responseXML.querySelectorAll("update");
      for (let i = 0; i < updates.length; i++) {
        const update = updates[i];
        const id = update.getAttribute("id");
        if (id.indexOf(":") > -1) { // is a JSF element id, but not a technical id from the framework
          console.debug("[tobago-sheet][complete] Update after jsf.ajax complete: #" + id); // @DEV_ONLY

          const sheet = document.getElementById(id);
          sheet.id = id + "::lazy-temporary";

          const page = Page.page();
          page.insertAdjacentHTML("beforeend", `<div id="${id}"></div>`);
          const sheetLoader = document.getElementById(id);
        }
      }
    } else if (event.status === "success") {
      updates = event.responseXML.querySelectorAll("update");
      for (let i = 0; i < updates.length; i++) {
        const update = updates[i];
        const id = update.getAttribute("id");
        if (id.indexOf(":") > -1) { // is a JSF element id, but not a technical id from the framework
          console.debug("[tobago-sheet][success] Update after jsf.ajax complete: #" + id); // @DEV_ONLY

          // sync the new rows into the sheet
          const sheetLoader = document.getElementById(id);
          const sheet = document.getElementById(id + "::lazy-temporary");
          sheet.id = id;
          const tbody = sheet.querySelector(".tobago-sheet-bodyTable>tbody");

          const newRows = sheetLoader.querySelectorAll(".tobago-sheet-bodyTable>tbody>tr");
          for (i = 0; i < newRows.length; i++) {
            const newRow = newRows[i];
            const rowIndex = Number(newRow.getAttribute("row-index"));
            const row = tbody.querySelector("tr[row-index='" + rowIndex + "']");
            // replace the old row with the new row
            row.insertAdjacentElement("afterend", newRow);
            tbody.removeChild(row);
          }

          sheetLoader.parentElement.removeChild(sheetLoader);
          this.lazyActive = false;
        }
      }
    }
  }

  lazyError(data): void {
    console.error("Sheet lazy loading error:"
        + "\nError Description: " + data.description
        + "\nError Name: " + data.errorName
        + "\nError errorMessage: " + data.errorMessage
        + "\nResponse Code: " + data.responseCode
        + "\nResponse Text: " + data.responseText
        + "\nStatus: " + data.status
        + "\nType: " + data.type);
  }

  // tbd: how to do this in Tobago 5?
  reloadWithAction(source: HTMLElement): void {
    console.debug("reload sheet with action '" + source.id + "'"); // @DEV_ONLY
    const executeIds = this.id;
    const renderIds = this.id;
    const lazy = this.lazy;

    jsf.ajax.request(
        source.id,
        null,
        {
          "javax.faces.behavior.event": "reload",
          execute: executeIds,
          render: renderIds,
          onevent: lazy ? this.lazyResponse.bind(this) : undefined,
          onerror: lazy ? this.lazyError.bind(this) : undefined
        });
  }

  loadColumnWidths(): number[] {
    const hidden = document.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "widths");
    if (hidden) {
      return JSON.parse(hidden.getAttribute("value"));
    } else {
      return undefined;
    }
  }

  saveColumnWidths(widths: number[]): void {
    const hidden = document.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "widths");
    if (hidden) {
      hidden.setAttribute("value", JSON.stringify(widths));
    } else {
      console.warn("ignored, should not be called, id='" + this.id + "'");
    }
  }

  isColumnRendered(): boolean[] {
    const hidden = document.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "rendered");
    return JSON.parse(hidden.getAttribute("value"));
  }

  addHeaderFillerWidth(): void {
    const last = document.getElementById(this.id).querySelector(".tobago-sheet-headerTable col:last-child");
    if (last) {
      last.setAttribute("width", String(Sheet.SCROLL_BAR_SIZE));
    }
  }

  mousedown(event: MouseEvent): void {

    Page.page().dataset.SheetMousedownData = this.id;

    // begin resizing
    console.debug("down");

    const resizeElement = event.currentTarget as HTMLElement;
    const columnIndex = parseInt(resizeElement.dataset.tobagoColumnIndex);
    const headerColumn = this.getHeaderCols().item(columnIndex);
    const mousemoveListener = this.mousemove.bind(this);
    const mouseupListener = this.mouseup.bind(this);
    this.mousemoveData = {
      columnIndex: columnIndex,
      originalClientX: event.clientX,
      originalHeaderColumnWidth: parseInt(headerColumn.getAttribute("width")),
      mousemoveListener: mousemoveListener,
      mouseupListener: mouseupListener
    };

    document.addEventListener("mousemove", mousemoveListener);
    document.addEventListener("mouseup", mouseupListener);
  }

  mousemove(event: MouseEvent): boolean {
    console.debug("move");
    let delta = event.clientX - this.mousemoveData.originalClientX;
    delta = -Math.min(-delta, this.mousemoveData.originalHeaderColumnWidth - 10);
    let columnWidth = this.mousemoveData.originalHeaderColumnWidth + delta;
    this.getHeaderCols().item(this.mousemoveData.columnIndex).setAttribute("width", columnWidth);
    this.getBodyCols().item(this.mousemoveData.columnIndex).setAttribute("width", columnWidth);
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
    const tokens = JSON.parse(this.dataset.tobagoLayout).columns;
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

  scroll(event): void {
    console.debug("scroll");

    const sheetBody: HTMLElement = event.currentTarget;

    this.syncScrolling();

    // store the position in a hidden field
    const hidden = this.getHiddenScrollPosition();
    hidden.setAttribute("value",
        JSON.stringify([Math.round(sheetBody.scrollLeft), Math.round(sheetBody.scrollTop)]));
  }

  mousedownOnRow(event: MouseEvent): void {
    console.debug("mousedownOnRow");
    this.mousedownOnRowData = {
      x: event.clientX,
      y: event.clientY
    };
  }

  clickOnCheckbox(event: MouseEvent): void {
    const checkbox = event.currentTarget as HTMLInputElement;
    if (checkbox.checked) {
      this.selectAll();
    } else {
      this.deselectAll();
    }
  }

  clickOnRow(event: MouseEvent): void {

    const row = event.currentTarget as HTMLTableRowElement;
    if (row.classList.contains("tobago-sheet-columnSelector") || !Sheet.isInputElement(row)) {

      if (Math.abs(this.mousedownOnRowData.x - event.clientX)
          + Math.abs(this.mousedownOnRowData.y - event.clientY) > 5) {
        // The user has moved the mouse. We assume, the user want to select some text inside the sheet,
        // so we doesn't select the row.
        return;
      }

      if (window.getSelection) {
        window.getSelection().removeAllRanges();
      }

      const rows = this.getRowElements();
      const selector = this.getSelectorCheckbox(row);
      const selectionMode = this.dataset.tobagoSelectionMode;

      if ((!event.ctrlKey && !event.metaKey && !selector)
          || selectionMode === "single" || selectionMode === "singleOrNone") {
        this.deselectAll();
        this.resetSelected();
      }

      const lastClickedRowIndex = parseInt(this.dataset.tobagoLastClickedRowIndex);
      if (event.shiftKey && selectionMode === "multi" && lastClickedRowIndex > -1) {
        if (lastClickedRowIndex <= row.sectionRowIndex) {
          this.selectRange(rows, lastClickedRowIndex, row.sectionRowIndex, true, false);
        } else {
          this.selectRange(rows, row.sectionRowIndex, lastClickedRowIndex, true, false);
        }
      } else if (selectionMode !== "singleOrNone" || !this.isRowSelected(row)) {
        this.toggleSelection(row, selector);
      }
    }
  }

  clickOnPaging(event: MouseEvent): void {
    const element = event.currentTarget as HTMLElement;

    const output = element.querySelector(".tobago-sheet-pagingOutput") as HTMLElement;
    output.style.display = "none";

    const input = element.querySelector(".tobago-sheet-pagingInput") as HTMLInputElement;
    input.style.display = "initial";
    input.focus();
    input.select();
  }

  blurPaging(event: FocusEvent): void {
    const input = event.currentTarget as HTMLInputElement;
    const output = input.parentElement.querySelector(".tobago-sheet-pagingOutput") as HTMLElement;
    if (output.innerHTML !== input.value) {
      console.debug(
          "Reloading sheet '" + this.id + "' old value='" + output.innerHTML + "' new value='" + input.value + "'");
      output.innerHTML = input.value;
      jsf.ajax.request(
          input.id,
          null,
          {
            "javax.faces.behavior.event": "reload",
            execute: this.id,
            render: this.id
          });
    } else {
      console.info("no update needed");
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
    return this.querySelector("tobago-sheet>.tobago-sheet-body");
  }

  getBodyTable(): HTMLElement {
    return this.querySelector("tobago-sheet>.tobago-sheet-body>.tobago-sheet-bodyTable");
  }

  getBodyCols(): NodeListOf<HTMLElement> {
    return this.querySelectorAll("tobago-sheet>.tobago-sheet-body>.tobago-sheet-bodyTable>colgroup>col");
  }

  getHiddenSelected(): HTMLInputElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "selected")  as HTMLInputElement;
  }

  getHiddenScrollPosition(): HTMLInputElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "scrollPosition")  as HTMLInputElement;
  }

  /**
   * Get the element, which indicates the selection
   */
  getSelectorCheckbox(row: HTMLTableRowElement): HTMLInputElement {
    return row.querySelector("tr>td>input.tobago-sheet-columnSelector");
  }

  getRowElements(): NodeListOf<HTMLTableRowElement> {
    return this.getBodyTable().querySelectorAll("tbody>tr");
  }

  getFirst(): number {
    return parseInt(this.dataset.tobagoFirst);
  }

  isRowSelected(row: HTMLTableRowElement): boolean {
    return this.isSelected(parseInt(row.dataset.tobagoRowIndex));
  }

  isSelected(rowIndex: number): boolean {
    const value = <number[]>JSON.parse(this.getHiddenSelected().value);
    return value.indexOf(rowIndex) > -1;
  }

  resetSelected():void {
    this.getHiddenSelected().value = JSON.stringify([]);
  }

  toggleSelection(row: HTMLTableRowElement, checkbox: HTMLInputElement): void {
    this.dataset.tobagoLastClickedRowIndex = String(row.sectionRowIndex);
    if (checkbox && !checkbox.disabled) {
      const selected = this.getHiddenSelected();
      const rowIndex = Number(row.getAttribute("row-index"));
      if (this.isSelected(rowIndex)) {
        this.deselectRow(selected, rowIndex, row, checkbox);
      } else {
        this.selectRow(selected, rowIndex, row, checkbox);
      }
    }
  }

  selectAll(): void {
    const rows = this.getRowElements();
    this.selectRange(rows, 0, rows.length - 1, true, false);
  }

  deselectAll(): void {
    const rows = this.getRowElements();
    this.selectRange(rows, 0, rows.length - 1, false, true);
  }

  toggleAll(): void {
    const rows = this.getRowElements();
    this.selectRange(rows, 0, rows.length - 1, true, true);
  }

  selectRange(
      rows: NodeListOf<HTMLTableRowElement>, first: number, last: number, selectDeselected: boolean,
      deselectSelected: boolean): void {
    const selected = this.getHiddenSelected();
    const value = new Set<number>(JSON.parse(selected.value));
    for (let i = first; i <= last; i++) {
      const row = rows.item(i);
      const checkbox = this.getSelectorCheckbox(row);
      if (checkbox && !checkbox.disabled) {
        const rowIndex = Number(row.getAttribute("row-index"));
        const on = value.has(rowIndex);
        if (selectDeselected && !on) {
          this.selectRow(selected, rowIndex, row, checkbox);
        } else if (deselectSelected && on) {
          this.deselectRow(selected, rowIndex, row, checkbox);
        }
      }
    }
  }

  /**
   * @param selected input-element type=hidden: Hidden field with the selection state information
   * @param rowIndex int: zero based index of the row.
   * @param row tr-element: the row.
   * @param checkbox input-element: selector in the row.
   */
  selectRow(selected: HTMLInputElement, rowIndex: number, row: HTMLTableRowElement, checkbox: HTMLInputElement): void {
    const selectedSet = new Set<number>(JSON.parse(selected.value));
    selected.value = JSON.stringify(Array.from(selectedSet.add(rowIndex)));
    row.classList.add("tobago-sheet-row-markup-selected");
    row.classList.add("table-info");
    checkbox.checked = true;
    setTimeout(function ():void {
      checkbox.checked = true;
    }, 0);
  }

  /**
   * @param selected input-element type=hidden: Hidden field with the selection state information
   * @param rowIndex int: zero based index of the row.
   * @param row tr-element: the row.
   * @param checkbox input-element: selector in the row.
   */
  deselectRow(
      selected: HTMLInputElement, rowIndex: number, row: HTMLTableRowElement, checkbox: HTMLInputElement): void {
    const selectedSet = new Set<number>(JSON.parse(selected.value));
    selectedSet.delete(rowIndex);
    selected.value = JSON.stringify(Array.from(selectedSet));
    row.classList.remove("tobago-sheet-row-markup-selected");
    row.classList.remove("table-info");
    checkbox.checked = false;
    // XXX check if this is still needed... Async because of TOBAGO-1312
    setTimeout(function (): void {
      checkbox.checked = false;
    }, 0);
  }

}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-sheet", Sheet);
});
