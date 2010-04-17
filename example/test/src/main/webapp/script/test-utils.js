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

function checkLeft(id, left) {
  var offsetLeft = document.getElementById(id).offsetLeft;
  if (offsetLeft != left) {
    LOG.error("The element with id=" + id + " has wrong left: expected=" + left
        + " actual=" + offsetLeft);
  }
}

function checkTop(id, top) {
  var offsetTop = document.getElementById(id).offsetTop;
  if (offsetTop != top) {
    LOG.error("The element with id=" + id + " has wrong top: expected=" + top
        + " actual=" + offsetTop);
  }
}

function checkWidth(id, width) {
  var offsetWidth = document.getElementById(id).offsetWidth;
  if (offsetWidth != width) {
    LOG.error("The element with id=" + id + " has wrong width: expected=" + width
        + " actual=" + offsetWidth);
  }
}

function checkHeight(id, height) {
  var offsetHeight = document.getElementById(id).offsetHeight;
  if (offsetHeight != height) {
    LOG.error("The element with id=" + id + " has wrong height: expected=" + height
        + " actual=" + offsetHeight);
  }
}

function checkLayout(id, left, top, width, height) {
  checkLeft(id, left);
  checkTop(id, top);
  checkWidth(id, width);
  checkHeight(id, height);
}
