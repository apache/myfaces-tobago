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
import {Command} from "./tobago-command";

class DateTime {

  static init(element: HTMLElement) {
    for (const e of DomUtils.selfOrQuerySelectorAll(element, ".tobago-date:not([readonly]):not([disabled])")) {
      const date: HTMLInputElement = e as HTMLInputElement;

      const analyzed = DateTime.analyzePattern(date.dataset.tobagoPattern);
      const options = {
        format: analyzed,
        showTodayButton: date.dataset.tobagoTodayButton === "data-tobago-today-button",
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
        keyBinds: {
          left: function ($widget) {
            const widget: HTMLDivElement = $widget[0] as HTMLDivElement;
            if (widget === undefined) {
              if (date.selectionStart === date.selectionEnd) {
                if (date.selectionStart > 0 || date.selectionStart > 0) {
                  date.selectionStart--;
                  date.selectionEnd--;
                }
              } else {
                date.selectionEnd = date.selectionStart;
              }
            } else if (DomUtils.isVisible(widget.querySelector(".datepicker"))) {
              this.date(this.date().clone().subtract(1, 'd'));
            }
          },
          right: function ($widget) {
            const widget: HTMLDivElement = $widget[0] as HTMLDivElement;
            if (widget === undefined) {
              if (date.selectionStart === date.selectionEnd) {
                if (date.selectionStart > 0 || date.selectionStart < date.value.length) {
                  date.selectionEnd++;
                  date.selectionStart++;
                }
              } else {
                date.selectionStart = date.selectionEnd;
              }
            } else if (DomUtils.isVisible(widget.querySelector(".datepicker"))) {
              this.date(this.date().clone().add(1, 'd'));
            }
          },
          enter: function ($widget) {
            const widget: HTMLDivElement = $widget[0] as HTMLDivElement;
            if (widget !== undefined && DomUtils.isVisible(widget.querySelector(".datepicker"))) {
              this.hide();
              fixKey(13);
            } else {
              //jQuery because used by datetimepicker
              jQuery(date).trigger(jQuery.Event("keypress", {
                which: 13,
                target: date
              }));
            }
          },
          escape: function ($widget) {
            const widget: HTMLDivElement = $widget[0] as HTMLDivElement;
            if (widget !== undefined && DomUtils.isVisible(widget.querySelector(".datepicker"))) {
              this.hide();
              fixKey(27);
            }
          },
          'delete': function () {
            if (date.selectionStart < date.value.length) {
              const selectionStart = date.selectionStart;
              let selectionEnd = date.selectionEnd;

              if (selectionStart === selectionEnd && selectionStart < date.value.length) {
                selectionEnd++;
              }
              date.value = date.value.substr(0, selectionStart)
                  + date.value.substr(selectionEnd, date.value.length);

              date.selectionEnd = selectionStart;
              date.selectionStart = selectionStart;
            }
          }
        },
        widgetParent: '.tobago-page-menuStore'
      };

      /**
       * After ESC or ENTER is pressed we need to fire the keyup event manually.
       * see: https://github.com/tempusdominus/bootstrap-4/issues/159
       */
      function fixKey(keyCode) {
        let keyupEvent = jQuery.Event("keyup");
        keyupEvent.which = keyCode;
        jQuery(date).trigger(keyupEvent);
      }

      const i18n = date.dataset.tobagoDateTimeI18n ? JSON.parse(date.dataset.tobagoDateTimeI18n) : undefined;
      if (i18n) {
        const monthNames = i18n.monthNames;
        if (monthNames) {
          moment.localeData()._months = monthNames;
        }
        const monthNamesShort = i18n.monthNamesShort;
        if (monthNamesShort) {
          moment.localeData()._monthsShort = monthNamesShort;
        }
        const dayNames = i18n.dayNames;
        if (dayNames) {
          moment.localeData()._weekdays = dayNames;
        }
        const dayNamesShort = i18n.dayNamesShort;
        if (dayNamesShort) {
          moment.localeData()._weekdaysShort = dayNamesShort;
        }
        const dayNamesMin = i18n.dayNamesMin;
        if (dayNamesMin) {
          moment.localeData()._weekdaysMin = dayNamesMin;
        }
        const firstDay = i18n.firstDay;
        if (firstDay) {
          moment.localeData()._week.dow = firstDay;
        }
      }

      let $dateParent = jQuery(date).parent(); //use jQuery because required for datetimepicker
      $dateParent.datetimepicker(options);

      // we need to add the change listener here, because
      // in line 1307 of bootstrap-datetimepicker.js
      // the 'stopImmediatePropagation()' stops the change-event
      // execution of line 686 in tobago.js
      $dateParent.on('dp.change', function (event) {
        let input: HTMLInputElement = this.querySelector("input");
        let commands = input.dataset.tobagoCommands ? JSON.parse(input.dataset.tobagoCommands) : undefined;
        if (commands && commands.change) {
          if (commands.change.execute || commands.change.render) {
            jsf.ajax.request(
                input.getAttribute("name"),
                event,
                {
                  "javax.faces.behavior.event": "change",
                  execute: commands.change.execute,
                  render: commands.change.render
                });
          } else if (commands.change.action) {
            Command.submitAction(this.firstElementChild, commands.change.action, commands.change);
          }
        }
      });

      // set position
      $dateParent.on('dp.show', function () {
        let datepicker: HTMLDivElement = document.querySelector(".bootstrap-datetimepicker-widget");
        let div: HTMLDivElement = this;
        let top, left;
        if (datepicker.classList.contains("bottom")) {
          top = DomUtils.offset(div).top + div.offsetHeight;
          left = DomUtils.offset(div).left;
          datepicker.style.top = top + "px";
          datepicker.style.bottom = "auto";
          datepicker.style.left = left + "px";
        } else if (datepicker.classList.contains("top")) {
          top = DomUtils.offset(div).top - datepicker.offsetHeight;
          left = DomUtils.offset(div).left;
          datepicker.style.top = top + "px";
          datepicker.style.bottom = "auto";
          datepicker.style.left = left + "px";
        }
        DateTime.addPastClass(date);
      });

      // set css class in update - like changing the month
      $dateParent.on('dp.update', function () {
        DateTime.addPastClass(date);
      });

      // fix for bootstrap-datetimepicker v4.17.45
      $dateParent.on('dp.show', function () {
        const collapseIn = document.querySelector(".bootstrap-datetimepicker-widget .collapse.in");
        const pickerSwitch = document.querySelector(".bootstrap-datetimepicker-widget .picker-switch a");

        if (collapseIn !== null) {
          collapseIn.classList.add("show");
        }
        if (pickerSwitch !== null) {
          pickerSwitch.addEventListener(
              "click", function () {
                // the click is executed before togglePicker() function
                let datetimepicker: HTMLDivElement = document.querySelector(".bootstrap-datetimepicker-widget");
                datetimepicker.querySelector(".collapse.in").classList.remove("in");
                datetimepicker.querySelector(".collapse.show").classList.add("in");
              });
        }
      });
    }
  }

