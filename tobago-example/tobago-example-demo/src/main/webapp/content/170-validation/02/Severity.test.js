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

it("Check severity CSS classes", function (done) {
  const alertsFn = querySelectorAllFn("#page\\:messages .alert");
  const submitFn = elementByIdFn("page:mainForm:submit");
  const fatalInputFieldFn = elementByIdFn("page:mainForm:fatal::field");
  const errorInputFieldFn = elementByIdFn("page:mainForm:error::field");
  const warnInputFieldFn = elementByIdFn("page:mainForm:warn::field");
  const infoInputFieldFn = elementByIdFn("page:mainForm:info::field");
  const fatalButtonFn = querySelectorFn("#page\\:mainForm\\:fatal tobago-popover .btn");
  const errorButtonFn = querySelectorFn("#page\\:mainForm\\:error tobago-popover .btn");
  const warnButtonFn = querySelectorFn("#page\\:mainForm\\:warn tobago-popover .btn");
  const infoButtonFn = querySelectorFn("#page\\:mainForm\\:info tobago-popover .btn");

  let test = new JasmineTestTool(done);
  test.setup(() => alertsFn().length > 0, null, "click", submitFn);
  test.do(() => expect(fatalInputFieldFn().classList).toContain("border-danger"));
  test.do(() => expect(errorInputFieldFn().classList).toContain("border-danger"));
  test.do(() => expect(warnInputFieldFn().classList).toContain("border-warning"));
  test.do(() => expect(infoInputFieldFn().classList).toContain("border-info"));
  test.do(() => expect(fatalButtonFn().classList).toContain("btn-danger"));
  test.do(() => expect(errorButtonFn().classList).toContain("btn-danger"));
  test.do(() => expect(warnButtonFn().classList).toContain("btn-warning"));
  test.do(() => expect(infoButtonFn().classList).toContain("btn-info"));
  test.start();
});
