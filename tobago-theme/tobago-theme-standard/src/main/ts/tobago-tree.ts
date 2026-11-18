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

import {Selectable} from "./tobago-selectable";
import {Key} from "./tobago-key";
import {TreeNode} from "./tobago-tree-node";
import {EventListenerStore} from "./tobago-event-listener-store";

export class Tree extends HTMLElement {

  private listeners: EventListenerStore = new EventListenerStore();

  /** ID des zuletzt fokussierten Tree-Nodes (wird über AJAX-Updates hinweg gespeichert) */
  private static focusedNodeId: string | null = null;
  /** CSS-Selektor oder Tag des zuletzt fokussierten Unter-Elements (z. B. ".tobago-toggle" oder "input") */
  private static focusedSubElementClass: string | null = null;

  constructor() {
    super();

    this.handleKeydown = this.handleKeydown.bind(this);
  }

  connectedCallback(): void {
    this.listeners.add(this, "focusout", (event: FocusEvent) => {
      const target = event.target as HTMLElement;
      const node = target.closest("tobago-tree-node") as HTMLElement;
      if (node && node.id) {
        Tree.focusedNodeId = node.id;
        if (target.classList.contains("tobago-toggle")) {
          Tree.focusedSubElementClass = ".tobago-toggle";
        } else if (target.tagName === "INPUT") {
          Tree.focusedSubElementClass = "input";
        }
      }
    });

    if (Tree.focusedNodeId) {
      const savedNode = this.querySelector(`tobago-tree-node[id="${Tree.focusedNodeId}"]`) as HTMLElement;
      if (savedNode) {
        const targetSelector = Tree.focusedSubElementClass || ".tobago-toggle, input";
        const subEl = savedNode.querySelector(targetSelector) as HTMLElement;
        if (subEl) {
          subEl.focus();
        }
      }
    }
    this.listeners.add(this, "keydown", this.handleKeydown);
  }

  disconnectedCallback(): void {
    this.listeners.disconnect();
  }

  clearSelectedNodes(): void {
    this.hiddenInputSelected.value = "[]"; //empty set
  }

  addSelectedNode(selectedNode: number): void {
    const selectedNodes = new Set(JSON.parse(this.hiddenInputSelected.value));
    selectedNodes.add(selectedNode);
    this.hiddenInputSelected.value = JSON.stringify(Array.from(selectedNodes));
  }

  deleteSelectedNode(selectedNode: number): void {
    const selectedNodes = new Set(JSON.parse(this.hiddenInputSelected.value));
    selectedNodes.delete(selectedNode);
    this.hiddenInputSelected.value = JSON.stringify(Array.from(selectedNodes));
  }

  private getSelectedNodes(): NodeListOf<TreeNode> {
    const queryString: string[] = [];
    for (const selectedNodeIndex of JSON.parse(this.hiddenInputSelected.value)) {
      if (queryString.length > 0) {
        queryString.push(", ");
      }
      queryString.push("tobago-tree-node[index='");
      queryString.push(selectedNodeIndex);
      queryString.push("']");
    }

    if (queryString.length > 0) {
      return this.querySelectorAll(queryString.join(""));
    } else {
      return null;
    }
  }

  private get hiddenInputSelected(): HTMLInputElement {
    return this.querySelector(":scope > input[type=hidden].tobago-selected");
  }

  private clearExpandedNodes(): void {
    this.hiddenInputExpanded.value = "[]"; //empty set
  }

  private addExpandedNode(expandedNode: number): void {
    const expandedNodes = new Set(JSON.parse(this.hiddenInputExpanded.value));
    expandedNodes.add(expandedNode);
    this.hiddenInputExpanded.value = JSON.stringify(Array.from(expandedNodes));
  }

  private deleteExpandedNode(expandedNode: number): void {
    const expandedNodes = new Set(JSON.parse(this.hiddenInputExpanded.value));
    expandedNodes.delete(expandedNode);
    this.hiddenInputExpanded.value = JSON.stringify(Array.from(expandedNodes));
  }

  get hiddenInputExpanded(): HTMLInputElement {
    return this.querySelector(":scope > input[type=hidden].tobago-expanded");
  }

