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
import {elementByIdFn, querySelectorAllFn} from "/script/tobago-test.js";

it("tc:button", function (done) {
  const eventNames = ["click", "dblclick", "focus", "blur"];
  const eventComponentFn = elementByIdFn("page:mainForm:buttonevent");
  const ajaxComponentFn = elementByIdFn("page:mainForm:buttonajax");

  const test = new JasmineTestTool(done);
  createSteps(test, "button", eventNames, eventComponentFn, ajaxComponentFn, null);
  test.start();
});

it("tc:in", function (done) {
  const eventNames = ["change", "click", "dblclick", "focus", "blur"];
  const eventComponentFn = elementByIdFn("page:mainForm:inevent::field");
  const ajaxComponentFn = elementByIdFn("page:mainForm:inajax::field");

  const changeValueFn = function (componentFn) {
    let oldValue = componentFn().value;
    let newValue = "ping";
    if (oldValue === "ping") {
      newValue = "pong"
    }
    componentFn().value = newValue;
  };

  const test = new JasmineTestTool(done);
  createSteps(test, "in", eventNames, eventComponentFn, ajaxComponentFn, changeValueFn);
  test.start();
});

it("tc:row", function (done) {
  const eventNames = ["click", "dblclick"];
  const eventComponentFn = elementByIdFn("page:mainForm:sheetevent:0:selectPlanet");
  const ajaxComponentFn = elementByIdFn("page:mainForm:sheetajax:0:selectPlanet");

  const test = new JasmineTestTool(done);
  createSteps(test, "row", eventNames, eventComponentFn, ajaxComponentFn, null);
  test.start();
});

it("tc:selectBooleanCheckbox", function (done) {
  const eventNames = ["change", "click", "dblclick", "focus", "blur"];
  const eventComponentFn = elementByIdFn("page:mainForm:selectBooleanCheckboxevent::field");
  const ajaxComponentFn = elementByIdFn("page:mainForm:selectBooleanCheckboxajax::field");

  const changeValueFn = function (componentFn) {
    componentFn().checked = !componentFn().checked;
  };

  const test = new JasmineTestTool(done);
  createSteps(test, "selectBooleanCheckbox", eventNames, eventComponentFn, ajaxComponentFn, changeValueFn);
  test.start();
});

it("tc:selectOneList", function (done) {
  const eventNames = ["change", "focus", "blur"];
  const eventComponentFn = elementByIdFn("page:mainForm:selectOneListevent::field");
  const ajaxComponentFn = elementByIdFn("page:mainForm:selectOneListajax::field");

  const clickEventNames = ["click", "dblclick"];
  const clickEventComponentFn = elementByIdFn("page:mainForm:selectOneListevent::selectField");

  const changeValueFn = function (componentFn) {
    if (componentFn().closest("tobago-select-one-list").querySelectorAll("option")[0].selected) {
      componentFn().closest("tobago-select-one-list").querySelectorAll("option")[1].selected = true;
    } else {
      componentFn().closest("tobago-select-one-list").querySelectorAll("option")[0].selected = true;
    }
  };

  const test = new JasmineTestTool(done);
  createSteps(test, "selectOneList", eventNames, eventComponentFn, ajaxComponentFn, changeValueFn);
  createSteps(test, "selectOneList", clickEventNames, clickEventComponentFn, ajaxComponentFn, changeValueFn);
  test.start();
});

it("tc:selectOneListbox", function (done) {
  const eventNames = ["change", "click", "dblclick", "focus", "blur"];
  const eventComponentFn = elementByIdFn("page:mainForm:selectOneListboxevent::field");
  const ajaxComponentFn = elementByIdFn("page:mainForm:selectOneListboxajax::field");

  const changeValueFn = function (componentFn) {
    if (componentFn().querySelectorAll("option")[0].selected) {
      componentFn().querySelectorAll("option")[1].selected = true;
    } else {
      componentFn().querySelectorAll("option")[0].selected = true;
    }
  };

  const test = new JasmineTestTool(done);
  createSteps(test, "selectOneListbox", eventNames, eventComponentFn, ajaxComponentFn, changeValueFn);
  test.start();
});

it("tc:textarea", function (done) {
  const eventNames = ["change", "click", "dblclick", "focus", "blur", "input"];
  const eventComponentFn = elementByIdFn("page:mainForm:textareaevent::field");
  const ajaxComponentFn = elementByIdFn("page:mainForm:textareaajax::field");

  const changeValueFn = function (componentFn) {
    let oldValue = componentFn().value;
    let newValue = "ping";
    if (oldValue === "ping") {
      newValue = "pong"
    }
    componentFn().value = newValue;
  };

  const test = new JasmineTestTool(done);
  createSteps(test, "textarea", eventNames, eventComponentFn, ajaxComponentFn, changeValueFn);
  test.start();
});

