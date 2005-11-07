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

var activeElement = null;
var oldX = 0;
var newWidth = 0;

function initSheet(sheetId) {
  //PrintDebug("initSheet(" + sheetId +")");
  var i = 0;
  var element = document.getElementById(sheetId + "_header_div");
  if (element) {
    addEventListener(element, "mousemove", doResize);
    addEventListener(element, "mouseup", endResize);
    element = document.getElementById(sheetId + "_data_div");
    addEventListener(element, "scroll", doScroll);
	  var resizer = document.getElementById(sheetId + "_header_resizer_" + i++ );
	  while (resizer) {
      if (resizer.className.match(/tobago-sheet-header-resize-cursor/)) {
	      addEventListener(resizer, "click", stopEventPropagation);
	      addEventListener(resizer, "mousedown", beginResize);
      }
      resizer = document.getElementById(sheetId + "_header_resizer_" + i++ );
    }

    adjustHeaderDiv(sheetId);
    adjustResizer(sheetId);

    setupHeader(sheetId);
  }
  else {
    PrintDebug("Kann sheet mit id=\"" + sheetId + "\" nicht finden!");
  }

  var sheet = document.getElementById(sheetId + "_outer_div");
  var firstSelectionRow = getFirstSelectionRow(sheetId);
  if (firstSelectionRow) {
    sheet.tobagoLastClickedRowId = firstSelectionRow.id;
  }
  addSelectionListener(sheetId);
  adjustScrollBars(sheetId);
}

function addSelectionListener(sheetId) {
  var row = getFirstSelectionRow(sheetId);
  if (row) {
    var i = row.id.substring(row.id.lastIndexOf("_data_tr_") + 9);
    i++;
    while (row) {
//       PrintDebug("rowId = " + row.id + "   next i=" + i);
      addEventListener(row, "click", doSelection);
      row = document.getElementById(sheetId + "_data_tr_" + i++ );
    }
    //PrintDebug("preSelected rows = " + document.getElementById(sheetId + "::selected").value);
  }
}

function getFirstSelectionRow(sheetId) {
  var element = document.getElementById(sheetId + "_data_row_0_column0");// data div
  while (element && element.id.search(new RegExp("^" + sheetId + "_data_tr_\\d+$")) == -1) {
    //PrintDebug("element id = " + element.id);
    element = element.parentNode;
  }
  //PrintDebug("element id = " + element.id);
  return element;
}


function doSelection(event) {
  if (! event) {
    event = window.event;
  }

  clearSelection();

  //PrintDebug("event.ctrlKey = " + event.ctrlKey);
  //PrintDebug("event.shiftKey = " + event.shiftKey);

  var srcElement;
  if (event.target) {
    srcElement = event.target;
  }
  else {
    srcElement = event.srcElement;
  }
//  PrintDebug("srcElement = " + srcElement.tagName);
  if (! isInputElement(srcElement.tagName)) {


    var dataRow = getActiveElement(event);
    while (dataRow.id.search(new RegExp("_data_tr_\\d+$")) == -1) {
      dataRow = dataRow.parentNode;
    }
    var rowId = dataRow.id;
    //PrintDebug("rowId = " + rowId);
    var sheetId = rowId.substring(0, rowId.lastIndexOf("_data_tr_"));
    var hidden = document.getElementById(sheetId + "::selected");
    var selected = hidden.value;
    var sheet = document.getElementById(sheetId + "_outer_div");
    var rowIndex = rowId.substring(rowId.lastIndexOf("_data_tr_") + 9);
    var selector = document.getElementById(sheetId + "_data_row_selector_" + rowIndex);

    //PrintDebug("last id = " + sheet.tobagoLastClickedRowId);

    if (! event.ctrlKey && ! selector) {
      // clearAllSelections();
      hidden.value = "";
    }

    if (event.shiftKey) {
      selectRange(dataRow, sheet.tobagoLastClickedRowId, hidden);
    }
    else {
      sheet.tobagoLastClickedRowId = rowId;
      tobagoSheetToggleSelectionForRow(dataRow, hidden);
    }
    updateSelectionView(sheetId, hidden.value);
    //PrintDebug("selected rows = " + hidden.value);
  }
}

