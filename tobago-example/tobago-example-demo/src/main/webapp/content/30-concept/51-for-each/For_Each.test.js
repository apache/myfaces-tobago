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

QUnit.test("Add a river and reset.", function (assert) {
  let nameFn = testFrameQuerySelectorFn("#page\\:mainForm\\:add\\:inName\\:\\:field");
  let lengthFn = testFrameQuerySelectorFn("#page\\:mainForm\\:add\\:inLength\\:\\:field");
  let dischargeFn = testFrameQuerySelectorFn("#page\\:mainForm\\:add\\:inDischarge\\:\\:field");
  let addFn = testFrameQuerySelectorFn("#page\\:mainForm\\:add\\:buttonAdd");
  let resetFn = testFrameQuerySelectorFn("#page\\:mainForm\\:reset\\:buttonReset");
  let forEachBoxesFn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:forEach .tobago-box");
  let uiRepeatSectionsFn = testFrameQuerySelectorAllFn("#page\\:mainForm\\:uiRepeat .tobago-section");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    resetFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(forEachBoxesFn().length, 3);
    assert.equal(uiRepeatSectionsFn().length, 3);
  });
  TTT.action(function () {
    nameFn().value = "Mississippi";
    lengthFn().value = "6275";
    dischargeFn().value = "16200";
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
