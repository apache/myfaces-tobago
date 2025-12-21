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

import {querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("ajax execute", function (done) {
  let timestampFn = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  let textFn = querySelectorFn("#page\\:mainForm\\:outText .form-control-plaintext");
  let tipFn = querySelectorFn("#page\\:mainForm\\:outTip .form-control-plaintext");
  let buttonFn = querySelectorFn("#page\\:mainForm\\:ajaxButton");

  let timestampValue = timestampFn().textContent;
  let textValue = textFn().textContent;
  let tipValue = tipFn().getAttribute('title');

  let test = new JasmineTestTool(done);
  test.event("click", buttonFn, () => timestampFn() && timestampFn().textContent !== timestampValue);
  test.do(() => expect(timestampFn().textContent).not.toBe(timestampValue));
  test.do(() => expect(textFn().textContent).toBe(textValue));
  test.do(() => expect(tipFn().getAttribute('title')).toBe(tipValue));
  test.start();
});
