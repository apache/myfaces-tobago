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

import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Click on dropdown menu in sortable 'name' header", function (done) {
  const nameSorter = elementByIdFn("page:mainForm:sheet:name_sorter");
  const outputName = elementByIdFn("page:mainForm:sheet:_col0");
  const dropdownButton = elementByIdFn("page:mainForm:sheet:nameDropdown::command");
  const dropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:sheet:nameDropdown']");

  const test = new JasmineTestTool(done);
  test.do(() => expect(nameSorter().classList).toContain("tobago-sortable"));
  test.do(() => expect(nameSorter().classList).not.toContain("tobago-ascending"));
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));

  test.event("click", dropdownButton, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(nameSorter().classList).toContain("tobago-sortable"));
  test.do(() => expect(nameSorter().classList).not.toContain("tobago-ascending"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));

  test.event("click", outputName, () => nameSorter().classList.contains("tobago-ascending"));
  test.do(() => expect(nameSorter().classList).toContain("tobago-sortable"));
  test.do(() => expect(nameSorter().classList).toContain("tobago-ascending"));
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));

  test.start();
});

it("Click 'Submit' in 'Period' column", function (done) {
  const hiddenInputSelected = elementByIdFn("page:mainForm:sheet::selected");
  const row1Button = elementByIdFn("page:mainForm:sheet:1:submit");
  const row3Button = elementByIdFn("page:mainForm:sheet:3:submit");

  const test = new JasmineTestTool(done);
  test.setup(() => hiddenInputSelected().value === "[3]", null, "click", row3Button);
  test.do(() => expect(hiddenInputSelected().value).toBe("[3]"));
  test.event("click", row1Button, () => hiddenInputSelected().value === "[1]");
  test.do(() => expect(hiddenInputSelected().value).toBe("[1]"));

  test.start();
});

it("Click dropdown menu in row 0 and row 2", function (done) {
  const hiddenInputSelected = elementByIdFn("page:mainForm:sheet::selected");
  const row1Button = elementByIdFn("page:mainForm:sheet:1:submit");
  const row0DropdownButton = elementByIdFn("page:mainForm:sheet:0:yearDropdown::command");
  const row0DropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:sheet:0:yearDropdown']");
  const row2DropdownButton = elementByIdFn("page:mainForm:sheet:2:yearDropdown::command");
  const row2DropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:sheet:2:yearDropdown']");

  const test = new JasmineTestTool(done);
  test.setup(() => hiddenInputSelected().value === "[1]", null, "click", row1Button);
  test.do(() => expect(hiddenInputSelected().value).toBe("[1]"));
  test.do(() => expect(row0DropdownMenu().classList).not.toContain("show"));
  test.do(() => expect(row2DropdownMenu().classList).not.toContain("show"));

  test.event("click", row0DropdownButton, () => row0DropdownMenu().classList.contains("show"));
  test.do(() => expect(hiddenInputSelected().value).toBe("[1]"));
  test.do(() => expect(row0DropdownMenu().classList).toContain("show"));
  test.do(() => expect(row2DropdownMenu().classList).not.toContain("show"));

  test.event("click", row2DropdownButton, () => row2DropdownMenu().classList.contains("show"));
  test.do(() => expect(hiddenInputSelected().value).toBe("[1]"));
  test.do(() => expect(row0DropdownMenu().classList).not.toContain("show"));
  test.do(() => expect(row2DropdownMenu().classList).toContain("show"));

  test.start();
});
