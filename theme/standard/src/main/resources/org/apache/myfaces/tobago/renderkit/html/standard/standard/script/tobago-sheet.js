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

Tobago.Sheets = {
  sheets: {},

  get: function(id) {
    return this.sheets[id];
  },

  put: function(sheet) {
    this.sheets[sheet.id] = sheet;
  },

  selectAll: function(id) {
    var sheet = this.get(id).selectAll();
  },

  unSelectAll: function(id) {
    var sheet = this.get(id).unSelectAll();
  },

  toggleAllSelections: function(id) {
    var sheet = this.get(id).toggleAllSelections();
  }

};

Tobago.Sheet = function(sheetId, enableAjax, checkedImage, uncheckedImage, selectable, columnSelectorIndex, autoReload,
                        clickActionId, clickReloadComponentId, dblClickActionId, dblClickReloadComponentId, simpleSheet) {
  this.startTime = new Date();
  this.id = sheetId;
  Tobago.Sheets.put(this);
  this.ajaxEnabled = enableAjax;
  this.checkedImage = checkedImage;
  this.uncheckedImage = uncheckedImage;
  this.selectable = selectable;
  this.columnSelectorIndex = columnSelectorIndex;
  this.autoReload = autoReload;
  this.clickActionId = clickActionId;
  this.clickReloadComponentId = clickReloadComponentId;
  this.dblClickActionId = dblClickActionId;
  this.dblClickReloadComponentId = dblClickReloadComponentId;

  this.resizerId = undefined;
  this.oldX      = 0;
  this.newWidth  = 0;

  this.firstRowId = undefined;
  this.firstRowIndex = -1;

  this.outerDivId    = this.id + "_outer_div";
  this.headerDivId   = this.id + "_header_div";
  this.contentDivId  = this.id + "_data_div";
  this.selectedId    = this.id + Tobago.SUB_COMPONENT_SEP +"selected";
  this.headerWidthsId = this.id + Tobago.SUB_COMPONENT_SEP + "widths";
  this.scrollPositionId = this.id + Tobago.SUB_COMPONENT_SEP + "scrollPosition";

  if (this.ajaxEnabled) {
    Tobago.ajaxComponents[this.id] = this;
     // option are only used for ajax request
    this.options = {
      method: 'post',
      asynchronous: true,
      onComplete: Tobago.bind(this, "onComplete"),
      parameters: '',
      evalScripts: true,
      onFailure: Tobago.bind(this, "onFailure")
    };
  }


  this.simpleSheet = simpleSheet;//Tobago.element(this.outerDivId).className.indexOf("tobago-simpleSheet-content") > -1;

  this.setup();

  LOG.debug("New Sheet with id " + this.id);
  this.endTime = new Date();
  LOG.debug("Sheet-setup time = " + (this.setupEnd.getTime() - this.setupStart.getTime()));
  LOG.debug("Sheet-total time = " + (this.endTime.getTime() - this.startTime.getTime()));
};

Tobago.Sheet.prototype.sortOnclickRegExp
      = new RegExp("Tobago.submitAction2\\(this, ('|\")(.*?)('|\"), null, null\\)");


Tobago.Sheet.prototype.setupElements = function() {

    this.firstRowId = this.getFirstRowId();
    this.firstRowIndex = (this.firstRowId != undefined)
        ? this.firstRowId.substring(this.firstRowId.lastIndexOf("_data_tr_") + 9)
        : -1;
  };

Tobago.Sheet.prototype.setupSortHeaders = function() {
    var i = 0;
    var idPrefix = this.id + "_header_box_";
    var headerBox = Tobago.element(idPrefix + i++);
    while (headerBox) {
      if (headerBox.onclick) {
        var match = this.sortOnclickRegExp.exec(headerBox.onclick.valueOf());
//        LOG.debug("match[0] = " + match[0]);
//        LOG.debug("match[1] = " + match[1]);
//        LOG.debug("*match[2] = " + match[2]);
//        LOG.debug("match[3] = " + match[3]);
//        LOG.debug("match[4] = " + match[4]);
//        LOG.debug("match[5] = " + match[5]);
//        LOG.debug("match[6] = " + match[6]);
//        headerBox.formId = match[2];
        headerBox.sorterId = match[2];
//        delete headerBox.onclick;
        headerBox.onclick = null;
//        LOG.debug("headerBox.id = " + headerBox.id);
        Tobago.addBindEventListener(headerBox, "click", this, "doSort");
      }
      headerBox = Tobago.element(idPrefix + i++);
    }
  };

