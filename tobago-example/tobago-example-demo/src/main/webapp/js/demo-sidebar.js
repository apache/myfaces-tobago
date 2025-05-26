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
export class Sidebar extends HTMLElement {
    connectedCallback() {
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
        this.insertAdjacentHTML("afterbegin", "<strong>On this page</strong><nav id='tableOfContents'></nav>");
        this.sectionTree = {
            id: this.rootSection.id,
            title: this.getSectionTitle(this.rootSection),
            level: 0,
            children: []
        };
        this.buildSectionTree(this.rootSection, this.sectionTree);
        this.renderTableOfContent(this.sectionTree, this.tableOfContentsNav);
        this.updateActiveNavLink();
        this.scrollEventListener = this.scrollEvent.bind(this);
        window.addEventListener("scroll", this.scrollEventListener);
    }
    disconnectedCallback() {
        window.removeEventListener("scroll", this.scrollEventListener);
        window.clearTimeout(this.scrollTimeout);
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
        const div = document.createElement("div");
        div.innerHTML = value;
        return div.innerText;
    }
    scrollEvent(event) {
        window.clearTimeout(this.scrollTimeout);
        this.scrollTimeout = window.setTimeout(() => this.updateActiveNavLink(), 10);
    }
    updateActiveNavLink() {
        const scrollPaddingTop = parseFloat(this.rootHtml.style.scrollPaddingTop) + 1;
        let activeSectionId = this.rootSection.id;
        let activeSectionTop = this.rootSection.getBoundingClientRect().top;
        this.rootSection.querySelectorAll("tobago-section").forEach((section) => {
            const sectionTop = section.getBoundingClientRect().top;
            if (sectionTop > activeSectionTop && sectionTop <= scrollPaddingTop) {
                activeSectionId = section.id;
                activeSectionTop = sectionTop;
            }
        });
        this.navLinks.forEach((navLink) => {
            if (navLink.href.endsWith("#" + activeSectionId)) {
                navLink.classList.add("active");
            }
            else {
                navLink.classList.remove("active");
            }
        });
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
    get navLinks() {
        return this.tableOfContentsNav.querySelectorAll("a");
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
//# sourceMappingURL=demo-sidebar.js.map