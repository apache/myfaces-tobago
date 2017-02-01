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
  var $form1InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:in\\:\\:field");
  var $form1OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:out span");

  var $button = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:submit");

  $form1InputField.val("Alice");
  $button.click();

  waitForAjax(function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    return $form1InputField.val() == "Alice" && $form1OutputField.text() == "Alice";
  }, function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);

    assert.equal($form1InputField.val(), "Alice");
    assert.equal($form1OutputField.text(), "Alice");

    var $alert = jQueryFrame("#page\\:messages .alert-danger label");
    assert.equal($alert.length, "0");

    done();
  });
});

QUnit.test("submit inner form 2, violate required field", function (assert) {
  assert.expect(3);
  var done = assert.async();
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:in\\:\\:field");
  var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:out span");

  var $button = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:submit");

  var form2OutputFieldValue = $form2OutputField.text();

  $form2InputField.val("");
  $button.click();

  waitForAjax(function () {
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    return $form2InputField.val() == "" && $form2OutputField.text() == form2OutputFieldValue;
  }, function () {
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);

    assert.equal($form2InputField.val(), "");
    assert.equal($form2OutputField.text(), form2OutputFieldValue);

    var $alert = jQueryFrame("#page\\:messages .alert-danger label");
    assert.equal($alert.length, "1");

    done();
  });
});

QUnit.test("submit inner form 2 without violations", function (assert) {
  assert.expect(3);
  var done = assert.async();
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:in\\:\\:field");
  var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:out span");

  var $button = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:submit");

  $form2InputField.val("Bob");
  $button.click();

  waitForAjax(function () {
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    return $form2InputField.val() == "Bob" && $form2OutputField.text() == "Bob";
  }, function () {
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);

    assert.equal($form2InputField.val(), "Bob");
    assert.equal($form2OutputField.text(), "Bob");

    var $alert = jQueryFrame("#page\\:messages .alert-danger label");
    assert.equal($alert.length, "0");

    done();
  });
});

QUnit.test("submit outer form, violate both required fields", function (assert) {
  assert.expect(7);
  var done = assert.async();
  var $form1InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:in\\:\\:field");
  var $form1OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:out span");
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:in\\:\\:field");
  var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:out span");
  var $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:out span");

  var $button = jQueryFrame("#page\\:mainForm\\:outerForm\\:submit");

  var form1OutputFieldValue = $form1OutputField.text();
  var form2OutputFieldValue = $form2OutputField.text();
  var outerFormOutputFieldValue = $outerFormOutputField.text();

  $form1InputField.val("Charlie");
  $form2InputField.val("");
  $outerFormInputField.val("");
  $button.click();

  waitForAjax(function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);
    return $form1InputField.val() == "Charlie"
        && $form1OutputField.text() == form1OutputFieldValue
        && $form2InputField.val() == ""
        && $form2OutputField.text() == form2OutputFieldValue
        && $outerFormInputField.val() == ""
        && $outerFormOutputField.text() == outerFormOutputFieldValue;
  }, function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);

    assert.equal($form1InputField.val(), "Charlie");
    assert.equal($form1OutputField.text(), form1OutputFieldValue);
    assert.equal($form2InputField.val(), "");
    assert.equal($form2OutputField.text(), form2OutputFieldValue);
    assert.equal($outerFormInputField.val(), "");
    assert.equal($outerFormOutputField.text(), outerFormOutputFieldValue);

    var $alert = jQueryFrame("#page\\:messages .alert-danger label");
    assert.equal($alert.length, "2");

    done();
  });
});

QUnit.test("submit outer form, violate required field in form 2", function (assert) {
  assert.expect(7);
  var done = assert.async();
  var $form1InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:in\\:\\:field");
  var $form1OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:out span");
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:in\\:\\:field");
  var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:out span");
  var $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:out span");

  var $button = jQueryFrame("#page\\:mainForm\\:outerForm\\:submit");

  var form1OutputFieldValue = $form1OutputField.text();
  var form2OutputFieldValue = $form2OutputField.text();
  var outerFormOutputFieldValue = $outerFormOutputField.text();

  $form1InputField.val("Dave");
  $form2InputField.val("");
  $outerFormInputField.val("Eve");
  $button.click();

  waitForAjax(function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);
    return $form1InputField.val() == "Dave"
        && $form1OutputField.text() == form1OutputFieldValue
        && $form2InputField.val() == ""
        && $form2OutputField.text() == form2OutputFieldValue
        && $outerFormInputField.val() == "Eve"
        && $outerFormOutputField.text() == outerFormOutputFieldValue;
  }, function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);

    assert.equal($form1InputField.val(), "Dave");
    assert.equal($form1OutputField.text(), form1OutputFieldValue);
    assert.equal($form2InputField.val(), "");
    assert.equal($form2OutputField.text(), form2OutputFieldValue);
    assert.equal($outerFormInputField.val(), "Eve");
    assert.equal($outerFormOutputField.text(), outerFormOutputFieldValue);

    var $alert = jQueryFrame("#page\\:messages .alert-danger label");
    assert.equal($alert.length, "1");

    done();
  });
});

