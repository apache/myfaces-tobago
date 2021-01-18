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
import { Popover } from "bootstrap";
class TobagoRange extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        this.popover = new Popover(this.range, {
            container: this.menuStore,
            content: this.content.bind(this),
            trigger: "input",
            placement: "auto",
            delay: {
                show: 0,
                hide: 500
            }
        });
        const range = this.range;
        const listener = this.updatePopover.bind(this);
        range.addEventListener("input", listener);
        range.addEventListener("focus", listener);
    }
    get range() {
        return this.querySelector("input[type=range]");
    }
    get menuStore() {
        const root = this.getRootNode();
        return root.querySelector(".tobago-page-menuStore");
    }
    get tooltipBody() {
        return this.querySelector(".popover-body");
    }
    content() {
        return this.range.value;
    }
    updatePopover() {
        // XXX why update doesn't show the new content?
        //  this.popover.update();
        this.popover.show();
    }
}
document.addEventListener("tobago.init", function (event) {
    if (window.customElements.get("tobago-range") == null) {
        window.customElements.define("tobago-range", TobagoRange);
    }
});
//# sourceMappingURL=tobago-range.js.map