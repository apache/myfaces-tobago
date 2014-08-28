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

Tobago.Calendar = {};

Tobago.Calendar.init = function (elements) {
  // init next/prev buttons
  var buttons = Tobago.Utils.selectWidthJQuery(elements, ".tobago-calendar-header[data-tobago-command]");
  buttons.each(function () {
    var button = jQuery(this);
    var step;
    switch (button.data("tobago-command")) {
      case "fastPrev":
        step = -12;
        break;
      case "prev":
        step = -1;
        break;
      case "next":
        step = 1;
        break;
      case "fastNext":
        step = 12;
        break;
      default:
        // ignore
        return;
    }
    button.click(function () {
      Tobago.Calendar.addMonth(jQuery(this).parents(".tobago-calendar"), step);
    });
  });

  // click directly on a day
  var days = Tobago.Utils.selectWidthJQuery(elements, ".tobago-calendar-day");
  days.each(function () {
    var day = jQuery(this);
    day.click(function () {
      var day = jQuery(this);
      var column = day.index();
      var row = day.parent().index();
      Tobago.Calendar.selectDay(jQuery(this).parents(".tobago-calendar"), row, column);
    });
  });

  // init from data field, if there is any (e. g. we are in date picker popup)
  var calendarWithDateField = Tobago.Utils.selectWidthJQuery(elements, ".tobago-calendar[data-tobago-date-input-id]");
  calendarWithDateField.each(function () {
    var calendar = jQuery(this);
    Tobago.Calendar.initFromDateField(calendar);
  });

  var okButton
      = Tobago.Utils.selectWidthJQuery(elements, ".tobago-calendar").parent().find("button[data-tobago-date-picker-ok]");
  okButton.click(function () {
    var button = jQuery(this);
    var calendar = button.parent().parent().find(".tobago-calendar");
    var time = button.parent().parent().find(".tobago-time");
    Tobago.Calendar.writeIntoField(calendar, time);
    Tobago.Popup.close(button);
  });

};

Tobago.Calendar.MONTH_LENGTH = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

Tobago.Calendar.isLeapYear = function (year) {
  return (year % 4 == 0)
      && (year % 100 != 0 || year % 400 == 0);
};

Tobago.Calendar.getMonthLength = function (month, year) {
  if (month == 2 && Tobago.Calendar.isLeapYear(year)) {
    return 29;
  }
  return Tobago.Calendar.MONTH_LENGTH[month - 1];
};

Tobago.Calendar.getPreviousMonthLength = function (month, year) {
  if (month == 1) {
    return Tobago.Calendar.getMonthLength(12, year - 1);
  } else {
    return Tobago.Calendar.getMonthLength(month - 1, year);
  }
};

Tobago.Calendar.getDayOfWeek = function (day, month, year) {
  var date = new Date();
  date.setDate(day);
  date.setMonth(month - 1);
  date.setFullYear(year);
  // getDay() -> 0 (Su) to 6 (Sa)
  return date.getDay() + 1; // +1 -> Java compatibility
};

Tobago.Calendar.firstDayOffset = function (month, year, firstDayOfWeek) {
  var day = Tobago.Calendar.getDayOfWeek(1, month, year);
  return (day + 7 - firstDayOfWeek) % 7;
};

Tobago.Calendar.getMonthName = function (calendar, month) {
  return calendar.data("tobago-month-names").split(',')[month - 1];
};

/**
 * @param calendar The base element as jQuery object (root of the component).
 */
Tobago.Calendar.initCalendarData = function (calendar, year, month, day) {
  calendar.data("tobago-day", day);
  calendar.data("tobago-month", month);
  calendar.data("tobago-year", year);
  Tobago.Calendar.recalculateValues(calendar);
};

/**
 * @param calendar The base element as jQuery object (root of the component).
 */