Tobago.Sheet.prototype.setupPagingLinks = function() {
    idPrefix = this.id + Tobago.SUB_COMPONENT_SEP;
    var linkBox = Tobago.element(idPrefix + "pagingLinks");
    if (linkBox) {
      for (i = 0 ; i < linkBox.childNodes.length ; i++) {
        var child = linkBox.childNodes[i];
        if (child.nodeType == 1 && child.tagName.toUpperCase() == "A") {
          child.href = "#";
          Tobago.addBindEventListener(child, "click", this, "doPagingDirect");
        }
      }
    }
  };

Tobago.Sheet.prototype.setupPagePaging = function() {
    linkBox = Tobago.element(idPrefix + "pagingPages");
    if (linkBox) {
      for (i = 0 ; i < linkBox.childNodes.length ; i++) {
        var child = linkBox.childNodes[i];
        if (child.nodeType == 1 && child.tagName.toUpperCase() == "IMG") {
          // first, prev, next and last commands
          if (child.onclick) {
//            delete child.onclick;
            child.onclick = null;
            Tobago.addBindEventListener(child, "click", this, "doPaging");
          }
        } else if (child.nodeType == 1 && child.tagName.toUpperCase() == "SPAN") {
//          LOG.debug("Page : onclick =" + child.onclick);
          if (child.onclick) {
//            delete child.onclick;
            child.onclick = null;
            var toPageId = this.id + Tobago.COMPONENT_SEP + "ToPage";
            Tobago.addEventListener(child, "click",
                Tobago.bind(this, "insertTarget", toPageId));
          }

        }
      }
    }
  };

Tobago.Sheet.prototype.setupRowPaging = function() {
    var toRowId = this.id + Tobago.COMPONENT_SEP + "ToRow";
    var rowText = Tobago.element(toRowId + Tobago.SUB_COMPONENT_SEP + "text");
    if (rowText) {
      var parent = rowText.parentNode;
//      LOG.debug("row : onclick =" + parent.onclick);
      if (parent.onclick) {
//        delete parent.onclick;
        parent.onclick = null;
        Tobago.addEventListener(parent, "click",
            Tobago.bind(this, "insertTarget", toRowId));
      }
    }
  };

Tobago.Sheet.prototype.doSort = function(event) {
    var element = Event.element(event);
    while (element && !element.sorterId) {
      element = element.parentNode;
    }
//    LOG.debug("element.id = " + element.id);
//    LOG.debug("sorterId = " + element.sorterId);
    this.reloadWithAction2(event.srcElement, element.sorterId, null);
  };

Tobago.Sheet.prototype.doPagingDirect = function(event) {
    var element = Event.element(event);
    var action = this.id + Tobago.COMPONENT_SEP + "ToPage";

    var page = element.id.lastIndexOf('_');
    page = element.id.substring(page + 1);
    var hidden = document.createElement('input');
    hidden.type = 'hidden';
    hidden.value = page;
    hidden.name = action + Tobago.SUB_COMPONENT_SEP +  "value";
    Tobago.element(this.outerDivId).appendChild(hidden);

    this.reloadWithAction2(event.srcElement, action, null);
  };

Tobago.Sheet.prototype.doPaging = function(event) {
    var element = Event.element(event);
    var action = "unset";

  this.ppPrefix
        = Tobago.SUB_COMPONENT_SEP + "pagingPages" + Tobago.SUB_COMPONENT_SEP;

    this.firstRegExp = new RegExp(this.ppPrefix + "First$");

    this.prevRegExp = new RegExp(this.ppPrefix + "Prev$");

    this.nextRegExp = new RegExp(this.ppPrefix + "Next$");

    this.lastRegExp = new RegExp(this.ppPrefix + "Last$");


    if (element.id.match(this.firstRegExp)){
      action = this.id + Tobago.COMPONENT_SEP +"First";
    } else if (element.id.match(this.prevRegExp)){
      action = this.id + Tobago.COMPONENT_SEP +"Prev";
    } else if (element.id.match(this.nextRegExp)){
      action = this.id + Tobago.COMPONENT_SEP +"Next";
    } else if (element.id.match(this.lastRegExp)){
      action = this.id + Tobago.COMPONENT_SEP +"Last";
    }
    this.reloadWithAction2(event.srcElement, action, null);
  };

  Tobago.Sheet.prototype.reloadWithAction = function(action, options) {
    this.reloadWithAction2(null, action, options);
  };

  Tobago.Sheet.prototype.reloadWithAction2 = function(source, action, options) {
    LOG.debug("reload sheet with action \"" + action + "\"");
    var divElement = Tobago.element(this.outerDivId);
    var reloadOptions = Tobago.extend({}, this.options);
    reloadOptions = Tobago.extend(reloadOptions, options);
    Tobago.Updater.update2(source, divElement, null, action, this.id, reloadOptions);
  };

