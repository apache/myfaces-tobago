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

import {Listener, Phase} from "./tobago-listener";
import {DomUtils} from "./tobago-utils";

/**
 * Initializes the tab-groups.
 * @param element
 */
class TabGroup {
  static init = function (element: HTMLElement) {

    const markupString = "selected";
    const markupCssClass = "tobago-tab-markup-selected";

    for (const e of DomUtils.selfOrQuerySelectorAll(element, ".tobago-tabGroup")) {
      const tabGroup: HTMLDivElement = e as HTMLDivElement;
      const hiddenInput: HTMLInputElement = TabGroup.getHiddenInput(tabGroup);
      const tabs: NodeListOf<HTMLLIElement> = TabGroup.getTabs(tabGroup);

      for (const tab of tabs) {
        const navLink: HTMLLinkElement = tab.querySelector(":scope > .nav-link");
        if (!navLink.classList.contains("disabled")) {
          navLink.addEventListener('click', function () {
            const tabGroupIndex: string = tab.dataset.tobagoTabGroupIndex;
            hiddenInput.value = tabGroupIndex;

            if (tabGroup.dataset.tobagoSwitchType === "client") {
              const activeTab: HTMLLIElement = TabGroup.getActiveTab(tabGroup);
              const activeNavLink: HTMLLinkElement = TabGroup.getActiveNavLink(tabGroup);
              const activeTabContent: HTMLDivElement = TabGroup.getActiveTabContent(tabGroup);

              if (activeTab.dataset.tobagoMarkup !== undefined) {
                const markups: Set<string> = new Set(JSON.parse(activeTab.dataset.tobagoMarkup));
                markups.delete(markupString);
                activeTab.dataset.tobagoMarkup = JSON.stringify(Array.from(markups));
              }
              activeTab.classList.remove(markupCssClass);
              activeNavLink.classList.remove("active");
              activeTabContent.classList.remove("active");

              const markup: string = tab.dataset.tobagoMarkup;
              const markups: Set<string> = markup ? new Set(JSON.parse(markup)) : new Set();
              markups.add(markupString);
              tab.dataset.tobagoMarkup = JSON.stringify(Array.from(markups));
              tab.classList.add(markupCssClass);
              navLink.classList.add("active");
              TabGroup.getTabContent(tabGroup, tabGroupIndex).classList.add("active");
            }
          });
        }
      }
    }
  };

  static getHiddenInput(tabGroup: HTMLDivElement): HTMLInputElement {
    return tabGroup.querySelector(":scope > input[type=hidden]");
  }

  static getTabs(tabGroup: HTMLDivElement): NodeListOf<HTMLLIElement> {
    return tabGroup.querySelectorAll(":scope > .card-header > .tobago-tabGroup-header > .tobago-tab");
  }

  static getActiveTab(tabGroup: HTMLDivElement): HTMLLIElement {
    return this.getActiveNavLink(tabGroup).parentElement as HTMLLIElement;
  }

  static getActiveNavLink(tabGroup: HTMLDivElement): HTMLLinkElement {
    return tabGroup.querySelector(":scope > .card-header > .tobago-tabGroup-header > .tobago-tab > .nav-link.active");
  }

  static getActiveTabContent(tabGroup: HTMLDivElement): HTMLDivElement {
    return tabGroup.querySelector(":scope > .card-body.tab-content > .tobago-tab-content.active");
  }

  static getTabContent(tabGroup: HTMLDivElement, tabGroupIndex: string): HTMLDivElement {
    return tabGroup.querySelector(":scope > .card-body > .tobago-tab-content[data-tobago-tab-group-index='"
        + tabGroupIndex + "']");
  }
}

Listener.register(TabGroup.init, Phase.DOCUMENT_READY);
Listener.register(TabGroup.init, Phase.AFTER_UPDATE);
