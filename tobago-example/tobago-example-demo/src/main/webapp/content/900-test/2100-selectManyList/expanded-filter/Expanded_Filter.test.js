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

it("filter for 'asdf'; no-entries footer must be visible", function (done) {
  const noEntriesFooter = querySelectorFn("#page\\:mainForm\\:selectManyList .tobago-no-entries");
  const filterInput = elementByIdFn("page:mainForm:selectManyList::filter");

  const test = new JasmineTestTool(done);
  test.do(() => expect(noEntriesFooter().classList).toContain("d-none"));
  test.do(() => filterInput().value = 'asdf');
  test.event("input", filterInput, () => !noEntriesFooter().classList.contains("d-none"));
  test.do(() => expect(noEntriesFooter().classList).not.toContain("d-none"));
  test.start();
});
