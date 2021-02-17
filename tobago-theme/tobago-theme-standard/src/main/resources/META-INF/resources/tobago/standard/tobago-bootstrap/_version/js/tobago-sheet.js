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
    clickActionId, clickReloadComponentId, dblClickActionId, dblClickReloadComponentId, behaviorCommands) {
  console.debug("New Sheet with id " + sheetId); // @DEV_ONLY
  console.time("[tobago-sheet] constructor"); // @DEV_ONLY
  this.id = sheetId;
  Tobago.Sheets.put(this);
  this.clickActionId = clickActionId;
  this.clickReloadComponentId = clickReloadComponentId;
  this.dblClickActionId = dblClickActionId;
  this.dblClickReloadComponentId = dblClickReloadComponentId;
  this.behaviorCommands = behaviorCommands;

  this.setup();

  console.timeEnd("[tobago-sheet] constructor"); // @DEV_ONLY
};

Tobago.Sheet.init = function(elements) {
  console.time("[tobago-sheet] init"); // @DEV_ONLY
  var sheets = Tobago.Utils.selectWithJQuery(elements, ".tobago-sheet");
  sheets.each(function initSheets() {
    var sheet = jQuery(this);
    var id = sheet.attr("id");
    if (sheet.data("tobago-lazy-initialized")) {
      return;
    }
    var commands = sheet.data("tobago-row-action");
    var click = commands ? commands.click : undefined;
    var dblclick = commands ? commands.dblclick : undefined;
    new Tobago.Sheet(id, undefined, undefined, undefined, undefined,
        click !== undefined ? click.action  : undefined,
        click !== undefined ? click.partially : undefined, // fixme: partially no longer used
        dblclick !== undefined ? dblclick.action : undefined,
        dblclick !== undefined ? dblclick.partially: undefined, // fixme: partially no longer used
        sheet.data("tobago-behavior-commands")); // type array

    //////////////////////////////////////////////
    // XXX bugfix for IE11 (lower than IE11 isn't supported for that feature)
    // if a max-height is set on the sheet,
    if (Tobago.browser.isMsie && sheet.css("max-height") !== "none") {
      sheet.css("height", sheet.css("height")); // reset the height to the same value
    }
  });

  Tobago.Sheet.setup2(sheets);

  Tobago.Utils.selectWithJQuery(elements, ".tobago-sheet-header .tobago-sheet-columnSelector").click(function(event) {
    var $checkbox = jQuery(event.target);
    if ($checkbox.is(':checked')) {
      Tobago.Sheet.selectAll($checkbox.closest(".tobago-sheet"));
    } else {
      Tobago.Sheet.deselectAll($checkbox.closest(".tobago-sheet"));
    }
  });

  console.timeEnd("[tobago-sheet] init"); // @DEV_ONLY
};

Tobago.registerListener(Tobago.Sheet.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Sheet.init, Tobago.Phase.AFTER_UPDATE);

Tobago.Sheet.prototype.reloadWithAction = function(source, action) {
    console.debug("reload sheet with action '" + action + "'"); // @DEV_ONLY
  var executeIds = this.id;
  var renderIds = this.id;
  var lazy = jQuery(Tobago.Utils.escapeClientId(this.id)).data("tobago-lazy");

  jsf.ajax.request(
      action,
      null,
      {
        "javax.faces.behavior.event": "reload",
        execute: executeIds,
        render: renderIds,
        onevent: lazy ? Tobago.Sheet.lazyResponse : undefined,
        onerror: lazy ? Tobago.Sheet.lazyError: undefined
      });
};