Tobago.Calendar.addMonth = function (calendar, monthDelta) {
  var yearDelta = 0;
  if (monthDelta > 0) {
    yearDelta = Math.floor(monthDelta / 12);
    monthDelta = monthDelta % 12;
  } else {
    yearDelta = -Math.floor(-monthDelta / 12);
    monthDelta = -((-monthDelta) % 12);
  }
  var day = calendar.data("tobago-day");
  var month = calendar.data("tobago-month") + monthDelta;
  var year = calendar.data("tobago-year") + yearDelta;
  if (month < 1) {
    month = 12;
    --year;
  } else if (month > 12) {
    month = 1;
    ++year;
  }
  if (day > Tobago.Calendar.getMonthLength(month, year)) {
    day = Tobago.Calendar.getMonthLength(month, year);
  }
  Tobago.Calendar.initCalendarData(calendar, year, month, day);
  Tobago.Calendar.initCalendar(calendar);
};

Tobago.Calendar.recalculateValues = function (calendar) {
  var day = calendar.data("tobago-day");
  var month = calendar.data("tobago-month");
  var year = calendar.data("tobago-year");

  if (month < 1) {
    month += 12;
    --year;
  } else if (month > 12) {
    month -= 12;
    ++year;
  }

  if (day < 1) {
    --month;
    if (month < 1) {
      month += 12;
      --year;
    }
    day += Tobago.Calendar.getMonthLength(month, year);
  } else if (day > Tobago.Calendar.getMonthLength(month, year)) {
    day -= Tobago.Calendar.getMonthLength(month, year);
    ++month;
    if (month > 12) {
      month -= 12;
      ++year;
    }
  }

  calendar.data("tobago-day", day);
  calendar.data("tobago-month", month);
  calendar.data("tobago-year", year);
};

Tobago.Calendar.selectDay = function (calendar, row, column) {
//  alert(id + " "  + row + " " + column);
  var month = calendar.data("tobago-month");
  var year = calendar.data("tobago-year");
  var firstDayOfWeek = calendar.data("tobago-first-day-of-week");
  var firstDayOffset = Tobago.Calendar.firstDayOffset(month, year, firstDayOfWeek);
  Tobago.Calendar.initCalendarData(calendar, year, month, row * 7 + column - firstDayOffset + 1);
  Tobago.Calendar.initCalendar(calendar);
};

Tobago.Calendar.initCalendar = function (calendar) {
  var day = calendar.data("tobago-day");
  var month = calendar.data("tobago-month");
  var year = calendar.data("tobago-year");
  var firstDayOfWeek = calendar.data("tobago-first-day-of-week");
  var firstDayOffset = Tobago.Calendar.firstDayOffset(month, year, firstDayOfWeek);
  var prevMonthLength = Tobago.Calendar.getPreviousMonthLength(month, year);
  var monthLength = Tobago.Calendar.getMonthLength(month, year);

  var monthTitle = calendar.find(".tobago-calendar-header > [data-tobago-command=month]");
  monthTitle.html(Tobago.Calendar.getMonthName(calendar, month));
  var yearTitle = calendar.find(".tobago-calendar-header > [data-tobago-command=year]");
  yearTitle.html(year);

  calendar.find(".tobago-calendar-grid").children(".tobago-calendar-row").each(function (i) {
    var row = jQuery(this);
    row.children(".tobago-calendar-day").each(function (j) {
      var span = jQuery(this);
      var display = (i * 7) + j - firstDayOffset + 1;
      if (display <= 0) {
        span.addClass("tobago-calendar-day-markup-disabled");
        span.removeClass("tobago-calendar-day-markup-selected");
        display += prevMonthLength;
      } else if (display > monthLength) {
        span.addClass("tobago-calendar-day-markup-disabled");
        span.removeClass("tobago-calendar-day-markup-selected");
        display -= monthLength;
      } else {
        span.removeClass("tobago-calendar-day-markup-disabled");
        if (display == day) {
          span.addClass("tobago-calendar-day-markup-selected");
        } else {
          span.removeClass("tobago-calendar-day-markup-selected");
        }
      }
      span.html(display);
    });
  });
};

/**
 * @param element jQuery element with a reference to the date field (with data attribute tobago-date-input-id)
 * @return the date field as jQuery object
 */
Tobago.Calendar.getDateField = function (element) {
  return jQuery(Tobago.Utils.escapeClientId(element.data("tobago-date-input-id")));
};

