// menu.js

function initMenuBar(divId, pageId) {
  var menubar = document.getElementById(divId);
  if (menubar && menubar.menu) {
    menubar.menu.menubar = menubar;
    var top = getAbsoluteTop(menubar) + getMenubarBorderWidth();
    var left = getAbsoluteLeft(menubar) + getMenubarBorderWidth();
    var body = document.getElementById(pageId);
    var className = "tobago-menubar-container";
    menubar.menu.htmlElement = document.createElement('div');
    menubar.menu.htmlElement.className = className;
    menubar.menu.htmlElement.style.top = top;
    menubar.menu.htmlElement.style.left = left;
    menubar.menu.htmlElement.innerHTML = menubar.menu.toHtml();
    body.appendChild(menubar.menu.htmlElement);

    initMenuItems(menubar.menu);
    setItemWidth(menubar.menu);
    setItemPositions(menubar.menu);
    setItemsVisible(menubar.menu);
  }
  else {
    PrintDebug("keine menubar mit id ='" + divId + "'gefunden!");
  }
}

function createMenuRoot(id) {
  var menu = new MenuItem();
  menu.id = id + getSubComponentSeparator() + "menuRoot";
  menu.level = 0;
  return menu;
}

function MenuItem(label, action, disabled) {
  this.label = label;
  this.action = action;
  this.disabled = disabled;
  this.subItems = new Array();

  this.addMenuItem = function(menuItem) {
    var index = this.subItems.length;
    this.subItems[index] = menuItem;
    menuItem.parent = this;
    menuItem.id = this.id + getSubComponentSeparator() + index;
    menuItem.index = index;
    menuItem.level = this.level + 1;
  }

  this.toHtml = function() {
    var html = "";
    if (this.level != 0) {
      var onClick = "";
      var debug = false;
      if (this.action) {
        onClick = ' onclick="tobagoMenuItemOnmouseout(this, true) ; ' + this.action + '"';
      }
      if (this.level == 1) {
        onClick = ' onclick="tobagoMenuOpenMenu(this)"';
      }
      if (onClick == "" && this.subItems.length > 0) {
        onClick = ' onclick="tobagoMenuOpenMenu(this)"';
        debug = true;
      }

      html += '<div class="tobago-menu-item"'
          + ' id="' + this.id + '"'
          + ' onmouseover="tobagoMenuItemOnmouseover(this)"'
          + ' onmouseout="tobagoMenuItemOnmouseout(this)"'
          + onClick
          + '>' + this.label + "</div>";
          //PrintDebug("adding menu entry '" + this.label + "'");
    }
    if (this.subItems.length > 0) {
      if (this.level != 0) {
        html += '<div class="tobago-menubar-subitem-container"'
            + ' id="' + this.id + getSubComponentSeparator() + 'items" >';
      }
      for (var i = 0; i< this.subItems.length; i++) {
        html += this.subItems[i].toHtml()
      }
      if (this.level != 0) {
        html += '</div>';
        if (isIE()) {
          html += '<iframe'
              + ' id="' + this.id + getSubComponentSeparator() + 'iframe" >'
              + ' class="tobago-menubar-subitem-iframe" '
              + ' style="display: none; visibility: hidden;"'
              + ' frameborder="0" scrolling="no" ></iframe>'
        }
      }
    }
    return html;
  }

  this.setHover = function() {
    if (! this.disabled) {
      addCssClass(this.htmlElement, "tobago-menu-item-hover");
    }
    if (this.level != 1 || this.parent.menuOpen) {
      this.openSubMenus();
    }
  }

  this.openMenu = function() {
    PrintDebug("open menu on " + this.label );
    if (this.level == 1) {
      this.parent.menuOpen = 1;
      this.setHover();
    }
    else {
      if (this.subItems.length > 0) {
        this.onMouseOver();
      }
    }
  }

  this.openSubMenus = function() {
    if (this.subItemContainer) {
      this.subItemContainer.style.visibility = 'visible';
      if (this.subItemIframe) {
        this.subItemIframe.style.visibility = 'visible';
      }
    }
  }

  this.removeHover = function(mouseOut) {
    if (mouseOut) {
      this.hover = false;
    }
    if (this.level > 0) {
      //PrintDebug("removeHover on " + this.id);
      if (! this.hover && ! this.isHoverChildren()) {
        removeCssClass(this.htmlElement, "tobago-menu-item-hover");
        removeCssClass(this.htmlElement, "tobago-menu-focus");
        if (this.subItemContainer) {
          this.subItemContainer.style.visibility = 'hidden';
          if (this.subItemIframe) {
            this.subItemIframe.style.visibility = 'hidden';
          }
        }
        if (this.level == 1 && ! this.parent.isHoverChildren()) {
          this.parent.menuOpen = 0;
        }
      }
      if (this.parent) {
        this.parent.removeHover(false);
      }
    }
  }

  this.onMouseOver = function() {
    //PrintDebug("onMouseOver " + this.id);
    this.hover = true;
    clearTimeout(this.removeHoverTimer);
    this.hoverTimer
        = setTimeout("tobagoMenuSetHover('" + this.id +"')", getMenuTimeoutHover());
  }
  this.onMouseOut = function(clicked) {
    PrintDebug("onMouseOut " + this.id + "  clicked = " + clicked);
    if (this.hover) {
      clearTimeout(this.removeHoverTimer);
      clearTimeout(this.hoverTimer);
    }
    if (clicked) {
      this.removeHover(true);
    } else {
      this.removeHoverTimer
          = setTimeout("tobagoMenuRemoveHover('" + this.id +"', true)", getMenuTimeoutOut());
    }
  }

  this.isHoverChildren = function() {
    for (var i = 0; i < this.subItems.length; i++) {
       if (this.subItems[i].hover) {
         return true;
       }
       else if (this.subItems[i].isHoverChildren()) {
         return true;
       }
    }
    return false;
  }


  this.keyDown = function() {
    if (this.level == 1) {
     this.nextItem(-1, 1);
    }
    else {
      if (this.parent.nextItem(this.index, 1)) {
        this.hover = false;
      }
    }
  }

  this.keyUp = function() {
    if (this.level == 1) {
     this.nextItem(this.subItems.length, -1);
    }
    else {
      if (this.parent.nextItem(this.index, -1)) {
        this.hover = false;
      }
    }
  }

  this.nextItem = function(start, offset) {
    var i = start + offset;


    while (!(this.subItems[i] && ! this.subItems[i].disabled)) {
      if (i == start) {
        break;
      }
      if (offset > 0) {
        if (i >= this.subItems.length) {
          i = -1;
        }
      }
      else {
        if (i < 0) {
          i = this.subItems.length;
        }
      }
      i += offset;
    }

    var j = this.subItems[i].htmlElement.childNodes.length;
    var span = tobagoMenuGetLabelTag(this.subItems[i].htmlElement.childNodes);
    if (span) {
      this.subItems[i].hover = true;
      span.focus();
      return true;
    }
    return false;
  }

  this.collapse = function() {
    if (this.level < 2) {
      this.onMouseOut(true);
    }
    else if (this.level == 2
        && !(this.subItemContainer
             && this.subItemContainer.style.visibility == 'visible' )) {
      this.onMouseOut(true);
      this.parent.onMouseOut(true);
    }
    else {
      if (this.subItemContainer && this.subItemContainer.style.visibility == 'visible') {
        this.subItemContainer.style.visibility = 'hidden';
      }
      else {
        var aTag = tobagoMenuGetLabelTag(this.parent.htmlElement.childNodes);
        if (aTag) {
          this.parent.hover = true;
          if (this.parent.subItemContainer) {
            this.parent.subItemContainer.style.visibility = 'hidden';
          }
          aTag.focus();
        }
      }
    }
  }

//  this.itemDown = function(start) {
//    var i = start + 1;
//
//
//    while (!(this.subItems[i] && ! this.subItems[i].disabled)) {
//      if (i == start) {
//        break;
//      }
//      if (i == this.subItems.length) {
//        i = -1;
//      }
//      i++;
//    }
//
//    var j = this.subItems[i].htmlElement.childNodes.length;
//    var span;
//    for (var k = 0; k < j; k++) {
//      span = this.subItems[i].htmlElement.childNodes[k];
//      if (span.className.match(/tobago-menubar-item-span/)) {
//        this.subItems[i].hover = true;
//        span.focus();
//        return true;
//      }
//    }
//    return false;
//  }

}

