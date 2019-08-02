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

import {Listener, Phase} from "./tobago-listener";
import {DomUtils} from "./tobago-utils";

class Tree {

  static toggleNode = function (event: MouseEvent) {
    const element = event.currentTarget as HTMLElement;
    const node: HTMLDivElement = element.closest(".tobago-treeNode") as HTMLDivElement;
    const data = node.closest(".tobago-tree, .tobago-sheet") as HTMLElement;
    const expanded = data.querySelector(
        ":scope > .tobago-tree-expanded, :scope > .tobago-sheet-expanded") as HTMLInputElement;
    const togglesIcon = node.querySelectorAll(".tobago-treeNode-toggle i") as NodeListOf<HTMLElement>;
    const togglesImage = node.querySelectorAll(".tobago-treeNode-toggle img") as NodeListOf<HTMLImageElement>;
    const rowIndex = Tree.rowIndex(node);
    if (Tree.isExpanded(node, expanded)) {
      Tree.hideChildren(node);
      for (const icon of togglesIcon) {
        icon.classList.remove(icon.dataset["tobagoOpen"]);
        icon.classList.add(icon.dataset["tobagoClosed"]);
      }
      for (const image of togglesImage) {
        let src = image.dataset["tobagoClosed"];
        if (!src) { // use the open icon if there is no closed icon
          src = image.dataset["tobagoOpen"];
        }
        image.setAttribute("src", src);
      }
      const set = Tree.getSet(expanded);
      set.delete(rowIndex);
      Tree.setSet(expanded, set);
      node.classList.remove("tobago-treeNode-markup-expanded");
    } else {
      const reload = Tree.showChildren(node, expanded);
      Tree.setSet(expanded, Tree.getSet(expanded).add(rowIndex));
      if (reload) {
        jsf.ajax.request(
            node.id,
            event,
            {
              //"javax.faces.behavior.event": "click",
              execute: data.id,
              render: data.id
            });
      } else {
        for (const icon of togglesIcon) {
          icon.classList.remove(icon.dataset["tobagoClosed"]);
          icon.classList.add(icon.dataset["tobagoOpen"]);
        }
        for (const image of togglesImage) {
          let src = image.dataset["tobagoOpen"];
          if (!src) { // use the open icon if there is no closed icon
            src = image.dataset["tobagoClosed"];
          }
          image.setAttribute("src", src);
        }
        node.classList.add("tobago-treeNode-markup-expanded");
      }
    }
  };

  /**
   * Hide all children of the node recursively.
   * @param node A HTMLElement as a node of the tree.
   */
  static hideChildren = function (node: HTMLElement): void {
    for (const child of Tree.findTreeChildren(node)) {
      if (Tree.isInSheet(node)) {
        child.parentElement.parentElement.classList.add("d-none");
      } else {
        child.classList.add("d-none");
      }
      Tree.hideChildren(child);
    }
  };

  /**
   * Show the children of the node recursively, there parents are expanded.
   * @param node A HTMLElement as a node of the tree.
   * @param expanded The hidden field which contains the expanded state.
   * @return is reload needed (to get all nodes from the server)
   */
  static showChildren = function (node: HTMLElement, expanded: HTMLInputElement): boolean {
    const children = Tree.findTreeChildren(node);
    if (children.length === 0) {
      return true;
    }
    for (const child of children) {
      if (Tree.isInSheet(node)) {
        child.parentElement.parentElement.classList.remove("d-none");
      } else {
        child.classList.remove("d-none");
      }
      if (Tree.isExpanded(child, expanded)) {
        const reload = Tree.showChildren(child, expanded);
        if (reload) {
          return true;
        }
      }
    }
    return false;
  };

  static commandFocus = function (event: FocusEvent) {
    const command = event.currentTarget as HTMLElement;
    const node = command.parentElement;
    const tree = node.closest(".tobago-tree");
    const selected = tree.querySelector(".tobago-tree-selected") as HTMLInputElement;
    selected.value = String(Tree.rowIndex(node));
    for (const otherNode of tree.querySelectorAll(".tobago-treeNode-markup-selected")) {
      if (otherNode !== node) {
        otherNode.classList.remove("tobago-treeNode-markup-selected");
      }
    }
    node.classList.add("tobago-treeNode-markup-selected");
  };

