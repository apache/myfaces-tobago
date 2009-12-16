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

// todo: rename xxx_
function xxx_tobagoMenuHandelKey(event) {

  var handled = false;
  
  var code = event.which;
  if (code  == 0) {
    code = event.keyCode;
  }

  switch (code) {
    case 27: // escape
      xxx_tobagoMenuSwitchOff($(this).closest(".tobago-menuBar-default"));
      handled = true;
      break;
    case 37: // cursor left
      if ($(this).parent().hasClass('tobago-menu-top')) {
        $(this).parent().prev('li').children('a').focus();
      } else if ($(this).parent().parent().parent().hasClass('tobago-menu-top')) {
        $(this).parent().parent().parent().prev('li').children('a').focus();
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
        $(this).closest('.tobago-menu-top').next('li').children('a').focus();
      }
      handled = true;
      break;
    case 40: // cursor down
      if ($(this).parent().hasClass('tobago-menu-top')) {
        $(this).next('ol').children(":nth-child(1)").children('a').focus();
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

function xxx_tobagoMenuOpen(event) {

  // close menus in other branches
  $(this).parent().siblings().find("ol").css('visibility', 'hidden');

  // close sub sub menu 
  $(this).next("ol").children().find("ol").css('visibility', 'hidden');

  // open sub menu
  // todo: this must be done only one time...
  var sub = $(this).next("ol");
  if (sub.size() > 0) { // XXX check if there is a nicer method
    // compute position
    if ($(this).parent().hasClass('tobago-menu-top')) {
      // is top menu
      sub.css('left', sub.parent().position().left);
      sub.css('top', sub.parent().outerHeight());
    } else {
      // is sub menu
      sub.css('left', sub.parent().position().left + sub.parent().outerWidth());
      sub.css('top', sub.parent().position().top - 1); // 1 = border-top
    }

    // show
    sub.css('visibility', 'visible');
  }
      
  // old "hover" off
  $(this).parent().siblings('.tobago-menu-selected').removeClass("tobago-menu-selected");
  $(this).next("ol").children('.tobago-menu-selected').removeClass("tobago-menu-selected");
  // "hover" on
  $(this).parents('li').addClass("tobago-menu-selected");
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
  menuBar.find("li")
      .bind('mouseover', xxx_tobagoMenuMouseOver)
      .children('a')
      .bind('focus', xxx_tobagoMenuOpen)
      .bind(compatibleKeyEvent(), xxx_tobagoMenuHandelKey);
  menu.children('a').focus();
  menuBar.attr('menu-active', 'true');        // write state back
}

function xxx_tobagoMenuSwitchOff(menuBar) {
  menuBar.find("ol").css('visibility', 'hidden');
  menuBar.find("li")
      .unbind('mouseover', xxx_tobagoMenuMouseOver)
      .children('a')
      .unbind('focus', xxx_tobagoMenuOpen)
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

xxx_tobagoMenuInit();
