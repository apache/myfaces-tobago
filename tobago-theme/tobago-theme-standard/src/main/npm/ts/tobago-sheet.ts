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

import {Listener, Phase} from "./tobago-listener";
import {DomUtils} from "./tobago-utils";
import {Tobago4} from "./tobago-core";

class Sheet {

  static readonly SHEETS: Map<string, Sheet> = new Map<string, Sheet>();
  static readonly SCROLL_BAR_SIZE: number = Sheet.getScrollBarSize();

  id: string;

  clickActionId: string;
  clickReloadComponentId: string;
  dblClickActionId: string;
  dblClickReloadComponentId: string;

  mousemoveData: any;
  mousedownOnRowData: any;

  loadColumnWidths(): number[] {
    const hidden = document.getElementById(this.id + Tobago4.SUB_COMPONENT_SEP + "widths");
    if (hidden) {
      return JSON.parse(hidden.getAttribute("value"));
    } else {
      return undefined;
    }
  }

  saveColumnWidths(widths: number[]) {
    const hidden = document.getElementById(this.id + Tobago4.SUB_COMPONENT_SEP + "widths");
    if (hidden) {
      hidden.setAttribute("value", JSON.stringify(widths));
    } else {
      console.warn("ignored, should not be called, id='" + this.id + "'");
    }
  }

  getElement(): HTMLElement {
    return document.getElementById(this.id);
  }

  isColumnRendered(): boolean[] {
    const hidden = document.getElementById(this.id + Tobago4.SUB_COMPONENT_SEP + "rendered");
    return JSON.parse(hidden.getAttribute("value"));
  };

  addHeaderFillerWidth() {
    const last = document.getElementById(this.id).querySelector(".tobago-sheet-headerTable col:last-child");
    if (last) {
      last.setAttribute("width", String(Sheet.SCROLL_BAR_SIZE));
    }
  };

