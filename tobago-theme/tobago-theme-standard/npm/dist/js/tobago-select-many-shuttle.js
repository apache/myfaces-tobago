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
import { Focus } from "./tobago-focus";
class SelectManyShuttle extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        this.unselectedSelect.addEventListener("focus", Focus.setLastFocusId);
        this.selectedSelect.addEventListener("focus", Focus.setLastFocusId);
        if (this.unselectedSelect.getAttribute("readonly") !== "readonly" && !this.unselectedSelect.disabled) {
            this.unselectedSelect.addEventListener("dblclick", this.addSelectedItems.bind(this));
        }
        if (this.selectedSelect.getAttribute("readonly") !== "readonly" && !this.selectedSelect.disabled) {
            this.selectedSelect.addEventListener("dblclick", this.removeSelectedItems.bind(this));
        }
        if (!this.addAllButton.disabled) {
            this.addAllButton.addEventListener("click", this.addAllItems.bind(this));
        }
        if (!this.addButton.disabled) {
            this.addButton.addEventListener("click", this.addSelectedItems.bind(this));
        }
        if (!this.removeButton.disabled) {
            this.removeButton.addEventListener("click", this.removeSelectedItems.bind(this));
        }
        if (!this.removeAllButton.disabled) {
            this.removeAllButton.addEventListener("click", this.removeAllItems.bind(this));
        }
    }
    addAllItems(event) {
        this.addItems(this.unselectedSelect.querySelectorAll("option:not(:disabled)"));
    }
    addSelectedItems(event) {
        this.addItems(this.unselectedSelect.querySelectorAll("option:checked"));
    }
    removeSelectedItems(event) {
        this.removeItems(this.selectedSelect.querySelectorAll("option:checked"));
    }
    removeAllItems(event) {
        this.removeItems(this.selectedSelect.querySelectorAll("option:not(:disabled)"));
    }
    addItems(options) {
        for (const option of options) {
            this.selectedSelect.add(option);
            this.changeHiddenOption(option, true);
        }
    }
    removeItems(options) {
        for (const option of options) {
            this.unselectedSelect.add(option);
            this.changeHiddenOption(option, false);
        }
    }
    changeHiddenOption(option, select) {
        const hiddenOption = this.hiddenSelect.querySelector("option[value='" + option.value + "']");
        hiddenOption.selected = select;
        this.dispatchEvent(new Event("change"));
    }
    get unselectedSelect() {
        return this.querySelector(".tobago-selectManyShuttle-unselected");
    }
    get selectedSelect() {
        return this.querySelector(".tobago-selectManyShuttle-selected");
    }
    get hiddenSelect() {
        return this.querySelector(".tobago-selectManyShuttle-hidden");
    }
    get addAllButton() {
        return this.querySelector(".tobago-selectManyShuttle-addAll");
    }
    get addButton() {
        return this.querySelector(".tobago-selectManyShuttle-add");
    }
    get removeButton() {
        return this.querySelector(".tobago-selectManyShuttle-remove");
    }
    get removeAllButton() {
        return this.querySelector(".tobago-selectManyShuttle-removeAll");
    }
}
document.addEventListener("DOMContentLoaded", function (event) {
    window.customElements.define("tobago-select-many-shuttle", SelectManyShuttle);
});
//# sourceMappingURL=tobago-select-many-shuttle.js.map