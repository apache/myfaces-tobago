
var activeElement = null;
var oldX = 0;
var newWidth = 0;

function initSheet(sheetId) {
  PrintDebug("initSheet(" + sheetId +")");
  var i = 0;
  var element = document.getElementById(sheetId + "_header_div");
  if (element) {
    addEventListener(element, "mousemove", doResize);
    addEventListener(element, "mouseup", endResize);
    element = document.getElementById(sheetId + "_data_div");
    addEventListener(element, "scroll", doScroll);
	  var resizer = document.getElementById(sheetId + "_header_resizer_" + i++ );
	  while (resizer) {
	    addEventListener(resizer, "click", stopEventPropagation);
	    addEventListener(resizer, "mousedown", beginResize);
      resizer = document.getElementById(sheetId + "_header_resizer_" + i++ );
    }

    adjustHeaderDiv(sheetId);
    adjustResizer(sheetId);

    setupHeader(sheetId)
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
      // PrintDebug("rowId = " + row.id + "   next i=" + i);
      addEventListener(row, "click", doSelection);
      row = document.getElementById(sheetId + "_data_tr_" + i++ );
    }
    PrintDebug("preSelected rows = " + document.getElementById(sheetId + "::selected").value);
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
  //PrintDebug("srcElement = " + srcElement.tagName);
  if (srcElement.tagName.search(/DIV/) == 0 || srcElement.tagName.search(/TD/) == 0) {


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
    //PrintDebug("last id = " + sheet.tobagoLastClickedRowId);

    if (! event.ctrlKey) {
      // clearAllSelections();
      hidden.value = "";
    }

    if (event.shiftKey) {
      selectRange(dataRow, sheet.tobagoLastClickedRowId, hidden);
    }
    else {
      sheet.tobagoLastClickedRowId = rowId;
      toggleSelection(dataRow, hidden);
    }
    updateSelectionView(sheetId, hidden.value);
    PrintDebug("selected rows = " + hidden.value);
  }
}

function updateSelectionView(sheetId, selected) {
  var row = getFirstSelectionRow(sheetId);
  var i = row.id.substring(row.id.lastIndexOf("_data_tr_") + 9);
  while (row) {
    var re = new RegExp("," + i +",");
    var classes = row.className;
    if (selected.search(re) == -1) { // not selected: remove selection class
      row.className = classes.replace(/tobago-sheet-row-selected/g, "");
    }
    else {  // selected: check selection class
      if (classes.search(/tobago-sheet-row-selected/) == -1) {
        row.className = classes + " tobago-sheet-row-selected ";
      }
    }
    row = document.getElementById(sheetId + "_data_tr_" + ++i );
  }
}

function selectRange(dataRow, lastId, hidden) {
  var lastRow = document.getElementById(lastId);
  var firstIndex = lastRow.id.substring(lastRow.id.lastIndexOf("_data_tr_") + 9) - 0;
  var lastIndex  = dataRow.id.substring(dataRow.id.lastIndexOf("_data_tr_") + 9) - 0;
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
      hidden.value = hidden.value + "," + i + ",";
    }
  }
}

function toggleSelection(dataRow, hidden) {
  var rowIndex = dataRow.id.substring(dataRow.id.lastIndexOf("_data_tr_") + 9);
  var re = new RegExp("," + rowIndex + ",");
  if (hidden.value.search(re) != -1) {
    hidden.value = hidden.value.replace(re, "");
  }
  else {
    hidden.value = hidden.value + "," + rowIndex + ",";
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
      cell.style.width = newWidth + "px"
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
      dataFiller.style.width = Math.max((clientWidth - width), 0) + "px"
    }
  }
}

function adjustHeaderDiv(sheetId) {
  var contentDiv = document.getElementById(sheetId + "_data_div");
  var clientWidth = contentDiv.clientWidth;
  var headerDiv = document.getElementById(sheetId + "_header_div");
  var minWidth = contentDiv.style.width.replace(/px/, "") - getScrollbarWidth(); // div width - scrollbar width
  minWidth = Math.max(minWidth, 0); // not less than 0
  headerDiv.style.width = Math.max(clientWidth, minWidth);
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
    headerBox.style.marginLeft = "0px"
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