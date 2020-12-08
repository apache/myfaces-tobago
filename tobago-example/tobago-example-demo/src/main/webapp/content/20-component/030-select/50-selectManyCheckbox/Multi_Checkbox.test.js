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

import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("not implemented yet", function (done) {
  let test = new JasmineTestTool(done);
  test.do(() => fail("not implemented yet"));
  test.start();
});

/*
import {querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("submit: select cat", function (assert) {
  let animalsFn = querySelectorAllFn("#page\\:mainForm\\:animals input");
  let submitFn = querySelectorFn("#page\\:mainForm\\:submit");
  let outputFn = querySelectorFn("#page\\:mainForm\\:animalsOutput span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    animalsFn().item(0).checked = true;
    animalsFn().item(1).checked = false;
    animalsFn().item(2).checked = false;
    animalsFn().item(3).checked = false;
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Cat ");
  });
  TTT.startTest();
});

QUnit.test("submit: select fox and rabbit", function (assert) {
  let animalsFn = querySelectorAllFn("#page\\:mainForm\\:animals input");
  let submitFn = querySelectorFn("#page\\:mainForm\\:submit");
  let outputFn = querySelectorFn("#page\\:mainForm\\:animalsOutput span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    animalsFn().item(0).checked = false;
    animalsFn().item(1).checked = false;
    animalsFn().item(2).checked = true;
    animalsFn().item(3).checked = true;
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "Fox Rabbit ");
  });
  TTT.startTest();
});

QUnit.test("ajax: click 'Two'", function (assert) {
  let number2Fn = querySelectorFn("#page\\:mainForm\\:numbers\\:\\:1");
  let outputFn = querySelectorFn("#page\\:mainForm\\:resultOutput span");
  let newOutputValue;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    if (number2Fn().checked) {
      newOutputValue = parseInt(outputFn().textContent) - 2;
      number2Fn().checked = false;
      number2Fn().dispatchEvent(new Event("change", {bubbles: true}));
    } else {
      newOutputValue = parseInt(outputFn().textContent) + 2;
      number2Fn().checked = true;
      number2Fn().dispatchEvent(new Event("change", {bubbles: true}));
    }
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, newOutputValue);
  });
  TTT.startTest();
});

QUnit.test("ajax: click 'Three'", function (assert) {
  let number3Fn = querySelectorFn("#page\\:mainForm\\:numbers\\:\\:2");
  let outputFn = querySelectorFn("#page\\:mainForm\\:resultOutput span");
  let newOutputValue;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    if (number3Fn().checked) {
      newOutputValue = parseInt(outputFn().textContent) - 3;
      number3Fn().checked = false;
      number3Fn().dispatchEvent(new Event("change", {bubbles: true}));
    } else {
      newOutputValue = parseInt(outputFn().textContent) + 3;
      number3Fn().checked = true;
      number3Fn().dispatchEvent(new Event("change", {bubbles: true}));
    }
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, newOutputValue);
  });
  TTT.startTest();
});

QUnit.test("ajax: click 'Two'", function (assert) {
  let number2Fn = querySelectorFn("#page\\:mainForm\\:numbers\\:\\:1");
  let outputFn = querySelectorFn("#page\\:mainForm\\:resultOutput span");
  let newOutputValue;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    if (number2Fn().checked) {
      newOutputValue = parseInt(outputFn().textContent) - 2;
      number2Fn().checked = false;
      number2Fn().dispatchEvent(new Event("change", {bubbles: true}));
    } else {
      newOutputValue = parseInt(outputFn().textContent) + 2;
      number2Fn().checked = true;
      number2Fn().dispatchEvent(new Event("change", {bubbles: true}));
    }
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, newOutputValue);
  });
  TTT.startTest();
});
*/