QUnit.test("submit outer form, violate required field in outer form", function (assert) {
  assert.expect(7);
  var done = assert.async();
  var $form1InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:in\\:\\:field");
  var $form1OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:out span");
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:in\\:\\:field");
  var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:out span");
  var $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:out span");

  var $button = jQueryFrame("#page\\:mainForm\\:outerForm\\:submit");

  var form1OutputFieldValue = $form1OutputField.text();
  var form2OutputFieldValue = $form2OutputField.text();
  var outerFormOutputFieldValue = $outerFormOutputField.text();

  $form1InputField.val("Frank");
  $form2InputField.val("Grace");
  $outerFormInputField.val("");
  $button.click();

  waitForAjax(function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);
    return $form1InputField.val() == "Frank"
        && $form1OutputField.text() == form1OutputFieldValue
        && $form2InputField.val() == "Grace"
        && $form2OutputField.text() == form2OutputFieldValue
        && $outerFormInputField.val() == ""
        && $outerFormOutputField.text() == outerFormOutputFieldValue;
  }, function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);

    assert.equal($form1InputField.val(), "Frank");
    assert.equal($form1OutputField.text(), form1OutputFieldValue);
    assert.equal($form2InputField.val(), "Grace");
    assert.equal($form2OutputField.text(), form2OutputFieldValue);
    assert.equal($outerFormInputField.val(), "");
    assert.equal($outerFormOutputField.text(), outerFormOutputFieldValue);

    var $alert = jQueryFrame("#page\\:messages .alert-danger label");
    assert.equal($alert.length, "1");

    done();
  });
});

QUnit.test("submit outer form without violations", function (assert) {
  assert.expect(7);
  var done = assert.async();
  var $form1InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:in\\:\\:field");
  var $form1OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:out span");
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:in\\:\\:field");
  var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:out span");
  var $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:out span");

  var $button = jQueryFrame("#page\\:mainForm\\:outerForm\\:submit");

  $form1InputField.val("Hank");
  $form2InputField.val("Irene");
  $outerFormInputField.val("John");
  $button.click();

  waitForAjax(function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);
    return $form1InputField.val() == "Hank"
        && $form1OutputField.text() == "Hank"
        && $form2InputField.val() == "Irene"
        && $form2OutputField.text() == "Irene"
        && $outerFormInputField.val() == "John"
        && $outerFormOutputField.text() == "John";
  }, function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);

    assert.equal($form1InputField.val(), "Hank");
    assert.equal($form1OutputField.text(), "Hank");
    assert.equal($form2InputField.val(), "Irene");
    assert.equal($form2OutputField.text(), "Irene");
    assert.equal($outerFormInputField.val(), "John");
    assert.equal($outerFormOutputField.text(), "John");

    var $alert = jQueryFrame("#page\\:messages .alert-danger label");
    assert.equal($alert.length, "0");

    done();
  });
});

QUnit.test("submit inner forms, violate required field in form 2", function (assert) {
  assert.expect(7);
  var done = assert.async();
  var $form1InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:in\\:\\:field");
  var $form1OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:out span");
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:in\\:\\:field");
  var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:out span");
  var $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:out span");

  var $button = jQueryFrame("#page\\:mainForm\\:outerForm\\:submitInnerForms");

  var form1OutputFieldValue = $form1OutputField.text();
  var form2OutputFieldValue = $form2OutputField.text();
  var outerFormOutputFieldValue = $outerFormOutputField.text();

  $form1InputField.val("Kate");
  $form2InputField.val("");
  $outerFormInputField.val("Leonard");
  $button.click();

  waitForAjax(function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);
    return $form1InputField.val() == "Kate"
    && $form1OutputField.text() == form1OutputFieldValue
    && $form2InputField.val() == ""
    && $form2OutputField.text() == form2OutputFieldValue
    && $outerFormInputField.val() == "Leonard"
    && $outerFormOutputField.text(), outerFormOutputFieldValue;
  }, function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);

    assert.equal($form1InputField.val(), "Kate");
    assert.equal($form1OutputField.text(), form1OutputFieldValue);
    assert.equal($form2InputField.val(), "");
    assert.equal($form2OutputField.text(), form2OutputFieldValue);
    assert.equal($outerFormInputField.val(), "Leonard");
    assert.equal($outerFormOutputField.text(), outerFormOutputFieldValue);

    var $alert = jQueryFrame("#page\\:messages .alert-danger label");
    assert.equal($alert.length, "1");

    done();
  });
});

