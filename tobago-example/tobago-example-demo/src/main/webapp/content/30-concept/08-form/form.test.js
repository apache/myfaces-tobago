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
  let form1InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:form1\\:in1\\:\\:field");
  let form2InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:in2\\:\\:field");
  let form1OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:form1\\:out1 span");
  let form2OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:out2 span");
  let form1SubmitButtonFn = jQueryFrameFn("#page\\:mainForm\\:form1\\:submit1");
  let $form2OutputFieldValue = form2OutputFieldFn().text();

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    form1InputFieldFn().val("Oliver");
    form2InputFieldFn().val("Peter");
    form1SubmitButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal(form1InputFieldFn().val(), "Oliver");
    assert.equal(form1OutputFieldFn().text(), "Oliver");
    assert.equal(form2InputFieldFn().val(), "Peter");
    assert.equal(form2OutputFieldFn().text(), $form2OutputFieldValue);
  });
  TTT.startTest();
});

QUnit.test("submit form 2", function (assert) {
  let form1InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:form1\\:in1\\:\\:field");
  let form2InputFieldFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:in2\\:\\:field");
  let form1OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:form1\\:out1 span");
  let form2OutputFieldFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:out2 span");
  let form2SubmitButtonFn = jQueryFrameFn("#page\\:mainForm\\:form2\\:submit2");
  let $form1OutputFieldValue = form1OutputFieldFn().text();

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    form1InputFieldFn().val("Oliver");
    form2InputFieldFn().val("Peter");
    form2SubmitButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal(form1InputFieldFn().val(), "Oliver");
    assert.equal(form1OutputFieldFn().text(), $form1OutputFieldValue);
    assert.equal(form2InputFieldFn().val(), "Peter");
    assert.equal(form2OutputFieldFn().text(), "Peter");
  });
  TTT.startTest();
});
