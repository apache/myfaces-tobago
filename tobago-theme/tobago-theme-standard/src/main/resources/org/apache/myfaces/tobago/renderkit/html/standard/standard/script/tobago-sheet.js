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

Tobago.Sheet = function(
    sheetId, unused1, unused2, unused3, unused4,
    clickActionId, clickReloadComponentId, dblClickActionId, dblClickReloadComponentId, renderedPartially) {
  console.debug("New Sheet with id " + sheetId); // @DEV_ONLY
  console.time("[tobago-sheet] constructor"); // @DEV_ONLY
  this.id = sheetId;
  Tobago.Sheets.put(this);
  this.clickActionId = clickActionId;
  this.clickReloadComponentId = clickReloadComponentId;
  this.dblClickActionId = dblClickActionId;
  this.dblClickReloadComponentId = dblClickReloadComponentId;
  this.renderedPartially = renderedPartially;

  this.ppPrefix = Tobago.SUB_COMPONENT_SEP + "pagingArrows" + Tobago.SUB_COMPONENT_SEP;

  this.firstRegExp = new RegExp(this.ppPrefix + "First$");

  this.prevRegExp = new RegExp(this.ppPrefix + "Prev$");

  this.nextRegExp = new RegExp(this.ppPrefix + "Next$");

  this.lastRegExp = new RegExp(this.ppPrefix + "Last$");

  this.firstRowRegExp = new RegExp("^" + this.id + "_data_tr_\\d+$");

  console.timeEnd("[tobago-sheet] constructor"); // @DEV_ONLY

  this.setup();
};

Tobago.Sheet.init = function(elements) {
  console.time("[tobago-sheet] init"); // @DEV_ONLY
  var sheets = Tobago.Utils.selectWithJQuery(elements, ".tobago-sheet");
  sheets.each(function initSheets() {
    var sheet = jQuery(this);
    var id = sheet.attr("id");
    var commands = sheet.data("tobago-row-action");
    var click = commands ? commands.click : undefined;
    var dblclick = commands ? commands.dblclick : undefined;
    new Tobago.Sheet(id, undefined, undefined, undefined, undefined,
        click != undefined ? click.action  : undefined,
        click != undefined ? click.partially : undefined,
        dblclick != undefined ? dblclick.action : undefined,
        dblclick != undefined ? dblclick.partially: undefined,
        sheet.data("tobago-partial-ids")); // type array
  });

  Tobago.Sheet.setup2(sheets);

  var commands;
  commands = Tobago.Utils.selectWithJQuery(elements, ".tobago-menu-markup-sheetSelectAll");
  commands.click(function() {
    var sheet = jQuery(Tobago.Utils.escapeClientId(jQuery(this).data("tobago-sheet-id")));
    Tobago.Sheet.selectAll(sheet);
  });
  commands = Tobago.Utils.selectWithJQuery(elements, ".tobago-menu-markup-sheetDeselectAll");
  commands.click(function() {
    var sheet = jQuery(Tobago.Utils.escapeClientId(jQuery(this).data("tobago-sheet-id")));
    Tobago.Sheet.deselectAll(sheet);
  });
  commands = Tobago.Utils.selectWithJQuery(elements, ".tobago-menu-markup-sheetToggleAll");
  commands.click(function() {
    var sheet = jQuery(Tobago.Utils.escapeClientId(jQuery(this).data("tobago-sheet-id")));
    Tobago.Sheet.toggleAll(sheet);
  });

  console.timeEnd("[tobago-sheet] init"); // @DEV_ONLY
};

Tobago.registerListener(Tobago.Sheet.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Sheet.init, Tobago.Phase.AFTER_UPDATE);

