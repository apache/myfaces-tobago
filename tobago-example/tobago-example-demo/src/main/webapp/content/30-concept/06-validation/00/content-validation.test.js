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
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $textarea = jQueryFrameFn("#page\\:mainForm\\:required\\:textarea\\:\\:field");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:required\\:submit_r");
  var textareaValue = $textarea().val();

  var TTT = new TobagoTestTools(assert);
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
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $textarea = jQueryFrameFn("#page\\:mainForm\\:required\\:textarea\\:\\:field");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:required\\:submit_r");

  var TTT = new TobagoTestTools(assert);
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
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $in = jQueryFrameFn("#page\\:mainForm\\:validateLength\\:in_vl\\:\\:field");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:validateLength\\:submit_vl");

  var TTT = new TobagoTestTools(assert);
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
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $in = jQueryFrameFn("#page\\:mainForm\\:validateLength\\:in_vl\\:\\:field");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:validateLength\\:submit_vl");

  var TTT = new TobagoTestTools(assert);
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
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $in = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  var TTT = new TobagoTestTools(assert);
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
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $in = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  var TTT = new TobagoTestTools(assert);
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
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $in = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  var TTT = new TobagoTestTools(assert);
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
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $in = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  var TTT = new TobagoTestTools(assert);
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
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $in = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:in_rv\\:\\:field");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:submit_rv");

  var TTT = new TobagoTestTools(assert);
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
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $in = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:in_rv\\:\\:field");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:submit_rv");

  var TTT = new TobagoTestTools(assert);
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
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $in = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:in_rv\\:\\:field");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:regexValidation\\:submit_rv");

  var TTT = new TobagoTestTools(assert);
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
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $in = jQueryFrameFn("#page\\:mainForm\\:customValidator\\:in_cv\\:\\:field");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:customValidator\\:submit_cv");

  var TTT = new TobagoTestTools(assert);
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
  var $messages = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var $in = jQueryFrameFn("#page\\:mainForm\\:customValidator\\:in_cv\\:\\:field");
  var $submit = jQueryFrameFn("#page\\:mainForm\\:customValidator\\:submit_cv");

  var TTT = new TobagoTestTools(assert);
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
