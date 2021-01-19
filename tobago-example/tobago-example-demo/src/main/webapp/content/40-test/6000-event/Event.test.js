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

it("must be fixed first", function (done) {
  let test = new JasmineTestTool(done);
  test.do(() => fail("not implemented yet"));
  test.start();
});

/*
import {querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("tc:button", function (assert) {
  let eventNames = ["click", "dblclick", "focus", "blur"];
  let eventComponentFn = querySelectorFn("#page\\:mainForm\\:buttonevent");
  let ajaxComponentFn = querySelectorFn("#page\\:mainForm\\:buttonajax");
  testEvent(assert, "button", eventNames, eventComponentFn, ajaxComponentFn, null);
});

QUnit.test("tc:in", function (assert) {
  let eventNames = ["change", "click", "dblclick", "focus", "blur"];
  let eventComponentFn = querySelectorFn("#page\\:mainForm\\:inevent\\:\\:field");
  let ajaxComponentFn = querySelectorFn("#page\\:mainForm\\:inajax\\:\\:field");
  let changeValue = function (componentFn) {
    let oldValue = componentFn().value;
    let newValue = "hello";
    if (oldValue === "hello") {
      newValue = "hello there!"
    }
    componentFn().value = newValue;
  };
  testEvent(assert, "in", eventNames, eventComponentFn, ajaxComponentFn, changeValue);
});

QUnit.test("tc:row", function (assert) {
  let eventNames = ["click", "dblclick"];
  let eventComponentFn = querySelectorFn("#page\\:mainForm\\:sheetevent\\:0\\:selectPlanet");
  let ajaxComponentFn = querySelectorFn("#page\\:mainForm\\:sheetajax\\:0\\:selectPlanet");
  testEvent(assert, "row", eventNames, eventComponentFn, ajaxComponentFn, null);
});

QUnit.test("tc:selectBooleanCheckbox", function (assert) {
  let eventNames = ["change", "click", "dblclick", "focus", "blur"];
  let eventComponentFn = querySelectorFn("#page\\:mainForm\\:selectBooleanCheckboxevent\\:\\:field");
  let ajaxComponentFn = querySelectorFn("#page\\:mainForm\\:selectBooleanCheckboxajax\\:\\:field");
  let changeValue = function (componentFn) {
    componentFn().checked = !componentFn().checked;
  };
  testEvent(assert, "selectBooleanCheckbox", eventNames, eventComponentFn, ajaxComponentFn, changeValue);
});

QUnit.test("tc:textarea", function (assert) {
  let eventNames = ["change", "click", "dblclick", "focus", "blur"];
  let eventComponentFn = querySelectorFn("#page\\:mainForm\\:textareaevent\\:\\:field");
  let ajaxComponentFn = querySelectorFn("#page\\:mainForm\\:textareaajax\\:\\:field");
  let changeValue = function (componentFn) {
    let oldValue = componentFn().value;
    let newValue = "hello";
    if (oldValue === "hello") {
      newValue = "hello there!"
    }
    componentFn().value = newValue;
  };
  testEvent(assert, "textarea", eventNames, eventComponentFn, ajaxComponentFn, changeValue);
});

function testEvent(assert, componentName, eventNames, eventComponentFn, ajaxComponentFn, changeValueFunc) {
  let oldActionCount;
  let oldActionListenerCount;
  let oldAjaxListenerCount;
  let oldValueChangeListenerCount;
  let oldTimestamp;

  let TTT = new TobagoTestTool(assert);
  for (let i = 0; i < eventNames.length; i++) {
    let eventName = eventNames[i];

    TTT.action(function () {
      activateComponent(componentName, eventName);
    });
    TTT.waitForResponse();
    TTT.action(function () {
      oldActionCount = getActionCount();
      oldActionListenerCount = getActionListenerCount();
      oldAjaxListenerCount = getAjaxListenerCount();
      oldValueChangeListenerCount = getValueChangeListenerCount();
      oldTimestamp = getTimestamp();

      if (changeValueFunc !== null) {
        changeValueFunc(eventComponentFn);
      }
      eventComponentFn().dispatchEvent(new Event(eventName));
    });
    TTT.waitForResponse();
    TTT.asserts(5, function () {
      assert.equal(getActionCount(), oldActionCount + 1, eventName + " - tc:event - action");
      assert.equal(getActionListenerCount(), oldActionListenerCount + 1, eventName + " - tc:event - actionListener");
      assert.equal(getAjaxListenerCount(), oldAjaxListenerCount, eventName + " - tc:event - ajaxListener");
      if (changeValueFunc !== null) {
        assert.equal(getValueChangeListenerCount(), oldValueChangeListenerCount + 1,
            eventName + " - tc:event - valueChangeListener");
      } else {
        assert.equal(getValueChangeListenerCount(), oldValueChangeListenerCount,
            eventName + " - tc:event - valueChangeListener");
      }
      assert.ok(getTimestamp() > oldTimestamp, eventName + " - tc:event - timestamp");
    });
    TTT.action(function () {
      oldActionCount = getActionCount();
      oldActionListenerCount = getActionListenerCount();
      oldAjaxListenerCount = getAjaxListenerCount();
      oldValueChangeListenerCount = getValueChangeListenerCount();
      oldTimestamp = getTimestamp();

      if (changeValueFunc !== null) {
        changeValueFunc(ajaxComponentFn);
      }
      ajaxComponentFn().dispatchEvent(new Event(eventName));
    });
    TTT.waitForResponse();
    TTT.asserts(5, function () {
      assert.equal(getActionCount(), oldActionCount, eventName + " - f:ajax - action");
      assert.equal(getActionListenerCount(), oldActionListenerCount, eventName + " - f:ajax - actionListener");
      assert.equal(getAjaxListenerCount(), oldAjaxListenerCount + 1, eventName + " - f:ajax - ajaxListener");
      if (changeValueFunc !== null) {
        assert.equal(getValueChangeListenerCount(), oldValueChangeListenerCount + 1,
            eventName + " - f:ajax - valueChangeListener");
      } else {
        assert.equal(getValueChangeListenerCount(), oldValueChangeListenerCount,
            eventName + " - f:ajax - valueChangeListener");
      }
      assert.ok(getTimestamp() > oldTimestamp, eventName + " - f:ajax - timestamp");
    });
  }
  TTT.startTest();
}

function activateComponent(componentName, eventName) {
  const rows = querySelectorAllFn("#page\\:mainForm\\:componentTable .tobago-sheet-row")();
  Array.prototype.forEach.call(rows, function (element, i) {
    if (element.querySelectorAll("td").item(0).querySelector(".tobago-out").textContent === componentName) {
      const buttons = element.querySelectorAll("button");
      Array.prototype.forEach.call(buttons, function (element, i) {
        let id = element.getAttribute("id");
        if (id !== undefined && id.indexOf(eventName + "Behavior") >= 0) {
          element.dispatchEvent(new Event("click", {bubbles: true}));
        }
      });
    }
  });
}

function getActionCount() {
  return parseInt(querySelectorFn("#page\\:mainForm\\:inAction\\:\\:field")().value);
}

function getActionListenerCount() {
  return parseInt(querySelectorFn("#page\\:mainForm\\:inActionListener\\:\\:field")().value);
}

function getAjaxListenerCount() {
  return parseInt(querySelectorFn("#page\\:mainForm\\:inAjaxListener\\:\\:field")().value);
}

function getValueChangeListenerCount() {
  return parseInt(querySelectorFn("#page\\:mainForm\\:inValueChangeListener\\:\\:field")().value);
}

function getTimestamp() {
  return parseInt(querySelectorFn("#page\\:mainForm\\:inTimestamp\\:\\:field")().value);
}
*/
