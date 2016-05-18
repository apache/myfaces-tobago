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

  Tobago.Utils.selectWithJQuery(elements, ".tobago-date")
      .not("[disabled]")
      .not("[readonly]")
      .each(function () {
        var date = jQuery(this);

        var analyzed = Tobago.DateTime.analyzePattern(date.data("tobago-pattern"));
        var options = {
          format: analyzed,
          icons: {
            time: 'fa fa-clock-o',
            date: 'fa fa-calendar',
            up: 'fa fa-chevron-up',
            down: 'fa fa-chevron-down',
            previous: 'fa fa-chevron-left',
            next: 'fa fa-chevron-right',
            today: 'fa fa-calendar-check-o',
            clear: 'fa fa-trash',
            close: 'fa fa-times'
          }
        };

        var i18n = date.data("tobago-date-time-i18n");
        if (i18n) {
          var monthNames = i18n.monthNames;
          if (monthNames) {
            moment.localeData()._months = monthNames;
          }
          var monthNamesShort = i18n.monthNamesShort;
          if (monthNamesShort) {
            moment.localeData()._monthsShort = monthNamesShort;
          }
          var dayNames = i18n.dayNames;
          if (dayNames) {
            moment.localeData()._weekdays = dayNames;
          }
          var dayNamesShort = i18n.dayNamesShort;
          if (dayNamesShort) {
            moment.localeData()._weekdaysShort = dayNamesShort;
          }
          var dayNamesMin = i18n.dayNamesMin;
          if (dayNamesMin) {
            moment.localeData()._weekdaysMin = dayNamesMin;
          }
          var firstDay = i18n.firstDay;
          if (firstDay) {
            moment.localeData()._week.dow = firstDay;
          }
        }

        date.parent().datetimepicker(options);
      });
};

/*
 Get the pattern from the "Java world" (http://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html)
 and convert it to 'moment.js'.
 Attention: Not every pattern char is supported.
 */
Tobago.DateTime.analyzePattern = function (pattern) {

  if (!pattern || pattern.length > 100) {
    console.warn("Pattern not supported: " + pattern);  // @DEV_ONLY
    pattern = "";
  }
  if(pattern.search("G") > -1 || pattern.search("W") > -1 || pattern.search("F") > -1
      || pattern.search("K") > -1 || pattern.search("z") > -1 || pattern.search("X") > -1) {
    console.warn("Pattern chars 'G', 'W', 'F', 'K', 'z' and 'X' are not supported: " + pattern); // @DEV_ONLY
    pattern = "";
  }

  pattern = pattern.replace(/y/g, "Y");
  pattern = pattern.replace(/\bY\b/g, "YYYY");
  pattern = pattern.replace(/\bYYY\b/g, "YY");
  pattern = pattern.replace(/YYYYY(Y)+/g, "YYYYY");

  pattern = pattern.replace(/MMMM(M)+/g, "MMMM");

  pattern = pattern.replace(/\bw\b/g, "W");
  pattern = pattern.replace(/ww(w)+/g, "WW");

  pattern = pattern.replace(/DDD(D)*/g, "DDDD");
  pattern = pattern.replace(/\bD{1,2}\b/g, "DDD");

  pattern = pattern.replace(/dd(d)*/g, "DD");
  pattern = pattern.replace(/\bd\b/g, "D");

  pattern = pattern.replace(/\bE{1,3}\b/g, "dd");
  pattern = pattern.replace(/EEEE(E)*/g, "dddd");

  pattern = pattern.replace(/u(u)*/g, "E");
  pattern = pattern.replace(/a(a)*/g, "A");
  pattern = pattern.replace(/HH(H)*/g, "HH");
  pattern = pattern.replace(/kk(k)*/g, "kk");
  pattern = pattern.replace(/hh(h)*/g, "hh");
  pattern = pattern.replace(/mm(m)*/g, "mm");
  pattern = pattern.replace(/ss(s)*/g, "ss");
  pattern = pattern.replace(/SSS(S)*/g, "SSS");

  pattern = pattern.replace(/ZZ(Z)*/g, "ZZ");

  return pattern;
};

Tobago.registerListener(Tobago.DateTime.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.DateTime.init, Tobago.Phase.AFTER_UPDATE);
