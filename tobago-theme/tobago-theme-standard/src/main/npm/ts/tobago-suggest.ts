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

import Autocomplete from "@trevoreyre/autocomplete-js/Autocomplete.js";

export class Suggest extends HTMLElement {

  autocomplete: Autocomplete;

  get hiddenInput(): HTMLInputElement {
    return this.querySelector(":scope > input[type=hidden]");
  }

  get suggestInput(): HTMLInputElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.getElementById(this.for) as HTMLInputElement;
  }

  get base(): HTMLElement {
    return this.suggestInput.closest("tobago-in");
  }

  get for(): string {
    return this.getAttribute("for");
  }

  set for(forValue: string) {
    this.setAttribute("for", forValue);
  }

  get items(): string[] {
    return JSON.parse(this.getAttribute("items"));
  }

  constructor() {
    super();
  }

  connectedCallback(): void {
    console.log("* autocomplete init *********************************************************************");

    this.base.classList.add("autocomplete");
    this.suggestInput.classList.add("autocomplete-input");

    this.suggestInput.insertAdjacentHTML("afterend", `<ul class="autocomplete-result-list"></ul>`);

    const options = {
      search: input => {
        console.debug("input = '" + input + "'");
        if (input.length < 1) {
          return [];
        }
        const inputLower = input.toLowerCase();
        let strings = this.items.filter(country => {
          return country.toLowerCase().startsWith(inputLower);
        });
        console.debug("out   = '" + strings + "'");
        return strings;
      }
    };
    this.autocomplete = new Autocomplete(this.base, options);

    console.log(this.autocomplete);
  }

}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-suggest", Suggest);
});