QUnit.test("submit inner forms without violations", function (assert) {
  assert.expect(7);
  var done = assert.async();
  var $form1InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:in\\:\\:field");
  var $form1OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:out span");
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:in\\:\\:field");
  var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:out span");
  var $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:out span");

  var $button = jQueryFrame("#page\\:mainForm\\:outerForm\\:submitInnerForms");

  var outerFormOutputFieldValue = $outerFormOutputField.text();

  $form1InputField.val("Mike");
  $form2InputField.val("Neil");
  $outerFormInputField.val("");
  $button.click();

  waitForAjax(function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);
    return $form1InputField.val() == "Mike"
        && $form1OutputField.text() == "Mike"
        && $form2InputField.val() == "Neil"
        && $form2OutputField.text() == "Neil"
        && $outerFormInputField.val() == ""
        && $outerFormOutputField.text() == outerFormOutputFieldValue;
  }, function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);

    assert.equal($form1InputField.val(), "Mike");
    assert.equal($form1OutputField.text(), "Mike");
    assert.equal($form2InputField.val(), "Neil");
    assert.equal($form2OutputField.text(), "Neil");
    assert.equal($outerFormInputField.val(), "");
    assert.equal($outerFormOutputField.text(), outerFormOutputFieldValue);

    var $alert = jQueryFrame("#page\\:messages .alert-danger label");
    assert.equal($alert.length, "0");

    done();
  });
});

QUnit.test("submit outer value, violate required field", function (assert) {
  assert.expect(7);
  var done = assert.async();
  var $form1InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:in\\:\\:field");
  var $form1OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:out span");
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:in\\:\\:field");
  var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:out span");
  var $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:out span");

  var $button = jQueryFrame("#page\\:mainForm\\:outerForm\\:submitOuterValue");

  var form1OutputFieldValue = $form1OutputField.text();
  var form2OutputFieldValue = $form2OutputField.text();
  var outerFormOutputFieldValue = $outerFormOutputField.text();

  $form1InputField.val("Oscar");
  $form2InputField.val("Penny");
  $outerFormInputField.val("");
  $button.click();

  waitForAjax(function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);
    return $form1InputField.val() == "Oscar"
        && $form1OutputField.text() == form1OutputFieldValue
        && $form2InputField.val() == "Penny"
        && $form2OutputField.text() == form2OutputFieldValue
        && $outerFormInputField.val() == ""
        && $outerFormOutputField.text() == outerFormOutputFieldValue;
  }, function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);

    assert.equal($form1InputField.val(), "Oscar");
    assert.equal($form1OutputField.text(), form1OutputFieldValue);
    assert.equal($form2InputField.val(), "Penny");
    assert.equal($form2OutputField.text(), form2OutputFieldValue);
    assert.equal($outerFormInputField.val(), "");
    assert.equal($outerFormOutputField.text(), outerFormOutputFieldValue);

    var $alert = jQueryFrame("#page\\:messages .alert-danger label");
    assert.equal($alert.length, "1");

    done();
  });
});

QUnit.test("submit outer value without violations", function (assert) {
  assert.expect(7);
  var done = assert.async();
  var $form1InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:in\\:\\:field");
  var $form1OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form1\\:out span");
  var $form2InputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:in\\:\\:field");
  var $form2OutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:form2\\:out span");
  var $outerFormInputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:in\\:\\:field");
  var $outerFormOutputField = jQueryFrame("#page\\:mainForm\\:outerForm\\:out span");

  var $button = jQueryFrame("#page\\:mainForm\\:outerForm\\:submitOuterValue");

  var form1OutputFieldValue = $form1OutputField.text();
  var form2OutputFieldValue = $form2OutputField.text();

  $form1InputField.val("Quin");
  $form2InputField.val("Sue");
  $outerFormInputField.val("Ted");
  $button.click();

  waitForAjax(function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);
    return $form1InputField.val() == "Quin"
        && $form1OutputField.text() == form1OutputFieldValue
        && $form2InputField.val() == "Sue"
        && $form2OutputField.text() == form2OutputFieldValue
        && $outerFormInputField.val() == "Ted"
        && $outerFormOutputField.text() == "Ted";
  }, function () {
    $form1InputField = jQueryFrame($form1InputField.selector);
    $form1OutputField = jQueryFrame($form1OutputField.selector);
    $form2InputField = jQueryFrame($form2InputField.selector);
    $form2OutputField = jQueryFrame($form2OutputField.selector);
    $outerFormInputField = jQueryFrame($outerFormInputField.selector);
    $outerFormOutputField = jQueryFrame($outerFormOutputField.selector);

    assert.equal($form1InputField.val(), "Quin");
    assert.equal($form1OutputField.text(), form1OutputFieldValue);
    assert.equal($form2InputField.val(), "Sue");
    assert.equal($form2OutputField.text(), form2OutputFieldValue);
    assert.equal($outerFormInputField.val(), "Ted");
    assert.equal($outerFormOutputField.text(), "Ted");

    var $alert = jQueryFrame("#page\\:messages .alert-danger label");
    assert.equal($alert.length, "0");

    done();
  });
});
