/* Copyright (c) 2002 Atanion GmbH, Germany
   All rights reserved.
   $Id$
  */

function toggle(node, treeHiddenId, openFolderIcon, folderIcon) {
  var content = document.getElementById(node.id + "-cont");
  if (content) {
    var selectState = document.getElementById(treeHiddenId + '-selectState');
    var icon;
    if (! selectState) {
      icon = document.getElementById(node.id + '-icon');
    }
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

function toggleSelect(node, treeHiddenId, uncheckedIcon, checkedIcon) {
  var selectState = document.getElementById(treeHiddenId + '-selectState');
  var hidden = document.getElementById(treeHiddenId);
  var icon = document.getElementById(node.id + '-markIcon');
  if (selectState.value.indexOf(";" + nodeStateId(node) + ";", 0) > -1) {
    icon.src = uncheckedIcon;
    selectState.value = selectState.value.replace(";" + nodeStateId(node) + ";" , ";");
  } else {
    icon.src = checkedIcon;
    selectState.value = selectState.value + nodeStateId(node) + ";" ;
  }
}

function storeMarker(node, treeHiddenId) {
  var markerHidden = document.getElementById(treeHiddenId + '-marker');
  markerHidden.value = node.id;
}

function nodeStateId(node) {
  var last = node.id.lastIndexOf(":") + 1;
  return node.id.substring(last);
}

var TreeManager = {
  _counter: 0,
  getId: function() {
    return "tobago.node." + this._counter++;
  }
}

function TreeNode(label, id, hideIcons, hideJunctions, hideRootJunction,
    hideRoot, treeHiddenId, selectable, mutable,
    formId, selected, marked, treeResources,
    action, parent, icon) {
	this.label = label;
	this.id = id;
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
  this.treeResources = treeResources;
	this.action = action;
	this.icon = icon;
	this.childNodes = [];
// fixme: page:form
	this.onclick
	  = mutable
      ? "storeMarker(this.parentNode, '" + treeHiddenId + "')"
      : selectable
        ? "toggleSelect(this.parentNode, '" + treeHiddenId
            + "', '" + treeResources.getImage("unchecked.gif")
            + "', '" + treeResources.getImage("checked.gif")
            + "')"
	      : "submitAction('" + formId + "','" + id + "')";
	this.onfocus = "storeMarker(this.parentNode, '" + treeHiddenId + "')";
//	this.ondblclick = "toggle(this.parentNode, '" + treeHiddenId + "')";

  var hidden = document.getElementById(treeHiddenId);
  var iconOnClick = '';
  var markIcon = '';
  var markIconOnClick = '';
  if (selectable) {
    if (selected) {
      var selectState = document.getElementById(treeHiddenId + '-selectState');
      selectState.value = selectState.value + nodeStateId(this) + ";" ;
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
  }

  this.toString = function (depth, last) { // merge with folder...
    var str = '';
    str += '<div id="' + this.id + '" class="tree-item">';
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
    if (this.action) {
      str += '<a class="tree-item-label" href="' + this.action + '" id="'
        + this.id + '-anchor">' + this.label + '</a>';
    } else {
    // todo: mozilla shoud use href="javascript:;" and ie href="#"
      str += '<a class="tree-item-label" href="#"' //
        + ' onclick="' + this.onclick + '"'
        + ' onfocus="' + this.onfocus + '"'
        + '>'
        + this.label + '</a>';
    }
    str += '</div>';
    return str;
  }

  // is the node the last child of its paranet?
  this.isLast = function() {
    if (!this.parentNode) return true;
    var siblings = this.parentNode.childNodes;
    if (siblings.length == 0) return true;
    return (this == siblings[siblings.length-1]);
  }

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
  }

	if (parent) {
	  parent.add(this);
	}
}

function TreeFolder(label, id, hideIcons, hideJunctions, hideRootJunction,
    hideRoot, treeHiddenId, selectable, mutable, formId, selected, marked,
    expanded, treeResources,
    action, parent, icon, openIcon) {
	this.base = TreeNode;
	this.base(label, id, hideIcons, hideJunctions, hideRootJunction,
	    hideRoot, treeHiddenId, selectable, mutable, formId, selected, marked, treeResources,
	    action, parent);
  this.open = false;
  this.expanded = expanded;


  if (this.expanded) {
    var hidden = document.getElementById(this.treeHiddenId);
    hidden.value = hidden.value + nodeStateId(this) + ";" ;
  }

  this.icon = icon
      || treeResources.getImage("foldericon.gif");
  this.openIcon = openIcon
      || treeResources.getImage("openfoldericon.gif");

  this.add = function (node) {
  	node.parentNode = this;
  	this.childNodes[this.childNodes.length] = node;
  	return node;
  }

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
      if (this.action) {
        str += '<a class="tree-folder-label" href="' + this.action + '" id="'
        + this.id + '-anchor">' + this.label + '</a>';
      } else {
    // todo: mozilla shoud use href="javascript:;" and ie href="#"
        str += '<a class="tree-folder-label" href="#"'
            + ' onclick="' + this.onclick + '"'
            + ' onfocus="' + this.onfocus + '"'
//            + ' ondblclick="' + this.ondblclick + '"'
            + '>'
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
  }
}

TreeFolder.prototype = new TreeNode;
