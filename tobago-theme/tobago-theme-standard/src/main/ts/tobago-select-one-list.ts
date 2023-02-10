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

import {TobagoFilterRegistry} from "./tobago-filter-registry";
import {SelectListBase} from "./tobago-select-list-base";
import {Key} from "./tobago-key";
import {Css} from "./tobago-css";

class SelectOneList extends SelectListBase {
  constructor() {
    super();
  }

  get spanText(): string {
    return this.selectField.querySelector("span").textContent;
  }

  set spanText(text: string) {
    this.selectField.querySelector("span").textContent = text;
  }

  connectedCallback(): void {
    super.connectedCallback();
    document.addEventListener("click", this.clickEvent.bind(this));
    this.filterInput.addEventListener("focus", this.focusEvent.bind(this));
    this.filterInput.addEventListener("blur", this.blurEvent.bind(this));
    this.selectField.addEventListener("keydown", this.keydownEvent.bind(this));
    this.tbody.addEventListener("click", this.select.bind(this));

    this.sync();

    if (this.filter) {
      this.filterInput.addEventListener("input", this.filterEvent.bind(this));
    }

    if (document.activeElement.id === this.filterInput.id) {
      this.focusEvent();
    }
  }

  select(event: MouseEvent): void {
    const target = <HTMLElement>event.target;
    const row = target.closest("tr");
    this.selectRow(row);
  }

  selectRow(row: HTMLTableRowElement): void {
    const itemValue = row.dataset.tobagoValue;
    const select = this.hiddenSelect;
    const option: HTMLOptionElement = select.querySelector(`[value="${itemValue}"]`);
    option.selected = true;
    this.filterInput.value = null;
    this.sync();
  }

  sync() {
    this.rows.forEach((row) => {
      if (row.dataset.tobagoValue === this.hiddenSelect.value) {
        this.spanText = this.hiddenSelect.value;
        row.classList.add(Css.TABLE_PRIMARY); // highlight list row
      } else {
        row.classList.remove(Css.TABLE_PRIMARY); // remove highlight list row
      }
    });
  }

  filterEvent(event: Event): void {
    const input = event.currentTarget as HTMLInputElement;
    const searchString = input.value;
    if (searchString !== null) {
      this.spanText = null;
      this.showDropdown();
    }
    const filterFunction = TobagoFilterRegistry.get(this.filter);
    // XXX todo: if filterFunction not found?
    if (filterFunction != null) {
      this.querySelectorAll("tr").forEach(row => {
        const itemValue = row.dataset.tobagoValue;
        if (filterFunction(itemValue, searchString)) {
          row.classList.remove(Css.D_NONE);
        } else {
          row.classList.add(Css.D_NONE);
        }
      });
    }
  }

  private clickEvent(event: MouseEvent): void {
    if (!this.disabled) {
      if (this.isPartOfSelectField(event.target as Element) || this.isPartOfTobagoOptions(event.target as Element)) {
        if (!this.filterInput.disabled) {
          this.filterInput.focus();
        }
        this.showDropdown();
      } else {
        this.leaveComponent();
      }
    }
  }

  private keydownEvent(event: KeyboardEvent) {
    switch (event.key) {
      case Key.ESCAPE:
        this.hideDropdown();
        this.removePreselection();
        break;
      case Key.ARROW_DOWN:
        event.preventDefault();
        this.showDropdown();
        this.preselectNextTableRow();
        break;
      case Key.ARROW_UP:
        event.preventDefault();
        this.showDropdown();
        this.preselectPreviousTableRow();
        break;
      case Key.BACKSPACE:
        if (this.filterInput.value.length === 0) {
          this.spanText = null;
          this.filterInput.dispatchEvent(new Event("input"));
        }
        break;
      case Key.ENTER:
      case Key.SPACE:
        if (this.preselectedRow) {
          event.preventDefault();
          const row = this.tbody.querySelector<HTMLTableRowElement>("." + Css.TOBAGO_PRESELECT);
          this.selectRow(row);
        } else if (document.activeElement.id === this.filterInput.id) {
          this.showDropdown();
        }
        break;
    }
  }

  private leaveComponent(): void {
    this.setFocus(false);
    this.filterInput.value = null;
    this.filterInput.dispatchEvent(new Event("input"));
    this.spanText = this.hiddenSelect.value;
    this.hideDropdown();
  }

  private blurEvent(event: FocusEvent): void {
    if (event.relatedTarget !== null) {
      //relatedTarget is the new focused element; null indicate a mouseclick or an inactive browser window
      if (!this.isPartOfSelectField(event.relatedTarget as Element)
          && !this.isPartOfTobagoOptions(event.relatedTarget as Element)) {
        this.leaveComponent();
      }
    }
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-select-one-list") == null) {
    window.customElements.define("tobago-select-one-list", SelectOneList);
  }
});