  get selectable(): Selectable {
    return Selectable[this.getAttribute("selectable")] as Selectable;
  }

  get nodes(): NodeListOf<TreeNode> {
    return this.querySelectorAll("tobago-tree-node");
  }

  get visibleNodes(): TreeNode[] {
    return Array.from(this.nodes).filter(node => !node.classList.contains("d-none"));
  }

  private get hasCheckboxes(): boolean {
    return this.querySelector("input[type=checkbox], input[type=radio]") !== null;
  }

  focusNode(node: HTMLElement): void {
    if (!node) {
      return;
    }

    const toggle = node.querySelector(".tobago-toggle") as HTMLElement;
    const input = node.querySelector("input[type=checkbox], input[type=radio]") as HTMLInputElement;

    if (input) {
      input.focus();
    } else if (toggle) {
      toggle.focus();
    }
  }

  private handleKeydown(event: KeyboardEvent): void {
    const target = event.target as HTMLElement;
    if (!target) {
      return;
    }

    let node = target.closest("tobago-tree-node") as HTMLElement;
    if (!node) {
      node = Array.from(this.nodes).find(n => n.contains(document.activeElement)) as HTMLElement;
    }
    if (!node) {
      return;
    }

    const toggle = node.querySelector(".tobago-toggle") as HTMLElement;
    const input = node.querySelector("input[type=checkbox], input[type=radio]") as HTMLInputElement;

    switch (event.key) {
      case Key.SPACE:
      case "Space":
      case Key.ENTER:
        this.handleSelectionKeydown(event, input);
        break;
      case Key.ARROW_UP:
        this.handleVerticalNavigation(event, node, -1);
        break;
      case Key.ARROW_DOWN:
        this.handleVerticalNavigation(event, node, 1);
        break;
      case Key.ARROW_LEFT:
        this.handleArrowLeft(event, node, toggle, input);
        break;
      case Key.ARROW_RIGHT:
        this.handleArrowRight(event, target, node, toggle, input);
        break;
    }
  }

  private handleSelectionKeydown(event: KeyboardEvent, input: HTMLInputElement): void {
    event.preventDefault();
    if (input) {
      input.checked = input.type === "radio" || !input.checked;
      input.dispatchEvent(new Event("change", {bubbles: true}));
    }
  }

  private handleVerticalNavigation(event: KeyboardEvent, node: HTMLElement, direction: -1 | 1): void {
    event.preventDefault();
    event.stopPropagation();

    const all = this.visibleNodes;
    const index = all.indexOf(node as TreeNode);
    if (index === -1) {
      return;
    }

    const next = all[index + direction];
    if (next) {
      this.focusNode(next);
    }
  }

  private handleArrowLeft(event: KeyboardEvent, node: HTMLElement, toggle: HTMLElement, input: HTMLInputElement): void {
    event.preventDefault();
    const expanded = node.classList.contains("tobago-expanded");

    if (toggle && expanded) {
      toggle.click();
      if (this.hasCheckboxes && input) {
        setTimeout(() => input.focus(), 0);
      }
      return;
    }

    const parentId = node.getAttribute("parent");
    if (parentId) {
      const parent = this.querySelector(`tobago-tree-node[id="${parentId}"]`) as HTMLElement;
      this.focusNode(parent);
    }
  }

  private handleArrowRight(event: KeyboardEvent, target: HTMLElement, node: HTMLElement,
                           toggle: HTMLElement, input: HTMLInputElement): void {
    event.preventDefault();

    const expandable = node.getAttribute("expandable") === "expandable" || node.classList.contains("tobago-expandable");
    const expanded = node.classList.contains("tobago-expanded");

    if (expandable && !expanded && toggle) {
      toggle.click();
      if (this.hasCheckboxes && input) {
        setTimeout(() => input.focus(), 150);
      }
      return;
    }

    if (this.hasCheckboxes && target.classList.contains("tobago-toggle") && input) {
      input.focus();
      return;
    }

    const child = this.querySelector(`tobago-tree-node[parent="${node.id}"]`) as HTMLElement;
    if (child && (child as any).offsetParent !== null) {
      this.focusNode(child);
    }
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-tree") == null) {
    window.customElements.define("tobago-tree", Tree);
  }
});