function isInputElement(tagName) {
  if (   tagName == "INPUT"
      || tagName == "TEXTAREA"
      || tagName == "A"
      || tagName == "BUTTON"
      ) {
    return true;
  }
  return false;
}

function updateSelectionView(sheetId, selected) {
  if (! selected) {
    var hidden =
      document.getElementById(sheetId + getSubComponentSeparator() + "selected");
    if (hidden) {
      selected = hidden.value;
    }
  }
  var row = getFirstSelectionRow(sheetId);
  if (row) {
    var outerDiv = document.getElementById(sheetId + "_outer_div");
    var i = row.id.substring(row.id.lastIndexOf("_data_tr_") + 9);
  }  
  while (row) {

    var selector = document.getElementById(sheetId + "_data_row_selector_" + i);
    var re = new RegExp("," + i +",");
    var classes = row.className;
    var img = document.getElementById(sheetId + "_data_row_selector_" + i);
    if (selected.search(re) == -1) { // not selected: remove selection class
      row.className = classes.replace(/tobago-sheet-row-selected/g, "");

      if (img && (!selector || !selector.src.match(/Disabled/))) {
        img.src = outerDiv.uncheckedImage;
      }
    }
    else {  // selected: check selection class
      if (classes.search(/tobago-sheet-row-selected/) == -1) {
        row.className = classes + " tobago-sheet-row-selected ";
      }
      if (img && (!selector || !selector.src.match(/Disabled/))) {
        img.src = outerDiv.checkedImage;
      }
    }
    row = document.getElementById(sheetId + "_data_tr_" + ++i );
  }
}

function selectRange(dataRow, lastId, hidden) {
  var lastRow = document.getElementById(lastId);
  var firstIndex = lastRow.id.substring(lastRow.id.lastIndexOf("_data_tr_") + 9) - 0;
  var lastIndex  = dataRow.id.substring(dataRow.id.lastIndexOf("_data_tr_") + 9) - 0;
  var sheetId = hidden.id.substring(
      0,hidden.id.lastIndexOf(getSubComponentSeparator() + "selected"));

  var start;
  var end;
  if (firstIndex > lastIndex) {
    start = lastIndex;
    end = firstIndex;
  }
  else {
    start = firstIndex;
    end = lastIndex;
  }
  for (var i = start; i <= end; i++) {
    var re = new RegExp("," + i + ",");
    if (hidden.value.search(re) == -1) {
      var selector = document.getElementById(sheetId + "_data_row_selector_" + i);
      if (!selector || !selector.src.match(/Disabled/)) {
        hidden.value = hidden.value + "," + i + ",";
      }
    }
  }
}

function tobagoSheetToggleSelectionForRow(dataRow, hidden) {
  var rowIndex = dataRow.id.substring(dataRow.id.lastIndexOf("_data_tr_") + 9);
  tobagoSheetToggleSelection(rowIndex, hidden);
}

//function tobagoSheetToggleSelectionState(sheetId, rowIndex) {
//  var hidden
//  = document.getElementById(sheetId + getSubComponentSeparator() + "selected");
// tobagoSheetToggleSelection(rowIndex, hidden);
//  updateSelectionView(sheetId, hidden.value);
//}
function tobagoSheetToggleSelectionState(event) {
  if (! event) {
    event = window.event;
  }


  var imgElement;
  if (event.target) {
    imgElement = event.target;
  }
  else {
    imgElement = event.srcElement;
  }

  var sheetId = imgElement.id.substr(0, imgElement.id.lastIndexOf("_data_row_selector_"));
  var rowIndex = imgElement.id.substr(
      imgElement.id.lastIndexOf("_data_row_selector_") + 19) - 0;
  var row = document.getElementById(sheetId + "_data_tr_" + rowIndex);
  var sheet = document.getElementById(sheetId + "_outer_div");
  var hidden
    = document.getElementById(sheetId + getSubComponentSeparator() + "selected");

  if (event.shiftKey) {
      selectRange(row, sheet.tobagoLastClickedRowId, hidden);
  }
  else {
    tobagoSheetToggleSelection(rowIndex, hidden);
  }
  updateSelectionView(sheetId, hidden.value);
}

