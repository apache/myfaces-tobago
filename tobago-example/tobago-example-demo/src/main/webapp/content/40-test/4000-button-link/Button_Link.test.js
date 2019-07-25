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

QUnit.test("Standard Action Button", function (assert) {
  var commandFn = jQueryFrameFn("#page\\:mainForm\\:standardButtonAction");
  var destinationSectionFn = jQueryFrameFn("#page\\:actionSection");
  testStandardCommands(commandFn, destinationSectionFn, assert);
});

QUnit.test("Standard Link Button", function (assert) {
  var commandFn = jQueryFrameFn("#page\\:mainForm\\:standardButtonLink");
  var destinationSectionFn = jQueryFrameFn("#page\\:linkSection");
  testStandardCommands(commandFn, destinationSectionFn, assert);
});

QUnit.test("Standard Action Link", function (assert) {
  var commandFn = jQueryFrameFn("#page\\:mainForm\\:standardLinkAction");
  var destinationSectionFn = jQueryFrameFn("#page\\:actionSection");
  testStandardCommands(commandFn, destinationSectionFn, assert);
});

QUnit.test("Standard Link Link", function (assert) {
  var commandFn = jQueryFrameFn("#page\\:mainForm\\:standardLinkLink");
  var destinationSectionFn = jQueryFrameFn("#page\\:linkSection");
  testStandardCommands(commandFn, destinationSectionFn, assert);
});

function testStandardCommands(commandFn, destinationSectionFn, assert) {
  var backFn = jQueryFrameFn("#page\\:back");

  var TTT = new TobagoTestTool(assert);
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
  var commandFn = jQueryFrameFn("#page\\:mainForm\\:targetButtonAction");
  testTargetCommands(commandFn, "#textInput", "accessed by action", assert);
});

QUnit.test("Target Link Button", function (assert) {
  var commandFn = jQueryFrameFn("#page\\:mainForm\\:targetButtonLink");
  testTargetCommands(commandFn, "#textInput", "accessed by link", assert);
});

QUnit.test("Target Action Link", function (assert) {
  var commandFn = jQueryFrameFn("#page\\:mainForm\\:targetLinkAction");
  testTargetCommands(commandFn, "#textInput", "accessed by action", assert);
});

QUnit.test("Target Link Link", function (assert) {
  var commandFn = jQueryFrameFn("#page\\:mainForm\\:targetLinkLink");
  testTargetCommands(commandFn, "#textInput", "accessed by link", assert);
});

QUnit.test("Style must not be a dropdown item", function (assert) {
  assert.expect(3);

  var dropdownMenuFn = jQueryFrameFn("#page\\:mainForm\\:dropdownWithStyle .dropdown-menu");
  var styleAsItemFn = jQueryFrameFn("#page\\:mainForm\\:dropdownWithStyle .dropdown-menu .dropdown-item > style");
  var buttonFn = jQueryFrameFn("#page\\:mainForm\\:dropdownWithStyle > .tobago-button");

  assert.equal(dropdownMenuFn().length, 1);
  assert.equal(styleAsItemFn().length, 0);
  assert.equal(buttonFn().css("width"), "200px");
});

function testTargetCommands(commandFn, targetTextInputSelector, expectedText, assert) {
  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    commandFn()[0].click();
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
