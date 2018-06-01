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
  var $command = jQueryFrameFn("#page\\:mainForm\\:standardButtonAction");
  var $destinationSection = jQueryFrameFn("#page\\:actionSection");
  testStandardCommands($command, $destinationSection, assert);
});

QUnit.test("Standard Link Button", function (assert) {
  var $command = jQueryFrameFn("#page\\:mainForm\\:standardButtonLink");
  var $destinationSection = jQueryFrameFn("#page\\:linkSection");
  testStandardCommands($command, $destinationSection, assert);
});

QUnit.test("Standard Action Link", function (assert) {
  var $command = jQueryFrameFn("#page\\:mainForm\\:standardLinkAction");
  var $destinationSection = jQueryFrameFn("#page\\:actionSection");
  testStandardCommands($command, $destinationSection, assert);
});

QUnit.test("Standard Link Link", function (assert) {
  var $command = jQueryFrameFn("#page\\:mainForm\\:standardLinkLink");
  var $destinationSection = jQueryFrameFn("#page\\:linkSection");
  testStandardCommands($command, $destinationSection, assert);
});

function testStandardCommands($command, $destinationSection, assert) {
  var $back = jQueryFrameFn("#page\\:back");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $command()[0].click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($destinationSection().length, 1);
  });
  TTT.action(function () {
    $back()[0].click();
  });
  TTT.waitForResponse();
  TTT.asserts(1, function () {
    assert.equal($command().length, 1);
  });
  TTT.startTest();
}

QUnit.test("Target Action Button", function (assert) {
  var $command = jQueryFrameFn("#page\\:mainForm\\:targetButtonAction");
  testTargetCommands($command, "#textInput", "accessed by action", assert);
});

QUnit.test("Target Link Button", function (assert) {
  var $command = jQueryFrameFn("#page\\:mainForm\\:targetButtonLink");
  testTargetCommands($command, "#textInput", "accessed by link", assert);
});

QUnit.test("Target Action Link", function (assert) {
  var $command = jQueryFrameFn("#page\\:mainForm\\:targetLinkAction");
  testTargetCommands($command, "#textInput", "accessed by action", assert);
});

QUnit.test("Target Link Link", function (assert) {
  var $command = jQueryFrameFn("#page\\:mainForm\\:targetLinkLink");
  testTargetCommands($command, "#textInput", "accessed by link", assert);
});

QUnit.test("Style must not be a dropdown item", function (assert) {
  assert.expect(3);

  var $dropdownMenu = jQueryFrameFn("#page\\:mainForm\\:dropdownWithStyle .dropdown-menu");
  var $styleAsItem = jQueryFrameFn("#page\\:mainForm\\:dropdownWithStyle .dropdown-menu .dropdown-item > style");
  var $button = jQueryFrameFn("#page\\:mainForm\\:dropdownWithStyle > .tobago-button");

  assert.equal($dropdownMenu().length, 1);
  assert.equal($styleAsItem().length, 0);
  assert.equal($button().css("width"), "200px");
});

function testTargetCommands($command, targetTextInputSelector, expectedText, assert) {
  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $command()[0].click();
  });
  TTT.waitMs(2000); //TobagoTestTools.waitForResponse() didn't recognize responses on a target frame, so we just wait
  TTT.asserts(1, function () {
    var $targetTextInput = jQueryTargetFrame(targetTextInputSelector);
    assert.equal($targetTextInput.val(), expectedText);
  });
  TTT.startTest();
}

function jQueryTargetFrame(expression) {
  return jQueryFrame("#page\\:mainForm\\:targetFrame").contents().find(expression);
}
