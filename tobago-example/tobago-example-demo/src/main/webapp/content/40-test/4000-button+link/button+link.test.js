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

QUnit.test("Standard Action Button", function (assert) {
  let commandFn = jQueryFrameFn("#page\\:mainForm\\:standardButtonAction");
  let destinationSectionFn = jQueryFrameFn("#page\\:actionSection");
  testStandardCommands(commandFn, destinationSectionFn, assert);
});

QUnit.test("Standard Link Button", function (assert) {
  let commandFn = jQueryFrameFn("#page\\:mainForm\\:standardButtonLink");
  let destinationSectionFn = jQueryFrameFn("#page\\:linkSection");
  testStandardCommands(commandFn, destinationSectionFn, assert);
});

QUnit.test("Standard Action Link", function (assert) {
  let commandFn = jQueryFrameFn("#page\\:mainForm\\:standardLinkAction");
  let destinationSectionFn = jQueryFrameFn("#page\\:actionSection");
  testStandardCommands(commandFn, destinationSectionFn, assert);
});

QUnit.test("Standard Link Link", function (assert) {
  let commandFn = jQueryFrameFn("#page\\:mainForm\\:standardLinkLink");
  let destinationSectionFn = jQueryFrameFn("#page\\:linkSection");
  testStandardCommands(commandFn, destinationSectionFn, assert);
});

function testStandardCommands(commandFn, destinationSectionFn, assert) {
  let backFn = jQueryFrameFn("#page\\:back");

  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    commandFn()[0].click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(destinationSectionFn().length, 1);
  });
  TTT.action(function () {
    backFn()[0].click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal(commandFn().length, 1);
  });
  TTT.startTest();
}

QUnit.test("Target Action Button", function (assert) {
  let commandFn = jQueryFrameFn("#page\\:mainForm\\:targetButtonAction");
  testTargetCommands(commandFn, "#textInput", "accessed by action", assert);
});

QUnit.test("Target Link Button", function (assert) {
  let commandFn = jQueryFrameFn("#page\\:mainForm\\:targetButtonLink");
  testTargetCommands(commandFn, "#textInput", "accessed by link", assert);
});

QUnit.test("Target Action Link", function (assert) {
  let commandFn = jQueryFrameFn("#page\\:mainForm\\:targetLinkAction");
  testTargetCommands(commandFn, "#textInput", "accessed by action", assert);
});

QUnit.test("Target Link Link", function (assert) {
  let commandFn = jQueryFrameFn("#page\\:mainForm\\:targetLinkLink");
  testTargetCommands(commandFn, "#textInput", "accessed by link", assert);
});

QUnit.test("Style must not be a dropdown item", function (assert) {
  assert.expect(3);

  let dropdownMenuFn = jQueryFrameFn("#page\\:mainForm\\:dropdownWithStyle .dropdown-menu");
  let styleAsItemFn = jQueryFrameFn("#page\\:mainForm\\:dropdownWithStyle .dropdown-menu .dropdown-item > style");
  let buttonFn = jQueryFrameFn("#page\\:mainForm\\:dropdownWithStyle > .tobago-button");

  assert.equal(dropdownMenuFn().length, 1);
  assert.equal(styleAsItemFn().length, 0);
  assert.equal(buttonFn().css("width"), "200px");
});

function testTargetCommands(commandFn, targetTextInputSelector, expectedText, assert) {
  let TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    commandFn()[0].click();
  });
  TTT.waitMs(2000); //TobagoTestTools.waitForResponse() didn't recognize responses on a target frame, so we just wait
  TTT.asserts(1, function () {
    let $targetTextInput = jQueryTargetFrame(targetTextInputSelector);
    assert.equal($targetTextInput.val(), expectedText);
  });
  TTT.startTest();
}

function jQueryTargetFrame(expression) {
  return jQueryFrame("#page\\:mainForm\\:targetFrame").contents().find(expression);
}
