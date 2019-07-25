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
import {Tobago4Utils} from "./tobago-utils";

class Tree {
  static toggleNode = function ($element, event) {
    var src;
    var $node = $element.closest(".tobago-treeNode, .tobago-treeMenuNode");
    var $data = $node.closest(".tobago-treeMenu, .tobago-tree, .tobago-sheet");
    const expanded: HTMLInputElement
        = $data.children(".tobago-treeMenu-expanded, .tobago-tree-expanded, .tobago-sheet-expanded").get(0);
    var $toggles = $node.find(".tobago-treeMenuNode-toggle, .tobago-treeNode-toggle");
    var rowIndex = Tree.rowIndex($node.get(0));
    if (Tree.isExpanded($node.get(0), expanded)) {
      Tree.hideChildren($node.get(0));
      $toggles.find("i").each(function () {
        var $toggle = jQuery(this);
        $toggle.removeClass($toggle.data("tobago-open")).addClass($toggle.data("tobago-closed"));
      });
      $toggles.find("img").each(function () {
        var $toggle = jQuery(this);
        src = $toggle.data("tobago-closed");
        if (src === undefined) { // use the open icon if there is no close icon
          src = $toggle.data("tobago-open");
        }
        $toggle.attr("src", src);
      });
      const set = Tree.getSet(expanded);
      set.delete(rowIndex);
      Tree.setSet(expanded, set);
      $node.filter(".tobago-treeNode").removeClass("tobago-treeNode-markup-expanded");
      $node.filter(".tobago-treeMenuNode").removeClass("tobago-treeMenuNode-markup-expanded");
    } else {
      const reload = Tree.showChildren($node.get(0), expanded);
      Tree.setSet(expanded, Tree.getSet(expanded).add(rowIndex));
      if (reload) {
        jsf.ajax.request(
            $toggles.parent().attr("id"),
            event,
            {
              //"javax.faces.behavior.event": "click",
              execute: $data.attr("id"),
              render: $data.attr("id")
            });
      } else {
        $toggles.find("i").each(function () {
          var $toggle = jQuery(this);
          $toggle.removeClass($toggle.data("tobago-closed")).addClass($toggle.data("tobago-open"));
        });
        $toggles.find("img").each(function () {
          var $toggle = jQuery(this);
          src = $toggle.data("tobago-open");
          if (src === undefined) { // use the close icon if there is no open icon
            src = $toggle.data("tobago-closed");
          }
          $toggle.attr("src", src);
        });
        $node.filter(".tobago-treeNode").addClass("tobago-treeNode-markup-expanded");
        $node.filter(".tobago-treeMenuNode").addClass("tobago-treeMenuNode-markup-expanded");
      }
    }
  };

  /**
   * Hide all children of the node recursively.
   * @param node A jQuery-Object as a node of the tree.
   */
  static hideChildren = function (node: HTMLElement) {
    var inSheet = Tree.isInSheet(node);
    var children = Tree.findChildren(node);
    children.each(function () {
      if (inSheet) {
        jQuery(this).parent().parent().hide();
      } else {
        jQuery(this).hide();
      }
      Tree.hideChildren(this);
    });
  };

  /**
   * Show the children of the node recursively, there parents are expanded.
   * @param node A jQuery-Object as a node of the tree.
   * @param expanded The hidden field which contains the expanded state.
   * @return is reload needed (to get all nodes from the server)
   */
  static showChildren = function (node: HTMLElement, expanded: HTMLInputElement) {
    var inSheet = Tree.isInSheet(node);
    var children = Tree.findChildren(node);
    if (children.length == 0) {
      // no children in DOM, reload it from the server
      return true;
    }
    children.each(function () {
      var $child = jQuery(this);
      if (inSheet) {
        $child.parent().parent().show();
      } else {
        $child.show();
      }
      if (Tree.isExpanded($child.get(0), expanded)) {
        var reload = Tree.showChildren($child.get(0), expanded);
        if (reload) {
          return true;
        }
      }
    });
    return false;
  };

