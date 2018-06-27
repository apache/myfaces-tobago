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

QUnit.test("Add a river and reset.", function (assert) {
  var nameFn = jQueryFrameFn("#page\\:mainForm\\:add\\:inName\\:\\:field");
  var lengthFn = jQueryFrameFn("#page\\:mainForm\\:add\\:inLength\\:\\:field");
  var dischargeFn = jQueryFrameFn("#page\\:mainForm\\:add\\:inDischarge\\:\\:field");
  var addFn = jQueryFrameFn("#page\\:mainForm\\:add\\:buttonAdd");
  var resetFn = jQueryFrameFn("#page\\:mainForm\\:reset\\:buttonReset");
  var forEachBoxesFn = jQueryFrameFn("#page\\:mainForm\\:forEach .tobago-box");
  var uiRepeatSectionsFn = jQueryFrameFn("#page\\:mainForm\\:uiRepeat .tobago-section");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    resetFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(forEachBoxesFn().length, 3);
    assert.equal(uiRepeatSectionsFn().length, 3);
  });
  TTT.action(function () {
    nameFn().val("Mississippi");
    lengthFn().val("6275");
    dischargeFn().val("16200");
    addFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(forEachBoxesFn().length, 4);
    assert.equal(uiRepeatSectionsFn().length, 4);
  });
  TTT.action(function () {
    resetFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(forEachBoxesFn().length, 3);
    assert.equal(uiRepeatSectionsFn().length, 3);
  });
  TTT.startTest();
});
