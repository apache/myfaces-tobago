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
import {html, HTMLTemplateResult, render, TemplateResult} from "lit-html";

class SelectManyList extends SelectListBase {
  constructor() {
    super();
  }

  get badgeCloseButtons(): NodeListOf<HTMLButtonElement> {
    return this.selectField.querySelectorAll("button.btn.badge");
  }

  connectedCallback(): void {
    super.connectedCallback();

    // init badges
    this.querySelectorAll("option:checked").forEach(
        option => this.sync(<HTMLOptionElement>option)
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
        this.showDropdown();

      } else {
        this.leaveComponent();
      }
    }
  }

  protected select(row: HTMLTableRowElement): void {
    const itemValue = row.dataset.tobagoValue;
    const option: HTMLOptionElement = this.hiddenSelect.querySelector(`[value="${itemValue}"]`);
    option.selected = !option.selected;
    this.sync(option);
    this.filterInput.disabled = false;
    this.filterInput.focus({preventScroll: true});
  }

  private sync(option: HTMLOptionElement) {
    const itemValue = option.value;
    const row: HTMLTableRowElement = this.tbody.querySelector(`[data-tobago-value="${itemValue}"]`);
    if (option.selected) {
      // create badge
      const tabIndex: number = this.filterInput.tabIndex;
      const span = document.createElement("span");
      span.className = "btn-group";
      span.role="group";
      span.dataset.tobagoValue = itemValue;
      this.filterInput.insertAdjacentElement("beforebegin", span);
      render(
          this.getRowTemplate(row.innerText, option.disabled || this.hiddenSelect.disabled, tabIndex), span);

      // todo: nicer adding the @click with lit-html
      // const closeButton = this.selectField
      //     .querySelector(".btn-group[data-tobago-value='" + itemValue + "'] button.btn.badge");
      // closeButton?.addEventListener("click", this.removeBadge.bind(this));
      // closeButton?.addEventListener("focus", this.focusEvent.bind(this));
      // closeButton?.addEventListener("blur", this.blurEvent.bind(this));

      row.classList.add(Css.TABLE_PRIMARY); // highlight list row
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

      row.classList.remove(Css.TABLE_PRIMARY); // remove highlight list row
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


 private getRowTemplate(text: string, disabled: boolean, tabIndex: number): HTMLTemplateResult {
    console.debug("creating span: ", text, disabled, tabIndex);
    return disabled
        ? html`<tobago-badge class="badge text-bg-primary btn disabled">${text}</tobago-badge>`
        : html`<tobago-badge class="badge text-bg-primary btn">${text}</tobago-badge>
  <button type='button'
      class='tobago-button btn btn-secondary badge'
      ${tabIndex > 0 ? " tabindex='" + String(tabIndex) + "'" : ""}
      @click="${this.removeBadge.bind(this)}"
      @focus="${this.focusEvent.bind(this)}"
      @blur="${this.blurEvent.bind(this)}"><i class='bi-x-lg'></i></button>`;
  }

  private removeBadge(event: MouseEvent): void {
    const target = <HTMLElement>event.target;
    const group: HTMLElement = target.closest(".btn-group");
    const itemValue = group.dataset.tobagoValue;
    const option: HTMLOptionElement = this.hiddenSelect.querySelector(`[value="${itemValue}"]`);
    option.selected = false;
    this.sync(option);
  }

  protected leaveComponent(): void {
    this.focused = false;
    this.filterInput.value = null;
    this.filterInput.dispatchEvent(new Event("input"));
    this.hideDropdown();
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
