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
import {elementByIdFn, querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";

it("Simple Event", function (done) {
  const buttonFn = elementByIdFn("page:mainForm:simpleEvent");
  const oldCounterValues = getCounterValues();

  const test = new JasmineTestTool(done);
  test.event("click", buttonFn, () => getCounterValues().action1 > oldCounterValues.action1)
  test.do(() => expect(getCounterValues().buttonAction).toBe(oldCounterValues.buttonAction));
  test.do(() => expect(getCounterValues().buttonActionListener).toBe(oldCounterValues.buttonActionListener));
  test.do(() => expect(getCounterValues().action1).toBe(oldCounterValues.action1 + 1));
  test.do(() => expect(getCounterValues().actionListener1).toBe(oldCounterValues.actionListener1 + 1));
  test.do(() => expect(getCounterValues().ajaxListener1).toBe(oldCounterValues.ajaxListener1));
  test.do(() => expect(getCounterValues().action2).toBe(oldCounterValues.action2));
  test.do(() => expect(getCounterValues().actionListener2).toBe(oldCounterValues.actionListener2));
  test.do(() => expect(getCounterValues().ajaxListener2).toBe(oldCounterValues.ajaxListener2));
  test.do(() => expect(getCounterValues().action3).toBe(oldCounterValues.action3));
  test.do(() => expect(getCounterValues().actionListener3).toBe(oldCounterValues.actionListener3));
  test.do(() => expect(getCounterValues().ajaxListener3).toBe(oldCounterValues.ajaxListener3));
  test.start();
});

it("Simple Ajax", function (done) {
  const buttonFn = elementByIdFn("page:mainForm:simpleAjax");
  const oldCounterValues = getCounterValues();

  const test = new JasmineTestTool(done);
  test.event("click", buttonFn, () => getCounterValues().ajaxListener1 > oldCounterValues.ajaxListener1)
  test.do(() => expect(getCounterValues().buttonAction).toBe(oldCounterValues.buttonAction));
  test.do(() => expect(getCounterValues().buttonActionListener).toBe(oldCounterValues.buttonActionListener));
  test.do(() => expect(getCounterValues().action1).toBe(oldCounterValues.action1));
  test.do(() => expect(getCounterValues().actionListener1).toBe(oldCounterValues.actionListener1));
  test.do(() => expect(getCounterValues().ajaxListener1).toBe(oldCounterValues.ajaxListener1 + 1));
  test.do(() => expect(getCounterValues().action2).toBe(oldCounterValues.action2));
  test.do(() => expect(getCounterValues().actionListener2).toBe(oldCounterValues.actionListener2));
  test.do(() => expect(getCounterValues().ajaxListener2).toBe(oldCounterValues.ajaxListener2));
  test.do(() => expect(getCounterValues().action3).toBe(oldCounterValues.action3));
  test.do(() => expect(getCounterValues().actionListener3).toBe(oldCounterValues.actionListener3));
  test.do(() => expect(getCounterValues().ajaxListener3).toBe(oldCounterValues.ajaxListener3));
  test.start();
});

it("Simple EventAjax", function (done) {
  const buttonFn = elementByIdFn("page:mainForm:simpleEventAjax");
  const oldCounterValues = getCounterValues();

  const test = new JasmineTestTool(done);
  test.event("click", buttonFn, () => getCounterValues().ajaxListener1 > oldCounterValues.ajaxListener1)
  test.do(() => expect(getCounterValues().buttonAction).toBe(oldCounterValues.buttonAction));
  test.do(() => expect(getCounterValues().buttonActionListener).toBe(oldCounterValues.buttonActionListener));
  test.do(() => expect(getCounterValues().action1).toBe(oldCounterValues.action1));
  test.do(() => expect(getCounterValues().actionListener1).toBe(oldCounterValues.actionListener1));
  test.do(() => expect(getCounterValues().ajaxListener1).toBe(oldCounterValues.ajaxListener1 + 1));
  test.do(() => expect(getCounterValues().action2).toBe(oldCounterValues.action2));
  test.do(() => expect(getCounterValues().actionListener2).toBe(oldCounterValues.actionListener2));
  test.do(() => expect(getCounterValues().ajaxListener2).toBe(oldCounterValues.ajaxListener2));
  test.do(() => expect(getCounterValues().action3).toBe(oldCounterValues.action3));
  test.do(() => expect(getCounterValues().actionListener3).toBe(oldCounterValues.actionListener3));
  test.do(() => expect(getCounterValues().ajaxListener3).toBe(oldCounterValues.ajaxListener3));
  test.start();
});

it("Dropdown: Simple Event", function (done) {
  const dropdownButtonFn = elementByIdFn("page:mainForm:dropdown::command");
  const dropdownMenuFn = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:dropdown']");
  const entryFn = elementByIdFn("page:mainForm:simpleEventEntry");
  const oldCounterValues = getCounterValues();

  const test = new JasmineTestTool(done);
  test.event("click", dropdownButtonFn, () => dropdownMenuFn().parentElement.classList.contains("tobago-page-menuStore"))
  test.do(() => expect(dropdownMenuFn().parentElement.tagName).not.toEqual("TOBAGO-DROPDOWN"));
  test.do(() => expect(dropdownMenuFn().parentElement.classList.contains("tobago-page-menuStore")).toBeTrue());

  test.event("click", entryFn, () => getCounterValues().action1 > oldCounterValues.action1)
  test.do(() => expect(getCounterValues().buttonAction).toBe(oldCounterValues.buttonAction));
  test.do(() => expect(getCounterValues().buttonActionListener).toBe(oldCounterValues.buttonActionListener));
  test.do(() => expect(getCounterValues().action1).toBe(oldCounterValues.action1 + 1));
  test.do(() => expect(getCounterValues().actionListener1).toBe(oldCounterValues.actionListener1 + 1));
  test.do(() => expect(getCounterValues().ajaxListener1).toBe(oldCounterValues.ajaxListener1));
  test.do(() => expect(getCounterValues().action2).toBe(oldCounterValues.action2));
  test.do(() => expect(getCounterValues().actionListener2).toBe(oldCounterValues.actionListener2));
  test.do(() => expect(getCounterValues().ajaxListener2).toBe(oldCounterValues.ajaxListener2));
  test.do(() => expect(getCounterValues().action3).toBe(oldCounterValues.action3));
  test.do(() => expect(getCounterValues().actionListener3).toBe(oldCounterValues.actionListener3));
  test.do(() => expect(getCounterValues().ajaxListener3).toBe(oldCounterValues.ajaxListener3));
  test.do(() => expect(dropdownMenuFn().parentElement.tagName).toEqual("TOBAGO-DROPDOWN"));
  test.do(() => expect(dropdownMenuFn().parentElement.classList.contains("tobago-page-menuStore")).toBeFalse());
  test.start();
});

it("Dropdown: Simple Ajax", function (done) {
  const dropdownButtonFn = elementByIdFn("page:mainForm:dropdown::command");
  const dropdownMenuFn = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:dropdown']");
  const entryFn = elementByIdFn("page:mainForm:simpleAjaxEntry");
  const oldCounterValues = getCounterValues();

  const test = new JasmineTestTool(done);
  test.event("click", dropdownButtonFn, () => dropdownMenuFn().parentElement.classList.contains("tobago-page-menuStore"))
  test.do(() => expect(dropdownMenuFn().parentElement.tagName).not.toEqual("TOBAGO-DROPDOWN"));
  test.do(() => expect(dropdownMenuFn().parentElement.classList.contains("tobago-page-menuStore")).toBeTrue());

  test.event("click", entryFn, () => getCounterValues().ajaxListener1 > oldCounterValues.ajaxListener1)
  test.do(() => expect(getCounterValues().buttonAction).toBe(oldCounterValues.buttonAction));
  test.do(() => expect(getCounterValues().buttonActionListener).toBe(oldCounterValues.buttonActionListener));
  test.do(() => expect(getCounterValues().action1).toBe(oldCounterValues.action1));
  test.do(() => expect(getCounterValues().actionListener1).toBe(oldCounterValues.actionListener1));
  test.do(() => expect(getCounterValues().ajaxListener1).toBe(oldCounterValues.ajaxListener1 + 1));
  test.do(() => expect(getCounterValues().action2).toBe(oldCounterValues.action2));
  test.do(() => expect(getCounterValues().actionListener2).toBe(oldCounterValues.actionListener2));
  test.do(() => expect(getCounterValues().ajaxListener2).toBe(oldCounterValues.ajaxListener2));
  test.do(() => expect(getCounterValues().action3).toBe(oldCounterValues.action3));
  test.do(() => expect(getCounterValues().actionListener3).toBe(oldCounterValues.actionListener3));
  test.do(() => expect(getCounterValues().ajaxListener3).toBe(oldCounterValues.ajaxListener3));
  test.do(() => expect(dropdownMenuFn().parentElement.tagName).toEqual("TOBAGO-DROPDOWN"));
  test.do(() => expect(dropdownMenuFn().parentElement.classList.contains("tobago-page-menuStore")).toBeFalse());
  test.start();
});

it("Dropdown: Simple EventAjax", function (done) {
  const dropdownButtonFn = elementByIdFn("page:mainForm:dropdown::command");
  const dropdownMenuFn = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:dropdown']");
  const entryFn = elementByIdFn("page:mainForm:simpleEventAjaxEntry");
  const oldCounterValues = getCounterValues();

  const test = new JasmineTestTool(done);
  test.event("click", dropdownButtonFn, () => dropdownMenuFn().parentElement.classList.contains("tobago-page-menuStore"))
  test.do(() => expect(dropdownMenuFn().parentElement.tagName).not.toEqual("TOBAGO-DROPDOWN"));
  test.do(() => expect(dropdownMenuFn().parentElement.classList.contains("tobago-page-menuStore")).toBeTrue());

  test.event("click", entryFn, () => getCounterValues().ajaxListener1 > oldCounterValues.ajaxListener1)
  test.do(() => expect(getCounterValues().buttonAction).toBe(oldCounterValues.buttonAction));
  test.do(() => expect(getCounterValues().buttonActionListener).toBe(oldCounterValues.buttonActionListener));
  test.do(() => expect(getCounterValues().action1).toBe(oldCounterValues.action1));
  test.do(() => expect(getCounterValues().actionListener1).toBe(oldCounterValues.actionListener1));
  test.do(() => expect(getCounterValues().ajaxListener1).toBe(oldCounterValues.ajaxListener1 + 1));
  test.do(() => expect(getCounterValues().action2).toBe(oldCounterValues.action2));
  test.do(() => expect(getCounterValues().actionListener2).toBe(oldCounterValues.actionListener2));
  test.do(() => expect(getCounterValues().ajaxListener2).toBe(oldCounterValues.ajaxListener2));
  test.do(() => expect(getCounterValues().action3).toBe(oldCounterValues.action3));
  test.do(() => expect(getCounterValues().actionListener3).toBe(oldCounterValues.actionListener3));
  test.do(() => expect(getCounterValues().ajaxListener3).toBe(oldCounterValues.ajaxListener3));
  test.start();
});

it("Advanced Button: Option 1", function (done) {
  const optionId = 0; //Event 1 + no Ajax enabled
  const buttonFn = elementByIdFn("page:mainForm:advancedButton");
  const expectedCounterValues = getCounterValues();
  expectedCounterValues.action1++;
  expectedCounterValues.actionListener1++;

  testAdvancedButton(done, optionId, buttonFn, "dblclick", expectedCounterValues);
});

it("Advanced Button: Option 2", function (done) {
  const optionId = 1; //Event 2 + Ajax 3 enabled
  const buttonFn = elementByIdFn("page:mainForm:advancedButton");
  const expectedCounterValues = getCounterValues();
  expectedCounterValues.buttonAction++;
  expectedCounterValues.buttonActionListener++;
  expectedCounterValues.ajaxListener3++;

  testAdvancedButton(done, optionId, buttonFn, "dblclick", expectedCounterValues);
});

it("Advanced Button: Option 3", function (done) {
  const optionId = 2; //Event 3 + all Ajax enabled
  const buttonFn = elementByIdFn("page:mainForm:advancedButton");
  const expectedCounterValues = getCounterValues();
  expectedCounterValues.buttonAction++;
  expectedCounterValues.buttonActionListener++;
  expectedCounterValues.ajaxListener1++;
  expectedCounterValues.ajaxListener2++;
  expectedCounterValues.ajaxListener3++;

  testAdvancedButton(done, optionId, buttonFn, "click", expectedCounterValues);
});

it("Row: Option 1", function (done) {
  const optionId = 0; //Event 1 + no Ajax enabled
  const rowFn = elementByIdFn("page:mainForm:sheet:0:row");
  const expectedCounterValues = getCounterValues();
  expectedCounterValues.action1++;
  expectedCounterValues.actionListener1++;

  testAdvancedButton(done, optionId, rowFn, "dblclick", expectedCounterValues);
});

it("Row: Option 2", function (done) {
  const optionId = 1; //Event 2 + Ajax 3 enabled
  const rowFn = elementByIdFn("page:mainForm:sheet:0:row");
  const expectedCounterValues = getCounterValues();
  expectedCounterValues.action2++;
  expectedCounterValues.actionListener2++;
  expectedCounterValues.ajaxListener3++;

  testAdvancedButton(done, optionId, rowFn, "dblclick", expectedCounterValues);
});

it("Row: Option 3", function (done) {
  const optionId = 2; //Event 3 + all Ajax enabled
  const rowFn = elementByIdFn("page:mainForm:sheet:0:row");
  const expectedCounterValues = getCounterValues();
  expectedCounterValues.ajaxListener1++;
  expectedCounterValues.ajaxListener2++;
  expectedCounterValues.action3++;
  expectedCounterValues.actionListener3++;
  expectedCounterValues.ajaxListener3++;

  testAdvancedButton(done, optionId, rowFn, "click", expectedCounterValues);
});

it("Input: Click Event", function (done) {
  const inputFn = elementByIdFn("page:mainForm:inputClick::field");
  const hideFn = querySelectorFn("#page\\:mainForm\\:hideOperationTextBox");
  const operationOutFn = querySelectorFn("#page\\:mainForm\\:operationOut");

  const test = new JasmineTestTool(done);
  test.setup(() => operationOutFn() === null, null, "click", hideFn);
  test.do(() => expect(operationOutFn()).toBeNull());
  test.event("click", inputFn, () => operationOutFn() !== null);
  test.do(() => expect(operationOutFn()).not.toBeNull());
  test.start();
});

function testAdvancedButton(done, optionId, componentFn, eventType, expectedCounterValues) {
  const hideFn = elementByIdFn("page:mainForm:hideOperationTextBox");
  const operationOutFn = elementByIdFn("page:mainForm:operationOut");
  const optionsFn = querySelectorAllFn("#page\\:mainForm\\:advancedSelector input[type='radio']");
  const optionFn = elementByIdFn("page:mainForm:advancedSelector::" + optionId);

  const test = new JasmineTestTool(done);
  test.setup(() => operationOutFn() === null, null, "click", hideFn);
  test.setup(() => optionsFn().item(optionId).checked,
      () => {
        optionsFn().item(0).checked = false;
        optionsFn().item(1).checked = false;
        optionsFn().item(2).checked = false;
        optionsFn().item(optionId).checked = true;
      }, "change", optionFn);
  test.do(() => expect(operationOutFn()).toBeNull());
  test.do(() => expect(optionFn().checked).toBeTrue());

  test.event(eventType, componentFn, () => operationOutFn() !== null);
  test.do(() => expect(operationOutFn()).not.toBeNull());
  test.do(() => expect(getCounterValues().buttonAction).toBe(expectedCounterValues.buttonAction));
  test.do(() => expect(getCounterValues().buttonActionListener).toBe(expectedCounterValues.buttonActionListener));
  test.do(() => expect(getCounterValues().action1).toBe(expectedCounterValues.action1));
  test.do(() => expect(getCounterValues().actionListener1).toBe(expectedCounterValues.actionListener1));
  test.do(() => expect(getCounterValues().ajaxListener1).toBe(expectedCounterValues.ajaxListener1));
  test.do(() => expect(getCounterValues().action2).toBe(expectedCounterValues.action2));
  test.do(() => expect(getCounterValues().actionListener2).toBe(expectedCounterValues.actionListener2));
  test.do(() => expect(getCounterValues().ajaxListener2).toBe(expectedCounterValues.ajaxListener2));
  test.do(() => expect(getCounterValues().action3).toBe(expectedCounterValues.action3));
  test.do(() => expect(getCounterValues().actionListener3).toBe(expectedCounterValues.actionListener3));
  test.do(() => expect(getCounterValues().ajaxListener3).toBe(expectedCounterValues.ajaxListener3));

  test.start();
}

function getCounterValues() {
  const buttonActionCounter = intValueFromOutput("#page\\:mainForm\\:buttonActionCounter");
  const buttonActionListenerCounter = intValueFromOutput("#page\\:mainForm\\:buttonActionListenerCounter");
  const actionCount1 = intValueFromOutput("#page\\:mainForm\\:actionCounter1");
  const actionListenerCount1 = intValueFromOutput("#page\\:mainForm\\:actionListenerCounter1");
  const ajaxListenerCount1 = intValueFromOutput("#page\\:mainForm\\:ajaxListenerCounter1");
  const actionCount2 = intValueFromOutput("#page\\:mainForm\\:actionCounter2");
  const actionListenerCount2 = intValueFromOutput("#page\\:mainForm\\:actionListenerCounter2");
  const ajaxListenerCount2 = intValueFromOutput("#page\\:mainForm\\:ajaxListenerCounter2");
  const actionCount3 = intValueFromOutput("#page\\:mainForm\\:actionCounter3");
  const actionListenerCount3 = intValueFromOutput("#page\\:mainForm\\:actionListenerCounter3");
  const ajaxListenerCount3 = intValueFromOutput("#page\\:mainForm\\:ajaxListenerCounter3");

  return new CounterValues(buttonActionCounter, buttonActionListenerCounter,
      actionCount1, actionListenerCount1, ajaxListenerCount1,
      actionCount2, actionListenerCount2, ajaxListenerCount2,
      actionCount3, actionListenerCount3, ajaxListenerCount3);
}

function intValueFromOutput(expression) {
  return parseInt(querySelectorFn(expression + " .form-control-plaintext")().textContent);
}

class CounterValues {
  constructor(buttonAction, buttonActionListener,
              action1, actionListener1, ajaxListener1,
              action2, actionListener2, ajaxListener2,
              action3, actionListener3, ajaxListener3) {
    this.buttonAction = buttonAction;
    this.buttonActionListener = buttonActionListener;
    this.action1 = action1;
    this.actionListener1 = actionListener1;
    this.ajaxListener1 = ajaxListener1;
    this.action2 = action2;
    this.actionListener2 = actionListener2;
    this.ajaxListener2 = ajaxListener2;
    this.action3 = action3;
    this.actionListener3 = actionListener3;
    this.ajaxListener3 = ajaxListener3;
  }
}
