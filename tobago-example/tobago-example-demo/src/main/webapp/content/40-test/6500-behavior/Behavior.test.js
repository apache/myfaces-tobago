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

QUnit.test("Simple Event", function (assert) {
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:simpleEvent");
  let oldCounterValues = getCounterValues();

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(11, function () {
    compareCounterValues(assert, oldCounterValues, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0);
  });
  TTT.startTest();
});

QUnit.test("Simple Ajax", function (assert) {
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:simpleAjax");
  let oldCounterValues = getCounterValues();

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(11, function () {
    compareCounterValues(assert, oldCounterValues, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0);
  });
  TTT.startTest();
});

QUnit.test("Simple EventAjax", function (assert) {
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:simpleEventAjax");
  let oldCounterValues = getCounterValues();

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(11, function () {
    compareCounterValues(assert, oldCounterValues, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0);
  });
  TTT.startTest();
});

QUnit.test("Advanced Button: Option 1", function (assert) {
  const optionId = 0; //Event 1 + no Ajax enabled
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:advancedButton");
  testEventOption(assert, optionId, buttonFn, "dblclick", 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0);
});

QUnit.test("Advanced Button: Option 2", function (assert) {
  const optionId = 1; //Event 2 + Ajax 3 enabled
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:advancedButton");
  testAjaxOption(assert, optionId, buttonFn, "dblclick", 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1);
});

QUnit.test("Advanced Button: Option 3", function (assert) {
  const optionId = 2; //Event 3 + all Ajax enabled
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:advancedButton");
  testAjaxOption(assert, optionId, buttonFn, "click", 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1);
});

QUnit.test("Row: Option 1", function (assert) {
  const optionId = 0; //Event 1 + no Ajax enabled
  let rowFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:row");
  testEventOption(assert, optionId, rowFn, "dblclick", 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0);
});

QUnit.test("Row: Option 2", function (assert) {
  const optionId = 1; //Event 2 + Ajax 3 enabled
  let rowFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:row");
  testAjaxOption(assert, optionId, rowFn, "dblclick", 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1);
});

QUnit.test("Row: Option 3", function (assert) {
  const optionId = 2; //Event 3 + all Ajax enabled
  let rowFn = testFrameQuerySelectorFn("#page\\:mainForm\\:sheet\\:0\\:row");
  testAjaxOption(assert, optionId, rowFn, "click", 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1);
});

QUnit.test("Input: Click Event", function (assert) {
  let inputFn = testFrameQuerySelectorFn("#page\\:mainForm\\:inputClick\\:\\:field");
  testInputSection(assert, inputFn, "click");
});

function testEventOption(assert, optionId, componentFn, eventName,
                         buttonActionPlus, buttonActionListenerPlus,
                         action1Plus, actionListener1Plus, ajaxListener1Plus,
                         action2Plus, actionListener2Plus, ajaxListener2Plus,
                         action3Plus, actionListener3Plus, ajaxListener3Plus) {
  let hideFn = testFrameQuerySelectorFn("#page\\:mainForm\\:hideOperationTextBox");
  let operationOutFn = testFrameQuerySelectorFn("#page\\:mainForm\\:operationOut");
  let oldCounterValues = getCounterValues();
  let optionsFn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:advancedSelector input[type='radio']");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(operationOutFn(), null, "Content of operation test box must be hidden.");
  });
  TTT.action(function () {
    optionsFn().item(0).checked = false;
    optionsFn().item(1).checked = false;
    optionsFn().item(2).checked = false;
    optionsFn().item(optionId).checked = true;
    optionsFn().item(optionId).dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.action(function () {
    componentFn().dispatchEvent(new Event(eventName));
  });
  TTT.waitForResponse();
  TTT.asserts(12, function () {
    assert.notEqual(operationOutFn(), null, "Content of operation test box must be shown.");

    compareCounterValues(assert, oldCounterValues,
        buttonActionPlus, buttonActionListenerPlus,
        action1Plus, actionListener1Plus, ajaxListener1Plus,
        action2Plus, actionListener2Plus, ajaxListener2Plus,
        action3Plus, actionListener3Plus, ajaxListener3Plus);
  });
  TTT.startTest();
}

function testAjaxOption(assert, optionId, componentFn, eventName,
                        buttonActionPlus, buttonActionListenerPlus,
                        action1Plus, actionListener1Plus, ajaxListener1Plus,
                        action2Plus, actionListener2Plus, ajaxListener2Plus,
                        action3Plus, actionListener3Plus, ajaxListener3Plus) {
  let hideFn = testFrameQuerySelectorFn("#page\\:mainForm\\:hideOperationTextBox");
  let operationOutFn = testFrameQuerySelectorFn("#page\\:mainForm\\:operationOut");
  let oldCounterValues = getCounterValues();
  let optionsFn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:advancedSelector input[type='radio']");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(operationOutFn(), null, "Content of operation test box must be hidden.");
  });
  TTT.action(function () {
    optionsFn().item(0).checked = false;
    optionsFn().item(1).checked = false;
    optionsFn().item(2).checked = false;
    optionsFn().item(optionId).checked = true;
    optionsFn().item(optionId).dispatchEvent(new Event('change'));
  });
  TTT.waitForResponse();
  TTT.action(function () {
    componentFn().dispatchEvent(new Event(eventName));
  });
  TTT.waitForResponse();
  TTT.asserts(12, function () {
    assert.notEqual(operationOutFn(), null, "Content of operation test box must be shown.");

    compareCounterValues(assert, oldCounterValues,
        buttonActionPlus, buttonActionListenerPlus,
        action1Plus, actionListener1Plus, ajaxListener1Plus,
        action2Plus, actionListener2Plus, ajaxListener2Plus,
        action3Plus, actionListener3Plus, ajaxListener3Plus);
  });
  TTT.startTest();
}

