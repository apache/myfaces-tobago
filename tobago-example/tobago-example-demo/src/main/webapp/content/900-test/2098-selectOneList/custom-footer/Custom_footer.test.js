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

it("rendered=true, open dropdown, footer visible", function (done) {
  const filterInput = elementByIdFn("page:mainForm:renderedTrue::filter");
  const tobagoDropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:renderedTrue']");
  const tobagoSelectItems = querySelectorAllFn(".tobago-dropdown-menu[name='page:mainForm:renderedTrue'] .tobago-select-item");
  const customFooter = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:renderedTrue'] .tobago-custom-footer");

  const test = new JasmineTestTool(done);
  test.event("input", filterInput, () => tobagoDropdownMenu().classList.contains("show"));
  test.do(() => expect(tobagoDropdownMenu().classList).toContain("show"));
  test.do(() => expect(tobagoSelectItems().length).toBe(0));
  test.do(() => expect(customFooter().textContent.trim()).toBe("filter for results"));
  test.start();
});

it("rendered=false, open dropdown, footer not visible", function (done) {
  const filterInput = elementByIdFn("page:mainForm:renderedFalse::filter");
  const tobagoDropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:renderedFalse']");
  const tobagoSelectItems = querySelectorAllFn(".tobago-dropdown-menu[name='page:mainForm:renderedFalse'] .tobago-select-item");
  const customFooterCell = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:renderedFalse'] .tobago-custom-footer td");

  const test = new JasmineTestTool(done);
  test.event("input", filterInput, () => tobagoDropdownMenu().classList.contains("show"));
  test.do(() => expect(tobagoDropdownMenu().classList).toContain("show"));
  test.do(() => expect(tobagoSelectItems().length).toBe(0));
  test.do(() => expect(getComputedStyle(customFooterCell()).display).toBe("none"));
  test.start();
});
