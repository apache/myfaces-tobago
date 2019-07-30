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

import {jQueryFrameFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

function getToday(dateFieldFn) {
  var tobagoToday = dateFieldFn().data("tobago-today");
  var todayArray = tobagoToday.split("-");
  return todayArray[2] + "." + todayArray[1] + "." + todayArray[0];
}

QUnit.test("date with label", function (assert) {
  assert.expect(5);

  var labelFn = jQueryFrameFn("#page\\:mainForm\\:dNormal > label");
  var dateFieldFn = jQueryFrameFn("#page\\:mainForm\\:dNormal\\:\\:field");
  var dateButtonFn = jQueryFrameFn("#page\\:mainForm\\:dNormal button");
  var dayTodayFn = jQueryFrameFn(".day.today");
  var today = getToday(dateFieldFn);

  assert.equal(labelFn().text(), "Date");
  assert.equal(dateFieldFn().val(), today);

  dateFieldFn().val("32.05.2016");
  dateButtonFn().click();

  assert.equal(dateFieldFn().val(), today);
  assert.equal(dayTodayFn().hasClass("past"), false);
  assert.equal(dayTodayFn().prevAll(".past").length, dayTodayFn().prevAll().length);

  dateButtonFn().click(); // IE11: close datetimepicker for next test
});

QUnit.test("date+time pattern", function (assert) {
  var dateButtonFn = jQueryFrameFn("#page\\:mainForm\\:dateTimePattern .datepickerbutton");
  var datepickerFn = jQueryFrameFn(".bootstrap-datetimepicker-widget");
  var firstLiFn = jQueryFrameFn(".bootstrap-datetimepicker-widget .list-unstyled li:first-child");
  var lastLiFn = jQueryFrameFn(".bootstrap-datetimepicker-widget .list-unstyled li:last-child");
  var togglePickerButtonFn = jQueryFrameFn(".bootstrap-datetimepicker-widget .picker-switch a");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    dateButtonFn().click();
  });
  TTT.asserts(3, function () {
    assert.equal(datepickerFn().length, 1);
    assert.notEqual(firstLiFn().css("display"), "none"); //block
    assert.equal(lastLiFn().css("display"), "none");
  });
  TTT.action(function () {
    togglePickerButtonFn().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.equal(firstLiFn().css("display"), "none");
    assert.notEqual(lastLiFn().css("display"), "none"); //block
  });
  TTT.action(function () {
    dateButtonFn().click(); // IE11: close datetimepicker for next test
  });
  TTT.startTest();
});

QUnit.test("submit", function (assert) {
  var dateFieldFn = jQueryFrameFn("#page\\:mainForm\\:formSubmit\\:input\\:\\:field");
  var dateButtonFn = jQueryFrameFn("#page\\:mainForm\\:formSubmit\\:input button");
  var outFieldFn = jQueryFrameFn("#page\\:mainForm\\:formSubmit\\:output span");
  var submitButtonFn = jQueryFrameFn("#page\\:mainForm\\:formSubmit\\:button");
  var widgetFn = jQueryFrameFn(".bootstrap-datetimepicker-widget");
  var daysFn = jQueryFrameFn(".bootstrap-datetimepicker-widget .day");
  var day22 = 0;

  var TTT = new TobagoTestTool(assert);
  TTT.asserts(2, function () {
    assert.equal(dateFieldFn().val(), "22.05.2016");
    assert.equal(outFieldFn().text(), "22.05.2016");
  });
  TTT.action(function () {
    dateButtonFn().click();
  });
  TTT.asserts(2, function () {
    assert.ok(widgetFn().get(0), ".bootstrap-datetimepicker-widget should be available");

    for (let i = 0; i < daysFn().length; i++) {
      if (daysFn().eq(i).text() === "22") {
        day22 = i;
        break;
      }
    }
    assert.ok(daysFn().get(day22 + 10));
  });
  TTT.action(function () {
    daysFn().get(day22 + 10).click(); // Choose '01.06.2016'.
  });
  TTT.asserts(1, function () {
    assert.equal(dateFieldFn().val(), "01.06.2016");
  });
  TTT.action(function () {
    submitButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outFieldFn().text(), "01.06.2016");
  });
  TTT.startTest();
});

QUnit.test("ajax", function (assert) {
  var dateFieldFn = jQueryFrameFn("#page\\:mainForm\\:ajaxinput\\:\\:field");
  var dateButtonFn = jQueryFrameFn("#page\\:mainForm\\:ajaxinput button");
  var outFieldFn = jQueryFrameFn("#page\\:mainForm\\:outputfield span");
  var widgetFn = jQueryFrameFn(".bootstrap-datetimepicker-widget");
  var today = getToday(dateFieldFn);

  var TTT = new TobagoTestTool(assert);
  TTT.asserts(2, function () {
    assert.equal(dateFieldFn().val(), "");
    assert.equal(outFieldFn().text(), "");
  });
  TTT.action(function () {
    dateButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.ok(widgetFn().get(0));
    assert.equal(dateFieldFn().val(), today);
    assert.equal(outFieldFn().text(), today);
  });
  TTT.startTest();
});