  constructor(element: HTMLElement) {
    this.id = element.id;
    const commands = element.dataset["tobagoRowAction"];
    const behavior = element.dataset["tobagoBehaviorCommands"];
    this.clickActionId = null;//todo commands.click.action;
    this.clickReloadComponentId = null;//todo commands.click.partially; // fixme: partially no longer used?
    this.dblClickActionId = null;//todo commands.dblclick.action;
    this.dblClickReloadComponentId = null;//todo commands.dblclick.partially;// fixme: partially no longer used?

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
    console.info("columnWidths: " + columnWidths);
    if (columnWidths && columnWidths.length === 0) { // active, but empty
      // otherwise use the layout definition
      const tokens = JSON.parse(element.dataset["tobagoLayout"]).columns;
      const columnRendered = this.isColumnRendered();

      const headerCols = this.getHeaderCols();
      const bodyTable = this.getBodyTable();
      const bodyCols = this.getBodyCols();

      console.assert(headerCols.length - 1 === bodyCols.length, "header and body column number doesn't match");

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

    for (const resizeElement of <NodeListOf<HTMLElement>>element.querySelectorAll(".tobago-sheet-headerResize")) {
      resizeElement.addEventListener("click", function () {
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
    const selectionMode = element.dataset["tobagoSelectionMode"];
    if (selectionMode === "single" || selectionMode === "singleOrNone" || selectionMode === "multi") {

      for (const row of this.getRows()) {
        row.addEventListener("mousedown", this.mousedownOnRow.bind(this));

        row.addEventListener("click", this.clickOnRow.bind(this));

        // todo: check if this works correctly
        const sheet = Sheet.SHEETS.get(this.id);
        if (sheet && sheet.dblClickActionId) {
          row.addEventListener("dblclick", function (event) {
            // todo: re-implement
            sheet.doDblClick(event);
          });
        }
      }
    }

    for (const checkbox of <NodeListOf<HTMLInputElement>>element.querySelectorAll(".tobago-sheet-cell > input.tobago-sheet-columnSelector")) {
      checkbox.addEventListener("click", (event) => {
        event.preventDefault();
      });
    }

    // ---------------------------------------------------------------------------------------- //

    for (const checkbox of <NodeListOf<HTMLInputElement>>element.querySelectorAll(".tobago-sheet-header .tobago-sheet-columnSelector")) {
      checkbox.addEventListener("click", this.clickOnCheckbox.bind(this));
    }

    // init paging by pages ---------------------------------------------------------------------------------------- //

    for (const pagingText of <NodeListOf<HTMLElement>>element.querySelectorAll(".tobago-sheet-pagingText")) {

      pagingText.addEventListener("click", this.clickOnPaging.bind(this));

      const pagingInput = pagingText.querySelector("input.tobago-sheet-pagingInput");
      pagingInput.addEventListener("blur", this.blurPaging.bind(this));

      pagingInput.addEventListener("keydown", function (event: KeyboardEvent) {
        if (event.keyCode === 13) {
          event.stopPropagation();
          event.preventDefault();
          event.currentTarget.dispatchEvent(new Event("blur"));
        }
      });
    }

  }

  static init = function (element: HTMLElement) {
    console.time("[tobago-sheet] init");
    for (const sheetElement of DomUtils.selfOrElementsByClassName(element, "tobago-sheet")) {
      const sheet = new Sheet(sheetElement);
      Sheet.SHEETS.set(sheet.id, sheet);
    }
    console.timeEnd("[tobago-sheet] init");
  };

  mousedown(event: MouseEvent) {

    DomUtils.page().dataset["SheetMousedownData"] = this.id;

    // begin resizing
    console.debug("down");

    const resizeElement = <HTMLElement>event.currentTarget;
    const columnIndex = parseInt(resizeElement.dataset["tobagoColumnIndex"]);
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

  mousemove(event: MouseEvent) {
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

  mouseup(event: MouseEvent) {
    console.debug("up");

    // switch off the mouse move listener
    document.removeEventListener("mousemove", this.mousemoveData.mousemoveListener);
    document.removeEventListener("mouseup", this.mousemoveData.mouseupListener);
    // copy the width values from the header to the body, (and build a list of it)
    const tokens = JSON.parse(this.getElement().dataset["tobagoLayout"]).columns;
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

  scroll(event) {
    console.debug("scroll");

    const sheetBody: HTMLElement = event.currentTarget;

    this.syncScrolling();

    // store the position in a hidden field
    const hidden = this.getHiddenScrollPosition();
    hidden.setAttribute("value",
        JSON.stringify([Math.round(sheetBody.scrollLeft), Math.round(sheetBody.scrollTop)]));
  }

  mousedownOnRow(event: MouseEvent) {
    console.debug("mousedownOnRow");
    this.mousedownOnRowData = {
      x: event.clientX,
      y: event.clientY
    };
  }

  clickOnCheckbox(event: MouseEvent) {
    const checkbox = <HTMLInputElement>event.currentTarget;
    if (checkbox.checked) {
      this.selectAll();
    } else {
      this.deselectAll();
    }
  }

  clickOnRow(event: MouseEvent) {

    const row = <HTMLTableRowElement>event.currentTarget;
    if (row.classList.contains("tobago-sheet-columnSelector") || !Sheet.isInputElement(row)) {
      const sheet = this.getElement();

      if (Math.abs(this.mousedownOnRowData.x - event.clientX)
          + Math.abs(this.mousedownOnRowData.y - event.clientY) > 5) {
        // The user has moved the mouse. We assume, the user want to select some text inside the sheet,
        // so we doesn't select the row.
        return;
      }

      if (window.getSelection) {
        window.getSelection().removeAllRanges();
      }

      const rows = this.getRows();
      const selector = this.getSelectorCheckbox(row);
      const selectionMode = this.getElement().dataset["tobagoSelectionMode"];

      if ((!event.ctrlKey && !event.metaKey && !selector)
          || selectionMode === "single" || selectionMode === "singleOrNone") {
        this.deselectAll();
        this.resetSelected();
      }

      const lastClickedRowIndex = parseInt(sheet.dataset["tobagoLastClickedRowIndex"]);
      if (event.shiftKey && selectionMode === "multi" && lastClickedRowIndex > -1) {
        if (lastClickedRowIndex <= row.rowIndex) {
          this.selectRange(rows, lastClickedRowIndex, row.rowIndex, true, false);
        } else {
          this.selectRange(rows, row.rowIndex, lastClickedRowIndex, true, false);
        }
      } else if (selectionMode !== "singleOrNone" || !this.isRowSelected(row)) {
        this.toggleSelection(row, selector);
      }
      const rowAction = sheet.dataset["tobagoRowAction"];
      const commands = rowAction ? JSON.parse(rowAction) : undefined;
      const click = commands ? commands.click : undefined;
      const clickActionId = click ? click.action : undefined;
      const clickExecuteIds = click ? click.execute : undefined;
      const clickRenderIds = click ? click.render : undefined;

      if (clickActionId) {
        let action: string;
        const index = clickActionId.indexOf(sheet.id);
        const rowIndex = this.getDataIndex(row);
        if (index >= 0) {
          action = sheet.id + ":" + rowIndex + ":" + clickActionId.substring(index + sheet.id.length + 1);
        } else {
          action = sheet.id + ":" + rowIndex + ":" + clickActionId;
        }
        if (clickExecuteIds && clickExecuteIds.length > 0) {
          jsf.ajax.request(
              action,
              event,
              {
                //"javax.faces.behavior.event": "click",
                execute: clickExecuteIds,
                render: clickRenderIds
              });
        } else {
          Tobago4.submitAction(row, action);
        }
      }
    }
  }

  clickOnPaging(event: MouseEvent) {
    const element = <HTMLElement>event.currentTarget;

    const output = <HTMLElement>element.querySelector(".tobago-sheet-pagingOutput");
    output.style.display = "none";

    const input = <HTMLInputElement>element.querySelector(".tobago-sheet-pagingInput");
    input.style.display = "initial";
    input.focus();
    input.select();
  }

  blurPaging(event: FocusEvent) {
    const input = <HTMLInputElement>event.currentTarget;
    const output = <HTMLElement>input.parentElement.querySelector(".tobago-sheet-pagingOutput");
    if (output.innerHTML !== input.value) {
      console.debug("Reloading sheet '" + this.id + "' old value='" + output.innerHTML + "' new value='" + input.value + "'");
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

  syncScrolling() {
    // sync scrolling of body to header
    const header = this.getHeader();
    if (header) {
      header.scrollLeft = this.getBody().scrollLeft;
    }
  }

  getHeader(): HTMLElement {
    return this.getElement().querySelector(".tobago-sheet>header");
  };

  getHeaderTable(): HTMLElement {
    return this.getElement().querySelector(".tobago-sheet>header>table");
  };

  getHeaderCols(): NodeListOf<HTMLElement> {
    return this.getElement().querySelectorAll(".tobago-sheet>header>table>colgroup>col");
  };

  getBody(): HTMLElement {
    return this.getElement().querySelector(".tobago-sheet>.tobago-sheet-body");
  };

  getBodyTable(): HTMLElement {
    return this.getElement().querySelector(".tobago-sheet>.tobago-sheet-body>.tobago-sheet-bodyTable");
  };

  getBodyCols(): NodeListOf<HTMLElement> {
    return this.getElement().querySelectorAll(".tobago-sheet>.tobago-sheet-body>.tobago-sheet-bodyTable>colgroup>col");
  };

  getHiddenSelected(): HTMLInputElement {
    return <HTMLInputElement>document.getElementById(this.id + Tobago4.SUB_COMPONENT_SEP + "selected");
  };

  getHiddenScrollPosition() {
    return document.getElementById(this.id + Tobago4.SUB_COMPONENT_SEP + "scrollPosition");
  };

  static getScrollBarSize() {
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
  };

  doDblClick(event) {
    const row = <HTMLTableRowElement>event.currentTarget;
    const rowIndex = row.rowIndex + this.getFirst();
    if (this.dblClickActionId) {
      let action;
      const index = this.dblClickActionId.indexOf(this.id);
      if (index >= 0) {
        action = this.id + ":" + rowIndex + ":" + this.dblClickActionId.substring(index + this.id.length + 1);
      } else {
        action = this.id + ":" + rowIndex + ":" + this.dblClickActionId;
      }
      if (this.dblClickReloadComponentId && this.dblClickReloadComponentId.length > 0) {
        jsf.ajax.request(
            action,
            event,
            {
              //"javax.faces.behavior.event": "dblclick",
              execute: this.dblClickReloadComponentId,
              render: this.dblClickReloadComponentId
            });
      } else {
        Tobago4.submitAction(row, action);
      }
    }
  };

  /**
   * Get the element, which indicates the selection
   */
  getSelectorCheckbox(row): HTMLInputElement {
    return row.querySelector("tr>td>input.tobago-sheet-columnSelector");
  };

  getRows(): NodeListOf<HTMLTableRowElement> {
    return this.getBodyTable().querySelectorAll("tbody>tr");
  };

  getFirst(): number {
    return parseInt(this.getElement().dataset["tobagoFirst"]);
  }

  isRowSelected(row: HTMLTableRowElement) {
    let rowIndex = +row.dataset["tobagoRowIndex"];
    if (!rowIndex) {
      rowIndex = row.rowIndex + this.getFirst();
    }
    return this.isSelected(rowIndex);
  };

  isSelected(rowIndex) {
    return this.getHiddenSelected().getAttribute("value").indexOf("," + rowIndex + ",") >= 0;
  };

  resetSelected() {
    this.getHiddenSelected().setAttribute("value", ",");
  };

  toggleSelection(row: HTMLTableRowElement, checkbox: HTMLInputElement) {
    this.getElement().dataset["tobagoLastClickedRowIndex"] = String(row.rowIndex);
    if (checkbox && !checkbox.disabled) {
      const selected = this.getHiddenSelected();
      const rowIndex = this.getDataIndex(row);
      if (this.isSelected(rowIndex)) {
        this.deselectRow(selected, rowIndex, row, checkbox);
      } else {
        this.selectRow(selected, rowIndex, row, checkbox);
      }
    }
  };

  selectAll() {
    const rows = this.getRows();
    this.selectRange(rows, 0, rows.length - 1, true, false);
  };

  deselectAll() {
    const rows = this.getRows();
    this.selectRange(rows, 0, rows.length - 1, false, true);
  };

  toggleAll() {
    const rows = this.getRows();
    this.selectRange(rows, 0, rows.length - 1, true, true);
  };

  selectRange(
      rows: NodeListOf<HTMLTableRowElement>, first: number, last: number, selectDeselected: boolean, deselectSelected: boolean) {
    const selected = this.getHiddenSelected();
    const value = selected.value;
    for (let i = first; i <= last; i++) {
      const row = rows.item(i);
      const checkbox = this.getSelectorCheckbox(row);
      if (checkbox && !checkbox.disabled) {
        const rowIndex = this.getDataIndex(row);
        const on = value.indexOf("," + rowIndex + ",") >= 0;
        if (selectDeselected && !on) {
          this.selectRow(selected, rowIndex, row, checkbox);
        } else if (deselectSelected && on) {
          this.deselectRow(selected, rowIndex, row, checkbox);
        }
      }
    }
  };

  getDataIndex(row: HTMLTableRowElement): number {
    const rowIndex = parseInt(row.dataset["tobagoRowIndex"]);
    if (rowIndex) {
      return rowIndex;
    } else {
      return row.rowIndex + this.getFirst();
    }
  };

  /**
   * @param selected input-element type=hidden: Hidden field with the selection state information
   * @param rowIndex int: zero based index of the row.
   * @param row tr-element: the row.
   * @param checkbox input-element: selector in the row.
   */
  selectRow(selected: HTMLInputElement, rowIndex: number, row: HTMLTableRowElement, checkbox: HTMLInputElement) {
    selected.value = selected.value + rowIndex + ",";
    row.classList.add("tobago-sheet-row-markup-selected");
    row.classList.add("table-info");
    checkbox.checked = true;
    setTimeout(function () {
      checkbox.checked = true;
    }, 0);
  };

  /**
   * @param selected input-element type=hidden: Hidden field with the selection state information
   * @param rowIndex int: zero based index of the row.
   * @param row tr-element: the row.
   * @param checkbox input-element: selector in the row.
   */
  deselectRow(selected: HTMLInputElement, rowIndex: number, row: HTMLTableRowElement, checkbox: HTMLInputElement) {
    selected.value = selected.value.replace(new RegExp("," + rowIndex + ","), ",");
    row.classList.remove("tobago-sheet-row-markup-selected");
    row.classList.remove("table-info");
    checkbox.checked = false;
    // XXX check if this is still needed... Async because of TOBAGO-1312
    setTimeout(function () {
      checkbox.checked = false;
    }, 0);
  };

  static isInputElement = function (element: HTMLElement) {
    return ["INPUT", "TEXTAREA", "SELECT", "A", "BUTTON"].indexOf(element.tagName) > -1;
  };

}

Listener.register(Sheet.init, Phase.DOCUMENT_READY);
Listener.register(Sheet.init, Phase.AFTER_UPDATE);
