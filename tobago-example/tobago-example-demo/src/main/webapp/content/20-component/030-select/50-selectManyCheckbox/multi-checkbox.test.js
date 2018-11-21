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
  var animalsFn = jQueryFrameFn("#page\\:mainForm\\:animals input");
  var submitFn = jQueryFrameFn("#page\\:mainForm\\:submit");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:animalsOutput span");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    animalsFn().eq(0).prop("checked", true);
    animalsFn().eq(1).prop("checked", false);
    animalsFn().eq(2).prop("checked", false);
    animalsFn().eq(3).prop("checked", false);
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Cat ");
  });
  TTT.startTest();
});

QUnit.test("submit: select fox and rabbit", function (assert) {
  var animalsFn = jQueryFrameFn("#page\\:mainForm\\:animals input");
  var submitFn = jQueryFrameFn("#page\\:mainForm\\:submit");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:animalsOutput span");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    animalsFn().eq(0).prop("checked", false);
    animalsFn().eq(1).prop("checked", false);
    animalsFn().eq(2).prop("checked", true);
    animalsFn().eq(3).prop("checked", true);
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "Fox Rabbit ");
  });
  TTT.startTest();
});

QUnit.test("ajax: click 'Two'", function (assert) {
  var number2Fn = jQueryFrameFn("#page\\:mainForm\\:numbers\\:\\:1");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:resultOutput span");
  var newOutputValue;

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    if (number2Fn().prop("checked")) {
      newOutputValue = parseInt(outputFn().text()) - 2;
      number2Fn().prop("checked", false).trigger("change");
    } else {
      newOutputValue = parseInt(outputFn().text()) + 2;
      number2Fn().prop("checked", true).trigger("change");
    }
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), newOutputValue);
  });
  TTT.startTest();
});

QUnit.test("ajax: click 'Three'", function (assert) {
  var number3Fn = jQueryFrameFn("#page\\:mainForm\\:numbers\\:\\:2");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:resultOutput span");
  var newOutputValue;

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    if (number3Fn().prop("checked")) {
      newOutputValue = parseInt(outputFn().text()) - 3;
      number3Fn().prop("checked", false).trigger("change");
    } else {
      newOutputValue = parseInt(outputFn().text()) + 3;
      number3Fn().prop("checked", true).trigger("change");
    }
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), newOutputValue);
  });
  TTT.startTest();
});

QUnit.test("ajax: click 'Two'", function (assert) {
  var number2Fn = jQueryFrameFn("#page\\:mainForm\\:numbers\\:\\:1");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:resultOutput span");
  var newOutputValue;

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    if (number2Fn().prop("checked")) {
      newOutputValue = parseInt(outputFn().text()) - 2;
      number2Fn().prop("checked", false).trigger("change");
    } else {
      newOutputValue = parseInt(outputFn().text()) + 2;
      number2Fn().prop("checked", true).trigger("change");
    }
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), newOutputValue);
  });
  TTT.startTest();
});
