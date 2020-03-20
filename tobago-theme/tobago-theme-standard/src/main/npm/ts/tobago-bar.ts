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

import {DomUtils} from "./tobago-utils";

class Bar extends HTMLElement {

  private readonly CssClass = {
    SHOW: "show",
    COLLAPSE: "collapse",
    COLLAPSING: "collapsing"
  };
  private readonly ariaExpanded: string = "aria-expanded";
  private timeout: number;
  private expanded: boolean;

  constructor() {
    super();
    this.toggleButton.addEventListener("click", this.toggleCollapse.bind(this));
  }

  connectedCallback(): void {
    this.expanded = this.toggleButton.getAttribute(this.ariaExpanded) === "true";
  }

  private toggleCollapse(event: MouseEvent): void {
    window.clearTimeout(this.timeout);

    if (this.expanded) {
      this.expanded = false;
      this.navbarContent.style.height = this.navbarContent.scrollHeight + "px";
      this.navbarContent.offsetHeight; //force reflow, to make sure height is set
      this.navbarContent.classList.add(this.CssClass.COLLAPSING);
      this.navbarContent.classList.remove(this.CssClass.COLLAPSE);
      this.navbarContent.classList.remove(this.CssClass.SHOW);
      this.navbarContent.style.height = null;

      this.timeout = window.setTimeout(() => {
        this.navbarContent.classList.remove(this.CssClass.COLLAPSING);
        this.navbarContent.classList.add(this.CssClass.COLLAPSE);
        this.toggleButton.setAttribute(this.ariaExpanded, "false");
      }, DomUtils.getTransitionTime(this.navbarContent));
    } else {
      this.expanded = true;
      this.navbarContent.classList.remove(this.CssClass.COLLAPSE);
      this.navbarContent.classList.add(this.CssClass.COLLAPSING);
      this.navbarContent.style.height = this.navbarContent.scrollHeight + "px";

      this.timeout = window.setTimeout(() => {
        this.navbarContent.classList.remove(this.CssClass.COLLAPSING);
        this.navbarContent.classList.add(this.CssClass.COLLAPSE);
        this.navbarContent.classList.add(this.CssClass.SHOW);
        this.navbarContent.style.height = null;
        this.toggleButton.setAttribute(this.ariaExpanded, "true");
      }, DomUtils.getTransitionTime(this.navbarContent));
    }
  }

  private get toggleButton(): HTMLButtonElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(".navbar-toggler");
  }

  private get navbarContent(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(".navbar-collapse");
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-bar", Bar);
});
