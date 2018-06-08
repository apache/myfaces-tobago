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
  let form1InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let buttonFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:submit1");
  let alertFn = jQueryFrameFn("#page\\:messages .alert-danger label");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    form1InputFieldFn().val("Alice");
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(form1InputFieldFn().val(), "Alice");
    assert.equal(form1OutputFieldFn().text(), "Alice");
    assert.equal(alertFn().length, "0");
  });
  TTT.startTest();
});


QUnit.test("submit inner form 2, violate required field", function (assert) {
  let form2InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let buttonFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:submit2");
  let alertFn = jQueryFrameFn("#page\\:messages .alert-danger label");

  let form2OutputFieldValue = form2OutputFieldFn().text();

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    form2InputFieldFn().val("");
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(form2InputFieldFn().val(), "");
    assert.equal(form2OutputFieldFn().text(), form2OutputFieldValue);
    assert.equal(alertFn().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit inner form 2 without violations", function (assert) {
  let form2InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let buttonFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:submit2");
  let alertFn = jQueryFrameFn("#page\\:messages .alert-danger label");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    form2InputFieldFn().val("Bob");
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.equal(form2InputFieldFn().val(), "Bob");
    assert.equal(form2OutputFieldFn().text(), "Bob");
    assert.equal(alertFn().length, "0");
  });
  TTT.startTest();
});

QUnit.test("submit outer form, violate both required fields", function (assert) {
  let form1InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submit");
  let alertFn = jQueryFrameFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().text();
  let form2OutputFieldValue = form2OutputFieldFn().text();
  let outerFormOutputFieldValue = outerFormOutputFieldFn().text();

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    form1InputFieldFn().val("Charlie");
    form2InputFieldFn().val("");
    outerFormInputFieldFn().val("");
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().val(), "Charlie");
    assert.equal(form1OutputFieldFn().text(), form1OutputFieldValue);
    assert.equal(form2InputFieldFn().val(), "");
    assert.equal(form2OutputFieldFn().text(), form2OutputFieldValue);
    assert.equal(outerFormInputFieldFn().val(), "");
    assert.equal(outerFormOutputFieldFn().text(), outerFormOutputFieldValue);
    assert.equal(alertFn().length, "2");
  });
  TTT.startTest();
});

QUnit.test("submit outer form, violate required field in form 2", function (assert) {
  let form1InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submit");
  let alertFn = jQueryFrameFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().text();
  let form2OutputFieldValue = form2OutputFieldFn().text();
  let outerFormOutputFieldValue = outerFormOutputFieldFn().text();

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    form1InputFieldFn().val("Dave");
    form2InputFieldFn().val("");
    outerFormInputFieldFn().val("Eve");
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().val(), "Dave");
    assert.equal(form1OutputFieldFn().text(), form1OutputFieldValue);
    assert.equal(form2InputFieldFn().val(), "");
    assert.equal(form2OutputFieldFn().text(), form2OutputFieldValue);
    assert.equal(outerFormInputFieldFn().val(), "Eve");
    assert.equal(outerFormOutputFieldFn().text(), outerFormOutputFieldValue);
    assert.equal(alertFn().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit outer form, violate required field in outer form", function (assert) {
  let form1InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submit");
  let alertFn = jQueryFrameFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().text();
  let form2OutputFieldValue = form2OutputFieldFn().text();
  let outerFormOutputFieldValue = outerFormOutputFieldFn().text();

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    form1InputFieldFn().val("Frank");
    form2InputFieldFn().val("Grace");
    outerFormInputFieldFn().val("");
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().val(), "Frank");
    assert.equal(form1OutputFieldFn().text(), form1OutputFieldValue);
    assert.equal(form2InputFieldFn().val(), "Grace");
    assert.equal(form2OutputFieldFn().text(), form2OutputFieldValue);
    assert.equal(outerFormInputFieldFn().val(), "");
    assert.equal(outerFormOutputFieldFn().text(), outerFormOutputFieldValue);
    assert.equal(alertFn().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit outer form without violations", function (assert) {
  let form1InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submit");
  let alertFn = jQueryFrameFn("#page\\:messages .alert-danger label");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    form1InputFieldFn().val("Hank");
    form2InputFieldFn().val("Irene");
    outerFormInputFieldFn().val("John");
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().val(), "Hank");
    assert.equal(form1OutputFieldFn().text(), "Hank");
    assert.equal(form2InputFieldFn().val(), "Irene");
    assert.equal(form2OutputFieldFn().text(), "Irene");
    assert.equal(outerFormInputFieldFn().val(), "John");
    assert.equal(outerFormOutputFieldFn().text(), "John");
    assert.equal(alertFn().length, "0");
  });
  TTT.startTest();
});

