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

export abstract class SelectListBase extends HTMLElement {

  private static readonly SUFFIX_FILTER_UPDATE: string = "::filter-update";

  protected dropdownMenu: DropdownMenu;
  private timeout: number;
  private filterUpdateLoader: HTMLElement;
  protected lastSuccessfulSearchQuery: string;

  connectedCallback(): void {
    if (this.dropdownMenuElement) {
      this.dropdownMenu = new DropdownMenu(this.dropdownMenuElement, this.selectField, this, this.localMenu,
          DropdownMenuAlignment.centerFullWidth);
    }
    document.addEventListener("click", this.globalClickEvent.bind(this));
    this.hiddenSelect.addEventListener("click", this.labelClickEvent.bind(this));
    this.selectField.addEventListener("keydown", this.keydownEventBase.bind(this));
    this.filterInput.addEventListener("focus", this.focusEvent.bind(this));
    this.filterInput.addEventListener("blur", this.blurEvent.bind(this));
    this.options.addEventListener("keydown", this.keydownEventBase.bind(this));
    this.rows.forEach(row => row.addEventListener("blur", this.blurEvent.bind(this)));
    if (this.filter || this.serverSideFiltering) {
      this.filterInput.addEventListener("input", this.filterInputEvent.bind(this));
    }
    if (this.serverSideFiltering) {
      this.insertAdjacentHTML("beforeend", `<div class="${Css.SPINNER}"/>`);
    }
    this.tbody.addEventListener("click", this.tbodyClickEvent.bind(this));

    // handle autofocus; trigger focus event
    if (document.activeElement.id === this.filterInput.id) {
      this.focusEvent();
    }

    // redirect click events for ajax behavior
    this.selectField.addEventListener("click", () => this.hiddenSelect.dispatchEvent(new Event("click")));
    this.options.addEventListener("click", () => this.hiddenSelect.dispatchEvent(new Event("click")));
    this.selectField.addEventListener("dblclick", () => this.hiddenSelect.dispatchEvent(new Event("dblclick")));
    this.options.addEventListener("dblclick", () => this.hiddenSelect.dispatchEvent(new Event("dblclick")));
  }

  disconnectedCallback(): void {
    this.dropdownMenu?.disconnect();
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

  private keydownEventBase(event: KeyboardEvent) {
    switch (event.key) {
      case Key.ESCAPE:
        this.dropdownMenu?.hide();
        this.removePreselection();
        this.filterInput.focus({preventScroll: true});
        break;
      case Key.ARROW_DOWN:
        event.preventDefault();
        this.dropdownMenu?.show();
        this.preselectNextRow();
        break;
      case Key.ARROW_UP:
        event.preventDefault();
        this.dropdownMenu?.show();
        this.preselectPreviousRow();
        break;
      case Key.ENTER:
      case Key.SPACE:
        if (this.preselectedRow) {
          event.preventDefault();
          const row = this.tbody.querySelector<HTMLTableRowElement>("." + Css.TOBAGO_PRESELECT);
          this.select(row);
        } else if (document.activeElement.id === this.filterInput.id) {
          this.dropdownMenu?.show();
        }
        break;
      case Key.TAB:
        this.removePreselection();
        this.filterInput.focus({preventScroll: true});
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
          && !this.isPartOfTobagoOptions(event.relatedTarget as Element)) {
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
      faces.ajax.request(
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
    } else {
      const filterFunction = TobagoFilterRegistry.get(this.filter);
      // XXX todo: if filterFunction not found?

      let entriesCount = 0;
      if (filterFunction != null) {
        this.rows.forEach(row => {
          const itemValue = row.cells.item(0).textContent;
          if (filterFunction(itemValue, searchString)) {
            row.classList.remove(Css.D_NONE);
            entriesCount++;
          } else {
            row.classList.add(Css.D_NONE);
            row.classList.remove(Css.TOBAGO_PRESELECT);
          }
        });
      }

      if (entriesCount === 0) {
        this.noEntriesHint.classList.remove(Css.D_NONE);
      } else {
        this.noEntriesHint.classList.add(Css.D_NONE);
      }
    }
  }

  private lazyResponse(event: EventData): void {
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
              .querySelector<HTMLElement>(`.tobago-options[name='${this.id}'] table`);
          oldOptions.insertAdjacentElement("beforebegin", newOptions);
          oldOptions.remove();

          const oldBehaviors = this.querySelectorAll("tobago-behavior");
          const newBehaviors = this.filterUpdateLoader.querySelectorAll("tobago-behavior");
          oldBehaviors.forEach((oldBehavior, index) => {
            oldBehavior.insertAdjacentElement("beforebegin", newBehaviors.item(index));
            oldBehavior.remove();
          });

          this.filterUpdateLoader.remove();
        }
      }

      this.hiddenSelect.addEventListener("click", this.labelClickEvent.bind(this));
      this.options.addEventListener("keydown", this.keydownEventBase.bind(this));
      this.rows.forEach(row => row.addEventListener("blur", this.blurEvent.bind(this)));
      this.tbody.addEventListener("click", this.tbodyClickEvent.bind(this));

      // redirect click events for ajax behavior
      this.options.addEventListener("click", () => this.hiddenSelect.dispatchEvent(new Event("click")));
      this.options.addEventListener("dblclick", () => this.hiddenSelect.dispatchEvent(new Event("dblclick")));

      this.hideSpinner();
    }
  }

  private lazyError(data: ErrorData): void {
    console.error(`Select[One|Many]List filter loading error:
Error Name: ${data.errorName}
Error errorMessage: ${data.errorMessage}
Response Code: ${data.responseCode}
Response Text: ${data.responseText}
Status: ${data.status}
Type: ${data.type}`);
  }

  private showSpinner(): void {
    const rect = this.selectField.getBoundingClientRect();
    const cStyle = getComputedStyle(this.selectField);
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

    this.filterInput.style.marginRight = size + "px";
  }

  private hideSpinner(): void {
    this.spinner.classList.remove(Css.TOBAGO_SHOW);
    this.filterInput.style.marginRight = null;
  }

  private tbodyClickEvent(event: MouseEvent): void {
    const target = event.target as HTMLElement;
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
    row.focus();
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
    return root.querySelector(`.${Css.TOBAGO_DROPDOWN_MENU}[name='${this.id}']`);
  }

  get options(): HTMLElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(`.tobago-options[name='${this.id}'] table`);
  }

  get tbody(): HTMLElement {
    return this.options.querySelector("tbody");
  }

  get noEntriesHint(): HTMLTableRowElement {
    return this.options.querySelector("." + Css.TOBAGO_NO_ENTRIES);
  }

  get rows(): NodeListOf<HTMLTableRowElement> {
    return this.tbody.querySelectorAll<HTMLTableRowElement>("tr");
  }

  get enabledRows(): NodeListOf<HTMLTableRowElement> {
    return this.tbody.querySelectorAll<HTMLTableRowElement>("tr:not(." + Css.D_NONE + "):not(." + Css.DISABLED);
  }

  get preselectedRow(): HTMLTableRowElement {
    return this.tbody.querySelector<HTMLTableRowElement>("." + Css.TOBAGO_PRESELECT);
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

  private get spinner(): HTMLDivElement {
    return this.querySelector<HTMLDivElement>(`.${Css.SPINNER}`);
  }
}
