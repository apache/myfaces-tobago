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

function initMenuPopUp(divId, pageId, type) {
  initMenuComponents(divId, pageId, type);
}
function initMenuBar(divId, pageId) {
  initMenuComponents(divId, pageId, false);
}
function initMenuComponents(divId, pageId, popup) {
  var menubar = document.getElementById(divId);
  if (menubar && menubar.menu) {
    menubar.menu.menubar = menubar;
    var top = getAbsoluteTop(menubar) + getMenubarBorderWidth();
    var left = getAbsoluteLeft(menubar) + getMenubarBorderWidth();
    if (popup) {
      left = left + getElementWidth(menubar) - getPopupMenuWidth();
      menubar.menu.popup = popup;
      //PrintDebug("popupType = " + popup);
    }
    var body = document.getElementById(pageId);
    var className = "tobago-menuBar-container";
    if (popup) {
      className += " tobago-menuBar-container-" + popup;
    }
    menubar.menu.htmlElement = document.createElement('div');
    menubar.menu.htmlElement.className = className;
    menubar.menu.htmlElement.style.top = top;
    menubar.menu.htmlElement.style.left = left;
    menubar.menu.htmlElement.style.top = 0;
    menubar.menu.htmlElement.style.left = 0;
    
    
//    menubar.menu.htmlElement.innerHTML = menubar.menu.toHtml();
//    body.appendChild(menubar.menu.htmlElement);
//  //menubar.appendChild(menubar.menu.htmlElement);

    menubar.menu.htmlElement.innerHTML = menubar.menu.toHtml(false);
    menubar.appendChild(menubar.menu.htmlElement);
    var subitems = createSubmenus(menubar.menu);
    body.appendChild(subitems);
    

    
    initMenuItems(menubar.menu);
    setItemWidth(menubar.menu);
    setItemPositions(menubar.menu);
    if (popup) {
      adjustPopupImage(menubar.menu);
    }
    setItemsVisible(menubar.menu);
  }
  else {
    PrintDebug("keine menubar mit id ='" + divId + "'gefunden!");
  }
}

function createSubmenus(menu) {                   
  var htmlElement = document.createElement('div');
  htmlElement.className = "tobago-menuBar-submenuroot";
  htmlElement.innerHTML = menu.toHtml(true);
  return htmlElement;
}

