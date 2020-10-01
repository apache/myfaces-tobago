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
import { DomUtils } from "./tobago-utils";
class TreeListbox extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        this.applySelected();
        for (const listbox of this.listboxes) {
            if (!listbox.disabled) {
                listbox.addEventListener("change", this.select.bind(this));
            }
        }
    }
    select(event) {
        const listbox = event.currentTarget;
        this.unselectDescendants(listbox);
        this.setSelected();
        this.applySelected();
    }
    unselectDescendants(select) {
        let unselect = false;
        for (const listbox of this.listboxes) {
            if (unselect) {
                const checkedOption = listbox.querySelector("option:checked");
                if (checkedOption) {
                    checkedOption.selected = false;
                }
            }
            else if (listbox.id === select.id) {
                unselect = true;
            }
        }
    }
    setSelected() {
        const selected = [];
        for (const level of this.levelElements) {
            const checkedOption = level
                .querySelector(".tobago-treeListbox-select:not(.d-none) option:checked");
            if (checkedOption) {
                selected.push(checkedOption.index);
            }
        }
        this.hiddenInput.value = JSON.stringify(selected);
    }
    applySelected() {
        const selected = JSON.parse(this.hiddenInput.value);
        let nextActiveSelect = this.querySelector(".tobago-treeListbox-select");
        const levelElements = this.levelElements;
        for (let i = 0; i < levelElements.length; i++) {
            const level = levelElements[i];
            for (const select of this.getSelectElements(level)) {
                if ((nextActiveSelect !== null && select.id === nextActiveSelect.id)
                    || (nextActiveSelect === null && select.disabled)) {
                    const check = i < selected.length ? selected[i] : null;
                    this.show(select, check);
                    nextActiveSelect = this.getNextActiveSelect(select, check);
                }
                else {
                    this.hide(select);
                }
            }
        }
    }
    getSelectElements(level) {
        return level.querySelectorAll(".tobago-treeListbox-select");
    }
    getNextActiveSelect(select, check) {
        if (check !== null) {
            const option = select.querySelectorAll("option")[check];
            return this.querySelector(DomUtils.escapeClientId(option.id + DomUtils.SUB_COMPONENT_SEP + "parent"));
        }
        else {
            return null;
        }
    }
    show(select, check) {
        select.classList.remove("d-none");
        const checkedOption = select.querySelector("option:checked");
        if (checkedOption && checkedOption.index !== check) {
            checkedOption.selected = false;
        }
        if (check !== null && checkedOption.index !== check) {
            select.querySelectorAll("option")[check].selected = true;
        }
    }
    hide(select) {
        select.classList.add("d-none");
        const checkedOption = select.querySelector("option:checked");
        if (checkedOption) {
            checkedOption.selected = false;
        }
    }
    get listboxes() {
        return this.querySelectorAll(".tobago-treeListbox-select");
    }
    get levelElements() {
        return this.querySelectorAll(".tobago-treeListbox-level");
    }
    get hiddenInput() {
        return this.querySelector(DomUtils.escapeClientId(this.id + DomUtils.SUB_COMPONENT_SEP + "selected"));
    }
}
document.addEventListener("DOMContentLoaded", function (event) {
    window.customElements.define("tobago-tree-listbox", TreeListbox);
});
//# sourceMappingURL=tobago-tree-listbox.js.map