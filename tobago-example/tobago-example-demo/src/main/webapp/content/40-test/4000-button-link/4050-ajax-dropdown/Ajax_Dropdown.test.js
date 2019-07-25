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

QUnit.test("Execute 'AJAX' entry in dropdown menu", function (assert) {
  var dropdownMenuButtonFn = jQueryFrameFn("#page\\:mainForm\\:dropdownMenuButton\\:\\:command");
  var ajaxEntryFn = jQueryFrameFn("#page\\:mainForm\\:ajaxEntry");
  var inputFn = jQueryFrameFn("#page\\:mainForm\\:inputAjax\\:\\:field");
  var outputFn = jQueryFrameFn("#page\\:mainForm\\:outputAjax .tobago-out");

  var TTT = new TobagoTestTool(assert);
  TTT.asserts(1, function () {
    assert.equal(ajaxEntryFn().parents(".tobago-page-menuStore").length, 0, "Dropdown menu should be closed.");
  });
  TTT.action(function () {
    dropdownMenuButtonFn().click();
  });
  TTT.asserts(1, function () {
    assert.equal(ajaxEntryFn().parents(".tobago-page-menuStore").length, 1, "Dropdown menu should be opened.");
  });
  TTT.action(function () {
    inputFn().val("Tobago, yay!");
  });
  TTT.asserts(1, function () {
    assert.equal(outputFn().text(), "", "Output should be empty.");
  });
  TTT.action(function () {
    ajaxEntryFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal(outputFn().text(), "Tobago, yay!");
    assert.equal(ajaxEntryFn().parents(".tobago-page-menuStore").length, 0, "Dropdown menu should be closed.");
  });
  TTT.startTest();
});
