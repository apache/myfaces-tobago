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
        var $date = jQuery(this);

        var analyzed = Tobago.DateTime.analyzePattern($date.data("tobago-pattern"));
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
          },
          widgetParent: '.tobago-page'
        };

        var i18n = $date.data("tobago-date-time-i18n");
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

        $date.parent().datetimepicker(options);

        // we need to add the change listener here, because
        // in line 1307 of bootstrap-datetimepicker.js
        // the 'stopImmediatePropagation()' stops the change-event
        // execution of line 686 in tobago.js
        $date.parent().on('dp.change', function (event) {
          var commands = jQuery(this).find("input").data("tobago-commands");
          if (commands.change.partially) {
            //Tobago.reloadComponent(this, commands.change.partially, commands.change.action, commands.change);
            jsf.ajax.request(
                jQuery(this).find("input").attr("name"),
                event,
                {
                  "javax.faces.behavior.event": "change",
                  execute: commands.change.partially,
                  render: commands.change.partially
                });
          } else {
            Tobago.submitAction(this.firstElementChild, commands.change.action, commands.change);
          }
        });

        /*
         XXX need for fixing wrong positioning of bootstrap datepicker.
         See https://github.com/Eonasdan/bootstrap-datetimepicker/issues/790 for more information.
         */
        $date.parent().on('dp.show', function () {
          var datepicker = jQuery('.bootstrap-datetimepicker-widget');
          var $div = jQuery(this);
          var top, left;
          if (datepicker.hasClass('bottom')) {
            top = $div.offset().top + $div.outerHeight() - jQuery("body").offset().top;
            left = $div.offset().left;
            datepicker.css({
              'top': top + 'px',
              'bottom': 'auto',
              'left': left + 'px'
            });
          }
          else if (datepicker.hasClass('top')) {
            top = $div.offset().top - datepicker.outerHeight() - jQuery("body").offset().top;
            left = $div.offset().left;
            datepicker.css({
              'top': top + 'px',
              'bottom': 'auto',
              'left': left + 'px'
            });
          }
        });
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

  var analyzedPattern = "";
  var nextSegment = "";
  var escMode = false;
  for (i = 0; i < pattern.length; i++) {
    var currentChar = pattern.charAt(i);
    if (currentChar == "'" && escMode == false) {
      escMode = true;
      analyzedPattern += Tobago.DateTime.analyzePatternPart(nextSegment);
      nextSegment = "";
    } else if (currentChar == "'" && pattern.charAt(i + 1) == "'") {
      if (escMode) {
        nextSegment += "\\";
      }
      nextSegment += "'";
      i++;
    } else if (currentChar == "'" && escMode == true) {
      escMode = false;
      analyzedPattern += nextSegment;
      nextSegment = "";
    } else {
      if (escMode) {
        nextSegment += "\\";
      }
      nextSegment += currentChar;
    }
  }

  return analyzedPattern;
};

Tobago.DateTime.analyzePatternPart = function (pattern) {

  if (pattern.search("G") > -1 || pattern.search("W") > -1 || pattern.search("F") > -1
      || pattern.search("K") > -1 || pattern.search("z") > -1 || pattern.search("X") > -1) {
    console.warn("Pattern chars 'G', 'W', 'F', 'K', 'z' and 'X' are not supported: " + pattern); // @DEV_ONLY
    pattern = "";
  }

  if (pattern.search("y") > -1) {
    pattern = pattern.replace(/y/g, "Y");
  }
  if (pattern.search("Y") > -1) {
    pattern = pattern.replace(/\bY\b/g, "YYYY");
    pattern = pattern.replace(/\bYYY\b/g, "YY");
    pattern = pattern.replace(/YYYYYY+/g, "YYYYY");
  }

  if (pattern.search("MMMMM") > -1) {
    pattern = pattern.replace(/MMMMM+/g, "MMMM");
  }

  if (pattern.search("w") > -1) {
    pattern = pattern.replace(/\bw\b/g, "W");
    pattern = pattern.replace(/www+/g, "WW");
  }

  if (pattern.search("D") > -1) {
    pattern = pattern.replace(/DDD+/g, "DDDD");
    pattern = pattern.replace(/\bD{1,2}\b/g, "DDD");
  }

  if (pattern.search("d") > -1) {
    pattern = pattern.replace(/dd+/g, "DD");
    pattern = pattern.replace(/\bd\b/g, "D");
  }

  if (pattern.search("E") > -1) {
    pattern = pattern.replace(/\bE{1,3}\b/g, "dd");
    pattern = pattern.replace(/EEEE+/g, "dddd");
  }

  if (pattern.search("u") > -1) {
    pattern = pattern.replace(/u+/g, "E");
  }
  if (pattern.search("a") > -1) {
    pattern = pattern.replace(/a+/g, "A");
  }
  if (pattern.search("HHH") > -1) {
    pattern = pattern.replace(/HHH+/g, "HH");
  }
  if (pattern.search("kkk") > -1) {
    pattern = pattern.replace(/kkk+/g, "kk");
  }
  if (pattern.search("hhh") > -1) {
    pattern = pattern.replace(/hhh+/g, "hh");
  }
  if (pattern.search("mmm") > -1) {
    pattern = pattern.replace(/mmm+/g, "mm");
  }
  if (pattern.search("sss") > -1) {
    pattern = pattern.replace(/sss+/g, "ss");
  }
  if (pattern.search("SSSS") > -1) {
    pattern = pattern.replace(/SSSS+/g, "SSS");
  }
  if (pattern.search("Z") > -1) {
    pattern = pattern.replace(/\bZ\b/g, "ZZ");
    pattern = pattern.replace(/ZZZ+/g, "ZZ");
  }

  return pattern;
};

Tobago.registerListener(Tobago.DateTime.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.DateTime.init, Tobago.Phase.AFTER_UPDATE);
