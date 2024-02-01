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
import {elementByIdFn, querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";

it("open dropdown, select 'one', close dropdown, click on white space", function (done) {
  const badges = querySelectorAllFn("#page\\:mainForm\\:component\\:\\:selectField .btn-group");
  const filterInput = elementByIdFn("page:mainForm:component::filter");
  const dropdownMenu = querySelectorFn(".tobago-options.tobago-dropdown-menu[name='page:mainForm:component']");
  const entryOne = querySelectorFn(".tobago-options.tobago-dropdown-menu[name='page:mainForm:component'] td[value='one']");

  const test = new JasmineTestTool(done);
  test.do(() => expect(badges().length).toBe(0));
  test.event("click", filterInput, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.event("click", entryOne, () => badges().length === 1);
  test.do(() => expect(badges().length).toBe(1));
  test.event("blur", filterInput, () => !dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.event("click", filterInput, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.start();
});

it("width of filter input field must be '0px'", function (done) {
  const filterInput = elementByIdFn("page:mainForm:component::filter");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getComputedStyle(filterInput()).width).toBe("0px"));
  test.start();
});
