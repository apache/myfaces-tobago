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

Tobago.DateTime = {};

Tobago.DateTime.init = function (elements) {

  // time input fields
//  jQuery.datepicker.setDefaults(jQuery.datepicker.regional['de']);

  Tobago.Utils.selectWithJQuery(elements, ".tobago-date")
      .not("[disabled]")
      .not("[readonly]")
      .each(function () {
        var date = jQuery(this);

        var analyzed = Tobago.DateTime.analyzePattern(date.data("tobago-pattern"));
        var options = {
          showOn: "button", // fixme: use "button" end put a trigger inside it
          buttonImageOnly: true,
// tbd          changeMonth: true,
// tbd          changeYear: true,
          showAnim: "", // just show it directly
          showOtherMonths: true,
          selectOtherMonths: true
        };
        if (analyzed.dateFormat) {
          options.dateFormat = analyzed.dateFormat;
        }
        if (analyzed.timeFormat) {
          options.timeFormat = analyzed.timeFormat;
        }
        if (analyzed.separator) {
          options.separator = analyzed.separator;
        }
        var i18n = date.data("tobago-date-time-i18n");
        if (i18n) {
          var monthNames = i18n.monthNames;
          if (monthNames) {
            options.monthNames = monthNames;
          }
          var monthNamesShort = i18n.monthNamesShort;
          if (monthNamesShort) {
            options.monthNamesShort = monthNamesShort;
          }
          var dayNames = i18n.dayNames;
          if (dayNames) {
            options.dayNames = dayNames;
          }
          var dayNamesShort = i18n.dayNamesShort;
          if (dayNamesShort) {
            options.dayNamesShort = dayNamesShort;
          }
          var dayNamesMin = i18n.dayNamesMin;
          if (dayNamesMin) {
            options.dayNamesMin = dayNamesMin;
          }
          var firstDay = i18n.firstDay;
          if (firstDay) {
            options.firstDay = firstDay;
          }
        }

        switch (analyzed.type) {
          case "datetime":
            date.datetimepicker(options);
            break;
          case "date":
            date.datepicker(options);
            break;
          case "time":
            date.timepicker(options);
            break;
          default:
            console.error("invalid: not date-pattern nor time-pattern");  // @DEV_ONLY
        }

        // XXX workaround for Bootstrap with "old" jQuery UI input control
        date.siblings("span.input-group-btn").children("button.btn").click(function() {
          jQuery(this).parent().siblings(".ui-datepicker-trigger").click();
        });
      });
};

/*
 Get the pattern from the "Java world" (http://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html)
 and convert it to jQuery UI. Here we need the "date" "time" and "separator" separately.

 Attention: Not every pattern char is supported.
 Currently only date time side-by-side is allowed.
 */
