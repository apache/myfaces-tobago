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

QUnit.test("submit: select A", function (assert) {
  let selectAFn = jQueryFrameFn("#page\\:mainForm\\:selectA input");
  let selectBFn = jQueryFrameFn("#page\\:mainForm\\:selectB input");
  let selectCFn = jQueryFrameFn("#page\\:mainForm\\:selectC input");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:submit");
  let outputFn = jQueryFrameFn("#page\\:mainForm\\:submitOutput span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    selectAFn().prop("checked", true);
    selectBFn().prop("checked", false);
    selectCFn().prop("checked", false);
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "A ");
  });
  TTT.startTest();
});

QUnit.test("submit: select B and C", function (assert) {
  let selectAFn = jQueryFrameFn("#page\\:mainForm\\:selectA input");
  let selectBFn = jQueryFrameFn("#page\\:mainForm\\:selectB input");
  let selectCFn = jQueryFrameFn("#page\\:mainForm\\:selectC input");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:submit");
  let outputFn = jQueryFrameFn("#page\\:mainForm\\:submitOutput span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    selectAFn().prop("checked", false);
    selectBFn().prop("checked", true);
    selectCFn().prop("checked", true);
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "B C ");
  });
  TTT.startTest();
});

QUnit.test("ajax: select D", function (assert) {
  let selectDFn = jQueryFrameFn("#page\\:mainForm\\:selectD input");
  let outputDFn = jQueryFrameFn("#page\\:mainForm\\:outputD span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    selectDFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputDFn().text(), "true");
  });
  TTT.startTest();
});

QUnit.test("ajax: deselect D", function (assert) {
  let selectDFn = jQueryFrameFn("#page\\:mainForm\\:selectD input");
  let outputDFn = jQueryFrameFn("#page\\:mainForm\\:outputD span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    selectDFn().prop("checked", false).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputDFn().text(), "false");
  });
  TTT.startTest();
});

QUnit.test("ajax: select E", function (assert) {
  let selectEFn = jQueryFrameFn("#page\\:mainForm\\:selectE input");
  let outputEFn = jQueryFrameFn("#page\\:mainForm\\:outputE span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    selectEFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputEFn().text(), "true");
  });
  TTT.startTest();
});

QUnit.test("ajax: deselect E", function (assert) {
  let selectEFn = jQueryFrameFn("#page\\:mainForm\\:selectE input");
  let outputEFn = jQueryFrameFn("#page\\:mainForm\\:outputE span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    selectEFn().prop("checked", false).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputEFn().text(), "false");
  });
  TTT.startTest();
});

QUnit.test("ajax: select F", function (assert) {
  let selectFFn = jQueryFrameFn("#page\\:mainForm\\:selectF input");
  let outputFFn = jQueryFrameFn("#page\\:mainForm\\:outputF span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    selectFFn().prop("checked", true).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFFn().text(), "true");
  });
  TTT.startTest();
});

QUnit.test("ajax: deselect F", function (assert) {
  let selectFFn = jQueryFrameFn("#page\\:mainForm\\:selectF input");
  let outputFFn = jQueryFrameFn("#page\\:mainForm\\:outputF span");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    selectFFn().prop("checked", false).trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFFn().text(), "false");
  });
  TTT.startTest();
});