  static init = function (elements) {
    elements = elements.jQuery ? elements : jQuery(elements); // fixme jQuery -> ES5
    Tobago4Utils.selectWithJQuery(elements, ".tobago-treeNode-markup-folder .tobago-treeNode-toggle").click(function (event) {
      Tree.toggleNode(jQuery(this), event);
      return false;
    });

    Tobago4Utils.selectWithJQuery(elements, ".tobago-treeMenuNode-markup-folder .tobago-treeMenuNode-toggle")
        .parent().each(function () {
      // if there is no command, than the whole node element should be the toggle
      var toggle = jQuery(this).children(".tobago-treeMenuCommand").length === 0
          ? jQuery(this)
          : jQuery(this).find(".tobago-treeMenuNode-toggle");
      toggle.click(function (event) {
        Tree.toggleNode(jQuery(this), event);
      });
    });

    // normal hover effect (not possible with CSS in IE 6)
    Tobago4Utils.selectWithJQuery(elements, ".tobago-treeMenuNode").hover(function () {
      jQuery(this).toggleClass("tobago-treeMenuNode-markup-hover");
    });

    // selected for treeNode
    Tobago4Utils.selectWithJQuery(elements, ".tobago-treeCommand").focus(function () {
      var command = jQuery(this);
      var $node = command.parent(".tobago-treeNode");
      var tree = $node.closest(".tobago-tree");
      var selected = tree.children(".tobago-tree-selected");
      selected.val(Tree.rowIndex($node.get(0)));
      tree.find(".tobago-treeNode").removeClass("tobago-treeNode-markup-selected");
      $node.addClass("tobago-treeNode-markup-selected");
    });

    // selected for treeSelect
    Tobago4Utils.selectWithJQuery(elements, ".tobago-treeSelect > input").change(function () {
      var select = jQuery(this);
      var selected = select.prop("checked");
      var data = select.closest(".tobago-treeMenu, .tobago-tree, .tobago-sheet");
      const hidden: HTMLInputElement
          = data.children(".tobago-treeMenu-selected, .tobago-tree-selected, .tobago-sheet-selected").get(0);
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

    // selected for treeMenuNode
    Tobago4Utils.selectWithJQuery(elements, ".tobago-treeMenuCommand").focus(function () {
      var command = jQuery(this);
      var $node = command.parent(".tobago-treeMenuNode");
      var tree = $node.closest(".tobago-treeMenu");
      const selected: HTMLInputElement = tree.children(".tobago-treeMenu-selected").get(0);
      const set = new Set<number>();
      set.add(Tree.rowIndex($node.get(0)));
      Tree.setSet(selected, set);
      tree.find(".tobago-treeMenuNode").removeClass("tobago-treeMenuNode-markup-selected");
      $node.addClass("tobago-treeMenuNode-markup-selected");
    });

    // init selected field
    Tobago4Utils.selectWithJQuery(elements, ".tobago-treeMenu, .tobago-tree, .tobago-sheet").each(function () {
      const hidden: HTMLInputElement = jQuery(this).children(".tobago-treeMenu-selected, .tobago-tree-selected, .tobago-sheet-selected").get(0);
      if (hidden) {
        const value = new Set<number>();
        jQuery(this).find(".tobago-treeMenuNode-markup-selected, .tobago-treeNode-markup-selected").each(function () {
          value.add(Tree.rowIndex(this));
        });
        Tree.setSet(hidden, value);
      }
    });

    // init expanded field
    Tobago4Utils.selectWithJQuery(elements, ".tobago-treeMenu, .tobago-tree, .tobago-sheet").each(function () {
      const hidden: HTMLInputElement = jQuery(this).children(".tobago-treeMenu-expanded, .tobago-tree-expanded, .tobago-sheet-expanded").get(0);
      if (hidden) {
        const value = new Set<number>();
        jQuery(this).find(".tobago-treeMenuNode-markup-expanded, .tobago-treeNode-markup-expanded").each(function () {
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

  static isExpanded = function (node: HTMLElement, expanded: HTMLInputElement) {
    const rowIndex = Tree.rowIndex(node);
    return Tree.getSet(expanded).has(rowIndex);
  };

  static rowIndex = function (node: HTMLElement): number { // todo: use attribute data-tobago-row-index
    return parseInt(node.id.replace(/.+\:(\d+)(\:\w+)+/, '$1'));
  };

  static findChildren = function (node: HTMLElement) {
    var treeParentSelector = "[data-tobago-tree-parent='" + node.id + "']";
    var children;
    if (Tree.isInSheet(node)) {
      children = jQuery(node).parent("td").parent("tr").nextAll().children().children(treeParentSelector);
    } else { // normal tree
      children = jQuery(node).nextAll(treeParentSelector);
    }
    return children;
  };

  static isInSheet = function (node: HTMLElement) {
    return jQuery(node).parent("td").length === 1;
  };
}

Listener.register(Tree.init, Phase.DOCUMENT_READY);
Listener.register(Tree.init, Phase.AFTER_UPDATE);
