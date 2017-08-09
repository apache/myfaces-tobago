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

QUnit.test("submit form 1", function (assert) {
  assert.expect(4);
  var done = assert.async();

  var $form1InputField = jQueryFrame("#page\\:mainForm\\:form1\\:in\\:\\:field");
  var $form1SubmitButton = jQueryFrame("#page\\:mainForm\\:form1\\:submit");

  var $form2InputField = jQueryFrame("#page\\:mainForm\\:form2\\:in\\:\\:field");
  var $form2OutputFieldValue = jQueryFrame("#page\\:mainForm\\:form2\\:out span").text();

  $form1InputField.val("Oliver");
  $form2InputField.val("Peter");

  $form1SubmitButton.click();

  jQuery("#page\\:testframe").load(function () {
    $form1InputField = jQueryFrame("#page\\:mainForm\\:form1\\:in\\:\\:field");
    var $form1OutputField = jQueryFrame("#page\\:mainForm\\:form1\\:out span");
    $form2InputField = jQueryFrame("#page\\:mainForm\\:form2\\:in\\:\\:field");
    var $form2OutputField = jQueryFrame("#page\\:mainForm\\:form2\\:out span");

    assert.equal($form1InputField.val(), "Oliver");
    assert.equal($form1OutputField.text(), "Oliver");
    assert.equal($form2InputField.val(), "Peter");
    assert.equal($form2OutputField.text(), $form2OutputFieldValue);

    done();
  });
});

QUnit.test("submit form 2", function (assert) {
  assert.expect(4);
  var done = assert.async();

  var $form1InputField = jQueryFrame("#page\\:mainForm\\:form1\\:in\\:\\:field");
  var $form1OutputFieldValue = jQueryFrame("#page\\:mainForm\\:form1\\:out span").text();

  var $form2InputField = jQueryFrame("#page\\:mainForm\\:form2\\:in\\:\\:field");
  var $form2SubmitButton = jQueryFrame("#page\\:mainForm\\:form2\\:submit");

  $form1InputField.val("Oliver");
  $form2InputField.val("Peter");

  $form2SubmitButton.click();

  jQuery("#page\\:testframe").load(function () {
    $form1InputField = jQueryFrame("#page\\:mainForm\\:form1\\:in\\:\\:field");
    var $form1OutputField = jQueryFrame("#page\\:mainForm\\:form1\\:out span");
    $form2InputField = jQueryFrame("#page\\:mainForm\\:form2\\:in\\:\\:field");
    var $form2OutputField = jQueryFrame("#page\\:mainForm\\:form2\\:out span");

    assert.equal($form1InputField.val(), "Oliver");
    assert.equal($form1OutputField.text(), $form1OutputFieldValue);
    assert.equal($form2InputField.val(), "Peter");
    assert.equal($form2OutputField.text(), "Peter");

    done();
  });
});
