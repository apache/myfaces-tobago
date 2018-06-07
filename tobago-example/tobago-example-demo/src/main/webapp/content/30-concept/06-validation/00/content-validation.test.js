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
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $textarea = jQueryFrameFn("#page\\:mainForm\\:required\\:textarea\\:\\:field");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:required\\:submit_r");
  let textareaValue = $textarea().val();

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $textarea().val("");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($messages().length, 1);
    assert.equal($textarea().val(), textareaValue);
  });
  TTT.startTest();
});

QUnit.test("Required: Submit with content.", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $textarea = jQueryFrameFn("#page\\:mainForm\\:required\\:textarea\\:\\:field");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:required\\:submit_r");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $textarea().val("some content");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($messages().length, 0);
    assert.equal($textarea().val(), "some content");
  });
  TTT.startTest();
});

QUnit.test("Validate Length: Submit single character.", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $in = jQueryFrameFn("#page\\:mainForm\\:validateLength\\:in_vl\\:\\:field");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:validateLength\\:submit_vl");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val("a");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Validate Length: Submit two character.", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $in = jQueryFrameFn("#page\\:mainForm\\:validateLength\\:in_vl\\:\\:field");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:validateLength\\:submit_vl");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val("ab");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Validate Range: Submit no number.", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $in = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val("no number");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Validate Range: Submit number '2' which is out of range.", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $in = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val("2");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Validate Range: Submit number '78' which is out of range.", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $in = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val("78");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Validate Range: Submit number '64' which is within the range.", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $in = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val("64");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Regex Validation: Submit 'T' which violates the pattern.", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $in = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:in_rv\\:\\:field");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:submit_rv");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val("T");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Regex Validation: Submit '3' which violates the pattern.", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $in = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:in_rv\\:\\:field");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:submit_rv");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val("3");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Regex Validation: Submit 'T3' which is accepted.", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $in = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:in_rv\\:\\:field");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:submit_rv");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val("T3");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Custom Validator: Submit rejected string.", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $in = jQueryFrameFn("#page\\:mainForm\\:customValidator\\:in_cv\\:\\:field");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:customValidator\\:submit_cv");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val("java");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Custom Validator: Submit accepted string.", function (assert) {
  let $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  let $in = jQueryFrameFn("#page\\:mainForm\\:customValidator\\:in_cv\\:\\:field");
  let $submit = jQueryFrameFn("#page\\:mainForm\\:customValidator\\:submit_cv");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $in().val("tobago");
    $submit().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($messages().length, 0);
  });
  TTT.startTest();
});
