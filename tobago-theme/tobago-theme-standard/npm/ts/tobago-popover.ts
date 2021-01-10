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

class Popover extends HTMLElement {

  private popper: Popper;

  constructor() {
    super();
  }

  connectedCallback(): void {
    this.button.addEventListener("click", this.showPopover.bind(this));
    this.button.addEventListener("blur", this.hidePopover.bind(this));
  }

  showPopover(): void {
    this.menuStore.appendChild(this.popover);
    this.popper = new Popper(this.button, this.popover, {
      placement: "right",
      modifiers: {
        arrow: {
          element: ".popover-arrow"
        }
      },
      onCreate: this.updateBootstrapPopoverCss.bind(this),
      onUpdate: this.updateBootstrapPopoverCss.bind(this)
    });
    this.popover.classList.add("show");
  }

  hidePopover(): void {
    this.popover.classList.remove("show");
    this.appendChild(this.popover);

    if (this.popper !== undefined && this.popper !== null) {
      this.popper.destroy();
      this.popper = null;
    }
  }

  private updateBootstrapPopoverCss(): void {
    const placement = this.popover.getAttribute("x-placement");
    if (placement === "right" && !this.popover.classList.contains("bs-popover-end")) {
      this.popover.classList.add("bs-popover-end");
      this.popover.classList.remove("bs-popover-start");
      this.updateAfterCssClassChange();
    } else if (placement === "left" && !this.popover.classList.contains("bs-popover-start")) {
      this.popover.classList.add("bs-popover-start");
      this.popover.classList.remove("bs-popover-end");
      this.updateAfterCssClassChange();
    }
  }

  private updateAfterCssClassChange(): void {
    if (this.popper !== undefined && this.popper !== null) {
      this.popper.scheduleUpdate();
    }
  }

  get button(): HTMLLinkElement {
    return this.querySelector(":scope > .tobago-popover-button");
  }

  get popover(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(".tobago-popover-box[name='" + this.id + "']");
  }

  private get menuStore(): HTMLDivElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector(".tobago-page-menuStore");
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-popover") == null) {
    window.customElements.define("tobago-popover", Popover);
  }
});