function tobagoSheetToggleSelection(rowIndex, hidden) {
  var sheetId = hidden.id.substring(
      0,hidden.id.lastIndexOf(getSubComponentSeparator() + "selected"));
  document.getElementById(sheetId + "_outer_div").tobagoLastClickedRowId
      = document.getElementById(sheetId + "_data_tr_" + rowIndex).id;
  var selector = document.getElementById(sheetId + "_data_row_selector_" + rowIndex);
  if (!selector || !selector.src.match(/Disabled/)) {
    var re = new RegExp("," + rowIndex + ",");
    if (hidden.value.search(re) != -1) {
      hidden.value = hidden.value.replace(re, "");
    }
    else {
      hidden.value = hidden.value + "," + rowIndex + ",";
    }
  }
}



function doScroll(event) {
  if (! event) {
    event = window.event;
  }
  var dataPanel = getActiveElement(event);
  var sheetId = dataPanel.id.substring(0, dataPanel.id.lastIndexOf("_data_div"));
  var headerPanel = document.getElementById(sheetId + "_header_div");
  //PrintDebug("header / data  " + headerPanel.scrollLeft + "/" + dataPanel.scrollLeft);
  headerPanel.scrollLeft = dataPanel.scrollLeft;
  //PrintDebug("header / data  " + headerPanel.scrollLeft + "/" + dataPanel.scrollLeft);
  //PrintDebug("----------------------------------------------");
}


function beginResize(event) {
  if (! event) {
    event = window.event;
  }
  setActiveElement(event);
  if (activeElement) {
    oldX = event.clientX;
    var box = getHeaderBox(activeElement);
    var elementWidth = box.style.width;
    newWidth = elementWidth.substring(0,elementWidth.length-2);
  }
}

function doResize(event) {
  if (! event) {
    event = window.event;
  }

  element = activeElement;
  if (element) {
    var box = getHeaderBox(element);
    var elementWidth = box.style.width;
    var elementWidthPx = elementWidth.substring(0,elementWidth.length-2);
    var divX = event.clientX - oldX;
    newWidth = elementWidthPx-0 + divX;
    if (newWidth < 10) {
      newWidth = 10;
    } else {
      oldX = event.clientX;
    }
    box.style.width = newWidth + "px";
  }
}

function endResize(event) {
  if (! event) {
    event = window.event;
  }
  if (activeElement) {
    var i = 0;
    var columnNr = activeElement.id.substring(activeElement.id.lastIndexOf("_")+1, activeElement.id.length);
    var sheetId = activeElement.id.substring(0, activeElement.id.lastIndexOf("_header_resizer"));
    var row = 0;
    var cell = document.getElementById(sheetId + "_data_row_" + i++ + "_column" + columnNr);
    while (cell) {
      cell.style.width = newWidth + "px";
      cell = document.getElementById(sheetId + "_data_row_" + i++ + "_column" + columnNr);
    }

    adjustScrollBars(sheetId);
    adjustHeaderDiv(sheetId);
    adjustResizer(sheetId);
    storeSizes(sheetId);
    activeElement = null;
  }
}

function storeSizes(sheetId) {
  var index = 0;
  var header = document.getElementById(sheetId + "_header_box_" + index++);
  var widths = "";
  while (header) {
    width = header.style.width.replace(/px/, "");
    widths = widths + "," + width;
    header = document.getElementById(sheetId + "_header_box_" + index++);
  }

  var hidden = document.getElementById(sheetId + "::widths");
  hidden.value = widths;
}

function adjustResizer(sheetId) {
  // opera needs this
  if (opera) {
    var position = 5;
    var index = 0;
    var resizer = document.getElementById(sheetId + "_header_resizer_" + index);
    while (resizer) {
      resizer.style.right = - position;
      var headerBox = document.getElementById(sheetId + "_header_box_" + index++);
      var width = headerBox.style.width.replace(/px/, "") - 0;
      position += width;
      resizer = document.getElementById(sheetId + "_header_resizer_" + index);
    }
  }
}

