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

if (!Calendar) {
  var Calendar = new Object;
}

Calendar.MONTH_LENGTH = [31,28,31,30,31,30,31,31,30,31,30,31];

Calendar.isLeapYear = function (year) {
  return (year%4 == 0) 
      && (year%100 != 0 || year%400 == 0);
}

Calendar.getMonthLength = function (month, year) {
  if (month == 2 && Calendar.isLeapYear(year)) {
    return 29;
  }
  return Calendar.MONTH_LENGTH[month - 1];
}  

Calendar.getPreviousMonth = function (month, year) {
  return (month == 1) ? 12 : month - 1;
}  

Calendar.getDayOfWeek = function(day, month, year) {
  var date = new Date();
  date.setDate(day);
  date.setMonth(month - 1);
  date.setFullYear(year);
  // getDay() -> 0 (Su) to 6 (Sa)
  return date.getDay() + 1; // +1 -> Java compatibility
}

Calendar.firstDayOffset = function (month, year, firstDayOfWeek) {
  var day = Calendar.getDayOfWeek(1, month, year);
  return (day + 7 - firstDayOfWeek) % 7;
}

Calendar._weekCount = function (days) {
  return Math.floor(((days + (7 - 1)) / 7));
}

Calendar.weekCount = function (month, year, firstDayOfWeek) {
  var firstDayOffset = Calendar.firstDayOffset(month, year, firstDayOfWeek);
  var daysInMonth = Calendar.getMonthLength(month, year);
  return Calendar._weekCount(firstDayOffset + daysInMonth);
}

// -- calendar control -----------------------------------------------

// XXX make page global
function getFirstDayOfWeek(id) {
  return parseInt(document.getElementById(id + ":firstDayOfWeek").value, 10);
}

// XXX make page global
function getMonthName(id, month) {
  return document.getElementById(id + ":monthNames").value.split(',')[month-1];
}

function initCalendarData(id, year, month, day) {
  document.getElementById(id + ":day").value = day;
  document.getElementById(id + ":month").value = month;
  document.getElementById(id + ":year").value = year;
  recalculateValues(id);
}

function addMonth(id, monthDelta) {
  var yearDelta = 0;
  if (monthDelta > 0) {
    yearDelta = Math.floor(monthDelta / 12);
    monthDelta = monthDelta % 12;
  } else {
    yearDelta = -Math.floor(-monthDelta / 12);
    monthDelta = -((-monthDelta) % 12);
  }
  var year = parseInt(document.getElementById(id + ":year").value, 10) + yearDelta;
  var month = parseInt(document.getElementById(id + ":month").value, 10) + monthDelta;
  var day = parseInt(document.getElementById(id + ":day").value);
  if (month < 1) {
    month = 12;
    --year;
  } else if (month > 12) {
    month = 1;
    ++year;
  }
  if (day > Calendar.getMonthLength(month, year)) {
    day = Calendar.getMonthLength(month, year);
  }
  initCalendarData(id, year, month, day);
  initCalendar(id);
}

function recalculateValues(id) {
  var day = parseInt(document.getElementById(id + ":day").value);
  var month = parseInt(document.getElementById(id + ":month").value);
  var year = parseInt(document.getElementById(id + ":year").value);
//  alert("before " + day + "." + month + "." + year);

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
    day += Calendar.getMonthLength(month, year);
  } else if (day > Calendar.getMonthLength(month, year)) {
    day -= Calendar.getMonthLength(month, year);
    ++month;
    if (month > 12) {
      month -= 12;
      ++year;
    }
  }

//  alert("after " + day + "." + month + "." + year);

  document.getElementById(id + ":day").value = day;
  document.getElementById(id + ":month").value = month;
  document.getElementById(id + ":year").value = year;
}

function selectDay(id, row, column) {
//  alert(id + " "  + row + " " + column);
  var month = document.getElementById(id + ":month").value;
  var year = document.getElementById(id + ":year").value;
  var firstDayOfWeek = getFirstDayOfWeek(id);
  var firstDayOffset = Calendar.firstDayOffset(month, year, firstDayOfWeek);
  initCalendarData(id, year, month, row * 7 + column - firstDayOffset + 1);
  initCalendar(id);
}

