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

QUnit.test("Simple Event", function (assert) {
  assert.expect(11);
  var done = assert.async();
  var step = 1;

  var $button = jQueryFrame("#page\\:mainForm\\:simpleEvent");
  var oldCounterValues = getCounterValues();

  $button.click();

  jQuery("#page\\:testframe").load(function () {
    if (step === 1) {
      compareCounterValues(assert, oldCounterValues, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0);

      step++;
      done();
    }
  });
});

QUnit.test("Simple Ajax", function (assert) {
  assert.expect(11);
  var done = assert.async();
  var step = 1;

  var $button = jQueryFrame("#page\\:mainForm\\:simpleAjax");
  var oldCounterValues = getCounterValues();

  $button.click();

  waitForAjax(function () {
    var newCounterValues = getCounterValues();
    return step === 1 && oldCounterValues !== newCounterValues;
  }, function () {
    compareCounterValues(assert, oldCounterValues, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0);

    step++;
    done();
  });
});

QUnit.test("Simple EventAjax", function (assert) {
  assert.expect(11);
  var done = assert.async();
  var step = 1;

  var $button = jQueryFrame("#page\\:mainForm\\:simpleEventAjax");
  var oldCounterValues = getCounterValues();

  $button.click();

  waitForAjax(function () {
    var newCounterValues = getCounterValues();
    return step === 1 && oldCounterValues !== newCounterValues;
  }, function () {
    compareCounterValues(assert, oldCounterValues, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0);

    step++;
    done();
  });
});

QUnit.test("Advanced Button: Option 1", function (assert) {
  var $option = jQueryFrame("#page\\:mainForm\\:advancedSelector\\:\\:0");
  var $button = jQueryFrame("#page\\:mainForm\\:advancedButton");
  testEventOption(assert, $option, $button, "dblclick", 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0);
});

QUnit.test("Advanced Button: Option 2", function (assert) {
  var $option = jQueryFrame("#page\\:mainForm\\:advancedSelector\\:\\:1");
  var $button = jQueryFrame("#page\\:mainForm\\:advancedButton");

  testAjaxOption(assert, $option, $button, "dblclick", 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1);
});

QUnit.test("Advanced Button: Option 3", function (assert) {
  var $option = jQueryFrame("#page\\:mainForm\\:advancedSelector\\:\\:2");
  var $button = jQueryFrame("#page\\:mainForm\\:advancedButton");
  testAjaxOption(assert, $option, $button, "click", 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1);
});

QUnit.test("Row: Option 1", function (assert) {
  var $option = jQueryFrame("#page\\:mainForm\\:advancedSelector\\:\\:0");
  var $row = jQueryFrame("#page\\:mainForm\\:sheet\\:0\\:row");
  testEventOption(assert, $option, $row, "dblclick", 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0);
});

QUnit.test("Row: Option 2", function (assert) {
  var $option = jQueryFrame("#page\\:mainForm\\:advancedSelector\\:\\:1");
  var $row = jQueryFrame("#page\\:mainForm\\:sheet\\:0\\:row");
  testAjaxOption(assert, $option, $row, "dblclick", 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1);
});

QUnit.test("Row: Option 3", function (assert) {
  var $option = jQueryFrame("#page\\:mainForm\\:advancedSelector\\:\\:2");
  var $row = jQueryFrame("#page\\:mainForm\\:sheet\\:0\\:row");
  testAjaxOption(assert, $option, $row, "click", 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1);
});

QUnit.test("Input: Click Event", function (assert) {
  var $input = jQueryFrame("#page\\:mainForm\\:inputClick\\:\\:field");
  testInputSection(assert, $input, "click");
});

function testEventOption(assert, $option, $component, event,
                         buttonActionPlus, buttonActionListenerPlus,
                         action1Plus, actionListener1Plus, ajaxListener1Plus,
                         action2Plus, actionListener2Plus, ajaxListener2Plus,
                         action3Plus, actionListener3Plus, ajaxListener3Plus) {
  assert.expect(13);
  var done = assert.async(3);
  var step = 1;

  var $hide = jQueryFrame("#page\\:mainForm\\:hideOperationTextBox");
  var $operationOut = jQueryFrame("#page\\:mainForm\\:operationOut");
  var oldCounterValues = getCounterValues();

  $hide.click();

  jQuery("#page\\:testframe").load(function () {
    if (step === 1) {
      $operationOut = jQueryFrame($operationOut.selector);
      $option = jQueryFrame($option.selector);
      assert.equal($operationOut.length, 0, "Content of operation test box must be hidden.");

      $option.click();

      step++;
      done();
    } else if (step === 2) {
      $component = jQueryFrame($component.selector);
      $component.trigger(event);

      step++;
      done();
    } else if (step === 3) {
      $operationOut = jQueryFrame($operationOut.selector);
      assert.equal($operationOut.length, 1, "Content of operation test box must be shown.");

      compareCounterValues(assert, oldCounterValues,
          buttonActionPlus, buttonActionListenerPlus,
          action1Plus, actionListener1Plus, ajaxListener1Plus,
          action2Plus, actionListener2Plus, ajaxListener2Plus,
          action3Plus, actionListener3Plus, ajaxListener3Plus);

      step++;
      done();
    }
  });
}

