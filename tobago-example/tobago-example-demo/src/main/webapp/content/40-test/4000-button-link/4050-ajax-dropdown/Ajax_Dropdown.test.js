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

import {querySelectorFn} from "/script/tobago-test.js";
import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";

QUnit.test("Execute 'AJAX' entry in dropdown menu", function (assert) {
  let dropdownMenuButtonFn = querySelectorFn("#page\\:mainForm\\:dropdownMenuButton\\:\\:command");
  const ajaxEntryId = "#page\\:mainForm\\:ajaxEntry";
  let ajaxEntryFn = querySelectorFn(ajaxEntryId);
  let tobagoPageMenuStoreFn = querySelectorFn(".tobago-page-menuStore");
  let inputFn = querySelectorFn("#page\\:mainForm\\:inputAjax\\:\\:field");
  let outputFn = querySelectorFn("#page\\:mainForm\\:outputAjax .tobago-out");

  let TTT = new TobagoTestTool(assert);
  TTT.asserts(1, function () {
    assert.ok(tobagoPageMenuStoreFn().querySelector(ajaxEntryId) === null, "Dropdown menu should be closed.");
  });
  TTT.action(function () {
    dropdownMenuButtonFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.asserts(1, function () {
    assert.ok(tobagoPageMenuStoreFn().querySelector(ajaxEntryId) !== null, "Dropdown menu should be opened.");
  });
  TTT.action(function () {
    inputFn().value = "Tobago, yay!";
  });
  TTT.asserts(1, function () {
    assert.equal(outputFn().textContent, "", "Output should be empty.");
  });
  TTT.action(function () {
    ajaxEntryFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(outputFn().textContent, "Tobago, yay!");
    assert.ok(tobagoPageMenuStoreFn().querySelector(ajaxEntryId) === null, "Dropdown menu should be closed.");
  });
  TTT.startTest();
});
