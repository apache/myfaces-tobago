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

Tobago4.Sheets = {
  sheets: {},

  get: function(id) {
    return this.sheets[id];
  },

  put: function(sheet) {
    this.sheets[sheet.id] = sheet;
  }
};

Tobago4.Sheet = function(
    sheetId, unused1, unused2, unused3, unused4,
    clickActionId, clickReloadComponentId, dblClickActionId, dblClickReloadComponentId, behaviorCommands) {
  console.debug("New Sheet with id " + sheetId);
  console.time("[tobago-sheet] constructor");
  this.id = sheetId;
  Tobago4.Sheets.put(this);
  this.clickActionId = clickActionId;
  this.clickReloadComponentId = clickReloadComponentId;
  this.dblClickActionId = dblClickActionId;
  this.dblClickReloadComponentId = dblClickReloadComponentId;
  this.behaviorCommands = behaviorCommands;

  this.setup();

  console.timeEnd("[tobago-sheet] constructor");
};

Tobago4.Sheet.init = function(element:HTMLElement) {
  console.time("[tobago-sheet] init");
  const sheets: Array<HTMLElement> = Tobago.querySelectorAllOrSelfByClass(element, "tobago-sheet");
  sheets.forEach(function (element):void {
    var $sheet = jQuery(element);
    var id = $sheet.attr("id");
    var commands = $sheet.data("tobago-row-action");
    var click = commands ? commands.click : undefined;
    var dblclick = commands ? commands.dblclick : undefined;
    new Tobago4.Sheet(id, undefined, undefined, undefined, undefined,
        click !== undefined ? click.action  : undefined,
        click !== undefined ? click.partially : undefined, // fixme: partially no longer used
        dblclick !== undefined ? dblclick.action : undefined,
        dblclick !== undefined ? dblclick.partially: undefined, // fixme: partially no longer used
        $sheet.data("tobago-behavior-commands")); // type array

    //////////////////////////////////////////////
    // XXX bugfix for IE11 (lower than IE11 isn't supported for that feature)
    // if a max-height is set on the sheet,
    if (Tobago4.browser.isMsie && $sheet.css("max-height") !== "none") {
      $sheet.css("height", $sheet.css("height")); // reset the height to the same value
    }
  });

  Tobago4.Sheet.setup2(sheets);

  element.querySelectorAll( ".tobago-sheet-header .tobago-sheet-columnSelector").forEach(function (element) {
    element.addEventListener("click", function(event: MouseEvent) {
      var $checkbox = jQuery(event.target);
      if ($checkbox.is(':checked')) {
        Tobago4.Sheet.selectAll($checkbox.closest(".tobago-sheet"));
      } else {
        Tobago4.Sheet.deselectAll($checkbox.closest(".tobago-sheet"));
      }
    });
  });

  console.timeEnd("[tobago-sheet] init");
};

Tobago.Listener.register(Tobago4.Sheet.init, Tobago.Phase.DOCUMENT_READY);
Tobago.Listener.register(Tobago4.Sheet.init, Tobago.Phase.AFTER_UPDATE);

Tobago4.Sheet.reloadWithAction = function(elementId) {
    console.debug("reload sheet with action '" + elementId + "'");
  var executeIds = elementId;
  var renderIds = elementId;
  // XXX FIXME: behaviorCommands will probably be empty and not working!
  // if (this.behaviorCommands && this.behaviorCommands.reload) {
  //   if (this.behaviorCommands.reload.execute) {
  //     executeIds += " " + this.behaviorCommands.reload.execute;
  //   }
  //   if (this.behaviorCommands.reload.render) {
  //     renderIds += " " + this.behaviorCommands.reload.render;
  //   }
  // }
  jsf.ajax.request(
      elementId,
      null,
      {
        "javax.faces.behavior.event": "reload",
        execute: executeIds,
        render: renderIds
      });
};

