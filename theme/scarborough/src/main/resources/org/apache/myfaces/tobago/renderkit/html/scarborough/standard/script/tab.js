/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

function tobago_selectTab(controlId, selectedIndex, size) {
  var hidden = document.getElementById(controlId
      + '::activeIndex' /* TabGroupRenderer.ACTIVE_INDEX_POSTFIX*/);
  if (hidden) {
    hidden.value = selectedIndex;
  }

  for (i = 0; i < size; i++) {
    var tab = document.getElementById(controlId + '.' + i);
    if (i == selectedIndex) {
      tab.style.display='block';
    } else {
      tab.style.display='none';
    }
  }
}

function tobago_requestTab(controlId, selectedIndex, formId) {

  var hidden = document.getElementById(controlId
      + '::activeIndex' /* TabGroupRenderer.ACTIVE_INDEX_POSTFIX*/);
  if (hidden) {
    hidden.value = selectedIndex;
  }

  Tobago.submitAction(controlId);
}