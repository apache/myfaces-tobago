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

class Footer extends HTMLElement {

  private lastMaxFooterHeight: number;

  constructor() {
    super();
  }

  connectedCallback(): void {
    if (this.isFixed) {
      // call now
      this.adjustMargin();
      // and after resize
      window.addEventListener("resize", this.adjustMargin.bind(this));
    }
  }

  private adjustMargin(event?: MouseEvent): void {
    const style: CSSStyleDeclaration = window.getComputedStyle(this);
    const maxFooterHeight = this.offsetHeight + Number.parseInt(style.marginTop) + Number.parseInt(style.marginBottom);
    if (maxFooterHeight !== this.lastMaxFooterHeight) {
      this.lastMaxFooterHeight = maxFooterHeight;
      this.closest("body").style.marginBottom = `${maxFooterHeight}px`;
    }
  }

  private isFixed(): boolean {
    return this.classList.contains("fixed-bottom");
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-footer") == null) {
    window.customElements.define("tobago-footer", Footer);
  }
});
