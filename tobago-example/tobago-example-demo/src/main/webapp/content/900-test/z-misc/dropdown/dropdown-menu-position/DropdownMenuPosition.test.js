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

import {elementByIdFn, innerHeight, querySelectorFn, scrollTo} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Button dropdown on the right", function (done) {
  const button = elementByIdFn("page:mainForm:buttonDropdownRight::command");
  const dropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:buttonDropdownRight']");

  const test = new JasmineTestTool(done);
  test.event("click", button, () => dropdownMenu().classList.contains("show"));
  test.waitMs(100);
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(dropdownMenu().getBoundingClientRect().left).toBeGreaterThan(0));
  test.do(() => expect(dropdownMenu().getBoundingClientRect().left).toBeLessThan(window.innerWidth));
  test.do(() => expect(dropdownMenu().getBoundingClientRect().right).toBeGreaterThan(0));
  test.do(() => expect(dropdownMenu().getBoundingClientRect().right).toBeLessThan(window.innerWidth));
  test.start();
});

it("default - SelectOneList: upper half", function (done) {
  const tobagoHeader = querySelectorFn("tobago-header.sticky-top");
  const secondHeader = elementByIdFn("page:mainForm:secondHeader");
  const selectOneList = elementByIdFn("page:mainForm:defaultSelectOneList");
  const selectField = elementByIdFn("page:mainForm:defaultSelectOneList::selectField");
  const dropdownMenu = querySelectorFn(".tobago-options.tobago-dropdown-menu[name='page:mainForm:defaultSelectOneList']");

  const test = new JasmineTestTool(done);
  test.do(() => scrollTo(0, 0));
  test.do(() => scrollTo(rect(selectOneList()).x,
      rect(selectOneList()).y - parseHeight(tobagoHeader()) - parseHeight(secondHeader())));
  test.waitMs(1000);
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.event("click", selectField, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(rect(dropdownMenu()).left).toBeCloseTo(rect(selectField()).left), 1);
  test.do(() => expect(rect(dropdownMenu()).top).toBeCloseTo(rect(selectField()).bottom + parseStyle(dropdownMenu(), "marginTop"), 1));
  test.do(() => expect(parseStyle(dropdownMenu(), "width")).toBeCloseTo(parseStyle(selectField(), "width"), 1));
  test.start();
});

it("default - SelectOneList: lower half", function (done) {
  const tobagoFooter = querySelectorFn("tobago-footer.fixed-bottom");
  const selectOneList = elementByIdFn("page:mainForm:defaultSelectOneList");
  const selectField = elementByIdFn("page:mainForm:defaultSelectOneList::selectField");
  const dropdownMenu = querySelectorFn(".tobago-options.tobago-dropdown-menu[name='page:mainForm:defaultSelectOneList']");

  const test = new JasmineTestTool(done);
  test.do(() => scrollTo(0, 0));
  test.do(() => scrollTo(rect(selectOneList()).x, rect(selectOneList()).bottom - innerHeight() + parseStyle(tobagoFooter(), "height")));
  test.waitMs(1000);
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.event("click", selectField, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(rect(dropdownMenu()).left).toBeCloseTo(rect(selectField()).left, 1));
  test.do(() => expect(rect(dropdownMenu()).bottom).toBeCloseTo(rect(selectField()).top - parseStyle(dropdownMenu(), "marginBottom"), 1));
  test.start();
});

it("default - button dropdown: upper half", function (done) {
  const tobagoHeader = querySelectorFn("tobago-header.sticky-top");
  const button = elementByIdFn("page:mainForm:defaultButtonDropdown::command");
  const dropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:defaultButtonDropdown']");

  const test = new JasmineTestTool(done);
  test.do(() => scrollTo(0, 0));
  test.do(() => scrollTo(rect(button()).x, rect(button()).y - parseStyle(tobagoHeader(), "height")));
  test.waitMs(1000);
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.event("click", button, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(rect(dropdownMenu()).left).toBeCloseTo(rect(button()).left, 1));
  test.do(() => expect(rect(dropdownMenu()).top).toBeCloseTo(rect(button()).bottom + parseStyle(dropdownMenu(), "marginTop"), 1));
  test.start();
});

