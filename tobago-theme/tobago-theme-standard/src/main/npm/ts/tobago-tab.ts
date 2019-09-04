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

import {Command} from "./tobago-command";

class TabGroup extends HTMLElement {
  private markupCssClass: string = "tobago-tab-markup-selected";

  constructor() {
    super();
  }

  connectedCallback() {
    const tabGroup: TabGroup = this;
    const hiddenInput: HTMLInputElement = this.querySelector(":scope > input[type=hidden]");

    for (const tab of tabGroup.tabs) {
      const navLink: HTMLLinkElement = tab.querySelector(":scope > .nav-link");
      if (!navLink.classList.contains("disabled")) {
        navLink.addEventListener('click', function () {
          hiddenInput.value = tab.groupIndex;

          if (tabGroup.dataset.tobagoSwitchType === "client") {
            tabGroup.activeTab.classList.remove(tabGroup.markupCssClass);
            tabGroup.activeNavLink.classList.remove("active");
            tabGroup.activeTabContent.classList.remove("active");

            tab.classList.add(tabGroup.markupCssClass);
            navLink.classList.add("active");
            tabGroup.getTabContent(tab.groupIndex).classList.add("active");
          }
        });
      }
    }

    Command.init(tabGroup, true);
  }

  get tabs(): NodeListOf<Tab> {
    return this.querySelectorAll("tobago-tab[for='" + this.id + "']");
  }

  get activeTab(): Tab {
    return this.querySelector("tobago-tab[for='" + this.id + "']." + this.markupCssClass);
  }

  get activeNavLink(): HTMLLinkElement {
    return this.querySelector("tobago-tab[for='" + this.id + "'] > .nav-link.active");
  }

  get activeTabContent(): HTMLDivElement {
    return this.querySelector(":scope > .card-body.tab-content > .tobago-tab-content.active");
  }

  getTabContent(tabGroupIndex: string): HTMLDivElement {
    return this.querySelector(":scope > .card-body > .tobago-tab-content[data-tobago-tab-group-index='"
        + tabGroupIndex + "']");
  }
}

class Tab extends HTMLElement {
  constructor() {
    super();
  }

  connectedCallback() {
  }

  get groupIndex(): string {
    return this.dataset.tobagoTabGroupIndex;
  }
}

document.addEventListener("DOMContentLoaded", function (event) {
  window.customElements.define('tobago-tab-group', TabGroup);
  window.customElements.define('tobago-tab', Tab);
});