function createMenuRoot(id) {
  var menu = new MenuItem();
  menu.id = id + getSubComponentSeparator() + Tobago.Menu.MENU_ROOT_ID;
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
  };

  this.toHtml = function(createSubItems) {
    var html = "";
//    PrintDebug("toHtml() for"  + this.id + " l=" + this.level);
    if (this.level > 1 || (this.level == 1 && ! createSubItems)) {
//      PrintDebug("create menuitem" + this.id);
      var onClick = "";
      if (this.action) {
        onClick = ' onclick="tobagoMenuItemOnmouseout(this, true) ; ' + this.action + '"';
      }
      if (this.level == 1 || this.subItems.length > 0) {
        onClick = ' onclick="tobagoMenuOpenMenu(this, event)"';
      }



      html += '<div class="tobago-menu-item"'
          + ' id="' + this.id + '"';
      if (! this.disabled) {
        html += ' onmouseover="tobagoMenuItemOnmouseover(this)"'
            + ' onmouseout="tobagoMenuItemOnmouseout(this)"'
/*            + ' onfocus="tobagoMenuItemOnfocus(this)"'
            + ' onblur="tobagoMenuItemOnblur(this)"' */
            + onClick;
      }
      html += '>' + this.label + "</div>";
          //PrintDebug("adding menu entry '" + this.label + "'");
    }
    if (this.level == 0 || (createSubItems && this.subItems.length > 0)) {
//      PrintDebug("create subitems " + this.id);

      if (this.level != 0) {
        html += '<div class="tobago-menuBar-subitem-container"'
            + ' id="' + this.id + getSubComponentSeparator() + 'items" >';
      }
      for (var i = 0; i< this.subItems.length; i++) {
        html += this.subItems[i].toHtml(createSubItems);
      }
      if (this.level != 0) {
        html += '</div>';
        if (isIE()) {
          html += '<iframe'
              + ' id="' + this.id + getSubComponentSeparator() + 'iframe"'
              + ' class="tobago-menuBar-subitem-iframe" '
              + ' style="display: none; visibility: hidden;"'
              + ' frameborder="0" scrolling="no" ></iframe>';
        }
      }
    }
    return html;
  };

  this.openSubMenus = function() {
    if (! this.subItemContainer) {     
      return;
    }
    
    var width = this.subItemContainerStyleWidth.replace(/\D/g,"") - 0;
    var height = this.subItemContainerStyleHeight.replace(/\D/g,"") - 0;

    var containerParent;
    var leftOffset = 0;
    if (this.menuButton) {
      containerParent = this.menuButton;
      leftOffset = Tobago.Menu.toolbarLeftOffset;
    } else {
      containerParent = this.htmlElement;
    }
    var parentLeft = getAbsoluteLeft(containerParent) + leftOffset;
    var parentTop = getAbsoluteTop(containerParent) ;
    var parentWidth = this.htmlElement.style.width.replace(/\D/g,"") - 0;
    var parentHeight = this.htmlElement.style.height.replace(/\D/g,"") - 0;
    
    
    var innerLeft = getBrowserInnerLeft();
    var innerTop = getBrowserInnerTop();
    var innerWidth = getBrowserInnerWidth();
    var innerHeight = getBrowserInnerHeight();
    var innerRight = innerLeft + innerWidth;
    var innerBottom = innerTop + innerHeight;

    if (this.level == 1) {
      var containerRight = parentLeft + width;
      if (containerRight <= innerRight) {
//        this.subItemContainer.style.left = this.htmlElement.style.left;
        this.subItemContainer.style.left = parentLeft + "px";
      }
      else {
//        this.subItemContainer.style.left = this.htmlElement.style.left.replace(/\D/g,"") - (containerRight - innerRight) + "px";
        this.subItemContainer.style.left = parentLeft - (containerRight - innerRight) + "px";
      }
      var itemHeight = containerParent.clientHeight;
      this.subItemContainer.style.top = parentTop + itemHeight + "px";
    }
    else if (this.level > 1) {
      var containerRight = parentLeft + parentWidth + width;
      var left = 0;
      if (containerRight <= innerRight) {
        left = this.parent.childWidth;
      }
      else {
        left = "-" + this.childWidth;
      }
      this.subItemContainer.style.left = left + "px";
    
      var containerBottom = parentTop + height;
      if (containerBottom <= innerBottom) {
        this.subItemContainer.style.top = this.htmlElement.style.top;
      }
      else {
        this.subItemContainer.style.top = (innerBottom - containerBottom) + "px";
      }
      
    }
    
    this.subItemContainer.style.width = this.subItemContainerStyleWidth;
    this.subItemContainer.style.height = this.subItemContainerStyleHeight;

    this.setupIframe();
//    this.setSubMenuContainerVisibility("visible");
//    timing problem when call this directly ??
//    calling via 'setTimeout()' is not nice, but resolves the problem
    setTimeout('tobagoSetSubMenuContainerVisible("' + this.id +  '")', 0);
  };
  this.hideSubMenus = function() {
    this.setSubMenuContainerVisibility("hidden");
  };
  this.setSubMenuContainerVisibility = function(style) {
    if (this.subItemContainer) {
      this.subItemContainer.style.visibility = style;
      if (this.subItemIframe) {
        this.subItemIframe.style.visibility = style;
      }
    }
  };


  this.openMenu = function() {
    this.focusLabelTag();
  };

  this.onMouseOver = function() {
    this.mouseOver = true;
    //PrintDebug("onMouseOver " + this.id + " level :" + this.level);
    clearTimeout(this.onBlurTimer);

    if (this.level == 1) {
      if (! this.isNeighbourOpen()) {
        addCssClass(this.htmlElement, "tobago-menu-item-hover");
      }
      else {
        this.focusLabelTag();
        this.openSubMenus();
      }
    }
    else {
      this.focusLabelTag();
      this.hoverTimer = setTimeout("tobagoOpenSubMenus('" + this.id + "')",
          getOpenSubMenusTimeout());
    }
  };

  this.onMouseOut = function(clicked) {
    this.mouseOver = false;
    //PrintDebug("onMouseOut " + this.id + " clicked = " + clicked);
    clearTimeout(this.hoverTimer);
    if (clicked) {
      //this.blurLabelTag();
      this.focus = false;
      this.focusLost();
    }
    else {
      removeCssClass(this.htmlElement, "tobago-menu-item-hover");
    }
  };

  this.onFocus = function() {
    this.focus = true;
    addCssClass(this.htmlElement, "tobago-menu-item-focus");
    if (this.menuButton) {
      addCssClass(this.menuButton, "tobago-toolBar-button-menu-focus");
    }
    this.openSubMenus();
    
    if (this.action && window.event && window.event.altKey) {
    // focus came via alt-<accessKey> : ie needs click())
      this.htmlElement.click();
    }
  };
  this.onBlur = function() {
    //PrintDebug("onBlur " + this.id);
    this.focus = false;
    this.onBlurTimer = setTimeout("tobagoFocusLost('" + this.id + "')",
        getFocusLostTimeout());
  };

  this.focusLost = function() {
    //PrintDebug("focusLost " + this.id);
    if (this.level > 0
        && (!this.mouseOver)
        && !(this.level != 1 && this.focus)
        &&  ! this.childHasFocus()) {
      this.hideSubMenus();
      removeCssClass(this.htmlElement, "tobago-menu-item-focus");
      if (this.menuButton) {
        removeCssClass(this.menuButton, "tobago-toolBar-button-menu-focus");
      }
      this.parent.focusLost();
    }
  };

  this.isExpanded = function() {
    return this.subItemContainer
        && this.subItemContainer.style.visibility.match(/visible/);
  };

  this.focusLabelTag = function() {
    //PrintDebug("setze Focus " + this.id);
    var element = this.getLabelTag();
    if (element) {
      try {
      element.focus();
      } catch (ex) {
        // ignore
      }
    }
  };
  this.blurLabelTag = function() {
    //PrintDebug("entferne Focus " + this.id);
    var element = this.getLabelTag();
    if (element) {
      element.blur();
    }
  };

  this.childHasFocus = function() {
    for (var i = 0; i < this.subItems.length; i++) {
       if (this.subItems[i].focus) {
         return true;
       }
       else if (this.subItems[i].childHasFocus()) {
         return true;
       }
    }
    return false;
  };


  this.isNeighbourOpen = function() {
    for (var i = 0; i < this.parent.subItems.length; i++) {
      if (this.parent.subItems[i].isExpanded()) {
        return true;
      }
    }
    return false;
  }



  // #########################################################  key access

  this.keyDown = function() {
    if (this.level == 1) {
      this.openSubMenus();
      this.nextItem(-1, 1);
    }
    else {
      if (this.parent.nextItem(this.index, 1)) {
        this.hover = false;
      }
    }
  };

  this.keyUp = function() {
    if (this.level == 1) {
      this.openSubMenus();
      this.nextItem(this.subItems.length, -1);
    }
    else {
      if (this.parent.nextItem(this.index, -1)) {
        this.hover = false;
      }
    }
  };

  this.keyLeft = function() {
    if (this.level == 1) {
      var next = this.parent.nextItem(this.index, -1);
      if (next && next.htmlElement.id != this.htmlElement.id) { // menu has changed
        this.hover = false;
        next.openSubMenus();
      }
    }
    else if (this.level == 2) {
      if (this.subItemContainer && this.isExpanded()) {
        this.hideSubMenus();
      } else {
        var next = this.parent.parent.nextItem(this.parent.index, -1);
        if (next && next.htmlElement.id != this.parent.htmlElement.id) { // menu has changed
          this.hover = false;
          next.openSubMenus();
        }
      }
    }
    else { // level > 2
      var next = this.parent.getLabelTag();
      this.parent.hover = true;
      this.parent.hideSubMenus();
      next.focus();
      this.hover = false;
    }
  };

  this.keyRight = function() {
    if (this.level == 1) {
      var next = this.parent.nextItem(this.index, 1);
      if (next && next.htmlElement.id != this.htmlElement.id) { // menu has changed
        this.hover = false;
        next.openSubMenus();
      }
    }
    else if (this.level > 1) {
      if (this.subItemContainer) {
        this.openSubMenus();
        this.hover = false;
        var next = this.nextItem(-1, 1);
      } else {
        var parent = this.parent;
        while (parent.level != 1) {
          parent = parent.parent;
        }
        var next = parent.parent.nextItem(parent.index, 1);
        if (next && next.htmlElement.id != parent.htmlElement.id) { // menu has changed
          parent.hover = false;
          next.openSubMenus();
        }
      }
    }
  };

  this.nextItem = function(start, offset) {
    var i = start + offset;

    while (!(this.subItems[i] && ! this.subItems[i].disabled) && i != start) {
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
    var span = this.subItems[i].getLabelTag();
    if (span) {
      this.subItems[i].hover = true;
      span.focus();
      return this.subItems[i];
    }
    return false;
  };

  this.collapse = function() {
    //PrintDebug("collapse " + this.id);
    if (this.level < 2) {
      var aTag = this.getLabelTag();
      if (aTag) {
        aTag.blur();
      }
      //this.onMouseOut(true);
    }
    else if (this.level == 2
        && !(this.subItemContainer && this.isExpanded() )) {
      var aTag = this.getLabelTag();
      if (aTag) {
        aTag.blur();
      }
      //this.onMouseOut(true);
      //this.parent.onMouseOut(true);
    }
    else {
      if (this.isExpanded()) {
        this.hideSubMenus();
      }
      else {
        var aTag = this.parent.getLabelTag();
        if (aTag) {
          this.parent.hover = true;
          aTag.focus();
        }
      }
    }
  };
  
  this.getLabelTag = function() {
    var children = this.htmlElement.childNodes;
    for (var k = 0; k < children.length; k++) {
      if (children[k].className.match(/tobago-menuBar-item-span/)) {
        return children[k];
      }
    }
  };
  
  this.setSubitemArrowImage = function(image) {
    this.subitemArrowImage = image;
  };

  this.getSubitemArrowImage = function() {
    if (! this.subitemArrowImage) {
      if (this.parent) {
        this.subitemArrowImage = this.parent.getSubitemArrowImage();
      }
    }
    return this.subitemArrowImage;
  };
  
  this.addSubitemArrowImage = function() {
    if (this.level > 1 && this.subItems && this.subItems.length > 0) {
      var html = this.htmlElement.innerHTML;
      html += '<img class="tobago-menu-subitem-arrow" src="';
      html += this.getSubitemArrowImage() + '" />';
      this.htmlElement.innerHTML = html;
    }
  };
  
  this.setupIframe = function() {
    if (this.subItemIframe) {
      this.subItemIframe.style.width = this.subItemContainer.style.width;
      this.subItemIframe.style.height = this.subItemContainer.style.height;
      this.subItemIframe.style.top = this.subItemContainer.style.top;
      this.subItemIframe.style.left = this.subItemContainer.style.left;
    }
  };

  this.setMenuButton = function(button) {
    this.menuButton = button;
  };
}



