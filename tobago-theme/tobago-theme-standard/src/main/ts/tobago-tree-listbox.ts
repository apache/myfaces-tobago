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
    let unselect = false;
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
          .querySelector(".tobago-selected:not(.d-none) option:checked");
      if (checkedOption) {
        selected.push(checkedOption.index);
      }
    }
    this.hiddenInput.value = JSON.stringify(selected);
  }

  private applySelected(): void {
    const selected: number[] = JSON.parse(this.hiddenInput.value) as number[];
    let nextActiveSelect: HTMLSelectElement = this.querySelector(".tobago-selected");

    const levelElements = this.levelElements;
    for (let i = 0; i < levelElements.length; i++) {
      const level = levelElements[i];

      for (const select of this.getSelectElements(level)) {
        if ((nextActiveSelect !== null && select.id === nextActiveSelect.id)
            || (nextActiveSelect === null && select.disabled)) {
          const check: number = i < selected.length ? selected[i] : null;
          this.show(select, check);
          nextActiveSelect = this.getNextActiveSelect(select, check);
        } else {
          this.hide(select);
        }
      }
    }
  }

  private getSelectElements(level: HTMLDivElement): NodeListOf<HTMLSelectElement> {
    return level.querySelectorAll<HTMLSelectElement>(".tobago-selected");
  }

  private getNextActiveSelect(select: HTMLSelectElement, check: number): HTMLSelectElement {
    if (check !== null) {
      const option = select.querySelectorAll("option")[check];
      const rootNode = this.getRootNode() as ShadowRoot | Document;
      return rootNode.getElementById(option.id + "::parent") as HTMLSelectElement;
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
    return this.querySelectorAll(".tobago-selected");
  }

  private get levelElements(): NodeListOf<HTMLDivElement> {
    return this.querySelectorAll(".tobago-level");
  }

  private get hiddenInput(): HTMLInputElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.getElementById(this.id + "::selected") as HTMLInputElement;
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-tree-listbox") == null) {
    window.customElements.define("tobago-tree-listbox", TreeListbox);
  }
});
