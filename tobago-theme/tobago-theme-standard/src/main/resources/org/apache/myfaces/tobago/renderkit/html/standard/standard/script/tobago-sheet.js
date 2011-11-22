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
  }
};

// Info: 2nd parameter "enableAjax" was rededicated to "firstRowIndex"
Tobago.Sheet = function(sheetId, firstRowIndex, selectable, columnSelectorIndex, autoReload,
                        clickActionId, clickReloadComponentId, dblClickActionId, dblClickReloadComponentId, renderedPartially) {
  this.startTime = new Date(); // @DEV_ONLY
  this.id = sheetId;
  Tobago.Sheets.put(this);
  this.selectable = selectable;
  this.columnSelectorIndex = columnSelectorIndex;
  this.autoReload = autoReload;
  this.clickActionId = clickActionId;
  this.clickReloadComponentId = clickReloadComponentId;
  this.dblClickActionId = dblClickActionId;
  this.dblClickReloadComponentId = dblClickReloadComponentId;
  this.renderedPartially = renderedPartially;

  this.resizerId = undefined;
  this.oldX      = 0;
  this.newWidth  = 0;

  this.firstRowIndex = undefined;
  this.rowCount = undefined;
  this.lastClickedRowIndex = -1;

  this.headerDivId   = this.id + Tobago.SUB_COMPONENT_SEP + "header_div";
  this.contentDivId  = this.id + Tobago.SUB_COMPONENT_SEP + "data_div";
  this.selectedId    = this.id + Tobago.SUB_COMPONENT_SEP +"selected";
  this.headerWidthsId = this.id + Tobago.SUB_COMPONENT_SEP + "widths";
  this.scrollPositionId = this.id + Tobago.SUB_COMPONENT_SEP + "scrollPosition";

  this.mouseDownX = undefined;
  this.mouseDownY = undefined;

  Tobago.addAjaxComponent(this.id, this);
  // option are only used for ajax request
  this.options = {
    // overlay is created by sheet itself, so disable this in Tobago.Transport
    createOverlay: false
  };

  this.ppPrefix = Tobago.SUB_COMPONENT_SEP + "pagingPages" + Tobago.SUB_COMPONENT_SEP;

  this.firstRegExp = new RegExp(this.ppPrefix + "First$");

  this.prevRegExp = new RegExp(this.ppPrefix + "Prev$");

  this.nextRegExp = new RegExp(this.ppPrefix + "Next$");

  this.lastRegExp = new RegExp(this.ppPrefix + "Last$");

  this.firstRowRegExp = new RegExp("^" + this.id + "_data_tr_\\d+$");

  this.setup();

  LOG.debug("New Sheet with id " + this.id); // @DEV_ONLY
  this.endTime = new Date(); // @DEV_ONLY
  LOG.debug("Sheet-setup time = " + (this.setupEnd.getTime() - this.setupStart.getTime())); // @DEV_ONLY
  LOG.debug("Sheet-total time = " + (this.endTime.getTime() - this.startTime.getTime())); // @DEV_ONLY
};

Tobago.Sheet.prototype.setupSortHeaders = function() {
  var sheet = this;
  jQuery(Tobago.Utils.escapeClientId(sheet.id)).find(".tobago-sheet-header[sorterId]").each(function() {

    jQuery(this).click({sheet: sheet, sorterId: jQuery(this).attr("sorterId")}, function(event) {

      event.data.sheet.reloadWithAction(jQuery(this), event.data.sorterId);
      event.stopPropagation();
    });
  });
};

Tobago.Sheet.prototype.setupPagingLinks = function() {
    idPrefix = this.id + Tobago.SUB_COMPONENT_SEP;
    var linkBox = Tobago.element(idPrefix + "pagingLinks");
    if (linkBox) {
      for (i = 0 ; i < linkBox.childNodes.length ; i++) {
        var child = linkBox.childNodes[i];
        if (child.nodeType == 1 && child.tagName.toUpperCase() == "A") {
          Tobago.addBindEventListener(child, "click", this, "doPagingDirect");
        }
      }
    }
  };

