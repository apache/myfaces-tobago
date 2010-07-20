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

function tobago_switchTab(type, controlId, selectedIndex, size) {
  if ('client' == type) {
    tobago_selectTab(controlId, selectedIndex, size);
  } else if ('reloadPage' == type) {
    tobago_requestTab(controlId, selectedIndex);
  }
}

function tobago_nextTab(type, controlId, size) {
  var hidden = document.getElementById(controlId
      + '__activeIndex' /* TabGroupRenderer.ACTIVE_INDEX_POSTFIX*/);
  var selectedIndex = 0;
  if (hidden) {
    selectedIndex = hidden.value * 1;
  }
  //LOG.error("Selected Index: " + selectedIndex);
  for (i = selectedIndex + 1; i < size; i++) {
    var tab = document.getElementById(controlId + Tobago.SUB_COMPONENT_SEP2 + selectedIndex + Tobago.SUB_COMPONENT_SEP2 + i);
    //LOG.error("Search " + controlId + Tobago.SUB_COMPONENT_SEP2 + selectedIndex + Tobago.SUB_COMPONENT_SEP2 + i);
    if (tab && tab.className.indexOf('tobago-tab-link-markup-disabled') == -1) {
      //LOG.error("Selected Index: " + selectedIndex);
      selectedIndex = i;
      break;
    }
  }
  tobago_switchTab(type, controlId, selectedIndex, size);
}

function tobago_previousTab(type, controlId, size) {
  var hidden = document.getElementById(controlId
      + '__activeIndex' /* TabGroupRenderer.ACTIVE_INDEX_POSTFIX*/);
  var selectedIndex = 0;
  if (hidden) {
    selectedIndex = hidden.value;
  }

  for (i = selectedIndex - 1; i >= 0; i--) {
    var tab = document.getElementById(controlId + Tobago.SUB_COMPONENT_SEP2 + selectedIndex + Tobago.SUB_COMPONENT_SEP2 + i);
    if (tab && tab.className.indexOf('tobago-tab-link-markup-disabled') == -1) {
      selectedIndex = i;
      break;
    }
  }
  tobago_switchTab(type, controlId, selectedIndex, size);
}

function tobago_selectTab(controlId, selectedIndex, size) {
  var hidden = document.getElementById(controlId
      + '__activeIndex' /* TabGroupRenderer.ACTIVE_INDEX_POSTFIX*/);
  if (hidden) {
    hidden.value = selectedIndex;
  }

  for (i = 0; i < size; i++) {
    var tab = document.getElementById(controlId + Tobago.SUB_COMPONENT_SEP2 + i);
    if (tab) {
      if (i == selectedIndex) {
        tab.style.display = 'block';
      } else {
        tab.style.display = 'none';
      }
    }
  }
}

function tobago_requestTab(controlId, selectedIndex) {

  var hidden = document.getElementById(controlId
      + '__activeIndex' /* TabGroupRenderer.ACTIVE_INDEX_POSTFIX*/);
  if (hidden) {
    hidden.value = selectedIndex;
  }

  Tobago.submitAction(null /*todo: source*/, controlId);
}
