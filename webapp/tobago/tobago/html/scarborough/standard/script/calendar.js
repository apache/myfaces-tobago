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

function initCalendarDataX(id, year, month, day) {
  document.getElementById(id + ":day").value = day;
  document.getElementById(id + ":month").value = month;
  document.getElementById(id + ":year").value = year;
  recalculateValues(id);
}

function initCalendarData(id, month, year) {
  document.getElementById(id + ":month").value = month;
  document.getElementById(id + ":year").value = year;
  recalculateValues(id);
}

function addMonth(id, monthDelta) {
  var calendar = document.getElementById(id);
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
  if (month < 1) {
    month = 12;
    --year;
  } else if (month > 12) {
    month = 1;
    ++year;
  }
  initCalendarData(id, month, year);
  initCalendar(id);
}

function recalculateValues(id) {
  var day = parseInt(document.getElementById(id + ":day").value);
  var month = parseInt(document.getElementById(id + ":month").value);
  var year = parseInt(document.getElementById(id + ":year").value);
//  alert("before " + day + "." + month + "." + year);
  if (month < 1) {
    month = 12;
    --year;
  } else if (month > 12) {
    month = 1;
    ++year;
  }

  if (day < 1) {
    --month;
    day += Calendar.getMonthLength(month, year);
  } else if (day > Calendar.getMonthLength(month, year)) {
    day -= Calendar.getMonthLength(month, year);
    ++month;
  }

  if (month < 1) {
    month = 12;
    --year;
  } else if (month > 12) {
    month = 1;
    ++year;
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
  initCalendarX(id, year, month, row * 7 + column - firstDayOffset + 1);
  initCalendar(id);
}

function initCalendarX(id, year, month, day) {
//  alert(id + " "  + year + " "  + month + " "  + day);
  initCalendarDataX(id, year, month, day);
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
        var styleClass = "day";
        var monthAfterClick = month;
        if (d <= 0) {
          d = prevMonthLength + d;
          styleClass = "day-disabled";
          monthAfterClick = month - 1;
        } else if (d > monthLength) {
          styleClass = "day-disabled";
          d -= monthLength;
          monthAfterClick = month + 1;
        } else if (d == day) {
          styleClass = "day-selected";
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
  var textBox = window.opener.document.getElementById(textBoxId);
  document.getElementById(id + ":fieldId").value = textBoxId;
  var patternField = window.opener.document.getElementById(textBoxId + ":converterPattern");
  if (patternField) {
    document.calendar.formatPattern = patternField.value;
  } else {
    document.calendar.formatPattern = "yyyy-MM-dd";
  }

  var string = textBox.value;
  var date = new Date(getDateFromFormat(string, document.calendar.formatPattern));
  var day = date.getDate();
  var month = date.getMonth() + 1;
  var year = date.getFullYear();

  initCalendarDataX(id, year, month, day);
  initCalendar(id);
}

function writeIntoField(id) {
  var textBoxId = document.getElementById(id + ":fieldId");
  var textBox = window.opener.document.getElementById(textBoxId.value);

  var day = parseInt(document.getElementById(id + ":day").value);
  var month = parseInt(document.getElementById(id + ":month").value);
  var year = parseInt(document.getElementById(id + ":year").value);
  var date = new Date(year, month - 1, day);
  textBox.value = formatDate(date, document.calendar.formatPattern);
  alert(document.calendar.formatPattern);
}
