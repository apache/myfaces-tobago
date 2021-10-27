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
    if (!navLink.classList.contains("disabled")) {
      navLink.addEventListener("click", this.select.bind(this));
    }
  }

  get index(): number {
    return parseInt(this.getAttribute("index"));
  }

  get navLink(): HTMLLinkElement {
    return this.querySelector(".nav-link");
  }

  get tabGroup(): TabGroup {
    return this.closest("tobago-tab-group");
  }

  select(event: MouseEvent): void {
    const tabGroup = this.tabGroup;
    const old = tabGroup.getSelectedTab();
    tabGroup.selected = this.index;

    switch (tabGroup.switchType) {
      case "client":
        old.navLink.classList.remove("active");
        this.navLink.classList.add("active");
        old.content.classList.remove("active");
        this.content.classList.add("active");
        break;
      case "reloadTab":
        // will be done by <tobago-behavior>
        break;
      case "reloadPage":
        // will be done by <tobago-behavior>
        break;
      case "none": // todo
        console.error("Not implemented yet: %s", tabGroup.switchType);
        break;
      default:
        console.error("Unknown switchType='%s'", tabGroup.switchType);
        break;
    }
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
