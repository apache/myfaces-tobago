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
import { Listener, Phase } from "./tobago-listener";
import { DomUtils } from "./tobago-utils";
import { Focus } from "./tobago-focus";
export class In extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        this.input.addEventListener("focus", Focus.setLastFocusId);
    }
    get input() {
        return this.querySelector(DomUtils.escapeClientId(this.id + DomUtils.SUB_COMPONENT_SEP + "field"));
    }
}
document.addEventListener("DOMContentLoaded", function (event) {
    window.customElements.define("tobago-in", In);
});
// XXX regexp example only - blueprint
class RegExpTest {
    constructor(element) {
        this.element = element;
        this.regexp = new RegExp(this.element.dataset.regexp);
        console.debug("constructor: '%s'", element.id);
        this.element.addEventListener("change", this.checkValue.bind(this));
    }
    static init(element) {
        for (const input of DomUtils.selfOrElementsByClassName(element, "tobago-in")) { // todo only for data-regexp
            new RegExpTest(input);
        }
    }
    checkValue(event) {
        console.debug("changed: check if '%s' is okay!", this.regexp.toString());
        if (!this.regexp.test(this.element.value)) {
            this.element.classList.add("border-danger");
        }
        else {
            this.element.classList.remove("border-danger");
        }
    }
}
Listener.register(RegExpTest.init, Phase.DOCUMENT_READY);
Listener.register(RegExpTest.init, Phase.AFTER_UPDATE);
//# sourceMappingURL=tobago-in.js.map