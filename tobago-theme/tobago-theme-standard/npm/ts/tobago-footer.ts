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

class Footer extends HTMLElement {

  private lastMaxFooterHeight: number;

  constructor() {
    super();
  }

  connectedCallback(): void {
    this.lastMaxFooterHeight = 0;

    if(this.isFixed) {
      window.addEventListener("resize", this.resize.bind(this));

      if (this.body) {
        this.setMargins();
      }
    }
  }

  private resize(event: MouseEvent): void {
    const maxFooterHeight: number = DomUtils.outerHeightWithMargin(this);

    if (maxFooterHeight !== this.lastMaxFooterHeight) {
      this.setMargins();
      this.lastMaxFooterHeight = maxFooterHeight;
    }
  }

  private setMargins(): void {
    if (this.isFixed) {
      const maxFooterHeight = DomUtils.outerHeightWithMargin(this);

      if (maxFooterHeight > 0) {
        this.body.style.marginBottom = maxFooterHeight + "px";
      }
    }
  }

  private get body(): HTMLElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.querySelector("body");
  }

  private get isFixed(): boolean {
    return this.classList.contains("fixed-bottom");
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-footer") == null) {
    window.customElements.define("tobago-footer", Footer);
  }
});