Tobago.Sheet.lazyResponse = function(event) {
  if (event.status === "complete") {
    var updates = event.responseXML.querySelectorAll("update");
    for (var i = 0; i < updates.length; i++) {
      var update = updates[i];
      var id = update.getAttribute("id");
      if (id.indexOf(":") > -1) { // is a JSF element id, but not a technical id from the framework
        console.debug("[tobago-sheet][complete] Update after jsf.ajax complete: #" + id); // @DEV_ONLY

        var sheet = document.getElementById(id);
        sheet.id = id + "::lazy-temporary";

        var page = Tobago.findPage();
        page.get(0).insertAdjacentHTML("beforeend", "<div id='" + id + "'></div>");
        var sheetLoader = document.getElementById(id);
      }
    }
  } else if (event.status === "success") {
    updates = event.responseXML.querySelectorAll("update");
    for (i = 0; i < updates.length; i++) {
      update = updates[i];
      id = update.getAttribute("id");
      if (id.indexOf(":") > -1) { // is a JSF element id, but not a technical id from the framework
        console.debug("[tobago-sheet][success] Update after jsf.ajax complete: #" + id); // @DEV_ONLY

        // sync the new rows into the sheet
        sheetLoader = document.getElementById(id);
        sheet = document.getElementById(id + "::lazy-temporary");
        sheet.id = id;
        var tbody = sheet.querySelector(".tobago-sheet-bodyTable > tbody");

        var newRows = sheetLoader.querySelectorAll(".tobago-sheet-bodyTable > tbody > tr");
        for (i = 0; i < newRows.length; i++) {
          var newRow = newRows[i];
          var rowIndex = Number(newRow.dataset.tobagoRowIndex);
          var row = tbody.querySelector("tr[data-tobago-row-index='" + rowIndex + "']");
          // replace the old row with the new row
          row.insertAdjacentElement("afterend", newRow);
          tbody.removeChild(row);
        }

        sheetLoader.parentElement.removeChild(sheetLoader);
        jQuery(sheet).data("tobago-lazy-active", false);
      }
    }
  }

};

Tobago.Sheet.lazyError = function(data) {
  updates = event.responseXML.querySelectorAll("update");
  for (i = 0; i < updates.length; i++) {
    update = updates[i];
    id = update.getAttribute("id");
    if (id.indexOf(":") > -1) { // is a JSF element id, but not a technical id from the framework
      var sheet = document.getElementById(id);
      jQuery(sheet).data("tobago-lazy-active", false);
      console.error("Sheet lazy loading error:"
          + "\nError Description: " + data.description
          + "\nError Name: " + data.errorName
          + "\nError errorMessage: " + data.errorMessage
          + "\nResponse Code: " + data.responseCode
          + "\nResponse Text: " + data.responseText
          + "\nStatus: " + data.status
          + "\nType: " + data.type);
    }
  }
};

/*
Tobago.Sheet.logCol = function () {

  var headerTable = jQuery(".tobago-sheet-headerTable");
  var bodyTable = jQuery(".tobago-sheet-bodyTable");
  var headerColumns = headerTable.children("colgroup").children("col");
  var bodyColumns = bodyTable.children("colgroup").children("col");

  var r = "*** header col widths: " + headerTable.width() + " = [";

  var s = 0;
  for (const headerColumn of headerColumns) {
    r += " " + headerColumn.width;
    s += parseInt(headerColumn.width);
  }
  r += " ] sum=" + s;
  r += " body col widths: " + bodyTable.width() + " = [";

  s = 0;
  for (const bodyColumn of bodyColumns) {
    r += " " + bodyColumn.width;
    s += parseInt(bodyColumn.width);
  }
  r += " ] sum=" + s;

  console.warn(r);
}
*/