  static addPastClass(date: HTMLInputElement) {
    let today = date.dataset.tobagoToday;
    if (today.length === 10) {
      const todayArray = today.split("-");
      if (todayArray.length === 3) {
        const year = todayArray[0];
        const month = todayArray[1];
        const day = todayArray[2];
        const todayTimestamp = new Date(month + "/" + day + "/" + year).getTime();

        const days = document.querySelectorAll(".bootstrap-datetimepicker-widget .datepicker-days td.day[data-day]");
        for (const day of days) {
          const currentTimestamp = new Date(day.getAttribute("data-day")).getTime();
          if (currentTimestamp < todayTimestamp) {
            day.classList.add("past");
          }
        }
      }
    }
  }

  /*
   Get the pattern from the "Java world" (http://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html)
   and convert it to 'moment.js'.
   Attention: Not every pattern char is supported.
   */
  static analyzePattern = function (pattern) {

    if (!pattern || pattern.length > 100) {
      console.warn("Pattern not supported: " + pattern);
      pattern = "";
    }

    let analyzedPattern = "";
    let nextSegment = "";
    let escMode = false;
    for (let i = 0; i < pattern.length; i++) {
      const currentChar = pattern.charAt(i);
      if (currentChar == "'" && escMode == false) {
        escMode = true;
        analyzedPattern += DateTime.analyzePatternPart(nextSegment);
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
    if (nextSegment != "") {
      if (escMode) {
        analyzedPattern += nextSegment;
      } else {
        analyzedPattern += DateTime.analyzePatternPart(nextSegment);
      }
    }

    return analyzedPattern;
  };

  static analyzePatternPart = function (pattern) {

    if (pattern.search("G") > -1 || pattern.search("W") > -1 || pattern.search("F") > -1
        || pattern.search("K") > -1 || pattern.search("z") > -1 || pattern.search("X") > -1) {
      console.warn("Pattern chars 'G', 'W', 'F', 'K', 'z' and 'X' are not supported: " + pattern);
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
}

Listener.register(DateTime.init, Phase.DOCUMENT_READY);
Listener.register(DateTime.init, Phase.AFTER_UPDATE);