  static init = function (element: HTMLElement) {

    for (const toggle of DomUtils.selfOrQuerySelectorAll(element, ".tobago-treeNode-markup-folder .tobago-treeNode-toggle")) {
      toggle.addEventListener("click", Tree.toggleNode);
    }

    // selected for treeNode
    for (const command of DomUtils.selfOrQuerySelectorAll(element, ".tobago-treeCommand")) {
      command.addEventListener("focus", Tree.commandFocus);
    }

    for (const e of DomUtils.selfOrQuerySelectorAll(element, ".tobago-sheet, .tobago-tree")) {
      const sheetOrTree: HTMLDivElement = e as HTMLDivElement;

      // init selected field
      const hiddenInputSelected: HTMLInputElement = sheetOrTree.querySelector(":scope > .tobago-sheet-selected, :scope > .tobago-tree-selected");
      if (hiddenInputSelected) {
        const value = new Set<number>();
        for (const selected of sheetOrTree.querySelectorAll(".tobago-treeNode-markup-selected")) {
          value.add(Tree.rowIndex(selected));
        }
        Tree.setSet(hiddenInputSelected, value);
      }

      // selected for treeSelect
      for (const select of sheetOrTree.querySelectorAll(".tobago-treeSelect > input") as NodeListOf<HTMLInputElement>) {
        let value: Set<number>;
        // todo may use an class attribute for this value
        if (select.type === "radio") {
          value = new Set();
          value.add(Tree.rowIndex(select));
        } else if (select.checked) {
          value = Tree.getSet(hiddenInputSelected);
          value.add(Tree.rowIndex(select));
        } else {
          value = Tree.getSet(hiddenInputSelected);
          value.delete(Tree.rowIndex(select));
        }
        Tree.setSet(hiddenInputSelected, value);
      }

      // init expanded field
      const hiddenInputExpanded: HTMLInputElement = sheetOrTree.querySelector(":scope > .tobago-sheet-expanded, :scope > .tobago-tree-expanded");
      if (hiddenInputExpanded) {
        const value = new Set<number>();
        for (const expanded of sheetOrTree.querySelectorAll(".tobago-treeNode-markup-expanded")) {
          value.add(Tree.rowIndex(expanded));
        }
        Tree.setSet(hiddenInputExpanded, value);
      }

      // init tree selection for multiCascade
      if (sheetOrTree.dataset.tobagoSelectable === "multiCascade") {
        for (const treeNode of sheetOrTree.querySelectorAll(".tobago-treeNode") as NodeListOf<HTMLDivElement>) {

          const checkbox: HTMLInputElement = treeNode.querySelector(".tobago-treeSelect input[type=checkbox]");
          checkbox.addEventListener("change", function (event: Event) {
            for (const childTreeNode of Tree.findTreeChildren(treeNode)) {
              const childCheckbox: HTMLInputElement = childTreeNode.querySelector(".tobago-treeSelect input[type=checkbox]");
              childCheckbox.checked = checkbox.checked;

              const event = document.createEvent('HTMLEvents');
              event.initEvent('change', true, false);
              childCheckbox.dispatchEvent(event);
            }
          });
        }
      }
    }
  };

  static getSet(element: HTMLInputElement): Set<number> {
    return new Set(JSON.parse(element.value));
  }

  static setSet(element, set: Set<number>) {
    return element.value = JSON.stringify(Array.from(set));
  }

  static isExpanded = function (node: Element, expanded: HTMLInputElement) {
    const rowIndex = Tree.rowIndex(node);
    return Tree.getSet(expanded).has(rowIndex);
  };

  static rowIndex = function (node: Element): number { // todo: use attribute data-tobago-row-index
    return parseInt(node.id.replace(/.+\:(\d+)(\:\w+)+/, '$1'));
  };

  static findTreeChildren = function (treeNode: HTMLElement): NodeListOf<HTMLDivElement> {
    if (Tree.isInSheet(treeNode)) {
      return treeNode.closest("tbody")
          .querySelectorAll(".tobago-sheet-row[data-tobago-tree-parent='" + treeNode.id + "'] .tobago-treeNode");
    } else {
      return treeNode.parentElement.querySelectorAll(".tobago-treeNode[data-tobago-tree-parent='" + treeNode.id + "']");
    }
  };

  static isInSheet = function (node: Element) {
    return node.parentElement.tagName === "TD";
  };
}

Listener.register(Tree.init, Phase.DOCUMENT_READY);
Listener.register(Tree.init, Phase.AFTER_UPDATE);
