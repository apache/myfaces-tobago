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

it("initial load", function (done) {
  const submit = elementByIdFn("page:mainForm:submit");

  const test = new JasmineTestTool(done);
  test.setup(() => isLoaded(0) && isLoaded(9), () => focusRowIndex(0), "click", submit);
  test.do(() => expect(isLoaded(0)).toBeTrue());
  test.do(() => expect(isLoaded(9)).toBeTrue());
  test.do(() => expect(isLoaded(10)).toBeFalse());
  test.start();
});

it("focus row index 200 and scroll up", function (done) {
  const test = new JasmineTestTool(done);
  test.do(() => focusRowIndex(200)); //new area; 200 to 213 visible; range 195 to 218
  test.wait(() => isLoaded(200));
  test.do(() => expect(isLoaded(194)).toBeFalse());
  test.do(() => expect(isLoaded(195, 218)).toBeTrue());
  test.do(() => expect(isLoaded(219)).toBeFalse());

  test.do(() => focusRowIndex(197)); //discovered area; 197 to 200 visible; range 194 to 203
  test.wait(() => isLoaded(194));
  test.do(() => expect(isLoaded(184)).toBeFalse());
  test.do(() => expect(isLoaded(185, 218)).toBeTrue());
  test.do(() => expect(isLoaded(219)).toBeFalse());
  test.start();
});

it("focus row index 300 and scroll down", function (done) {
  const test = new JasmineTestTool(done);
  test.do(() => focusRowIndex(300)); //new area; 300 to 313 visible; range 295 to 318
  test.wait(() => isLoaded(300));
  test.do(() => expect(isLoaded(294)).toBeFalse());
  test.do(() => expect(isLoaded(295, 318)).toBeTrue());
  test.do(() => expect(isLoaded(319)).toBeFalse());

  test.do(() => focusRowIndex(313)); //discovered area; 313 to 316 visible; range 310 to 319
  test.wait(() => isLoaded(319));
  test.do(() => expect(isLoaded(294)).toBeFalse());
  test.do(() => expect(isLoaded(295, 328)).toBeTrue());
  test.do(() => expect(isLoaded(329)).toBeFalse());
  test.start();
});

function isLoaded(startRow, endRow) {
  if (endRow === undefined) {
    endRow = startRow;
  }
  for (let i = startRow; i <= endRow; i++) {
    if (row(i).getAttribute("dummy") != null || rowColumnPanel(i) === null) {
      return false;
    }
  }
  return true;
}

function row(rowIndex) {
  return querySelectorFn("tr[row-index='" + rowIndex + "']")();
}

function rowColumnPanel(rowIndex) {
  return querySelectorFn("tr[name='" + rowIndex + "'].tobago-column-panel")();
}

function focusRowIndex(rowIndex) {
  querySelectorFn("#page\\:mainForm\\:sheet .tobago-body")().scrollTop = row(rowIndex).offsetTop;
}