function createSteps(test, componentName, eventNames, eventComponentFn, ajaxComponentFn, changeValueFn) {
  const resetButtonFn = elementByIdFn("page:mainForm:reset");
  const actionCountFn = elementByIdFn("page:mainForm:inAction::field");
  const actionListenerCountFn = elementByIdFn("page:mainForm:inActionListener::field");
  const ajaxListenerCountFn = elementByIdFn("page:mainForm:inAjaxListener::field");
  const valueChangeListenerCountFn = elementByIdFn("page:mainForm:inValueChangeListener::field");
  const timestampFn = elementByIdFn("page:mainForm:inTimestamp::field");

  const rows = querySelectorAllFn("#page\\:mainForm\\:componentTable tr");
  let oldActionCount;
  let oldActionListenerCount;
  let oldAjaxListenerCount;
  let oldValueChangeListenerCount;
  let oldTimestamp;

  for (let i = 0; i < eventNames.length; i++) {
    const eventName = eventNames[i];
    let rowIdPrefix;

    rows().forEach((row) => {
      const tobagoOut = row.querySelector("tobago-out");
      if (row.querySelector("tobago-out .form-control-plaintext").textContent === componentName) {
        rowIdPrefix = tobagoOut.id.slice(0, tobagoOut.id.lastIndexOf(":"));
      }
    });

    const selectorButton = elementByIdFn(rowIdPrefix + ":" + eventName + "Behavior");

    test.do(() => oldTimestamp = parseInt(timestampFn().value));
    test.event("click", selectorButton, () => parseInt(timestampFn().value) > oldTimestamp);

    test.setup(() =>
            parseInt(actionCountFn().value)
            + parseInt(actionListenerCountFn().value)
            + parseInt(ajaxListenerCountFn().value)
            + parseInt(valueChangeListenerCountFn().value) === 0,
        null, "click", resetButtonFn);

    test.do(() => oldActionCount = parseInt(actionCountFn().value));
    test.do(() => oldActionListenerCount = parseInt(actionListenerCountFn().value));
    test.do(() => oldAjaxListenerCount = parseInt(ajaxListenerCountFn().value));
    test.do(() => oldValueChangeListenerCount = parseInt(valueChangeListenerCountFn().value));
    test.do(() => oldTimestamp = parseInt(timestampFn().value));
    if (changeValueFn !== null) {
      test.do(() => changeValueFn(eventComponentFn));
    }
    test.event(eventName, eventComponentFn, () => parseInt(timestampFn().value) > oldTimestamp);
    test.do(() => expect(parseInt(actionCountFn().value)).toBe(oldActionCount + 1));
    test.do(() => expect(parseInt(actionListenerCountFn().value)).toBe(oldActionListenerCount + 1));
    test.do(() => expect(parseInt(ajaxListenerCountFn().value)).toBe(oldAjaxListenerCount));
    test.do(() => expect(parseInt(valueChangeListenerCountFn().value))
        .toBe(changeValueFn !== null ? oldValueChangeListenerCount + 1 : oldValueChangeListenerCount));
    test.do(() => expect(parseInt(timestampFn().value)).toBeGreaterThan(oldTimestamp));

    test.do(() => oldActionCount = parseInt(actionCountFn().value));
    test.do(() => oldActionListenerCount = parseInt(actionListenerCountFn().value));
    test.do(() => oldAjaxListenerCount = parseInt(ajaxListenerCountFn().value));
    test.do(() => oldValueChangeListenerCount = parseInt(valueChangeListenerCountFn().value));
    test.do(() => oldTimestamp = parseInt(timestampFn().value));
    if (changeValueFn !== null) {
      test.do(() => changeValueFn(ajaxComponentFn));
    }
    test.event(eventName, ajaxComponentFn, () => parseInt(timestampFn().value) > oldTimestamp);
    test.do(() => expect(parseInt(actionCountFn().value)).toBe(oldActionCount));
    test.do(() => expect(parseInt(actionListenerCountFn().value)).toBe(oldActionListenerCount));
    test.do(() => expect(parseInt(ajaxListenerCountFn().value)).toBe(oldAjaxListenerCount + 1));
    test.do(() => expect(parseInt(valueChangeListenerCountFn().value))
        .toBe(changeValueFn !== null ? oldValueChangeListenerCount + 1 : oldValueChangeListenerCount));
    test.do(() => expect(parseInt(timestampFn().value)).toBeGreaterThan(oldTimestamp));
  }
}
