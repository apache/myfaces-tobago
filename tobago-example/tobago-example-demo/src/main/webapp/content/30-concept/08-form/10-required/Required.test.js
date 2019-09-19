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

QUnit.test("submit inner form 1 without violations", function (assert) {
  let form1InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:in1\\:\\:field");
  let form1SubmitButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:submit1");
  let form1OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:out1 span");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form1InputFieldFn().value = "Alice";
    form1SubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(form1InputFieldFn().value, "Alice");
    assert.equal(form1OutputFieldFn().textContent, "Alice");
    assert.equal(alertFn().length, "0");
  });
  TTT.startTest();
});

QUnit.test("submit inner form 2, violate required field", function (assert) {
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let form2SubmitButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:submit2");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form2InputFieldFn().value = "";
  });
  TTT.asserts(1, function () {
    assert.equal(form2InputFieldFn().value, "");
  });
  TTT.action(function () {
    form2SubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(form2InputFieldFn().value, "");
    assert.equal(form2OutputFieldFn().textContent, form2OutputFieldValue);
    assert.equal(alertFn().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit inner form 2 without violations", function (assert) {
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let form2SubmitButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:submit2");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form2InputFieldFn().value = "Bob";
  });
  TTT.asserts(1, function () {
    assert.equal(form2InputFieldFn().value, "Bob");
  });
  TTT.action(function () {
    form2SubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(form2InputFieldFn().value, "Bob");
    assert.equal(form2OutputFieldFn().textContent, "Bob");
    assert.equal(alertFn().length, "0");
  });
  TTT.startTest();
});

QUnit.test("submit outer form, violate both required fields", function (assert) {
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let outerFormInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormSubmitButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let outerFormOutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form2InputFieldFn().value = "";
    outerFormInputFieldFn().value = "";
    outerFormSubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal(form2InputFieldFn().value, "");
    assert.equal(form2OutputFieldFn().textContent, form2OutputFieldValue);
    assert.equal(outerFormInputFieldFn().value, "");
    assert.equal(outerFormOutputFieldFn().textContent, outerFormOutputFieldValue);
    assert.equal(alertFn().length, "2");
  });
  TTT.startTest();
});

QUnit.test("submit outer form, violate required field in form 2", function (assert) {
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let outerFormInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormSubmitButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let outerFormOutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form2InputFieldFn().value = "";
    outerFormInputFieldFn().value = "Charlie";
    outerFormSubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal(form2InputFieldFn().value, "");
    assert.equal(form2OutputFieldFn().textContent, form2OutputFieldValue);
    assert.equal(outerFormInputFieldFn().value, "Charlie");
    assert.equal(outerFormOutputFieldFn().textContent, outerFormOutputFieldValue);
    assert.equal(alertFn().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit outer form, violate required field in outer form", function (assert) {
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let outerFormInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormSubmitButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let outerFormOutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form2InputFieldFn().value = "Dave";
    outerFormInputFieldFn().value = "";
    outerFormSubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal(form2InputFieldFn().value, "Dave");
    assert.equal(form2OutputFieldFn().textContent, form2OutputFieldValue);
    assert.equal(outerFormInputFieldFn().value, "");
    assert.equal(outerFormOutputFieldFn().textContent, outerFormOutputFieldValue);
    assert.equal(alertFn().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit outer form without violations", function (assert) {
  let form1InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:in1\\:\\:field");
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:in2\\:\\:field");
  let outerFormInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormSubmitButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let form1OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm1\\:out1 span");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:innerForm2\\:out2 span");
  let outerFormOutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form1InputFieldFn().value = "Eve";
    form2InputFieldFn().value = "Frank";
    outerFormInputFieldFn().value = "Grace";
    outerFormSubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().value, "Eve");
    assert.equal(form1OutputFieldFn().textContent, "Eve");
    assert.equal(form2InputFieldFn().value, "Frank");
    assert.equal(form2OutputFieldFn().textContent, "Frank");
    assert.equal(outerFormInputFieldFn().value, "Grace");
    assert.equal(outerFormOutputFieldFn().textContent, "Grace");
    assert.equal(alertFn().length, "0");
  });
  TTT.startTest();
});
