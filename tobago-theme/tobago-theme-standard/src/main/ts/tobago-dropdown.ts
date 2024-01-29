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

import {MenuStore} from "./tobago-menu-store";
import {Css} from "./tobago-css";
import {Key} from "./tobago-key";
import {createPopper, Instance} from "@popperjs/core";
import {DropdownItem} from "./tobago-dropdown-item";
import {DropdownItemFactory} from "./tobago-dropdown-item-factory";

const TobagoDropdownEvent = {
  HIDE: "tobago.dropdown.hide",
  HIDDEN: "tobago.dropdown.hidden",
  SHOW: "tobago.dropdown.show",
  SHOWN: "tobago.dropdown.shown"
};

/**
 * The dropdown implementation of Bootstrap does not move the menu to the tobago-page-menuStore. This behavior is
 * implemented in this class.
 */
class Dropdown extends HTMLElement {

  private popper: Instance;
  private globalClickEventListener: EventListenerOrEventListenerObject;

  constructor() {
    super();
  }

  connectedCallback(): void {
    if (!this.classList.contains(Css.TOBAGO_DROPDOWN_SUBMENU)) { // ignore submenus
      this.popper = createPopper(this.toggle, this.dropdownMenu, {
        placement: this.dropdownMenu.classList.contains(Css.DROPDOWN_MENU_END) ? "bottom-end" : "bottom-start",
      });
      this.globalClickEventListener = this.globalClickEvent.bind(this) as EventListenerOrEventListenerObject;
      document.addEventListener("click", this.globalClickEventListener);
      this.addEventListener("keydown", this.keydownEvent.bind(this));
      this.dropdownMenu.addEventListener("keydown", this.keydownEvent.bind(this));
      this.toggle.addEventListener("keydown", this.tabHandling.bind(this));
      this.getAllDropdownItems(this.dropdownItems).forEach((dropdownItem) => {
        dropdownItem.element.addEventListener("focus", this.focusEvent.bind(this));
        if (dropdownItem.children) {
          dropdownItem.element.addEventListener("mouseenter", this.mouseenterEvent.bind(this));
        }
      });
    }
    // the click should not sort the column of a table - XXX not very nice - may look for a better solution
    if (this.closest("tr") != null) {
      this.addEventListener("click", (event) => {
        event.stopPropagation();
        if (this.expanded) {
          this.hideDropdown();
        } else {
          this.showDropdown();
        }
      });
    }
  }

  disconnectedCallback(): void {
    document.removeEventListener("click", this.globalClickEventListener);
    MenuStore.get().querySelector(`:scope > .${Css.TOBAGO_DROPDOWN_MENU}[name='${this.id}']`)?.remove();
  }

  get toggle(): HTMLButtonElement {
    return this.querySelector(".dropdown-toggle");
  }

  get expanded(): boolean {
    return this.toggle.ariaExpanded === "true";
  }

  get dropdownItems(): DropdownItem[] {
    return DropdownItemFactory.create(this.dropdownMenu);
  }

  get dropdownMenu(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(`.${Css.TOBAGO_DROPDOWN_MENU}[name='${this.id}']`);
  }

  private globalClickEvent(event: MouseEvent): void {
    if (!this.toggle.disabled) {
      const element = (event.target as Element);
      if (this.isPartOfDropdown(event.target as Element)) {
        if (element.getAttribute("for") !== null || element.tagName === "INPUT") {
          /* do nothing if for-label, because click event triggers a second time on input element
          do nothing if input element, because dropdown menu should stay open */
        } else if (element.querySelector<HTMLInputElement>(":scope.dropdown-item > input") !== null) {
          element.querySelector<HTMLInputElement>(":scope.dropdown-item > input")?.click();
        } else if (this.getSubMenuToggle(element as HTMLElement) !== null) {
          const subMenuToggle = this.getSubMenuToggle(element as HTMLElement);
          subMenuToggle.click();
        } else if (this.expanded) {
          this.hideDropdown();
        } else {
          this.showDropdown();
        }
      } else {
        this.hideDropdown();
      }
    }
  }

