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
         <li id="m1" class="tobago-menu-markup-top">
           <a>Menu 1</a>
         </li>
         <li id="m2" class="tobago-menu-markup-top">
           <a>Menu 2</a>
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

Tobago.Menu = {};

/*
  jQuery(this) is the "a" tag of a menu item.
 */
Tobago.Menu.handelKey = function(event) {

  var handled = false;

  var code = event.which;
  if (code  == 0) {
    code = event.keyCode;
  }

  switch (code) {
    case 27: // escape
      Tobago.Menu.closeAll();
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
        jQuery(this).parent().tobagoMenu_findSubMenu().children(":nth-child(1)").children('a').focus();
      } else {
        jQuery(this).parent().nextAll('li').children('a').eq(0).focus();
      }
      handled = true;
      break;
    default:
      break;
  }
  return !handled;
};

/**
 * jQuery(this) is a <a> tag of a menu item.
 */
Tobago.Menu.open = function(event) {

  var li = jQuery(this).parent();
  var sub = li.tobagoMenu_findSubMenu();

  // close menus in other branches
  li.siblings().tobagoMenu_findSubMenu().find('ol').andSelf().css('visibility', 'hidden');

  // close sub sub menu
  sub.children().find("ol").css('visibility', 'hidden');

  // we have to set display to none, because otherwise, the sub-menu may produce scroll-bars.
  // we have to set the display back to block, before evaluating the positions
  sub.css('display', 'block');

  // open sub menu
  if (sub.size() > 0) {
    // compute position
    var left;
    var top;
    var page = jQuery(".tobago-page-content:first");
    if (li.hasClass('tobago-menu-markup-top')) {
      // is top menu
      left = li.offset().left;
      top = li.offset().top + li.outerHeight();
      // fix menu position, when it is outside of the current page
      left = Math.max(0, Math.min(left, page.outerWidth() - sub.outerWidth()));
      top = Math.max(0, Math.min(top, page.outerHeight() - sub.outerHeight()));
      if (Tobago.browser.isMsie678) {
        left = Math.max(0, left - 1); // XXX TOBAGO-1339
      }
    } else {
      // is sub menu
      // fix menu position, when it is outside of the current page
      if (li.offset().left + li.outerWidth() + sub.outerWidth() <= page.outerWidth()) {
        left = li.position().left + li.outerWidth();
      } else {
        left = -sub.outerWidth();
      }
      top = li.position().top - 1; // 1 = border-top
      top = Math.min(top, page.outerHeight() - sub.outerHeight() + top - li.offset().top);
    }
    sub.css('left', left);
    sub.css('top', top);

    // show
    sub.css('visibility', 'visible');

    // IE6 select-tag fix
    if (Tobago.browser.isMsie6) {
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
};

Tobago.Menu.closeAll = function() {
  jQuery(".tobago-menuBar").each(function() {
    Tobago.Menu.switchOff(jQuery(this));
  });
  return false;
};

/**
* returns the browser specific event which should be used.
*/
function compatibleKeyEvent() {
  return Tobago.browser.isMsie || Tobago.browser.isWebkit ? 'keydown' : 'keypress';
}

Tobago.Menu.mouseOver = function(event) {
      jQuery(this).children('a').focus();
      return false;
};

Tobago.Menu.switchOn = function(menuBar, menu) {
  menuBar.find('li') // direct menus
      .add(menuBar.find('li').tobagoMenu_findSubMenu().find('li')) // add sub menus
      .bind('mouseover', Tobago.Menu.mouseOver)
      .children('a')
      .on('focus', Tobago.Menu.open)
      .bind(compatibleKeyEvent(), Tobago.Menu.handelKey);
  var a = menu.children('a');
  if (menu.parents(".tobago-toolBar").size() == 0
      && menu.parents(".tobago-box-headerToolBar").size() == 0
      && menu.parents(".tobago-sheet-toolBar").size() == 0
      && menu.parents(".tobago-tabGroup-toolBar").size() == 0) {
    a.trigger("focus");
  } else {
    // XXX the call in the previous line doesn't work with toolBar -> dropDown (don't know why), so using direct call
    // XXX the problem is since updating jQuery from 1.6.4 to 1.10.1
    a.each(Tobago.Menu.open);
  }
  jQuery("body").bind('click', Tobago.Menu.closeAll);
  menuBar.data('menu-active', true);        // write state back
};

Tobago.Menu.switchOff = function(menuBar) {
  menuBar.find("ol")
      .add(menuBar.find('li').tobagoMenu_findSubMenu().find('ol').andSelf())
      .css('visibility', 'hidden');
  menuBar.find('li').add(menuBar.find('li').tobagoMenu_findSubMenu().find('li'))
      .unbind('mouseover', Tobago.Menu.mouseOver)
      .children('a')
      .off('focus', Tobago.Menu.open)
      .unbind(compatibleKeyEvent(), Tobago.Menu.handelKey);
  jQuery("body").unbind('click', Tobago.Menu.closeAll);
  menuBar.find('.tobago-menu-markup-selected').removeClass("tobago-menu-markup-selected");
  menuBar.data('menu-active', false);        // write state back
};

/**
 * @param elements  a jQuery object to initialize (ajax) or null for initializing the whole document (full load).
 */
Tobago.Menu.init = function(elements) {

  var menus = Tobago.Utils.selectWidthJQuery(elements, ".tobago-menu-markup-top");

  // a click on the top menu make the complete menu active or inactive respectively.
  menus.click(function(event) {

    // e. g. disabled by a popup
    if (jQuery(this).children("a").data("disabled")) {
      return;
    }
    // register on click handlers
    var menuBar = jQuery(this).parent();
    var wasActive = menuBar.data('menu-active'); // read state

    if (wasActive) {
      Tobago.Menu.switchOff(menuBar);
    } else {
      Tobago.Menu.switchOn(menuBar, jQuery(this));
    }

    event.stopPropagation();
  });

  // IE6 select-tag fix
  // put a iframe inside the div, so that a <select> tag doesn't shine through.
  // the iframe must be resized (see above)
  if (Tobago.browser.isMsie6) {
    menus.children("ol").prepend(
        "<iframe class='tobago-menu-ie6bugfix' src='" + Tobago.blankPage + "'></iframe>");
  }

  jQuery(".tobago-page-menuStore").append(menus.children("ol"));

  var toolBarMenu = Tobago.Utils.selectWidthJQuery(
      elements, ".tobago-toolBar-menu .tobago-boxToolBar-menu .tobago-tabGroupToolBar-menu");
  // a click on toolBar menu opener -> forward to .tobago-menu-markup-top
  toolBarMenu.click(function(event) {
    jQuery(this).next().find('a').click();
    event.stopPropagation();
  });

  // init context menus
  var contextMenu = Tobago.Utils.selectWidthJQuery(elements, ".tobago-menu-contextMenu");
  contextMenu.parent().bind("contextmenu", function(event) {
    jQuery(this).children(".tobago-menu-contextMenu").find('a').click();
    event.stopPropagation();
    return false;
  });

};

jQuery.tobagoMenuParent = function(element) {
  var result = [];

  result.push(element);

  return result;
};

/*
  jQuery(this) is a list of "li" element of a menu item as jQuery object.
  Returns a list of "ol" objects. All sub menus as jQuery object.
*/
(function(jQuery) {
  jQuery.fn.extend({
    tobagoMenu_findSubMenu: function() {
      var menu = jQuery(this).children("ol");
      jQuery(this).each(function() {
        menu = menu.add(Tobago.Utils.findSubComponent(jQuery(this), "menu"));
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
        return Tobago.Utils.findSuperComponent(ol);
      }
      return ol;
    }
  });
})(jQuery);

Tobago.registerListener(Tobago.Menu.init, Tobago.Phase.DOCUMENT_READY, Tobago.Phase.Order.LATE);
Tobago.registerListener(Tobago.Menu.init, Tobago.Phase.AFTER_UPDATE, Tobago.Phase.Order.LATE);