function adjustPopupImage(menu) {
  if (menu.subItems && menu.subItems.length > 0) {
    var img = menu.subItems[0].htmlElement.childNodes[0];
    if (img) {
      img.style.top = getPopupImageTop(menu.popup);
    }
    else {
      PrintDebug("kein IMG im popup");
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
            //PrintDebug("item " + childElement.id + "  -->" + childElement.scrollWidth) ;//+ ":::" + childElement.innerHTML);
            width = Math.max(width, childElement.scrollWidth);
            childElement = document.getElementById(childIdPrefix + i++);
          }
          //PrintDebug("das waren " + (i-1) + " items  ---> width wird " + width);
          width += getMenuArrowWidth();
          menu.parent.childWidth = width;
        }
        
        menu.addSubitemArrowImage();
        
        menu.htmlElement.style.width = width + "px";

      }

    menu.htmlElement.style.overflow = 'hidden';
    }
  }
  for (var i = 0; i < menu.subItems.length; i++) {
    setItemWidth(menu.subItems[i]);
  }
  if (menu.subItemContainer && menu.level != 0) {
    menu.subItemContainerStyleWidth
        = (menu.childWidth + getSubitemContainerBorderWidthSum()) + "px";
    menu.subItemContainerStyleHeight = (menu.subItems.length  * getItemHeight()
        + getSubitemContainerBorderWidthSum()) + "px";

    menu.subItemContainer.style.width = "0px";
    menu.subItemContainer.style.height = "0px";

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
        var top = 0;
        if (menu.parent.popup) {
          eval("top = get" + menu.parent.popup + "MenuTopOffset()");
        }
        menu.htmlElement.style.top = top +"px";
        menu.htmlElement.style.height = itemHeight + "px";
        if (menu.subItemContainer) {
          menu.subItemContainer.style.top = (itemHeight + top) + "px";
        }
        var left = 0;
        if (menu.index != 0) {
          var neighbour = menu.parent.subItems[menu.index -1];
          var left = neighbour.htmlElement.style.left.replace(/\D/g,"") - 0;
          left += neighbour.htmlElement.style.width.replace(/\D/g,"") - 0;
        }
        menu.htmlElement.style.left = left + "px";
        if (menu.subItemContainer) {
//          menu.subItemContainer.style.left = left + "px";
          menu.subItemContainer.style.left = "0px";
        }
        menu.htmlElement.style.zIndex = "999";
      }
      else { // level not 0 or 1
        var top = (menu.index * getItemHeight());
        var left = 0;
        if (menu.level == 2 && menu.parent.parent.popup) {
          if (menu.parent.parent.popup == "ToolbarButton") {
            left = getPopupMenuWidth() - getElementWidth(menu.parent.parent.menubar);
          }  
          if (menu.parent.subItemContainer) {
            menu.parent.subItemContainer.style.left = left + "px";
            if (menu.parent.subItemIframe) {
              menu.parent.subItemIframe.style.left
                  = menu.parent.subItemContainer.style.left;
            }
          }
        }
        if (menu.subItemContainer) {
          //if (menu.level == 2) {
          //  top = getItemHeight();
          //}
          /*
          if (menu.level != 1) {
            left = menu.parent.childWidth;
          }
          menu.subItemContainer.style.top = top + "px";
          menu.subItemContainer.style.left = left + "px";*/
          menu.subItemContainer.style.top = "0px";
          menu.subItemContainer.style.left = "-" + menu.parent.childWidth + "px";
        }

        menu.htmlElement.style.top = top + "px";
        menu.htmlElement.style.left = "0px";

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
        && menu.parent.menubar.className.match(/tobago-menuBar-page-facet/)) {
      addCssClass(menu.htmlElement, 'tobago-menuBar-item-page-facet');
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
  return 30;
}

