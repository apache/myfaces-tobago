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
import {TreeNode} from "./tobago-tree-node";

export class Tree extends HTMLElement {

  /** ID des zuletzt fokussierten Tree-Nodes (wird über AJAX-Updates hinweg gespeichert) */
  private static focusedNodeId: string | null = null;
  /** CSS-Selektor oder Tag des zuletzt fokussierten Unter-Elements (z. B. ".tobago-toggle" oder "input") */
  private static focusedSubElementClass: string | null = null;

  constructor() {
    super();

    this.handleKeydown = this.handleKeydown.bind(this);
    this.addEventListener("focusout", (event) => {
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
    return Array.from(this.nodes).filter(node => (node as HTMLElement).offsetParent !== null) as TreeNode[];
  }

  private get hasCheckboxes(): boolean {
    return this.querySelector("input[type=checkbox], input[type=radio]") !== null;
  }

  connectedCallback(): void {
    for (const node of Array.from(this.nodes)) {
      (node as HTMLElement).setAttribute("tabindex", "-1");

      const subElements = node.querySelectorAll(".tobago-toggle, input[type=checkbox], input[type=radio]");
      subElements.forEach(el => el.setAttribute("tabindex", "0"));
    }

    if (Tree.focusedNodeId) {
      const savedNode = this.querySelector(`tobago-tree-node[id="${Tree.focusedNodeId}"]`) as HTMLElement;
      if (savedNode) {
        const targetSelector = Tree.focusedSubElementClass || ".tobago-toggle, input";
        const subEl = savedNode.querySelector(targetSelector) as HTMLElement;
        if (subEl) {
          subEl.focus();
        }
        this.addEventListener("keydown", this.handleKeydown);
        return;
      }
    }

    this.addEventListener("keydown", this.handleKeydown);
  }

  disconnectedCallback(): void {
    this.removeEventListener("keydown", this.handleKeydown);
  }

  focusNode(node: TreeNode | HTMLElement, preferCheckbox: boolean = false): void {
    if (!node) {
      return;
    }

    const toggle = node.querySelector(".tobago-toggle") as HTMLElement;
    const input = node.querySelector("input[type=checkbox], input[type=radio]") as HTMLInputElement;

    if (!this.hasCheckboxes && toggle) {
      toggle.focus();
      return;
    }

    if (input && (preferCheckbox || !toggle)) {
      input.focus();
    } else if (toggle) {
      toggle.focus();
    } else if (input) {
      input.focus();
    }
  }

  private handleKeydown(event: KeyboardEvent): void {
    const key = event.key || (event as any).key || "";
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
    const isInputTarget = target.tagName === "INPUT";

    if (["Space", "Enter"].includes(key)) {
      if (this.hasAttribute("data-debug")) {
        console.debug("tobago-tree keydown", key, "on node", node.id);
      }
      event.preventDefault();
      if (input) {
        input.checked = input.type === "radio" ? true : !input.checked;
        input.dispatchEvent(new Event("change", {bubbles: true}));
      }
      return;
    }

    const isUp = key === "ArrowUp";
    const isDown = key === "ArrowDown";
    const isLeft = key === "ArrowLeft";
    const isRight = key === "ArrowRight";

    if (isUp || isDown) {
      event.preventDefault();
      event.stopPropagation();

      const all = this.visibleNodes;
      const idx = all.indexOf(node as any);
      if (idx === -1) {
        return;
      }

      const next = isUp ? all[idx - 1] : all[idx + 1];
      if (next) {
        const hasNextToggle = next.querySelector(".tobago-toggle") !== null;
        const preferCheckbox = this.hasCheckboxes && (!target.classList.contains("tobago-toggle") || !hasNextToggle);
        this.focusNode(next, preferCheckbox);
      }
      return;
    }

    if (isLeft) {
      event.preventDefault();
      const expanded = node.classList.contains("tobago-expanded");

      if (toggle) {
        if (expanded) {
          toggle.click();
          return;
        }
        //if (this.hasCheckboxes && isInputTarget) {
        //  toggle.focus();
        //  return;
        //}
      }

      const parentId = node.getAttribute("parent");
      if (parentId) {
        const parent = this.querySelector(`tobago-tree-node[id="${parentId}"]`) as HTMLElement;
        this.focusNode(parent, false);
      }
      return;
    }

    if (isRight) {
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
        this.focusNode(child, this.hasCheckboxes);
      }
      return;
    }
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-tree") == null) {
    window.customElements.define("tobago-tree", Tree);
  }
});