Tobago.Sheet.prototype.setupPagingLinks = function() {
    idPrefix = this.id + Tobago.SUB_COMPONENT_SEP;
    var linkBox = Tobago.element(idPrefix + "pagingLinks");
    if (linkBox) {
      for (i = 0 ; i < linkBox.childNodes.length ; i++) {
        var child = linkBox.childNodes[i];
        if (child.nodeType == 1 && child.tagName.toUpperCase() == "IMG") {
          // first, prev, next and last commands
          if (undefined == jQuery(child).attr("data-tobago-disabled")) {
              Tobago.addBindEventListener(child, "click", this, "doPaging");
          }
        } else if (child.nodeType == 1 && child.tagName.toUpperCase() == "A") {
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
    console.debug("reload sheet with action \"" + action + "\""); // @DEV_ONLY
    Tobago.Updater.update(source, action, this.renderedPartially ? this.renderedPartially : this.id, options);
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
//    console.debug("insertTarget('" + actionId + "')")
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
      console.error("Can't find text field with id = \"" + textId + "\"!"); // @DEV_ONLY
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

  jQuery(sheets).each(function() {
    Tobago.Sheet.resetInputFieldSize(jQuery(this));
  });


  // adjust body row filler cells if no scrollbar present but was calculated
  jQuery(sheets).find("[data-tobago-sheet-verticalscrollbarwidth]").each(function() {
    var table = jQuery(this);
    var verticalScrollbarWidth = table.data("tobago-sheet-verticalscrollbarwidth");
    if (verticalScrollbarWidth !== undefined) {
      var bodyDiv = table.parent().next();
      Tobago.Sheet.adjustTableWidth(bodyDiv);
    }
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
      var columnIndex = jQuery(this).data("tobago-column-index");
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
        Tobago.Sheet.resetInputFieldSize(column.closest("table"));
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
        var headerCols = headerTable.find("col");
        var bodyCols = bodyTable.find("col");
        var widths = ",";
        var oldWidthList = [];
        var i;
        for (i = 0; i < bodyCols.length; i++) {
          oldWidthList[i] = bodyCols.eq(i).width();
        }
        var usedWidth = 0;
        for (i = 0; i < headerCols.length -1; i++) {
          // last column is the filler column
          var newWidth = headerCols.eq(i).width();
          // for the hidden field
          widths = widths + newWidth + ",";
          usedWidth += newWidth;

          var oldWidth = bodyCols.eq(i).width();
          if (oldWidth != newWidth) {
            // set to the body
            bodyCols.eq(i).attr("width", newWidth);
            // reset the width inside of the cells (TD) if the value was changed.
            var tds = jQuery("td:nth-child(" + (i + 1) + ")", bodyTable);
            if (tds.size() > 0) {
              var innerWidth = tds.children().eq(0).width() - oldWidthList[i] + newWidth;
              // setting all sizes of the inner cells to the same value
              tds.children().width(innerWidth);
              // XXX later, if we have box-sizing: border-box we can set the width to 100%
            }
          }
        }
        // adjust filler space
        var bodyDiv = bodyTable.parent();
        var bodyDivWidth = bodyDiv.css("width").replace("px", "") * 1;
        console.log("usedWidth : " + usedWidth); // @DEV_ONLY
        console.log("bodyDivWidth : " + bodyDivWidth); // @DEV_ONLY
        var bodyHeight = bodyDiv.css("height").replace("px", "");
        var scrollHeight = bodyDiv.prop("scrollHeight");
        console.log("bodyHeight : " + bodyHeight); // @DEV_ONLY
        console.log("scrollHeight : " + scrollHeight); // @DEV_ONLY
        var verticalScrollbarWidth = 0;
        if (bodyHeight < scrollHeight) {
          verticalScrollbarWidth = headerTable.data("tobago-sheet-verticalscrollbarwidth");
          if (verticalScrollbarWidth) {
            bodyDivWidth -= verticalScrollbarWidth;
          }
        }

        var headerFillerWidth = 0;
        var bodyFillerWidth = 0;
        if (usedWidth <= bodyDivWidth) {
          bodyFillerWidth = bodyDivWidth - usedWidth;
          headerFillerWidth = bodyFillerWidth + verticalScrollbarWidth;
          bodyTable.css("width", bodyDivWidth);
        } else {
          if (usedWidth >= (bodyDivWidth - verticalScrollbarWidth)) {
            headerFillerWidth = verticalScrollbarWidth;
          }
          bodyTable.css("width", usedWidth);
        }

        console.log("SET fillerWidth : " + headerFillerWidth); // @DEV_ONLY
        widths = widths + headerFillerWidth + ",";
        bodyCols.last().attr("width", bodyFillerWidth);
        headerCols.last().attr("width", headerFillerWidth);

        // store the width values in a hidden field
        Tobago.Sheet.hidden(sheet, "widths").val(widths);
        return false;
      });
    });
  });

  // scrolling
  jQuery(sheets).find(".tobago-sheet-body").bind("scroll", function () {
    var sheetBody = jQuery(this);
    var scrollLeft = sheetBody.prop("scrollLeft");
    var scrollTop = sheetBody.prop("scrollTop");

    // scrolling the table should move the header
    sheetBody.siblings(".tobago-sheet-headerDiv").prop("scrollLeft", scrollLeft);

    // store the position in a hidden field
    var hidden = Tobago.Sheet.hidden(sheetBody.parent(), "scrollPosition");
    hidden.val(Math.round(scrollLeft) + ";" + Math.round(scrollTop));
  });

  // init last click
  jQuery(sheets).each(function() {
    jQuery(this).data("tobago-last-clicked-row-index", -1);
  });

  // restore scroll position
  jQuery(sheets).each(function () {
    var sheet = jQuery(this);
    var hidden = Tobago.Sheet.hidden(sheet, "scrollPosition");
    var sep = hidden.val().indexOf(";");
    if (sep != -1) {
      var scrollLeft = hidden.val().substr(0, sep);
      var scrollTop = hidden.val().substr(sep + 1);
      var body = sheet.children(".tobago-sheet-body");
      body.prop("scrollLeft", scrollLeft);
      body.prop("scrollTop", scrollTop);
      sheet.children(".tobago-sheet-headerDiv").prop("scrollLeft", scrollLeft);
    }
  });

  // add selection listeners
  jQuery(sheets).each(function () {
    var sheet = jQuery(this);
    var selectionMode = sheet.data("tobago-selection-mode");
    if (selectionMode == "single" || selectionMode == "singleOrNone" || selectionMode == "multi") {
      Tobago.Sheet.getRows(sheet).each(function () {
        var row = jQuery(this);
        row.bind("mousedown", function (event) {
          sheet.data("tobago-mouse-down-x", event.clientX);
          sheet.data("tobago-mouse-down-y", event.clientY);
        });
        row.bind("click", function (event) {
          // todo: re-implement
          Tobago.Sheets.get(sheet.attr("id")).doSelection(event);
        });
        // todo:
        if (Tobago.Sheets.get(sheet.attr("id")).dblClickActionId) {
          row.bind("dblclick", function (event) {
            // todo: re-implement
            Tobago.Sheets.get(sheet.attr("id")).doDblClick(event);
          });
        }
      });
    }
    sheet.find("input.tobago-sheet-columnSelector").click(function(event) {event.preventDefault()});
  });

    // init reload
  jQuery(sheets).filter("[data-tobago-reload]").each(function() {
    var sheet = jQuery(this);
    Tobago.Sheets.get(sheet.attr("id")).initReload();
  });

};

