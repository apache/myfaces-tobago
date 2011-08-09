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

Tobago.Tree.destroy = function(node) {
  try {
    var hidden = Tobago.element(node.treeHiddenId);
    if (hidden.rootNode) {
      delete hidden.rootNode;
    }
    Tobago.destroyJsObject(node.treeResources);
    Tobago.destroyJsObject(node);
    delete node;
  } catch(ex) {
    // ignore
  }
};

Tobago.Tree.onClick = function(element) {
  var treeNode = Tobago.treeNodes[element.parentNode.id];
  if (treeNode) {
    treeNode.onClick();
  }
};

Tobago.Tree.DBL_CLICK_TIMEOUT = 300;

Tobago.Tree.onDblClick = function(element) {
  var treeNode = Tobago.treeNodes[element.parentNode.id];
  if (treeNode) {
    treeNode.onDblClick();
  }
};

function tobagoTreeNodeToggle(element) {
  var node = jQuery(element).closest(".tobago-treeNode, .tobago-treeMenuNode");
  var content = jQuery(Tobago.escapeClientId(node.attr("id") + Tobago.SUB_COMPONENT_SEP + "content"));
  var expanded = node.find(".tobago-treeMenuNode-expanded, .tobago-treeNode-expanded");
  var toggle = node.find(".tobago-treeMenuNode-toggle, .tobago-treeNode-toggle");
  if (content.css("display") == "none") {
    content.css("display", "block");
    toggle.each(function() {
      jQuery(this).attr("src", jQuery(this).attr("srcopen"));
      Tobago.fixPngAlpha(this);
    });
    expanded.attr("value", "true");
    node.filter(".tobago-treeNode").addClass("tobago-treeNode-markup-expanded");
    node.filter(".tobago-treeMenuNode").addClass("tobago-treeMenuNode-markup-expanded");
  } else {
    content.css("display", "none");
    toggle.each(function() {
      jQuery(this).attr("src", jQuery(this).attr("srcclose"));
      Tobago.fixPngAlpha(this);
    });
    expanded.attr("value", "false");
    node.filter(".tobago-treeNode").removeClass("tobago-treeNode-markup-expanded");
    node.filter(".tobago-treeMenuNode").removeClass("tobago-treeMenuNode-markup-expanded");
  }
}

function nodeStateId(node) {
    // this must do the same as nodeStateId() in TreeRenderer.java
  return node.id.substring(node.id.lastIndexOf(':') + 1);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// new stuff with jQuery
// TreeListbox

jQuery(document).ready(function () {

  // find all option tags and add the dedicated select tag in its data section.

  jQuery(".tobago-treeListbox > div > div > select > option").each(function() {
    var option = jQuery(this);
    var optionId = option.attr("id");
    var selectId = optionId + "::select";
    var select = jQuery(Tobago.escapeClientId(selectId));
    if (select.length == 1) {
      option.data("select", select);
    } else {
      var empty = option.parent().parent().next().children(":first");
      option.data("select", empty);
    }
  });

  // add on change on all select tag, all options that are not selected hide there dedicated
  // select tag, and the selected option show its dedicated select tag.

  jQuery(".tobago-treeListbox > div > div > select").each(function() {

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

  jQuery(".tobago-treeNode-markup-folder .tobago-treeNode-toggle").click(function() {
    tobagoTreeNodeToggle(this);
  });

  jQuery(".tobago-treeMenuNode-markup-folder .tobago-treeMenuNode-toggle").parent().each(function() {
    // if there is no command, than the whole node element should be the toggle
    var toggle = jQuery(this).children(".tobago-treeMenuCommand").size() == 0
        ? jQuery(this)
        : jQuery(this).find(".tobago-treeMenuNode-toggle");
    toggle.click(function() {
      tobagoTreeNodeToggle(this);
    });
  });

  // normal hover effect (not possible with CSS in IE 6)
  jQuery(".tobago-treeMenuNode").hover(function() {
    jQuery(this).toggleClass("tobago-treeMenuNode-markup-hover");
  });

  // marked for treeNode
  jQuery(".tobago-treeNode").focus(function() {
    var command = jQuery(this);
    var node = command.parent(".tobago-treeNode");
    var tree = node.closest(".tobago-tree");
    var marked = tree.children(".tobago-tree-marked");
    marked.attr("value", node.attr("id"));
    tree.find(".tobago-treeNode").removeClass("tobago-treeNode-markup-marked");
    node.addClass("tobago-treeNode-markup-marked");
  });

  // marked for treeMenuNode
  jQuery(".tobago-treeMenuCommand").focus(function() {
    var command = jQuery(this);
    var node = command.parent(".tobago-treeMenuNode");
    var tree = node.closest(".tobago-treeMenu");
    var marked = tree.children(".tobago-treeMenu-marked");
    marked.attr("value", node.attr("id"));
    tree.find(".tobago-treeMenuNode").removeClass("tobago-treeMenuNode-markup-marked");
    node.addClass("tobago-treeMenuNode-markup-marked");
  });

/*
  jQuery(".tobago-treeListbox > div > div > select > option:selected").change();
*/

/*
  jQuery(".tobago-treeListbox > div > div > select > option:selected").each(function() {
    jQuery(this).change();
  });
*/

});
