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

import {elementByIdFn, querySelectorAllFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Update toast", function (done) {
  const toasts = querySelectorAllFn(".tobago-page-toastStore .toast");
  const createButton = elementByIdFn("page:mainForm:createHeaderlessToast");
  const resetButton = elementByIdFn("page:mainForm:resetHeaderlessToasts");

  const test = new JasmineTestTool(done);
  test.setup(() => toasts().length === 0, null, "click", resetButton);
  test.event("click", createButton, () => toasts().length === 1);
  test.do(() => expect(toasts().length).toBe(1));
  test.do(() => expect(toasts()[0].textContent.indexOf("null")).toBe(-1));
  test.event("click", createButton, () => toasts().length === 2);
  test.do(() => expect(toasts().length).toBe(2));
  test.do(() => expect(toasts()[0].textContent.indexOf("null")).toBe(-1));
  test.do(() => expect(toasts()[1].textContent.indexOf("null")).toBe(-1));
  test.start();
});