Tobago.Calendar.initFromDateField = function (calendar) {
  var dateField = Tobago.Calendar.getDateField(calendar);
  var pattern = dateField.data("tobago-pattern");
  var value = dateField.val();
  var date = new Date(getDateFromFormat(value, pattern));
  if (date.getTime() == 0) {
    date = new Date();
  } else if (!pattern.match(/d/)) {
    // Workaround for patterns without day (d) like mm/yyyy (repair the result from parsing)
    date.setDate(date.getDate() + 1);
  }
  var day = date.getDate();
  var month = date.getMonth() + 1;
  var year = date.getFullYear();

  Tobago.Calendar.initCalendarData(calendar, year, month, day);
  Tobago.Calendar.initCalendar(calendar);
};

Tobago.Calendar.writeIntoField = function (calendar, time) {

  var day = calendar.data("tobago-day");
  var month = calendar.data("tobago-month");
  var year = calendar.data("tobago-year");

  var date;
  if (time.length > 0) {
    var hour = Tobago.Time.getValueField(time, "hour");
    var minute = Tobago.Time.getValueField(time, "minute");
    var second = Tobago.Time.getValueField(time, "second");
    date = new Date(year, month - 1, day, hour, minute, second);
  } else {
    date = new Date(year, month - 1, day);
  }
  var dateField = Tobago.Calendar.getDateField(calendar);
  var newValue = formatDate(date, dateField.data("tobago-pattern"));
  if (dateField.val() != newValue) {
    dateField.val(newValue);
    dateField.change();
  }

  dateField.focus();
};

Tobago.registerListener(Tobago.Calendar.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Calendar.init, Tobago.Phase.AFTER_UPDATE);

// --------------------------------------------------------------------------------------------------------------------

Tobago.Time = {};

Tobago.Time.init = function (elements) {

  // time input fields
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-time-input")
      .not("[disabled]").not("[readonly]")
      .focus(function () {
        Tobago.Time.focus(jQuery(this));
      })
      .blur(function () {
        Tobago.Time.blur(jQuery(this));
      })
      .bind("keyup", function (event) {
        Tobago.Time.keyUp(jQuery(this), event);
      });

  // increment button
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-time-incImage")
      .not("[disabled]").not("[readonly]")
      .click(function () {
        Tobago.Time.increment(jQuery(this));
      });

  // decrement button
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-time-decImage")
      .not("[disabled]").not("[readonly]")
      .click(function () {
        Tobago.Time.decrement(jQuery(this));
      });

  // init from data field, if there is any (e. g. we are in date picker popup)
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-time[data-tobago-date-input-id]")
      .each(function () {
        Tobago.Time.initFromDateField(jQuery(this));
      });
};

Tobago.Time.getValueField = function (time, name) {
  var input = Tobago.Time.findElement(time, name);
  return Tobago.Time.getValue(input);
};

Tobago.Time.getValue = function (input) {
  var number = parseInt(input.val(), 10); // use 10 to avoid parsing octal numbers, if the string begins with 0
  return isNaN(number) ? 0 : number;
};

Tobago.Time.setValueField = function (time, name, value) {
  var input = Tobago.Time.findElement(time, name);
  Tobago.Time.setValue(input, value);
};

Tobago.Time.setValue = function (input, value) {
  var max = input.data("tobago-max");
  value = parseInt(value);
  while (value >= max) {
    value -= max;
  }
  while (value < 0) {
    value += max;
  }
  if (value < 10) { // formatting 0#
    value = "0" + value;
  }
  input.val(value);

  Tobago.Time.updateHidden(input);
};
/**
 * Looks for a specific element inside of the time component.
 *
 * @param any jQuery element inside the "time" component.
 * @param suffix id suffix or null for search for the base time element
 */
Tobago.Time.findElement = function (any, suffix) {
  var id = any.attr("id");
  var pos = id.lastIndexOf(Tobago.SUB_COMPONENT_SEP);
  if (pos > -1) {
    id = id.substring(0, pos);
  }
  if (suffix != null) {
    id += Tobago.SUB_COMPONENT_SEP + suffix;
  }
  return jQuery(Tobago.Utils.escapeClientId(id));
};

