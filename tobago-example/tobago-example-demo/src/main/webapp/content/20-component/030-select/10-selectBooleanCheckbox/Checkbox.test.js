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

import {testFrameQuerySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("submit: select A", function (assert) {
  let selectAFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectA input");
  let selectBFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectB input");
  let selectCFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectC input");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:submit");
  let outputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:submitOutput span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectAFn().checked = true;
    selectBFn().checked = false;
    selectCFn().checked = false;
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "A ");
  });
  TTT.startTest();
});

QUnit.test("submit: select B and C", function (assert) {
  let selectAFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectA input");
  let selectBFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectB input");
  let selectCFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectC input");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:submit");
  let outputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:submitOutput span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectAFn().checked = false;
    selectBFn().checked = true;
    selectCFn().checked = true;
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "B C ");
  });
  TTT.startTest();
});

QUnit.test("ajax: select D", function (assert) {
  let selectDFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectD input");
  let outputDFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outputD span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectDFn().checked = true;
    selectDFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputDFn().textContent, "true");
  });
  TTT.startTest();
});

QUnit.test("ajax: deselect D", function (assert) {
  let selectDFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectD input");
  let outputDFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outputD span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectDFn().checked = false;
    selectDFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputDFn().textContent, "false");
  });
  TTT.startTest();
});

QUnit.test("ajax: select E", function (assert) {
  let selectEFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectE input");
  let outputEFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outputE span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectEFn().checked = true;
    selectEFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputEFn().textContent, "true");
  });
  TTT.startTest();
});

QUnit.test("ajax: deselect E", function (assert) {
  let selectEFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectE input");
  let outputEFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outputE span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectEFn().checked = false;
    selectEFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputEFn().textContent, "false");
  });
  TTT.startTest();
});

QUnit.test("ajax: select F", function (assert) {
  let selectFFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectF input");
  let outputFFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outputF span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectFFn().checked = true;
    selectFFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFFn().textContent, "true");
  });
  TTT.startTest();
});

QUnit.test("ajax: deselect F", function (assert) {
  let selectFFn = testFrameQuerySelectorFn("#page\\:mainForm\\:selectF input");
  let outputFFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outputF span");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    selectFFn().checked = false;
    selectFFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(outputFFn().textContent, "false");
  });
  TTT.startTest();
});
