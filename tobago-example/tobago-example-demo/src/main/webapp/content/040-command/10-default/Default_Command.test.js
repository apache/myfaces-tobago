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

it("Basics", function (done) {
  const inputFn = elementByIdFn("page:mainForm:basics:binput::field");
  const outputFn = querySelectorFn("#page\\:mainForm\\:basics\\:boutput .form-control-plaintext");
  const submitFn = elementByIdFn("page:mainForm:basics:bsubmit");

  const test = new JasmineTestTool(done);
  test.setup(() => outputFn().textContent === "", () => inputFn().value = "", "click", submitFn);
  test.do(() => inputFn().value = "Tobago");
  test.event("click", submitFn, () => outputFn().textContent.trim() === "Tobago");
  test.do(() => expect(outputFn().textContent.trim()).toBe("Tobago"));
  test.start();
});

it("Multiple default buttons", function (done) {
  const inputAFn = elementByIdFn("page:mainForm:outerform:forma:inputA::field");
  const outputAFn = querySelectorFn("#page\\:mainForm\\:outerform\\:forma\\:outputA .form-control-plaintext");
  const submitAFn = elementByIdFn("page:mainForm:outerform:forma:submitA");
  const inputBFn = elementByIdFn("page:mainForm:outerform:formb:inputB::field");
  const outputBFn = querySelectorFn("#page\\:mainForm\\:outerform\\:formb\\:outputB .form-control-plaintext");
  const submitBFn = elementByIdFn("page:mainForm:outerform:submitB");

  const test = new JasmineTestTool(done);
  test.setup(() => outputAFn().textContent === "" && outputBFn().textContent === "",
      () => {
        inputAFn().value = "";
        inputBFn().value = "";
      }, "click", submitBFn);

  test.do(() => inputAFn().value = "a");
  test.do(() => inputBFn().value = "b");
  test.event("click", submitAFn, () => outputAFn().textContent.trim() === "a");
  test.do(() => expect(outputAFn().textContent.trim()).toBe("a"));
  test.do(() => expect(outputBFn().textContent.trim()).toBe(""));

  test.do(() => inputAFn().value = "c");
  test.do(() => inputBFn().value = "d");
  test.event("click", submitBFn, () => outputAFn().textContent.trim() === "c" && outputBFn().textContent.trim() === "d");
  test.do(() => expect(outputAFn().textContent.trim()).toBe("c"));
  test.do(() => expect(outputBFn().textContent.trim()).toBe("d"));

  test.start();
});
