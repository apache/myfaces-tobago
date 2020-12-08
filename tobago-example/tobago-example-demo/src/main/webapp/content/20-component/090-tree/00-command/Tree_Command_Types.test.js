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

import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("not implemented yet", function (done) {
  let test = new JasmineTestTool(done);
  test.do(() => fail("not implemented yet"));
  test.start();
});

/*
import {querySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("execute both 'Action 1' and 'Action 2' two times", function (assert) {
  let action1Fn = querySelectorFn("#page\\:mainForm\\:tree\\:2\\:actionCommand");
  let action2Fn = querySelectorFn("#page\\:mainForm\\:tree\\:3\\:actionCommand");
  let actionCount1Fn = querySelectorFn("#page\\:mainForm\\:actionCount1 .tobago-out");
  let actionCount2Fn = querySelectorFn("#page\\:mainForm\\:actionCount2 .tobago-out");

  const counterBeforeTestResult1 = Number(actionCount1Fn().textContent);
  const counterBeforeTestResult2 = Number(actionCount2Fn().textContent);

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    action1Fn().dispatchEvent(new Event('click'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(Number(actionCount1Fn().textContent), counterBeforeTestResult1 + 1);
  });
  TTT.action(function () {
    action2Fn().dispatchEvent(new Event('click'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(Number(actionCount2Fn().textContent), counterBeforeTestResult2 + 1);
  });
  TTT.action(function () {
    action1Fn().dispatchEvent(new Event('click'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(Number(actionCount1Fn().textContent), counterBeforeTestResult1 + 2);
  });
  TTT.action(function () {
    action2Fn().dispatchEvent(new Event('click'));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(Number(actionCount2Fn().textContent), counterBeforeTestResult2 + 2);
  });
  TTT.startTest();
});
*/
