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

import {createPopper, Instance} from "@popperjs/core";
import {Css} from "./tobago-css";
import {TobagoFilterRegistry} from "./tobago-filter-registry";
import {Key} from "./tobago-key";

export abstract class SelectListBase extends HTMLElement {
  private popper: Instance;

  get disabled(): boolean {
    return this.classList.contains(Css.TOBAGO_DISABLED);
  }

  get focused(): boolean {
    return this.classList.contains(Css.TOBAGO_FOCUS);
  }

  set focused(focused: boolean) {
    if (focused) {
      this.classList.add(Css.TOBAGO_FOCUS);
    } else {
      this.classList.remove(Css.TOBAGO_FOCUS);
    }
  }

  get filter(): string {
    return this.getAttribute("filter");
  }

  get hiddenSelect(): HTMLSelectElement {
    return this.querySelector("select");
  }

  get hiddenOptions(): NodeListOf<HTMLOptionElement> {
    return this.hiddenSelect.querySelectorAll<HTMLOptionElement>("option");
  }

  get selectField(): HTMLDivElement {
    return this.querySelector(".tobago-select-field");
  }

  get filterInput(): HTMLInputElement {
    return this.querySelector(".tobago-filter");
  }

  get dropdownMenu(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(`.dropdown-menu[name='${this.id}']`);
  }

  get tbody(): HTMLElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(`.tobago-options[name='${this.id}'] tbody`);
  }

  get rows(): NodeListOf<HTMLTableRowElement> {
    return this.tbody.querySelectorAll<HTMLTableRowElement>("tr");
  }

  get enabledRows(): NodeListOf<HTMLTableRowElement> {
    return this.tbody.querySelectorAll<HTMLTableRowElement>("tr:not(." + Css.D_NONE + ")");
  }

  get preselectedRow(): HTMLTableRowElement {
    return this.tbody.querySelector<HTMLTableRowElement>("." + Css.TOBAGO_PRESELECT);
  }

  get menuStore(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(".tobago-page-menuStore");
  }

  connectedCallback(): void {
    if (this.dropdownMenu) {
      this.popper = createPopper(this.selectField, this.dropdownMenu, {});
      window.addEventListener("resize", () => this.updateDropdownMenuWidth());
    }
    document.addEventListener("click", this.globalClickEvent.bind(this));
    this.selectField.addEventListener("keydown", this.keydownEventBase.bind(this));
    this.filterInput.addEventListener("focus", this.focusEvent.bind(this));
    this.filterInput.addEventListener("blur", this.blurEvent.bind(this));
    if (this.filter) {
      this.filterInput.addEventListener("input", this.filterEvent.bind(this));
    }
    this.tbody.addEventListener("click", this.tbodyClickEvent.bind(this));

    // handle autofocus; trigger focus event
    if (document.activeElement.id === this.filterInput.id) {
      this.focusEvent();
    }
  }

  protected abstract globalClickEvent(event: MouseEvent): void;

  private keydownEventBase(event: KeyboardEvent) {
    switch (event.key) {
      case Key.ESCAPE:
        this.hideDropdown();
        this.removePreselection();
        break;
      case Key.ARROW_DOWN:
        event.preventDefault();
        this.showDropdown();
        this.preselectNextRow();
        break;
      case Key.ARROW_UP:
        event.preventDefault();
        this.showDropdown();
        this.preselectPreviousRow();
        break;
      case Key.ENTER:
      case Key.SPACE:
        if (this.preselectedRow) {
          event.preventDefault();
          const row = this.tbody.querySelector<HTMLTableRowElement>("." + Css.TOBAGO_PRESELECT);
          this.select(row);
        } else if (document.activeElement.id === this.filterInput.id) {
          this.showDropdown();
        }
        break;
      case Key.TAB:
        this.removePreselection();
        break;
    }
  }

  protected focusEvent(): void {
    if (!this.disabled) {
      this.focused = true;
    }
  }

  protected blurEvent(event: FocusEvent): void {
    if (event.relatedTarget !== null) {
      //relatedTarget is the new focused element; null indicate a mouseclick or an inactive browser window
      if (!this.isPartOfSelectField(event.relatedTarget as Element)
          && !this.isPartOfTobagoOptions(event.relatedTarget as Element)) {
        this.leaveComponent();
      }
    }
  }

  private filterEvent(event: Event): void {
    const input = event.currentTarget as HTMLInputElement;
    const searchString = input.value;
    this.showDropdown();
    const filterFunction = TobagoFilterRegistry.get(this.filter);
    // XXX todo: if filterFunction not found?
    if (filterFunction != null) {
      this.querySelectorAll("tr").forEach(row => {
        const itemValue = row.dataset.tobagoValue;
        if (filterFunction(itemValue, searchString)) {
          row.classList.remove(Css.D_NONE);
        } else {
          row.classList.add(Css.D_NONE);
          row.classList.remove(Css.TOBAGO_PRESELECT);
        }
      });
    }
  }

  private tbodyClickEvent(event: MouseEvent): void {
    const target = <HTMLElement>event.target;
    const row = target.closest("tr");
    this.select(row);
  }

  protected abstract leaveComponent(): void;

  protected abstract select(row: HTMLTableRowElement): void;

  protected preselectNextRow(): void {
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

  protected preselectPreviousRow(): void {
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
    if (!this.dropdownMenu) {
      row.scrollIntoView({block: "center"});
    }

    this.filterInput.disabled = false;
    this.filterInput.focus({preventScroll: true});
  }

  protected removePreselection(): void {
    this.preselectedRow?.classList.remove(Css.TOBAGO_PRESELECT);
  }

  protected showDropdown(): void {
    if (this.dropdownMenu && !this.dropdownMenu.classList.contains(Css.SHOW)) {
      this.selectField.classList.add(Css.SHOW);
      this.selectField.ariaExpanded = "true";
      this.dropdownMenu.classList.add(Css.SHOW);
      this.updateDropdownMenuWidth();
      this.popper.update();
    }
  }

  protected hideDropdown(): void {
    if (this.dropdownMenu?.classList.contains(Css.SHOW)) {
      this.selectField.classList.remove(Css.SHOW);
      this.selectField.ariaExpanded = "false";
      this.dropdownMenu.classList.remove(Css.SHOW);
    }
  }

  private updateDropdownMenuWidth(): void {
    if (this.dropdownMenu) {
      this.dropdownMenu.style.width = `${this.selectField.offsetWidth}px`;
    }
  }

  protected isPartOfSelectField(element: Element): boolean {
    if (element) {
      if (this.selectField.id === element.id) {
        return true;
      } else {
        return element.parentElement ? this.isPartOfSelectField(element.parentElement) : false;
      }
    } else {
      return false;
    }
  }

  protected isPartOfTobagoOptions(element: Element): boolean {
    if (element) {
      if (element.classList.contains(Css.TOBAGO_OPTIONS)
          && this.id === element.getAttribute("name")) {
        return true;
      } else {
        return element.parentElement ? this.isPartOfTobagoOptions(element.parentElement) : false;
      }
    } else {
      return false;
    }
  }
}
