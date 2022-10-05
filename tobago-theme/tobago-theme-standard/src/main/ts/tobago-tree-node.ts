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

import {Sheet} from "./tobago-sheet";
import {Tree} from "./tobago-tree";

export class TreeNode extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    if (this.expandable && this.toggles !== null) {
      this.toggles.forEach(element => element.addEventListener("click", this.toggleNode.bind(this)));
    }
  }

  private toggleNode(event: MouseEvent): void {
    if (this.expanded) {
      for (const icon of this.icons) {
        icon.classList.remove(icon.dataset.tobagoOpen);
        icon.classList.add(icon.dataset.tobagoClosed);
      }
      for (const image of this.images) {
        if (image.dataset.tobagoClosed) {
          image.src = image.dataset.tobagoClosed;
        } else {
          image.src = image.dataset.tobagoOpen;
        }
      }

      this.deleteExpandedNode(this.index);
      this.classList.remove("tobago-expanded");

      this.hideNodes(this.treeChildNodes);
      if (this.tree) {
        this.ajax(event, false);
      }
    } else {
      for (const icon of this.icons) {
        icon.classList.remove(icon.dataset.tobagoClosed);
        icon.classList.add(icon.dataset.tobagoOpen);
      }
      for (const image of this.images) {
        if (image.dataset.tobagoOpen) {
          image.src = image.dataset.tobagoOpen;
        } else {
          image.src = image.dataset.tobagoClosed;
        }
      }

      this.addExpandedNode(this.index);
      this.classList.add("tobago-expanded");

      this.showNodes(this.treeChildNodes);
      if (this.tree) {
        this.ajax(event, this.treeChildNodes.length === 0);
      }
    }
  }

  private ajax(event: Event, renderTree: boolean): void {
    jsf.ajax.request(
        this.id,
        event,
        {
          "jakarta.faces.behavior.event": "change",
          execute: this.tree.id,
          render: renderTree ? this.tree.id : null
        });
  }

  private hideNodes(treeChildNodes: NodeListOf<TreeNode>): void {
    for (const treeChildNode of treeChildNodes) {

      if (treeChildNode.sheet) {
        treeChildNode.closest("tr").classList.add("d-none");
      } else {
        treeChildNode.classList.add("d-none");
      }

      this.hideNodes(treeChildNode.treeChildNodes);
    }
  }

  private showNodes(treeChildNodes: NodeListOf<TreeNode>): void {
    for (const treeChildNode of treeChildNodes) {

      if (treeChildNode.sheet) {
        treeChildNode.closest("tr").classList.remove("d-none");
      } else {
        treeChildNode.classList.remove("d-none");
      }

      this.showNodes(treeChildNode.treeChildNodes);
    }
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

  get tree(): Tree {
    return this.closest("tobago-tree");
  }

  get sheet(): Sheet {
    return this.closest("tobago-sheet");
  }

  private get expandable(): boolean {
    return this.getAttribute("expandable") === "expandable";
  }

  private get expanded(): boolean {
    for (const expandedNodeIndex of this.expandedNodes) {
      if (expandedNodeIndex === this.index) {
        return true;
      }
    }
    return false;
  }

  private get expandedNodes(): Set<number> {
    return new Set(JSON.parse(this.hiddenInputExpanded.value));
  }

  private get hiddenInputExpanded(): HTMLInputElement {
    if (this.tree) {
      return this.tree.hiddenInputExpanded;
    } else if (this.sheet) {
      return this.sheet.getHiddenExpanded();
    } else {
      console.error("Cannot detect 'tobago-tree' or 'tobago-sheet'.");
      return null;
    }
  }

  private get treeChildNodes(): NodeListOf<TreeNode> {
    if (this.sheet) {
      return this.closest("tbody").querySelectorAll(`tobago-tree-node[parent='${this.id}']`);
    } else if (this.tree) {
      return this.parentElement.querySelectorAll(`tobago-tree-node[parent='${this.id}']`);
    } else {
      console.error("Cannot detect 'tobago-tree' or 'tobago-sheet'.");
      return null;
    }
  }

  private get toggles(): NodeListOf<HTMLSpanElement> {
    return this.querySelectorAll(".tobago-toggle");
  }

  private get icons(): NodeListOf<HTMLElement> {
    return this.querySelectorAll(".tobago-toggle i");
  }

  private get images(): NodeListOf<HTMLImageElement> {
    return this.querySelectorAll(".tobago-toggle img");
  }

  get index(): number {
    return Number(this.getAttribute("index"));
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-tree-node") == null) {
    window.customElements.define("tobago-tree-node", TreeNode);
  }
});
