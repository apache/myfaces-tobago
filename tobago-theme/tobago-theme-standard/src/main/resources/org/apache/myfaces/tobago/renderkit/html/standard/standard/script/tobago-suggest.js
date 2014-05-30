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

Tobago.Suggest = {};

Tobago.Suggest.init = function (elements) {

  var suggests = Tobago.Utils.selectWidthJQuery(elements, ".tobago-suggest");
  suggests.each(function () {
    jQuery(this).suggest();
  });
};

// call before Tobago.Menu.init
Tobago.registerListener(Tobago.Suggest.init, Tobago.Phase.DOCUMENT_READY, Tobago.Phase.Order.NORMAL);
Tobago.registerListener(Tobago.Suggest.init, Tobago.Phase.AFTER_UPDATE, Tobago.Phase.Order.NORMAL);

/*
XXX is this an alternative for initialization?

Tobago.registerListener({
  name: "suggest",
  before: ["menu"],
//    after: ["c", "d"],
  phases: [Tobago.Phase.DOCUMENT_READY, Tobago.Phase.AFTER_UPDATE],
  call: function(elements) {
    var suggests = Tobago.Utils.selectWidthJQuery(elements, ".tobago-suggest");
    suggests.each(function () {
      jQuery(this).suggest();
    });
  }
  or
  selector: ".tobago-suggest"
  call: function() {
    jQuery(this).suggest();
  }
});
*/

/**
 * Input suggest.
 */
(function ($) {

  $.widget("tobago.suggest", {

    options: {
    },

    subMenu: undefined,
    input: undefined,
    scheduled: undefined,
    delay: undefined,
    minChars: undefined,
    maxItems: undefined,
    update: undefined,
    totalCount: undefined,

    _create: function () {

      this.delay = this.element.data("tobago-suggest-delay");
      this.minChars = this.element.data("tobago-suggest-min-chars");
      this.maxItems = this.element.data("tobago-suggest-max-items");
      this.update = this.element.data("tobago-suggest-update");
      this.totalCount = this.element.data("tobago-suggest-total-count");

      // creating the menu stuff
      var menuBar = this.element.find(".tobago-menuBar");
      menuBar.find("a").data("tobago-ignore-focus", true); // XXX can be removed, after refactoring the menu to jQuery widget
      this.subMenu = menuBar.find("ol"); // is only one element

      if (this.update) {
        // remove all that are greater than the maxItems number,
        // but not the last one, because it contains the ellipsis hint for "there are more results".
        this.subMenu.children(":gt(" + (this.maxItems - 1) + ")").filter(":not(:last)").remove();
      }

      if (this.totalCount <= this.maxItems) {
        this.subMenu.children(":last").remove();
      }

      // init the input element
      this.input = jQuery(Tobago.Utils.escapeClientId(this.element.data("tobago-for")));

      if (this.input.data("tobago-suggest") == null) {
        this.input.on(compatibleKeyEvent(), function (event) {

          var handled = false;

          var code = event.which;
          if (code == 0) {
            code = event.keyCode;
          }
          console.info(code); // @DEV_ONLY
          var oldItem;
          var newItem;

          var input = jQuery(this);
          var widget = input.data("tobago-suggest");
          var keyCode = jQuery.ui.keyCode;
          switch (code) {
            case keyCode.ESCAPE:
              input.val(input.data("tobago-last-value"));
              widget._closeMenu();
              // XXX todo
              handled = true;
              break;
            case keyCode.ENTER:
//           case keyCode.NUMPAD_ENTER: todo: hides the 'l' character
              var wasOpen = widget._isMenuOpen();
              widget._closeMenu();
              handled = wasOpen;
              break;
            case keyCode.UP:
              oldItem = widget._selectedItem();
              if (oldItem.length == 1) {
                newItem = oldItem.prev(); // prev()
              } else {
                newItem = widget._lastItem();
              }
              widget._select(oldItem, newItem);
              handled = true;
              widget._openMenu();
              break;
            case keyCode.DOWN:
              oldItem = widget._selectedItem();
              if (oldItem.length == 1) {
                newItem = oldItem.next(); // next()
              } else {
                newItem = widget._firstItem();
              }
              widget._select(oldItem, newItem);
              handled = true;
              widget._openMenu();
              break;
            case keyCode.PAGE_UP:
            case keyCode.PAGE_DOWN:
            case keyCode.END:
            case keyCode.HOME:
            case keyCode.LEFT:
            case keyCode.RIGHT:
              // nothing special to do
              break;
            default:
              console.info("input.val() = " + input.val()); // @DEV_ONLY
              widget._scheduleUpdate();
              break;
          }

          // TODO: case keyCode.TAB:

          return !handled;
        });
      }
      this.input.data("tobago-suggest", this); // set or update

      this.element.css("left", this.input.css("left"));
      this.element.css("top", this.input.css("top"));

      // this must be done after the menu initialization
      this._on(this.input, {
        focus: function (event) {
          this._openMenu();
        },
        blur: function (event) {
          this._closeMenu();
        }});

      // when the input has focus at loading time, open the menu now
      if (this.input.get(0) == document.activeElement) { // has focus
        this._openMenu();
      }
    },

    _destroy: function () {
      this.subMenu.remove();
    },

    _openMenu: function() {
      if (this.input.val().length >= this.minChars) { // check min length
        if (! this._isMenuOpen()) {
          var menuBar = this.element.find(".tobago-menuBar");
          var menu = menuBar.children("li");
          var anchor = menu.children("a");
          Tobago.Menu.switchOn(menuBar, menu);
          anchor.each(Tobago.Menu.open);
          this.subMenu.outerWidth(this.input.outerWidth());
        }
      }
    },

    _closeMenu: function() {
      if (this._isMenuOpen()) {
        var menuBar = this.element.find(".tobago-menuBar");
        Tobago.Menu.switchOff(menuBar);
      }
    },

    _isMenuOpen: function() {
      return this.subMenu.css("visibility") != "hidden";
    },

    _selectedItem: function () {
      return this.subMenu.find("li.tobago-menu-markup-selected:first");
    },

    _scheduleUpdate: function() {
      clearTimeout(this.scheduled);
      this.scheduled = null;
      this.scheduled = this._delay(function () {
        if (this.input.val().length >= this.minChars) { // check min length
          if (this.input.val() != this.input.data("tobago-last-value")) {
            this.input.data("tobago-last-value", this.input.val());
            Tobago.reloadComponent(this.input.get(0), this.element.attr("id"), this.input.attr("id"),
                {createOverlay: false});
          }
        } else {
          this._closeMenu();
        }
      }, this.delay);
    },

    _firstItem: function () {
      return this.subMenu.find("li:first");
    },

    _lastItem: function () {
      return this.subMenu.find("li:last");
    },

    _select: function (oldItem, newItem) {
      if (oldItem.length == 1) {
        oldItem.removeClass("tobago-menu-markup-selected");
        this.input.val(this.input.data("tobago-last-value"));
      }
      if (newItem.length == 1) {
        newItem.addClass("tobago-menu-markup-selected");
        this.input.val(newItem.children("a").html());
      } else {

      }
    }

  });

}(jQuery));
