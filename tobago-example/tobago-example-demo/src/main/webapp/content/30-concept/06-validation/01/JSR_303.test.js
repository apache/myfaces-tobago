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

it("must be fixed first", function (done) {
  let test = new JasmineTestTool(done);
  test.do(() => fail("must be fixed first"));
  test.start();
});

/*it("Required: Submit without content.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = querySelectorFn("#page\\:mainForm\\:required\\:in1\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:required\\:submit1");

  let test = new JasmineTestTool(done);
  test.do(() => inFn().value = "");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.start();
});

it("Required: Submit with content.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = querySelectorFn("#page\\:mainForm\\:required\\:in1\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:required\\:submit1");

  let test = new JasmineTestTool(done);
  test.do(() => inFn().value = "some content");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Length: Submit single character.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = querySelectorFn("#page\\:mainForm\\:length\\:in2\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:length\\:submit2");

  let test = new JasmineTestTool(done);
  test.do(() => inFn().value = "a");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.start();
});

it("Length: Submit three characters.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = querySelectorFn("#page\\:mainForm\\:length\\:in2\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:length\\:submit2");

  let test = new JasmineTestTool(done);
  test.do(() => inFn().value = "abc");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 0);
  test.do(() => expect(messagesFn().length).toBe(0));
  test.start();
});

it("Length: Submit five characters.", function (done) {
  let messagesFn = querySelectorAllFn("#page\\:messages.tobago-messages div");
  let inFn = querySelectorFn("#page\\:mainForm\\:length\\:in2\\:\\:field");
  let submitFn = querySelectorFn("#page\\:mainForm\\:length\\:submit2");

  let test = new JasmineTestTool(done);
  test.do(() => inFn().value = "abcde");
  test.event("click", submitFn, () => messagesFn() && messagesFn().length === 1);
  test.do(() => expect(messagesFn().length).toBe(1));
  test.start();
});*/
