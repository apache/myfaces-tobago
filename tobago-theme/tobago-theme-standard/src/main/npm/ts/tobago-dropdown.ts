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

import Popper from "popper.js";

const Event = {
  HIDE: "tobago.dropdown.hide",
  HIDDEN: "tobago.dropdown.hidden",
  SHOW: "tobago.dropdown.show",
  SHOWN: "tobago.dropdown.shown"
};

class Dropdown extends HTMLElement {

  private dropdownEntries: DropdownEntry[] = [];
  private activeDropdownEntry: DropdownEntry;

  constructor() {
    super();
    if (!this.classList.contains("tobago-dropdown-submenu")) { // ignore submenus
      const root = this.getRootNode() as ShadowRoot | Document;

      this.createDropdownEntries(this.dropdownMenu, null);

      this.toggleButton.addEventListener("click", this.toggleDropdown.bind(this));
      root.addEventListener("mouseup", this.mouseupOnDocument.bind(this));
      root.addEventListener("keydown", this.keydownOnDocument.bind(this));
      root.addEventListener("keyup", this.keyupOnDocument.bind(this));
    }
  }

  connectedCallback(): void {
  }

  toggleDropdown(event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    if (this.dropdownVisible()) {
      this.closeDropdown();
    } else {
      this.openDropdown();
    }
  }

  mouseupOnDocument(event: MouseEvent): void {
    if (!this.toggleButtonSelected() && this.dropdownVisible()) {
      this.closeDropdown();
    }
  }

  keydownOnDocument(event: KeyboardEvent): void {
    if (this.dropdownVisible() && event.code === "Escape") {
      event.preventDefault();
      event.stopPropagation();
      this.closeDropdown();
    } else if ((this.toggleButtonSelected() || this.dropdownVisible())
        && (event.code === "ArrowUp" || event.code === "ArrowDown"
            || event.code === "ArrowLeft" || event.code === "ArrowRight")) {
      // prevent scrolling with arrow keys
      event.preventDefault();
      event.stopPropagation();
    }
  }

  keyupOnDocument(event: KeyboardEvent): void {
    const root = this.getRootNode() as ShadowRoot | Document;

    if (this.toggleButtonSelected() && !this.dropdownVisible()
        && (event.code === "ArrowUp" || event.code === "ArrowDown")) {
      event.preventDefault();
      event.stopPropagation();
      this.openDropdown();
      this.activeDropdownEntry = this.dropdownEntries[0];

      const interval = setInterval(() => {
        if (this.dropdownVisible()) {
          this.activeDropdownEntry.focus();
          clearInterval(interval);
        }
      }, 0);
    } else if (this.activeDropdownEntry && this.dropdownVisible()) {
      if (event.code === "ArrowUp" && this.activeDropdownEntry.previous) {
        this.activeDropdownEntry.clearCss();
        this.activeDropdownEntry = this.activeDropdownEntry.previous;
        this.activeDropdownEntry.focus();
      } else if (event.code === "ArrowDown" && this.activeDropdownEntry.next) {
        this.activeDropdownEntry.clearCss();
        this.activeDropdownEntry = this.activeDropdownEntry.next;
        this.activeDropdownEntry.focus();
      } else if (event.code === "ArrowRight" && this.activeDropdownEntry.children.length > 0) {
        this.activeDropdownEntry = this.activeDropdownEntry.children[0];
        this.activeDropdownEntry.focus();
      } else if (event.code === "ArrowLeft" && this.activeDropdownEntry.parent) {
        this.activeDropdownEntry.clearCss();
        this.activeDropdownEntry = this.activeDropdownEntry.parent;
        this.activeDropdownEntry.clearCss();
        this.activeDropdownEntry.focus();
      }
    }
  }

  openDropdown(): void {
    this.dispatchEvent(new CustomEvent(Event.HIDE));

    if (!this.inStickyHeader()) {
      this.menuStore.appendChild(this.dropdownMenu);
      new Popper(this.toggleButton, this.dropdownMenu, {
        placement: "bottom-start"
      });
    }

    for (const dropdownEntry of this.dropdownEntries) {
      dropdownEntry.clearCss();
    }

    this.dropdownMenu.classList.add("show");
    this.dispatchEvent(new CustomEvent(Event.HIDDEN));
  }