Tobago.Sheet.setup2 = function (sheets) {

  // synchronize column widths
  jQuery(sheets).each(function() {

    // basic idea: there are two possible sources for the sizes:
    // 1. the columns attribute of <tc:sheet> like {"columns":[1.0,1.0,1.0]}, held by data attribute "tobago-layout"
    // 2. the hidden field which may contain a value like ",300,200,100,"
    //
    // The 1st source usually is the default set by the developer.
    // The 2nd source usually is the value set by the user manipulating the column widths.
    //
    // So, if the 2nd is set, we use it, if not set, we use the 1st source.
    //

    var $sheet = jQuery(this);

    var hidden = Tobago.Sheet.findHiddenWidths($sheet);

    if (hidden.length > 0 && hidden.val()) {
      // if the hidden has a value, than also the colgroup/col are set correctly
      var columnWidths = jQuery.parseJSON(hidden.val());
      console.info("columnWidths: " + columnWidths); // @DEV_ONLY
    }

    // Tobago.Sheet.logCol()

    Tobago.Sheet.initHeaderFillerWidths($sheet, columnWidths);

    // Tobago.Sheet.logCol()

    var $headerTable = $sheet.children("header").children("table");
    var $bodyTable = $sheet.children("div").children("table");

    if (columnWidths !== undefined && columnWidths.length === 0) {
      // otherwise use the layout definition
      var layout = $sheet.data("tobago-layout");
      if (layout && layout.columns && layout.columns.length > 0) {
        var tokens = layout.columns;
        var rendered = jQuery.parseJSON(Tobago.Sheet.findHiddenRendered($sheet).val());

        var $headerCol = $headerTable.children("colgroup").children("col");
        var $bodyCol = $bodyTable.children("colgroup").children("col");

        console.assert($headerCol.length - 1 === $bodyCol.length, "header and body column number doesn't match");  // @DEV_ONLY

        console.debug("phase 1"); // @DEV_ONLY
        // phase one: collect data
        var i;
        var intValue;
        var sumRelative = 0;
        var widthRelative = $bodyTable.width();
        var r = 0;
        for (i = 0; i < tokens.length; i++) {
          if (rendered[i] === "true") {
            if (typeof tokens[i] === "number") {
              sumRelative += tokens[i];
            } else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
              intValue = parseInt(tokens[i].measure);
              if (tokens[i].measure.lastIndexOf("px") > 0) {
                widthRelative -= intValue;
              } else if (tokens[i].measure.lastIndexOf("%") > 0) {
                widthRelative -= parseInt($bodyTable.width() / 100 * intValue);
              }
            } else if(tokens[i] === "auto") {
              var value = $headerTable.children("tbody").children("tr").children("th").eq(r).outerWidth(true);
              widthRelative -= value;
              // console.log("auto -> " + value);
              tokens[i] = {measure: value + "px"}; // converting "auto" to a specific value
            } else {
              console.debug("(layout columns a) auto? token[i]='%s' i=%i", tokens[i], i); // @DEV_ONLY
            }
            r++;
          }
        }
        var scrollCol = $headerCol.last().width();
        if (scrollCol) {
          widthRelative -= scrollCol;
        }
        if (widthRelative < 0) {
          widthRelative = 0;
        }
        // Tobago.Sheet.logCol()

        console.info("phase 2"); // @DEV_ONLY
        // phase 2: set widths
        r = 0;
        for (i = 0; i < tokens.length; i++) {
          var colWidth = 0;
          if (rendered[i] === "true") {
            if (typeof tokens[i] === "number") {
              colWidth = parseInt((tokens[i] * widthRelative) / sumRelative);
            } else if (typeof tokens[i] === "object" && tokens[i].measure !== undefined) {
              intValue = parseInt(tokens[i].measure);
              if (tokens[i].measure.lastIndexOf("px") > 0) {
                colWidth = intValue;
              } else if (tokens[i].measure.lastIndexOf("%") > 0) {
                colWidth = parseInt($bodyTable.width() / 100 * intValue);
              }
            } else {
              console.debug("(layout columns b) auto? token[i]='%s' i=%i", tokens[i], i); // @DEV_ONLY
            }
            if (colWidth > 0) { // because tokens[i] == "auto"
              $headerCol.eq(r).attr("width", colWidth);
              $bodyCol.eq(r).attr("width", colWidth);
            }
            r++;
          }
        }
      }
      // Tobago.Sheet.logCol()
    }

    // release the overlast col width = 0
    $headerTable.find("col").eq(-2).removeAttr("width");
    $bodyTable.find("col").eq(-2).removeAttr("width");
  });

  // resize: mouse events
  jQuery(sheets).find(".tobago-sheet-headerResize").each(function () {
    jQuery(this).click(function () {
      return false;
    });
    jQuery(this).mousedown(function (event) {
      // begin resizing
      console.info("down"); // @DEV_ONLY
      // Tobago.Sheet.logCol()
      var columnIndex = jQuery(this).data("tobago-column-index");
      var body = jQuery("body");
      var sheet = jQuery(this).closest(".tobago-sheet");
      var headerTable = sheet.find(".tobago-sheet-headerTable");
      var bodyTable = sheet.find(".tobago-sheet-bodyTable");
      var headerColumn = headerTable.children("colgroup").children("col").eq(columnIndex);
      var bodyColumn = bodyTable.children("colgroup").children("col").eq(columnIndex);
      var data = {
        columnIndex: columnIndex,
        originalClientX: event.clientX,
        headerColumn: headerColumn,
        bodyColumn: bodyColumn,
        originalHeaderColumnWidth: parseInt(headerColumn.attr("width"))
      };

      // Set width attribute: Avoid scrollBar position flip to 0.
      headerTable.css("width", headerTable.outerWidth());
      bodyTable.css("width", bodyTable.outerWidth());

      body.on("mousemove", data, function(event) {
        console.info("move"); // @DEV_ONLY
        // Tobago.Sheet.logCol()
        var delta = event.clientX - event.data.originalClientX;
        // min column width is 40px
        delta = -Math.min(-delta, event.data.originalHeaderColumnWidth - 40);
        var columnWidth = event.data.originalHeaderColumnWidth + delta;
        event.data.headerColumn.attr("width", columnWidth);
        event.data.bodyColumn.attr("width", columnWidth);
        Tobago.clearSelection();
        // Tobago.Sheet.logCol()
        return false;
      });
      body.one("mouseup", function(event) {
        // switch off the mouse move listener
        jQuery("body").off("mousemove");
        console.info("up"); // @DEV_ONLY
        // Tobago.Sheet.logCol()
        // copy the width values from the header to the body, (and build a list of it)
        var tokens = sheet.data("tobago-layout").columns;
        var rendered = jQuery.parseJSON(Tobago.Sheet.findHiddenRendered(sheet).val());
        var hidden = Tobago.Sheet.findHiddenWidths(sheet);
        var hiddenWidths;
        if (hidden.length > 0 && hidden.val()) {
          hiddenWidths = jQuery.parseJSON(hidden.val());
        }
        var headerCols = headerTable.find("col");
        var bodyCols = bodyTable.find("col");
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
              var $tds = jQuery("td:nth-child(" + (headerBodyColCount + 1) + ")", bodyTable);
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
                widths[i] = parseInt(bodyTable.width() / 100 * intValue);
              }
            }
          }
        }
        // Remove width attribute: Avoid scrollBar position flip to 0.
        headerTable.css("width", "");
        bodyTable.css("width", "");

        // store the width values in a hidden field
        Tobago.Sheet.findHiddenWidths(sheet).val(JSON.stringify(widths));
        // Tobago.Sheet.logCol()
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
    sheetBody.siblings(".tobago-sheet-header").prop("scrollLeft", scrollLeft);

    // store the position in a hidden field
    var hidden = Tobago.Sheet.findHiddenScrollPosition(sheetBody.parent());
    hidden.val(Math.round(scrollLeft) + ";" + Math.round(scrollTop));
  });

  // restore scroll position
  jQuery(sheets).each(function () {
    var sheet = jQuery(this);
    var hidden = Tobago.Sheet.findHiddenScrollPosition(sheet);
    var sep = hidden.val().indexOf(";");
    if (sep !== -1) {
      var scrollLeft = hidden.val().substr(0, sep);
      var scrollTop = hidden.val().substr(sep + 1);
      var body = sheet.children(".tobago-sheet-body");
      body.prop("scrollLeft", scrollLeft);
      body.prop("scrollTop", scrollTop);
      sheet.children(".tobago-sheet-header").prop("scrollLeft", scrollLeft);
    }
  });

  // add selection listeners
  jQuery(sheets).each(function () {
    var sheet = jQuery(this);
    var selectionMode = sheet.data("tobago-selection-mode");
    if (selectionMode === "single" || selectionMode === "singleOrNone" || selectionMode === "multi") {
      Tobago.Sheet.getRows(sheet).each(function () {
        var row = jQuery(this);
        row.bind("mousedown", function (event) {
          sheet.data("tobago-mouse-down-x", event.clientX);
          sheet.data("tobago-mouse-down-y", event.clientY);
        });
        row.click(function (event) {
          var $target = jQuery(event.target);
          var $row = jQuery(this);
          if ($target.hasClass("tobago-sheet-columnSelector") || !Tobago.Sheet.isInputElement($target)) {
            var $sheet = $row.closest(".tobago-sheet");

            if (Math.abs($sheet.data("tobago-mouse-down-x") - event.clientX)
                + Math.abs($sheet.data("tobago-mouse-down-y") - event.clientY) > 5) {
              // The user has moved the mouse. We assume, the user want to select some text inside the sheet,
              // so we doesn't select the row.
              return;
            }

            Tobago.clearSelection();

            var $rows = Tobago.Sheet.getRows($sheet);
            var $selector = Tobago.Sheet.getSelectorCheckbox($row);

            var selectionMode = $sheet.data("tobago-selection-mode");

            if ((!event.ctrlKey && !event.metaKey && $selector.length === 0)
                || selectionMode === "single" || selectionMode === "singleOrNone") {
              Tobago.Sheet.deselectAll($sheet);
              Tobago.Sheet.resetSelected($sheet);
            }

            var lastClickedRowIndex = $sheet.data("tobago-last-clicked-row-index");
            if (event.shiftKey && selectionMode === "multi" && lastClickedRowIndex > -1) {
              if (lastClickedRowIndex <= $row.index()) {
                Tobago.Sheet.selectRange($sheet, $rows, lastClickedRowIndex, $row.index(), true, false);
              } else {
                Tobago.Sheet.selectRange($sheet, $rows, $row.index(), lastClickedRowIndex, true, false);
              }
            } else if (selectionMode !== "singleOrNone" || !Tobago.Sheet.isRowSelected($sheet, $row)) {
              Tobago.Sheet.toggleSelection($sheet, $row, $selector);
            }
            var commands = sheet.data("tobago-row-action");
            var click = commands ? commands.click : undefined;
            var clickActionId = click ? click.action : undefined;
            var clickExecuteIds = click ? click.execute : undefined;
            var clickRenderIds = click ? click.render : undefined;

            var id = $sheet.attr("id");

            if (clickActionId) {
              var action;
              var index = clickActionId.indexOf(id);
              var rowIndex = Tobago.Sheet.getDataIndex($sheet, $row);
              if (index >= 0) {
                action = id + ":" + rowIndex + ":" + clickActionId.substring(index + id.length +1);
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
                Tobago.submitAction($target.get(0), action);
              }
            }
          }
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
    sheet.find(".tobago-sheet-cell > input.tobago-sheet-columnSelector").click(function(event) {event.preventDefault()});
  });

  // lazy load by scrolling
  jQuery(sheets).each(function () {
    var $sheet = jQuery(this);
    var lazy = $sheet.data("tobago-lazy");

    function getTemplate(height, columns, rowIndex) {
      var tr = document.createElement("tr");
      tr.dataset.tobagoRowIndex = rowIndex;
      tr.classList.add("tobago-sheet-row");
      tr.setAttribute("dummy", "dummy");
      tr.style.height = height + "px";
      var td = document.createElement("td");
      td.classList.add("tobago-sheet-cell");
      td.colSpan = columns;
      tr.appendChild(td);
      return tr;
    }

    if (lazy) {
      // prepare the sheet with some auto-created (empty) rows
      var rows = $sheet.data("tobago-rows");
      var rowCount = $sheet.data("tobago-row-count");
      var $sheetBody = $sheet.find(".tobago-sheet-body");
      var $tbody = $sheetBody.find("tbody");
      var columns = $tbody.find("tr:first").find("td").length;
      var height = $tbody.height() / rows;
      var pointer = $tbody.get(0).rows[0]; // points current row
      for (var i = 0; i < rowCount; i++) {
        if (pointer) {
          var rowIndex = Number(pointer.dataset.tobagoRowIndex);
          if (i < rowIndex) {
            var template = getTemplate(height, columns, i);
            pointer.insertAdjacentElement("beforebegin", template);
          } else if (i === rowIndex) {
            pointer = pointer.nextElementSibling;
          } else {
            template = getTemplate(height, columns, i);
            pointer.insertAdjacentElement("afterend", template);
            pointer = template;
          }
        } else {
          template = getTemplate(height, columns, i);
          $tbody.get(0).insertAdjacentElement("beforeend", template);
          pointer = template;
        }
      }

      $sheetBody.bind("scroll", function () {
        Tobago.Sheet.lazyCheck($sheet);
      });

      // initial
      Tobago.Sheet.lazyCheck($sheet);

      $sheet.data("tobago-lazy-initialized", true);
    }
  });

  // init reload
  jQuery(sheets).filter("[data-tobago-reload]").each(function() {
    var sheet = jQuery(this);
    Tobago.Sheets.get(sheet.attr("id")).initReload();
  });

  // init paging by pages
  jQuery(sheets).find(".tobago-sheet-pagingText").each(function() {
    var pagingText = jQuery(this);
    pagingText.click(function () {
      var text = jQuery(this);
      text.children(".tobago-sheet-pagingOutput").hide();
      text.children(".tobago-sheet-pagingInput").show().focus().select();
    });
    pagingText.children(".tobago-sheet-pagingInput")
        .blur(function () {
          Tobago.Sheet.hideInputOrSubmit(jQuery(this));
        }).keydown(function (event) {
          if (event.keyCode === 13) {
            event.stopPropagation();
            event.preventDefault();
            jQuery(this).blur();
          }
        });
  });
};

/*
  when an event occurs (initial load OR scroll event OR AJAX response)

  then -> Tobago.Sheet.lazyCheck()
          1. check, if the lazy reload is currently active
             a) yes -> do nothing and exit
             b) no  -> step 2.
          2. check, if there are data need to load (depends on scroll position and already loaded data)
             a) yes -> set lazy reload to active and make an AJAX request with Tobago.Sheet.reloadLazy()
             b) no  -> do nothing and exit

   AJAX response -> 1. update the rows in the sheet from the response
                    2. go to the first part of this description
*/

Tobago.Sheet.lazyCheck = function($sheet) {
  if ($sheet.data("tobago-lazy-active")) {
    // nothing to do, because there is an active AJAX running
    return;
  }

  var lastCheck = $sheet.data("tobago-lazy-last-check");
  if (lastCheck && Date.now() - lastCheck < 100) {
    // do nothing, because the last call was just a moment ago
    return;
  }

  $sheet.data("tobago-lazy-last-check", Date.now());
  var next = Tobago.Sheet.nextLazyLoad($sheet);
  // console.info("next %o", next); // @DEV_ONLY
  if (next) {
    $sheet.data("tobago-lazy-active", true);
    var id = $sheet.attr("id");
    var input = document.getElementById(id + ":pageActionlazy");
    input.value = next;
    Tobago.Sheets.get(id).reloadWithAction(input, input.id);
  }
};

Tobago.Sheet.nextLazyLoad = function($sheet) {
  // find first tr in current visible area
  var $sheetBody = $sheet.find(".tobago-sheet-body");
  var rows = $sheet.data("tobago-rows");
  var $tbody = $sheetBody.find("table tbody");
  var min = 0;
  var trElements = $tbody.prop("rows");
  var max = trElements.length;
  // binary search
  while (min < max) {
    var i = Math.floor((max - min) / 2) + min;
    // console.log("min i max -> %d %d %d", min, i, max); // @DEV_ONLY
    if (Tobago.Sheet.isRowAboveVisibleArea($sheetBody, trElements[i])) {
      min = i + 1;
    } else {
      max = i;
    }
  }
  for (i = min; i < min + rows && i < trElements.length; i++) {
    if (Tobago.Sheet.isRowDummy($sheetBody, trElements[i])) {
      return i + 1;
    }
  }

  return null;
};

Tobago.Sheet.isRowAboveVisibleArea = function($sheetBody, tr) {
  var viewStart = $sheetBody.prop("scrollTop");
  var trEnd = tr.offsetTop + tr.clientHeight;
  return trEnd < viewStart;
};

Tobago.Sheet.isRowDummy = function($sheetBody, tr) {
  return tr.hasAttribute("dummy");
};

Tobago.Sheet.hideInputOrSubmit = function(input) {
  var output = input.siblings(".tobago-sheet-pagingOutput");
  var changed = output.html() !== input.val();
  var sheetId = input.parents(".tobago-sheet:first").attr("id");
  output.html(input.val());
  if (changed) {
    Tobago.Sheets.get(sheetId).reloadWithAction(input.get(0), input.attr("id"));
  } else {
    console.info("no update needed"); // @DEV_ONLY
    input.hide();
    output.show();
  }
};

Tobago.Sheet.findHiddenSelected = function($sheet){
  var id = $sheet.attr("id") + Tobago.SUB_COMPONENT_SEP + "selected";
  return jQuery(Tobago.Utils.escapeClientId(id));
};

Tobago.Sheet.findHiddenScrollPosition = function($sheet){
  var id = $sheet.attr("id") + Tobago.SUB_COMPONENT_SEP + "scrollPosition";
  return jQuery(Tobago.Utils.escapeClientId(id));
};

Tobago.Sheet.findHiddenWidths = function($sheet){
  var id = $sheet.attr("id") + Tobago.SUB_COMPONENT_SEP + "widths";
  return jQuery(Tobago.Utils.escapeClientId(id));
};

Tobago.Sheet.findHiddenRendered = function($sheet){
  var id = $sheet.attr("id") + Tobago.SUB_COMPONENT_SEP + "rendered";
  return jQuery(Tobago.Utils.escapeClientId(id));
};

Tobago.Sheet.initHeaderFillerWidths = function($sheet, columnWidths) {
  var autoFillNull = false;
  if (columnWidths !== undefined && columnWidths.length === 0) {
    var layout = $sheet.data("tobago-layout");
    if (layout && layout.columns && layout.columns.length > 0) {
      var rendered = jQuery.parseJSON(Tobago.Sheet.findHiddenRendered($sheet).val());
      var tokens = layout.columns;
      var i;
      for (i = 0; i < tokens.length; i++) {
        if (rendered[i] === "true") {
          if (typeof tokens[i] === "number") {
            autoFillNull = true;
          } else if (tokens[i] === "auto") {
            autoFillNull = true;
          }
        }
      }
    }
  }

  var $headerTable = $sheet.find(".tobago-sheet-headerTable");
  var $headerCols = $headerTable.find("col");
  $headerCols.last().attr("width", Tobago.Sheet.getScrollBarSize());
  if (autoFillNull) {
    $headerCols.eq(-2).attr("width", 0);
  }

  var $bodyTable = $sheet.find(".tobago-sheet-bodyTable");
  var $bodyCols = $bodyTable.find("col");
  $bodyCols.last().attr("width", Tobago.Sheet.getScrollBarSize());
  if (autoFillNull) {
    $bodyCols.eq(-2).attr("width", 0);
  }
};

Tobago.Sheet.getScrollBarSize = function() {
  var $outer = $('<div>').css({visibility: 'hidden', width: 100, overflow: 'scroll'}).appendTo('body'),
      widthWithScroll = $('<div>').css({width: '100%'}).appendTo($outer).outerWidth();
  $outer.remove();
  return 100 - widthWithScroll;
};

Tobago.Sheet.prototype.setup = function() {
  console.time("[tobago-sheet] setup"); // @DEV_ONLY
  this.initReload();
  console.timeEnd("[tobago-sheet] setup"); // @DEV_ONLY
};

Tobago.Sheet.prototype.initReload = function() {
  var sheet = jQuery(Tobago.Utils.escapeClientId(this.id));
  var reload = sheet.data("tobago-reload");
  if (typeof reload === "number") {
    Tobago.addReloadTimeout(this.id, Tobago.bind2(this, "reloadWithAction", null, this.id), reload);
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

    if (!Tobago.Sheet.isInputElement(jQuery(srcElement))) {
      var row = jQuery(srcElement).closest("tr");
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
          //Tobago.reloadComponent(srcElement, this.dblClickReloadComponentId, action);
          jsf.ajax.request(
              action,
              event,
              {
                //"javax.faces.behavior.event": "dblclick",
                execute: this.dblClickReloadComponentId,
                render: this.dblClickReloadComponentId
              });
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

Tobago.Sheet.getRows = function($sheet) {
  return $sheet.find(">div>table>tbody>tr");
};

Tobago.Sheet.isRowSelected = function(sheet, row) {
  return Tobago.Sheet.isSelected(sheet, row.data("tobago-row-index"));
};

Tobago.Sheet.isSelected = function(sheet, rowIndex) {
  return Tobago.Sheet.findHiddenSelected(sheet).val().indexOf("," + rowIndex + ",") >= 0;
};

Tobago.Sheet.resetSelected = function($sheet) {
  Tobago.Sheet.findHiddenSelected($sheet).val(",");
};

Tobago.Sheet.toggleSelection = function($sheet, $row, $checkbox) {
  $sheet.data("tobago-last-clicked-row-index", $row.index());
  if (!$checkbox.is(":disabled")) {
    var $selected = Tobago.Sheet.findHiddenSelected($sheet);
    var rowIndex = Tobago.Sheet.getDataIndex($sheet, $row);
    if (Tobago.Sheet.isSelected($sheet, rowIndex)) {
      Tobago.Sheet.deselectRow($selected, rowIndex, $row, $checkbox);
    } else {
      Tobago.Sheet.selectRow($selected, rowIndex, $row, $checkbox);
    }
  }
};

Tobago.Sheet.selectAll = function($sheet) {
  var $rows = Tobago.Sheet.getRows($sheet);
  Tobago.Sheet.selectRange($sheet, $rows, 0, $rows.length - 1, true, false);
};

Tobago.Sheet.deselectAll = function($sheet) {
  var $rows = Tobago.Sheet.getRows($sheet);
  Tobago.Sheet.selectRange($sheet, $rows, 0, $rows.length - 1, false, true);
};

Tobago.Sheet.toggleAll = function(sheet) {
  var rows = Tobago.Sheet.getRows(sheet);
  Tobago.Sheet.selectRange(sheet, rows, 0, rows.length - 1, true, true);
};

Tobago.Sheet.selectRange = function($sheet, $rows, first, last, selectDeselected, deselectSelected) {
  if ($rows.length === 0) {
    return;
  }
  var selected = Tobago.Sheet.findHiddenSelected($sheet);
  for (var i = first; i <= last; i++) {
    var row = $rows.eq(i);
    var checkbox = Tobago.Sheet.getSelectorCheckbox(row);
    if (!checkbox.is(":disabled")) {
      var rowIndex = Tobago.Sheet.getDataIndex($sheet, row);
      var on = selected.val().indexOf("," + rowIndex + ",") >= 0;
      if (selectDeselected && !on) {
        Tobago.Sheet.selectRow(selected, rowIndex, row, checkbox);
      } else if (deselectSelected && on) {
        Tobago.Sheet.deselectRow(selected, rowIndex, row, checkbox);
      }
    }
  }
};

Tobago.Sheet.getDataIndex = function(sheet, row) {
  return row.data("tobago-row-index");
};

/**
 * @param $selected input-element type=hidden: Hidden field with the selection state information
 * @param rowIndex int: zero based index of the row.
 * @param $row tr-element: the row.
 * @param $checkbox input-element: selector in the row.
 */
Tobago.Sheet.selectRow = function($selected, rowIndex, $row, $checkbox) {
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
Tobago.Sheet.deselectRow = function($selected, rowIndex, $row, $checkbox) {
  $selected.val($selected.val().replace(new RegExp("," + rowIndex + ","), ","));
  $row.removeClass("tobago-sheet-row-markup-selected table-info");
//  checkbox.prop("checked", false); Async because of TOBAGO-1312
  setTimeout(function() {$checkbox.prop("checked", false);}, 0);
};

Tobago.Sheet.isInputElement = function($element) {
  return ["INPUT", "TEXTAREA", "SELECT", "A", "BUTTON"].indexOf($element.prop("tagName")) > -1;
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
