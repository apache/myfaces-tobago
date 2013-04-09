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

Tobago.Tree = {};

Tobago.Tree.toggleNode = function(element) {
  var src;
  var node = element.closest(".tobago-treeNode, .tobago-treeMenuNode");
  var data = node.closest(".tobago-treeMenu, .tobago-tree, .tobago-sheet");
  var expanded = data.children(".tobago-treeMenu-expanded, .tobago-tree-expanded, .tobago-sheet-expanded");
  var toggle = node.find(".tobago-treeMenuNode-toggle, .tobago-treeNode-toggle");
  var rowIndex = Tobago.Tree.rowIndex(node);
  if (Tobago.Tree.isExpanded(node, expanded)) {
    Tobago.Tree.hideChildren(node);
    toggle.each(function() {
      src = jQuery(this).data("tobago-srcclose");
      if (src == null) { // use the open icon if there is no close icon
        src = jQuery(this).data("tobago-srcopen");
      }
      jQuery(this).attr("src", src);
      Tobago.fixPngAlpha(this);
    });
    expanded.val(expanded.val().replace(new RegExp("," + rowIndex + ","), ","));
    node.filter(".tobago-treeNode").removeClass("tobago-treeNode-markup-expanded");
    node.filter(".tobago-treeMenuNode").removeClass("tobago-treeMenuNode-markup-expanded");
  } else {
    var reload = Tobago.Tree.showChildren(node, expanded);
    expanded.val(expanded.val() + rowIndex + ",");
    if (reload) {
      Tobago.reloadComponent(element, data.attr("id"), toggle.parent().attr("id"), {});
    } else {
      toggle.each(function() {
        src = jQuery(this).data("tobago-srcopen");
        if (src == null) { // use the close icon if there is no open icon
          src = jQuery(this).data("tobago-srcclose");
        }
        jQuery(this).attr("src", src);
        Tobago.fixPngAlpha(this);
      });
      node.filter(".tobago-treeNode").addClass("tobago-treeNode-markup-expanded");
      node.filter(".tobago-treeMenuNode").addClass("tobago-treeMenuNode-markup-expanded");
    }
  }
};

/**
 * Hide all children of the node recursively.
 * @param node A jQuery-Object as a node of the tree.
 */
Tobago.Tree.hideChildren = function (node) {
  var inSheet = Tobago.Tree.isInSheet(node);
  var children = Tobago.Tree.findChildren(node);
  children.each(function() {
    if (inSheet) {
      jQuery(this).parent().parent().hide();
    } else {
      jQuery(this).hide();
    }
    Tobago.Tree.hideChildren(jQuery(this));
  });
};

/**
 * Show the children of the node recursively, there parents are expanded.
 * @param node A jQuery-Object as a node of the tree.
 * @return is reload needed (to get all nodes from the server)
 */
Tobago.Tree.showChildren = function (node, expanded) {
  var inSheet = Tobago.Tree.isInSheet(node);
  var children = Tobago.Tree.findChildren(node);
  if (children.length == 0) {
    // no children in DOM, reload it from the server
    return true;
  }
  children.each(function() {
    var child = jQuery(this);
    if (inSheet) {
      child.parent().parent().show();
    } else {
      child.show();
    }
    if (Tobago.Tree.isExpanded(child, expanded)) {
      var reload = Tobago.Tree.showChildren(child, expanded);
      if (reload) {
        return true;
      }
    }
  });
  return false;
};

