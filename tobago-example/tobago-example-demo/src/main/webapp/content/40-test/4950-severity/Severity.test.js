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

QUnit.test("Check severity CSS classes", function (assert) {
  let submitButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:submit");
  let fatalInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:fatal\\:\\:field");
  let errorInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:error\\:\\:field");
  let warnInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:warn\\:\\:field");
  let infoInputFieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:info\\:\\:field");
  let fatalButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:fatal .tobago-messages-button");
  let errorButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:error .tobago-messages-button");
  let warnButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:warn .tobago-messages-button");
  let infoButtonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:info .tobago-messages-button");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    submitButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(8, function () {
    assert.ok(fatalInputFieldFn().classList.contains("border-danger"));
    assert.ok(errorInputFieldFn().classList.contains("border-danger"));
    assert.ok(warnInputFieldFn().classList.contains("border-warning"));
    assert.ok(infoInputFieldFn().classList.contains("border-info"));

    assert.ok(fatalButtonFn().classList.contains("btn-danger"));
    assert.ok(errorButtonFn().classList.contains("btn-danger"));
    assert.ok(warnButtonFn().classList.contains("btn-warning"));
    assert.ok(infoButtonFn().classList.contains("btn-info"));
  });
  TTT.startTest();
});
