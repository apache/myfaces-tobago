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

  connectedCallback(): void {
    super.connectedCallback();
    this.selectField.addEventListener("keydown", this.keydownEvent.bind(this));
    if (this.filter || this.tobagoFilter) {
      this.filterInput.addEventListener("input", this.clearSpan.bind(this));
    }

    this.sync();
  }

  protected globalClickEvent(event: MouseEvent): void {
    if (!this.disabled) {
      if (this.isPartOfSelectField(event.target as Element)) {
        if (!this.filterInput.disabled) {
          this.filterInput.focus();
        }
        this.dropdownMenu?.show();
      } else if (this.isPartOfTobagoOptions(event.target as Element)) {
        if (!this.filterInput.disabled) {
          this.filterInput.focus();
        }
        this.dropdownMenu?.hide();
      } else {
        this.leaveComponent();
      }
    }
  }

  protected labelClickEvent(event: MouseEvent): void {
    event.stopPropagation(); // stop propagation to avoid execution of globalClickEvent()
    if (!this.filterInput.disabled) {
      this.filterInput.focus();
    }
  }

  private keydownEvent(event: KeyboardEvent) {
    switch (event.key) {
      case Key.ESCAPE:
        this.spanText = this.selectedOption.textContent;
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
    option.selected = this.hiddenSelect.required ? true : !option.selected;
    this.filterInput.value = null;
    this.sync();
    this.hiddenSelect.dispatchEvent(new Event("change", {bubbles: true}));
  }

  private sync() {
    this.spanText = this.selectedOption ? this.selectedOption.textContent : "";

    this.rows?.forEach((row) => {
      if (row.dataset.tobagoValue === this.hiddenSelect.value) {
        row.classList.add(Css.TABLE_PRIMARY); // highlight list row
      } else {
        row.classList.remove(Css.TABLE_PRIMARY); // remove highlight list row
      }
    });
  }

  protected leaveComponent(): void {
    this.focused = false;
    if (!this.tobagoFilter || (this.lastSuccessfulSearchQuery && this.lastSuccessfulSearchQuery.length > 0)) {
      this.filterInput.value = null;
      this.doFilter("");
    }
    this.spanText = this.selectedOption ? this.selectedOption.textContent : "";
    this.dropdownMenu?.hide();
  }

  get spanText(): string {
    return this.selectField.querySelector("span").textContent;
  }

  set spanText(text: string) {
    const spanElement = this.selectField.querySelector("span");
    spanElement.textContent = text;
    spanElement.style.marginRight = text ? null : "0"; //null remove inline style, so the margin from the CSS is used
  }

  get selectedOption(): HTMLOptionElement {
    const value = this.hiddenSelect.value;
    return this.hiddenSelect.querySelector(`[value="${value}"]`);
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-select-one-list") == null) {
    window.customElements.define("tobago-select-one-list", SelectOneList);
  }
});
