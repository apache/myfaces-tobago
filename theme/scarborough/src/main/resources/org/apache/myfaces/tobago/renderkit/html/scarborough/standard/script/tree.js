/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

function toggle(node, treeHiddenId, openFolderIcon, folderIcon) {
//  LOG.debug("toggle("+node+", "+treeHiddenId+", " + openFolderIcon + ", "+ folderIcon +")");
  var content = document.getElementById(node.id + "-cont");
  if (content) {
    var selectState = document.getElementById(treeHiddenId + '-selectState');
    var icon;
    //if (! selectState) { // why this if ?? {
      icon = document.getElementById(node.id + '-icon');
    //}
    var junction = document.getElementById(node.id + '-junction');
    var hidden = document.getElementById(treeHiddenId);
    if (content.style.display == 'none') {
      content.style.display = 'block';
      if (icon) {
        icon.src = openFolderIcon;
      }
      if (junction) {
        junction.src = junction.src.replace(/plus\./, "minus.");
      }
      hidden.value = hidden.value + nodeStateId(node) + ";" ;
    } else {
      content.style.display = 'none';
      if (icon) {
        icon.src = folderIcon;
      }
      if (junction) {
        junction.src = junction.src.replace(/minus\./, "plus.");
      }
      hidden.value = hidden.value.replace(";" + nodeStateId(node) + ";" , ";");
    }
  }
}

function tbgTreeStates(hiddenId) {
  LOG.debug("expandState =" + document.getElementById(hiddenId).value);
  LOG.debug("selectState =" + document.getElementById(hiddenId + "-selectState").value);
}

function tbgToggleExpand(node, treeHiddenId) {
  LOG.debug("tbgToggleExpand(" + node.label + ", " + treeHiddenId + ")");
  var expandState = document.getElementById(treeHiddenId);
  if (node.expanded) {
    node.expanded = false;
    expandState.value
        = expandState.value.replace(";" + nodeStateId(node) + ";" , ";");
  } else {
    node.expanded = true;
    expandState.value = expandState.value + nodeStateId(node) + ";";
  }
}

function tbgSetExpand(node, treeHiddenId) {
  var expandState = document.getElementById(treeHiddenId);
//  LOG.debug("tbgSetExpand(" + node.label + ", " + treeHiddenId + ") :: state = " + expandState.value);
  if (node.hasChildren() && ! node.expanded) {
    node.expanded = true;
    expandState.value = expandState.value + nodeStateId(node) + ";";
  }
  if (node.parentNode) {
    for (var i = 0; i < node.parentNode.childNodes.length; i++) {
//      LOG.debug(node.parentNode.childNodes[i].label + " := " + node.parentNode.childNodes[i].expanded);
      if (node.parentNode.childNodes[i].expanded ){
        tbgClearExpandStatesRecursiv(node.parentNode.childNodes[i], expandState);
        if (node.parentNode.childNodes[i] != node) {
          tbgUnexpand(node.parentNode.childNodes[i], expandState);
        }
      }
    }
  }
//  LOG.debug("tbgSetExpand -> expanded = " + expandState.value);
}


function tbgClearExpandStatesRecursiv(node, expandState) {
  if (node.hasChildren()) {
    for (var i = 0 ; i < node.childNodes.length; i++) {
      tbgUnexpand(node.childNodes[i], expandState);
      tbgClearExpandStatesRecursiv(node.childNodes[i], expandState);
    }
  }
}


function tbgUnexpand(node, expandState) {
  node.expanded = false;
  expandState.value
      = expandState.value.replace(";" + nodeStateId(node) + ";" , ";");
}


function tbgToggleSelect(node, treeHiddenId) {
//  LOG.debug("tbgToggleSelect(" + node.label + ", " + treeHiddenId + ")");
  if (!node.selectable) {
    return;
  }

  var selectState = document.getElementById(treeHiddenId + '-selectState');
  var setSelected = true;
  if (node.selected) {
    node.selected = false;
    selectState.value
        = selectState.value.replace(";" + nodeStateId(node) + ";" , ";");
    if (!node.required || selectState.value.length > 1) {
      setSelected = false;
    }
  }
  if (setSelected){

    var rootNode = document.getElementById(treeHiddenId).rootNode;
    if (node.selectable.match(/^single/)) {
      // remove all other selection marks
      clearSelectionExcept(rootNode, selectState, node);
    } else if (node.selectable.match(/^sibling/)) {
      if (node.selectable.match(/LeafOnly$/) && node.hasChildren()) {
        clearSelectionExcept(rootNode, selectState, node);
      } else {
        clearSelectionExceptSibling(rootNode, selectState, node);
      }
    }

    if (!node.selectable.match(/LeafOnly$/) || !node.hasChildren()) {
      node.selected = true;
      selectState.value = selectState.value + nodeStateId(node) + ";"
//      LOG.debug("selectable = " + node.selectable);
    }
  }
//  LOG.debug("tbgToggleSelect -> selected = " + selectState.value);
}

