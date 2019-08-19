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

QUnit.test("Required: Submit without content.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:required\\:in1\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:required\\:submit1");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "";
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Required: Submit with content.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:required\\:in1\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:required\\:submit1");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "some content";
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Length: Submit single character.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:length\\:in2\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:length\\:submit2");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "a";
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Length: Submit three characters.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:length\\:in2\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:length\\:submit2");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "abc";
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Length: Submit five characters.", function (assert) {
  let messagesFn = testFrameQuerySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = testFrameQuerySelectorFn("#page\\:mainForm\\:length\\:in2\\:\\:field");
  let submitFn = testFrameQuerySelectorFn("#page\\:mainForm\\:length\\:submit2");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().value = "abcde";
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});