function testInputSection(assert, inputFn, eventName) {
  let hideFn = testFrameQuerySelectorFn("#page\\:mainForm\\:hideOperationTextBox");
  let operationOutFn = testFrameQuerySelectorFn("#page\\:mainForm\\:operationOut");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    hideFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.ok(operationOutFn() === null, "Content of operation test box must be hidden.");
  });
  TTT.action(function () {
    inputFn().dispatchEvent(new Event(eventName));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.notOk(operationOutFn() === null, "Content of operation test box must be shown.");
  });
  TTT.startTest();
}

function getCounterValues() {
  let buttonActionCounter = testFrameQuerySelectorFn("#page\\:mainForm\\:buttonActionCounter.tobago-out")().textContent;
  let buttonActionListenerCounter = testFrameQuerySelectorFn("#page\\:mainForm\\:buttonActionListenerCounter.tobago-out")().textContent;

  let actionCount1 = testFrameQuerySelectorFn("#page\\:mainForm\\:actionCounter1.tobago-out")().textContent;
  let actionListenerCount1 = testFrameQuerySelectorFn("#page\\:mainForm\\:actionListenerCounter1.tobago-out")().textContent;
  let ajaxListenerCount1 = testFrameQuerySelectorFn("#page\\:mainForm\\:ajaxListenerCounter1.tobago-out")().textContent;

  let actionCount2 = testFrameQuerySelectorFn("#page\\:mainForm\\:actionCounter2.tobago-out")().textContent;
  let actionListenerCount2 = testFrameQuerySelectorFn("#page\\:mainForm\\:actionListenerCounter2.tobago-out")().textContent;
  let ajaxListenerCount2 = testFrameQuerySelectorFn("#page\\:mainForm\\:ajaxListenerCounter2.tobago-out")().textContent;

  let actionCount3 = testFrameQuerySelectorFn("#page\\:mainForm\\:actionCounter3.tobago-out")().textContent;
  let actionListenerCount3 = testFrameQuerySelectorFn("#page\\:mainForm\\:actionListenerCounter3.tobago-out")().textContent;
  let ajaxListenerCount3 = testFrameQuerySelectorFn("#page\\:mainForm\\:ajaxListenerCounter3.tobago-out")().textContent;

  return [buttonActionCounter, buttonActionListenerCounter,
    actionCount1, actionListenerCount1, ajaxListenerCount1,
    actionCount2, actionListenerCount2, ajaxListenerCount2,
    actionCount3, actionListenerCount3, ajaxListenerCount3]
}

function compareCounterValues(assert, oldCounterValues,
                              buttonActionPlus, buttonActionListenerPlus,
                              action1Plus, actionListener1Plus, ajaxListener1Plus,
                              action2Plus, actionListener2Plus, ajaxListener2Plus,
                              action3Plus, actionListener3Plus, ajaxListener3Plus) {
  let newCounterValues = getCounterValues();

  assert.equal(parseInt(newCounterValues[0]), parseInt(oldCounterValues[0]) + buttonActionPlus);
  assert.equal(parseInt(newCounterValues[1]), parseInt(oldCounterValues[1]) + buttonActionListenerPlus);
  assert.equal(parseInt(newCounterValues[2]), parseInt(oldCounterValues[2]) + action1Plus);
  assert.equal(parseInt(newCounterValues[3]), parseInt(oldCounterValues[3]) + actionListener1Plus);
  assert.equal(parseInt(newCounterValues[4]), parseInt(oldCounterValues[4]) + ajaxListener1Plus);
  assert.equal(parseInt(newCounterValues[5]), parseInt(oldCounterValues[5]) + action2Plus);
  assert.equal(parseInt(newCounterValues[6]), parseInt(oldCounterValues[6]) + actionListener2Plus);
  assert.equal(parseInt(newCounterValues[7]), parseInt(oldCounterValues[7]) + ajaxListener2Plus);
  assert.equal(parseInt(newCounterValues[8]), parseInt(oldCounterValues[8]) + action3Plus);
  assert.equal(parseInt(newCounterValues[9]), parseInt(oldCounterValues[9]) + actionListener3Plus);
  assert.equal(parseInt(newCounterValues[10]), parseInt(oldCounterValues[10]) + ajaxListener3Plus);
}
