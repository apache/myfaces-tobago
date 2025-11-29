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

import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("before input group", function (done) {
  const inputGroupTextFn = querySelectorFn("#page\\:mainForm\\:beforeInputGroup .input-group-text");
  const inputFn = elementByIdFn("page:mainForm:beforeInputGroup::field");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getTopLeftRadius(inputGroupTextFn())).toBeGreaterThan(0));
  test.do(() => expect(getBottomLeftRadius(inputGroupTextFn())).toBeGreaterThan(0));
  test.do(() => expect(getTopRightRadius(inputGroupTextFn())).toBe(0));
  test.do(() => expect(getBottomRightRadius(inputGroupTextFn())).toBe(0));

  test.do(() => expect(getTopRightRadius(inputFn())).toBeGreaterThan(0));
  test.do(() => expect(getBottomRightRadius(inputFn())).toBeGreaterThan(0));
  test.do(() => expect(getTopLeftRadius(inputFn())).toBe(0));
  test.do(() => expect(getBottomLeftRadius(inputFn())).toBe(0));
  test.start();
});

it("after input group", function (done) {
  const inputFn = elementByIdFn("page:mainForm:afterInputGroup::field");
  const inputGroupTextFn = querySelectorFn("#page\\:mainForm\\:afterInputGroup .input-group-text");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getTopLeftRadius(inputFn())).toBeGreaterThan(0));
  test.do(() => expect(getBottomLeftRadius(inputFn())).toBeGreaterThan(0));
  test.do(() => expect(getTopRightRadius(inputFn())).toBe(0));
  test.do(() => expect(getBottomRightRadius(inputFn())).toBe(0));

  test.do(() => expect(getTopRightRadius(inputGroupTextFn())).toBeGreaterThan(0));
  test.do(() => expect(getBottomRightRadius(inputGroupTextFn())).toBeGreaterThan(0));
  test.do(() => expect(getTopLeftRadius(inputGroupTextFn())).toBe(0));
  test.do(() => expect(getBottomLeftRadius(inputGroupTextFn())).toBe(0));
  test.start();
});

it("before and after input group", function (done) {
  const beforeTextFn = querySelectorFn("#page\\:mainForm\\:bothInputGroup .input-group-text:first-child");
  const inputFn = elementByIdFn("page:mainForm:bothInputGroup::field");
  const afterTextFn = querySelectorFn("#page\\:mainForm\\:bothInputGroup .input-group-text:last-child");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getTopLeftRadius(beforeTextFn())).toBeGreaterThan(0));
  test.do(() => expect(getBottomLeftRadius(beforeTextFn())).toBeGreaterThan(0));
  test.do(() => expect(getTopRightRadius(beforeTextFn())).toBe(0));
  test.do(() => expect(getBottomRightRadius(beforeTextFn())).toBe(0));

  test.do(() => expect(getTopLeftRadius(inputFn())).toBe(0));
  test.do(() => expect(getTopRightRadius(inputFn())).toBe(0));
  test.do(() => expect(getBottomLeftRadius(inputFn())).toBe(0));
  test.do(() => expect(getBottomRightRadius(inputFn())).toBe(0));

  test.do(() => expect(getTopRightRadius(afterTextFn())).toBeGreaterThan(0));
  test.do(() => expect(getBottomRightRadius(afterTextFn())).toBeGreaterThan(0));
  test.do(() => expect(getTopLeftRadius(afterTextFn())).toBe(0));
  test.do(() => expect(getBottomLeftRadius(afterTextFn())).toBe(0));
  test.start();
});

function getTopLeftRadius($element) {
  return parseInt(getComputedStyle($element).borderTopLeftRadius);
}

function getTopRightRadius($element) {
  return parseInt(getComputedStyle($element).borderTopRightRadius);
}

function getBottomLeftRadius($element) {
  return parseInt(getComputedStyle($element).borderBottomLeftRadius);
}

function getBottomRightRadius($element) {
  return parseInt(getComputedStyle($element).borderBottomRightRadius);
}