function tbgUnselect(node, selectState) {
  node.selected = false;
    selectState.value
        = selectState.value.replace(";" + nodeStateId(node) + ";" , ";");
}

function clearSelectionExcept(node, selectState, selectedNode) {
  if (node != selectedNode) {
    tbgUnselect(node, selectState);
  }
  if (node.childNodes) {
    for (var i = 0; i < node.childNodes.length; i++) {
      clearSelectionExcept(node.childNodes[i], selectState, selectedNode);
    }
  }
}

function clearSelectionExceptSibling(node, selectState, selectedNode) {
//  LOG.debug(node.label + "  ---");
  if (node.parentNode && selectedNode.parentNode != node.parentNode) {
    tbgUnselect(node, selectState);
  }
  if (node.childNodes) {
    for (var i = 0; i < node.childNodes.length; i++) {
      clearSelectionExceptSibling(node.childNodes[i], selectState, selectedNode);
    }
  }
}


function toggleSelect(node, treeHiddenId, uncheckedIcon, checkedIcon) {
  var selectState = document.getElementById(treeHiddenId + '-selectState');
  var hidden = document.getElementById(treeHiddenId);
  var icon = document.getElementById(node.id + '-markIcon');
  var treeNode = Tobago.treeNodes[node.id];
//  LOG.debug("treeNode.required = " + treeNode.required);
  if (! (treeNode.selectable.match(/LeafOnly$/) && treeNode.childNodes.length > 0)) {
    if (treeNode.selectable.match(/^single/)) {
      if (! (selectState.value.indexOf(";" + nodeStateId(node) + ";", 0) > -1)) {
//        LOG.debug("single selection changed");
        icon.src = checkedIcon;
        oldIcon = getIconForId(node, selectState.value);
        oldNode = getNodeForId(treeNode, selectState.value);
        if (oldIcon && oldIcon != icon
            && (!(oldNode && oldNode.childNodes.length > 0 && oldNode.selectable
                  && oldNode.selectable.match(/LeafOnly$/)))) {
          oldIcon.src = uncheckedIcon;
        }
        selectState.value = ";" +  nodeStateId(node) + ";" ;
      } else {
//        LOG.debug("single selection cleared");
        if (! treeNode.required) {
          icon.src = uncheckedIcon;
          selectState.value = ";" ;
        } else {
//          LOG.debug("single selection hold");
        }
      }
    }
    else {
      if (selectState.value.indexOf(";" + nodeStateId(node) + ";", 0) > -1) {
        // actual node is selected -> remove selection
//        LOG.debug("selectState.value = " + selectState.value);
//        LOG.debug("nodeSelectState   = " + ";" + nodeStateId(node) + ";" );
//        LOG.debug("required          = " +  treeNode.required);
        if (!(treeNode.required && selectState.value == ";" + nodeStateId(node) + ";") ) {
          icon.src = uncheckedIcon;
          selectState.value = selectState.value.replace(";" + nodeStateId(node) + ";" , ";");
        }

//        LOG.debug("selectState.value = " + selectState.value);
      } else {
        // actual node is not selected -> set selection
        if (treeNode.selectable.match(/^siblings/)) {
           // remove all nodes which are not siblings of actual node
          // TODO:


        } else {
          icon.src = checkedIcon;
          selectState.value = selectState.value + nodeStateId(node) + ";" ;
        }
      }
    }
  }
}

function createJavascriptVariable(id) {
  if (id && id.replace) {
    return id.replace(/:/g, "_");
  }
  else {
    return id;
  }
}

function getIconForId(node, selectId) {
  var id = node.id.substring(0, node.id.lastIndexOf(":") + 1) + selectId.replace(/;/g, "") + '-markIcon';
  return document.getElementById(id);
}

