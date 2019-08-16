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

import {testFrameQuerySelectorAllFn, testFrameQuerySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("submit: Addition (2 + 4)", function (assert) {
  let number1Fn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:selectNum1 input");
  let number2Fn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:selectNum2 input");
  let submitAddFn = testFrameQuerySelectorFn("#page\\:mainForm\\:submitAdd");
  let outputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:resultOutput span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    number1Fn().item(0).checked = false;
    number1Fn().item(1).checked = true; // Select 2
    number1Fn().item(2).checked = false;
    number2Fn().item(0).checked = false;
    number2Fn().item(1).checked = false;
    number2Fn().item(2).checked = true; // Select 4
    submitAddFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "6");
  });
  TTT.startTest();
});

QUnit.test("submit: Subtraction (4 - 1)", function (assert) {
  let number1Fn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:selectNum1 input");
  let number2Fn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:selectNum2 input");
  let submitSubFn = testFrameQuerySelectorFn("#page\\:mainForm\\:submitSub");
  let outputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:resultOutput span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    number1Fn().item(0).checked = false;
    number1Fn().item(1).checked = false;
    number1Fn().item(2).checked = true; // Select 4
    number2Fn().item(0).checked = true; // Select 1
    number2Fn().item(1).checked = false;
    number2Fn().item(2).checked = false;
    submitSubFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "3");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Mars", function (assert) {
  let planetFn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:selectPlanet input");
  let moonsFn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:moonradio label.form-check-label");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    planetFn().item(0).checked = false;
    planetFn().item(2).checked = false;
    planetFn().item(1).checked = true; // Mars.
    planetFn().item(1).dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(moonsFn().item(0).textContent, "Phobos");
    assert.equal(moonsFn().item(1).textContent, "Deimos");
  });
  TTT.startTest();
});

QUnit.test("ajax: select Jupiter", function (assert) {
  let planetFn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:selectPlanet input");
  let moonsFn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:moonradio label.form-check-label");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    planetFn().item(0).checked = false;
    planetFn().item(1).checked = false;
    planetFn().item(2).checked = true; // Jupiter.
    planetFn().item(2).dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal(moonsFn().item(0).textContent, "Europa");
    assert.equal(moonsFn().item(1).textContent, "Ganymed");
    assert.equal(moonsFn().item(2).textContent, "Io");
    assert.equal(moonsFn().item(3).textContent, "Kallisto");
  });
  TTT.startTest();
});
