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

export class Tree extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
  }

  get isSheet(): boolean {
    // TODO if sheet is implemented as custom element use:
    // return this.tagName === "TOBAGO-SHEET";
    return this.classList.contains("tobago-sheet");
  }

  clearSelectedNodes(): void {
    this.hiddenInputSelected.value = "[]"; //empty set
  }

  addSelectedNode(selectedNode: number): void {
    const selectedNodes = new Set(JSON.parse(this.hiddenInputSelected.value));
    selectedNodes.add(selectedNode);
    this.hiddenInputSelected.value = JSON.stringify(Array.from(selectedNodes));
  }

  getSelectedNodes(): NodeListOf<TreeNode> {
    let queryString: string = "";
    for (const selectedNodeIndex of JSON.parse(this.hiddenInputSelected.value)) {
      if (queryString.length > 0) {
        queryString += ", ";
      }
      queryString += "tobago-tree-node[index='" + selectedNodeIndex + "']";
    }

    if(queryString.length > 0) {
      return this.querySelectorAll(queryString) as NodeListOf<TreeNode>;
    } else {
      return null;
    }
  }

  deleteSelectedNode(selectedNode: number): void {
    const selectedNodes = new Set(JSON.parse(this.hiddenInputSelected.value));
    selectedNodes.delete(selectedNode);
    this.hiddenInputSelected.value = JSON.stringify(Array.from(selectedNodes));
  }

  private get hiddenInputSelected(): HTMLInputElement {
    if (this.isSheet) {
      return this.querySelector(":scope > .tobago-sheet-selected");
    } else {
      return this.querySelector(":scope > .tobago-tree-selected");
    }
  }

  clearExpandedNodes(): void {
    this.hiddenInputExpanded.value = "[]"; //empty set
  }

  addExpandedNode(expandedNode: number): void {
    const expandedNodes = new Set(JSON.parse(this.hiddenInputExpanded.value));
    expandedNodes.add(expandedNode);
    this.hiddenInputExpanded.value = JSON.stringify(Array.from(expandedNodes));
  }

  deleteExpandedNode(expandedNode: number): void {
    const expandedNodes = new Set(JSON.parse(this.hiddenInputExpanded.value));
    expandedNodes.delete(expandedNode);
    this.hiddenInputExpanded.value = JSON.stringify(Array.from(expandedNodes));
  }

  get expandedNodes(): Set<number> {
    return new Set(JSON.parse(this.hiddenInputExpanded.value));
  }

  private get hiddenInputExpanded(): HTMLInputElement {
    if (this.isSheet) {
      return this.querySelector(":scope > .tobago-sheet-expanded");
    } else {
      return this.querySelector(":scope > .tobago-tree-expanded");
    }
  }

  get selectable(): Selectable {
    return Selectable[this.getAttribute("selectable")];
  }
}

