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

QUnit.test("Execute 'AJAX' entry in dropdown menu", function (assert) {
  let $dropdownMenuButton = jQueryFrameFn("#page\\:mainForm\\:dropdownMenuButton\\:\\:command");
  let $ajaxEntry = jQueryFrameFn("#page\\:mainForm\\:ajaxEntry");
  let $input = jQueryFrameFn("#page\\:mainForm\\:inputAjax\\:\\:field");
  let $output = jQueryFrameFn("#page\\:mainForm\\:outputAjax .tobago-out");

  let TTT = new TobagoTestTools(assert);
  TTT.asserts(1, function () {
    assert.equal($ajaxEntry().parents(".tobago-page-menuStore").length, 0, "Dropdown menu should be closed.");
  });
  TTT.action(function () {
    $dropdownMenuButton().click();
  });
  TTT.asserts(1, function () {
    assert.equal($ajaxEntry().parents(".tobago-page-menuStore").length, 1, "Dropdown menu should be opened.");
  });
  TTT.action(function () {
    $input().val("Tobago, yay!");
  });
  TTT.asserts(1, function () {
    assert.equal($output().text(), "", "Output should be empty.");
  });
  TTT.action(function () {
    $ajaxEntry().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($output().text(), "Tobago, yay!");
    assert.equal($ajaxEntry().parents(".tobago-page-menuStore").length, 0, "Dropdown menu should be closed.");
  });
  TTT.startTest();
});
