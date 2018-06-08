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
  let number1Fn = jQueryFrameFn("#page\\:mainForm\\:selectNum1 input");
  let number2Fn = jQueryFrameFn("#page\\:mainForm\\:selectNum2 input");
  let submitAddFn = jQueryFrameFn("#page\\:mainForm\\:submitAdd");
  let outputFn = jQueryFrameFn("#page\\:mainForm\\:resultOutput span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    number1Fn().eq(0).prop("checked", false);
    number1Fn().eq(1).prop("checked", true); // Select 2
    number1Fn().eq(2).prop("checked", false);
    number2Fn().eq(0).prop("checked", false);
    number2Fn().eq(1).prop("checked", false);
    number2Fn().eq(2).prop("checked", true); // Select 4
    submitAddFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "6");
  });
  TTT.startTest();
});

QUnit.test("submit: Subtraction (4 - 1)", function (assert) {
  let number1Fn = jQueryFrameFn("#page\\:mainForm\\:selectNum1 input");
  let number2Fn = jQueryFrameFn("#page\\:mainForm\\:selectNum2 input");
  let submitSubFn = jQueryFrameFn("#page\\:mainForm\\:submitSub");
  let outputFn = jQueryFrameFn("#page\\:mainForm\\:resultOutput span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    number1Fn().eq(0).prop("checked", false);
    number1Fn().eq(1).prop("checked", false);
    number1Fn().eq(2).prop("checked", true); // Select 4
    number2Fn().eq(0).prop("checked", true); // Select 1
    number2Fn().eq(1).prop("checked", false);
    number2Fn().eq(2).prop("checked", false);
    submitSubFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "3");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Mars", function (assert) {
  let planetFn = jQueryFrameFn("#page\\:mainForm\\:selectPlanet input");
  let moonsFn = jQueryFrameFn("#page\\:mainForm\\:moonradio label.form-check-label");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    planetFn().eq(0).prop("checked", false);
    planetFn().eq(2).prop("checked", false);
    planetFn().eq(1).prop("checked", true).trigger("change"); // Mars.
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(moonsFn().eq(0).text(), "Phobos");
    assert.equal(moonsFn().eq(1).text(), "Deimos");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Jupiter", function (assert) {
  let planetFn = jQueryFrameFn("#page\\:mainForm\\:selectPlanet input");
  let moonsFn = jQueryFrameFn("#page\\:mainForm\\:moonradio label.form-check-label");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    planetFn().eq(0).prop("checked", false);
    planetFn().eq(1).prop("checked", false);
    planetFn().eq(2).prop("checked", true).trigger("change"); // Jupiter.
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal(moonsFn().eq(0).text(), "Europa");
    assert.equal(moonsFn().eq(1).text(), "Ganymed");
    assert.equal(moonsFn().eq(2).text(), "Io");
    assert.equal(moonsFn().eq(3).text(), "Kallisto");
  });
  TTT.startTest();
});
