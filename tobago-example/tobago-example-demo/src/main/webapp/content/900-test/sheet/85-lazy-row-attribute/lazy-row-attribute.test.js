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

it("focus row index 200 and scroll up", function (done) {
  const test = new JasmineTestTool(done);
  test.do(() => focusRowIndex(200)); //new area; visible: 200 to 213; range: 175 to 238
  test.wait(() => isLoaded(200));
  test.do(() => expect(isLoaded(174)).toBeFalse());
  test.do(() => expect(isLoaded(175, 238)).toBeTrue());
  test.do(() => expect(isLoaded(239)).toBeFalse());

  test.do(() => focusRowIndex(189)); //discovered area; visible 189 to 202; range: 174 to 217
  test.wait(() => isLoaded(174));
  test.do(() => expect(isLoaded(124)).toBeFalse());
  test.do(() => expect(isLoaded(125, 238)).toBeTrue());
  test.do(() => expect(isLoaded(239)).toBeFalse());
  test.start();
});

it("focus row index 400 and scroll down", function (done) {
  const test = new JasmineTestTool(done);
  test.do(() => focusRowIndex(400)); //new area; visible: 400 to 413; range: 375 to 438
  test.wait(() => isLoaded(400));
  test.do(() => expect(isLoaded(374)).toBeFalse());
  test.do(() => expect(isLoaded(375, 438)).toBeTrue());
  test.do(() => expect(isLoaded(439)).toBeFalse());

  test.do(() => focusRowIndex(411)); //discovered area; visible 411 to 424; range: 396 to 439
  test.wait(() => isLoaded(439));
  test.do(() => expect(isLoaded(374)).toBeFalse());
  test.do(() => expect(isLoaded(375, 488)).toBeTrue());
  test.do(() => expect(isLoaded(489)).toBeFalse());
  test.start();
});

function isLoaded(startRow, endRow) {
  if (endRow === undefined) {
    endRow = startRow;
  }
  for (let i = startRow; i <= endRow; i++) {
    if (row(i).getAttribute("dummy") != null) {
      return false;
    }
  }
  return true;
}

function row(rowIndex) {
  return querySelectorFn("tr[row-index='" + rowIndex + "']")();
}

function focusRowIndex(rowIndex) {
  querySelectorFn("#page\\:mainForm\\:sheet .tobago-body")().scrollTop = row(rowIndex).offsetTop;
}
