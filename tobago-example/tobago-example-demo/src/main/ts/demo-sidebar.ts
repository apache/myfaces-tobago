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

import * as bootstrap from "bootstrap";
import { html, render } from "lit-html";

interface SectionNode {
  element: HTMLElement;
  id: string;
  title: string;
  level: number;
  children: SectionNode[];
  expanded: boolean;
}

export class Sidebar extends HTMLElement {
  private sectionTree: SectionNode[] = [];

  // Gets the scroll offset from an attribute or uses the default
  get scrollOffset(): number {
    const attributeValue = this.getAttribute("scroll-offset");
    return attributeValue ? parseInt(attributeValue, 10) : 70;
  }

  constructor() {
    super();
  }

  connectedCallback(): void {
    // Build the section tree hierarchy
    this.buildSectionTree();

    // Render the initial tree
    this.renderContentTree();

    // Add event listener for page changes
    window.addEventListener("hashchange", () => {
      this.handleHashChange();
    });

    // Initial scroll to hash if present
    if (window.location.hash) {
      // Delay to ensure DOM is ready
      setTimeout(() => this.handleHashChange(), 100);
    }

    // Initial highlight of active section
    this.updateActiveSection();
  }

  // Builds the hierarchical tree structure from the DOM
  buildSectionTree(): void {
    const sections = Array.from(this.sections);

    // Reset the tree
    this.sectionTree = [];

    if (sections.length === 0) {
      return;
    }

    // First pass: create all nodes with their level
    const allNodes: SectionNode[] = sections.map(section => {
      const level = this.getSectionLevel(section);
      return {
        element: section,
        id: section.id,
        title: this.getSectionTitle(section),
        level: level,
        children: [],
        expanded: true
      };
    });

    // Sort by DOM order
    allNodes.sort((a, b) => {
      return this.getElementPosition(a.element) - this.getElementPosition(b.element);
    });

    // Second pass: build the tree structure
    for (let i = 0; i < allNodes.length; i++) {
      const currentNode = allNodes[i];

      if (currentNode.level === 1) {
        // Top level sections go directly into the tree
        this.sectionTree.push(currentNode);
      } else {
        // Find the parent for this node
        let parentIndex = i - 1;
        while (parentIndex >= 0) {
          if (allNodes[parentIndex].level < currentNode.level) {
            allNodes[parentIndex].children.push(currentNode);
            break;
          }
          parentIndex--;
        }

        // If no parent found, add to the root level
        if (parentIndex < 0) {
          this.sectionTree.push(currentNode);
        }
      }
    }
  }

  // Determines the section level based on nesting or heading level
  getSectionLevel(section: HTMLElement): number {
    // Try to determine level from the parent-child relationship
    let parent = section.parentElement;
    let level = 1;

    while (parent) {
      if (parent.tagName.toLowerCase() === "tobago-section") {
        level++;
      }
      parent = parent.parentElement;
    }

    // Fallback to heading level if available
    const heading = section.querySelector("h1, h2, h3, h4, h5, h6");
    if (heading && level === 1) {
      const headingLevel = parseInt(heading.tagName.substring(1));
      return headingLevel - 1;
    }

    return level;
  }

  // Get the position of an element in the DOM tree for sorting
  getElementPosition(element: HTMLElement): number {
    const allElements = document.querySelectorAll("*");
    return Array.prototype.indexOf.call(allElements, element);
  }

  // Updates the active section based on current URL hash
  updateActiveSection(): void {
    // Remove active class from all links
    const links = this.querySelectorAll(".sidebar-link");
    links.forEach(link => link.classList.remove("active"));

    // Get current hash without the #
    const currentHash = window.location.hash.substring(1);

    if (currentHash) {
      // Find and activate the correct link
      const activeLink = this.querySelector(`a[href="#${currentHash}"]`);
      if (activeLink) {
        activeLink.classList.add("active");

        // Expand parent sections if needed
        let parent = activeLink.closest("li")?.parentElement;
        while (parent) {
          if (parent.classList.contains("sidebar-submenu")) {
            parent.classList.add("show");
          }
          parent = parent.parentElement;
        }
      }
    }
  }

