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
import {ClientBehaviors} from "./tobago-client-behaviors";

class TabGroup extends HTMLElement {

  private hiddenInput: HTMLInputElement;

  constructor() {
    super();
    this.hiddenInput = this.querySelector(":scope > input[type=hidden]");
  }

  get switchType(): string {
    return this.getAttribute("switch-type");
  }

  get tabs(): NodeListOf<Tab> {
    return this.querySelectorAll(":scope > .card-header > .card-header-tabs > tobago-tab");
  }

  getSelectedTab(): Tab {
    return this.querySelector(
        `:scope > .card-header > .card-header-tabs > tobago-tab[index='${this.selected}']`);
  }

  get selected(): number {
    return parseInt(this.hiddenInput.value);
  }

  set selected(index: number) {
    this.hiddenInput.value = String(index);
  }
}

export class Tab extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    const navLink = this.navLink;
    if (!navLink.classList.contains(Css.DISABLED)) {
      navLink.addEventListener("click", this.select.bind(this));
    }
    navLink.addEventListener("keydown", (event) => {
      if (event.key === "Enter" || event.key === " ") {
        event.preventDefault();
        navLink.click();
      }
    });
    navLink.addEventListener("focus", () => this.classList.add(Css.TOBAGO_FOCUS));
    navLink.addEventListener("blur", () => this.classList.remove(Css.TOBAGO_FOCUS));

    this.addEventListener("mouseenter", () => this.classList.add(Css.TOBAGO_HOVER));
    this.addEventListener("mouseleave", () => this.classList.remove(Css.TOBAGO_HOVER));

    const mutationObserver = new MutationObserver((mutations, observer) => {
      if (this.navLink.classList.contains(Css.ACTIVE)) {
        this.classList.add(Css.TOBAGO_ACTIVE);
      } else {
        this.classList.remove(Css.TOBAGO_ACTIVE);
      }
    });
    mutationObserver.observe(this.navLink, {attributes: true, attributeFilter: ["class"]});
  }

  get index(): number {
    return parseInt(this.getAttribute("index"));
  }

  get navLink(): HTMLLinkElement {
    return this.querySelector(".nav-link");
  }

  get barFacet(): HTMLDivElement {
    return this.querySelector(":scope > div");
  }

  get tabGroup(): TabGroup {
    return this.closest("tobago-tab-group");
  }

  select(event: MouseEvent): void {
    const tabGroup = this.tabGroup;
    const old = tabGroup.getSelectedTab();
    tabGroup.selected = this.index;
    const fireTabChange = old.index != this.index;
    switch (tabGroup.switchType) {
      case "client":
        old.navLink.classList.remove(Css.ACTIVE);
        old.navLink.tabIndex = 0;
        this.navLink.classList.add(Css.ACTIVE);
        this.navLink.tabIndex = -1;
        old.content.classList.remove(Css.ACTIVE);
        this.content.classList.add(Css.ACTIVE);
        break;
      case "reloadTab":
        if (fireTabChange) {
          this.fireTabChangeEvent(tabGroup);
        }
        // will be done by <tobago-behavior>
        break;
      case "reloadPage":
        if (fireTabChange) {
          this.fireTabChangeEvent(tabGroup);
        }
        break;
      case "none": // todo
        console.error("Not implemented yet: %s", tabGroup.switchType);
        break;
      default:
        console.error("Unknown switchType='%s'", tabGroup.switchType);
        break;
    }
  }

  private fireTabChangeEvent(tabGroup: TabGroup) {
    tabGroup.dispatchEvent(new CustomEvent(ClientBehaviors.TAB_CHANGE, {
      detail: {
        index: this.index
      },
    }));
  }

  get content(): HTMLElement {
    return this.closest("tobago-tab-group")
        .querySelector(`:scope > .card-body.tab-content > .tab-pane[data-index='${this.index}']`);
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-tab", Tab);
  window.customElements.define("tobago-tab-group", TabGroup);
});
