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
    // the click should not sort the column of a table - XXX not very nice - may look for a better solution
    if (this.closest("tr") != null) {
      this.addEventListener("click", (event) => {
        event.stopPropagation();
      });
    }
  }

  openDropdown(): void {
    this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.SHOW));

    if (!this.insideNavbar()) {
      MenuStore.appendChild(this.dropdownMenu);
    }

    this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.SHOWN));
  }

  closeDropdown(): void {
    this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.HIDE));
    if (!this.insideNavbar()) {
      this.appendChild(this.dropdownMenu);
    }
    this.dispatchEvent(new CustomEvent(TobagoDropdownEvent.HIDDEN));
  }

  /**
   * The bootstrap dropdown implementation doesn't adjust the position of the dropdown menu if inside a '.navbar'.
   * In this case the dropdown menu should not be appended to the menu store.
   * https://github.com/twbs/bootstrap/blob/0d81d3cbc14dfcdca8a868e3f25189a4f1ab273c/js/src/dropdown.js#L294
   */
  private insideNavbar(): boolean {
    return Boolean(this.closest(".navbar"));
  }

  private get dropdownMenu(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(`.dropdown-menu[name='${this.id}']`);
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-dropdown") == null) {
    window.customElements.define("tobago-dropdown", Dropdown);
  }
});
