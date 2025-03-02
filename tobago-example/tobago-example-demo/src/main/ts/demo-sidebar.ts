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
  private sectionElements: HTMLElement[] = [];
  private resizeObserver: ResizeObserver | null = null;
  private scrollThrottleTimeout: number | null = null;
  private hashChangeHandler: () => void;
  private resizeHandler: () => void;

  /**
   * Gets the scroll offset from an attribute or uses the default
   */
  get scrollOffset(): number {
    const attributeValue = this.getAttribute("scroll-offset");
    return attributeValue ? parseInt(attributeValue, 10) : 70;
  }

  constructor() {
    super();
    // Bind methods to preserve this context
    this.hashChangeHandler = this.handleHashChange.bind(this);
    this.resizeHandler = this.throttle(this.adjustFixedPosition.bind(this), 100);
  }

  connectedCallback(): void {
    // Cache all section elements first to avoid repeated DOM queries
    this.sectionElements = Array.from(document.querySelectorAll<HTMLElement>("tobago-section[id^='page:mainForm:']"));

    // Build the section tree hierarchy once
    this.buildSectionTree();

    // Render the initial tree
    this.renderContentTree();

    // Add event listeners for page changes
    window.addEventListener("hashchange", this.hashChangeHandler);
    window.addEventListener("resize", this.resizeHandler);

    // Use ResizeObserver instead of window resize for better performance
    this.setupResizeObserver();

    // Initial position adjustment
    this.adjustFixedPosition();

    // Initial scroll to hash if present with proper timing
    if (window.location.hash) {
      requestAnimationFrame(() => {
        this.handleHashChange();
        this.updateActiveSection();
      });
    } else {
      this.updateActiveSection();
    }
  }

  disconnectedCallback(): void {
    // Clean up event listeners
    window.removeEventListener("hashchange", this.hashChangeHandler);
    window.removeEventListener("resize", this.resizeHandler);

    // Clean up resize observer
    if (this.resizeObserver) {
      this.resizeObserver.disconnect();
      this.resizeObserver = null;
    }

    // Clear any pending timeouts
    if (this.scrollThrottleTimeout !== null) {
      window.clearTimeout(this.scrollThrottleTimeout);
      this.scrollThrottleTimeout = null;
    }
  }

  /**
   * Throttle function to limit execution frequency
   */
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

  /**
   * Set up resize observer for more efficient layout adjustments
   */
  private setupResizeObserver(): void {
    if ("ResizeObserver" in window) {
      this.resizeObserver = new ResizeObserver(this.throttle(() => {
        this.adjustFixedPosition();
      }, 100));

      // Observe header elements that might affect positioning
      const header = document.querySelector("header");
      const navbar = document.querySelector(".navbar");

      if (header) this.resizeObserver.observe(header);
      if (navbar) this.resizeObserver.observe(navbar);
    }
  }

  /**
   * Adjusts the fixed position based on current header height
   * Using requestAnimationFrame to batch layout reads/writes
   */
  private adjustFixedPosition(): void {
    requestAnimationFrame(() => {
      try {
        // Batch all DOM reads first
        const headerHeight = document.querySelector("header")?.clientHeight || 0;
        const navbarHeight = document.querySelector(".navbar")?.clientHeight || 0;
        const fixedTopHeight = document.querySelector(".fixed-top")?.clientHeight || 0;

        // Calculate the offset only once
        const topOffset = Math.max(20, headerHeight, navbarHeight + 10, fixedTopHeight + 10);

        // Then batch all DOM writes
        this.style.top = `${topOffset}px`;
        this.style.maxHeight = `calc(100vh - ${topOffset + 20}px)`;
      } catch (error) {
        console.warn("Error adjusting sidebar position:", error);
      }
    });
  }

  /**
   * Builds the hierarchical tree structure from the DOM using a single-pass algorithm
   */
  buildSectionTree(): void {
    // Reset the tree
    this.sectionTree = [];

    if (this.sectionElements.length === 0) {
      return;
    }

    // Sort by DOM order (natural document position)
    // This avoids the expensive getElementPosition method
    this.sectionElements.sort((a, b) => {
      // Use compareDocumentPosition for efficient DOM ordering
      const position = a.compareDocumentPosition(b);
      return position & Node.DOCUMENT_POSITION_FOLLOWING ? -1 : 1;
    });

    // Use a stack-based approach for building the tree (single pass)
    const stack: SectionNode[] = [];

    this.sectionElements.forEach(section => {
      const level = this.getSectionLevel(section);
      const newNode: SectionNode = {
        element: section,
        id: section.id,
        title: this.getSectionTitle(section),
        level: level,
        children: [],
        expanded: true
      };

      // Pop the stack until we find the parent level
      while (stack.length > 0 && stack[stack.length - 1].level >= level) {
        stack.pop();
      }

      if (stack.length === 0) {
        // This is a top-level node
        this.sectionTree.push(newNode);
      } else {
        // Add as child to the current parent
        stack[stack.length - 1].children.push(newNode);
      }

      // Push this node to the stack
      stack.push(newNode);
    });
  }

  /**
   * Determines the section level based on nesting or heading level
   * Caches section heading information to avoid repeated DOM traversal
   */
  getSectionLevel(section: HTMLElement): number {
    // Cache for section levels
    if ((section as any)._cachedLevel) {
      return (section as any)._cachedLevel;
    }

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
    if (level === 1) {
      const heading = section.querySelector("h1, h2, h3, h4, h5, h6");
      if (heading) {
        const headingLevel = parseInt(heading.tagName.substring(1));
        level = headingLevel - 1;
      }
    }

    // Cache the result
    (section as any)._cachedLevel = level;
    return level;
  }

  /**
   * Updates the active section based on current URL hash
   * Uses more efficient selectors and DOM operations
   */
  updateActiveSection(): void {
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

  /**
   * Gets the title from a section with caching for performance
   */
  private getSectionTitle(section: HTMLElement): string {
    // Use cached title if available
    if ((section as any)._cachedTitle) {
      return (section as any)._cachedTitle;
    }

    try {
      let title = "";

      // Try to get title from header element
      const header = section.querySelector(".tobago-header");
      if (header) {
        const heading = header.querySelector("h1, h2, h3, h4, h5, h6");
        if (heading) {
          title = heading.textContent?.trim() || "";
        }

        if (!title) {
          const span = header.querySelector("span");
          if (span) {
            title = span.textContent?.trim() || "";
          }
        }
      }

      // Fallback: Try to find any heading in the section
      if (!title) {
        const heading = section.querySelector("h1, h2, h3, h4, h5, h6");
        if (heading) {
          title = heading.textContent?.trim() || "";
        }
      }

      // Last resort: use ID
      if (!title) {
        title = section.id || "Unknown Section";
      }

      // Cache the result
      (section as any)._cachedTitle = title;
      return title;

    } catch (error) {
      console.warn("Error getting section title:", error);
      return section.id || "Unknown Section";
    }
  }

  /**
   * Renders the content tree with the hierarchical structure
   */
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

    // Set up event handlers after rendering
    this.setupEventHandlers();
  }

  /**
   * Set up event handlers for navigation links
   * Binds events once after rendering
   */
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

  /**
   * Recursively renders tree nodes using lit-html
   */
  renderTreeNodes(nodes: SectionNode[]): any {
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

  /**
   * Navigate to a section with proper scrolling and history update
   */
  private navigateToSection(targetId: string): void {
    try {
      // Use querySelector with attribute selector, with CSS escaping for safety
      const target = document.querySelector(`[id="${CSS.escape(targetId)}"]`);

      if (target && target instanceof HTMLElement) {
        // Update URL hash without triggering scroll
        history.replaceState(null, "", `#${targetId}`);

        // Schedule the scroll
        this.scrollToElement(target);

        // Update active section
        this.updateActiveSection();
      }
    } catch (error) {
      console.warn("Error navigating to section:", error);
    }
  }

  /**
   * Handle hash change events with throttling
   */
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

  /**
   * Scrolls to the specified element with appropriate offset
   * Uses more efficient position calculation
   */
  private scrollToElement(targetElement: HTMLElement): void {
    requestAnimationFrame(() => {
      try {
        // Get offset from attribute or use default
        const offset = this.scrollOffset;

        // Use getBoundingClientRect for more accurate positioning
        const rect = targetElement.getBoundingClientRect();
        const scrollTop = window.scrollY || document.documentElement.scrollTop;

        // Calculate position with offset
        const targetPosition = Math.max(0, rect.top + scrollTop - offset);

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
}

// Register the custom element
if (typeof window !== "undefined") {
  window.addEventListener("DOMContentLoaded", function(): void {
    if (!window.customElements.get("demo-sidebar")) {
      window.customElements.define("demo-sidebar", Sidebar);
    }
  });
}
