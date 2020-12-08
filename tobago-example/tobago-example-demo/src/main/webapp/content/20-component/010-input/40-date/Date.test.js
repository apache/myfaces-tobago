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

import {querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("not implemented yet", function (done) {
  let test = new JasmineTestTool(done);
  test.do(() => fail("not implemented yet"));
  test.start();
});
/*

function getToday(dateFieldFn) {
  let tobagoToday = dateFieldFn().dataset.tobagoToday;
  let todayArray = tobagoToday.split("-");
  return todayArray[2] + "." + todayArray[1] + "." + todayArray[0];
}

QUnit.test("date with label", function (assert) {
  assert.expect(5);

  let labelFn = querySelectorFn("#page\\:mainForm\\:dNormal > label");
  let dateFieldFn = querySelectorFn("#page\\:mainForm\\:dNormal\\:\\:field");
  let dateButtonFn = querySelectorFn("#page\\:mainForm\\:dNormal button");
  let dayTodayFn = querySelectorFn(".day.today");
  let today = getToday(dateFieldFn);

  assert.equal(labelFn().textContent, "Date");
  assert.equal(dateFieldFn().value, today);

  dateFieldFn().value = "32.05.2016";
  dateButtonFn().dispatchEvent(new Event("click", {bubbles: true}));

  assert.equal(dateFieldFn().value, today);
  assert.notOk(dayTodayFn().classList.contains("past"));
  if (dayTodayFn().previousElementSibling !== null) {
    assert.ok(dayTodayFn().previousElementSibling.classList.contains("past"));
  } else {
    assert.notOk(dayTodayFn().nextElementSibling.classList.contains("past"));
  }

  dateButtonFn().dispatchEvent(new Event("click", {bubbles: true})); // IE11: close datetimepicker for next test
});

QUnit.test("date+time pattern", function (assert) {
  let dateButtonFn = querySelectorFn("#page\\:mainForm\\:dateTimePattern .datepickerbutton");
  let datepickerFn = querySelectorFn(".bootstrap-datetimepicker-widget");
  let firstLiFn = querySelectorFn(".bootstrap-datetimepicker-widget .list-unstyled li:first-child");
  let lastLiFn = querySelectorFn(".bootstrap-datetimepicker-widget .list-unstyled li:last-child");
  let togglePickerButtonFn = querySelectorFn(".bootstrap-datetimepicker-widget .picker-switch a");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    dateButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(3, function () {
    assert.ok(datepickerFn() !== null);
    assert.notEqual(getComputedStyle(firstLiFn()).display, "none"); //block
    assert.equal(getComputedStyle(lastLiFn()).display, "none");
  });
  TTT.action(function () {
    togglePickerButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.equal(getComputedStyle(firstLiFn()).display, "none");
    assert.notEqual(getComputedStyle(lastLiFn()).display, "none"); //block
  });
  TTT.action(function () {
    dateButtonFn().dispatchEvent(new Event("click", {bubbles: true})); // IE11: close datetimepicker for next test
  });
  TTT.startTest();
});

QUnit.test("submit", function (assert) {
  let dateFieldFn = querySelectorFn("#page\\:mainForm\\:formSubmit\\:input\\:\\:field");
  let dateButtonFn = querySelectorFn("#page\\:mainForm\\:formSubmit\\:input button");
  let outFieldFn = querySelectorFn("#page\\:mainForm\\:formSubmit\\:output span");
  let submitButtonFn = querySelectorFn("#page\\:mainForm\\:formSubmit\\:button");
  let widgetFn = querySelectorAllFn(".bootstrap-datetimepicker-widget");
  let daysFn = querySelectorAllFn(".bootstrap-datetimepicker-widget .day");
  let day22 = 0;

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(2, function () {
    assert.equal(dateFieldFn().value, "22.05.2016");
    assert.equal(outFieldFn().textContent, "22.05.2016");
  });
  TTT.action(function () {
    dateButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(2, function () {
    assert.ok(widgetFn().item(0), ".bootstrap-datetimepicker-widget should be available");

    for (let i = 0; i < daysFn().length; i++) {
      if (daysFn().item(i).textContent === "22") {
        day22 = i;
        break;
      }
    }
    assert.ok(daysFn().item(day22 + 10));
  });
  TTT.action(function () {
    daysFn().item(day22 + 10).dispatchEvent(new Event("click", {bubbles: true})); // Choose '01.06.2016'.
  });
  TTT.asserts(1, function () {
    assert.equal(dateFieldFn().value, "01.06.2016");
  });
  TTT.action(function () {
    submitButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outFieldFn().textContent, "01.06.2016");
  });
  TTT.startTest();
});

QUnit.test("ajax", function (assert) {
  let dateFieldFn = querySelectorFn("#page\\:mainForm\\:ajaxinput\\:\\:field");
  let dateButtonFn = querySelectorFn("#page\\:mainForm\\:ajaxinput button");
  let outFieldFn = querySelectorFn("#page\\:mainForm\\:outputfield span");
  let widgetFn = querySelectorAllFn(".bootstrap-datetimepicker-widget");
  let today = getToday(dateFieldFn);

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(2, function () {
    assert.equal(dateFieldFn().value, "");
    assert.equal(outFieldFn().textContent, "");
  });
  TTT.action(function () {
    dateButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.ok(widgetFn().item(0));
    assert.equal(dateFieldFn().value, today);
    assert.equal(outFieldFn().textContent, today);
  });
  TTT.startTest();
});
*/
