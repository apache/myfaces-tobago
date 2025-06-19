(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(require('prismjs')) :
    typeof define === 'function' && define.amd ? define(['prismjs'], factory) :
    (global = typeof globalThis !== 'undefined' ? globalThis : global || self, factory(global.Prism));
})(this, (function (Prism) { 'use strict';

    function _interopNamespaceDefault(e) {
        var n = Object.create(null);
        if (e) {
            Object.keys(e).forEach(function (k) {
                if (k !== 'default') {
                    var d = Object.getOwnPropertyDescriptor(e, k);
                    Object.defineProperty(n, k, d.get ? d : {
                        enumerable: true,
                        get: function () { return e[k]; }
                    });
                }
            });
        }
        n.default = e;
        return Object.freeze(n);
    }

    var Prism__namespace = /*#__PURE__*/_interopNamespaceDefault(Prism);

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
    class DemoAlert extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.addEventListener("click", this.alert.bind(this));
        }
        alert() {
            window.alert(this.value);
        }
        get value() {
            return this.getAttribute("value");
        }
    }
    document.addEventListener("DOMContentLoaded", function (event) {
        if (window.customElements.get("demo-alert") == null) {
            window.customElements.define("demo-alert", DemoAlert);
        }
    });

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
    class DemoCopyToClipboard extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.addEventListener("click", (event) => {
                const sourceElement = document.getElementById(this.source);
                if (window.getSelection) {
                    const selection = window.getSelection();
                    const range = document.createRange();
                    range.selectNodeContents(sourceElement);
                    selection.removeAllRanges();
                    selection.addRange(range);
                }
                else {
                    console.warn("Text select not possible: Unsupported browser.");
                }
                try {
                    const result = document.execCommand("copy");
                    console.debug("result: " + result);
                }
                catch (error) {
                    console.error("Copying text not possible");
                }
            });
        }
        get source() {
            return this.getAttribute("source");
        }
        set source(name) {
            this.setAttribute("source", name);
        }
    }
    document.addEventListener("DOMContentLoaded", function (event) {
        if (window.customElements.get("demo-copy-to-clipboard") == null) {
            window.customElements.define("demo-copy-to-clipboard", DemoCopyToClipboard);
        }
    });

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
    /*
      simple example of a quick typing extra input date field.
    */
    document.addEventListener("DOMContentLoaded", function (event) {
        document.querySelectorAll("tobago-date input[data-quick-pattern]").forEach(input => {
            console.debug("quick-pattern found for id=", input.id);
            const quickPattern = input.dataset.quickPattern;
            let regexp;
            switch (quickPattern) {
                case "ddmm":
                    regexp = "[0-3][0-9][0-1][0-9]";
                    break;
                case "mmdd":
                    regexp = "[0-1][0-9][0-3][0-9]";
                    break;
                default:
                    console.error("Unsupported pattern", quickPattern);
                    return;
            }
            const quick = document.createElement("input");
            quick.id = input.id + "::quick";
            quick.type = "text";
            quick.className = "form-control";
            quick.style.maxWidth = "5em";
            quick.placeholder = quickPattern;
            quick.pattern = regexp;
            quick.setAttribute("targetId", input.id);
            input.insertAdjacentElement("beforebegin", quick);
            quick.addEventListener("blur", (event => {
                const quick = event.currentTarget;
                let value = quick.value;
                let day, month, year;
                if (value.length == 4) {
                    day = Number.parseInt(value.substring(0, 2));
                    month = Number.parseInt(value.substring(2, 4));
                    year = new Date().getFullYear();
                }
                if (value.length == 6) ;
                let string = `${year}-${month < 10 ? "0" + month : month}-${day < 10 ? "0" + day : day}`;
                console.info("date ->", string);
                const input = document.getElementById(quick.getAttribute("targetId"));
                input.value = string;
            }));
        });
    });

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
                Prism__namespace.highlightElement(this.querySelector("code"));
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
    // todo: this code is not tested
    class DemoInspect {
        static initInspect(element) {
            for (const code of element.querySelectorAll("code")) {
                for (const br of code.querySelectorAll("br")) {
                    br.parentNode.insertBefore(document.createTextNode("\n"), br);
                    br.parentNode.removeChild(br);
                }
            }
            for (const e of element.querySelectorAll("tobago-in")) {
                // do highlighting with hovering only in the content-area
                if (e.closest("#page\\:content")) {
                    e.addEventListener("hover", function (event) {
                        // clear old selections:
                        for (const selected of document.querySelectorAll(".demo-selected")) {
                            selected.classList.remove("demo-selected");
                        }
                        const element = event.currentTarget;
                        element.classList.add("demo-selected");
                        const clientId = element.closest("[id]").id;
                        const id = clientId.substr(clientId.lastIndexOf(":") + 1);
                        const source = document.getElementById("demo-view-source");
                        for (const span of source.querySelectorAll("span.token.attr-value")) {
                            if (span.textContent === `id="${id}"`) {
                                span.parentElement.classList.add("demo-selected");
                            }
                        }
                    });
                }
            }
        }
    }
    document.addEventListener("DOMContentLoaded", function (event) {
        let element = document.documentElement; // XXX fixme
        // XXX init areas after Faces AJAX update not implemented yet!
        DemoInspect.initInspect(element); //TODO fix inspection
    });

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
    /**
     * Utility links:
     * Copies the values from the data-login attribute to the username/password fields.
     */
    class DemoLogin extends HTMLElement {
        constructor() {
            super();
        }
        connectedCallback() {
            this.addEventListener("click", this.fillFields.bind(this));
        }
        fillFields(event) {
            const rootNode = this.getRootNode();
            const username = rootNode.getElementById(this.usernameId);
            username.value = this.username;
            const password = rootNode.getElementById(this.passwordId);
            password.value = this.password;
            event.preventDefault();
        }
        get username() {
            return this.getAttribute("username");
        }
        get usernameId() {
            return this.getAttribute("username-id");
        }
        get password() {
            return this.getAttribute("password");
        }
        get passwordId() {
            return this.getAttribute("password-id");
        }
    }
    document.addEventListener("DOMContentLoaded", function (event) {
        if (window.customElements.get("demo-login") == null) {
            window.customElements.define("demo-login", DemoLogin);
        }
    });

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
    class Sidebar extends HTMLElement {
        connectedCallback() {
            this.updateLeft();
            this.insertAdjacentHTML("afterbegin", "<strong>On this page</strong><nav id='tableOfContents'></nav>");
            if (this.fixedHeader) {
                const resizeObserver = new ResizeObserver(() => {
                    const headerHeight = this.fixedHeader.offsetHeight;
                    const headerMarginBottom = parseFloat(getComputedStyle(this.fixedHeader).marginBottom);
                    this.rootHtml.style.scrollPaddingTop = headerHeight + headerMarginBottom + "px";
                    this.style.top = headerHeight + "px";
                });
                resizeObserver.observe(this.fixedHeader);
            }
            if (this.fixedFooter) {
                const resizeObserver = new ResizeObserver(() => this.style.bottom = this.fixedFooter.offsetHeight + "px");
                resizeObserver.observe(this.fixedFooter);
            }
            this.sectionTree = {
                id: this.rootSection.id,
                title: this.getSectionTitle(this.rootSection),
                level: 0,
                children: []
            };
            this.buildSectionTree(this.rootSection, this.sectionTree);
            this.renderTableOfContent(this.sectionTree, this.tableOfContentsNav);
            this.scrollEventListener = this.scrollEvent.bind(this);
            window.addEventListener("scroll", this.scrollEventListener);
            this.resizeEventListener = this.resizeEvent.bind(this);
            window.addEventListener("resize", this.resizeEventListener);
            this.updateActiveNavLinks();
        }
        disconnectedCallback() {
            window.removeEventListener("scroll", this.scrollEventListener);
            window.removeEventListener("resize", this.resizeEventListener);
        }
        buildSectionTree(element, parent) {
            for (const child of element.children) {
                if (child.tagName === "TOBAGO-SECTION") {
                    const sectionNode = {
                        id: child.id,
                        title: this.getSectionTitle(child),
                        level: parent ? parent.level + 1 : 0,
                        children: []
                    };
                    parent.children.push(sectionNode);
                    this.buildSectionTree(child, sectionNode);
                }
                else {
                    this.buildSectionTree(child, parent);
                }
            }
        }
        getSectionTitle(section) {
            const titleSpan = section.querySelector(".tobago-header span");
            return titleSpan ? titleSpan.textContent.trim() : section.id;
        }
        renderTableOfContent(node, parent) {
            const nodeId = this.sanitize(node.id);
            const nodeTitle = this.sanitize(node.title);
            parent.insertAdjacentHTML("afterbegin", `<a href="#${nodeId}">${nodeTitle}</a>`);
            const children = node.children;
            if (children.length > 0) {
                const ul = document.createElement("ul");
                parent.insertAdjacentElement("beforeend", ul);
                for (const child of children) {
                    const li = document.createElement("li");
                    ul.insertAdjacentElement("beforeend", li);
                    this.renderTableOfContent(child, li);
                }
            }
        }
        sanitize(value) {
            return value.split("<").join("&lt;").split(">").join("&gt;");
        }
        scrollEvent(event) {
            requestAnimationFrame(() => {
                this.updateActiveNavLinks();
                this.updateTableOfContentsScrollPosition();
            });
        }
        resizeEvent(event) {
            requestAnimationFrame(() => {
                this.updateActiveNavLinks();
                this.updateLeft();
            });
        }
        updateActiveNavLinks() {
            const scrollPaddingTop = parseFloat(this.rootHtml.style.scrollPaddingTop) + 1;
            const footerMarginTop = parseFloat(getComputedStyle(this.fixedFooter).marginTop);
            const bottomBorder = this.fixedFooter.getBoundingClientRect().top - footerMarginTop;
            const sectionIds = [this.rootSection.id];
            const sectionTops = [this.rootSection.getBoundingClientRect().top];
            const sectionBottoms = [this.rootSection.getBoundingClientRect().bottom];
            this.rootSection.querySelectorAll("tobago-section").forEach((section) => {
                sectionIds.push(section.id);
                const sectionRect = section.getBoundingClientRect();
                sectionTops.push(sectionRect.top);
                sectionBottoms.push(sectionRect.bottom);
            });
            for (let i = 0; i < sectionIds.length; i++) {
                const lastIndex = i >= sectionIds.length - 1;
                const top = sectionTops[i];
                const bottom = lastIndex ? sectionBottoms[i] : sectionTops[i + 1];
                const navLink = this.tableOfContentsNav.querySelector("a[href='#" + sectionIds[i] + "']");
                if (top > bottomBorder || bottom < scrollPaddingTop) {
                    navLink.classList.remove("active");
                }
                else {
                    navLink.classList.add("active");
                }
            }
        }
        updateTableOfContentsScrollPosition() {
            const activeNavLinks = this.activeNavLinks;
            if (activeNavLinks.length > 0) {
                const centerIndex = Math.floor(activeNavLinks.length / 2);
                activeNavLinks.item(centerIndex).scrollIntoView({ block: "center", behavior: "smooth" });
            }
        }
        updateLeft() {
            this.style.left = this.parentElement.getBoundingClientRect().left + "px";
        }
        get rootHtml() {
            const rootNode = this.getRootNode();
            return rootNode.querySelector("html");
        }
        get fixedHeader() {
            const rootNode = this.getRootNode();
            return rootNode.querySelector("tobago-header.sticky-top");
        }
        get rootSection() {
            const rootNode = this.getRootNode();
            return rootNode.getElementById("page:content");
        }
        get tableOfContentsNav() {
            const rootNode = this.getRootNode();
            return rootNode.getElementById("tableOfContents");
        }
        get activeNavLinks() {
            return this.tableOfContentsNav.querySelectorAll("a.active");
        }
        get fixedFooter() {
            const rootNode = this.getRootNode();
            return rootNode.querySelector("tobago-footer.fixed-bottom");
        }
    }
    if (typeof window !== "undefined") {
        window.addEventListener("DOMContentLoaded", function () {
            if (!window.customElements.get("demo-sidebar")) {
                window.customElements.define("demo-sidebar", Sidebar);
            }
        });
    }

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
    // todo: this code is not tested
    class DemoTest {
        static initTestLinks(element) {
            const runLink = document.getElementById("page:header:runtest");
            if (runLink && parent.document.getElementById("qunit")) {
                runLink.classList.add("d-none");
            }
        }
        static initTestFrame(element) {
            const testFrame = document.getElementById("page:testframe");
            if (testFrame) {
                alert("Might currently not working...");
                testFrame.addEventListener("onload", function () {
                    // XXX is element an iframe?
                    const iframe = element;
                    iframe.style.height = "" + iframe.contentWindow.document.querySelector("body").scrollHeight + "px";
                });
            }
        }
    }
    document.addEventListener("DOMContentLoaded", function (event) {
        let element = document.documentElement; // XXX fixme
        // XXX init areas after Faces AJAX update not implemented yet!
        DemoTest.initTestLinks(element);
        DemoTest.initTestFrame(element);
    });

}));
//# sourceMappingURL=demo.js.map