Tobago.Tree.init = function(elements) {

  var listboxSelects = Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeListbox").find("select");

  // find all option tags and add the dedicated select tag in its data section.

  listboxSelects.children("option").each(function() {
    var option = jQuery(this);
    var select = option.closest(".tobago-treeListbox-level").next()
        .find("[data-tobago-treeparent='" + option.attr("id") + "']");
    if (select.length == 1) {
      option.data("tobago-select", select);
    } else {
      var empty = option.closest(".tobago-treeListbox-level").next().children(":first");
      option.data("tobago-select", empty);
    }
  });

  // add on change on all select tag, all options that are not selected hide there dedicated
  // select tag, and the selected option show its dedicated select tag.

  listboxSelects.each(function() {

    jQuery(this).change(function() {
      jQuery(this).children("option:not(:selected)").each(function() {
        jQuery(this).data("tobago-select").hide();
      });
      jQuery(this).children("option:selected").data("tobago-select").show();

      // Deeper level (2nd and later) should only show the empty select tag.
      // The first child is the empty selection.
      jQuery(this).parent().nextAll(":not(:first)").children(":not(:first)").hide();
      jQuery(this).parent().nextAll(":not(:first)").children(":first").show();

    });

    jQuery(this).focus(function() {
      jQuery(this).change();
    });

  });

  Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeNode-markup-folder .tobago-treeNode-toggle").click(function() {
    Tobago.Tree.toggleNode(jQuery(this));
    return false;
  });

  Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeMenuNode-markup-folder .tobago-treeMenuNode-toggle")
      .parent().each(function() {
    // if there is no command, than the whole node element should be the toggle
    var toggle = jQuery(this).children(".tobago-treeMenuCommand").size() == 0
        ? jQuery(this)
        : jQuery(this).find(".tobago-treeMenuNode-toggle");
    toggle.click(function() {
      Tobago.Tree.toggleNode(jQuery(this));
    });
  });

  // normal hover effect (not possible with CSS in IE 6)
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeMenuNode").hover(function() {
    jQuery(this).toggleClass("tobago-treeMenuNode-markup-hover");
  });

  // selected for treeNode
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeCommand").focus(function() {
    var command = jQuery(this);
    var node = command.parent(".tobago-treeNode");
    var tree = node.closest(".tobago-tree");
    var selected = tree.children(".tobago-tree-selected");
    selected.val(Tobago.Tree.rowIndex(node));
    tree.find(".tobago-treeNode").removeClass("tobago-treeNode-markup-selected");
    node.addClass("tobago-treeNode-markup-selected");
  });

  // selected for treeSelect
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeSelect > input").change(function () {
    var select = jQuery(this);
    var selected = select.prop("checked");
    var data = select.closest(".tobago-treeMenu, .tobago-tree, .tobago-sheet");
    var hidden = data.children(".tobago-treeMenu-selected, .tobago-tree-selected, .tobago-sheet-selected");
    var newValue;
    if (select.attr("type") == "radio") {
      newValue = "," + Tobago.Tree.rowIndex(select) + ",";
    } else if (selected) {
      newValue = hidden.val() + Tobago.Tree.rowIndex(select) + ",";
    } else {
      newValue = hidden.val().replace(new RegExp("," + Tobago.Tree.rowIndex(select) + ","), ",");
    }
    // XXX optimize: regexp and Tobago.Tree.rowIndex
    hidden.val(newValue);
  });

  // selected for treeMenuNode
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeMenuCommand").focus(function() {
    var command = jQuery(this);
    var node = command.parent(".tobago-treeMenuNode");
    var tree = node.closest(".tobago-treeMenu");
    var selected = tree.children(".tobago-treeMenu-selected");
    selected.val(Tobago.Tree.rowIndex(node));
    tree.find(".tobago-treeMenuNode").removeClass("tobago-treeMenuNode-markup-selected");
    node.addClass("tobago-treeMenuNode-markup-selected");
  });

  // init selected field
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeMenu, .tobago-tree, .tobago-sheet").each(function() {
    var hidden = jQuery(this).children(".tobago-treeMenu-selected, .tobago-tree-selected, .tobago-sheet-selected");
    var string = ",";
    jQuery(this).find(".tobago-treeMenuNode-markup-selected, .tobago-treeNode-markup-selected").each(function() {
      string += Tobago.Tree.rowIndex(jQuery(this)) + ",";
    });
    hidden.val(string);
  });

  // init expanded field
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeMenu, .tobago-tree, .tobago-sheet").each(function() {
    var hidden = jQuery(this).children(".tobago-treeMenu-expanded, .tobago-tree-expanded, .tobago-sheet-expanded");
    var string = ",";
    jQuery(this).find(".tobago-treeMenuNode-markup-expanded, .tobago-treeNode-markup-expanded").each(function() {
      string += Tobago.Tree.rowIndex(jQuery(this)) + ",";
    });
    hidden.val(string);
  });

  // init tree selection for multiCascade
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-tree[data-tobago-selectable=multiCascade]").each(function() {
    var tree = jQuery(this);
    tree.find("input[type=checkbox]").each(function() {
      jQuery(this).change(function(event) {
        var node = jQuery(event.target).parents(".tobago-treeNode");
        var checked = node.find("input[type=checkbox]").prop("checked");
        var children = Tobago.Tree.findChildren(node);
        children.each(function() {
          var childsCheckbox = jQuery(this).find("input[type=checkbox]");
          childsCheckbox.prop("checked", checked);
          childsCheckbox.change();
        });
      });
    });
  });

};

Tobago.Tree.isExpanded = function(node, expanded) {
  var rowIndex = Tobago.Tree.rowIndex(node);
  return expanded.val().indexOf("," + rowIndex + ",") > -1;
};

Tobago.Tree.rowIndex = function (node) {
  return node.attr("id").replace(/.+\:(\d+)(\:\w+)+/, '$1');
};

Tobago.Tree.findChildren = function (node) {
  var treeParentSelector = "[data-tobago-treeparent='" + node.attr("id") + "']";
  var children;
  if (Tobago.Tree.isInSheet(node)) {
    children = node.parent("td").parent("tr").nextAll().children().children(treeParentSelector);
  } else { // normal tree
    children = node.nextAll(treeParentSelector);
  }
  return children;
};

Tobago.Tree.isInSheet = function(node) {
  return node.parent("td").size() == 1;
};

Tobago.registerListener(Tobago.Tree.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Tree.init, Tobago.Phase.AFTER_UPDATE);
