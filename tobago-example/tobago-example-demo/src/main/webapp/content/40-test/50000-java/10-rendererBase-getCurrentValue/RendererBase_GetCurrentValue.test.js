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

function test(done, idSuffix, expectedText) {
  let test = new JasmineTestTool(done);
  let outFn = querySelectorFn("#page\\:mainForm\\:" + idSuffix);
  test.do(() => expect(outFn().textContent.trim()).toBe(expectedText));
  test.start();
}

it("formatted values: out string", function (done) {
  test(done, "outString", "simple string");
});

it("formatted values: out string from method", function (done) {
  test(done, "outStringFromMethod", "HELLO WORLD!");
});

it("formatted values: out date", function (done) {
  test(done, "outDate", "24.07.1969");
});

it("formatted values: out date from method", function (done) {
  test(done, "outDateFromMethod", "24.07.2019");
});

it("formatted values: out currency", function (done) {
  test(done, "outCurrency", "TTD");
});

it("formatted values: out currency from method", function (done) {
  test(done, "outCurrencyFromMethod", "ISK");
});