QUnit.test("submit inner forms, violate required field in form 2", function (assert) {
  let form1InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submitInnerForms");
  let alertFn = jQueryFrameFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().text();
  let form2OutputFieldValue = form2OutputFieldFn().text();
  let outerFormOutputFieldValue = outerFormOutputFieldFn().text();

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    form1InputFieldFn().val("Kate");
    form2InputFieldFn().val("");
    outerFormInputFieldFn().val("Leonard");
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().val(), "Kate");
    assert.equal(form1OutputFieldFn().text(), form1OutputFieldValue);
    assert.equal(form2InputFieldFn().val(), "");
    assert.equal(form2OutputFieldFn().text(), form2OutputFieldValue);
    assert.equal(outerFormInputFieldFn().val(), "Leonard");
    assert.equal(outerFormOutputFieldFn().text(), outerFormOutputFieldValue);
    assert.equal(alertFn().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit inner forms without violations", function (assert) {
  let form1InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submitInnerForms");
  let alertFn = jQueryFrameFn("#page\\:messages .alert-danger label");

  let outerFormOutputFieldValue = outerFormOutputFieldFn().text();

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    form1InputFieldFn().val("Mike");
    form2InputFieldFn().val("Neil");
    outerFormInputFieldFn().val("");
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().val(), "Mike");
    assert.equal(form1OutputFieldFn().text(), "Mike");
    assert.equal(form2InputFieldFn().val(), "Neil");
    assert.equal(form2OutputFieldFn().text(), "Neil");
    assert.equal(outerFormInputFieldFn().val(), "");
    assert.equal(outerFormOutputFieldFn().text(), outerFormOutputFieldValue);
    assert.equal(alertFn().length, "0");
  });
  TTT.startTest();
});

QUnit.test("submit outer value, violate required field", function (assert) {
  let form1InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submitOuterValue");
  let alertFn = jQueryFrameFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().text();
  let form2OutputFieldValue = form2OutputFieldFn().text();
  let outerFormOutputFieldValue = outerFormOutputFieldFn().text();

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    form1InputFieldFn().val("Oscar");
    form2InputFieldFn().val("Penny");
    outerFormInputFieldFn().val("");
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().val(), "Oscar");
    assert.equal(form1OutputFieldFn().text(), form1OutputFieldValue);
    assert.equal(form2InputFieldFn().val(), "Penny");
    assert.equal(form2OutputFieldFn().text(), form2OutputFieldValue);
    assert.equal(outerFormInputFieldFn().val(), "");
    assert.equal(outerFormOutputFieldFn().text(), outerFormOutputFieldValue);
    assert.equal(alertFn().length, "1");
  });
  TTT.startTest();
});

QUnit.test("submit outer value without violations", function (assert) {
  let form1InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:in1\\:\\:field");
  let form1OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form1\\:out1 span");
  let form2InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:in2\\:\\:field");
  let form2OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:form2\\:out2 span");
  let outerFormInputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  let outerFormOutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:out span");
  let buttonFn = jQueryFrameFn("#page\\:mainForm\\:outerForm\\:submitOuterValue");
  let alertFn = jQueryFrameFn("#page\\:messages .alert-danger label");

  let form1OutputFieldValue = form1OutputFieldFn().text();
  let form2OutputFieldValue = form2OutputFieldFn().text();

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    form1InputFieldFn().val("Quin");
    form2InputFieldFn().val("Sue");
    outerFormInputFieldFn().val("Ted");
    buttonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(7, function () {
    assert.equal(form1InputFieldFn().val(), "Quin");
    assert.equal(form1OutputFieldFn().text(), form1OutputFieldValue);
    assert.equal(form2InputFieldFn().val(), "Sue");
    assert.equal(form2OutputFieldFn().text(), form2OutputFieldValue);
    assert.equal(outerFormInputFieldFn().val(), "Ted");
    assert.equal(outerFormOutputFieldFn().text(), "Ted");
    assert.equal(alertFn().length, "0");
  });
  TTT.startTest();
});