Tobago4.Sheet.setup2 = function (sheets) {

  // synchronize column widths
  sheets.forEach(function (element):void {
    var $sheet = jQuery(element);

    // basic idea: there are two possible sources for the sizes:
    // 1. the columns attribute of <tc:sheet> like {"columns":[1.0,1.0,1.0]}, held by data attribute "tobago-layout"
    // 2. the hidden field which may contain a value like ",300,200,100,"
    //
    // The 1st source usually is the default set by the developer.
    // The 2nd source usually is the value set by the user manipulating the column widths.
    //
    // So, if the 2nd is set, we use it, if not set, we use the 1st source.
    //

    var hidden = Tobago4.Sheet.findHiddenWidths($sheet);

    if (hidden.length > 0 && hidden.val()) {
      // if the hidden has a value, than also the colgroup/col are set correctly
      var columnWidths = jQuery.parseJSON(hidden.val());
      console.info("columnWidths: " + columnWidths);
    }
    if (columnWidths !== undefined && columnWidths.length === 0) {
      // otherwise use the layout definition
      var layout = $sheet.data("tobago-layout");
      if (layout && layout.columns && layout.columns.length > 0) {
        var tokens = layout.columns;
        var rendered = jQuery.parseJSON(Tobago4.Sheet.findHiddenRendered($sheet).val());

        var $headerTable = $sheet.children("header").children("table");
        var $headerCol = $headerTable.children("colgroup").children("col");
        var $bodyTable = $sheet.children("div").children("table");
        var $bodyCol = $bodyTable.children("colgroup").children("col");

        console.assert($headerCol.length - 1 === $bodyCol.length, "header and body column number doesn't match");

        var i;
        var intValue;
        var sumRelative = 0;
        var widthRelative = $bodyTable.width();
        for (i = 0; i < tokens.length; i++) {
          if (rendered[i] === "true") {
            if (typeof tokens[i] === "number") {
              sumRelative += tokens[i];
            } else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
              intValue = parseInt(tokens[i].measure);
              if (tokens[i].measure.lastIndexOf("px") > 0) {
                widthRelative -= intValue;
              } else if (tokens[i].measure.lastIndexOf("%") > 0) {
                widthRelative -= $bodyTable.width() / 100 * intValue;
              }
            } else {
              console.debug("auto? = " + tokens[i]);
            }
          }
        }
        if (widthRelative < 0) {
          widthRelative = 0;
        }

        var headerBodyColCount = 0;
        for (i = 0; i < tokens.length; i++) {
          var colWidth = 0;
          if (rendered[i] === "true") {
            if (typeof tokens[i] === "number") {
              colWidth = tokens[i] * widthRelative / sumRelative;
            } else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
              intValue = parseInt(tokens[i].measure);
              if (tokens[i].measure.lastIndexOf("px") > 0) {
                colWidth = intValue;
              } else if (tokens[i].measure.lastIndexOf("%") > 0) {
                colWidth = $bodyTable.width() / 100 * intValue;
              }
            } else {
              console.debug("auto? = " + tokens[i]);
            }
            if (colWidth > 0) { // because tokens[i] == "auto"
              $headerCol.eq(headerBodyColCount).attr("width", colWidth);
              $bodyCol.eq(headerBodyColCount).attr("width", colWidth);
            }
            headerBodyColCount++;
          }
        }
      }
    }
    Tobago4.Sheet.addHeaderFillerWidth($sheet);
  });

  // resize: mouse events
  sheets.forEach(function (element): void {
    element.querySelectorAll(".tobago-sheet-headerResize").forEach(function (resizeElement): void {
      const $resizeElement = jQuery(resizeElement);
      $resizeElement.click(function () {
        return false;
      });
      $resizeElement.mousedown(function (event) {
        // begin resizing
        console.info("down");
        var columnIndex = $resizeElement.data("tobago-column-index");
        var $body = jQuery("body");
        var $sheet = $resizeElement.closest(".tobago-sheet");
        var $headerTable = $sheet.find(".tobago-sheet-headerTable");
        var $bodyTable = $sheet.find(".tobago-sheet-bodyTable");
        var headerColumn = $headerTable.children("colgroup").children("col").eq(columnIndex);
        var bodyColumn = $bodyTable.children("colgroup").children("col").eq(columnIndex);
        var data = {
          columnIndex: columnIndex,
          originalClientX: event.clientX,
          headerColumn: headerColumn,
          bodyColumn: bodyColumn,
          originalHeaderColumnWidth: parseInt(headerColumn.attr("width"))
        };

        // Set width attribute: Avoid scrollBar position flip to 0.
        $headerTable.css("width", $headerTable.outerWidth());
        $bodyTable.css("width", $bodyTable.outerWidth());

        $body.on("mousemove", data, function (event) {
          console.info("move");
          var delta = event.clientX - event.data.originalClientX;
          delta = -Math.min(-delta, event.data.originalHeaderColumnWidth - 10);
          var columnWidth = event.data.originalHeaderColumnWidth + delta;
          event.data.headerColumn.attr("width", columnWidth);
          event.data.bodyColumn.attr("width", columnWidth);
          if (window.getSelection) {
            window.getSelection().removeAllRanges();
          }
          return false;
        });
        $body.one("mouseup", function (event) {
          // switch off the mouse move listener
          jQuery("body").off("mousemove");
          console.info("up");
          // copy the width values from the header to the body, (and build a list of it)
          var tokens = $sheet.data("tobago-layout").columns;
          var rendered = jQuery.parseJSON(Tobago4.Sheet.findHiddenRendered($sheet).val());
          var hidden = Tobago4.Sheet.findHiddenWidths($sheet);
          var hiddenWidths;
          if (hidden.length > 0 && hidden.val()) {
            hiddenWidths = jQuery.parseJSON(hidden.val());
          }
          var headerCols = $headerTable.find("col");
          var bodyCols = $bodyTable.find("col");
          var widths = [];
          var oldWidthList = [];
          var i;
          for (i = 0; i < bodyCols.length; i++) {
            oldWidthList[i] = bodyCols.eq(i).width();
          }
          var usedWidth = 0;
          var headerBodyColCount = 0;
          for (i = 0; i < rendered.length; i++) {
            if (rendered[i] === "true") {
              // last column is the filler column
              var newWidth = headerCols.eq(headerBodyColCount).width();
              // for the hidden field
              widths[i] = newWidth;
              usedWidth += newWidth;

              var oldWidth = bodyCols.eq(headerBodyColCount).width();
              if (oldWidth !== newWidth) {
                // set to the body
                bodyCols.eq(headerBodyColCount).attr("width", newWidth);
                // reset the width inside of the cells (TD) if the value was changed.
                var $tds = jQuery("td:nth-child(" + (headerBodyColCount + 1) + ")", $bodyTable);
                if ($tds.length > 0) {
                  var innerWidth = $tds.children().eq(0).width() - oldWidthList[headerBodyColCount] + newWidth;
                  // setting all sizes of the inner cells to the same value
                  $tds.children().width(innerWidth);
                  // XXX later, if we have box-sizing: border-box we can set the width to 100%
                }
              }
              headerBodyColCount++;
            } else if (hiddenWidths !== undefined && hiddenWidths.length >= i) {
              widths[i] = hiddenWidths[i];
            } else {
              if (typeof tokens[i] === "number") {
                widths[i] = 100;
              } else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
                var intValue = parseInt(tokens[i].measure);
                if (tokens[i].measure.lastIndexOf("px") > 0) {
                  widths[i] = intValue;
                } else if (tokens[i].measure.lastIndexOf("%") > 0) {
                  widths[i] = $bodyTable.width() / 100 * intValue;
                }
              }
            }
          }
          // Remove width attribute: Avoid scrollBar position flip to 0.
          $headerTable.css("width", "");
          $bodyTable.css("width", "");

          // store the width values in a hidden field
          Tobago4.Sheet.findHiddenWidths($sheet).val(JSON.stringify(widths));
          return false;
        });
      });
    });
  });

  // scrolling
  sheets.forEach(function (element): void {
    var $sheet = jQuery(element);
    $sheet.find(".tobago-sheet-body").on("scroll", function () {
      var sheetBody = jQuery(this);
      var scrollLeft = sheetBody.prop("scrollLeft");
      var scrollTop = sheetBody.prop("scrollTop");

      // scrolling the table should move the header
      sheetBody.siblings(".tobago-sheet-header").prop("scrollLeft", scrollLeft);

      // store the position in a hidden field
      var hidden = Tobago4.Sheet.findHiddenScrollPosition(sheetBody.parent());
      hidden.val(Math.round(scrollLeft) + ";" + Math.round(scrollTop));
    });
  });

  // restore scroll position
  sheets.forEach(function (element):void {
    var $sheet = jQuery(element);
    var $hidden = Tobago4.Sheet.findHiddenScrollPosition($sheet);
    var sep = $hidden.val().indexOf(";");
    if (sep !== -1) {
      var scrollLeft = $hidden.val().substr(0, sep);
      var scrollTop = $hidden.val().substr(sep + 1);
      var $body = $sheet.children(".tobago-sheet-body");
      $body.prop("scrollLeft", scrollLeft);
      $body.prop("scrollTop", scrollTop);
      $sheet.children(".tobago-sheet-header").prop("scrollLeft", scrollLeft);
    }
  });

  // add selection listeners
  sheets.forEach(function (element): void {
    var $sheet = jQuery(element);
    var selectionMode = $sheet.data("tobago-selection-mode");
    if (selectionMode === "single" || selectionMode === "singleOrNone" || selectionMode === "multi") {
      Tobago4.Sheet.getRows($sheet).each(function () {
        var $row = jQuery(this);
        $row.on("mousedown", function (event) {
          $sheet.data("tobago-mouse-down-x", event.clientX);
          $sheet.data("tobago-mouse-down-y", event.clientY);
        });
        $row.click(function (event) {
          var $target = jQuery(event.target);
          var $row = jQuery(this);
          if ($target.hasClass("tobago-sheet-columnSelector") || !Tobago4.Sheet.isInputElement($target)) {
            var $sheet = $row.closest(".tobago-sheet");

            if (Math.abs($sheet.data("tobago-mouse-down-x") - event.clientX)
                + Math.abs($sheet.data("tobago-mouse-down-y") - event.clientY) > 5) {
              // The user has moved the mouse. We assume, the user want to select some text inside the sheet,
              // so we doesn't select the row.
              return;
            }

            if (window.getSelection) {
              window.getSelection().removeAllRanges();
            }

            var $rows = Tobago4.Sheet.getRows($sheet);
            var $selector = Tobago4.Sheet.getSelectorCheckbox($row);

            var selectionMode = $sheet.data("tobago-selection-mode");

            if ((!event.ctrlKey && !event.metaKey && $selector.length === 0)
                || selectionMode === "single" || selectionMode === "singleOrNone") {
              Tobago4.Sheet.deselectAll($sheet);
              Tobago4.Sheet.resetSelected($sheet);
            }

            var lastClickedRowIndex = $sheet.data("tobago-last-clicked-row-index");
            if (event.shiftKey && selectionMode === "multi" && lastClickedRowIndex > -1) {
              if (lastClickedRowIndex <= $row.index()) {
                Tobago4.Sheet.selectRange($sheet, $rows, lastClickedRowIndex, $row.index(), true, false);
              } else {
                Tobago4.Sheet.selectRange($sheet, $rows, $row.index(), lastClickedRowIndex, true, false);
              }
            } else if (selectionMode !== "singleOrNone" || !Tobago4.Sheet.isRowSelected($sheet, $row)) {
              Tobago4.Sheet.toggleSelection($sheet, $row, $selector);
            }
            var commands = $sheet.data("tobago-row-action");
            var click = commands ? commands.click : undefined;
            var clickActionId = click ? click.action : undefined;
            var clickExecuteIds = click ? click.execute : undefined;
            var clickRenderIds = click ? click.render : undefined;

            var id = $sheet.attr("id");

            if (clickActionId) {
              var action;
              var index = clickActionId.indexOf(id);
              var rowIndex = Tobago4.Sheet.getDataIndex($sheet, $row);
              if (index >= 0) {
                action = id + ":" + rowIndex + ":" + clickActionId.substring(index + id.length + 1);
              } else {
                action = id + ":" + rowIndex + ":" + clickActionId;
              }
              if (clickExecuteIds && clickExecuteIds.length > 0) {
                //Tobago.reloadComponent($target.get(0), clickReloadComponentId, action)
                jsf.ajax.request(
                    action,
                    event,
                    {
                      //"javax.faces.behavior.event": "click",
                      execute: clickExecuteIds,
                      render: clickRenderIds
                    });
              } else {
                Tobago4.submitAction($target.get(0), action);
              }
            }
          }
        });
        // todo:
        if (Tobago4.Sheets.get($sheet.attr("id")).dblClickActionId) {
          $row.on("dblclick", function (event) {
            // todo: re-implement
            Tobago4.Sheets.get($sheet.attr("id")).doDblClick(event);
          });
        }
      });
    }
    $sheet.find(".tobago-sheet-cell > input.tobago-sheet-columnSelector").click(
        function (event) {
          event.preventDefault();
        });
  });

    // init reload
  sheets.forEach(function (element): void {
    var $sheet = jQuery(element);
    $sheet.filter("[data-tobago-reload]").each(function () {
      var $sheet = jQuery(this);
      Tobago4.Sheets.get($sheet.attr("id")).initReload();
    });
  });

  // init paging by pages
  sheets.forEach(function (element): void {
    var $sheet = jQuery(element);
    $sheet.find(".tobago-sheet-pagingText").each(function () {
      var pagingText = jQuery(this);
      pagingText.click(function () {
        var text = jQuery(this);
        text.children(".tobago-sheet-pagingOutput").hide();
        text.children(".tobago-sheet-pagingInput").show().focus().select();
      });
      pagingText.children(".tobago-sheet-pagingInput")
          .blur(function () {
            Tobago4.Sheet.hideInputOrSubmit(jQuery(this));
          }).keydown(function (event) {
        if (event.keyCode === 13) {
          event.stopPropagation();
          event.preventDefault();
          jQuery(this).blur();
        }
      });
    });
  });
};

