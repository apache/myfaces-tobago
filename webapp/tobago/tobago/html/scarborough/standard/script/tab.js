/* Copyright (c) 2003 Atanion GmbH, Germany
   All rights reserved.
   $Id$
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

  submitAction(formId, "");
}