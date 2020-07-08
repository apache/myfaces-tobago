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

import {ComboBoxLightElement} from "@vaadin/vaadin-combo-box/vaadin-combo-box-light";

class Suggest extends HTMLElement {

  static callback: (items: String[], size: number) => {};// todo string vs String

  static timeout: number;

  constructor() {
    super();
  }

  get hiddenInput(): HTMLInputElement {
    return this.querySelector(":scope > input[type=hidden]");
  }

  get suggestInput(): HTMLInputElement {
    const root = this.getRootNode() as ShadowRoot | Document;
    return root.getElementById(this.for) as HTMLInputElement;
  }

  get for(): string {
    return this.getAttribute("for");
  }

  set for(forValue: string) {
    this.setAttribute("for", forValue);
  }

  get minChars(): number {
    return parseInt(this.getAttribute("min-chars"));
  }

  set minChars(minChars: number) {
    this.setAttribute("min-chars", String(minChars));
  }

  get delay(): number {
    return parseInt(this.getAttribute("delay"));
  }

  set delay(delay: number) {
    this.setAttribute("delay", String(delay));
  }

  get maxItems(): number {
    return parseInt(this.getAttribute("max-items"));
  }

  set maxItems(maxItems: number) {
    this.setAttribute("max-items", String(maxItems));
  }

  get update(): boolean {
    return this.hasAttribute("update");
  }

  set update(update: boolean) {
    if (update) {
      this.setAttribute("update", "");
    } else {
      this.removeAttribute("update");
    }
  }

  get totalCount(): number {
    return parseInt(this.getAttribute("total-count"));
  }

  set totalCount(totalCount: number) {
    this.setAttribute("total-count", String(totalCount));
  }

  get items(): string[] {
    return JSON.parse(this.getAttribute("items"));
  }

  set items(items: string[]) {
    this.setAttribute("items", JSON.stringify(items));
  }

  get localMenu(): boolean {
    return this.hasAttribute("local-menu");
  }

  set localMenu(update: boolean) {
    if (update) {
      this.setAttribute("local-menu", "");
    } else {
      this.removeAttribute("local-menu");
    }
  }

  connectedCallback(): void {

    let vaadinComboBox: ComboBoxLightElement = this.suggestInput.parentElement;

    if (vaadinComboBox.tagName !== "VAADIN-COMBO-BOX-LIGHT") { // new
      vaadinComboBox = document.createElement("vaadin-combo-box-light");
      vaadinComboBox.attrForValue = "value";
      vaadinComboBox.allowCustomValue = true;
      vaadinComboBox.readOnly = this.suggestInput.readOnly;
      vaadinComboBox.disabled = this.suggestInput.disabled;
      this.suggestInput.classList.add("input"); // todo do this in SuggestRenderer?
      const parent = this.suggestInput.parentElement;
      vaadinComboBox.appendChild(this.suggestInput);
      parent.appendChild(vaadinComboBox);

      vaadinComboBox.dataProvider = function dataProvider(
          params: { page: number, pageSize: number, filter: string },
          callback: (items: String[], size: number) => {}): void {
        console.info("call for data: %o", params);
        console.info("vaadinComboBox id: %s", vaadinComboBox.id);
        const suggest = vaadinComboBox.closest("tobago-in").querySelector("tobago-suggest") as Suggest;
        suggest.hiddenInput.value = params.filter;
        if (suggest.update) {
          if (params.filter.length >= suggest.minChars) {
            if (Suggest.timeout) {
              window.clearTimeout(Suggest.timeout);
            }
            Suggest.timeout = window.setTimeout(function (): void {
              Suggest.callback = callback;
              jsf.ajax.request(
                  suggest.id,
                  null, // todo: event?
                  {
                    "javax.faces.behavior.event": "suggest",
                    execute: suggest.id,
                    render: suggest.id
                  });
            }, suggest.delay);
          } else {
            callback([], 0);
          }
        } else {
          const items = suggest.items;
          const filteredItems:string[] = [];
          const lowerFilter = params.filter.toLocaleLowerCase();
          for (const item of items) {
            if (item.toLowerCase().indexOf(lowerFilter) > -1) {
              filteredItems.push(item);
            }
          }
          callback(filteredItems, filteredItems.length);
        }
      };
    } else { // already initialized: so update items (from AJAX) only
      if (Suggest.callback) {
        Suggest.callback(this.items, this.totalCount);
        Suggest.callback = null;
      } else {
        console.warn("Missing Suggest.callback!");
      }
    }
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-suggest", Suggest);
});