Tobago4.Sheet.hideInputOrSubmit = function($input) {
  var $output = $input.siblings(".tobago-sheet-pagingOutput");
  var changed = $output.html() !== $input.val();
  var sheetId = $input.parents(".tobago-sheet:first").attr("id");
  $output.html($input.val());
  if (changed) {
    Tobago4.Sheets.get(sheetId).reloadWithAction($input.attr("id"));
  } else {
    console.info("no update needed");
    $input.hide();
    $output.show();
  }
};

Tobago4.Sheet.findHiddenSelected = function($sheet){
  var id = $sheet.attr("id") + Tobago4.SUB_COMPONENT_SEP + "selected";
  return jQuery(Tobago4.Utils.escapeClientId(id));
};

Tobago4.Sheet.findHiddenScrollPosition = function($sheet){
  var id = $sheet.attr("id") + Tobago4.SUB_COMPONENT_SEP + "scrollPosition";
  return jQuery(Tobago4.Utils.escapeClientId(id));
};

Tobago4.Sheet.findHiddenWidths = function($sheet){
  var id = $sheet.attr("id") + Tobago4.SUB_COMPONENT_SEP + "widths";
  return jQuery(Tobago4.Utils.escapeClientId(id));
};

Tobago4.Sheet.findHiddenRendered = function($sheet){
  var id = $sheet.attr("id") + Tobago4.SUB_COMPONENT_SEP + "rendered";
  return jQuery(Tobago4.Utils.escapeClientId(id));
};