function tobagoMenuGetLabelTag(children) {
  for (var k = 0; k < children.length; k++) {
    if (children[k].className.match(/tobago-menubar-item-span/)) {
      return children[k];
    }
  }
}



function setItemWidth(menu) {

  if (menu.level != 0) {
    if (menu.htmlElement) {

      if (menu.level == 1) {
        menu.htmlElement.style.width = menu.htmlElement.scrollWidth + "px";
      }
      else { // level not 0 or 1

        var width = menu.parent.childWidth;

        if (! width) {
          width = 0;
          var re = new RegExp("(.*" + getSubComponentSeparator() +")\\d$");
          var childIdPrefix = menu.id.match(re)[1];
          var i = 0;
          var childElement = document.getElementById(childIdPrefix + i++);
          while (childElement) {
            //PrintDebug("item " + childElement.id + "  -->" + childElement.scrollWidth);
            width = Math.max(width, childElement.scrollWidth);
            childElement = document.getElementById(childIdPrefix + i++);
          }
         // PrintDebug("das waren " + (i-1) + " items  ---> width wird " + width);
          width += getMenuArrowWidth();
          menu.parent.childWidth = width;
        }
        menu.htmlElement.style.width = width + "px";

      }

    menu.htmlElement.style.overflow = 'hidden';
    }
  }
  for (var i = 0; i < menu.subItems.length; i++) {
    setItemWidth(menu.subItems[i]);
  }
  if (menu.subItemContainer && menu.level != 0) {
    menu.subItemContainer.style.width
        = (menu.childWidth + getSubitemContainerBorderWidthSum()) + "px";
    menu.subItemContainer.style.height = (menu.subItems.length  * getItemHeight()
        + getSubitemContainerBorderWidthSum()) + "px";

    if (menu.subItemIframe) {
      menu.subItemIframe.style.width = menu.subItemContainer.style.width;
      menu.subItemIframe.style.height = menu.subItemContainer.style.height;
    }
  }
}

