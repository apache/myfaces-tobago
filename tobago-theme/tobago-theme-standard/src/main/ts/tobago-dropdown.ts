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
import {Key} from "./tobago-key";
import {DropdownItem} from "./tobago-dropdown-item";
import {DropdownItemFactory} from "./tobago-dropdown-item-factory";
import {DropdownMenu, DropdownMenuAlignment} from "./tobago-dropdown-menu";
import {ClientBehaviors} from "./tobago-client-behaviors";
import {EventListenerStore} from "./util/EventListenerStore";
import {FocusableElement, tabbable} from "tabbable";

class Dropdown extends HTMLElement {
  private listeners: EventListenerStore = new EventListenerStore();
  private dropdownMenu: DropdownMenu;

  constructor() {
    super();
  }

  connectedCallback(): void {
    if (!this.classList.contains(Css.TOBAGO_DROPDOWN_SUBMENU)) { // ignore submenus
      this.dropdownMenu = new DropdownMenu(this.dropdownMenuElement, this.toggle, this, this.insideNavbar(),
          this.dropdownMenuElement.classList.contains(Css.DROPDOWN_MENU_END)
              ? DropdownMenuAlignment.end : DropdownMenuAlignment.start);
      this.listeners.add(document, "click", this.globalClickEvent.bind(this));
      this.listeners.add(this.toggle, "keydown", this.toggleButtonKeydownEvent.bind(this));
      this.listeners.add(this.dropdownMenuElement, "keydown", this.dropdownMenuKeydownEvent.bind(this));

      if (!this.hasPanelFacet()) {
        this.getAllDropdownItems(this.dropdownItems).forEach((dropdownItem) => {
          this.listeners.add(dropdownItem.element, "focus", this.focusEvent.bind(this));
          if (dropdownItem.children) {
            this.listeners.add(dropdownItem.element, "mouseenter", this.mouseenterEvent.bind(this));
          }
        });

        this.listeners.add(this, ClientBehaviors.DROPDOWN_HIDDEN, this.dropdownHidden.bind(this));
      }
    }
  }

  disconnectedCallback(): void {
    this.listeners.disconnect();
    this.dropdownMenu?.disconnect();
  }

  private globalClickEvent(event: MouseEvent): void {
    if (!this.toggle.disabled) {
      const element = (event.target as HTMLElement);
      if (element.isConnected) { /* ignore all clicks on elements which are not connected to the DOM (e.g. remove badge
                                   button from SelectManyList component */
        const isToggleButton = this.isPartOfRootToggleButton(element);

        if (isToggleButton) {
          this.toggle.focus(); //Workaround for Safari
        }

        if (this.isPartOfDropdown(element)) {
          if (this.autoClose === "outside") {
            if (this.expanded) {
              if (isToggleButton) {
                this.dropdownMenu.hide();
              }
            } else {
              this.dropdownMenu.show();
            }
          } else {
            if (element.getAttribute("for") !== null || element.tagName === "INPUT") {
              /* do nothing if for-label, because click event triggers a second time on input element
              do nothing if input element, because dropdown menu should stay open */
            } else if (element.querySelector<HTMLInputElement>(":scope.dropdown-item > input") !== null) {
              element.querySelector<HTMLInputElement>(":scope.dropdown-item > input")?.click();
            } else if (this.getSubMenuToggle(element as HTMLElement) !== null) {
              const subMenuToggle = this.getSubMenuToggle(element as HTMLElement);
              subMenuToggle.click();
            } else if (this.expanded) {
              this.dropdownMenu.hide();
            } else {
              this.dropdownMenu.show();
            }
          }
        } else {
          this.dropdownMenu.hide();
        }
      }
    }
  }

  private isPartOfDropdown(element: HTMLElement): boolean {
    if (element) {
      const tobagoFor = element.dataset.tobagoFor;
      if (tobagoFor) {
        return this.isPartOfDropdown(document.getElementById(tobagoFor));
      } else if (this.id === element.id) {
        return true;
      } else {
        return element.parentElement ? this.isPartOfDropdown(element.parentElement) : false;
      }
    } else {
      return false;
    }
  }

