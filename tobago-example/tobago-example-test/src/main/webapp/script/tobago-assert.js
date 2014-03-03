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
 * Utilities to make client side tests easier.
 */

var TobagoAssert = {

  assertLeft:function (elementOrId, left, epsilon) {
    var element = TobagoAssert.jQueryElement(elementOrId);
    epsilon = epsilon != null ? epsilon : 0;
    var offsetLeft = element.offset().left;
    if (Math.abs(offsetLeft - left) > epsilon) {
      TobagoAssert.fail("left", element, left, offsetLeft);
    }
  },

  assertTop:function (elementOrId, top, epsilon) {
    var element = TobagoAssert.jQueryElement(elementOrId);
    epsilon = epsilon != null ? epsilon : 0;
    var offsetTop = element.offset().top;
    if (Math.abs(offsetTop - top) > epsilon) {
      TobagoAssert.fail("top", element, top, offsetTop);
    }
  },

  assertWidth:function (elementOrId, width, epsilon) {
    var element = TobagoAssert.jQueryElement(elementOrId);
    epsilon = epsilon != null ? epsilon : 0;
    var offsetWidth = element.get(0).offsetWidth;
    if (Math.abs(offsetWidth - width) > epsilon) {
      TobagoAssert.fail("width", element, width, offsetWidth);
    }
  },

  assertHeight:function (elementOrId, height, epsilon) {
    var element = TobagoAssert.jQueryElement(elementOrId);
    epsilon = epsilon != null ? epsilon : 0;
    var offsetHeight = element.get(0).offsetHeight;
    if (Math.abs(offsetHeight - height) > epsilon) {
      TobagoAssert.fail("height", element, height, offsetHeight);
    }
  },

  fail: function(name, element, expected, actual) {
    var text;
    if (element) {
      element.overlay({error: true, ajax: true});
      text = "The element '" + element.get(0).tagName + "' with id='" + element.attr("id")
          + "' has wrong " + name + ": expected=" + expected + " actual=" + actual;
      var overlay = element.data("tobago-overlay").overlay;
      overlay.attr("title", (overlay.attr("title") === undefined ? "" : overlay.attr("title") + "\n" ) + text);
      console.error(text);
    } else {
      text = name + ": expected=" + expected + " actual=" + actual;
      console.error(text);
    }
  },

  assertLayout:function (elementOrId, left, top, width, height) {
    var element = TobagoAssert.jQueryElement(elementOrId);
    TobagoAssert.assertLeft(element, left);
    TobagoAssert.assertTop(element, top);
    TobagoAssert.assertWidth(element, width);
    TobagoAssert.assertHeight(element, height);
  },

  assertAbsence:function (id) {
    var element = document.getElementById(id);
    if (element != null) {
      console.error("The element with id=" + id + " was found, but should not!");
    }
  },

  assertAttribute:function (elementOrId, attribute, expected) {
    if ("value" == attribute) {
      console.error("The assertAttribute() is not allowed for the value attribute, please use assertValue() instead.");
    }
    var element = TobagoAssert.jQueryElement(elementOrId);
    if (element.attr(attribute) != expected) {
      console.error("The attribute '" + attribute + "' of element with id=" + element.attr('id')
          + " is '" + element.attr(attribute) + "', but expected was '" + expected + "'.");
    }
  },

  assertValue:function (elementOrId, expected) {
    var element = TobagoAssert.jQueryElement(elementOrId);
    console.assert(element.val() == expected,
        "The value of element with id=" + element.attr('id')
        + " is '" + element.val() + "', but expected was '" + expected + "'.");
  },

  assertContent:function (elementOrId, expected) {
    var element = TobagoAssert.jQueryElement(elementOrId);
    if (element.html() != expected) {
      console.error("The content of element with id=" + element.attr('id')
          + " is '" + element.html() + "', but expected was '" + expected + "'.");
    }
  },

  /**
   * Util to get an jQuery object from a plain id string (unescaped) or a jQuery object.
   */
  jQueryElement:function (elementOrId) {
    var element;
    if (typeof elementOrId == "string") {
      element = jQuery(Tobago.Utils.escapeClientId(elementOrId));
    } else { // JQuery Object Array
      element = elementOrId;
    }
    return element;
  }
};

(function ($) {

  $.widget("test.assertLayout", {

    _create: function () {

      var epsilon = this.element.data("assert-epsilon");

      var left = this.element.data("assert-left");
      if (left != null) {
        TobagoAssert.assertLeft(this.element, left, epsilon);
      }
      var top = this.element.data("assert-top");
      if (top != null) {
        TobagoAssert.assertTop(this.element, top, epsilon);
      }
      var width = this.element.data("assert-width");
      if (width != null) {
        TobagoAssert.assertWidth(this.element, width, epsilon);
      }
      var height = this.element.data("assert-height");
      if (height != null) {
        TobagoAssert.assertHeight(this.element, height, epsilon);
      }
    },

    _destroy: function () {
    }

  });

}(jQuery));

Tobago.registerListener(function() {
    jQuery("[data-assert-width],[data-assert-height],[data-assert-left],[data-assert-top]").assertLayout();
}, Tobago.Phase.DOCUMENT_READY);
