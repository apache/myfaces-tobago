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

import {activeElementFn, elementByIdFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Dropdown key events", function (done) {
  const toggleButtonFn = elementByIdFn("page:mainForm:root::command");
  const dropdownMenuFn = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:root']");
  const entry1Fn = elementByIdFn("page:mainForm:entry1");
  const checkboxFn = elementByIdFn("page:mainForm:checkbox::field");
  const toggleFn = elementByIdFn("page:mainForm:toggle::field");
  const radioSunFn = elementByIdFn("page:mainForm:radio::0");
  const radioMoonFn = elementByIdFn("page:mainForm:radio::1");
  const multiCheckboxFn = elementByIdFn("page:mainForm:multiCheckbox::0");
  const entry2Fn = elementByIdFn("page:mainForm:entry2");
  const entry3Fn = elementByIdFn("page:mainForm:entry3::command");
  const entry3ParentFn = elementByIdFn("page:mainForm:entry3");
  const subFn = elementByIdFn("page:mainForm:sub::command");
  const subParentFn = elementByIdFn("page:mainForm:sub");
  const link311Fn = elementByIdFn("page:mainForm:link311");
  const link312Fn = elementByIdFn("page:mainForm:link312::command");
  const link312ParentFn = elementByIdFn("page:mainForm:link312");
  const link313Fn = elementByIdFn("page:mainForm:link313");

  const test = new JasmineTestTool(done);
  test.setup(() => !dropdownMenuFn().classList.contains("show"), null, "click", toggleButtonFn);
  test.setup(() => activeElementFn() === toggleButtonFn(), null, "focus", toggleButtonFn);
  test.do(() => expect(dropdownMenuFn().classList).not.toContain("show"));
  test.do(() => expect(dropdownMenuFn().parentElement.tagName).toEqual("TOBAGO-DROPDOWN"));
  test.do(() => expect(activeElementFn()).toEqual(toggleButtonFn()));

  test.do(() => activeElementFn().dispatchEvent(
      new KeyboardEvent("keydown", {bubbles: true, key: 'ArrowDown'})));
  test.wait(() => dropdownMenuFn().classList.contains("show"));
  test.do(() => expect(dropdownMenuFn().classList).toContain("show"));
  test.do(() => expect(dropdownMenuFn().parentElement.classList).toContain("tobago-page-menuStore"));
  test.do(() => expect(activeElementFn()).toEqual(entry1Fn()));

  test.do(() => activeElementFn().dispatchEvent(
      new KeyboardEvent("keydown", {bubbles: true, key: 'ArrowDown'})));
  test.wait(() => activeElementFn() === checkboxFn());
  test.do(() => expect(activeElementFn()).toEqual(checkboxFn()));

  test.do(() => activeElementFn().dispatchEvent(
      new KeyboardEvent("keydown", {bubbles: true, key: 'ArrowDown'})));
  test.wait(() => activeElementFn() === toggleFn());
  test.do(() => expect(activeElementFn()).toEqual(toggleFn()));

  test.do(() => activeElementFn().dispatchEvent(
      new KeyboardEvent("keydown", {bubbles: true, key: 'ArrowDown'})));
  test.wait(() => activeElementFn() === radioSunFn());
  test.do(() => expect(activeElementFn()).toEqual(radioSunFn()));

  test.do(() => activeElementFn().dispatchEvent(
      new KeyboardEvent("keydown", {bubbles: true, key: 'ArrowDown'})));
  test.wait(() => activeElementFn() === radioMoonFn());
  test.do(() => expect(activeElementFn()).toEqual(radioMoonFn()));

  test.do(() => activeElementFn().dispatchEvent(
      new KeyboardEvent("keydown", {bubbles: true, key: 'ArrowDown'})));
  test.wait(() => activeElementFn() === multiCheckboxFn());
  test.do(() => expect(activeElementFn()).toEqual(multiCheckboxFn()));

  test.do(() => activeElementFn().dispatchEvent(
      new KeyboardEvent("keydown", {bubbles: true, key: 'ArrowDown'})));
  test.wait(() => activeElementFn() === entry2Fn());
  test.do(() => expect(activeElementFn()).toEqual(entry2Fn()));

  test.do(() => expect(entry3ParentFn().classList).not.toContain("tobago-show"));
  test.do(() => activeElementFn().dispatchEvent(
      new KeyboardEvent("keydown", {bubbles: true, key: 'ArrowDown'})));
  test.wait(() => activeElementFn() === entry3Fn());
  test.do(() => expect(activeElementFn()).toEqual(entry3Fn()));
  test.do(() => expect(entry3ParentFn().classList).toContain("tobago-show"));

  test.do(() => expect(subParentFn().classList).not.toContain("tobago-show"));
  test.do(() => activeElementFn().dispatchEvent(
      new KeyboardEvent("keydown", {bubbles: true, key: 'ArrowRight'})));
  test.wait(() => activeElementFn() === subFn());
  test.do(() => expect(activeElementFn()).toEqual(subFn()));
  test.do(() => expect(subParentFn().classList).toContain("tobago-show"));

  test.do(() => activeElementFn().dispatchEvent(
      new KeyboardEvent("keydown", {bubbles: true, key: 'ArrowRight'})));
  test.wait(() => activeElementFn() === link311Fn());
  test.do(() => expect(activeElementFn()).toEqual(link311Fn()));

  test.do(() => activeElementFn().dispatchEvent(
      new KeyboardEvent("keydown", {bubbles: true, key: 'ArrowUp'})));
  test.wait(() => activeElementFn() === link313Fn());
  test.do(() => expect(activeElementFn()).toEqual(link313Fn()));

  test.do(() => expect(link312ParentFn().classList).not.toContain("tobago-show"));
  test.do(() => activeElementFn().dispatchEvent(
      new KeyboardEvent("keydown", {bubbles: true, key: 'ArrowUp'})));
  test.wait(() => activeElementFn() === link312Fn());
  test.do(() => expect(activeElementFn()).toEqual(link312Fn()));
  test.do(() => expect(link312ParentFn().classList).toContain("tobago-show"));

  test.do(() => expect(entry3ParentFn().classList).toContain("tobago-show"));
  test.do(() => expect(subParentFn().classList).toContain("tobago-show"));
  test.do(() => expect(link312ParentFn().classList).toContain("tobago-show"));
  test.do(() => activeElementFn().dispatchEvent(
      new KeyboardEvent("keydown", {bubbles: true, key: 'ArrowLeft'})));
  test.wait(() => activeElementFn() === subFn());
  test.do(() => expect(activeElementFn()).toEqual(subFn()));
  test.do(() => expect(entry3ParentFn().classList).toContain("tobago-show"));
  test.do(() => expect(subParentFn().classList).toContain("tobago-show"));
  test.do(() => expect(link312ParentFn().classList).not.toContain("tobago-show"));

  test.do(() => activeElementFn().dispatchEvent(
      new KeyboardEvent("keydown", {bubbles: true, key: 'Escape'})));
  test.do(() => expect(dropdownMenuFn().classList).not.toContain("show"));
  test.do(() => expect(entry3ParentFn().classList).not.toContain("tobago-show"));
  test.do(() => expect(subParentFn().classList).not.toContain("tobago-show"));
  test.do(() => expect(link312ParentFn().classList).not.toContain("tobago-show"));

  test.start();
});

it("Composite component inside dropdown menu", function () {
  const tobagoDropdown = elementByIdFn("page:mainForm:ccDropdown");
  expect(tobagoDropdown().children.length).toEqual(2);
  expect(tobagoDropdown().children[0].tagName).toEqual("BUTTON");
  expect(tobagoDropdown().children[1].tagName).toEqual("DIV");
  expect(tobagoDropdown().children[1].classList.contains("tobago-dropdown-menu")).toBeTrue();

  const tobagoDropdownMenu = tobagoDropdown().children[1];
  expect(tobagoDropdownMenu.children.length).toEqual(2);
  expect(tobagoDropdownMenu.children[0].tagName).toEqual("TOBAGO-DROPDOWN");
  expect(tobagoDropdownMenu.children[0].id).toContain("page:mainForm:ccddEntry");
  expect(tobagoDropdownMenu.children[1].tagName).toEqual("TOBAGO-DROPDOWN");
  expect(tobagoDropdownMenu.children[1].id).toContain("page:mainForm:compositeComponent:");
});
