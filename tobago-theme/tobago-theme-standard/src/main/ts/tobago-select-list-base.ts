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

import {Css} from "./tobago-css";
import {TobagoFilterRegistry} from "./tobago-filter-registry";
import {Key} from "./tobago-key";
import {DropdownMenu, DropdownMenuAlignment} from "./tobago-dropdown-menu";
import {Spinner} from "./tobago-spinner";
import {EventListenerStore} from "./tobago-event-listener-store";
import {OptionsControls} from "./tobago-options-controls";

export abstract class SelectListBase extends HTMLElement {

  private static readonly SUFFIX_FILTER_UPDATE: string = "::filter-update";

  protected listeners: EventListenerStore = new EventListenerStore();
  protected dropdownMenu: DropdownMenu;
  private spinner: Spinner;
  protected optionsControls: OptionsControls;
  private timeout: number;
  private filterUpdateLoader: HTMLElement;
  protected lastSuccessfulSearchQuery: string;

  connectedCallback(): void {
    this.optionsControls = new OptionsControls(this.options, this.select.bind(this));
    if (this.dropdownMenuElement) {
      this.dropdownMenu = new DropdownMenu(this.dropdownMenuElement, this.selectField, this, this.localMenu,
          DropdownMenuAlignment.centerFullWidth);
    }
    this.listeners.add(document, "click", this.globalClickEvent.bind(this));
    this.listeners.add(this.hiddenSelect, "click", this.labelClickEvent.bind(this));
    this.listeners.add(this.selectField, "keydown", this.keydownEventBase.bind(this));
    this.listeners.add(this.filterInput, "focus", this.focusEvent.bind(this));
    this.listeners.add(this.filterInput, "blur", this.blurEvent.bind(this));
    this.listeners.add(this.options, "keydown", this.keydownEventBase.bind(this));
    this.optionsControls.rows.forEach(row => this.optionsControls
        .listeners.add(row, "blur", this.blurEvent.bind(this)));
    if (this.filter || this.serverSideFiltering) {
      this.listeners.add(this.filterInput, "input", this.filterInputEvent.bind(this));
    }
    if (this.serverSideFiltering) {
      this.insertAdjacentHTML("beforeend", `<div class="${Css.SPINNER}"/>`);
      this.spinner = new Spinner(this.selectField, this.spinnerDiv);
    }

    // handle autofocus; trigger focus event
    if (document.activeElement.id === this.filterInput.id) {
      this.focusEvent();
    }

    // redirect click events for ajax behavior
    this.listeners.add(this.selectField, "click", () => this.hiddenSelect.dispatchEvent(new Event("click")));
    this.listeners.add(this.options, "click", () => this.hiddenSelect.dispatchEvent(new Event("click")));
    this.listeners.add(this.selectField, "dblclick", () => this.hiddenSelect.dispatchEvent(new Event("dblclick")));
    this.listeners.add(this.options, "dblclick", () => this.hiddenSelect.dispatchEvent(new Event("dblclick")));
  }

  disconnectedCallback(): void {
    this.optionsControls.disconnect();
    this.dropdownMenu?.disconnect();
    this.listeners.disconnect();
  }

  protected abstract globalClickEvent(event: MouseEvent): void;

  /**
   * The label for attribute targets the hidden select field, so an event which is dispatched on the label is also
   * dispatched on the hidden select field. This method must ensure that a click on a label focus the
   * Select[One/Many]List component.
   * @param event
   * @protected
   */
  protected abstract labelClickEvent(event: MouseEvent): void;

