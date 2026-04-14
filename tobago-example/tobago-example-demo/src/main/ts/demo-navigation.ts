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

export class Navigation extends HTMLElement {
  private headerResizeObserver: ResizeObserver;
  private footerResizeObserver: ResizeObserver;

  connectedCallback(): void {
    if (this.fixedHeader) {
      this.headerResizeObserver = new ResizeObserver(() => this.updatePositionAndSize());
      this.headerResizeObserver.observe(this.fixedHeader);
    }
    if (this.fixedFooter) {
      this.footerResizeObserver = new ResizeObserver(() => this.updatePositionAndSize());
      this.footerResizeObserver.observe(this.fixedFooter);
    }
    this.updatePositionAndSize();
    this.activeLink?.scrollIntoView({block: "center"});
  }

  disconnectedCallback(): void {
    this.headerResizeObserver?.disconnect();
    this.footerResizeObserver?.disconnect();
  }

  private updatePositionAndSize(): void {
    const headerHeight = this.fixedHeader.offsetHeight;
    const footerHeight = this.fixedFooter.offsetHeight;

    this.navigationExpanded.style.top = "calc(" + headerHeight + " + 1rem)";
    this.navigationExpanded.style.height = "calc(100vh - " + headerHeight + " - " + footerHeight + " - 2rem)";
  }

  get navigationExpanded(): HTMLElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.querySelector<HTMLElement>("tobago-panel.navigation-expanded");
  }

  get fixedHeader(): HTMLElement {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.querySelector<HTMLElement>("tobago-header.sticky-top");
  }

  get fixedFooter(): HTMLElement | null {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    return rootNode.querySelector<HTMLElement>("tobago-footer.fixed-bottom");
  }

  get activeLink(): HTMLElement {
    return this.querySelector(".tobago-link.active");
  }
}

if (typeof window !== "undefined") {
  window.addEventListener("DOMContentLoaded", function (): void {
    if (!window.customElements.get("demo-navigation")) {
      window.customElements.define("demo-navigation", Navigation);
    }
  });
}
