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
  var content = jQuery(Tobago.Utils.escapeClientId(node.attr("id") + Tobago.SUB_COMPONENT_SEP + "content"));
  var expanded = node.find(".tobago-treeMenuNode-expanded, .tobago-treeNode-expanded");
  var toggle = node.find(".tobago-treeMenuNode-toggle, .tobago-treeNode-toggle");
  if (content.css("display") == "none") {
    content.css("display", "block");
    toggle.each(function() {
      src = jQuery(this).data("tobago-srcopen");
      if (src == null) { // use the close icon if there is no open icon
        src = jQuery(this).data("tobago-srcclose");
      }
      jQuery(this).attr("src", src);
      Tobago.fixPngAlpha(this);
    });
    expanded.attr("value", "true");
    node.filter(".tobago-treeNode").addClass("tobago-treeNode-markup-expanded");
    node.filter(".tobago-treeMenuNode").addClass("tobago-treeMenuNode-markup-expanded");
  } else {
    content.css("display", "none");
    toggle.each(function() {
      src = jQuery(this).data("tobago-srcclose");
      if (src == null) { // use the open icon if there is no close icon
        src = jQuery(this).data("tobago-srcopen");
      }
      jQuery(this).attr("src", src);
      Tobago.fixPngAlpha(this);
    });
    expanded.attr("value", "false");
    node.filter(".tobago-treeNode").removeClass("tobago-treeNode-markup-expanded");
    node.filter(".tobago-treeMenuNode").removeClass("tobago-treeMenuNode-markup-expanded");
  }
};

Tobago.Tree.init = function(elements) {

  var listboxSelects = Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeListbox").find("select");

  // find all option tags and add the dedicated select tag in its data section.

  listboxSelects.children("option").each(function() {
    var option = jQuery(this);
    var optionId = option.attr("id");
    var selectId = optionId + "::select";
    var select = jQuery(Tobago.Utils.escapeClientId(selectId));
    if (select.length == 1) {
      option.data("select", select);
    } else {
      var empty = option.parent().parent().next().children(":first");
      option.data("select", empty);
    }
  });

  // add on change on all select tag, all options that are not selected hide there dedicated
  // select tag, and the selected option show its dedicated select tag.

  listboxSelects.each(function() {

    jQuery(this).change(function() {
      jQuery(this).children("option:not(:selected)").each(function() {
        jQuery(this).data("select").hide();
      });
      jQuery(this).children("option:selected").data("select").show();

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

  // marked for treeNode
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeNode").focus(function() {
    var command = jQuery(this);
    var node = command.parent(".tobago-treeNode");
    var tree = node.closest(".tobago-tree");
    var marked = tree.children(".tobago-tree-marked");
    marked.attr("value", node.attr("id"));
    tree.find(".tobago-treeNode").removeClass("tobago-treeNode-markup-marked");
    node.addClass("tobago-treeNode-markup-marked");
  });

  // marked for treeMenuNode
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-treeMenuCommand").focus(function() {
    var command = jQuery(this);
    var node = command.parent(".tobago-treeMenuNode");
    var tree = node.closest(".tobago-treeMenu");
    var marked = tree.children(".tobago-treeMenu-marked");
    marked.attr("value", node.attr("id"));
    tree.find(".tobago-treeMenuNode").removeClass("tobago-treeMenuNode-markup-marked");
    node.addClass("tobago-treeMenuNode-markup-marked");
  });
};

