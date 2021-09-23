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
import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";

it("Style tag inside grid layout", function (done) {
  let submitFn = elementByIdFn("page:mainForm:submitButton");
  let outputFn = querySelectorFn("#page\\:mainForm\\:output .form-control-plaintext");
  let timestampFn = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  let timestamp;

  let test = new JasmineTestTool(done);
  test.do(() => expect(outputFn().classList).toContain("text-warning"));
  test.do(() => timestamp = timestampFn().textContent)
  test.event("click", submitFn, () => timestamp < timestampFn().textContent);
  test.do(() => expect(timestamp < timestampFn().textContent)
      .toBe(true, "value of new timestamp must be higher"));
  test.do(() => expect(outputFn().classList).toContain("text-warning"));

  test.start();
});
