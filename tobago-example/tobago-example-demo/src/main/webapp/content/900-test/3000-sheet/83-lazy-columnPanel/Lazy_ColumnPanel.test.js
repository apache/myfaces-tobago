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

import {elementByIdFn, isFirefox, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("initial load", function (done) {
  const submit = elementByIdFn("page:mainForm:submit");

  const test = new JasmineTestTool(done);
  test.setup(() => isLoaded(0) && isLoaded(9), null, "click", submit);
  test.do(() => expect(isLoaded(0)).toBeTrue());
  test.do(() => expect(isLoaded(9)).toBeTrue());
  test.do(() => expect(isLoaded(10)).toBeFalse());
  test.start();
});

it("focus row index 200 and scroll up", function (done) {
  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 200);
  test.do(() => focusRowIndex(200));
  test.wait(() => isLoaded(200));

  if (isFirefox()) {
    test.wait(() => isLoaded(199));
    test.do(() => expect(isLoaded(189)).toBeFalse());
    test.do(() => expect(isLoaded(190, 199)).toBeTrue());
  } else {
    test.do(() => expect(isLoaded(199)).toBeFalse());
  }
  test.do(() => expect(isLoaded(200, 209)).toBeTrue());
  test.do(() => expect(isLoaded(210)).toBeFalse());

  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 200);
  test.do(() => focusRowIndex(199));
  test.wait(() => isLoaded(199));
  test.do(() => expect(isLoaded(189)).toBeFalse());
  test.do(() => expect(isLoaded(190, 209)).toBeTrue());
  test.do(() => expect(isLoaded(210)).toBeFalse());
  test.start();
});

it("focus row index 300 and scroll down", function (done) {
  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 200);
  test.do(() => focusRowIndex(300));
  test.wait(() => isLoaded(300));

  if (isFirefox()) {
    test.wait(() => isLoaded(299));
    test.do(() => expect(isLoaded(289)).toBeFalse());
    test.do(() => expect(isLoaded(290, 299)).toBeTrue());
  } else {
    test.do(() => expect(isLoaded(299)).toBeFalse());
  }
  test.do(() => expect(isLoaded(300, 309)).toBeTrue());
  test.do(() => expect(isLoaded(310)).toBeFalse());

  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 200);
  test.do(() => focusRowIndex(301));
  test.wait(() => isLoaded(310));
  if (isFirefox()) {
    test.do(() => expect(isLoaded(289)).toBeFalse());
    test.do(() => expect(isLoaded(290, 299)).toBeTrue());
  } else {
    test.do(() => expect(isLoaded(299)).toBeFalse());
  }
  test.do(() => expect(isLoaded(300, 319)).toBeTrue());
  test.do(() => expect(isLoaded(320)).toBeFalse());
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
