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

class TobagoScroll extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    const text = this.hiddenElement.value;
    const values = JSON.parse(text) as number[];
    if (values.length === 2) {
      this.parentElement.scrollLeft = values[0];
      this.parentElement.scrollTop = values[1];
    } else {
      console.warn("Syntax error for scroll position: ", text);
    }

    this.parentElement.addEventListener("scroll", this.storeScrollPosition.bind(this));
  }

  storeScrollPosition(event: Event): void {
    const panel = event.currentTarget as HTMLElement;
    const scrollLeft = panel.scrollLeft;
    const scrollTop = panel.scrollTop;
    this.hiddenElement.value = JSON.stringify([scrollLeft, scrollTop]);
  }

  get hiddenElement(): HTMLInputElement {
    return this.querySelector("input");
  }

}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-scroll") == null) {
    window.customElements.define("tobago-scroll", TobagoScroll);
  }
});
