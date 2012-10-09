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

Tobago.Sheet.init = function(elements) {
  var sheets = Tobago.Utils.selectWidthJQuery(elements, ".tobago-sheet");
  sheets.each(function initSheets() {
    var sheet = jQuery(this);
    var id = sheet.attr("id");
    var frequency = sheet.data("tobago-reload");
    var selectionMode = sheet.data("tobago-selectionmode");
    var commands = sheet.data("tobago-rowaction");
    var click = commands ? commands.click : undefined;
    var dblclick = commands ? commands.dblclick : undefined;
    var columnSelectorIndex
        = sheet.find(".tobago-sheet-headerTable > .tobago-sheet-header > .tobago-sheet-toolBar").parent().index();
    new Tobago.Sheet(id, undefined, selectionMode, columnSelectorIndex, frequency,
        click != undefined ? click.actionId  : undefined,
        click != undefined ? click.partially : undefined,
        dblclick != undefined ? dblclick.actionId : undefined,
        dblclick != undefined ? dblclick.partially: undefined,
        // todo: use sheet.data("data-tobago-partially"). What is the type? Array? Test it.
        sheet.attr("data-tobago-partially"));
  });

  Tobago.Sheet.setup2(sheets);

  var commands;
  commands = Tobago.Utils.selectWidthJQuery(elements, ".tobago-menu-markup-sheetSelectAll");
  commands.click(function() {
    Tobago.Sheets.get(jQuery(this).data("tobago-sheetid")).selectAll();
  });
  commands = Tobago.Utils.selectWidthJQuery(elements, ".tobago-menu-markup-sheetDeselectAll");
  commands.click(function() {
    Tobago.Sheets.get(jQuery(this).data("tobago-sheetid")).deselectAll();
  });
  commands = Tobago.Utils.selectWidthJQuery(elements, ".tobago-menu-markup-sheetToggleAll");
  commands.click(function() {
    Tobago.Sheets.get(jQuery(this).data("tobago-sheetid")).toggleAll();
  });

};

Tobago.registerListener(Tobago.Sheet.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Sheet.init, Tobago.Phase.AFTER_UPDATE);