Tobago.Sheet.prototype.insertTarget = function(event, actionId) {
//    LOG.debug("insertTarget('" + actionId + "')")
    var textId = actionId + Tobago.SUB_COMPONENT_SEP + "text";
    var text = Tobago.element(textId);
    if (text) {
      var span = text.parentNode;
      var hiddenId = actionId + Tobago.SUB_COMPONENT_SEP +  "value";
      span.style.cursor = 'auto';
      var input = Tobago.element(hiddenId);
      if (! input) {
        input = document.createElement('input');
        input.type='text';
        input.id=hiddenId;
        input.name=hiddenId;
        input.className = "tobago-sheet-paging-input";
        input.actionId = actionId;
        Tobago.addBindEventListener(input, "blur", this, "delayedHideInput");
        Tobago.addBindEventListener(input, "keydown", this, "doKeyEvent");
        input.style.display = 'none';
        span.insertBefore(input, text);
      }
      input.value=text.innerHTML;
      input.style.display = '';
      text.style.display = 'none';
      input.focus();
      input.select();
    }
    else {
      LOG.error("Can't find text field with id = \"" + textId + "\"!");
    }
  };

Tobago.Sheet.prototype.delayedHideInput = function(event) {
    var element = Event.element(event);
    if (element) {
      this.textInput = element;
      setTimeout(Tobago.bind(this, "hideInput"), 100);
    }
  };

Tobago.Sheet.prototype.hideInput = function() {
    if (this.textInput) {
      this.textInput.parentNode.style.cursor = 'pointer';
      this.textInput.style.display = 'none';
      this.textInput.nextSibling.style.display = '';
    }
  };

Tobago.Sheet.prototype.doKeyEvent = function(event) {
    var input = Event.element(event);
    if (input) {

      var keyCode;
      if (event.which) {
        keyCode = event.which;
      } else {
        keyCode = event.keyCode;
      }
      if (keyCode == 13) {
        if (input.value != input.nextSibling.innerHTML) {
          this.reloadWithAction2(event.srcElement, input.actionId, null);
          Tobago.stopEventPropagation(event);
        }
        else {
          this.textInput = input;
          this.hideInput();
        }
      }
    }
  };

Tobago.Sheet.prototype.onComplete = function(transport) {
    //LOG.debug("sheet reloaded : " + transport.responseText.substr(0,20));
    this.setup();
  };

Tobago.Sheet.prototype.onFailure = function() {
  Tobago.deleteOverlay(Tobago.element(this.outerDivId));
  this.initReload();
};

Tobago.Sheet.prototype.setupResizer = function() {
    var headerDiv = Tobago.element(this.headerDivId);
    if (headerDiv) {
      Tobago.addBindEventListener(headerDiv, "mousemove", this, "doResize");
      Tobago.addBindEventListener(headerDiv, "mouseup", this, "endResize");

      var length = headerDiv.childNodes.length;
      for (var i = 0; i < length; i++) {
        var resizer = Tobago.element(this.id + "_header_resizer_" + i);
        if (resizer && resizer.className.match(/tobago-sheet-header-resize-cursor/)) {
          Tobago.addEventListener(resizer, "click", Tobago.stopEventPropagation);
          Tobago.addBindEventListener(resizer, "mousedown", this, "beginResize");
        }
      }
    }

    var contentDiv = Tobago.element(this.contentDivId);
    Tobago.addBindEventListener(contentDiv, "scroll", this, "doScroll");
  };

