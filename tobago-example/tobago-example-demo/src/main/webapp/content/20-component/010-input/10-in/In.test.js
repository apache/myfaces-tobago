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

it("inputfield with label", function (done) {
  let labelFn = querySelectorFn("#page\\:mainForm\\:iNormal > label");
  let inputFieldFn = querySelectorFn("#page\\:mainForm\\:iNormal\\:\\:field");

  const test = new JasmineTestTool(done);
  test.do(() => expect(labelFn().textContent).toBe("Input"));
  test.do(() => expect(inputFieldFn().value).toBe("Some Text"));
  test.do(() => inputFieldFn().value = "abc");
  test.do(() => expect(inputFieldFn().value).toBe("abc"));
  test.start();
});

it("ajax change event", function (done) {
  let inputFieldFn = querySelectorFn("#page\\:mainForm\\:inputAjax\\:\\:field");
  let outputFieldFn = querySelectorFn("#page\\:mainForm\\:outputAjax .form-control-plaintext");

  const test = new JasmineTestTool(done);
  test.do(() => inputFieldFn().value = "some input text");
  test.event("change", inputFieldFn,
      () => outputFieldFn() && outputFieldFn().textContent === "some input text");
  test.do(() => expect(outputFieldFn().textContent).toBe("some input text"));
  test.start();
});
