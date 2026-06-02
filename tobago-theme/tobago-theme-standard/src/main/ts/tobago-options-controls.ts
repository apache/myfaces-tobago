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

import {Css} from "./tobago-css";
import {EventListenerStore} from "./tobago-event-listener-store";

export class OptionsControls {
  listeners: EventListenerStore = new EventListenerStore();
  private optionsElement: HTMLElement;
  private select: (row: HTMLTableRowElement) => void;

  constructor(optionsElement: HTMLElement, select: (row: HTMLTableRowElement) => void) {
    this.optionsElement = optionsElement;
    this.select = select;
    this.listeners.add(this.tbody, "click", this.tbodyClickEvent.bind(this));
  }

  /**
   * Call this in the disconnectedCallback() method.
   */
  disconnect(): void {
    this.listeners.disconnect();
  }

  private tbodyClickEvent(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    const row = target.closest("tr");
    this.select(row);
  }

  renderRows(items: string[]): void {
    const dropdownItems: HTMLTableRowElement[] = [];
    if (items) {
      for (const item of items) {
        const tr = document.createElement("tr");
        tr.classList.add(Css.TOBAGO_SELECT_ITEM);
        tr.tabIndex = -1;
        tr.dataset.tobagoValue = item;
        const td = document.createElement("td");
        td.textContent = item;
        tr.appendChild(td);
        dropdownItems.push(tr);
      }
    }
    this.tbody.replaceChildren(...dropdownItems);
  }

  preselectNextRow(): void {
    const rows = this.enabledRows;
    const index = this.preselectIndex(rows);
    if (index >= 0) {
      if (index + 1 < rows.length) {
        rows.item(index).classList.remove(Css.TOBAGO_PRESELECT);
        this.preselect(rows.item(index + 1));
      } else {
        rows.item(rows.length - 1).classList.remove(Css.TOBAGO_PRESELECT);
        this.preselect(rows.item(0));
      }
    } else if (rows.length > 0) {
      this.preselect(rows.item(0));
    }
  }

  preselectPreviousRow(): void {
    const rows = this.enabledRows;
    const index = this.preselectIndex(rows);
    if (index >= 0) {
      if ((index - 1) >= 0) {
        rows.item(index).classList.remove(Css.TOBAGO_PRESELECT);
        this.preselect(rows.item(index - 1));
      } else {
        rows.item(0).classList.remove(Css.TOBAGO_PRESELECT);
        this.preselect(rows.item(rows.length - 1));
      }
    } else if (rows.length > 0) {
      this.preselect(rows.item(rows.length - 1));
    }
  }

  private preselectIndex(rows: NodeListOf<HTMLTableRowElement>): number {
    for (let i = 0; i < rows.length; i++) {
      if (rows.item(i).classList.contains(Css.TOBAGO_PRESELECT)) {
        return i;
      }
    }
    return -1;
  }

  private preselect(row: HTMLTableRowElement): void {
    row.classList.add(Css.TOBAGO_PRESELECT);
    row.focus();
  }

  removePreselection(): void {
    this.preselectedRow?.classList.remove(Css.TOBAGO_PRESELECT);
  }

  /**
   * todo: interface
   * client side filtering
   * @param filterFunction (itemLabel, query) => filterFunction
   * @param query to search for
   */
  filter(filterFunction: Function, query: string): void {
    if (filterFunction != null) {
      this.rows.forEach(row => {
        const itemLabel = row.cells.item(0).textContent;
        if (filterFunction(itemLabel, query)) {
          row.classList.remove(Css.D_NONE);
        } else {
          row.classList.add(Css.D_NONE);
          row.classList.remove(Css.TOBAGO_PRESELECT);
        }
      });
    }
  }

  get tbody(): HTMLTableSectionElement {
    return this.optionsElement.querySelector("tbody");
  }

  get rows(): NodeListOf<HTMLTableRowElement> {
    return this.tbody.querySelectorAll<HTMLTableRowElement>("tr");
  }

  get visibleRows(): NodeListOf<HTMLTableRowElement> {
    return this.tbody.querySelectorAll<HTMLTableRowElement>("tr:not(." + Css.D_NONE + ")");
  }

  private get enabledRows(): NodeListOf<HTMLTableRowElement> {
    return this.tbody.querySelectorAll<HTMLTableRowElement>("tr:not(." + Css.D_NONE + "):not(." + Css.DISABLED + ")");
  }

  get preselectedRow(): HTMLTableRowElement {
    return this.tbody.querySelector<HTMLTableRowElement>("." + Css.TOBAGO_PRESELECT);
  }
}