function adjustScrollBars(sheetId) {
  var dataFiller = document.getElementById(sheetId + "_data_row_0_column_filler");
  if (dataFiller) {
    dataFiller.style.width = "0px";
    var contentDiv = document.getElementById(sheetId + "_data_div");
    var clientWidth = contentDiv.clientWidth;

    if (contentDiv.scrollWidth <= clientWidth) {
      var width = 0;
      var i = 0;
      var cellDiv = document.getElementById(sheetId + "_data_row_0_column" + i++);
      while (cellDiv) {
        var tmp = cellDiv.style.width.replace(/px/, "") - 0 ;
        width += tmp;
        cellDiv = document.getElementById(sheetId + "_data_row_0_column" + i++);
      }
      dataFiller.style.width = Math.max((clientWidth - width), 0) + "px";
    }
  }
}

function adjustHeaderDiv(sheetId) {
  var contentDiv = document.getElementById(sheetId + "_data_div");
  var contentWidth = contentDiv.style.width.replace(/px/, "") - 0;
  var clientWidth = contentDiv.clientWidth;
    var boxSum = 0;
    var idx = 0;
    var box = document.getElementById(sheetId + "_header_box_" + idx++);
    while (box) {
      boxSum += (box.style.width.replace(/px/, "") - 0);
      box = document.getElementById(sheetId + "_header_box_" + idx++);
    }
  if (clientWidth == 0) {
    PrintDebug("clientWidth 1 = " + clientWidth);
    clientWidth = Math.min(contentWidth, boxSum);
    PrintDebug("clientWidth 2 = " + clientWidth);
  }
  var headerDiv = document.getElementById(sheetId + "_header_div");
  var minWidth = contentWidth - getScrollbarWidth(); // div width - scrollbar width
  minWidth = Math.max(minWidth, 0); // not less than 0
  headerDiv.style.width = Math.max(clientWidth, minWidth);
  var fillBox = document.getElementById(sheetId + "_header_box_filler");
  fillBox.style.width = Math.max(headerDiv.style.width.replace(/px/, "") - boxSum, 0);
//  PrintDebug("adjustHeaderDiv(" + sheetId + ") : clientWidth = " + clientWidth + " :: width => " + headerDiv.style.width);
  //headerDiv.style.width = clientWidth;
}

function getScrollbarWidth() {
  if (gecko) {
    return 16;
  }
  else {
    return 17;
  }
}

function setupHeader(sheetId) {
  var headerBox = document.getElementById(sheetId + "_header_box_0");
  if (headerBox) {

    if (opera) {
    headerBox.style.marginLeft = "0px";
    }

    var width = headerBox.style.width;
    var tmpWidth = width.replace(/px/, "");
    headerBox.style.width = tmpWidth -0 + 10 + "px";
    headerBox.style.width = width;
  }
}

function setActiveElement(event) {
  if (! event) {
    event = window.event;
  }
  activeElement = getActiveElement(event);
}

function getHeaderBox(element) {
    var boxId = element.id.replace(/_header_resizer_/, "_header_box_");
    return document.getElementById(boxId);
}

function tobagoSheetSetUncheckedImage(sheetId, image) {
  var div = document.getElementById(sheetId + "_outer_div");
  if (div) {
    div.uncheckedImage = image;
  }
}

function tobagoSheetSetCheckedImage(sheetId, image) {
  var div = document.getElementById(sheetId + "_outer_div");
  if (div) {
    div.checkedImage = image;
  }
}

