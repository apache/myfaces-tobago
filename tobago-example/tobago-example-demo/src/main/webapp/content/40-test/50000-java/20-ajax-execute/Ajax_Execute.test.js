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

QUnit.test("ajax excecute", function (assert) {
  let in1Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:in1\\:\\:field");
  let in2Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:in2\\:\\:field");
  let in3Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:in3\\:\\:field");
  let in4Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:in4\\:\\:field");
  let clearButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:clear");
  let submitButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:submit");
  let reloadButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:reload");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    in1Fn().value = "a";
    in2Fn().value = "b";
    in3Fn().value = "c";
    in4Fn().value = "d";
    clearButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal(in1Fn().value, "");
    assert.equal(in2Fn().value, "");
    assert.equal(in3Fn().value, "");
    assert.equal(in4Fn().value, "");
  });
  TTT.action(function () {
    in1Fn().value = "a";
    in2Fn().value = "b";
    in3Fn().value = "c";
    in4Fn().value = "d";
    submitButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal(in1Fn().value, "a");
    assert.equal(in2Fn().value, "b");
    assert.equal(in3Fn().value, "c");
    assert.equal(in4Fn().value, "");
  });
  TTT.action(function () {
    reloadButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal(in1Fn().value, "a");
    assert.equal(in2Fn().value, "");
    assert.equal(in3Fn().value, "c");
    assert.equal(in4Fn().value, "");
  });
  TTT.startTest();
});
