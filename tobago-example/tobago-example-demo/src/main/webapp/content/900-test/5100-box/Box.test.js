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

it("selectOneList dropdown must not force scroll bar on tc:box", function (done) {
  const boxBody = querySelectorFn("#page\\:mainForm\\:box .card-body");
  const filterInput = elementByIdFn("page:mainForm:selectOneList::filter");
  const dropdownMenu = querySelectorFn(".tobago-options.tobago-dropdown-menu[name='page:mainForm:selectOneList']");

  const test = new JasmineTestTool(done);
  test.event("click", filterInput, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(hasScrollbar(boxBody())).toBeFalse());
  test.event("blur", filterInput, () => !dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.do(() => expect(hasScrollbar(boxBody())).toBeFalse());
  test.start();
});

it("selectManyList dropdown must not force scroll bar on tc:box", function (done) {
  const boxBody = querySelectorFn("#page\\:mainForm\\:box .card-body");
  const filterInput = elementByIdFn("page:mainForm:selectManyList::filter");
  const dropdownMenu = querySelectorFn(".tobago-options.tobago-dropdown-menu[name='page:mainForm:selectManyList']");

  const test = new JasmineTestTool(done);
  test.event("click", filterInput, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(hasScrollbar(boxBody())).toBeFalse());
  test.event("blur", filterInput, () => !dropdownMenu().classList.contains("show"));
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.do(() => expect(hasScrollbar(boxBody())).toBeFalse());
  test.start();
});

function hasScrollbar(element) {
  return element.scrollHeight > element.clientHeight;
}