Tobago.Sheet.prototype.setup = function() {
    this.setupStart = new Date();
    //LOG.debug("Tobago.Sheet.setup(" + this.id +")");

    var divElement = document.getElementById(this.outerDivId);
    if (divElement.skipUpdate) {
        LOG.debug("skip setup");
        divElement.skipUpdate = false;
        Tobago.deleteOverlay(Tobago.element(divElement));
    } else {

      // ToDo: find a better way to fix this problem
      // IE needs this in case of ajax loading of style classes
//      commenting this out because of TOBAGO-191
//      var outerDiv = divElement;
//      outerDiv.className = outerDiv.className;
//      outerDiv.innerHTML = outerDiv.innerHTML;

      this.setupElements();

      this.setupResizer();
      if (!this.simpleSheet) {
        this.adjustHeaderDiv();
        this.adjustResizer();
        this.setupHeader();
        this.adjustScrollBars();
      }
      this.adjustHeaderDivFirefoxFix();

      if (this.firstRowId) {
        this.tobagoLastClickedRowId = this.firstRowId;
      }
      this.setScrollPosition(divElement);

      if (this.selectable && (this.selectable == "single" || this.selectable == "multi")) {
        this.addSelectionListener();
        this.updateSelectionView();
      }

      if (this.ajaxEnabled) {
        this.setupSortHeaders();
        this.setupPagingLinks();
        this.setupPagePaging();
        this.setupRowPaging();
      }
    }
    this.initReload();
    this.setupEnd = new Date();
  };

Tobago.Sheet.prototype.setScrollPosition = function(divElement) {

  var hidden = divElement.childNodes[1];

  if (hidden) {
    var sep = hidden.value.indexOf(";");
    if (sep != -1) {
      var scrollLeft = hidden.value.substr(0, sep);
      var scrollTop = hidden.value.substr(sep + 1);
      var contentDiv = Tobago.element(this.contentDivId);
      contentDiv.scrollLeft = scrollLeft;
      contentDiv.scrollTop = scrollTop;
      var headerDiv = Tobago.element(this.headerDivId);
      if (headerDiv) {
        headerDiv.scrollLeft = contentDiv.scrollLeft;
      }
    }
  }
};

Tobago.Sheet.prototype.initReload = function() {
  if (typeof this.autoReload == "number" && Tobago.element(this.contentDivId)) {
    Tobago.addReloadTimeout(this.id, Tobago.bind2(this, "reloadWithAction2", null, this.id), this.autoReload);
  }
};

Tobago.Sheet.prototype.adjustScrollBars = function() {
    var dataFiller = Tobago.element(this.id + "_data_row_0_column_filler");
    if (dataFiller) {
      var contentDiv = Tobago.element(this.contentDivId);
      var contentTable = contentDiv.getElementsByTagName("table")[0];
      var tableWidth = contentTable.style.width;
      contentTable.style.width = "10px";
      dataFiller.style.width = "0px";
      var clientWidth = contentDiv.clientWidth;

      if (contentDiv.scrollWidth <= clientWidth) {
        var width = 0;
        var i = 0;
        var cellDiv = Tobago.element(this.id + "_data_row_0_column" + i++);
        while (cellDiv) {
          var tmp = cellDiv.style.width.replace(/px/, "") - 0 ;
          width += tmp;
          cellDiv = Tobago.element(this.id + "_data_row_0_column" + i++);
        }
        dataFiller.style.width = Math.max((clientWidth - width), 0) + "px";
      }
      contentTable.style.width = tableWidth;
    }
  };

Tobago.Sheet.prototype.addSelectionListener = function() {
    var row = Tobago.element(this.firstRowId);
    if (row) {
      var i = 0;
      while (row) {
        Tobago.addBindEventListener(row, "mousedown", this, "doMouseDownSelect");
        Tobago.addBindEventListener(row, "click", this, "doSelection");
        if (this.dblClickActionId) {
          Tobago.addBindEventListener(row, "dblclick", this, "doDblClick");
        }
        row = this.getSiblingRow(row, ++i);
      }
      //LOG.debug("preSelected rows = " + Tobago.element(sheetId + "::selected").value);
    }
  };

