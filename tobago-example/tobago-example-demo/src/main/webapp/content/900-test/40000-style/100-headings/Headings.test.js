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

it("Test h1", function (done) {
  let alinkFn = querySelectorFn("#page\\:mainForm\\:link1");
  let buttonlinkFn = querySelectorFn("#page\\:mainForm\\:actionLink1");
  testFont(done, alinkFn, buttonlinkFn);
});

it("Test h2", function (done) {
  let alinkFn = querySelectorFn("#page\\:mainForm\\:link2");
  let buttonlinkFn = querySelectorFn("#page\\:mainForm\\:actionLink2");
  testFont(done, alinkFn, buttonlinkFn);
});

it("Test h3", function (done) {
  let alinkFn = querySelectorFn("#page\\:mainForm\\:link3");
  let buttonlinkFn = querySelectorFn("#page\\:mainForm\\:actionLink3");
  testFont(done, alinkFn, buttonlinkFn);
});

it("Test h4", function (done) {
  let alinkFn = querySelectorFn("#page\\:mainForm\\:link4");
  let buttonlinkFn = querySelectorFn("#page\\:mainForm\\:actionLink4");
  testFont(done, alinkFn, buttonlinkFn);
});

it("Test h5", function (done) {
  let alinkFn = querySelectorFn("#page\\:mainForm\\:link5");
  let buttonlinkFn = querySelectorFn("#page\\:mainForm\\:actionLink5");
  testFont(done, alinkFn, buttonlinkFn);
});

it("Test h6", function (done) {
  let alinkFn = querySelectorFn("#page\\:mainForm\\:link6");
  let buttonlinkFn = querySelectorFn("#page\\:mainForm\\:actionLink6");
  testFont(done, alinkFn, buttonlinkFn);
});

it("Test no heading", function (done) {
  let alinkFn = querySelectorFn("#page\\:mainForm\\:link0");
  let buttonlinkFn = querySelectorFn("#page\\:mainForm\\:actionLink0");
  testFont(done, alinkFn, buttonlinkFn);
});

function testFont(done, alinkFn, buttonlinkFn) {
  let test = new JasmineTestTool(done);
  const alinkComputedStyle = getComputedStyle(alinkFn());
  const buttonlinkFnComputedStyle = getComputedStyle(buttonlinkFn());
  test.do(() => expect(alinkComputedStyle.color).toBe(buttonlinkFnComputedStyle.color));
  test.do(() => expect(alinkComputedStyle.fontFamily).toBe(buttonlinkFnComputedStyle.fontFamily));
  test.do(() => expect(alinkComputedStyle.fontSize).toBe(buttonlinkFnComputedStyle.fontSize));
  test.do(() => expect(alinkComputedStyle.fontWeight).toBe(buttonlinkFnComputedStyle.fontWeight));
  test.do(() => expect(alinkComputedStyle.textDecoration).toBe(buttonlinkFnComputedStyle.textDecoration));
  test.start();
}

it("Ajax reload for section 2", function (done) {
  let reloadButtonFn = querySelectorFn("#page\\:mainForm\\:reloadSection2");
  let section2HeaderFn = querySelectorFn("#page\\:mainForm\\:levelTwoSection h3");
  let timestampFn = querySelectorFn("#page\\:mainForm\\:timestamp .form-control-plaintext");
  let firstTimestamp = timestampFn().textContent;

  let test = new JasmineTestTool(done);
  test.do(() => expect(section2HeaderFn() !== null).toBe(true));
  test.event("click", reloadButtonFn, () => firstTimestamp < timestampFn().textContent);
  test.do(() => expect(section2HeaderFn() !== null).toBe(true));
  test.do(() => expect(firstTimestamp < timestampFn().textContent).toBe(true, "value of new timestamp must be higher"));
  test.start();
});