it("default - button dropdown: lower half", function (done) {
  const tobagoFooter = querySelectorFn("tobago-footer.fixed-bottom");
  const button = elementByIdFn("page:mainForm:defaultButtonDropdown::command");
  const dropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:defaultButtonDropdown']");

  const test = new JasmineTestTool(done);
  test.do(() => scrollTo(0, 0));
  test.do(() => scrollTo(rect(button()).x, rect(button()).bottom - innerHeight() + parseStyle(tobagoFooter(), "height")));
  test.waitMs(1000);
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.event("click", button, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(rect(dropdownMenu()).left).toBeCloseTo(rect(button()).left, 1));
  test.do(() => expect(rect(dropdownMenu()).bottom).toBeCloseTo(rect(button()).top - parseStyle(dropdownMenu(), "marginBottom"), 1));
  test.start();
});

it("default - button dropdown in tc:in after-facet: upper half", function (done) {
  const tobagoHeader = querySelectorFn("tobago-header.sticky-top");
  const button = elementByIdFn("page:mainForm:defaultInAfterButtonDropdown::command");
  const dropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:defaultInAfterButtonDropdown']");

  const test = new JasmineTestTool(done);
  test.do(() => scrollTo(0, 0));
  test.do(() => scrollTo(rect(button()).x, rect(button()).y - parseStyle(tobagoHeader(), "height")));
  test.waitMs(1000);
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.event("click", button, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(rect(dropdownMenu()).right).toBeCloseTo(rect(button()).right, 1));
  test.do(() => expect(rect(dropdownMenu()).top).toBeCloseTo(rect(button()).bottom + parseStyle(dropdownMenu(), "marginTop"), 1));
  test.start();
});

it("default - button dropdown in tc:in after-facet: lower half", function (done) {
  const tobagoFooter = querySelectorFn("tobago-footer.fixed-bottom");
  const button = elementByIdFn("page:mainForm:defaultInAfterButtonDropdown::command");
  const dropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:defaultInAfterButtonDropdown']");

  const test = new JasmineTestTool(done);
  test.do(() => scrollTo(0, 0));
  test.do(() => scrollTo(rect(button()).x, rect(button()).bottom - innerHeight() + parseStyle(tobagoFooter(), "height")));
  test.waitMs(1000);
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.event("click", button, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(rect(dropdownMenu()).right).toBeCloseTo(rect(button()).right, 1));
  test.do(() => expect(rect(dropdownMenu()).bottom).toBeCloseTo(rect(button()).top - parseStyle(dropdownMenu(), "marginBottom"), 1));
  test.start();
});

it("transformX0 - SelectOneList: upper half", function (done) {
  const tobagoHeader = querySelectorFn("tobago-header.sticky-top");
  const secondHeader = elementByIdFn("page:mainForm:secondHeader");
  const selectOneList = elementByIdFn("page:mainForm:transformX0selectOneList");
  const selectField = elementByIdFn("page:mainForm:transformX0selectOneList::selectField");
  const dropdownMenu = querySelectorFn(".tobago-options.tobago-dropdown-menu[name='page:mainForm:transformX0selectOneList']");

  const test = new JasmineTestTool(done);
  test.do(() => scrollTo(0, 0));
  test.do(() => scrollTo(rect(selectOneList()).x,
      rect(selectOneList()).y - parseHeight(tobagoHeader()) - parseHeight(secondHeader())));
  test.waitMs(1000);
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.event("click", selectField, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(rect(dropdownMenu()).left).toBeCloseTo(rect(selectField()).left, 1));
  test.do(() => expect(rect(dropdownMenu()).top).toBeCloseTo(rect(selectField()).bottom + parseStyle(dropdownMenu(), "marginTop"), 1));
  test.do(() => expect(parseStyle(dropdownMenu(), "width")).toBeCloseTo(parseStyle(selectField(), "width"), 1));
  test.start();
});

