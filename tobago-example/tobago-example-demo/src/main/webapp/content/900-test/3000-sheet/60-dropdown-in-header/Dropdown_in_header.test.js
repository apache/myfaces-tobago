/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("open dropdown menu", function (done) {
  const dropdownButton = elementByIdFn("page:mainForm:sheet:dropdown::command");
  const dropdownMenu = querySelectorFn(".tobago-dropdown-menu[name='page:mainForm:sheet:dropdown']");
  const outName = querySelectorFn("#page\\:mainForm\\:sheet\\:0\\:outName .form-control-plaintext");
  let timestamp;
  let firstRowName;

  const test = new JasmineTestTool(done);
  test.do(() => firstRowName = outName().textContent);
  test.event("click", dropdownButton, () => dropdownMenu().classList.contains("show"));
  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 500);
  test.do(() => expect(dropdownMenu().classList).toContain("show"));
  test.do(() => expect(outName().textContent).toEqual(firstRowName));
  test.start();
});