Tobago.DateTime.analyzePattern = function (pattern) {

  if (!pattern || pattern.length > 100) {
    console.warn("Pattern not supported: " + pattern);  // @DEV_ONLY
    pattern = "";
  }

  var dateChars = ["G", "y", "Y", "M", "L", "w", "W", "D", "d", "F", "E", "u"];
  var timeChars = ["a", "H", "k", "K", "h", "m", "s", "S", "z", "Z", "X"];
  var i, pos;
  var dateFormat = "", timeFormat = "", separator = " ";

  var minDate = Infinity, maxDate = -1;
  for (i = 0; i < dateChars.length; i++) {
    pos = pattern.indexOf(dateChars[i]);
    if (pos > -1) {
      minDate = Math.min(minDate, pos);
    }
    pos = pattern.lastIndexOf(dateChars[i]);
    if (pos > -1) {
      maxDate = Math.max(maxDate, pos);
    }
  }

  var minTime = Infinity, maxTime = -1;
  for (i = 0; i < timeChars.length; i++) {
    pos = pattern.indexOf(timeChars[i]);
    if (pos > -1) {
      minTime = Math.min(minTime, pos);
    }
    pos = pattern.lastIndexOf(timeChars[i]);
    if (pos > -1) {
      maxTime = Math.max(maxTime, pos);
    }
  }

  if (minDate != Infinity) {
    if (minTime != Infinity) {
      if (maxDate < minTime) {
        dateFormat = pattern.substring(0, maxDate + 1);
        separator = pattern.substring(maxDate + 1, minTime);
        timeFormat = pattern.substring(minTime);
      } else {
        // should not happen, the Renderer should check the pattern.
        console.warn("Pattern not supported: " + pattern);  // @DEV_ONLY
      }
    } else {
      dateFormat = pattern;
    }
  } else {
    if (minTime != Infinity) {
      timeFormat = pattern;
    } else { // empty pattern
      // should not happen, the Renderer should check the pattern.
      console.warn("Pattern not supported: " + pattern);  // @DEV_ONLY
    }
  }

  // different pattern in Java and jQuery UI

  if (dateFormat.search(/y{3,}/) > -1) {
    dateFormat = dateFormat.replace(/y{3,}/g, "yy")
  } else if (dateFormat.search(/y{2}/) > -1) {
    dateFormat = dateFormat.replace(/y{2}/g, "y")
  } else {
    dateFormat = dateFormat.replace(/y/g, "yy")
  }

  if (dateFormat.search(/M{4,}/) > -1) {
    dateFormat = dateFormat.replace(/M{4,}/g, "MM")
  } else if (dateFormat.search(/M{3}/) > -1) {
    dateFormat = dateFormat.replace(/M{3}/g, "M")
  } else if (dateFormat.search(/M{2}/) > -1) {
    dateFormat = dateFormat.replace(/M{2}/g, "mm")
  } else {
    dateFormat = dateFormat.replace(/M/g, "m")
  }

  if (dateFormat.search(/L{4,}/) > -1) {
    dateFormat = dateFormat.replace(/L{4,}/g, "MM")
  } else if (dateFormat.search(/L{3}/) > -1) {
    dateFormat = dateFormat.replace(/L{3}/g, "M")
  } else if (dateFormat.search(/L{2}/) > -1) {
    dateFormat = dateFormat.replace(/L{2}/g, "mm")
  } else {
    dateFormat = dateFormat.replace(/L/g, "m")
  }

  if (dateFormat.search(/D{3,}/) > -1) {
    dateFormat = dateFormat.replace(/D{3,}/g, "oo")
  } else if (dateFormat.search(/D{1,2}/) > -1) {
    dateFormat = dateFormat.replace(/D{1,2}/g, "o")
  }

  if (dateFormat.search(/d{2,}/) > -1) {
    dateFormat = dateFormat.replace(/d{2,}/g, "dd")
  } else {
    dateFormat = dateFormat.replace(/d/g, "d")
  }

  if (dateFormat.search(/E{4,}/) > -1) {
    dateFormat = dateFormat.replace(/E{4,}/g, "DD")
  } else {
    dateFormat = dateFormat.replace(/E{1,3}/g, "D")
  }

  timeFormat = timeFormat.replace(/S{1,}/g, "l");

  timeFormat = timeFormat.replace(/a{1,}/g, "TT");

  timeFormat = timeFormat.replace(/z/g, "Z"); // XXX is this correct?

  if (dateFormat == "") {
    dateFormat = null;
  }
  if (timeFormat == "") {
    timeFormat = null;
  }

  return {
    dateFormat: dateFormat,
    separator: separator,
    timeFormat: timeFormat,
    type: dateFormat != null ? timeFormat != null ? "datetime" : "date" : timeFormat != null ? "time" : null
  };

};

Tobago.registerListener(Tobago.DateTime.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.DateTime.init, Tobago.Phase.AFTER_UPDATE);

// XXX Check this
jQuery(document).ready(function () {
  jQuery(".tobago-date").each(function() {
    var date = jQuery(this);
    var format = date.data("tobago-pattern");
    format = format.replace(/M/g, "m");
    date.datepicker({
      format: format
    });
  });
});
