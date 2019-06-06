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

import {Listener, Phase} from "./tobago-listener";
import {DomUtils} from "./tobago-utils";
import {Tobago4} from "./tobago-core";

class Sheet {

  static readonly SHEETS: Map<string, Sheet> = new Map<string, Sheet>();

  id: string;

  clickActionId: string;
  clickReloadComponentId: string;
  dblClickActionId: string;
  dblClickReloadComponentId: string;

  loadColumnWidths(): number[] {
    const hidden = document.getElementById(this.id + Tobago4.SUB_COMPONENT_SEP + "widths");
    if (hidden) {
      return JSON.parse(hidden.getAttribute("value"));
    } else {
      return undefined;
    }
  }

  saveColumnWidths(widths: number[]) {
    const hidden = document.getElementById(this.id + Tobago4.SUB_COMPONENT_SEP + "widths");
    if (hidden) {
      hidden.setAttribute("value", JSON.stringify(widths));
    } else {
      console.warn("ignored, should not be called, id='" + this.id + "'");
    }
  }

  getElement(): HTMLElement {
    return document.getElementById(this.id);
  }

  isColumnRendered():boolean[] {
    const hidden = document.getElementById(this.id + Tobago4.SUB_COMPONENT_SEP + "rendered");
    return JSON.parse(hidden.getAttribute("value"));
  };

  addHeaderFillerWidth() {
    document.getElementById(this.id).querySelector(".tobago-sheet-headerTable col:last-child")
        .setAttribute("width", String(Sheet.getScrollBarSize()));
  };