Tobago.Sheet.prototype.setupSortHeaders = function() {
  var sheet = this;
  jQuery(Tobago.Utils.escapeClientId(sheet.id)).find(".tobago-sheet-header[data-tobago-sorterId]").each(function() {

    jQuery(this).click({sheet: sheet, sorterId: jQuery(this).data("tobago-sorterId")}, function(event) {

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
    Tobago.Updater.update(source, action, this.renderedPartially?this.renderedPartially:this.id, options);
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
          Tobago.stopEventPropagation(event);
          event.returnValue = false;
          this.reloadWithAction(event.srcElement, input.actionId);
        } else {
          this.textInput = input;
          this.hideInput();
        }
      }
    }
  };

Tobago.Sheet.setup2 = function (sheets) {

  // set the height of the header-cells to the height of the td (height: 100% doesn't work)
  jQuery(sheets).find(".tobago-sheet-headerCell").each(function () {
    var div = jQuery(this);
    div.height(div.parent().height());
  });

  // set the height of the header-cells to the height of the td (height: 100% doesn't work)
  jQuery(sheets).children(".tobago-sheet-headerDiv").each(function () {
    jQuery(this).height(jQuery(this).children(":first").height());
  });

  // resize: mouse events
  jQuery(sheets).find(".tobago-sheet-headerResize").each(function () {
    jQuery(this).click(function () {
      return false;
    });
    jQuery(this).mousedown(function (event) {
      // begin resizing
//      console.log("begin");
//      console.log(event);
      var columnIndex = jQuery(this).data("tobago-columnindex");
      var body = jQuery("body");
      var column = jQuery(this).closest("table").children("colgroup").children("col").eq(columnIndex);
      var filler = column.siblings("col:last");
      var data = {
        columnIndex: columnIndex,
        originalClientX: event.clientX,
        column: column,
        originalColumnWidth: parseInt(column.attr("width")),
        filler: filler,
        originalFillerWidth: parseInt(filler.attr("width"))
      };
      body.on("mousemove", data, function(event) {
        var delta = event.clientX - event.data.originalClientX;
//        console.log("columnResize(): " + event.data.columnIndex + ", delta: " + delta);
        delta = - Math.min(-delta, event.data.originalColumnWidth - 10);
        var columnWidth = event.data.originalColumnWidth + delta;
        var fillerWidth = Math.max(event.data.originalFillerWidth - delta, 0);
        event.data.column.attr("width", columnWidth);
        event.data.filler.attr("width", fillerWidth);
        var sum = 0;
        column.parent().children("col").each(function() {
          sum += parseInt(jQuery(this).attr("width"));
        });
        Tobago.clearSelection();
        return false;
      });
      body.one("mouseup", {sheet: jQuery(this).closest(".tobago-sheet")}, function (event) {
        // switch off the mouse move listener
        jQuery("body").off("mousemove");

        // copy the width values from the header to the body, (and build a list of it)
        var sheet = event.data.sheet;
        var headerTable = sheet.find(".tobago-sheet-headerTable");
        var bodyTable = sheet.find(".tobago-sheet-bodyTable");
        var headerList = headerTable.find("col");
        var bodyList = bodyTable.find("col");
        var widths = ",";
        for (var i = 0; i < headerList.length; i++) {
          var newWidth = headerList.eq(i).width();
          // for the hidden field
          widths = widths + newWidth + ",";

          var oldWidth = bodyList.eq(i).width();
          if (oldWidth != newWidth) {
            // set to the body
            bodyList.eq(i).attr("width", newWidth);
            // reset the width inside of the cells (TD) if the value was changed.
            var tds = jQuery("td:nth-child(" + (i + 1) + ")", bodyTable);
            if (tds.size() > 0) {
              var innerWidth = tds.children().eq(0).width() - oldWidth + newWidth;
              // setting all sizes of the inner cells to the same value
              tds.children().width(innerWidth);
              // XXX later, if we have box-sizing: border-box we can set the width to 100%
            }
          }
        }
        // store the width values in a hidden field
        Tobago.Sheet.hidden(sheet, "widths").val(widths);
        return false;
      });
    });
  });

  // scrolling
  jQuery(sheets).find(".tobago-sheet-body").bind("scroll", function () {
    var scrollLeft = jQuery(this).prop("scrollLeft");
    var scrollTop = jQuery(this).prop("scrollTop");

    // scrolling the table should move the header
    jQuery(this).siblings(".tobago-sheet-headerDiv").prop("scrollLeft", scrollLeft);

    // store the position in a hidden field
    var hidden = Tobago.Sheet.hidden(jQuery(this).parent(), "scrollPosition");
    hidden.val(scrollLeft + ";" + scrollTop);
  });
};

/** Returns the specific hidden field of a sheet
 * @param sheet The sheet as jQuery Object
 * @param idSuffix The if suffix of the hidden field
 * @return the hidden field.
 * */
Tobago.Sheet.hidden = function(sheet, idSuffix) {
  var id = sheet.attr("id") + Tobago.SUB_COMPONENT_SEP + idSuffix;
  return sheet.children(Tobago.Utils.escapeClientId(id));
};
/*

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
*/


Tobago.Sheet.prototype.setup = function() {
  this.setupStart = new Date(); // @DEV_ONLY

  // IE 6+7
  if (jQuery.browser.msie && parseInt(jQuery.browser.version) <= 7) {
    jQuery(Tobago.Utils.escapeClientId(this.id) + ">div>table>colgroup>col").each(function() {
      Tobago.Sheet.fixIE67ColWidth(jQuery(this));
    });

    // to avoid horizontal scroll bar. This value must be removed, when resizing the columns!
    jQuery(Tobago.Utils.escapeClientId(this.id)).find(".tobago-sheet-cell-markup-filler").each(function() {
      jQuery(this).css("width", "0");
    });
  }

  this.setScrollPosition();

  this.firstRowIndex = parseInt(this.getRows().eq(0).attr("rowIndexInModel"));
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
  this.setupEnd = new Date(); // @DEV_ONLY
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

Tobago.Sheet.prototype.addSelectionListener = function() {
  var currentSheet = this;
  currentSheet.getRows().each(function() {
    var row = this;
    Tobago.addBindEventListener(row, "mousedown", currentSheet, "doMouseDownSelect");
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

    if (srcElement.id.search(/_data_row_selector_/) > -1  || !Tobago.isInputElement(srcElement.tagName)) {

      if (Math.abs(this.mouseDownX - event.clientX) + Math.abs(this.mouseDownY - event.clientY) > 5) {
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
        this.resetSelected();
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
      if (this.clickActionId) {
        var action;
        var index = this.clickActionId.indexOf(this.id);
        if (index >= 0) {
          action = this.id + ":" + rowIndex + ":" + this.clickActionId.substring(index + this.id.length +1);
        } else {
          action = this.id + ":" + rowIndex + ":" + this.clickActionId;
        }
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

    if (! Tobago.isInputElement(srcElement.tagName)) {
      var row = jQuery(Tobago.element(event)).closest("tr");
      var rowIndex = row.index() + this.firstRowIndex;
      if (this.dblClickActionId) {
        var action;
        var index = this.clickActionId.indexOf(this.id);
        if (index >= 0) {
          action = this.id + ":" + rowIndex + ":" + this.clickActionId.substring(index + this.id.length +1);
        } else {
          action = this.id + ":" + rowIndex + ":" + this.clickActionId;
        }
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
  return jQuery(Tobago.Utils.escapeClientId(this.id) + ">div>table.tobago-sheet-bodyTable>tbody>tr");
};

Tobago.Sheet.prototype.isSelected = function(rowIndex) {
  var selected = Tobago.element(this.selectedId);
  return selected.value.indexOf("," + rowIndex + ",") >= 0;
};

Tobago.Sheet.prototype.resetSelected = function() {
  var selected = Tobago.element(this.selectedId);
  return selected.value = ",";
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

/**
 * Fixes the wrong computation of col width in IE 6/7: padding will not be handled correctly.
 * @param col A jQuery object
 */
Tobago.Sheet.fixIE67ColWidth = function(col) {
  var td = col.closest("table").children("tbody").children("tr:first").children("td").eq(col.index());
  var delta = td.outerWidth() - td.width();
  col.attr("width", col.attr("width") - delta);
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