function initCalendar(id) {
  var day = parseInt(document.getElementById(id + ":day").value);
  var month = parseInt(document.getElementById(id + ":month").value);
  var year = parseInt(document.getElementById(id + ":year").value);
  var firstDayOfWeek = getFirstDayOfWeek(id);
  var firstDayOffset = Calendar.firstDayOffset(month, year, firstDayOfWeek);
//  var weekCount = Calendar.weekCount(month, year, firstDayOfWeek);
  var prevMonthLength = Calendar.getMonthLength(
      Calendar.getPreviousMonth(month, year), year);
  var monthLength = Calendar.getMonthLength(month, year);
  // alert(month + "/" + year + " weekCount: " + weekCount + "; firstDayOffset: " + firstDayOffset);
  var title = document.getElementById(id + ":title");
  title.innerHTML = getMonthName(id, month) + " " + year;
  for (var week = 0; week < 6; ++week) {
    var row = document.getElementById(id + ":" + week);
//    if (week < weekCount) {
      row.style.display = "";
      for (var column = 0; column < 7; ++column) {
        var el = document.getElementById(id + ":" + week + ":" + column);
        var d = (week * 7) + column - firstDayOffset + 1;
        var styleClass = "tobago-calendar-day";
        var monthAfterClick = month;
        if (d <= 0) {
          d = prevMonthLength + d;
          styleClass = "tobago-calendar-day-disabled";
          monthAfterClick = month - 1;
        } else if (d > monthLength) {
          styleClass = "tobago-calendar-day-disabled";
          d -= monthLength;
          monthAfterClick = month + 1;
        } else if (d == day) {
          styleClass = "tobago-calendar-day-selected";
        }
        el.className = styleClass;
        el.innerHTML = d;
      }
//    } else {
//      row.style.display = "none";
//    }
  }
}

function initCalendarParse(id, textBoxId) {
  var textBox = document.getElementById(textBoxId);
  document.getElementById(id + ":fieldId").value = textBoxId;
  var patternField = document.getElementById(textBoxId + ":converterPattern");
  if (patternField) {
    document.calendar.formatPattern = patternField.value;
  } else {
    document.calendar.formatPattern = "yyyy-MM-dd";
  }

  var string = textBox.value;
  var date = new Date(getDateFromFormat(string, document.calendar.formatPattern));
  if (date.getTime() == 0) {
    date = new Date();
  } else if (! document.calendar.formatPattern.match(/d/)) {
    // XXX Workaround for patterns without day (d) like mm/yyyy (repair the result from parsing)
    date.setDate(date.getDate() + 1);
  }
  var day = date.getDate();
  var month = date.getMonth() + 1;
  var year = date.getFullYear();

  initCalendarData(id, year, month, day);
  initCalendar(id);
}

function writeIntoField2(obj) {
  var id = obj.id;
  var index = id.lastIndexOf(':');
  return writeIntoField(obj, id.substring(0, index));
}


function writeIntoField(obj, id) {
  var textBoxId = document.getElementById(id + ":calendar:fieldId");
  var textBox = document.getElementById(textBoxId.value);

  var day = parseInt(document.getElementById(id + ":calendar:day").value);
  var month = parseInt(document.getElementById(id + ":calendar:month").value);
  var year = parseInt(document.getElementById(id + ":calendar:year").value);

  var idPrefix = id + ":time" + Tobago.SUB_COMPONENT_SEP;
  var hour = document.getElementById(idPrefix + "hour");
  if (hour) {
    hour = parseInt(hour.value, 10);
  } else {
    hour = 0;
  }
  var minute = document.getElementById(idPrefix + "minute");
  if (minute) {
    minute = parseInt(minute.value, 10);
  } else {
    minute = 0;
  }
  var second = document.getElementById(idPrefix + "second");
  if (second) {
    second = parseInt(second.value, 10);
  } else {
    second = 0;
  }
  var date = new Date(year, month - 1, day, hour, minute, second);

  Tobago.closePopup(obj)

  textBox.focus();
  
  var newValue =  formatDate(date, document.calendar.formatPattern);
  if (textBox.value != newValue) {
    textBox.value = newValue;
    Tobago.raiseEvent("change", textBox);
  }
}
// ------------------------------------------------------------------

function tbgGetTimeInput(imageButton) {
  if (imageButton.parentNode.selectedId) {
    input = document.getElementById(imageButton.parentNode.selectedId);
  } else {
    var id = imageButton.id.substring(0, imageButton.id.lastIndexOf(":"));
    input = document.getElementById(id + ":hour");
  }
  return input;
}

