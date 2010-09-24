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

/*
 jQuery code to manage the menu.
 The DOM structure looks like this:
 <html>
   <body>
     <form>
       <ol class="tobago-menuBar">
         <li class="tobago-menu-markup-top">
           <a id="m1">Menu 1</a>
         </li>
         <li class="tobago-menu-markup-top">
           <a id="m2">Menu 2</a>
         </li>
         ...
       </ol>
       <div class="tobago-page">
         // page content     
       </div>
       <span ... </span>
       <div class="tobago-menu-store">  // container for the sub menus.
         <ol id="m1::menu">
--->         insert iframe here
           <li class="tobago-menu">
             <a>Sub Menu 1.1</a>
               <ol>
--->         insert iframe here
                 <li class="tobago-menu">
                   <a>Sub Sub Menu 1.1.1</a>
                 </li>
                 ...
       </div>
     </form>
   </body>
 </html>
 The menu items of the top level (id="m1") are connected to the sub menus of the store div (id="m1::menu").   
 */

// todo: rename xxx_ and check the other function names.
/*
  jQuery(this) is the "a" tag of a menu item.
 */
function xxx_tobagoMenuHandelKey(event) {

  var handled = false;
  
  var code = event.which;
  if (code  == 0) {
    code = event.keyCode;
  }

  switch (code) {
    case 27: // escape
      xxx_tobagoMenuCloseAll(event, jQuery(this));
      handled = true;
      break;
    case 37: // cursor left
      if (jQuery(this).parent().hasClass('tobago-menu-markup-top')) {
        jQuery(this).parent().prev('li').children('a').focus();
      } else if (jQuery(this).parent().parent().tobagoMenu_findParentMenu().parent().hasClass('tobago-menu-markup-top')) {
        jQuery(this).parent().parent().tobagoMenu_findParentMenu().parent().prev('li').children('a').focus();
      } else {
        jQuery(this).closest('ol').prev('a').focus();
      }
      handled = true;
      break;
    case 38: // cursor up
      if (jQuery(this).parent().hasClass('tobago-menu-markup-top')) {
        // nothing
      } else {
        jQuery(this).parent().prevAll('li').children('a').eq(0).focus();
      }
      handled = true;
      break;
    case 39: // cursor right
      if (jQuery(this).parent().hasClass('tobago-menu-markup-top')) {
        jQuery(this).parent().next('li').children('a').focus();
      } else if (jQuery(this).next('ol').size() > 0) {
        jQuery(this).next('ol').children(":nth-child(1)").children('a').focus();
      } else {
        jQuery(this).parents('ol:last').tobagoMenu_findParentMenu().parent().next('li').children('a').focus();
      }
      handled = true;
      break;
    case 40: // cursor down
      if (jQuery(this).parent().hasClass('tobago-menu-markup-top')) {
        jQuery(this).tobagoMenu_findSubMenu().children(":nth-child(1)").children('a').focus();
      } else {
        jQuery(this).parent().nextAll('li').children('a').eq(0).focus();
      }
      handled = true;
      break;
    default:
      break;
  }
  return !handled;
}

/*
  jQuery(this) is a <a> tag of a menu item.
*/
function xxx_tobagoMenuOpen(event) {

  var li = jQuery(this).parent();
  var sub = jQuery(this).tobagoMenu_findSubMenu();
  
  // close menus in other branches
  li.siblings().children('a').tobagoMenu_findSubMenu().find('ol').andSelf().css('visibility', 'hidden');

  // close sub sub menu 
  sub.children().find("ol").css('visibility', 'hidden');

  // open sub menu
  if (sub.size() > 0) {
    // compute position
    // XXX Todo: "compute position" is not perfect! Should be enhanced, especially for sub menus.
    // XXX The problem is, when the menu opens at the edge of the visual window.
    var left;
    var top;
    if (li.hasClass('tobago-menu-markup-top')) {
      // is top menu
      left = li.offset().left;
      top = li.offset().top + li.outerHeight();
      // fix menu position, when it is outside of the current page
      var page = jQuery(".tobago-page-content:first");
      left = Math.max(0, Math.min(left, page.outerWidth() - sub.outerWidth()));
      top = Math.max(0, Math.min(top, page.outerHeight() - sub.outerHeight()));
    } else {
      // is sub menu
      left = li.position().left + li.outerWidth();
      top = li.position().top - 1; // 1 = border-top
      // XXX todo: fix sub menu position, when it is outside of the page
    }
    sub.css('left', left);
    sub.css('top', top);

    // show
    sub.css('visibility', 'visible');
    
    // IE6 select-tag fix
    if (jQuery.browser.msie && parseInt(jQuery.browser.version) <= 6) {
      //          sub.css('width', sub.width());
      //          sub.css('height', sub.height());
      //          sub.css('display', 'none');
      //          sub.css('display', 'block');
      var iframe = sub.children("iframe:first");
      iframe.css('width', sub.outerWidth());
      iframe.css('height', sub.outerHeight());
      iframe.css('left', -(parseInt(sub.css('border-left-width')) + parseInt(sub.css('padding-left'))));
      iframe.css('top', -(parseInt(sub.css('border-top-width')) + parseInt(sub.css('padding-top'))));
    }
  }
      
  // old "hover" off
  li.siblings('.tobago-menu-markup-selected').removeClass("tobago-menu-markup-selected");
  sub.children('.tobago-menu-markup-selected').removeClass("tobago-menu-markup-selected");
  // "hover" on
  jQuery(this).parents('li').addClass("tobago-menu-markup-selected");
}

