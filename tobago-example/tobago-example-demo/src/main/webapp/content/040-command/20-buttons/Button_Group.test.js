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

import {elementByIdFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Dropdown button has 'btn-group'", function (done) {
  const dropdownFn = elementByIdFn("page:mainForm:splitPrimary");

  const test = new JasmineTestTool(done);
  test.do(() => expect(dropdownFn().classList.contains("btn-group")).toBe(true, "id=buttonWithLinks must have 'btn-group'"));
  test.start();
});

it("Split button has rounded corners", function (done) {
  const buttonFn = elementByIdFn("page:mainForm:splitPrimary::command");
  const buttonStyle = getComputedStyle(buttonFn());

  const test = new JasmineTestTool(done);
  test.do(() => expect(buttonStyle.borderTopLeftRadius).toBe("0px"));
  test.do(() => expect(buttonStyle.borderBottomLeftRadius).toBe("0px"));
  test.do(() => expect(buttonStyle.borderTopRightRadius).toBe("6px"));
  test.do(() => expect(buttonStyle.borderBottomRightRadius).toBe("6px"));
  test.start();
});