function getNodeForId(node, selectId) {

  var match = selectId.match(/^;(.*);$/);
  if (match) {
    selectId = match[1];
  } else {
      return null;
  }
  while (node.parentNode) {
    node = node.parentNode;
    if (nodeStateId(node) == selectId) {
      return node;
    }
  }

  // node is rootNode
  return getNodeForIdRecursiv(node, selectId);
}

function getNodeForIdRecursiv(node, selectId) {

  for (var i=0; i<node.childNodes.length; ++i) {
    if (nodeStateId(node.childNodes[i]) == selectId) {
      return node.childNodes[i];
    }
    var found = getNodeForIdRecursiv(node.childNodes[i], selectId);
    if (found) {
      return found;
    }
  }
}

function storeMarker(node, treeHiddenId) {
  var markerHidden = document.getElementById(treeHiddenId + '-marker');
  if (markerHidden) {
    markerHidden.value = node.id;
  }
}

function nodeStateId(node) {
  // this must do the same as nodeStateId() in TreeRenderer.java
  var last = node.id.lastIndexOf(":") + 1;
  return node.id.substring(last);
}

var TreeManager = {
  _counter: 0,
  getId: function() {
    return "tobago.node." + this._counter++;
  }
};

function TreeNode(label, id, hideIcons, hideJunctions, hideRootJunction,
    hideRoot, treeHiddenId, selectable, mutable,
    formId, selected, marked, required, disabled, treeResources,
    action, parent, icon) {
	this.label = label;
	this.id = id;
  Tobago.treeNodes[id] = this;
  this.hideIcons = hideIcons || false;
  this.hideJunctions = hideJunctions || false;
  this.hideRootJunction = hideRootJunction || false;
  this.hideRoot = hideRoot || false;
  this.treeHiddenId = treeHiddenId;
  this.selectable = selectable;
  this.mutable = mutable;
  this.formId = formId;
  this.selected = selected;
  this.marked = marked;
  this.required = required;
  this.disabled = disabled;
  this.treeResources = treeResources;
	this.action = action;
	this.icon = icon;
	this.childNodes = [];
// FIXME: page:form
	this.onclick
	  = mutable
      ? "storeMarker(this.parentNode, '" + treeHiddenId + "')"
      : selectable
        ? "toggleSelect(this.parentNode, '" + treeHiddenId
            + "', '" + treeResources.getImage("unchecked.gif")
            + "', '" + treeResources.getImage("checked.gif")
            + "')"
	      : "Tobago.submitAction('" + id + "')";
	this.onfocus = "storeMarker(this.parentNode, '" + treeHiddenId + "')";
//	this.ondblclick = "toggle(this.parentNode, '" + treeHiddenId + "')";

  var hidden = document.getElementById(treeHiddenId);
  var iconOnClick = '';
  var markIcon = '';
  var markIconOnClick = '';
  if (selectable) {
    if (selected) {
      markIcon = treeResources.getImage("checked.gif");
    } else {
      markIcon = treeResources.getImage("unchecked.gif");
    }
    markIconOnClick
        = 'onclick="toggleSelect(this.parentNode, \'' + treeHiddenId
            + '\', \'' + treeResources.getImage("unchecked.gif")
            + '\', \'' + treeResources.getImage("checked.gif")
            + '\')"';
  }
  if (marked) {
    storeMarker(this, treeHiddenId);
  }

  this.add = function (node) {
  };

  this.toString = function (depth, last) { // merge with folder...
    var str = '';
    str += '<div id="' + this.id + '" class="tree-item">'; // XXX or mark this?!
    str += this.indent(depth, last);
    if (! this.hideJunctions
        && ! (this.hideRootJunction && this.hideRoot && depth == 1)) {
      str += '<img class="tree-junction" id="' + this.id
          + '-junction" src="' + ((last)
            ? treeResources.getImage("L.gif")
            : treeResources.getImage("T.gif"))
          + '" alt="">';
    } else if (( !this.hideRoot && depth >0 ) || (this.hideRoot && depth > 1)) {
      str += '<img class="tree-junction" id="' + this.id
          + '-junction" src="' + treeResources.getImage("blank.gif")
          + '" alt="">';
    }
    if (! this.hideIcons) {
      str += '<img class="tree-icon" id="' + this.id
//          + '-icon" src="' + this.icon + '" ' + iconOnClick + ' alt="">';
          + '-icon" src="' + treeResources.getImage("new.gif") + '" ' + iconOnClick + ' alt="">';
    }
    if (selectable) {
      str += '<img class="tree-icon" id="' + this.id
          + '-markIcon" src="' + markIcon + '" ' + markIconOnClick + ' alt="">';
    }
    var itemStyle = "tree-item-label";
    if (this.disabled) {
      itemStyle += " tree-item-label-disabled";
    }
    if (this.marked) {
      itemStyle += " tree-item-marker";
    }
    if (this.action && !this.disabled) {
      str += '<a class="' + itemStyle + '" href="' + this.action + '" id="'
        + this.id + '-anchor">' + this.label + '</a>';
    } else {
    // TODO: mozilla shoud use href="javascript:;" and ie href="#"
      str += '<a class="' + itemStyle + '"';
      if (!this.disabled) {
          str += ' href="#"' + ' onclick="' + this.onclick + '"'
              + ' onfocus="' + this.onfocus + '"';
      }
      str += '>'
          + this.label + '</a>';
    }
    str += '</div>';
    return str;
  };

  // is the node the last child of its paranet?
  this.isLast = function() {
    if (!this.parentNode) return true;
    var siblings = this.parentNode.childNodes;
    if (siblings.length == 0) return true;
    return (this == siblings[siblings.length-1]);
  };

  this.indent = function(depth, last) {
    if (!depth) depth = 0;
    var str = "";
    var node = this;
    while (node.parentNode) {
      node = node.parentNode;
      if((!node.parentNode && (node.hideRootJunction || node.hideRoot))
        || (node.parentNode && !node.parentNode.parentNode
            && node.hideRootJunction && node.hideRoot)) {
        break;
      }
      str = '<img class="tree-junction" src="'
        + ((node.isLast() || node.hideJunctions)
          ? treeResources.getImage("blank.gif")
          : treeResources.getImage("I.gif")) + '" alt="">' + str;
    }
    return str;
  };

  this.initSelection = function() {
    if (this.selected) {
      var selectState = document.getElementById(treeHiddenId + '-selectState');
      if (selectState) {
        selectState.value = selectState.value + nodeStateId(this) + ";";
      }
    }
  };

	if (parent) {
	  parent.add(this);
	}

  this.hasChildren = function() {
    return (this.childNodes && this.childNodes.length > 0);
  }
}