function getMenubarBorderWidth() {
  return 2;
}

function getSubitemContainerBorderWidthSum() {
  return 4; // border * 2
}

function getItemHeight(menu) {
  if (menu && menu.level == 1) {
    if (menu.parent.popup) {
      if (menu.parent.popup == "ToolbarButton") {
        return 20;
      }
      else if (menu.parent.popup == "SheetSelector") {
       return 16;
      }
    }

    if (menu.parent.menubar.className.match(/tobago-menuBar-page-facet/)) {
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
  return 20;
}

function getPopupMenuWidth() {
  return 21;
}

function getPopupImageTop(popup) {
  if (popup == "ToolBarButton") {
    return "2px";
  }
  else if (popup == "SheetSelector") {
    return "0px";
  }
  else {
    PrintDebug("unbekanter Popup Typ :" + popup);
    return "0px";
  }
}

function getToolBarButtonMenuTopOffset() {
  return -1;
}

function getSheetSelectorMenuTopOffset() {
  return -1;
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
  //("tobagoMenuFocus" );
  if (! event) {
    event = window.event;
  }
  var element = getActiveElement(event);
  element.parentNode.menuItem.onFocus();
}
function tobagoMenuBlur(event) {
  //PrintDebug("tobagoMenuBlur" );

  if (! event) {
    event = window.event;
  }
  var element = getActiveElement(event);
  element.parentNode.menuItem.onBlur();
}

function tobagoMenuDown(event) {
  var element = getActiveElement(event);
  element.parentNode.menuItem.keyDown();
}
function tobagoMenuUp(event) {
  var element = getActiveElement(event);
  element.parentNode.menuItem.keyUp();
}
function tobagoMenuLeft(event) {
  var element = getActiveElement(event);
  element.parentNode.menuItem.keyLeft();
}
function tobagoMenuRight(event) {
  var element = getActiveElement(event);
  element.parentNode.menuItem.keyRight();
}

function stopEventPropagation(event) {
  if (event.stopPropagation) {
    //PrintDebug("event.stopPropagation() " + event.cancelable);
    event.preventDefault();
    event.stopPropagation();
  }
  else {
    //PrintDebug("event.cancelBubble = true");
    event.cancelBubble = true;
  }
}

function tobagoMenuItemCollapse(event) {
  var element = getActiveElement(event);
  element.parentNode.menuItem.collapse();
}



// mozilla can't cancel default action at keydown event
// ie can't see up/down/... in keypress event
// so both handlers are installed but only one should do the action
function tobagoMenuKeyPress(event) {
  if (event.stopPropagation) { // mozilla event
    tobagoMenuHandelKey(event);
  }
}

function tobagoMenuKeyDown(event) {
  if (!event || !event.stopPropagation) { // ! mozilla event
    tobagoMenuHandelKey(window.event);
  }
}

function tobagoMenuHandelKey(event) {
  var cancel;
  var code;
  if (event.which) {
    code = event.which;
  } else {
    code = event.keyCode;
  }

  if (code == 27) {  // ESC
    tobagoMenuItemCollapse(event);
  }
  else if (code == 37) { // left
    tobagoMenuLeft(event);
    cancel = true;
  }
  else if (code == 38) { // up
    tobagoMenuUp(event);
    cancel = true;
  }
  else if (code == 39) { // right
    tobagoMenuRight(event);
    cancel = true;
  }
  else if (code == 40) { // down
    tobagoMenuDown(event);
    cancel = true;
  }

  if (cancel) {
    event.returnValue = false;
    event.cancelBubble = true;
    if (event.preventDefault) {
      event.preventDefault();
    }
    if (event.stopPropagation) {
      event.stopPropagation();
    }
  }
}



// ----------------------------------------------------------------------------
function getFocusLostTimeout() {
  return 100;
}
function getOpenSubMenusTimeout() {
  return 100;
}

function tobagoOpenSubMenus(id) {
  document.getElementById(id).menuItem.openSubMenus();
}
function tobagoFocusLost(id) {
  document.getElementById(id).menuItem.focusLost();
}

function tobagoMenuItemOnmouseover(element) {
  element.menuItem.onMouseOver();
}
function tobagoMenuItemOnmouseout(element, clicked) {
  element.menuItem.onMouseOut(clicked);
}
/*
function tobagoMenuItemOnfocus(element) {
  PrintDebug("tobagoMenuItemOnfocus " + element.id);
  element.menuItem.onFocus();
}
function tobagoMenuItemOnblur(element) {
  PrintDebug("tobagoMenuItemOnblur " + element.id);
  element.menuItem.onBlur();
}
*/
function tobagoMenuOpenMenu(element, event) {
  if (event) {
    stopEventPropagation(event);
  }
  element.menuItem.openMenu();
}
function tobagoButtonOpenMenu(button, idPrefix) {
  var menu = $(idPrefix + Tobago.subComponentSeparator
      + Tobago.Menu.MENU_ROOT_ID + Tobago.subComponentSeparator + 0);
  if (menu) {
    menu.menuItem.setMenuButton(button);
    tobagoMenuOpenMenu(menu);
  }
}

function tobagoSetSubMenuContainerVisible(id){
  document.getElementById(id).menuItem.setSubMenuContainerVisibility("visible");
}
