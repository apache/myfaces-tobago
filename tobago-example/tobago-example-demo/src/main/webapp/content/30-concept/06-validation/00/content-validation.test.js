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

Selectors = {};
Selectors.testframe = "#page\\:testframe";
Selectors.messages = "#page\\:messages .tobago-messages";
Selectors.textarea = "#page\\:mainForm\\:required\\:textarea\\:\\:field";
Selectors.requiredSubmit = "#page\\:mainForm\\:required\\:submit";
Selectors.lengthField = "#page\\:mainForm\\:validateLength\\:in\\:\\:field";
Selectors.lengthSubmit = "#page\\:mainForm\\:validateLength\\:submit";
Selectors.rangeField = "#page\\:mainForm\\:validateRange\\:in\\:\\:field";
Selectors.rangeSubmit = "#page\\:mainForm\\:validateRange\\:submit";
Selectors.regexField = "#page\\:mainForm\\:regexValidation\\:in\\:\\:field";
Selectors.regexSubmit = "#page\\:mainForm\\:regexValidation\\:submit";
Selectors.customField = "#page\\:mainForm\\:customValidator\\:in\\:\\:field";
Selectors.customSubmit = "#page\\:mainForm\\:customValidator\\:submit";

QUnit.test("Required: Submit without content.", function (assert) {
  assert.expect(2);
  var done = assert.async();

  var $messages = jQueryFrame(Selectors.messages);
  var $textarea = jQueryFrame(Selectors.textarea);
  var textareaValue = $textarea.val();
  var $submit = jQueryFrame(Selectors.requiredSubmit);

  $textarea.val("");
  $submit.click();

  jQuery(Selectors.testframe).on("load", function () {
    $messages = jQueryFrame(Selectors.messages);
    assert.equal($messages.length, 1);
    assert.equal($textarea.val(), textareaValue);
    done();
  });
});

QUnit.test("Required: Submit with content.", function (assert) {
  assert.expect(2);
  var done = assert.async();

  var $messages = jQueryFrame(Selectors.messages);
  var $textarea = jQueryFrame(Selectors.textarea);
  var $submit = jQueryFrame(Selectors.requiredSubmit);

  $textarea.val("some content");
  $submit.click();

  jQuery(Selectors.testframe).on("load", function () {
    $messages = jQueryFrame(Selectors.messages);
    $textarea = jQueryFrame(Selectors.textarea);
    assert.equal($messages.length, 0);
    assert.equal($textarea.val(), "some content");
    done();
  });
});

QUnit.test("Validate Length: Submit single character.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame(Selectors.messages);
  var $in = jQueryFrame(Selectors.lengthField);
  var $submit = jQueryFrame(Selectors.lengthSubmit);

  $in.val("a");
  $submit.click();

  jQuery(Selectors.testframe).on("load", function () {
    $messages = jQueryFrame(Selectors.messages);
    assert.equal($messages.length, 1);
    done();
  });
});

QUnit.test("Validate Length: Submit two character.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame(Selectors.messages);
  var $in = jQueryFrame(Selectors.lengthField);
  var $submit = jQueryFrame(Selectors.lengthSubmit);

  $in.val("ab");
  $submit.click();

  jQuery(Selectors.testframe).on("load", function () {
    $messages = jQueryFrame(Selectors.messages);
    assert.equal($messages.length, 0);
    done();
  });
});

QUnit.test("Validate Range: Submit no number.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame(Selectors.messages);
  var $in = jQueryFrame(Selectors.rangeField);
  var $submit = jQueryFrame(Selectors.rangeSubmit);

  $in.val("no number");
  $submit.click();

  jQuery(Selectors.testframe).on("load", function () {
    $messages = jQueryFrame(Selectors.messages);
    assert.equal($messages.length, 1);
    done();
  });
});

QUnit.test("Validate Range: Submit number '2' which is out of range.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame(Selectors.messages);
  var $in = jQueryFrame(Selectors.rangeField);
  var $submit = jQueryFrame(Selectors.rangeSubmit);

  $in.val("2");
  $submit.click();

  jQuery(Selectors.testframe).on("load", function () {
    $messages = jQueryFrame(Selectors.messages);
    assert.equal($messages.length, 1);
    done();
  });
});

QUnit.test("Validate Range: Submit number '78' which is out of range.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame(Selectors.messages);
  var $in = jQueryFrame(Selectors.rangeField);
  var $submit = jQueryFrame(Selectors.rangeSubmit);

  $in.val("78");
  $submit.click();

  jQuery(Selectors.testframe).on("load", function () {
    $messages = jQueryFrame(Selectors.messages);
    assert.equal($messages.length, 1);
    done();
  });
});

QUnit.test("Validate Range: Submit number '64' which is within the range.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame(Selectors.messages);
  var $in = jQueryFrame(Selectors.rangeField);
  var $submit = jQueryFrame(Selectors.rangeSubmit);

  $in.val("64");
  $submit.click();

  jQuery(Selectors.testframe).on("load", function () {
    $messages = jQueryFrame(Selectors.messages);
    assert.equal($messages.length, 0);
    done();
  });
});

QUnit.test("Regex Validation: Submit 'T' which violates the pattern.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame(Selectors.messages);
  var $in = jQueryFrame(Selectors.regexField);
  var $submit = jQueryFrame(Selectors.regexSubmit);

  $in.val("T");
  $submit.click();

  jQuery(Selectors.testframe).on("load", function () {
    $messages = jQueryFrame(Selectors.messages);
    assert.equal($messages.length, 1);
    done();
  });
});

QUnit.test("Regex Validation: Submit '3' which violates the pattern.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame(Selectors.messages);
  var $in = jQueryFrame(Selectors.regexField);
  var $submit = jQueryFrame(Selectors.regexSubmit);

  $in.val("3");
  $submit.click();

  jQuery(Selectors.testframe).on("load", function () {
    $messages = jQueryFrame(Selectors.messages);
    assert.equal($messages.length, 1);
    done();
  });
});

QUnit.test("Regex Validation: Submit 'T3' which is accepted.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame(Selectors.messages);
  var $in = jQueryFrame(Selectors.regexField);
  var $submit = jQueryFrame(Selectors.regexSubmit);

  $in.val("T3");
  $submit.click();

  jQuery(Selectors.testframe).on("load", function () {
    $messages = jQueryFrame(Selectors.messages);
    assert.equal($messages.length, 0);
    done();
  });
});

QUnit.test("Custom Validator: Submit rejected string.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame(Selectors.messages);
  var $in = jQueryFrame(Selectors.customField);
  var $submit = jQueryFrame(Selectors.customSubmit);

  $in.val("java");
  $submit.click();

  jQuery(Selectors.testframe).on("load", function () {
    $messages = jQueryFrame(Selectors.messages);
    assert.equal($messages.length, 1);
    done();
  });
});

QUnit.test("Custom Validator: Submit accepted string.", function (assert) {
  assert.expect(1);
  var done = assert.async();

  var $messages = jQueryFrame(Selectors.messages);
  var $in = jQueryFrame(Selectors.customField);
  var $submit = jQueryFrame(Selectors.customSubmit);

  $in.val("tobago");
  $submit.click();

  jQuery(Selectors.testframe).on("load", function () {
    $messages = jQueryFrame(Selectors.messages);
    assert.equal($messages.length, 0);
    done();
  });
});
