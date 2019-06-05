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
    var $expanded = $data.children(".tobago-treeMenu-expanded, .tobago-tree-expanded, .tobago-sheet-expanded");
    var $toggles = $node.find(".tobago-treeMenuNode-toggle, .tobago-treeNode-toggle");
    var rowIndex = Tree.rowIndex($node);
    if (Tree.isExpanded($node, $expanded)) {
      Tree.hideChildren($node);
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
      $expanded.val($expanded.val().replace(new RegExp("," + rowIndex + ","), ","));
      $node.filter(".tobago-treeNode").removeClass("tobago-treeNode-markup-expanded");
      $node.filter(".tobago-treeMenuNode").removeClass("tobago-treeMenuNode-markup-expanded");
    } else {
      var reload = Tree.showChildren($node, $expanded);
      $expanded.val($expanded.val() + rowIndex + ",");
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
  static hideChildren = function (node) {
    var inSheet = Tree.isInSheet(node);
    var children = Tree.findChildren(node);
    children.each(function () {
      if (inSheet) {
        jQuery(this).parent().parent().hide();
      } else {
        jQuery(this).hide();
      }
      Tree.hideChildren(jQuery(this));
    });
  };

  /**
   * Show the children of the node recursively, there parents are expanded.
   * @param node A jQuery-Object as a node of the tree.
   * @return is reload needed (to get all nodes from the server)
   */
  static showChildren = function (node, expanded) {
    var inSheet = Tree.isInSheet(node);
    var children = Tree.findChildren(node);
    if (children.length == 0) {
      // no children in DOM, reload it from the server
      return true;
    }
    children.each(function () {
      var child = jQuery(this);
      if (inSheet) {
        child.parent().parent().show();
      } else {
        child.show();
      }
      if (Tree.isExpanded(child, expanded)) {
        var reload = Tree.showChildren(child, expanded);
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
      var node = command.parent(".tobago-treeNode");
      var tree = node.closest(".tobago-tree");
      var selected = tree.children(".tobago-tree-selected");
      selected.val(Tree.rowIndex(node));
      tree.find(".tobago-treeNode").removeClass("tobago-treeNode-markup-selected");
      node.addClass("tobago-treeNode-markup-selected");
    });

    // selected for treeSelect
    Tobago4Utils.selectWithJQuery(elements, ".tobago-treeSelect > input").change(function () {
      var select = jQuery(this);
      var selected = select.prop("checked");
      var data = select.closest(".tobago-treeMenu, .tobago-tree, .tobago-sheet");
      var hidden = data.children(".tobago-treeMenu-selected, .tobago-tree-selected, .tobago-sheet-selected");
      var newValue;
      if (select.attr("type") === "radio") {
        newValue = "," + Tree.rowIndex(select) + ",";
      } else if (selected) {
        newValue = hidden.val() + Tree.rowIndex(select) + ",";
      } else {
        newValue = (hidden.val() as string).replace(new RegExp("," + Tree.rowIndex(select) + ","), ",");
      }
      // XXX optimize: regexp and Tobago.Tree.rowIndex
      hidden.val(newValue);
    });

    // selected for treeMenuNode
    Tobago4Utils.selectWithJQuery(elements, ".tobago-treeMenuCommand").focus(function () {
      var command = jQuery(this);
      var node = command.parent(".tobago-treeMenuNode");
      var tree = node.closest(".tobago-treeMenu");
      var selected = tree.children(".tobago-treeMenu-selected");
      selected.val(Tree.rowIndex(node));
      tree.find(".tobago-treeMenuNode").removeClass("tobago-treeMenuNode-markup-selected");
      node.addClass("tobago-treeMenuNode-markup-selected");
    });

    // init selected field
    Tobago4Utils.selectWithJQuery(elements, ".tobago-treeMenu, .tobago-tree, .tobago-sheet").each(function () {
      var hidden = jQuery(this).children(".tobago-treeMenu-selected, .tobago-tree-selected, .tobago-sheet-selected");
      var string = ",";
      jQuery(this).find(".tobago-treeMenuNode-markup-selected, .tobago-treeNode-markup-selected").each(function () {
        string += Tree.rowIndex(jQuery(this)) + ",";
      });
      hidden.val(string);
    });

    // init expanded field
    Tobago4Utils.selectWithJQuery(elements, ".tobago-treeMenu, .tobago-tree, .tobago-sheet").each(function () {
      var hidden = jQuery(this).children(".tobago-treeMenu-expanded, .tobago-tree-expanded, .tobago-sheet-expanded");
      var string = ",";
      jQuery(this).find(".tobago-treeMenuNode-markup-expanded, .tobago-treeNode-markup-expanded").each(function () {
        string += Tree.rowIndex(jQuery(this)) + ",";
      });
      hidden.val(string);
    });

    // init tree selection for multiCascade
    Tobago4Utils.selectWithJQuery(elements, ".tobago-tree[data-tobago-selectable=multiCascade]").each(function () {
      var tree = jQuery(this);
      tree.find("input[type=checkbox]").each(function () {
        jQuery(this).change(function (event) {
          var node = jQuery(event.target).parents(".tobago-treeNode");
          var checked = node.find("input[type=checkbox]").prop("checked");
          var children = Tree.findChildren(node);
          children.each(function () {
            var childsCheckbox = jQuery(this).find("input[type=checkbox]");
            childsCheckbox.prop("checked", checked);
            childsCheckbox.change();
          });
        });
      });
    });

  };

  static isExpanded = function (node, expanded) {
    var rowIndex = Tree.rowIndex(node);
    return expanded.val().indexOf("," + rowIndex + ",") > -1;
  };

  static rowIndex = function (node) {
    return node.attr("id").replace(/.+\:(\d+)(\:\w+)+/, '$1');
  };

  static findChildren = function (node) {
    var treeParentSelector = "[data-tobago-tree-parent='" + node.attr("id") + "']";
    var children;
    if (Tree.isInSheet(node)) {
      children = node.parent("td").parent("tr").nextAll().children().children(treeParentSelector);
    } else { // normal tree
      children = node.nextAll(treeParentSelector);
    }
    return children;
  };

  static isInSheet = function (node) {
    return node.parent("td").length === 1;
  };
}

Listener.register(Tree.init, Phase.DOCUMENT_READY);
Listener.register(Tree.init, Phase.AFTER_UPDATE);

class TreeListbox {
  static init = function (elements) {
    elements = elements.jQuery ? elements : jQuery(elements); // fixme jQuery -> ES5
    var treeListbox = Tobago4Utils.selectWithJQuery(elements, ".tobago-treeListbox");
    // hide select tags for level > root
    treeListbox.children().find("select:not(:first)").hide();

    var listboxSelects = treeListbox.find("select");

    listboxSelects.children("option").each(TreeListbox.initNextLevel);
    listboxSelects.each(TreeListbox.initListeners);
  };

// find all option tags and add the dedicated select tag in its data section.
  static initNextLevel = function () {
    var option = jQuery(this);
    var select = option.closest(".tobago-treeListbox-level").next()
        .find("[data-tobago-tree-parent='" + option.attr("id") + "']");
    if (select.length == 1) {
      option.data("tobago-select", select);
    } else {
      var empty = option.closest(".tobago-treeListbox-level").next().children(":first");
      option.data("tobago-select", empty);
    }
  };

// add on change on all select tag, all options that are not selected hide there dedicated
// select tag, and the selected option show its dedicated select tag.
  static initListeners = function () {

    jQuery(this).change(TreeListbox.onChange);

    jQuery(this).focus(function () {
      jQuery(this).change();
    });
  };

  static onChange = function () {
    var listbox = jQuery(this);
    listbox.children("option:not(:selected)").each(function () {
      jQuery(this).data("tobago-select").hide();
    });
    listbox.children("option:selected").each(function () {
      jQuery(this).data("tobago-select").show();
    });
    TreeListbox.setSelected(listbox);

    // Deeper level (2nd and later) should only show the empty select tag.
    // The first child is the empty selection.
    listbox.parent().nextAll(":not(:first)").each(function () {
      jQuery(this).children(":not(:first)").hide();
      jQuery(this).children(":first").show();
    });

  };

  static setSelected = function (listbox) {
    var hidden = listbox.closest(".tobago-treeListbox").children("[data-tobago-selection-mode]");
    if (hidden.length == 1) {
      var selectedValue = ";";
      listbox.children("option:selected").each(function () {
        selectedValue += jQuery(this).attr("id") + ";";
      });
      hidden.val(selectedValue);
    }
  };
}

Listener.register(TreeListbox.init, Phase.DOCUMENT_READY);
Listener.register(TreeListbox.init, Phase.AFTER_UPDATE);