Tobago.Sheet.prototype.setupPagePaging = function() {
    linkBox = Tobago.element(idPrefix + "pagingPages");
    if (linkBox) {
      for (var i = 0 ; i < linkBox.childNodes.length ; i++) {
        var child = linkBox.childNodes[i];
        if (child.nodeType == 1 && child.tagName.toUpperCase() == "IMG") {
          // first, prev, next and last commands
          if (undefined == jQuery(child).attr("data-tobago-disabled")) {
            Tobago.addBindEventListener(child, "click", this, "doPaging");
          }
        } else if (child.nodeType == 1 && child.tagName.toUpperCase() == "SPAN") {
          var toPageId = this.id + Tobago.COMPONENT_SEP + "ToPage";
          Tobago.addEventListener(child, "click", Tobago.bind(this, "insertTarget", toPageId));
        }
      }
    }
  };

Tobago.Sheet.prototype.setupRowPaging = function() {
    var toRowId = this.id + Tobago.COMPONENT_SEP + "ToRow";
    var rowText = Tobago.element(toRowId + Tobago.SUB_COMPONENT_SEP + "text");
    if (rowText) {
      var parent = rowText.parentNode;
      Tobago.addEventListener(parent, "click", Tobago.bind(this, "insertTarget", toRowId));
    }
  };


Tobago.Sheet.prototype.doPagingDirect = function(event) {
    var element = Tobago.element(event);
    var action = this.id + Tobago.COMPONENT_SEP + "ToPage";

    var page = element.id.lastIndexOf('_');
    page = element.id.substring(page + 1);
    var hidden = document.createElement('input');
    hidden.type = 'hidden';
    hidden.value = page;
    hidden.name = action + Tobago.SUB_COMPONENT_SEP +  "value";
    Tobago.element(this.id).appendChild(hidden);

    this.reloadWithAction(event.srcElement, action);
  };

Tobago.Sheet.prototype.doPaging = function(event) {
    var element = Tobago.element(event);
    var action = "unset";
    if (element.id.match(this.firstRegExp)){
      action = this.id + Tobago.COMPONENT_SEP +"First";
    } else if (element.id.match(this.prevRegExp)){
      action = this.id + Tobago.COMPONENT_SEP +"Prev";
    } else if (element.id.match(this.nextRegExp)){
      action = this.id + Tobago.COMPONENT_SEP +"Next";
    } else if (element.id.match(this.lastRegExp)){
      action = this.id + Tobago.COMPONENT_SEP +"Last";
    }
    this.reloadWithAction(event.srcElement, action);
  };

Tobago.Sheet.prototype.reloadWithAction = function(source, action, options) {
    LOG.debug("reload sheet with action \"" + action + "\""); // @DEV_ONLY
    var reloadOptions = Tobago.extend({}, this.options);
    reloadOptions = Tobago.extend(reloadOptions, options);
    Tobago.createOverlay(Tobago.element(this.id));
    Tobago.Updater.update(source, action, this.renderedPartially?this.renderedPartially:this.id, reloadOptions);
  };

Tobago.Sheet.prototype.afterDoUpdateSuccess = function() {
  this.setup();
};

Tobago.Sheet.prototype.afterDoUpdateNotModified = function() {
  this.initReload();
};

Tobago.Sheet.prototype.afterDoUpdateError = function() {
  this.initReload();
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
        input.className = "tobago-sheet-pagingInput";
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
      LOG.error("Can't find text field with id = \"" + textId + "\"!"); // @DEV_ONLY
    }
  };

