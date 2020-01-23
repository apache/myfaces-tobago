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

class Dropdown extends HTMLElement {

  private _deselectComponent = this.deselectComponent.bind(this);
  private _toggleDropdown = this.toggleDropdown.bind(this);
  private _setCloseFlag = this.setCloseFlag.bind(this);
  private closeFlag: boolean = false;

  constructor() {
    super();
  }

  connectedCallback(): void {
    this.toggleButton.addEventListener("mouseup", this._toggleDropdown);
    this.toggleButton.addEventListener("blur", this._setCloseFlag);
    window.addEventListener("mouseup", this._deselectComponent);
    //TODO add keyboard support
  }

  disconnectedCallback(): void {
    this.toggleButton.removeEventListener("mouseup", this._toggleDropdown);
    this.toggleButton.removeEventListener("blur", this._setCloseFlag);
    window.removeEventListener("mouseup", this._deselectComponent);
  }

  toggleDropdown(event: Event): void {
    this.resetCloseFlag();

    const visible: boolean = this.dropdownMenu.classList.contains("show");
    if (visible) {
      this.closeDropdown();
    } else {
      this.openDropdown();
    }
  }

  deselectComponent(event: Event): void {
    const visible: boolean = this.dropdownMenu.classList.contains("show");
    if (this.closeFlag && visible) {
      this.resetCloseFlag();

      const target: HTMLElement = event.target as HTMLElement;
      this.closeDropdown();
      target.dispatchEvent(new MouseEvent("click", {bubbles: true}));
    }
  }

  openDropdown(): void {
    if (!this.inStickyHeader()) {
      this.menuStore.appendChild(this.dropdownMenu);
      new Popper(this.toggleButton, this.dropdownMenu, {
        placement: "bottom-start"
      });
    }

    this.dropdownMenu.classList.add("show");
  }

  closeDropdown(): void {
    this.dropdownMenu.classList.remove("show");
    this.appendChild(this.dropdownMenu);
  }

  private get toggleButton(): HTMLElement {
    return this.querySelector(":scope > button[data-toggle='dropdown']");
  }

  private inStickyHeader(): boolean {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector("header.tobago-header.sticky-top tobago-dropdown[id='" + this.id + "']") !== null;
  }

  private get dropdownMenu(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(".dropdown-menu[name='" + this.id + "']");
  }

  private get menuStore(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(".tobago-page-menuStore");
  }

  setCloseFlag(event: Event): void {
    this.closeFlag = true;
  }

  resetCloseFlag(): void {
    this.closeFlag = false;
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-dropdown", Dropdown);
});
