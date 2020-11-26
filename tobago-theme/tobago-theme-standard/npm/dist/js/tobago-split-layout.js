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
import { Page } from "./tobago-page";
class SplitLayout extends HTMLElement {
    constructor() {
        super();
        let first = true;
        let justAdded = false;
        for (let child of this.children) {
            if (justAdded) { // skip, because the just added had enlarges the list of children
                justAdded = false;
                continue;
            }
            if (getComputedStyle(child).display === "none") {
                continue;
            }
            if (first) { // the first needs no splitter handle
                first = false;
                continue;
            }
            const splitter = document.createElement("div");
            splitter.classList.add(this.orientation === "horizontal" ? "tobago-splitLayout-horizontal" : "tobago-splitLayout-vertical");
            justAdded = true;
            splitter.addEventListener("mousedown", this.start.bind(this));
            child.parentElement.insertBefore(splitter, child);
        }
    }
    /**
     * Get the previous sibling element (without <style> elements).
     */
    // todo: calls of this method can probably be simplified
    static previousElementSibling(element) {
        let sibling = element.previousElementSibling;
        while (sibling != null) {
            if (sibling.tagName !== "STYLE") {
                return sibling;
            }
            sibling = sibling.previousElementSibling;
        }
        return null;
    }
    get orientation() {
        return this.getAttribute("orientation");
    }
    set orientation(orientation) {
        this.setAttribute("orientation", orientation);
    }
    start(event) {
        event.preventDefault();
        const splitter = event.target;
        const previous = SplitLayout.previousElementSibling(splitter);
        this.offset = this.orientation === "horizontal"
            ? event.pageX - previous.offsetWidth : event.pageY - previous.offsetHeight;
        const mousedown = SplitLayoutMousedown.save(event, splitter);
        document.addEventListener("mousemove", this.move.bind(this));
        document.addEventListener("mouseup", this.stop.bind(this));
        const previousArea = mousedown.previous;
        if (this.orientation === "horizontal") {
            previousArea.style.width = String(previousArea.offsetWidth + "px");
        }
        else {
            previousArea.style.height = String(previousArea.offsetHeight + "px");
        }
        previousArea.style.flexGrow = "inherit";
        previousArea.style.flexBasis = "auto";
        console.debug("initial width/height = '%s'", (this.orientation === "horizontal" ? previousArea.style.width : previousArea.style.height));
    }
    move(event) {
        event.preventDefault();
        const data = SplitLayoutMousedown.load();
        const previousArea = data.previous;
        if (previousArea) {
            if (this.orientation === "horizontal") {
                previousArea.style.width = String(event.pageX - this.offset) + "px";
            }
            else {
                previousArea.style.height = String(event.pageY - this.offset) + "px";
            }
        }
    }
    stop(event) {
        document.removeEventListener("mousemove", this.move.bind(this)); // fixme remove the real added
        document.removeEventListener("mouseup", this.stop.bind(this)); // fixme remove the real added
        SplitLayoutMousedown.remove();
    }
}
class SplitLayoutMousedown {
    constructor(data) {
        if (data) {
            this.data = typeof data === "string" ? JSON.parse(data) : data;
        }
    }
    static save(event, splitter) {
        const horizontal = splitter.classList.contains("tobago-splitLayout-horizontal");
        const previous = SplitLayout.previousElementSibling(splitter);
        const data = {
            splitLayoutId: splitter.parentElement.id,
            horizontal: horizontal,
            splitterIndex: this.indexOfSplitter(splitter, horizontal)
        };
        Page.page(splitter).dataset.SplitLayoutMousedownData = JSON.stringify(data);
        return new SplitLayoutMousedown(data);
    }
    static load() {
        const element = document.documentElement; // XXX this might be the wrong element in case of shadow dom
        return new SplitLayoutMousedown(Page.page(element).dataset.SplitLayoutMousedownData);
    }
    static remove() {
        const element = document.documentElement; // XXX this might be the wrong element in case of shadow dom
        Page.page(element).dataset.SplitLayoutMousedownData = null;
    }
    static indexOfSplitter(splitter, horizontal) {
        const list = splitter.parentElement.getElementsByClassName(horizontal ? "tobago-splitLayout-horizontal" : "tobago-splitLayout-vertical");
        for (let i = 0; i < list.length; i++) {
            if (list.item(i) === splitter) {
                return i;
            }
        }
        return -1;
    }
    get splitter() {
        return this.data ? document.getElementById(this.data.splitLayoutId).getElementsByClassName(this.data.horizontal ? "tobago-splitLayout-horizontal" : "tobago-splitLayout-vertical")
            .item(this.data.splitterIndex) : null;
    }
    get previous() {
        return this.splitter ? SplitLayout.previousElementSibling(this.splitter) : null;
    }
}
document.addEventListener("tobago.init", function (event) {
    if (window.customElements.get("tobago-split-layout") == null) {
        window.customElements.define("tobago-split-layout", SplitLayout);
    }
});
//# sourceMappingURL=tobago-split-layout.js.map