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
  let form1InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:submit1");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form1InputFieldFn().value = "Alice";
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
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
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:submit2");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let form2OutputFieldValue = form2OutputFieldFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form2InputFieldFn().value = "";
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
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
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:submit2");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form2InputFieldFn().value = "Bob";
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
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
  let form1InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().textContent;
  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form1InputFieldFn().value = "Charlie";
    form2InputFieldFn().value = "";
    outerFormInputFieldFn().value = "";
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().value, "Charlie");
    assert.equal(form1OutputFieldFn().textContent, form1OutputFieldValue);
    assert.equal(form2InputFieldFn().value, "");
    assert.equal(form2OutputFieldFn().textContent, form2OutputFieldValue);
    assert.equal(outerFormInputFieldFn().value, "");
    assert.equal(outerFormOutputFieldFn().textContent, outerFormOutputFieldValue);
    assert.equal(alertFn().length, "2");
  });
  TTT.startTest();
});

QUnit.test("submit outer form, violate required field in form 2", function (assert) {
  let form1InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().textContent;
  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form1InputFieldFn().value = "Dave";
    form2InputFieldFn().value = "";
    outerFormInputFieldFn().value = "Eve";
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().value, "Dave");
    assert.equal(form1OutputFieldFn().textContent, form1OutputFieldValue);
    assert.equal(form2InputFieldFn().value, "");
    assert.equal(form2OutputFieldFn().textContent, form2OutputFieldValue);
    assert.equal(outerFormInputFieldFn().value, "Eve");
    assert.equal(outerFormOutputFieldFn().textContent, outerFormOutputFieldValue);
    assert.equal(alertFn().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit outer form, violate required field in outer form", function (assert) {
  let form1InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().textContent;
  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form1InputFieldFn().value = "Frank";
    form2InputFieldFn().value = "Grace";
    outerFormInputFieldFn().value = "";
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().value, "Frank");
    assert.equal(form1OutputFieldFn().textContent, form1OutputFieldValue);
    assert.equal(form2InputFieldFn().value, "Grace");
    assert.equal(form2OutputFieldFn().textContent, form2OutputFieldValue);
    assert.equal(outerFormInputFieldFn().value, "");
    assert.equal(outerFormOutputFieldFn().textContent, outerFormOutputFieldValue);
    assert.equal(alertFn().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit outer form without violations", function (assert) {
  let form1InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:submit");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form1InputFieldFn().value = "Hank";
    form2InputFieldFn().value = "Irene";
    outerFormInputFieldFn().value = "John";
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().value, "Hank");
    assert.equal(form1OutputFieldFn().textContent, "Hank");
    assert.equal(form2InputFieldFn().value, "Irene");
    assert.equal(form2OutputFieldFn().textContent, "Irene");
    assert.equal(outerFormInputFieldFn().value, "John");
    assert.equal(outerFormOutputFieldFn().textContent, "John");
    assert.equal(alertFn().length, "0");
  });
  TTT.startTest();
});

QUnit.test("submit inner forms, violate required field in form 2", function (assert) {
  let form1InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:submitInnerForms");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().textContent;
  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form1InputFieldFn().value = "Kate";
    form2InputFieldFn().value = "";
    outerFormInputFieldFn().value = "Leonard";
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().value, "Kate");
    assert.equal(form1OutputFieldFn().textContent, form1OutputFieldValue);
    assert.equal(form2InputFieldFn().value, "");
    assert.equal(form2OutputFieldFn().textContent, form2OutputFieldValue);
    assert.equal(outerFormInputFieldFn().value, "Leonard");
    assert.equal(outerFormOutputFieldFn().textContent, outerFormOutputFieldValue);
    assert.equal(alertFn().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit inner forms without violations", function (assert) {
  let form1InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:submitInnerForms");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form1InputFieldFn().value = "Mike";
    form2InputFieldFn().value = "Neil";
    outerFormInputFieldFn().value = "";
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().value, "Mike");
    assert.equal(form1OutputFieldFn().textContent, "Mike");
    assert.equal(form2InputFieldFn().value, "Neil");
    assert.equal(form2OutputFieldFn().textContent, "Neil");
    assert.equal(outerFormInputFieldFn().value, "");
    assert.equal(outerFormOutputFieldFn().textContent, outerFormOutputFieldValue);
    assert.equal(alertFn().length, "0");
  });
  TTT.startTest();
});

QUnit.test("submit outer value, violate required field", function (assert) {
  let form1InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:submitOuterValue");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().textContent;
  let form2OutputFieldValue = form2OutputFieldFn().textContent;
  let outerFormOutputFieldValue = outerFormOutputFieldFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form1InputFieldFn().value = "Oscar";
    form2InputFieldFn().value = "Penny";
    outerFormInputFieldFn().value = "";
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().value, "Oscar");
    assert.equal(form1OutputFieldFn().textContent, form1OutputFieldValue);
    assert.equal(form2InputFieldFn().value, "Penny");
    assert.equal(form2OutputFieldFn().textContent, form2OutputFieldValue);
    assert.equal(outerFormInputFieldFn().value, "");
    assert.equal(outerFormOutputFieldFn().textContent, outerFormOutputFieldValue);
    assert.equal(alertFn().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit outer value without violations", function (assert) {
  let form1InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outerForm\\:submitOuterValue");
  let alertFn = testFrameQuerySelectorAllFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().textContent;
  let form2OutputFieldValue = form2OutputFieldFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form1InputFieldFn().value = "Quin";
    form2InputFieldFn().value = "Sue";
    outerFormInputFieldFn().value = "Ted";
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().value, "Quin");
    assert.equal(form1OutputFieldFn().textContent, form1OutputFieldValue);
    assert.equal(form2InputFieldFn().value, "Sue");
    assert.equal(form2OutputFieldFn().textContent, form2OutputFieldValue);
    assert.equal(outerFormInputFieldFn().value, "Ted");
    assert.equal(outerFormOutputFieldFn().textContent, "Ted");
    assert.equal(alertFn().length, "0");
  });
  TTT.startTest();
});
