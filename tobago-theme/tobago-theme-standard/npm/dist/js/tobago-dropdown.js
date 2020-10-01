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
import Popper from "popper.js";
const Event = {
    HIDE: "tobago.dropdown.hide",
    HIDDEN: "tobago.dropdown.hidden",
    SHOW: "tobago.dropdown.show",
    SHOWN: "tobago.dropdown.shown"
};
class Dropdown extends HTMLElement {
    constructor() {
        super();
        this.dropdownEntries = [];
        if (!this.classList.contains("tobago-dropdown-submenu")) { // ignore submenus
            const root = this.getRootNode();
            this.createDropdownEntries(this.dropdownMenu, null);
            this.toggleButton.addEventListener("click", this.toggleDropdown.bind(this));
            root.addEventListener("mouseup", this.mouseupOnDocument.bind(this));
            root.addEventListener("keydown", this.keydownOnDocument.bind(this));
        }
    }
    connectedCallback() {
    }
    toggleDropdown(event) {
        event.preventDefault();
        event.stopPropagation();
        if (this.dropdownVisible()) {
            this.closeDropdown();
        }
        else {
            this.openDropdown();
        }
    }
    mouseupOnDocument(event) {
        if (!this.toggleButtonSelected(event) && this.dropdownVisible()
            && !this.dropdownMenu.contains(event.target)) {
            this.closeDropdown();
        }
    }
    keydownOnDocument(event) {
        if (this.toggleButtonSelected(event) && !this.dropdownVisible()
            && (event.code === "ArrowUp" || event.code === "ArrowDown")) {
            event.preventDefault();
            event.stopPropagation();
            this.openDropdown();
            const interval = setInterval(() => {
                if (this.dropdownVisible()) {
                    if (this.activeDropdownEntry) {
                        this.activeDropdownEntry.focus();
                    }
                    else {
                        this.dropdownEntries[0].focus();
                    }
                    clearInterval(interval);
                }
            }, 0);
        }
        else if (this.dropdownVisible()
            && (event.code === "ArrowUp" || event.code === "ArrowDown"
                || event.code === "ArrowLeft" || event.code === "ArrowRight"
                || event.code === "Tab")) {
            event.preventDefault();
            event.stopPropagation();
            if (!this.activeDropdownEntry) {
                this.dropdownEntries[0].focus();
            }
            else if (event.code === "ArrowUp" && this.activeDropdownEntry.previous) {
                this.activeDropdownEntry.previous.focus();
            }
            else if (event.code === "ArrowDown" && this.activeDropdownEntry.next) {
                this.activeDropdownEntry.next.focus();
            }
            else if (event.code === "ArrowRight" && this.activeDropdownEntry.children.length > 0) {
                this.activeDropdownEntry.children[0].focus();
            }
            else if (event.code === "ArrowLeft" && this.activeDropdownEntry.parent) {
                this.activeDropdownEntry.parent.focus();
            }
            else if (!event.shiftKey && event.code === "Tab") {
                if (this.activeDropdownEntry.children.length > 0) {
                    this.activeDropdownEntry.children[0].focus();
                }
                else if (this.activeDropdownEntry.next) {
                    this.activeDropdownEntry.next.focus();
                }
                else {
                    let parent = this.activeDropdownEntry.parent;
                    while (parent) {
                        if (parent.next) {
                            this.activeDropdownEntry.clear();
                            parent.next.focus();
                            break;
                        }
                        else {
                            parent = parent.parent;
                        }
                    }
                }
            }
            else if (event.shiftKey && event.code === "Tab") {
                if (this.activeDropdownEntry.previous) {
                    this.activeDropdownEntry.previous.focus();
                }
                else if (this.activeDropdownEntry.parent) {
                    this.activeDropdownEntry.parent.focus();
                }
            }
        }
        else if (this.dropdownVisible() && event.code === "Escape") {
            event.preventDefault();
            event.stopPropagation();
            this.closeDropdown();
        }
    }
    openDropdown() {
        this.dispatchEvent(new CustomEvent(Event.SHOW));
        if (!this.inStickyHeader()) {
            this.menuStore.appendChild(this.dropdownMenu);
            new Popper(this.toggleButton, this.dropdownMenu, {
                placement: "bottom-start"
            });
        }
        for (const dropdownEntry of this.dropdownEntries) {
            dropdownEntry.clear();
        }
        this.dropdownMenu.classList.add("show");
        this.dispatchEvent(new CustomEvent(Event.SHOWN));
    }
    closeDropdown() {
        this.dispatchEvent(new CustomEvent(Event.HIDE));
        this.dropdownMenu.classList.remove("show");
        this.appendChild(this.dropdownMenu);
        this.dispatchEvent(new CustomEvent(Event.HIDDEN));
    }
    get toggleButton() {
        return this.querySelector(":scope > button[data-toggle='dropdown']");
    }
    toggleButtonSelected(event) {
        return this.toggleButton.contains(event.target);
    }
    inStickyHeader() {
        return Boolean(this.closest("tobago-header.sticky-top"));
    }
    get dropdownMenu() {
        const root = this.getRootNode();
        return root.querySelector(".dropdown-menu[name='" + this.id + "']");
    }
    dropdownVisible() {
        return this.dropdownMenu.classList.contains("show");
    }
    get menuStore() {
        const root = this.getRootNode();
        return root.querySelector(".tobago-page-menuStore");
    }
    get activeDropdownEntry() {
        for (const dropdownEntry of this.dropdownEntries) {
            if (dropdownEntry.active) {
                return dropdownEntry;
            }
        }
        return null;
    }
    createDropdownEntries(dropdownMenu, parent) {
        let lastDropdownEntry = null;
        for (const dropdownItem of dropdownMenu.children) {
            if (dropdownItem.classList.contains("dropdown-item")) {
                const entry = this.createDropdownEntry(dropdownItem, parent, lastDropdownEntry);
                lastDropdownEntry = entry;
                this.dropdownEntries.push(entry);
                if (dropdownItem.classList.contains("tobago-dropdown-submenu")) {
                    this.createDropdownEntries(dropdownItem.querySelector(".dropdown-menu"), entry);
                }
            }
            else {
                const dropdownItems = dropdownItem.querySelectorAll(".dropdown-item");
                for (const dropdownItem of dropdownItems) {
                    const entry = this.createDropdownEntry(dropdownItem, parent, lastDropdownEntry);
                    lastDropdownEntry = entry;
                    this.dropdownEntries.push(entry);
                }
            }
        }
    }
    createDropdownEntry(dropdownItem, parent, previous) {
        const entry = new DropdownEntry(dropdownItem);
        if (parent) {
            entry.parent = parent;
            parent.children.push(entry);
        }
        if (previous) {
            previous.next = entry;
            entry.previous = previous;
        }
        return entry;
    }
}
class DropdownEntry {
    constructor(dropdownItem) {
        this._children = [];
        this._baseElement = dropdownItem;
        if (dropdownItem.classList.contains("tobago-dropdown-submenu")) {
            this.focusElement = dropdownItem.querySelector(".tobago-link");
        }
        else if (dropdownItem.tagName === "LABEL") {
            const root = dropdownItem.getRootNode();
            this.focusElement = root.getElementById(dropdownItem.getAttribute("for"));
        }
        else {
            this.focusElement = dropdownItem;
        }
        this._baseElement.addEventListener("mouseenter", this.activate.bind(this));
        this._baseElement.addEventListener("mouseleave", this.deactivate.bind(this));
    }
    activate(event) {
        this.active = true;
    }
    deactivate(event) {
        this.active = false;
    }
    get previous() {
        return this._previous;
    }
    set previous(value) {
        this._previous = value;
    }
    get next() {
        return this._next;
    }
    set next(value) {
        this._next = value;
    }
    get parent() {
        return this._parent;
    }
    set parent(value) {
        this._parent = value;
    }
    get children() {
        return this._children;
    }
    set children(value) {
        this._children = value;
    }
    get active() {
        return this._active;
    }
    set active(value) {
        this._active = value;
    }
    focus() {
        var _a, _b;
        (_a = this.previous) === null || _a === void 0 ? void 0 : _a.clear();
        (_b = this.next) === null || _b === void 0 ? void 0 : _b.clear();
        if (this.parent) {
            this.parent.active = false;
            this.parent._baseElement.classList.add("tobago-dropdown-open");
        }
        for (const child of this.children) {
            child.clear();
        }
        this._baseElement.classList.remove("tobago-dropdown-open");
        this._baseElement.classList.add("tobago-dropdown-selected");
        this.active = true;
        this.focusElement.focus();
    }
    clear() {
        this._baseElement.classList.remove("tobago-dropdown-open");
        this._baseElement.classList.remove("tobago-dropdown-selected");
        this.active = false;
    }
}
document.addEventListener("DOMContentLoaded", function (event) {
    window.customElements.define("tobago-dropdown", Dropdown);
});
//# sourceMappingURL=tobago-dropdown.js.map