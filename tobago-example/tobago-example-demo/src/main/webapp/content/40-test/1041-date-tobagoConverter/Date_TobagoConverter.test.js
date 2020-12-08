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

import {querySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("Date - f:convertDateTime", function (assert) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:myfacesDateInput\\:\\:field");
  let datepickerButtonFn = querySelectorFn("#page\\:mainForm\\:myfacesDateInput .datepickerbutton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:myfacesDateOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "dd.MM.yyyy HH:mm:ss", true, true,
      "14.12.2018 12:34:56", "14.12.2018 12:34:56");
});

QUnit.test("Date - tc:convertDateTime", function (assert) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:tobagoDateInput\\:\\:field");
  let datepickerButtonFn = querySelectorFn("#page\\:mainForm\\:tobagoDateInput .datepickerbutton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:tobagoDateOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "dd.MM.yyyy HH:mm:ss", true, true,
      "14.12.2018 12:34:56", "14.12.2018 12:34:56");
});

QUnit.test("Calendar - tc:convertDateTime", function (assert) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:tobagoDateInput\\:\\:field");
  let datepickerButtonFn = querySelectorFn("#page\\:mainForm\\:tobagoDateInput .datepickerbutton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:tobagoDateOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "dd.MM.yyyy HH:mm:ss", true, true,
      "14.12.2018 12:34:56", "14.12.2018 12:34:56");
});

QUnit.test("LocalDate - f:convertDateTime", function (assert) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:myfacesLocalDateInput\\:\\:field");
  let datepickerButtonFn = querySelectorFn("#page\\:mainForm\\:myfacesLocalDateInput .datepickerbutton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:myfacesLocalDateOutput .tobago-out");

  if (inputFn() !== null) {
    testConvertDateTime(assert,
        inputFn, datepickerButtonFn, outputFn,
        "dd.MM.yyyy", true, false,
        "14.12.2018", "14.12.2018");
  } else {
    assert.ok(true, "MyFaces 2.3 not activated");
  }
});

QUnit.test("LocalDate - tc:convertDateTime", function (assert) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:tobagoLocalDateInput\\:\\:field");
  let datepickerButtonFn = querySelectorFn("#page\\:mainForm\\:tobagoLocalDateInput .datepickerbutton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:tobagoLocalDateOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "dd.MM.yyyy", true, false,
      "14.12.2018", "14.12.2018");
});

QUnit.test("LocalTime - f:convertDateTime", function (assert) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:myfacesLocalTimeInput\\:\\:field");
  let datepickerButtonFn = querySelectorFn("#page\\:mainForm\\:myfacesLocalTimeInput .datepickerbutton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:myfacesLocalTimeOutput .tobago-out");

  if (inputFn() !== null) {
    testConvertDateTime(assert,
        inputFn, datepickerButtonFn, outputFn,
        "HH:mm:ss", false, true,
        "12:34:56", "12:34:56");
  } else {
    assert.ok(true, "MyFaces 2.3 not activated");
  }
});

QUnit.test("LocalTime - tc:convertDateTime", function (assert) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:tobagoLocalTimeInput\\:\\:field");
  let datepickerButtonFn = querySelectorFn("#page\\:mainForm\\:tobagoLocalTimeInput .datepickerbutton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:tobagoLocalTimeOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "HH:mm:ss", false, true,
      "12:34:56", "12:34:56");
});

QUnit.test("LocalDateTime - f:convertDateTime", function (assert) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:myfacesLocalDateTimeInput\\:\\:field");
  let datepickerButtonFn = querySelectorFn("#page\\:mainForm\\:myfacesLocalDateTimeInput .datepickerbutton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:myfacesLocalDateTimeOutput .tobago-out");

  if (inputFn() !== null) {
    testConvertDateTime(assert,
        inputFn, datepickerButtonFn, outputFn,
        "dd.MM.yyyy HH:mm:ss", true, true,
        "14.12.2018 12:34:56", "14.12.2018 12:34:56");
  } else {
    assert.ok(true, "MyFaces 2.3 not activated");
  }
});

QUnit.test("LocalDateTime - tc:convertDateTime", function (assert) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:tobagoLocalDateTimeInput\\:\\:field");
  let datepickerButtonFn = querySelectorFn("#page\\:mainForm\\:tobagoLocalDateTimeInput .datepickerbutton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:tobagoLocalDateTimeOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "dd.MM.yyyy HH:mm:ss", true, true,
      "14.12.2018 12:34:56", "14.12.2018 12:34:56");
});

