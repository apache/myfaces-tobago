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

QUnit.test("submit inner form 1 without violations", function (assert) {
  let $form1InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:in1\\:\\:field");
  let $form1SubmitButton = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:submit1");
  let $form1OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:out1 span");
  let $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form1InputField().val("Alice");
    $form1SubmitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($form1InputField().val(), "Alice");
    assert.equal($form1OutputField().text(), "Alice");
    assert.equal($alert().length, "0");
  });
  TTT.startTest();
});

QUnit.test("submit inner form 2, violate required field", function (assert) {
  let $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let $form2SubmitButton = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:submit2");
  let $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let form2OutputFieldValue = $form2OutputField().text();
  let $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form2InputField().val("");
  });
  TTT.asserts(1, function () {
    assert.equal($form2InputField().val(), "");
  });
  TTT.action(function () {
    $form2SubmitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($form2InputField().val(), "");
    assert.equal($form2OutputField().text(), form2OutputFieldValue);
    assert.equal($alert().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit inner form 2 without violations", function (assert) {
  let $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let $form2SubmitButton = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:submit2");
  let $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form2InputField().val("Bob");
  });
  TTT.asserts(1, function () {
    assert.equal($form2InputField().val(), "Bob");
  });
  TTT.action(function () {
    $form2SubmitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal($form2InputField().val(), "Bob");
    assert.equal($form2OutputField().text(), "Bob");
    assert.equal($alert().length, "0");
  });
  TTT.startTest();
});

QUnit.test("submit outer form, violate both required fields", function (assert) {
  let $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let $outerFormInputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let $outerFormSubmitButton = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submit");
  let $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let $outerFormOutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  let $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  let form2OutputFieldValue = $form2OutputField().text();
  let outerFormOutputFieldValue = $outerFormOutputField().text();

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form2InputField().val("");
    $outerFormInputField().val("");
    $outerFormSubmitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal($form2InputField().val(), "");
    assert.equal($form2OutputField().text(), form2OutputFieldValue);
    assert.equal($outerFormInputField().val(), "");
    assert.equal($outerFormOutputField().text(), outerFormOutputFieldValue);
    assert.equal($alert().length, "2");
  });
  TTT.startTest();
});

QUnit.test("submit outer form, violate required field in form 2", function (assert) {
  let $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let $outerFormInputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let $outerFormSubmitButton = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submit");
  let $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let $outerFormOutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  let $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  let form2OutputFieldValue = $form2OutputField().text();
  let outerFormOutputFieldValue = $outerFormOutputField().text();

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form2InputField().val("");
    $outerFormInputField().val("Charlie");
    $outerFormSubmitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal($form2InputField().val(), "");
    assert.equal($form2OutputField().text(), form2OutputFieldValue);
    assert.equal($outerFormInputField().val(), "Charlie");
    assert.equal($outerFormOutputField().text(), outerFormOutputFieldValue);
    assert.equal($alert().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit outer form, violate required field in outer form", function (assert) {
  let $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let $outerFormInputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let $outerFormSubmitButton = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submit");
  let $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let $outerFormOutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  let $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  let form2OutputFieldValue = $form2OutputField().text();
  let outerFormOutputFieldValue = $outerFormOutputField().text();

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form2InputField().val("Dave");
    $outerFormInputField().val("");
    $outerFormSubmitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal($form2InputField().val(), "Dave");
    assert.equal($form2OutputField().text(), form2OutputFieldValue);
    assert.equal($outerFormInputField().val(), "");
    assert.equal($outerFormOutputField().text(), outerFormOutputFieldValue);
    assert.equal($alert().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit outer form without violations", function (assert) {
  let $form1InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:in1\\:\\:field");
  let $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let $outerFormInputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let $outerFormSubmitButton = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submit");
  let $form1OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:out1 span");
  let $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let $outerFormOutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  let $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form1InputField().val("Eve");
    $form2InputField().val("Frank");
    $outerFormInputField().val("Grace");
    $outerFormSubmitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal($form1InputField().val(), "Eve");
    assert.equal($form1OutputField().text(), "Eve");
    assert.equal($form2InputField().val(), "Frank");
    assert.equal($form2OutputField().text(), "Frank");
    assert.equal($outerFormInputField().val(), "Grace");
    assert.equal($outerFormOutputField().text(), "Grace");
    assert.equal($alert().length, "0");
  });
  TTT.startTest();
});
