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
class DemoSearch extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        const input = this.input;
        const a = this.a;
        if (input && a) {
            a.href = DemoSearch.GOOGLE;
            input.addEventListener("change", this.change.bind(this));
            input.addEventListener("keypress", this.keypress.bind(this));
        }
    }
    change(event) {
        this.a.href = DemoSearch.GOOGLE + encodeURI(this.input.value);
    }
    keypress(event) {
        if (event.which === 13) {
            this.change(event);
        }
    }
    get input() {
        return this.querySelector("input");
    }
    get a() {
        return this.querySelector("a");
    }
}
DemoSearch.GOOGLE = "https://www.google.com/search?q=site%3Atobago-vm.apache.org+demo-4-release+";
document.addEventListener("DOMContentLoaded", function (event) {
    if (window.customElements.get("demo-search") == null) {
        window.customElements.define("demo-search", DemoSearch);
    }
});
//# sourceMappingURL=demo-search.js.map