function tbgSetTimeInputValue(input, value) {

  value = parseInt(value);
//  LOG.debug("value = " + value);
  if (input.parentNode.parentNode.hourMode) {
    if (value < 0) {
      value = 23;
    } else if (value > 23) { // TODO: 12/24 hour mode ?
      value = 0;
    }
  } else {
    if (value < 0) {
      value = 59;
    } else if (value > 59) {
      value = 0;
    }
  }
  if (value < 10) {
    value = "0" + value;
  }
  input.value = value;
  var id = input.id.substring(0,input.id.lastIndexOf(Tobago.SUB_COMPONENT_SEP));
  var hidden = document.getElementById(id);
  if (hidden) {
     var idPrefix = id + Tobago.SUB_COMPONENT_SEP;
     var hour = document.getElementById(idPrefix + "hour");
    if (hour) {
      hour = parseInt(hour.value, 10);
      if (hour < 10) {
        hour = "0" + hour;
      }
    } else {
      hour = "00";
    }
    var minute = document.getElementById(idPrefix + "minute");
    if (minute) {
      minute = parseInt(minute.value, 10);
      if (minute < 10) {
        minute = "0" + minute;
      }
    } else {
      minute = "00";
    }
    hidden.value = hour + ":" + minute;

    var second = document.getElementById(idPrefix + "second");
    if (second) {
      second = parseInt(second.value, 10);
      if (second < 10) {
        second = "0" + second;
      }
      hidden.value = hidden.value + ":" + second;
    }
  }
//  LOG.debug("value 2 = " + input.value);
}

function tbgDecTime(imageButton, hour) {
  var input = tbgGetTimeInput(imageButton);
  tbgSetTimeInputValue(input, input.value - 1);
  input.focus();
}
function tbgIncTime(imageButton, hour) {
  var input = tbgGetTimeInput(imageButton);
  tbgSetTimeInputValue(input, input.value - 0 + 1)
  input.focus();
}

function tbgTimerInputFocus(input, hour) {
//  LOG.debug("focus " + input.id + " hourMode=" + hour);
  input.parentNode.parentNode.selectedId = input.id;
  input.parentNode.parentNode.hourMode = hour;
  Tobago.addCssClass(input, "tobago-time-input-selected");
  input.oldValue = input.value;
}

function tbgTimerInputBlur(input) {
//  LOG.debug("value XX = " + input.value);
  var value = parseInt(input.value, 10); // use 10 to avoid parsing octal numbers, if the string begins with 0
//  LOG.debug("value 3  = " + value);
  if (isNaN(value)) {
    input.value = input.oldValue;
    return;
  }
  if (input.parentNode.parentNode.hourMode) {
    if (value > 23 || value < 0) {
      value = input.oldValue;
    }
  } else {
    if (value > 59 || value < 0) {
      value = input.oldValue;
    }
  }
  tbgSetTimeInputValue(input, value)
  Tobago.removeCssClass(input, "tobago-time-input-selected");
//  LOG.debug("value  4= " + input.value);
}

function tbgInitTimeParse(timeId, dateId) {
  var time = document.getElementById(dateId);
  if (time) {

    var patternField = document.getElementById(dateId + ":converterPattern");
    var formatPattern;
    if (patternField) {
      formatPattern = patternField.value;
    } else {
      formatPattern = "HH:mm";
    }


    var date = new Date(getDateFromFormat(time.value, formatPattern));
    if (date.getTime() == 0) {
      date = new Date();
    }
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var seconds = date.getSeconds();

    LOG.debug("init time :" + hours + ":" + minutes + ":" + seconds);
    tbgInitTimeData(timeId, hours, minutes, seconds);
  }
}

function tbgInitTimeData(id, hours, minutes, seconds) {
  var element = document.getElementById(id);
  if (element) {
    var idPrefix = id + Tobago.SUB_COMPONENT_SEP;
    var hourField = document.getElementById(idPrefix + "hour");
    if (hourField) {
      hourField.value = hours < 10 ? "0" + hours : hours;
    }
    var minuteField = document.getElementById(idPrefix + "minute");
    if (minuteField) {
      minuteField.value = minutes < 10 ? "0" + minutes : minutes;
    }
    var secondsField = document.getElementById(idPrefix + "second");
    if (secondsField) {
      secondsField.value = seconds < 10 ? "0" + seconds : seconds;
    }
  }
}

function tbgTimerKeyUp(inputElement, event) {
  if (! event) {
    event = window.event;
  }
  switch (event.keyCode) {
    case 38:
      tbgSetTimeInputValue(inputElement, inputElement.value - 0 + 1);
      break;
    case 40:
      tbgSetTimeInputValue(inputElement, inputElement.value - 1);
      break;
  }
}