QUnit.test("OffsetTime - f:convertDateTime", function (assert) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:myfacesOffsetTimeInput\\:\\:field");
  let datepickerButtonFn = querySelectorFn("#page\\:mainForm\\:myfacesOffsetTimeInput .datepickerbutton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:myfacesOffsetTimeOutput .tobago-out");

  if (inputFn() !== null) {
    testConvertDateTime(assert,
        inputFn, datepickerButtonFn, outputFn,
        "HH:mm:ss Z", false, true,
        "12:34:56 +0200", "12:34:56");
  } else {
    assert.ok(true, "MyFaces 2.3 not activated");
  }
});

QUnit.test("OffsetTime - tc:convertDateTime", function (assert) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:tobagoOffsetTimeInput\\:\\:field");
  let datepickerButtonFn = querySelectorFn("#page\\:mainForm\\:tobagoOffsetTimeInput .datepickerbutton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:tobagoOffsetTimeOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "HH:mm:ss Z", false, true,
      "12:34:56 +0200", "12:34:56");
});

QUnit.test("OffsetDateTime - f:convertDateTime", function (assert) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:myfacesOffsetDateTimeInput\\:\\:field");
  let datepickerButtonFn = querySelectorFn("#page\\:mainForm\\:myfacesOffsetDateTimeInput .datepickerbutton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:myfacesOffsetDateTimeOutput .tobago-out");

  if (inputFn() !== null) {
    testConvertDateTime(assert,
        inputFn, datepickerButtonFn, outputFn,
        "dd.MM.yyyy HH:mm:ss Z", true, true,
        "14.12.2018 12:34:56 +0200", "14.12.2018 12:34:56");
  } else {
    assert.ok(true, "MyFaces 2.3 not activated");
  }
});

QUnit.test("OffsetDateTime - tc:convertDateTime", function (assert) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:tobagoOffsetDateTimeInput\\:\\:field");
  let datepickerButtonFn = querySelectorFn("#page\\:mainForm\\:tobagoOffsetDateTimeInput .datepickerbutton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:tobagoOffsetDateTimeOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "dd.MM.yyyy HH:mm:ss Z", true, true,
      "14.12.2018 12:34:56 +0200", "14.12.2018 12:34:56");
});

QUnit.test("ZonedDateTime - f:convertDateTime", function (assert) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:myfacesZonedDateTimeInput\\:\\:field");
  let datepickerButtonFn = querySelectorFn("#page\\:mainForm\\:myfacesZonedDateTimeInput .datepickerbutton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:myfacesZonedDateTimeOutput .tobago-out");

  if (inputFn() !== null) {
    testConvertDateTime(assert,
        inputFn, datepickerButtonFn, outputFn,
        "dd.MM.yyyy HH:mm:ss Z", true, true,
        "14.12.2018 12:34:56 +0200", "14.12.2018 12:34:56");
  } else {
    assert.ok(true, "MyFaces 2.3 not activated");
  }
});

QUnit.test("ZonedDateTime - tc:convertDateTime", function (assert) {
  let inputFn = querySelectorFn("#page\\:mainForm\\:tobagoZonedDateTimeInput\\:\\:field");
  let datepickerButtonFn = querySelectorFn("#page\\:mainForm\\:tobagoZonedDateTimeInput .datepickerbutton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:tobagoZonedDateTimeOutput .tobago-out");

  testConvertDateTime(assert,
      inputFn, datepickerButtonFn, outputFn,
      "dd.MM.yyyy HH:mm:ss Z", true, true,
      "14.12.2018 12:34:56 +0200", "14.12.2018 12:34:56");
});

function testConvertDateTime(assert, inputFn, datepickerButtonFn, outputFn,
                             expectPattern, expectCalendarIcon, expectClockIcon,
                             inputValue, expectOutputValue) {
  let submitButtonFn = querySelectorFn("#page\\:mainForm\\:submitButton");

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(3, function () {
    assert.equal(inputFn().dataset.tobagoPattern, expectPattern);
    assert.equal(datepickerButtonFn().querySelector(".fa-calendar") !== null, expectCalendarIcon);
    assert.equal(datepickerButtonFn().querySelector(".fa-clock-o") !== null, expectClockIcon);
  });
  TTT.action(function () {
        inputFn().value = inputValue;
        submitButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
      }
  );
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, expectOutputValue);
  });
  TTT.startTest();
}
