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

it("menu store must be available", function (done) {
  const timestampFn = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  const submitFn = elementByIdFn("page:mainForm:submitButton");
  const forceNpeFn = elementByIdFn("page:mainForm:forceNpeButton");
  const menuStoreFn = querySelectorFn(".tobago-page-menuStore");

  let timestampValue = timestampFn().textContent;

  let test = new JasmineTestTool(done);
  test.setup(() => menuStoreFn() !== null, null, "click", submitFn);
  test.do(() => expect(menuStoreFn()).not.toBeNull());
  test.event("click", forceNpeFn, () => timestampFn().textContent !== timestampValue);
  test.do(() => expect(menuStoreFn()).not.toBeNull());

  // click submit to unbreak the page -> footer must be rendered
  test.do(() => timestampValue = timestampFn().textContent);
  test.event("click", submitFn, () => timestampFn().textContent !== timestampValue);
  test.start();
});
