/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import {Css} from "./tobago-css";
import {DropdownItemFactory} from "./tobago-dropdown-item-factory";

export class DropdownItem {
  private _element: HTMLElement;

  constructor(element: HTMLElement) {
    this._element = element;
  }

  get ancestors(): DropdownItem[] {
    const ancestors: DropdownItem[] = [];
    let dropdownItem: DropdownItem = this as DropdownItem;
    while (dropdownItem != null) {
      ancestors.push(dropdownItem);
      dropdownItem = dropdownItem.parent;
    }
    return ancestors;
  }

  get children(): DropdownItem[] {
    const dropdownMenu = this.dropdownMenu;
    if (dropdownMenu) {
      return DropdownItemFactory.create(dropdownMenu);
    }
  }

  get element(): HTMLElement {
    return this._element;
  }

  get disabled(): boolean {
    const input = this.element.querySelector("input");
    return input ? input.disabled : this.element.getAttribute("disabled") === "disabled";
  }

  get focused(): boolean {
    const root = this.element.getRootNode() as ShadowRoot | Document;
    return root.activeElement === this.element;
  }

  get parent(): DropdownItem {
    const dropdownItemElement: HTMLElement = this.element
        .closest(`.${Css.TOBAGO_DROPDOWN_MENU}`)
        ?.closest("tobago-dropdown")
        ?.querySelector(":scope." + Css.TOBAGO_DROPDOWN_SUBMENU + " > ." + Css.DROPDOWN_ITEM);
    return dropdownItemElement ? new DropdownItem(dropdownItemElement) : null;
  }

  get siblings(): DropdownItem[] {
    const dropdownMenu: HTMLDivElement = this.element.closest(`.${Css.TOBAGO_DROPDOWN_MENU}`);
    if (dropdownMenu) {
      return DropdownItemFactory.create(dropdownMenu);
    }
  }

  private get dropdownMenu(): HTMLDivElement {
    return this.element.parentElement.querySelector(":scope > ." + Css.TOBAGO_DROPDOWN_MENU);
  }

  focus(options?: FocusOptions): void {
    this.element.focus(options);
  }

  showSubMenu(): void {
    if (!this.disabled && this.dropdownMenu) {
      this.element.parentElement.classList.add(Css.TOBAGO_SHOW);
    }
  }

  hideSubMenu(): void {
    if (this.dropdownMenu) {
      this.element.parentElement.classList.remove(Css.TOBAGO_SHOW);
    }
  }
}
