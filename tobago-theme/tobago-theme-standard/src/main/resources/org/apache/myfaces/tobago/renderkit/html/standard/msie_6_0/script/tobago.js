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
 * theme: standard
 * agent: msie_6_0
 */

Tobago.fixPngAlphaAll = function(elements) {
  if (jQuery.browser.msie && parseInt(jQuery.browser.version) <= 6) {
    // fix png images
    var images = Tobago.Utils.selectWidthJQuery(elements, "img[src$='.png']");
    images.each(function() {
      Tobago.fixPngAlpha(this);
    });

    // fix png backgrounds of the labels
    Tobago.Utils.selectWidthJQuery(elements,
        "label.tobago-label-markup-fatal, label.tobago-label-markup-error, " +
        "label.tobago-label-markup-warn, label.tobago-label-markup-info").each(function() {
      Tobago.fixBackgroundPngAlpha(this);
    });
  }
};

Tobago.fixPngAlpha = function(element) {
  if (jQuery.browser.msie
      && parseInt(jQuery.browser.version) <= 6
      && element.src.toLowerCase().match(/.*png/)
      && Tobago.isActiveXEnabled()) {
    Tobago.fixPngAlphaInternal(element);
  }
};

Tobago.isActiveXEnabled = function () {
  try {
    new ActiveXObject("Shell.UIHelper");
  } catch(e) {
    return false;
  }
  return true;
};

Tobago.fixPngAlphaInternal = function(element) {
  element.runtimeStyle.backgroundImage = "none";
  element.runtimeStyle.filter
      = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + element.src + "', sizingMethod='scale')";
  element.src = Tobago.pngFixBlankImage;
};

Tobago.fixBackgroundPngAlpha = function(element) {
  var label = jQuery(element);
  var url = label.css("background-image");
  label.append("<img src='" + url.substring(5, url.length - 2) + "' />");
  Tobago.fixPngAlphaInternal(label.children("img").get(0));
  label.css("background-image", "none");
};

/* TOBAGO-789 */
Tobago.fixSelectionOnFocusIn = function() {
  try {
    var src = window.event.srcElement;
    if (src) {
      src.tmpIndex = src.selectedIndex;
    }
  } catch (e) {
    // ignore
  }
};

/* TOBAGO-789 */
Tobago.fixSelectionOnFocus = function() {
  try {
    var src = window.event.srcElement;
    if (src) {
      src.selectedIndex = src.tmpIndex;
    }
  } catch (e) {
    // ignore
  }
};

Tobago.registerCallback(Tobago.fixPngAlphaAll, Tobago.Phase.WINDOW_LOAD);
Tobago.registerCallback(Tobago.fixPngAlphaAll, Tobago.Phase.AFTER_UPDATE);