  private isPartOfRootToggleButton(element: HTMLElement): boolean {
    if (element) {
      if (this.toggle.id === element.id) {
        return true;
      } else {
        return element.parentElement ? this.isPartOfRootToggleButton(element.parentElement) : false;
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

  private toggleButtonKeydownEvent(event: KeyboardEvent): void {
    switch (event.key) {
      case Key.ARROW_DOWN:
        if (this.hasPanelFacet()) {
          if (!this.expanded) {
            event.preventDefault(); //prevent scrolling the dropdown menu
            this.dropdownMenu.show();
            this.firstFocusableElement.focus();
          }
        } else {
          event.preventDefault(); //prevent click event if radio button is selected
          this.dropdownMenu.show();
          this.getNextDropdownItem()?.focus();
        }
        break;
      case Key.ARROW_UP:
        if (this.hasPanelFacet()) {
          if (!this.expanded) {
            event.preventDefault(); //prevent scrolling the dropdown menu
            this.dropdownMenu.show();
            this.lastFocusableElement.focus();
          }
        } else {
          event.preventDefault(); //prevent click event if radio button is selected
          this.dropdownMenu.show();
          this.getPreviousDropdownItem()?.focus();
        }
        break;
      case Key.ESCAPE:
        this.dropdownMenu.hide();
        break;
      case Key.TAB:
        if (this.expanded) {
          event.preventDefault();
          if (this.hasPanelFacet()) {
            const focusableElements: FocusableElement[] = tabbable(this.dropdownMenuElement);
            const firstFocusableElement = focusableElements[0];
            const lastFocusableElement = focusableElements[focusableElements.length - 1];
            if (event.shiftKey) {
              lastFocusableElement.focus();
            } else {
              firstFocusableElement.focus();
            }
          } else {
            if (event.shiftKey) {
              this.getPreviousDropdownItem()?.focus();
            } else {
              this.getNextDropdownItem()?.focus();
            }
          }
        }
        break;
      default:
        break;
    }
  }

  private dropdownMenuKeydownEvent(event: KeyboardEvent): void {
    switch (event.key) {
      case Key.ARROW_DOWN:
        if (this.hasPanelFacet()) {
          if (!this.expanded) {
            event.preventDefault(); //prevent scrolling the dropdown menu
            this.dropdownMenu.show();
            this.firstFocusableElement.focus();
          }
        } else {
          event.preventDefault(); //prevent click event if radio button is selected
          this.dropdownMenu.show();
          this.getNextDropdownItem()?.focus();
        }
        break;
      case Key.ARROW_UP:
        if (this.hasPanelFacet()) {
          if (!this.expanded) {
            event.preventDefault(); //prevent scrolling the dropdown menu
            this.dropdownMenu.show();
            this.lastFocusableElement.focus();
          }
        } else {
          event.preventDefault(); //prevent click event if radio button is selected
          this.dropdownMenu.show();
          this.getPreviousDropdownItem()?.focus();
        }
        break;
      case Key.ARROW_RIGHT:
        if (!this.hasPanelFacet()) {
          event.preventDefault(); //prevent click event if radio button is selected
          this.getSubmenuDropdownItem()?.focus();
        }
        break;
      case Key.ARROW_LEFT:
        if (!this.hasPanelFacet()) {
          event.preventDefault(); //prevent click event if radio button is selected
          this.getParentDropdownItem()?.focus();
        }
        break;
      case Key.ESCAPE:
        this.toggle.focus();
        this.dropdownMenu.hide();
        break;
      case Key.TAB:
        if (this.hasPanelFacet()) {
          const focusableElements: FocusableElement[] = tabbable(this.dropdownMenuElement);
          const firstFocusableElement = focusableElements[0];
          const lastFocusableElement = focusableElements[focusableElements.length - 1];
          if (event.shiftKey) {
            if (document.activeElement === firstFocusableElement
                || (document.activeElement === this.toggle && this.expanded)
            ) {
              event.preventDefault();
              lastFocusableElement.focus();
            }
          } else {
            if (document.activeElement === lastFocusableElement
                || (document.activeElement === this.toggle && this.expanded)) {
              event.preventDefault();
              firstFocusableElement.focus();
            }
          }
        } else {
          if (this.expanded) {
            event.preventDefault();
            if (event.shiftKey) {
              this.getPreviousDropdownItem()?.focus();
            } else {
              this.getNextDropdownItem()?.focus();
            }
          }
        }
        break;
      default:
        break;
    }
  }

  private dropdownHidden(): void {
    this.getAllDropdownItems(this.dropdownItems).forEach((dropdownItem) => dropdownItem.hideSubMenu());
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

  /**
   * If the tobago-dropdown has a panel-facet, the menu does contain a *form* not *dropdown-items*.
   * The dropdown menu should stay open if a component inside the *form* is clicked.
   * @private
   */
  private hasPanelFacet(): boolean {
    return this.dropdownMenuElement.querySelector(":scope > .tobago-panel-facet") !== null;
  }

  get toggle(): HTMLButtonElement {
    return this.querySelector(".dropdown-toggle");
  }

  get expanded(): boolean {
    return this.toggle.ariaExpanded === "true";
  }

  get autoClose(): string {
    return this.getAttribute("data-tobago-auto-close");
  }

  get dropdownItems(): DropdownItem[] {
    return DropdownItemFactory.create(this.dropdownMenuElement);
  }

  get dropdownMenuElement(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(`.${Css.TOBAGO_DROPDOWN_MENU}[name='${this.id}']`);
  }

  get firstFocusableElement(): HTMLElement | SVGElement {
    const focusableElements: FocusableElement[] = tabbable(this.dropdownMenuElement);
    return focusableElements.length > 0 ? focusableElements[0] : null;
  }

  get lastFocusableElement(): HTMLElement | SVGElement {
    const focusableElements: FocusableElement[] = tabbable(this.dropdownMenuElement);
    return focusableElements.length > 0 ? focusableElements[focusableElements.length - 1] : null;
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-dropdown") == null) {
    window.customElements.define("tobago-dropdown", Dropdown);
  }
});
