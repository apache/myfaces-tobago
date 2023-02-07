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

class SelectManyList extends SelectListBase {
  constructor() {
    super();
  }

  get badgeCloseButtons(): NodeListOf<HTMLButtonElement> {
    return this.selectField.querySelectorAll("button.btn.badge");
  }

  connectedCallback(): void {
    super.connectedCallback();
    document.addEventListener("click", this.clickEvent.bind(this));
    this.filterInput.addEventListener("focus", this.focusEvent.bind(this));
    this.filterInput.addEventListener("blur", this.blurEvent.bind(this));
    this.selectField.addEventListener("keydown", this.keydownEvent.bind(this));

    // init badges
    this.querySelectorAll("option:checked").forEach(
        option => this.sync(<HTMLOptionElement>option)
    );

    this.initList();

    if (this.filter) {
      this.filterInput.addEventListener("input", this.filterEvent.bind(this));
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

    if (!this.disabled && !this.filter) {
      // disable input field to prevent focus.
      if (this.badgeCloseButtons.length > 0 && this.filterInput.id === document.activeElement.id) {
        this.badgeCloseButtons.item(this.badgeCloseButtons.length - 1).focus();
      }
      if (!this.preselectedRow) {
        this.filterInput.disabled = this.badgeCloseButtons.length > 0;
      }
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
          row.classList.remove(this.CssClass.D_NONE);
        } else {
          row.classList.add(this.CssClass.D_NONE);
        }
      });
    }
  }

  private clickEvent(event: MouseEvent): void {
    if (!this.disabled) {
      if (this.isDeleted(event.target as Element)) {
        // do nothing, this is probably a removed badge
      } else if (this.isPartOfSelectField(event.target as Element)
          || this.isPartOfTobagoOptions(event.target as Element)) {

        if (!this.filterInput.disabled) {
          this.filterInput.focus();
        } else if (this.badgeCloseButtons.length > 0) {
          this.badgeCloseButtons[0].focus();
        }
        this.showDropdown();

      } else {
        this.leaveComponent();
      }
    }
  }

  private keydownEvent(event: KeyboardEvent) {
    switch (event.key) {
      case this.Key.ESCAPE:
        this.hideDropdown();
        this.removePreselection();
        break;
      case this.Key.ARROW_DOWN:
        event.preventDefault();
        this.showDropdown();
        this.preselectNextTableRow();
        break;
      case this.Key.ARROW_UP:
        event.preventDefault();
        this.showDropdown();
        this.preselectPreviousTableRow();
        break;
      case this.Key.ENTER:
      case this.Key.SPACE:
        if (this.preselectedRow) {
          event.preventDefault();
          const row = this.tbody.querySelector<HTMLTableRowElement>("." + this.CssClass.TOBAGO_PRESELECT);
          this.selectRow(row);
          this.filterInput.disabled = false;
          this.filterInput.focus({preventScroll: true});
        } else if (document.activeElement.id === this.filterInput.id) {
          this.showDropdown();
        }
        break;
      case this.Key.TAB:
        this.removePreselection();
        break;
    }
  }

  private leaveComponent(): void {
    this.setFocus(false);
    this.filterInput.value = null;
    this.filterInput.dispatchEvent(new Event("input"));
    this.hideDropdown();
  }

  private isDeleted(element: Element): boolean {
    return element.closest("html") === null;
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
