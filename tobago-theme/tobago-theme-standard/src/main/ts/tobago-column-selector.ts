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

import {Sheet} from "./tobago-sheet";
import {Selectable} from "./tobago-selectable";

export class ColumnSelector {
  private sheet: Sheet;

  constructor(sheet: Sheet) {
    this.sheet = sheet;

    if (this.headerElement.type === "checkbox") {
      this.headerElement.addEventListener("click", this.initSelectAllCheckbox.bind(this));
      this.syncHeaderElementDisabledState();
    }
  }

  private initSelectAllCheckbox(event: MouseEvent): void {
    const selected = this.sheet.selected;

    if (this.sheet.lazy || this.sheet.rows === 0) {
      if (selected.size === this.sheet.rowCount) {
        selected.clear();
      } else {
        for (let i = 0; i < this.sheet.rowCount; i++) {
          selected.add(i);
        }
      }
    } else {
      const rowIndexes: number[] = [];
      this.sheet.rowElements.forEach((rowElement) => {
        if (rowElement.hasAttribute("row-index")) {
          if (!this.rowElement(rowElement).disabled) {
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

    this.sheet.selected = selected;
  }

  public syncHeaderElementDisabledState(): void {
    if (this.headerElement.type === "checkbox") {
      if (this.sheet.lazy) {
        this.headerElement.disabled = false;
      } else {
        this.headerElement.disabled = this.getRowElements(true, null).length === 0;
      }
    }
  }

  public syncHeaderElementCheckedState(givenSelected?: Set<number>): void {
    if (this.headerElement.type === "checkbox") {
      const selected = givenSelected ? givenSelected : this.sheet.selected;

      if (this.sheet.lazy || this.sheet.rows === 0) {
        this.headerElement.checked = selected.size === this.sheet.rowCount;
      } else {
        const numOfEnabledRowElements = this.getRowElements(true, null).length;

        if (numOfEnabledRowElements === 0) {
          const numOfDisabledCheckedRowElements = this.getRowElements(false, true).length;
          this.headerElement.checked = numOfDisabledCheckedRowElements === this.sheet.rows;
        } else {
          const numOfEnabledCheckedRowElements = this.getRowElements(true, true).length;
          this.headerElement.checked = numOfEnabledCheckedRowElements === numOfEnabledRowElements;
        }
      }
    }
  }

  public get headerElement(): HTMLInputElement {
    return this.sheet.querySelector("thead input.tobago-selected[name='" + this.sheet.id + "::columnSelector']");
  }

  public get selectable(): Selectable {
    return Selectable[this.headerElement.dataset.tobagoSelectionMode];
  }

  private getRowElements(enabled: boolean, checked?: boolean): NodeListOf<HTMLInputElement> {
    return this.sheet.tableBody.querySelectorAll(
        "tr[row-index] td.tobago-column-selector input.tobago-selected"
        + (enabled ? ":not(:disabled)" : ":disabled")
        + (checked ? ":checked" : checked === false ? ":not(:checked)" : "")
    );
  }

  public rowElement(element: HTMLTableRowElement): HTMLInputElement {
    return element.querySelector("td.tobago-column-selector > input.tobago-selected");
  }
}
