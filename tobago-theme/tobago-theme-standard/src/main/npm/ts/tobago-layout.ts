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

Tobago4.Layout = {};

Tobago4.Layout.init = function (elements) {

  elements = elements.jQuery ? elements : jQuery(elements); // fixme jQuery -> ES5
  Tobago4.Layout.initSplitLayout(elements);

  // fixing fixed header/footer: content should not scroll behind the footer

  var body = Tobago4.Utils.selectWithJQuery(elements, "body");
  var headers = Tobago4.Utils.selectWithJQuery(elements, ".fixed-top");
  var footers = Tobago4.Utils.selectWithJQuery(elements, ".fixed-bottom");

  setMargins(body, headers, footers);

  var lastMaxHeaderHeight = 0;
  var lastMaxFooterHeight = 0;
  jQuery(window).resize(function () {
    var maxHeaderHeight = getMaxHeaderHeight(headers);
    var maxFooterHeight = getMaxFooterHeight(footers);

    if (maxHeaderHeight !== lastMaxHeaderHeight
        || maxFooterHeight !== lastMaxFooterHeight) {
      setMargins(body, headers, footers);

      lastMaxHeaderHeight = maxHeaderHeight;
      lastMaxFooterHeight = maxFooterHeight;
    }
  });


  function setMargins(body, headers, footers) {
    var maxHeaderHeight = getMaxHeaderHeight(headers);
    var maxFooterHeight = getMaxFooterHeight(footers);

    if (maxHeaderHeight > 0) {
      body.css("margin-top", maxHeaderHeight + "px");
    }
    if (maxFooterHeight > 0) {
      body.css("margin-bottom", maxFooterHeight + "px");
    }
  }
};

var getMaxHeaderHeight = function (headers) {
  var maxHeaderHeight = 0;
  headers.each(function () {
    var height = jQuery(this).outerHeight(true);
    if (height > maxHeaderHeight) {
      maxHeaderHeight = height;
    }
  });
  return maxHeaderHeight;
};

var getMaxFooterHeight = function (footers) {
  var maxFooterHeight = 0;
  footers.each(function () {
    var height = jQuery(this).outerHeight(true);
    if (height > maxFooterHeight) {
      maxFooterHeight = height;
    }
  });
  return maxFooterHeight;
};

Tobago4.Layout.initSplitLayout = function (elements) {
  var splitter;
  splitter = Tobago4.Utils.selectWithJQuery(elements, ".tobago-splitLayout-horizontal");
  splitter.each(function () {
    var splitter = jQuery(this);
    splitter.on("mousedown", {splitter: splitter}, function (event) {
      var prev = splitter.prevAll(":not(style):last");
      var width = prev.outerWidth();
      console.info("initial width = " + width);
      prev.css("width", width + "px");
      prev.css({"flex-grow": "inherit", "flex-basis": "auto"});
      jQuery(document).on(
          "mousemove",
          {offset: event.pageX - width, splitter: splitter},
          Tobago4.Layout.moveHorizontally);
      jQuery(document).on(
          "mouseup",
          Tobago4.Layout.stop);
    });
  });
  splitter = Tobago4.Utils.selectWithJQuery(elements, ".tobago-splitLayout-vertical");
  splitter.each(function () {
    var splitter = jQuery(this);
    splitter.on("mousedown", {splitter: splitter}, function (event) {
      var prev = splitter.prevAll(":not(style):last");
      var height = prev.outerHeight();
      console.info("initial height = " + height);
      prev.css("height", height + "px");
      prev.css({"flex-grow": "inherit", "flex-basis": "auto"});
      jQuery(document).on(
          "mousemove",
          {offset: event.pageY - height, splitter: splitter},
          Tobago4.Layout.moveVertically);
      jQuery(document).on(
          "mouseup",
          Tobago4.Layout.stop);
    });
  });
};

Tobago4.Layout.moveHorizontally = function (event) {
  console.info("" + event.pageX + " " + event.data.offset);
  var prev = event.data.splitter.prev();
  prev.width(event.pageX - event.data.offset + "px");
};

Tobago4.Layout.moveVertically = function (event) {
  console.info("" + event.pageY + " " + event.data.offset);
  var prev = event.data.splitter.prev();
  prev.height(event.pageY - event.data.offset + "px");
};

Tobago4.Layout.stop = function (event) {
  jQuery(document).off("mousemove", Tobago4.Layout.moveHorizontally);
  jQuery(document).off("mousemove", Tobago4.Layout.moveVertically);
  jQuery(document).off("mouseup", Tobago4.Layout.stop);
};

Tobago.Listener.register(Tobago4.Layout.init, Tobago.Phase.DOCUMENT_READY);
Tobago.Listener.register(Tobago4.Layout.init, Tobago.Phase.AFTER_UPDATE);
