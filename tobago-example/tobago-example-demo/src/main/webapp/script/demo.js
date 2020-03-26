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

Demo = {};

(function ($) {
  $.widget("demo.alert", {
    _create: function () {
      this._on({
        click: function (event) {
          var text = this.element.data("alert-text");
          alert(text);
        }
      });
    }
  });
}(jQuery));

var initAlert = function () {
  jQuery("[data-alert-text]").alert();
};

Tobago.registerListener(initAlert, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(initAlert, Tobago.Phase.AFTER_UPDATE);

var initInspect = function (elements) {

  jQuery("code").find("br").replaceWith("\n");

  // var tobagoElements = Tobago.Utils.selectWithJQuery(elements, ".tobago-in,.tobago-out,.tobago-date");
  var tobagoElements = Tobago.Utils.selectWithJQuery(elements, ".tobago-flexLayout");

  // do highlighting with hovering only in the content-area
  tobagoElements = tobagoElements.filter(function () {
    return jQuery(this).parents("#page\\:content").length === 1;
  });

  tobagoElements.hover(function () {

    // clear old selections:
    jQuery(".demo-selected").removeClass("demo-selected");

    var element = jQuery(this);
    element.addClass("demo-selected");

    var clientId = element.closest("[id]").attr("id");
    var id = clientId.substr(clientId.lastIndexOf(":") + 1);

    var source = jQuery("#demo-view-source");

    var span = source.find("span.token.attr-value").filter(function () {
      return jQuery(this).prev().text() + jQuery(this).text() === 'id=' + '"' + id + '"';
    });
    var tag = span.parent();
    tag.addClass("demo-selected");
  });
};

Tobago.registerListener(initInspect, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(initInspect, Tobago.Phase.AFTER_UPDATE);

Demo.prismHighlight = function (elements) {
  // call highlighting again. (is called for all, not only for the elements, because it's easier to implement.)
  Prism.highlightAll();
};

Tobago.registerListener(Demo.prismHighlight, Tobago.Phase.AFTER_UPDATE);

var initTestLinks = function () {
  var $runLink = jQuery("#page\\:header\\:runtest");
  var $closeLink = jQuery("#page\\:header\\:closetest");

  if (jQuery(parent.document.getElementById("qunit")).length) {
    $runLink.hide();
    $closeLink.attr("onclick", "window.top.location.href = location.href");
  } else {
    $closeLink.hide();
  }
};

Tobago.registerListener(initTestLinks, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(initTestLinks, Tobago.Phase.AFTER_UPDATE);

var initTestframe = function () {
  jQuery("#page\\:testframe").attr("onload", "this.height = this.contentWindow.jQuery('body').prop('scrollHeight');");
};

Tobago.registerListener(initTestframe, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(initTestframe, Tobago.Phase.AFTER_UPDATE);

Demo.initGoogleSearch = function () {
  var $input = jQuery("#page\\:search\\:searchField");
  var $button = jQuery("#page\\:search\\:searchCommand");

  var search = "+site%3Atobago-vm.apache.org+demo-4";
  $input.change(function () {
    $button.attr("href", "https://www.google.com/search?q=" + encodeURI($input.val()) + search);
  });
  $input.keypress(function (e) {
    if (e.which === 13) {
      console.log("ENTER");
      window.location.href = "https://www.google.com/search?q=" + encodeURI($input.val()) + search;
    }
  });
};

Tobago.registerListener(Demo.initGoogleSearch, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Demo.initGoogleSearch, Tobago.Phase.AFTER_UPDATE);