Tobago.Sheet.prototype.doMouseDownSelect = function(event) {
  if (!event) {
    event = window.event;
  }
  this.mouseDownX = event.clientX;
  this.mouseDownY = event.clientY;
};

Tobago.Sheet.prototype.doSelection = function(event) {
    if (! event) {
      event = window.event;
    }

    var srcElement;
    if (event.target) {
      // W3C DOM level 2
      srcElement = event.target;
    } else {
      // IE
      srcElement = event.srcElement;
    }

    //LOG.debug("event.ctrlKey = " + event.ctrlKey);
    //LOG.debug("event.metaKey = " + event.metaKey);
    //LOG.debug("event.shiftKey = " + event.shiftKey);
    //LOG.debug("srcElement = " + srcElement.tagName);
    //LOG.debug("Actionid " + this.clickActionId);
    //LOG.debug("ID " + this.id);
    if (! Tobago.isInputElement(srcElement.tagName)) {

      if (Math.abs(this.mouseDownX - event.clientX) + Math.abs(this.mouseDownY - event.clientY) > 5) {
        // The user has moved the mouse. We assume, the user want to select some text inside the sheet,
        // so we doesn't select the row.
        return;
      }

      Tobago.clearSelection();

      var dataRow = Tobago.element(event);
      while (dataRow.id.search(new RegExp("_data_tr_\\d+$")) == -1) {
        dataRow = dataRow.parentNode;
      }
      var rowId = dataRow.id;
      //LOG.debug("rowId = " + rowId);
      var rowIndex = rowId.substring(rowId.lastIndexOf("_data_tr_") + 9);
      var selector = Tobago.element(this.id + "_data_row_selector_" + rowIndex);

      if ((!event.ctrlKey && !event.metaKey && !selector) || this.selectable == "single" ) {
        // clearAllSelections();
        Tobago.element(this.selectedId).value = ",";
      }

      if (event.shiftKey && this.selectable == "multi") {
        this.selectRange(dataRow);
      }
      else {
        this.tobagoLastClickedRowId = rowId;
        this.toggleSelectionForRow(dataRow);
      }
      this.updateSelectionView();
      //LOG.debug("selected rows = " + hidden.value);
      if (this.clickActionId) {
        var action = this.id + ":" + rowIndex + ":" + this.clickActionId;
        //LOG.debug("Action " + action);
        if (this.clickReloadComponentId && this.clickReloadComponentId.length > 0) {
          Tobago.reloadComponent2(srcElement, this.clickReloadComponentId[0], action, null);
        } else {
          Tobago.submitAction2(srcElement, action, true, null);
        }
      }
    }
  };

Tobago.Sheet.prototype.doDblClick = function(event) {
    if (! event) {
      event = window.event;
    }

    var srcElement;
    if (event.target) {
      // W3C DOM level 2
      srcElement = event.target;
    } else {
      // IE
      srcElement = event.srcElement;
    }

    //LOG.debug("event.ctrlKey = " + event.ctrlKey);
    //LOG.debug("event.metaKey = " + event.metaKey);
    //LOG.debug("event.shiftKey = " + event.shiftKey);
    //LOG.debug("srcElement = " + srcElement.tagName);
    //LOG.debug("Actionid " + this.clickActionId);
    //LOG.debug("ID " + this.id);
    if (! Tobago.isInputElement(srcElement.tagName)) {
      var dataRow = Tobago.element(event);
      while (dataRow.id.search(new RegExp("_data_tr_\\d+$")) == -1) {
        dataRow = dataRow.parentNode;
      }
      var rowId = dataRow.id;
      //LOG.debug("rowId = " + rowId);
      var rowIndex = rowId.substring(rowId.lastIndexOf("_data_tr_") + 9);
      //LOG.debug("selected rows = " + hidden.value);
      if (this.dblClickActionId) {
        var action = this.id + ":" + rowIndex + ":" + this.dblClickActionId;
        //LOG.debug("dblAction " + action);
        if (this.dblClickReloadComponentId && this.dblClickReloadComponentId.length > 0) {
          Tobago.reloadComponent2(srcElement, this.dblClickReloadComponentId[0], action, null);
        } else {
          Tobago.submitAction2(srcElement, action, true, null);
        }
      }
    }
  };