function TreeFolder(label, id, hideIcons, hideJunctions, hideRootJunction,
    hideRoot, treeHiddenId, selectable, mutable, formId, selected, marked,
    expanded, required, disabled, treeResources, action, parent, icon, openIcon) {
	this.base = TreeNode;
	this.base(label, id, hideIcons, hideJunctions, hideRootJunction,
	    hideRoot, treeHiddenId, selectable, mutable, formId, selected, marked,
      required, disabled, treeResources, action, parent, icon);
  this.open = false;
  this.expanded = expanded;


  if (this.expanded) {
    var hidden = document.getElementById(this.treeHiddenId);
    var regex = new RegExp(";" + nodeStateId(this) + ";");
    if (! hidden.value.match(regex)) {
      hidden.value = hidden.value + nodeStateId(this) + ";" ;
    }
  }

  this.icon = icon
      || treeResources.getImage("foldericon.gif");
  this.openIcon = openIcon
      || treeResources.getImage("openfoldericon.gif");

  this.add = function (node) {
  	node.parentNode = this;
  	this.childNodes[this.childNodes.length] = node;
  	return node;
  };

  this.toString = function (depth, last) {
    if (!depth) depth = 0;
    var hidden = document.getElementById(this.treeHiddenId);
//    var isOpen
//        = hidden.value.indexOf(";" + nodeStateId(this) + ";", 0) > -1
//      || depth == 0 && this.hideRoot; // don't close a hidden root!
    var str = '';
    var iconOnClickFunction = '';
    var actualIcon = '';
    var markIcon = '';
    var markIconOnClickFunction = '';
    if (selectable) {
      if (selectable.match(/LeafOnly$/)) {
        markIcon = treeResources.getImage("1x1.gif");
      } else {
        if (selected) {
          markIcon = treeResources.getImage("checked.gif");
        } else {
          markIcon = treeResources.getImage("unchecked.gif");
        }
        markIconOnClickFunction
            = 'onclick="toggleSelect(this.parentNode, \'' + treeHiddenId
              + '\', \'' + treeResources.getImage("unchecked.gif")
              + '\', \'' + treeResources.getImage("checked.gif")
              + '\')"';
        }
    }

    actualIcon = (this.expanded ? this.openIcon : this.icon) ;
    iconOnClickFunction = "toggle";


    if (! this.hideRoot || depth > 0) {
      str += '<div id="' + this.id + '" class="tree-item">';
      str += this.indent(depth, last);
      if (!(   this.hideJunctions
            || this.hideRootJunction && depth == 0
            || this.hideRootJunction && this.hideRoot && depth == 1)) {
        str += '<img class="tree-junction" id="' + this.id
            + '-junction" src="' + (this.expanded
              ? ((depth == 0)
                ? treeResources.getImage("Rminus.gif")
                : (last)
                  ? treeResources.getImage("Lminus.gif")
                  : treeResources.getImage("Tminus.gif"))
              : ((depth == 0)
                ? treeResources.getImage("Rplus.gif")
                : (last)
                  ? treeResources.getImage("Lplus.gif")
                  : treeResources.getImage("Tplus.gif"))
              )
            + '" onclick="toggle(this.parentNode, \'' + treeHiddenId
            + '\', \'' + treeResources.getImage("openfoldericon.gif")
            + '\', \'' + treeResources.getImage("foldericon.gif")
            + '\')"'
            + ' alt="">';
      } else if (( !this.hideRoot && depth >0 ) || (this.hideRoot && depth > 1)) {
        str += '<img class="tree-junction" id="' + this.id
            + '-junction" src="' + treeResources.getImage("blank.gif")
            + '" alt="">';
    }
      if (! this.hideIcons) {
        str += '<img class="tree-icon" id="' + this.id
            + '-icon" src="' + actualIcon + '"'
            + ' onclick="' + iconOnClickFunction + '(this.parentNode, \'' + treeHiddenId
            + '\', \'' + treeResources.getImage("openfoldericon.gif")
            + '\', \'' + treeResources.getImage("foldericon.gif")
            + '\')"'
            + ' alt="">';
      }
      if (selectable) {
        str += '<img class="tree-icon" id="' + this.id
            + '-markIcon" src="' + markIcon + '" ' + markIconOnClickFunction + ' alt="">';
      }
      var itemStyle = "tree-folder-label";

      if (this.disabled) {
        itemStyle += " tree-folder-label-disabled";
      }
      if (this.marked) {
        itemStyle += " tree-item-marker";
      }
      if (this.action && !this.disabled) {
        str += '<a class="' + itemStyle + '" href="' + this.action + '" id="'
        + this.id + '-anchor">' + this.label + '</a>';
      } else {
    // TODO: mozilla shoud use href="javascript:;" and ie href="#"
        str += '<a class="' + itemStyle + '"';
        if (!this.disabled) {
            str += ' href="#"' + ' onclick="' + this.onclick + '"'
                + ' onfocus="' + this.onfocus + '"';
        }
        str += '>'
            + this.label + '</a>';
      }
      str += '</div>';
    }
    str += '<div id="' + this.id
        + '-cont" class="tree-container" style="display: '
        + (this.expanded ? 'block' : 'none') + ';">';
    for (var i=0; i<this.childNodes.length; ++i) {
      var lastChild = i+1 == this.childNodes.length;
      var n = this.childNodes[i];
      str += n.toString(depth+1, lastChild);
    }
    str += '</div>';

    return str;
  };



  this.initSelection = function() {
    if (this.selected) {
      var selectState = document.getElementById(this.treeHiddenId + '-selectState');
      if (selectState) {
        selectState.value = selectState.value + nodeStateId(this) + ";" ;
      }
    }

    for (var i=0; i<this.childNodes.length; ++i) {
      this.childNodes[i].initSelection();
    }
  };
}