Tobago.Sheet.adjustTableWidth = function(bodyDiv) {
  var bodyHeight = bodyDiv.css("height").replace("px", "");
  var scrollHeight = bodyDiv.prop("scrollHeight");
  console.log("bodyHeight : " + bodyHeight); // @DEV_ONLY
  console.log("scrollHeight : " + scrollHeight); // @DEV_ONLY
  if (bodyHeight >= scrollHeight) {
    var table = bodyDiv.find("table");
    var bodyDivWidth = bodyDiv.css("width").replace("px", "") * 1;
    console.log("bodyDivWidth : " + bodyDivWidth); // @DEV_ONLY
    var tableWidth = table.css("width").replace("px", "") * 1;
    console.log("tableWidth : " + tableWidth); // @DEV_ONLY
    if (tableWidth < bodyDivWidth) {

      var fillerWidth = bodyDiv.prev().find("col").last().attr("width").replace("px", "") * 1;
      console.log("set filler col width : " + fillerWidth); // @DEV_ONLY
      table.find("col").last().attr("width", fillerWidth);

      console.log("set table width : " + bodyDivWidth); // @DEV_ONLY
      table.css("width", bodyDivWidth);
    }
  }
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

Tobago.Sheet.prototype.setup = function() {
  console.time("[tobago-sheet] setup"); // @DEV_ONLY

  // IE 6+7
  if (Tobago.browser.isMsie67) {
    jQuery(Tobago.Utils.escapeClientId(this.id) + ">div>table>colgroup>col").each(function() {
      Tobago.Sheet.fixIE67ColWidth(jQuery(this));
    });

    // to avoid horizontal scroll bar. This value must be removed, when resizing the columns!
    jQuery(Tobago.Utils.escapeClientId(this.id)).find(".tobago-sheet-cell-markup-filler").each(function() {
      jQuery(this).css("width", "0");
    });
  }

  this.setupPagingLinks();
  this.setupPagePaging();
  this.setupRowPaging();

  this.initReload();
  console.timeEnd("[tobago-sheet] setup"); // @DEV_ONLY
};

Tobago.Sheet.prototype.initReload = function() {
  var sheet = jQuery(Tobago.Utils.escapeClientId(this.id));
  var reload = sheet.data("tobago-reload");
  if (typeof reload == "number") {
    Tobago.addReloadTimeout(this.id, Tobago.bind2(this, "reloadWithAction", null, this.id), reload);
  }
  /*
  setTimeout(function() {
//    this.reloadWithAction(null, sheet.attr("id"));
    Tobago.Updater.update(null, sheet.attr("id"), this.renderedPartially ? this.renderedPartially : this.id, options);
  }, reload);
  */
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

      // find the row, if the has clicked on an element inside the row
      var row = jQuery(Tobago.element(event)).closest("tr");
      var sheet = row.closest(".tobago-sheet");

      if (Math.abs(sheet.data("tobago-mouse-down-x") - event.clientX)
          + Math.abs(sheet.data("tobago-mouse-down-y") - event.clientY) > 5) {
        // The user has moved the mouse. We assume, the user want to select some text inside the sheet,
        // so we doesn't select the row.
        return;
      }

      Tobago.clearSelection();

      var rows = Tobago.Sheet.getRows(sheet);
      var selector = Tobago.Sheet.getSelectorCheckbox(row);

      var selectionMode = sheet.data("tobago-selection-mode");

      if ((!event.ctrlKey && !event.metaKey && selector.size() == 0)
          || selectionMode == "single" || selectionMode == "singleOrNone") {
        Tobago.Sheet.deselectAll(sheet);
        Tobago.Sheet.resetSelected(sheet);
      }

      var lastClickedRowIndex = sheet.data("tobago-last-clicked-row-index");
      if (event.shiftKey && selectionMode == "multi" && lastClickedRowIndex > -1) {
        if (lastClickedRowIndex <= row.index()) {
          Tobago.Sheet.selectRange(sheet, rows, lastClickedRowIndex, row.index(), true, false);
        } else {
          Tobago.Sheet.selectRange(sheet, rows, row.index(), lastClickedRowIndex, true, false);
        }
      } else if (selectionMode != "singleOrNone" || !Tobago.Sheet.isRowSelected(sheet, row)) {
        Tobago.Sheet.toggleSelection(sheet, row, selector);
      }
      if (this.clickActionId) {
        var action;
        var index = this.clickActionId.indexOf(this.id);
        var rowIndex = Tobago.Sheet.getDataIndex(sheet, row);
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
      var sheet = row.closest(".tobago-sheet");
      var rowIndex = row.index() + sheet.data("tobago-first");
      if (this.dblClickActionId) {
        var action;
        var index = this.dblClickActionId.indexOf(this.id);
        if (index >= 0) {
          action = this.id + ":" + rowIndex + ":" + this.dblClickActionId.substring(index + this.id.length +1);
        } else {
          action = this.id + ":" + rowIndex + ":" + this.dblClickActionId;
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
 * @param row as a jQuery object
 */
Tobago.Sheet.getSelectorCheckbox = function(row) {
  return row.find(">td>input.tobago-sheet-columnSelector");
};

Tobago.Sheet.prototype.getSiblingRow = function(row, i) {
  return row.parentNode.childNodes[i];
};

Tobago.Sheet.getRows = function(sheet) {
  return sheet.find(">div>table.tobago-sheet-bodyTable>tbody>tr");
};

Tobago.Sheet.isRowSelected = function(sheet, row) {
  var rowIndex = +row.data("tobago-row-index");
  if (!rowIndex) {
    rowIndex = row.index() + sheet.data("tobago-first");
  }
  return Tobago.Sheet.isSelected(sheet, rowIndex);
};

Tobago.Sheet.isSelected = function(sheet, rowIndex) {
  return Tobago.Sheet.hidden(sheet, "selected").val().indexOf("," + rowIndex + ",") >= 0;
};

Tobago.Sheet.resetSelected = function(sheet) {
  Tobago.Sheet.hidden(sheet, "selected").val(",");
};

Tobago.Sheet.toggleSelection = function(sheet, row, checkbox) {
  sheet.data("tobago-last-clicked-row-index", row.index());
  if (!checkbox.is(":disabled")) {
    var selected = Tobago.Sheet.hidden(sheet, "selected");
    var rowIndex = Tobago.Sheet.getDataIndex(sheet, row);
    if (Tobago.Sheet.isSelected(sheet, rowIndex)) {
      Tobago.Sheet.deselectRow(selected, rowIndex, row, checkbox);
    } else {
      Tobago.Sheet.selectRow(selected, rowIndex, row, checkbox);
    }
  }
};

Tobago.Sheet.selectAll = function(sheet) {
  var rows = Tobago.Sheet.getRows(sheet);
  if (rows.size() > 0) {
    Tobago.Sheet.selectRange(sheet, rows, 0, rows.size() - 1, true, false);
  }
};

Tobago.Sheet.deselectAll = function(sheet) {
  var rows = Tobago.Sheet.getRows(sheet);
  if (rows.size() > 0) {
    Tobago.Sheet.selectRange(sheet, rows, 0, rows.size() - 1, false, true);
  }
};

Tobago.Sheet.toggleAll = function(sheet) {
  var rows = Tobago.Sheet.getRows(sheet);
  if (rows.size() > 0) {
    Tobago.Sheet.selectRange(sheet, rows, 0, rows.size() - 1, true, true);
  }
};

Tobago.Sheet.selectRange = function(sheet, rows, first, last, selectDeselected, deselectSelected) {
//  console.info("select any 15");
//  var start = new Date().getTime();

  var selected = Tobago.Sheet.hidden(sheet, "selected");
  for (var i = first; i <= last; i++) {
    var row = rows.eq(i);
    var checkbox = Tobago.Sheet.getSelectorCheckbox(row);
    if (!checkbox.is(":disabled")) {
      var rowIndex = Tobago.Sheet.getDataIndex(sheet, row);
      var on = selected.val().indexOf("," + rowIndex + ",") >= 0;
      if (selectDeselected && !on) {
        Tobago.Sheet.selectRow(selected, rowIndex, row, checkbox);
      } else if (deselectSelected && on) {
        Tobago.Sheet.deselectRow(selected, rowIndex, row, checkbox);
      }
    }
  }

//  console.info("select any 15 +++++++++++++ " + (new Date().getTime() - start));
};

Tobago.Sheet.getDataIndex = function(sheet, row) {
  var rowIndex = row.data("tobago-row-index");
  if (rowIndex) {
    return +rowIndex;
  } else {
    return row.index() + sheet.data("tobago-first");
  }
};

/**
 * @param selected input-element type=hidden: Hidden field with the selection state information
 * @param rowIndex int: zero based index of the row.
 * @param row tr-element: the row.
 * @param checkbox input-element: selector in the row.
 */
Tobago.Sheet.selectRow = function(selected, rowIndex, row, checkbox) {
  selected.val(selected.val() + rowIndex + ",");
  row.addClass("tobago-sheet-row-markup-selected");
//  checkbox.prop("checked", true);
  setTimeout(function() {checkbox.prop("checked", true);}, 0);
};

/**
 * @param selected input-element type=hidden: Hidden field with the selection state information
 * @param rowIndex int: zero based index of the row.
 * @param row tr-element: the row.
 * @param checkbox input-element: selector in the row.
 */
Tobago.Sheet.deselectRow = function(selected, rowIndex, row, checkbox) {
  selected.val(selected.val().replace(new RegExp("," + rowIndex + ","), ","));
  row.removeClass("tobago-sheet-row-markup-selected");
//  checkbox.prop("checked", false);
  setTimeout(function() {checkbox.prop("checked", false);}, 0);
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

Tobago.Sheet.resetInputFieldSize = function (table) {
  var span = table.find("td>div>span");
  span.children("input,select").each(function () {
    var input = jQuery(this);
    input.outerWidth(input.parent().width());
  });
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
