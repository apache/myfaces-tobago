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

it("focus row index 25 and scroll up", function (done) {
  const submit = elementByIdFn("page:mainForm:submit");

  const test = new JasmineTestTool(done);
  test.do(() => focusRowIndex(100));
  test.wait(() => isLoaded(100));
  test.setup(() => isLoaded(100) && !isLoaded(0) && !isLoaded(15), () => focusRowIndex(100), "click", submit);
  test.do(() => focusRowIndex(15)); //visible rows: 15 to 28 (new area)
  test.wait(() => isLoaded(15));
  test.do(() => expect(isLoaded(4)).toBeFalse());
  test.do(() => expect(isLoaded(5, 38)).toBeTrue());
  test.do(() => expect(isLoaded(39)).toBeFalse());

  test.do(() => focusRowIndex(8)); //visible rows: 8 to 17 (known area)
  test.wait(() => isLoaded(4));
  test.do(() => expect(isLoaded(0, 38)).toBeTrue());
  test.do(() => expect(isLoaded(39)).toBeFalse());
  test.start();
});

it("focus row index 9970 (max row index is 9999) and scroll down", function (done) {
  const test = new JasmineTestTool(done);
  test.do(() => focusRowIndex(9970)); //visible rows: 9970 to 9983 (new area)
  test.wait(() => isLoaded(9970));
  test.do(() => expect(isLoaded(9959)).toBeFalse());
  test.do(() => expect(isLoaded(9960, 9993)).toBeTrue());
  test.do(() => expect(isLoaded(9994)).toBeFalse());

  test.do(() => focusRowIndex(9980)); //visible rows: 9980 to 9989 (known area)
  test.wait(() => isLoaded(9999));
  test.do(() => expect(isLoaded(9959)).toBeFalse());
  test.do(() => expect(isLoaded(9960, 9999)).toBeTrue());
  test.start();
});

it("focus row index 500 and go up", function (done) {
  const test = new JasmineTestTool(done);
  test.do(() => focusRowIndex(500)); //visible rows: 500 to 513 (new area)
  test.wait(() => isLoaded(500));
  test.do(() => expect(isLoaded(489)).toBeFalse());
  test.do(() => expect(isLoaded(490, 523)).toBeTrue());
  test.do(() => expect(isLoaded(524)).toBeFalse());

  test.do(() => focusRowIndex(494)); //visible rows: 494 to 503 (known area)
  test.wait(() => isLoaded(489));
  test.do(() => expect(isLoaded(469)).toBeFalse());
  test.do(() => expect(isLoaded(470, 523)).toBeTrue());
  test.do(() => expect(isLoaded(524)).toBeFalse());
  test.start();
});

it("focus row index 1000 and go down", function (done) {
  const test = new JasmineTestTool(done);
  test.do(() => focusRowIndex(1000)); //visible rows: 1000 to 1013 (new area)
  test.wait(() => isLoaded(1000));
  test.do(() => expect(isLoaded(989)).toBeFalse());
  test.do(() => expect(isLoaded(990, 1023)).toBeTrue());
  test.do(() => expect(isLoaded(1024)).toBeFalse());

  test.do(() => focusRowIndex(1010)); //visible rows: 1010 to 1019 (known area)
  test.wait(() => isLoaded(1024));
  test.do(() => expect(isLoaded(989)).toBeFalse());
  test.do(() => expect(isLoaded(990, 1043)).toBeTrue());
  test.do(() => expect(isLoaded(1044)).toBeFalse());
  test.start();
});

it("focus row index 1500 and sort name asc, focus row index 2000 and sort desc", function (done) {
  const nameColumn = elementByIdFn("page:mainForm:sheet:nameColumn_sorter");
  const nameOutputs = querySelectorAllFn("tr tobago-out[id$=':name']");

  const test = new JasmineTestTool(done);
  test.do(() => focusRowIndex(1500));
  test.wait(() => isLoaded(1500));
  test.do(() => expect(isLoaded(1489)).toBeFalse());
  test.do(() => expect(isLoaded(1490, 1523)).toBeTrue());
  test.do(() => expect(isLoaded(1524)).toBeFalse());

  test.event("click", nameColumn, () => nameColumn().classList.contains("tobago-ascending"));
  test.wait(() => isLoaded(1500));
  test.do(() => expect(isLoaded(1489)).toBeFalse());
  test.do(() => expect(isLoaded(1490, 1519)).toBeTrue()); //preloaded from server
  test.do(() => expect(isLoaded(1520)).toBeFalse());
  test.do(() => expect(nameOutputs().length).toEqual(30));
  test.do(() => expect(nameOutputs()[0].id).not.toEqual("page:mainForm:sheet:0:name"));

  test.do(() => focusRowIndex(2000)); //visible rows: 2000 to 2013 (new area)
  test.wait(() => isLoaded(2000));
  test.do(() => expect(isLoaded(1989)).toBeFalse());
  test.do(() => expect(isLoaded(1990, 2023)).toBeTrue());
  test.do(() => expect(isLoaded(2024)).toBeFalse());

  test.event("click", nameColumn, () => nameColumn().classList.contains("tobago-descending"));
  test.wait(() => isLoaded(2000));
  test.do(() => expect(isLoaded(1989)).toBeFalse());
  test.do(() => expect(isLoaded(1990, 2019)).toBeTrue()); //preloaded from server
  test.do(() => expect(isLoaded(2020)).toBeFalse());
  test.do(() => expect(nameOutputs().length).toEqual(30));
  test.do(() => expect(nameOutputs()[0].id).not.toEqual("page:mainForm:sheet:0:name"));
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