  closeDropdown(): void {
    this.dispatchEvent(new CustomEvent(Event.SHOW));
    this.dropdownMenu.classList.remove("show");
    this.appendChild(this.dropdownMenu);
    this.dispatchEvent(new CustomEvent(Event.SHOWN));
  }

  private get toggleButton(): HTMLElement {
    return this.querySelector(":scope > button[data-toggle='dropdown']");
  }

  private toggleButtonSelected(): boolean {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.activeElement === this.toggleButton;
  }

  private inStickyHeader(): boolean {
    const root = this.getRootNode() as ShadowRoot | Document;
    return Boolean(root.querySelector("header.tobago-header.sticky-top tobago-dropdown[id='" + this.id + "']"));
  }

  private get dropdownMenu(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(".dropdown-menu[name='" + this.id + "']");
  }

  private dropdownVisible(): boolean {
    return this.dropdownMenu.classList.contains("show");
  }

  private get menuStore(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(".tobago-page-menuStore");
  }

  private createDropdownEntries(dropdownMenu: HTMLDivElement, parent: DropdownEntry): void {
    let lastDropdownEntry: DropdownEntry = null;

    for (const dropdownItem of dropdownMenu.children) {
      if (dropdownItem.classList.contains("dropdown-item")) {
        const entry = this.createDropdownEntry(dropdownItem as HTMLElement, parent, lastDropdownEntry);

        lastDropdownEntry = entry;
        this.dropdownEntries.push(entry);

        if (dropdownItem.classList.contains("tobago-dropdown-submenu")) {
          this.createDropdownEntries(dropdownItem.querySelector(".dropdown-menu"), entry);
        }
      } else {
        const dropdownItems: NodeListOf<HTMLElement> = dropdownItem.querySelectorAll(".dropdown-item");
        for (const dropdownItem of dropdownItems) {
          const entry = this.createDropdownEntry(dropdownItem, parent, lastDropdownEntry);

          lastDropdownEntry = entry;
          this.dropdownEntries.push(entry);
        }
      }
    }
  }

  private createDropdownEntry(
      dropdownItem: HTMLElement, parent: DropdownEntry, previous: DropdownEntry): DropdownEntry {

    const entry = new DropdownEntry(dropdownItem);
    if (parent) {
      entry.parent = parent;
      parent.children.push(entry);
    }

    if (previous) {
      previous.next = entry;
      entry.previous = previous;
    }

    return entry;
  }
}

class DropdownEntry {

  private _previous: DropdownEntry;
  private _next: DropdownEntry;
  private _parent: DropdownEntry;
  private _children: DropdownEntry[] = [];
  private readonly _baseElement: HTMLElement;
  private readonly focusElement: HTMLElement;

  constructor(dropdownItem: HTMLElement) {
    this._baseElement = dropdownItem;
    if (dropdownItem.classList.contains("tobago-dropdown-submenu")) {
      this.focusElement = dropdownItem.querySelector(".tobago-link");
    } else if (dropdownItem.tagName === "LABEL") {
      this.focusElement = dropdownItem.querySelector("input");
    } else {
      this.focusElement = dropdownItem;
    }
  }

  get previous(): DropdownEntry {
    return this._previous;
  }

  set previous(value: DropdownEntry) {
    this._previous = value;
  }

  get next(): DropdownEntry {
    return this._next;
  }

  set next(value: DropdownEntry) {
    this._next = value;
  }

  get parent(): DropdownEntry {
    return this._parent;
  }

  set parent(value: DropdownEntry) {
    this._parent = value;
  }

  get children(): DropdownEntry[] {
    return this._children;
  }

  set children(value: DropdownEntry[]) {
    this._children = value;
  }

  public focus(): void {
    if (this.parent) {
      this.parent._baseElement.classList.add("tobago-dropdown-open");
    }

    this._baseElement.classList.add("tobago-dropdown-selected");
    this.focusElement.focus();
  }

  public clearCss(): void {
    this._baseElement.classList.remove("tobago-dropdown-open");
    this._baseElement.classList.remove("tobago-dropdown-selected");
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-dropdown", Dropdown);
});
