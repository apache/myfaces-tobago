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

import {SelectOneListbox} from "./tobago-select-one-listbox";

class SelectManyListbox extends SelectOneListbox {

  constructor() {
    super();
  }

  connectedCallback(): void {
    if (this.filter != null) {
      const input = this.filterInput;
      input.addEventListener("keyup", this.filterEvent.bind(this));
    }

    FilterRegistry.set("contains",
      (candidate: string, value: string): boolean =>
        candidate.indexOf(value) >= 0
    );

    FilterRegistry.set("prefix",
      (candidate: string, value: string): boolean =>
        candidate.indexOf(value) == 0
    );
  }

  filterEvent(event: Event): void {
    const input = event.currentTarget as HTMLInputElement;
    const searchString = input.value;
    console.info("searchString", searchString);
    const filterFunction = FilterRegistry.get(this.filter);
    // XXX todo: if filterFunction not found?
    if (filterFunction != null) {
      this.querySelectorAll("option").forEach(option => {
        if (filterFunction(option.textContent.toLowerCase(), searchString)) {
          option.classList.remove("d-none");
        } else {
          option.classList.add("d-none");
        }
      });
    }
  }

  get filter(): string {
    return this.getAttribute("filter");
  }

  get filterInput(): HTMLInputElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return <HTMLInputElement>root.getElementById(this.id + "::filter");
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-select-many-listbox") == null) {
    window.customElements.define("tobago-select-many-listbox", SelectManyListbox);
  }
});

class FilterRegistry {

  // function(string, string): boolean
  private static map: Map<string, any> = new Map<string, any>();

  static set(key: string, value: any): void {
    this.map.set(key, value);
  }

  static get(key: string): any {
    const value = this.map.get(key);
    if (value) {
      return value;
    } else {
      console.warn("FilterRegistry.get(" + key + ") = undefined");
      return null;
    }
  }

}