function setItemPositions(menu) {

  if (menu.level != 0) {
    if (menu.htmlElement) {

      if (menu.level == 1) {
        var itemHeight = getItemHeight(menu);
        menu.htmlElement.style.top = "0px";
        menu.htmlElement.style.height = itemHeight + "px";
        if (menu.subItemContainer) {
          menu.subItemContainer.style.top = itemHeight + "px";
        }
        var left = 0;
        if (menu.index != 0) {
          var neighbour = menu.parent.subItems[menu.index -1];
          var left = neighbour.htmlElement.style.left.replace(/\D/g,"") - 0;
          left += neighbour.htmlElement.style.width.replace(/\D/g,"") - 0;
        }
        menu.htmlElement.style.left = left + "px";
        if (menu.subItemContainer) {
          menu.subItemContainer.style.left = left + "px";
        }  
      }
      else { // level not 0 or 1
        var top = top = (menu.index * getItemHeight());
        var left = 0;
        if (menu.subItemContainer) {
          //if (menu.level == 2) {
          //  top = getItemHeight();
          //}
          if (menu.level != 1) {
            left = menu.parent.childWidth;
          }
          menu.subItemContainer.style.top = top + "px";
          menu.subItemContainer.style.left = left + "px";
        }

        menu.htmlElement.style.top = top + "px";
        menu.htmlElement.style.left = "0px";

      }
      if (menu.subItemIframe) {
        menu.subItemIframe.style.top = menu.subItemContainer.style.top;
        menu.subItemIframe.style.left = menu.subItemContainer.style.left;
      }
    }
  }
  for (var i = 0; i < menu.subItems.length; i++) {
    setItemPositions(menu.subItems[i]);
  }
}

function setItemsVisible(menu) {
  for (var i = 0; i < menu.subItems.length; i++) {
    menu.subItems[i].htmlElement.style.visibility = 'visible';
  }
}

