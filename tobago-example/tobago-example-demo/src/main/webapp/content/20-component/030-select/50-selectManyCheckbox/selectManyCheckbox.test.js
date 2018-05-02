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

QUnit.test("submit: select cat", function (assert) {
  var $animals = jQueryFrameFn("#page\\:mainForm\\:animals input");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:submit");
  var $output = jQueryFrameFn("#page\\:mainForm\\:animalsOutput span");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $animals().eq(0).prop("checked", true);
    $animals().eq(1).prop("checked", false);
    $animals().eq(2).prop("checked", false);
    $animals().eq(3).prop("checked", false);
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Cat ");
  });
  TTT.startTest();
});

QUnit.test("submit: select fox and rabbit", function (assert) {
  var $animals = jQueryFrameFn("#page\\:mainForm\\:animals input");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:submit");
  var $output = jQueryFrameFn("#page\\:mainForm\\:animalsOutput span");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $animals().eq(0).prop("checked", false);
    $animals().eq(1).prop("checked", false);
    $animals().eq(2).prop("checked", true);
    $animals().eq(3).prop("checked", true);
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "Fox Rabbit ");
  });
  TTT.startTest();
});

QUnit.test("ajax: click 'Two'", function (assert) {
  var $number2 = jQueryFrameFn("#page\\:mainForm\\:numbers\\:\\:1");
  var $output = jQueryFrameFn("#page\\:mainForm\\:resultOutput span");
  var newOutputValue;

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    if ($number2().prop("checked")) {
      newOutputValue = parseInt($output().text()) - 2;
      $number2().prop("checked", false).trigger("change");
    } else {
      newOutputValue = parseInt($output().text()) + 2;
      $number2().prop("checked", true).trigger("change");
    }
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), newOutputValue);
  });
  TTT.startTest();
});

QUnit.test("ajax: click 'Three'", function (assert) {
  var $number3 = jQueryFrameFn("#page\\:mainForm\\:numbers\\:\\:2");
  var $output = jQueryFrameFn("#page\\:mainForm\\:resultOutput span");
  var newOutputValue;

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    if ($number3().prop("checked")) {
      newOutputValue = parseInt($output().text()) - 3;
      $number3().prop("checked", false).trigger("change");
    } else {
      newOutputValue = parseInt($output().text()) + 3;
      $number3().prop("checked", true).trigger("change");
    }
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), newOutputValue);
  });
  TTT.startTest();
});

QUnit.test("ajax: click 'Two'", function (assert) {
  var $number2 = jQueryFrameFn("#page\\:mainForm\\:numbers\\:\\:1");
  var $output = jQueryFrameFn("#page\\:mainForm\\:resultOutput span");
  var newOutputValue;

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    if ($number2().prop("checked")) {
      newOutputValue = parseInt($output().text()) - 2;
      $number2().prop("checked", false).trigger("change");
    } else {
      newOutputValue = parseInt($output().text()) + 2;
      $number2().prop("checked", true).trigger("change");
    }
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), newOutputValue);
  });
  TTT.startTest();
});
