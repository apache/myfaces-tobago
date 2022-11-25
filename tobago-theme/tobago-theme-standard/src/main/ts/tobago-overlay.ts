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

/**
 * Create a overlay barrier and animate it.
 */

import {Page} from "./tobago-page";

// XXX issue: if a ajax call is scheduled on the same element, the animation arrow will stacking and not desapearing.
// XXX issue: "error" is not implemented correctly
// see http://localhost:8080/demo-5-snapshot/content/140-partial/Partial_Ajax.xhtml to use this feature
// XXX todo: check full page transitions

export class Overlay extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    setTimeout(this.render.bind(this), this.delay);
  }

  disconnectedCallback() {
    console.log("disconnected from the DOM");
    const forElement = document.getElementById(this.for);
    if (forElement) {
      forElement.classList.remove("position-relative");
    }
  }

  render(): void {
    let icon;
    switch (this.type) {
      case "error":
        icon = "<i class='bi-flash fs-1'></i>";
        break;
      case "drop-zone":
        icon = "<i class='bi-upload fs-1'></i>";
        break;
      case "wait":
        icon = "<span class='spinner-border'></span>";
        break;
      default:
        icon = "";
    }

    this.insertAdjacentHTML("afterbegin", `<div>${icon}</div>`);

    const forElement = document.getElementById(this.for);
    if (forElement) {
      forElement.classList.add("position-relative");
    }
  }

  get for(): string {
    return this.getAttribute("for");
  }

  set for(forString: string) {
    this.setAttribute("for", forString);
  }

  /**
   * Examples:
   * wait, error, ajax, drop-zone
   */
  get type(): string {
    let attribute = this.getAttribute("type");
    if (attribute) {
      return attribute;
    } else {
      return "wait";
    }
  }

  set type(type: string) {
    this.setAttribute("type", type);
  }

  /**
   * The delay for the wait overlay. If not set the default delay is read from config.
   */
  get delay(): number {
    if (this.hasAttribute("delay")) {
      return parseInt(this.getAttribute("delay"));
    } else if (this.type === "ajax") {
      return Page.page(this).waitOverlayDelayAjax;
    } else {
      return Page.page(this).waitOverlayDelayFull;
    }
  }

  set delay(delay: number) {
    this.setAttribute("delay", String(delay));
  }

}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-overlay") == null) {
    window.customElements.define("tobago-overlay", Overlay);
  }
});
