/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

function stopHideEvent(id) {
  document.getElementById(id).addEventListener("tobago.dropdown.hide", (event) => {
    event.preventDefault();
  });
}

function observeTobagoSuggest() {
  const observer = new MutationObserver((mutations) => {
    for (const mutation of mutations) {
      stopHideEvent("page:mainForm:suggest");
    }
  });
  observer.observe(document.getElementById("page:mainForm:suggest").parentElement, {childList: true});
}

document.addEventListener("DOMContentLoaded", function (event) {
  stopHideEvent("page:mainForm:button");
  stopHideEvent("page:mainForm:link");
  stopHideEvent("page:mainForm:suggest");
  stopHideEvent("page:mainForm:selectOneList");
  stopHideEvent("page:mainForm:selectManyList");
  observeTobagoSuggest();
});