export class TreeNode extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    if (this.isExpandable() && this.toggles !== null) {
      this.toggles.forEach(element => element.addEventListener("click", this.toggleNode.bind(this)));
    }
  }

  get tree(): Tree {
    return this.closest("tobago-tree") as Tree; //TODO how to detect tobago-sheet?
  }

  isExpandable(): boolean {
    return this.getAttribute("expandable") === "expandable";
  }

  isExpanded(): boolean {
    for (const expandedNodeIndex of this.tree.expandedNodes) {
      if (expandedNodeIndex === this.index) {
        return true;
      }
    }
    return false;
  }

  get treeChildNodes(): NodeListOf<TreeNode> {
    if (this.tree.isSheet) {
      return this.closest("tbody").querySelectorAll("tobago-tree-node[parent='" + this.id + "']");
    } else {
      return this.parentElement.querySelectorAll("tobago-tree-node[parent='" + this.id + "']");
    }
  }

  get toggles(): NodeListOf<HTMLSpanElement> {
    return this.querySelectorAll(".tobago-treeNode-toggle");
  }

  get icons(): NodeListOf<HTMLElement> {
    return this.querySelectorAll(".tobago-treeNode-toggle i");
  }

  get images(): NodeListOf<HTMLImageElement> {
    return this.querySelectorAll(".tobago-treeNode-toggle img");
  }

  get index(): number {
    return Number(this.getAttribute("index"));
  }

  toggleNode(event: MouseEvent): void {
    if (this.isExpanded()) {
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

      this.tree.deleteExpandedNode(this.index);
      this.classList.remove("tobago-treeNode-markup-expanded");

      this.hideNodes(this.treeChildNodes);
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

      this.tree.addExpandedNode(this.index);
      this.classList.add("tobago-treeNode-markup-expanded");

      if (this.treeChildNodes.length === 0) {
        jsf.ajax.request(
            this.id,
            event,
            {
              execute: this.tree.id,
              render: this.tree.id
            });
      } else {
        this.showNodes(this.treeChildNodes);
      }
    }
  }

  hideNodes(treeChildNodes: NodeListOf<TreeNode>): void {
    for (const treeChildNode of treeChildNodes) {

      if (treeChildNode.tree.isSheet) {
        treeChildNode.closest("tobago-sheet-row").classList.add("d-none");
      } else {
        treeChildNode.classList.add("d-none");
      }

      this.hideNodes(treeChildNode.treeChildNodes);
    }
  }

  showNodes(treeChildNodes: NodeListOf<TreeNode>): void {
    for (const treeChildNode of treeChildNodes) {

      if (treeChildNode.tree.isSheet) {
        treeChildNode.closest("tobago-sheet-row").classList.remove("d-none");
      } else {
        treeChildNode.classList.remove("d-none");
      }

      this.showNodes(treeChildNode.treeChildNodes);
    }
  }
}

export class TreeSelect extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    this.input.addEventListener("change", this.select.bind(this));
  }

  get tree(): Tree {
    return this.closest("tobago-tree") as Tree;
  }

  get treeNode(): TreeNode {
    return this.closest("tobago-tree-node") as TreeNode;
  }

  get treeSelectChildren(): NodeListOf<TreeSelect> {
    let treeNodeId: string = this.treeNode.id;
    if (this.tree.isSheet) {
      return this.closest("tbody")
          .querySelectorAll("tobago-tree-node[parent='" + treeNodeId + "'] tobago-tree-select");
    } else {
      let treeNode = this.closest("tobago-tree-node");
      return treeNode.parentElement
          .querySelectorAll("tobago-tree-node[parent='" + treeNode.id + "'] tobago-tree-select");
    }
  }

  get input(): HTMLInputElement {
    return this.querySelector("input");
  }

  select(event: Event): void {
    switch (this.input.type) {
      case "radio":
        this.tree.clearSelectedNodes();
        this.tree.addSelectedNode(this.treeNode.index);
        break;
      case "checkbox":
        if (this.input.checked) {
          this.tree.addSelectedNode(this.treeNode.index);
        } else {
          this.tree.deleteSelectedNode(this.treeNode.index);
        }

        if (this.tree.selectable === Selectable.multiCascade) {
          let treeNodeIds = [];
          this.selectChildren(this.treeSelectChildren, this.input.checked, treeNodeIds);

          /*if (treeNodeIds.length > 0) {
            for (const id of treeNodeIds) {
              let ts: TreeSelect = document.getElementById(id).querySelector("tobago-tree-select") as TreeSelect;
              ts.input.dispatchEvent(new Event("change", {bubbles: false}));
            }
          }*/
        }
        break;
    }
  }

  selectChildren(treeSelectChildren: NodeListOf<TreeSelect>, checked: boolean, treeNodeIds: Array<string>): void {
    for (const treeSelect of treeSelectChildren) {
      if (treeSelect.input.checked !== checked) {
        treeSelect.input.checked = checked;
        treeNodeIds.push(treeSelect.treeNode.id);
      }
      if (checked) {
        this.tree.addSelectedNode(treeSelect.treeNode.index);
      } else {
        this.tree.deleteSelectedNode(treeSelect.treeNode.index);
      }

      this.selectChildren(treeSelect.treeSelectChildren, checked, treeNodeIds);
    }
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  window.customElements.define("tobago-tree-select", TreeSelect);
  window.customElements.define("tobago-tree-node", TreeNode);
  window.customElements.define("tobago-tree", Tree);
});
