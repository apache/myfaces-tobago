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
import {querySelectorFn, querySelectorAllFn, elementByIdFn} from "/script/tobago-test.js";

it("No label set", function (done) {
  const inputFn = querySelectorFn("#page\\:mainForm\\:sectionNoLabel .tobago-section-content > tobago-in input");

  const test = new JasmineTestTool(done);
  test.do(() => expect(inputFn().classList).toContain("testcssclass"));
  test.start();
});

it("labelLayout=none", function (done) {
  const inputFn = querySelectorFn("#page\\:mainForm\\:sectionNone .tobago-section-content > tobago-in input");

  const test = new JasmineTestTool(done);
  test.do(() => expect(inputFn().classList).toContain("testcssclass"));
  test.start();
});

it("labelLayout=skip", function (done) {
  const inputFn = querySelectorFn("#page\\:mainForm\\:sectionSkip .tobago-section-content > tobago-in input");

  const test = new JasmineTestTool(done);
  test.do(() => expect(inputFn().classList).toContain("testcssclass"));
  test.start();
});

it("labelLayout=top", function (done) {
  const inputFn = querySelectorFn("#page\\:mainForm\\:sectionTop .tobago-section-content > tobago-in input");

  const test = new JasmineTestTool(done);
  test.do(() => expect(inputFn().classList).toContain("testcssclass"));
  test.start();
});

it("labelLayout=flowLeft", function (done) {
  const inputFn = querySelectorFn("#page\\:mainForm\\:sectionFlowLeft .tobago-section-content > tobago-in input");

  const test = new JasmineTestTool(done);
  test.do(() => expect(inputFn().classList).toContain("testcssclass"));
  test.start();
});

it("labelLayout=flowRight", function (done) {
  const inputFn = querySelectorFn("#page\\:mainForm\\:sectionFlowRight .tobago-section-content > tobago-in input");

  const test = new JasmineTestTool(done);
  test.do(() => expect(inputFn().classList).toContain("testcssclass"));
  test.start();
});

it("labelLayout=flexLeft", function (done) {
  const inputFn = querySelectorFn("#page\\:mainForm\\:sectionFlexLeft .tobago-section-content > tobago-in input");

  const test = new JasmineTestTool(done);
  test.do(() => expect(inputFn().classList).toContain("testcssclass"));
  test.start();
});

it("labelLayout=flexRight", function (done) {
  const inputFn = querySelectorFn("#page\\:mainForm\\:sectionFlexRight .tobago-section-content > tobago-in input");

  const test = new JasmineTestTool(done);
  test.do(() => expect(inputFn().classList).toContain("testcssclass"));
  test.start();
});

it("labelLayout=segmentLeft", function (done) {
  const inputFn = elementByIdFn("page:mainForm:inSegmentLeft::field");

  const test = new JasmineTestTool(done);
  test.do(() => expect(inputFn().classList).toContain("testcssclass"));
  test.start();
});

it("labelLayout=segmentRight", function (done) {
  const inputFn = elementByIdFn("page:mainForm:inSegmentRight::field");

  const test = new JasmineTestTool(done);
  test.do(() => expect(inputFn().classList).toContain("testcssclass"));
  test.start();
});
