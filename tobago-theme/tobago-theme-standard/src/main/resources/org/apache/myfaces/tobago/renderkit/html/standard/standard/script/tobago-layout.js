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

function init(table) {

  var elements = table.children("tbody").children("tr").children("td").children();
  elements.each(function () {
    var element = jQuery(this);
    var data = element.data("tobago-layout");
    if (data !== undefined) {
      var width = data.width;
      if (width == null && element.prop("tagName") == "img") {
        width = element.outerWidth();
      }
      if (width != null) {
        element.css("width", width + "px");
      }

      var height = data.height;
      if (height == null && element.prop("tagName") == "img") {
        height = element.outerHeight();
      }
      if (height != null) {
        element.css("height", height + "px");
      }
    }
  });
}

function layout(table, horizontal) {
  var cells;
  var banks;
  var tokens;
  var css;
  var desired;

  if (horizontal) {
//    cells = table.find("tr:first>td");
    banks = table.children("colgroup").children("col");
    tokens = table.data("tobago-layout").columns;
    css = "width";
    desired = table.outerWidth();
//    desired = table.parent().data("tobago-style").width.replace("px", ""); // todo: data("tobago-layout") wohl doch nicht so gut...? der wert wurde ja schon berechnet...
  } else {
//    cells = table.find("tr");
    banks = table.children("tbody").children("tr");
    tokens = table.data("tobago-layout").rows;
    css = "height";
    desired = table.outerHeight();
//    desired = table.parent().data("tobago-style").height.replace("px", ""); // todo: data("tobago-layout")
  }

  if (tokens) {
    table.css(css, "0px");
    var i;
    var cell;
    var sumRelative = 0;
    var sumUsed = 0;
    for (i = 0; i < tokens.length; i++) {
//      cell = cells.eq(i);
      cell = banks.eq(i);
      switch (typeof tokens[i]) {
        case "number":
          // a relative value
          sumRelative += tokens[i];
          break;
        case "string":
          // a string, currently only "auto" is supported
          if ("auto" == tokens[i]) {
            // nothing to do
            sumUsed += horizontal ? cell.outerWidth() : cell.outerHeight();
          } else {
            console.warn("currently only 'auto' is supported, but found: '" + tokens[i] + "'");  // @DEV_ONLY
          }
          break;
        case "object":
          if (tokens[i].pixel) {
            setLength(table, banks, i, css, tokens[i].pixel + "px");
            sumUsed += tokens[i].pixel;
          } else {
            console.warn("can't find pixel in object: '" + tokens[i] + "'");  // @DEV_ONLY
          }
          break;
        default:
          console.warn("unsupported type of: '" + tokens[i] + "'");  // @DEV_ONLY
          break;
      }
    }

    table.css(css, "");
    var rest = desired - sumUsed;
    if (rest > 0 && sumRelative > 0) {
      for (i = 0; i < tokens.length; i++) {
        if (typeof tokens[i] == "number") {
          setLength(table, banks, i, css, rest * tokens[i] / sumRelative + "px");
        }
      }
    }
  }
}

function setLength(table, banks, i, css, length) {
  banks.eq(i).css(css, length);
  /*
   if (css == "width") {
   var trs = table.children("tbody").children("tr");
   trs.each(function () {
   jQuery(this).children("td").eq(i).children().css(css, length);
   });
   } else {
   var tr = table.children("tbody").children("tr").eq(i);
   tr.children("td").children().css(css, length);
   }
   */
}

function setLength2(banks, i, css, length) {
  banks.eq(i).css(css, length);
}

function layoutFlex(container, horizontal) {

  // todo: modernizr
  // if (!Modernizr.flexbox && !Modernizr.flexboxtweener) ... do other

  var cells;
  var banks;
  var tokens;
  var css;

  if (horizontal) {
    banks = container.children();
    tokens = container.data("tobago-layout").columns;
    css = "width";
  } else {
    banks = container.children();
    tokens = container.data("tobago-layout").rows;
    css = "height";
  }

  if (tokens) {
    var i;
    var cell;
    for (i = 0; i < tokens.length; i++) {
      cell = banks.eq(i);
      switch (typeof tokens[i]) {
        case "number":
          // a relative value
          // todo: check for "any other" (non-layout) elements
          var flex = "flex";
          if (Tobago.browser.isMsie678910) { // todo: modernizr
            flex = "-ms-flex";
          }
          // using "0px" and not "0", because IE11 needs the "px"
          //container.children().eq(i).css(Modernizr.prefixed("flex"), tokens[i] + " 0 0px");  // todo: modernizr
          container.children().eq(i).css(flex, tokens[i] + " 0 0px");
          break;
        case "string":
          // a string, currently only "auto" is supported
          break;
        case "object":
          if (tokens[i].pixel) {
            setLength2(banks, i, css, tokens[i].pixel + "px");
          } else {
            console.warn("can't find pixel in object: '" + tokens[i] + "'");  // @DEV_ONLY
          }
          break;
        default:
          console.warn("unsupported type of: '" + tokens[i] + "'");  // @DEV_ONLY
          break;
      }
    }
  }
}

jQuery(document).ready(function () {
  var gridLayouts = jQuery(".tobago-gridLayout");

  gridLayouts.each(function () {
    var table = jQuery(this);
    init(table);
  });

  gridLayouts.each(function () {
    var table = jQuery(this);
    layout(table, true);
    layout(table, false);
  });

  //////////////////////////////////////////////

  // the flex stuff.

  var flexLayouts = jQuery(".tobago-flexLayout");

  flexLayouts.each(function () {
    var container = jQuery(this);
    layoutFlex(container, true);
    layoutFlex(container, false);
  });

  //////////////////////////////////////////////

  // fixing fixed header/footer: content should not scroll behind the footer
  // XXX Is there a CSS solution?

  var header = jQuery(".navbar-fixed-top");
  var footer = jQuery(".navbar-fixed-bottom");
  var content = footer.prev();

  content.css({
    marginTop: (parseInt(content.css("margin-top").replace("px", "")) + header.outerHeight(true)) + "px",
    marginBottom: (parseInt(content.css("margin-bottom").replace("px", "")) + footer.outerHeight(true)) + "px"
  });

});
