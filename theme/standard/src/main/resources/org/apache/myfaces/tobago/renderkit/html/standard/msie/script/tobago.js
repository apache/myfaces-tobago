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

Tobago.loadPngFix = function() {
  var images = document.getElementsByTagName("img");
  var supported = /MSIE (5\.5)|[6789]/.test(navigator.userAgent)
      && navigator.platform == "Win32"
      && Tobago.isActiveXEnabled();
  if (! supported) {
    return;
  }
  for (var i = 0; i < images.length; i++) {
    var image = images[i];
    Tobago.fixImage(image);
    Tobago.addEventListener(image, 'propertyChanged', Tobago.propertyChanged);
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

Tobago.propertyChanged = function() {
  var pName = event.propertyName;
  if (pName != "src") return;
  // if not set to blank
  if (! new RegExp(Tobago.pngFixBlankImage).test(src)) {
    Tobago.fixImage(this);
  }
};

Tobago.fixImage = function(element) {
  // get src
  var src = element.src;
  // check for real change

  if (src == element.realSrc) {
    element.src = Tobago.pngFixBlankImage;
    return;
  }

  if (! new RegExp(Tobago.pngFixBlankImage).test(src)) {
    // backup old src
    element.realSrc = src;
  }

  // test for png
  if (element.realSrc != null &&
      /\.png$/.test(element.realSrc.toLowerCase())) {
    // get width and height of old src
    var origWidth = element.clientWidth;
    var origHeight = element.clientHeight;

    // set blank image
    element.src = Tobago.pngFixBlankImage;
    // set filter

    element.runtimeStyle.filter
        = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"
        + src + "',sizingMethod='scale')";
    element.style.width = origWidth + 'px';
    element.style.height = origHeight + 'px';

  } else {
    // remove filter
    element.runtimeStyle.filter = "";
  }
};

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