Tobago4.Sheet.addHeaderFillerWidth = function($sheet) {
  var $headerTable = $sheet.find(".tobago-sheet-headerTable");
  var $headerCols = $headerTable.find("col");
  $headerCols.last().attr("width", Tobago4.Sheet.getScrollBarSize());
};

Tobago4.Sheet.getScrollBarSize = function() {
  var $outer = $('<div>').css({visibility: 'hidden', width: 100, overflow: 'scroll'}).appendTo('body'),
      widthWithScroll = $('<div>').css({width: '100%'}).appendTo($outer).outerWidth();
  $outer.remove();
  return 100 - widthWithScroll;
};

Tobago4.Sheet.prototype.setup = function() {
  console.time("[tobago-sheet] setup");
  this.initReload();
  console.timeEnd("[tobago-sheet] setup");
};

Tobago4.Sheet.prototype.initReload = function() {
  var $sheet = jQuery(Tobago4.Utils.escapeClientId(this.id));
  var reload = $sheet.data("tobago-reload");
  if (typeof reload === "number") {
    Tobago4.addReloadTimeout(this.id, Tobago4.Sheet.reloadWithAction, reload);
  }
};

Tobago4.Sheet.prototype.doDblClick = function(event) {
    var target = event.target;
    if (!Tobago4.Sheet.isInputElement(jQuery(target))) {
      var row = jQuery(target).closest("tr");
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
          jsf.ajax.request(
              action,
              event,
              {
                //"javax.faces.behavior.event": "dblclick",
                execute: this.dblClickReloadComponentId,
                render: this.dblClickReloadComponentId
              });
        } else {
          Tobago4.submitAction(target, action);
        }
      }
    }
  };

