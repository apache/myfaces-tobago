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

import {jQueryFrameFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("Required: Submit without content.", function (assert) {
  var messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:required\\:in1\\:\\:field");
  var submitFn = jQueryFrameFn("#page\\:mainForm\\:required\\:submit1");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().val("");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Required: Submit with content.", function (assert) {
  var messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:required\\:in1\\:\\:field");
  var submitFn = jQueryFrameFn("#page\\:mainForm\\:required\\:submit1");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().val("some content");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Length: Submit single character.", function (assert) {
  var messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:length\\:in2\\:\\:field");
  var submitFn = jQueryFrameFn("#page\\:mainForm\\:length\\:submit2");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().val("a");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});

QUnit.test("Length: Submit three characters.", function (assert) {
  var messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:length\\:in2\\:\\:field");
  var submitFn = jQueryFrameFn("#page\\:mainForm\\:length\\:submit2");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().val("abc");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 0);
  });
  TTT.startTest();
});

QUnit.test("Length: Submit five characters.", function (assert) {
  var messagesFn = jQueryFrameFn("#page\\:messages.tobago-messages div");
  var inFn = jQueryFrameFn("#page\\:mainForm\\:length\\:in2\\:\\:field");
  var submitFn = jQueryFrameFn("#page\\:mainForm\\:length\\:submit2");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    inFn().val("abcde");
    submitFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(messagesFn().length, 1);
  });
  TTT.startTest();
});