Tobago.Time.updateHidden = function (anyInput) {
  var time = Tobago.Time.findElement(anyInput, null);

  var hour = Tobago.Time.getValueField(time, "hour");
  var minute = Tobago.Time.getValueField(time, "minute");

  var second = 0;
  if (Tobago.Time.findElement(time, "second").length > 0) {
    second = Tobago.Time.getValueField(time, "second");
  }

  var pattern = time.data("tobago-pattern");
  var value = formatDate(new Date(1970, 1, 1, hour, minute, second), pattern);
  var hidden = Tobago.Time.findElement(time, "field");
  hidden.val(value);
};

Tobago.Time.increment = function (imageButton) {
  var input = Tobago.Time.findCurrent(imageButton);
  Tobago.Time.setValue(input, Tobago.Time.getValue(input) + 1);
  input.focus();
};

Tobago.Time.decrement = function (imageButton) {
  var input = Tobago.Time.findCurrent(imageButton);
  Tobago.Time.setValue(input, Tobago.Time.getValue(input) - 1);
  input.focus();
};

Tobago.Time.findCurrent = function (imageButton) {
  var time = Tobago.Time.findElement(imageButton, null);
  var unit = time.data("tobago-lastactive");
  var input;
  if (unit != null) {
    input = Tobago.Time.findElement(imageButton, unit);
  }
  if (input == null) { // if there is no "last active" then use "hour"
    input = Tobago.Time.findElement(imageButton, "hour");
  }
  return input;
};

Tobago.Time.focus = function (input) {
  // save the last active
  var time = Tobago.Time.findElement(input, null);
  time.data("tobago-lastactive", input.data("tobago-unit"));

  // save the old value
  input.data("tobago-oldvalue", input.val());

  // set a class for glow effects
  time.children(".tobago-time-borderDiv").addClass("tobago-time-borderDiv-markup-focus");
};

Tobago.Time.blur = function (input) {
  var value = parseInt(input.val(), 10); // use 10 to avoid parsing octal numbers, if the string begins with 0
  if (isNaN(value)) {
    value = input.data("tobago-oldvalue");
  }
  Tobago.Time.setValue(input, value);

  var time = Tobago.Time.findElement(input, null);
  time.children(".tobago-time-borderDiv").removeClass("tobago-time-borderDiv-markup-focus");
};

Tobago.Time.initFromDateField = function (time) {
  var dateField = Tobago.Calendar.getDateField(time);

  var pattern = dateField.data("tobago-pattern");
  if (!pattern) { // todo: test it
    pattern = "HH:mm";
  }

  var date = new Date(getDateFromFormat(dateField.val(), pattern));
  if (date.getTime() == 0) {
    date = new Date();
  }

  Tobago.Time.setValueField(time, "hour", date.getHours());
  Tobago.Time.setValueField(time, "minute", date.getMinutes());
  if (pattern.indexOf("s") > -1) {
    Tobago.Time.setValueField(time, "second", date.getSeconds());
  }
};

Tobago.Time.keyUp = function (input, event) {
  switch (event.keyCode) {
    case 38:
      Tobago.Time.setValue(input, Tobago.Time.getValue(input) + 1);
      break;
    case 40:
      Tobago.Time.setValue(input, Tobago.Time.getValue(input) - 1);
      break;
  }
};

Tobago.registerListener(Tobago.Time.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Time.init, Tobago.Phase.AFTER_UPDATE);


Tobago.DateTime = {};

Tobago.DateTime.init = function (elements) {

  // time input fields
//  jQuery.datepicker.setDefaults(jQuery.datepicker.regional['de']);

  Tobago.Utils.selectWidthJQuery(elements, ".tobago-date, .tobago-time")
      .not("[disabled]")
      .not("[readonly]")
      .each(function () {
        var date = jQuery(this);
        date.width(date.width() - 5 - 16); // reserve space for the picker.

        var analyzed = Tobago.DateTime.analyzePattern(date.data("tobago-pattern"));
        var options = {
          showOn: "button",
          buttonImageOnly: true,
// tbd          changeMonth: true,
// tbd          changeYear: true,
          showAnim: "", // just show it directly
          showOtherMonths: true,
          selectOtherMonths: true
        };
        var icon = date.data("tobago-date-time-icon");
        if (icon) {
          options.buttonImage = icon;
        }
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

        var buttonImage = date.next("img");
        buttonImage.css({
          position: "absolute",
          left: parseInt(date.css("left")) + date.outerWidth(true) + 5 + "px",
          top: date.css("top")
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
