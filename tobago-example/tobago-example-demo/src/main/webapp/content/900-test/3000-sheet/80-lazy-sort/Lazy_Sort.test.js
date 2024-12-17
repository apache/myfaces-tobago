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

import {elementByIdFn, querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("focus row index 5 (min row index is 0) and scroll up", function (done) {
  const submit = elementByIdFn("page:mainForm:submit");
  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => focusRowIndex(100));
  test.wait(() => isLoaded(100));
  test.setup(() => isLoaded(100) && !isLoaded(5), () => focusRowIndex(100), "click", submit);
  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 1000);
  test.do(() => focusRowIndex(5));
  test.wait(() => isLoaded(5));
  test.do(() => expect(isLoaded(4)).toBeFalse());
  test.do(() => expect(isLoaded(5, 24)).toBeTrue());
  test.do(() => expect(isLoaded(25)).toBeFalse());

  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 100);
  test.do(() => focusRowIndex(4));
  test.wait(() => isLoaded(4));
  test.do(() => expect(isLoaded(0, 24)).toBeTrue());
  test.do(() => expect(isLoaded(25)).toBeFalse());
  test.start();
});

it("focus row index 9970 (max row index is 9999) and scroll down", function (done) {
  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 100);
  test.do(() => focusRowIndex(9970));
  test.wait(() => isLoaded(9970));
  test.do(() => expect(isLoaded(9969)).toBeFalse());
  test.do(() => expect(isLoaded(9970, 9989)).toBeTrue());
  test.do(() => expect(isLoaded(9990)).toBeFalse());

  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 100);
  test.do(() => focusRowIndex(9971));
  test.wait(() => isLoaded(9990));
  test.do(() => expect(isLoaded(9969)).toBeFalse());
  test.do(() => expect(isLoaded(9970, 9999)).toBeTrue());
  test.start();
});

it("focus row index 500 and go up", function (done) {
  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 100);
  test.do(() => focusRowIndex(500));
  test.wait(() => isLoaded(500));
  test.do(() => expect(isLoaded(499)).toBeFalse());
  test.do(() => expect(isLoaded(500, 519)).toBeTrue());
  test.do(() => expect(isLoaded(520)).toBeFalse());

  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 100);
  test.do(() => focusRowIndex(499));
  test.wait(() => isLoaded(499));
  test.do(() => expect(isLoaded(479)).toBeFalse());
  test.do(() => expect(isLoaded(480, 519)).toBeTrue());
  test.do(() => expect(isLoaded(520)).toBeFalse());
  test.start();
});

it("focus row index 1000 and go down", function (done) {
  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => focusRowIndex(1000));
  test.wait(() => isLoaded(1000));
  test.do(() => expect(isLoaded(999)).toBeFalse());
  test.do(() => expect(isLoaded(1000, 1019)).toBeTrue());
  test.do(() => expect(isLoaded(1020)).toBeFalse());

  test.do(() => focusRowIndex(1001));
  test.wait(() => isLoaded(1020));
  test.do(() => expect(isLoaded(999)).toBeFalse());
  test.do(() => expect(isLoaded(1000, 1039)).toBeTrue());
  test.do(() => expect(isLoaded(1040)).toBeFalse());
  test.start();
});

it("focus row index 1500 and sort name asc, focus row index 2000 and sort desc", function (done) {
  const nameColumn = elementByIdFn("page:mainForm:sheet:nameColumn_sorter");
  const nameOutputs = querySelectorAllFn("tr tobago-out[id$=':name']");

  let timestamp;

  const test = new JasmineTestTool(done);
  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 100);
  test.do(() => focusRowIndex(1500));
  test.wait(() => isLoaded(1500));
  test.do(() => expect(isLoaded(1499)).toBeFalse());
  test.do(() => expect(isLoaded(1500, 1519)).toBeTrue());
  test.do(() => expect(isLoaded(1520)).toBeFalse());

  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 100);
  test.event("click", nameColumn, () => nameOutputs().length === 40);
  test.do(() => expect(nameOutputs().length).toEqual(40));
  test.do(() => expect(nameOutputs()[0].id).not.toEqual("page:mainForm:sheet:0:name"));

  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 100);
  test.do(() => focusRowIndex(2000));
  test.wait(() => isLoaded(2000));
  test.do(() => expect(isLoaded(1999)).toBeFalse());
  test.do(() => expect(isLoaded(2000, 2019)).toBeTrue());
  test.do(() => expect(isLoaded(2020)).toBeFalse());

  test.do(() => timestamp = Date.now());
  test.wait(() => Date.now() - timestamp > 100);
  test.event("click", nameColumn, () => nameOutputs().length === 40);
  test.do(() => expect(nameOutputs().length).toEqual(40));
  test.do(() => expect(isLoaded(1979)).toBeFalse());
  test.do(() => expect(isLoaded(1980, 2019)).toBeTrue());
  test.do(() => expect(isLoaded(2020)).toBeFalse());
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
