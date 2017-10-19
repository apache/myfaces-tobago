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

  // fixing fixed header/footer: content should not scroll behind the footer

  var body = Tobago.Utils.selectWithJQuery(elements, "body");
  var headers = Tobago.Utils.selectWithJQuery(elements, ".fixed-top");
  var footers = Tobago.Utils.selectWithJQuery(elements, ".fixed-bottom");

  setMargins(body, headers, footers);

  jQuery(window).resize(function () {
    setMargins(body, headers, footers);
  });
};

var setMargins = function (body, headers, footers) {
  var maxHeaderHeight = getMaxHeaderHeight(headers);
  var maxFooterHeight = getMaxFooterHeight(footers);

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

Tobago.registerListener(Tobago.Layout.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Layout.init, Tobago.Phase.AFTER_UPDATE);
