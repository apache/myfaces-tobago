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

export class SelectListBase extends HTMLElement {
  private popper: Instance;

  get disabled(): boolean {
    return this.classList.contains(Css.TOBAGO_DISABLED);
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

  get filter(): string {
    return this.getAttribute("filter");
  }

  get filterEnabled(): boolean {
    return this.filter?.length > 0;
  }

  get filterInput(): HTMLInputElement {
    return this.querySelector(".tobago-filter");
  }

  get dropdownMenu(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(`.dropdown-menu[name='${this.id}']`);
  }

  get menuStore(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(".tobago-page-menuStore");
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

  connectedCallback(): void {
    if (this.dropdownMenu) {
      this.popper = createPopper(this.selectField, this.dropdownMenu, {});
      window.addEventListener("resize", this.resizeEvent.bind(this));
    }
  }

  protected focusEvent(): void {
    if (!this.hiddenSelect.disabled) {
      if (!this.classList.contains(Css.TOBAGO_FOCUS)) {
        this.setFocus(true);
      }
    }
  }

  protected setFocus(focus: boolean): void {
    if (focus) {
      this.classList.add(Css.TOBAGO_FOCUS);
    } else {
      this.classList.remove(Css.TOBAGO_FOCUS);
    }
  }

  protected preselectNextTableRow(): void {
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

  protected preselectPreviousTableRow(): void {
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

  protected removePreselection(): void {
    this.preselectedRow?.classList.remove(Css.TOBAGO_PRESELECT);
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

  private preselect(row: HTMLTableRowElement): void {
    row.classList.add(Css.TOBAGO_PRESELECT);
    if (!this.dropdownMenu) {
      row.scrollIntoView({block: "center"});
    }

    this.filterInput.disabled = false;
    this.filterInput.focus({preventScroll: true});
  }

  private preselectIndex(rows: NodeListOf<HTMLTableRowElement>): number {
    for (let i = 0; i < rows.length; i++) {
      if (rows.item(i).classList.contains(Css.TOBAGO_PRESELECT)) {
        return i;
      }
    }
    return -1;
  }

  private resizeEvent(event: UIEvent): void {
    this.updateDropdownMenuWidth();
  }

  private updateDropdownMenuWidth(): void {
    if (this.dropdownMenu) {
      this.dropdownMenu.style.width = `${this.selectField.offsetWidth}px`;
    }
  }
}
