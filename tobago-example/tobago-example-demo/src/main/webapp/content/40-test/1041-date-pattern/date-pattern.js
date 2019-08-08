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

TestDateTime = {};

// TODO: to be reimplemented!!!

TestDateTime.init = function () {

  var panel = jQuery("#page\\:jquery_date_time_pattern");
  var inputs = panel.find(".tobago-in, .tobago-date");

  for (var i = 0; i < inputs.length; i += 5) {
    var javaPattern = inputs.eq(i);
    var javaFormatted = inputs.eq(i + 1);
    var jQueryUIDatePattern = inputs.eq(i + 2);
    var jQueryUITimePattern = inputs.eq(i + 3);
    var jQueryUIFormatted = inputs.eq(i + 4);
    var result = jQueryUIFormatted.next();

    var pattern = javaFormatted.data("tobago-pattern");
    javaPattern.val(pattern);

    var analyzed = DateTime.analyzePattern(pattern);

    jQueryUIDatePattern.val(analyzed.dateFormat);
    jQueryUITimePattern.val(analyzed.timeFormat);

    try {
      var javaValue = javaFormatted.val();
      var date = null; // type: JS date
      var time = null; // type: jQuery UI time object
      var jQueryValue = null;
      var i18n = javaFormatted.data("tobago-date-time-i18n");
      switch (analyzed.type) {
        case "datetime":
          // todo: use moment.js
          date = jQuery.datepicker.parseDateTime(
              analyzed.dateFormat, analyzed.timeFormat, javaValue, i18n, {
                separator: analyzed.separator,
                // workaround for bug: https://github.com/trentrichardson/jQuery-Timepicker-Addon/issues/736
                timeFormat: analyzed.timeFormat
              }
          );
          time = {
            hour: date.getHours(),
            minute: date.getMinutes(),
            second: date.getSeconds(),
            millisec: date.getMilliseconds(),
            microsec: date.getMicroseconds()
          };
          // todo: use moment.js
          jQueryValue
              = jQuery.datepicker.formatDate(analyzed.dateFormat, date, i18n)
              + analyzed.separator
              + jQuery.datepicker.formatTime(analyzed.timeFormat, time, i18n);
          break;
        case "date":
          // todo: use moment.js
          date = jQuery.datepicker.parseDate(analyzed.dateFormat, javaValue, i18n);
          jQueryValue = jQuery.datepicker.formatDate(analyzed.dateFormat, date, i18n);
          break;
        case "time":
          // todo: use moment.js
          time = jQuery.datepicker.parseTime(analyzed.timeFormat, javaValue, i18n);
          jQueryValue = jQuery.datepicker.formatTime(analyzed.timeFormat, time, i18n);
          break;
        default:
          console.error("invalid: not date-pattern nor time-pattern");
          jQueryValue = "no type";
      }
      jQueryUIFormatted.val(jQueryValue);

    } catch (e) {
      console.error(e);
    }

    if (javaFormatted.val() !== jQueryUIFormatted.val()) {
      jQueryUIFormatted.addClass("tobago-in-markup-error");

      if (javaPattern.hasClass("tobago-in-markup-error")) {
        result.addClass("tobago-label-markup-warn");
        result.html("java pattern broken");
      } else {
        result.addClass("tobago-label-markup-error");
        result.html("pattern conversion failed");
      }
    } else {
      result.addClass("tobago-label-markup-info");
      if (javaPattern.hasClass("tobago-in-markup-error")) {
        result.html("fixed");
      } else {
        result.html("okay");
      }
    }
  }
};

Listener.register(TestDateTime.init, Phase.DOCUMENT_READY);
