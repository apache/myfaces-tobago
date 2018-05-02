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
  var $form1InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  var $form1OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  var $button = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:submit1");
  var $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form1InputField().val("Alice");
    $button().click();
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
  var $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  var $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  var $button = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:submit2");
  var $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  var form2OutputFieldValue = $form2OutputField().text();

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form2InputField().val("");
    $button().click();
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
  var $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  var $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  var $button = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:submit2");
  var $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form2InputField().val("Bob");
    $button().click();
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
  var $form1InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  var $form1OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  var $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  var $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  var $outerFormInputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  var $button = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submit");
  var $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  var form1OutputFieldValue = $form1OutputField().text();
  var form2OutputFieldValue = $form2OutputField().text();
  var outerFormOutputFieldValue = $outerFormOutputField().text();

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form1InputField().val("Charlie");
    $form2InputField().val("");
    $outerFormInputField().val("");
    $button().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal($form1InputField().val(), "Charlie");
    assert.equal($form1OutputField().text(), form1OutputFieldValue);
    assert.equal($form2InputField().val(), "");
    assert.equal($form2OutputField().text(), form2OutputFieldValue);
    assert.equal($outerFormInputField().val(), "");
    assert.equal($outerFormOutputField().text(), outerFormOutputFieldValue);
    assert.equal($alert().length, "2");
  });
  TTT.startTest();
});

QUnit.test("submit outer form, violate required field in form 2", function (assert) {
  var $form1InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  var $form1OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  var $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  var $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  var $outerFormInputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  var $button = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submit");
  var $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  var form1OutputFieldValue = $form1OutputField().text();
  var form2OutputFieldValue = $form2OutputField().text();
  var outerFormOutputFieldValue = $outerFormOutputField().text();

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form1InputField().val("Dave");
    $form2InputField().val("");
    $outerFormInputField().val("Eve");
    $button().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal($form1InputField().val(), "Dave");
    assert.equal($form1OutputField().text(), form1OutputFieldValue);
    assert.equal($form2InputField().val(), "");
    assert.equal($form2OutputField().text(), form2OutputFieldValue);
    assert.equal($outerFormInputField().val(), "Eve");
    assert.equal($outerFormOutputField().text(), outerFormOutputFieldValue);
    assert.equal($alert().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit outer form, violate required field in outer form", function (assert) {
  var $form1InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  var $form1OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  var $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  var $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  var $outerFormInputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  var $button = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submit");
  var $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  var form1OutputFieldValue = $form1OutputField().text();
  var form2OutputFieldValue = $form2OutputField().text();
  var outerFormOutputFieldValue = $outerFormOutputField().text();

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form1InputField().val("Frank");
    $form2InputField().val("Grace");
    $outerFormInputField().val("");
    $button().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal($form1InputField().val(), "Frank");
    assert.equal($form1OutputField().text(), form1OutputFieldValue);
    assert.equal($form2InputField().val(), "Grace");
    assert.equal($form2OutputField().text(), form2OutputFieldValue);
    assert.equal($outerFormInputField().val(), "");
    assert.equal($outerFormOutputField().text(), outerFormOutputFieldValue);
    assert.equal($alert().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit outer form without violations", function (assert) {
  var $form1InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  var $form1OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  var $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  var $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  var $outerFormInputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  var $button = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submit");
  var $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form1InputField().val("Hank");
    $form2InputField().val("Irene");
    $outerFormInputField().val("John");
    $button().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal($form1InputField().val(), "Hank");
    assert.equal($form1OutputField().text(), "Hank");
    assert.equal($form2InputField().val(), "Irene");
    assert.equal($form2OutputField().text(), "Irene");
    assert.equal($outerFormInputField().val(), "John");
    assert.equal($outerFormOutputField().text(), "John");
    assert.equal($alert().length, "0");
  });
  TTT.startTest();
});

QUnit.test("submit inner forms, violate required field in form 2", function (assert) {
  var $form1InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  var $form1OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  var $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  var $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  var $outerFormInputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  var $button = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submitInnerForms");
  var $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  var form1OutputFieldValue = $form1OutputField().text();
  var form2OutputFieldValue = $form2OutputField().text();
  var outerFormOutputFieldValue = $outerFormOutputField().text();

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form1InputField().val("Kate");
    $form2InputField().val("");
    $outerFormInputField().val("Leonard");
    $button().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal($form1InputField().val(), "Kate");
    assert.equal($form1OutputField().text(), form1OutputFieldValue);
    assert.equal($form2InputField().val(), "");
    assert.equal($form2OutputField().text(), form2OutputFieldValue);
    assert.equal($outerFormInputField().val(), "Leonard");
    assert.equal($outerFormOutputField().text(), outerFormOutputFieldValue);
    assert.equal($alert().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit inner forms without violations", function (assert) {
  var $form1InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  var $form1OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  var $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  var $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  var $outerFormInputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  var $button = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submitInnerForms");
  var $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  var outerFormOutputFieldValue = $outerFormOutputField().text();

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form1InputField().val("Mike");
    $form2InputField().val("Neil");
    $outerFormInputField().val("");
    $button().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal($form1InputField().val(), "Mike");
    assert.equal($form1OutputField().text(), "Mike");
    assert.equal($form2InputField().val(), "Neil");
    assert.equal($form2OutputField().text(), "Neil");
    assert.equal($outerFormInputField().val(), "");
    assert.equal($outerFormOutputField().text(), outerFormOutputFieldValue);
    assert.equal($alert().length, "0");
  });
  TTT.startTest();
});

QUnit.test("submit outer value, violate required field", function (assert) {
  var $form1InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  var $form1OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  var $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  var $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  var $outerFormInputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  var $button = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submitOuterValue");
  var $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  var form1OutputFieldValue = $form1OutputField().text();
  var form2OutputFieldValue = $form2OutputField().text();
  var outerFormOutputFieldValue = $outerFormOutputField().text();

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form1InputField().val("Oscar");
    $form2InputField().val("Penny");
    $outerFormInputField().val("");
    $button().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal($form1InputField().val(), "Oscar");
    assert.equal($form1OutputField().text(), form1OutputFieldValue);
    assert.equal($form2InputField().val(), "Penny");
    assert.equal($form2OutputField().text(), form2OutputFieldValue);
    assert.equal($outerFormInputField().val(), "");
    assert.equal($outerFormOutputField().text(), outerFormOutputFieldValue);
    assert.equal($alert().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit outer value without violations", function (assert) {
  var $form1InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  var $form1OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  var $form2InputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  var $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  var $outerFormInputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  var $button = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submitOuterValue");
  var $alert = jQueryFrameFn("#page\\:messages .alert-danger label");

  var form1OutputFieldValue = $form1OutputField().text();
  var form2OutputFieldValue = $form2OutputField().text();

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form1InputField().val("Quin");
    $form2InputField().val("Sue");
    $outerFormInputField().val("Ted");
    $button().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal($form1InputField().val(), "Quin");
    assert.equal($form1OutputField().text(), form1OutputFieldValue);
    assert.equal($form2InputField().val(), "Sue");
    assert.equal($form2OutputField().text(), form2OutputFieldValue);
    assert.equal($outerFormInputField().val(), "Ted");
    assert.equal($outerFormOutputField().text(), "Ted");
    assert.equal($alert().length, "0");
  });
  TTT.startTest();
});
