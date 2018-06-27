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

QUnit.test("Date - f:convertDateTime", function (assert) {
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:myfacesDateInput\\:\\:field");
  var datepickerButtonFn = jQueryFrameFn("#page\\:mainForm\\:myfacesDateInput .datepickerbutton");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:myfacesDateOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "dd.MM.yyyy HH:mm:ss", true, true,
      "14.12.2018 12:34:56", "14.12.2018 12:34:56");
});

QUnit.test("Date - tc:convertDateTime", function (assert) {
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoDateInput\\:\\:field");
  var datepickerButtonFn = jQueryFrameFn("#page\\:mainForm\\:tobagoDateInput .datepickerbutton");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoDateOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "dd.MM.yyyy HH:mm:ss", true, true,
      "14.12.2018 12:34:56", "14.12.2018 12:34:56");
});

QUnit.test("Calendar - tc:convertDateTime", function (assert) {
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoDateInput\\:\\:field");
  var datepickerButtonFn = jQueryFrameFn("#page\\:mainForm\\:tobagoDateInput .datepickerbutton");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoDateOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "dd.MM.yyyy HH:mm:ss", true, true,
      "14.12.2018 12:34:56", "14.12.2018 12:34:56");
});

QUnit.test("LocalDate - f:convertDateTime", function (assert) {
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:myfacesLocalDateInput\\:\\:field");
  var datepickerButtonFn = jQueryFrameFn("#page\\:mainForm\\:myfacesLocalDateInput .datepickerbutton");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:myfacesLocalDateOutput .tobago-out");

  if (inputFn().length > 0) {
    testConvertDateTime(assert,
        inputFn, datepickerButtonFn, outputFn,
        "dd.MM.yyyy", true, false,
        "14.12.2018", "14.12.2018");
  } else {
    assert.ok(true, "MyFaces 2.3 not activated");
  }
});

QUnit.test("LocalDate - tc:convertDateTime", function (assert) {
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoLocalDateInput\\:\\:field");
  var datepickerButtonFn = jQueryFrameFn("#page\\:mainForm\\:tobagoLocalDateInput .datepickerbutton");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoLocalDateOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "dd.MM.yyyy", true, false,
      "14.12.2018", "14.12.2018");
});

QUnit.test("LocalTime - f:convertDateTime", function (assert) {
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:myfacesLocalTimeInput\\:\\:field");
  var datepickerButtonFn = jQueryFrameFn("#page\\:mainForm\\:myfacesLocalTimeInput .datepickerbutton");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:myfacesLocalTimeOutput .tobago-out");

  if (inputFn().length > 0) {
    testConvertDateTime(assert,
        inputFn, datepickerButtonFn, outputFn,
        "HH:mm:ss", false, true,
        "12:34:56", "12:34:56");
  } else {
    assert.ok(true, "MyFaces 2.3 not activated");
  }
});

QUnit.test("LocalTime - tc:convertDateTime", function (assert) {
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoLocalTimeInput\\:\\:field");
  var datepickerButtonFn = jQueryFrameFn("#page\\:mainForm\\:tobagoLocalTimeInput .datepickerbutton");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoLocalTimeOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "HH:mm:ss", false, true,
      "12:34:56", "12:34:56");
});

QUnit.test("LocalDateTime - f:convertDateTime", function (assert) {
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:myfacesLocalDateTimeInput\\:\\:field");
  var datepickerButtonFn = jQueryFrameFn("#page\\:mainForm\\:myfacesLocalDateTimeInput .datepickerbutton");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:myfacesLocalDateTimeOutput .tobago-out");

  if (inputFn().length > 0) {
    testConvertDateTime(assert,
        inputFn, datepickerButtonFn, outputFn,
        "dd.MM.yyyy HH:mm:ss", true, true,
        "14.12.2018 12:34:56", "14.12.2018 12:34:56");
  } else {
    assert.ok(true, "MyFaces 2.3 not activated");
  }
});

QUnit.test("LocalDateTime - tc:convertDateTime", function (assert) {
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoLocalDateTimeInput\\:\\:field");
  var datepickerButtonFn = jQueryFrameFn("#page\\:mainForm\\:tobagoLocalDateTimeInput .datepickerbutton");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoLocalDateTimeOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "dd.MM.yyyy HH:mm:ss", true, true,
      "14.12.2018 12:34:56", "14.12.2018 12:34:56");
});

