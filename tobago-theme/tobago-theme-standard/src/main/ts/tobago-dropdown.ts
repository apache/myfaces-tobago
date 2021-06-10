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

  constructor() {
    super();
    if (!this.classList.contains("tobago-dropdown-submenu")) { // ignore submenus
      this.addEventListener("shown.bs.dropdown", this.openDropdown.bind(this));
      this.addEventListener("hidden.bs.dropdown", this.closeDropdown.bind(this));
    }
  }

  openDropdown(): void {
    this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.SHOW));

    if (!this.inStickyHeader()) {
      this.menuStore.appendChild(this.dropdownMenu);
    }

    this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.SHOWN));
  }

  closeDropdown(): void {
    this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.HIDE));
    if (!this.inStickyHeader()) {
      this.appendChild(this.dropdownMenu);
    }
    this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.HIDDEN));
  }

  private inStickyHeader(): boolean {
    return Boolean(this.closest("tobago-header.sticky-top"));
  }

  private get dropdownMenu(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(".dropdown-menu[name='" + this.id + "']");
  }

  private get menuStore(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(".tobago-page-menuStore");
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-dropdown") == null) {
    window.customElements.define("tobago-dropdown", Dropdown);
  }
});
