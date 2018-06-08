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

QUnit.test("Required: Submit without content.", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let textareaFn = jQueryFrameFn("#page\\:mainForm\\:required\\:textarea\\:\\:field");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:required\\:submit_r");
  let textareaValue = textareaFn().val();

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    textareaFn().val("");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(textareaFn().val(), textareaValue);
  });
  TTT.startTest();
});

QUnit.test("Required: Submit with content.", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let textareaFn = jQueryFrameFn("#page\\:mainForm\\:required\\:textarea\\:\\:field");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:required\\:submit_r");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    textareaFn().val("some content");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(textareaFn().val(), "some content");
  });
  TTT.startTest();
});

QUnit.test("Validate Length: Submit single character.", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let inFn = jQueryFrameFn("#page\\:mainForm\\:validateLength\\:in_vl\\:\\:field");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:validateLength\\:submit_vl");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    inFn().val("a");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Validate Length: Submit two character.", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let inFn = jQueryFrameFn("#page\\:mainForm\\:validateLength\\:in_vl\\:\\:field");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:validateLength\\:submit_vl");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    inFn().val("ab");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Validate Range: Submit no number.", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let inFn = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    inFn().val("no number");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Validate Range: Submit number '2' which is out of range.", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let inFn = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    inFn().val("2");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Validate Range: Submit number '78' which is out of range.", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let inFn = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    inFn().val("78");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Validate Range: Submit number '64' which is within the range.", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let inFn = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    inFn().val("64");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Regex Validation: Submit 'T' which violates the pattern.", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let inFn = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:in_rv\\:\\:field");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:submit_rv");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    inFn().val("T");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Regex Validation: Submit '3' which violates the pattern.", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let inFn = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:in_rv\\:\\:field");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:submit_rv");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    inFn().val("3");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Regex Validation: Submit 'T3' which is accepted.", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let inFn = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:in_rv\\:\\:field");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:submit_rv");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    inFn().val("T3");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Custom Validator: Submit rejected string.", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let inFn = jQueryFrameFn("#page\\:mainForm\\:customValidator\\:in_cv\\:\\:field");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:customValidator\\:submit_cv");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    inFn().val("java");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Custom Validator: Submit accepted string.", function (assert) {
  let messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let inFn = jQueryFrameFn("#page\\:mainForm\\:customValidator\\:in_cv\\:\\:field");
  let submitFn = jQueryFrameFn("#page\\:mainForm\\:customValidator\\:submit_cv");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    inFn().val("tobago");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});
