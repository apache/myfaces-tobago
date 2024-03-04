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

import {Popover} from "bootstrap";

enum SanitizeMode {
  auto, never
}

class TobagoPopover extends HTMLElement {

  private instance: Popover;

  constructor() {
    super();
  }

  connectedCallback(): void {
    this.instance = new Popover(this.baseElement, {
      container: this.popoverStore,
      customClass: this.customClass,
      title: this.label,
      content: this.value,
      html: !this.escape,
      sanitize: this.sanitize === SanitizeMode.auto,
      trigger: "focus"
    });
  }

  get baseElement(): HTMLElement {
    if (this.parentElement?.tagName === "A" || this.parentElement?.tagName === "BUTTON") {
      return this.parentElement;
    } else {
      return this;
    }
  }

  get popoverStore(): HTMLDivElement {
    const root = document.getRootNode() as ShadowRoot | Document;
    return root.querySelector<HTMLDivElement>(".tobago-page-popoverStore");
  }

  get customClass(): string {
    const customClass = this.getAttribute("custom-class");
    return customClass ? customClass : "";
  }

  get label(): string {
    const label = this.getAttribute("label");
    return label ? label : "";
  }

  get value(): string {
    return this.getAttribute("value");
  }

  get escape(): boolean {
    return this.getAttribute("escape") !== "false";
  }

  get sanitize(): SanitizeMode {
    return SanitizeMode[this.getAttribute("sanitize")];
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-popover") == null) {
    window.customElements.define("tobago-popover", TobagoPopover);
  }
});
