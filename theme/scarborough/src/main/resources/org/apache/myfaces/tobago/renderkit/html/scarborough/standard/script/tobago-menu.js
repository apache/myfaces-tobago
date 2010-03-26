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

function initMenuPopUp(divId, pageId, type) {
  initMenuComponents(divId, pageId, type);
}
function initMenuBar(divId, pageId) {
  initMenuComponents(divId, pageId, false);
}
function initMenuComponents(divId, pageId, popup) {
  var menubar = document.getElementById(divId);
  if (menubar && menubar.menu) {
    if (! menubar.id) {
      menubar.id = Tobago.createHtmlId();
    }
    menubar.menu.menubarId = menubar.id;
    var top = Tobago.getAbsoluteTop(menubar) + getMenubarBorderWidth();
    var left = Tobago.getAbsoluteLeft(menubar) + getMenubarBorderWidth();
    if (popup) {
      left = left + menubar.scrollWidth - getPopupMenuWidth();
      menubar.menu.popup = popup;
      //LOG.debug("popupType = " + popup);
    }
    var body = document.getElementById(pageId);
    var className = "tobago-menuBar-container";
    if (popup) {
      className += " tobago-menuBar-container-" + popup;
    }
    var htmlElement = document.createElement('div');
    htmlElement.id = Tobago.createHtmlId();
    htmlElement.className = className;
    htmlElement.style.top = top;
    htmlElement.style.left = left;
    htmlElement.style.top = 0;
    htmlElement.style.left = 0;
    
    
//    menubar.menu.htmlElement.innerHTML = menubar.menu.toHtml();
//    body.appendChild(menubar.menu.htmlElement);
//  //menubar.appendChild(menubar.menu.htmlElement);

    htmlElement.innerHTML = menubar.menu.toHtml(false);
    if (menubar.firstChild) {
      menubar.removeChild(menubar.firstChild);
    }
    menubar.appendChild(htmlElement);

    createSubmenus(menubar.menu, body);

    initMenuItems(menubar.menu);
    setItemWidth(menubar.menu);
    setItemPositions(menubar.menu);
    if (popup) {
      adjustPopupImage(menubar.menu);
    }
    setItemsVisible(menubar.menu);
  }
  else {
    LOG.debug("keine menubar mit id ='" + divId + "'gefunden!");
  }
}

function createSubmenus(menu, body) {
  var id = menu.id + Tobago.SUB_COMPONENT_SEP + "submenuroot";
  var htmlElement = Tobago.element(id);
  if (!htmlElement) {
    htmlElement = document.createElement('div');
    htmlElement.className = "tobago-menuBar-submenuroot";
    htmlElement.id = id;
    body.appendChild(htmlElement);
  }

  htmlElement.innerHTML = menu.toHtml(true);
  return htmlElement;
}

function createMenuRoot(id) {
  var menu = new Tobago.Menu.Item();
  menu.id = id + Tobago.SUB_COMPONENT_SEP + Tobago.Menu.MENU_ROOT_ID;
  menu.level = 0;
  Tobago.addJsObject(menu);
  return menu;
}

Tobago.Menu.Item = function(label, action, disabled, separator) {
  this.label = label;
  this.action = action;
  this.disabled = disabled;
  this.subItems = new Array();
  this.separator = separator;
};

Tobago.Menu.Item.prototype.addMenuItem = function(menuItem) {
    var index = this.subItems.length;
    this.subItems[index] = menuItem;
    menuItem.parent = this;
    menuItem.id = this.id + Tobago.SUB_COMPONENT_SEP + index;
    menuItem.index = index;
    menuItem.level = this.level + 1;
  };

