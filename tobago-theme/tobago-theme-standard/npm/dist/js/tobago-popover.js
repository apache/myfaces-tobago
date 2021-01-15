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
// XXX which? nothing works...
// import * as bootstrap from "bootstrap/dist/js/bootstrap";
// import * as bootstrap from "bootstrap/dist/js/bootstrap.esm";
// import * as bootstrap from "bootstrap/dist/js/bootstrap.bundle";
// import {createPopper} from "@popperjs/core/dist/esm/popper";
class Popover extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        // @ts-ignore
        this.popover = new bootstrap.Popover(this.trigger, {
            container: this.menuStore
        });
    }
    get trigger() {
        return this;
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