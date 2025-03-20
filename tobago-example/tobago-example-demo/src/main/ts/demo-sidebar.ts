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

import { html, render } from "lit-html";

interface SectionNode {
  id: string;
  title: string;
  level: number;
  children: SectionNode[];
}

export class Sidebar extends HTMLElement {
  private sectionTree: SectionNode[] = [];
  private scrollThrottleTimeout?: number;
  private resizeHandler = this.throttle(this.adjustFixedPosition.bind(this), 100);

  connectedCallback(): void {
    // Build the section tree hierarchy
    this.buildSectionTree();

    // Render the initial tree
    this.renderContentTree();

    // Add event listeners for page changes
    window.addEventListener("resize", this.resizeHandler);

    // Initial position adjustment
    this.adjustFixedPosition();

    // Initial scroll to hash if present with proper timing
    if (window.location.hash) {
      this.handleHashChange();
    }

    // Update active section in all cases
    this.updateActiveSection();
  }

  disconnectedCallback(): void {
    // Clean up event listeners
    window.removeEventListener("resize", this.resizeHandler);

    // Clear any pending timeouts
    if (this.scrollThrottleTimeout !== null) {
      window.clearTimeout(this.scrollThrottleTimeout);
      this.scrollThrottleTimeout = null;
    }
  }

  private throttle(func: Function, delay: number): () => void {
    let lastCall = 0;
    return function(): void {
      const now = new Date().getTime();
      if (now - lastCall >= delay) {
        lastCall = now;
        func();
      }
    };
  }

  private adjustFixedPosition(): void {
    requestAnimationFrame(() => {
      try {
        // Calculate the offset only once
        const topOffset = Math.max(20, this.headerHeight + 10);

        // Then batch all DOM writes
        this.style.top = `${topOffset}px`;
        this.style.maxHeight = `calc(100vh - ${topOffset + 20 + this.footerHeight}px)`;
      } catch (error) {
        console.warn("Error adjusting sidebar position:", error);
      }
    });
  }

  private buildSectionTree(): void {
    // Create a root node to start the recursion
    const rootNode: SectionNode = {
      id: "root",
      title: "Root",
      level: 0,
      children: []
    };

    // Start recursive building from the document body with level 1
    this.buildSectionTreeRecursive(document.body, 1, rootNode);

    // Set the children of the root node as the section tree
    this.sectionTree = rootNode.children;
  }

  private buildSectionTreeRecursive(element: Element, level: number, parent: SectionNode): void {
    for (let i = 0; i < element.children.length; i++) {
      const child = element.children[i];
      if (child.tagName === "TOBAGO-SECTION" && child.id.startsWith("page:mainForm:")) {
        const sectionNode: SectionNode = {
          id: child.id,
          title: this.getSectionTitle(child),
          level: level,
          children: []
        };
        parent.children.push(sectionNode);
        this.buildSectionTreeRecursive(child, (level + 1), sectionNode);
      } else {
        this.buildSectionTreeRecursive(child, level, parent);
      }
    }
  }

  private updateActiveSection(): void {
    requestAnimationFrame(() => {
      try {
        // Remove active class from all links
        const links = this.querySelectorAll(".sidebar-link.active");
        links.forEach(link => link.classList.remove("active"));

        // Get current hash without the #
        const currentHash = window.location.hash.substring(1);

        if (currentHash) {
          // Find and activate the correct link - use attribute selector for better performance
          const activeLink = this.querySelector(`a.sidebar-link[href="#${CSS.escape(currentHash)}"]`);
          if (activeLink) {
            activeLink.classList.add("active");

            // Expand parent sections if needed
            let parent = activeLink.closest("li")?.parentElement;
            while (parent) {
              if (parent.classList.contains("sidebar-submenu")) {
                parent.classList.add("show");
                const toggleButton = parent.previousElementSibling?.querySelector(".sidebar-toggle");
                if (toggleButton) {
                  toggleButton.setAttribute("aria-expanded", "true");
                  toggleButton.classList.remove("collapsed");
                }
              }
              parent = parent.parentElement;
            }
          }
        }
      } catch (error) {
        console.warn("Error updating active section:", error);
      }
    });
  }

  private getSectionTitle(section: Element): string {
    const titleSpan = section.querySelector(".tobago-header span");
    return titleSpan ? titleSpan.textContent.trim() : section.id;
  }

  private renderContentTree(): void {
    // Create template with lit-html
    const template = html`
      <h5 class="sidebar-title">On this page</h5>
      <div class="sidebar-tree">
        ${this.renderTreeNodes(this.sectionTree)}
      </div>
    `;

    // Render to this element
    render(template, this);

    // Set up event handlers after rendering
    this.setupEventHandlers();
  }

