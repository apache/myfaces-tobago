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

QUnit.test("Required: Submit without content.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let textareaFn = testFrameQuerySelectorFn("#page\\:mainForm\\:required\\:textarea\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:required\\:submit_r");
  let textareaValue = textareaFn().value;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    textareaFn().value = "";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(messagesFn().length, 1);
    assert.equal(textareaFn().value, textareaValue);
  });
  TTT.startTest();
});

QUnit.test("Required: Submit with content.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let textareaFn = testFrameQuerySelectorFn("#page\\:mainForm\\:required\\:textarea\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:required\\:submit_r");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    textareaFn().value = "some content";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(messagesFn().length, 0);
    assert.equal(textareaFn().value, "some content");
  });
  TTT.startTest();
});

QUnit.test("Validate Length: Submit single character.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:validateLength\\:in_vl\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:validateLength\\:submit_vl");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "a";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Validate Length: Submit two character.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:validateLength\\:in_vl\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:validateLength\\:submit_vl");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "ab";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Validate Range: Submit no number.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "no number";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Validate Range: Submit number '2' which is out of range.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "2";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Validate Range: Submit number '78' which is out of range.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "78";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Validate Range: Submit number '64' which is within the range.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "64";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Regex Validation: Submit 'T' which violates the pattern.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:regexValidation\\:in_rv\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:regexValidation\\:submit_rv");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "T";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Regex Validation: Submit '3' which violates the pattern.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:regexValidation\\:in_rv\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:regexValidation\\:submit_rv");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "3";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Regex Validation: Submit 'T3' which is accepted.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:regexValidation\\:in_rv\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:regexValidation\\:submit_rv");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "T3";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Custom Validator: Submit rejected string.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:customValidator\\:in_cv\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:customValidator\\:submit_cv");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "java";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Custom Validator: Submit accepted string.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:customValidator\\:in_cv\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:customValidator\\:submit_cv");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "tobago";
    submitFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});
