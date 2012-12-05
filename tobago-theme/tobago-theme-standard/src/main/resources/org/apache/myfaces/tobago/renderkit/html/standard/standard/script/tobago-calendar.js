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
  var calendarWithDateField = Tobago.Utils.selectWidthJQuery(elements, ".tobago-calendar[data-tobago-dateinputid]");
  calendarWithDateField.each(function () {
    var calendar = jQuery(this);
    Tobago.Calendar.initFromDateField(calendar);
  });

  var okButton
      = Tobago.Utils.selectWidthJQuery(elements, ".tobago-calendar").parent().find("button[data-tobago-datepickerok]");
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
  return calendar.data("tobago-monthnames").split(',')[month - 1];
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
  var firstDayOfWeek = calendar.data("tobago-firstdayofweek");
  var firstDayOffset = Tobago.Calendar.firstDayOffset(month, year, firstDayOfWeek);
  Tobago.Calendar.initCalendarData(calendar, year, month, row * 7 + column - firstDayOffset + 1);
  Tobago.Calendar.initCalendar(calendar);
};

Tobago.Calendar.initCalendar = function (calendar) {
  var day = calendar.data("tobago-day");
  var month = calendar.data("tobago-month");
  var year = calendar.data("tobago-year");
  var firstDayOfWeek = calendar.data("tobago-firstdayofweek");
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
 * @param element jQuery element with a reference to the date field (with data attribute tobago-dateinputid)
 * @return the date field as jQuery object
 */
Tobago.Calendar.getDateField = function (element) {
  return jQuery(Tobago.Utils.escapeClientId(element.data("tobago-dateinputid")));
};

Tobago.Calendar.initFromDateField = function (calendar) {
  var dateField = Tobago.Calendar.getDateField(calendar);
  var pattern = dateField.data("tobago-pattern");
  var value = dateField.attr("value");
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
  Tobago.Utils.selectWidthJQuery(elements, ".tobago-time[data-tobago-dateinputid]")
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