Tobago.Sheet.prototype.getSelectionElementForRow = function(row) {
  if (this.columnSelectorIndex >= 0) {
    if (this.simpleSheet) {
      var cell = row.firstChild;
      while (cell && cell.firstChild) {
        if (cell.firstChild.id.search(/_data_row_selector_/) > -1) {
          return cell.firstChild;
        }
        cell = cell.nextSibling;
      }
    } else {
      return row.childNodes[this.columnSelectorIndex].childNodes[0].childNodes[0].childNodes[0];
    }
  }
};

Tobago.Sheet.prototype.getSiblingRow = function(row, i) {
  return row.parentNode.childNodes[i];
};

Tobago.Sheet.prototype.updateSelectionView = function(sheetId) {
  var selected = Tobago.element(this.selectedId).value;
  var row = Tobago.element(this.firstRowId);
  var i = 0;

  while (row) {

    // you must not use Tobago.element() or document.getElementById() because the IE 6? doen't index the ids.
    // to the performace is quadratic aka. O(N*N) when N is the number of rows.

    var rowIndex = i + this.firstRowIndex * 1; // * 1 to keep integer operation
    var re = new RegExp("," + rowIndex + ",");
    var classes = row.className;
    var image = this.getSelectionElementForRow(row);

    if (selected.search(re) == -1) { // not selected: remove selection class

      Tobago.removeCssClass(row, "tobago-sheet-row-selected");

      if (image && image.src && !image.src.match(/Disabled/)) {
        image.src = this.uncheckedImage;
      }

    } else {  // selected: check selection class
      if (classes.search(/tobago-sheet-row-selected/) == -1) {
        Tobago.addCssClass(row, "tobago-sheet-row-selected");
      }
      if (image && image.src && !image.src.match(/Disabled/)) {
        image.src = this.checkedImage;
      }
    }
    row = this.getSiblingRow(row, ++i);
  }
};

Tobago.Sheet.prototype.toggleSelectionForRow = function(dataRow) {
    var rowIndex = dataRow.id.substring(dataRow.id.lastIndexOf("_data_tr_") + 9);
    this.toggleSelection(rowIndex);
  };

Tobago.Sheet.prototype.toggleSelection = function(rowIndex) {
    this.tobagoLastClickedRowId = Tobago.element(this.id + "_data_tr_" + rowIndex).id;
    var selector = Tobago.element(this.id + "_data_row_selector_" + rowIndex);
    if (!selector || !selector.src || !selector.src.match(/Disabled/)) {
      var re = new RegExp("," + rowIndex + ",");
      var selected = Tobago.element(this.selectedId);
      if (selected.value.search(re) != -1) {
        selected.value = selected.value.replace(re, ",");
      }
      else {
        selected.value = selected.value + rowIndex + ",";
      }
    }
  };

Tobago.Sheet.prototype.selectRange = function(dataRow) {
    var lastRow = Tobago.element(this.tobagoLastClickedRowId);
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
    var selected = Tobago.element(this.selectedId);
    for (var i = start; i <= end; i++) {
      var re = new RegExp("," + i + ",");
      if (selected.value.search(re) == -1) {
        var selector = Tobago.element(this.id + "_data_row_selector_" + i);
        if (!selector || !selector.src || !selector.src.match(/Disabled/)) {
          selected.value = selected.value + i + ",";
        }
      }
    }
  };

Tobago.Sheet.prototype.getFirstRowId = function() {
  var element;
  if (this.simpleSheet) {
    element = Tobago.element(this.contentDivId).firstChild;
  } else {
    element = Tobago.element(this.id + "_data_row_0_column0");// data div
    this.firstRowRegExp = new RegExp("^" + this.id + "_data_tr_\\d+$");
    while (element && element.id.search(this.firstRowRegExp) == -1) {
      //      LOG.debug("element id = " + element.id);
      element = element.parentNode;
    }
    //    LOG.debug("firstRowId = " + element.id);
  }
  return element ? element.id : undefined;
};

Tobago.Sheet.prototype.setupHeader = function() {
    var headerBox = Tobago.element(this.id + "_header_box_0");
    if (headerBox) {

      if (window.opera) {
        headerBox.style.marginLeft = "0px";
      }

      var width = headerBox.style.width;
      var tmpWidth = width.replace(/px/, "");
      headerBox.style.width = tmpWidth -0 + 10 + "px";
      headerBox.style.width = width;
    }
  };

