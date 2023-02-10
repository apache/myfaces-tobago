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

class Footer extends HTMLElement {

  private lastMaxFooterHeight: number;

  constructor() {
    super();
  }

  get fixed(): boolean {
    return this.classList.contains(Css.FIXED_BOTTOM);
  }

  get height(): number {
    const style: CSSStyleDeclaration = getComputedStyle(this);
    return this.offsetHeight + Number.parseInt(style.marginTop) + Number.parseInt(style.marginBottom);
  }

  connectedCallback(): void {
    if (this.fixed) {
      this.adjustMargin();
      window.addEventListener("resize", this.adjustMargin.bind(this));
    }
  }

  private adjustMargin(event?: Event): void {
    if (this.hasBiggestFixedFooterHeight()) {
      const root = this.getRootNode() as ShadowRoot | Document;
      const body = root.querySelector<HTMLBodyElement>("body");
      body.style.marginBottom = `${this.height}px`;
    }
  }

  private hasBiggestFixedFooterHeight(): boolean {
    const root = this.getRootNode() as ShadowRoot | Document;
    const footers = root.querySelectorAll<Footer>("tobago-footer");
    for (const footer of footers) {
      if (footer.fixed && footer.height > this.height) {
        return false;
      }
    }
    return true;
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-footer") == null) {
    window.customElements.define("tobago-footer", Footer);
  }
});
