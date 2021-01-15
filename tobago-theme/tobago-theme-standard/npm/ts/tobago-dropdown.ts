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

/**
 * The dropdown implementation of Bootstrap does not support submenus. Therefore we need an own dropdown implementation.
 */
class Dropdown extends HTMLElement {

  private dropdownEntries: DropdownEntry[] = [];

  constructor() {
    super();
    if (!this.classList.contains("tobago-dropdown-submenu")) { // ignore submenus
      const root = this.getRootNode() as ShadowRoot | Document;

      this.createDropdownEntries(this.dropdownMenu, null);

      this.toggleButton.addEventListener("click", this.toggleDropdown.bind(this));
      root.addEventListener("mouseup", this.mouseupOnDocument.bind(this));
      root.addEventListener("keydown", this.keydownOnDocument.bind(this));
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
    if (!this.toggleButtonSelected(event) && this.dropdownVisible()
        && !this.dropdownMenu.contains(event.target as HTMLElement)) {
      this.closeDropdown();
    }
  }

  keydownOnDocument(event: KeyboardEvent): void {
    if (this.toggleButtonSelected(event) && !this.dropdownVisible()
        && (event.code === "ArrowUp" || event.code === "ArrowDown")) {
      event.preventDefault();
      event.stopPropagation();
      this.openDropdown();

      const interval = setInterval(() => {
        if (this.dropdownVisible()) {

          if (this.activeDropdownEntry) {
            this.activeDropdownEntry.focus();
          } else {
            this.dropdownEntries[0].focus();
          }
          clearInterval(interval);
        }
      }, 0);
    } else if (this.dropdownVisible()
        && (event.code === "ArrowUp" || event.code === "ArrowDown"
            || event.code === "ArrowLeft" || event.code === "ArrowRight"
            || event.code === "Tab")) {
      event.preventDefault();
      event.stopPropagation();

      if (!this.activeDropdownEntry) {
        this.dropdownEntries[0].focus();
      } else if (event.code === "ArrowUp" && this.activeDropdownEntry.previous) {
        this.activeDropdownEntry.previous.focus();
      } else if (event.code === "ArrowDown" && this.activeDropdownEntry.next) {
        this.activeDropdownEntry.next.focus();
      } else if (event.code === "ArrowRight" && this.activeDropdownEntry.children.length > 0) {
        this.activeDropdownEntry.children[0].focus();
      } else if (event.code === "ArrowLeft" && this.activeDropdownEntry.parent) {
        this.activeDropdownEntry.parent.focus();
      } else if (!event.shiftKey && event.code === "Tab") {
        if (this.activeDropdownEntry.children.length > 0) {
          this.activeDropdownEntry.children[0].focus();
        } else if (this.activeDropdownEntry.next) {
          this.activeDropdownEntry.next.focus();
        } else {
          let parent: DropdownEntry = this.activeDropdownEntry.parent;
          while (parent) {
            if (parent.next) {
              this.activeDropdownEntry.clear();
              parent.next.focus();
              break;
            } else {
              parent = parent.parent;
            }
          }
        }
      } else if (event.shiftKey && event.code === "Tab") {
        if (this.activeDropdownEntry.previous) {
          this.activeDropdownEntry.previous.focus();
        } else if (this.activeDropdownEntry.parent) {
          this.activeDropdownEntry.parent.focus();
        }
      }
    } else if (this.dropdownVisible() && event.code === "Escape") {
      event.preventDefault();
      event.stopPropagation();
      this.closeDropdown();
    }
  }

  openDropdown(): void {
    this.dispatchEvent(new CustomEvent(Event.SHOW));

    if (!this.inStickyHeader()) {
      this.menuStore.appendChild(this.dropdownMenu);
      new Popper(this.toggleButton, this.dropdownMenu, {
        placement: "bottom-start"
      });
    }

    for (const dropdownEntry of this.dropdownEntries) {
      dropdownEntry.clear();
    }

    this.dropdownMenu.classList.add("show");
    this.dispatchEvent(new CustomEvent(Event.SHOWN));
  }

  closeDropdown(): void {
    this.dispatchEvent(new CustomEvent(Event.HIDE));
    this.dropdownMenu.classList.remove("show");
    this.appendChild(this.dropdownMenu);
    this.dispatchEvent(new CustomEvent(Event.HIDDEN));
  }

  private get toggleButton(): HTMLElement {
    return this.querySelector(":scope > button[data-toggle='dropdown']");
  }

  private toggleButtonSelected(event: Event): boolean {
    return this.toggleButton.contains(event.target as HTMLElement);
  }

  private inStickyHeader(): boolean {
    return Boolean(this.closest("tobago-header.sticky-top"));
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

  private get activeDropdownEntry(): DropdownEntry {
    for (const dropdownEntry of this.dropdownEntries) {
      if (dropdownEntry.active) {
        return dropdownEntry;
      }
    }
    return null;
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
  private _active: boolean;

  constructor(dropdownItem: HTMLElement) {
    this._baseElement = dropdownItem;
    if (dropdownItem.classList.contains("tobago-dropdown-submenu")) {
      this.focusElement = dropdownItem.querySelector(".tobago-link");
    } else if (dropdownItem.tagName === "LABEL") {
      const root = dropdownItem.getRootNode() as ShadowRoot | Document;
      this.focusElement = root.getElementById(dropdownItem.getAttribute("for"));
    } else {
      this.focusElement = dropdownItem;
    }

    this._baseElement.addEventListener("mouseenter", this.activate.bind(this));
    this._baseElement.addEventListener("mouseleave", this.deactivate.bind(this));
  }

  activate(event: MouseEvent): void {
    this.active = true;
  }

  deactivate(event: MouseEvent): void {
    this.active = false;
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

  get active(): boolean {
    return this._active;
  }

  set active(value: boolean) {
    this._active = value;
  }

  public focus(): void {
    this.previous?.clear();
    this.next?.clear();
    if (this.parent) {
      this.parent.active = false;
      this.parent._baseElement.classList.add("tobago-dropdown-open");
    }
    for (const child of this.children) {
      child.clear();
    }
    this._baseElement.classList.remove("tobago-dropdown-open");
    this._baseElement.classList.add("tobago-dropdown-selected");
    this.active = true;
    this.focusElement.focus();
  }

  public clear(): void {
    this._baseElement.classList.remove("tobago-dropdown-open");
    this._baseElement.classList.remove("tobago-dropdown-selected");
    this.active = false;
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-dropdown") == null) {
    window.customElements.define("tobago-dropdown", Dropdown);
  }
});
