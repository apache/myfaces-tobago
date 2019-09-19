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

QUnit.test("Standard Action Button", function (assert) {
  let commandFn = testFrameQuerySelectorFn("#page\\:mainForm\\:standardButtonAction");
  let destinationSectionFn = testFrameQuerySelectorFn("#page\\:actionSection");
  testStandardCommands(commandFn, destinationSectionFn, assert);
});

QUnit.test("Standard Link Button", function (assert) {
  let commandFn = testFrameQuerySelectorFn("#page\\:mainForm\\:standardButtonLink");
  let destinationSectionFn = testFrameQuerySelectorFn("#page\\:linkSection");
  testStandardCommands(commandFn, destinationSectionFn, assert);
});

QUnit.test("Standard Action Link", function (assert) {
  let commandFn = testFrameQuerySelectorFn("#page\\:mainForm\\:standardLinkAction");
  let destinationSectionFn = testFrameQuerySelectorFn("#page\\:actionSection");
  testStandardCommands(commandFn, destinationSectionFn, assert);
});

QUnit.test("Standard Link Link", function (assert) {
  let commandFn = testFrameQuerySelectorFn("#page\\:mainForm\\:standardLinkLink");
  let destinationSectionFn = testFrameQuerySelectorFn("#page\\:linkSection");
  testStandardCommands(commandFn, destinationSectionFn, assert);
});

function testStandardCommands(commandFn, destinationSectionFn, assert) {
  let backFn = testFrameQuerySelectorFn("#page\\:back");

  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    commandFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.ok(destinationSectionFn() !== null);
  });
  TTT.action(function () {
    backFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.ok(commandFn() !== null);
  });
  TTT.startTest();
}

QUnit.test("Target Action Button", function (assert) {
  let commandFn = testFrameQuerySelectorFn("#page\\:mainForm\\:targetButtonAction");
  testTargetCommands(commandFn, "accessed by action", assert);
});

QUnit.test("Target Link Button", function (assert) {
  let commandFn = testFrameQuerySelectorFn("#page\\:mainForm\\:targetButtonLink");
  testTargetCommands(commandFn, "accessed by link", assert);
});

QUnit.test("Target Action Link", function (assert) {
  let commandFn = testFrameQuerySelectorFn("#page\\:mainForm\\:targetLinkAction");
  testTargetCommands(commandFn, "accessed by action", assert);
});

QUnit.test("Target Link Link", function (assert) {
  let commandFn = testFrameQuerySelectorFn("#page\\:mainForm\\:targetLinkLink");
  testTargetCommands(commandFn, "accessed by link", assert);
});

QUnit.test("Style must not be a dropdown item", function (assert) {
  assert.expect(3);

  let dropdownMenuFn = testFrameQuerySelectorFn("#page\\:mainForm\\:dropdownWithStyle .dropdown-menu");
  let styleAsItemFn = testFrameQuerySelectorFn("#page\\:mainForm\\:dropdownWithStyle .dropdown-menu .dropdown-item > style");
  let buttonFn = testFrameQuerySelectorFn("#page\\:mainForm\\:dropdownWithStyle > .tobago-button");

  assert.ok(dropdownMenuFn() !== null);
  assert.equal(styleAsItemFn(), null);
  assert.equal(buttonFn().offsetWidth, 200);
});

function testTargetCommands(commandFn, expectedText, assert) {
  let TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    commandFn().dispatchEvent(new Event("click", {bubbles: true}));
  });
  TTT.waitMs(2000); //TobagoTestTools.waitForResponse() didn't recognize responses on a target frame, so we just wait
  TTT.asserts(1, function () {
    assert.equal(getTargetFrameTestInputValue(), expectedText);
  });
  TTT.startTest();
}

function getTargetFrameTestInputValue() {
  return testFrameQuerySelectorFn("#page\\:mainForm\\:targetFrame")().contentWindow
      .document.querySelector("#textInput").value;
}
