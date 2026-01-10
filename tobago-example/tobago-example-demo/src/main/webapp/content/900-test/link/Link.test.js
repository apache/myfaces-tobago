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

it("Standard Action Link", function (done) {
  const commandFn = elementByIdFn("page:mainForm:standardLinkAction");
  const destinationSectionFn = elementByIdFn("page:mainForm:actionSection");
  testStandardCommands(done, commandFn, destinationSectionFn);
});

it("Standard Outcome Link", function (done) {
  const commandFn = elementByIdFn("page:mainForm:standardLinkOutcome");
  const destinationSectionFn = elementByIdFn("page:mainForm:outcomeSection");
  testStandardCommands(done, commandFn, destinationSectionFn);
});

it("Standard Link Link", function (done) {
  const commandFn = elementByIdFn("page:mainForm:standardLinkLink");
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

it("Target Action Link", function (done) {
  const link = elementByIdFn("page:mainForm:targetLinkAction");
  const expectedValue = "accessed by action";
  testTargetCommands(done, link, expectedValue);
});

it("Target Outcome Link", function (done) {
  const link = elementByIdFn("page:mainForm:targetLinkOutcome");
  const expectedValue = "accessed by outcome";
  testTargetCommands(done, link, expectedValue);
});

it("Target Link Link", function (done) {
  const link = elementByIdFn("page:mainForm:targetLinkLink");
  const expectedValue = "accessed by link";
  testTargetCommands(done, link, expectedValue);
});

function testTargetCommands(done, link, expectedValue) {
  const resetButton = elementByIdFn("page:mainForm:reset");

  const test = new JasmineTestTool(done);
  test.setup(() => getTargetFrameInput() === null, null, "click", resetButton);
  test.event("click", link,
      () => getTargetFrameInput() !== null && getTargetFrameInput().value === expectedValue);
  test.do(() => expect(getTargetFrameInput().value).toBe(expectedValue));
  test.start();
}

function getTargetFrameInput() {
  return elementByIdFn("page:mainForm:targetFrame")().contentWindow
      .document.getElementById("page:textInput::field");
}

it("compare a.link and button.link", function (done) {
  const aLinkText = querySelectorFn("#page\\:mainForm\\:aLink200px span");
  const buttonLinkText = querySelectorFn("#page\\:mainForm\\:buttonLink200px span");

  const test = new JasmineTestTool(done);
  test.do(() => expect(aLinkText().offsetLeft).toBe(buttonLinkText().offsetLeft));
  test.start();
});

it("Dropdown menu must have three entries", function (done) {
  const dropdown = elementByIdFn("page:mainForm:dropdownRepeat");

  const test = new JasmineTestTool(done);
  test.do(() => expect(dropdown()).not.toBeNull());
  test.do(() => expect(dropdown().querySelectorAll(".dropdown-item").length).toBe(3));
  test.do(() => expect(dropdown().querySelectorAll(".dropdown-item")[0].textContent).toBe("Nile"));
  test.do(() => expect(dropdown().querySelectorAll(".dropdown-item")[1].textContent).toBe("Amazon"));
  test.do(() => expect(dropdown().querySelectorAll(".dropdown-item")[2].textContent).toBe("Yangtze"));
  test.start();
});

it("Test h1", function (done) {
  let alinkFn = querySelectorFn("#page\\:mainForm\\:link1");
  let buttonlinkFn = querySelectorFn("#page\\:mainForm\\:actionLink1");
  testFont(done, alinkFn, buttonlinkFn);
});

it("Test h2", function (done) {
  let alinkFn = querySelectorFn("#page\\:mainForm\\:link2");
  let buttonlinkFn = querySelectorFn("#page\\:mainForm\\:actionLink2");
  testFont(done, alinkFn, buttonlinkFn);
});

it("Test h3", function (done) {
  let alinkFn = querySelectorFn("#page\\:mainForm\\:link3");
  let buttonlinkFn = querySelectorFn("#page\\:mainForm\\:actionLink3");
  testFont(done, alinkFn, buttonlinkFn);
});

it("Test h4", function (done) {
  let alinkFn = querySelectorFn("#page\\:mainForm\\:link4");
  let buttonlinkFn = querySelectorFn("#page\\:mainForm\\:actionLink4");
  testFont(done, alinkFn, buttonlinkFn);
});

it("Test h5", function (done) {
  let alinkFn = querySelectorFn("#page\\:mainForm\\:link5");
  let buttonlinkFn = querySelectorFn("#page\\:mainForm\\:actionLink5");
  testFont(done, alinkFn, buttonlinkFn);
});

it("Test h6", function (done) {
  let alinkFn = querySelectorFn("#page\\:mainForm\\:link6");
  let buttonlinkFn = querySelectorFn("#page\\:mainForm\\:actionLink6");
  testFont(done, alinkFn, buttonlinkFn);
});

it("Test no heading", function (done) {
  let alinkFn = querySelectorFn("#page\\:mainForm\\:link0");
  let buttonlinkFn = querySelectorFn("#page\\:mainForm\\:actionLink0");
  testFont(done, alinkFn, buttonlinkFn);
});

function testFont(done, alinkFn, buttonlinkFn) {
  let test = new JasmineTestTool(done);
  const alinkComputedStyle = getComputedStyle(alinkFn());
  const buttonlinkFnComputedStyle = getComputedStyle(buttonlinkFn());
  test.do(() => expect(alinkComputedStyle.color).toBe(buttonlinkFnComputedStyle.color));
  test.do(() => expect(alinkComputedStyle.fontFamily).toBe(buttonlinkFnComputedStyle.fontFamily));
  test.do(() => expect(alinkComputedStyle.fontSize).toBe(buttonlinkFnComputedStyle.fontSize));
  test.do(() => expect(alinkComputedStyle.fontWeight).toBe(buttonlinkFnComputedStyle.fontWeight));
  test.do(() => expect(alinkComputedStyle.textDecoration).toBe(buttonlinkFnComputedStyle.textDecoration));
  test.start();
}
