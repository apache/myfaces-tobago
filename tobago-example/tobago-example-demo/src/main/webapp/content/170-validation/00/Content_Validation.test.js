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

import {querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Required: Submit without content.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let textareaFn = querySelectorFn("#page\\:mainForm\\:required\\:textarea\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:required\\:submit_r");

  let test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 0,
      () => textareaFn().value = "Alice",
      "click", submitFn);
  test.do(() => textareaFn().value = "");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.do(() => expect(textareaFn().value).toBe(""));
  test.start();
});

it("Required: Submit with content.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let textareaFn = querySelectorFn("#page\\:mainForm\\:required\\:textarea\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:required\\:submit_r");

  let test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 1,
      () => textareaFn().value = "",
      "click", submitFn);
  test.do(() => textareaFn().value = "some content");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 0
      && textareaFn().value && textareaFn().value === "some content");
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => expect(textareaFn().value).toBe("some content"));
  test.start();
});

it("Validate Length: Submit single character.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let inFn = querySelectorFn("#page\\:mainForm\\:validateLength\\:in_vl\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:validateLength\\:submit_vl");

  let test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 0,
      () => inFn().value = "Bob",
      "click", submitFn);
  test.do(() => inFn().value = "a");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.start();
});

it("Validate Length: Submit two character.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let inFn = querySelectorFn("#page\\:mainForm\\:validateLength\\:in_vl\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:validateLength\\:submit_vl");

  let test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 1,
      () => inFn().value = "x",
      "click", submitFn);
  test.do(() => inFn().value = "ab");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Validate Range: Submit no number.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let inFn = querySelectorFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 0,
      () => inFn().value = "42",
      "click", submitFn);
  test.do(() => inFn().value = "no number");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.start();
});

it("Validate Range: Submit number '2' which is out of range.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let inFn = querySelectorFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 0,
      () => inFn().value = "42",
      "click", submitFn);
  test.do(() => inFn().value = "2");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.start();
});

it("Validate Range: Submit number '78' which is out of range.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let inFn = querySelectorFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 0,
      () => inFn().value = "42",
      "click", submitFn);
  test.do(() => inFn().value = "78");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.start();
});

it("Validate Range: Submit number '64' which is within the range.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let inFn = querySelectorFn("#page\\:mainForm\\:validateRange\\:in_vr\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:validateRange\\:submit_vr");

  let test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 1,
      () => inFn().value = "1000",
      "click", submitFn);
  test.do(() => inFn().value = "64");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Regex Validation: Submit 'T' which violates the pattern.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let inFn = querySelectorFn("#page\\:mainForm\\:regexValidation\\:in_rv\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:regexValidation\\:submit_rv");

  let test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 0,
      () => inFn().value = "T3",
      "click", submitFn);
  test.do(() => inFn().value = "T");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.start();
});

it("Regex Validation: Submit '3' which violates the pattern.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let inFn = querySelectorFn("#page\\:mainForm\\:regexValidation\\:in_rv\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:regexValidation\\:submit_rv");

  let test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 0,
      () => inFn().value = "T3",
      "click", submitFn);
  test.do(() => inFn().value = "3");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.start();
});

it("Regex Validation: Submit 'T3' which is accepted.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let inFn = querySelectorFn("#page\\:mainForm\\:regexValidation\\:in_rv\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:regexValidation\\:submit_rv");

  let test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 1,
      () => inFn().value = "Charlie",
      "click", submitFn);
  test.do(() => inFn().value = "T3");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Custom Validator: Submit rejected string.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let inFn = querySelectorFn("#page\\:mainForm\\:customValidator\\:in_cv\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:customValidator\\:submit_cv");

  let test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 0,
      () => inFn().value = "tobago",
      "click", submitFn);
  test.do(() => inFn().value = "java");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.start();
});

it("Custom Validator: Submit accepted string.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages .alert");
  let inFn = querySelectorFn("#page\\:mainForm\\:customValidator\\:in_cv\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:customValidator\\:submit_cv");

  let test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 1,
      () => inFn().value = "Dave",
      "click", submitFn);
  test.do(() => inFn().value = "tobago");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});
