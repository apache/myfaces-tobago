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

QUnit.test("submit: Addition (2 + 4)", function (assert) {
  var $number1 = jQueryFrameFn("#page\\:mainForm\\:selectNum1 input");
  var $number2 = jQueryFrameFn("#page\\:mainForm\\:selectNum2 input");
  var $submitAdd = jQueryFrameFn("#page\\:mainForm\\:submitAdd");
  var $output = jQueryFrameFn("#page\\:mainForm\\:resultOutput span");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $number1().eq(0).prop("checked", false);
    $number1().eq(1).prop("checked", true); // Select 2
    $number1().eq(2).prop("checked", false);
    $number2().eq(0).prop("checked", false);
    $number2().eq(1).prop("checked", false);
    $number2().eq(2).prop("checked", true); // Select 4
    $submitAdd().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "6");
  });
  TTT.startTest();
});

QUnit.test("submit: Subtraction (4 - 1)", function (assert) {
  var $number1 = jQueryFrameFn("#page\\:mainForm\\:selectNum1 input");
  var $number2 = jQueryFrameFn("#page\\:mainForm\\:selectNum2 input");
  var $submitSub = jQueryFrameFn("#page\\:mainForm\\:submitSub");
  var $output = jQueryFrameFn("#page\\:mainForm\\:resultOutput span");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $number1().eq(0).prop("checked", false);
    $number1().eq(1).prop("checked", false);
    $number1().eq(2).prop("checked", true); // Select 4
    $number2().eq(0).prop("checked", true); // Select 1
    $number2().eq(1).prop("checked", false);
    $number2().eq(2).prop("checked", false);
    $submitSub().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($output().text(), "3");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Mars", function (assert) {
  var $planet = jQueryFrameFn("#page\\:mainForm\\:selectPlanet input");
  var $moons = jQueryFrameFn("#page\\:mainForm\\:moonradio label.form-check-label");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $planet().eq(0).prop("checked", false);
    $planet().eq(2).prop("checked", false);
    $planet().eq(1).prop("checked", true).trigger("change"); // Mars.
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($moons().eq(0).text(), "Phobos");
    assert.equal($moons().eq(1).text(), "Deimos");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Jupiter", function (assert) {
  var $planet = jQueryFrameFn("#page\\:mainForm\\:selectPlanet input");
  var $moons = jQueryFrameFn("#page\\:mainForm\\:moonradio label.form-check-label");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $planet().eq(0).prop("checked", false);
    $planet().eq(1).prop("checked", false);
    $planet().eq(2).prop("checked", true).trigger("change"); // Jupiter.
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal($moons().eq(0).text(), "Europa");
    assert.equal($moons().eq(1).text(), "Ganymed");
    assert.equal($moons().eq(2).text(), "Io");
    assert.equal($moons().eq(3).text(), "Kallisto");
  });
  TTT.startTest();
});
