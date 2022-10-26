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
import {createPopper, Instance} from "@popperjs/core";

class SelectManyList extends HTMLElement {
  private popper: Instance;

  private readonly CssClass = {
    DROPDOWN_MENU: "dropdown-menu",
    SHOW: "show",
    TABLE_ACTIVE: "table-active",
    TABLE_PRIMARY: "table-primary",
    TOBAGO_DISABLED: "tobago-disabled",
    TOBAGO_FOCUS: "tobago-focus",
    TOBAGO_MARK: "tobago-mark",
    TOBAGO_OPTIONS: "tobago-options"
  };

  private readonly Key = {
    ARROW_DOWN: "ArrowDown",
    ARROW_UP: "ArrowUp",
    ENTER: "Enter",
    ESCAPE: "Escape",
    SPACE: " "
  };

  constructor() {
    super();
  }

  get hiddenSelect(): HTMLSelectElement {
    return this.querySelector("select");
  }

  get selectField(): HTMLDivElement {
    return this.querySelector(".tobago-select-field");
  }

  get badgeCloseButtons(): NodeListOf<HTMLButtonElement> {
    return this.selectField.querySelectorAll("button.btn.badge");
  }

  get filter(): string {
    return this.getAttribute("filter");
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

  get enabledRows(): NodeListOf<HTMLTableRowElement> {
    return this.tbody.querySelectorAll<HTMLTableRowElement>("tr:not(.tobago-disabled)");
  }

  get markedRow(): HTMLTableRowElement {
    return this.tbody.querySelector<HTMLTableRowElement>("." + this.CssClass.TOBAGO_MARK);
  }

  connectedCallback(): void {
    if (this.dropdownMenu) {
      this.popper = createPopper(this.selectField, this.dropdownMenu, {});
      window.addEventListener("resize", this.resizeEvent.bind(this));
    }
    document.addEventListener("click", this.clickEvent.bind(this));
    this.filterInput.addEventListener("focus", this.focusEvent.bind(this));
    this.filterInput.addEventListener("blur", this.blurEvent.bind(this));
    this.selectField.addEventListener("keydown", this.keydownEvent.bind(this));

    // init badges
    this.querySelectorAll("option:checked").forEach(
      option => this.sync(<HTMLOptionElement>option)
    );

    this.initList();

    // init filter
    if (this.filter) {
      const input = this.filterInput;
      input.addEventListener("keyup", this.filterEvent.bind(this));
    }

    // handle autofocus; trigger focus event
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
    console.info("itemValue", itemValue);
    const select = this.hiddenSelect;
    const option: HTMLOptionElement = select.querySelector(`[value="${itemValue}"]`);
    option.selected = !option.selected;
    this.sync(option);
  }

  removeBadge(event: MouseEvent): void {
    const target = <HTMLElement>event.target;
    const group: HTMLElement = target.closest(".btn-group");
    const itemValue = group.dataset.tobagoValue;
    const select = this.hiddenSelect;
    const option: HTMLOptionElement = select.querySelector(`[value="${itemValue}"]`);
    option.selected = false;
    this.sync(option);
  }

  sync(option: HTMLOptionElement) {
    const itemValue = option.value;
    const row: HTMLTableRowElement = this.tbody.querySelector(`[data-tobago-value="${itemValue}"]`);
    if (option.selected) {
      // create badge
      const tabIndex: number = this.filterInput.tabIndex;
      this.filterInput.insertAdjacentHTML("beforebegin",
        this.getRowTemplate(itemValue, row.innerText, option.disabled || this.hiddenSelect.disabled, tabIndex));

      // todo: nicer adding the @click with lit-html
      const closeButton = this.selectField
        .querySelector(".btn-group[data-tobago-value='" + itemValue + "'] button.btn.badge");
      closeButton?.addEventListener("click", this.removeBadge.bind(this));
      closeButton?.addEventListener("focus", this.focusEvent.bind(this));
      closeButton?.addEventListener("blur", this.blurEvent.bind(this));

      // highlight list row
      row.classList.add(this.CssClass.TABLE_PRIMARY);
    } else {
      // remove badge
      const badge = this.selectField.querySelector(`[data-tobago-value="${itemValue}"]`);
      const previousBadge = badge.previousElementSibling;
      const nextBadge = badge.nextElementSibling.tagName === "SPAN" ? badge.nextElementSibling : null;
      badge.remove();
      if (previousBadge) {
        previousBadge.querySelector<HTMLButtonElement>("button.btn.badge").focus();
      } else if (nextBadge) {
        nextBadge.querySelector<HTMLButtonElement>("button.btn.badge").focus();
      } else {
        this.filterInput.disabled = false;
        this.filterInput.focus();
      }

      // remove highlight list row
      row.classList.remove(this.CssClass.TABLE_PRIMARY);
    }

    if (!this.classList.contains(this.CssClass.TOBAGO_DISABLED) && !this.filter) {
      // disable input field to prevent focus.
      if (this.badgeCloseButtons.length > 0 && this.filterInput.id === document.activeElement.id) {
        this.badgeCloseButtons.item(this.badgeCloseButtons.length - 1).focus();
      }
      this.filterInput.disabled = this.badgeCloseButtons.length > 0;
    }
  }

  getRowTemplate(value: string, text: string, disabled: boolean, tabIndex: number): string {
    return disabled ? `
<span class="btn-group" role="group" data-tobago-value="${value}">
  <tobago-badge class="badge text-bg-primary btn disabled">${text}</tobago-badge>
</span>` : `
<span class="btn-group" role="group" data-tobago-value="${value}">
  <tobago-badge class="badge text-bg-primary btn">${text}</tobago-badge>
  <button type='button' class='tobago-button btn btn-secondary badge'
  ${tabIndex > 0 ? " tabindex='" + String(tabIndex) + "'" : ""}><i class='bi-x-lg'></i></button>
</span>`;
  }

  filterEvent(event: Event): void {
    const input = event.currentTarget as HTMLInputElement;
    const searchString = input.value;
    console.info("searchString", searchString);
    const filterFunction = TobagoFilterRegistry.get(this.filter);
    // XXX todo: if filterFunction not found?
    if (filterFunction != null) {
      this.querySelectorAll("tr").forEach(row => {
        const itemValue = row.dataset.tobagoValue;
        if (filterFunction(itemValue, searchString)) {
          row.classList.remove("d-none");
        } else {
          row.classList.add("d-none");
        }
      });
    }
  }

  private clickEvent(event: MouseEvent): void {
    if (this.isDeleted(event.target as Element)) {
      // do nothing, this is probably a removed badge
    } else if (this.isPartOfSelectField(event.target as Element)
      || this.isPartOfTobagoOptions(event.target as Element)) {

      if (!this.filterInput.disabled) {
        this.filterInput.focus();
      } else if (this.badgeCloseButtons.length > 0) {
        this.badgeCloseButtons[0].focus();
      }

    } else {
      this.hideDropdown();
      this.setFocus(false);
    }
  }

  private keydownEvent(event: KeyboardEvent) {
    switch (event.key) {
      case this.Key.ESCAPE:
        this.hideDropdown();
        this.removeTableRowMark();
        break;
      case this.Key.ARROW_DOWN:
        event.preventDefault();
        this.showDropdown();
        this.markNextTableRow();
        break;
      case this.Key.ARROW_UP:
        event.preventDefault();
        this.showDropdown();
        this.markPreviousTableRow();
        break;
      case this.Key.ENTER:
      case this.Key.SPACE:
        if (this.markedRow) {
          event.preventDefault();
          this.selectMarkedOption();
        } else if (document.activeElement.id === this.filterInput.id) {
          this.showDropdown();
        }
        break;
    }
  }

  private markNextTableRow(): void {
    const rows = this.enabledRows;
    const indexOfMark = this.indexOfTobagoMark(rows);
    if (indexOfMark >= 0) {
      if (indexOfMark + 1 < rows.length) {
        rows.item(indexOfMark).classList.remove(this.CssClass.TOBAGO_MARK);
        this.addTableRowMark(rows.item(indexOfMark + 1));
      } else {
        rows.item(rows.length - 1).classList.remove(this.CssClass.TOBAGO_MARK);
        this.addTableRowMark(rows.item(0));
      }
    } else if (rows.length > 0) {
      this.addTableRowMark(rows.item(0));
    }
  }

  private markPreviousTableRow(): void {
    const rows = this.enabledRows;
    const indexOfMark = this.indexOfTobagoMark(rows);
    if (indexOfMark >= 0) {
      if ((indexOfMark - 1) >= 0) {
        rows.item(indexOfMark).classList.remove(this.CssClass.TOBAGO_MARK);
        this.addTableRowMark(rows.item(indexOfMark - 1));
      } else {
        rows.item(0).classList.remove(this.CssClass.TOBAGO_MARK);
        this.addTableRowMark(rows.item(rows.length - 1));
      }
    } else if (rows.length > 0) {
      this.addTableRowMark(rows.item(rows.length - 1));
    }
  }

  private addTableRowMark(row: HTMLTableRowElement): void {
    row.classList.add(this.CssClass.TOBAGO_MARK);
    if (!this.dropdownMenu) {
      row.scrollIntoView({block: "center"});
    }
  }

  private removeTableRowMark(): void {
    this.markedRow?.classList.remove(this.CssClass.TOBAGO_MARK);
  }

  private selectMarkedOption(): void {
    const row = this.tbody.querySelector<HTMLTableRowElement>("." + this.CssClass.TOBAGO_MARK);
    this.selectRow(row);
  }

  private indexOfTobagoMark(rows: NodeListOf<HTMLTableRowElement>): number {
    for (let i = 0; i < rows.length; i++) {
      if (rows.item(i).classList.contains(this.CssClass.TOBAGO_MARK)) {
        return i;
      }
    }
    return -1;
  }

  private isPartOfSelectField(element: Element): boolean {
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

  private isPartOfTobagoOptions(element: Element): boolean {
    if (element) {
      if (element.classList.contains(this.CssClass.TOBAGO_OPTIONS)
        && this.id === element.getAttribute("name")) {
        return true;
      } else {
        return element.parentElement ? this.isPartOfTobagoOptions(element.parentElement) : false;
      }
    } else {
      return false;
    }
  }

  private isDeleted(element: Element): boolean {
    return element.closest("html") === null;
  }

  private showDropdown(): void {
    if (this.dropdownMenu && !this.dropdownMenu.classList.contains(this.CssClass.SHOW)) {
      this.selectField.classList.add(this.CssClass.SHOW);
      this.selectField.ariaExpanded = "true";
      this.dropdownMenu.classList.add(this.CssClass.SHOW);
      this.updateDropdownMenuWidth();
      this.popper.update();
    }
  }

  private hideDropdown(): void {
    if (this.dropdownMenu?.classList.contains(this.CssClass.SHOW)) {
      this.selectField.classList.remove(this.CssClass.SHOW);
      this.selectField.ariaExpanded = "false";
      this.dropdownMenu.classList.remove(this.CssClass.SHOW);
    }
  }

  private resizeEvent(event: UIEvent): void {
    this.updateDropdownMenuWidth();
  }

  private updateDropdownMenuWidth(): void {
    if (this.dropdownMenu) {
      this.dropdownMenu.style.width = `${this.selectField.offsetWidth}px`;
    }
  }

  private focusEvent(): void {
    if (!this.hiddenSelect.disabled) {
      if (!this.classList.contains(this.CssClass.TOBAGO_FOCUS)) {
        this.setFocus(true);
        this.showDropdown();
      }
    }
  }

  private blurEvent(event: FocusEvent): void {
    if (event.relatedTarget !== null) {
      //relatedTarget is the new focused element; null indicate a mouseclick or an inactive browser window
      if (!this.isPartOfSelectField(event.relatedTarget as Element)
        && !this.isPartOfTobagoOptions(event.relatedTarget as Element)) {
        this.setFocus(false);
        this.hideDropdown();
      }
    }
  }

  private setFocus(focus: boolean): void {
    if (focus) {
      this.classList.add(this.CssClass.TOBAGO_FOCUS);
    } else {
      this.classList.remove(this.CssClass.TOBAGO_FOCUS);
    }
  }

  private initList() {
    const tbody = this.tbody;
    tbody.addEventListener("click", this.select.bind(this));
    tbody.querySelectorAll("tr").forEach((row: HTMLTableRowElement) => {
      // row stuff
    });
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-select-many-list") == null) {
    window.customElements.define("tobago-select-many-list", SelectManyList);
  }
});