function xxx_tobagoMenuCloseAll() {
  jQuery(".tobago-menuBar").each(function() {
    xxx_tobagoMenuSwitchOff(jQuery(this));
  });
  return false;
}

/**
* returns the browser specific event which should be used.
*/
function compatibleKeyEvent() {
  return jQuery.browser.msie || jQuery.browser.safari ? 'keydown' : 'keypress';
}

function xxx_tobagoMenuMouseOver(event) {
      jQuery(this).children('a').focus();
      return false;
}

function xxx_tobagoMenuSwitchOn(menuBar, menu) {
  menuBar.find('li') // direct menus
      .add(menuBar.find('li').children('a').tobagoMenu_findSubMenu().find('li')) // add sub menus
      .bind('mouseover', xxx_tobagoMenuMouseOver)
      .children('a')
      .bind('focus', xxx_tobagoMenuOpen)
      .bind(compatibleKeyEvent(), xxx_tobagoMenuHandelKey);
  menu.children('a').focus();
  jQuery("body").bind('click', xxx_tobagoMenuCloseAll);
  menuBar.attr('menu-active', 'true');        // write state back
}

function xxx_tobagoMenuSwitchOff(menuBar) {
  menuBar.find("ol")
      .add(menuBar.find('li').children('a').tobagoMenu_findSubMenu().find('ol').andSelf())
      .css('visibility', 'hidden');
  menuBar.find('li').add(menuBar.find('li').children('a').tobagoMenu_findSubMenu().find('li'))
      .unbind('mouseover', xxx_tobagoMenuMouseOver)
      .children('a')
      .unbind('focus', xxx_tobagoMenuOpen)
      .unbind(compatibleKeyEvent(), xxx_tobagoMenuHandelKey);
  jQuery("body").unbind('click', xxx_tobagoMenuCloseAll);
  menuBar.find('.tobago-menu-markup-selected').removeClass("tobago-menu-markup-selected");
  menuBar.attr('menu-active', 'false');        // write state back
}

/**
 * @param elements  a jQuery object to initialize (ajax) or null for initializing the whole document (full load).
 */
function xxx_tobagoMenuInit(elements) {

  var menus = Tobago.selectWidthJQuery(elements, ".tobago-menu-markup-top");

  // a click on the top menu make the complete menu active or inactive respectively.
  menus.click(function(event) {

    // e. g. disabled by a popup
    if (jQuery(this).children("a").attr("disabled")) {
      return;
    }
    // register on click handlers
    var menuBar = jQuery(this).parent();
    var wasActive = 'true' == menuBar.attr('menu-active'); // read state

    if (wasActive) {
      xxx_tobagoMenuSwitchOff(menuBar);
    } else {
      xxx_tobagoMenuSwitchOn(menuBar, jQuery(this));
    }

    event.stopPropagation();
  });

  // IE6 select-tag fix
  // put a iframe inside the div, so that a <select> tag doesn't shine through.
  // the iframe must be resized (see above)
  if (jQuery.browser.msie && parseInt(jQuery.browser.version) <= 6) {
    menus.children("ol").prepend(
        "<iframe class='tobago-menu-ie6bugfix' src='" + Tobago.blankPage + "'></iframe>");
  }

  jQuery(".tobago-page-menuStore").append(menus.children("ol"));

  var toolBarMenu = Tobago.selectWidthJQuery(
      elements, ".tobago-toolBar-menu .tobago-boxToolBar-menu .tobago-tabGroupToolBar-menu");
  // a click on toolBar menu opener -> forward to .tobago-menu-markup-top
  toolBarMenu.click(function(event) {
    $(this).next().find('a').click();
    event.stopPropagation();
  });

  // init context menus
  var contextMenu = Tobago.selectWidthJQuery(elements, ".tobago-menu-contextMenu");
  contextMenu.parent().bind("contextmenu", function(event) {
    jQuery(this).children(".tobago-menu-contextMenu").find('a').click();
    event.stopPropagation();
    return false;
  });

}

jQuery.tobagoMenuParent = function(element) {
  var result = [];

  result.push(element);

  return result;
};

/*
  jQuery(this) is a list of "a" element of a menu item as jQuery object.
  Returns a list of "ol" objects. All sub menus as jQuery object.
*/
(function(jQuery) {
  jQuery.fn.extend({
    tobagoMenu_findSubMenu: function() {
      var menu = jQuery(this).next("ol");
      jQuery(this).each(function() {
        menu = menu.add(tobagoUtil_findSubComponent(jQuery(this), "menu"));
      });
      return menu;
    }
  });
})(jQuery);

/*
  jQuery(this) is a "ol" element which represents a sub menu.
  returns the "a" element connected with the given sub menu.  
*/
(function(jQuery) {
  jQuery.fn.extend({
    tobagoMenu_findParentMenu: function() {
      var ol = jQuery(this);
      if (ol.attr('id').lastIndexOf("::") >= 0) {
        return tobagoUtil_findSuperComponent(ol);
    }
        return ol;
      }
  });
})(jQuery);

function tobagoUtil_findSubComponent(element, subId) {
  return jQuery(tobagoUtil_getSubComponentId(element.attr('id'), subId));
}

function tobagoUtil_getSubComponentId(id, subId) {
  return "#" + id.replace(/:/g, "\\:") + "\\:\\:" + subId; 
}

function tobagoUtil_findSuperComponent(element) {
  return jQuery(tobagoUtil_getSuperComponentId(element.attr('id')));
}

function tobagoUtil_getSuperComponentId(id) {
  return "#" + id.substring(0, id.lastIndexOf("::")).replace(/:/g, "\\:"); 
}
