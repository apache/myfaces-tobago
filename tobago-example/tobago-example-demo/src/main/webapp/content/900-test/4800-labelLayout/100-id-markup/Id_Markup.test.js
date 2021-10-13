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
  const test = new JasmineTestTool(done);
  let sectionFn = elementByIdFn("page:mainForm:sectionNoLabel");
  let rootDiv = sectionFn().querySelector(".tobago-section-content > tobago-in");
  let input = rootDiv.querySelector("input");

  test.do(() => expect(input.classList.contains("testcssclass")));
  test.start();
});

it("labelLayout=none", function (done) {
  const test = new JasmineTestTool(done);
  let sectionFn = elementByIdFn("page:mainForm:sectionNone");
  let rootDiv = sectionFn().querySelector(".tobago-section-content > tobago-in");
  let input = rootDiv.querySelector("input");

  test.do(() => expect(input.classList.contains("testcssclass")));
  test.start();
});

it("labelLayout=skip", function (done) {
  const test = new JasmineTestTool(done);
  let sectionFn = elementByIdFn("page:mainForm:sectionSkip");
  let rootDiv = sectionFn().querySelector(".tobago-section-content > tobago-in");
  let input = rootDiv.querySelector("input");

  test.do(() => expect(input.classList.contains("testcssclass")));
  test.start();
});

it("labelLayout=top", function (done) {
  const test = new JasmineTestTool(done);
  let sectionFn = elementByIdFn("page:mainForm:sectionTop");
  let rootDiv = sectionFn().querySelector(".tobago-section-content > tobago-in");
  let input = rootDiv.querySelector("input");

  test.do(() => expect(input.classList.contains("testcssclass")));
  test.start();
});

it("labelLayout=flowLeft", function (done) {
  const test = new JasmineTestTool(done);
  let sectionFn =elementByIdFn("page:mainForm:sectionFlowLeft");
  let rootDiv = sectionFn().querySelector(".tobago-section-content > tobago-in");
  let input = rootDiv.querySelector("input");

  test.do(() => expect(input.classList.contains("testcssclass")));
  test.start();
});

it("labelLayout=flowRight", function (done) {
  const test = new JasmineTestTool(done);
  let sectionFn = elementByIdFn("page:mainForm:sectionFlowRight");
  let rootDiv = sectionFn().querySelector(".tobago-section-content > tobago-in");
  let input = rootDiv.querySelector("input");

  test.do(() => expect(input.classList.contains("testcssclass")));
  test.start();
});

it("labelLayout=flexLeft", function (done) {
  const test = new JasmineTestTool(done);
  let sectionFn = elementByIdFn("page:mainForm:sectionFlexLeft");
  let rootDiv = sectionFn().querySelector(".tobago-section-content > tobago-in");
  let input = rootDiv.querySelector("input");

  test.do(() => expect(input.classList.contains("testcssclass")));
  test.start();
});

it("labelLayout=flexRight", function (done) {
  const test = new JasmineTestTool(done);
  let sectionFn = elementByIdFn("page:mainForm:sectionFlexRight");
  let rootDiv = sectionFn().querySelector(".tobago-section-content > tobago-in");
  let input = rootDiv.querySelector("input");

  test.do(() => expect(input.classList.contains("testcssclass")));
  test.start();
});

it("labelLayout=segmentLeft", function (done) {
  const test = new JasmineTestTool(done);
  const inputFn = elementByIdFn("page:mainForm:inSegmentLeft::field");

  test.do(() => expect(inputFn().classList).toContain("testcssclass"));
  test.start();
});

it("labelLayout=segmentRight", function (done) {
  const test = new JasmineTestTool(done);
  const inputFn = elementByIdFn("page:mainForm:inSegmentRight::field");

  test.do(() => expect(inputFn().classList).toContain("testcssclass"));
  test.start();
});
