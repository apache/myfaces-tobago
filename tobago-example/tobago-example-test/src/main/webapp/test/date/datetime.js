TestDateTime = {};

TestDateTime.init = function () {

  var panel = jQuery("#page\\:jquery_date_time_pattern");
  var inputs = panel.children(".tobago-in, .tobago-date, .tobago-time");

  for (var i = 0; i < inputs.length; i += 5) {
    var javaPattern = inputs.eq(i);
    var javaFormatted = inputs.eq(i + 1);
    var jQueryUIDatePattern = inputs.eq(i + 2);
    var jQueryUITimePattern = inputs.eq(i + 3);
    var jQueryUIFormatted = inputs.eq(i + 4);
    var result = jQueryUIFormatted.next();

    var pattern = javaFormatted.data("tobago-pattern");
    javaPattern.val(pattern);

    var analyzed = Tobago.DateTime.analyzePattern(pattern);

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
          jQueryValue
              = jQuery.datepicker.formatDate(analyzed.dateFormat, date, i18n)
              + analyzed.separator
              + jQuery.datepicker.formatTime(analyzed.timeFormat, time, i18n);
          break;
        case "date":
          date = jQuery.datepicker.parseDate(analyzed.dateFormat, javaValue, i18n);
          jQueryValue = jQuery.datepicker.formatDate(analyzed.dateFormat, date, i18n);
          break;
        case "time":
          time = jQuery.datepicker.parseTime(analyzed.timeFormat, javaValue, i18n);
          jQueryValue = jQuery.datepicker.formatTime(analyzed.timeFormat, time, i18n);
          break;
        default:
          console.error("invalid: not date-pattern nor time-pattern");  // @DEV_ONLY
          jQueryValue = "no type";
      }
      jQueryUIFormatted.val(jQueryValue);

    } catch (e) {
      console.error(e);
    }

    if (javaFormatted.val() != jQueryUIFormatted.val()) {
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

TestDateTime.test = function () {
  TestDateTime.testDate();
  TestDateTime.testTime();
  TestDateTime.testBoth();
};

TestDateTime.testDate = function () {

  var date;
  var format = "dd.mm.yy";
  var initial = "13.05.2014";
  var result;

  date = jQuery.datepicker.parseDate(format, initial);
  result = jQuery.datepicker.formatDate(format, date);

  if (result != initial) {
    console.error("Error!");
  } else {
    console.info("Okay!");
  }

};

TestDateTime.testTime = function () {

  var date;
  var format = "HH:mm:ss";
  var initial = "14:06:55";
  var result;

  date = jQuery.datepicker.parseTime(format, initial);
  result = jQuery.datepicker.formatTime(format, date);

  if (result != initial) {
    console.error("Error!");
  } else {
    console.info("Okay!");
  }

};

TestDateTime.testBoth = function () {

  var date, dateT;
  var formatD = "dd.mm.yy";
  var formatT = "HH:mm:ss";
  var initial = "13.05.2014 13:05:55";
  var result;
  var separator = " ";

  date = jQuery.datepicker.parseDateTime(formatD, formatT, initial);
  dateT = {
    hour: date.getHours(),
    minute: date.getMinutes(),
    second: date.getSeconds(),
    millisec: date.getMilliseconds(),
    microsec: date.getMicroseconds()
  };
  result = jQuery.datepicker.formatDate(formatD, date) + separator + jQuery.datepicker.formatTime(formatT, dateT);

  if (result != initial) {
    console.error("Error!");
  } else {
    console.info("Okay!");
  }

};

Tobago.registerListener(TestDateTime.init, Tobago.Phase.DOCUMENT_READY);

Tobago.registerListener(TestDateTime.test, Tobago.Phase.DOCUMENT_READY);