/**
 * Get the element, which indicates the selection
 * @param row as a jQuery object
 */
Tobago4.Sheet.getSelectorCheckbox = function(row) {
  return row.find(">td>input.tobago-sheet-columnSelector");
};

Tobago4.Sheet.prototype.getSiblingRow = function(row, i) {
  return row.parentNode.childNodes[i];
};

Tobago4.Sheet.getRows = function($sheet) {
  return $sheet.find(">div>table>tbody>tr");
};

Tobago4.Sheet.isRowSelected = function($sheet, row) {
  var rowIndex = +row.data("tobago-row-index");
  if (!rowIndex) {
    rowIndex = row.index() + $sheet.data("tobago-first");
  }
  return Tobago4.Sheet.isSelected($sheet, rowIndex);
};

Tobago4.Sheet.isSelected = function($sheet, rowIndex) {
  return Tobago4.Sheet.findHiddenSelected($sheet).val().indexOf("," + rowIndex + ",") >= 0;
};

Tobago4.Sheet.resetSelected = function($sheet) {
  Tobago4.Sheet.findHiddenSelected($sheet).val(",");
};

Tobago4.Sheet.toggleSelection = function($sheet, $row, $checkbox) {
  $sheet.data("tobago-last-clicked-row-index", $row.index());
  if (!$checkbox.is(":disabled")) {
    var $selected = Tobago4.Sheet.findHiddenSelected($sheet);
    var rowIndex = Tobago4.Sheet.getDataIndex($sheet, $row);
    if (Tobago4.Sheet.isSelected($sheet, rowIndex)) {
      Tobago4.Sheet.deselectRow($selected, rowIndex, $row, $checkbox);
    } else {
      Tobago4.Sheet.selectRow($selected, rowIndex, $row, $checkbox);
    }
  }
};

