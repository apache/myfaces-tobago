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

import Autocomplete from "@trevoreyre/autocomplete-js";

export class Suggest {

  autocomplete: Autocomplete;
  tobagoIn: HTMLElement;
  resolve: any;

  constructor(tobagoIn: HTMLElement) {
    this.tobagoIn = tobagoIn;
  }

  public init(): void {
    if (!this.suggest) {
      console.warn("[tobago-suggest] could not find tobago-suggest");
      return;
    }

    this.registerAjaxListener();
    this.base.classList.add("autocomplete");
    this.base.insertAdjacentHTML("afterbegin", "<div class=\"autocomplete-pseudo-container\"></div>");
    this.suggestInput.classList.add("autocomplete-input");
    this.suggestInput.insertAdjacentHTML("afterend", "<ul class=\"autocomplete-result-list\"></ul>");

    const options = {
      search: input => {
        console.debug("[tobago-suggest] input = '" + input + "'");
        const minChars = this.minChars ? this.minChars : 1;
        if (input.length < minChars) {
          return [];
        }
        this.hiddenInput.value = input.toLowerCase();

        this.positioningSpinner();

        return new Promise(resolve => {
          if (input.length < 1) {
            return resolve([]);
          }

          if (this.update) {
            this.resolve = resolve;
            const suggestId = this.suggest.id;
            jsf.ajax.request(suggestId, null, {
              "javax.faces.behavior.event": "suggest",
              execute: suggestId,
              render: suggestId
            });
          } else {
            return resolve(this.filterItems());
          }
        });
      },
      onUpdate: (results, selectedIndex) => {
        this.positioningResultList();
        this.setResultListMaxHeight();
      },
      debounceTime: this.delay
    };

    new Autocomplete(this.base, options);
    if (!this.localMenu) {
      this.menuStore.append(this.resultList);
    }
  }

  private registerAjaxListener(): void {
    jsf.ajax.addOnEvent(this.resolvePromise.bind(this));
  }

  private resolvePromise(event: EventData): void {
    if (event.source === this.suggest && event.status === "success") {
      return this.resolve(this.filterItems());
    }
  }

  private filterItems(): string[] {
    return this.items.filter(item => {
      return item.toLowerCase().indexOf(this.hiddenInput.value) > -1;
    });
  }

  private positioningSpinner(): void {
    const baseRect = this.base.getBoundingClientRect();
    const suggestInputRect = this.suggestInput.getBoundingClientRect();
    const suggestInputStyle = getComputedStyle(this.suggestInput);
    this.pseudoContainer.style.left = suggestInputRect.x - baseRect.x + suggestInputRect.width
        - parseFloat(getComputedStyle(this.pseudoContainer, ":after").width)
        - parseFloat(suggestInputStyle.marginRight)
        - parseFloat(suggestInputStyle.borderRight)
        - parseFloat(suggestInputStyle.paddingRight) + "px";
    this.pseudoContainer.style.top = suggestInputRect.y - baseRect.y + (suggestInputRect.height / 2) + "px";
  }

  private positioningResultList(): void {
    const space: number = 2;

    if (this.localMenu) {
      const parentRect = this.suggestInput.parentElement.getBoundingClientRect();
      const suggestInputRect = this.suggestInput.getBoundingClientRect();
      this.resultList.style.marginLeft = (suggestInputRect.x - parentRect.x) + "px";
      this.resultList.style.maxWidth = suggestInputRect.width + "px";
      this.resultList.style.marginTop = space + "px";
      this.resultList.style.marginBottom = space + "px";
    } else {
      const suggestInputRect = this.suggestInput.getBoundingClientRect();
      this.resultList.style.minWidth = suggestInputRect.width + "px";
      this.resultList.style.left = suggestInputRect.left + "px";
      if (this.resultListPosition === "below") {
        this.resultList.style.marginTop =
            window.scrollY + suggestInputRect.top + suggestInputRect.height + space + "px";
        this.resultList.style.marginBottom = null;
      } else if (this.resultListPosition === "above") {
        this.resultList.style.marginTop = null;
        this.resultList.style.marginBottom = -(window.scrollY + suggestInputRect.top - space) + "px";
      }
    }
  }

  private setResultListMaxHeight(): void {
    const resultListEntry = this.resultList.querySelector(".autocomplete-result");
    if (this.maxItems && resultListEntry) {
      const resultListStyle = getComputedStyle(this.resultList);
      this.resultList.style.maxHeight = (
          parseFloat(resultListStyle.borderTop)
          + parseFloat(resultListStyle.paddingTop)
          + (this.maxItems * parseFloat(getComputedStyle(resultListEntry).height))
          + parseFloat(resultListStyle.paddingBottom)
          + parseFloat(resultListStyle.borderBottom)) + "px";
    }
  }

  private get base(): HTMLElement {
    return this.tobagoIn;
  }

  private get pseudoContainer(): HTMLDivElement {
    return this.base.querySelector(":scope > .autocomplete-pseudo-container");
  }

  private get suggestInput(): HTMLInputElement {
    const root = this.base.getRootNode() as ShadowRoot | Document;
    return root.getElementById(this.suggest.getAttribute("for")) as HTMLInputElement;
  }

  private get suggest(): HTMLElement {
    return this.base.querySelector("tobago-suggest");
  }

  private get hiddenInput(): HTMLInputElement {
    return this.suggest.querySelector(":scope > input[type=hidden]");
  }

  private get items(): string[] {
    return JSON.parse(this.suggest.getAttribute("items"));
  }

  private get resultList(): HTMLUListElement {
    const root = this.base.getRootNode() as ShadowRoot | Document;
    const resultListId = this.suggestInput.getAttribute("aria-owns");
    return root.getElementById(resultListId) as HTMLUListElement;
  }

  private get resultListPosition(): string {
    return this.base.dataset.position;
  }

  private get menuStore(): HTMLDivElement {
    const root = this.base.getRootNode() as ShadowRoot | Document;
    return root.querySelector(".tobago-page-menuStore");
  }

  private get update(): boolean {
    return this.suggest.getAttribute("update") !== null;
  }

  private get delay(): number {
    return parseInt(this.suggest.getAttribute("delay"));
  }

  private get maxItems(): number {
    return parseInt(this.suggest.getAttribute("max-items"));
  }

  private get minChars(): number {
    return parseInt(this.suggest.getAttribute("min-chars"));
  }

  private get localMenu(): boolean {
    return this.suggest.getAttribute("local-menu") !== null;
  }
}
