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
        const centerIndex = Math.floor(activeNavLinks.length / 2);
        activeNavLinks.item(centerIndex).scrollIntoView({ block: "center", behavior: "smooth" });
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
//# sourceMappingURL=demo-sidebar.js.map