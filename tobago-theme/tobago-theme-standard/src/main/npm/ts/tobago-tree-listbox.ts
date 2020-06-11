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

class TreeListbox extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    this.applySelected();

    for (const listbox of this.listboxes) {
      if (!listbox.disabled) {
        listbox.addEventListener("change", this.select.bind(this));
      }
    }
  }

  private select(event: Event): void {
    const listbox = event.currentTarget as HTMLSelectElement;
    this.unselectDescendants(listbox);
    this.setSelected();
    this.applySelected();
  }

  private unselectDescendants(select: HTMLSelectElement): void {
    let unselect: boolean = false;
    for (const listbox of this.listboxes) {
      if (unselect) {
        const checkedOption = listbox.querySelector<HTMLOptionElement>("option:checked");
        if (checkedOption) {
          checkedOption.selected = false;
        }
      } else if (listbox.id === select.id) {
        unselect = true;
      }
    }
  }

  private setSelected(): void {
    const selected: number[] = [];
    for (const level of this.levelElements) {
      const checkedOption: HTMLOptionElement = level
          .querySelector(".tobago-treeListbox-select:not(.d-none) option:checked");
      if (checkedOption) {
        selected.push(checkedOption.index);
      }
    }
    this.hiddenInput.value = JSON.stringify(selected);
  }

  private applySelected(): void {
    const selected: number[] = JSON.parse(this.hiddenInput.value);
    let nextActiveSelectId: string = this.querySelector(".tobago-treeListbox-select").id;

    const levelElements = this.levelElements;
    for (let i = 0; i < levelElements.length; i++) {
      const level = levelElements[i];

      for (const select of this.getSelectElements(level)) {
        if (select.id === nextActiveSelectId || (nextActiveSelectId === null && select.disabled)) {
          const check: number = i < selected.length ? selected[i] : null;
          this.show(select, check);
          nextActiveSelectId = this.getNextActiveSelectId(select, check);
        } else {
          this.hide(select);
        }
      }
    }
  }

  private getSelectElements(level: HTMLDivElement): NodeListOf<HTMLSelectElement> {
    return level.querySelectorAll<HTMLSelectElement>(".tobago-treeListbox-select");
  }

  private getNextActiveSelectId(select: HTMLSelectElement, check: number): string {
    if (check !== null) {
      const option = select.querySelectorAll("option")[check];
      return option.id + DomUtils.SUB_COMPONENT_SEP + "parent";
    } else {
      return null;
    }
  }

  private show(select: HTMLSelectElement, check: number): void {
    select.classList.remove("d-none");
    const checkedOption = select.querySelector<HTMLOptionElement>("option:checked");
    if (checkedOption && checkedOption.index !== check) {
      checkedOption.selected = false;
    }
    if (check !== null && checkedOption.index !== check) {
      select.querySelectorAll("option")[check].selected = true;
    }
  }

  private hide(select: HTMLSelectElement): void {
    select.classList.add("d-none");
    const checkedOption = select.querySelector<HTMLOptionElement>("option:checked");
    if (checkedOption) {
      checkedOption.selected = false;
    }
  }

  private get listboxes(): NodeListOf<HTMLSelectElement> {
    return this.querySelectorAll(".tobago-treeListbox-select");
  }

  private get levelElements(): NodeListOf<HTMLDivElement> {
    return this.querySelectorAll(".tobago-treeListbox-level");
  }

  private get hiddenInput(): HTMLInputElement {
    return this.querySelector(DomUtils.escapeClientId(this.id + DomUtils.SUB_COMPONENT_SEP + "selected"));
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-tree-listbox", TreeListbox);
});