TreeFolder.prototype = new TreeNode;

// //////////////////////////////////////////////////  listTree

function tobagoTreeListboxInitSelection(node) {
  node.initSelection();
}

function tobagoTreeListboxClearValueChangedMarker(hiddenId) {
  var rootNode = document.getElementById(hiddenId).rootNode;
  rootNode.valueChanged = false;

}

function tobagoTreeListboxChange(element, hiddenId) {
  return;
}

function tbgGetParentNode(selectElement, rootNode) {
  var level = selectElement.id.lastIndexOf("_");
  var idPrefix = selectElement.id.substr(0, level);
  level = selectElement.id.substr(level + 1) - 0;

  var selector = document.getElementById(idPrefix + "_" + level);

  var node = rootNode;
  for (var actualLevel = 0 ;actualLevel < level; actualLevel++) {
    for (var i = 0; i < node.childNodes.length; i++) {
      if (node.childNodes[i].hasChildren() && node.childNodes[i].expanded) {
        node = node.childNodes[i];
        break;
      }
    }
  }

  return node;
}


function tbgGetActualNode(element, rootNode) {
  var actualNode = rootNode;
  var selectElement = element.parentNode;
  var level = selectElement.id.lastIndexOf("_");
  var idPrefix = selectElement.id.substr(0, level);
  level = selectElement.id.substr(level + 1) - 0;

  var selector = document.getElementById(idPrefix + "_" + level);

  var node = rootNode;
  for (var actualLevel = 0 ;actualLevel < level; actualLevel++) {
    for (var i = 0; i < node.childNodes.length; i++) {
      if (node.childNodes[i].hasChildren() && node.childNodes[i].expanded) {
        node = node.childNodes[i];
        break;
      }
    }
  }

  return node.childNodes[selector.value];
}


