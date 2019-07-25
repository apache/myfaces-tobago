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
import {DomUtils, Tobago4Utils} from "./tobago-utils";

class Tree {

  static toggleNode = function (event: MouseEvent) {
    const element = event.currentTarget as HTMLElement;
    const node = element.closest(".tobago-treeNode");
    const data = node.closest(".tobago-tree, .tobago-sheet") as HTMLElement;
    const expanded = DomUtils.childrenQuerySelector(data,
        ".tobago-tree-expanded, .tobago-sheet-expanded") as HTMLInputElement;
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
   * @param node A jQuery-Object as a node of the tree.
   */
  static hideChildren = function (node: Element) {
    const children = Tree.findChildren(node);
    children.each(function () {
      const child = this;
      if (Tree.isInSheet(node)) {
        child.parentNode.parentNode.classList.add("d-none");
      } else {
        child.classList.add("d-none");
      }
      Tree.hideChildren(child);
    });
  };

  /**
   * Show the children of the node recursively, there parents are expanded.
   * @param node A jQuery-Object as a node of the tree.
   * @param expanded The hidden field which contains the expanded state.
   * @return is reload needed (to get all nodes from the server)
   */
  static showChildren = function (node: Element, expanded: HTMLInputElement) {
    const children = Tree.findChildren(node);
    if (children.length == 0) {
      // no children in DOM, reload it from the server
      return true;
    }
    children.each(function () {
      const child = this;
      if (Tree.isInSheet(node)) {
        this.parentNode.parentNode.classList.remove("d-none");
      } else {
        this.classList.remove("d-none");
      }
      if (Tree.isExpanded(child, expanded)) {
        const reload = Tree.showChildren(child, expanded);
        if (reload) {
          return true;
        }
      }
    });
    return false;
  };

  static commandFocus = function (event: FocusEvent) {
    const command = event.currentTarget as HTMLElement;
    const node = command.parentElement;
    const tree = node.closest(".tobago-tree");
    const selected = DomUtils.childrenQuerySelector(tree, ".tobago-tree-selected") as HTMLInputElement;
    selected.value = String(Tree.rowIndex(node));
    for (const otherNode of tree.querySelectorAll(".tobago-treeNode-markup-selected")) {
      if (otherNode !== node) {
        otherNode.classList.remove("tobago-treeNode-markup-selected");
      }
    }
    node.classList.add("tobago-treeNode-markup-selected");
    console.info("-----------------> focus ---> " + node.id); // remove
  };

  static init = function (element: HTMLElement) {

    for (const toggle of DomUtils.selfOrQuerySelectorAll(element, ".tobago-treeNode-markup-folder .tobago-treeNode-toggle")) {
      toggle.addEventListener("click", Tree.toggleNode);
    }

    // selected for treeNode
    for (const command of DomUtils.selfOrQuerySelectorAll(element, ".tobago-treeCommand")) {
      command.addEventListener("focus", Tree.commandFocus);
    }

    var elements = jQuery(elements); // fixme remove!

    // selected for treeSelect
    Tobago4Utils.selectWithJQuery(elements, ".tobago-treeSelect > input").change(function () {
      var select = jQuery(this);
      var selected = select.prop("checked");
      var data = select.closest(".tobago-tree, .tobago-sheet");
      const hidden: HTMLInputElement
          = data.children(".tobago-tree-selected, .tobago-sheet-selected").get(0);
      let value: Set<number>;
      // todo may use an class attribute for this value
      if (select.attr("type") === "radio") {
        value = new Set();
        value.add(Tree.rowIndex(select.get(0)));
      } else if (selected) {
        value = Tree.getSet(hidden);
        value.add(Tree.rowIndex(select.get(0)));
      } else {
        value = Tree.getSet(hidden);
        value.delete(Tree.rowIndex(select.get(0)));
      }
      Tree.setSet(hidden, value);
    });

    // init selected field
    Tobago4Utils.selectWithJQuery(elements, ".tobago-tree, .tobago-sheet").each(function () {
      const data: HTMLElement = this;
      const hidden = DomUtils.childrenQuerySelector(data, ".tobago-tree-selected, .tobago-sheet-selected");
      if (hidden) {
        const value = new Set<number>();
        for (const selected of data.querySelectorAll(".tobago-treeNode-markup-selected")) {
          value.add(Tree.rowIndex(selected));
        }
        Tree.setSet(hidden, value);
      }
    });

    // init expanded field
    Tobago4Utils.selectWithJQuery(elements, ".tobago-tree, .tobago-sheet").each(function () {
      const hidden: HTMLInputElement = jQuery(this).children(".tobago-tree-expanded, .tobago-sheet-expanded").get(0);
      if (hidden) {
        const value = new Set<number>();
        jQuery(this).find(".tobago-treeNode-markup-expanded").each(function () {
          value.add(Tree.rowIndex(this));
        });
        Tree.setSet(hidden, value);
      }
    });

    // init tree selection for multiCascade
    Tobago4Utils.selectWithJQuery(elements, ".tobago-tree[data-tobago-selectable=multiCascade]").each(function () {
      var tree = jQuery(this);
      tree.find("input[type=checkbox]").each(function () {
        jQuery(this).change(function (event) {
          var node = jQuery(event.target).parents(".tobago-treeNode");
          var checked = node.find("input[type=checkbox]").prop("checked");
          var children = Tree.findChildren(node.get(0));
          children.each(function () {
            var childsCheckbox = jQuery(this).find("input[type=checkbox]");
            childsCheckbox.prop("checked", checked);
            childsCheckbox.change();
          });
        });
      });
    });

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

  static findChildren = function (node: Element) {
    const treeParentSelector = "[data-tobago-tree-parent='" + node.id + "']";
    let children;
    if (Tree.isInSheet(node)) {
      children = jQuery(node).parent("td").parent("tr").nextAll().children().children(treeParentSelector);
    } else { // normal tree
      children = jQuery(node).nextAll(treeParentSelector);
    }
    return children;
  };

  static isInSheet = function (node: Element) {
    return node.parentElement.tagName === "TD";
  };
}

Listener.register(Tree.init, Phase.DOCUMENT_READY);
Listener.register(Tree.init, Phase.AFTER_UPDATE);
