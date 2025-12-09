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

import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";
import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";

it("Standard Action Button", function (done) {
  const commandFn = elementByIdFn("page:mainForm:standardButtonAction");
  const destinationSectionFn = elementByIdFn("page:mainForm:actionSection");
  testStandardCommands(done, commandFn, destinationSectionFn);
});

it("Standard Outcome Button", function (done) {
  const commandFn = elementByIdFn("page:mainForm:standardButtonOutcome");
  const destinationSectionFn = elementByIdFn("page:mainForm:outcomeSection");
  testStandardCommands(done, commandFn, destinationSectionFn);
});

it("Standard Link Button", function (done) {
  const commandFn = elementByIdFn("page:mainForm:standardButtonLink");
  const destinationSectionFn = elementByIdFn("page:mainForm:linkSection");
  testStandardCommands(done, commandFn, destinationSectionFn);
});

function testStandardCommands(done, commandFn, destinationSectionFn) {
  const backFn = elementByIdFn("page:mainForm:back");

  const test = new JasmineTestTool(done);
  test.event("click", commandFn, () => destinationSectionFn() !== null);
  test.do(() => expect(destinationSectionFn()).not.toBeNull());
  test.event("click", backFn, () => commandFn() !== null);
  test.do(() => expect(commandFn()).not.toBeNull());
  test.start();
}

it("Target Action Button", function (done) {
  const button = elementByIdFn("page:mainForm:targetButtonAction");
  const expectedValue = "accessed by action";
  testTargetCommands(done, button, expectedValue);
});

it("Target Outcome Button", function (done) {
  const button = elementByIdFn("page:mainForm:targetButtonOutcome");
  const expectedValue = "accessed by outcome";
  testTargetCommands(done, button, expectedValue);
});

it("Target Link Button", function (done) {
  const button = elementByIdFn("page:mainForm:targetButtonLink");
  const expectedValue = "accessed by link";
  testTargetCommands(done, button, expectedValue);
});

function testTargetCommands(done, button, expectedValue) {
  const resetButton = elementByIdFn("page:mainForm:reset");

  const test = new JasmineTestTool(done);
  test.setup(() => getTargetFrameInput() === null, null, "click", resetButton);
  test.event("click", button,
      () => getTargetFrameInput() !== null && getTargetFrameInput().value === expectedValue);
  test.do(() => expect(getTargetFrameInput().value).toBe(expectedValue));
  test.start();
}

function getTargetFrameInput() {
  return elementByIdFn("page:mainForm:targetFrame")().contentWindow
      .document.getElementById("page:textInput::field");
}

it("Style must not be a dropdown item", function (done) {
  const dropdownMenuFn = querySelectorFn("#page\\:mainForm\\:dropdownWithStyle .tobago-dropdown-menu");
  const styleAsItemFn = querySelectorFn("#page\\:mainForm\\:dropdownWithStyle .tobago-dropdown-menu .dropdown-item > style");
  const buttonFn = elementByIdFn("page:mainForm:dropdownWithStyle::command");

  const test = new JasmineTestTool(done);
  test.do(() => expect(dropdownMenuFn()).not.toBeNull());
  test.do(() => expect(styleAsItemFn()).toBeNull());
  test.do(() => expect(buttonFn().offsetWidth).toEqual(200));
  test.start();
});