  private isPartOfDropdown(element: Element): boolean {
    if (element) {
      if (this.id === element.id || this.id === element.getAttribute("name")) {
        return true;
      } else {
        return element.parentElement ? this.isPartOfDropdown(element.parentElement) : false;
      }
    } else {
      return false;
    }
  }

  private getSubMenuToggle(element: HTMLElement): HTMLElement {
    if (element && element.classList.contains(Css.DROPDOWN_ITEM)
        && element.parentElement && element.parentElement.classList.contains(Css.TOBAGO_DROPDOWN_SUBMENU)) {
      return element;
    } else if (element.parentElement) {
      return this.getSubMenuToggle(element.parentElement);
    } else {
      return null;
    }
  }

  private keydownEvent(event: KeyboardEvent): void {
    switch (event.key) {
      case Key.ARROW_DOWN:
        event.preventDefault(); //prevent click event if radio button is selected
        this.showDropdown();
        this.getNextDropdownItem()?.focus();
        break;
      case Key.ARROW_UP:
        event.preventDefault(); //prevent click event if radio button is selected
        this.showDropdown();
        this.getPreviousDropdownItem()?.focus();
        break;
      case Key.ARROW_RIGHT:
        event.preventDefault(); //prevent click event if radio button is selected
        this.getSubmenuDropdownItem()?.focus();
        break;
      case Key.ARROW_LEFT:
        event.preventDefault(); //prevent click event if radio button is selected
        this.getParentDropdownItem()?.focus();
        break;
      case Key.ESCAPE:
        this.hideDropdown();
        break;
      default:
        break;
    }
  }

  private tabHandling(event: KeyboardEvent): void {
    if (event.key === Key.TAB) {
      if (event.shiftKey) {
        this.hideDropdown();
      } else if (this.dropdownMenu.classList.contains(Css.SHOW)) {
        event.preventDefault(); //avoid selecting second element
        this.getNextDropdownItem()?.focus();
      }
    }
  }