function tobagoSheetSelectAll(sheetId) {
  var hidden
    = document.getElementById(sheetId + getSubComponentSeparator() + "selected");
  var row = getFirstSelectionRow(sheetId);
  var i = row.id.substring(row.id.lastIndexOf("_data_tr_") + 9);
  while (row) {
    var selector = document.getElementById(sheetId + "_data_row_selector_" + i);
    if (!selector || !selector.src.match(/Disabled/)) {
      var re = new RegExp("," + i + ",");
      if (hidden.value.search(re) == -1) {
        hidden.value = hidden.value + "," + i + ",";
      }
    }
    row = document.getElementById(sheetId + "_data_tr_" + ++i );
  }
  updateSelectionView(sheetId, hidden.value);
}
function tobagoSheetUnselectAll(sheetId) {
  var hidden
    = document.getElementById(sheetId + getSubComponentSeparator() + "selected");
  var row = getFirstSelectionRow(sheetId);
  var selector = document.getElementById(sheetId + "_data_row_selector_" + i);
  if (selector) {
    var i = row.id.substring(row.id.lastIndexOf("_data_tr_") + 9);
    while (row) {
      var selector = document.getElementById(sheetId + "_data_row_selector_" + i);
      if (!selector || !selector.src.match(/Disabled/)) {
        var re = new RegExp("," + i + ",", 'g');
        hidden.value = hidden.value.replace(re, "");
      }
      row = document.getElementById(sheetId + "_data_tr_" + ++i );
    }
  } else {
    hidden.value = "";
  }
  updateSelectionView(sheetId, hidden.value);
}

function tobagoSheetToggleAllSelections(sheetId) {
  var hidden
    = document.getElementById(sheetId + getSubComponentSeparator() + "selected");
  var row = getFirstSelectionRow(sheetId);
  var i = row.id.substring(row.id.lastIndexOf("_data_tr_") + 9);
  while (row) {
    tobagoSheetToggleSelection(i, hidden);
    row = document.getElementById(sheetId + "_data_tr_" + ++i );
  }
  updateSelectionView(sheetId, hidden.value);
}


function tobagoSheetSetPagerPage(commandId, page, submitCommand) {
  var element = document.getElementById(
      commandId + getSubComponentSeparator() + "link_" + page);
  var hidden = document.createElement('input');
  hidden.type = 'hidden';
  hidden.value = page;
  hidden.name = commandId + getSubComponentSeparator() +  "value";
  element.parentNode.appendChild(hidden);
  eval(submitCommand);
}

function tobagoSheetEditPagingRow(span, commandId, onClickCommand, commandName) {

  var text = document.getElementById(commandId + getSubComponentSeparator() + "text");
  if (text) {
    PrintDebug("text gefunden");
    span = text.parentNode;
    var hiddenId = commandId + getSubComponentSeparator() +  "value";
    span.style.cursor = 'auto';
    input = text.inputElement;
    if (! input) {
      PrintDebug("creating new input");
      input = document.createElement('input');
      text.inputElement = input;
      input.textElement = text;      
      input.type='text';
      input.id=hiddenId;
      input.name=hiddenId;
      input.className = "tobago-sheet-paging-input";
      input.onClickCommand = onClickCommand;
      addEventListener(input, 'blur', delayedHideInput);
      //addEventListener(input, 'keyup', keyUp);
      addEventListener(input, 'keydown', keyEvent);
    }
    input.value=text.innerHTML;
    span.replaceChild(input, text);
    input.focus();
    input.select();
  }
  else {
    PrintDebug("Can't find start field! ");
  }
}


function delayedHideInput(event) {
  var input = getActiveElement(event);
  if (input) {
    setTimeout('hideInput("' + input.id + '", 100)');
  } else {
    PrintDebug("Can't find input field! ");
  }
}
function hideInput(inputId) {
  var input = document.getElementById(inputId);
  if (input && !input.submitted) {
    input.parentNode.style.cursor = 'pointer';
    input.parentNode.replaceChild(input.textElement, input);
  } else {
    PrintDebug("Can't find input field! " + inputId);
  }
}

function keyEvent(event) {
  var input = getActiveElement(event);
  var keyCode;
  if (event.which) {
//    PrintDebug('mozilla');
    keyCode = event.which;
  } else {
//    PrintDebug('ie');
    keyCode = event.keyCode;
  }
  if (keyCode == 13) {
    //PrintDebug('new="' + input.value + '" old="' + input.textElement.innerHTML + '"');
    if (input.value != input.textElement.innerHTML) {
      //PrintDebug('changed : onClick = "' + input.onClickCommand + '"');
      input.submitted = true;
      eval(input.onClickCommand);
    }
    else {
      //PrintDebug('NOT changed');
      hideInput(input.id);
    }
  }
}


