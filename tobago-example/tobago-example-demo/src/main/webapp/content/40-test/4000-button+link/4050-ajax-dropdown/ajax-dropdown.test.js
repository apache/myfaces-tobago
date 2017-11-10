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
  assert.expect(5);
  var done = assert.async(1);

  var $dropdownMenuButton = jQueryFrame("#page\\:mainForm\\:dropdownMenuButton\\:\\:command");
  var $ajaxEntry = jQueryFrame("#page\\:mainForm\\:ajaxEntry");
  var $input = jQueryFrame("#page\\:mainForm\\:inputAjax\\:\\:field");
  var $output = jQueryFrame("#page\\:mainForm\\:outputAjax .tobago-out");
  var $menuStore = jQuery(".tobago-page-menuStore");

  assert.equal($menuStore.find($ajaxEntry.selector).length, 0, "Dropdown menu should be closed.");
  $dropdownMenuButton.click();

  $menuStore = jQueryFrame($menuStore.selector);
  assert.equal($menuStore.find($ajaxEntry.selector).length, 1, "Dropdown menu should be opened.");

  $input.val("Tobago, yay!");
  assert.equal($output.text(), "", "Output should be empty.");

  $ajaxEntry = jQueryFrame($ajaxEntry.selector);
  $ajaxEntry.click();

  waitForAjax(function () {
    $output = jQueryFrame($output.selector);
    $menuStore = jQueryFrame($menuStore.selector);

    return $output.text() === "Tobago, yay!" && $menuStore.find($ajaxEntry.selector).length === 0;
  }, function () {
    assert.equal($output.text(), "Tobago, yay!");
    assert.equal($menuStore.find($ajaxEntry.selector).length, 0, "Dropdown menu should be closed.");

    done();
  });
});
