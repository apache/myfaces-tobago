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

QUnit.test("inputfield with label", function (assert) {
  let labelFn = testFrameQuerySelectorFn("#page\\:mainForm\\:iNormal > label");
  let inputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:iNormal\\:\\:field");

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(2, function () {
    assert.equal(labelFn().textContent, "Input");
    assert.equal(inputFieldFn().value, "Some Text");
  });
  TTT.action(function () {
    inputFieldFn().value = "abc";
  });
  TTT.asserts(1, function () {
    assert.equal(inputFieldFn().value, "abc");
  });
  TTT.startTest();
});

QUnit.test("ajax change event", function (assert) {
  let inputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:inputAjax\\:\\:field");
  let outputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outputAjax span");

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(2, function () {
    assert.equal(inputFieldFn().value, "");
    assert.equal(outputFieldFn().textContent, "");
  });
  TTT.action(function () {
    inputFieldFn().value = "qwe";
    inputFieldFn().dispatchEvent(new Event("change", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFieldFn().value, "qwe");
  });
  TTT.asserts(1, function () {
    assert.equal(outputFieldFn().textContent, "qwe");
  });
  TTT.startTest();
});
