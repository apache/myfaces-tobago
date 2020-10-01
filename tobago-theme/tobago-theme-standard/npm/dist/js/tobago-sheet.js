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
import { DomUtils } from "./tobago-utils";
import { Page } from "./tobago-page";
export class Sheet extends HTMLElement {
    constructor() {
        super();
    }
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
    }
    static isInputElement(element) {
        return ["INPUT", "TEXTAREA", "SELECT", "A", "BUTTON"].indexOf(element.tagName) > -1;
    }
    static getRowTemplate(columns, rowIndex) {
        return `<tr row-index="${rowIndex}" class="tobago-sheet-row" dummy="dummy">
<td class="tobago-sheet-cell" colspan="${columns}"> </td>
</tr>`;
    }
    connectedCallback() {
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
            console.assert(headerCols.length - 1 === bodyCols.length, "header and body column number doesn't match: %d != %d ", headerCols.length - 1, bodyCols.length);
            let sumRelative = 0; // tbd: is this needed?
            let widthRelative = bodyTable.offsetWidth;
            for (let i = 0; i < tokens.length; i++) {
                if (columnRendered[i]) {
                    if (typeof tokens[i] === "number") {
                        sumRelative += tokens[i];
                    }
                    else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
                        const intValue = parseInt(tokens[i].measure);
                        if (tokens[i].measure.lastIndexOf("px") > 0) {
                            widthRelative -= intValue;
                        }
                        else if (tokens[i].measure.lastIndexOf("%") > 0) {
                            widthRelative -= bodyTable.offsetWidth * intValue / 100;
                        }
                    }
                    else {
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
                    }
                    else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
                        const intValue = parseInt(tokens[i].measure);
                        if (tokens[i].measure.lastIndexOf("px") > 0) {
                            colWidth = intValue;
                        }
                        else if (tokens[i].measure.lastIndexOf("%") > 0) {
                            colWidth = bodyTable.offsetWidth * intValue / 100;
                        }
                    }
                    else {
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
        for (const resizeElement of this.querySelectorAll(".tobago-sheet-headerResize")) {
            resizeElement.addEventListener("click", function () {
                return false;
            });
            resizeElement.addEventListener("mousedown", this.mousedown.bind(this));
        }
        // scrolling -------------------------------------------------------------------------------------------------- //
        const sheetBody = this.getBody();
        // restore scroll position
        const value = JSON.parse(this.getHiddenScrollPosition().getAttribute("value"));
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
        for (const checkbox of this.querySelectorAll(".tobago-sheet-cell > input.tobago-sheet-columnSelector")) {
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
            let current = tableBody.rows[0]; // current row in this algorithm, begin with first
            // the algorithm goes straight through all rows, not selectors, because of performance
            for (let i = 0; i < rowCount; i++) {
                if (current) {
                    const rowIndex = Number(current.getAttribute("row-index"));
                    if (i < rowIndex) {
                        const template = Sheet.getRowTemplate(columns, i);
                        current.insertAdjacentHTML("beforebegin", template);
                    }
                    else if (i === rowIndex) {
                        current = current.nextElementSibling;
                        // } else { TBD: I think this is not possible
                        //   const template = Sheet.getRowTemplate(columns, i);
                        //   current.insertAdjacentHTML("afterend", template);
                        //   current = current.nextElementSibling as HTMLTableRowElement;
                    }
                }
                else {
                    const template = Sheet.getRowTemplate(columns, i);
                    tableBody.insertAdjacentHTML("beforeend", template);
                }
            }
            sheetBody.addEventListener("scroll", this.lazyCheck.bind(this));
            // initial
            this.lazyCheck();
        }
        // ---------------------------------------------------------------------------------------- //
        for (const checkbox of this.querySelectorAll(".tobago-sheet-header .tobago-sheet-columnSelector")) {
            checkbox.addEventListener("click", this.clickOnCheckbox.bind(this));
        }
        // init paging by pages ---------------------------------------------------------------------------------------- //
        for (const pagingText of this.querySelectorAll(".tobago-sheet-pagingText")) {
            pagingText.addEventListener("click", this.clickOnPaging.bind(this));
            const pagingInput = pagingText.querySelector("input.tobago-sheet-pagingInput");
            pagingInput.addEventListener("blur", this.blurPaging.bind(this));
            pagingInput.addEventListener("keydown", function (event) {
                if (event.keyCode === 13) {
                    event.stopPropagation();
                    event.preventDefault();
                    event.currentTarget.dispatchEvent(new Event("blur"));
                }
            });
        }
    }
    // attribute getter + setter ---------------------------------------------------------- //
    get lazyActive() {
        return this.hasAttribute("lazy-active");
    }
    set lazyActive(update) {
        if (update) {
            this.setAttribute("lazy-active", "");
        }
        else {
            this.removeAttribute("lazy-active");
        }
    }
    get lazy() {
        return this.hasAttribute("lazy");
    }
    set lazy(update) {
        if (update) {
            this.setAttribute("lazy", "");
        }
        else {
            this.removeAttribute("lazy");
        }
    }
    get lazyUpdate() {
        return this.hasAttribute("lazy-update");
    }
    get rows() {
        return parseInt(this.getAttribute("rows"));
    }
    get rowCount() {
        return parseInt(this.getAttribute("row-count"));
    }
    get tableBodyDiv() {
        return this.querySelector(".tobago-sheet-body");
    }
    get tableBody() {
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
    lazyCheck(event) {
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
            const rootNode = this.getRootNode();
            const input = rootNode.getElementById(this.id + ":pageActionlazy");
            input.value = String(next);
            this.reloadWithAction(input);
        }
    }
    nextLazyLoad() {
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
            }
            else {
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
    isRowAboveVisibleArea(tr) {
        const sheetBody = this.tableBodyDiv;
        const viewStart = sheetBody.scrollTop;
        const trEnd = tr.offsetTop + tr.clientHeight;
        return trEnd < viewStart;
    }
    isRowDummy(tr) {
        return tr.hasAttribute("dummy");
    }
    lazyResponse(event) {
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
        }
        else if (event.status === "success") {
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
    lazyError(data) {
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
    reloadWithAction(source) {
        console.debug("reload sheet with action '" + source.id + "'"); // @DEV_ONLY
        const executeIds = this.id;
        const renderIds = this.id;
        const lazy = this.lazy;
        jsf.ajax.request(source.id, null, {
            "javax.faces.behavior.event": "reload",
            execute: executeIds,
            render: renderIds,
            onevent: lazy ? this.lazyResponse.bind(this) : undefined,
            onerror: lazy ? this.lazyError.bind(this) : undefined
        });
    }
    loadColumnWidths() {
        const hidden = document.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "widths");
        if (hidden) {
            return JSON.parse(hidden.getAttribute("value"));
        }
        else {
            return undefined;
        }
    }
    saveColumnWidths(widths) {
        const hidden = document.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "widths");
        if (hidden) {
            hidden.setAttribute("value", JSON.stringify(widths));
        }
        else {
            console.warn("ignored, should not be called, id='" + this.id + "'");
        }
    }
    isColumnRendered() {
        const hidden = document.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "rendered");
        return JSON.parse(hidden.getAttribute("value"));
    }
    addHeaderFillerWidth() {
        const last = document.getElementById(this.id).querySelector(".tobago-sheet-headerTable col:last-child");
        if (last) {
            last.setAttribute("width", String(Sheet.SCROLL_BAR_SIZE));
        }
    }
    mousedown(event) {
        Page.page().dataset.SheetMousedownData = this.id;
        // begin resizing
        console.debug("down");
        const resizeElement = event.currentTarget;
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
    mousemove(event) {
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
    mouseup(event) {
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
        const widths = [];
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
            }
            else if (columnWidths !== undefined && columnWidths.length >= i) {
                widths[i] = columnWidths[i];
            }
            else {
                if (typeof tokens[i] === "number") {
                    widths[i] = 100;
                }
                else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
                    const intValue = parseInt(tokens[i].measure);
                    if (tokens[i].measure.lastIndexOf("px") > 0) {
                        widths[i] = intValue;
                    }
                    else if (tokens[i].measure.lastIndexOf("%") > 0) {
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
        const sheetBody = event.currentTarget;
        this.syncScrolling();
        // store the position in a hidden field
        const hidden = this.getHiddenScrollPosition();
        hidden.setAttribute("value", JSON.stringify([Math.round(sheetBody.scrollLeft), Math.round(sheetBody.scrollTop)]));
    }
    mousedownOnRow(event) {
        console.debug("mousedownOnRow");
        this.mousedownOnRowData = {
            x: event.clientX,
            y: event.clientY
        };
    }
    clickOnCheckbox(event) {
        const checkbox = event.currentTarget;
        if (checkbox.checked) {
            this.selectAll();
        }
        else {
            this.deselectAll();
        }
    }
    clickOnRow(event) {
        const row = event.currentTarget;
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
                }
                else {
                    this.selectRange(rows, row.sectionRowIndex, lastClickedRowIndex, true, false);
                }
            }
            else if (selectionMode !== "singleOrNone" || !this.isRowSelected(row)) {
                this.toggleSelection(row, selector);
            }
        }
    }
    clickOnPaging(event) {
        const element = event.currentTarget;
        const output = element.querySelector(".tobago-sheet-pagingOutput");
        output.style.display = "none";
        const input = element.querySelector(".tobago-sheet-pagingInput");
        input.style.display = "initial";
        input.focus();
        input.select();
    }
    blurPaging(event) {
        const input = event.currentTarget;
        const output = input.parentElement.querySelector(".tobago-sheet-pagingOutput");
        if (output.innerHTML !== input.value) {
            console.debug("Reloading sheet '" + this.id + "' old value='" + output.innerHTML + "' new value='" + input.value + "'");
            output.innerHTML = input.value;
            jsf.ajax.request(input.id, null, {
                "javax.faces.behavior.event": "reload",
                execute: this.id,
                render: this.id
            });
        }
        else {
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
    getHeader() {
        return this.querySelector("tobago-sheet>header");
    }
    getHeaderTable() {
        return this.querySelector("tobago-sheet>header>table");
    }
    getHeaderCols() {
        return this.querySelectorAll("tobago-sheet>header>table>colgroup>col");
    }
    getBody() {
        return this.querySelector("tobago-sheet>.tobago-sheet-body");
    }
    getBodyTable() {
        return this.querySelector("tobago-sheet>.tobago-sheet-body>.tobago-sheet-bodyTable");
    }
    getBodyCols() {
        return this.querySelectorAll("tobago-sheet>.tobago-sheet-body>.tobago-sheet-bodyTable>colgroup>col");
    }
    getHiddenSelected() {
        const rootNode = this.getRootNode();
        return rootNode.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "selected");
    }
    getHiddenScrollPosition() {
        const rootNode = this.getRootNode();
        return rootNode.getElementById(this.id + DomUtils.SUB_COMPONENT_SEP + "scrollPosition");
    }
    getHiddenExpanded() {
        return this.querySelector(DomUtils.escapeClientId(this.id + DomUtils.SUB_COMPONENT_SEP + "expanded"));
    }
    /**
     * Get the element, which indicates the selection
     */
    getSelectorCheckbox(row) {
        return row.querySelector("tr>td>input.tobago-sheet-columnSelector");
    }
    getRowElements() {
        return this.getBodyTable().querySelectorAll("tbody>tr");
    }
    getFirst() {
        return parseInt(this.dataset.tobagoFirst);
    }
    isRowSelected(row) {
        return this.isSelected(parseInt(row.dataset.tobagoRowIndex));
    }
    isSelected(rowIndex) {
        const value = JSON.parse(this.getHiddenSelected().value);
        return value.indexOf(rowIndex) > -1;
    }
    resetSelected() {
        this.getHiddenSelected().value = JSON.stringify([]);
    }
    toggleSelection(row, checkbox) {
        this.dataset.tobagoLastClickedRowIndex = String(row.sectionRowIndex);
        if (checkbox && !checkbox.disabled) {
            const selected = this.getHiddenSelected();
            const rowIndex = Number(row.getAttribute("row-index"));
            if (this.isSelected(rowIndex)) {
                this.deselectRow(selected, rowIndex, row, checkbox);
            }
            else {
                this.selectRow(selected, rowIndex, row, checkbox);
            }
        }
    }
    selectAll() {
        const rows = this.getRowElements();
        this.selectRange(rows, 0, rows.length - 1, true, false);
    }
    deselectAll() {
        const rows = this.getRowElements();
        this.selectRange(rows, 0, rows.length - 1, false, true);
    }
    toggleAll() {
        const rows = this.getRowElements();
        this.selectRange(rows, 0, rows.length - 1, true, true);
    }
    selectRange(rows, first, last, selectDeselected, deselectSelected) {
        const selected = this.getHiddenSelected();
        const value = new Set(JSON.parse(selected.value));
        for (let i = first; i <= last; i++) {
            const row = rows.item(i);
            const checkbox = this.getSelectorCheckbox(row);
            if (checkbox && !checkbox.disabled) {
                const rowIndex = Number(row.getAttribute("row-index"));
                const on = value.has(rowIndex);
                if (selectDeselected && !on) {
                    this.selectRow(selected, rowIndex, row, checkbox);
                }
                else if (deselectSelected && on) {
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
    selectRow(selected, rowIndex, row, checkbox) {
        const selectedSet = new Set(JSON.parse(selected.value));
        selected.value = JSON.stringify(Array.from(selectedSet.add(rowIndex)));
        row.classList.add("tobago-sheet-row-markup-selected");
        row.classList.add("table-info");
        checkbox.checked = true;
        setTimeout(function () {
            checkbox.checked = true;
        }, 0);
    }
    /**
     * @param selected input-element type=hidden: Hidden field with the selection state information
     * @param rowIndex int: zero based index of the row.
     * @param row tr-element: the row.
     * @param checkbox input-element: selector in the row.
     */
    deselectRow(selected, rowIndex, row, checkbox) {
        const selectedSet = new Set(JSON.parse(selected.value));
        selectedSet.delete(rowIndex);
        selected.value = JSON.stringify(Array.from(selectedSet));
        row.classList.remove("tobago-sheet-row-markup-selected");
        row.classList.remove("table-info");
        checkbox.checked = false;
        // XXX check if this is still needed... Async because of TOBAGO-1312
        setTimeout(function () {
            checkbox.checked = false;
        }, 0);
    }
}
Sheet.SCROLL_BAR_SIZE = Sheet.getScrollBarSize();
document.addEventListener("DOMContentLoaded", function (event) {
    window.customElements.define("tobago-sheet", Sheet);
});
//# sourceMappingURL=tobago-sheet.js.map