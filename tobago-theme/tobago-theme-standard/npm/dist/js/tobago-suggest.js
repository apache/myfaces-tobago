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
    constructor(tobagoIn) {
        this.tobagoIn = tobagoIn;
    }
    init() {
        if (!this.suggest) {
            console.warn("[tobago-suggest] could not find tobago-suggest");
            return;
        }
        this.registerAjaxListener();
        this.base.classList.add("autocomplete");
        this.base.insertAdjacentHTML("afterbegin", `<div class="autocomplete-pseudo-container"></div>`);
        this.suggestInput.classList.add("autocomplete-input");
        this.suggestInput.insertAdjacentHTML("afterend", `<ul class="autocomplete-result-list"></ul>`);
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
                    }
                    else {
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
    registerAjaxListener() {
        jsf.ajax.addOnEvent(this.resolvePromise.bind(this));
    }
    resolvePromise(event) {
        if (event.source === this.suggest && event.status === "success") {
            return this.resolve(this.filterItems());
        }
    }
    filterItems() {
        return this.items.filter(item => {
            return item.toLowerCase().indexOf(this.hiddenInput.value) > -1;
        });
    }
    positioningSpinner() {
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
    positioningResultList() {
        const space = 2;
        if (this.localMenu) {
            const parentRect = this.suggestInput.parentElement.getBoundingClientRect();
            const suggestInputRect = this.suggestInput.getBoundingClientRect();
            this.resultList.style.marginLeft = (suggestInputRect.x - parentRect.x) + "px";
            this.resultList.style.maxWidth = suggestInputRect.width + "px";
            this.resultList.style.marginTop = space + "px";
            this.resultList.style.marginBottom = space + "px";
        }
        else {
            const suggestInputRect = this.suggestInput.getBoundingClientRect();
            this.resultList.style.minWidth = suggestInputRect.width + "px";
            this.resultList.style.left = suggestInputRect.left + "px";
            if (this.resultListPosition === "below") {
                this.resultList.style.marginTop =
                    window.scrollY + suggestInputRect.top + suggestInputRect.height + space + "px";
                this.resultList.style.marginBottom = null;
            }
            else if (this.resultListPosition === "above") {
                this.resultList.style.marginTop = null;
                this.resultList.style.marginBottom = -(window.scrollY + suggestInputRect.top - space) + "px";
            }
        }
    }
    setResultListMaxHeight() {
        const resultListEntry = this.resultList.querySelector(".autocomplete-result");
        if (this.maxItems && resultListEntry) {
            const resultListStyle = getComputedStyle(this.resultList);
            this.resultList.style.maxHeight = (parseFloat(resultListStyle.borderTop)
                + parseFloat(resultListStyle.paddingTop)
                + (this.maxItems * parseFloat(getComputedStyle(resultListEntry).height))
                + parseFloat(resultListStyle.paddingBottom)
                + parseFloat(resultListStyle.borderBottom)) + "px";
        }
    }
    get base() {
        return this.tobagoIn;
    }
    get pseudoContainer() {
        return this.base.querySelector(":scope > .autocomplete-pseudo-container");
    }
    get suggestInput() {
        const root = this.base.getRootNode();
        return root.getElementById(this.suggest.getAttribute("for"));
    }
    get suggest() {
        return this.base.querySelector("tobago-suggest");
    }
    get hiddenInput() {
        return this.suggest.querySelector(":scope > input[type=hidden]");
    }
    get items() {
        return JSON.parse(this.suggest.getAttribute("items"));
    }
    get resultList() {
        const root = this.base.getRootNode();
        const resultListId = this.suggestInput.getAttribute("aria-owns");
        return root.getElementById(resultListId);
    }
    get resultListPosition() {
        return this.base.dataset.position;
    }
    get menuStore() {
        const root = this.base.getRootNode();
        return root.querySelector(".tobago-page-menuStore");
    }
    get update() {
        return this.suggest.getAttribute("update") !== null;
    }
    get delay() {
        return parseInt(this.suggest.getAttribute("delay"));
    }
    get maxItems() {
        return parseInt(this.suggest.getAttribute("max-items"));
    }
    get minChars() {
        return parseInt(this.suggest.getAttribute("min-chars"));
    }
    get localMenu() {
        return this.suggest.getAttribute("local-menu") !== null;
    }
}
//# sourceMappingURL=tobago-suggest.js.map