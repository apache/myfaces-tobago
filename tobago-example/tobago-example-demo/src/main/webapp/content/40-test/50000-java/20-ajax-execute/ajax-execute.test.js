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

QUnit.test("ajax excecute", function (assert) {
  let in1Fn = jQueryFrameFn("#page\\:mainForm\\:in1\\:\\:field");
  let in2Fn = jQueryFrameFn("#page\\:mainForm\\:in2\\:\\:field");
  let in3Fn = jQueryFrameFn("#page\\:mainForm\\:in3\\:\\:field");
  let in4Fn = jQueryFrameFn("#page\\:mainForm\\:in4\\:\\:field");
  let clearButtonFn = jQueryFrameFn("#page\\:mainForm\\:clear");
  let submitButtonFn = jQueryFrameFn("#page\\:mainForm\\:submit");
  let reloadButtonFn = jQueryFrameFn("#page\\:mainForm\\:reload");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    in1Fn().val("a");
    in2Fn().val("b");
    in3Fn().val("c");
    in4Fn().val("d");
    clearButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal(in1Fn().val(), "");
    assert.equal(in2Fn().val(), "");
    assert.equal(in3Fn().val(), "");
    assert.equal(in4Fn().val(), "");
  });
  TTT.action(function () {
    in1Fn().val("a");
    in2Fn().val("b");
    in3Fn().val("c");
    in4Fn().val("d");
    submitButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal(in1Fn().val(), "a");
    assert.equal(in2Fn().val(), "b");
    assert.equal(in3Fn().val(), "c");
    assert.equal(in4Fn().val(), "");
  });
  TTT.action(function () {
    reloadButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(4, function () {
    assert.equal(in1Fn().val(), "a");
    assert.equal(in2Fn().val(), "");
    assert.equal(in3Fn().val(), "c");
    assert.equal(in4Fn().val(), "");
  });
  TTT.startTest();
});
