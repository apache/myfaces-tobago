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

import {Focus} from "./tobago-focus";

interface SelectManyShuttleDetail {
  added: string[];
  removed: string[];
  unselected: string[];
  selected: string[];
}

class SelectManyShuttle extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    /*
     * Event bubbling must be stopped, because a change event is dispatched if an option tag is clicked.
     * But the change event should only be executed if an entry is moved to/from the right side.
     */
    this.unselectedSelect.onchange = (event) => event.stopPropagation();
    this.selectedSelect.onchange = (event) => event.stopPropagation();

    this.unselectedSelect.addEventListener("focus", Focus.setLastFocusId);
    this.selectedSelect.addEventListener("focus", Focus.setLastFocusId);

    if (this.unselectedSelect.getAttribute("readonly") !== "readonly" && !this.unselectedSelect.disabled) {
      this.unselectedSelect.addEventListener("dblclick", this.addSelectedItems.bind(this));
    }

    if (this.selectedSelect.getAttribute("readonly") !== "readonly" && !this.selectedSelect.disabled) {
      this.selectedSelect.addEventListener("dblclick", this.removeSelectedItems.bind(this));
    }

    if (!this.addAllButton.disabled) {
      this.addAllButton.addEventListener("click", this.addAllItems.bind(this));
    }

    if (!this.addButton.disabled) {
      this.addButton.addEventListener("click", this.addSelectedItems.bind(this));
    }

    if (!this.removeButton.disabled) {
      this.removeButton.addEventListener("click", this.removeSelectedItems.bind(this));
    }

    if (!this.removeAllButton.disabled) {
      this.removeAllButton.addEventListener("click", this.removeAllItems.bind(this));
    }
  }

  private addAllItems(event: MouseEvent): void {
    this.addItems(this.unselectedSelect.querySelectorAll("option:not(:disabled)"));
  }

  private addSelectedItems(event: MouseEvent): void {
    this.addItems(this.unselectedSelect.querySelectorAll("option:checked"));
  }

  private removeSelectedItems(event: MouseEvent): void {
    this.removeItems(this.selectedSelect.querySelectorAll("option:checked"));
  }

  private removeAllItems(event: MouseEvent): void {
    this.removeItems(this.selectedSelect.querySelectorAll("option:not(:disabled)"));
  }

  private addItems(options: NodeListOf<HTMLOptionElement>): void {
    for (const option of options) {
      this.selectedSelect.add(option);
      this.changeHiddenOption(option, true);
    }
    this.fireChangeEvent(options, null);
  }

  private removeItems(options: NodeListOf<HTMLOptionElement>): void {
    for (const option of options) {
      this.unselectedSelect.add(option);
      this.changeHiddenOption(option, false);
    }
    this.fireChangeEvent(null, options);
  }

  private changeHiddenOption(option: HTMLOptionElement, select: boolean): void {
    const hiddenOption: HTMLOptionElement = this.hiddenSelect.querySelector(`option[value='${option.value}']`);
    hiddenOption.selected = select;
  }

  private fireChangeEvent(added: NodeListOf<HTMLOptionElement>, removed: NodeListOf<HTMLOptionElement>): void {
    this.dispatchEvent(new CustomEvent("change", {
      detail: {
        added: added ? Array.from(added).map((option: HTMLOptionElement) => option.value) : null,
        removed: removed ? Array.from(removed).map((option: HTMLOptionElement) => option.value) : null,
        unselected: Array.from(this.hiddenSelect.options)
            .filter((option: HTMLOptionElement) => !option.selected)
            .map((option: HTMLOptionElement) => option.value),
        selected: Array.from(this.hiddenSelect.options)
            .filter((option: HTMLOptionElement) => option.selected)
            .map((option: HTMLOptionElement) => option.value)
      }
    }));
  }

  get unselectedSelect(): HTMLSelectElement {
    return this.querySelector(".tobago-unselected");
  }

  get selectedSelect(): HTMLSelectElement {
    return this.querySelector(".tobago-selected");
  }

  get hiddenSelect(): HTMLSelectElement {
    return this.querySelector("select.d-none");
  }

  get addAllButton(): HTMLButtonElement {
    return this.querySelector(".btn-group-vertical button:nth-child(1)");
  }

  get addButton(): HTMLButtonElement {
    return this.querySelector(".btn-group-vertical button:nth-child(2)");
  }

  get removeButton(): HTMLButtonElement {
    return this.querySelector(".btn-group-vertical button:nth-child(3)");
  }

  get removeAllButton(): HTMLButtonElement {
    return this.querySelector(".btn-group-vertical button:nth-child(4)");
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-select-many-shuttle") == null) {
    window.customElements.define("tobago-select-many-shuttle", SelectManyShuttle);
  }
});
