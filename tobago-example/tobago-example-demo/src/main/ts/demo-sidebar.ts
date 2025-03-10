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
  private sectionElements: HTMLElement[] = [];
  private resizeObserver: ResizeObserver | null = null;
  private scrollThrottleTimeout: number | null = null;
  private hashChangeHandler: () => void;
  private resizeHandler = this.throttle(this.adjustFixedPosition.bind(this), 100);
  private isManualNavigation = false;
  private sectionObservers: IntersectionObserver[] = [];

  connectedCallback(): void {
    // Cache all section elements first to avoid repeated DOM queries
    this.sectionElements = Array.from(document.querySelectorAll<HTMLElement>("tobago-section[id^='page:mainForm:']"));

    // Build the section tree hierarchy once
    this.buildSectionTree();

    // Render the initial tree
    this.renderContentTree();

    // Add event listeners for page changes
    window.addEventListener("resize", this.resizeHandler);

    // Use ResizeObserver instead of window resize for better performance
    this.setupResizeObserver();

    // Setup intersection observers for each section
    this.setupIntersectionObservers();

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

    // Clean up resize observer
    if (this.resizeObserver) {
      this.resizeObserver.disconnect();
      this.resizeObserver = null;
    }

    // Clean up intersection observers
    this.sectionObservers.forEach(observer => observer.disconnect());
    this.sectionObservers = [];

    // Clear any pending timeouts
    if (this.scrollThrottleTimeout !== null) {
      window.clearTimeout(this.scrollThrottleTimeout);
      this.scrollThrottleTimeout = null;
    }
  }

  /**
   * Debounce function to execute after events stop
   */
  private debounce(func: Function, delay: number): () => void {
    let timeout: number | null = null;
    return function(): void {
      if (timeout !== null) {
        window.clearTimeout(timeout);
      }
      timeout = window.setTimeout(() => {
        func();
        timeout = null;
      }, delay);
    };
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

      // Observe header and footer elements that might affect positioning
      const header = document.querySelector("tobago-header.sticky-top");
      const footer = document.querySelector("tobago-footer");

      if (header) this.resizeObserver.observe(header);
      if (footer) this.resizeObserver.observe(footer);
    }
  }

  /**
   * Sets up Intersection Observers for tracking section visibility
   */
  private setupIntersectionObservers(): void {
    if (!("IntersectionObserver" in window)) return;

    // Clean up existing observers
    this.sectionObservers.forEach(observer => observer.disconnect());
    this.sectionObservers = [];

    // Get header height for offset calculation
    const headerHeight = this.getHeaderHeight();
    const scrollOffset = this.scrollOffset;
    const totalOffset = headerHeight + scrollOffset;

    // Create the observer options
    const options = {
      root: null,
      rootMargin: `-${totalOffset}px 0px -${window.innerHeight - totalOffset - 10}px 0px`,
      threshold: 0
    };

    // Create and setup observer
    const observer = new IntersectionObserver(entries => {
      if (this.isManualNavigation) return;

      // Find the top-most visible section
      const visibleEntries = entries.filter(entry => entry.isIntersecting);

      if (visibleEntries.length > 0) {
        // Sort by their position in the viewport (top to bottom)
        visibleEntries.sort((a, b) => {
          const rectA = a.boundingClientRect;
          const rectB = b.boundingClientRect;
          return rectA.top - rectB.top;
        });

        // Take the top-most visible section
        const topSection = visibleEntries[0].target as HTMLElement;
        const sectionId = topSection.id;

        // Update URL and active state
        history.replaceState(null, "", `#${sectionId}`);
        this.updateActiveSection();
      }
    }, options);

    // Observe all sections
    this.sectionElements.forEach(section => {
      observer.observe(section);
    });

    this.sectionObservers.push(observer);
  }

  /**
   * Adjusts the fixed position based on current header height
   * Using requestAnimationFrame to batch layout reads/writes
   */
  private adjustFixedPosition(): void {
    requestAnimationFrame(() => {
      try {
        // Get header and footer heights
        const headerHeight = this.getHeaderHeight();
        const footerHeight = this.getFooterHeight();

        // Calculate the offset only once
        const topOffset = Math.max(20, headerHeight + 10);

        // Then batch all DOM writes
        this.style.top = `${topOffset}px`;
        this.style.maxHeight = `calc(100vh - ${topOffset + 20 + footerHeight}px)`;
      } catch (error) {
        console.warn("Error adjusting sidebar position:", error);
      }
    });
  }

  /**
   * Builds the hierarchical tree structure from the DOM recursively
   */
  private buildSectionTree(): void {
    // Reset the tree
    this.sectionTree = [];

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

  /**
   * Recursively builds the section tree
   */
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

  /**
   * Determines the section level based on nesting or heading level
   * Caches section heading information to avoid repeated DOM traversal
   */
  private getSectionLevel(section: HTMLElement): number {
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

  /**
   * Gets the title from a section with caching for performance
   */
  private getSectionTitle(section: Element): string {
    const titleSpan = section.querySelector(".tobago-header span");
    return titleSpan ? titleSpan.textContent.trim() : section.id;
  }

  /**
   * Renders the content tree with the hierarchical structure
   */
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

  /**
   * Navigate to a section with proper scrolling and history update
   */
  private navigateToSection(targetId: string): void {
    try {
      // Set flag to indicate manual navigation
      this.isManualNavigation = true;

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

        // Reset the manual navigation flag after scrolling completes
        setTimeout(() => {
          this.isManualNavigation = false;
        }, 500); // Slightly longer than the scroll animation

        // Remove focus from current element after navigation
        if (document.activeElement instanceof HTMLElement) {
          document.activeElement.blur();
        }
      }
    } catch (error) {
      console.warn("Error navigating to section:", error);
      this.isManualNavigation = false;
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

  /**
   * Gets the header height using offsetHeight
   */
  private getHeaderHeight(): number {
    const header = document.querySelector("tobago-header.sticky-top") as HTMLElement;
    return header ? header.offsetHeight : 0;
  }

  /**
   * Gets the footer height accounting for margins
   */
  private getFooterHeight(): number {
    const footer = document.querySelector("tobago-footer.fixed-bottom");
    if (!footer) {
      return 0;
    }

    // Use the height property from Footer class if available
    if ((footer as any).height !== undefined) {
      return (footer as any).height;
    }

    // Fallback calculation if height property is not available
    const style = getComputedStyle(footer);
    return (footer as HTMLElement).offsetHeight +
        parseFloat(style.marginTop || "0") +
        parseFloat(style.marginBottom || "0");
  }

  /**
   * Gets the scroll offset from an attribute or uses the default
   */
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
