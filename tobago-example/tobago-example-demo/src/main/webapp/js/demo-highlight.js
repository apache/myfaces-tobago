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
// this import seems to initialize and do the highlighting?!?
import * as Prism from "prismjs";
class DemoHighlight extends HTMLElement {
    constructor() {
        super();
    }
    connectedCallback() {
        if (this.language) {
            this.innerHTML = `<pre><code class="language-${this.language}">${this.innerHTML.trim()}</demo-highlight>`;
        }
        else {
            this.innerHTML = `<pre><code>${this.innerHTML.trim()}</demo-highlight>`;
        }
        // XXX this doesn't work, because rollup? replaces this with "unknown".
        // XXX But, highlighting works automatically ?!? (but not, when removing this)
        try {
            Prism.highlightElement(this.querySelector("code"));
        }
        catch (e) {
            console.debug("Prism.highlight");
            // ignore
        }
    }
    get language() {
        return this.getAttribute("language");
    }
}
document.addEventListener("DOMContentLoaded", function (event) {
    if (window.customElements.get("demo-highlight") == null) {
        window.customElements.define("demo-highlight", DemoHighlight);
    }
});
//# sourceMappingURL=demo-highlight.js.map