  // Gets the page content element
  get pageContent(): HTMLElement | null {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.getElementById("page:content");
  }

  // Gets all sections on the page
  get sections(): NodeListOf<HTMLElement> {
    return document.querySelectorAll<HTMLElement>(
        "tobago-section[id^='page:mainForm:']"
    );
  }

  // Renders the content tree with the hierarchical structure
  renderContentTree(): void {
    // Create template with lit-html
    const template = html`
      <h5 class="sidebar-title">On this page</h5>
      <div class="sidebar-tree">
        ${this.renderTreeNodes(this.sectionTree)}
      </div>
    `;

    // Render to this element
    render(template, this);
  }

  // Recursively renders tree nodes using lit-html
  renderTreeNodes(nodes: SectionNode[]): any {
    // Bind this to a variable to use in the event listener
    const self = this;

    return html`
      <ul class="nav flex-column sidebar-nav">
        ${nodes.map(node => html`
          <li class="nav-item">
            <a class="nav-link sidebar-link" href="#${node.id}" @click="${(e: Event) => self.handleNavigation(e)}">
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

  // Extracts the title from a section
  private getSectionTitle(section: HTMLElement): string {
    try {
      // Try to get title from header element
      const header = section.querySelector(".tobago-header");
      if (header) {
        const heading = header.querySelector("h1, h2, h3, h4, h5, h6");
        if (heading) {
          return heading.textContent?.trim() || section.id;
        }

        const span = header.querySelector("span");
        if (span) {
          return span.textContent?.trim() || section.id;
        }
      }

      // Fallback: Try to find any heading in the section
      const heading = section.querySelector("h1, h2, h3, h4, h5, h6");
      if (heading) {
        return heading.textContent?.trim() || section.id;
      }

      // Last resort: use ID
      return section.id;
    } catch (error) {
      return section.id || "Unknown Section";
    }
  }

  // Handles navigation when a link is clicked
  private handleNavigation(event: Event): void {
    event.preventDefault();
    event.stopPropagation();

    try {
      // Get the ID from the href attribute
      const linkElement = event.currentTarget as HTMLAnchorElement;
      const targetId = linkElement.getAttribute("href")?.substring(1);

      if (!targetId) {
        return;
      }

      // Use querySelector with attribute selector to avoid issues with special characters in IDs
      const target = document.querySelector(`[id="${targetId}"]`);

      if (target && target instanceof HTMLElement) {
        this.scrollToElement(target);

        // Update URL hash without triggering scroll
        history.replaceState(null, "", `#${targetId}`);

        // Manually update active section
        this.updateActiveSection();
      }
    } catch (error) {
      // Silent error handling in production
    }
  }

  // Handle hash change events
  private handleHashChange(): void {
    const hash = window.location.hash.substring(1);
    if (hash) {
      const target = document.querySelector(`[id="${hash}"]`);
      if (target instanceof HTMLElement) {
        this.scrollToElement(target);
      }
    }
    this.updateActiveSection();
  }

  // Scrolls to the specified element with appropriate offset
  private scrollToElement(targetElement: HTMLElement): void {
    try {
      // Get offset from attribute or use default
      const offset = this.scrollOffset;

      // Calculate absolute position of element relative to the document
      let absoluteTop = 0;
      let element = targetElement;

      while (element && element instanceof HTMLElement) {
        absoluteTop += element.offsetTop;
        const offsetParent = element.offsetParent as HTMLElement;
        if (!offsetParent || !(offsetParent instanceof HTMLElement)) break;
        element = offsetParent;
      }

      // Apply offset
      const targetPosition = Math.max(0, absoluteTop - offset);

      // Perform the scroll
      window.scrollTo({
        top: targetPosition,
        behavior: "smooth"
      });
    } catch (error) {
      // Silent error handling in production
    }
  }
}

// Register the custom element
document.addEventListener("DOMContentLoaded", function (event: Event): void {
  if (!window.customElements.get("demo-sidebar")) {
    window.customElements.define("demo-sidebar", Sidebar);
  }
});