Tobago.Menu.Item.prototype.toHtml = function(createSubItems) {
    var html = "";
//    LOG.debug("toHtml() for"  + this.id + " l=" + this.level);
    if (this.level > 1 || (this.level == 1 && ! createSubItems)) {
//      LOG.debug("create menuitem" + this.id);
      var onClick = "";
      if (this.action) {
        onClick = ' onclick="tobagoMenuItemOnmouseout(this, true) ; ' + this.action + '"';
      }
      if (this.level == 1 || this.subItems.length > 0) {
        onClick = ' onclick="tobagoMenuOpenMenu(this, event)"';
      }

      var itemStyle = "tobago-menu-item";
      if (this.separator) {
        itemStyle += "-separator";
      }
      html += '<div class="' + itemStyle + '"'
          + ' id="' + this.id + '"';
      if (! this.disabled) {
        html += ' onmouseover="tobagoMenuItemOnmouseover(this)"'
            + ' onmouseout="tobagoMenuItemOnmouseout(this)"'
/*            + ' onfocus="tobagoMenuItemOnfocus(this)"'
            + ' onblur="tobagoMenuItemOnblur(this)"' */
            + onClick;
      }
      html += '>' + this.label + "</div>";
          //LOG.debug("adding menu entry '" + this.label + "'");
    }
    if (this.level == 0 || (createSubItems && this.subItems.length > 0)) {
//      LOG.debug("create subitems " + this.id);

      if (this.level != 0) {
        html += '<div class="tobago-menuBar-subitem-container"'
            + ' id="' + this.id + Tobago.SUB_COMPONENT_SEP + 'items" >';
      }
      for (var i = 0; i< this.subItems.length; i++) {
        html += this.subItems[i].toHtml(createSubItems);
      }
      if (this.level != 0) {
        html += '</div>';
        if (document.all) {
          html += '<iframe'
              + ' id="' + this.id + Tobago.SUB_COMPONENT_SEP + 'iframe"'
              + ' class="tobago-menuBar-subitem-iframe" '
              + ' src="' + Tobago.contextPath.value  + '/org/apache/myfaces/tobago/renderkit/html/standard/blank.html" '
              //+ ' style="display: none; "'
              + ' style="visibility: hidden;"'
              + ' frameborder="0" scrolling="no" ></iframe>';
        }
      }
    }
    return html;
  };

Tobago.Menu.Item.prototype.openSubMenus = function() {
    if (! this.subItemContainerId) {
      return;
    }

    var htmlElement = Tobago.element(this.id);
    var subItemContainer = Tobago.element(this.subItemContainerId);

    var width = this.subItemContainerStyleWidth.replace(/\D/g,"") - 0;
    var height = this.subItemContainerStyleHeight.replace(/\D/g,"") - 0;

    var containerParent;
    var leftOffset = 0;
    if (this.menuButton) {
      containerParent = this.menuButton;
      leftOffset = Tobago.Config.get("Menu", "toolbarLeftOffset");
    } else {
      containerParent = htmlElement;
    }
    var parentLeft = Tobago.getAbsoluteLeft(containerParent) + leftOffset;
    var parentTop = Tobago.getAbsoluteTop(containerParent) ;
    var parentWidth = htmlElement.style.width.replace(/\D/g,"") - 0;
    var parentHeight = htmlElement.style.height.replace(/\D/g,"") - 0;
    
    
    var innerLeft = Tobago.getBrowserInnerLeft();
    var innerTop = Tobago.getBrowserInnerTop();
    var innerWidth = Tobago.getBrowserInnerWidth();
    var innerHeight = Tobago.getBrowserInnerHeight();
    var innerRight = innerLeft + innerWidth;
    var innerBottom = innerTop + innerHeight;

    if (this.level == 1) {
      var containerRight = parentLeft + width;
      if (containerRight <= innerRight) {
//        subItemContainer.style.left = htmlElement.style.left;
        subItemContainer.style.left = parentLeft + "px";
      }
      else {
//        subItemContainer.style.left = htmlElement.style.left.replace(/\D/g,"") - (containerRight - innerRight) + "px";
        subItemContainer.style.left = parentLeft - (containerRight - innerRight) + "px";
      }
      var itemHeight = containerParent.clientHeight;
      subItemContainer.style.top = parentTop + itemHeight + "px";
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
      subItemContainer.style.left = left + "px";
    
      var containerBottom = parentTop + height;
      if (containerBottom <= innerBottom) {
        subItemContainer.style.top = htmlElement.style.top;
      }
      else {
        subItemContainer.style.top = (innerBottom - containerBottom) + "px";
      }
      
    }
    
    subItemContainer.style.width = this.subItemContainerStyleWidth;
    subItemContainer.style.height = this.subItemContainerStyleHeight;

    this.setupIframe(subItemContainer);
//    this.setSubMenuContainerVisibility("visible");
//    timing problem when call this directly ??
//    calling via 'setTimeout()' is not nice, but resolves the problem
    setTimeout('tobagoSetSubMenuContainerVisible("' + this.id +  '")', 0);
  };

