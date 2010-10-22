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

function checkLeft(element, left) {
  var offsetLeft = element.offsetLeft;
  var parent = element;
  while (parent.offsetParent != null) {
    parent = parent.offsetParent;
    offsetLeft += parent.offsetLeft;
  }
  if (offsetLeft != left) {
    LOG.error("The element '" + element.tagName + "' with id='" + element.id + "' has wrong left: expected=" + left + " actual=" + offsetLeft);
  }
}

function checkTop(element, top) {
  var offsetTop = element.offsetTop;
  var parent = element;
  while (parent.offsetParent != null) {
    parent = parent.offsetParent;
    offsetTop += parent.offsetTop;
  }
  if (offsetTop != top) { 
    LOG.error("The element '" + element.tagName + "' with id='" + element.id + "' has wrong top: expected=" + top + " actual=" + offsetTop);
  }
}

function checkWidth(element, width) {
  var offsetWidth = element.offsetWidth;
  if (offsetWidth != width) {
    LOG.error("The element '" + element.tagName + "' with id='" + element.id + "' has wrong width: expected=" + width + " actual=" + offsetWidth);
  }
}

function checkHeight(element, height) {
  var offsetHeight = element.offsetHeight;
  if (offsetHeight != height) {
    LOG.error("The element '" + element.tagName + "' with id='" + element.id + "' has wrong height: expected=" + height + " actual=" + offsetHeight);
  }
}

function checkLayout(elementOrId, left, top, width, height) {
  var element;
  if (typeof elementOrId == "string") {
    element = document.getElementById(elementOrId);
    checkLeft(element, left);
    checkTop(element, top);
    checkWidth(element, width);
    checkHeight(element, height);
  } else { // JQuery Object Array
    element = elementOrId.get(0);
    checkLeft(element, left);
    checkTop(element, top);
    checkWidth(element, width);
    checkHeight(element, height);
  }
}

function checkAbsence(id) {
  var element = document.getElementById(id);
  if (element != null) {
    LOG.error("The element with id=" + id + " was found, but should not!");
  }
}
