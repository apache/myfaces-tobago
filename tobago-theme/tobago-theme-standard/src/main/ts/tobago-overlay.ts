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

import {Config} from "./tobago-config";

// XXX issue: if a ajax call is scheduled on the same element, the animation arrow will stacking and not desapearing.
// XXX issue: "error" is not implemented correctly
// see http://localhost:8080/demo-5-snapshot/content/140-partial/Partial_Ajax.xhtml to use this feature
// XXX todo: check full page transitions

Config.set("Tobago.waitOverlayDelay", 1000);
Config.set("Ajax.waitOverlayDelay", 1000);

class Overlay extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    setTimeout(this.render.bind(this), this.delay);
  }

  render(): void {
    const icon = this.error
        ? "<i class='bi-flash fs-1'></i>"
        : "<span class='spinner-border'></span>";

    this.insertAdjacentHTML("afterbegin", `<div>${icon}</div>`);
  }

  get for(): string {
    return this.getAttribute("for");
  }

  set for(forString: string) {
    this.setAttribute("for", forString);
  }

  /**
   * Is this overlay for an AJAX request, or an normal submit?
   * We need this information, because AJAX need to clone the animated image, but for a normal submit
   * we must not clone it, because the animation stops in some browsers.
   */
  get ajax(): boolean {
    return this.hasAttribute("ajax");
  }

  set ajax(ajax: boolean) {
    if (ajax) {
      this.setAttribute("ajax", "");
    } else {
      this.removeAttribute("ajax");
    }
  }

  /**
   * This boolean indicates, if the overlay is "error" or "wait".
   */
  get error(): boolean {
    return this.hasAttribute("error");
  }

  set error(error: boolean) {
    if (error) {
      this.setAttribute("error", "");
    } else {
      this.removeAttribute("error");
    }
  }

  /**
   * The delay for the wait overlay. If not set the default delay is read from Tobago.Config.
   */
  get delay(): number {
    if (this.hasAttribute("delay")) {
      return parseInt(this.getAttribute("delay"));
    } else {
      return Config.get(this.ajax ? "Ajax.waitOverlayDelay" : "Tobago.waitOverlayDelay");
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