  constructor(element: HTMLElement) {
    this.id = element.id;
    const commands = element.dataset["tobagoRowAction"];
    const behavior = element.dataset["tobagoBehaviorCommands"];
    this.clickActionId = null;//todo commands.click.action;
    this.clickReloadComponentId = null;//todo commands.click.partially; // fixme: partially no longer used?
    this.dblClickActionId = null;//todo commands.dblclick.action;
    this.dblClickReloadComponentId = null;//todo commands.dblclick.partially;// fixme: partially no longer used?

    // synchronize column widths ----------------------------------------------------------------------------------- //

    // basic idea: there are two possible sources for the sizes:
    // 1. the columns attribute of <tc:sheet> like {"columns":[1.0,1.0,1.0]}, held by data attribute "tobago-layout"
    // 2. the hidden field which may contain a value like ",300,200,100,"
    //
    // The 1st source usually is the default set by the developer.
    // The 2nd source usually is the value set by the user manipulating the column widths.
    //
    // So, if the 2nd is set, we use it, if not set, we use the 1st source.

    let columnWidths = this.loadColumnWidths();
    console.info("columnWidths: " + columnWidths);
    if (columnWidths && columnWidths.length === 0) { // active, but empty
      // otherwise use the layout definition
      const tokens = JSON.parse(element.dataset["tobagoLayout"]).columns;
      const columnRendered = this.isColumnRendered();

      const headerCols = element.querySelectorAll(".tobago-sheet>header>table>colgroup>col");
      const bodyTable = <HTMLElement>element.querySelector(".tobago-sheet>div>table");
      const bodyCols = element.querySelectorAll(".tobago-sheet>div>table>colgroup>col");

      console.assert(headerCols.length - 1 === bodyCols.length, "header and body column number doesn't match");

      let sumRelative = 0; // tbd: is this needed?
      let widthRelative = bodyTable.offsetWidth;
      for (let i = 0; i < tokens.length; i++) {
        if (columnRendered[i]) {
          if (typeof tokens[i] === "number") {
            sumRelative += tokens[i];
          } else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
            const intValue = parseInt(tokens[i].measure);
            if (tokens[i].measure.lastIndexOf("px") > 0) {
              widthRelative -= intValue;
            } else if (tokens[i].measure.lastIndexOf("%") > 0) {
              widthRelative -= bodyTable.offsetWidth * intValue / 100;
            }
          } else {
            console.debug("auto? = " + tokens[i]);
          }
        }
      }
      if (widthRelative < 0) {
        widthRelative = 0;
      }

      let headerBodyColCount = 0;
      for (let i = 0; i < tokens.length; i++) {
        let colWidth = 0;
        if (columnRendered[i]) {
          if (typeof tokens[i] === "number") {
            colWidth = tokens[i] * widthRelative / sumRelative;
          } else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
            const intValue = parseInt(tokens[i].measure);
            if (tokens[i].measure.lastIndexOf("px") > 0) {
              colWidth = intValue;
            } else if (tokens[i].measure.lastIndexOf("%") > 0) {
              colWidth = bodyTable.offsetWidth * intValue / 100;
            }
          } else {
            console.debug("auto? = " + tokens[i]);
          }
          if (colWidth > 0) { // because tokens[i] == "auto"
            headerCols.item(headerBodyColCount).setAttribute("width", String(colWidth));
            bodyCols.item(headerBodyColCount).setAttribute("width", String(colWidth));
          }
          headerBodyColCount++;
        }
      }
    }
    this.addHeaderFillerWidth();

    // resize column: mouse events -------------------------------------------------------------------------------- //

    element.querySelectorAll(".tobago-sheet-headerResize").forEach(function (resizeElement): void {
      const $resizeElement = jQuery(resizeElement);
      $resizeElement.on("click", function () {
        return false;
      });
      $resizeElement.on("mousedown", function (event) {

        DomUtils.page().dataset["SheetMousedownData"] = element.id;

        // begin resizing
        console.info("down");
        var columnIndex = $resizeElement.data("tobago-column-index");
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

        jQuery(document).on("mousemove", data, function (event) {
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
        jQuery(document).on("mouseup", function (event) {
          // switch off the mouse move listener
          jQuery(document).off("mousemove");
          console.info("up");
          const sheet = Sheet.SHEETS.get(DomUtils.page().dataset["SheetMousedownData"]);
          // copy the width values from the header to the body, (and build a list of it)
          var tokens = $sheet.data("tobago-layout").columns;
          const columnRendered = sheet.isColumnRendered();
          var columnWidths = sheet.loadColumnWidths();

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
          for (i = 0; i < columnRendered.length; i++) {
            if (columnRendered[i]) {
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
            } else if (columnWidths !== undefined && columnWidths.length >= i) {
              widths[i] = columnWidths[i];
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
          sheet.saveColumnWidths(widths);
          return false;
        });
      });
    });
  }

  static init = function (element: HTMLElement) {
    console.time("[tobago-sheet] init");
    for (const sheetElement of DomUtils.selfOrElementsByClassName(element, "tobago-sheet")) {
      const sheet = new Sheet(sheetElement);
      Sheet.SHEETS.set(sheet.id, sheet);
    }

    const sheets: Array<HTMLElement> = DomUtils.selfOrElementsByClassName(element, "tobago-sheet");
    Sheet.setup(sheets);

    element.querySelectorAll(".tobago-sheet-header .tobago-sheet-columnSelector").forEach(function (element) {
      element.addEventListener("click", function (event: MouseEvent) {
        var $checkbox = jQuery(event.target);
        if ($checkbox.is(':checked')) {
          Sheet.selectAll($checkbox.closest(".tobago-sheet"));
        } else {
          Sheet.deselectAll($checkbox.closest(".tobago-sheet"));
        }
      });
    });

    console.timeEnd("[tobago-sheet] init");
  };

  static setup = function (sheets) {

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
        var hidden = Sheet.findHiddenScrollPosition(sheetBody.parent());
        hidden.val(Math.round(scrollLeft) + ";" + Math.round(scrollTop));
      });
    });

    // restore scroll position
    sheets.forEach(function (element): void {
      var $sheet = jQuery(element);
      var $hidden = Sheet.findHiddenScrollPosition($sheet);
      const value = $hidden.get(0).getAttribute("value");
      var sep = value.indexOf(";");
      if (sep !== -1) {
        var scrollLeft = value.substr(0, sep);
        var scrollTop = value.substr(sep + 1);
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
        Sheet.getRows($sheet).each(function () {
          var $row = jQuery(this);
          $row.on("mousedown", function (event) {
            $sheet.data("tobago-mouse-down-x", event.clientX);
            $sheet.data("tobago-mouse-down-y", event.clientY);
          });
          $row.click(function (event) {
            var $target = jQuery(event.target);
            var $row = jQuery(this);
            if ($target.hasClass("tobago-sheet-columnSelector") || !Sheet.isInputElement($target)) {
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

              var $rows = Sheet.getRows($sheet);
              var $selector = Sheet.getSelectorCheckbox($row);

              var selectionMode = $sheet.data("tobago-selection-mode");

              if ((!event.ctrlKey && !event.metaKey && $selector.length === 0)
                  || selectionMode === "single" || selectionMode === "singleOrNone") {
                Sheet.deselectAll($sheet);
                Sheet.resetSelected($sheet);
              }

              var lastClickedRowIndex = $sheet.data("tobago-last-clicked-row-index");
              if (event.shiftKey && selectionMode === "multi" && lastClickedRowIndex > -1) {
                if (lastClickedRowIndex <= $row.index()) {
                  Sheet.selectRange($sheet, $rows, lastClickedRowIndex, $row.index(), true, false);
                } else {
                  Sheet.selectRange($sheet, $rows, $row.index(), lastClickedRowIndex, true, false);
                }
              } else if (selectionMode !== "singleOrNone" || !Sheet.isRowSelected($sheet, $row)) {
                Sheet.toggleSelection($sheet, $row, $selector);
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
                var rowIndex = Sheet.getDataIndex($sheet, $row);
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
          // todo: check if this works correctly
          const sheet = Sheet.SHEETS.get($sheet.attr("id"));
          if (sheet && sheet.dblClickActionId) {
            $row.on("dblclick", function (event) {
              // todo: re-implement
              sheet.doDblClick(event);
            });
          }
        });
      }
      $sheet.find(".tobago-sheet-cell > input.tobago-sheet-columnSelector").click(
          function (event) {
            event.preventDefault();
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
              Sheet.hideInputOrSubmit(jQuery(this));
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

  static hideInputOrSubmit = function ($input) {
    let $output = $input.siblings(".tobago-sheet-pagingOutput");
    let changed = $output.html() !== $input.val();
    let sheetId = $input.parents(".tobago-sheet:first").attr("id");
    $output.html($input.val());
    if (changed) {
      console.debug("reloading sheet '" + $input.attr("id") + "' '" + sheetId + "'");
      jsf.ajax.request(
          $input.attr("id"),
          null,
          {
            "javax.faces.behavior.event": "reload",
            execute: sheetId,
            render: sheetId
          });
    } else {
      console.info("no update needed");
      $input.hide();
      $output.show();
    }
  };

  static findHiddenSelected = function ($sheet) {
    var id = $sheet.attr("id") + Tobago4.SUB_COMPONENT_SEP + "selected";
    return jQuery(DomUtils.escapeClientId(id));
  };

  static findHiddenScrollPosition = function ($sheet) {
    var id = $sheet.attr("id") + Tobago4.SUB_COMPONENT_SEP + "scrollPosition";
    return jQuery(DomUtils.escapeClientId(id));
  };

  static getScrollBarSize = function ():number {
    var $outer = $('<div>').css({visibility: 'hidden', width: 100, overflow: 'scroll'}).appendTo('body'),
        widthWithScroll = $('<div>').css({width: '100%'}).appendTo($outer).outerWidth();
    $outer.remove();
    return 100 - widthWithScroll;
  };

  doDblClick(event) {
    var target = event.target;
    if (!Sheet.isInputElement(jQuery(target))) {
      var row = jQuery(target).closest("tr");
      var $sheet = row.closest(".tobago-sheet");
      var rowIndex = row.index() + $sheet.data("tobago-first");
      if (this.dblClickActionId) {
        var action;
        var index = this.dblClickActionId.indexOf(this.id);
        if (index >= 0) {
          action = this.id + ":" + rowIndex + ":" + this.dblClickActionId.substring(index + this.id.length + 1);
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
  static getSelectorCheckbox = function (row) {
    return row.find(">td>input.tobago-sheet-columnSelector");
  };

  static getSiblingRow = function (row, i) {
    return row.parentNode.childNodes[i];
  };

  static getRows = function ($sheet) {
    return $sheet.find(">div>table>tbody>tr");
  };

  static isRowSelected = function ($sheet, row) {
    var rowIndex = +row.data("tobago-row-index");
    if (!rowIndex) {
      rowIndex = row.index() + $sheet.data("tobago-first");
    }
    return Sheet.isSelected($sheet, rowIndex);
  };

  static isSelected = function ($sheet, rowIndex) {
    return Sheet.findHiddenSelected($sheet).get(0).getAttribute("value").indexOf("," + rowIndex + ",") >= 0;
  };

  static resetSelected = function ($sheet) {
    Sheet.findHiddenSelected($sheet).val(",");
  };

  static toggleSelection = function ($sheet, $row, $checkbox) {
    $sheet.data("tobago-last-clicked-row-index", $row.index());
    if (!$checkbox.is(":disabled")) {
      var $selected = Sheet.findHiddenSelected($sheet);
      var rowIndex = Sheet.getDataIndex($sheet, $row);
      if (Sheet.isSelected($sheet, rowIndex)) {
        Sheet.deselectRow($selected, rowIndex, $row, $checkbox);
      } else {
        Sheet.selectRow($selected, rowIndex, $row, $checkbox);
      }
    }
  };

  static selectAll = function ($sheet) {
    var $rows = Sheet.getRows($sheet);
    Sheet.selectRange($sheet, $rows, 0, $rows.length - 1, true, false);
  };

  static deselectAll = function ($sheet) {
    var $rows = Sheet.getRows($sheet);
    Sheet.selectRange($sheet, $rows, 0, $rows.length - 1, false, true);
  };

  static toggleAll = function (sheet) {
    var rows = Sheet.getRows(sheet);
    Sheet.selectRange(sheet, rows, 0, rows.length - 1, true, true);
  };

  static selectRange = function ($sheet, $rows, first, last, selectDeselected, deselectSelected) {
    if ($rows.length === 0) {
      return;
    }
    var selected = Sheet.findHiddenSelected($sheet);
    for (var i = first; i <= last; i++) {
      var $row = $rows.eq(i);
      var checkbox = Sheet.getSelectorCheckbox($row);
      if (!checkbox.is(":disabled")) {
        var rowIndex = Sheet.getDataIndex($sheet, $row);
        var on = selected.get(0).getAttribute("value").indexOf("," + rowIndex + ",") >= 0;
        if (selectDeselected && !on) {
          Sheet.selectRow(selected, rowIndex, $row, checkbox);
        } else if (deselectSelected && on) {
          Sheet.deselectRow(selected, rowIndex, $row, checkbox);
        }
      }
    }
  };

  static getDataIndex = function ($sheet, $row) {
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
  static selectRow = function ($selected, rowIndex, $row, $checkbox) {
    $selected.val($selected.val() + rowIndex + ",");
    $row.addClass("tobago-sheet-row-markup-selected table-info");
//  checkbox.prop("checked", true);
    setTimeout(function () {
      $checkbox.prop("checked", true);
    }, 0);
  };

  /**
   * @param $selected input-element type=hidden: Hidden field with the selection state information
   * @param rowIndex int: zero based index of the row.
   * @param $row tr-element: the row.
   * @param $checkbox input-element: selector in the row.
   */
  static deselectRow = function ($selected, rowIndex, $row, $checkbox) {
    $selected.val($selected.val().replace(new RegExp("," + rowIndex + ","), ","));
    $row.removeClass("tobago-sheet-row-markup-selected table-info");
//  checkbox.prop("checked", false); Async because of TOBAGO-1312
    setTimeout(function () {
      $checkbox.prop("checked", false);
    }, 0);
  };

  static isInputElement = function ($element) {
    return ["INPUT", "TEXTAREA", "SELECT", "A", "BUTTON"].indexOf($element.prop("tagName")) > -1;
  };

}

Listener.register(Sheet.init, Phase.DOCUMENT_READY);
Listener.register(Sheet.init, Phase.AFTER_UPDATE);
