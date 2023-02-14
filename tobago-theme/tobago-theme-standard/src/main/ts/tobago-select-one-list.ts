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
    this.selectField.addEventListener("keydown", this.keydownEvent.bind(this));
    if (this.filter) {
      this.filterInput.addEventListener("input", this.clearSpan.bind(this));
    }

    this.sync();
  }

  protected globalClickEvent(event: MouseEvent): void {
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
        this.spanText = this.hiddenSelect.value;
        break;
      case Key.BACKSPACE:
        if (this.filterInput.value.length === 0) {
          this.filterInput.dispatchEvent(new Event("input"));
        }
        break;
    }
  }

  private clearSpan(): void {
    this.spanText = null;
  }

  protected select(row: HTMLTableRowElement): void {
    const itemValue = row.dataset.tobagoValue;
    const option: HTMLOptionElement = this.hiddenSelect.querySelector(`[value="${itemValue}"]`);
    option.selected = true;
    this.filterInput.value = null;
    this.sync();
  }

  private sync() {
    this.rows.forEach((row) => {
      if (row.dataset.tobagoValue === this.hiddenSelect.value) {
        this.spanText = this.hiddenSelect.value;
        row.classList.add(Css.TABLE_PRIMARY); // highlight list row
      } else {
        row.classList.remove(Css.TABLE_PRIMARY); // remove highlight list row
      }
    });
  }

  protected leaveComponent(): void {
    this.focused = false;
    this.filterInput.value = null;
    this.filterInput.dispatchEvent(new Event("input"));
    this.spanText = this.hiddenSelect.value;
    this.hideDropdown();
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-select-one-list") == null) {
    window.customElements.define("tobago-select-one-list", SelectOneList);
  }
});
