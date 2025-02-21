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
import {Css} from "./tobago-css";
import {html, HTMLTemplateResult, render} from "lit-html";

class SelectManyList extends SelectListBase {
  constructor() {
    super();
  }

  get badges(): HTMLDivElement {
    return this.selectField.querySelector(".tobago-badges");
  }

  get badgeCloseButtons(): NodeListOf<HTMLButtonElement> {
    return this.badges.querySelectorAll("button.btn.badge");
  }

  connectedCallback(): void {
    super.connectedCallback();

    // init badges
    this.querySelectorAll("option:checked").forEach(
        option => this.sync((option as HTMLOptionElement))
    );
  }

  protected globalClickEvent(event: MouseEvent): void {
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
        this.dropdownMenu.show();

      } else {
        this.leaveComponent();
      }
    }
  }

  protected labelClickEvent(event: MouseEvent): void {
    event.stopPropagation(); // stop propagation to avoid execution of globalClickEvent()
    if (!this.filterInput.disabled) {
      this.filterInput.focus();
    } else if (this.badgeCloseButtons.length > 0) {
      this.badgeCloseButtons[0].focus();
    }
  }

  protected select(row: HTMLTableRowElement): void {
    const itemValue = row.dataset.tobagoValue;
    const option: HTMLOptionElement = this.hiddenSelect.querySelector(`[value="${itemValue}"]`);
    option.selected = !option.selected;
    this.sync(option);
    this.hiddenSelect.dispatchEvent(new Event("change", {bubbles: true}));
  }

  private sync(option: HTMLOptionElement) {
    const itemValue = option.value;
    const row: HTMLTableRowElement = this.tbody.querySelector(`[data-tobago-value="${itemValue}"]`);
    if (option.selected) {
      // create badge
      const tabIndex: number = this.filterInput.tabIndex;
      const span = document.createElement("span");
      span.className = "btn-group";
      span.role = "group";
      span.dataset.tobagoValue = itemValue;
      this.badges.insertAdjacentElement("beforeend", span);
      render(this.getRowTemplate(row.innerText, option.disabled || this.hiddenSelect.disabled, tabIndex), span);

      row.classList.add(Css.TABLE_PRIMARY); // highlight list row
    } else {
      // remove badge
      const badge = this.selectField.querySelector(`[data-tobago-value="${itemValue}"]`);
      badge.remove();

      row.classList.remove(Css.TABLE_PRIMARY); // remove highlight list row
    }

    if (!this.disabled && !this.filter) {
      // disable input field to prevent focus.
      if (this.badgeCloseButtons.length > 0 && this.filterInput.id === document.activeElement.id) {
        this.badgeCloseButtons.item(this.badgeCloseButtons.length - 1).focus();
      }
      this.filterInput.disabled = this.badgeCloseButtons.length > 0;
    }
  }

  private getRowTemplate(text: string, disabled: boolean, tabIndex: number): HTMLTemplateResult {
    console.debug("creating span: ", text, disabled, tabIndex);
    return disabled
        ? html`<tobago-badge class="badge text-bg-primary btn disabled">${text}</tobago-badge>`
        : html`<tobago-badge class="badge text-bg-primary btn">${text}</tobago-badge>
  <button type='button'
      class='tobago-button btn btn-secondary badge' aria-label='deselect ${text}'
      ${tabIndex > 0 ? " tabindex='" + String(tabIndex) + "'" : ""}
      @click="${this.removeBadge.bind(this)}"
      @focus="${this.focusEvent.bind(this)}"
      @blur="${this.blurEvent.bind(this)}"><i class='bi-x-lg'></i></button>`;
  }

  private removeBadge(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    const group: HTMLElement = target.closest(".btn-group");
    const itemValue = group.dataset.tobagoValue;
    const option: HTMLOptionElement = this.hiddenSelect.querySelector(`[value="${itemValue}"]`);
    option.selected = false;

    const badge = this.badges.querySelector(`[data-tobago-value="${itemValue}"]`);
    const previousBadge = badge.previousElementSibling;
    const nextBadge = badge.nextElementSibling?.tagName === "SPAN" ? badge.nextElementSibling : null;
    if (previousBadge) {
      previousBadge.querySelector<HTMLButtonElement>("button.btn.badge").focus();
    } else if (nextBadge) {
      nextBadge.querySelector<HTMLButtonElement>("button.btn.badge").focus();
    } else {
      this.filterInput.disabled = false;
      this.filterInput.focus();
    }

    this.sync(option);
    this.hiddenSelect.dispatchEvent(new Event("change", {bubbles: true}));
  }

  protected leaveComponent(): void {
    this.focused = false;
    this.filterInput.value = null;
    this.filterInput.dispatchEvent(new Event("input"));
    this.dropdownMenu.hide();
  }

  private isDeleted(element: Element): boolean {
    return element.closest("html") === null;
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-select-many-list") == null) {
    window.customElements.define("tobago-select-many-list", SelectManyList);
  }
});
