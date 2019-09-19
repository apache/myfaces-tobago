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
  let timestampFn = testFrameQuerySelectorFn("#page\\:mainForm\\:timestamp span");
  let textFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outText span");
  let tipFn = testFrameQuerySelectorFn("#page\\:mainForm\\:outTip span");
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:ajaxButton");

  let timestampValue = timestampFn().textContent;
  let textValue = textFn().textContent;
  let tipValue = tipFn().getAttribute('title');

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    buttonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(3, function () {
    assert.notEqual(timestampFn().textContent, timestampValue);
    assert.equal(textFn().textContent, textValue);
    assert.equal(tipFn().getAttribute('title'), tipValue);
  });
  TTT.startTest();
});
