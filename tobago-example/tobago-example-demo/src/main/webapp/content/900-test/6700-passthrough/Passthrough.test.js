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
import {elementByIdFn, querySelectorAllFn} from "/script/tobago-test.js";

it("check DOM", function (done) {
  const hInputText = elementByIdFn("page:mainForm:hInputText");
  const tcInField = elementByIdFn("page:mainForm:tcIn::field");
  const inputNumberElements = querySelectorAllFn("input[type='number']");

  const test = new JasmineTestTool(done);
  test.do(() => expect(hInputText().getAttribute("autofocus")).toBe("true"));
  test.do(() => expect(tcInField().getAttribute("type")).toBe("number"));
  test.do(() => expect(inputNumberElements().length).toBe(1));
  test.start();
});