  protected keydownEventBase(event: KeyboardEvent) {
    switch (event.key) {
      case Key.ESCAPE:
        if (this.dropdownMenuElement && this.dropdownMenu.visible) {
          this.dropdownMenu?.hide();
          event.stopPropagation(); //prevent closing a parent dropdown form menu
        }
        this.optionsControls.removePreselection();
        this.filterInput.focus({preventScroll: true});
        break;
      case Key.ARROW_DOWN:
        event.preventDefault();
        this.dropdownMenu?.show();
        this.optionsControls.preselectNextRow();
        break;
      case Key.ARROW_UP:
        event.preventDefault();
        this.dropdownMenu?.show();
        this.optionsControls.preselectPreviousRow();
        break;
      case Key.ENTER:
      case Key.SPACE:
        if (this.optionsControls.preselectedRow) {
          event.preventDefault();
          this.select(this.optionsControls.preselectedRow);
        } else if (document.activeElement.id === this.filterInput.id) {
          this.dropdownMenu?.show();
        }
        break;
      case Key.TAB:
        if (this.dropdownMenuElement && this.dropdownMenu.visible) {
          event.preventDefault();
          if (event.shiftKey) {
            this.optionsControls.preselectPreviousRow();
          } else {
            this.optionsControls.preselectNextRow();
          }
        } else {
          this.optionsControls.removePreselection();
          this.filterInput.focus({preventScroll: true});
        }
        break;
      default:
        this.filterInput.focus({preventScroll: true});
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
          && !this.isPartOfTobagoOptions(event.relatedTarget as HTMLElement)) {
        this.leaveComponent();
      }
    }
  }

  private filterInputEvent(event: Event): void {
    const input = event.currentTarget as HTMLInputElement;
    const searchString = input.value;
    this.dropdownMenu?.show();

    if (this.serverSideFiltering) {
      if (searchString.length >= this.minChars) {
        this.showSpinner();

        window.clearTimeout(this.timeout);
        this.timeout = window.setTimeout(() => this.doFilter(searchString), this.delay);
      }
    } else {
      this.doFilter(searchString);
    }
  }

  /**
   * This function is also used for resetting the filter. Therefor "delay" and "minChar" must not be tested inside this
   * function.
   */
  protected doFilter(searchString: string) {
    if (this.serverSideFiltering) {
      this.hiddenQueryInput.value = searchString;
      tobago.ajax.request(
          this.id,
          null,
          {
            params: {
              "javax.faces.behavior.event": "filter",
              selectListUpdate: this.id
            },
            execute: this.id,
            render: this.id,
            onevent: this.lazyResponse.bind(this),
            onerror: this.lazyError.bind(this)
          });
    } else if (this.filter) {
      const filterFunction = TobagoFilterRegistry.get(this.filter);
      this.optionsControls.filter(filterFunction, searchString);
      this.showNoEntriesHint = this.optionsControls.visibleRows.length === 0;
    }
  }

  private lazyResponse(event: faces.AjaxEvent): void {
    const updates: NodeListOf<Element> = event.responseXML?.querySelectorAll("update");
    if (updates && event.status === "complete") {
      for (const update of updates) {
        const id = update.getAttribute("id");
        if (this.id === id) { // is a JSF element id, but not a technical id from the framework
          update.id = update.id + SelectListBase.SUFFIX_FILTER_UPDATE; //hide from faces.js
          this.filterUpdateLoader = document.createElement("div");
          this.filterUpdateLoader.innerHTML = update.textContent;
        }
      }
    } else if (updates && event.status === "success") {
      for (const update of updates) {
        const id = update.getAttribute("id");
        if (this.id + SelectListBase.SUFFIX_FILTER_UPDATE === id) {
          this.lastSuccessfulSearchQuery = this.hiddenQueryInput.value;

          const newHiddenSelect = this.filterUpdateLoader.querySelector<HTMLSelectElement>("select");
          const oldHiddenSelect = this.hiddenSelect;
          oldHiddenSelect.insertAdjacentElement("beforebegin", newHiddenSelect);
          oldHiddenSelect.remove();

          const oldOptions = this.options;
          const newOptions = this.filterUpdateLoader
              .querySelector<HTMLElement>(`.tobago-options[data-tobago-for='${this.id}'] table`);
          oldOptions.insertAdjacentElement("beforebegin", newOptions);
          oldOptions.remove();

          const oldBehaviors = this.querySelectorAll("tobago-behavior");
          const newBehaviors = this.filterUpdateLoader.querySelectorAll("tobago-behavior");
          oldBehaviors.forEach((oldBehavior, index) => {
            oldBehavior.insertAdjacentElement("beforebegin", newBehaviors.item(index));
            oldBehavior.remove();
          });

          this.optionsControls.disconnect();
          this.optionsControls = new OptionsControls(this.options, this.select.bind(this));

          this.filterUpdateLoader.remove();
        }
      }

      this.listeners.cleanup();
      this.listeners.add(this.hiddenSelect, "click", this.labelClickEvent.bind(this));
      this.listeners.add(this.options, "keydown", this.keydownEventBase.bind(this));
      this.optionsControls.rows.forEach(row => this.optionsControls
          .listeners.add(row, "blur", this.blurEvent.bind(this)));

      // redirect click events for ajax behavior
      this.listeners.add(this.options, "click", () => this.hiddenSelect.dispatchEvent(new Event("click")));
      this.listeners.add(this.options, "dblclick", () => this.hiddenSelect.dispatchEvent(new Event("dblclick")));

      this.hideSpinner();
    }
  }

  private lazyError(data: faces.AjaxError): void {
    console.error(`Select[One|Many]List filter loading error:
Error Name: ${data.errorName}
Error errorMessage: ${data.errorMessage}
Response Code: ${data.responseCode}
Response Text: ${data.responseText}
Status: ${data.status}
Type: ${data.type}`);
  }

  private showSpinner(): void {
    this.filterInput.style.marginRight = this.spinner.size + "px";
    this.spinner.show();
  }

  private hideSpinner(): void {
    this.spinner.hide();
    this.filterInput.style.marginRight = null;
  }

  protected abstract leaveComponent(): void;

  protected abstract select(row: HTMLTableRowElement): void;

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

  protected isPartOfTobagoOptions(element: HTMLElement): boolean {
    if (element) {
      if (element.classList.contains(Css.TOBAGO_OPTIONS)
          && this.id === element.dataset.tobagoFor) {
        return true;
      } else {
        return element.parentElement ? this.isPartOfTobagoOptions(element.parentElement) : false;
      }
    } else {
      return false;
    }
  }

  get disabled(): boolean {
    return this.classList.contains(Css.TOBAGO_DISABLED);
  }

  get focused(): boolean {
    return this.classList.contains(Css.TOBAGO_FOCUS);
  }

  set focused(focused: boolean) {
    if (focused && !this.focused) {
      this.classList.add(Css.TOBAGO_FOCUS);
      this.hiddenSelect.dispatchEvent(new Event("focus", {bubbles: true}));
    } else if (!focused && this.focused) {
      this.classList.remove(Css.TOBAGO_FOCUS);
      this.hiddenSelect.dispatchEvent(new Event("blur", {bubbles: true}));
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

  get dropdownMenuElement(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(`.${Css.TOBAGO_DROPDOWN_MENU}[data-tobago-for='${this.id}']`);
  }

  get options(): HTMLElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(`.tobago-options[data-tobago-for='${this.id}'] table`);
  }

  get noEntriesHint(): HTMLTableRowElement {
    return this.options.querySelector("." + Css.TOBAGO_NO_ENTRIES);
  }

  set showNoEntriesHint(show: boolean) {
    if (show) {
      this.noEntriesHint.classList.remove(Css.D_NONE);
    } else {
      this.noEntriesHint.classList.add(Css.D_NONE);
    }
  }

  get localMenu(): boolean {
    return this.hasAttribute("local-menu");
  }

  get hiddenQueryInput(): HTMLInputElement {
    return this.querySelector("input[id$='::query']");
  }

  get serverSideFiltering(): boolean {
    return !!this.hiddenQueryInput;
  }

  get delay(): number {
    const delay = this.hiddenQueryInput?.dataset.tobagoDelay;
    return delay ? parseInt(delay) : 0;
  }

  get minChars(): number {
    const minChars = this.hiddenQueryInput?.dataset.tobagoMinChars;
    return minChars ? parseInt(minChars) : 0;
  }

  private get spinnerDiv(): HTMLDivElement {
    return this.querySelector<HTMLDivElement>(`.${Css.SPINNER}`);
  }
}
