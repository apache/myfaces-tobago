
var activeElement = null;
var oldX = 0;
var newWidth = 0;

function initSheet(sheetId) {
  var i = 0;
  var element = document.getElementById(sheetId + "_header_div");
  if (element) {
    addEventListener(element, "mousemove", doResize);
    addEventListener(element, "mouseup", endResize);
    element = document.getElementById(sheetId + "_data_div");
    addEventListener(element, "scroll", doScroll);
	  var resizer = document.getElementById(sheetId + "_header_resizer_" + i++ );
	  while (resizer) {
	    addEventListener(resizer, "click", stopPropagation);
	    addEventListener(resizer, "mousedown", beginResize);
      resizer = document.getElementById(sheetId + "_header_resizer_" + i++ );
    }

    adjustHeaderDiv(sheetId);
    adjustResizer(sheetId);

    setupHeader(sheetId)
  }

  adjustScrollBars(sheetId);
}


function stopPropagation(event) {
  if (! event) {
    event = window.event;
  }
  if (ie || opera) {
    event.cancelBubble = true;
  }
  else { // this is DOM2
    event.stopPropagation();
  }
}

function doScroll(event) {
  if (! event) {
    event = window.event;
  }
  var dataPanel = getActiveElement(event);
  var sheetId = dataPanel.id.substring(0, dataPanel.id.lastIndexOf("_data_div"));
  var headerPanel = document.getElementById(sheetId + "_header_div");
  Ausgeben("header / data  " + headerPanel.scrollLeft + "/" + dataPanel.scrollLeft);
  headerPanel.scrollLeft = dataPanel.scrollLeft;
  Ausgeben("header / data  " + headerPanel.scrollLeft + "/" + dataPanel.scrollLeft);
  Ausgeben("----------------------------------------------");
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

  var hidden = document.getElementById(sheetId + ":widths");
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
  var minWidth = contentDiv.style.width.replace(/px/, "") - 16 ; // div width - scrollbar width
  minWidth = Math.max(minWidth, 0); // not less than 0
  headerDiv.style.width = Math.max(clientWidth, minWidth);
  //headerDiv.style.width = clientWidth;
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