it("transformX0 - SelectOneList: lower half", function (done) {
  const tobagoFooter = querySelectorFn("tobago-footer.fixed-bottom");
  const selectOneList = elementByIdFn("page:mainForm:transformX0selectOneList");
  const selectField = elementByIdFn("page:mainForm:transformX0selectOneList::selectField");
  const dropdownMenu = querySelectorFn(".tobago-options.tobago-dropdown-menu[name='page:mainForm:transformX0selectOneList']");

  const test = new JasmineTestTool(done);
  test.do(() => scrollTo(0, 0));
  test.do(() => scrollTo(rect(selectOneList()).x, rect(selectOneList()).bottom - innerHeight() + parseStyle(tobagoFooter(), "height")));
  test.waitMs(1000);
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.event("click", selectField, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(rect(dropdownMenu()).left).toBeCloseTo(rect(selectField()).left, 1));
  test.do(() => expect(rect(dropdownMenu()).bottom).toBeCloseTo(rect(selectField()).top - parseStyle(dropdownMenu(), "marginBottom"), 1));
  test.start();
});

it("transformX0 - button dropdown: upper half", function (done) {
  const tobagoHeader = querySelectorFn("tobago-header.sticky-top");
  const button = elementByIdFn("page:mainForm:transformX0buttonDropdown::command");
  const dropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:transformX0buttonDropdown']");

  const test = new JasmineTestTool(done);
  test.do(() => scrollTo(0, 0));
  test.do(() => scrollTo(rect(button()).x, rect(button()).y - parseStyle(tobagoHeader(), "height")));
  test.waitMs(1000);
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.event("click", button, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(rect(dropdownMenu()).left).toBeCloseTo(rect(button()).left, 1));
  test.do(() => expect(rect(dropdownMenu()).top).toBeCloseTo(rect(button()).bottom + parseStyle(dropdownMenu(), "marginTop"), 1));
  test.start();
});

it("transformX0 - button dropdown: lower half", function (done) {
  const tobagoFooter = querySelectorFn("tobago-footer.fixed-bottom");
  const button = elementByIdFn("page:mainForm:transformX0buttonDropdown::command");
  const dropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:transformX0buttonDropdown']");

  const test = new JasmineTestTool(done);
  test.do(() => scrollTo(0, 0));
  test.do(() => scrollTo(rect(button()).x, rect(button()).bottom - innerHeight() + parseStyle(tobagoFooter(), "height")));
  test.waitMs(1000);
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.event("click", button, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(rect(dropdownMenu()).left).toBeCloseTo(rect(button()).left, 1));
  test.do(() => expect(rect(dropdownMenu()).bottom).toBeCloseTo(rect(button()).top - parseStyle(dropdownMenu(), "marginBottom"), 1));
  test.start();
});

it("transformX0 - button dropdown in tc:in after-facet: upper half", function (done) {
  const tobagoHeader = querySelectorFn("tobago-header.sticky-top");
  const button = elementByIdFn("page:mainForm:transformX0inAfterButtonDropdown::command");
  const dropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:transformX0inAfterButtonDropdown']");

  const test = new JasmineTestTool(done);
  test.do(() => scrollTo(0, 0));
  test.do(() => scrollTo(rect(button()).x, rect(button()).y - parseStyle(tobagoHeader(), "height")));
  test.waitMs(1000);
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.event("click", button, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(rect(dropdownMenu()).right).toBeCloseTo(rect(button()).right, 1));
  test.do(() => expect(rect(dropdownMenu()).top).toBeCloseTo(rect(button()).bottom + parseStyle(dropdownMenu(), "marginTop"), 1));
  test.start();
});

it("transformX0 - button dropdown in tc:in after-facet: lower half", function (done) {
  const tobagoFooter = querySelectorFn("tobago-footer.fixed-bottom");
  const button = elementByIdFn("page:mainForm:transformX0inAfterButtonDropdown::command");
  const dropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:transformX0inAfterButtonDropdown']");

  const test = new JasmineTestTool(done);
  test.do(() => scrollTo(0, 0));
  test.do(() => scrollTo(rect(button()).x, rect(button()).bottom - innerHeight() + parseStyle(tobagoFooter(), "height")));
  test.waitMs(1000);
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.event("click", button, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(rect(dropdownMenu()).right).toBeCloseTo(rect(button()).right, 1));
  test.do(() => expect(rect(dropdownMenu()).bottom).toBeCloseTo(rect(button()).top - parseStyle(dropdownMenu(), "marginBottom"), 1));
  test.start();
});

function rect(element) {
  return element.getBoundingClientRect();
}

function parseHeight(element) {
  return parseInt(getComputedStyle(element)["height"])
}

function parseStyle(element, style) {
  return parseInt(getComputedStyle(element)[style])
}