  private showDropdown(): void {
    this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.SHOW));

    // use Css.SHOW instead of Css.TOBAGO_SHOW for first dropdown menu to use bootstrap CSS
    if (!this.insideNavbar() && !this.dropdownMenu.classList.contains(Css.SHOW)) {
      MenuStore.appendChild(this.dropdownMenu);
    }
    this.toggle.classList.add(Css.SHOW);
    this.toggle.ariaExpanded = "true";
    this.dropdownMenu.classList.add(Css.SHOW);
    this.popper.update();

    this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.SHOWN));
  }

  private hideDropdown(): void {
    this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.HIDE));

    // use Css.SHOW instead of Css.TOBAGO_SHOW for first dropdown menu to use bootstrap CSS
    this.toggle.classList.remove(Css.SHOW);
    this.toggle.ariaExpanded = "false";
    this.dropdownMenu.classList.remove(Css.SHOW);
    this.getAllDropdownItems(this.dropdownItems).forEach((dropdownItem) => dropdownItem.hideSubMenu());
    if (!this.insideNavbar()) {
      this.appendChild(this.dropdownMenu);
    }

    this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.HIDDEN));
  }

  private getNextDropdownItem(): DropdownItem {
    const focusedDropdownItem = this.getFocusedDropdownItem(this.dropdownItems);
    if (focusedDropdownItem) {
      const enabledDropdownItems = this.getEnabledDropdownItems(focusedDropdownItem.siblings);
      if (enabledDropdownItems.length > 0) {
        const index = enabledDropdownItems.findIndex((value) => value.element === focusedDropdownItem.element);
        const newIndex = index === -1 ? 0 : index + 1;
        if (newIndex >= 0 && newIndex < enabledDropdownItems.length) {
          return enabledDropdownItems[newIndex];
        } else {
          return enabledDropdownItems[0];
        }
      }
    } else {
      const enabledDropdownItems = this.getEnabledDropdownItems(this.dropdownItems);
      if (enabledDropdownItems.length > 0) {
        return enabledDropdownItems[0];
      }
    }
  }

  private getPreviousDropdownItem(): DropdownItem {
    const focusedDropdownItem = this.getFocusedDropdownItem(this.dropdownItems);
    if (focusedDropdownItem) {
      const enabledDropdownItems = this.getEnabledDropdownItems(focusedDropdownItem.siblings);
      if (enabledDropdownItems.length > 0) {
        const index = enabledDropdownItems.findIndex((value) => value.element === focusedDropdownItem.element);
        const newIndex = index === -1 ? 0 : index - 1;
        if (newIndex >= 0 && newIndex < enabledDropdownItems.length) {
          return enabledDropdownItems[newIndex];
        } else {
          return enabledDropdownItems[enabledDropdownItems.length - 1];
        }
      }
    } else {
      const enabledDropdownItems = this.getEnabledDropdownItems(this.dropdownItems);
      if (enabledDropdownItems.length > 0) {
        return enabledDropdownItems[enabledDropdownItems.length - 1];
      }
    }
  }

  private getSubmenuDropdownItem(): DropdownItem {
    const focusedDropdownItems = this.getFocusedDropdownItem(this.dropdownItems);
    if (focusedDropdownItems && focusedDropdownItems.children) {
      const enabledDropdownItems = this.getEnabledDropdownItems(focusedDropdownItems.children);
      if (enabledDropdownItems.length > 0) {
        return enabledDropdownItems[0];
      }
    }
  }

  private getParentDropdownItem(): DropdownItem {
    const focusedDropdownItems = this.getFocusedDropdownItem(this.dropdownItems);
    if (focusedDropdownItems) {
      return focusedDropdownItems.parent;
    }
  }

  private focusEvent(event: FocusEvent): void {
    const dropdownItem = new DropdownItem(event.target as HTMLElement);
    this.handleSubMenuShowState(dropdownItem);
  }

  private mouseenterEvent(event: MouseEvent): void {
    const dropdownItem = new DropdownItem(event.target as HTMLElement);
    this.handleSubMenuShowState(dropdownItem);
  }

  private handleSubMenuShowState(dropdownItem: DropdownItem) {
    const ancestors: DropdownItem[] = dropdownItem.ancestors;
    const allDropdownItems: DropdownItem[] = this.getAllDropdownItems(this.dropdownItems);
    allDropdownItems.forEach((dropdownItem) => {
      if (ancestors.find((ancestor) => ancestor.element === dropdownItem.element)) {
        dropdownItem.showSubMenu();
      } else {
        dropdownItem.hideSubMenu();
      }
    });
  }

  private getFocusedDropdownItem(dropdownItems: DropdownItem[]): DropdownItem {
    if (dropdownItems) {
      for (const dropdownItem of dropdownItems) {
        const focusedDropdownItem = this.getFocusedDropdownItem(dropdownItem.children);
        if (focusedDropdownItem != null) {
          return focusedDropdownItem;
        } else if (dropdownItem.focused) {
          return dropdownItem;
        }
      }
    }
  }

  private getEnabledDropdownItems(dropdownItems: DropdownItem[]): DropdownItem[] {
    const enabledDropdownItems: DropdownItem[] = [];
    dropdownItems.forEach((dropdownItem) => {
      if (!dropdownItem.disabled) {
        enabledDropdownItems.push(dropdownItem);
      }
    });
    return enabledDropdownItems;
  }

  private getAllDropdownItems(dropdownItems: DropdownItem[]): DropdownItem[] {
    let allDropdownItems: DropdownItem[] = [];
    dropdownItems.forEach((dropdownItem) => {
      allDropdownItems.push(dropdownItem);
      if (dropdownItem.children) {
        allDropdownItems = allDropdownItems.concat(this.getAllDropdownItems(dropdownItem.children));
      }
    });
    return allDropdownItems;
  }

  /**
   * The bootstrap dropdown implementation doesn't adjust the position of the dropdown menu if inside a '.navbar'.
   * In this case the dropdown menu should not be appended to the menu store.
   * https://github.com/twbs/bootstrap/blob/0d81d3cbc14dfcdca8a868e3f25189a4f1ab273c/js/src/dropdown.js#L294
   */
  private insideNavbar(): boolean {
    return Boolean(this.closest(".navbar"));
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-dropdown") == null) {
    window.customElements.define("tobago-dropdown", Dropdown);
  }
});
