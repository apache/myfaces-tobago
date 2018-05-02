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
  var $form1InputField = jQueryFrameFn("#page\\:mainForm\\:form1\\:in1\\:\\:field");
  var $form2InputField = jQueryFrameFn("#page\\:mainForm\\:form2\\:in2\\:\\:field");
  var $form1OutputField = jQueryFrameFn("#page\\:mainForm\\:form1\\:out1 span");
  var $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:form2\\:out2 span");
  var $form1SubmitButton = jQueryFrameFn("#page\\:mainForm\\:form1\\:submit1");
  var $form2OutputFieldValue = $form2OutputField().text();

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form1InputField().val("Oliver");
    $form2InputField().val("Peter");
    $form1SubmitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal($form1InputField().val(), "Oliver");
    assert.equal($form1OutputField().text(), "Oliver");
    assert.equal($form2InputField().val(), "Peter");
    assert.equal($form2OutputField().text(), $form2OutputFieldValue);
  });
  TTT.startTest();
});

QUnit.test("submit form 2", function (assert) {
  var $form1InputField = jQueryFrameFn("#page\\:mainForm\\:form1\\:in1\\:\\:field");
  var $form2InputField = jQueryFrameFn("#page\\:mainForm\\:form2\\:in2\\:\\:field");
  var $form1OutputField = jQueryFrameFn("#page\\:mainForm\\:form1\\:out1 span");
  var $form2OutputField = jQueryFrameFn("#page\\:mainForm\\:form2\\:out2 span");
  var $form2SubmitButton = jQueryFrameFn("#page\\:mainForm\\:form2\\:submit2");
  var $form1OutputFieldValue = $form1OutputField().text();

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $form1InputField().val("Oliver");
    $form2InputField().val("Peter");
    $form2SubmitButton().click();
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal($form1InputField().val(), "Oliver");
    assert.equal($form1OutputField().text(), $form1OutputFieldValue);
    assert.equal($form2InputField().val(), "Peter");
    assert.equal($form2OutputField().text(), "Peter");
  });
  TTT.startTest();
});
