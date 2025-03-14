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

import {SuggestFilter} from "./tobago-suggest-filter";
import {Css} from "./tobago-css";
import {DropdownMenu, DropdownMenuAlignment} from "./tobago-dropdown-menu";
import {Key} from "./tobago-key";
import {ClientBehaviors} from "./tobago-client-behaviors";

export class Suggest {
  private tobagoIn: HTMLElement;
  private dropdownMenu: DropdownMenu;
  private timeout: number;

  constructor(tobagoIn: HTMLElement) {
    this.tobagoIn = tobagoIn;
  }

  public init(): void {
    if (!this.tobagoSuggest) {
      console.warn("[tobago-suggest] could not find tobago-suggest");
      return;
    }
    this.tobagoSuggest.insertAdjacentHTML("beforebegin", `<div class="${Css.SPINNER}"/>`);
    this.tobagoSuggest.insertAdjacentHTML("beforebegin", `<div class="${Css.TOBAGO_DROPDOWN_MENU_ANCHOR}"}">
    <ul id="${this.tobagoSuggest.id + "::dropdownMenu"}" class="${Css.TOBAGO_DROPDOWN_MENU}"
    name="${this.tobagoSuggest.id}" role="listbox"/></div>`);

    this.dropdownMenu = new DropdownMenu(this.dropdownMenuElement, this.inputField, this.tobagoIn, this.localMenu,
        DropdownMenuAlignment.centerFullWidth);

    this.inputField.role = "combobox";
    this.inputField.autocomplete = "off";
    this.inputField.autocapitalize = "off";
    this.inputField.spellcheck = false;
    this.inputField.ariaAutoComplete = "list";
    this.inputField.ariaHasPopup = "listbox";
    this.inputField.setAttribute("aria-owns", this.dropdownMenuElement.id);

    this.tobagoIn.addEventListener(ClientBehaviors.DROPDOWN_HIDDEN, this.deleteResults.bind(this));
    this.inputField.addEventListener("input", this.inputEvent.bind(this));
    this.inputField.addEventListener("keydown", this.keydownEvent.bind(this));
    this.dropdownMenuElement.addEventListener("keydown", this.keydownEvent.bind(this));

    this.inputField.addEventListener("blur", this.blurEvent.bind(this));
    this.dropdownMenuElement.addEventListener("blur", this.blurEvent.bind(this));
  }

  /**
   * Call this in the disconnectedCallback() method.
   */
  public disconnect(): void {
    this.dropdownMenu.disconnect();
  }

  private blurEvent(event: FocusEvent): void {
    const element = (event.relatedTarget as Element);
    if (!this.isPartOfSuggest(element)) {
      this.dropdownMenu.hide();
    }
  }

  private isPartOfSuggest(element: Element): boolean {
    if (element) {
      if (this.inputField.id === element.id || this.tobagoSuggest.id === element.getAttribute("name")) {
        return true;
      } else {
        return element.parentElement ? this.isPartOfSuggest(element.parentElement) : false;
      }
    } else {
      return false;
    }
  }

  private keydownEvent(event: KeyboardEvent): void {
    switch (event.key) {
      case Key.ARROW_DOWN:
        this.focusNextDropdownItem(event);
        break;
      case Key.ARROW_UP:
        this.focusPreviousDropdownItem(event);
        break;
      case Key.TAB: {
        if (event.shiftKey) {
          this.focusPreviousDropdownItem(event);
        } else {
          this.focusNextDropdownItem(event);
        }
        break;
      }
      case Key.ESCAPE:
        this.dropdownMenu.hide();
        this.inputField.focus();
        break;
      default:
        break;
    }
  }

