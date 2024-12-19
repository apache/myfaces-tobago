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

import {elementByIdFn, querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Tab with working sheet", function (done) {
  const tabGroupIndex = elementByIdFn("page:mainForm:tabGroup::index");
  const navLink = querySelectorFn("#page\\:mainForm\\:tabWithWorkingSheet .nav-link");
  const cols = querySelectorAllFn("#page\\:mainForm\\:tabWithWorkingSheet\\:sheetWorking header colgroup col");
  const firstTh = querySelectorFn("#page\\:mainForm\\:tabWithWorkingSheet\\:sheetWorking header tr th");
  const firstTd = querySelectorFn("#page\\:mainForm\\:tabWithWorkingSheet\\:sheetWorking .tobago-body tbody tr td");

  const test = new JasmineTestTool(done);
  test.setup(() => tabGroupIndex().value === "0", null, "click", navLink);
  test.do(() => expect(cols()[0].width).toBe(cols()[1].width));
  test.do(() => expect(cols()[0].width).toBe(cols()[2].width));
  test.do(() => expect(firstTh().getBoundingClientRect().width).toBe(firstTd().getBoundingClientRect().width));
  test.start();
});

it("Tab with sheet", function (done) {
  const tabGroupIndex = elementByIdFn("page:mainForm:tabGroup::index");
  const navLink = querySelectorFn("#page\\:mainForm\\:tabWithSheet .nav-link");
  const cols = querySelectorAllFn("#page\\:mainForm\\:tabWithSheet\\:sheet header colgroup col");
  const firstTh = querySelectorFn("#page\\:mainForm\\:tabWithSheet\\:sheet header tr th");
  const firstTd = querySelectorFn("#page\\:mainForm\\:tabWithSheet\\:sheet .tobago-body tbody tr td");

  const test = new JasmineTestTool(done);
  test.setup(() => tabGroupIndex().value === "0", null, "click", navLink);
  test.do(() => expect(cols()[0].width).toBe(cols()[1].width));
  test.do(() => expect(cols()[0].width).toBe(cols()[2].width));
  test.do(() => expect(firstTh().getBoundingClientRect().width).toBe(firstTd().getBoundingClientRect().width));
  test.start();
});

it("Tab with lazy sheet", function (done) {
  const tabGroupIndex = elementByIdFn("page:mainForm:tabGroup::index");
  const navLink = querySelectorFn("#page\\:mainForm\\:tabWithLazySheet .nav-link");
  const cols = querySelectorAllFn("#page\\:mainForm\\:tabWithLazySheet\\:lazySheet header colgroup col");
  const firstTh = querySelectorFn("#page\\:mainForm\\:tabWithLazySheet\\:lazySheet header tr th");
  const firstTd = querySelectorFn("#page\\:mainForm\\:tabWithLazySheet\\:lazySheet .tobago-body tbody tr td");

  const test = new JasmineTestTool(done);
  test.setup(() => tabGroupIndex().value === "0", null, "click", navLink);
  test.do(() => expect(cols()[0].width).toBe(cols()[1].width));
  test.do(() => expect(cols()[0].width).toBe(cols()[2].width));
  test.do(() => expect(firstTh().getBoundingClientRect().width).toBe(firstTd().getBoundingClientRect().width));
  test.start();
});
