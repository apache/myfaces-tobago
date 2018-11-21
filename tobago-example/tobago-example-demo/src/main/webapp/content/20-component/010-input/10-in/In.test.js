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

QUnit.test("inputfield with label", function (assert) {
  var labelFn = jQueryFrameFn("#page\\:mainForm\\:iNormal > label");
  var inputFieldFn = jQueryFrameFn("#page\\:mainForm\\:iNormal\\:\\:field");

  var TTT = new TobagoTestTool(assert);
  TTT.asserts(2, function () {
    assert.equal(labelFn().text(), "Input");
    assert.equal(inputFieldFn().val(), "Some Text");
  });
  TTT.action(function () {
    inputFieldFn().val("abc");
  });
  TTT.asserts(1, function () {
    assert.equal(inputFieldFn().val(), "abc");
  });
  TTT.startTest();
});

QUnit.test("ajax change event", function (assert) {
  var inputFieldFn = jQueryFrameFn("#page\\:mainForm\\:inputAjax\\:\\:field");
  var outputFieldFn = jQueryFrameFn("#page\\:mainForm\\:outputAjax span:first");

  var TTT = new TobagoTestTool(assert);
  TTT.asserts(2, function () {
    assert.equal(inputFieldFn().val(), "");
    assert.equal(outputFieldFn().text(), "");
  });
  TTT.action(function () {
    inputFieldFn().val("qwe").trigger("change");
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(inputFieldFn().val(), "qwe");
  });
  TTT.asserts(1, function () {
    assert.equal(outputFieldFn().text(), "qwe");
  });
  TTT.startTest();
});
