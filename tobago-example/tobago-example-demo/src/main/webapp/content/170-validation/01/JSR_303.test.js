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

import {elementByIdFn, querySelectorAllFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Required: Submit without content.", function (done) {
  const messagesFn = querySelectorAllFn("#page\\:messages .alert");
  const inFn = elementByIdFn("page:mainForm:required:in1::field");
  const submitFn = elementByIdFn("page:mainForm:required:submit1");

  const test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 0,
      () => inFn().value = "Alice",
      "click", submitFn);
  test.do(() => inFn().value = "");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.do(() => expect(inFn().value).toBe(""));
  test.start();
});

it("Required: Submit with content.", function (done) {
  const messagesFn = querySelectorAllFn("#page\\:messages .alert");
  const inFn = elementByIdFn("page:mainForm:required:in1::field");
  const submitFn = elementByIdFn("page:mainForm:required:submit1");

  const test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 1,
      () => inFn().value = "",
      "click", submitFn);
  test.do(() => inFn().value = "some content");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.do(() => expect(inFn().value).toBe("some content"));
  test.start();
});

it("Length: Submit single character.", function (done) {
  const messagesFn = querySelectorAllFn("#page\\:messages .alert");
  const inFn = elementByIdFn("page:mainForm:length:in2::field");
  const submitFn = elementByIdFn("page:mainForm:length:submit2");

  const test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 0,
      () => inFn().value = "Bob",
      "click", submitFn);
  test.do(() => inFn().value = "a");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.start();
});

it("Length: Submit three characters.", function (done) {
  const messagesFn = querySelectorAllFn("#page\\:messages .alert");
  const inFn = elementByIdFn("page:mainForm:length:in2::field");
  const submitFn = elementByIdFn("page:mainForm:length:submit2");

  const test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 1,
      () => inFn().value = "provoke exception",
      "click", submitFn);
  test.do(() => inFn().value = "abc");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Length: Submit five characters.", function (done) {
  const messagesFn = querySelectorAllFn("#page\\:messages .alert");
  const inFn = elementByIdFn("page:mainForm:length:in2::field");
  const submitFn = elementByIdFn("page:mainForm:length:submit2");

  const test = new JasmineTestTool(done);
  test.setup(() => messagesFn() && messagesFn().length === 0,
      () => inFn().value = "Dave",
      "click", submitFn);
  test.do(() => inFn().value = "abcde");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.start();
});