function tbgGetSelectionCount(element) {
  var count = 0;
  for (var i = 0; i < element.options.length; i++) {
    if (element.options[i].selected) {count++;}
  }
  return count;
}

function tbgClearSelectionStates(node) {
  node.selected = false;
  if (node.hasChildren()) {
    for (var i = 0; i < node.childNodes.length; i++) {
      tbgClearSelectionStates(node.childNodes[i]);
    }
  }
}

function tbgGetSelectedIndizes(element) {
  var indizes = new Array();
  for (var i = 0; i < element.options.length; i++) {
    if (element.options[i].selected) {
      indizes[indizes.length] = i;
    }
  }
  return indizes;
}

function tbgTreeListboxChange(element, hiddenId) {
  // this handler is invoked only in sibling mode
  var rootNode = document.getElementById(hiddenId).rootNode;
  var expandState = document.getElementById(hiddenId);
  var selectState = document.getElementById(hiddenId + '-selectState');
//  LOG.debug("1 selectState : " + selectState.value);
//  LOG.debug("1 expandState : " + expandState.value);

  var parentNode = tbgGetParentNode(element, rootNode);

  var singleSelection = tbgGetSelectionCount(element) == 1;

  tbgClearSelectionStates(rootNode);

  var level = element.id.lastIndexOf("_");
  var idPrefix = element.id.substr(0, level);
  level = element.id.substr(level + 1) - 0;

  if (singleSelection) {
    // if selected is folderNode : expand folder
    // else : set new selected.

    var actualNode = parentNode.childNodes[element.selectedIndex];
    if (actualNode.hasChildren()) {
      // actual node is folder : expand
      tbgSetExpand(actualNode, hiddenId);
      selectState.value = ";";
      tobagoTreeListboxSetup(actualNode, idPrefix, level + 1, hiddenId);
    } else {
      tbgClearExpandStatesRecursiv(parentNode, expandState);
      actualNode.selected = true;
      selectState.value = ";" + nodeStateId(actualNode) + ";";

      tobagoTreeListboxDisable(idPrefix, level + 1);
    }
  } else {
    // multiselection

    var selected = tbgGetSelectedIndizes(element);
    selectState.value = ";";
    for (var i = 0; i < selected.length; i++) {
      var idx = selected[i];
      if (!parentNode.childNodes[idx].hasChildren()) {
        parentNode.childNodes[idx].selecded = true;
        selectState.value += nodeStateId(parentNode.childNodes[idx]) + ";";
      } else {
        element.options[idx].selected = false;
      }
    }
  }

//  LOG.debug("2 selectState : " + selectState.value);
//  LOG.debug("2 expandState : " + expandState.value);

}

