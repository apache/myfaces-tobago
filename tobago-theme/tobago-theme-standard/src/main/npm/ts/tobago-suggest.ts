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

class Suggest extends HTMLElement {

  static asyncResults: (data: string[]) => void;

  hiddenInput: HTMLInputElement;

  static loadFromServer = function (input: HTMLInputElement): (query, syncResults, asyncResults) => void {

    let timeout;

    return function findMatches(query, syncResults, asyncResults): void {

      const root = input.getRootNode() as ShadowRoot | Document;
      let suggest = root.getElementById(input.dataset.tobagoSuggestFor) as Suggest;

      // todo: suggest.hiddenInput.value should contain the last query value
      if (suggest.hiddenInput.value !== query) {

        if (timeout) {
          clearTimeout(timeout);
        }

        const delay = suggest.delay;

        timeout = setTimeout(function (): void {
          suggest.hiddenInput.value = query;
          Suggest.asyncResults = asyncResults;
          delete suggest.dataset.tobagoSuggestData;
          console.info("query: '" + query + "'");

          jsf.ajax.request(
              suggest.id,
              null, // todo: event?
              {
                "javax.faces.behavior.event": "suggest",
                execute: suggest.id,
                render: suggest.id
              });
        }, delay);

      }
    };
  };

  static fromClient = function (data): (query, syncResults) => void {
    return (query, syncResults): void => {
      const result = [];
      for (let i = 0; i < data.length; i++) {
        if (data[i].indexOf(query) >= 0) {
          result.push(data[i]);
        }
      }
      syncResults(result);
    };
  };

  constructor() {
    super();
    this.hiddenInput = document.createElement("input");
    this.hiddenInput.setAttribute("type", "hidden");
    this.hiddenInput.setAttribute("name", this.id);
    this.appendChild(this.hiddenInput);
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

  get data(): string[] {
    return JSON.parse(this.getAttribute("data"));
  }

  set data(data: string[]) {
    this.setAttribute("data", JSON.stringify(data));
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
    const root = this.getRootNode() as ShadowRoot | Document;
    const input = root.getElementById(this.for) as HTMLInputElement;
    const $input = jQuery(input);

    if (this.update && input.classList.contains("tt-input")) { // already initialized: so only update data
      if (Suggest.asyncResults) {
        Suggest.asyncResults(this.data);
        Suggest.asyncResults = null;
      }
    } else { // new
      input.dataset.tobagoSuggestFor = this.id;
      input.autocomplete = "off";

      let source;
      if (this.update) {
        source = Suggest.loadFromServer(input);
      } else {
        source = Suggest.fromClient(this.data);
      }

      let suggestPopup = root.getElementById(this.id + "::popup");
      if (suggestPopup) {
        suggestPopup.parentElement.removeChild(suggestPopup);
      }
      suggestPopup = document.createElement("div");
      suggestPopup.id = this.id + "::popup";
      suggestPopup.classList.add("tt-menu", "tt-empty");
      root.querySelector(".tobago-page-menuStore").appendChild(suggestPopup);

      const menu = this.localMenu ? null : suggestPopup;

      $input.typeahead({
        menu: menu,
        minLength: this.minChars,
        hint: true,// todo
        highlight: true // todo
      }, {
        //name: 'test',// todo
        limit: this.maxItems,
        source: source
      });
      // old with jQuery:
      $input.on("typeahead:change", function (event: JQuery.Event): void {
        const input = this;
        input.dispatchEvent(new Event("change"));
      });
      // new without jQuery:
      // input.addEventListener("typeahead:change", (event: Event) => {
      //   const input = event.currentTarget as HTMLInputElement;
      //   input.dispatchEvent(new Event("change"));
      // });

      // old with jQuery:
      $input.on("typeahead:open", function (event: JQuery.Event): void {
        const input = this;
        const suggestPopup = root.getElementById(input.dataset.tobagoSuggestFor + "::popup");
        suggestPopup.style.top = DomUtils.offset(input).top + input.offsetHeight + "px";
        suggestPopup.style.left = DomUtils.offset(input).left + "px";
        suggestPopup.style.minWidth = input.offsetWidth + "px";
      });

      // new without jQuery:
      // input.addEventListener("typeahead:open", (event: Event) => {
      //   const input = event.currentTarget as HTMLInputElement;
      //   const suggestPopup = document.getElementById(input.dataset.tobagoSuggestFor + "::popup");
      //   suggestPopup.style.top = DomUtils.offset(input).top + input.offsetHeight + "px";
      //   suggestPopup.style.left = DomUtils.offset(input).left + "px";
      //   suggestPopup.style.minWidth = input.offsetWidth + "px";
      // });

    }
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-suggest", Suggest);
});
