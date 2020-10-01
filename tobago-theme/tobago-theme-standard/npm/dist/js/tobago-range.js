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
class Range extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        let range = this.range;
        let listener = this.showPopper.bind(this);
        range.addEventListener("input", listener);
        range.addEventListener("focus", listener);
    }
    get range() {
        return this.querySelector("input[type=range]");
    }
    get tooltip() {
        return this.querySelector(".popover");
    }
    get tooltipBody() {
        return this.querySelector(".popover-body");
    }
    showPopper() {
        let tooltip = this.tooltip;
        let range = this.range;
        // update value to display
        this.tooltipBody.innerHTML = `${range.value}`; // todo: use html from lit-html
        // init
        if (!this.popper) {
            this.popper = new Popper(range, tooltip, {
                placement: "right"
            });
        }
        // show
        tooltip.classList.remove("d-none");
        // hide after some seconds
        if (this.timeout) {
            window.clearTimeout(this.timeout);
        }
        this.timeout = window.setTimeout(() => {
            tooltip.classList.add("d-none");
            console.log("timeout");
        }, 5000);
    }
}
document.addEventListener("DOMContentLoaded", function (event) {
    window.customElements.define("tobago-range", Range);
});
//# sourceMappingURL=tobago-range.js.map