function tbgTreeListboxClick(selectElement, hiddenId) {

  var rootNode = document.getElementById(hiddenId).rootNode;
  var expandState = document.getElementById(hiddenId);
  var selectState = document.getElementById(hiddenId + '-selectState');
//  LOG.debug("1 selectState : " + selectState.value);
//  LOG.debug("1 expandState : " + expandState.value);

  var actualNode = tbgGetClickedNode(selectElement, rootNode);

//  var actualNode = tbgGetActualNode(element, rootNode);


  tbgSetExpand(actualNode, hiddenId);
  tbgToggleSelect(actualNode, hiddenId);


  // update gui
  var parentNode = actualNode.parentNode;
  for (var i = 0; i < selectElement.options.length; i++) {
      selectElement.options[i].selected
          = (parentNode.childNodes[i].selected
             || (parentNode.childNodes[i].hasChildren() && parentNode.childNodes[i].expanded));
  }

  var level = selectElement.id.lastIndexOf("_");
  var idPrefix = selectElement.id.substr(0, level);
  level = selectElement.id.substr(level + 1) - 0;

  parentSelectElement = document.getElementById(idPrefix + "_" + (level - 1));
  if (parentSelectElement) {
//    LOG.debug("clear parent");
    parentNode = parentNode.parentNode;
    for (var i = 0; i < parentSelectElement.options.length; i++) {
      parentSelectElement.options[i].selected
          = (parentNode.childNodes[i].hasChildren() && parentNode.childNodes[i].expanded);
    }
  }


  if (actualNode.childNodes && actualNode.childNodes.length > 0 && actualNode.expanded ) {
    tobagoTreeListboxSetup(actualNode, idPrefix, level + 1, hiddenId);
  } else {
    tobagoTreeListboxDisable(idPrefix, level + 1);
  }
//  LOG.debug("2 selectState : " + selectState.value);
//  LOG.debug("2 expandState : " + expandState.value);

}

function tbgGetClickedNode(element, rootNode) {
  // alle selected options von element aufsammeln
  // alle selected nodes von actualNode aufsammeln
  // die node die nur in einer der samlungen vorkommt ist die geclickte!
  var parentNode = tbgGetParentNode(element, rootNode);

  var idx = -1;

  if (rootNode.selectable.match(/^single/)) {
    idx = element.selectedIndex;
    if (idx == -1) {
      // node was deselected
      for (var i = 0; i < parentNode.childNodes; i++ ) {
        if (parentNode.childNodes[i].selected) {
          idx = i;
          break;
        }
      }
    }
  }

  if (idx > -1) {
    return parentNode.childNodes[idx];
  }


}

/*function   tbgGetdif(bigArray, smallArray) {
  if (smallArray.length < 1 && bigArray.length == 1) {
    return bigArray[0];
  }
  for (var i = 0; i < smallArray.length; i++) {
    if (bigArray[i] != smallArray[i]) {return bigArray[i]; }
  }
  return -1;
}*/




function tobagoTreeListboxSetup(node, idPrefix, level, hiddenId) {
  tobagoTreeListboxEnable(idPrefix, level);
  var selector = document.getElementById(idPrefix + "_" + level);
  for (var i = 0; i < node.childNodes.length; i++) {
    selector.options[selector.options.length] = tobagoTreeListboxCreateOption(node.childNodes[i], i, hiddenId);
  }
}

function tobagoTreeListboxCreateOption(node, index, hiddenId) {
  var label = node.label;
  if (node.childNodes && node.childNodes.length) {
    label += " \u2192";
  }
  var option = new Option(label, index);
  option.hiddenId = hiddenId;
//  Tobago.addEventListener(option, 'click', tbgTreeListboxClick);

  return option;
}

function tobagoTreeListboxRemoveOptions(idPrefix, start) {
  var selector = document.getElementById(idPrefix + "_" + start);
  while (selector && selector.hasChildNodes()) {
    selector.removeChild(selector.firstChild);
  }
}

function tobagoTreeListboxEnable(idPrefix, start) {
  var selector = document.getElementById(idPrefix + "_" + start);
  while (selector) {
    tobagoTreeListboxRemoveOptions(idPrefix, start++);
    Tobago.removeCssClass(selector, "tobago-treeListbox-unused");
    selector = document.getElementById(idPrefix + "_" + start);
  }
}
function tobagoTreeListboxDisable(idPrefix, start) {
  tobagoTreeListboxRemoveOptions(idPrefix, start);
  var selector = document.getElementById(idPrefix + "_" + start);
  while (selector) {
    tobagoTreeListboxRemoveOptions(idPrefix, start++);
    Tobago.addCssClass(selector, "tobago-treeListbox-unused");
    selector.oldValue = -1;
    selector = document.getElementById(idPrefix + "_" + start);
  }
}