QUnit.test("OffsetTime - f:convertDateTime", function (assert) {
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:myfacesOffsetTimeInput\\:\\:field");
  var datepickerButtonFn = jQueryFrameFn("#page\\:mainForm\\:myfacesOffsetTimeInput .datepickerbutton");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:myfacesOffsetTimeOutput .tobago-out");

  if (inputFn().length > 0) {
    testConvertDateTime(assert,
        inputFn, datepickerButtonFn, outputFn,
        "HH:mm:ss Z", false, true,
        "12:34:56 +0200", "12:34:56");
  } else {
    assert.ok(true, "MyFaces 2.3 not activated");
  }
});

QUnit.test("OffsetTime - tc:convertDateTime", function (assert) {
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoOffsetTimeInput\\:\\:field");
  var datepickerButtonFn = jQueryFrameFn("#page\\:mainForm\\:tobagoOffsetTimeInput .datepickerbutton");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoOffsetTimeOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "HH:mm:ss Z", false, true,
      "12:34:56 +0200", "12:34:56");
});

QUnit.test("OffsetDateTime - f:convertDateTime", function (assert) {
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:myfacesOffsetDateTimeInput\\:\\:field");
  var datepickerButtonFn = jQueryFrameFn("#page\\:mainForm\\:myfacesOffsetDateTimeInput .datepickerbutton");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:myfacesOffsetDateTimeOutput .tobago-out");

  if (inputFn().length > 0) {
    testConvertDateTime(assert,
        inputFn, datepickerButtonFn, outputFn,
        "dd.MM.yyyy HH:mm:ss Z", true, true,
        "14.12.2018 12:34:56 +0200", "14.12.2018 12:34:56");
  } else {
    assert.ok(true, "MyFaces 2.3 not activated");
  }
});

QUnit.test("OffsetDateTime - tc:convertDateTime", function (assert) {
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoOffsetDateTimeInput\\:\\:field");
  var datepickerButtonFn = jQueryFrameFn("#page\\:mainForm\\:tobagoOffsetDateTimeInput .datepickerbutton");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoOffsetDateTimeOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "dd.MM.yyyy HH:mm:ss Z", true, true,
      "14.12.2018 12:34:56 +0200", "14.12.2018 12:34:56");
});

QUnit.test("ZonedDateTime - f:convertDateTime", function (assert) {
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:myfacesZonedDateTimeInput\\:\\:field");
  var datepickerButtonFn = jQueryFrameFn("#page\\:mainForm\\:myfacesZonedDateTimeInput .datepickerbutton");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:myfacesZonedDateTimeOutput .tobago-out");

  if (inputFn().length > 0) {
    testConvertDateTime(assert,
        inputFn, datepickerButtonFn, outputFn,
        "dd.MM.yyyy HH:mm:ss Z", true, true,
        "14.12.2018 12:34:56 +0200", "14.12.2018 12:34:56");
  } else {
    assert.ok(true, "MyFaces 2.3 not activated");
  }
});

QUnit.test("ZonedDateTime - tc:convertDateTime", function (assert) {
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoZonedDateTimeInput\\:\\:field");
  var datepickerButtonFn = jQueryFrameFn("#page\\:mainForm\\:tobagoZonedDateTimeInput .datepickerbutton");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:tobagoZonedDateTimeOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "dd.MM.yyyy HH:mm:ss Z", true, true,
      "14.12.2018 12:34:56 +0200", "14.12.2018 12:34:56");
});

function testConvertDateTime(assert, inputFn, datepickerButtonFn, outputFn,
                             expectPattern, expectCalendarIcon, expectClockIcon,
                             inputValue, expectOutputValue) {
  var submitButtonFn = jQueryFrameFn("#page\\:mainForm\\:submitButton");

  var TTT = new TobagoTestTool(assert);
  TTT.asserts(3, function () {
    assert.equal(inputFn().data("tobago-pattern"), expectPattern);
    assert.equal(datepickerButtonFn().find(".fa-calendar").length, expectCalendarIcon);
    assert.equal(datepickerButtonFn().find(".fa-clock-o").length, expectClockIcon);
  });
  TTT.action(function () {
    inputFn().val(inputValue);
    submitButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), expectOutputValue);
  });
  TTT.startTest();
}
