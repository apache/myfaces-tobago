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

import {isFirefox, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("focus row index 200 and scroll up", function (done) {
  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 200);
  test.do(() => focusRowIndex(200));
  test.wait(() => isLoaded(200));

  if (isFirefox()) {
    test.wait(() => isLoaded(199));
    test.do(() => expect(isLoaded(149)).toBeFalse());
    test.do(() => expect(isLoaded(150, 199)).toBeTrue());
  } else {
    test.do(() => expect(isLoaded(199)).toBeFalse());
  }
  test.do(() => expect(isLoaded(200, 249)).toBeTrue());
  test.do(() => expect(isLoaded(250)).toBeFalse());

  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 200);
  test.do(() => focusRowIndex(199));
  test.wait(() => isLoaded(199));
  test.do(() => expect(isLoaded(149)).toBeFalse());
  test.do(() => expect(isLoaded(150, 249)).toBeTrue());
  test.do(() => expect(isLoaded(250)).toBeFalse());
  test.start();
});

it("focus row index 400 and scroll down", function (done) {
  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 200);
  test.do(() => focusRowIndex(400));
  test.wait(() => isLoaded(400));

  if (isFirefox()) {
    test.wait(() => isLoaded(399));
    test.do(() => expect(isLoaded(349)).toBeFalse());
    test.do(() => expect(isLoaded(350, 399)).toBeTrue());
  } else {
    test.do(() => expect(isLoaded(399)).toBeFalse());
  }
  test.do(() => expect(isLoaded(400, 449)).toBeTrue());
  test.do(() => expect(isLoaded(450)).toBeFalse());

  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 200);
  test.do(() => focusRowIndex(401));
  test.wait(() => isLoaded(450));
  if (isFirefox()) {
    test.do(() => expect(isLoaded(349)).toBeFalse());
    test.do(() => expect(isLoaded(350, 399)).toBeTrue());
  } else {
    test.do(() => expect(isLoaded(399)).toBeFalse());
  }
  test.do(() => expect(isLoaded(400, 499)).toBeTrue());
  test.do(() => expect(isLoaded(500)).toBeFalse());
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
