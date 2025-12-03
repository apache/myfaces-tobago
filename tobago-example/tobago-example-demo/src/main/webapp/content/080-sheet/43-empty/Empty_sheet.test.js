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

it("Default: No selection of empty-row-message", function (done) {
  const sheet = elementByIdFn("page:mainForm:default");
  const noEntriesRow = querySelectorFn("#page\\:mainForm\\:default .tobago-no-entries");

  let rowSelectionChange = 0;
  let clickNoEntriesRow = 0;

  const test = new JasmineTestTool(done);
  test.do(() => sheet().addEventListener("tobago.sheet.rowSelectionChange", () => rowSelectionChange++));
  test.do(() => noEntriesRow().addEventListener("click", () => clickNoEntriesRow++));
  test.event("click", noEntriesRow, () => clickNoEntriesRow >= 1);
  test.do(() => expect(clickNoEntriesRow).toBe(1));
  test.do(() => expect(rowSelectionChange).toBe(0));
  test.start();
});

it("Custom: No selection of empty-row-message", function (done) {
  const sheet = elementByIdFn("page:mainForm:facet");
  const noEntriesRow = querySelectorFn("#page\\:mainForm\\:facet .tobago-no-entries");

  let rowSelectionChange = 0;
  let clickNoEntriesRow = 0;

  const test = new JasmineTestTool(done);
  test.do(() => sheet().addEventListener("tobago.sheet.rowSelectionChange", () => rowSelectionChange++));
  test.do(() => noEntriesRow().addEventListener("click", () => clickNoEntriesRow++));
  test.event("click", noEntriesRow, () => clickNoEntriesRow >= 1);
  test.do(() => expect(clickNoEntriesRow).toBe(1));
  test.do(() => expect(rowSelectionChange).toBe(0));
  test.start();
});