Tobago.Sheet.prototype.adjustResizer = function() {
    // opera needs this
    if (window.opera) {
      var position = 5;
      var index = 0;
      var resizer = Tobago.element(this.id + "_header_resizer_" + index);
      while (resizer) {
        resizer.style.right = - position;
        var headerBox = Tobago.element(this.id + "_header_box_" + index++);
        var width = headerBox.style.width.replace(/px/, "") - 0;
        position += width;
        resizer = Tobago.element(this.id + "_header_resizer_" + index);
      }
    }
  };

Tobago.Sheet.prototype.adjustHeaderDiv = function () {
    var headerDiv = Tobago.element(this.headerDivId);
    if (!headerDiv) {
      return;
    }
    var contentDiv = Tobago.element(this.contentDivId);
    var contentTable = contentDiv.getElementsByTagName("table")[0];
    contentTable.style.width = "10px";
    var contentWidth = contentDiv.style.width.replace(/px/, "") - 0;
    var clientWidth = contentDiv.clientWidth;
    var boxSum = 0;
    var idx = 0;
    var box = Tobago.element(this.id + "_header_box_" + idx++);
    while (box) {
      boxSum += (box.style.width.replace(/px/, "") - 0);
      box = Tobago.element(this.id + "_header_box_" + idx++);
    }
    if (clientWidth == 0) {
      clientWidth = Math.min(contentWidth, boxSum);
    }
    var minWidth = contentWidth - Tobago.Config.get("Sheet", "scrollbarWidth")
                   - Tobago.Config.get("Sheet", "contentBorderWidth");
    minWidth = Math.max(minWidth, 0); // not less than 0
    headerDiv.style.width = Math.max(clientWidth, minWidth);
    var fillBox = Tobago.element(this.id + "_header_box_filler");
    if (fillBox) {
        fillBox.style.width = Math.max(headerDiv.style.width.replace(/px/, "") - boxSum, 0);
    }
    //  LOG.debug("adjustHeaderDiv(" + sheetId + ") : clientWidth = " + clientWidth + " :: width => " + headerDiv.style.width);
    //headerDiv.style.width = clientWidth;
    var clientWidth2 = contentDiv.clientWidth;
    if (clientWidth > clientWidth2) {
      // IE needs this
      headerDiv.style.width = Math.max(clientWidth2, minWidth);
      //    LOG.debug("second time adjustHeaderDiv(" + sheetId + ") : clientWidth2 = " + clientWidth2 + " :: width => " + headerDiv.style.width);
    }
    contentTable.style.width = contentDiv.clientWidth + "px";
    //  LOG.debug("div width   :" + contentDiv.clientWidth);
    //  LOG.debug("table width :" + contentTable.clientWidth);
    //  LOG.debug("boxSum      :" + boxSum);
    //  LOG.debug("filler      :" + fillBox.clientWidth);
    //  LOG.debug("fillerstyle :" + fillBox.style.width);
    //  LOG.debug("##########################################");
};

Tobago.Sheet.prototype.adjustHeaderDivFirefoxFix = function () {
  // XXX fix for Firefox 3.0 (3.5 and 3.6 are working)
  var headerDiv = Tobago.element(this.headerDivId);
  if (!headerDiv) {
    return;
  }
  if (navigator.userAgent.indexOf("Firefox/3.0") > -1) {
    var length = headerDiv.childNodes.length;
    for (var i = 0; i < length; i++) {
      var box2 = Tobago.element(this.id + "_header_box_" + i);
      var outer = Tobago.element(this.id + "_header_outer_" + i);
      if (box2 && outer) {
        outer.style.width = box2.style.width;
      }
    }
  }
};

Tobago.Sheet.prototype.beginResize = function(event) {
    if (! event) {
      event = window.event;
    }
    this.resizerId = Tobago.element(event).id;
    if (this.resizerId) {
      this.oldX = event.clientX;
      var elementWidth = this.getHeaderBox().style.width;
      this.newWidth = elementWidth.substring(0,elementWidth.length-2);
    }
  };

Tobago.Sheet.prototype.getHeaderBox = function() {
    var boxId = this.resizerId.replace(/_header_resizer_/, "_header_box_");
    return Tobago.element(boxId);
  };