  private focusNextDropdownItem(event: KeyboardEvent): void {
    if (this.dropdownMenu.visible) {
      event.preventDefault(); //prevent scrolling if dropdown menu has a scrollbar
      if (this.inputField === this.activeElement) {
        this.dropdownItems.item(0).focus();
      } else {
        for (let i = 0; i < this.dropdownItems.length; i++) {
          if (this.dropdownItems.item(i) === this.activeElement) {
            const nextItemIndex = ++i;
            if (nextItemIndex >= this.dropdownItems.length) {
              this.dropdownItems.item(0).focus();
            } else {
              this.dropdownItems.item(nextItemIndex).focus();
            }
            break;
          }
        }
      }
    }
  }

  private focusPreviousDropdownItem(event: KeyboardEvent): void {
    if (this.dropdownMenu.visible) {
      event.preventDefault(); //prevent scrolling if dropdown menu has a scrollbar
      if (this.inputField === this.activeElement) {
        this.dropdownItems.item(this.dropdownItems.length - 1).focus();
      } else {
        for (let i = 0; i < this.dropdownItems.length; i++) {
          if (this.dropdownItems.item(i) === this.activeElement) {
            const previousItemIndex = --i;
            if (previousItemIndex < 0) {
              this.dropdownItems.item(this.dropdownItems.length - 1).focus();
            } else {
              this.dropdownItems.item(previousItemIndex).focus();
            }
            break;
          }
        }
      }
    }
  }

  private inputEvent(event: InputEvent): void {
    window.clearTimeout(this.timeout);

    const inputElement: HTMLInputElement = event.currentTarget as HTMLInputElement;
    const input = inputElement.value;

    if (input.length >= this.minChars) {
      this.hiddenInput.value = input.toLowerCase();
      this.showSpinner();

      if (this.update) {
        this.timeout = window.setTimeout(() => {
          const suggestId = this.tobagoSuggest.id;
          faces.ajax.request(suggestId, null, {
            params: {
              "jakarta.faces.behavior.event": "suggest"
            },
            execute: suggestId,
            render: suggestId,
            onevent: this.ajaxEvent.bind(this),
          });
        }, this.delay);
      } else {
        switch (this.filter) {
          case SuggestFilter.all:
            this.renderResults(this.items);
            break;
          case SuggestFilter.prefix:
            this.renderResults(this.items.filter(item => item.toLowerCase().startsWith(this.hiddenInput.value)));
            break;
          case SuggestFilter.contains:
          default:
            this.renderResults(this.items.filter(item => item.toLowerCase().indexOf(this.hiddenInput.value) > -1));
            break;
        }
      }
    } else {
      this.dropdownMenu.hide();
    }
  }

  private showSpinner(): void {
    const rect = this.inputField.getBoundingClientRect();
    const cStyle = getComputedStyle(this.inputField);
    const top = window.scrollY + rect.top;
    const right = window.scrollX + rect.right;
    const bottom = window.scrollY + rect.bottom;
    const innerBorderTop = top + parseFloat(cStyle.borderTopWidth) + parseFloat(cStyle.paddingTop);
    const innerBorderRight = right - parseFloat(cStyle.borderRight) - parseFloat(cStyle.paddingRight);
    const innerBorderBottom = bottom - parseFloat(cStyle.borderBottom) - parseFloat(cStyle.paddingBottom);
    const size = innerBorderBottom - innerBorderTop;
    this.spinner.style.width = size + "px";
    this.spinner.style.height = size + "px";
    this.spinner.style.top = innerBorderTop + "px";
    this.spinner.style.left = (innerBorderRight - size) + "px";
    this.spinner.classList.add(Css.TOBAGO_SHOW);
  }

  private hideSpinner(): void {
    this.spinner.classList.remove(Css.TOBAGO_SHOW);
  }

  private ajaxEvent(event: EventData): void {
    if (event.status === "success") {
      this.renderResults(this.items);
    }
  }

