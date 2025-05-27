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

interface SectionNode {
  id: string;
  title: string;
  level: number;
  children: SectionNode[];
}

export class Sidebar extends HTMLElement {
  private sectionTree: SectionNode;
  private scrollEventListener;
  private resizeEventListener;

  connectedCallback(): void {
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

  disconnectedCallback(): void {
    window.removeEventListener("scroll", this.scrollEventListener);
    window.removeEventListener("resize", this.resizeEventListener);
  }

  private buildSectionTree(element: HTMLElement, parent: SectionNode): void {
    for (const child of element.children) {
      if (child.tagName === "TOBAGO-SECTION") {
        const sectionNode: SectionNode = {
          id: child.id,
          title: this.getSectionTitle(<HTMLElement>child),
          level: parent ? parent.level + 1 : 0,
          children: []
        };
        parent.children.push(sectionNode);
        this.buildSectionTree(<HTMLElement>child, sectionNode);
      } else {
        this.buildSectionTree(<HTMLElement>child, parent);
      }
    }
  }

  private getSectionTitle(section: HTMLElement): string {
    const titleSpan = section.querySelector(".tobago-header span");
    return titleSpan ? titleSpan.textContent.trim() : section.id;
  }

  private renderTableOfContent(node: SectionNode, parent: HTMLElement): void {
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

  private sanitize(value: string): string {
    return value.split("<").join("&lt;").split(">").join("&gt;");
  }

  private scrollEvent(event: Event): void {
    requestAnimationFrame(() => {
      this.updateActiveNavLinks();
      this.updateTableOfContentsScrollPosition();
    });
  }

  private resizeEvent(event: Event): void {
    requestAnimationFrame(() => {
      this.updateActiveNavLinks();
      this.updateLeft();
    });
  }

  private updateActiveNavLinks(): void {
    const scrollPaddingTop = parseFloat(this.rootHtml.style.scrollPaddingTop) + 1;
    const footerMarginTop = parseFloat(getComputedStyle(this.fixedFooter).marginTop);
    const bottomBorder = this.fixedFooter.getBoundingClientRect().top - footerMarginTop;

    const sectionIds = [this.rootSection.id];
    const sectionTops = [this.rootSection.getBoundingClientRect().top];
    const sectionBottoms = [this.rootSection.getBoundingClientRect().bottom];
    this.rootSection.querySelectorAll("tobago-section").forEach((section: HTMLElement) => {
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
      } else {
        navLink.classList.add("active");
      }
    }
  }

  private updateTableOfContentsScrollPosition(): void {
    const activeNavLinks = this.activeNavLinks;
    const centerIndex = Math.floor(activeNavLinks.length / 2);
    activeNavLinks.item(centerIndex).scrollIntoView({block: "center", behavior: "smooth"});
  }

  private updateLeft(): void {
    this.style.left = this.parentElement.getBoundingClientRect().left + "px";
  }

  get rootHtml(): HTMLElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.querySelector<HTMLElement>("html");
  }

  get fixedHeader(): HTMLElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.querySelector<HTMLElement>("tobago-header.sticky-top");
  }

  get rootSection(): HTMLElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.getElementById("page:content");
  }

  get tableOfContentsNav(): HTMLElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.getElementById("tableOfContents");
  }

  get activeNavLinks(): NodeListOf<HTMLAnchorElement> {
    return this.tableOfContentsNav.querySelectorAll("a.active");
  }

  get fixedFooter(): HTMLElement | null {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.querySelector<HTMLElement>("tobago-footer.fixed-bottom");
  }
}

if (typeof window !== "undefined") {
  window.addEventListener("DOMContentLoaded", function (): void {
    if (!window.customElements.get("demo-sidebar")) {
      window.customElements.define("demo-sidebar", Sidebar);
    }
  });
}
