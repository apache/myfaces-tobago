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
import {Spinner} from "./tobago-spinner";
import {EventListenerStore} from "./tobago-event-listener-store";
import {OptionsControls} from "./tobago-options-controls";

export class Suggest {
  private listeners: EventListenerStore = new EventListenerStore();
  private tobagoIn: HTMLElement;
  private dropdownMenu: DropdownMenu;
  private spinner: Spinner;
  private optionsControls: OptionsControls;
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
    this.spinner = new Spinner(this.inputField, this.spinnerDiv);

    /* eslint-disable max-len */
    this.tobagoSuggest.insertAdjacentHTML("beforebegin", `<div class="${Css.TOBAGO_DROPDOWN_MENU_ANCHOR}">
  <div id="${this.tobagoSuggest.id + "::dropdownMenu"}" class="tobago-options ${Css.TOBAGO_DROPDOWN_MENU}" data-tobago-for="${this.tobagoSuggest.id}">
    <table class="table table-hover table-sm">
      <tbody>
      </tbody>
    </table>
  </div>
</div>
`);
    /* eslint-enable max-len */
    this.optionsControls = new OptionsControls(this.optionsElement, this.select.bind(this));

    this.dropdownMenu = new DropdownMenu(this.dropdownMenuElement, this.inputField, [this.tobagoIn, this.tobagoSuggest],
        this.localMenu, DropdownMenuAlignment.centerFullWidth);

    this.inputField.role = "combobox";
    this.inputField.autocomplete = "off";
    this.inputField.autocapitalize = "off";
    this.inputField.spellcheck = false;
    this.inputField.ariaAutoComplete = "list";
    this.inputField.ariaHasPopup = "listbox";
    this.inputField.setAttribute("aria-owns", this.dropdownMenuElement.id);

    this.listeners.add(document, "click", this.globalClickEvent.bind(this));
    this.listeners.add(this.tobagoIn, ClientBehaviors.TOBAGO_DROPDOWN_HIDDEN, this.deleteResults.bind(this));
    this.listeners.add(this.inputField, "input", this.inputEvent.bind(this));
    this.listeners.add(this.inputField, "keydown", this.keydownEvent.bind(this));
    this.listeners.add(this.dropdownMenuElement, "keydown", this.keydownEvent.bind(this));
    this.listeners.add(this.inputField, "focus", this.focusEvent.bind(this));
    this.listeners.add(this.inputField, "blur", this.blurEvent.bind(this));
    this.listeners.add(this.dropdownMenuElement, "blur", this.blurEvent.bind(this));

