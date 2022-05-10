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

it("default sheet", function (done) {
  const reloadFn = elementByIdFn("page:mainForm:reload");
  const selectedFn = elementByIdFn("page:mainForm:sheetDefault::selected");
  const row2Fn = querySelectorFn("#page\\:mainForm\\:sheetDefault tr[row-index='2']");
  const row4Fn = querySelectorFn("#page\\:mainForm\\:sheetDefault tr[row-index='4']");

  const test = new JasmineTestTool(done);
  test.setup(() => selectedFn().value === "[]", "click", reloadFn);
  test.do(() => expect(selectedFn().value).toEqual("[]"));
  test.event("click", row2Fn, () => selectedFn().value === "[2]");
  test.do(() => expect(selectedFn().value).toEqual("[2]"));
  test.event("click", row4Fn, () => selectedFn().value === "[4]");
  test.do(() => expect(selectedFn().value).toEqual("[4]"));
  test.start();
});

it("columnSelector sheet", function (done) {
  const reloadFn = elementByIdFn("page:mainForm:reload");
  const selectedFn = elementByIdFn("page:mainForm:sheetColumnSelector::selected");
  const row2Fn = querySelectorFn("#page\\:mainForm\\:sheetColumnSelector tr[row-index='2']");
  const row4Fn = querySelectorFn("#page\\:mainForm\\:sheetColumnSelector tr[row-index='4']");

  const test = new JasmineTestTool(done);
  test.setup(() => selectedFn().value === "[]", "click", reloadFn);
  test.do(() => expect(selectedFn().value).toEqual("[]"));
  test.event("click", row2Fn, () => selectedFn().value === "[2]");
  test.do(() => expect(selectedFn().value).toEqual("[2]"));
  test.event("click", row4Fn, () => selectedFn().value === "[2,4]");
  test.do(() => expect(selectedFn().value).toEqual("[2,4]"));
  test.start();
});

it("disabled columnSelector sheet", function (done) {
  const reloadFn = elementByIdFn("page:mainForm:reload");
  const sheetFn = elementByIdFn("page:mainForm:sheetColumnSelectorDisabled");
  const selectedFn = elementByIdFn("page:mainForm:sheetColumnSelectorDisabled::selected");
  const row2Fn = querySelectorFn("#page\\:mainForm\\:sheetColumnSelectorDisabled tr[row-index='2']");
  const row4Fn = querySelectorFn("#page\\:mainForm\\:sheetColumnSelectorDisabled tr[row-index='4']");

  const test = new JasmineTestTool(done);
  test.setup(() => sheetFn().getAttribute("data-tobago-last-clicked-row-index") === null, "click", reloadFn);
  test.do(() => expect(selectedFn().value).toEqual("[]"));
  test.event("click", row2Fn, () => sheetFn().getAttribute("data-tobago-last-clicked-row-index") === "2");
  test.do(() => expect(selectedFn().value).toEqual("[]"));
  test.event("click", row4Fn, () => sheetFn().getAttribute("data-tobago-last-clicked-row-index") === "4");
  test.do(() => expect(selectedFn().value).toEqual("[]"));
  test.start();
});