function testAjaxOption(assert, $option, $component, event,
                        buttonActionPlus, buttonActionListenerPlus,
                        action1Plus, actionListener1Plus, ajaxListener1Plus,
                        action2Plus, actionListener2Plus, ajaxListener2Plus,
                        action3Plus, actionListener3Plus, ajaxListener3Plus) {
  assert.expect(13);
  var done = assert.async(3);
  var step = 1;

  var $hide = jQueryFrame("#page\\:mainForm\\:hideOperationTextBox");
  var $operationOut = jQueryFrame("#page\\:mainForm\\:operationOut");
  var oldCounterValues = getCounterValues();

  $hide.click();

  jQuery("#page\\:testframe").load(function () {
    if (step === 1) {
      $operationOut = jQueryFrame($operationOut.selector);
      $option = jQueryFrame($option.selector);
      assert.equal($operationOut.length, 0, "Content of operation test box must be hidden.");

      $option.click();

      step++;
      done();
    } else if (step === 2) {
      $component = jQueryFrame($component.selector);
      $component.trigger(event);

      step++;
      done();

      waitForAjax(function () {
        var newCounterValues = getCounterValues();
        return step === 3 && oldCounterValues !== newCounterValues;
      }, function () {
        $operationOut = jQueryFrame($operationOut.selector);
        assert.equal($operationOut.length, 1, "Content of operation test box must be shown.");

        compareCounterValues(assert, oldCounterValues,
            buttonActionPlus, buttonActionListenerPlus,
            action1Plus, actionListener1Plus, ajaxListener1Plus,
            action2Plus, actionListener2Plus, ajaxListener2Plus,
            action3Plus, actionListener3Plus, ajaxListener3Plus);

        step++;
        done();
      });
    }
  });
}

function testInputSection(assert, $input, eventName) {
  assert.expect(2);
  var done = assert.async(2);
  var step = 1;

  var $hide = jQueryFrame("#page\\:mainForm\\:hideOperationTextBox");
  $hide.click();

  jQuery("#page\\:testframe").load(function () {
    if (step === 1) {
      $input = jQueryFrame($input.selector);
      assert.ok(isOperationTestBoxCollapsed(), "Content of operation test box must be hidden.");

      $input.trigger(eventName);

      step++;
      done();
    } else if (step === 2) {
      assert.notOk(isOperationTestBoxCollapsed(), "Content of operation test box must be shown.");

      step++;
      done();
    }
  });
}

function getCounterValues() {
  var buttonActionCounter = jQueryFrame("#page\\:mainForm\\:buttonActionCounter.tobago-out").text();
  var buttonActionListenerCounter = jQueryFrame("#page\\:mainForm\\:buttonActionListenerCounter.tobago-out").text();

  var actionCount1 = jQueryFrame("#page\\:mainForm\\:actionCounter1.tobago-out").text();
  var actionListenerCount1 = jQueryFrame("#page\\:mainForm\\:actionListenerCounter1.tobago-out").text();
  var ajaxListenerCount1 = jQueryFrame("#page\\:mainForm\\:ajaxListenerCounter1.tobago-out").text();

  var actionCount2 = jQueryFrame("#page\\:mainForm\\:actionCounter2.tobago-out").text();
  var actionListenerCount2 = jQueryFrame("#page\\:mainForm\\:actionListenerCounter2.tobago-out").text();
  var ajaxListenerCount2 = jQueryFrame("#page\\:mainForm\\:ajaxListenerCounter2.tobago-out").text();

  var actionCount3 = jQueryFrame("#page\\:mainForm\\:actionCounter3.tobago-out").text();
  var actionListenerCount3 = jQueryFrame("#page\\:mainForm\\:actionListenerCounter3.tobago-out").text();
  var ajaxListenerCount3 = jQueryFrame("#page\\:mainForm\\:ajaxListenerCounter3.tobago-out").text();

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
  var newCounterValues = getCounterValues();

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

function isOperationTestBoxCollapsed() {
  var $operationOut = jQueryFrame("#page\\:mainForm\\:operationOut");
  return $operationOut.length === 0;
}