  private renderResults(items: string[]): void {
    if (items.length > 0) {
      const dropdownItems: HTMLLIElement[] = [];
      for (let i = 0; i < items.length; i++) {
        const li = document.createElement("li");
        li.setAttribute("data-result-index", i.toString());
        li.setAttribute("role", "option");
        const button = document.createElement("button");
        button.type = "button";
        button.classList.add(Css.DROPDOWN_ITEM);
        button.textContent = items[i];
        button.addEventListener("click", this.selectDropdownItem.bind(this));
        li.appendChild(button);
        dropdownItems.push(li);
      }
      this.dropdownMenuElement.replaceChildren(...dropdownItems);
      this.hideSpinner();
      this.dropdownMenu.show();

      if (this.maxItems > 0) {
        // do this after this.dropdownMenu.show(); to get the correct itemHeight
        const itemHeight = this.dropdownMenuElement.querySelector("li").offsetHeight;
        const dropdownMenuElementStyle = getComputedStyle(this.dropdownMenuElement);
        const paddingTop = parseFloat(dropdownMenuElementStyle.paddingTop);
        const paddingBottom = parseFloat(dropdownMenuElementStyle.paddingBottom);
        const itemBasedMaxHeight = paddingTop + (this.maxItems * itemHeight) + paddingBottom;
        const bodyBasedMaxHeight = parseFloat(this.dropdownMenuElement.style.maxHeight);

        if (itemBasedMaxHeight < bodyBasedMaxHeight) {
          this.dropdownMenuElement.style.maxHeight = itemBasedMaxHeight + "px";
        }
      }
    } else {
      this.hideSpinner();
      this.dropdownMenu.hide();
    }
  }

  private deleteResults(): void {
    this.dropdownMenuElement.innerHTML = null;
  }

  private selectDropdownItem(event: MouseEvent): void {
    const button: HTMLButtonElement = event.currentTarget as HTMLButtonElement;
    this.inputField.value = button.textContent;
    this.dropdownMenu.hide();
    this.inputField.focus();
  }

  private get activeElement(): Element {
    const root = this.tobagoIn.getRootNode() as ShadowRoot | Document;
    return root.activeElement;
  }

  private get inputField(): HTMLInputElement {
    const root = this.tobagoIn.getRootNode() as ShadowRoot | Document;
    return root.getElementById(this.tobagoSuggest.getAttribute("for")) as HTMLInputElement;
  }

  private get spinner(): HTMLDivElement {
    return this.tobagoIn.querySelector<HTMLDivElement>(`.${Css.SPINNER}`);
  }

  private get tobagoSuggest(): HTMLElement {
    return this.tobagoIn.querySelector("tobago-suggest");
  }

  private get minChars(): number {
    return parseInt(this.tobagoSuggest.getAttribute("min-chars"));
  }

  private get delay(): number {
    return parseInt(this.tobagoSuggest.getAttribute("delay"));
  }

  private get maxItems(): number {
    return parseInt(this.tobagoSuggest.getAttribute("max-items"));
  }

  private get update(): boolean {
    return this.tobagoSuggest.getAttribute("update") !== null;
  }

  private get totalCount(): number {
    return parseInt(this.tobagoSuggest.getAttribute("total-count"));
  }

  private get filter(): SuggestFilter {
    return SuggestFilter[this.tobagoSuggest.getAttribute("filter")] as SuggestFilter;
  }

  private get items(): string[] {
    return JSON.parse(this.tobagoSuggest.getAttribute("items")) as string[];
  }

  private get localMenu(): boolean {
    return this.tobagoSuggest.getAttribute("local-menu") !== null;
  }

  private get hiddenInput(): HTMLInputElement {
    return this.tobagoSuggest.querySelector(":scope > input[type=hidden]");
  }

  private get dropdownMenuElement(): HTMLUListElement {
    const root = this.tobagoIn.getRootNode() as ShadowRoot | Document;
    return root.getElementById(this.tobagoSuggest.id + "::dropdownMenu") as HTMLUListElement;
  }

  private get dropdownItems(): NodeListOf<HTMLButtonElement> {
    return this.dropdownMenuElement.querySelectorAll<HTMLButtonElement>("li button.dropdown-item");
  }
}