    if (!this.update) { //client side filtering
      this.optionsControls.renderRows(this.items);
    }
  }

  /**
   * Call this in the disconnectedCallback() method.
   */
  public disconnect(): void {
    this.spinnerDiv.remove();
    delete this.spinner;
    this.dropdownMenuAnchor.remove();
    this.optionsControls.disconnect();
    delete this.optionsControls;
    this.dropdownMenu.disconnect();
    delete this.dropdownMenu;
    this.listeners.disconnect();
  }

  protected globalClickEvent(event: MouseEvent): void {
    if (!this.inputField.disabled) {
      if (!this.isPartOfSuggest(event.relatedTarget as HTMLElement)) {
        this.dropdownMenu.hide();
      }
    }
  }

  private focusEvent(event: FocusEvent): void {
    const inputElement: HTMLInputElement = event.currentTarget as HTMLInputElement;
    const input = inputElement.value;

    const lastFocusedElement = event.relatedTarget as HTMLElement;
    if (this.minChars === 0 && input.length === 0 && !this.isPartOfSuggest(lastFocusedElement)) {
      this.inputEvent(event); //open dropdown on click for minChars=0 suggests
    }
  }

  private blurEvent(event: FocusEvent): void {
    const element = (event.relatedTarget as HTMLElement);
    if (element !== null) {
      //relatedTarget is the new focused element; null indicate a mouseclick or an inactive browser window
      if (!this.isPartOfSuggest(element)) {
        this.dropdownMenu.hide();
      }
    }
  }

  private isPartOfSuggest(element: HTMLElement): boolean {
    if (element) {
      if (this.inputField.id === element.id || this.tobagoSuggest.id === element.dataset.tobagoFor) {
        return true;
      } else {
        return element.parentElement ? this.isPartOfSuggest(element.parentElement) : false;
      }
    } else {
      return false;
    }
  }

  private select(row: HTMLTableRowElement): void {
    this.inputField.value = row.cells.item(0).textContent;
    this.dropdownMenu.hide();
    this.inputField.focus();
  }

  private keydownEvent(event: KeyboardEvent): void {
    switch (event.key) {
      case Key.ARROW_DOWN:
        event.preventDefault();
        this.optionsControls.preselectNextRow();
        break;
      case Key.ARROW_UP:
        event.preventDefault();
        this.optionsControls.preselectPreviousRow();
        break;
      case Key.TAB: {
        if (this.dropdownMenu.visible) {
          event.preventDefault();
        }
        if (event.shiftKey) {
          this.optionsControls.preselectPreviousRow();
        } else {
          this.optionsControls.preselectNextRow();
        }
        break;
      }
      case Key.ENTER:
        if (!this.dropdownMenu.visible) {
          this.processInputValue(this.inputField.value);
        }
      case Key.SPACE:
        if (this.optionsControls.preselectedRow) {
          event.preventDefault();
          this.select(this.optionsControls.preselectedRow);
        }
        break;
      case Key.ESCAPE:
        this.inputField.focus();
        if (this.dropdownMenuElement && this.dropdownMenu.visible) {
          this.dropdownMenu?.hide();
          event.stopPropagation(); //prevent closing a parent dropdown form menu
        }
        break;
      default:
        break;
    }
  }

  private inputEvent(event: UIEvent): void {
    const inputElement: HTMLInputElement = event.currentTarget as HTMLInputElement;
    this.processInputValue(inputElement.value);
  }

  private processInputValue(inputValue: string): void {
    window.clearTimeout(this.timeout);

    if (inputValue.length >= this.minChars) {
      this.hiddenInput.value = inputValue.toLowerCase();

      if (this.update) {
        this.spinner.show();
        this.timeout = window.setTimeout(() => {
          const suggestId = this.tobagoSuggest.id;
          tobago.ajax.request(suggestId, null, {
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
            break;
          case SuggestFilter.prefix:
            this.optionsControls
                .filter((item: string, query: string) => item.toLowerCase().startsWith(query),
                    this.hiddenInput.value);
            break;
          case SuggestFilter.contains:
          default:
            this.optionsControls
                .filter((item: string, query: string) => item.toLowerCase().indexOf(this.hiddenInput.value) > -1,
                    this.hiddenInput.value);
            break;
        }
        if (this.optionsControls.visibleRows.length > 0) {
          this.dropdownMenu.show();
        } else {
          this.dropdownMenu.hide();
        }
      }
    } else {
      this.spinner.hide();
      this.dropdownMenu.hide();
    }
  }

  private ajaxEvent(event: faces.AjaxEvent): void {
    if (event.status === "success") {
      setTimeout(() => { //use setTimeout with 1 ms to give time to reregister listeners to the tobago-suggest element
        this.dropdownMenu.updateEventElements([this.tobagoIn, this.tobagoSuggest]);
        this.optionsControls.renderRows(this.items);
        this.spinner.hide();

        if (this.items.length > 0) {
          this.dropdownMenu.show();
        } else {
          this.dropdownMenu.hide();
        }
      }, 1);
    }
  }

  private deleteResults(): void {
    if (this.update) { //server side filtering
      this.optionsControls.renderRows(null);
    }
  }

  private get inputField(): HTMLInputElement {
    const root = this.tobagoIn.getRootNode() as ShadowRoot | Document;
    return root.getElementById(this.tobagoSuggest.getAttribute("for")) as HTMLInputElement;
  }

  private get spinnerDiv(): HTMLDivElement {
    return this.tobagoIn.querySelector<HTMLDivElement>(`.${Css.SPINNER}`);
  }

  private get dropdownMenuAnchor(): HTMLDivElement {
    return this.tobagoIn.querySelector<HTMLDivElement>(`.${Css.TOBAGO_DROPDOWN_MENU_ANCHOR}`);
  }

  private get optionsElement(): HTMLDivElement {
    return this.dropdownMenuAnchor.querySelector(".tobago-options");
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

  private get dropdownMenuElement(): HTMLTableElement {
    const root = this.tobagoIn.getRootNode() as ShadowRoot | Document;
    return root.querySelector(`.${Css.TOBAGO_DROPDOWN_MENU}[data-tobago-for="${this.tobagoSuggest.id}"]`);
  }
}
