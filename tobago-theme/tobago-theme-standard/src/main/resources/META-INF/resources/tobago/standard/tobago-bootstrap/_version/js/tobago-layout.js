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

Tobago.Layout = {};

Tobago.Layout.init = function (elements) {

  Tobago.Layout.initSplitLayout(elements);

  // fixing fixed header/footer: content should not scroll behind the footer

  var body = Tobago.Utils.selectWithJQuery(elements, "body");
  var headers = Tobago.Utils.selectWithJQuery(elements, ".fixed-top");
  var footers = Tobago.Utils.selectWithJQuery(elements, ".fixed-bottom");

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
};

var setMargins = function (body, headers, footers, currentHeaderHeight, currentFooterHeight) {
  var maxHeaderHeight = currentHeaderHeight ? currentHeaderHeight : getMaxHeaderHeight(headers);
  var maxFooterHeight = currentFooterHeight ? currentFooterHeight : getMaxFooterHeight(footers);

  if (maxHeaderHeight > 0) {
    body.css("margin-top", maxHeaderHeight + "px");
  }
  if (maxFooterHeight > 0) {
    body.css("margin-bottom", maxFooterHeight + "px");
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

Tobago.Layout.initSplitLayout = function (elements) {
  var splitter;
  splitter = Tobago.Utils.selectWithJQuery(elements, ".tobago-splitLayout-horizontal");
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
          Tobago.Layout.moveHorizontally);
      jQuery(document).on(
          "mouseup",
          Tobago.Layout.stop);
    });
  });
  splitter = Tobago.Utils.selectWithJQuery(elements, ".tobago-splitLayout-vertical");
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
          Tobago.Layout.moveVertically);
      jQuery(document).on(
          "mouseup",
          Tobago.Layout.stop);
    });
  });
};

Tobago.Layout.moveHorizontally = function (event) {
  console.info("" + event.pageX + " " + event.data.offset);
  var prev = event.data.splitter.prev();
  prev.width(event.pageX - event.data.offset + "px");
};

Tobago.Layout.moveVertically = function (event) {
  console.info("" + event.pageY + " " + event.data.offset);
  var prev = event.data.splitter.prev();
  prev.height(event.pageY - event.data.offset + "px");
};

Tobago.Layout.stop = function (event) {
  jQuery(document).off("mousemove", Tobago.Layout.moveHorizontally);
  jQuery(document).off("mousemove", Tobago.Layout.moveVertically);
  jQuery(document).off("mouseup", Tobago.Layout.stop);
};

Tobago.registerListener(Tobago.Layout.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Layout.init, Tobago.Phase.AFTER_UPDATE);
