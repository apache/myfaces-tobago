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

class TobagoRange extends HTMLElement {

  private timer: ReturnType<typeof setTimeout>;

  constructor() {
    super();
  }

  connectedCallback(): void {
    this.range.addEventListener("input", this.updateTooltip.bind(this));
    this.range.addEventListener("focus", this.updateTooltip.bind(this));
  }

  private get range(): HTMLInputElement {
    return this.querySelector("input[type=range]");
  }

  private get tooltip(): HTMLInputElement {
    return this.querySelector(".tobago-tooltip");
  }

  private updateTooltip(): void {
    this.tooltip.textContent = this.range.value;

    this.tooltip.style.top = String(this.range.offsetTop - this.tooltip.offsetHeight) + "px";
    this.tooltip.style.left = String(this.range.offsetLeft + this.range.offsetWidth / 2
      - this.tooltip.offsetWidth / 2) + "px";
    this.tooltip.classList.add("show");

    clearTimeout(this.timer);
    this.timer = setTimeout(() => {
      this.tooltip.classList.remove("show");
    }, 1000);
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-range") == null) {
    window.customElements.define("tobago-range", TobagoRange);
  }
});
