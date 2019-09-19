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

import {testFrameQuerySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("submit form 1", function (assert) {
  let form1InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:form1\\:in1\\:\\:field");
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:form2\\:in2\\:\\:field");
  let form1OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:form1\\:out1 span");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:form2\\:out2 span");
  let form1SubmitButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:form1\\:submit1");
  let $form2OutputFieldValue = form2OutputFieldFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form1InputFieldFn().value = "Oliver";
    form2InputFieldFn().value = "Peter";
    form1SubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal(form1InputFieldFn().value, "Oliver");
    assert.equal(form1OutputFieldFn().textContent, "Oliver");
    assert.equal(form2InputFieldFn().value, "Peter");
    assert.equal(form2OutputFieldFn().textContent, $form2OutputFieldValue);
  });
  TTT.startTest();
});

QUnit.test("submit form 2", function (assert) {
  let form1InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:form1\\:in1\\:\\:field");
  let form2InputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:form2\\:in2\\:\\:field");
  let form1OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:form1\\:out1 span");
  let form2OutputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:form2\\:out2 span");
  let form2SubmitButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:form2\\:submit2");
  let $form1OutputFieldValue = form1OutputFieldFn().textContent;

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    form1InputFieldFn().value = "Oliver";
    form2InputFieldFn().value = "Peter";
    form2SubmitButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal(form1InputFieldFn().value, "Oliver");
    assert.equal(form1OutputFieldFn().textContent, $form1OutputFieldValue);
    assert.equal(form2InputFieldFn().value, "Peter");
    assert.equal(form2OutputFieldFn().textContent, "Peter");
  });
  TTT.startTest();
});
