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
class Popover extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        this.button.addEventListener("click", this.showPopover.bind(this));
        this.button.addEventListener("blur", this.hidePopover.bind(this));
    }
    showPopover() {
        this.menuStore.appendChild(this.popover);
        this.popper = new Popper(this.button, this.popover, {
            placement: "right",
            modifiers: {
                arrow: {
                    element: ".arrow"
                }
            },
            onCreate: this.updateBootstrapPopoverCss.bind(this),
            onUpdate: this.updateBootstrapPopoverCss.bind(this)
        });
        this.popover.classList.add("show");
    }
    hidePopover() {
        this.popover.classList.remove("show");
        this.appendChild(this.popover);
        if (this.popper !== undefined && this.popper !== null) {
            this.popper.destroy();
            this.popper = null;
        }
    }
    updateBootstrapPopoverCss() {
        const placement = this.popover.getAttribute("x-placement");
        if (placement === "right" && !this.popover.classList.contains("bs-popover-right")) {
            this.popover.classList.add("bs-popover-right");
            this.popover.classList.remove("bs-popover-left");
            this.updateAfterCssClassChange();
        }
        else if (placement === "left" && !this.popover.classList.contains("bs-popover-left")) {
            this.popover.classList.add("bs-popover-left");
            this.popover.classList.remove("bs-popover-right");
            this.updateAfterCssClassChange();
        }
    }
    updateAfterCssClassChange() {
        if (this.popper !== undefined && this.popper !== null) {
            this.popper.scheduleUpdate();
        }
    }
    get button() {
        return this.querySelector(":scope > .tobago-popover-button");
    }
    get popover() {
        const root = this.getRootNode();
        return root.querySelector(".tobago-popover-box[name='" + this.id + "']");
    }
    get menuStore() {
        const root = this.getRootNode();
        return root.querySelector(".tobago-page-menuStore");
    }
}
document.addEventListener("tobago.init", function (event) {
    if (window.customElements.get("tobago-popover") == null) {
        window.customElements.define("tobago-popover", Popover);
    }
});
//# sourceMappingURL=tobago-popover.js.map