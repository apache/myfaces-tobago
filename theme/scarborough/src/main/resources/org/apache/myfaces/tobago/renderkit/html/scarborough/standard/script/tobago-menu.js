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
     <ol class="tobago-menuBar-default">
       <li class="tobago-menu-top">
         <a id="m1">Menu 1</a>
       </li>
       <li class="tobago-menu-top">
         <a id="m2">Menu 2</a>
       </li>
       ...
     </ol>
     <div>
       // page content     
     </div>
     <div class="tobago-menu-store">  // container for the sub menus.
       <ol id="m1::menu">
         <li class="tobago-menu-parent">
           <a>Sub Menu 1.1</a>
             <ol>
               <li class="tobago-menu-parent">
                 <a>Sub Sub Menu 1.1.1</a>
               </li>
               ...
     </div>
   </body>
 </html>
 The menu items of the top level (id="m1") are connected to the sub menus of the store div (id="m1::menu").   
 */

// todo: rename xxx_ and check the other function names.
/*
  $(this) is the "a" tag of a menu item.
 */
function xxx_tobagoMenuHandelKey(event) {

  var handled = false;
  
  var code = event.which;
  if (code  == 0) {
    code = event.keyCode;
  }

  switch (code) {
    case 27: // escape
      xxx_tobagoMenuClose(event, $(this));
      handled = true;
      break;
    case 37: // cursor left
      if ($(this).parent().hasClass('tobago-menu-top')) {
        $(this).parent().prev('li').children('a').focus();
      } else if ($(this).parent().parent().tobagoMenu_findParentMenu().parent().hasClass('tobago-menu-top')) {
        $(this).parent().parent().tobagoMenu_findParentMenu().parent().prev('li').children('a').focus();
      } else {
        $(this).closest('ol').prev('a').focus();
      }
      handled = true;
      break;
    case 38: // cursor up
      if ($(this).parent().hasClass('tobago-menu-top')) {
        // nothing
      } else {
        $(this).parent().prevAll('li').children('a').eq(0).focus();
      }
      handled = true;
      break;
    case 39: // cursor right
      if ($(this).parent().hasClass('tobago-menu-top')) {
        $(this).parent().next('li').children('a').focus();
      } else if ($(this).next('ol').size() > 0) {
        $(this).next('ol').children(":nth-child(1)").children('a').focus();
      } else {
        $(this).parents('ol:last').tobagoMenu_findParentMenu().parent().next('li').children('a').focus();
      }
      handled = true;
      break;
    case 40: // cursor down
      if ($(this).parent().hasClass('tobago-menu-top')) {
        $(this).tobagoMenu_findSubMenu().children(":nth-child(1)").children('a').focus();
      } else {
        $(this).parent().nextAll('li').children('a').eq(0).focus();
      }
      handled = true;
      break;
    default:
      break;
  }
  return !handled;
}

/*
  $(this) is a <a> tag of a menu item.
*/
function xxx_tobagoMenuOpen(event) {

  var li = $(this).parent();
  var sub = $(this).tobagoMenu_findSubMenu();
  
  // close menus in other branches
  li.siblings().children('a').tobagoMenu_findSubMenu().find('ol').andSelf().css('visibility', 'hidden');

  // close sub sub menu 
  sub.children().find("ol").css('visibility', 'hidden');

  // open sub menu
  if (sub.size() > 0) {
    // compute position
    if (li.hasClass('tobago-menu-top')) {
      // is top menu
      sub.css('left', li.offset().left);
      sub.css('top', li.offset().top + li.outerHeight());
    } else {
      // is sub menu
      sub.css('left', li.position().left + li.outerWidth());
      sub.css('top', li.position().top - 1); // 1 = border-top
    }

    // show
    sub.css('visibility', 'visible');
  }
      
  // old "hover" off
  li.siblings('.tobago-menu-selected').removeClass("tobago-menu-selected");
  sub.children('.tobago-menu-selected').removeClass("tobago-menu-selected");
  // "hover" on
  $(this).parents('li').addClass("tobago-menu-selected");
}

function xxx_tobagoMenuClose(event, src) {
  if (src === undefined) {
    src = $(this);
  }
  if (src.parent().hasClass('tobago-menu-top')) {
    xxx_tobagoMenuSwitchOff(src.parent().parent());
  } else {
    xxx_tobagoMenuSwitchOff(src.parents('ol:last').tobagoMenu_findParentMenu().parent().parent());
  }
  return false;
}

/**
* returns the browser specific event which should be used.
*/
function compatibleKeyEvent() {
  return jQuery.browser.msie || jQuery.browser.safari ? 'keydown' : 'keypress';
}

function xxx_tobagoMenuMouseOver(event) {
      $(this).children('a').focus();
      return false;
}

function xxx_tobagoMenuSwitchOn(menuBar, menu) {
  menuBar.find('li') // direct menus
      .add(menuBar.find('li').children('a').tobagoMenu_findSubMenu().find('li')) // add sub menus
      .bind('mouseover', xxx_tobagoMenuMouseOver)
      .children('a')
      .bind('focus', xxx_tobagoMenuOpen)
      .bind('blur', xxx_tobagoMenuClose)
      .bind(compatibleKeyEvent(), xxx_tobagoMenuHandelKey);
  menu.children('a').focus();
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
      .unbind('blur', xxx_tobagoMenuClose)
      .unbind(compatibleKeyEvent(), xxx_tobagoMenuHandelKey);
  menuBar.attr('menu-active', 'false');        // write state back
}

function xxx_tobagoMenuInit() {
  $(document).ready(function() {

    // a click on the top menu make the complete menu active or inactive respectively.
    $(".tobago-menu-top").click(function(event) {

      // register on click handlers
      var menuBar = $(this).parent();
      var wasActive = 'true' == menuBar.attr('menu-active'); // read state

      if (wasActive) {
        xxx_tobagoMenuSwitchOff(menuBar);
      } else {
        xxx_tobagoMenuSwitchOn(menuBar, $(this));
      }

      event.stopPropagation();

    });
  });
}

jQuery.tobagoMenuParent = function(element) {
  var result = [];

  result.push(element);

  return result;
};


/*
  $(this) is a list of "a" element of a menu item as jQuery object.
  Returns a list of "ol" objects. All sub menus as jQuery object.
*/
(function($) {
  $.fn.extend({
    tobagoMenu_findSubMenu: function() {
      var menu = $(this).next("ol");
      $(this).each(function() {
        menu = menu.add(tobagoUtil_findSubComponent($(this), "menu"));
      });
      return menu;
    }
  });
})(jQuery);

/*
  $(this) is a "ol" element which represents a sub menu.
  returns the "a" element connected with the given sub menu.  
*/
(function($) {
  $.fn.extend({
    tobagoMenu_findParentMenu: function() {
      var ol = $(this);
      if (ol.attr('id').lastIndexOf("::") >= 0) {
        return tobagoUtil_findSuperComponent(ol);
    }
        return ol;
      }
  });
})(jQuery);

function tobagoUtil_findSubComponent(element, subId) {
  return $(tobagoUtil_getSubComponentId(element.attr('id'), subId));
}

function tobagoUtil_getSubComponentId(id, subId) {
  return "#" + id.replace(":", "\\:") + "\\:\\:" + subId; 
}

function tobagoUtil_findSuperComponent(element) {
  return $(tobagoUtil_getSuperComponentId(element.attr('id')));
}

function tobagoUtil_getSuperComponentId(id) {
  return "#" + id.substring(0, id.lastIndexOf("::")).replace(":", "\\:"); 
}

xxx_tobagoMenuInit();
