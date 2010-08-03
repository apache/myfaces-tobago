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

Tobago.loadPngFix = function() {
  if (this.getBrowser().type == "msie" && this.getBrowser().version <= 6) {
    // fix png images
    var images = document.images;
    for (var i = 0; i < images.length; i++) {
      Tobago.fixPngAlpha(images[i]);
    }
  }
};

Tobago.fixPngAlpha = function(element) {
  if (element.fixPngAlphaApplied != "applied") {
    element.fixPngAlphaApplied = "applied";
    if (this.getBrowser().type == "msie"
        && this.getBrowser().version <= 6
        && element.src.toLowerCase().match(/.*png/)
        && Tobago.isActiveXEnabled()) {
      Tobago.addEventListener(element, 'propertychange', Tobago.propertyChange);
      Tobago.fixImage(element);
    }
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

Tobago.propertyChange = function() {
  if (event.propertyName != "src") {
    return;
  }
  // if not set to blank (to avoid endless loop)
  if (! new RegExp(Tobago.pngFixBlankImage).test(event.srcElement.src)) {
    Tobago.fixImage(event.srcElement);
  }
};

Tobago.fixImage = function(element) {
  element.runtimeStyle.backgroundImage = "none";
  element.runtimeStyle.filter
      = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + element.src + "', sizingMethod='scale')";
  element.src = Tobago.pngFixBlankImage;
};
