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

Tobago.Tree.updateMarker = function(node, add) {
  node = Tobago.element(node);
  if (node) {
    node = node.firstChild;
    while (node) {
      if (node.className && node.className.indexOf("tobago-treeNode-default") > -1) {
        if (add) {
          Tobago.addCssClass(node, "tobago-treeNode-marker");
        } else {
          Tobago.removeCssClass(node, "tobago-treeNode-marker");
        }
      }
      node = node.nextSibling;
    }
  }
}

Tobago.Tree.storeMarker = function(node, treeHiddenId) {
  var markerHidden = document.getElementById(treeHiddenId + '-marker');
  if (markerHidden) {
    Tobago.Tree.updateMarker(markerHidden.value, false);
    markerHidden.value = node.id;
  }
  Tobago.Tree.updateMarker(node.id, true);
}

function tobagoTreeNodeToggle(node, treeHiddenId, openFolderIcon, folderIcon, openMenuIcon, closeMenuIcon) {
  LOG.debug("toggle(" + node + ", " + treeHiddenId + ", " + openFolderIcon + ", " + folderIcon + ", "
      + openMenuIcon + ", " + closeMenuIcon + ")");
  var content = document.getElementById(node.id + "-cont");
  if (content) {
    var expandedState = document.getElementById(node.id + '-expanded');
    var icon = document.getElementById(node.id + '-icon');
    var menuIcon = document.getElementById(node.id + '-menuIcon');
    var junction = document.getElementById(node.id + '-junction');
    var hidden = document.getElementById(treeHiddenId);
    if (content.style.display == 'none') {
      content.style.display = 'block';
      if (icon) {
        icon.src = openFolderIcon;
      }
      if (menuIcon) {
        menuIcon.src = openMenuIcon;
      }
      if (junction) {
        junction.src = junction.src.replace(/plus\./, "minus.");
      }
      hidden.value = hidden.value + nodeStateId(node) + ";";
      expandedState.value = "true";
    } else {
      content.style.display = 'none';
      if (icon) {
        icon.src = folderIcon;
      }
      if (menuIcon) {
        menuIcon.src = closeMenuIcon;
      }
      if (junction) {
        junction.src = junction.src.replace(/minus\./, "plus.");
      }
      hidden.value = hidden.value.replace(";" + nodeStateId(node) + ";", ";");
      expandedState.value = "false";
    }
  }
}

function nodeStateId(node) {
  // this must do the same as nodeStateId() in TreeRenderer.java
  return node.id.substring(node.id.lastIndexOf(':') + 1);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// new stuff with jQuery
// TreeListbox

function asJQueryId(string) {
  return "#" + string.replace(/:/g, "\\:");
}

$(document).ready(function () {

  // find all option tags and add the dedicated select tag in its data section.

  $(".tobago-treeListbox-default > div > div > select > option").each(function() {
    var option = $(this);
    var optionId = option.attr("id");
    var selectId = optionId + "::select";
    var select = $(asJQueryId(selectId));
    if (select.length == 1) {
      option.data("select", select);
    } else {
      var empty = option.parent().parent().next().children(":first");
      option.data("select", empty);
    }
  });

  // add on change on all select tag, all options that are not selected hide there dedicated
  // select tag, and the selected option show its dedicated select tag.

  $(".tobago-treeListbox-default > div > div > select").each(function() {

    $(this).change(function() {
      $(this).children("option:not(:selected)").each(function() {
        $(this).data("select").hide();
      });
      $(this).children("option:selected").data("select").show();

      // Deeper level (2nd and later) should only show the empty select tag.
      // The first child is the empty selection.
      $(this).parent().nextAll(":not(:first)").children(":not(:first)").hide();
      $(this).parent().nextAll(":not(:first)").children(":first").show();

    });

    $(this).focus(function() {
      $(this).change();
    });

  });

/*
  $(".tobago-treeListbox-default > div > div > select > option:selected").change();
*/

/*
  $(".tobago-treeListbox-default > div > div > select > option:selected").each(function() {
    $(this).change();
  });
*/

});
