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

function getToday($dateField) {
  var tobagoToday = $dateField().data("tobago-today");
  var todayArray = tobagoToday.split("-");
  return todayArray[2] + "." + todayArray[1] + "." + todayArray[0];
}

QUnit.test("date with label", function (assert) {
  assert.expect(5);

  var $label = jQueryFrameFn("#page\\:mainForm\\:dNormal > label");
  var $dateField = jQueryFrameFn("#page\\:mainForm\\:dNormal\\:\\:field");
  var $dateButton = jQueryFrameFn("#page\\:mainForm\\:dNormal button");
  var $dayToday = jQueryFrameFn(".day.today");
  var today = getToday($dateField);

  assert.equal($label().text(), "Date");
  assert.equal($dateField().val(), today);

  $dateField().val("32.05.2016");
  $dateButton().click();

  assert.equal($dateField().val(), today);
  assert.equal($dayToday().hasClass("past"), false);
  assert.equal($dayToday().prevAll(".past").length, $dayToday().prevAll().length);

  $dateButton().click(); // IE11: close datetimepicker for next test
});

QUnit.test("date+time pattern", function (assert) {
  var $dateButton = jQueryFrameFn("#page\\:mainForm\\:dateTimePattern .datepickerbutton");
  var $datepicker = jQueryFrameFn(".bootstrap-datetimepicker-widget");
  var $firstLi = jQueryFrameFn(".bootstrap-datetimepicker-widget .list-unstyled li:first-child");
  var $lastLi = jQueryFrameFn(".bootstrap-datetimepicker-widget .list-unstyled li:last-child");
  var $togglePickerButton = jQueryFrameFn(".bootstrap-datetimepicker-widget .picker-switch a");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $dateButton().click();
  });
  TTT.asserts(3, function () {
    assert.equal($datepicker().length, 1);
    assert.notEqual($firstLi().css("display"), "none"); //block
    assert.equal($lastLi().css("display"), "none");
  });
  TTT.action(function () {
    $togglePickerButton().click();
  });
  TTT.waitMs(1000); // wait for animation
  TTT.asserts(2, function () {
    assert.equal($firstLi().css("display"), "none");
    assert.notEqual($lastLi().css("display"), "none"); //block
  });
  TTT.action(function () {
    $dateButton().click(); // IE11: close datetimepicker for next test
  });
  TTT.startTest();
});

QUnit.test("submit", function (assert) {
  var $dateField = jQueryFrameFn("#page\\:mainForm\\:formSubmit\\:input\\:\\:field");
  var $dateButton = jQueryFrameFn("#page\\:mainForm\\:formSubmit\\:input button");
  var $outField = jQueryFrameFn("#page\\:mainForm\\:formSubmit\\:output span");
  var $submitButton = jQueryFrameFn("#page\\:mainForm\\:formSubmit\\:button");
  var $widget = jQueryFrameFn(".bootstrap-datetimepicker-widget");
  var $days = jQueryFrameFn(".bootstrap-datetimepicker-widget .day");
  var day22 = 0;

  var TTT = new TobagoTestTools(assert);
  TTT.asserts(2, function () {
    assert.equal($dateField().val(), "22.05.2016");
    assert.equal($outField().text(), "22.05.2016");
  });
  TTT.action(function () {
    $dateButton().click();
  });
  TTT.asserts(2, function () {
    assert.ok($widget().get(0), ".bootstrap-datetimepicker-widget should be available");

    for (i = 0; i < $days().length; i++) {
      if ($days().eq(i).text() === "22") {
        day22 = i;
        break;
      }
    }
    assert.ok($days().get(day22 + 10));
  });
  TTT.action(function () {
    $days().get(day22 + 10).click(); // Choose '01.06.2016'.
  });
  TTT.asserts(1, function () {
    assert.equal($dateField().val(), "01.06.2016");
  });
  TTT.action(function () {
    $submitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($outField().text(), "01.06.2016");
  });
  TTT.startTest();
});

QUnit.test("ajax", function (assert) {
  var $dateField = jQueryFrameFn("#page\\:mainForm\\:ajaxinput\\:\\:field");
  var $dateButton = jQueryFrameFn("#page\\:mainForm\\:ajaxinput button");
  var $outField = jQueryFrameFn("#page\\:mainForm\\:outputfield span");
  var $widget = jQueryFrameFn(".bootstrap-datetimepicker-widget");
  var today = getToday($dateField);

  var TTT = new TobagoTestTools(assert);
  TTT.asserts(2, function () {
    assert.equal($dateField().val(), "");
    assert.equal($outField().text(), "");
  });
  TTT.action(function () {
    $dateButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.ok($widget().get(0));
    assert.equal($dateField().val(), today);
    assert.equal($outField().text(), today);
  });
  TTT.startTest();
});
