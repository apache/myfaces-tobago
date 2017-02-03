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
  assert.expect(3);
  var done = assert.async();
  var $form1InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm1\\:in\\:\\:field");
  var $form1SubmitButton = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm1\\:submit");

  $form1InputField.val("Alice");
  $form1SubmitButton.click();

  jQuery("#page\\:testframe").load(function () {
    $form1InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm1\\:in\\:\\:field");
    var $form1OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm1\\:out span");
    var $alert = jQueryFrame("#page\\:messages .alert-danger label");

    assert.equal($form1InputField.val(), "Alice");
    assert.equal($form1OutputField.text(), "Alice");
    assert.equal($alert.length, "0");

    done();
  });
});

QUnit.test("submit inner form 2, violate required field", function (assert) {
  assert.expect(4);
  var done = assert.async();
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:in\\:\\:field");
  var $form2SubmitButton = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:submit");

  var form2OutputFieldValue = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:out span").text();

  $form2InputField.val("");
  assert.equal($form2InputField.val(), "");
  $form2SubmitButton.click();

  jQuery("#page\\:testframe").load(function () {
    $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:in\\:\\:field");
    var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:out span");
    var $alert = jQueryFrame("#page\\:messages .alert-danger label");

    assert.equal($form2InputField.val(), "");
    assert.equal($form2OutputField.text(), form2OutputFieldValue);
    assert.equal($alert.length, "1");

    done();
  });
});

QUnit.test("submit inner form 2 without violations", function (assert) {
  assert.expect(4);
  var done = assert.async();
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:in\\:\\:field");
  var $form2SubmitButton = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:submit");

  $form2InputField.val("Bob");
  assert.equal($form2InputField.val(), "Bob");
  $form2SubmitButton.click();

  jQuery("#page\\:testframe").load(function () {
    $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:in\\:\\:field");
    var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:out span");
    var $alert = jQueryFrame("#page\\:messages .alert-danger label");

    assert.equal($form2InputField.val(), "Bob");
    assert.equal($form2OutputField.text(), "Bob");
    assert.equal($alert.length, "0");

    done();
  });
});

QUnit.test("submit outer form, violate both required fields", function (assert) {
  assert.expect(5);
  var done = assert.async();
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:in\\:\\:field");
  var $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormSubmitButton = jQueryFrame("#page\\:mainForm\\:outerForm\\:submit");

  var form2OutputFieldValue = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:out span").text();
  var outerFormOutputFieldValue = jQueryFrame("#page\\:mainForm\\:outerForm\\:out span").text();

  $form2InputField.val("");
  $outerFormInputField.val("");
  $outerFormSubmitButton.click();

  jQuery("#page\\:testframe").load(function () {
    $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:in\\:\\:field");
    var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:out span");
    $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
    var $outerFormOutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:out span");

    var $alert = jQueryFrame("#page\\:messages .alert-danger label");

    assert.equal($form2InputField.val(), "");
    assert.equal($form2OutputField.text(), form2OutputFieldValue);
    assert.equal($outerFormInputField.val(), "");
    assert.equal($outerFormOutputField.text(), outerFormOutputFieldValue);

    assert.equal($alert.length, "2");

    done();
  });
});

QUnit.test("submit outer form, violate required field in form 2", function (assert) {
  assert.expect(5);
  var done = assert.async();
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:in\\:\\:field");
  var $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormSubmitButton = jQueryFrame("#page\\:mainForm\\:outerForm\\:submit");

  var form2OutputFieldValue = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:out span").text();
  var outerFormOutputFieldValue = jQueryFrame("#page\\:mainForm\\:outerForm\\:out span").text();

  $form2InputField.val("");
  $outerFormInputField.val("Charlie");
  $outerFormSubmitButton.click();

  jQuery("#page\\:testframe").load(function () {
    $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:in\\:\\:field");
    var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:out span");
    $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
    var $outerFormOutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:out span");

    var $alert = jQueryFrame("#page\\:messages .alert-danger label");

    assert.equal($form2InputField.val(), "");
    assert.equal($form2OutputField.text(), form2OutputFieldValue);
    assert.equal($outerFormInputField.val(), "Charlie");
    assert.equal($outerFormOutputField.text(), outerFormOutputFieldValue);

    assert.equal($alert.length, "1");

    done();
  });
});

QUnit.test("submit outer form, violate required field in outer form", function (assert) {
  assert.expect(5);
  var done = assert.async();
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:in\\:\\:field");
  var $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormSubmitButton = jQueryFrame("#page\\:mainForm\\:outerForm\\:submit");

  var form2OutputFieldValue = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:out span").text();
  var outerFormOutputFieldValue = jQueryFrame("#page\\:mainForm\\:outerForm\\:out span").text();

  $form2InputField.val("Dave");
  $outerFormInputField.val("");
  $outerFormSubmitButton.click();

  jQuery("#page\\:testframe").load(function () {
    $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:in\\:\\:field");
    var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:out span");
    $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
    var $outerFormOutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:out span");

    var $alert = jQueryFrame("#page\\:messages .alert-danger label");

    assert.equal($form2InputField.val(), "Dave");
    assert.equal($form2OutputField.text(), form2OutputFieldValue);
    assert.equal($outerFormInputField.val(), "");
    assert.equal($outerFormOutputField.text(), outerFormOutputFieldValue);

    assert.equal($alert.length, "1");

    done();
  });
});

QUnit.test("submit outer form without violations", function (assert) {
  assert.expect(7);
  var done = assert.async();
  var $form1InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm1\\:in\\:\\:field");
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:in\\:\\:field");
  var $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormSubmitButton = jQueryFrame("#page\\:mainForm\\:outerForm\\:submit");

  $form1InputField.val("Eve");
  $form2InputField.val("Frank");
  $outerFormInputField.val("Grace");
  $outerFormSubmitButton.click();

  jQuery("#page\\:testframe").load(function () {
    $form1InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm1\\:in\\:\\:field");
    var $form1OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm1\\:out span");
    $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:in\\:\\:field");
    var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:innerForm2\\:out span");
    $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
    var $outerFormOutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:out span");

    var $alert = jQueryFrame("#page\\:messages .alert-danger label");

    assert.equal($form1InputField.val(), "Eve");
    assert.equal($form1OutputField.text(), "Eve");
    assert.equal($form2InputField.val(), "Frank");
    assert.equal($form2OutputField.text(), "Frank");
    assert.equal($outerFormInputField.val(), "Grace");
    assert.equal($outerFormOutputField.text(), "Grace");

    assert.equal($alert.length, "0");

    done();
  });
});
