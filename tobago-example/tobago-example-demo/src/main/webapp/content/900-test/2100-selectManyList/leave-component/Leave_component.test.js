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
import {activeElementFn, elementByIdFn, querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";

it("Open dropdown, filter, leave component", function (done) {
  const selectManyList = elementByIdFn("page:mainForm:selectManyList");
  const selectField = elementByIdFn("page:mainForm:selectManyList::selectField");
  const filterInput = elementByIdFn("page:mainForm:selectManyList::filter");
  const dropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:selectManyList']");
  const hiddenSelectItems = querySelectorAllFn(".tobago-dropdown-menu[name='page:mainForm:selectManyList'] .tobago-select-item.d-none");
  const submitButton = elementByIdFn("page:mainForm:submit");

  const test = new JasmineTestTool(done);
  test.setup(() => activeElementFn() === submitButton(), null, "focus", submitButton);
  test.do(() => expect(activeElementFn()).toBe(submitButton()));
  test.do(() => expect(selectManyList().classList).not.toContain("tobago-focus"));
  test.do(() => expect(filterInput().value).toBe(""));
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.do(() => expect(hiddenSelectItems().length).toBe(0));

  test.event("click", selectField, () => dropdownMenu().classList.contains("show"));
  test.do(() => expect(activeElementFn()).toBe(filterInput()));
  test.do(() => expect(selectManyList().classList).toContain("tobago-focus"));
  test.do(() => expect(filterInput().value).toBe(""));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(hiddenSelectItems().length).toBe(0));

  test.do(() => filterInput().value = 'a');
  test.event("input", filterInput, () => hiddenSelectItems().length > 0);
  test.do(() => expect(activeElementFn()).toBe(filterInput()));
  test.do(() => expect(selectManyList().classList).toContain("tobago-focus"));
  test.do(() => expect(filterInput().value).toBe("a"));
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(hiddenSelectItems().length).toBe(5));

  test.event("focus", submitButton, () => hiddenSelectItems().length === 0);
  test.do(() => expect(activeElementFn()).toBe(submitButton()));
  test.do(() => expect(selectManyList().classList).not.toContain("tobago-focus"));
  test.do(() => expect(filterInput().value).toBe(""));
  test.do(() => expect(dropdownMenu().classList).not.toContain("show"));
  test.do(() => expect(hiddenSelectItems().length).toBe(0));
  test.start();
});