Tobago.Menu.Item.prototype.hideSubMenus = function() {
    this.setSubMenuContainerVisibility("hidden");
  };

Tobago.Menu.Item.prototype.setSubMenuContainerVisibility = function(style) {
    if (this.subItemContainerId) {
      Tobago.element(this.subItemContainerId).style.visibility = style;
      if (this.subItemIframeId) {
        Tobago.element(this.subItemIframeId).style.visibility = style;
      }
    }
  };


Tobago.Menu.Item.prototype.openMenu = function() {
    this.focusLabelTag();
  };

Tobago.Menu.Item.prototype.onMouseOver = function() {

  var div = Tobago.element(this.id);
  // e. g. disabled by a popup
  for (var i = 0; i < div.childNodes.length; i++) {
    var child = div.childNodes[i];
    if (child.tagName == "A" && child.disabled){
      return;
    }
  }

    this.mouseOver = true;
    //LOG.debug("onMouseOver " + this.id + " level :" + this.level);
    clearTimeout(this.onBlurTimer);

    if (this.level == 1) {
      if (! this.isNeighbourOpen()) {
        Tobago.addCssClass(this.id, "tobago-menu-item-hover");
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

Tobago.Menu.Item.prototype.onMouseOut = function(clicked) {
    this.mouseOver = false;
    //LOG.debug("onMouseOut " + this.id + " clicked = " + clicked);
    clearTimeout(this.hoverTimer);
    if (clicked) {
      //this.blurLabelTag();
      this.focus = false;
      this.focusLost();
    }
    else {
      Tobago.removeCssClass(this.id, "tobago-menu-item-hover");
    }
  };

Tobago.Menu.Item.prototype.onFocus = function() {

  var div = Tobago.element(this.id);
  // e. g. disabled by a popup
  for (var i = 0; i < div.childNodes.length; i++) {
    var child = div.childNodes[i];
    if (child.tagName == "A" && child.disabled){
      return;
    }
  }

    this.focus = true;
    Tobago.addCssClass(this.id, "tobago-menu-item-focus");
    if (this.menuButton) {
      Tobago.addCssClass(this.menuButton, "tobago-toolBar-button-menu-focus");
    }
    this.openSubMenus();
    
    if (this.action && window.event && window.event.altKey) {
    // focus came via alt-<accessKey> : ie needs click())
      Tobago.element(this.id).click();
    }
  };

Tobago.Menu.Item.prototype.onBlur = function() {
    //LOG.debug("onBlur " + this.id);
    this.focus = false;
    this.onBlurTimer = setTimeout("tobagoFocusLost('" + this.id + "')",
        getFocusLostTimeout());
  };

Tobago.Menu.Item.prototype.focusLost = function() {
    //LOG.debug("focusLost " + this.id);
    if (this.level > 0
        && (!this.mouseOver)
        && !(this.level != 1 && this.focus)
        &&  ! this.childHasFocus()) {
      this.hideSubMenus();
      Tobago.removeCssClass(this.id, "tobago-menu-item-focus");
      if (this.menuButton) {
        Tobago.removeCssClass(this.menuButton, "tobago-toolBar-button-menu-focus");
      }
      this.parent.focusLost();
    }
  };

Tobago.Menu.Item.prototype.isExpanded = function() {
    return this.subItemContainerId
        && Tobago.element(this.subItemContainerId).style.visibility.match(/visible/);
  };

Tobago.Menu.Item.prototype.focusLabelTag = function() {
    //LOG.debug("setze Focus " + this.id);
    var element = this.getLabelTag();
    if (element) {
      try {
      element.focus();
      } catch (ex) {
        // ignore
      }
    }
  };

Tobago.Menu.Item.prototype.blurLabelTag = function() {
    //LOG.debug("entferne Focus " + this.id);
    var element = this.getLabelTag();
    if (element) {
      element.blur();
    }
  };

Tobago.Menu.Item.prototype.childHasFocus = function() {
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


Tobago.Menu.Item.prototype.isNeighbourOpen = function() {
    for (var i = 0; i < this.parent.subItems.length; i++) {
      if (this.parent.subItems[i].isExpanded()) {
        return true;
      }
    }
    return false;
  }



  // #########################################################  key access

Tobago.Menu.Item.prototype.keyDown = function() {
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

Tobago.Menu.Item.prototype.keyUp = function() {
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

Tobago.Menu.Item.prototype.keyLeft = function() {
    if (this.level == 1) {
      var next = this.parent.nextItem(this.index, -1);
      if (next && next.id != this.id) { // menu has changed
        this.hover = false;
        next.openSubMenus();
      }
    }
    else if (this.level == 2) {
      if (this.isExpanded()) {
        this.hideSubMenus();
      } else {
        var next = this.parent.parent.nextItem(this.parent.index, -1);
        if (next && next.id != this.parent.id) { // menu has changed
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

Tobago.Menu.Item.prototype.keyRight = function() {
    if (this.level == 1) {
      var next = this.parent.nextItem(this.index, 1);
      if (next && next.id != this.id) { // menu has changed
        this.hover = false;
        next.openSubMenus();
      }
    }
    else if (this.level > 1) {
      if (this.subItemContainerId) {
        this.openSubMenus();
        this.hover = false;
        var next = this.nextItem(-1, 1);
      } else {
        var parent = this.parent;
        while (parent.level != 1) {
          parent = parent.parent;
        }
        var next = parent.parent.nextItem(parent.index, 1);
        if (next && next.id != parent.id) { // menu has changed
          parent.hover = false;
          next.openSubMenus();
        }
      }
    }
  };

Tobago.Menu.Item.prototype.nextItem = function(start, offset) {
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

    var j = Tobago.element(this.subItems[i].id).childNodes.length;
    var span = this.subItems[i].getLabelTag();
    if (span) {
      this.subItems[i].hover = true;
      span.focus();
      return this.subItems[i];
    }
    return false;
  };

Tobago.Menu.Item.prototype.collapse = function() {
    //LOG.debug("collapse " + this.id);
    if (this.level < 2) {
      var aTag = this.getLabelTag();
      if (aTag) {
        aTag.blur();
      }
      //this.onMouseOut(true);
    }
    else if (this.level == 2
        && !(this.isExpanded() )) {
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
  
Tobago.Menu.Item.prototype.getLabelTag = function() {
    var children = Tobago.element(this.id).childNodes;
    for (var k = 0; k < children.length; k++) {
      if (children[k].className.match(/tobago-menuBar-item-span/)) {
        return children[k];
      }
    }
  };
  
Tobago.Menu.Item.prototype.setSubitemArrowImage = function(image) {
    this.subitemArrowImage = image;
  };

Tobago.Menu.Item.prototype.getSubitemArrowImage = function() {
    if (! this.subitemArrowImage) {
      if (this.parent) {
        this.subitemArrowImage = this.parent.getSubitemArrowImage();
      }
    }
    return this.subitemArrowImage;
  };
  
Tobago.Menu.Item.prototype.addSubitemArrowImage = function() {
    if (this.level > 1 && this.subItems && this.subItems.length > 0) {
      var htmlElement = Tobago.element(this.id);
      var html = htmlElement.innerHTML;
      html += '<img class="tobago-menu-subitem-arrow" src="';
      html += this.getSubitemArrowImage() + '" />';
      htmlElement.innerHTML = html;
    }
  };
  
Tobago.Menu.Item.prototype.setupIframe = function(subItemContainer) {
    var subItemIframe = Tobago.element(this.subItemIframeId);
    if (subItemIframe) {
      subItemIframe.style.width = subItemContainer.scrollWidth;
      subItemIframe.style.height = subItemContainer.scrollHeight;
      subItemIframe.style.top = subItemContainer.style.top;
      subItemIframe.style.left = subItemContainer.style.left;
    }
  };

Tobago.Menu.Item.prototype.setMenuButton = function(button) {
    this.menuButton = button;
  };



function adjustPopupImage(menu) {
  if (menu.subItems && menu.subItems.length > 0) {
    var img = Tobago.element(menu.subItems[0].id).childNodes[0];
    if (img) {
      img.style.top = getPopupImageTop(menu.popup);
    }
    else {
      LOG.debug("kein IMG im popup");
    }
  }
}

function setItemWidth(menu) {

  if (menu.level != 0) {
    var htmlElement = Tobago.element(menu.id);
    if (htmlElement) {

      if (menu.level == 1) {
        htmlElement.style.width = htmlElement.scrollWidth + "px";
      }
      else { // level not 0 or 1

        var width = menu.parent.childWidth;

        if (! width) {
          width = 0;
          var re = new RegExp("(.*" + Tobago.SUB_COMPONENT_SEP +")\\d$");
          var childIdPrefix = menu.id.match(re)[1];
          var i = 0;
          var childElement = document.getElementById(childIdPrefix + i++);
          while (childElement) {
            //LOG.debug("item " + childElement.id + "  -->" + childElement.scrollWidth) ;//+ ":::" + childElement.innerHTML);
            width = Math.max(width, childElement.scrollWidth);
            childElement = document.getElementById(childIdPrefix + i++);
          }
          //LOG.debug("das waren " + (i-1) + " items  ---> width wird " + width);
          width += getMenuArrowWidth();
          menu.parent.childWidth = width;
        }
        
        menu.addSubitemArrowImage();
        
        htmlElement.style.width = width + "px";

      }

    htmlElement.style.overflow = 'hidden';
    }
  }
  for (var i = 0; i < menu.subItems.length; i++) {
    setItemWidth(menu.subItems[i]);
  }
  var subItemContainer = Tobago.element(menu.subItemContainerId);
  if (subItemContainer && menu.level != 0) {
    menu.subItemContainerStyleWidth
        = (menu.childWidth + getSubitemContainerBorderWidthSum()) + "px";
    var subMenuHeight = 0;
    for (var i = 0; i < menu.subItems.length; ++i) {
      var item = menu.subItems[i];
      subMenuHeight += (item.separator ? getSeparatorHeight() : getItemHeight());
    }
    menu.subItemContainerStyleHeight = (subMenuHeight + getSubitemContainerBorderWidthSum()) + "px";

    subItemContainer.style.width = "0px";
    subItemContainer.style.height = "0px";

    var subItemIframe = Tobago.element(menu.subItemIframeId);
    if (subItemIframe) {
      subItemIframe.style.width = subItemContainer.style.width;
      subItemIframe.style.height = subItemContainer.style.height;
    }
  }
}

function setItemPositions(menu) {

  if (menu.level != 0) {
    var htmlElement = Tobago.element(menu.id);
    if (htmlElement) {

      if (menu.level == 1) {
        var itemHeight = getItemHeight(menu);
        var top = 0;
        if (menu.parent.popup) {
          top = Tobago.Config.get("Menu", menu.parent.popup + "MenuTopOffset");
        }
        htmlElement.style.top = top +"px";
        htmlElement.style.height = itemHeight + "px";
        var subItemContainer = Tobago.element(menu.subItemContainerId);
        if (subItemContainer) {
          subItemContainer.style.top = (itemHeight + top) + "px";
        }
        var left = 0;
        if (menu.index != 0) {
          var neighbour = menu.parent.subItems[menu.index -1];
          var left = Tobago.element(neighbour.id).style.left.replace(/\D/g,"") - 0;
          left += Tobago.element(neighbour.id).style.width.replace(/\D/g,"") - 0;
        }
        htmlElement.style.left = left + "px";
        if (subItemContainer) {
//          menu.subItemContainer.style.left = left + "px";
          subItemContainer.style.left = "0px";
        }
        htmlElement.style.zIndex = "999";
      }
      else { // level not 0 or 1
        var top = 0;
        for (var i = 0; i < menu.index; ++i) {
          var item = menu.parent.subItems[i];
          top += (item.separator ? getSeparatorHeight() : getItemHeight());
        }
        var left = 0;
        if (menu.level == 2 && menu.parent.parent.popup) {
          if (menu.parent.parent.popup == "ToolbarButton") {
            var menubar = Tobago.element(menu.parent.parent.menubarId);
            left = getPopupMenuWidth() - menubar.scrollWidth;
          }
          var subItemContainer = Tobago.element(menu.parent.subItemContainerId);
          if (subItemContainer) {
            subItemContainer.style.left = left + "px";

            if (menu.parent.subItemIframe) {
              menu.parent.subItemIframe.style.left
                  = menu.parent.subItemContainer.style.left;
            }
          }
        }
        var subItemContainer = Tobago.element(menu.subItemContainerId);
        if (subItemContainer) {
          //if (menu.level == 2) {
          //  top = getItemHeight();
          //}
          /*
          if (menu.level != 1) {
            left = menu.parent.childWidth;
          }
          subItemContainer.style.top = top + "px";
          subItemContainer.style.left = left + "px";*/
          subItemContainer.style.top = "0px";
          subItemContainer.style.left = "-" + menu.parent.childWidth + "px";
        }

        htmlElement.style.top = top + "px";
        htmlElement.style.left = "0px";

      }
    }
  }
  for (var i = 0; i < menu.subItems.length; i++) {
    setItemPositions(menu.subItems[i]);
  }
}

function setItemsVisible(menu) {
  for (var i = 0; i < menu.subItems.length; i++) {
    Tobago.element(menu.subItems[i].id).style.visibility = 'visible';
  }
}

function initMenuItems(menu) {
  var htmlElement = Tobago.element(menu.id);
  if (htmlElement) {

    htmlElement.menuItem = menu;
    if (menu.parent && menu.parent.menubarId
        && Tobago.element(menu.parent.menubarId).className.match(/tobago-menuBar-page-facet/)) {
      Tobago.addCssClass(htmlElement, 'tobago-menuBar-item-page-facet');
    }
    var id = menu.id + Tobago.SUB_COMPONENT_SEP + 'items';
    if (Tobago.element(id)) {
      menu.subItemContainerId = id;
    }
    id = menu.id + Tobago.SUB_COMPONENT_SEP + 'iframe';
    if (Tobago.element(id)) {
      menu.subItemIframeId = id;
    }
    var subItemIframe = Tobago.element(menu.subItemIframeId);
    if (subItemIframe) {
      subItemIframe.style.visibility = "hidden";
      subItemIframe.style.position = "absolute";
      subItemIframe.style.border = "0px solid black";
      subItemIframe.style.zIndex
          = Tobago.getRuntimeStyle(htmlElement).zIndex - 1;
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

    if (Tobago.element(menu.parent.menubarId).className.match(/tobago-menuBar-page-facet/)) {
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

function getSeparatorHeight() {
  return 20;
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
    LOG.debug("unbekanter Popup Typ :" + popup);
    return "0px";
  }
}

function getSheetSelectorMenuTopOffset() {
  return -1;
}

function menuCheckToggle(id) {
  var element = document.getElementById(id);
  var form = document.forms[0];
  if (element) {
    //LOG.debug("remove  " + id);
    form.removeChild(element);
  }
  else {
    //LOG.debug("adding " + id);
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
  var element = Tobago.element(event);
  element.parentNode.menuItem.onFocus();
}
function tobagoMenuBlur(event) {
  //LOG.debug("tobagoMenuBlur" );

  if (! event) {
    event = window.event;
  }
  var element = Tobago.element(event);
  element.parentNode.menuItem.onBlur();
}

function tobagoMenuDown(event) {
  var element = Tobago.element(event);
  element.parentNode.menuItem.keyDown();
}
function tobagoMenuUp(event) {
  var element = Tobago.element(event);
  element.parentNode.menuItem.keyUp();
}
function tobagoMenuLeft(event) {
  var element = Tobago.element(event);
  element.parentNode.menuItem.keyLeft();
}
function tobagoMenuRight(event) {
  var element = Tobago.element(event);
  element.parentNode.menuItem.keyRight();
}

function tobagoMenuItemCollapse(event) {
  var element = Tobago.element(event);
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
  return 0;
}
function getOpenSubMenusTimeout() {
  return 0;
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
  LOG.debug("tobagoMenuItemOnfocus " + element.id);
  element.menuItem.onFocus();
}
function tobagoMenuItemOnblur(element) {
  LOG.debug("tobagoMenuItemOnblur " + element.id);
  element.menuItem.onBlur();
}
*/
function tobagoMenuOpenMenu(element, event) {
  if (event) {
    Tobago.stopEventPropagation(event);
  }
  element.menuItem.openMenu();
}
function tobagoButtonOpenMenu(button, idPrefix) {
  var menu = Tobago.element(idPrefix + Tobago.SUB_COMPONENT_SEP
      + Tobago.Menu.MENU_ROOT_ID + Tobago.SUB_COMPONENT_SEP + 0);
  if (menu) {
    menu.menuItem.setMenuButton(button);
    tobagoMenuOpenMenu(menu);
  }
}

function tobagoSetSubMenuContainerVisible(id){
  document.getElementById(id).menuItem.setSubMenuContainerVisibility("visible");
}