  private setupEventHandlers(): void {
    // Use event delegation for better performance
    this.addEventListener("click", (event) => {
      const target = event.target as HTMLElement;
      if (target.classList.contains("sidebar-link") ||
          target.closest(".sidebar-link")) {

        const link = target.classList.contains("sidebar-link") ?
            target : target.closest(".sidebar-link") as HTMLElement;

        event.preventDefault();
        event.stopPropagation();

        const href = link.getAttribute("href");
        if (href && href.startsWith("#")) {
          const targetId = href.substring(1);
          this.navigateToSection(targetId);
        }
      }
    });
  }

  private renderTreeNodes(nodes: SectionNode[]): any {
    return html`
      <ul class="nav flex-column sidebar-nav">
        ${nodes.map(node => html`
          <li class="nav-item">
            <a class="nav-link sidebar-link" href="#${node.id}">
              ${node.title}
            </a>
            ${node.children.length > 0
                ? html`<div class="sidebar-child-items">${this.renderTreeNodes(node.children)}</div>`
                : null
            }
          </li>
        `)}
      </ul>
    `;
  }

  private navigateToSection(targetId: string): void {
    try {
      // Use querySelector with attribute selector, with CSS escaping for safety
      const target = document.querySelector(`[id="${CSS.escape(targetId)}"]`);

      if (target && target instanceof HTMLElement) {
        // Update URL hash without triggering scroll
        history.replaceState(null, "", `#${targetId}`);

        // Get the clicked link directly and update its state
        const clickedLink = this.querySelector(`a.sidebar-link[href="#${CSS.escape(targetId)}"]`);
        if (clickedLink) {
          // Remove active class from all links
          const links = this.querySelectorAll(".sidebar-link.active");
          links.forEach(link => link.classList.remove("active"));

          // Add active class to clicked link
          clickedLink.classList.add("active");
        }

        // Schedule the scroll
        this.scrollToElement(target);

        // Remove focus from current element after navigation
        if (document.activeElement instanceof HTMLElement) {
          document.activeElement.blur();
        }
      }
    } catch (error) {
      console.warn("Error navigating to section:", error);
    }
  }

  private handleHashChange(): void {
    // Clear previous timeout if it exists
    if (this.scrollThrottleTimeout !== null) {
      window.clearTimeout(this.scrollThrottleTimeout);
    }

    // Set a new timeout
    this.scrollThrottleTimeout = window.setTimeout(() => {
      const hash = window.location.hash.substring(1);
      if (hash) {
        const target = document.querySelector(`[id="${CSS.escape(hash)}"]`);
        if (target instanceof HTMLElement) {
          this.scrollToElement(target);
        }
      }
      this.updateActiveSection();
      this.scrollThrottleTimeout = null;
    }, 50); // Short timeout to batch multiple hash changes
  }

  private scrollToElement(targetElement: HTMLElement): void {
    requestAnimationFrame(() => {
      try {
        // Use getBoundingClientRect for more accurate positioning
        const rect = targetElement.getBoundingClientRect();
        const scrollTop = window.scrollY || document.documentElement.scrollTop;

        // Calculate position with offset
        const targetPosition = Math.max(0, rect.top + scrollTop - this.scrollOffset);

        // Perform the scroll
        window.scrollTo({
          top: targetPosition,
          behavior: "smooth"
        });
      } catch (error) {
        console.warn("Error scrolling to element:", error);
      }
    });
  }

  get header(): HTMLElement | null {
    return document.querySelector<HTMLElement>("tobago-header.sticky-top");
  }

  get headerHeight(): number {
    return this.header ? this.header.offsetHeight : 0;
  }

  get footer(): HTMLElement | null {
    return document.querySelector<HTMLElement>("tobago-footer.fixed-bottom");
  }

  get footerHeight(): number {
    if (!this.footer) {
      return 0;
    }

    const style = getComputedStyle(this.footer);
    return this.footer.offsetHeight +
        parseFloat(style.marginTop) +
        parseFloat(style.marginBottom);
  }

  get scrollOffset(): number {
    const attributeValue = this.getAttribute("scroll-offset");
    return attributeValue ? parseInt(attributeValue, 10) : 70;
  }
}

// Register the custom element
if (typeof window !== "undefined") {
  window.addEventListener("DOMContentLoaded", function(): void {
    if (!window.customElements.get("demo-sidebar")) {
      window.customElements.define("demo-sidebar", Sidebar);
    }
  });
}