function initMenuItems(menu) {
  menu.htmlElement = document.getElementById(menu.id);
  if (menu.htmlElement) {
    menu.htmlElement.menuItem = menu;
    if (menu.parent && menu.parent.menubar
        && menu.parent.menubar.className.match(/tobago-menubar-page-facet/)) {
      addCssClass(menu.htmlElement, 'tobago-menubar-item-page-facet')
    }
    menu.subItemContainer =
        document.getElementById(menu.id + getSubComponentSeparator() + 'items');
    menu.subItemIframe =
        document.getElementById(menu.id + getSubComponentSeparator() + 'iframe');
    if (menu.subItemIframe) {
      menu.subItemIframe.style.visibility = "hidden";
      menu.subItemIframe.style.position = "absolute";
      menu.subItemIframe.style.border = "0px solid black";
      menu.subItemIframe.style.zIndex
          = tobagoGetRuntimeStyle(menu.htmlElement).zIndex - 1;
    }
  }
  for (var i = 0; i < menu.subItems.length; i++) {
    initMenuItems(menu.subItems[i]);
  }
}


function tobagoMenuItemOnmouseover(element) {
  element.menuItem.onMouseOver()
}
function tobagoMenuItemOnmouseout(element, clicked) {
  element.menuItem.onMouseOut(clicked)
}

function tobagoMenuOpenMenu(element) {
  element.menuItem.openMenu();
}

function tobagoMenuSetHover(id) {
  document.getElementById(id).menuItem.setHover();
}
function tobagoMenuRemoveHover(id, mouseOut) {
  document.getElementById(id).menuItem.removeHover(mouseOut);
}



function getMenuTimeoutHover() {
  return 100;
}
function getMenuTimeoutOut() {
  return 150;
}

function getMenubarBorderWidth() {
  return 2;
}

function getSubitemContainerBorderWidthSum() {
  return 4; // border * 2
}

function getItemHeight(menu) {
  if (menu && menu.level == 1) {
    if (menu.parent.menubar.className.match(/tobago-menubar-page-facet/)) {
      return 23;
    }
    else {
      return 20;
    }
  }
  else {
    return 20;
  }
}

function getMenuArrowWidth() {
  return 15;
}

function menuCheckToggle(id) {
  var element = document.getElementById(id);
  var form = document.forms[0];
  if (element) {
    //PrintDebug("remove  " + id);
    form.removeChild(element);
  }
  else {
    //PrintDebug("adding " + id);
    element = document.createElement('INPUT');
    element.type = 'hidden';
    element.name = id;
    element.id = id;
    element.value = 'true';
    form.appendChild(element);
  }
}

function menuSetRadioValue(id, value) {
  var element = document.getElementById(id);
  if (! element) {
    element = document.createElement('INPUT');
    element.type = 'hidden';
    element.name = id;
    element.id = id;
    document.forms[0].appendChild(element);
  }
  element.value = value;
}

function tobagoMenuFocus(event) {
  PrintDebug("tobagoMenuFocus" );
  if (! event) {
    event = window.event;
  }
  var element = getActiveElement(event);
  element.parentNode.menuItem.hover = true;
  addCssClass(element.parentNode, "tobago-menu-focus");
}
function tobagoMenuBlur(event) {
  PrintDebug("tobagoMenuBlur" );

  if (! event) {
    event = window.event;
  }
  var element = getActiveElement(event);
  element.parentNode.menuItem.onMouseOut();
}

function tobagoMenuDown(event) {
  var element = getActiveElement(event);
  element.parentNode.menuItem.keyDown();
  stopEventPropagation(event);
}
function tobagoMenuUp(event) {
  var element = getActiveElement(event);
  element.parentNode.menuItem.keyUp();
  stopEventPropagation(event);
}

function stopEventPropagation(event) {
  if (event.stopPropagation) {
    PrintDebug("event.stopPropagation() " + event.cancelable);
    event.preventDefault();
    event.stopPropagation();
  }
  else {
    PrintDebug("event.cancelBubble = true");
    event.cancelBubble = true;
  }
}

function tobagoMenuItemCollapse(event) {
  var element = getActiveElement(event);
  element.parentNode.menuItem.collapse();
}


function tobagoMenuKeyDown(event) {
  if (! event) {
    event = window.event;
  }

  var code;
  if (event.which) {
    code = event.which;
  } else {
    code = event.keyCode;
  }

  if (code == 27) {  // ESC
    tobagoMenuItemCollapse(event);
  }
  else if (code == 38) {
    tobagoMenuUp(event);
  }
  else if (code == 40) {
    tobagoMenuDown(event);
  }

  PrintDebug("keyevent catched : " + code);
}
