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

it("Only pixel values set", function (done) {
  const columnHeads = querySelectorAllFn("#page\\:mainForm\\:pixelOnly header table thead tr th");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getComputedStyle(columnHeads()[0]).width).toBe("10px"));
  test.do(() => expect(getComputedStyle(columnHeads()[1]).width).toBe("20px"));
  test.do(() => expect(getComputedStyle(columnHeads()[2]).width).toBe("30px"));
  test.start();
});

it("Only percent values set for a 1000px-sheet", function (done) {
  const columnHeads = querySelectorAllFn("#page\\:mainForm\\:percentOnly header table thead tr th");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getComputedStyle(columnHeads()[0]).width).toBe("100px"));
  test.do(() => expect(getComputedStyle(columnHeads()[1]).width).toBe("200px"));
  test.do(() => expect(getComputedStyle(columnHeads()[2]).width).toBe("300px"));
  test.start();
});

it("Only auto value set for a 900px-sheet", function (done) {
  const columnHeads = querySelectorAllFn("#page\\:mainForm\\:autoOnly header table thead tr th");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getComputedStyle(columnHeads()[0]).width).toBe("300px"));
  test.do(() => expect(getComputedStyle(columnHeads()[1]).width).toBe("300px"));
  test.do(() => expect(getComputedStyle(columnHeads()[2]).width).toBe("300px"));
  test.start();
});

it("There must be no horizontal scrollbar", function (done) {
  const sheetBody = querySelectorFn("#page\\:mainForm\\:testSheetColumnWidth .tobago-body");

  const test = new JasmineTestTool(done);
  test.do(() => expect(sheetBody().clientWidth).toBeGreaterThanOrEqual(sheetBody().scrollWidth));
  test.start();
});