Tobago.Sheet.prototype.delayedHideInput = function(event) {
    var element = Tobago.element(event);
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
    var input = Tobago.element(event);
    if (input) {

      var keyCode;
      if (event.which) {
        keyCode = event.which;
      } else {
        keyCode = event.keyCode;
      }
      if (keyCode == 13) {
        if (input.value != input.nextSibling.innerHTML) {
          this.reloadWithAction(event.srcElement, input.actionId);
          Tobago.stopEventPropagation(event);          
        }
        else {
          this.textInput = input;
          this.hideInput();
        }
      }
    }
  };

Tobago.Sheet.prototype.setupResizer = function() {
    var headerDiv = Tobago.element(this.headerDivId);
    if (headerDiv) {
      Tobago.addBindEventListener(headerDiv, "mousemove", this, "doResize");
      Tobago.addBindEventListener(headerDiv, "mouseup", this, "endResize");

      var length = headerDiv.childNodes.length;
      for (var i = 0; i < length; i++) {
        var resizer = Tobago.element(this.id + Tobago.SUB_COMPONENT_SEP + "header_spacer_" + i);
        if (resizer && resizer.className.match(/tobago-sheet-headerSpacer-markup-resizable/)) {
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

  this.setupResizer();

  // IE 6+7
  if (jQuery.browser.msie && parseInt(jQuery.browser.version) <= 7) {
    jQuery(Tobago.Utils.escapeClientId(this.id) + ">div>table>colgroup>col").each(function() {
      Tobago.Sheet.fixIE67ColWidth(jQuery(this));
    });
  }

  this.adjustHeaderDiv();
  this.adjustResizer();

  this.setupHeader();

  //      this.adjustScrollBars();
  this.setScrollPosition();

  this.firstRowIndex = parseInt(this.getRows().eq(0).attr("rowIndexInModel"), 10);
  this.rowCount = this.getRows().size();

  if (this.selectable
      && (this.selectable == "single" || this.selectable == "singleOrNone" || this.selectable == "multi")) {
    this.addSelectionListener();
  }

  this.setupSortHeaders();
  this.setupPagingLinks();
  this.setupPagePaging();
  this.setupRowPaging();

  this.initReload();
  this.setupEnd = new Date();
};

Tobago.Sheet.prototype.setScrollPosition = function() {
  var hidden = Tobago.element(this.scrollPositionId);
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
    Tobago.addReloadTimeout(this.id, Tobago.bind2(this, "reloadWithAction", null, this.id), this.autoReload);
  }
};

/*
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
*/

Tobago.Sheet.prototype.addSelectionListener = function() {
  var currentSheet = this;
  currentSheet.getRows().each(function() {
    var row = this;
    Tobago.addBindEventListener(row, "mousedown", currentSheet, "doMouseDownSelect");
    Tobago.addBindEventListener(row, "mouseup", currentSheet, "doMouseUpSelect");
    Tobago.addBindEventListener(row, "click", currentSheet, "doSelection");
    if (currentSheet.dblClickActionId) {
      Tobago.addBindEventListener(row, "dblclick", currentSheet, "doDblClick");
    }
  });
};

Tobago.Sheet.prototype.doMouseDownSelect = function(event) {
  if (!event) {
    event = window.event;
  }
  this.mouseDownX = event.clientX;
  this.mouseDownY = event.clientY;
};

Tobago.Sheet.prototype.doMouseUpSelect = function(event) {
  if (!event) {
    event = window.event;
  }
  this.mouseDownX = undefined;
  this.mouseDownY = undefined;
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
    //LOG.debug("event.shiftKey = " + event.shiftKey);
    //LOG.debug("srcElement = " + srcElement.tagName);
    //LOG.debug("Actionid " + this.clickActionId);
    //LOG.debug("ID " + this.id);
    if (srcElement.id.search(/_data_row_selector_/) > -1  || !Tobago.isInputElement(srcElement.tagName)) {

      if (this.mouseDownX != undefined &&
          Math.abs(this.mouseDownX - event.clientX) + Math.abs(this.mouseDownY - event.clientY) > 5) {
        // The user has moved the mouse. We assume, the user want to select some text inside the sheet,
        // so we doesn't select the row.
        return;
      }
      
      Tobago.clearSelection();

      // find the row, if the has clicked on an element inside the row
      var row = jQuery(Tobago.element(event)).closest("tr");
      var selector = this.getSelectorCheckbox(row.get(0));
      var rowIndex = row.index() + this.firstRowIndex;
      var wasSelected = this.isSelected(rowIndex);

      if ((!event.ctrlKey && !event.metaKey && !selector)
          || this.selectable == "single" || this.selectable == "singleOrNone") {
        this.deselectAll();
      }

      if (event.shiftKey && this.selectable == "multi") {
        if (this.lastClickedRowIndex <= rowIndex) {
          this.selectRange(this.lastClickedRowIndex, rowIndex + 1, true, false);
        } else {
          this.selectRange(rowIndex, this.lastClickedRowIndex + 1, true, false);
        }
      } else if (this.selectable != "singleOrNone" || !wasSelected) {
        this.toggleSelection(rowIndex, row.get(0), selector);
      }
      //LOG.debug("selected rows = " + hidden.value);
      if (this.clickActionId) {
        var action = this.id + ":" + rowIndex + ":" + this.clickActionId;
        //LOG.debug("Action " + action);
        if (this.clickReloadComponentId && this.clickReloadComponentId.length > 0) {
          Tobago.reloadComponent(srcElement, this.clickReloadComponentId, action)
        } else {
          Tobago.submitAction(srcElement, action);
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
    //LOG.debug("event.shiftKey = " + event.shiftKey);
    //LOG.debug("srcElement = " + srcElement.tagName);
    //LOG.debug("Actionid " + this.clickActionId);
    //LOG.debug("ID " + this.id);
    if (! Tobago.isInputElement(srcElement.tagName)) {
      var row = jQuery(Tobago.element(event)).closest("tr");
      var rowIndex = row.index() + this.firstRowIndex;
      //LOG.debug("selected rows = " + hidden.value);
      if (this.dblClickActionId) {
        var action = this.id + ":" + rowIndex + ":" + this.dblClickActionId;
        //LOG.debug("dblAction " + action);
        if (this.dblClickReloadComponentId && this.dblClickReloadComponentId.length > 0) {
          Tobago.reloadComponent(srcElement, this.dblClickReloadComponentId, action)
        } else {
          Tobago.submitAction(srcElement, action);
        }
      }
    }
  };

/**
 * Get the element, which indicates the selection
 * @param row
 */
Tobago.Sheet.prototype.getSelectorCheckbox = function(row) {
  if (this.columnSelectorIndex >= 0) {
    return row.childNodes[this.columnSelectorIndex].childNodes[0];
  } else {
    return null;
  }
};

Tobago.Sheet.prototype.getSiblingRow = function(row, i) {
  return row.parentNode.childNodes[i];
};

Tobago.Sheet.prototype.isEnabled = function(checkbox) {
  return checkbox == null
      || checkbox.attributes == null
      || checkbox.attributes.disabled === undefined
      || checkbox.attributes.disabled == null
      || checkbox.attributes.disabled.value != "true";
};

Tobago.Sheet.prototype.getRows = function() {
  // todo: use a util for "id replace"
  // find all rows in current sheet
  return jQuery(Tobago.Utils.escapeClientId(this.id) + ">div>table>tbody>tr");
};

Tobago.Sheet.prototype.isSelected = function(rowIndex) {
  var selected = Tobago.element(this.selectedId);
  return selected.value.indexOf("," + rowIndex + ",") >= 0;
};

Tobago.Sheet.prototype.toggleSelection = function(rowIndex, row, checkbox) {
  this.lastClickedRowIndex = rowIndex;
  if (this.isEnabled(checkbox)) {
    var selected = Tobago.element(this.selectedId);
    if (selected.value.indexOf("," + rowIndex + ",") < 0) {
      this.selectRow(selected, rowIndex, row, checkbox);
    } else {
      this.deselectRow(selected, rowIndex, row, checkbox);
    }
  }
};

Tobago.Sheet.prototype.selectAll = function() {
  this.selectRange(this.firstRowIndex, this.firstRowIndex + this.rowCount, true, false);
};

Tobago.Sheet.prototype.deselectAll = function() {
  this.selectRange(this.firstRowIndex, this.firstRowIndex + this.rowCount, false, true);
};

Tobago.Sheet.prototype.toggleAll = function() {
  this.selectRange(this.firstRowIndex, this.firstRowIndex + this.rowCount, true, true);
};

Tobago.Sheet.prototype.selectRange = function(indexA, indexB, selectDeselected, deselectSelected) {
//  LOG.info("select any 15");
//  var start = new Date().getTime();

  var selected = Tobago.element(this.selectedId);
  var rows = this.getRows();
  for (var rowIndex = indexA; rowIndex < indexB; rowIndex++) {
    var row = rows.get(rowIndex - this.firstRowIndex);
    var checkbox = this.getSelectorCheckbox(row);
    if (this.isEnabled(checkbox)) {
      if (selectDeselected && selected.value.indexOf("," + rowIndex + ",") < 0) {
        this.selectRow(selected, rowIndex, row, checkbox);
      } else if (deselectSelected && selected.value.indexOf("," + rowIndex + ",") >= 0) {
        this.deselectRow(selected, rowIndex, row, checkbox);
      }
    }
  }

//  LOG.info("select any 15 +++++++++++++ " + (new Date().getTime() - start));
};

/**
 * @param selected input-element type=hidden: Hidden field with the selection state information
 * @param rowIndex int: zero based index of the row.
 * @param row tr-element: the row.
 * @param checkbox input-element: selector in the row.
 */
Tobago.Sheet.prototype.selectRow = function(selected, rowIndex, row, checkbox) {
  selected.value = selected.value + rowIndex + ",";
  row.className = row.className + " tobago-sheet-row-markup-selected";
  if (checkbox != null) {
    checkbox.checked = true;
  }
};

/**
 * @param selected input-element type=hidden: Hidden field with the selection state information
 * @param rowIndex int: zero based index of the row.
 * @param row tr-element: the row.
 * @param checkbox input-element: selector in the row.
 */
Tobago.Sheet.prototype.deselectRow = function(selected, rowIndex, row, checkbox) {
  selected.value = selected.value.replace(new RegExp("," + rowIndex + ","), ",");
  var c = " " + row.className + " ";
  c = c.replace(/ tobago-sheet-row-markup-selected /, " ");
  row.className = c.substring(1, c.length - 1);
  if (checkbox != null) {
    checkbox.checked = false;
  }
};

Tobago.Sheet.prototype.setupHeader = function() {
    var headerBox = Tobago.element(this.id + Tobago.SUB_COMPONENT_SEP + "header_box_0");
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
      var resizer = Tobago.element(this.id + Tobago.SUB_COMPONENT_SEP + "header_spacer_" + index);
      while (resizer) {
        resizer.style.right = - position;
        var headerBox = Tobago.element(this.id + Tobago.SUB_COMPONENT_SEP + "header_box_" + index++);
        var width = headerBox.style.width.replace(/px/, "") - 0;
        position += width;
        resizer = Tobago.element(this.id + Tobago.SUB_COMPONENT_SEP + "header_spacer_" + index);
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
    var box = Tobago.element(this.id + Tobago.SUB_COMPONENT_SEP + "header_box_" + idx++);
    while (box) {
      boxSum += (box.style.width.replace(/px/, "") - 0);
      box = Tobago.element(this.id + Tobago.SUB_COMPONENT_SEP + "header_box_" + idx++);
    }
    if (clientWidth == 0) {
      clientWidth = Math.min(contentWidth, boxSum);
    }
    var minWidth = contentWidth - Tobago.Config.get("Tobago", "verticalScrollbarWeight")
                   - Tobago.Config.get("Sheet", "contentBorderWidth");
    minWidth = Math.max(minWidth, 0); // not less than 0
    headerDiv.style.width = Math.max(clientWidth, minWidth);
    var fillBox = Tobago.element(this.id + Tobago.SUB_COMPONENT_SEP + "header_box_filler");
    fillBox.style.width = Math.max(headerDiv.style.width.replace(/px/, "") - boxSum, 0);
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

Tobago.Sheet.prototype.beginResize = function(event) {
    if (! event) {
      event = window.event;
    }
    this.resizerId = Tobago.element(event).id;
    if (this.resizerId) {
      this.oldX = event.clientX;
      var elementWidth = this.getHeaderBox().style.width;
      this.newWidth = elementWidth.substring(0, elementWidth.length - 2);
    }
  };

Tobago.Sheet.prototype.getHeaderBox = function() {
    var boxId = this.resizerId.replace(/header_spacer_/, "header_box_");
    return Tobago.element(boxId);
  };

Tobago.Sheet.prototype.doResize = function(event) {
    if (! event) {
      event = window.event;
    }
    if (this.resizerId) {
      var box = jQuery(this.getHeaderBox());
      var extraWidth = box.outerWidth() - box.width();
      var elementWidth = box.outerWidth();
      var divX = event.clientX - this.oldX;
      this.newWidth = elementWidth + divX;
      if (this.newWidth < 10) {
        this.newWidth = 10;
      } else {
        this.oldX = event.clientX;
      }
      box.width(this.newWidth - extraWidth);
    }
  };

Tobago.Sheet.prototype.endResize = function(event) {
    if (! event) {
      event = window.event;
    }
    if (this.resizerId) {
      var width = this.newWidth;
      var columnNr = this.resizerId.substring(this.resizerId.lastIndexOf("_") + 1, this.resizerId.length);
      var table = jQuery(Tobago.Utils.escapeClientId(this.id) + ">div>table");
      var col = jQuery("colgroup>col", table).eq(columnNr);
      col.attr("width", width);

      if (jQuery.browser.msie && parseInt(jQuery.browser.version) <= 7) {
        Tobago.Sheet.fixIE67ColWidth(col);
      }

      var index = parseInt(columnNr, 10) + 1;
      var tds = jQuery("td:nth-child(" + index + ")", table);
      var horizontalDiff = 0;
      var widthWithoutPadding = width;
      if (tds.size() > 0) {
        horizontalDiff = tds.eq(0).outerWidth() - tds.eq(0).width();
        widthWithoutPadding = width - horizontalDiff;
      }
      tds.children().borderBoxWidth(widthWithoutPadding);

      this.adjustHeaderDiv();
      this.adjustResizer();
      this.storeSizes();
      delete this.resizerId;
    }
  };

/**
 * Fixes the wrong computation of col width in IE 6/7: padding will not be handled correctly.
 * @param col A jQuery object
 */
Tobago.Sheet.fixIE67ColWidth = function(col) {
  var td = col.closest("table").children("tbody").children("tr:first").children("td").eq(col.index());
  var delta = td.outerWidth() - td.width();
  col.attr("width", col.attr("width") - delta);
};

Tobago.Sheet.prototype.storeSizes = function() {
    var index = 0;
    var idPrefix = this.id + Tobago.SUB_COMPONENT_SEP + "header_div";
    var headerDiv = jQuery(Tobago.Utils.escapeClientId(idPrefix));

    var widths = "";
    jQuery(headerDiv).children().each(function() {
        if (!this.id || this.id === undefined
                || this.id.indexOf("header_box_filler") != -1) {
            return true;
        }
        widths = widths + "," + jQuery(this).outerWidth();
    });
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

(function(jQuery){
 jQuery.fn.borderBoxWidth = function(width) {
    return this.each(function() {
       obj = jQuery(this);
       var extraWidth = obj.outerWidth() - obj.width();
       obj.width(width - extraWidth);
    });
 };
})(jQuery);