Tobago4.Sheet.selectAll = function($sheet) {
  var $rows = Tobago4.Sheet.getRows($sheet);
  Tobago4.Sheet.selectRange($sheet, $rows, 0, $rows.length - 1, true, false);
};

Tobago4.Sheet.deselectAll = function($sheet) {
  var $rows = Tobago4.Sheet.getRows($sheet);
  Tobago4.Sheet.selectRange($sheet, $rows, 0, $rows.length - 1, false, true);
};

Tobago4.Sheet.toggleAll = function(sheet) {
  var rows = Tobago4.Sheet.getRows(sheet);
  Tobago4.Sheet.selectRange(sheet, rows, 0, rows.length - 1, true, true);
};

Tobago4.Sheet.selectRange = function($sheet, $rows, first, last, selectDeselected, deselectSelected) {
  if ($rows.length === 0) {
    return;
  }
  var selected = Tobago4.Sheet.findHiddenSelected($sheet);
  for (var i = first; i <= last; i++) {
    var $row = $rows.eq(i);
    var checkbox = Tobago4.Sheet.getSelectorCheckbox($row);
    if (!checkbox.is(":disabled")) {
      var rowIndex = Tobago4.Sheet.getDataIndex($sheet, $row);
      var on = selected.val().indexOf("," + rowIndex + ",") >= 0;
      if (selectDeselected && !on) {
        Tobago4.Sheet.selectRow(selected, rowIndex, $row, checkbox);
      } else if (deselectSelected && on) {
        Tobago4.Sheet.deselectRow(selected, rowIndex, $row, checkbox);
      }
    }
  }
};

Tobago4.Sheet.getDataIndex = function($sheet, $row) {
  var rowIndex = $row.data("tobago-row-index");
  if (rowIndex) {
    return +rowIndex;
  } else {
    return $row.index() + $sheet.data("tobago-first");
  }
};

/**
 * @param $selected input-element type=hidden: Hidden field with the selection state information
 * @param rowIndex int: zero based index of the row.
 * @param $row tr-element: the row.
 * @param $checkbox input-element: selector in the row.
 */
Tobago4.Sheet.selectRow = function($selected, rowIndex, $row, $checkbox) {
  $selected.val($selected.val() + rowIndex + ",");
  $row.addClass("tobago-sheet-row-markup-selected table-info");
//  checkbox.prop("checked", true);
  setTimeout(function() {$checkbox.prop("checked", true);}, 0);
};

/**
 * @param $selected input-element type=hidden: Hidden field with the selection state information
 * @param rowIndex int: zero based index of the row.
 * @param $row tr-element: the row.
 * @param $checkbox input-element: selector in the row.
 */
Tobago4.Sheet.deselectRow = function($selected, rowIndex, $row, $checkbox) {
  $selected.val($selected.val().replace(new RegExp("," + rowIndex + ","), ","));
  $row.removeClass("tobago-sheet-row-markup-selected table-info");
//  checkbox.prop("checked", false); Async because of TOBAGO-1312
  setTimeout(function() {$checkbox.prop("checked", false);}, 0);
};

Tobago4.Sheet.isInputElement = function($element) {
  return ["INPUT", "TEXTAREA", "SELECT", "A", "BUTTON"].indexOf($element.prop("tagName")) > -1;
};