Tobago.Sheet.prototype.doResize = function(event) {
    if (! event) {
      event = window.event;
    }
    if (this.resizerId) {
      var box = this.getHeaderBox();
      var elementWidth = box.style.width;
      var elementWidthPx = elementWidth.substring(0,elementWidth.length-2);
      var divX = event.clientX - this.oldX;
      this.newWidth = elementWidthPx-0 + divX;
      if (this.newWidth < 10) {
        this.newWidth = 10;
      } else {
        this.oldX = event.clientX;
      }
      box.style.width = this.newWidth + "px";
    }
  };

Tobago.Sheet.prototype.endResize = function(event) {
    if (! event) {
      event = window.event;
    }
    if (this.resizerId) {
      var columnNr
          = this.resizerId.substring(this.resizerId.lastIndexOf("_") + 1,
          this.resizerId.length);
      var idPrefix = this.id + "_data_row_";
      var idPostfix = "_column" + columnNr;
      var i = 0;
      var cell = Tobago.element(idPrefix + i++ + idPostfix);
      while (cell) {
        cell.style.width = this.newWidth + "px";
        cell = Tobago.element(idPrefix + i++ + idPostfix);
      }

      this.adjustScrollBars();
      this.adjustHeaderDiv();
      this.adjustResizer();
      this.storeSizes();
      this.adjustHeaderDivFirefoxFix();
      delete this.resizerId;
    }
  };

Tobago.Sheet.prototype.storeSizes = function() {
    var index = 0;
    var idPrefix = this.id + "_header_box_";
    var header = Tobago.element(idPrefix + index++);
    var widths = "";
    while (header) {
      width = header.style.width.replace(/px/, "");
      widths = widths + "," + width;
      header = Tobago.element(idPrefix + index++);
    }
    Tobago.element(this.headerWidthsId).value = widths;
  };

Tobago.Sheet.prototype.doScroll = function(event) {
    //LOG.debug("header / data  " + this.headerDiv.scrollLeft + "/" + this.contentDiv.scrollLeft);
    var headerDiv = Tobago.element(this.headerDivId);
    var contentDiv = Tobago.element(this.contentDivId);
    if (headerDiv) {
      headerDiv.scrollLeft = contentDiv.scrollLeft;
    }
    var hidden = Tobago.element(this.scrollPositionId);
    if (hidden) {
      hidden.value = contentDiv.scrollLeft + ";" + contentDiv.scrollTop;
    }
    //LOG.debug("header / data  " + this.headerDiv.scrollLeft + "/" + this.contentDiv.scrollLeft);
    //LOG.debug("----------------------------------------------");
  };



Tobago.Sheet.prototype.selectAll = function() {
    var row = Tobago.element(this.firstRowId);
    var i = this.firstRowIndex;
    var j = 0;
    var selected = Tobago.element(this.selectedId);
    while (row) {
      var image = this.getSelectionElementForRow(row);
      if (!image || !image.src || !image.src.match(/Disabled/)) {
        var re = new RegExp("," + i + ",");
        if (selected.value.search(re) == -1) {
          selected.value = selected.value + i + ",";
        }
      }
      i++;
      row = this.getSiblingRow(row, ++j);
    }
    this.updateSelectionView();
  };

Tobago.Sheet.prototype.unSelectAll = function() {
    var selected = Tobago.element(this.selectedId);
    var row = Tobago.element(this.firstRowId);
    var image = this.getSelectionElementForRow(row);
    if (image) {
      var i = this.firstRowIndex;
      var j = 0;
      while (row) {
        image = this.getSelectionElementForRow(row);
        if (!image || !image.src || !image.src.match(/Disabled/)) {
          var re = new RegExp("," + i + ",", 'g');
          selected.value = selected.value.replace(re, ",");
        }
        i++;
        row = this.getSiblingRow(row, ++j);
      }
    } else {
      selected.value = ",";
    }
    this.updateSelectionView();
  };

Tobago.Sheet.prototype.toggleAllSelections = function(sheetId) {
    var row = Tobago.element(this.firstRowId);
    var i = this.firstRowIndex;
    var j = 0;
    while (row) {
      this.toggleSelection(i);
      i++;
      row = this.getSiblingRow(row, ++j);
    }
    this.updateSelectionView();
  };
